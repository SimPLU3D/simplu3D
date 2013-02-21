package fr.ign.cogit.model.regle.consequences.batiment;

import fr.ign.cogit.model.application.Batiment;
import fr.ign.cogit.model.regle.consequences.Consequence;
import fr.ign.cogit.representation.regle.Incoherence;
import fr.ign.cogit.representation.regle.batiment.InterdictionRepresentation;

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
