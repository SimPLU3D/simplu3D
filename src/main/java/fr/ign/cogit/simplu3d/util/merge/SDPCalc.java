package fr.ign.cogit.simplu3d.util.merge;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.precision.GeometryPrecisionReducer;

import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.loader.LoaderCuboid;
import fr.ign.cogit.simplu3d.util.CuboidGroupCreation;

/**
 * Computes the "Surface de plancher" from a collection of cuboids, essentially
 * the ground surface x nb of floors. It does it by building a partition from
 * the intersections of the cuboids, associating the correct height to each
 * part, and finally summing them
 * 
 * @author imran
 *
 */
public class SDPCalc {

	private double FLOOR_HEIGHT = 3;

	public SDPCalc() {
	}

	public SDPCalc(double floorHeight) {
		this.FLOOR_HEIGHT = floorHeight;
	}

	/**
	 * 
	 * structure to combine a surface and its associated height
	 *
	 */
	public class GeomHeightPair {
		public double height;
		public Geometry geom;

		public GeomHeightPair(Geometry g, double h) {
			this.height = h;
			this.geom = g;
		}

		public double sdp() {
			double epsilon = 0.01;
			// if height is x.99 we want it to be x+1
			if (height - ((int) (height)) > (1 - epsilon)) {
				height = (int) (height) + 1;
			}
			return geom.getArea() * (Math.floor(height / FLOOR_HEIGHT));
		}

		public double surface() {
			return geom.getArea();
		}
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	public double process(String shape) {
		List<Cuboid> lCuboid = LoaderCuboid.loadFromShapeFile(shape);
		return process(lCuboid);
	}

	public double process(List<? extends AbstractSimpleBuilding> cubes) {
		double sdp = 0;
		CuboidGroupCreation<AbstractSimpleBuilding> cGC = new CuboidGroupCreation<AbstractSimpleBuilding>();
		List<List<AbstractSimpleBuilding>> lGroupes = cGC.createGroup(cubes, 0);
		System.out.println("nb groupes formé " + lGroupes.size());
		for (List<AbstractSimpleBuilding> g : lGroupes)
			sdp += sdpGroup(g, true);
		return sdp;
	}

	public double processSurface(String shape) {
		List<Cuboid> lCuboid = LoaderCuboid.loadFromShapeFile(shape);
		return process(lCuboid);
	}

	public double processSurface(List<Cuboid> cubes) {
		double sdp = 0;
		CuboidGroupCreation<Cuboid> cGC = new CuboidGroupCreation<Cuboid>();
		List<List<Cuboid>> lGroupes = cGC.createGroup(cubes, 0);
		System.out.println("nb groupes formé " + lGroupes.size());
		for (List<Cuboid> g : lGroupes)
			sdp += sdpGroup(g, false);
		return sdp;
	}

	GeometryPrecisionReducer reducer = new GeometryPrecisionReducer(new PrecisionModel(100000.0));

	/**
	 * If true = sdp if false = surface 2D
	 * 
	 * @param group
	 * @param sdp_or_surface
	 * @return
	 */
	private double sdpGroup(List<? extends AbstractSimpleBuilding> group, boolean sdp_or_surface) {
		// The list of already met pair
		List<GeomHeightPair> aCurrent = new ArrayList<>();

		// We initial with the first element of the list
		AbstractSimpleBuilding cuboid = group.remove(0);
		aCurrent.add(new GeomHeightPair(reducer.reduce(cuboid.toGeometry()), cuboid.height));

		// For each building
		for (AbstractSimpleBuilding b : group) {

			// We create a new pair
			GeomHeightPair bgeom = new GeomHeightPair(reducer.reduce(b.toGeometry()), b.height);

			int nbPair = aCurrent.size();

			List<GeomHeightPair> newGeometryPair = new ArrayList<>();
			// We process all the current pair
			for (int i = 0; i < nbPair; i++) {

				GeomHeightPair a = aCurrent.get(i);
				// If there is no intersection, we continue;
				Geometry intersection = a.geom.intersection(bgeom.geom);

				if (intersection == null || intersection.isEmpty()) {
					continue;
				}

				// There is an intersection, we remove from the list
				// As we will create new pair from it
				aCurrent.remove(i);
				i--;
				nbPair--;

				// We compute the difference
				Geometry diff = a.geom.difference(bgeom.geom);

				if (diff != null && !diff.isEmpty()) {
					// It it is not empty the difference of a is a new pair
					newGeometryPair.add(new GeomHeightPair(reducer.reduce(diff.buffer(0)), a.height));
				}

				// The intersection is a new pair with the max height
				newGeometryPair
						.add(new GeomHeightPair(reducer.reduce(intersection.buffer(0)), Math.max(a.height, b.height)));

				// If it exists the difference of b is a new pair with the
				// height
				Geometry diffB = bgeom.geom.difference(a.geom);
				if (diffB == null || diffB.isEmpty()) {
					bgeom = null;
					break;
				}
				bgeom = new GeomHeightPair(reducer.reduce(diffB.buffer(0)), b.height);
			}

			aCurrent.addAll(newGeometryPair);

			System.out.println("aprec [" + aCurrent.size() + "] " + group.size());

			if (bgeom != null) {
				aCurrent.add(bgeom);
			}

		}

		geometryPairByGroup.add(aCurrent);

		double sdp = 0;
		for (GeomHeightPair e : aCurrent) {
			// System.out.println("sdp partiel " + e.sdp());

			if (sdp_or_surface) {
				sdp += e.sdp();
			} else {
				sdp += e.surface();
			}

			// System.out.println(e.height + " -- " + e.geom);
		}
		return sdp;
	}

	private List<List<GeomHeightPair>> geometryPairByGroup = new ArrayList<>();

	public double getFLOOR_HEIGHT() {
		return FLOOR_HEIGHT;
	}

	public List<List<GeomHeightPair>> getGeometryPairByGroup() {
		return geometryPairByGroup;
	}

	public static void main(String[] args) {
		SDPCalc sd = new SDPCalc();

		String shpeIn = "/home/mickael/data/mbrasebin/donnees/IAUIDF/data_grille/results_77_test/77007650/simul_77007650_true_no_demo_sampler.shp";

		double sdp = sd.process(shpeIn);
		System.out.println("SDP :" + sdp);
	}
}
