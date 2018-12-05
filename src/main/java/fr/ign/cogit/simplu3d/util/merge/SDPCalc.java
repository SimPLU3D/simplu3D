package fr.ign.cogit.simplu3d.util.merge;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.precision.GeometryPrecisionReducer;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion2DObject;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.loader.LoaderCuboid;
import fr.ign.cogit.simplu3d.util.CuboidGroupCreation;
import fr.ign.cogit.simplu3d.util.JTS;

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
	//	System.out.println("nb groupes formé " + lGroupes.size());
		for (List<AbstractSimpleBuilding> g : lGroupes)
			sdp += sdpGroup(g, true);
		return sdp;
	}

	public double processSurface(String shape) {
		List<Cuboid> lCuboid = LoaderCuboid.loadFromShapeFile(shape);
		return process(lCuboid);
	}

	public double processSurface(List<? extends AbstractSimpleBuilding> cubes) {
		double sdp = 0;
		CuboidGroupCreation<AbstractSimpleBuilding> cGC = new CuboidGroupCreation<AbstractSimpleBuilding>();
		List<List<AbstractSimpleBuilding>> lGroupes = cGC.createGroup(cubes, 0);
	//	System.out.println("nb groupes formé " + lGroupes.size());
		for (List<AbstractSimpleBuilding> g : lGroupes) {
			sdp += sdpGroup(g, false);
		}
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
		
		List<AbstractSimpleBuilding> lCurrentGroup = new ArrayList<>();
		lCurrentGroup.addAll(group);
		// We initial with the first element of the list
		AbstractSimpleBuilding cuboid = lCurrentGroup.remove(0);
		aCurrent.add(new GeomHeightPair(reducer.reduce(cuboid.toGeometry()), cuboid.height));

		// For each building
		for (AbstractSimpleBuilding b : lCurrentGroup) {

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

	//		System.out.println("aprec [" + aCurrent.size() + "] " + lCurrentGroup.size());

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
		// The in shapefile
		String shpeIn = "/home/mbrasebin/Documents/Donnees/IAUIDF/Resultats/ResultatChoisy/results_pchoisy/24/simul_24_true_no_demo_sampler.shp";

		// The out shapefile
		String shapeOut = "/tmp/tmp/sdp.shp";

		// Instanciating the object
		SDPCalc sd = new SDPCalc();

		// Calculating sdp and generating the merge geometry
		double sdp = sd.process(shpeIn);

		System.out.println("SDP :" + sdp);
		// Getting and adding the merged geometry to the collection
		IFeatureCollection<IFeature> featColl = new FT_FeatureCollection<>();

		List<List<GeomHeightPair>> llGeomPair = sd.getGeometryPairByGroup();

		int count = 0;

		for (List<GeomHeightPair> geomPairs : llGeomPair) {
			for (GeomHeightPair g : geomPairs) {

				IGeometry jtsGeom = JTS.fromJTS(g.geom);

				if (jtsGeom == null || jtsGeom.coord().isEmpty()) {
					continue;
				}

				IMultiSurface<IOrientableSurface> os = FromGeomToSurface.convertMSGeom(jtsGeom);

				for (IOrientableSurface osTemp : os) {
					if (osTemp.area() < 0.01) {
						continue;
					}
					IGeometry extruded = Extrusion2DObject.convertFromGeometry(osTemp, 0, g.height);

					IMultiSurface<IOrientableSurface> finalOs = FromGeomToSurface.convertMSGeom(extruded);

					IFeature feat = new DefaultFeature(finalOs);

					AttributeManager.addAttribute(feat, "ID", (count++), "Integer");
					AttributeManager.addAttribute(feat, "HAUTEUR", g.height, "Double");
					featColl.add(feat);

				}

			}

		}

		// Export the result
		ShapefileWriter.write(featColl, shapeOut);

	}
}
