package fr.ign.cogit.simplu3d.iauidf;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.calculation.OrientedBoundingBox;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.exec.BasicSimulator;
import fr.ign.cogit.simplu3d.iauidf.predicate.PredicateIAUIDF;
import fr.ign.cogit.simplu3d.iauidf.regulation.Regulation;
import fr.ign.cogit.simplu3d.iauidf.tool.BandProduction;
import fr.ign.cogit.simplu3d.importer.applicationClasses.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.importer.applicationClasses.RoadImporter;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.convert.GenerateSolidFromCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.classconstrained.MultipleBuildingsCuboid;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;
import fr.ign.parameters.Parameters;

public class Exec {

	public static boolean DEBUG_MODE = true;
	private static Logger log = Logger.getLogger(Exec.class);
	public final static String nomDTM = "MNT_BD3D.asc";
	public static Parameters p;
	public static List<IMultiSurface<IOrientableSurface>> lMS = new ArrayList<>();
	public static List<IMultiSurface<IOrientableSurface>> debugSurface = new ArrayList<>();
	public static List<IMultiCurve<IOrientableCurve>> debugLine = new ArrayList<>();

	// Initialisation des attributs différents du schéma de base
	// et le fichier de paramètre commun à toutes les simulations
	public static void init() throws Exception {
		RoadImporter.ATT_NOM_RUE = "NOM_VOIE_G";
		RoadImporter.ATT_LARGEUR = "LARGEUR";
		RoadImporter.ATT_TYPE = "NATURE";

		CadastralParcelLoader.TYPE_ANNOTATION = 2;
		PredicateIAUIDF.RIGHT_OF_LEFT_FOR_ART_71 = SpecificCadastralBoundary.LEFT_SIDE;
	}

	public static void main(String[] args) throws Exception {
		// Dossier contenant IMU_MANTES_TEST.csv et les sous dossier par code
		// IMU
		String folder = "C:/Users/mbrasebin/Desktop/ilotTest2/";
		String csvFile = folder + "IMU_MANTES_TEST.csv";
		// Chargement des règlement par code IMU (on peut avoir plusieurs
		// réglements pour un code IMU du fait des bandes)
		Map<Integer, List<Regulation>> mapReg = Regulation
				.loadRegulationSet(csvFile);
		// Fonction de test : chargement de la réglementation :
		testLoadedRegulation(mapReg);
		// TODO : gérer les attributs indépendemment de la casse.
		// Initialisation des noms d'attributs
		init();
		// On traite indépendamment chaque zone imu
		for (int imu : mapReg.keySet()) {
			 if (imu != 75032350)
			 continue;
			// int imu = 78020432; 78020440; //(int)
			// mapReg.keySet().toArray()[0];
			System.out.println("Numéro imu : " + imu);
			boolean simul = simulRegulationByIMU(imu, mapReg.get(imu), folder
					+ imu + "/");
			if (!simul) {
				log.warn("--Probleme pour la simulation : " + imu);
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
	public static boolean simulRegulationByIMU(int imu, List<Regulation> lReg,
			String folderImu) throws Exception {
		if (DEBUG_MODE) {
			debugSurface = new ArrayList<>();
			debugLine = new ArrayList<>();
		}
		// On instancie l'environnement associé à l'IMU
		Environnement env = LoaderSHP.load(folderImu, new FileInputStream(
				folderImu + nomDTM));
		boolean isOk = true;
		// Stocke les résultats en sorties
		IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();

		// On parcourt chaque parcelle et on applique la simulation dessus
		int nbBPU = env.getBpU().size();
		for (int i = 0; i < nbBPU; i++) {
			
			if(i > 5)break;
				
			//	if(i !=0) continue;
	  //	System.out.println(env.getBpU().get(i).getCadastralParcel().get(0).getId());
			 //	if (env.getBpU().get(i).getId() != 47)	 continue;
			 
			System.out.println("Parcelle numéro : " + env.getBpU().get(i).getId());
			System.out.println(env.getBpU().get(i).getGeom());
			IFeatureCollection<IFeature> featCTemp = simulRegulationByBasicPropertyUnit(
					env, env.getBpU().get(i), imu, lReg);
			System.out.println("Nombre de blocs : " + featCTemp.size());
			isOk = isOk && (featCTemp != null);
			if (featCTemp != null) {
				featC.addAll(featCTemp);
			}
			
		}
		System.out.println("-- Nombre de surface : " + debugSurface.size());
		String fileName = folderImu + "simul_" + imu + ".shp";
		System.out.println(fileName);
		ShapefileWriter.write(featC, fileName);
		if (DEBUG_MODE) {
			saveShapeTest(folderImu);
		}
		return isOk;
	}

	private static boolean initiateSimulationParamters(Regulation r1,
			Regulation r2) throws Exception {
		// Chargement du fichier de configuration
		String folderName = BasicSimulator.class.getClassLoader()
				.getResource("scenario/").getPath();
		String fileName = "parameters_iauidf.xml";
		p = Parameters.unmarshall(new File(folderName + fileName));
		

		if (r2 != null) {

			double newHeightMax = Math.max(r1.getArt_102(), r2.getArt_102());
			
			if(newHeightMax != 99.0){
				p.set("maxheight",newHeightMax);
			}
			
			
			
			
			
		} else {
			if(r1.getArt_102()  != 99){
				p.set("maxheight", r1.getArt_102()  );
			}
			
		
		
		}

		if (p.getDouble("maxheight") < p.getDouble("minheight")) {
			return false;
		}
		


		System.out.println("Hauteur " + p.getDouble("minheight") + " "
				+ p.getDouble("maxheight"));

		if (r2 != null) {
			if ((r1.getArt_74() == 0) && (r2.getArt_74() == 0) && (r2.getArt_14() !=  99) && (r1.getArt_14() !=  99) &&(p.getDouble("maxheight") != 99.0)) {
				p.set("minheight", p.getDouble("maxheight") - 0.1);
			}
		} else {
			if ((r1.getArt_74() == 0) && (r1.getArt_14() !=  99) && (p.getDouble("maxheight") != 99.0) ) {
				p.set("minheight", p.getDouble("maxheight") - 0.1);
			}
		}
		
		
		

		double longueur1 =  Double.NEGATIVE_INFINITY;
		
		if(r1.getGeomBande() != null &&  ! r1.getGeomBande().isEmpty()){
			OrientedBoundingBox oBB1 = new OrientedBoundingBox(r1.getGeomBande());

			 longueur1 = oBB1.getLength();
		}


		if (r2 != null) {
			OrientedBoundingBox oBB2 = new OrientedBoundingBox(
					r2.getGeomBande());

			double longueur2 = oBB2.getLength();

			p.set("maxlen",
					Math.min(p.getDouble("maxlen"),
							Math.max(longueur1, longueur2)));

			p.set("maxwid",
					Math.min(p.getDouble("maxwid"),
							Math.max(longueur1, longueur2)));

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

		p.set("temp",
				Math.min(
						p.getDouble("temp"),
						p.getDouble("maxlen") * p.getDouble("maxlen")
								* p.getDouble("maxheight")));

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
	public static IFeatureCollection<IFeature> simulRegulationByBasicPropertyUnit(
			Environnement env, BasicPropertyUnit bPU, int imu,
			List<Regulation> lReg) throws Exception {
		// Stocke les résultats en sorties
		IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();
		// //////On découpe la parcelle en bande en fonction des règlements
		// On met les règlements dans l'ordre des bandes (le premier est la
		// première bande, le second la seconde ou null s'il n'y en a pas)
		List<Regulation> orderedRegulation = orderedRegulation(lReg, imu);
		if (orderedRegulation == null) {
			return null;
		}
		Regulation r1 = orderedRegulation.get(0);
		Regulation r2 = orderedRegulation.get(1);
		
		System.out.println("R1 : " + r1);
		System.out.println("R2 : " + r2);
		
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
		GraphConfiguration<Cuboid> cc = oCB.process(bPU, p, env, imu, pred, r1,
				r2, bP);
		if (cc == null) {
			return featC;
		}

		for (GraphVertex<Cuboid> v : cc.getGraph().vertexSet()) {
			IMultiSurface<IOrientableSurface> iMS = new GM_MultiSurface<>();
			iMS.addAll(GenerateSolidFromCuboid.generate(v.getValue())
					.getFacesList());
			IFeature feat = new DefaultFeature(iMS);
			// On ajoute des attributs aux entités (dimension des objets)
			AttributeManager
					.addAttribute(feat, "Longueur",
							Math.max(v.getValue().length, v.getValue().width),
							"Double");
			AttributeManager
					.addAttribute(feat, "Largeur",
							Math.min(v.getValue().length, v.getValue().width),
							"Double");
			AttributeManager.addAttribute(feat, "Hauteur", v.getValue().height,
					"Double");
			AttributeManager.addAttribute(feat, "Rotation",
					v.getValue().orientation, "Double");
			featC.add(feat);
		}
		return featC;
	}

	public static List<Regulation> orderedRegulation(List<Regulation> lReg,
			int imu) {
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
			log.error("0 ou plus de 2 règlements pour un code imu, cas non traité : "
					+ imu);
			return null;
		}
		List<Regulation> orderedList = new ArrayList<>();
		orderedList.add(r1);
		orderedList.add(r2);
		return orderedList;
	}

	// Affiche les réglements chargés
	public static void testLoadedRegulation(
			Map<Integer, List<Regulation>> mapReg) {
		for (int key : mapReg.keySet()) {
			log.debug("-----key----------");
			for (Regulation reg : mapReg.get(key)) {
				log.debug(reg.toString());
			}
		}
	}

	private static void saveShapeTest(String folderImu) {
		IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();
		// Petit script pour sauvegarder les bandes pour vérification
		// Le fichier généré se trouve dans le dossier imu
		for (IMultiSurface<IOrientableSurface> iS : debugSurface) {
			if (iS != null && iS.isValid() && !iS.isEmpty()) {
				featC.add(new DefaultFeature(iS));
			}
		}
		ShapefileWriter.write(featC, folderImu + "generatedBand.shp");
		IFeatureCollection<IFeature> featC2 = new FT_FeatureCollection<>();
		// Petit script pour sauvegarder les bandes pour vérification
		// Le fichier généré se trouve dans le dossier imu
		for (IMultiCurve<IOrientableCurve> iS : debugLine) {
			if (iS != null && iS.isValid() && !iS.isEmpty()) {
				featC2.add(new DefaultFeature(iS));
			}
		}
		ShapefileWriter.write(featC2, folderImu + "generatedLine.shp");
	}
}
