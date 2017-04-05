package fr.ign.cogit.simplu3d.experiments.openmole;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.experiments.iauidf.predicate.PredicateIAUIDF;
import fr.ign.cogit.simplu3d.experiments.iauidf.regulation.Regulation;
import fr.ign.cogit.simplu3d.experiments.iauidf.tool.BandProduction;
import fr.ign.cogit.simplu3d.experiments.iauidf.tool.ParcelAttributeTransfert;
import fr.ign.cogit.simplu3d.importer.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.model.ParcelBoundarySide;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.loader.LoaderCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.mix.MultipleBuildingsCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.mix.MultipleBuildingsTrapezoidCuboid;
import fr.ign.cogit.simplu3d.util.SDPCalc;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;
import fr.ign.parameters.Parameters;

public class EPFIFTask {

  public static boolean USE_DEMO_SAMPLER = false;
  public static boolean INTERSECTION = false;
  public static int FLOOR_SIZE = 3;
  public static int MAX_PARCEL_AREA = 10000;

  // public static boolean DEBUG_MODE = false;
  // public static List<IMultiSurface<IOrientableSurface>> lMS = new
  // ArrayList<>();
  // public static List<IMultiSurface<IOrientableSurface>> debugSurface = new
  // ArrayList<>();
  // public static List<IMultiCurve<IOrientableCurve>> debugLine = new
  // ArrayList<>();

  private static List<String> idparWithNoRules = new ArrayList<>();

  public static String run(File folder, File folderOut, File parameterFile,
      long seed) throws Exception {
    init();
    MultipleBuildingsCuboid.ALLOW_INTERSECTING_CUBOID = INTERSECTION;

    System.out.println("folder out = " + folderOut);
    if (!folderOut.exists()) {
      folderOut.mkdirs();
      if (folderOut.exists())
        System.out.println("I had to create it though");
      else {
        System.out.println("I could not create it...");
        throw new Exception("Could not create temp directory");
      }
    } else {
      System.out.println("We're all good!");
    }

    Environnement env = LoaderSHP.loadNoDTM(folder);
    String[] folderSplit = folder.getAbsolutePath().split(File.separator);
    // Identifiant de l'imu courant
    String imu = folderSplit[folderSplit.length - 1];
    // Stocke les résultats en sorties
    Map<String, List<Regulation>> regulation = loadRules(
        new File(folder + "/parcelle.shp"), Integer.parseInt(imu));
    IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();
    String result = "";
    SDPCalc sdp = new SDPCalc();
    for (BasicPropertyUnit bPU : env.getBpU()) {
      String id = bPU.getCadastralParcels().get(0).getCode();
      System.out.println("idpar " + id);
      List<Regulation> lR = regulation.get(id);
      if (bPU.getArea() > MAX_PARCEL_AREA) {
        result += imu + " ; " + id + " ; " + (-69) + " ; " + (-69) + "\n";
        continue;
      }
      if (lR != null && !lR.isEmpty()) {
        // On simule indépendemment chaque unité foncière
        IFeatureCollection<IFeature> feats = simulationForEachBPU(env, bPU, lR,
            Integer.parseInt(folderSplit[folderSplit.length - 1]),
            parameterFile);
        if (feats.size() > 0) {
          featC.addAll(feats);
          double sd = sdp.process(LoaderCuboid.loadFromCollection(feats));
          result += imu + " ; " + id + " ; " + feats.size() + " ; " + sd + "\n";
        } else
          result += imu + " ; " + id + " ; " + 0 + " ; " + 0 + "\n";
      } else {
        System.out.println("Regulation not found : " + id);
      }

    }
    // On écrit le fichier en sortie dans le folderout
    String uds = USE_DEMO_SAMPLER ? "demo_sampler" : "no_demo_sampler";
    String fileName = folderOut + "/simul_" + imu + "_" + INTERSECTION + "_"
        + uds + ".shp";
    System.out.println(fileName);
    ShapefileWriter.write(featC, fileName); // , CRS.decode("EPSG:2154") =>
                                            // supprimé à cause de la
                                            // compatibilité OSIG/Geotools

    // SDPCalc sdp = new SDPCalc();
    // double sd = sdp.process(LoaderCuboid.loadFromCollection(featC));
    // String returnRes = imu + " -> " + featC.size() + " objects in shapefile
    // || "
    // + "SDP : " + sd;
    if (idparWithNoRules.size() > 0) {
      for (String id : idparWithNoRules)
        result += imu + " ; " + id + " ; " + -1 + " ; " + -1 + "\n";
      result += "\n";
    }
    System.out.println("res " + result);
    writeCSV(
        folderOut + "/simul_" + imu + "_" + INTERSECTION + "_" + uds + ".csv",
        result);
    return result;
  }

  // Initialisation des attributs différents du schéma de base
  // et le fichier de paramètre commun à toutes les simulations
  public static void init() throws Exception {
    CadastralParcelLoader.TYPE_ANNOTATION = 2;
    PredicateIAUIDF.RIGHT_OF_LEFT_FOR_ART_71 = ParcelBoundarySide.LEFT;
  }

  private static Map<String, List<Regulation>> loadRules(File parcelle,
      int imu) {

    IFeatureCollection<IFeature> featC = ShapefileReader
        .read(parcelle.getAbsolutePath());
    Map<String, List<Regulation>> map = new HashMap<>();

    for (IFeature feat : featC) {

      List<Regulation> lRegulation = new ArrayList<>();

      String id = feat.getAttribute(ParcelAttributeTransfert.PARCELLE_ID)
          .toString();

      int code_imu = imu; /// l'imu n'est pas dans le .csv
      /// Integer.parseInt(newmap.get(att_imu).toString());
      String libelle_zone = feat
          .getAttribute(ParcelAttributeTransfert.att_libelle_zone).toString(); // LIBELLE_ZONE

      // int insee = Integer.parseInt(
      // feat.getAttribute(ParcelAttributeTransfert.att_insee).toString());
      String tempo = feat.getAttribute(ParcelAttributeTransfert.att_insee)
          .toString();
      int insee = Integer.parseInt(tempo.equals("") ? "-1" : tempo);
      // si champs vide ya pas eu de correspondance aves le fichier de regles
      // inutile de parser la suite (qui va planter..)
      if (insee == -1) {
        idparWithNoRules.add(id);
        continue;
      }
      int date_approbation = 10022001; // on désactive on en a pas besoin
                                       // Integer.parseInt(feat.getAttribute(ParcelAttributeTransfert.att_date_approbation).toString());
      // test
      // code_imu = Integer.parseInt(
      // feat.getAttribute(ParcelAttributeTransfert.att_imu).toString());
      //
      String libelle_de_base = feat
          .getAttribute(ParcelAttributeTransfert.att_libelle_de_base)
          .toString(); // LIBELLE_DE_BASE
      String libelle_de_dul = feat
          .getAttribute(ParcelAttributeTransfert.att_libelle_de_dul).toString(); // LIBELLE_DE_DUL
      int fonctions = Integer.parseInt(
          feat.getAttribute(ParcelAttributeTransfert.att_fonctions).toString());
      int top_zac = Integer.parseInt(
          feat.getAttribute(ParcelAttributeTransfert.att_top_zac).toString());
      int zonage_coherent = Integer.parseInt(
          feat.getAttribute(ParcelAttributeTransfert.att_zonage_coherent)
              .toString());
      int correction_zonage = Integer.parseInt(
          feat.getAttribute(ParcelAttributeTransfert.att_correction_zonage)
              .toString());
      int typ_bande = Integer.parseInt(
          feat.getAttribute(ParcelAttributeTransfert.att_typ_bande).toString());
      int bande = Integer.parseInt(
          feat.getAttribute(ParcelAttributeTransfert.att_bande).toString());
      double art_5 = Double.parseDouble(
          feat.getAttribute(ParcelAttributeTransfert.att_art_5).toString());
      double art_6 = Double.parseDouble(
          feat.getAttribute(ParcelAttributeTransfert.att_art_6).toString());
      int art_71 = Integer.parseInt(
          feat.getAttribute(ParcelAttributeTransfert.att_art_71).toString());
      double art_72 = Double.parseDouble(
          feat.getAttribute(ParcelAttributeTransfert.att_art_72).toString());
      double art_73 = Double.parseDouble(
          feat.getAttribute(ParcelAttributeTransfert.att_art_73).toString());
      int art_74 = Integer.parseInt(
          feat.getAttribute(ParcelAttributeTransfert.att_art_74).toString());
      double art_8 = Double.parseDouble(
          feat.getAttribute(ParcelAttributeTransfert.att_art_8).toString());
      double art_9 = Double.parseDouble(
          feat.getAttribute(ParcelAttributeTransfert.att_art_9).toString());
      int art_10_top = Integer.parseInt(feat
          .getAttribute(ParcelAttributeTransfert.att_art_10_top).toString());
      double art_101 = Double.parseDouble(
          feat.getAttribute(ParcelAttributeTransfert.att_art_101).toString());
      double art_102 = Double.parseDouble(
          feat.getAttribute(ParcelAttributeTransfert.att_art_102).toString());
      double art_12 = Double.parseDouble(
          feat.getAttribute(ParcelAttributeTransfert.att_art_12).toString());
      double art_13 = Double.parseDouble(
          feat.getAttribute(ParcelAttributeTransfert.att_art_13).toString());
      double art_14 = Double.parseDouble(
          feat.getAttribute(ParcelAttributeTransfert.att_art_14).toString());

      Regulation r = new Regulation(code_imu, libelle_zone, insee,
          date_approbation, libelle_de_base, libelle_de_dul, fonctions, top_zac,
          zonage_coherent, correction_zonage, typ_bande, bande, art_5, art_6,
          art_71, art_72, art_73, art_74, art_8, art_9, art_10_top, art_101,
          art_102, art_12, art_13, art_14);

      lRegulation.add(r);

      // System.out.println(r.toString());

      if (bande != 0) {

        int fonctions_2 = Integer.parseInt(feat
            .getAttribute(ParcelAttributeTransfert.att_fonctions_2).toString());
        double art_5_2 = Double.parseDouble(
            feat.getAttribute(ParcelAttributeTransfert.att_art_5_2).toString());
        double art_6_2 = Double.parseDouble(
            feat.getAttribute(ParcelAttributeTransfert.att_art_6_2).toString());
        int art_71_2 = Integer.parseInt(feat
            .getAttribute(ParcelAttributeTransfert.att_art_71_2).toString());
        double art_72_2 = Double.parseDouble(feat
            .getAttribute(ParcelAttributeTransfert.att_art_72_2).toString());
        double art_73_2 = Double.parseDouble(feat
            .getAttribute(ParcelAttributeTransfert.att_art_73_2).toString());
        int art_74_2 = Integer.parseInt(feat
            .getAttribute(ParcelAttributeTransfert.att_art_74_2).toString());
        double art_8_2 = Double.parseDouble(
            feat.getAttribute(ParcelAttributeTransfert.att_art_8_2).toString());
        double art_9_2 = Double.parseDouble(
            feat.getAttribute(ParcelAttributeTransfert.att_art_9_2).toString());
        int art_10_top_2 = Integer.parseInt(
            feat.getAttribute(ParcelAttributeTransfert.att_art_10_top_2)
                .toString());
        double art_101_2 = Double.parseDouble(feat
            .getAttribute(ParcelAttributeTransfert.att_art_101_2).toString()); // ATTENTION
        // A
        // CHANGER
        double art_102_2 = Double.parseDouble(feat
            .getAttribute(ParcelAttributeTransfert.att_art_102_2).toString());
        double art_12_2 = Double.parseDouble(feat
            .getAttribute(ParcelAttributeTransfert.att_art_12_2).toString());
        double art_13_2 = Double.parseDouble(feat
            .getAttribute(ParcelAttributeTransfert.att_art_13_2).toString());
        double art_14_2 = Double.parseDouble(feat
            .getAttribute(ParcelAttributeTransfert.att_art_14_2).toString());

        Regulation r2 = new Regulation(code_imu, libelle_zone, insee,
            date_approbation, libelle_de_base, libelle_de_dul, fonctions_2,
            top_zac, zonage_coherent, correction_zonage, typ_bande, bande,
            art_5_2, art_6_2, art_71_2, art_72_2, art_73_2, art_74_2, art_8_2,
            art_9_2, art_10_top_2, art_101_2, art_102_2, art_12_2, art_13_2,
            art_14_2);

        // System.out.println(r2.toString());

        lRegulation.add(r2);
      }
      map.put(id, lRegulation);
    }
    return map;
  }

  public static IFeatureCollection<IFeature> simulationForEachBPU(
      Environnement env, BasicPropertyUnit bPU, List<Regulation> lRegulation,
      int imu, File fParam) throws Exception {

    // Stocke les résultats
    IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();

    // On ne simule pas sur les très petites parcelles qui peuvent être des
    // erreurs dus à la carte topo
    if (bPU.getCadastralParcels().get(0).getArea() < 5) {
      System.out.println("Probablement une erreur de carte topologique.");
      return featC;
    }

    // Il y a 1 ou 2 réglementaiton par parcelle
    Regulation r1 = lRegulation.get(0);
    Regulation r2 = null;

    if (lRegulation.size() > 1) {
      r2 = lRegulation.get(1);
    }

    System.out.println("R1 : " + r1);

    if (r2 != null) {

      System.out.println("R2 : " + r2);

    }

    // Somme nous dans le cas où les bâtiments doivent être accolé aux
    // limites latérales ?
    if (r1 != null && r1.getArt_71() == 2
        || r2 != null && r2.getArt_71() == 2) {

      // Cas ou les bâtiments se collent d'un des 2 côtés, on simule
      // les 2
      // côtés et on regarde pour chaque parcelle quelle est la
      // meilleure
      // :

      PredicateIAUIDF.RIGHT_OF_LEFT_FOR_ART_71 = ParcelBoundarySide.RIGHT;

      IFeatureCollection<IFeature> featC1 = new FT_FeatureCollection<>();

      featC1.addAll(
          simulRegulationByBasicPropertyUnit(env, bPU, imu, r1, r2, fParam));

      PredicateIAUIDF.RIGHT_OF_LEFT_FOR_ART_71 = ParcelBoundarySide.LEFT;

      IFeatureCollection<IFeature> featC2 = new FT_FeatureCollection<>();

      featC2.addAll(
          simulRegulationByBasicPropertyUnit(env, bPU, imu, r1, r2, fParam));

      featC.addAll(fusionne(featC1, featC2));

    } else {
      featC.addAll(
          simulRegulationByBasicPropertyUnit(env, bPU, imu, r1, r2, fParam));

    }
    return featC;
  }

  /**
   * 
   * @param bPU
   * @param imu
   * @param lReg
   * @return
   * @throws Exception
   */
  public static IFeatureCollection<IFeature> simulRegulationByBasicPropertyUnit(
      Environnement env, BasicPropertyUnit bPU, int imu, Regulation r1,
      Regulation r2, File fParam) throws Exception {
    // Stocke les résultats en sorties
    IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();
    // //////On découpe la parcelle en bande en fonction des règlements

    // ART_5 Superficie minimale 88= non renseignable, 99= non réglementé
    // Si ce n'est pas respecté on ne fait même pas de simulation
    double r_art5 = r1.getArt_5();
    if (r_art5 != 99) {
      if (bPU.getPol2D().area() < r_art5) {
        return featC;
      }
    }
    // Processus découpant la zone dans laquelle on met les bâtiments à
    // partir des règles
    BandProduction bP = new BandProduction(bPU, r1, r2);

    if (r2 == null || r2.getGeomBande() == null
        || r2.getGeomBande().isEmpty()) {
      r2 = null;
      System.out.println("Une seule bande");
    }

    // if (DEBUG_MODE) {
    //
    // if (r1 != null && r1.getGeomBande() != null) {
    // debugSurface.add(r1.getGeomBande());
    // }
    //
    // if (r2 != null && r2.getGeomBande() != null) {
    // debugSurface.add(r2.getGeomBande());
    // }
    // debugLine.add(bP.getLineRoad());
    // }

    Parameters p = initiateSimulationParamters(r1, r2, fParam);
    // initialisation des paramètres de simulation
    if (p == null) {
      return featC;
    }

    if (USE_DEMO_SAMPLER) {

      featC.addAll(simulRegulationByBasicPropertyUnitFinalTrapezoid(env, bPU,
          imu, r1, r2, p, bP));

    } else {

      featC.addAll(simulRegulationByBasicPropertyUnitFinal(env, bPU, imu, r1,
          r2, p, bP));
    }

    return featC;
  }

  private static IFeatureCollection<IFeature> fusionne(
      IFeatureCollection<IFeature> featC1,
      IFeatureCollection<IFeature> featC2) {

    IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();

    while (!(featC1.isEmpty()) && !featC2.isEmpty()) {

      IFeature featTemp = featC1.get(0);
      featC1.remove(0);

      int currentID = Integer
          .parseInt(featTemp.getAttribute("ID_PARC").toString());

      List<IFeature> lF1 = new ArrayList<>();
      lF1.add(featTemp);

      int nbElem = featC1.size();

      for (int i = 0; i < nbElem; i++) {

        featTemp = featC1.get(i);

        if (Integer.parseInt(
            featTemp.getAttribute("ID_PARC").toString()) == currentID) {

          lF1.add(featTemp);
          featC1.remove(i);
          i--;
          nbElem--;
        }

      }

      List<IFeature> lF2 = new ArrayList<>();
      int nbElem2 = featC2.size();

      for (int i = 0; i < nbElem2; i++) {

        featTemp = featC2.get(i);

        if (Integer.parseInt(
            featTemp.getAttribute("ID_PARC").toString()) == currentID) {

          lF2.add(featTemp);
          featC2.remove(i);
          i--;
          nbElem2--;
        }

      }

      double contrib1 = 0;
      for (IFeature feat : lF1) {
        contrib1 = contrib1 + feat.getGeom().area()
            * Double.parseDouble(feat.getAttribute("Hauteur").toString());
      }

      double contrib2 = 0;
      for (IFeature feat : lF2) {
        contrib2 = contrib2 + feat.getGeom().area()
            * Double.parseDouble(feat.getAttribute("Hauteur").toString());
      }

      if (contrib1 > contrib2) {

        featC.addAll(lF1);

      } else {

        featC.addAll(lF2);

      }

      if (featC1.isEmpty()) {

        featC.addAll(featC2);
        featC2.clear();

      }

    }

    return featC;
  }

  private static IFeatureCollection<IFeature> simulRegulationByBasicPropertyUnitFinal(
      Environnement env, BasicPropertyUnit bPU, int imu, Regulation r1,
      Regulation r2, Parameters p, BandProduction bP) throws Exception {

    IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();

    // Création du Sampler (qui va générer les propositions de solutions)
    MultipleBuildingsCuboid oCB = new MultipleBuildingsCuboid();
    PredicateIAUIDF<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new PredicateIAUIDF<>(
        bPU, r1, r2);

    if (p.getBoolean("shapefilewriter")) {
      new File(p.getString("result") + imu).mkdir();
    }
    // Lancement de l'optimisation avec unité foncière, paramètres,
    // environnement, id et prédicat

    GraphConfiguration<Cuboid> cc = oCB.process(bPU, p, env, pred, r1, r2, bP);
    if (cc == null) {
      return featC;
    }

    // On liste les boîtes simulées et on ajoute les attributs nécessaires
    for (GraphVertex<Cuboid> v : cc.getGraph().vertexSet()) {
      IFeature feat = new DefaultFeature(v.getValue().generated3DGeom());
      // On ajoute des attributs aux entités (dimension des objets)
      AttributeManager.addAttribute(feat, "Longueur",
          Math.max(v.getValue().length, v.getValue().width), "Double");
      AttributeManager.addAttribute(feat, "Largeur",
          Math.min(v.getValue().length, v.getValue().width), "Double");
      AttributeManager.addAttribute(feat, "Hauteur", v.getValue().height,
          "Double");
      AttributeManager.addAttribute(feat, "Rotation", v.getValue().orientation,
          "Double");
      AttributeManager.addAttribute(feat, "ID_PARC", bPU.getId(), "Integer");
      double area = 0;
      double volume = 0;
      if (v.getValue().getFootprint() != null
          && (!v.getValue().getFootprint().isEmpty())) {
        area = v.getValue().getFootprint().area();
        volume = v.getValue().getVolume();
      }

      AttributeManager.addAttribute(feat, "Aire", area, "Double");
      AttributeManager.addAttribute(feat, "Volume", volume, "Double");
      featC.add(feat);
    }
    return featC;

  }

  private static IFeatureCollection<IFeature> simulRegulationByBasicPropertyUnitFinalTrapezoid(
      Environnement env, BasicPropertyUnit bPU, int imu, Regulation r1,
      Regulation r2, Parameters p, BandProduction bP) throws Exception {

    IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();

    // Création du Sampler (qui va générer les propositions de solutions)
    MultipleBuildingsTrapezoidCuboid oCB = new MultipleBuildingsTrapezoidCuboid();
    PredicateIAUIDF<AbstractSimpleBuilding, GraphConfiguration<AbstractSimpleBuilding>, BirthDeathModification<AbstractSimpleBuilding>> pred = new PredicateIAUIDF<>(
        bPU, r1, r2);

    if (p.getBoolean("shapefilewriter")) {
      new File(p.getString("result") + imu).mkdir();
    }
    // Lancement de l'optimisation avec unité foncière, paramètres,
    // environnement, id et prédicat

    GraphConfiguration<AbstractSimpleBuilding> cc = oCB.process(bPU, p, env,
        pred, r1, r2, bP);
    if (cc == null) {
      return featC;
    }

    // On liste les boîtes simulées et on ajoute les attributs nécessaires
    for (GraphVertex<AbstractSimpleBuilding> v : cc.getGraph().vertexSet()) {

      IFeature feat = new DefaultFeature(v.getValue().generated3DGeom());
      // On ajoute des attributs aux entités (dimension des objets)
      double longueur = Math.max(v.getValue().length, v.getValue().width);
      double largeur = Math.min(v.getValue().length, v.getValue().width);
      double hauteur = v.getValue().height;
      double ori = v.getValue().orientation;
      int idParc = bPU.getId();
      AttributeManager.addAttribute(feat, "Longueur", longueur, "Double");
      AttributeManager.addAttribute(feat, "Largeur", largeur, "Double");
      AttributeManager.addAttribute(feat, "Hauteur", hauteur, "Double");
      AttributeManager.addAttribute(feat, "Rotation", ori, "Double");
      AttributeManager.addAttribute(feat, "ID_PARC", idParc, "Integer");
      double area = 0;
      double volume = 0;
      if (v.getValue().getFootprint() != null
          && (!v.getValue().getFootprint().isEmpty())) {
        area = v.getValue().getFootprint().area();
      }
      AttributeManager.addAttribute(feat, "Aire", area, "Double");
      AttributeManager.addAttribute(feat, "Volume", volume, "Double");

      featC.add(feat);
    }
    return featC;

  }

  private static Parameters initiateSimulationParamters(Regulation r1,
      Regulation r2, File f) throws Exception {
    // Chargement du fichier de configuration

    Parameters p = Parameters.unmarshall(f);

    if (r2 != null) {

      double newHeightMax = Math.max(r1.getArt_102(), r2.getArt_102());

      if (newHeightMax != 99.0) {
        p.set("maxheight", newHeightMax);
      }

    } else {
      if (r1.getArt_102() != 99) {
        p.set("maxheight", r1.getArt_102());
      }

    }

    if (p.getDouble("maxheight") < p.getDouble("minheight")) {
      return null;
    }

    System.out.println(
        "Hauteur " + p.getDouble("minheight") + " " + p.getDouble("maxheight"));

    if (r2 != null) {
      if ((r1.getArt_74() == 0) && (r2.getArt_74() == 0)
          && (r2.getArt_14() != 99) && (r1.getArt_14() != 99)
          && (p.getDouble("maxheight") != 99.0)) {
        p.set("minheight", p.getDouble("maxheight") - 0.1);
      }
    } else {
      if ((r1.getArt_74() == 0) && (r1.getArt_14() != 99)
          && (p.getDouble("maxheight") != 99.0)) {
        p.set("minheight", p.getDouble("maxheight") - 0.1);
      }
    }

    /*
     * double longueur1 = Double.NEGATIVE_INFINITY;
     * 
     * if (r1.getGeomBande() != null && !r1.getGeomBande().isEmpty()) {
     * OrientedBoundingBox oBB1 = new OrientedBoundingBox(r1.getGeomBande());
     * 
     * longueur1 = oBB1.getLength(); }
     * 
     * 
     * if (r2 != null) { OrientedBoundingBox oBB2 = new
     * OrientedBoundingBox(r2.getGeomBande());
     * 
     * double longueur2 = oBB2.getLength();
     * 
     * p.set("maxlen", Math.min(p.getDouble("maxlen"), Math.max(longueur1,
     * longueur2)));
     * 
     * p.set("maxwid", Math.min(p.getDouble("maxwid"), Math.max(longueur1,
     * longueur2)));
     * 
     * } else { p.set("maxlen", Math.min(p.getDouble("maxlen"), longueur1));
     * p.set("maxwid", Math.min(p.getDouble("maxwid"), longueur1));
     * 
     * }
     */

    if (p.getDouble("maxlen") < p.getDouble("minlen")) {
      return null;
    }

    if (p.getDouble("maxwid") < p.getDouble("minwid")) {
      return null;
    }

    p.set("temp", Math.min(p.getDouble("temp"), p.getDouble("maxlen")
        * p.getDouble("maxlen") * p.getDouble("maxheight")));

    return p;
  }

  private static void writeCSV(String fileName, String lines) {
    try {
      Files.write(Paths.get(fileName), lines.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("csv writing failed");
    }
    System.out.println("csv file " + fileName + " written");
  }

}
