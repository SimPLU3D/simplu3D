package fr.ign.cogit.simplu3d.calculation.fuzzy;

import java.math.BigDecimal;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.simplu3d.fuzzy.FuzzyDouble;

public class Calculation2D {
  
  
  
  
  
  public static FuzzyDouble area(IGeometry geom){
   
    double value = geom.area();
    
    

   
    return new FuzzyDouble(value,0);
    
    
   
   
  }
  
  /*
  
  public static double area(IGeometry geom){
   
    return geom.area();
   
 
   
  }*/

}
