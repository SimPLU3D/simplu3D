package fr.ign.cogit.simplu3d.implantation.method;

import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.scenario.IScenario;

public interface IImplantation {
  
  public boolean newStep();
    
  public double getCurrentSatisfaction();
    
  public AbstractBuilding getBestBuilding();
  
  public IScenario getScenario();
 
}
