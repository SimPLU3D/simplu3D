package fr.ign.cogit.simplu3d.scenario.implLScenario;

import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;

public class COSBasicLScenario extends BasicLScenario {

  public COSBasicLScenario(BasicPropertyUnit bPU, double largMin,
      double largMax, double longMin, double longMax, double hMin, double hMax,
       double largMin2, double largMax2,
      double hMin2, double hMax2) {
    super(bPU, largMin, largMax, longMin, longMax, hMin, hMax,
        largMin2, largMax2, hMin2, hMax2);
  }

  @Override
  public double satisfcation() {

    double volBuilt = 0;

    for (AbstractBuilding b : this.getbPU().getBuildings()) {
      volBuilt = volBuilt + b.getFootprint().area() * b.height(1, 2);
    }

    return volBuilt;
  }

}
