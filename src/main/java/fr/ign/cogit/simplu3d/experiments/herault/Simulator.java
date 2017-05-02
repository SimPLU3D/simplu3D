package fr.ign.cogit.simplu3d.experiments.herault;

import java.io.File;

import fr.ign.cogit.simplu3d.demo.DemoEnvironmentProvider;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.cuboid.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.parameters.Parameters;

public class Simulator {

	public static void main(String[] args) throws Exception {
		String folder = "/home/mickael/data/mbrasebin/donnees/Jennifer/Donnees/3AU/";

		// On charge le fichier de configuration
		String fileName = "parameters.xml";
		Parameters p = Parameters.unmarshall(new File(folder + fileName));

		// On charge l'environnement géographique
		Environnement env = LoaderSHP.loadNoDTM(new File(folder));

		System.out.println(env.getBpU().size());
		
		
		System.out.println("Number of forbidden zones : " + env.getPrescriptions().size());
		
		for(BasicPropertyUnit bPU : env.getBpU()){
			
			if(! bPU.getCadastralParcels().get(0).hasToBeSimulated()){
				continue;
			}

			System.out.println("Simulation de la parcelle : " +  bPU.getCadastralParcels().get(0).getCode());
			
			// Instantiation of the sampler
			OptimisedBuildingsCuboidFinalDirectRejection oCB = new OptimisedBuildingsCuboidFinalDirectRejection();
			
			
			
		}
		

	}

}
