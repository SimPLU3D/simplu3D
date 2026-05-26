package fr.ign.cogit.simplu3d.util.distribution;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import fr.ign.cogit.simplu3d.io.feature.FeatureType;
import fr.ign.cogit.simplu3d.model.AbstractUrbanFeature;
import fr.ign.cogit.simplu3d.model.UrbanFeature;
import fr.ign.common.equation.LineEquation;
import fr.ign.common.io.ShapefileReader;
import fr.ign.common.io.ShapefileWriter;
import fr.ign.common.util.OrientedBoundingBox;
import fr.ign.common.util.Vecteur;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.data.DataUtilities;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.*;

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

	public static void createParcelGroupsAndExport(DefaultFeatureCollection parcelles, int numberMaxOfSimulatedParcel, double areaMax,
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
	public static void createParcelGroupsAndExport(DefaultFeatureCollection parcelles, int numberMaxOfSimulatedParcel, double areaMax,
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
		//parcelles.initSpatialIndex(Tiling.class, true);

		// Initialization of ID attribut to -1
		parcelles.forEach(x -> setIDBlock(x, -1));
		// Adding missing attributes ID and NAME_BAND set by ATTRIBUTE_NAME_ID
		// and
		// ATTRIBUTE_NAME_BAND attribute name
		parcelles.forEach(ZonePackager::generateMissingAttributes);

		// Current group ID
		File folderTemp = new File(tempFolder);
		folderTemp.mkdirs();

		int idCurrentGroup = 0;

		while (!parcelles.isEmpty()) {
			// We get the first parcel and removes it from the list
			SimpleFeature currentParcel = parcelles.iterator().next();
			parcelles.remove(currentParcel);
			// Step 1 : determining the parcel in the same block
			// Collection that will contain a list of parcels in the same block
			DefaultFeatureCollection grapFeatures = new DefaultFeatureCollection();
			// Initializing the number of parcels
			grapFeatures.add(currentParcel);
			List<SimpleFeature> candidateParcelles = Collections.singletonList(currentParcel);
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
				DefaultFeatureCollection grapFeatures = ShapefileReader.read(new File(f, "parcelle.shp").toURI().toURL());
				if (grapFeatures.isEmpty()) {
					continue;
				}
				List<DefaultFeatureCollection> listOfCutUrbanBlocks = determineCutBlocks(grapFeatures, grapFeatures, numberMaxOfSimulatedParcel,
						areaMax);
				for (DefaultFeatureCollection featCollCutUrbanBlock : listOfCutUrbanBlocks) {
					System.out.println("---- Group " + idCurrentGroup + " has " + featCollCutUrbanBlock.size() + " elements");
					for (SimpleFeature feat : featCollCutUrbanBlock) {
						setIDBlock(feat, idCurrentGroup);
					}
					// if we want to append a "-" between the defined zipCode and the idCurrentGroup
					if (zip) {
						System.out.println("pack with zip");
						createFolderAndExport(folderOut + "/" + packagingName + "/" + idCurrentGroup + "/", featCollCutUrbanBlock, debug);
					} else if (!packagingName.isEmpty()) {
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
	public static Map<Integer, DefaultFeatureCollection> createParcelGroups(
			DefaultFeatureCollection parcelles,
			int numberMaxOfSimulatedParcel,
			double areaMax
	) throws Exception {

		// Map Integer / Features of the group
		Map<Integer, DefaultFeatureCollection> mapResult = new HashMap<>();

		// Initialisation of spatial index with updates
		//parcelles.initSpatialIndex(Tiling.class, true);

		// Initializatino of ID attribut to -1
		parcelles.forEach(x -> setIDBlock(x, -1));
		// Adding missing attributes ID and NAME_BAND set by ATTRIBUTE_NAME_ID
		// and
		// ATTRIBUTE_NAME_BAND attribute name
		parcelles.forEach(ZonePackager::generateMissingAttributes);

		// Current group ID
		int idCurrentGroup = 0;

		while (!parcelles.isEmpty()) {
			// We get the first parcel and removes it from the list
			SimpleFeature currentParcel = parcelles.iterator().next();
			parcelles.remove(currentParcel);
			// Step 1 : determining the parcel in the same block
			// Collection that will contain a list of parcels in the same block
			DefaultFeatureCollection grapFeatures = new DefaultFeatureCollection();
			// Initializing the number of parcels
			grapFeatures.add(currentParcel);
			List<SimpleFeature> candidateParcelles = Collections.singletonList(currentParcel);
			System.out.println("Generating new block - still " + parcelles.size() + "  elements left");
			// Initialisation of the recursion method that affects ID neighbour
			// by neighbour
			selectByNeighbourdHood(candidateParcelles, parcelles, grapFeatures);
			System.out.println("The block has : " + grapFeatures.size() + "  elements");
			// Step 2 : cutting the block into bag of limited number of parcel
			// In order to have more balanced bags and increase the distribution
			// performances
			System.out.println("Splitting group");
			List<DefaultFeatureCollection> listOfCutUrbanBlocks = determineCutBlocks(grapFeatures, grapFeatures, numberMaxOfSimulatedParcel,
					areaMax);
			for (DefaultFeatureCollection featCollCutUrbanBlock : listOfCutUrbanBlocks) {
				System.out.println("---- Group " + idCurrentGroup + " has " + featCollCutUrbanBlock.size() + " elements");
				for (SimpleFeature feat : featCollCutUrbanBlock) {
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
	 */
	public static List<DefaultFeatureCollection> determineCutBlocks(
			DefaultFeatureCollection featColl,
			DefaultFeatureCollection featCollTotal,
			int numberMaxOfSimulatedParcel,
			double areaMax
	) throws Exception {
		// Is the block empty enough ?
		if (featColl.size() <= numberMaxOfSimulatedParcel) {
		  return new ArrayList<>(List.of(determineSimulationBLock(featColl, featCollTotal)));
		}
		// We keep when the area is enough small and if the simule attribute value is true
		long nbOfSimulatedParcel = featColl.stream()
				.filter(feat -> (((Geometry)feat.getDefaultGeometry()).getArea() < areaMax))
				.filter(ZonePackager::hasToBeSimulated).count();
		if (nbOfSimulatedParcel <= numberMaxOfSimulatedParcel) {
      return new ArrayList<>(List.of(determineSimulationBLock(featColl, featCollTotal)));
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
	private static DefaultFeatureCollection determineSimulationBLock(
			DefaultFeatureCollection featColl,
			DefaultFeatureCollection featCollTotal
	) throws CloneNotSupportedException, IOException, SchemaException {
/*
		if (!featCollTotal.hasSpatialIndex()) {
			featCollTotal.initSpatialIndex(Tiling.class, false);
		}
*/
		// InitialGeometry
//		CoordinateList dpl = new CoordinateList();
//		for (IFeature feat : featColl) {
//			dpl.addAll(feat.getGeom().coord());
//		}
//		IGeometry area = new GM_MultiPoint(dpl);
		List<Geometry> geometries = featColl.stream().map(s->(Geometry) s.getDefaultGeometry()).toList();

		Geometry area = new GeometryFactory().createGeometryCollection(geometries.toArray(new Geometry[0])).buffer(CONTEXT_AREA);
		// FIXME USE SPATIAL INDEX?
		Collection<SimpleFeature> featCollSelect = featCollTotal.stream().filter(f->((Geometry)f.getDefaultGeometry()).intersects(area)).toList();
		DefaultFeatureCollection finalFeatColl = new DefaultFeatureCollection();
		finalFeatColl.addAll(featColl.collection());
		List<String> attributeSpecs = new ArrayList<>();
		Map<String, Integer> index = new HashMap<>();
		index.put(null, 0);
		attributeSpecs.add("geometry:Polygon");
		index.put(ZonePackager.ATTRIBUTE_NAME_ID, 1);
		attributeSpecs.add(ZonePackager.ATTRIBUTE_NAME_ID+":"+"String");
		if (ATTRIBUTE_SIMUL_TYPE.equals("String") || ATTRIBUTE_SIMUL_TYPE.equals("Boolean")) {
			index.put(ZonePackager.ATTRIBUTE_SIMUL, 2);
			attributeSpecs.add(ZonePackager.ATTRIBUTE_SIMUL+":"+"String");
		} else {
			index.put(ZonePackager.ATTRIBUTE_SIMUL, 2);
			attributeSpecs.add(ZonePackager.ATTRIBUTE_SIMUL+":"+"Integer");
		}
		index.put(ZonePackager.ATTRIBUTE_NAME_BAND, 3);
		attributeSpecs.add(ZonePackager.ATTRIBUTE_NAME_BAND+":"+"Integer");
		SimpleFeatureType featureType = DataUtilities.createType("Parcel", String.join(",", attributeSpecs));
		for (SimpleFeature feat : featCollSelect) {
			if (featColl.contains(feat)) {
				continue;
			}
			List<Object> attributes = new ArrayList<>();
			attributes.add(feat.getDefaultGeometry());
			attributes.add(feat.getAttribute(ZonePackager.ATTRIBUTE_NAME_ID));
			// It is a new context feature we add a false attribute
			//AttributeManager.addAttribute(featureFakeClone, ZonePackager.ATTRIBUTE_NAME_ID, feat.getAttribute(ZonePackager.ATTRIBUTE_NAME_ID), "String");
			if (ATTRIBUTE_SIMUL_TYPE.equals("String") || ATTRIBUTE_SIMUL_TYPE.equals("Boolean")) {
				// The attribute is stored as boolean
				attributes.add("false");
				//AttributeManager.addAttribute(featureFakeClone, ZonePackager.ATTRIBUTE_SIMUL, "false", "String");
			} else {
				// The attribute is stored as Integer
				attributes.add("0");
				//AttributeManager.addAttribute(featureFakeClone, ZonePackager.ATTRIBUTE_SIMUL, "0", "Integer");
			}
			attributes.add(42);
			//AttributeManager.addAttribute(featureFakeClone, ZonePackager.ATTRIBUTE_NAME_BAND, 42, "Integer");
			SimpleFeature featureFakeClone = new SimpleFeatureImpl(attributes.toArray(), featureType, null, false, index);
			finalFeatColl.add(featureFakeClone);
		}
		return finalFeatColl;
	}

	/**
	 * Determine the splitting of a set of parcels into 2 subsets of parcels.
	 */
	private static List<DefaultFeatureCollection> determine(DefaultFeatureCollection featColl) throws Exception {
		// We make two collection that contains different features
		DefaultFeatureCollection collection1 = new DefaultFeatureCollection();
		DefaultFeatureCollection collection2 = new DefaultFeatureCollection();
		// We arbitrary split the block into two parts
/*
		if (!featColl.hasSpatialIndex()) {
			featColl.initSpatialIndex(Tiling.class, false);
		}
*/
		// InitialGeemetry
		CoordinateList dpl = new CoordinateList();
		for (SimpleFeature feat : featColl) {
			Geometry geometry = (Geometry) feat.getDefaultGeometry();
			dpl.addAll(List.of(geometry.getCoordinates()));
		}
		Geometry area = new GeometryFactory().createMultiPointFromCoords(dpl.toCoordinateArray());
		int nbIterationMax = 15;
		// It is possible that all the parcels intersect one of the two splitted
		// OBB in the case we split the one that intersects all the parcels and
		// subdivide it
		for (int i = 0; i < nbIterationMax; i++) {
			// instead while(true) { for more robustness
			// We cut in a first direction
			List<Polygon> poly = computeSplittingPolygon(area, true, 0, 0, 1, 0);
			//Collection<SimpleFeature> selection = featColl.select(poly.get(0));
			Polygon polygon = poly.getFirst();
			//FIXME Use spatial index?
			Collection<SimpleFeature> selection = featColl.stream().filter(f -> ((Geometry) f.getDefaultGeometry()).intersects(polygon)).toList();
			// All elements are in a same side, we cut in an other direct
			if (selection.size() == featColl.size() || selection.isEmpty()) {
				poly = computeSplittingPolygon(area, false, 0, 0, 1, 0);
				Polygon finalPolygon = poly.getFirst();;
				selection = featColl.stream().filter(f -> ((Geometry) f.getDefaultGeometry()).intersects(finalPolygon)).toList();
			}
			if (selection.size() == featColl.size()) {
				area = poly.getFirst();
				continue;
			}
			if (selection.isEmpty()) {
				area = poly.get(1);
				continue;
			}
			collection1.addAll(selection);
			for (SimpleFeature feat : featColl) {
				if (!selection.contains(feat)) {
					collection2.add(feat);
				}
			}
			return new ArrayList<>(Arrays.asList(collection1, collection2));
		}
		// The algo does not seem to work, we only but 1 feature in each collection.
		System.out.println("Going through this way");
		SimpleFeature feature = featColl.iterator().next();
		collection1.add(feature);
		featColl.remove(feature);
		collection1.addAll(featColl.collection());
		return new ArrayList<>(Arrays.asList(collection1, collection2));
	}

	/**
	 * Computed the splitting polygons composed by two boxes determined from the oriented bounding boxes splited from a line at its middle
	 * 
	 * @param pol
	 *            : the input polygon
	 * @param shortDirectionSplit
	 *            : it is splitted by the short edges or by the long edge.
	 */
	public static List<Polygon> computeSplittingPolygon(Geometry pol, boolean shortDirectionSplit, double noise, int decompositionLevel,
			int decompositionLevelWithRoad, double roadWidth) throws Exception {

		// Determination of the bounding box
		OrientedBoundingBox oBB = new OrientedBoundingBox(pol);

		// Detmermination of the split vector
		Vecteur splitDirection = (shortDirectionSplit) ? oBB.shortestDirection() : oBB.longestDirection();

		Coordinate centroid = oBB.getCentre();

		// The noise value is determined by noise parameters and parcel width
		// (to avoid lines that go out of parcel)
		double noiseTemp = Math.min(oBB.getWidth() / 3, noise);

		// X and Y move of the centroid
		double alphaX = (0.5 - Math.random()) * noiseTemp;
		double alphaY = (0.5 - Math.random()) * noiseTemp;
		Coordinate translateCentroid = new Coordinate(centroid.getX() + alphaX, centroid.getY() + alphaY);
		// Determine the points that intersect the line and the OBB according to
		// chosen direction
		// This points will be used for splitting
		CoordinateList intersectedPoints = determineIntersectedPoints(new LineEquation(translateCentroid, splitDirection),
				(shortDirectionSplit) ? oBB.getLongestEdges() : oBB.getShortestEdges());
		// Construction of the two splitting polygons by using the OBB edges and the
		// intersection points
		Polygon pol1 = determinePolygon(intersectedPoints, (shortDirectionSplit) ? oBB.getShortestEdges().get(0) : oBB.getLongestEdges().get(0),
				decompositionLevel, decompositionLevelWithRoad, roadWidth);
		Polygon pol2 = determinePolygon(intersectedPoints, (shortDirectionSplit) ? oBB.getShortestEdges().get(1) : oBB.getLongestEdges().get(1),
				decompositionLevel, decompositionLevelWithRoad, roadWidth);

		// Generated polygons are added and returned
		List<Polygon> outList = new ArrayList<>();
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
	private static Polygon determinePolygon(
			CoordinateList intersectedPoints, 
			LineString edge, 
			int decompositionLevel,
			int decompositionLevelWithRoad, 
			double roadWidth
	) {
		Coordinate dp1 = intersectedPoints.get(0);
		Coordinate dp2 = intersectedPoints.get(1);
		Vecteur v = new Vecteur(dp1, dp2);
		Vecteur v1 = new Vecteur(edge.getCoordinateN(0), edge.getCoordinateN(1));
		CoordinateList dpl1 = new CoordinateList();
		if (v.prodScalaire(v1) > 0) {
			dpl1.add(dp2);
			dpl1.add(dp1);
			dpl1.addAll(List.of(edge.getCoordinates()));
			dpl1.add(dp2);
		} else {
			dpl1.add(dp1);
			dpl1.add(dp2);
			dpl1.addAll(List.of(edge.getCoordinates()));
			dpl1.add(dp1);
		}
		Polygon pol = edge.getFactory().createPolygon(dpl1.toCoordinateArray());
		if (decompositionLevel < decompositionLevelWithRoad) {
			Coordinate[] dpl = new Coordinate[]{dp1, dp2};
			LineString directionOfCut = edge.getFactory().createLineString(dpl);
			Geometry geom = pol.difference(directionOfCut.buffer(roadWidth));
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
			if (geom instanceof Polygon) {
				pol = (Polygon) geom;
			}
		}
		return pol;
	}

	/**
	 * Determine the splitting points from line equation and OBB edges
	 */
	private static CoordinateList determineIntersectedPoints(LineEquation eq, List<LineString> ls) {
		Coordinate dp1 = eq.intersectionLineLine(
				new LineEquation(
						ls.get(0).getCoordinateN(0),
						ls.get(0).getCoordinateN(1)
				)
		);
		Coordinate dp2 = eq.intersectionLineLine(
				new LineEquation(
						ls.get(1).getCoordinateN(0),
						ls.get(1).getCoordinateN(1)
				)
		);
		if (dp1 == null) {
			System.out.println("determineIntersectedPoints: Null");
			dp1 = eq.intersectionLineLine(
					new LineEquation(
							ls.getFirst().getCoordinateN(0),
							ls.getFirst().getCoordinateN(1)
					)
			);
		}
		CoordinateList dpl = new CoordinateList();
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
	public static void selectByNeighbourdHood(
			List<SimpleFeature> featCandidates,
			DefaultFeatureCollection parcelles,
			DefaultFeatureCollection grapFeatures
	) {
		for (SimpleFeature currentParcel : featCandidates) {
			// Update of the current grap
			grapFeatures.add(currentParcel);
			// We select the surrounding parcels
			Geometry geom = (Geometry) currentParcel.getDefaultGeometry();
			// FIXME use spatialindex?
			Collection<SimpleFeature> surroundingParcels = parcelles.stream().filter(f->((Geometry)f.getDefaultGeometry()).intersects(geom.buffer(0.1))).toList();
			// We only keep features where ID is not set
			List<SimpleFeature> listNotSetSurroundingParcels = surroundingParcels.stream().filter(x -> -1 == getIDBlock(x)).collect(Collectors.toList());
			// We set the group value to the features
			listNotSetSurroundingParcels.forEach(x -> setIDBlock(x, 1));
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
	public static void exportFolder(Map<Integer, DefaultFeatureCollection> map, String folderIn, boolean debug) {
		(new File(folderIn)).mkdirs();
		// For each key we create a folder with associated features
		map.keySet().parallelStream().forEach(x -> createFolderAndExport(folderIn + x + "/", map.get(x), debug));
	}

	/**
	 * Create a folder for an entry of the map (the name parcelle.shp is used in the simulator).
	 */
	public static void createFolderAndExport(String path, DefaultFeatureCollection features, boolean debug) {
		File f = new File(path);
		f.mkdirs();
		try {
			if (debug) {
				// If we want to export the bounding boxes
				Collection<UrbanFeature> pop = new ArrayList<>();
				UrbanFeature feat = new AbstractUrbanFeature();
				feat.setGeometry(new GeometryFactory().toGeometry(features.getBounds()));
				pop.add(feat);
				FeatureType featureType = new FeatureType("Debug");
				featureType.setGeometryType("Polygon");
				ShapefileWriter.write(pop, featureType, CRS.decode(ZonePackager.SRID_END), path + "bbox.shp");
			}
			FeatureType featureType = new FeatureType("Feature");
			featureType.setGeometryType("Polygon");
			ShapefileWriter.write(
					features.stream().map(feature-> {
						UrbanFeature feat = new AbstractUrbanFeature();
						feat.setGeometry((Geometry) feature.getDefaultGeometry());
						return feat;
					}).toList(),
					featureType,
					CRS.decode(ZonePackager.SRID_END),
					path + "parcelle.shp"
			);
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
	public static int getIDBlock(SimpleFeature x) {
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
	public static void setIDBlock(SimpleFeature x, int value) {
		x.setAttribute(ZonePackager.ATTRIBUTE_NAME_GROUP, value);//FIXME ADD to featureType
	}

	private static boolean hasToBeSimulated(SimpleFeature feat) {
		Object o = feat.getAttribute(ATTRIBUTE_SIMUL);
		String strO = o.toString();
		try {
			int str0ToInt = Integer.parseInt(strO);
			return (str0ToInt == 1);
		} catch (Exception e) {
			try {
                return Boolean.parseBoolean(strO);
			} catch (Exception e2) {
				e.printStackTrace();
				return false;
			}
		}
	}

	/**
	 * Adding missing attributes : - the ID is generated from a concatenation of several attributes - the ATTRIBUTE_NAME_BAND that is set to 0 as there is only one band regulation
	 */
	private static void generateMissingAttributes(SimpleFeature x) {
		// We set the NAME_BAND value to 0
		x.setAttribute(ZonePackager.ATTRIBUTE_NAME_BAND, 0);//FIXME Add Attribute to FeatureType
		Object departement = x.getAttribute(ATTRIBUTE_DEPARTEMENT);
		Object commune = x.getAttribute(ATTRIBUTE_COMMUNE);
		if (commune == null) {
			/// It is maybe only an IDPAR ?
			Object idpar = x.getAttribute(ATTRIBUTE_NAME_ID);
			if (idpar == null) {
				return;
			}
			x.setAttribute(ZonePackager.ATTRIBUTE_NAME_ID, idpar);//FIXME Add Attribute to FeatureType
			return;
		}
		String prefix = x.getAttribute(ATTRIBUTE_PREFIXE).toString();
		String section = x.getAttribute(ATTRIBUTE_SECTION).toString();
		String numero = x.getAttribute(ATTRIBUTE_NUMERO).toString();
		// Departement may be null when ATT_COMMUNE already contains the
		// departement number
		String strDepartement = (departement != null) ? departement.toString() : "";
		String idFinal = strDepartement + commune + prefix + section + numero;
		x.setAttribute(ZonePackager.ATTRIBUTE_NAME_ID, idFinal);//FIXME Add Attribute to FeatureType
	}
}
