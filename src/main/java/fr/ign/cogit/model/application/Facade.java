package fr.ign.cogit.model.application;

import fr.ign.cogit.sig3d.model.citygml.building.CG_WallSurface;

public class Facade extends CG_WallSurface{
  
  public boolean isAveugle;
  
  
  public final static String FOND = "FOND";
  public final static String LATERAL = "LAT";
  public final static String VOIE = "VOIE";
  
  
  private String facadeType;
  private Materiau mat;
  
  
  
  public Materiau getMat() {
    return mat;
  }


  public void setMat(Materiau mat) {
    this.mat = mat;
  }


  public Facade(){
    super();
  }
  
  
  public Facade(String type, boolean isAveugle){
    super();
    this.facadeType = type;
    this.isAveugle = isAveugle;
  }


  public boolean isAveugle() {
    return isAveugle;
  }


  public void setAveugle(boolean isAveugle) {
    this.isAveugle = isAveugle;
  }


  public String getType() {
    return facadeType;
  }


  public void setType(String type) {
    this.facadeType = type;
  }
  
  
  

}
