package fr.ign.cogit.simplu3d.model.application;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.feature.FT_Feature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;

public class UrbaZone extends FT_Feature {

  public final String CLASSE = "Zone";

  private IFeatureCollection<SubParcel> sousParcelles = new FT_FeatureCollection<SubParcel>();
  private String name = "";

  public UrbaZone(IOrientableSurface geom) {
    super();

  }

  public IFeatureCollection<SubParcel> getSousParcelles() {
    return sousParcelles;
  }

  public void setSousParcelles(IFeatureCollection<SubParcel> sousParcelles) {
    this.sousParcelles = sousParcelles;
  }

  public String getName() {
    return name;
  }

  public void setType(String name) {
    this.name = name;
  }

}
