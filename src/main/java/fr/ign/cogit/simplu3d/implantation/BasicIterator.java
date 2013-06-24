package fr.ign.cogit.simplu3d.implantation;

import fr.ign.cogit.simplu3d.implantation.method.IImplantation;
import fr.ign.cogit.simplu3d.model.application.Building;

public class BasicIterator {

  private IImplantation impl;
  private int nbIteration;

  public BasicIterator(IImplantation impl, int nbIteration) {

    this.impl = impl;
    this.nbIteration = nbIteration;

  }

  public Building getFinalBuilding() {

    for (int i = 0; i < nbIteration; i++) {

      System.out.println("Step : " + i);
      boolean isOk = impl.newStep();

      if (!isOk) {
        System.out.println("Pas de bâtiment placé");
      }

    }

    impl.getScenario().end();

    return impl.getBuilding();
  }

}
