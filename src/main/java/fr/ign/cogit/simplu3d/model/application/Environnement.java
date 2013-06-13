package fr.ign.cogit.simplu3d.model.application;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.sig3d.model.citygml.core.CG_CityModel;
import fr.ign.cogit.sig3d.semantic.MNTAire;

public class Environnement extends CG_CityModel {

  private IFeatureCollection<CadastralParcel> parcelles;
  private IFeatureCollection<SubParcel> sousParcelles;
  private IFeatureCollection<Building> batiments;
  private IFeatureCollection<UrbaZone> zones;
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

  public IFeatureCollection<CadastralParcel> getParcelles() {
    return parcelles;
  }

  public void setParcelles(IFeatureCollection<CadastralParcel> parcelles) {
    this.parcelles = parcelles;
  }

  private IFeatureCollection<Road> voiries;

  public Environnement() {

  }

  public IFeatureCollection<SubParcel> getSousParcelles() {
    if (sousParcelles == null) {
      sousParcelles = new FT_FeatureCollection<SubParcel>();
    }
    return sousParcelles;
  }

  public void setSousParcelles(IFeatureCollection<SubParcel> sousParcelles) {
    this.sousParcelles = sousParcelles;
  }

  public IFeatureCollection<Building> getBuilding() {
    
    if(batiments == null){
      batiments = new FT_FeatureCollection<Building>();
    }
    return batiments;
  }

  public void setBatiments(IFeatureCollection<Building> batiments) {
    this.batiments = batiments;
  }

  public IFeatureCollection<UrbaZone> getZones() {
    return zones;
  }

  public void setZones(IFeatureCollection<UrbaZone> zones) {
    this.zones = zones;
  }

  public IFeatureCollection<Road> getVoiries() {
    return voiries;
  }

  public void setVoiries(IFeatureCollection<Road> voiries) {
    this.voiries = voiries;
  }

}
