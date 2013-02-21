package fr.ign.cogit.model.regle.consequences.batiment.hauteur;

import fr.ign.cogit.model.regle.consequences.Consequence;

public class ContrainteHMax extends Consequence{
  
  private double hauteurMax;
  
  

  public ContrainteHMax(double hauteurMax) {
    super();
    this.hauteurMax = hauteurMax;
  }



  public double getHauteurMax() {
    return hauteurMax;
  }



  public void setHauteurMax(double hauteurMax) {
    this.hauteurMax = hauteurMax;
  }



  @Override
  public String toString() {
   
    return "Hauteur maximale tolérée : " + getHauteurMax();
  }
  
  

}
