package fr.ign.cogit.simplu3d.enau.energy;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.rjmcmc.energy.UnaryEnergy;

public class ProspectEnergy2<T> implements UnaryEnergy<T> {
	

	private double slope;
	private double hIni = 0;
	Geometry jtsCurveLimiteFrontParcel = null;

	public ProspectEnergy2(double slope, BasicPropertyUnit bPU) {
		this.slope = slope;


		List<IOrientableCurve> lCurveVoirie = new ArrayList<>();

		for (CadastralParcel cP : bPU.getCadastralParcel()) {
			// for (SubParcel sB : cP.getSubParcel()) {

			for (SpecificCadastralBoundary sCB : cP
					.getSpecificCadastralBoundary()) {

				if (sCB.getType() != SpecificCadastralBoundary.ROAD) {

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

		Cuboid c = (Cuboid) t;


		double h = -1;
		double distance = c.toGeometry().distance(jtsCurveLimiteFrontParcel);

		h = ((Cuboid) c).height;
		
	//	System.out.println("Height : " + height);
		//	System.out.println("Distance : " + distance + "  slope " + slope + "  hIni "+ hIni);

		return                  Math.min(distance * slope + hIni, h) /    Math.max(distance * slope + hIni, h);
		
	
	}

}
