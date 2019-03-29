package fr.ign.cogit.simplu3d.util.distribution;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.calculation.OrientedBoundingBox;
import fr.ign.cogit.geoxygene.sig3d.equation.LineEquation;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiPoint;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.geoxygene.util.index.Tiling;

public class ZonePackager {

	// Attribute to store the id_block, it is only used to check if the
	// algorithm
	// works right
	public static String ATTRIBUTE_NAME_GROUP = "id_block";

	// IDPAR is used during simulation to identify the parcels
	// It will serve during the simulation to make the link between
	// simulation results and parcels
	public static String ATTRIBUTE_NAME_ID = "IDPAR";

	// ATTRIBUTE THAT STORE THE NUMBER OF BAND (0 here)
	// IT is used during the simulation
	public static String ATTRIBUTE_NAME_BAND = "B1_T_BANDE";

	// ATTRIBUTE USED TO DETERMINE IF A PARCEL HAS TO BE SIMULATED
	public static String ATTRIBUTE_SIMUL = "SIMUL";
	public static String ATTRIBUTE_SIMUL_TYPE = "String"; // or Integer ?

	// OUTPUT SRID
	public static String SRID_END = "EPSG:2154";

	// Define the radius in which parcel are kept for the context
	public static double CONTEXT_AREA = 0.5;

	// ATTRIBUTES TO REGENERATE IDPAR
	public static String ATTRIBUTE_DEPARTEMENT = "CODE_DEP";
	public static String ATTRIBUTE_COMMUNE = "CODE_COM";
	public static String ATTRIBUTE_PREFIXE = "CODE_ARR";
	public static String ATTRIBUTE_SECTION = "SECTION";
	public static String ATTRIBUTE_NUMERO = "NUMERO";

	public static void createParcelGroupsAndExport(IFeatureCollection<IFeature> parcelles, int numberMaxOfSimulatedParcel, double areaMax,
			String tempFolder, String folderOutPath, boolean debug) throws Exception {
		createParcelGroupsAndExport(parcelles, numberMaxOfSimulatedParcel, areaMax, tempFolder, folderOutPath, "", debug);
	}

	/**
	 * Create parcel groups and export with a temporary export to avoid out of memory error
	 * 
	 * @param parcelles
	 *            the set of parcels to decompose
	 * @param numberMaxOfSimulatedParcel
	 *            the number of max parcels to simulated
	 * @param areaMax
	 *            the maximal area to consider a parcel as simulable
	 * @param tempFolder
	 *            temporary folder that can be cleaned after the simulation
	 * @param folderOutPath
	 *            the final result
	 * @param packagingName : packaging option. If emprty, everything pack are generated in the folderOutPath
	 *            if length ==5, it'll be considered as a zipFile and a folder will be created for each zip
	 *            otherwise, make superpackages of x element in each, x would be the value of the packagingName 
	 * @param debug
	 *            if we want to export the current bounding box
	 * @throws Exception
	 *             exception
	 */
	public static void createParcelGroupsAndExport(IFeatureCollection<IFeature> parcelles, int numberMaxOfSimulatedParcel, double areaMax,
			String tempFolder, String folderOutPath, String packagingName, boolean debug) throws Exception {

		boolean zip = true;
		if (packagingName.length() != 5) {
			zip = false;
		}
		else {
			try {
				Integer.valueOf(packagingName);
			}
			catch (NumberFormatException n) {
				System.out.println("invalid packaging name on createParcelGroupsAndExport");
				packagingName = "";
			}
		}

		// Initialization of spatial index with updates
		parcelles.initSpatialIndex(Tiling.class, true);

		// Initialization of ID attribut to -1
		parcelles.stream().forEach(x -> setIDBlock(x, -1));
		// Adding missing attributes ID and NAME_BAND set by ATTRIBUTE_NAME_ID
		// and
		// ATTRIBUTE_NAME_BAND attribute name
		parcelles.stream().forEach(x -> generateMissingAttributes(x));

		// Current group ID
		File folderTemp = new File(tempFolder);
		folderTemp.mkdirs();

		int idCurrentGroup = 0;

		while (!parcelles.isEmpty()) {
			// We get the first parcel and removes it from the list
			IFeature currentParcel = parcelles.get(0);
			parcelles.remove(0);

			// Step 1 : determining the parcel in the same block
			// Collection that will contain a list of parcels in the same block
			IFeatureCollection<IFeature> grapFeatures = new FT_FeatureCollection<>();
			// Initializing the number of parcels
			grapFeatures.add(currentParcel);

			List<IFeature> candidateParcelles = Arrays.asList(currentParcel);

			System.out.println("Generating new block - still " + parcelles.size() + "  elements left");

			// Initialisation of the recursion method that affects ID neighbour
			// by neighbour
			selectByNeighbourdHood(candidateParcelles, parcelles, grapFeatures);

			System.out.println("The block has : " + grapFeatures.size() + "  elements");

			// We store the result in a current folder
			createFolderAndExport(folderTemp.getAbsolutePath() + "/" + idCurrentGroup + "/", grapFeatures, debug);
			idCurrentGroup++;
		}

		// Current group ID
		File folderOut = new File(folderOutPath);
		folderOut.mkdirs();

		System.out.println("Splitting group");
		// Step 2 : cutting the block into bag of limited number of parcel
		// In order to have more balanced bags and increase the distribution
		// performances
		idCurrentGroup = 0;

		int count = 0;
		int folder = 0;
		for (File f : folderTemp.listFiles()) {
			if (f.isDirectory()) {

				IFeatureCollection<IFeature> grapFeatures = ShapefileReader.read(new String(f + "/parcelle.shp"));
				if (grapFeatures == null || grapFeatures.isEmpty()) {
					continue;
				}
				List<IFeatureCollection<IFeature>> listOfCutUrbanBlocks = determineCutBlocks(grapFeatures, grapFeatures, numberMaxOfSimulatedParcel,
						areaMax);

				for (IFeatureCollection<IFeature> featCollCutUrbanBlock : listOfCutUrbanBlocks) {
					System.out.println("---- Group " + idCurrentGroup + " has " + featCollCutUrbanBlock.size() + " elements");
					for (IFeature feat : featCollCutUrbanBlock) {
						setIDBlock(feat, idCurrentGroup);
					}
					// if we want to append a "-" between the defined zipCode and the idCurrentGroup
					if (zip) {
						System.out.println("pack with zip");
						createFolderAndExport(folderOut + "/" + packagingName + "/" + idCurrentGroup + "/", featCollCutUrbanBlock, debug);
					} else if (!packagingName.equals("")) {
						// case where we want to create a package for every number of a (the number is contained into the zipCode string
						System.out.println("pack of number "+folder);
						createFolderAndExport(folderOut + "/" + folder + "/" + idCurrentGroup + "/", featCollCutUrbanBlock, debug);
					} else {
						// no ordering on packages on other files
						System.out.println("pack in no order");
						createFolderAndExport(folderOut + "/" + idCurrentGroup + "/", featCollCutUrbanBlock, debug);
					}
					idCurrentGroup++;
				}
			}
			count++;
			if (!zip && count == Integer.parseInt(packagingName)) {
				count =- Integer.parseInt(packagingName);
				folder++;
			}
		}
	}

	/**
	 * 
	 * @param parcelles
	 *            the set of parcels to decompose
	 * @param numberMaxOfSimulatedParcel
	 *            the number of max parcels to simulated
	 * @param areaMax
	 *            the maximal area to consider a parcel as simulable
	 * @return a map of group of parcels by urban blocks
	 * @throws Exception
	 *             exception
	 */
	public static Map<Integer, IFeatureCollection<IFeature>> createParcelGroups(IFeatureCollection<IFeature> parcelles,
			int numberMaxOfSimulatedParcel, double areaMax) throws Exception {

		// Map Integer / Features of the group
		Map<Integer, IFeatureCollection<IFeature>> mapResult = new HashMap<>();

		// Initialisation of spatial index with updates
		parcelles.initSpatialIndex(Tiling.class, true);

		// Initializatino of ID attribut to -1
		parcelles.stream().forEach(x -> setIDBlock(x, -1));
		// Adding missing attributes ID and NAME_BAND set by ATTRIBUTE_NAME_ID
		// and
		// ATTRIBUTE_NAME_BAND attribute name
		parcelles.stream().forEach(x -> generateMissingAttributes(x));

		// Current group ID
		int idCurrentGroup = 0;

		while (!parcelles.isEmpty()) {
			// We get the first parcel and removes it from the list
			IFeature currentParcel = parcelles.get(0);
			parcelles.remove(0);

			// Step 1 : determining the parcel in the same block
			// Collection that will contain a list of parcels in the same block
			IFeatureCollection<IFeature> grapFeatures = new FT_FeatureCollection<>();
			// Initializing the number of parcels
			grapFeatures.add(currentParcel);

			List<IFeature> candidateParcelles = Arrays.asList(currentParcel);

			System.out.println("Generating new block - still " + parcelles.size() + "  elements left");

			// Initialisation of the recursion method that affects ID neighbour
			// by neighbour
			selectByNeighbourdHood(candidateParcelles, parcelles, grapFeatures);

			System.out.println("The block has : " + grapFeatures.size() + "  elements");

			// Step 2 : cutting the block into bag of limited number of parcel
			// In order to have more balanced bags and increase the distribution
			// performances
			System.out.println("Splitting group");
			List<IFeatureCollection<IFeature>> listOfCutUrbanBlocks = determineCutBlocks(grapFeatures, grapFeatures, numberMaxOfSimulatedParcel,
					areaMax);
			for (IFeatureCollection<IFeature> featCollCutUrbanBlock : listOfCutUrbanBlocks) {
				System.out.println("---- Group " + idCurrentGroup + " has " + featCollCutUrbanBlock.size() + " elements");
				for (IFeature feat : featCollCutUrbanBlock) {
					setIDBlock(feat, idCurrentGroup);
				}
				mapResult.put(idCurrentGroup, featCollCutUrbanBlock);
				idCurrentGroup++;
			}

		}

		return mapResult;
	}

	/**
	 * This method is used to cut the collection of blocks into sub block and check the number of simulable parcels.
	 * It is a recursive method.
	 * @param featColl
	 *            the considered set of parcels for a block
	 * @param featCollTotal
	 *            the collection that contains all the parcels
	 * @param numberMaxOfSimulatedParcel
	 *            the number of max parcels to simulated
	 * @param areaMax
	 *            the maximal area
	 * @return
	 * @throws Exception
	 */
	public static List<IFeatureCollection<IFeature>> determineCutBlocks(IFeatureCollection<IFeature> featColl,
			IFeatureCollection<IFeature> featCollTotal, int numberMaxOfSimulatedParcel, double areaMax) throws Exception {
		// Is the block empty enough ?
		if (featColl.size() <= numberMaxOfSimulatedParcel) {
		  return new ArrayList<IFeatureCollection<IFeature>>(Arrays.asList(determineSimulationBLock(featColl, featCollTotal)));
		}
		// We keep when the area is enough small and if the simule attribute value is true
		long nbOfSimulatedParcel = featColl.getElements().stream()
		    .filter(feat -> (feat.getGeom().area() < areaMax))
				.filter(feat -> (hasToBeSimulated(feat))).count();
		if (nbOfSimulatedParcel <= numberMaxOfSimulatedParcel) {
      return new ArrayList<IFeatureCollection<IFeature>>(Arrays.asList(determineSimulationBLock(featColl, featCollTotal)));
		}
		return determine(featColl).stream().flatMap(f->{
      try {
        return determineCutBlocks(f, featCollTotal, numberMaxOfSimulatedParcel, areaMax).stream();
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    }).collect(Collectors.toList());
//		List<IFeatureCollection<IFeature>> collections = determine(featColl);
//		// We split into two parts and re-apply the method on them
//    List<IFeatureCollection<IFeature>> results = new ArrayList<>(2);
//		results.addAll(determineCutBlocks(collections.get(0), featCollTotal, numberMaxOfSimulatedParcel, areaMax));
//		results.addAll(determineCutBlocks(collections.get(1), featCollTotal, numberMaxOfSimulatedParcel, areaMax));
//		return results;
	}

	/**
	 * Determine the simulation block and get the context around the simulable parcels (CONTEXT_AREA value determines the radius).
	 * 
	 * @param featColl
	 * @param featCollTotal
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private static IFeatureCollection<IFeature> determineSimulationBLock(IFeatureCollection<IFeature> featColl,
			IFeatureCollection<IFeature> featCollTotal) throws CloneNotSupportedException {
		if (!featCollTotal.hasSpatialIndex()) {
			featCollTotal.initSpatialIndex(Tiling.class, false);
		}
		// InitialGeometry
//		IDirectPositionList dpl = new DirectPositionList();
//		for (IFeature feat : featColl) {
//			dpl.addAll(feat.getGeom().coord());
//		}
//		IGeometry area = new GM_MultiPoint(dpl);
		IGeometry area = featColl.getGeomAggregate().buffer(CONTEXT_AREA);
		Collection<IFeature> featCollSelect = featCollTotal.select(area.buffer(CONTEXT_AREA));
		IFeatureCollection<IFeature> finalFeatColl = new FT_FeatureCollection<>();
		finalFeatColl.addAll(featColl);
		for (IFeature feat : featCollSelect) {
			if (featColl.contains(feat)) {
				continue;
			}
      DefaultFeature featureFakeClone = new DefaultFeature();
      featureFakeClone.setGeom(feat.getGeom());
      // It is a new context feature we add a false attribute
      AttributeManager.addAttribute(featureFakeClone, ZonePackager.ATTRIBUTE_NAME_ID, feat.getAttribute(ZonePackager.ATTRIBUTE_NAME_ID),
          "String");
      if (ATTRIBUTE_SIMUL_TYPE.equals("String") || ATTRIBUTE_SIMUL_TYPE.equals("Boolean")) {
        // The attribute is stored as boolean
        AttributeManager.addAttribute(featureFakeClone, ZonePackager.ATTRIBUTE_SIMUL, "false", "String");
      } else {
        // The attribute is stored as Integer
        AttributeManager.addAttribute(featureFakeClone, ZonePackager.ATTRIBUTE_SIMUL, "0", "Integer");
      }
      AttributeManager.addAttribute(featureFakeClone, ZonePackager.ATTRIBUTE_NAME_BAND, 42, "Integer");
      finalFeatColl.add(featureFakeClone);
		}
		return finalFeatColl;
	}

	/**
	 * Determine the splitting of a set of parcels into 2 subsets of parcels.
	 * 
	 * @param featColl
	 * @return
	 * @throws Exception
	 */
	private static List<IFeatureCollection<IFeature>> determine(IFeatureCollection<IFeature> featColl) throws Exception {
		// We make two collection that contains different features
		IFeatureCollection<IFeature> collection1 = new FT_FeatureCollection<>();
		IFeatureCollection<IFeature> collection2 = new FT_FeatureCollection<>();
		// We arbitrary split the block into two parts
		if (!featColl.hasSpatialIndex()) {
			featColl.initSpatialIndex(Tiling.class, false);
		}
		// InitialGeemetry
		IDirectPositionList dpl = new DirectPositionList();
		for (IFeature feat : featColl) {
			dpl.addAll(feat.getGeom().coord());
		}
		IGeometry area = new GM_MultiPoint(dpl);
		int nbIterationMax = 15;
		// It is possible that all the parcels intersect one of the two splitted
		// OBB in the case we split the one that intersects all the parcels and
		// subdivide it
		for (int i = 0; i < nbIterationMax; i++) {
			// instead while(true) { for more robustness
			// We cut in a first direction
			List<IPolygon> poly = computeSplittingPolygon(area, true, 0, 0, 1, 0);
			Collection<IFeature> selection = featColl.select(poly.get(0));
			// All elements are in a same side, we cut in an other direct
			if (selection.size() == featColl.size() || selection.isEmpty()) {
				poly = computeSplittingPolygon(area, false, 0, 0, 1, 0);
				selection = featColl.select(poly.get(0));
			}
			if (selection.size() == featColl.size()) {
				area = poly.get(0);
				continue;
			}
			if (selection.isEmpty()) {
				area = poly.get(1);
				continue;
			}
			collection1.addAll(selection);
			for (IFeature feat : featColl) {
				if (!selection.contains(feat)) {
					collection2.add(feat);
				}
			}
			return new ArrayList<IFeatureCollection<IFeature>>(Arrays.asList(collection1, collection2));
		}

		// The algo does not seem to work, we only but 1 feature in each collection.
		System.out.println("Going through this way");
		collection1.add(featColl.get(0));
		featColl.remove(0);
		collection1.addAll(featColl);
		return new ArrayList<IFeatureCollection<IFeature>>(Arrays.asList(collection1, collection2));
	}

	/**
	 * Computed the splitting polygons composed by two boxes determined from the oriented bounding boxes splited from a line at its middle
	 * 
	 * @param pol
	 *            : the input polygon
	 * @param shortDirectionSplit
	 *            : it is splitted by the short edges or by the long edge.
	 * @return
	 * @throws Exception
	 */
	public static List<IPolygon> computeSplittingPolygon(IGeometry pol, boolean shortDirectionSplit, double noise, int decompositionLevel,
			int decompositionLevelWithRoad, double roadWidth) throws Exception {

		// Determination of the bounding box
		OrientedBoundingBox oBB = new OrientedBoundingBox(pol);

		// Detmermination of the split vector
		Vecteur splitDirection = (shortDirectionSplit) ? oBB.shortestDirection() : oBB.longestDirection();

		IDirectPosition centroid = oBB.getCentre();

		// The noise value is determined by noise parameters and parcel width
		// (to avoid lines that go out of parcel)
		double noiseTemp = Math.min(oBB.getWidth() / 3, noise);

		// X and Y move of the centroid
		double alphaX = (0.5 - Math.random()) * noiseTemp;
		double alphaY = (0.5 - Math.random()) * noiseTemp;
		IDirectPosition translateCentroid = new DirectPosition(centroid.getX() + alphaX, centroid.getY() + alphaY);

		// Determine the points that intersect the line and the OBB according to
		// chosen direction
		// This points will be used for splitting
		IDirectPositionList intersectedPoints = determineIntersectedPoints(new LineEquation(translateCentroid, splitDirection),
				(shortDirectionSplit) ? oBB.getLongestEdges() : oBB.getShortestEdges());

		// Construction of the two splitting polygons by using the OBB edges and the
		// intersection points
		IPolygon pol1 = determinePolygon(intersectedPoints, (shortDirectionSplit) ? oBB.getShortestEdges().get(0) : oBB.getLongestEdges().get(0),
				decompositionLevel, decompositionLevelWithRoad, roadWidth);
		IPolygon pol2 = determinePolygon(intersectedPoints, (shortDirectionSplit) ? oBB.getShortestEdges().get(1) : oBB.getLongestEdges().get(1),
				decompositionLevel, decompositionLevelWithRoad, roadWidth);

		// Generated polygons are added and returned
		List<IPolygon> outList = new ArrayList<>();
		outList.add(pol1);
		outList.add(pol2);

		return outList;
	}

	/**
	 * Build the output polygon from OBB edges and splitting points
	 * 
	 * @param intersectedPoints
	 * @param edge
	 * @return
	 */
	private static IPolygon determinePolygon(IDirectPositionList intersectedPoints, ILineString edge, int decompositionLevel,
			int decompositionLevelWithRoad, double roadWidth) {

		IDirectPosition dp1 = intersectedPoints.get(0);
		IDirectPosition dp2 = intersectedPoints.get(1);

		Vecteur v = new Vecteur(dp1, dp2);

		Vecteur v1 = new Vecteur(edge.coord().get(0), edge.coord().get(1));

		IDirectPositionList dpl1 = new DirectPositionList();
		if (v.prodScalaire(v1) > 0) {

			dpl1.add(dp2);
			dpl1.add(dp1);
			dpl1.addAll(edge.coord());

			dpl1.add(dp2);

		} else {

			dpl1.add(dp1);
			dpl1.add(dp2);
			dpl1.addAll(edge.coord());

			dpl1.add(dp1);

		}

		IPolygon pol = new GM_Polygon(new GM_LineString(dpl1));

		if (decompositionLevel < decompositionLevelWithRoad) {

			IDirectPositionList dpl = new DirectPositionList(dp1, dp2);

			ILineString directionOfCut = (new GM_LineString(dpl));

			IGeometry geom = pol.difference(directionOfCut.buffer(roadWidth));

			// To check the geometries
			// Decomment the follong lines

			// System.out.println(geom);
			// System.out.println(roadWidth);
			// System.out.println(directionOfCut);

			// We keep it if it is only a polygon
			// If it is not a polygon it means that the OBB is too small to support
			// this
			// operation
			// So we do not create the road
			if (geom instanceof IPolygon) {
				pol = (IPolygon) geom;
			}
		}

		return pol;

	}

	/**
	 * Determine the splitting points from line equation and OBB edges
	 * 
	 * @param eq
	 * @param ls
	 * @return
	 */
	private static IDirectPositionList determineIntersectedPoints(LineEquation eq, List<ILineString> ls) {

		IDirectPosition dp1 = eq.intersectionLineLine(new LineEquation(ls.get(0).coord().get(0), ls.get(0).coord().get(1)));
		IDirectPosition dp2 = eq.intersectionLineLine(new LineEquation(ls.get(1).coord().get(0), ls.get(1).coord().get(1)));

		if (dp1 == null) {
			System.out.println("determineIntersectedPoints: Null");
			dp1 = eq.intersectionLineLine(new LineEquation(ls.get(0).coord().get(0), ls.get(0).coord().get(1)));

		}
		IDirectPositionList dpl = new DirectPositionList();
		dpl.add(dp1);
		dpl.add(dp2);

		return dpl;

	}

	/**
	 * A method that determine the neighbour parcels from candidates (featCandidates) and remove them from the general parcel collections (parcelles) and set the value
	 * attributeCount for the group. The result is stored in grapFeatures that will be reused in the different uses of the recursive method
	 * 
	 * @param featCandidates
	 *            candidates parcels
	 * @param parcelles
	 *            the collection that contains all parcels
	 * @param grapFeatures
	 *            an intermediary results stored for the recursive method
	 */
	public static void selectByNeighbourdHood(List<IFeature> featCandidates, IFeatureCollection<IFeature> parcelles,
			IFeatureCollection<IFeature> grapFeatures) {

		for (IFeature currentParcel : featCandidates) {
			// Update of the current grap
			grapFeatures.addUnique(currentParcel);
			// We select the surrounding parcels
			Collection<IFeature> surroundingParcels = parcelles.select(currentParcel.getGeom().buffer(0.1));

			// We only keep features where ID is not set
			List<IFeature> listNotSetSurroundingParcels = surroundingParcels.stream().filter(x -> -1 == getIDBlock(x)).collect(Collectors.toList());
			// We set the group value to the features
			listNotSetSurroundingParcels.stream().forEach(x -> setIDBlock(x, 1));

			// We remove the list from existing parcels
			parcelles.removeAll(listNotSetSurroundingParcels);

			if (!listNotSetSurroundingParcels.isEmpty()) {
				// We relaunch with the new selected parcels
				selectByNeighbourdHood(listNotSetSurroundingParcels, parcelles, grapFeatures);
			}
		}

	}

	/**
	 * Create a folder for each entry of the map
	 * 
	 * 
	 * 
	 * @param map
	 *            the map to export
	 * @param folderIn
	 *            the folder where the map is exported
	 * @param debug
	 *            do we want to export the corresponding bbox ?
	 */
	public static void exportFolder(Map<Integer, IFeatureCollection<IFeature>> map, String folderIn, boolean debug) {

		(new File(folderIn)).mkdirs();
		// For each key we create a folder with associated features
		map.keySet().parallelStream().forEach(x -> createFolderAndExport(folderIn + x + "/", map.get(x), debug));
	}

	/**
	 * Create a folder for an entry of the map (the name parcelle.shp is used in the simulator).
	 * 
	 * @param path
	 * @param features
	 */
	public static void createFolderAndExport(String path, IFeatureCollection<IFeature> features, boolean debug) {
		// We create the folder and store the collection.
		// This hint is to ensure that the first item has rules.
		// Because the schema of the shapefile export is based on the schema of
		// the first feature.
		int nbElem = features.size();
		for (int i = 0; i < nbElem; i++) {
			IFeature feat = features.get(i);
			if (hasToBeSimulated(feat)) {
				features.remove(i);
				features.getElements().add(0, feat);
				break;
			}
		}
		File f = new File(path);
		f.mkdirs();
		try {
			if (debug) {
				// If we want to export the bounding boxes
				IFeatureCollection<IFeature> pop = new FT_FeatureCollection<>();
				IFeature feat = new DefaultFeature(features.getEnvelope().getGeom());
				pop.add(feat);
				ShapefileWriter.write(pop, path + "bbox.shp", CRS.decode(ZonePackager.SRID_END));
			}
			ShapefileWriter.write(features, path + "parcelle.shp", CRS.decode(ZonePackager.SRID_END));
		} catch (NoSuchAuthorityCodeException e) {
			e.printStackTrace();
		} catch (FactoryException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get IDBlock value for a feature
	 * 
	 * @param x
	 *            the feature
	 * @return the ID value
	 */
	public static int getIDBlock(IFeature x) {
		return Integer.parseInt(x.getAttribute(ZonePackager.ATTRIBUTE_NAME_GROUP).toString());
	}

	/**
	 * Set IDBlock value for a feature
	 * 
	 * @param x
	 *            the feature
	 * @param value
	 *            the ID to set
	 */
	public static void setIDBlock(IFeature x, int value) {
		AttributeManager.addAttribute(x, ZonePackager.ATTRIBUTE_NAME_GROUP, value, "Integer");
	}

	private static boolean hasToBeSimulated(IFeature feat) {

		Object o = feat.getAttribute(ATTRIBUTE_SIMUL);

		String strO = o.toString();

		try {
			int str0ToInt = Integer.parseInt(strO);

			return (str0ToInt == 1);
		} catch (Exception e) {
			try {
				boolean str0ToBoolean = Boolean.parseBoolean(strO);
				return str0ToBoolean;
			} catch (Exception e2) {
				e.printStackTrace();
				return false;
			}

		}
	}

	/**
	 * Adding missing attributes : - the ID is generated from a concatenation of several attributes - the ATTRIBUTE_NAME_BAND that is set to 0 as there is only one band regulation
	 * 
	 * @param x
	 */
	private static void generateMissingAttributes(IFeature x) {
		// We set the NAME_BAND value to 0
		AttributeManager.addAttribute(x, ZonePackager.ATTRIBUTE_NAME_BAND, 0, "Integer");

		Object departement = x.getAttribute(ATTRIBUTE_DEPARTEMENT);
		Object commune = x.getAttribute(ATTRIBUTE_COMMUNE);

		if (commune == null) {
			/// It is maybe only an IDPAR ?
			Object idpar = x.getAttribute(ATTRIBUTE_NAME_ID);
			if (idpar == null) {
				return;
			}
			AttributeManager.addAttribute(x, ZonePackager.ATTRIBUTE_NAME_ID, idpar, "String");
			return;

		}
		String prefix = x.getAttribute(ATTRIBUTE_PREFIXE).toString();
		String section = x.getAttribute(ATTRIBUTE_SECTION).toString();
		String numero = x.getAttribute(ATTRIBUTE_NUMERO).toString();

		// Departement may be null when ATT_COMMUNE already contains the
		// departement number
		String strDepartement = (departement != null) ? departement.toString() : "";

		String idFinal = strDepartement + commune + prefix + section + numero;
		AttributeManager.addAttribute(x, ZonePackager.ATTRIBUTE_NAME_ID, idFinal, "String");

	}

}
