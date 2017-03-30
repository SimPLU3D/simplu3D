package fr.ign.cogit.simplu3d.experiments.enau.calculation;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.calculation.Util;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.experiments.enau.geometry.DeformedCuboid;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;

public class ENAUMorphoIndicators {
	
	BasicPropertyUnit bpu;
	DeformedCuboid dC;
	
	public ENAUMorphoIndicators(BasicPropertyUnit bpu, DeformedCuboid dC){
		
		this.bpu = bpu;
		this.dC = dC;
		init();
		
		
	}
	
	Geometry jtsCurveLimiteFrontParcel = null;
	Geometry jtsCurveLimiteSepParcel = null;
	
	private  void init(){
		
		List<IOrientableCurve> lCurveVoirie = new ArrayList<>();
		List<IOrientableCurve> lCurveSep= new ArrayList<>();
		for (CadastralParcel cP : bpu.getCadastralParcels()) {
			// for (SubParcel sB : cP.getSubParcel()) {

			for (ParcelBoundary sCB : cP
					.getBoundaries()) {

				IGeometry geom = sCB.getGeom();
				
				if (sCB.getType() == ParcelBoundaryType.ROAD) {

				

					if (geom instanceof IOrientableCurve) {

						lCurveVoirie.add((IOrientableCurve) geom);

					} else {
						System.out
								.println("Classe UB14PredicateFull : quelque chose n'est pas un ICurve");
					}

				}else{
					
					if (geom instanceof IOrientableCurve) {
						lCurveSep.add((IOrientableCurve)geom);
					}
					
				}

				// }

			}

		}


//		System.out.println(lCurveVoirie.size());

		GeometryFactory gf = new GeometryFactory();

		if (!lCurveVoirie.isEmpty()) {
			try {
				this.jtsCurveLimiteFrontParcel = AdapterFactory.toGeometry(gf,
						new GM_MultiCurve<>(lCurveVoirie));
				this.jtsCurveLimiteSepParcel = AdapterFactory.toGeometry(gf,
						new GM_MultiCurve<>(lCurveSep));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	
	///////////////Satisfaction servitude vue
	public double getSatisfactionServitudeVue(double valeurCible){


		double dist = this.dC.toGeometry().distance(jtsCurveLimiteSepParcel);

		return Math.min(valeurCible, dist)
				/ Math.max(valeurCible, dist);
	}	
	
	
	//////Satisfaction hauteur
	public double getSatisfactionHauteur(double valeurCible){
		double value1 = Math.min(this.dC.height1, valeurCible) / Math.max(this.dC.height1, valeurCible);
		double value2 = Math.min(this.dC.height2, valeurCible) / Math.max(this.dC.height2, valeurCible);
		double value3 = Math.min(this.dC.height3, valeurCible) / Math.max(this.dC.height3, valeurCible);
		double value4 = Math.min(this.dC.height4, valeurCible) / Math.max(this.dC.height4, valeurCible);
		
		
		return (value1 + value2 + value3 + value4)/4;
	}
	
	
	///////Satisfaction alignement
	public double getSatisfactionAlignement(double valeurCible){

		double dist = this.dC.toGeometry().distance(jtsCurveLimiteFrontParcel);

		System.out.println("Distance : " + dist);
		
		return Math.min(valeurCible, dist)
				/ Math.max(valeurCible, dist);
	}
	
	
	
	///////Satisfaction CES
	public double getSatisfactionCES(double valeurCible){

		double ces = this.dC.toGeometry().getArea() / bpu.getPol2D().area();

		return Math.min(valeurCible, ces)
				/ Math.max(valeurCible, ces);
	}
	
	
	////Satisfaction prospect Route
	public double getSatisfactionProspectRoute(double slope, double hIni){
		GeometryFactory geomFact = new GeometryFactory();
		
		Coordinate[] coord = this.dC.toGeometry().getCoordinates();
		
		double distance1 = jtsCurveLimiteFrontParcel.distance(geomFact.createPoint(coord[0]));
		double distance2 = jtsCurveLimiteFrontParcel.distance(geomFact.createPoint(coord[1]));
		double distance3 = jtsCurveLimiteFrontParcel.distance(geomFact.createPoint(coord[2]));
		double distance4 = jtsCurveLimiteFrontParcel.distance(geomFact.createPoint(coord[3]));
		
		double contrib1 =  Math.min(distance1 * slope + hIni, dC.height1) /    Math.max(distance1 * slope + hIni, dC.height1);
		double contrib2 =  Math.min(distance2 * slope + hIni, dC.height2) /    Math.max(distance2 * slope + hIni, dC.height2);
		double contrib3 =  Math.min(distance3 * slope + hIni, dC.height3) /    Math.max(distance3 * slope + hIni, dC.height3);
		double contrib4 =  Math.min(distance4 * slope + hIni, dC.height4) /    Math.max(distance4 * slope + hIni, dC.height4);
		
		
		return (contrib1 + contrib2 + contrib3 + contrib4) / 4;
	}
	
	
	////Satisfaction prospect LimSep
	public double getSatisfactionProspectLimSep(double slope, double hIni){
		GeometryFactory geomFact = new GeometryFactory();
		
		Coordinate[] coord = this.dC.toGeometry().getCoordinates();
		
		double distance1 = jtsCurveLimiteSepParcel.distance(geomFact.createPoint(coord[0]));
		double distance2 = jtsCurveLimiteSepParcel.distance(geomFact.createPoint(coord[1]));
		double distance3 = jtsCurveLimiteSepParcel.distance(geomFact.createPoint(coord[2]));
		double distance4 = jtsCurveLimiteSepParcel.distance(geomFact.createPoint(coord[3]));
		
		double contrib1 =  Math.min(distance1 * slope + hIni, dC.height1) /    Math.max(distance1 * slope + hIni, dC.height1);
		double contrib2 =  Math.min(distance2 * slope + hIni, dC.height2) /    Math.max(distance2 * slope + hIni, dC.height2);
		double contrib3 =  Math.min(distance3 * slope + hIni, dC.height3) /    Math.max(distance3 * slope + hIni, dC.height3);
		double contrib4 =  Math.min(distance4 * slope + hIni, dC.height4) /    Math.max(distance4 * slope + hIni, dC.height4);
		
		
		return (contrib1 + contrib2 + contrib3 + contrib4) / 4;
	}
	
	
	public double volume(){
		
		
		double volume = Math.abs(Util.volumeUnderSurface(dC.getTriangle()));
		volume = volume - dC.getTriangle().get(0).area() * dC.getZmin()
				- dC.getTriangle().get(1).area() * dC.getZmin();
		
		return volume;
		
	}


}
