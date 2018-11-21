package fr.ign.cogit.simplu3d.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IPoint;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Arc;
import fr.ign.cogit.geoxygene.contrib.cartetopo.CarteTopo;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Face;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromPolygonToLineString;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion2DObject;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.geoxygene.util.algo.PointInPolygon;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.geoxygene.util.index.Tiling;
import fr.ign.cogit.simplu3d.util.SDPCalc.GeomHeightPair;

/**
 * Class that enables the fusion of Cuboid Geometry in order to make consistant
 * geomtries one by path of cuboid
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see http://www.cecill.info/
 * 
 * 
 * 
 * copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class Recal3D {

	private static Logger logger = Logger.getLogger(Recal3D.class);

	private final static String ATT_TEMP = "TEMP";

	public static void main(String[] args) throws CloneNotSupportedException {

		DirectPosition.PRECISION = 6;
		// Fixing the Z Min
		double ZMIN = 0;// 139.;

		// The shapefile to simplu3D simulatoin to process
		String shpeIn = "/home/mickael/data/mbrasebin/donnees/IAUIDF/data_grille/results_77_test/77007650/simul_77007650_true_no_demo_sampler.shp";

		// Folder in
		String strShpOut = "/tmp/tmp/";

		Recal3D recal = new Recal3D();

		IFeatureCollection<IFeature> fus = recal.fusionneGeomByGroup(shpeIn, ZMIN);

		ShapefileWriter.write(fus, strShpOut + "fus.shp");

	}
	
	double surface;

	public IFeatureCollection<IFeature> fusionneGeomByGroup(String shpIn, double zMini) {
		
		//Creating a partition of cuboid with height stored as ATT_TEMP attribute
		IFeatureCollection<IFeature> featColl = this.createPartitionCollection(shpIn);
		

		//Creating the neighbourhood relationship with CarteTopo
		CarteTopo carteTopo = newCarteTopo("-aex90", featColl, 0.5);

		//Spatial index to speed up
		featColl.initSpatialIndex(Tiling.class, false);
		
		//Surface is computed
		surface = 0;

		//The results
		IFeatureCollection<IFeature> featCollOut = new FT_FeatureCollection<>();

		// For each group the faces are treated (normally there is only one)
		// for (Groupe g : lG) {

		List<IOrientableSurface> lOS = new ArrayList<>();

		// We prepare the list of available faces


		// For each face we look at the maximal height
		for (Face f : carteTopo.getPopFaces()) {

			//We do not treat the infinite height
			if (f.isInfinite()) {
				continue;
			}

			//We took a point into the face and look if we find it in the original data
			IPoint p = new GM_Point(PointInPolygon.get(f.getGeometrie(), 0.001));

			if (!f.getGeometrie().contains(p)) {
				//This may happen for all as CarteTopo create face for them
				logger.info("Point not in polygon : " +f);
			}

			Collection<IFeature> featSelect = featColl.select(p);

			double zMax = Double.NEGATIVE_INFINITY;

			if (featSelect.isEmpty()) {

				zMax = zMini;
				System.out.println(f.getGeometrie());
				System.out.println("New empty face detected");

			}

			for (IFeature feat : featSelect) {

				zMax = Math.max(zMax, Double.parseDouble(feat.getAttribute(ATT_TEMP).toString()));
			}

			IPolygon poly = (IPolygon) f.getGeometrie().clone();

			if (!featSelect.isEmpty()) {

				lOS.add(poly);
				surface = surface + poly.area();
			}

			AttributeManager.addAttribute(f, "Z", zMax, "Double");

			for (IDirectPosition dp : poly.coord()) {
				dp.setZ(zMax);
			}

		}

		// For each edges we determine the minimal and maximal height in
		// order to generate vertical faces

		for (Arc a : carteTopo.getListeArcs()) {

			Face fd = a.getFaceDroite();
			Face fg = a.getFaceGauche();

			if (fd == null && fg == null) {
				continue;
			}

			double z1 = 0;
			double z2 = 0;

			if (fd == null || fd.isInfinite()) {

				z1 = Double.parseDouble(fg.getAttribute("Z").toString());
				z2 = zMini;

			} else if (fg == null || fg.isInfinite()) {

				z1 = Double.parseDouble(fd.getAttribute("Z").toString());
				z2 = zMini;

			} else {

				z1 = Double.parseDouble(fg.getAttribute("Z").toString());
				z2 = Double.parseDouble(fd.getAttribute("Z").toString());

			}

			double zMin = Math.min(z1, z2);
			double zMax = Math.max(z1, z2);

			if (zMax == zMini) {

				continue;
			}

			ILineString lineToExtrude = (ILineString) a.getGeometrie().clone();

			surface = surface + lineToExtrude.length() * (zMax - zMin);

			// Extrusion of the geometry according to the considered height
			IGeometry geom = Extrusion2DObject.convertFromLine(lineToExtrude, zMin, zMax);

			lOS.addAll(FromGeomToSurface.convertGeom(geom));

		}

		System.out.println("surface : " + surface);
		featCollOut.add(new DefaultFeature(new GM_MultiSurface<>(lOS)));

		return featCollOut;
	}
	
	/**
	 * 
	 * @param shpIn shapefile from Cuboid simplu3D simulation
	 * @return a partition with polygons that have a Z
	 */
	public IFeatureCollection<IFeature> createPartitionCollection(String shpIn){
		//Launching SDPCalc to get the partition as a collection of GeomHeightPair
		SDPCalc sd = new SDPCalc();
		sd.process(shpIn);

		List<List<GeomHeightPair>> llGeomPair = sd.getGeometryPairByGroup();

		//Agregating the pair into a collection of features
		//With the height stored as ATT_TEMP
		IFeatureCollection<IFeature> featColl = new FT_FeatureCollection<>();
		for (List<GeomHeightPair> lPair : llGeomPair) {
			for (GeomHeightPair pair : lPair) {

				IGeometry geom = JTS.fromJTS(pair.geom);
				for (IOrientableSurface os : FromGeomToSurface.convertGeom(geom)) {

					IFeature feat = new DefaultFeature(os);
					AttributeManager.addAttribute(feat, ATT_TEMP, pair.height, "Double");
					featColl.add(feat);
				}
			}
		}

		return featColl;
	}


	private static CarteTopo newCarteTopo(String name, IFeatureCollection<? extends IFeature> collection,
			double threshold) {

		try {
			// Initialisation d'une nouvelle CarteTopo
			CarteTopo carteTopo = new CarteTopo(name);
			carteTopo.setBuildInfiniteFace(false);
			// Récupération des arcs de la carteTopo
			IPopulation<Arc> arcs = carteTopo.getPopArcs();
			// Import des arcs de la collection dans la carteTopo
			for (IFeature feature : collection) {

				List<ILineString> lLLS = FromPolygonToLineString
						.convertPolToLineStrings((IPolygon) FromGeomToSurface.convertGeom(feature.getGeom()).get(0));

				for (ILineString ls : lLLS) {

					// affectation de la géométrie de l'objet issu de la
					// collection
					// à l'arc de la carteTopo
					for (int i = 0; i < ls.numPoints() - 1; i++) {
						// création d'un nouvel élément
						Arc arc = arcs.nouvelElement();
						arc.setGeometrie(new GM_LineString(ls.getControlPoint(i), ls.getControlPoint(i + 1)));
						// instanciation de la relation entre l'arc créé et
						// l'objet
						// issu de la collection
						arc.addCorrespondant(feature);
					}

				}

			}
			if (!test(carteTopo)) {
				logger.error("");
				System.exit(0);
			}
			carteTopo.creeNoeudsManquants(0.0);

			if (!test(carteTopo)) {
				logger.error("");
				System.exit(0);
			}

			carteTopo.fusionNoeuds(threshold);

			if (!test(carteTopo)) {
				logger.error("");
				System.exit(0);
			}

			carteTopo.decoupeArcs(0.01);
			carteTopo.splitEdgesWithPoints(0.01);

			carteTopo.filtreArcsDoublons();
			if (!test(carteTopo)) {
				logger.error("");
				System.exit(0);
			}

			// Création de la topologie Arcs Noeuds

			carteTopo.creeTopologieArcsNoeuds(threshold);
			// La carteTopo est rendue planaire
			if (!test(carteTopo)) {
				logger.error("");
				System.exit(0);
			}

			// DEBUG.addAll(carteTopo.getListeArcs());
			// Création des faces de la carteTopo
			carteTopo.creeTopologieFaces();
			if (!test(carteTopo)) {
				logger.error("");
				System.out.println("Error 3");
			}

			return carteTopo;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 * @param ct
	 *            a CarteTopo to test
	 * @return true if carteTopo is Valid
	 */
	private static boolean test(CarteTopo ct) {
		for (Arc a : ct.getPopArcs()) {

			if (a.getGeometrie().coord().size() < 2) {
				return false;
			}

		}

		return true;

	}

}
