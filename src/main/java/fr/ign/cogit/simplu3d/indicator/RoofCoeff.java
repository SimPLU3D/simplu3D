package fr.ign.cogit.simplu3d.indicator;

import fr.ign.cogit.simplu3d.calculation.HauteurCalculation;
import fr.ign.cogit.simplu3d.calculation.util.PointBasType;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;

public class RoofCoeff {

  private double value = 0;

  public RoofCoeff(AbstractBuilding b) {

    double h1 = HauteurCalculation.calculate(b,
        PointBasType.PLUS_BAS_BATIMENT,
        HauteurCalculation.POINT_HAUT_TYPE.PLUS_HAUT_FAITAGE);
    double h2 = HauteurCalculation.calculate(b,
        PointBasType.PLUS_BAS_BATIMENT,
        HauteurCalculation.POINT_HAUT_TYPE.PLUS_HAUT_EGOUT);

    value = h1 / h2 - 1;

  }

  public Double getValue() {
    // TODO Auto-generated method stub
    return value;
  }

}
