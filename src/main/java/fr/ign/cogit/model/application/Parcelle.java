package fr.ign.cogit.model.application;

import org.citygml4j.model.citygml.landuse.LandUse;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.sig3d.model.citygml.landuse.CG_LandUse;

public class Parcelle extends CG_LandUse {


  public final String CLASSE = "Parcelle";
  
  private IFeatureCollection<SousParcelle> sousParcelles = new FT_FeatureCollection<SousParcelle>();
  private IFeatureCollection<Bordure> bordures = new FT_FeatureCollection<Bordure>();
  
  public Parcelle(){
    super();
  }
      
  
  public Parcelle(IMultiSurface<IOrientableSurface> iMS) {
    super();

    this.setLod2MultiSurface(iMS);
    this.setGeom(iMS);

    this.setClazz(CLASSE);

  }

  public IFeatureCollection<Bordure> getBordures() {
    return bordures;
  }


  public void setBordures(IFeatureCollection<Bordure> bordures) {
    this.bordures = bordures;
  }


  public Parcelle(LandUse landUse) {
    super(landUse);

    this.setClazz(CLASSE);

  }
  
  
  public IFeatureCollection<SousParcelle> getSousParcelles() {
    return sousParcelles;
  }

  public void setSousParcelles(IFeatureCollection<SousParcelle> sousParcelles) {
    this.sousParcelles = sousParcelles;
  }

}
