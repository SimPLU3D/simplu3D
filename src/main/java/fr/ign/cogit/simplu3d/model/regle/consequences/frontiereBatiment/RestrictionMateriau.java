package fr.ign.cogit.simplu3d.model.regle.consequences.frontiereBatiment;

import java.util.List;

import fr.ign.cogit.simplu3d.model.application.Materiau;
import fr.ign.cogit.simplu3d.model.regle.consequences.Consequence;

public class RestrictionMateriau extends Consequence {
  
  
  private List<Materiau> materiauxToleres;

  public List<Materiau> getMateriauxToleres() {
    return materiauxToleres;
  }

  public void setMateriauxToleres(List<Materiau> materiauxToleres) {
    this.materiauxToleres = materiauxToleres;
  }

  public RestrictionMateriau(List<Materiau> materiauxToleres) {
    super();
    this.materiauxToleres = materiauxToleres;
  }

  @Override
  public String toString() {
    
    StringBuffer sb = new StringBuffer();
    sb.append("Les métériaux autorisés sont :");
    
    for(Materiau m : this.getMateriauxToleres()){
      sb.append(m.toString());
      sb.append("\n");
    }
    
    
    return sb.toString();
  }
  

}
