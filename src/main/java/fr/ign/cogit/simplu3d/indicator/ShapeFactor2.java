package fr.ign.cogit.simplu3d.indicator;

import fr.ign.cogit.sig3d.indicator.util.PointBasType;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;



public class ShapeFactor2  {

  private double value;

  public ShapeFactor2(AbstractBuilding b) {
    
    

    double h1 = HauteurCalculation.calculate(b,
        PointBasType.PLUS_BAS_BATIMENT,
       1);
    


    double area = b.getFootprint().area();
    
    
    
    value = Math.pow(h1, 2) / area;

  }


  public Double getValue() {
    // TODO Auto-generated method stub
    return value;
  }



}
