package fr.ign.cogit.simplu3d.model.application;

import java.util.ArrayList;
import java.util.List;

import org.citygml4j.model.citygml.landuse.LandUse;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.sig3d.model.citygml.landuse.CG_LandUse;

public class Zone extends CG_LandUse {

  public final String CLASSE = "Zone";

  private List<Document> documents = new ArrayList<Document>();
  private IFeatureCollection<SousParcelle> sousParcelles = new FT_FeatureCollection<SousParcelle>();
  private String type = "";
  

  public Zone(IMultiSurface<IOrientableSurface> iMS) {
    super();
    
    this.setLod2MultiSurface(iMS);
    this.setGeom(iMS);

    this.setClazz(CLASSE);

  }

  public Zone(LandUse build) {
    super(build);
    

    this.setClazz(CLASSE);
  }

  public List<Document> getDocuments() {
    return documents;
  }

  public void setDocuments(List<Document> documents) {
    this.documents = documents;
  }

  public IFeatureCollection<SousParcelle> getSousParcelles() {
    return sousParcelles;
  }

  public void setSousParcelles(IFeatureCollection<SousParcelle> sousParcelles) {
    this.sousParcelles = sousParcelles;
  }


  
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  

}
