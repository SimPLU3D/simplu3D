package fr.ign.cogit.simplu3d.experiments.mupcity.util;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion2DObject;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;

public class TransformTo3DBuildings {

	public static void main(String[] args) {
		String folder = "/home/mcolomb/informatique/workspace/simplu3d/simplu3D/src/main/resources/fr/ign/cogit/simplu3d/fontain/";
		String fileIn = folder + "bati.shp";
		String fileOut = folder + "bati3d.shp";
		export(fileIn, fileOut);
		System.out.println("That is all");
	}

	public static void export(String fileIn, String fileOut) {

		IFeatureCollection<IFeature> featColl = ShapefileReader.read(fileIn);

		for (IFeature feat : featColl) {
			IGeometry geom = feat.getGeom();

			double hauteur = Double.parseDouble(feat.getAttribute("HAUTEUR").toString());

			List<IOrientableSurface> los = FromGeomToSurface.convertGeom(geom);

			List<IOrientableSurface> losout = new ArrayList<>();

			for (IOrientableSurface os : los) {

				IGeometry geomTemp = Extrusion2DObject.convertFromPolygon((IPolygon) os, 0, hauteur);

				losout.addAll(FromGeomToSurface.convertGeom(geomTemp));

			}
			feat.setGeom(new GM_MultiSurface<>(losout));
		}

		System.out.println(featColl.size());

		ShapefileWriter.write(featColl, fileOut);

	}

}
