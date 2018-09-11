package fr.ign.cogit.simplu3d.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.operation.union.CascadedPolygonUnion;

import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;

public class CuboidGroupCreation {
	/**
	 * The create group function that separates cuboid into list of near cuboid
	 * 
	 * @param lBatIn
	 * @param connexionDistance : minimal distance to consider 2 boxes as connected
	 * @return
	 */
	public static List<List<AbstractSimpleBuilding>> createGroup(List<? extends AbstractSimpleBuilding> lBatIn,
			double connexionDistance) {

		List<List<AbstractSimpleBuilding>> listGroup = new ArrayList<>();

		while (!lBatIn.isEmpty()) {

			AbstractSimpleBuilding batIni = lBatIn.remove(0);

			List<AbstractSimpleBuilding> currentGroup = new ArrayList<>();
			currentGroup.add(batIni);

			int nbElem = lBatIn.size();

			bouclei: for (int i = 0; i < nbElem; i++) {

				for (AbstractSimpleBuilding batTemp : currentGroup) {

					if (lBatIn.get(i).getFootprint().distance(batTemp.getFootprint()) <= connexionDistance) {

						currentGroup.add(lBatIn.get(i));
						lBatIn.remove(i);
						i = -1;
						nbElem--;
						continue bouclei;

					}
				}

			}

			listGroup.add(currentGroup);
		}

		return listGroup;

	}

	private static long c = 0;

	/**
	 * Check the width of a group
	 * 
	 * @param lO
	 * @param widthBuffer
	 * @return
	 */
	public static boolean checkWidth(List<? extends AbstractSimpleBuilding> lO, double widthBuffer) {
		c = 0;
		if (lO.size() < 2)
			return true;
		Geometry union = getGroupGeom(lO);
		if (union == null)
			return false;
		// Récupérer le polygone sans le trou
		// will that do it ?
		// System.out.println(union.getClass());
		if (union instanceof Polygon) {

			union = union.buffer(5).buffer(-5);
		}
		boolean multi = false;
		if (union instanceof MultiPolygon) {
			// System.out.println("multi " + union);
			return false;

		}

		Geometry negativeBuffer = union.buffer(-widthBuffer);

		if (negativeBuffer.isEmpty() || negativeBuffer.getArea() < 0.001) {
			++c;
			if (c % 10000 == 0 || multi) {

			}
			return true;
		}
		// System.out.println("too big");
		// System.out.println(union);
		// System.out.println(negativeBuffer);
		// System.out.println("---------------------");
		return false;

	}

	/**
	 * Create the geometry from a group
	 * 
	 * @param g
	 * @return
	 */
	private static Geometry getGroupGeom(List<? extends AbstractSimpleBuilding> g) {
		Collection<Geometry> collGeom = new ArrayList<>();
		for (AbstractSimpleBuilding o : g) {
			collGeom.add(o.toGeometry()/* .buffer(0.4) */);
		}
		Geometry union = null;
		try {
			union = CascadedPolygonUnion.union(collGeom);
		} catch (Exception e) {
			return null;
		}
		/* union = TopologyPreservingSimplifier.simplify(union, 0.4); */
		return union;
	}


	public static boolean checkDistanceInterGroups(List<List<AbstractSimpleBuilding>> lGroupes, double distanceInterBati) {
		// si un seul groupe
		if (lGroupes.size() < 2)
			return true;
		// on va stocker les hauteurs pour pas les recalculer
		double[] heights = new double[lGroupes.size()];
		for (int i = 0; i < lGroupes.size(); ++i) {
			heights[i] = getGroupeHeight(lGroupes.get(i));
		}
		for (int i = 0; i < lGroupes.size() - 1; ++i) {
			for (int j = i + 1; j < lGroupes.size(); ++j) {
				double distanceGroupes = getGroupGeom(lGroupes.get(i)).distance(getGroupGeom(lGroupes.get(j)));
				double d = Math.max(Math.max(heights[i], heights[j]) * 0.5, distanceInterBati);
				// System.out.println("max(dist groupes, heights) : " + d
				// + "---- dit inter bati : " + distanceInterBati);
				if (distanceGroupes < d)
					return false;
			}
		}
		return true;
	}
	
	private static double getGroupeHeight(List<? extends AbstractSimpleBuilding> g) {
		double max = -1;
		for (AbstractBuilding b : g) {
			if (((AbstractSimpleBuilding) b).getHeight() > max)
				max = ((AbstractSimpleBuilding) b).getHeight();
		}
		return max;
	}

}
