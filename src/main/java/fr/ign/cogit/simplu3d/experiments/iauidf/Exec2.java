package fr.ign.cogit.simplu3d.experiments.iauidf;

import java.io.File;
import java.util.ArrayList;
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
import fr.ign.cogit.geoxygene.sig3d.calculation.OrientedBoundingBox;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.exec.BasicSimulator;
import fr.ign.cogit.simplu3d.experiments.iauidf.predicate.PredicateIAUIDF;
import fr.ign.cogit.simplu3d.experiments.iauidf.regulation.Regulation;
import fr.ign.cogit.simplu3d.experiments.iauidf.tool.BandProduction;
import fr.ign.cogit.simplu3d.importer.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.importer.RoadImporter;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary.SpecificCadastralBoundarySide;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.mix.MultipleBuildingsCuboid;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;
import fr.ign.parameters.Parameters;

public class Exec2 {

	public static boolean DEBUG_MODE = true;
	private static Logger log = Logger.getLogger(Exec2.class);
	public static Parameters p;
	public static List<IMultiSurface<IOrientableSurface>> lMS = new ArrayList<>();
	public static List<IMultiSurface<IOrientableSurface>> debugSurface = new ArrayList<>();
	public static List<IMultiCurve<IOrientableCurve>> debugLine = new ArrayList<>();

	public final static String NAME_FILE_PARCE = "Parcelles_Zonages_EE_2014_OK.shp";
	public final static String folder = "/home/mickael/data/mbrasebin/donnees/IAUIDF/IMU_ee/";

	// Initialisation des attributs différents du schéma de base
	// et le fichier de paramètre commun à toutes les simulations
	public static void init() throws Exception {
		RoadImporter.ATT_NOM_RUE = "NOM_VOIE_G";
		RoadImporter.ATT_LARGEUR = "LARGEUR";
		RoadImporter.ATT_TYPE = "NATURE";

		LoaderSHP.NOM_FICHIER_PARCELLE = "parcelle.shp";

		CadastralParcelLoader.TYPE_ANNOTATION = 2;

		PredicateIAUIDF.RIGHT_OF_LEFT_FOR_ART_71 = SpecificCadastralBoundarySide.LEFT;
	}

	public static void main(String[] args) throws Exception {
		init();
		// Dossier contenant IMU_MANTES_TEST.csv et les sous dossier par code
		// IMU

		List<Integer> listimu = prepareFiles(folder);

		// On traite indépendamment chaque zone imu
		for (int currentImu : listimu) {

			if (currentImu != 75021669) {
				continue;
			}

			System.out.println("Numéro imu : " + currentImu);

			try {
				boolean simul = simulRegulationByIMU(currentImu, folder + currentImu + "/");

				if (!simul) {
					log.warn("--Probleme pour la simulation : " + currentImu);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Simulations portant sur chaque zone IMU
	 * 
	 * @param imu
	 * @param lReg
	 * @return
	 * @throws Exception
	 */
	public static boolean simulRegulationByIMU(int imu, String folderImu) throws Exception {
		if (DEBUG_MODE) {
			debugSurface = new ArrayList<>();
			debugLine = new ArrayList<>();
		}
		Environnement env = LoaderSHP.loadNoDTM(new File(folderImu));

		// Stocke les résultats en sorties
		IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();

		boolean isOk = true;

		IFeatureCollection<IFeature> featParcel = ShapefileReader.read(folder + NAME_FILE_PARCE);

		System.out.println(folderImu + LoaderSHP.NOM_FICHIER_PARCELLE);
		System.out.println("Number of parcels : " + featParcel.size());

		for (BasicPropertyUnit bPU : env.getBpU()) {

			if (!bPU.getCadastralParcel().get(0).hasToBeSimulated()) {
				continue;
			}

			if (bPU.getCadastralParcel().get(0).getArea() < 5) {
				System.out.println("Probablement une erreur de carte topologique.");
				continue;
			}

			IFeature feat = retrieveFeat(featParcel, bPU);

			if (feat == null) {

				continue;
			}

			Regulation r1 = new Regulation(feat);
			Regulation r2 = null;

			if (0 != ((int) Double.parseDouble(feat.getAttribute("2_BANDE").toString()))) {

				r2 = new Regulation(Regulation.returnObjTab2(feat));
			}

			System.out.println("R1 : " + r1);

			if (r2 != null) {

				System.out.println("R2 : " + r2);

			}
			if (r1 != null && r1.getArt_71() == 2 || r2 != null && r2.getArt_71() == 2) {

				// Cas ou les bâtiments se collent d'un des 2 côtés, on simule
				// les 2
				// côtés et on regarde pour chaque parcelle quelle est la
				// meilleure
				// :

				PredicateIAUIDF.RIGHT_OF_LEFT_FOR_ART_71 = SpecificCadastralBoundarySide.RIGHT;

				IFeatureCollection<IFeature> featC1 = new FT_FeatureCollection<>();

				featC1.addAll(simulRegulationByBasicPropertyUnit(env, bPU, imu, r1, r2));

				PredicateIAUIDF.RIGHT_OF_LEFT_FOR_ART_71 = SpecificCadastralBoundarySide.LEFT;

				IFeatureCollection<IFeature> featC2 = new FT_FeatureCollection<>();

				env = LoaderSHP.loadNoDTM(new File(folderImu));

				featC2.addAll(simulRegulationByBasicPropertyUnit(env, bPU, imu, r1, r2));

				featC.addAll(fusionne(featC1, featC2));

			} else {

				featC.addAll(simulRegulationByBasicPropertyUnit(env, bPU, imu, r1, r2));

			}

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

	private static boolean initiateSimulationParamters(Regulation r1, Regulation r2) throws Exception {
		// Chargement du fichier de configuration
		String folderName = BasicSimulator.class.getClassLoader().getResource("scenario/").getPath();
		String fileName = "parameters_iauidf.xml";
		p = Parameters.unmarshall(new File(folderName + fileName));

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
	 * @param lReg
	 * @return
	 * @throws Exception
	 */
	public static IFeatureCollection<IFeature> simulRegulationByBasicPropertyUnit(Environnement env,
			BasicPropertyUnit bPU, int imu, Regulation r1, Regulation r2) throws Exception {
		// Stocke les résultats en sorties
		IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();
		// //////On découpe la parcelle en bande en fonction des règlements

		// ART_5 Superficie minimale 88= non renseignable, 99= non réglementé
		int r_art5 = r1.getArt_5();
		if (r_art5 != 99) {
			if (bPU.getpol2D().area() < r_art5) {
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

	public static List<Integer> prepareFiles(String folder) {
		List<Integer> lS = new ArrayList<>();
		File f = new File(folder);

		File[] fTab = f.listFiles();

		int nbF = fTab.length;

		for (int i = 0; i < nbF; i++) {
			File fTemp = fTab[i];
			if (fTemp.isDirectory()) {
				lS.add(Integer.parseInt(fTemp.getName()));
			}
		}

		return lS;

	}
}
