package fr.ign.cogit.simplu3d.experiments.thesis.predicate;

import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.mpp.configuration.AbstractBirthDeathModification;
import fr.ign.mpp.configuration.AbstractGraphConfiguration;
import fr.ign.rjmcmc.configuration.ConfigurationModificationPredicate;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.7
 **/
public class UXL3PredicateBuildingSeparation<O extends AbstractSimpleBuilding, C extends AbstractGraphConfiguration<O, C, M>, M extends AbstractBirthDeathModification<O, C, M>>
		implements ConfigurationModificationPredicate<C, M> {

	Geometry jtsCurveLimiteFondParcel = null;
	Geometry jtsCurveLimiteFrontParcel = null;
	Geometry jtsCurveLimiteLatParcel = null;

	Geometry surface = null;

	public UXL3PredicateBuildingSeparation(BasicPropertyUnit bPU) throws Exception {

		// Pour simplifier la vérification, on extrait les différentes bordures
		// de
		// parcelles
		IMultiCurve<IOrientableCurve> curveLimiteFondParcel = new GM_MultiCurve<>();
		IMultiCurve<IOrientableCurve> curveLimiteFrontParcel = new GM_MultiCurve<>();
		IMultiCurve<IOrientableCurve> curveLimiteLatParcel = new GM_MultiCurve<>();

		// On parcourt les parcelles du BasicPropertyUnit (un propriétaire peut
		// avoir plusieurs parcelles)
		for (CadastralParcel cP : bPU.getCadastralParcels()) {

			// On parcourt les limites séparaticves
			for (ParcelBoundary sCB : cP.getBoundaries()) {

				// En fonction du type on ajoute à telle ou telle géométrie
				IGeometry geom = sCB.getGeom();

				if (geom == null || geom.isEmpty() || geom.length() < 0.01) {
					continue;
				}

				// Fond de parcel
				if (sCB.getType() == ParcelBoundaryType.BOT) {

					if (geom instanceof IOrientableCurve) {
						curveLimiteFondParcel.add((IOrientableCurve) geom);

					} else {
						System.out.println(
								"Classe SamplePredicate : quelque chose n'est pas un ICurve : " + geom.getClass());
					}

				}

				// Limite latérale
				if (sCB.getType() == ParcelBoundaryType.LAT) {

					if (geom instanceof IOrientableCurve) {
						curveLimiteLatParcel.add((IOrientableCurve) geom);

					} else {
						System.out.println(
								"Classe SamplePredicate : quelque chose n'est pas un ICurve : " + geom.getClass());
					}

				}

				// Limite front
				if (sCB.getType() == ParcelBoundaryType.ROAD) {

					if (geom instanceof IOrientableCurve) {
						curveLimiteFrontParcel.add((IOrientableCurve) geom);

					} else {
						System.out.println(
								"Classe SamplePredicate : quelque chose n'est pas un ICurve : " + geom.getClass());
					}

				}

			}

		}

		GeometryFactory gf = new GeometryFactory();

		this.surface = AdapterFactory.toGeometry(gf, bPU.getGeom());

		if (!curveLimiteFondParcel.isEmpty()) {
			this.jtsCurveLimiteFondParcel = AdapterFactory.toGeometry(gf, curveLimiteFondParcel);
		}

		if (!curveLimiteFrontParcel.isEmpty()) {
			this.jtsCurveLimiteFrontParcel = AdapterFactory.toGeometry(gf, curveLimiteFrontParcel);
		}

		if (!curveLimiteLatParcel.isEmpty()) {
			this.jtsCurveLimiteLatParcel = AdapterFactory.toGeometry(gf, curveLimiteLatParcel);
		}

	}

	@Override
	public boolean check(C c, M m) {

		List<O> lO = m.getBirth();

		O batDeath = null;
		
		

		if (!m.getDeath().isEmpty()) {

			batDeath = m.getDeath().get(0);

		}

		for (O ab : lO) {
			// System.out.println("Oh une naissance");

			Iterator<O> iTBat = c.iterator();

			while (iTBat.hasNext()) {

				O batTemp = iTBat.next();

				if (batTemp == batDeath) {
					continue;
				}

				if (batTemp.getFootprint().distance(ab.getFootprint()) < 5) {
					return false;
				}

			}

			// Pas vérifié ?

			boolean checked = true;

			
			if(jtsCurveLimiteFondParcel != null){
				checked = ab.prospectJTS(jtsCurveLimiteFondParcel, 0.5, 0);
				if (!checked) {
					return false;
				}
		
			}
		
			
			if(jtsCurveLimiteFrontParcel!=null){
				checked = ab.prospectJTS(jtsCurveLimiteFrontParcel, 0.5, 0);
				if (!checked) {
					return false;
				}
				checked = (ab.toGeometry().distance(jtsCurveLimiteFrontParcel) > 5);
			}

		
			if(jtsCurveLimiteLatParcel != null){
				checked = ab.prospectJTS(jtsCurveLimiteLatParcel, 0.5, 0);
				if (!checked) {
					return false;
				}
			
			}
		



			if (!checked) {
				return false;
			}

		}

		return true;

	}
}
