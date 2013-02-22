package fr.ign.cogit.simplu3d.model.regle.consequences.batiment.hauteur;

import fr.ign.cogit.simplu3d.model.regle.consequences.Consequence;

public class ContrainteNBEtage extends Consequence {
  
  
  public int nbEtages;

  public ContrainteNBEtage(int nbEtages) {
    super();
    this.nbEtages = nbEtages;
  }

  public int getNbEtages() {
    return nbEtages;
  }

  public void setNbEtages(int nbEtages) {
    this.nbEtages = nbEtages;
  }

  @Override
  public String toString() {
    
    return "Nombre maximum d'étages : " + nbEtages;
  }
  
  

}
