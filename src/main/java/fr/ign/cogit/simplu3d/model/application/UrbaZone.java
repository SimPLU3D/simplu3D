package fr.ign.cogit.simplu3d.model.application;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.sig3d.model.citygml.landuse.CG_LandUse;

/**
 * 
 * @author MBrasebin
 * 
 */
public class UrbaZone extends CG_LandUse {

  public final String CLASSE = "Zone";

  private IFeatureCollection<SubParcel> subParcels = new FT_FeatureCollection<SubParcel>();
  private String name = "";

  public UrbaZone(IOrientableSurface geom) {
    super();

  }

  public IFeatureCollection<SubParcel> getSubParcels() {
    return subParcels;
  }

  public void setSubParcels(IFeatureCollection<SubParcel> subParcels) {
    this.subParcels = subParcels;
  }

  public String getName() {
    return name;
  }

  public void setType(String name) {
    this.name = name;
  }

}
