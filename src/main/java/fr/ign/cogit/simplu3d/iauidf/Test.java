package fr.ign.cogit.simplu3d.iauidf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ign.cogit.simplu3d.iauidf.regulation.Regulation;

public class Test {
	
	public static void main(String[] args) throws Exception{
		
		File f = new File("/home/mickael/data/mbrasebin/donnees/IAUIDF/DonneesIAU/Eval_EPF/rules.csv");
		
		
		if (!f.exists()) {
			return;
		}

		// On lit le fichier
		BufferedReader in = new BufferedReader(new FileReader(f));
		String line = in.readLine();
		
		
		Object[] listItem = line.split(";");
		
		List<Map<String, Object>> lMap = new ArrayList<>();
		
		
		
		// On traite chaque ligne
		while ((line = in.readLine()) != null) {
			
			Object[] listValue = line.split(";");
			
			Map<String, Object> newmap = new HashMap<>();
			
			for(int i=0; i < listValue.length; i++)
			{
				newmap.put(listItem[i].toString(), listValue[i]);
				
			}
			
			
			System.out.println("*************************************************");
			System.out.println("*******************"+listValue[0]+"******************");
			
			int code_imu = Integer.parseInt(newmap.get("IMU").toString());
			String libelle_zone =newmap.get("LIBELLE_ZONE").toString();
			int insee = Integer.parseInt(newmap.get("INSEE").toString()); 
			int date_approbation = Integer.parseInt(newmap.get("INSEE").toString()); 
			String libelle_de_base = newmap.get("LIBELLE_DE_BASE").toString();
			String libelle_de_dul = newmap.get("LIBELLE_DE_DUL").toString();
			int fonctions = Integer.parseInt(newmap.get("FONCTIONS").toString()); 
			int top_zac =  Integer.parseInt(newmap.get("TOP_ZAC").toString()); 
			int zonage_coherent =  Integer.parseInt(newmap.get("ZONAGE_COHERENT").toString()); 
			int correction_zonage =  Integer.parseInt(newmap.get("CORRECTION_ZONAGE").toString());
			int typ_bande =  Integer.parseInt(newmap.get("TYP_BANDE").toString());
			int bande = Integer.parseInt(newmap.get("BANDE").toString());
			int art_5 = Integer.parseInt(newmap.get("ART_5").toString());		
			double art_6 = Double.parseDouble(newmap.get("ART_6").toString());		
			int art_71 = Integer.parseInt(newmap.get("ART_71").toString());		
			int art_72 = Integer.parseInt(newmap.get("ART_72").toString());				
			double art_73 = Double.parseDouble(newmap.get("ART_73").toString());
			int art_74 = Integer.parseInt(newmap.get("ART_74").toString());		
			int art_8 = Integer.parseInt(newmap.get("ART_8").toString());			
			double art_9 = Double.parseDouble(newmap.get("ART_9").toString());		
			int art_10_top = Integer.parseInt(newmap.get("ART_10_TOP_1").toString());		
			int art_101 = Integer.parseInt(newmap.get("ART_10_2").toString());		//ATTENTION A CHANGER
			int art_102 = Integer.parseInt(newmap.get("ART_10_2").toString());	
			int art_12 = Integer.parseInt(newmap.get("ART_12").toString());			
			double  art_13 = Double.parseDouble(newmap.get("ART_13").toString());		
			double art_14 = Double.parseDouble(newmap.get("ART_14").toString());	
			
			Regulation r = new Regulation(code_imu, libelle_zone,  insee,
					 date_approbation,  libelle_de_base,
					 libelle_de_dul,  fonctions,  top_zac,
					 zonage_coherent,  correction_zonage,  typ_bande,
					 bande,  art_5,  art_6,  art_71,  art_72,
					 art_73,  art_74,  art_8,  art_9,  art_10_top,
					 art_101,  art_102,  art_12,  art_13,
					 art_14);
			
			
			System.out.println(r.toString());
			
			int fonctions_2 = Integer.parseInt(newmap.get("FONCTIONS_2").toString()); 
			int art_5_2 = Integer.parseInt(newmap.get("ART_5_2").toString());		
			double art_6_2 = Double.parseDouble(newmap.get("ART_6_2").toString());		
			int art_71_2 = Integer.parseInt(newmap.get("ART_71_2").toString());		
			int art_72_2 = Integer.parseInt(newmap.get("ART_72_2").toString());				
			double art_73_2 = Double.parseDouble(newmap.get("ART_73_2").toString());
			int art_74_2 = Integer.parseInt(newmap.get("ART_74_2").toString());		
			int art_8_2 = Integer.parseInt(newmap.get("ART_8_2").toString());			
			double art_9_2 = Double.parseDouble(newmap.get("ART_9_2").toString());		
			int art_10_top_2 = Integer.parseInt(newmap.get("ART_10_TOP_1_2").toString());		
			int art_101_2 = Integer.parseInt(newmap.get("ART_10_2_2").toString());		//ATTENTION A CHANGER
			int art_102_2 = Integer.parseInt(newmap.get("ART_10_2_2").toString());		
			int art_12_2 = Integer.parseInt(newmap.get("ART_12_2").toString());			
			double art_13_2 = Double.parseDouble(newmap.get("ART_13_2").toString());		
			double art_14_2 = Double.parseDouble(newmap.get("ART_14_2").toString());	
			
			Regulation r2 = new Regulation(code_imu, libelle_zone,  insee,
					 date_approbation,  libelle_de_base,
					 libelle_de_dul,  fonctions_2,  top_zac,
					 zonage_coherent,  correction_zonage,  typ_bande,
					 bande,  art_5_2,  art_6_2,  art_71_2,  art_72_2,
					 art_73_2,  art_74_2,  art_8_2,  art_9_2,  art_10_top_2,
					 art_101_2,  art_102_2,  art_12_2,  art_13_2,
					 art_14_2);
			
		
		
			System.out.println(r2.toString());
			
			System.out.println("*************************************************");
	
		
	}
		
		
		
		
		in.close();
		
	}

}
