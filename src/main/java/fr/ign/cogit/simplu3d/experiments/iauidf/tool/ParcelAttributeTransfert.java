package fr.ign.cogit.simplu3d.experiments.iauidf.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.experiments.iauidf.regulation.Regulation;

/**
 * Class to transfer rules from a rule file on parcelle.
 * 
 * @author mickael
 *
 */
public class ParcelAttributeTransfert {
	
	
	public static String PARCELLE_ID = "IDPAR";

	public static String att_imu = "IMU";
	public static String att_libelle_zone = "Libelle_Zo";
	public static String att_insee = "INSEE";
	public static String att_date_approbation = "date_dul";

	public static String att_libelle_de_base = "LIBELLE_DE"; // LIBELLE_DE_BASE
	public static String att_libelle_de_dul = "LIBELLE__1"; // LIBELLE_DE_DUL
	public static String att_fonctions = "FONCTIONS";
	public static String att_top_zac = "TOP_ZAC";
	public static String att_zonage_coherent = "ZONAGE_COH";
	public static String att_correction_zonage = "CORRECTION";
	public static String att_typ_bande = "TYP_BANDE2";
	public static String att_bande = "BANDE1";
	public static String att_art_5 = "ART_51";
	public static String att_art_6 = "ART_61";
	public static String att_art_71 = "ART_711";
	public static String att_art_72 = "ART_721";
	public static String att_art_73 = "ART_731";
	public static String att_art_74 = "ART_741";
	public static String att_art_8 = "ART_81";
	public static String att_art_9 = "ART_91";
	public static String att_art_10_top = "ART_10_TOP";
	public static String att_art_10 = "ART_101"; // ATTENTION A CHANGER
	public static String att_art_10_m = "b1_haut_m";
	public static String att_art_12 = "ART_121";
	public static String att_art_13 = "ART_131";
	public static String att_art_14 = "ART_141";
	public static String att_fonctions_2 = "FONCTIONS";
	public static String att_art_5_2 = "ART_52";
	public static String att_art_6_2 = "ART_62";
	public static String att_art_71_2 = "ART_712";
	public static String att_art_72_2 = "ART_722";
	public static String att_art_73_2 = "ART_732";
	public static String att_art_74_2 = "ART_742";
	public static String att_art_8_2 = "ART_82";
	public static String att_art_9_2 = "ART_92";
	public static String att_art_10_top_2 = "ART_10_T_1";
	public static String att_art_10_2 = "ART_102"; // ATTENTION A CHANGER
	public static String att_art_10_m_2 = "b2_haut_m";
	public static String att_art_12_2 = "ART_122";
	public static String att_art_13_2 = "ART_132";
	public static String att_art_14_2 = "ART_142";

	public static void main(String[] args) throws Exception {
		
		
		File folder = new File("/home/mickael/data/mbrasebin/donnees/IAUIDF/Est_Ensemble/EstEnsemble");
		File fileCSV = new File("/home/mickael/data/mbrasebin/donnees/IAUIDF/Est_Ensemble/Regles_parcelles2.csv");
		
		 Map<String, List<Regulation>> map = getRegulation(fileCSV);
		 transfer(folder,map);
		

	}

	public static Map<String, List<Regulation>> getRegulation(File f) throws Exception {

		// On lit le fichier
		BufferedReader in = new BufferedReader(new FileReader(f));
		String line = in.readLine();

		Object[] listItem = line.split(";");

		Map<String, List<Regulation>> lMap = new HashMap<>();

		while ((line = in.readLine()) != null) {

			Object[] listValue = line.split(";");

			Map<String, Object> newmap = new HashMap<>();

			for (int i = 0; i < listValue.length; i++) {
				newmap.put(listItem[i].toString(), listValue[i]);

			}

			System.out.println("*************************************************");
			System.out.println("*******************" + listValue[0] + "******************");
			
			
			if("93048000BF0127".length() != listValue[0].toString().length()){
				System.exit(0);
			}

			int code_imu = 0; ///l'imu n'est pas dans le .csv Integer.parseInt(newmap.get(att_imu).toString());
			String libelle_zone = newmap.get(att_libelle_zone).toString(); // LIBELLE_ZONE
			int insee = Integer.parseInt( newmap.get(att_insee).toString());
			int date_approbation =  10022001 ; //on désactive on en a pas besoin  Integer.parseInt(newmap.get(att_date_approbation).toString());
			String libelle_de_base = newmap.get(att_libelle_de_base).toString(); // LIBELLE_DE_BASE
			String libelle_de_dul = newmap.get(att_libelle_de_dul).toString(); // LIBELLE_DE_DUL
			int fonctions = Integer.parseInt(newmap.get(att_fonctions).toString());
			int top_zac = Integer.parseInt(newmap.get(att_top_zac).toString());
			int zonage_coherent = Integer.parseInt(newmap.get(att_zonage_coherent).toString());
			int correction_zonage = Integer.parseInt(newmap.get(att_correction_zonage).toString());
			int typ_bande = Integer.parseInt(newmap.get(att_typ_bande).toString());
			int bande = Integer.parseInt(newmap.get(att_bande).toString());
			int art_5 = Integer.parseInt(newmap.get(att_art_5).toString());
			double art_6 = Double.parseDouble(newmap.get(att_art_6).toString());
			int art_71 = Integer.parseInt(newmap.get(att_art_71).toString());
			double art_72 = Double.parseDouble(newmap.get(att_art_72).toString());
			double art_73 = Double.parseDouble(newmap.get(att_art_73).toString());
			int art_74 = Integer.parseInt(newmap.get(att_art_74).toString());
			double art_8 = Double.parseDouble(newmap.get(att_art_8).toString());
			double art_9 = Double.parseDouble(newmap.get(att_art_9).toString());
			int art_10_top = Integer.parseInt(newmap.get(att_art_10_top).toString());
			double art_10 = Double.parseDouble(newmap.get(att_art_10).toString()); 
			double art_10_m =  Double.parseDouble(newmap.get(att_art_10_m).toString());
			double art_12 =  Double.parseDouble(newmap.get(att_art_12).toString());
			double art_13 = Double.parseDouble(newmap.get(att_art_13).toString());
			double art_14 = Double.parseDouble(newmap.get(att_art_14).toString());

			Regulation r = new Regulation(code_imu, libelle_zone, insee, date_approbation, libelle_de_base,
					libelle_de_dul, fonctions, top_zac, zonage_coherent, correction_zonage, typ_bande, bande, art_5,
					art_6, art_71, art_72, art_73, art_74, art_8, art_9, art_10_top, art_10, art_10_m, art_12, art_13,
					art_14);

			List<Regulation> lRegulation = new ArrayList<>();
			lRegulation.add(r);
			lMap.put(newmap.get(PARCELLE_ID).toString(), lRegulation);

			// System.out.println(r.toString());

				int fonctions_2 = Integer.parseInt(newmap.get(att_fonctions_2).toString());
				int art_5_2 = Integer.parseInt(newmap.get(att_art_5_2).toString());
				double art_6_2 = Double.parseDouble(newmap.get(att_art_6_2).toString());
				int art_71_2 = Integer.parseInt(newmap.get(att_art_71_2).toString());
				double art_72_2 = Double.parseDouble(newmap.get(att_art_72_2).toString());
				double art_73_2 = Double.parseDouble(newmap.get(att_art_73_2).toString());
				int art_74_2 = Integer.parseInt(newmap.get(att_art_74_2).toString());
				double art_8_2 = Double.parseDouble(newmap.get(att_art_8_2).toString());
				double art_9_2 = Double.parseDouble(newmap.get(att_art_9_2).toString());
				int art_10_top_2 = Integer.parseInt(newmap.get(att_art_10_top_2).toString());
				double art_10_2 =  Double.parseDouble(newmap.get(att_art_10_2).toString()); // ATTENTION
																						// A
																						// CHANGER
				double art_10_m_2 =  Double.parseDouble(newmap.get(att_art_10_m_2).toString());
				double art_12_2 = Double.parseDouble(newmap.get(att_art_12_2).toString());
				double art_13_2 = Double.parseDouble(newmap.get(att_art_13_2).toString());
				double art_14_2 = Double.parseDouble(newmap.get(att_art_14_2).toString());

				Regulation r2 = new Regulation(code_imu, libelle_zone, insee, date_approbation, libelle_de_base,
						libelle_de_dul, fonctions_2, top_zac, zonage_coherent, correction_zonage, typ_bande, bande,
						art_5_2, art_6_2, art_71_2, art_72_2, art_73_2, art_74_2, art_8_2, art_9_2, art_10_top_2,
						art_10_2, art_10_m_2, art_12_2, art_13_2, art_14_2);

				//		System.out.println(r2.toString());

				lRegulation.add(r2);
			

		//	System.out.println("*************************************************");

		}
		
		in.close();

		return lMap;

	}

	public static void transfer(File folder, Map<String, List<Regulation>> regulationMap) {

		File[] files = folder.listFiles();

		for (File file : files) {

			if (file.isDirectory()) {

				File parcelle = new File(file.getAbsolutePath() + "/parcelle.shp");

				if (!parcelle.exists()) {
					System.out.println("File does not exist : " + parcelle.getAbsolutePath());
					continue;
				}
				
				String[] tab = file.getAbsolutePath().split("/");
				
				transferParcelleFile(parcelle,  regulationMap , Integer.parseInt(tab[tab.length-1]));
				

			}

		}
	}

	public static void transferParcelleFile(File parcelle,Map<String, List<Regulation>>  regulationMap, int imu) {
		
		IFeatureCollection<IFeature> featC = ShapefileReader.read(parcelle.getAbsolutePath());
		
		for(IFeature feat: featC){
			String id = feat.getAttribute(PARCELLE_ID).toString();
			
			List<Regulation> lReg = regulationMap.get(id);
			
			if(lReg == null){
				
			 System.out.println("Regulation not found : " + parcelle + "   id : "  + id);
				
			}
			
			


			AttributeManager.addAttribute(feat, att_insee, imu, "Integer");
			AttributeManager.addAttribute(feat, att_imu, imu, "Integer");
			AttributeManager.addAttribute(feat, att_libelle_zone, lReg.get(0).getLibelle_zone(), "String");
			AttributeManager.addAttribute(feat, att_libelle_zone, lReg.get(0).getInsee(), "Integer");
			AttributeManager.addAttribute(feat, att_date_approbation, lReg.get(0).getDate_approbation(), "Integer");
			
			
			AttributeManager.addAttribute(feat, att_libelle_de_base, lReg.get(0).getLibelle_de_base(), "String");
			AttributeManager.addAttribute(feat, att_libelle_de_dul, lReg.get(0).getLibelle_de_dul(), "String");
			AttributeManager.addAttribute(feat, att_fonctions, lReg.get(0).getFonctions(), "Integer");
			AttributeManager.addAttribute(feat, att_top_zac, lReg.get(0).getTop_zac(), "Integer");
			AttributeManager.addAttribute(feat, att_zonage_coherent, lReg.get(0).getZonage_coherent(), "Integer");
			AttributeManager.addAttribute(feat, att_correction_zonage, lReg.get(0).getCorrection_zonage(), "Integer");
			AttributeManager.addAttribute(feat, att_typ_bande, lReg.get(0).getTyp_bande(), "Integer");
			AttributeManager.addAttribute(feat, att_bande, lReg.get(0).getBande(), "Integer");
			AttributeManager.addAttribute(feat, att_art_5, lReg.get(0).getArt_5(), "Double");
			AttributeManager.addAttribute(feat, att_art_6, lReg.get(0).getArt_6(), "Double");
			AttributeManager.addAttribute(feat, att_art_71, lReg.get(0).getArt_71(), "Integer");
			AttributeManager.addAttribute(feat, att_art_72, lReg.get(0).getArt_72(), "Double");
			AttributeManager.addAttribute(feat, att_art_73, lReg.get(0).getArt_73(), "Double");
			AttributeManager.addAttribute(feat, att_art_74, lReg.get(0).getArt_74(), "Integer");
			AttributeManager.addAttribute(feat, att_art_8, lReg.get(0).getArt_8(), "Double");
			AttributeManager.addAttribute(feat, att_art_9, lReg.get(0).getArt_9(), "Double");
			AttributeManager.addAttribute(feat, att_art_10_top, lReg.get(0).getArt_10_top(), "Integer");
			AttributeManager.addAttribute(feat, att_art_10, lReg.get(0).getArt_10(), "Integer");
			AttributeManager.addAttribute(feat, att_art_10_m, lReg.get(0).getArt_10_m(), "Double");
			AttributeManager.addAttribute(feat, att_art_12, lReg.get(0).getArt_12(), "Double");
			AttributeManager.addAttribute(feat, att_art_13, lReg.get(0).getArt_13(), "Double");
			AttributeManager.addAttribute(feat, att_art_14, lReg.get(0).getArt_14(), "Double");
			
	
			AttributeManager.addAttribute(feat, att_fonctions_2, lReg.get(1).getFonctions(), "String");
			AttributeManager.addAttribute(feat, att_art_5_2, lReg.get(1).getArt_5(), "Double");
			AttributeManager.addAttribute(feat, att_art_6_2, lReg.get(1).getArt_6(), "Double");
			AttributeManager.addAttribute(feat, att_art_71_2, lReg.get(1).getArt_71(), "String");
			AttributeManager.addAttribute(feat, att_art_72_2, lReg.get(1).getArt_72(), "Double");
			AttributeManager.addAttribute(feat, att_art_73_2, lReg.get(1).getArt_73(), "Double");
			AttributeManager.addAttribute(feat, att_art_74_2, lReg.get(1).getArt_74(), "String");
			AttributeManager.addAttribute(feat, att_art_8_2, lReg.get(1).getArt_8(), "Double");
			AttributeManager.addAttribute(feat, att_art_9_2, lReg.get(1).getArt_9(), "Double");
			AttributeManager.addAttribute(feat, att_art_10_top_2, lReg.get(1).getArt_10_top(), "String");
			AttributeManager.addAttribute(feat, att_art_10_2, lReg.get(1).getArt_10(), "String");
			AttributeManager.addAttribute(feat, att_art_10_m_2, lReg.get(1).getArt_10_m(), "Double");
			AttributeManager.addAttribute(feat, att_art_12_2, lReg.get(1).getArt_12(), "Double");
			AttributeManager.addAttribute(feat, att_art_13_2, lReg.get(1).getArt_13(), "Double");
			AttributeManager.addAttribute(feat, att_art_14_2, lReg.get(1).getArt_14(), "Double");

			
			
			
			
			

	
			
			
		}
		
		ShapefileWriter.write(featC, parcelle.getAbsolutePath());
		

	}

}
