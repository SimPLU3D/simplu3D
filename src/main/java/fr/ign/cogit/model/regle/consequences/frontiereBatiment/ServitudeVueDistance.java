package fr.ign.cogit.model.regle.consequences.frontiereBatiment;

import fr.ign.cogit.model.regle.consequences.Consequence;

public class ServitudeVueDistance extends Consequence{
  
  private double distance;
  
  
  public ServitudeVueDistance(double distance) {
    super();
    this.distance = distance;
  }

  public double getdistance() {
    return distance;
  }

  public void setdistance(double distance) {
    this.distance = distance;
  }

  @Override
  public String toString() {
    // TODO Auto-generated method stub
    return "Servitude de vue : distance " + distance;
  }
  
}