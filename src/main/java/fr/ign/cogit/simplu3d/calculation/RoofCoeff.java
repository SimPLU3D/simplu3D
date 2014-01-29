package fr.ign.cogit.simplu3d.calculation;

import fr.ign.cogit.simplu3d.calculation.HauteurCalculation;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;

public class RoofCoeff {

  private double value = 0;

  public RoofCoeff(AbstractBuilding b) {

    double h1 = HauteurCalculation.calculate(b,
       0,
       1);
    double h2 = HauteurCalculation.calculate(b,
        0,
       0);

    value = h1 / h2 - 1;

  }

  public Double getValue() {
    // TODO Auto-generated method stub
    return value;
  }

}
