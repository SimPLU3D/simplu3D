package fr.ign.cogit.simplu3d.indicator;

import fr.ign.cogit.simplu3d.calculation.HauteurCalculation;
import fr.ign.cogit.simplu3d.calculation.util.PointBasType;
import fr.ign.cogit.simplu3d.model.application._AbstractBuilding;



public class ShapeFactor2  {

  private double value;

  public ShapeFactor2(_AbstractBuilding b) {
    
    

    double h1 = HauteurCalculation.calculate(b,
        PointBasType.PLUS_BAS_BATIMENT,
        HauteurCalculation.POINT_HAUT_TYPE.PLUS_HAUT_FAITAGE);
    


    double area = b.getFootprint().area();
    
    
    
    value = Math.pow(h1, 2) / area;

  }


  public Double getValue() {
    // TODO Auto-generated method stub
    return value;
  }



}
