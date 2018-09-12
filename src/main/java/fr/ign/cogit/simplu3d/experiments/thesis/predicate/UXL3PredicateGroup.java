package fr.ign.cogit.simplu3d.experiments.thesis.predicate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.util.CuboidGroupCreation;
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
public class UXL3PredicateGroup<O extends Cuboid, C extends AbstractGraphConfiguration<O, C, M>, M extends AbstractBirthDeathModification<O, C, M>>
		implements ConfigurationModificationPredicate<C, M> {

	IMultiCurve<IOrientableCurve> curveS;
	int numberMaxOfBoxesInGroup;

	public UXL3PredicateGroup(BasicPropertyUnit bPU, int numberMaxOfBoxesInGroup) {

		this.numberMaxOfBoxesInGroup = numberMaxOfBoxesInGroup;

		List<IOrientableCurve> lCurve = new ArrayList<>();

		for (CadastralParcel cP : bPU.getCadastralParcels()) {
			// for (SubParcel sB : cP.getSubParcel()) {
			for (ParcelBoundary sCB : cP.getBoundaries()) {

				if (sCB.getType() != ParcelBoundaryType.INTRA) {
					IGeometry geom = sCB.getGeom();

					if (geom instanceof IOrientableCurve) {
						lCurve.add((IOrientableCurve) geom);

					} else {
						System.out.println("Classe UXL3 : quelque chose n'est pas un ICurve");
					}

					// }

				}

			}

		}

		curveS = new GM_MultiCurve<>(lCurve);

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

			// Pas vérifié ?

			boolean checked = true;

			checked = ab.prospect(curveS, 0.5, 0);
			if (!checked) {
				return false;
			}

			checked = (ab.getFootprint().distance(curveS) > 5);
			if (!checked) {
				return false;
			}

		}

		List<O> lBatIni = new ArrayList<>();

		Iterator<O> iTBat = c.iterator();

		while (iTBat.hasNext()) {

			O batTemp = iTBat.next();

			if (batTemp == batDeath) {
				continue;
			}

			lBatIni.add(batTemp);

		}

		for (O ab : lO) {

			lBatIni.add(ab);

		}

	    CuboidGroupCreation<AbstractSimpleBuilding> cGR = new CuboidGroupCreation<AbstractSimpleBuilding>();

		List<List<AbstractSimpleBuilding>> groupes = cGR.createGroup(lBatIni, 0.5);

		int nbElem = groupes.size();

		for (int i = 0; i < nbElem; i++) {

			if (groupes.get(i).size() > numberMaxOfBoxesInGroup) {
				return false;
			}

			for (int j = i + 1; j < nbElem; j++) {

				if (compareGroup(groupes.get(i), groupes.get(j)) < 5) {
					return false;
				}

			}
		}

		return true;

	}

	private double compareGroup(List<AbstractSimpleBuilding> l1, List<AbstractSimpleBuilding> l2) {

		double min = Double.POSITIVE_INFINITY;

		for (AbstractSimpleBuilding o1 : l1) {
			for (AbstractSimpleBuilding o2 : l2) {

				min = Math.min(o1.getFootprint().distance(o2.getFootprint()), min);

			}
		}
		// System.out.println(min);
		return min;

	}

}
