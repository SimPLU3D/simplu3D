package fr.ign.cogit.simplu3d.model.regle.conditions;

import fr.ign.cogit.simplu3d.model.regle.ElementRegle;

public abstract class Condition implements ElementRegle {
  
  
  public abstract boolean isChecked();
  
  
  private String description;


  public String getDescription() {
    return description;
  }


  public void setDescription(String description) {
    this.description = description;
  }
  
    
  public String toString(){
    return getDescription();
  }

}
