package fr.ign.cogit.simplu3d.experiments.iauidf;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.ign.cogit.simplu3d.util.SimpluParameters;
import fr.ign.cogit.simplu3d.util.SimpluParametersJSON;
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
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.calculation.OrientedBoundingBox;
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

public class Exec {

	public static boolean DEBUG_MODE = true;
	private static Logger log = Logger.getLogger(Exec.class);
	public final static String nomDTM = "MNT_BD3D.asc";
	public static SimpluParameters p;
	public static List<IMultiSurface<IOrientableSurface>> lMS = new ArrayList<>();
	public static List<IMultiSurface<IOrientableSurface>> debugSurface = new ArrayList<>();
	public static List<IMultiCurve<IOrientableCurve>> debugLine = new ArrayList<>();

	// Initialisation des attributs différents du schéma de base
	// et le fichier de paramètre commun à toutes les simulations
	public static void init() throws Exception {
//		RoadReader.ATT_NOM_RUE = "NOM_VOIE_G";
//		RoadReader.ATT_LARGEUR = "LARGEUR";
//		RoadReader.ATT_TYPE = "NATURE";

		CadastralParcelLoader.TYPE_ANNOTATION = 2;
		PredicateIAUIDF.RIGHT_OF_LEFT_FOR_ART_71 = ParcelBoundarySide.LEFT;
	}

	public static void main(String[] args) {
		// Dossier contenant IMU_MANTES_TEST.csv et les sous dossier par code
		// IMU
		String folder = "C:/Users/mbrasebin/Desktop/Zone5/";
		String csvFile = folder + "regles.csv";
		// Chargement des règlement par code IMU (on peut avoir plusieurs
		// réglements pour un code IMU du fait des bandes)
		Map<Integer, List<Regulation>> mapReg = null;
		try {
			mapReg = Regulation.loadRegulationSet(csvFile);
			// Fonction de test : chargement de la réglementation :
			testLoadedRegulation(mapReg);
			// TODO : gérer les attributs indépendemment de la casse.
			// Initialisation des noms d'attributs
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// On traite indépendamment chaque zone imu
		for (int imu : mapReg.keySet()) {

			if (imu != 78020280 && imu != 78021045 && imu != 78031977) {
				continue;
			}

			System.out.println("Numéro imu : " + imu);

			try {
				boolean simul = simulRegulationByIMU(imu, mapReg.get(imu), folder + imu + "/");

				if (!simul) {
					log.warn("--Probleme pour la simulation : " + imu);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public static IFeatureCollection<IFeature> simulSimpleRegulationByBasicPropertyUnit(Environnement env, int imu,
			Regulation r1, Regulation r2) throws Exception {
		IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();
		// On parcourt chaque parcelle et on applique la simulation dessus
		int nbBPU = env.getBpU().size();
		for (int i = 0; i < nbBPU; i++) {

			// if(env.getBpU().get(i).getId() != 0) continue;

			System.out.println("Parcelle numéro : " + env.getBpU().get(i).getId());
			System.out.println(env.getBpU().get(i).getGeom());
			IFeatureCollection<IFeature> featCTemp = simulRegulationByBasicPropertyUnit(env, env.getBpU().get(i), imu,
					r1, r2);
			System.out.println("Nombre de blocs : " + featCTemp.size());

			if (featCTemp != null) {
				featC.addAll(featCTemp);
			}

		}

		return featC;

	}

	/**
	 * Simulations portant sur chaque zone IMU
	 * 
	 * @param imu
	 * @param lReg
	 * @return
	 * @throws Exception
	 */
	public static boolean simulRegulationByIMU(int imu, List<Regulation> lReg, String folderImu) throws Exception {
		if (DEBUG_MODE) {
			debugSurface = new ArrayList<>();
			debugLine = new ArrayList<>();
		}

		// On met les règlements dans l'ordre des bandes (le premier est la
		// première bande, le second la seconde ou null s'il n'y en a pas)
		List<Regulation> orderedRegulation = orderedRegulation(lReg, imu);
		if (orderedRegulation == null) {
			return false;
		}
		Regulation r1 = orderedRegulation.get(0);
		Regulation r2 = orderedRegulation.get(1);

		System.out.println("R1 : " + r1);
		System.out.println("R2 : " + r2);

		// Stocke les résultats en sorties
		IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();

		boolean isOk = true;

		if (r1 != null && r1.getArt_71() == 2 || r2 != null && r2.getArt_71() == 2) {

			// Cas ou les bâtiments se collent d'un des 2 côtés, on simule les 2
			// côtés et on regarde pour chaque parcelle quelle est la meilleure
			// :

			PredicateIAUIDF.RIGHT_OF_LEFT_FOR_ART_71 = ParcelBoundarySide.RIGHT;

			IFeatureCollection<IFeature> featC1 = new FT_FeatureCollection<>();

			Environnement env = LoaderSHP.load(new File(folderImu), new FileInputStream(folderImu + nomDTM));

			featC1.addAll(simulSimpleRegulationByBasicPropertyUnit(env, imu, r1, r2));

			PredicateIAUIDF.RIGHT_OF_LEFT_FOR_ART_71 = ParcelBoundarySide.LEFT;

			IFeatureCollection<IFeature> featC2 = new FT_FeatureCollection<>();

			env = LoaderSHP.load(new File(folderImu), new FileInputStream(folderImu + nomDTM));

			featC2.addAll(simulSimpleRegulationByBasicPropertyUnit(env, imu, r1, r2));

			featC = fusionne(featC1, featC2);

		} else {

			// On instancie l'environnement associé à l'IMU
			Environnement env = LoaderSHP.load(new File(folderImu), new FileInputStream(folderImu + nomDTM));

			featC.addAll(simulSimpleRegulationByBasicPropertyUnit(env, imu, r1, r2));

		}

		System.out.println("-- Nombre de surface : " + debugSurface.size());
		String fileName = folderImu + "simul_" + imu + ".shp";
		System.out.println(fileName);
		ShapefileWriter.write(featC, fileName, CRS.decode("EPSG:2154"));
		if (DEBUG_MODE) {
			saveShapeTest(folderImu);
		}

		return isOk;
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

	private static boolean initiateSimulationParamters(Regulation r1, Regulation r2) throws Exception {
		// Chargement du fichier de configuration
		String folderName = BasicSimulator.class.getClassLoader().getResource("scenario/").getPath();
		String fileName = "parameters_iauidf.xml";
		p = new SimpluParametersJSON(new File(folderName + fileName));

		if (r2 != null) {

			double newHeightMax = Math.max(r1.getArt_10_m(), r2.getArt_10_m());

			if (newHeightMax != 99.0) {
				p.set("maxheight", newHeightMax);
			}

		} else {
			if (r1.getArt_10_m() != 99) {
				p.set("maxheight", r1.getArt_10_m());
			}

		}

		if (p.getDouble("maxheight") < p.getDouble("minheight")) {
			return false;
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

		double longueur1 = Double.NEGATIVE_INFINITY;

		if (r1.getGeomBande() != null && !r1.getGeomBande().isEmpty()) {
			OrientedBoundingBox oBB1 = new OrientedBoundingBox(r1.getGeomBande());

			longueur1 = oBB1.getLength();
		}

		if (r2 != null) {
			OrientedBoundingBox oBB2 = new OrientedBoundingBox(r2.getGeomBande());

			double longueur2 = oBB2.getLength();

			p.set("maxlen", Math.min(p.getDouble("maxlen"), Math.max(longueur1, longueur2)));

			p.set("maxwid", Math.min(p.getDouble("maxwid"), Math.max(longueur1, longueur2)));

		} else {
			p.set("maxlen", Math.min(p.getDouble("maxlen"), longueur1));
			p.set("maxwid", Math.min(p.getDouble("maxwid"), longueur1));

		}

		if (p.getDouble("maxlen") < p.getDouble("minlen")) {
			return false;
		}

		if (p.getDouble("maxwid") < p.getDouble("minwid")) {
			return false;
		}

		p.set("temp", Math.min(p.getDouble("temp"),
				p.getDouble("maxlen") * p.getDouble("maxlen") * p.getDouble("maxheight")));

		return true;
	}

	/**
	 * 
	 * @param bPU
	 * @param imu
	 * @return
	 * @throws Exception
	 */
	public static IFeatureCollection<IFeature> simulRegulationByBasicPropertyUnit(Environnement env,
			BasicPropertyUnit bPU, int imu, Regulation r1, Regulation r2) throws Exception {
		// Stocke les résultats en sorties
		IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();
		// //////On découpe la parcelle en bande en fonction des règlements

		// ART_5 Superficie minimale 88= non renseignable, 99= non réglementé
		double r_art5 = r1.getArt_5();
		if (r_art5 != 99.0) {
			if (bPU.getPol2D().area() < r_art5) {
				return featC;
			}
		}
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

		// initialisation des paramètres de simulations
		if (!initiateSimulationParamters(r1, r2)) {
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

		for (GraphVertex<Cuboid> v : cc.getGraph().vertexSet()) {

			IFeature feat = new DefaultFeature(v.getValue().generated3DGeom());
			// On ajoute des attributs aux entités (dimension des objets)
			AttributeManager.addAttribute(feat, "Longueur", Math.max(v.getValue().length, v.getValue().width),
					"Double");
			AttributeManager.addAttribute(feat, "Largeur", Math.min(v.getValue().length, v.getValue().width), "Double");
			AttributeManager.addAttribute(feat, "Hauteur", v.getValue().height, "Double");
			AttributeManager.addAttribute(feat, "Rotation", v.getValue().orientation, "Double");
			AttributeManager.addAttribute(feat, "ID_PARC", bPU.getId(), "Integer");
			featC.add(feat);
		}
		return featC;
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
