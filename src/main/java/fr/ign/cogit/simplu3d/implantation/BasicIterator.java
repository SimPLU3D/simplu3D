package fr.ign.cogit.simplu3d.implantation;

import fr.ign.cogit.simplu3d.implantation.method.IImplantation;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;

public class BasicIterator {

  private IImplantation impl;
  private int nbIteration;

  public BasicIterator(IImplantation impl, int nbIteration) {

    this.impl = impl;
    this.nbIteration = nbIteration;

  }

  public AbstractBuilding getFinalBuilding() {
    
    
    long t = System.currentTimeMillis();

    for (int i = 0; i < nbIteration; i++) {
      
      if(i ==1){
        t = System.currentTimeMillis();
      }

      if (i % 1000 == 0) {
        System.out.println("Step : " + i);
      }

      boolean isOk = impl.newStep();

      if (isOk) {
        System.out.println("Bâtiment placé - Satisfaction : "
            + impl.getCurrentSatisfaction());
      }

    }

    
    System.out.println("Temps en ms " + (System.currentTimeMillis() - t));
    
    impl.getScenario().end();
    
    

    return impl.getBestBuilding();
  }

}
