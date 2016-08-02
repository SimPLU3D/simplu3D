package fr.ign.cogit.simplu3d.experiments.enau.energy;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.experiments.enau.geometry.DeformedCuboid;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.rjmcmc.energy.UnaryEnergy;

public class ProspectEnergy<T extends AbstractSimpleBuilding>  implements UnaryEnergy<T> {
	

	private double slope, hIni;

	Geometry jtsCurveLimiteFrontParcel = null;

	public ProspectEnergy(double slope, double hIni, BasicPropertyUnit bPU) {
		this.slope = slope;
		this.hIni = hIni;

		List<IOrientableCurve> lCurveVoirie = new ArrayList<>();

		for (CadastralParcel cP : bPU.getCadastralParcels()) {
			// for (SubParcel sB : cP.getSubParcel()) {

			for (ParcelBoundary sCB : cP
					.getBoundaries()) {

				if (sCB.getType() == ParcelBoundaryType.ROAD) {

					IGeometry geom = sCB.getGeom();

					if (geom instanceof IOrientableCurve) {

						lCurveVoirie.add((IOrientableCurve) geom);

					} else {
						System.out
								.println("Classe UB14PredicateFull : quelque chose n'est pas un ICurve");
					}

				}

				// }

			}

		}

		// System.out.println("NB voirie : " + lCurveVoirie.size());

		// System.out.println("NB other : " + lCurveLatBot.size());

		System.out.println(lCurveVoirie.size());

		GeometryFactory gf = new GeometryFactory();

		if (!lCurveVoirie.isEmpty()) {
			try {
				this.jtsCurveLimiteFrontParcel = AdapterFactory.toGeometry(gf,
						new GM_MultiCurve<>(lCurveVoirie));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public double getValue(T t) {
		
		
		if(t instanceof Cuboid){
			
			Cuboid c = (Cuboid) t;


			double h = -1;
			double distance = c.toGeometry().distance(jtsCurveLimiteFrontParcel);

			h = ((Cuboid) c).height;
			

			return                  Math.min(distance * slope + hIni, h) /    Math.max(distance * slope + hIni, h);
			
			
		}
		
		
		
		if(t instanceof DeformedCuboid){
			DeformedCuboid c = (DeformedCuboid) t;
			
			GeometryFactory geomFact = new GeometryFactory();
			
			Coordinate[] coord = c.toGeometry().getCoordinates();
			
			double distance1 = jtsCurveLimiteFrontParcel.distance(geomFact.createPoint(coord[0]));
			double distance2 = jtsCurveLimiteFrontParcel.distance(geomFact.createPoint(coord[1]));
			double distance3 = jtsCurveLimiteFrontParcel.distance(geomFact.createPoint(coord[2]));
			double distance4 = jtsCurveLimiteFrontParcel.distance(geomFact.createPoint(coord[3]));
			
			double contrib1 =  Math.min(distance1 * slope + hIni, c.height1) /    Math.max(distance1 * slope + hIni, c.height1);
			double contrib2 =  Math.min(distance2 * slope + hIni, c.height2) /    Math.max(distance2 * slope + hIni, c.height2);
			double contrib3 =  Math.min(distance3 * slope + hIni, c.height3) /    Math.max(distance3 * slope + hIni, c.height3);
			double contrib4 =  Math.min(distance4 * slope + hIni, c.height4) /    Math.max(distance4 * slope + hIni, c.height4);
			
			
			return (contrib1 + contrib2 + contrib3 + contrib4) / 4;
			
		}
		
		
		System.out.println("Class not detected");
		return 0;
	
		
	
	}

}
