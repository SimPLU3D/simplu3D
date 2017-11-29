package fr.ign.cogit.simplu3d.experiments.herault.parcel;

import java.io.File;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.calculation.parcelDecomposition.OBBBlockDecomposition;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.random.Random;

public class ParcelSplitting {

	public static void main(String[] args) throws Exception {
		parcelSplit(new File("/home/mcolomb/tmp/test/parcelle2.shp"),new File("/home/mcolomb/tmp/test/parcDiv4.shp"),10000,50,0.5,5);
	}
	
	/**
	 * 
	 * @param fileIn : input file
	 * @param fileOut : output file
	 * @param maximalArea : Maximal area of a parcel
	 * @param maximalWidth : Maximal road access
	 * @param raodEpsilon : Likness to develop road access [0, 1]
	 * @param noise : Variation of parcel size
	 * @return shapefile of the splited parcels
	 * @throws Exception
	 */
	public static File parcelSplit(File fileIn, File fileOut, double maximalArea, double maximalWidth, double roadEpsilon, double noise ) throws Exception{

		String attNameToTransform = "SPLIT";

		IFeatureCollection<IFeature> ifeatColl = ShapefileReader.read(fileIn.toString());
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
					roadEpsilon, noise);

			IFeatureCollection<IFeature> featCollTemp = obb.decompParcel();

			for (IFeature featNew : featCollTemp) {
				IFeature featTemp = feat.cloneGeom();
				featTemp.setGeom(featNew.getGeom());
				ifeatCollOut.add(featTemp);
			}
		}
		ShapefileWriter.write(ifeatCollOut, fileOut.toString());
		return fileOut;
	}
}
