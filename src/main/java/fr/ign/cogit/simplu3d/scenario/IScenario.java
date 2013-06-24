package fr.ign.cogit.simplu3d.scenario;

import fr.ign.cogit.simplu3d.model.application.Building;

public interface IScenario {

  public Building newConfiguration();

  public double satisfcation();
  
  public void end();

}
