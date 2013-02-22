package fr.ign.cogit.simplu3d.model.regle.consequences;

import fr.ign.cogit.simplu3d.model.regle.ElementRegle;

public abstract class Consequence implements ElementRegle {

  private String description;
  
  private static int ID_COUNT=0;
  
  
  public Consequence(){
    id = (++ID_COUNT);
  }
  
  
  protected int id;
    
  public int id(){
    return id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public abstract String toString();
}
