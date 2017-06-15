package fr.ign.cogit.simplu3d.experiments.herault.parcel;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.calculation.OBBBlockDecomposition;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.random.Random;

public class ParcelSplitting {

	public static void main(String[] args) throws Exception {
		// Chemin vers le fichier contenant les parcelles
		String file = "/home/mickael/Bureau/Parcel_div/test/parcelle.shp";
		String fileOut = "/home/mickael/Bureau/Parcel_div/test/parcelle_transformed.shp";

		String attNameToTransform = "SPLIT";

		// Maximal area of a parcel
		double maximalArea = 100;
		// Maximal road access
		double maximalWidth = 50;

		// Likness to develop road access [0, 1]
		double epsilon = 0;
		// Variation of parcel size
		double noise = 10;

		IFeatureCollection<IFeature> ifeatColl = ShapefileReader.read(file);
		IFeatureCollection<IFeature> ifeatCollOut = new FT_FeatureCollection<>();

		for (IFeature feat : ifeatColl) {

			Object o = feat.getAttribute(attNameToTransform);
			if (o == null) {
				ifeatCollOut.add(feat);
				continue;
			}

			if (Integer.parseInt(o.toString()) != 1) {
				ifeatCollOut.add(feat);
				continue;
			}

			IPolygon pol = (IPolygon) FromGeomToSurface.convertGeom(feat.getGeom()).get(0);

			OBBBlockDecomposition obb = new OBBBlockDecomposition(pol, maximalArea, maximalWidth, Random.random(),
					epsilon, noise);

			IFeatureCollection<IFeature> featCollTemp = obb.decompParcel();

			for (IFeature featNew : featCollTemp) {
				IFeature featTemp = feat.cloneGeom();
				featTemp.setGeom(featNew.getGeom());
				ifeatCollOut.add(featTemp);
			}

		}

		ShapefileWriter.write(ifeatCollOut, fileOut);

	}

}
