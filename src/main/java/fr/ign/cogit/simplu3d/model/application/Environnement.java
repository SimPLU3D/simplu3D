package fr.ign.cogit.simplu3d.model.application;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.sig3d.model.citygml.core.CG_CityModel;
import fr.ign.cogit.sig3d.semantic.MNTAire;

public class Environnement extends CG_CityModel {

  private IFeatureCollection<Parcelle> parcelles;
  private IFeatureCollection<SousParcelle> sousParcelles;
  private IFeatureCollection<Batiment> batiments;
  private IFeatureCollection<Zone> zones;
  private IFeatureCollection<Alignement> alignements;
  private MNTAire terrain;

  public MNTAire getTerrain() {
    return terrain;
  }

  public void setTerrain(MNTAire terrain) {
    this.terrain = terrain;
  }

  public IFeatureCollection<Alignement> getAlignements() {
    return alignements;
  }

  public void setAlignements(IFeatureCollection<Alignement> alignements) {
    this.alignements = alignements;
  }

  public static double DEFAULT_ZERO_Z = 139; // 41; //138

  public static IDirectPosition dpTranslate = null;

  public static boolean VERBOSE = false;
  public static boolean TRANSLATE_TO_ZERO = false;

  public IFeatureCollection<Parcelle> getParcelles() {
    return parcelles;
  }

  public void setParcelles(IFeatureCollection<Parcelle> parcelles) {
    this.parcelles = parcelles;
  }

  private IFeatureCollection<Voirie> voiries;

  public Environnement() {

  }

  public IFeatureCollection<SousParcelle> getSousParcelles() {
    if (sousParcelles == null) {
      sousParcelles = new FT_FeatureCollection<SousParcelle>();
    }
    return sousParcelles;
  }

  public void setSousParcelles(IFeatureCollection<SousParcelle> sousParcelles) {
    this.sousParcelles = sousParcelles;
  }

  public IFeatureCollection<Batiment> getBatiments() {
    
    if(batiments == null){
      batiments = new FT_FeatureCollection<Batiment>();
    }
    return batiments;
  }

  public void setBatiments(IFeatureCollection<Batiment> batiments) {
    this.batiments = batiments;
  }

  public IFeatureCollection<Zone> getZones() {
    return zones;
  }

  public void setZones(IFeatureCollection<Zone> zones) {
    this.zones = zones;
  }

  public IFeatureCollection<Voirie> getVoiries() {
    return voiries;
  }

  public void setVoiries(IFeatureCollection<Voirie> voiries) {
    this.voiries = voiries;
  }

}
