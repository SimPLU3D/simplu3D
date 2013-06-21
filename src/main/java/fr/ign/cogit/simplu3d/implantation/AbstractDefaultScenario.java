package fr.ign.cogit.simplu3d.implantation;

import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;

public abstract class AbstractDefaultScenario implements IScenario {

  private BasicPropertyUnit bPU;

  public AbstractDefaultScenario(BasicPropertyUnit bPU) {
    this.bPU = bPU;
  }

  public BasicPropertyUnit getbPU() {
    return bPU;
  }

  public void setbPU(BasicPropertyUnit bPU) {
    this.bPU = bPU;
  }

}
