package fr.ign.cogit.simplu3d.implantation.method;

import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.scenario.IScenario;

public interface IImplantation {
  
  public boolean newStep();
    
  public double getCurrentSatisfaction();
    
  public Building getBuilding();
  
  public IScenario getScenario();
 
}
