package fr.ign.cogit.simplu3d.model.regle.consequences.batiment;

import fr.ign.cogit.simplu3d.model.application.Batiment;
import fr.ign.cogit.simplu3d.model.regle.consequences.Consequence;
import fr.ign.cogit.simplu3d.representation.regle.Incoherence;
import fr.ign.cogit.simplu3d.representation.regle.batiment.InterdictionRepresentation;

public class Interdiction extends Consequence{
  
  
  public Interdiction(){
    super();
  }

  @Override
  public String toString() {
    // TODO Auto-generated method stub
    return " Interdiction de bâtir  ";
  }
  
  
  
  
  
  
  
  
  
  public Incoherence generateIncoherence(Batiment b){
    
    
    return new InterdictionRepresentation(this, b);
    
  }
    
  
  
  
  

}
