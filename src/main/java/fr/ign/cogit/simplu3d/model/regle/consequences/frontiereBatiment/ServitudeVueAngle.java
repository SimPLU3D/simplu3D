package fr.ign.cogit.simplu3d.model.regle.consequences.frontiereBatiment;

import fr.ign.cogit.simplu3d.model.regle.consequences.Consequence;

public class ServitudeVueAngle extends Consequence{
  
  
  
  private double angle;
  
  

  public ServitudeVueAngle(double angle) {
    super();
    this.angle = angle;
  }

  public double getAngle() {
    return angle;
  }

  public void setAngle(double angle) {
    this.angle = angle;
  }

  @Override
  public String toString() {
    // TODO Auto-generated method stub
    return "Servitude de vue : angle " + angle;
  }
  
  
  
  
  
  
  
  
  

}
