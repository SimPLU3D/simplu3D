package fr.ign.cogit.simplu3d.iauidf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import fr.ign.cogit.simplu3d.exec.BasicSimulator;
import fr.ign.cogit.simplu3d.iauidf.regulation.Regulation;
import fr.ign.cogit.simplu3d.importer.applicationClasses.RoadImporter;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.parameters.Parameters;

public class Exec {

	private static Logger log = Logger.getLogger(Exec.class);

	public final static String nomDTM = "MNT_BD3D.asc";
	
	
	public static Parameters p;

	// Initialisation des attributs différents du schéma de base
	// et le fichier de paramètre commun à toutes les simulations
	public static void init() throws Exception {

		RoadImporter.ATT_NOM_RUE = "NOM_VOIE_G";
		RoadImporter.ATT_LARGEUR = "LARGEUR";
		RoadImporter.ATT_TYPE = "NATURE";
		
		
		// Chargement du fichier de configuration
		String folderName = BasicSimulator.class.getClassLoader()
				.getResource("scenario/").getPath();
		String fileName = "parameters_iauidf.xml";
		p = Parameters.unmarshall(new File(folderName + fileName));
	}
	
	
	
	

	public static void main(String[] args) throws IOException,
			CloneNotSupportedException {
		// Dossier contenant IMU_MANTES_TEST.csv et les sous dossier par code
		// IMU
		String folder = "C:/Users/mbrasebin/Desktop/Ilots_test/COGIT78/";
		String csvFile = folder + "IMU_MANTES_TEST.csv";

		// Chargement des règlement par code IMU (on peut avoir plusieurs
		// réglements pour un code IMU du fait des bandes)
		Map<Integer, List<Regulation>> mapReg = Regulation
				.loadRegulationSet(csvFile);

		// Fonction de test : chargement de la réglementation :
		testLoadedRegulation(mapReg);

		// @TODO : gérer les attributs indépendemment de la casse.
		// Initialisation des noms d'attributs
		init();

		// On traite indépendamment chaque zone imu
		for (int imu : mapReg.keySet()) {

			boolean simul = simulRegulationByIMU(imu, mapReg.get(imu), folder
					+ imu + "/");

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
	public static boolean simulRegulationByIMU(int imu, List<Regulation> lReg,
			String folderImu) throws FileNotFoundException,
			CloneNotSupportedException {

		Environnement env = LoaderSHP.load(folderImu, new FileInputStream(
				folderImu + nomDTM));

		boolean isOk = true;

		for (BasicPropertyUnit bPU : env.getBpU()) {

			isOk = isOk && simulRegulationByBasicPropertyUnit(bPU, imu, lReg);

		}

		return true;
	}

	/**
	 * 
	 * @param bPU
	 * @param imu
	 * @param lReg
	 * @return
	 */
	public static boolean simulRegulationByBasicPropertyUnit(
			BasicPropertyUnit bPU, int imu, List<Regulation> lReg) {
		
		

		return true;
	}

	// Affiche les règlements chargés
	public static void testLoadedRegulation(
			Map<Integer, List<Regulation>> mapReg) {

		for (int key : mapReg.keySet()) {

			log.debug("-----key----------");

			for (Regulation reg : mapReg.get(key)) {

				log.debug(reg.toString());
			}

		}

	}

}
