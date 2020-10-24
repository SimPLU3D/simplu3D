package fr.ign.cogit.simplu3d.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.operation.union.CascadedPolygonUnion;

import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;

public class CuboidGroupCreation<C extends AbstractSimpleBuilding> {

	private List<C> connectedGroup(List<List<C>> listGroup, C cuboid, double connexionDistance) {
		for (List<C> group : listGroup) {
			for (C c : group) {
				if (c.getFootprint().distance(cuboid.getFootprint()) <= connexionDistance) return group;
			}
		}
		return null;
	}
	/**
	 * Create a group from cuboids
	 * 
	 * @param lCuboids          a list of cuboids
	 * @param connexionDistance a connexion distance to determine if a cuboid
	 *                          belongs to the group
	 * @return a list of group of connected cuboids (a group is a list)
	 */
	public List<List<C>> createGroup(List<? extends C> lCuboids, double connexionDistance) {
		List<List<C>> listGroup = new ArrayList<>();
		for (C cuboid : lCuboids) {
			List<C> group = connectedGroup(listGroup, cuboid, connexionDistance);
			if (group == null) {
				List<C> currentGroup = new ArrayList<>();
				currentGroup.add(cuboid);
				listGroup.add(currentGroup);
			} else {
				group.add(cuboid);
			}
		}
		return listGroup;
	}

	private static long c = 0;

	/**
	 * Indicate if the wdth of a list of cuboid is less than a width value
	 * 
	 * @param lO          the list of cuboids to test
	 * @param widthBuffer the width to test
	 * @return true if the width is less than the input value
	 */
	public boolean checkWidth(List<? extends C> lO, double widthBuffer) {
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
	private Geometry getGroupGeom(List<? extends C> g) {
		Collection<Geometry> collGeom = new ArrayList<>();
		for (C o : g) {
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

	public boolean checkDistanceInterGroups(List<List<C>> lGroupes, List<Double> distanceInterBati) {
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

				// If there is only one distance we use it or we use the max of the distance
				// constraints of the groups
				double distInterBatiCalculated = (distanceInterBati.size() == 1) ? distanceInterBati.get(0)
						: Math.min(distanceInterBati.get(i), distanceInterBati.get(j));

				double d = Math.max(Math.max(heights[i], heights[j]) * 0.5, distInterBatiCalculated);
				// System.out.println("max(dist groupes, heights) : " + d
				// + "---- dit inter bati : " + distanceInterBati);
				if (distanceGroupes < d)
					return false;
			}
		}
		return true;
	}

	private double getGroupeHeight(List<? extends C> g) {
		double max = -1;
		for (C b : g) {
			if ((b).getHeight() > max)
				max = ((C) b).getHeight();
		}
		return max;
	}

}
