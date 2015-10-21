package fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.birth;

import java.util.List;

import org.apache.log4j.Logger;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Face;
import fr.ign.cogit.geoxygene.contrib.delaunay.TriangulationJTS;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.distribution.EquiSurfaceDistributionJTS;
import fr.ign.cogit.geoxygene.sig3d.topology.TriangulationLoader;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.rjmcmc.kernel.Transform;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * @TODO Suppress constructor
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
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

  //
  // public TransformToSurface(double[] d, double[] v, IGeometry geom) {
  // // On prépare la géométrie pour être triangulée
  // prepareGeometry(geom);
  // this.mat = new double[d.length];
  // this.delta = new double[d.length];
  // this.inv = new double[d.length];
  // this.determinant = 1.;
  // for (int i = 2; i < d.length; ++i) {
  // double dvalue = d[i];
  // determinant *= dvalue;
  // mat[i] = dvalue;
  // inv[i] = 1 / dvalue;
  // delta[i] = v[i];
  // }
  // this.absDeterminant = 1;// Math.abs(determinant);
  // }

  private EquiSurfaceDistributionJTS eq;

  private void prepareGeometry(IGeometry geom) {
    List<IOrientableSurface> lOS = FromGeomToSurface.convertGeom(geom);

    IMultiSurface<IOrientableSurface> iMS = new GM_MultiSurface<>();

    for (IOrientableSurface oS : lOS) {

      TriangulationJTS triangulation = TriangulationLoader
          .generate((IPolygon) oS);
      try {
        triangulation.triangule();
      } catch (Exception e) {
        e.printStackTrace();
      }

      for (Face f : triangulation.getPopFaces()) {
        if (oS.buffer(0.5).contains(f.getGeom())) {
          iMS.add(f.getGeometrie());
        }
      }

    }

    try {
		eq = new EquiSurfaceDistributionJTS(iMS);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }

  public double getDeterminant() {
    return this.determinant;
  }

  @Override
  public double apply(boolean direct, double[] val0, double[] val1) {
    if (direct) {
      IDirectPosition dp = eq.sample(val0[0], val0[1]);
      if (dp == null) {
        val1[0] = 0.;
        val1[1] = 0.;
      } else {
        val1[0] = dp.getX();
        val1[1] = dp.getY();
      }
      for (int i = 2; i < val1.length; i++) {
        val1[i] = val0[i] * mat[i] + delta[i];
      }
      return 1;
    } else {
      IDirectPosition dp = eq.inversample(val0[0], val0[1]);
      if (dp == null) {
        val1[0] = 0.;
        val1[1] = 0.;
      } else {
        val1[0] = dp.getX();
        val1[1] = dp.getY();
      }
      for (int i = 2; i < val1.length; i++) {
        val1[i] = (val0[i] - delta[i]) * inv[i];
      }
      return 1;
    }
  }

//  @Override
  public double getAbsJacobian(boolean direct) {
    if (direct)
      return this.absDeterminant;
    return 1 / this.absDeterminant;
  }

  @Override
  public int dimension() {
    return this.mat.length;
  }
}
