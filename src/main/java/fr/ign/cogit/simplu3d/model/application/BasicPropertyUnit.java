package fr.ign.cogit.simplu3d.model.application;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.sig3d.convert.geom.FromGeomToSurface;

public class BasicPropertyUnit extends DefaultFeature {
  
  public List<Building> buildings = new ArrayList<Building>();
  public List<CadastralParcel> cadastralParcel = new ArrayList<CadastralParcel>();
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
  
  public IMultiSurface<IOrientableSurface> generateGeom(){
    IMultiSurface<IOrientableSurface>  geom = new GM_MultiSurface<IOrientableSurface>();
    
    
    for (CadastralParcel cP : this.getCadastralParcel()) {
      geom.addAll(FromGeomToSurface.convertGeom(cP.getGeom()));
    }
    return geom;
  }
  
  

}
