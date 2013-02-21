package fr.ign.cogit.representation.regle;

import fr.ign.cogit.model.regle.consequences.Consequence;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;

public class Incoherence extends DefaultFeature {
  
  private String s;
  
  public Incoherence(Consequence c){
    
    s = c.toString();
    
    
  }
  
  
  
  public String getDescription(){
    return s;
  }
  

}
