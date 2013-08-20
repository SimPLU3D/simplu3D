package fr.ign.cogit.simplu3d.io.load.application;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.semantic.DTMArea;
import fr.ign.cogit.geoxygene.sig3d.util.ColorShade;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.simplu3d.importer.applicationClasses.AlignementImporter;
import fr.ign.cogit.simplu3d.importer.applicationClasses.AssignLinkToBordure;
import fr.ign.cogit.simplu3d.importer.applicationClasses.BasicPropertyUnitImporter;
import fr.ign.cogit.simplu3d.importer.applicationClasses.BuildingImporter;
import fr.ign.cogit.simplu3d.importer.applicationClasses.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.importer.applicationClasses.RoadImporter;
import fr.ign.cogit.simplu3d.importer.applicationClasses.RulesImporter;
import fr.ign.cogit.simplu3d.importer.applicationClasses.SubParcelImporter;
import fr.ign.cogit.simplu3d.importer.applicationClasses.ZonesImporter;
import fr.ign.cogit.simplu3d.model.application.Alignement;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.PLU;
import fr.ign.cogit.simplu3d.model.application.Road;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.model.application.UrbaZone;
import fr.ign.cogit.simplu3d.util.AssignZ;

public class LoaderSHP {

  /*
   * Nom des fichiers en entrée
   */
  public final static String NOM_FICHIER_ZONAGE = "zonage.shp";
  public final static String NOM_FICHIER_PARCELLE = "parcelle.shp";
  public final static String NOM_FICHIER_VOIRIE = "route.shp";
  public final static String NOM_FICHIER_BATIMENTS = "bati.shp";
  public final static String NOM_FICHIER_TERRAIN = "MNT_BD3D.asc";
  public final static String NOM_FICHIER_PRESC_LINEAIRE = "PRESCRIPTION_LIN.shp";

  public final static boolean SURSAMPLED = true;

  /*
   * Attributs du fichier zone
   */

  public static Environnement load(String folder)
      throws CloneNotSupportedException {
    Environnement env = Environnement.getInstance();
    env.folder = folder;

    // Chargement des fichiers

    IFeatureCollection<IFeature> zoneColl = ShapefileReader.read(folder
        + NOM_FICHIER_ZONAGE);
    IFeatureCollection<IFeature> parcelleColl = ShapefileReader.read(folder
        + NOM_FICHIER_PARCELLE);
    IFeatureCollection<IFeature> voirieColl = ShapefileReader.read(folder
        + NOM_FICHIER_VOIRIE);
    IFeatureCollection<IFeature> batiColl = ShapefileReader.read(folder
        + NOM_FICHIER_BATIMENTS);
    IFeatureCollection<IFeature> prescriptions = ShapefileReader.read(folder
        + NOM_FICHIER_PRESC_LINEAIRE);

    // Etape 0 : doit on translater tous les objets ?

    if (Environnement.TRANSLATE_TO_ZERO) {

      Environnement.dpTranslate = zoneColl.envelope().center();

      for (IFeature feat : zoneColl) {
        feat.setGeom(feat.getGeom().translate(
            -Environnement.dpTranslate.getX(),
            -Environnement.dpTranslate.getY(),
            
            0));

      }

      for (IFeature feat : parcelleColl) {
        feat.setGeom(feat.getGeom().translate(
            -Environnement.dpTranslate.getX(),
            -Environnement.dpTranslate.getY(), 0));

      }

      for (IFeature feat : voirieColl) {
        feat.setGeom(feat.getGeom().translate(
            -Environnement.dpTranslate.getX(),
            -Environnement.dpTranslate.getY(), 0));

      }

      for (IFeature feat : batiColl) {
        feat.setGeom(feat.getGeom().translate(
            -Environnement.dpTranslate.getX(),
            -Environnement.dpTranslate.getY(), 0));

      }

      for (IFeature feat : prescriptions) {
        feat.setGeom(feat.getGeom().translate(
            -Environnement.dpTranslate.getX(),
            -Environnement.dpTranslate.getY(), 0));

      }
    }

    // Etape 1 : création de l'objet PLU
    PLU plu = new PLU();
    env.setPlu(plu);

    // Etape 2 : création des zones et assignation des règles aux zones
    IFeatureCollection<UrbaZone> zones = ZonesImporter.importUrbaZone(zoneColl);

    for (UrbaZone z : zones) {

      RulesImporter.importer(folder, z);
    }

    // Etape 3 : assignement des zonages au PLU
    plu.lUrbaZone.addAll(zones);
    env.setUrbaZones(zones);

    // Etape 4 : chargement des parcelles et créations des bordures
    IFeatureCollection<CadastralParcel> parcelles = CadastralParcelLoader
        .assignBordureToParcelleWithOrientation(parcelleColl, 1);

    env.setCadastralParcels(parcelles);

    // Etape 5 : import des sous parcelles
    IFeatureCollection<SubParcel> sousParcelles = SubParcelImporter.create(
        parcelles, zones);
    env.setSubParcels(sousParcelles);

    // Etape 8 : création des unités foncirèes
    IFeatureCollection<BasicPropertyUnit> collBPU = BasicPropertyUnitImporter
        .importBPU(parcelles);
    env.setBpU(collBPU);

    // Etape 7 : import des bâtiments
    IFeatureCollection<Building> buildings = BuildingImporter.importBuilding(
        batiColl, collBPU);
    env.getBuildings().addAll(buildings);

    // Etape 8 : chargement des voiries

    IFeatureCollection<Road> roads = RoadImporter.importRoad(voirieColl);
    env.setRoads(roads);

    // Etape 9 : on affecte les liens entres une bordure et ses objets adjacents
    AssignLinkToBordure.process(parcelles, roads);

    // Etape 10 : on importe les alignements
    IFeatureCollection<Alignement> alignementColl = AlignementImporter
        .importRecul(prescriptions, parcelles);
    env.setAlignements(alignementColl);

    // Etape 11 : on affecte des z à tout ce bon monde // - parcelles,
    // sous-parcelles route sans z, zonage, les bordures etc...
    DTMArea dtm = new DTMArea(folder + NOM_FICHIER_TERRAIN, "Terrain", true, 1,
        ColorShade.BLUE_CYAN_GREEN_YELLOW_WHITE);
    env.setTerrain(dtm);
    try {
      AssignZ.toParcelle(env.getParcelles(), dtm, SURSAMPLED);
      AssignZ.toSousParcelle(env.getSubParcels(), dtm, SURSAMPLED);
      AssignZ.toVoirie(env.getRoads(), dtm, SURSAMPLED);
      AssignZ.toAlignement(alignementColl, dtm, SURSAMPLED);
      AssignZ.toZone(env.getUrbaZones(), dtm, false);
    } catch (Exception e) { // TODO Auto-generated catch block
      e.printStackTrace();

    }

    return env;
  }

}
