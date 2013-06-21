package fr.ign.cogit.simplu3d.model.application;

import tudresden.ocl20.pivot.model.IModel;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.sig3d.model.citygml.core.CG_CityModel;
import fr.ign.cogit.sig3d.semantic.MNTAire;
import fr.ign.cogit.simplu3d.importer.model.ImportModelInstance;

/**
 * 
 * @author MBrasebin
 * 
 */
public class Environnement extends CG_CityModel {

  
  
  
  
  public PLU plu;
  
  
  //On charge le modèle
  public static IModel model = ImportModelInstance
      .getModel("target/classes/fr/ign/cogit/simplu3d/importer/model/ModelProviderClass.class");

  
  
  


  public IFeatureCollection<CadastralParcel> cadastralParcels = new FT_FeatureCollection<CadastralParcel>();
  public IFeatureCollection<SubParcel> subParcels = new FT_FeatureCollection<SubParcel>();
  public IFeatureCollection<AbstractBuilding> buildings = new FT_FeatureCollection<AbstractBuilding>();
  public IFeatureCollection<UrbaZone> urbaZones = new FT_FeatureCollection<UrbaZone>();
  public IFeatureCollection<Alignement> alignements = new FT_FeatureCollection<Alignement>();
  public IFeatureCollection<BasicPropertyUnit> bpU = new FT_FeatureCollection<BasicPropertyUnit>();
  
  


  public MNTAire terrain;
  public IFeatureCollection<Road> roads = new FT_FeatureCollection<Road>();

  public static double DEFAULT_ZERO_Z = 139; // 41; //138

  public static IDirectPosition dpTranslate = null;

  public static boolean VERBOSE = false;
  public static boolean TRANSLATE_TO_ZERO = false;

  public IFeatureCollection<CadastralParcel> getParcelles() {
    return cadastralParcels;
  }

  public void setParcelles(IFeatureCollection<CadastralParcel> parcelles) {
    this.cadastralParcels = parcelles;
  }

  public Environnement() {

  }

  public IFeatureCollection<CadastralParcel> getCadastralParcels() {
    return cadastralParcels;
  }

  public void setCadastralParcels(
      IFeatureCollection<CadastralParcel> cadastralParcels) {
    this.cadastralParcels = cadastralParcels;
  }

  public IFeatureCollection<SubParcel> getSubParcels() {
    return subParcels;
  }

  public void setSubParcels(IFeatureCollection<SubParcel> subParcels) {
    this.subParcels = subParcels;
  }

  public IFeatureCollection<AbstractBuilding> getBuildings() {
    return buildings;
  }

  public void setBuildings(IFeatureCollection<AbstractBuilding> buildings) {
    this.buildings = buildings;
  }

  public IFeatureCollection<UrbaZone> getUrbaZones() {
    return urbaZones;
  }

  public void setUrbaZones(IFeatureCollection<UrbaZone> urbaZones) {
    this.urbaZones = urbaZones;
  }

  public IFeatureCollection<Alignement> getAlignements() {
    return alignements;
  }

  public void setAlignements(IFeatureCollection<Alignement> alignements) {
    this.alignements = alignements;
  }

  public MNTAire getTerrain() {
    return terrain;
  }

  public void setTerrain(MNTAire terrain) {
    this.terrain = terrain;
  }

  public static double getDEFAULT_ZERO_Z() {
    return DEFAULT_ZERO_Z;
  }

  public static void setDEFAULT_ZERO_Z(double dEFAULT_ZERO_Z) {
    DEFAULT_ZERO_Z = dEFAULT_ZERO_Z;
  }

  public static IDirectPosition getDpTranslate() {
    return dpTranslate;
  }

  public static void setDpTranslate(IDirectPosition dpTranslate) {
    Environnement.dpTranslate = dpTranslate;
  }

  public static boolean isVERBOSE() {
    return VERBOSE;
  }

  public static void setVERBOSE(boolean vERBOSE) {
    VERBOSE = vERBOSE;
  }

  public static boolean isTRANSLATE_TO_ZERO() {
    return TRANSLATE_TO_ZERO;
  }

  public static void setTRANSLATE_TO_ZERO(boolean tRANSLATE_TO_ZERO) {
    TRANSLATE_TO_ZERO = tRANSLATE_TO_ZERO;
  }

  public IFeatureCollection<Road> getRoads() {
    return roads;
  }

  public void setRoads(IFeatureCollection<Road> roads) {
    this.roads = roads;
  }
  
  public IFeatureCollection<BasicPropertyUnit> getBpU() {
    return bpU;
  }

  public void setBpU(IFeatureCollection<BasicPropertyUnit> bpU) {
    this.bpU = bpU;
  }
  
  public PLU getPlu() {
    return plu;
  }

  public void setPlu(PLU plu) {
    this.plu = plu;
  }

}
