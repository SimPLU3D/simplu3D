package fr.ign.cogit.simplu3d.iauidf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.exec.BasicSimulator;
import fr.ign.cogit.simplu3d.iauidf.regulation.Regulation;
import fr.ign.cogit.simplu3d.iauidf.tool.BandProduction;
import fr.ign.cogit.simplu3d.importer.applicationClasses.RoadImporter;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.classconstrained.MultipleBuildingsCuboid;
import fr.ign.parameters.Parameters;

public class Exec {

  private static Logger log = Logger.getLogger(Exec.class);

  public final static String nomDTM = "MNT_BD3D.asc";

  public static Parameters p;

  public static List<IMultiSurface<IOrientableSurface>> lMS = new ArrayList<>();

  private static List<IMultiSurface<IOrientableSurface>> debugSurface = new ArrayList<>();

  // Initialisation des attributs différents du schéma de base
  // et le fichier de paramètre commun à toutes les simulations
  public static void init() throws Exception {

    RoadImporter.ATT_NOM_RUE = "NOM_VOIE_G";
    RoadImporter.ATT_LARGEUR = "LARGEUR";
    RoadImporter.ATT_TYPE = "NATURE";

    // Chargement du fichier de configuration
    String folderName = BasicSimulator.class.getClassLoader().getResource("scenario/").getPath();
    String fileName = "parameters_iauidf.xml";
    p = Parameters.unmarshall(new File(folderName + fileName));
  }

  public static void main(String[] args) throws Exception {

    // Dossier contenant IMU_MANTES_TEST.csv et les sous dossier par code
    // IMU
    String folder = "/home/adminlocal/IAUIDF/";
    String csvFile = folder + "IMU_MANTES_TEST.csv";

    // Chargement des règlement par code IMU (on peut avoir plusieurs
    // réglements pour un code IMU du fait des bandes)
    Map<Integer, List<Regulation>> mapReg = Regulation.loadRegulationSet(csvFile);

    // Fonction de test : chargement de la réglementation :
    testLoadedRegulation(mapReg);

    // @TODO : gérer les attributs indépendemment de la casse.
    // Initialisation des noms d'attributs
    init();

    // On traite indépendamment chaque zone imu
    for (int imu : mapReg.keySet()) {

      boolean simul = simulRegulationByIMU(imu, mapReg.get(imu), folder + imu + "/");

      if (!simul) {
        log.warn("--Probleme pour la simulation : " + imu);
      }

    }

  }

  /**
   * Simulations portant sur chaque zone IMU
   * 
   * @param imu
   * @param lReg
   * @return
   * @throws CloneNotSupportedException
   * @throws FileNotFoundException
   */
  public static boolean simulRegulationByIMU(int imu, List<Regulation> lReg, String folderImu)
      throws FileNotFoundException, CloneNotSupportedException {

    debugSurface = new ArrayList<>();

    Environnement env = LoaderSHP.load(folderImu, new FileInputStream(folderImu + nomDTM));

    boolean isOk = true;

    // On parcourt chaque parcelle et on applique la simulation dessus
    for (BasicPropertyUnit bPU : env.getBpU()) {

      isOk = isOk && simulRegulationByBasicPropertyUnit(bPU, imu, lReg);

    }

    System.out.println("-- Nombre de surface : " + debugSurface.size());

    IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();

    for (IMultiSurface<IOrientableSurface> iS : debugSurface) {
      if (iS != null && iS.isValid() && !iS.isEmpty()) {
        featC.add(new DefaultFeature(iS));
      }

    }

    ShapefileWriter.write(featC, folderImu + "generatedBand.shp");

    return isOk;
  }

  /**
   * 
   * @param bPU
   * @param imu
   * @param lReg
   * @return
   */
  public static boolean simulRegulationByBasicPropertyUnit(BasicPropertyUnit bPU, int imu, List<Regulation> lReg) {

    // //////On découpe la parcelle en bande en fonction des règlements

    // On met les règlements dans l'ordre des bandes (le premier est la
    // première bande, le second la seconde ou null s'il n'y en a pas)
    List<Regulation> orderedRegulation = orderedRegulation(lReg, imu);
    if (orderedRegulation == null) {
      return false;
    }
    Regulation r1 = orderedRegulation.get(0);
    Regulation r2 = orderedRegulation.get(1);

    List<IMultiSurface<IOrientableSurface>> lSurface = BandProduction.getBands(bPU, r1, r2, p.getDouble("minwid"));

    debugSurface.addAll(lSurface);

    // Création du Sampler (qui va générer les propositions de solutions)
    // A voir quand on aura le nouveau sampler

    MultipleBuildingsCuboid oCB = new MultipleBuildingsCuboid();
    try {
      oCB.process(bPU, p, null, imu, null);
    } catch (Exception e) {
      // TODO OUPS, that didn't work
      e.printStackTrace();
    }
    return true;
  }

  public static List<Regulation> orderedRegulation(List<Regulation> lReg, int imu) {
    Regulation r1, r2 = null;
    if (lReg.size() == 1) {
      r1 = lReg.get(0);
      if (r1.getTyp_bande() == 2) {
        log.error("Une seule bande et un indice de 2 ? " + imu);
        r2 = null;
      }
    } else if (lReg.size() == 2) {

      if (lReg.get(0).getTyp_bande() == 1) {
        r1 = lReg.get(0);
        r2 = lReg.get(1);
      } else {
        r1 = lReg.get(1);
        r2 = lReg.get(0);

      }

      if (r1.getTyp_bande() != 1 || r2.getTyp_bande() != 2) {
        log.error("Type bande r1 et r2 incorrects : " + imu);
        return null;
      }

    } else {
      log.error("0 ou plus de 2 règlements pour un code imu, cas non traité : " + imu);
      return null;
    }

    List<Regulation> orderedList = new ArrayList<>();
    orderedList.add(r1);
    orderedList.add(r2);
    return orderedList;
  }

  // Affiche les règlements chargés
  public static void testLoadedRegulation(Map<Integer, List<Regulation>> mapReg) {

    for (int key : mapReg.keySet()) {

      log.debug("-----key----------");

      for (Regulation reg : mapReg.get(key)) {

        log.debug(reg.toString());
      }

    }

  }

}
