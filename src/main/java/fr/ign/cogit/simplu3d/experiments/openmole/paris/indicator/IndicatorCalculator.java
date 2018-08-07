package fr.ign.cogit.simplu3d.experiments.openmole.paris.indicator;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.analysis.streetprofile.Profile;
import fr.ign.cogit.geoxygene.sig3d.calculation.raycasting.RayCasting;
import fr.ign.cogit.geoxygene.sig3d.calculation.raycasting.Visibility;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;

public class IndicatorCalculator {

	// Le dossier contenant les données nécessaires aux calculs
	private static String folderName = "/home/mbrasebin/Documents/Donnees/Exp/Eugene_Million/";
	//// INPUT FILES in folderName
	private static String simulationFile = folderName + "simulation.shp";
	private static String buildingFile = folderName + "batiment.shp";
	private static String roadFile = folderName + "route.shp";
	private static String pointFile = folderName + "point.shp";

	// Le dossier de sauvegarde
	private static String outputFolder = "/tmp/tmp/";
	//// OUTPUT FILES in outputFolder
	private static String skyOpeness = outputFolder + "openess.shp";
	private static String debug3DSkyOp = outputFolder + "3DOP.shp";
	private static String debugProfile = outputFolder + "profile.shp";

	public static void main(String[] args) {
		// Boolean to export debug
		// - Les sphères 3D de l'ovuerture de ciel
		// - Les points 3D du profile
		boolean export3D = true;

		// Import des bâtiments existants et simulés et fusion au sein d'une même
		// collection
		IFeatureCollection<IFeature> totalBuildingFeatureCollection = buildingFusion(simulationFile, buildingFile);
		System.out.println("Total number of building : " + totalBuildingFeatureCollection.size());

		// Désactiver la ligne suivante s'il y a besoin d'exporter le réultat de la
		// fusion
		// ShapefileWriter.write(totalBuildingFeatureCollection, outputFolder +
		// "fusBuilding.shp");

		// import of the points for sky view factor calculation
		IFeatureCollection<IFeature> featPoints = ShapefileReader.read(pointFile);
		System.out.println("Number of points : " + featPoints.size());

		// Import of roads for road profile
		IFeatureCollection<IFeature> routes = ShapefileReader.read(roadFile);
		System.out.println("Number of routes : " + routes.size());

		long t = System.currentTimeMillis();

		// Calculating skyview factor attributes are points, buildings, number of points
		// for 180°, distance to consider a building, debug mod for 3D export
		IFeatureCollection<IFeature> featCOut = skyOpenessCalculation(featPoints, totalBuildingFeatureCollection, 180,
				100, export3D);
		System.out.println("Time to calculate SkyOpeness : " + (System.currentTimeMillis() - t));
		t = System.currentTimeMillis();

		// Statistics are export as a shapefile
		ShapefileWriter.write(featCOut, skyOpeness);

		// Profile calculation
		Profile profile = profileCalculation(routes, totalBuildingFeatureCollection, 1, 1, 100, export3D);
		// We will see nextly which indicator is relevant ...

		System.out.println("Time to calculate Profile : " + (System.currentTimeMillis() - t));
	}

	/**
	 * 
	 * @param pathSimulatedBuilding Fichier shapefile de bâtiments simulés
	 * @param pathContextBuilding   Fichier shapefile de bâtiments 3D
	 * @return la fusion des collections contenues dans les deux fichiers
	 */
	public static IFeatureCollection<IFeature> buildingFusion(String pathSimulatedBuilding,
			String pathContextBuilding) {

		IFeatureCollection<IFeature> featC = ShapefileReader.read(pathContextBuilding);
		featC.addAll(ShapefileReader.read(pathSimulatedBuilding));
		return featC;
	}

	/**
	 * 
	 * @param points les points surlequels l'ouverture de ciel sera calculée
	 * @param buildings les bâtiments 
	 * @param step le nombre de points pour 180°
	 * @param rayon la distance jusqu'à laquelle les objets sont pris en compte
	 * @param export3D Est-ce que l'on exporte les sphères 3D dans debug3DSkyOp (en attribut statique)
	 * @return
	 */
	public static IFeatureCollection<IFeature> skyOpenessCalculation(IFeatureCollection<IFeature> points,
			IFeatureCollection<IFeature> buildings, int step, double rayon, boolean export3D) {
		// Intern algorithm parameters do not touch
		RayCasting.EPSILON = 0.01;
		int resultType = RayCasting.TYPE_FIRST_POINT_AND_SPHERE;

		// Exporte des points avec comme attributs les indicateurs calculés (cf
		// https://github.com/IGNF/geoxygene-sig3d-appli#outputs-1)
		IFeatureCollection<IFeature> reastultsFeat = new FT_FeatureCollection<>();

		// Export les sphère 3D si nécessaire
		IFeatureCollection<IFeature> iFeature3D = new FT_FeatureCollection<>();

		// DefaultParameters (do not touch)
		boolean isSphere = false;
		Visibility.WELL_ORIENTED_FACE = false;
		RayCasting.CHECK_IS_ON_EDGE = true;

		// Pour chaque point on fait un raycasting
		for (IFeature currentFeature : points) {
			RayCasting rC = new RayCasting(currentFeature.getGeom().coord().get(0), buildings, step, rayon, resultType,
					isSphere);
			rC.cast();
			// On prépare les résultats (le point avec les attributs calculés)
			IFeature featOut = rC.prepareRayCastingRecords(currentFeature);

			if (featOut != null) {
				// On l'ajoute à la collection
				reastultsFeat.add(featOut);
			}

			// On stocke la sphère 3D si nécessaire
			if (export3D) {
				// Generating the geometry
				IFeature feat = new DefaultFeature(new GM_MultiSurface<>(rC.getGeneratedSolid().getFacesList()));
				iFeature3D.add(feat);
			}

		}

		if (export3D) {
			// On sauve la sphère 3D au besoin
			ShapefileWriter.write(iFeature3D, debug3DSkyOp);

		}
		// On exporte la collection avec les points et les indicateurs calculés
		return reastultsFeat;

	}

	/**
	 * 
	 * @param routes les routes surlesquelles le parcourt sera effectué (l'algorithme fusionner les objets pour faire une seule ligne)
	 * @param buildings les bâtimetns qui seront utilisés pour le profile
	 * @param stepXY le pas en absicess curviligne pour l'échantillonage du profile
	 * @param stepZ le pas en altitude pour l'échantillonage du profile
	 * @param maxDist la distance maximale jusqu'à laquelle les bâtimets sont pris en compte
	 * @param export3D l'export 3D des points dans le fichier debugProfile (en attribut statique)
	 * @return
	 */
	public static Profile profileCalculation(IFeatureCollection<IFeature> routes,
			IFeatureCollection<IFeature> buildings, double stepXY, double stepZ, double maxDist, boolean export3D) {

		Profile profile = new Profile(routes,
				// Set of contigus roads from which the profil is calculated
				buildings,
				// 3D buildings used

				null);
		// Setting attributes
		profile.setXYStep(stepXY);
		profile.setZStep(stepZ);
		profile.setLongCut(maxDist);

		profile.setDisplayInit(true);

		System.out.println("Loading data");
		profile.loadData(false);
		System.out.println("Processing");
		profile.process();

		System.out.println("Writing output");

		if (export3D) {
			IFeatureCollection<IFeature> ft1 = profile.getBuildingSide1();
			IFeatureCollection<IFeature> ft2 = profile.getBuildingSide2();

			IFeatureCollection<IFeature> featCollPointOut = new FT_FeatureCollection<>();
			if (ft1 != null) {
				featCollPointOut.addAll(ft1);
			}

			if (ft2 != null) {
				featCollPointOut.addAll(ft2);
			}
			ShapefileWriter.write(featCollPointOut, debugProfile);
		}

		return profile;
	}
}
