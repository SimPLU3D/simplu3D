package fr.ign.cogit.simplu3d.experiments.openmole;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.feature.DefaultFeatureCollections;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.referencing.factory.ReferencingObjectFactory;
import org.geotools.referencing.factory.epsg.FactoryUsingWKT;
import org.geotools.referencing.operation.DefaultMathTransformFactory;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.experiments.iauidf.Exec_EPFIF;
import fr.ign.cogit.simplu3d.experiments.iauidf.regulation.Regulation;
import fr.ign.cogit.simplu3d.experiments.iauidf.tool.ParcelAttributeTransfert;
import fr.ign.cogit.simplu3d.experiments.openmole.msc.TestDatumFactory;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.util.AssignZ;

public class IAUIDFTask {

	public static void main(String[] args) throws Exception {
		File folder = new File("/home/mickael/data/mbrasebin/donnees/IAUIDF/Est_Ensemble/EstEnsemble/75025410/");
		File folderOut = new File("/home/mickael/temp/");
		File parameterFile = new File(
				"/home/mickael/data/mbrasebin/workspace/simPLU3D/simplu3D/src/main/resources/scenario/parameters_iauidf.xml");
		long seed = 42L;
		boolean b = run(folder, folderOut, parameterFile, seed);

		System.out.println(b);

	}

	public static Hints hints = null;

	public static void prepareHints() {
		hints = GeoTools.getDefaultHints();
		hints.put(Hints.CRS_AUTHORITY_FACTORY, FactoryUsingWKT.class);
		hints.put(Hints.CRS_FACTORY, ReferencingObjectFactory.class);
		hints.put(Hints.MATH_TRANSFORM_FACTORY, DefaultMathTransformFactory.class);
		hints.put(Hints.DATUM_FACTORY, TestDatumFactory.class);
		hints.put(Hints.FEATURE_COLLECTIONS, DefaultFeatureCollections.class);
		hints.put(Hints.FILTER_FACTORY, FilterFactoryImpl.class);
	}

	public static boolean run(File folder, File folderOut, File parameterFile, long seed) throws Exception {
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
		if (hints == null)
			prepareHints();
		GeoTools.init(hints);
		AssignZ.DEFAULT_Z = 0;

		// On prépare des paramètres
		Exec_EPFIF.init();

		// On charge l'environnement
		Environnement env = LoaderSHP.loadNoDTM(folder);

		String[] folderSplit = folder.getAbsolutePath().split(File.separator);
		//Identifiant de l'imu courant
		String imu = folderSplit[folderSplit.length - 1];
		// Stocke les résultats en sorties
		IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();
		
		//On charge dans la map les règlements pour chaque parcelle (1 ou 2)
		Map<String, List<Regulation>> map = loadRules(new File(folder + "/parcelle.shp"));

		for (BasicPropertyUnit bPU : env.getBpU()) {

			String id = bPU.getCadastralParcels().get(0).getCode();

			/*
			 * if(! id.equals("930100000V0139")){ continue; }
			 */

			List<Regulation> lR = map.get(id);

			if (lR != null && !lR.isEmpty()) {
				//On simule indépendemment chaque unité foncière
				featC.addAll(Exec_EPFIF.simulationForEachBPU(env, bPU, lR,
						Integer.parseInt(folderSplit[folderSplit.length - 1]), parameterFile));
			} else {
				System.out.println("Regulation not found : " + id);
			}

		}
		//On écrit le fichier en sortie dans le folderout
		String fileName = folderOut + "/simul_" + imu + ".shp";
		System.out.println(fileName);
		ShapefileWriter.write(featC, fileName); // , CRS.decode("EPSG:2154") => supprimé à cause de la compatibilité OSIG/Geotools

		return true;
	}

	private static Map<String, List<Regulation>> loadRules(File parcelle) {

		IFeatureCollection<IFeature> featC = ShapefileReader.read(parcelle.getAbsolutePath());
		Map<String, List<Regulation>> map = new HashMap<>();

		for (IFeature feat : featC) {

			List<Regulation> lRegulation = new ArrayList<>();

			String id = feat.getAttribute(ParcelAttributeTransfert.PARCELLE_ID).toString();

			int code_imu = 0; /// l'imu n'est pas dans le .csv
								/// Integer.parseInt(newmap.get(att_imu).toString());
			String libelle_zone = feat.getAttribute(ParcelAttributeTransfert.att_libelle_zone).toString(); // LIBELLE_ZONE
			int insee = Integer.parseInt(feat.getAttribute(ParcelAttributeTransfert.att_insee).toString());
			int date_approbation = 10022001; // on désactive on en a pas besoin
												// Integer.parseInt(feat.getAttribute(ParcelAttributeTransfert.att_date_approbation).toString());
			String libelle_de_base = feat.getAttribute(ParcelAttributeTransfert.att_libelle_de_base).toString(); // LIBELLE_DE_BASE
			String libelle_de_dul = feat.getAttribute(ParcelAttributeTransfert.att_libelle_de_dul).toString(); // LIBELLE_DE_DUL
			int fonctions = Integer.parseInt(feat.getAttribute(ParcelAttributeTransfert.att_fonctions).toString());
			int top_zac = Integer.parseInt(feat.getAttribute(ParcelAttributeTransfert.att_top_zac).toString());
			int zonage_coherent = Integer
					.parseInt(feat.getAttribute(ParcelAttributeTransfert.att_zonage_coherent).toString());
			int correction_zonage = Integer
					.parseInt(feat.getAttribute(ParcelAttributeTransfert.att_correction_zonage).toString());
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
			double art_102 = Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_102).toString());
			double art_12 = Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_12).toString());
			double art_13 = Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_13).toString());
			double art_14 = Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_14).toString());

			Regulation r = new Regulation(code_imu, libelle_zone, insee, date_approbation, libelle_de_base,
					libelle_de_dul, fonctions, top_zac, zonage_coherent, correction_zonage, typ_bande, bande, art_5,
					art_6, art_71, art_72, art_73, art_74, art_8, art_9, art_10_top, art_101, art_102, art_12, art_13,
					art_14);

			lRegulation.add(r);

			// System.out.println(r.toString());

			if (bande != 0) {

				int fonctions_2 = Integer
						.parseInt(feat.getAttribute(ParcelAttributeTransfert.att_fonctions_2).toString());
				double art_5_2 = Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_5_2).toString());
				double art_6_2 = Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_6_2).toString());
				int art_71_2 = Integer.parseInt(feat.getAttribute(ParcelAttributeTransfert.att_art_71_2).toString());
				double art_72_2 = Double
						.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_72_2).toString());
				double art_73_2 = Double
						.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_73_2).toString());
				int art_74_2 = Integer.parseInt(feat.getAttribute(ParcelAttributeTransfert.att_art_74_2).toString());
				double art_8_2 = Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_8_2).toString());
				double art_9_2 = Double.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_9_2).toString());
				int art_10_top_2 = Integer
						.parseInt(feat.getAttribute(ParcelAttributeTransfert.att_art_10_top_2).toString());
				double art_101_2 = Double
						.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_101_2).toString()); // ATTENTION
				// A
				// CHANGER
				double art_102_2 = Double
						.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_102_2).toString());
				double art_12_2 = Double
						.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_12_2).toString());
				double art_13_2 = Double
						.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_13_2).toString());
				double art_14_2 = Double
						.parseDouble(feat.getAttribute(ParcelAttributeTransfert.att_art_14_2).toString());

				Regulation r2 = new Regulation(code_imu, libelle_zone, insee, date_approbation, libelle_de_base,
						libelle_de_dul, fonctions_2, top_zac, zonage_coherent, correction_zonage, typ_bande, bande,
						art_5_2, art_6_2, art_71_2, art_72_2, art_73_2, art_74_2, art_8_2, art_9_2, art_10_top_2,
						art_101_2, art_102_2, art_12_2, art_13_2, art_14_2);

				// System.out.println(r2.toString());

				lRegulation.add(r2);
			}

			map.put(id, lRegulation);

		}

		return map;

	}

}
