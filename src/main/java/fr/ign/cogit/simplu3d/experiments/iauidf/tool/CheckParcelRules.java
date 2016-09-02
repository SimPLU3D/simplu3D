package fr.ign.cogit.simplu3d.experiments.iauidf.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.simplu3d.experiments.iauidf.Exec_EPFIF;
import fr.ign.cogit.simplu3d.experiments.iauidf.regulation.Regulation;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.Environnement;

public class CheckParcelRules {
	
	public static void main(String[] args) throws Exception{
		LogManager.getLogManager().reset();
		Exec_EPFIF.init();
		File folder = new File("/home/mickael/data/mbrasebin/donnees/IAUIDF/Est_Ensemble/EstEnsemble");
		test(folder);
		
	}
	

	public static void test(File folder) {

		File[] files = folder.listFiles();

		for (File file : files) {

			
			
			String[] tab = file.getAbsolutePath().split("/");
			
			
			System.out.println("ID zone : " + tab[tab.length-1]);
		
			
			testLoaderEnv(file, Integer.parseInt(tab[tab.length-1]));
			
			if (file.isDirectory()) {

				File parcelle = new File(file.getAbsolutePath() + "/parcelle.shp");
				
				
				

				if (!parcelle.exists()) {
					System.out.println("File does not exist : " + parcelle.getAbsolutePath());
					continue;
				}
				
			
				testFile(parcelle);
				testLoaderRules(parcelle);

			}

		}
	}
	
	private static void testLoaderEnv(File folder, int id){
		try {
			Environnement env= LoaderSHP.loadNoDTM(folder);
			if(env.getCadastralParcels().isEmpty()){
				System.out.println("Numero imu : " + id + "  PARCELLE EMPTY") ;
			}
		} catch (Exception e) {
			System.out.println("Numero imu : " + id);
			e.printStackTrace();
		}
		
	}
	
	private static void testLoaderRules(File parcelle){
		
		IFeatureCollection<IFeature> featC = ShapefileReader.read(parcelle.getAbsolutePath());
		
		
		for(IFeature feat:featC){
			
			List<Regulation> lRegulation = new ArrayList<>();
			
			
			int code_imu = 0; ///l'imu n'est pas dans le .csv Integer.parseInt(newmap.get(att_imu).toString());
			String libelle_zone = feat.getAttribute(ParcelAttributeTransfert.att_libelle_zone).toString(); // LIBELLE_ZONE
			int insee = Integer.parseInt( feat.getAttribute(ParcelAttributeTransfert.att_insee).toString());
			int date_approbation =  10022001 ; //on désactive on en a pas besoin  Integer.parseInt(feat.getAttribute(ParcelAttributeTransfert.att_date_approbation).toString());
			String libelle_de_base = feat.getAttribute(ParcelAttributeTransfert.att_libelle_de_base).toString(); // LIBELLE_DE_BASE
			String libelle_de_dul = feat.getAttribute(ParcelAttributeTransfert.att_libelle_de_dul).toString(); // LIBELLE_DE_DUL
			int fonctions = Integer.parseInt(feat.getAttribute(ParcelAttributeTransfert.att_fonctions).toString());
			int top_zac = Integer.parseInt(feat.getAttribute(ParcelAttributeTransfert.att_top_zac).toString());
			int zonage_coherent = Integer.parseInt(feat.getAttribute(ParcelAttributeTransfert.att_zonage_coherent).toString());
			int correction_zonage = Integer.parseInt(feat.getAttribute(ParcelAttributeTransfert.att_correction_zonage).toString());
			int typ_bande = Integer.parseInt(feat.getAttribute(ParcelAttributeTransfert.att_typ_bande).toString());
			int bande = Integer.parseInt(feat.getAttribute(ParcelAttributeTransfert.att_bande).toString());
			double art_5 = Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_5).toString());
			double art_6 = Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_6).toString());
			int art_71 = Integer.parseInt(feat.getAttribute(ParcelAttributeTransfert.att_art_71).toString());
			double art_72 = Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_72).toString());
			double art_73 = Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_73).toString());
			int art_74 = Integer.parseInt(feat.getAttribute(ParcelAttributeTransfert.att_art_74).toString());
			double art_8 = Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_8).toString());
			double art_9 = Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_9).toString());
			int art_10_top = Integer.parseInt(feat.getAttribute(ParcelAttributeTransfert.att_art_10_top).toString());
			double art_101 = Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_101).toString()); 
			double art_102 =  Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_102).toString());
			double art_12 =  Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_12).toString());
			double art_13 = Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_13).toString());
			double art_14 = Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_14).toString());

			Regulation r = new Regulation(code_imu, libelle_zone, insee, date_approbation, libelle_de_base,
					libelle_de_dul, fonctions, top_zac, zonage_coherent, correction_zonage, typ_bande, bande, art_5,
					art_6, art_71, art_72, art_73, art_74, art_8, art_9, art_10_top, art_101, art_102, art_12, art_13,
					art_14);

		
			lRegulation.add(r);


			// System.out.println(r.toString());

				int fonctions_2 = Integer.parseInt(feat.getAttribute(ParcelAttributeTransfert.att_fonctions_2).toString());
				double art_5_2 =  Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_5_2).toString());
				double art_6_2 = Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_6_2).toString());
				int art_71_2 = Integer.parseInt(feat.getAttribute(ParcelAttributeTransfert.att_art_71_2).toString());
				double art_72_2 = Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_72_2).toString());
				double art_73_2 = Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_73_2).toString());
				int art_74_2 = Integer.parseInt(feat.getAttribute(ParcelAttributeTransfert.att_art_74_2).toString());
				double art_8_2 = Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_8_2).toString());
				double art_9_2 = Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_9_2).toString());
				int art_10_top_2 = Integer.parseInt(feat.getAttribute(ParcelAttributeTransfert.att_art_10_top_2).toString());
				double art_101_2 =  Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_101_2).toString()); // ATTENTION
																						// A
																						// CHANGER
				double art_102_2 =  Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_102_2).toString());
				double art_12_2 = Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_12_2).toString());
				double art_13_2 = Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_13_2).toString());
				double art_14_2 = Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_14_2).toString());

				Regulation r2 = new Regulation(code_imu, libelle_zone, insee, date_approbation, libelle_de_base,
						libelle_de_dul, fonctions_2, top_zac, zonage_coherent, correction_zonage, typ_bande, bande,
						art_5_2, art_6_2, art_71_2, art_72_2, art_73_2, art_74_2, art_8_2, art_9_2, art_10_top_2,
						art_101_2, art_102_2, art_12_2, art_13_2, art_14_2);

				//		System.out.println(r2.toString());

				lRegulation.add(r2);
			
				
				
			
		}
		
		

	}
//newmap.get(ParcelAttributeTransfert.

	private static void testFile(File parcelle) {
		IFeatureCollection<IFeature> featC = ShapefileReader.read(parcelle.getAbsolutePath());
		
		
		for(IFeature feat: featC){
			String id = feat.getAttribute(ParcelAttributeTransfert.PARCELLE_ID).toString();
		
			checkAttribute(feat,id,ParcelAttributeTransfert.att_imu);
			
	
			
checkAttribute(feat,id,ParcelAttributeTransfert.att_imu);
checkAttribute(feat,id,ParcelAttributeTransfert.att_libelle_zone);
checkAttribute(feat,id,ParcelAttributeTransfert.att_insee);
checkAttribute(feat,id,ParcelAttributeTransfert.att_date_approbation);

checkAttribute(feat,id,ParcelAttributeTransfert.att_libelle_de_base);
checkAttribute(feat,id,ParcelAttributeTransfert.att_libelle_de_dul);
checkAttribute(feat,id,ParcelAttributeTransfert.att_fonctions);
checkAttribute(feat,id,ParcelAttributeTransfert.att_top_zac);
checkAttribute(feat,id,ParcelAttributeTransfert.att_zonage_coherent);
checkAttribute(feat,id,ParcelAttributeTransfert.att_correction_zonage);
checkAttribute(feat,id,ParcelAttributeTransfert.att_typ_bande);
checkAttribute(feat,id,ParcelAttributeTransfert.att_bande);
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_5);
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_6);
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_71);
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_72);
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_73);
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_74);
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_8);
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_9);
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_10_top);
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_101);
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_102);
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_12);
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_13);
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_14);
checkAttribute(feat,id,ParcelAttributeTransfert.att_fonctions_2);
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_5_2);
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_6_2 );
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_71_2);
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_72_2 );
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_73_2 );
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_74_2);
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_8_2);
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_9_2 );
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_10_top_2);
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_101_2);
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_102_2);
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_12_2);
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_13_2);
checkAttribute(feat,id,ParcelAttributeTransfert.att_art_14_2);			
			
			
		}
	}
	
	public static void checkAttribute(IFeature feat,String id, String nomAtt){
		if(feat.getAttribute(nomAtt) ==null || feat.getAttribute(nomAtt).toString().isEmpty()  ){
			System.out.println("Problem on parcelle " + id + "with attribute " + nomAtt);
		}
	}
	
	
	
	

}
