package fr.ign.cogit.simplu3d.experiments.iauidf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IPoint;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.exec.BasicSimulator;
import fr.ign.cogit.simplu3d.experiments.iauidf.predicate.PredicateIAUIDF;
import fr.ign.cogit.simplu3d.experiments.iauidf.regulation.Regulation;
import fr.ign.cogit.simplu3d.experiments.iauidf.tool.BandProduction;
import fr.ign.cogit.simplu3d.importer.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.model.ParcelBoundarySide;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.mix.MultipleBuildingsCuboid;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;
import fr.ign.parameters.Parameters;

/**
 * Simulator for EPFIF comparator
 * 
 * @author mickaelbrasebin
 *
 */
public class Exec_EPFIF {

	public static boolean DEBUG_MODE = false;
	private static Logger log = Logger.getLogger(Exec_EPFIF.class);

	public static List<IMultiSurface<IOrientableSurface>> lMS = new ArrayList<>();
	public static List<IMultiSurface<IOrientableSurface>> debugSurface = new ArrayList<>();
	public static List<IMultiCurve<IOrientableCurve>> debugLine = new ArrayList<>();

	public final static String folder = "/home/mickael/data/mbrasebin/donnees/IAUIDF/Nouveaux_tests_comparatifs/Eval_EPF_2/";
	public final static String file_rules = folder + "rules.csv";
	public final static String out_folder = folder + "out/";

	public static void main(String[] args) throws Exception {

		MultipleBuildingsCuboid.ALLOW_INTERSECTING_CUBOID = false;

		init();
		// Dossier contenant IMU_MANTES_TEST.csv et les sous dossier par code
		// IMU

		Map<Integer, List<Regulation>> regulation = prepareRegulation();

		// On traite indépendamment chaque zone imu
		for (int currentImu : regulation.keySet()) {

			System.out.println("Numéro imu : " + currentImu);
	

			try {

				List<Regulation> lR = regulation.get(currentImu);

				String folderName = BasicSimulator.class.getClassLoader().getResource("scenario/").getPath();
				String fileName = "parameters_iauidf.xml";
				File f = new File(folderName + fileName);

				boolean simul = simulRegulationByIMU(currentImu, folder + currentImu + "/", lR, f);

				if (!simul) {
					log.warn("--Probleme pour la simulation : " + currentImu);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	// Initialisation des attributs différents du schéma de base
	// et le fichier de paramètre commun à toutes les simulations
	public static void init() throws Exception {

		CadastralParcelLoader.TYPE_ANNOTATION = 2;

		PredicateIAUIDF.RIGHT_OF_LEFT_FOR_ART_71 = ParcelBoundarySide.LEFT;
	}

	public static Map<Integer, List<Regulation>> prepareRegulation() throws IOException {

		File f = new File(file_rules);

		Map<Integer, List<Regulation>> lMap = new HashMap<>();
		if (!f.exists()) {
			return null;
		}

		// On lit le fichier
		BufferedReader in = new BufferedReader(new FileReader(f));
		String line = in.readLine();

		Object[] listItem = line.split(";");

		// On traite chaque ligne
		while ((line = in.readLine()) != null) {

			Object[] listValue = line.split(";");

			Map<String, Object> newmap = new HashMap<>();

			for (int i = 0; i < listValue.length; i++) {
				newmap.put(listItem[i].toString(), listValue[i]);

			}

			System.out.println("*************************************************");
			System.out.println("*******************" + listValue[0] + "******************");

			int code_imu = Integer.parseInt(newmap.get("IMU").toString());
			String libelle_zone = newmap.get("VILLE").toString(); // LIBELLE_ZONE
			int insee = Integer.parseInt(newmap.get("INSEE").toString());
			int date_approbation = Integer.parseInt(newmap.get("INSEE").toString());
			String libelle_de_base = newmap.get("VILLE").toString(); // LIBELLE_DE_BASE
			String libelle_de_dul = newmap.get("VILLE").toString(); // LIBELLE_DE_DUL
			int fonctions = Integer.parseInt(newmap.get("FONCTIONS").toString());
			int top_zac = Integer.parseInt(newmap.get("TOP_ZAC").toString());
			int zonage_coherent = Integer.parseInt(newmap.get("ZONAGE_COHERENT").toString());
			int correction_zonage = Integer.parseInt(newmap.get("CORRECTION_ZONAGE").toString());
			int typ_bande = Integer.parseInt(newmap.get("TYP_BANDE").toString());
			int bande = Integer.parseInt(newmap.get("BANDE").toString());
			int art_5 = Integer.parseInt(newmap.get("ART_5").toString());
			double art_6 = Double.parseDouble(newmap.get("ART_6").toString());
			int art_71 = Integer.parseInt(newmap.get("ART_71").toString());
			double art_72 = Double.parseDouble(newmap.get("ART_72").toString());
			double art_73 = Double.parseDouble(newmap.get("ART_73").toString());
			int art_74 = Integer.parseInt(newmap.get("ART_74").toString());
			double art_8 = Double.parseDouble(newmap.get("ART_8").toString());
			double art_9 = Double.parseDouble(newmap.get("ART_9").toString());
			int art_10_top = Integer.parseInt(newmap.get("ART_10_TOP").toString());
			int art_101 = Integer.parseInt(newmap.get("ART_10").toString()); // ATTENTION
																				// A
																				// CHANGER
			int art_102 = Integer.parseInt(newmap.get("ART_10").toString());
			double art_12 = Double.parseDouble(newmap.get("ART_12").toString());
			double art_13 = Double.parseDouble(newmap.get("ART_13").toString());
			double art_14 = Double.parseDouble(newmap.get("ART_14").toString());

			Regulation r = new Regulation(code_imu, libelle_zone, insee, date_approbation, libelle_de_base,
					libelle_de_dul, fonctions, top_zac, zonage_coherent, correction_zonage, typ_bande, bande, art_5,
					art_6, art_71, art_72, art_73, art_74, art_8, art_9, art_10_top, art_101, art_102, art_12, art_13,
					art_14);

			List<Regulation> lRegulation = new ArrayList<>();
			lRegulation.add(r);
			lMap.put(code_imu, lRegulation);

			System.out.println(r.toString());

			if (bande != 0) {
				int fonctions_2 = Integer.parseInt(newmap.get("FONCTIONS_2").toString());
				int art_5_2 = Integer.parseInt(newmap.get("ART_5_2").toString());
				double art_6_2 = Double.parseDouble(newmap.get("ART_6_2").toString());
				int art_71_2 = Integer.parseInt(newmap.get("ART_71_2").toString());
				double art_72_2 = Double.parseDouble(newmap.get("ART_72_2").toString());
				double art_73_2 = Double.parseDouble(newmap.get("ART_73_2").toString());
				int art_74_2 = Integer.parseInt(newmap.get("ART_74_2").toString());
				double art_8_2 = Double.parseDouble(newmap.get("ART_8_2").toString());
				double art_9_2 = Double.parseDouble(newmap.get("ART_9_2").toString());
				int art_10_top_2 = Integer.parseInt(newmap.get("ART_10_TOP_1_2").toString());
				int art_101_2 = Integer.parseInt(newmap.get("ART_10_2_2").toString()); // ATTENTION
																						// A
																						// CHANGER
				int art_102_2 = Integer.parseInt(newmap.get("ART_10_2_2").toString());
				double art_12_2 = Double.parseDouble(newmap.get("ART_12_2").toString());
				double art_13_2 = Double.parseDouble(newmap.get("ART_13_2").toString());
				double art_14_2 = Double.parseDouble(newmap.get("ART_14_2").toString());

				Regulation r2 = new Regulation(code_imu, libelle_zone, insee, date_approbation, libelle_de_base,
						libelle_de_dul, fonctions_2, top_zac, zonage_coherent, correction_zonage, typ_bande, bande,
						art_5_2, art_6_2, art_71_2, art_72_2, art_73_2, art_74_2, art_8_2, art_9_2, art_10_top_2,
						art_101_2, art_102_2, art_12_2, art_13_2, art_14_2);

				System.out.println(r2.toString());

				lRegulation.add(r2);
			}

			System.out.println("*************************************************");

		}

		in.close();

		return lMap;

	}

	/**
	 * Simulations portant sur chaque zone IMU
	 * 
	 * @param imu
	 * @param lReg
	 * @return
	 * @throws Exception
	 */
	public static boolean simulRegulationByIMU(int imu, String folderImu, List<Regulation> lRegulation, File fParam)
			throws Exception {
		if (DEBUG_MODE) {
			debugSurface = new ArrayList<>();
			debugLine = new ArrayList<>();
		}
		Environnement env = LoaderSHP.loadNoDTM(new File(folderImu));

		// Stocke les résultats en sorties
		IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();

		boolean isOk = true;

		for (BasicPropertyUnit bPU : env.getBpU()) {

			if (bPU.getCadastralParcels().get(0).hasToBeSimulated()) {
				featC.addAll(simulationForEachBPU(env, bPU, lRegulation, imu, fParam));
			}

		}
		System.out.println("-- Nombre de surfaces : " + debugSurface.size());
		String fileName = out_folder + "simul_" + imu + ".shp";
		System.out.println(fileName);
		ShapefileWriter.write(featC, fileName, CRS.decode("EPSG:2154"));
		if (DEBUG_MODE) {
			saveShapeTest(folderImu);
		}

		return isOk;
	}

	public static IFeatureCollection<IFeature> simulationForEachBPU(Environnement env, BasicPropertyUnit bPU,
			List<Regulation> lRegulation, int imu, File fParam) throws Exception {

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
		if (r1 != null && r1.getArt_71() == 2 || r2 != null && r2.getArt_71() == 2) {

			// Cas ou les bâtiments se collent d'un des 2 côtés, on simule
			// les 2
			// côtés et on regarde pour chaque parcelle quelle est la
			// meilleure
			// :

			PredicateIAUIDF.RIGHT_OF_LEFT_FOR_ART_71 = ParcelBoundarySide.RIGHT;

			IFeatureCollection<IFeature> featC1 = new FT_FeatureCollection<>();

			featC1.addAll(simulRegulationByBasicPropertyUnit(env, bPU, imu, r1, r2, fParam));

			PredicateIAUIDF.RIGHT_OF_LEFT_FOR_ART_71 = ParcelBoundarySide.LEFT;

			IFeatureCollection<IFeature> featC2 = new FT_FeatureCollection<>();

			featC2.addAll(simulRegulationByBasicPropertyUnit(env, bPU, imu, r1, r2, fParam));

			featC.addAll(fusionne(featC1, featC2));

		} else {

			featC.addAll(simulRegulationByBasicPropertyUnit(env, bPU, imu, r1, r2, fParam));

		}
		return featC;
	}

	public static IFeature retrieveFeat(IFeatureCollection<IFeature> featColl, BasicPropertyUnit bPU) {

		for (IFeature feat : featColl) {

			if (feat.getGeom().coord().isEmpty()) {
				continue;
			}

			IPoint dp = new GM_Point(feat.getGeom().centroid());

			if (bPU.getpol2D().contains(dp)) {
				return feat;
			}

		}

		return null;
	}

	private static IFeatureCollection<IFeature> fusionne(IFeatureCollection<IFeature> featC1,
			IFeatureCollection<IFeature> featC2) {

		IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();

		while (!(featC1.isEmpty()) && !featC2.isEmpty()) {

			IFeature featTemp = featC1.get(0);
			featC1.remove(0);

			int currentID = Integer.parseInt(featTemp.getAttribute("ID_PARC").toString());

			List<IFeature> lF1 = new ArrayList<>();
			lF1.add(featTemp);

			int nbElem = featC1.size();

			for (int i = 0; i < nbElem; i++) {

				featTemp = featC1.get(i);

				if (Integer.parseInt(featTemp.getAttribute("ID_PARC").toString()) == currentID) {

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

				if (Integer.parseInt(featTemp.getAttribute("ID_PARC").toString()) == currentID) {

					lF2.add(featTemp);
					featC2.remove(i);
					i--;
					nbElem2--;
				}

			}

			double contrib1 = 0;
			for (IFeature feat : lF1) {
				contrib1 = contrib1
						+ feat.getGeom().area() * Double.parseDouble(feat.getAttribute("Hauteur").toString());
			}

			double contrib2 = 0;
			for (IFeature feat : lF2) {
				contrib2 = contrib2
						+ feat.getGeom().area() * Double.parseDouble(feat.getAttribute("Hauteur").toString());
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

	private static Parameters initiateSimulationParamters(Regulation r1, Regulation r2, File f) throws Exception {
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

		System.out.println("Hauteur " + p.getDouble("minheight") + " " + p.getDouble("maxheight"));

		if (r2 != null) {
			if ((r1.getArt_74() == 0) && (r2.getArt_74() == 0) && (r2.getArt_14() != 99) && (r1.getArt_14() != 99)
					&& (p.getDouble("maxheight") != 99.0)) {
				p.set("minheight", p.getDouble("maxheight") - 0.1);
			}
		} else {
			if ((r1.getArt_74() == 0) && (r1.getArt_14() != 99) && (p.getDouble("maxheight") != 99.0)) {
				p.set("minheight", p.getDouble("maxheight") - 0.1);
			}
		}

		/*
		 * double longueur1 = Double.NEGATIVE_INFINITY;
		 * 
		 * if (r1.getGeomBande() != null && !r1.getGeomBande().isEmpty()) {
		 * OrientedBoundingBox oBB1 = new
		 * OrientedBoundingBox(r1.getGeomBande());
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

		p.set("temp", Math.min(p.getDouble("temp"),
				p.getDouble("maxlen") * p.getDouble("maxlen") * p.getDouble("maxheight")));

		return p;
	}

	/**
	 * 
	 * @param bPU
	 * @param imu
	 * @param lReg
	 * @return
	 * @throws Exception
	 */
	public static IFeatureCollection<IFeature> simulRegulationByBasicPropertyUnit(Environnement env,
			BasicPropertyUnit bPU, int imu, Regulation r1, Regulation r2, File fParam) throws Exception {
		// Stocke les résultats en sorties
		IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();
		// //////On découpe la parcelle en bande en fonction des règlements

		// ART_5 Superficie minimale 88= non renseignable, 99= non réglementé
		// Si ce n'est pas respecté on ne fait même pas de simulation
		double r_art5 = r1.getArt_5();
		if (r_art5 != 99) {
			if (bPU.getpol2D().area() < r_art5) {
				return featC;
			}
		}
		// Processus découpant la zone dans laquelle on met les bâtiments à
		// partir des règles
		BandProduction bP = new BandProduction(bPU, r1, r2);

		if (r2 == null || r2.getGeomBande() == null || r2.getGeomBande().isEmpty()) {
			r2 = null;
			System.out.println("Une seule bande");
		}

		if (DEBUG_MODE) {

			if (r1 != null && r1.getGeomBande() != null) {
				debugSurface.add(r1.getGeomBande());
			}

			if (r2 != null && r2.getGeomBande() != null) {
				debugSurface.add(r2.getGeomBande());
			}
			debugLine.add(bP.getLineRoad());
		}

		Parameters p = initiateSimulationParamters(r1, r2, fParam);
		// initialisation des paramètres de simulation
		if (p == null) {
			return featC;
		}

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
			AttributeManager.addAttribute(feat, "Longueur", Math.max(v.getValue().length, v.getValue().width),
					"Double");
			AttributeManager.addAttribute(feat, "Largeur", Math.min(v.getValue().length, v.getValue().width), "Double");
			AttributeManager.addAttribute(feat, "Hauteur", v.getValue().height, "Double");
			AttributeManager.addAttribute(feat, "Rotation", v.getValue().orientation, "Double");
			AttributeManager.addAttribute(feat, "ID_PARC", bPU.getId(), "Integer");
			double area = 0;

			if (v.getValue().getFootprint() != null && (!v.getValue().getFootprint().isEmpty())) {
				area = v.getValue().getFootprint().area();
			}

			AttributeManager.addAttribute(feat, "Aire", area, "Double");

			featC.add(feat);
		}
		return featC;
	}

	// Affiche les réglements chargés
	public static void testLoadedRegulation(Map<Integer, List<Regulation>> mapReg) {
		for (int key : mapReg.keySet()) {
			log.debug("-----key----------");
			for (Regulation reg : mapReg.get(key)) {
				log.debug(reg.toString());
			}
		}
	}

	private static void saveShapeTest(String folderImu) throws NoSuchAuthorityCodeException, FactoryException {
		IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();
		// Petit script pour sauvegarder les bandes pour vérification
		// Le fichier généré se trouve dans le dossier imu
		for (IMultiSurface<IOrientableSurface> iS : debugSurface) {
			if (iS != null && iS.isValid() && !iS.isEmpty()) {
				featC.add(new DefaultFeature(iS));
			}
		}
		ShapefileWriter.write(featC, folderImu + "generatedBand.shp", CRS.decode("EPSG:2154"));
		IFeatureCollection<IFeature> featC2 = new FT_FeatureCollection<>();
		// Petit script pour sauvegarder les bandes pour vérification
		// Le fichier généré se trouve dans le dossier imu
		for (IMultiCurve<IOrientableCurve> iS : debugLine) {
			if (iS != null && iS.isValid() && !iS.isEmpty()) {
				featC2.add(new DefaultFeature(iS));
			}
		}
		ShapefileWriter.write(featC2, folderImu + "generatedLine.shp", CRS.decode("EPSG:2154"));
	}

}
