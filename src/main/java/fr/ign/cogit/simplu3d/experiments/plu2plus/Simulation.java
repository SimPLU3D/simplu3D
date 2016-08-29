package fr.ign.cogit.simplu3d.experiments.plu2plus;

import java.io.File;

import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.parameters.Parameters;

public class Simulation {

	public static void main(String[] args) throws Exception {
		String folderName = "/home/mickael/data/mbrasebin/donnees/PLU2PLUS/Projet/";
		String paramFile = folderName + "parameters_meylan.xml";

		Parameters p = Parameters.unmarshall(new File(paramFile));
		
		
		Environnement env = LoaderSHP.loadNoDTM(new File(folderName));

	}

}
