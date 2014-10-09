package fr.ign.cogit.simplu3d.io.load.application;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.semantic.AbstractDTM;
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
import fr.ign.cogit.simplu3d.model.application.Rule;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.model.application.UrbaZone;
import fr.ign.cogit.simplu3d.util.AssignZ;

public class LoadFromCollection {

  public final static boolean SURSAMPLED = false;

  public static Environnement load(IFeatureCollection<IFeature> zoneColl,
      IFeatureCollection<IFeature> parcelleColl,
      IFeatureCollection<IFeature> voirieColl,
      IFeatureCollection<IFeature> batiColl,
      IFeatureCollection<IFeature> prescriptions, String ruleFolder, AbstractDTM dtm)
      throws CloneNotSupportedException {
    Environnement env = Environnement.getInstance();

    // Etape 0 : doit on translater tous les objets ?

    if (Environnement.TRANSLATE_TO_ZERO) {
      Environnement.dpTranslate = zoneColl.envelope().center();
      for (IFeature feat : zoneColl) {
        feat.setGeom(feat.getGeom().translate(
            -Environnement.dpTranslate.getX(),
            -Environnement.dpTranslate.getY(), 0));
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

    
    if(ruleFolder != null){
      for (UrbaZone z : zones) {
        RulesImporter.importer(ruleFolder, z);
        System.out.println("Zone " + z.getName());
        for (Rule rule : z.getRules()) {
          System.out.println("rule " + rule.constraint + " with " + rule.text);
        }
      }

      
    }

    // Etape 3 : assignement des zonages au PLU
    plu.lUrbaZone.addAll(zones);
    env.setUrbaZones(zones);

    // Etape 4 : chargement des parcelles et créations des bordures
    IFeatureCollection<CadastralParcel> parcelles = CadastralParcelLoader
        .assignBordureToParcelleWithOrientation(parcelleColl, 3);

    env.setCadastralParcels(parcelles);

    // Etape 5 : import des sous parcelles
    IFeatureCollection<SubParcel> sousParcelles = SubParcelImporter.create(
        parcelles, zones);
    env.setSubParcels(sousParcelles);

    // Etape 6 : création des unités foncirèes
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
