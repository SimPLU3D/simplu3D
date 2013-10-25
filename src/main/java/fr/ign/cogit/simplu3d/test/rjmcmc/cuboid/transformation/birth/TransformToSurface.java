package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation.birth;

import java.util.List;

import org.apache.log4j.Logger;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Face;
import fr.ign.cogit.geoxygene.contrib.delaunay.TriangulationJTS;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.distribution.EquiSurfaceDistribution;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.sig3d.topology.TriangulationLoader;
import fr.ign.cogit.simplu3d.distribution.DistributionAssesment;
import fr.ign.rjmcmc.kernel.Transform;

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
    
    //On prépare la géométrie pour être triangulée
    prepareGeometry(geom);
    
    this.mat = new double[d.length];
    this.delta = new double[d.length];
    this.inv = new double[d.length];
    this.determinant = 1.;
    for (int i = 0; i < d.length; ++i) {
      double dvalue = d[i];
      determinant *= dvalue;
      mat[i] = dvalue;
      inv[i] = 1 / dvalue;
      delta[i] = v[i];
    }
    this.absDeterminant = Math.abs(determinant);
  }

  private EquiSurfaceDistribution eq;

  private void prepareGeometry(IGeometry geom) {
    List<IOrientableSurface> lOS = FromGeomToSurface.convertGeom(geom);

    TriangulationJTS triangulation = TriangulationLoader
        .generate((IPolygon) lOS.get(0));
    
    


    try {
      triangulation.triangule();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    IMultiSurface<IOrientableSurface> iMS = new GM_MultiSurface<>();

    for (Face f : triangulation.getPopFaces()) {

      if (lOS.get(0).buffer(0.5).contains(f.getGeom())) {
        
        iMS.add(f.getGeometrie());


      }

    }
//DistributionAssesment.featCD.add(new DefaultFeature(iMS));
    
    eq = new EquiSurfaceDistribution(iMS);
  }

  public double getDeterminant() {
    return this.determinant;
  }

  @Override
  public double getAbsJacobian() {
    return this.absDeterminant;
  }

  @Override
  public double getAbsJacobian(double[] v) {
    return this.absDeterminant;
  }

  /*
   * public double[] getDelta() { return this.delta; }
   * 
   * public double[] getMat() { return this.mat; }
   * 
   * public double[] getInv() { return this.inv; }
   */

  @Override
  public void apply(double[] in, double[] out) {
    
    IDirectPosition dp = eq.sample(in[0],in[1]);
    
    out[0] = dp.getX();
    out[1] = dp.getY();
  
    
    
    lastx =  out[0];
    lasty =  out[1];
    lastpx = in[0];
    lastpx = in[1];
    
    
    for (int i = 2; i < out.length; i++) {
      out[i] = in[i] * mat[i] + delta[i];
    }
    // const T *m = m_mat, *d = m_delta;
    // for(T *oit = out;oit<out+N;++oit) *oit = (*in++)*(*m++) + (*d++);
  }
  
  
  double lastx, lasty;
  double lastpx, lastpy;
  
  
  
  

  @Override
  public void inverse(double[] in, double[] out) {
    
    out[0] = lastpx;
    out[1] = lastpy;
    
    if(in[0] != lastx){
      System.out.println("Error system");
    }
    
    
    for (int i = 2; i < out.length; i++) {
      out[i] = (in[i] - delta[i]) * inv[i];
    }
    // const T *m = m_inv, *d = m_delta;
    // for(T *oit = out;oit<out+N;++oit) *oit = ((*in++) - (*d++))*(*m++);
  }

  @Override
  public int dimension() {
    return this.mat.length;
  }
}
