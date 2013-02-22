package fr.ign.cogit.simplu3d.representation.regle;

import fr.ign.cogit.simplu3d.model.regle.consequences.Consequence;
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
