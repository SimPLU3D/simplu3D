package fr.ign.cogit.simplu3d.implantation.method.impl;

import tudresden.ocl20.pivot.modelinstancetype.exception.TypeNotFoundInModelException;
import fr.ign.cogit.simplu3d.checker.FastRuleChecker;
import fr.ign.cogit.simplu3d.exec.GTRU3D;
import fr.ign.cogit.simplu3d.implantation.method.IImplantation;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.scenario.AbstractDefaultScenario;
import fr.ign.cogit.simplu3d.scenario.IScenario;

public class RandomWalk implements IImplantation {

  protected AbstractDefaultScenario aDS;
  private FastRuleChecker fRC;

  public RandomWalk(AbstractDefaultScenario aDS) {

    this.aDS = aDS;
    fRC = new FastRuleChecker(aDS.getbPU());
  }

  private double currentSatisfaction = 0;
  private AbstractBuilding currentBuilding = null;

  @Override
  public boolean newStep() {

    Building b = aDS.newConfiguration();

    boolean keep = false;

    if (b == null) {
      return false;
    }

    // On ajoute le bâtiment pour tester la configuration
    aDS.getbPU().buildings.add(b);
    aDS.getbPU().getCadastralParcel().get(0).getSubParcel().get(0)
        .getBuildingsParts().add(b);

    if (!fRC.hasNewBuildingInstance()) {

      try {
        fRC.addBuilding(b);
      } catch (TypeNotFoundInModelException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }

    if (fRC.check()) {
      double satisf = aDS.satisfcation();

      if (GTRU3D.DEBUG) {
        GTRU3D.DEBUG_FEAT.add(b.clone());

      }

      if (satisf > currentSatisfaction) {
        currentBuilding = b.clone(); // A optimiser pour plus atard
        keep = true;
        currentSatisfaction = satisf;
      }

    }

    // On enlève le bâtiment pour tester d'autres configurations
    aDS.getbPU().buildings.remove(b);
    aDS.getbPU().getCadastralParcel().get(0).getSubParcel().get(0)
        .getBuildingsParts().remove(b);
    // TODO Auto-generated method stub
    return keep;
  }

  public double getCurrentSatisfaction() {
    return currentSatisfaction;
  }

  @Override
  public AbstractBuilding getBestBuilding() {
    return this.currentBuilding;
  }

  @Override
  public IScenario getScenario() {
    // TODO Auto-generated method stub
    return aDS;
  }
}
