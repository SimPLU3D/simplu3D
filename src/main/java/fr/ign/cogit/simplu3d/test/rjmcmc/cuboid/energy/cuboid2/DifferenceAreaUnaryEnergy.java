package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.energy.cuboid2;

import com.vividsolutions.jts.geom.Geometry;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.Cuboid;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.Cuboid2;
import fr.ign.geometry.Rectangle2D;
import fr.ign.rjmcmc.energy.UnaryEnergy;

public class DifferenceAreaUnaryEnergy<T> implements UnaryEnergy<T> {
  Geometry bpu;
  public DifferenceAreaUnaryEnergy(Geometry p) {
    this.bpu = p;
  }

  @Override
  public double getValue(T t) {

    
    
    
    try {
      Geometry difference = ((Cuboid2) t).toGeometry().difference(this.bpu);
      
      
      if(t instanceof Cuboid2){
        
        
        
        if(difference.getArea() > 0 ){
          return 999999999;
        }
        
        return 0; // Math.exp(difference.getArea() * ((Cuboid)t).height ) ;
        
        
        
      }
      return difference.getArea();
    } catch (Exception e) {
      System.out.println("G = " + ((Rectangle2D) t).toGeometry());
      System.out.println("BPU = " + bpu);
    }
    return 0;
  }

}
