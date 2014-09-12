package fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.birth;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Face;
import fr.ign.cogit.geoxygene.contrib.delaunay.TriangulationJTS;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.distribution.EquiSurfaceDistribution;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.sig3d.topology.TriangulationLoader;
import fr.ign.rjmcmc.kernel.Transform;


/**
 * TODO : supprimer les deux constructeurs
 * @author MBrasebin
 *
 */
public class TransformToSurface implements Transform {
  /**
   * Logger.
   */
  static Logger LOGGER = Logger.getLogger(TransformToSurface.class.getName());

  private double delta[];
  private double mat[];
  private double inv[];
  private double determinant;
  private double absDeterminant;
  
  public TransformToSurface(Vector<Double> d, Vector<Double> v, IGeometry geom) {

    // On prépare la géométrie pour être triangulée
    prepareGeometry(geom);
    this.mat = new double[d.size()];
    this.delta = new double[d.size()];
    this.inv = new double[d.size()];
    this.determinant = 1.;
    for (int i = 2; i < d.size(); ++i) {
      double dvalue =d.get(i);
      determinant *= dvalue;
      mat[i] = dvalue;
      inv[i] = 1 / dvalue;
      delta[i] = v.get(i);
    }
    this.absDeterminant = 1;// Math.abs(determinant);
  }
  
  public TransformToSurface(double[] d, double[] v, IGeometry geom) {
    // On prépare la géométrie pour être triangulée
    prepareGeometry(geom);
    this.mat = new double[d.length];
    this.delta = new double[d.length];
    this.inv = new double[d.length];
    this.determinant = 1.;
    for (int i = 2; i < d.length; ++i) {
      double dvalue = d[i];
      determinant *= dvalue;
      mat[i] = dvalue;
      inv[i] = 1 / dvalue;
      delta[i] = v[i];
    }
    this.absDeterminant = 1;// Math.abs(determinant);
  }

  private EquiSurfaceDistribution eq;

  private void prepareGeometry(IGeometry geom) {
    List<IOrientableSurface> lOS = FromGeomToSurface.convertGeom(geom);
    TriangulationJTS triangulation = TriangulationLoader
        .generate((IPolygon) lOS.get(0));
    try {
      triangulation.triangule();
    } catch (Exception e) {
      e.printStackTrace();
    }
    IMultiSurface<IOrientableSurface> iMS = new GM_MultiSurface<>();
    for (Face f : triangulation.getPopFaces()) {
      if (lOS.get(0).buffer(0.5).contains(f.getGeom())) {
        iMS.add(f.getGeometrie());
      }
    }
    // DistributionAssesment.featCD.add(new DefaultFeature(iMS));
    eq = new EquiSurfaceDistribution(iMS);
  }

  public double getDeterminant() {
    return this.determinant;
  }

  @Override
  public double apply(boolean direct, Vector<Double> val0, Vector<Double> var0,
      Vector<Double> val1, Vector<Double> var1) {
    if (direct) {
      IDirectPosition dp = eq.sample(var0.get(0), var0.get(1));
      if (dp == null) {
        val1.set(0, 0.);
        val1.set(1, 0.);
      } else {
        val1.set(0, dp.getX());
        val1.set(1, dp.getY());
      }
      for (int i = 2; i < val1.size(); i++) {
        val1.set(i, val0.get(i) * mat[i] + delta[i]);
      }
      return 1;
    } else {
      IDirectPosition dp = eq.inversample(var1.get(0), var1.get(1));
      if (dp == null) {
        val1.set(0, 0.);
        val1.set(1, 0.);
      } else {
        val1.set(0, dp.getX());
        val1.set(1, dp.getY());
      }
      for (int i = 2; i < val1.size(); i++) {
        val1.set(i, (val0.get(i) - delta[i]) * inv[i]);
      }
      return 1;
    }
  }

  @Override
  public double getAbsJacobian(boolean direct) {
    if (direct)
      return this.absDeterminant;
    return 1 / this.absDeterminant;
  }

  @Override
  public int dimension(int n0, int n1) {
    return this.mat.length;
  }
}
