package fr.ign.cogit.simplu3d.iauidf;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import fr.ign.cogit.simplu3d.iauidf.regulation.Regulation;

public class Exec {
	
	
	private static Logger log = Logger.getLogger(Exec.class);

	public static void main(String[] args) throws IOException {
		// Dossier contenant IMU_MANTES_TEST.csv et les sous dossier par code
		// IMU
		String folder = "C:/Users/mbrasebin/Desktop/Ilots_test/COGIT78/";
		String csvFile = folder + "IMU_MANTES_TEST.csv";

		// Chargement des règlement par code IMU (on peut avoir plusieurs
		// réglements pour un code IMU du fait des bandes)
		Map<Integer, List<Regulation>> mapReg = Regulation
				.loadRegulationSet(csvFile);
		
		//Fonction de test : chargement de la réglementation :
		testLoadedRegulation(mapReg);

		//On traite indépendamment chaque zone imu
		for(int imu : mapReg.keySet()){
			
			boolean simul = simulRegulation(imu, mapReg.get(imu));
			
			if(! simul){
				log.warn("--Probleme pour la simulation : " + imu);
			}
					
		}
			
	}

	
	/**
	 * Simulations portant 
	 * @param imu
	 * @param lReg
	 * @return
	 */
	public static boolean simulRegulation(int imu, List<Regulation> lReg){
		
		
		
		
		
		return true;
	}
	
	
	//Affiche les règlements chargés
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
