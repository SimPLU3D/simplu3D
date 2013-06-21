package fr.ign.cogit.simplu3d.model.application;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.feature.DefaultFeature;

public class BasicPropertyUnit extends DefaultFeature {
  
  public List<Building> buildings = new ArrayList<Building>();
  public List<CadastralParcel> cadastralParcel = new ArrayList<>();
  public List<Building> getBuildings() {
    return buildings;
  }
  public void setBuildings(List<Building> buildings) {
    this.buildings = buildings;
  }
  public List<CadastralParcel> getCadastralParcel() {
    return cadastralParcel;
  }
  public void setCadastralParcel(List<CadastralParcel> cadastralParcel) {
    this.cadastralParcel = cadastralParcel;
  }
  
  
  
  

}
