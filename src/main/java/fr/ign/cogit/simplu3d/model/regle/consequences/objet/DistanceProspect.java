package fr.ign.cogit.simplu3d.model.regle.consequences.objet;

import fr.ign.cogit.simplu3d.model.regle.consequences.Consequence;

public class DistanceProspect extends Consequence {

  private double hauteurIni, alpha;

  public DistanceProspect(double hauteurIni, double alpha) {
    super();
    this.hauteurIni = hauteurIni;
   
    this.alpha = alpha;
    
  }

  @Override
  public String toString() {
    
    return  "Prospect avec hauteur initiale  " +  hauteurIni +" et de pente " + alpha;
  }

  public void setHauteurIni(double hauteurIni) {
    this.hauteurIni = hauteurIni;
  }

  public double getHauteurIni() {
    return hauteurIni;
  }

  public void setAlpha(double alpha) {
    this.alpha = alpha;
  }

  public double getAlpha() {
    return alpha;
  }

}
