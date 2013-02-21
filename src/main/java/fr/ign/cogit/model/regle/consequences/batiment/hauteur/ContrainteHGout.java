package fr.ign.cogit.model.regle.consequences.batiment.hauteur;

import fr.ign.cogit.model.regle.consequences.Consequence;

public class ContrainteHGout extends Consequence{
  
  private double hauteurGouttiere;
  
  

  public ContrainteHGout(double hauteurGouttiere) {
    super();
    this.hauteurGouttiere = hauteurGouttiere;
  }



  public double getHauteurGouttiere() {
    return hauteurGouttiere;
  }



  public void setHauteurGouttiere(double hauteurGouttiere) {
    this.hauteurGouttiere = hauteurGouttiere;
  }



  @Override
  public String toString() {
    // TODO Auto-generated method stub
    return "Hauteur gouttière maximale tolérée : " + hauteurGouttiere;
  }
  
  

}
