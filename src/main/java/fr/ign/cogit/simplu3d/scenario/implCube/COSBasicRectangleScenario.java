package fr.ign.cogit.simplu3d.scenario.implCube;

import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;

public class COSBasicRectangleScenario extends BasicRectangleScenario {

  public COSBasicRectangleScenario(BasicPropertyUnit bPU, double largMin,
      double largMax, double longMin, double longMax, double hMin, double hMax) {
    super(bPU, largMin, largMax, longMin, longMax, hMin, hMax);
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
