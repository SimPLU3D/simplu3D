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
	
	
	private static String folderName = "/home/mbrasebin/Documents/Donnees/Exp/Eugene_Million/";
	////INPUT FILES in folderName
	private static String simulationFile = folderName+"simulation.shp";
	private static String buildingFile = folderName+"batiment.shp";
	private static String roadFile = folderName+"route.shp";
	private static String pointFile = folderName+"point.shp";
	
	
	private static String outputFolder = "/tmp/tmp/";
	////OUTPUT FILES in outputFolder
	private static String skyOpeness = outputFolder + "openess.shp";
	private static String debug3DSkyOp = outputFolder + "3DOP.shp";
	private static String debugProfile = outputFolder + "profile.shp";
	
	
	public static void main(String[] args) {
		//Boolean to export debug
		boolean export3D = true;

		//Import and fusion of buildings
		IFeatureCollection<IFeature>  totalBuildingFeatureCollection = buildingFusion(simulationFile, buildingFile);
		System.out.println("Total number of building : " + totalBuildingFeatureCollection.size());
		//Write the result of building fusion
		//ShapefileWriter.write(totalBuildingFeatureCollection, outputFolder + "fusBuilding.shp");
		
		
		//import of the points for sky view factor calculation
		IFeatureCollection<IFeature> featPoints = ShapefileReader.read(pointFile);
		System.out.println("Number of points : " + featPoints.size());
		
		//Import of roads for road profile
		IFeatureCollection<IFeature> routes = ShapefileReader.read(roadFile);		
		System.out.println("Number of routes : " + routes.size());
		
		
		long t = System.currentTimeMillis();
		
		//Calculating skyview factor attributes are points, buildings, number of points for 180°, distance to consider a building, debug mod for 3D export
		 IFeatureCollection<IFeature>  featCOut =  skyOpenessCalculation(featPoints, totalBuildingFeatureCollection, 180, 100, export3D);
		System.out.println("Time to calculate SkyOpeness : " + (  System.currentTimeMillis() - t)); 
		t = System.currentTimeMillis();
		 
		 
		 //Statistics are export as a shapefile
		 ShapefileWriter.write(featCOut, skyOpeness);
		 
		 //Profile calculation
		 Profile profile = profileCalculation(routes, totalBuildingFeatureCollection,1,1,100,export3D);
		 //We will see nextly which indicator is relevant ...
		 
			System.out.println("Time to calculate Profile : " + (System.currentTimeMillis() - t)); 
	}
	
	
	
	
	public static IFeatureCollection<IFeature> buildingFusion(String pathSimulatedBuilding, String pathContextBuilding) {
		
		IFeatureCollection<IFeature> featC = ShapefileReader.read(pathContextBuilding);
		featC.addAll(ShapefileReader.read(pathSimulatedBuilding));
		return featC;
	}
	
	
	public static IFeatureCollection<IFeature> skyOpenessCalculation(IFeatureCollection<IFeature> points, IFeatureCollection<IFeature> buildings, int step, double rayon, boolean export3D) {
		
		
		
	
		RayCasting.EPSILON = 0.01;
		

		int resultType = RayCasting.TYPE_FIRST_POINT_AND_SPHERE;
		
		
		IFeatureCollection<IFeature> reastultsFeat = new FT_FeatureCollection<>();
		IFeatureCollection<IFeature> iFeature3D = new FT_FeatureCollection<>();
		
		
		// DefaultParameters
		boolean isSphere = false;
		Visibility.WELL_ORIENTED_FACE = false;
		RayCasting.CHECK_IS_ON_EDGE = true;
		
		for(IFeature currentFeature: points) {
			RayCasting rC = new RayCasting(currentFeature.getGeom().coord().get(0), buildings, step, rayon,
					resultType, isSphere);
			rC.cast();
			
			IFeature featOut = rC.prepareRayCastingRecords(currentFeature);
			
			if(featOut != null) {
				reastultsFeat.add(featOut);
			}
			
			
			
			if (export3D) {
				// Generating the geometry
				IFeature feat = new DefaultFeature(new GM_MultiSurface<>(rC.getGeneratedSolid().getFacesList()));
				iFeature3D.add(feat);
			}
			
		}
		
		if(export3D) {
			ShapefileWriter.write(iFeature3D, debug3DSkyOp);
			
		}
	
		return reastultsFeat;

	}
	
	
	public static Profile profileCalculation(IFeatureCollection<IFeature> routes, IFeatureCollection<IFeature> buildings, double stepXY, double stepZ, double maxDist, boolean export3D)
	{
		
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
		
		if(export3D) {
			IFeatureCollection<IFeature> ft1 = profile.getBuildingSide1();
			IFeatureCollection<IFeature> ft2 = profile.getBuildingSide2();

			IFeatureCollection<IFeature> featCollPointOut = new FT_FeatureCollection<>();
			if (ft1 != null ) {
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
