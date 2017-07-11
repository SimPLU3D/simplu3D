package fr.ign.cogit.simplu3d.exec.buildingprofile;

import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.analysis.streetprofile.Profile;
import fr.ign.cogit.geoxygene.sig3d.analysis.streetprofile.Profile.SIDE;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion2DObject;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.demo.DemoEnvironmentProvider;

public class RunBuildingProfile {

	public static void main(String[] args) {
		// Mandatory due to precision trunk in Geoxygene core
		DirectPosition.PRECISION = 10;

		// Settings of out folder
		String folderOut = "/home/pchapron/temp/";

		String folderIn = DemoEnvironmentProvider.class.getClassLoader().getResource("fr/ign/cogit/simplu3d/")
				.getPath();

		IFeatureCollection<IFeature> featCollRoad = ShapefileReader.read(folderIn + "data/route.shp");

		for (IFeature feat : featCollRoad) {
			feat.setGeom(Extrusion2DObject.convertFromGeometry(feat.getGeom(), 0, 0));
			// Roads geometries are set to zero as the tested simulation has a 0
			// zInf
		}

		Profile profile = new Profile(featCollRoad,
				// Set of contigus roads from which the profil is calculated
				ShapefileReader.read(folderIn + "simul/simul.shp"),
				// 3D buildings used
				ShapefileReader.read(folderIn + "data/parcelle.shp"),
				// Parcel as input (only buildings in the first parcel are used)
				1, // minimal height of the profile (it may be calculated
					// automatically)
				20, // maximal height of the profile (it may be calculated
					// automatically)
				1, // step along curvilinear abscissa
				1 // z step for profil calculation
		);

		// Data loading, if parcels have no z they are translated to the minimal
		// z of the scene
		profile.loadData();

		// This lines allows the visualisation of the scene
		//profile.display();

		// Calculation of the profilLe
		// The results may be acccessible by getPproj method
		// They are represented by 2D points with X = curvilinear abscissa et Y
		// = height
		// (the value is positive or negative according to the side of the orad)
		// height is measured according to an origin based on minimal height but
		// my be
		// parametrized by profile.setYProjectionShifting
		profile.process();

		// Update in the visualisation if available
		//profile.updateDisplay();

		// Point export
		profile.exportPoints(folderOut + "pointout.shp");

		List<Double> heights = profile.getHeightAlongRoad(SIDE.BOTH);

		IFeatureCollection<IFeature> featCOut = new FT_FeatureCollection<>();

		int count =0;
		for (Double d : heights) {
			featCOut.add(new DefaultFeature(new GM_Point(new DirectPosition(count * profile.getXYStep(), d))));
			count ++;
		}

		
		ShapefileWriter.write(featCOut, folderOut + "pointout_test.shp");
		System.out.println("Finished");

	}

}
