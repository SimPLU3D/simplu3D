package fr.ign.cogit.simplu3d.experiments.enau;

import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.calculation.OrientedBoundingBox;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion3DObject;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.algo.CommonAlgorithms;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;

public class TransformCoordinates {
	
	
	public static void main(String[] args){
		String folder = "C:/Users/mbrasebin/Desktop/Alia/";
		
		String fileAlia = folder + "coucheAlia.shp";
		String fileMick = folder + "parcelle.shp";
		
		String fileToTrans = folder + "couche_formeAlia.shp";
		
		String fileOut = folder +  "out.shp";
		String fileOutForm = folder + "couche_formeAlia_trans.shp";
		
		
		IFeatureCollection<IFeature> featCAlia = ShapefileReader.read(fileAlia);
		IFeature featA = featCAlia.get(0);
		
		IFeatureCollection<IFeature> featCMick = ShapefileReader.read(fileMick);
		IFeature featM = null;
		
		for(IFeature featTemp : featCMick){
			if(featTemp.getAttribute("id_parcell").toString().equals("3")){
				featM = featTemp;
				System.out.println("Appariement réussi");
			}
			
			
		}
		
		
		
		IDirectPosition dpA = featA.getGeom().centroid();
		IDirectPosition dpM = featM.getGeom().centroid();
		
		Vecteur v = new Vecteur(dpM.getX() - dpA.getX(), dpM.getY() - dpA.getY());
		
		featA.setGeom(featA.getGeom().translate(v.getX(), v.getY(), v.getZ()));
		
		OrientedBoundingBox oBBA = new OrientedBoundingBox(featA.getGeom());
		OrientedBoundingBox oBBM = new OrientedBoundingBox(featM.getGeom());
		
		
		double angleA = oBBA.getAngle();
		double angleM = oBBM.getAngle();
		
		double angleRotate =  angleM - angleA;
		
		IPolygon pol = CommonAlgorithms.rotation((IPolygon) FromGeomToSurface.convertGeom(featA.getGeom()).get(0), angleM - angleA);
		
		
		double h = oBBM.getLength()/oBBA.getLength();
		
		
		pol = CommonAlgorithms.homothetie(pol, h);
		
		
		featA.setGeom(pol);
		
		
		
		ShapefileWriter.write(featCAlia, fileOut);
		
		
		
		IFeatureCollection<IFeature> featCFormeAlia = ShapefileReader.read(fileToTrans);
		
		for(IFeature feat:featCFormeAlia){
			
			feat.setGeom(transformPolygon(feat.getGeom(), v,angleRotate , h, pol.centroid() ));
			
		}
		
		
		
		IFeature feat1 = featCFormeAlia.get(0);
		feat1.getGeom().coord().get(0).setZ(10.8);
		feat1.getGeom().coord().get(1).setZ(17);
		feat1.getGeom().coord().get(2).setZ(17);
		feat1.getGeom().coord().get(3).setZ(10.8);
		
		
		IMultiSurface<IOrientableSurface> ims1 = new GM_MultiSurface<>();
		
		List<IOrientableSurface> lOS = FromGeomToSurface.convertGeom(feat1.getGeom());
		
		ims1.add(lOS.get(0));
		
		
		
		ims1.addAll(FromGeomToSurface.convertGeom(Extrusion3DObject.convertitFromLineUntilZMin(
				
		new GM_LineString(lOS.get(0).coord())
				,-0.001)));
		
		
		feat1.setGeom(ims1);
		
		
		
		
		IFeature feat2 = featCFormeAlia.get(1);
		feat2.getGeom().coord().get(0).setZ(17);
		feat2.getGeom().coord().get(1).setZ(10.8);
		feat2.getGeom().coord().get(2).setZ(10.8);
		feat2.getGeom().coord().get(3).setZ(17);
		
		
		List<IOrientableSurface> lOS2 = FromGeomToSurface.convertGeom(feat2.getGeom());
		
		IMultiSurface<IOrientableSurface> ims2 = new GM_MultiSurface<>();
		
		ims2.add(lOS2.get(0));
		ims2.addAll(FromGeomToSurface.convertGeom(Extrusion3DObject.convertitFromLineUntilZMin(
				
				new GM_LineString(lOS2.get(0).coord())
						,-0.001)));
		
		feat2.setGeom(ims2);
		
		
		System.out.println("Geom 1 : " + ims1);
		System.out.println("Geom 2 : " + ims2);
		
		ShapefileWriter.write(featCFormeAlia, fileOutForm);
	}
	
	
	public static IPolygon transformPolygon(IGeometry geom, Vecteur trans, double rotate, double h, IDirectPosition centre){
		
		IPolygon pol = (IPolygon) FromGeomToSurface.convertGeom(geom).get(0);
		
		pol = CommonAlgorithms.translation(pol, trans.getX(), trans.getY());
		
		pol = CommonAlgorithms.rotation(pol, centre, rotate );
		
		pol = CommonAlgorithms.homothetie(pol, h,centre);
		
		return pol;
		
	}
	
	
	

}
