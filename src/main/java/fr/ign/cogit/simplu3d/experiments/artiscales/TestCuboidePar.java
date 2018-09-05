package fr.ign.cogit.simplu3d.experiments.artiscales;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.convert.FromGeomToLineString;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.experiments.iauidf.regulation.Regulation;
import fr.ign.cogit.simplu3d.io.feature.PrescriptionReader;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.model.Prescription;
import fr.ign.cogit.simplu3d.model.UrbaZone;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.builder.ParallelCuboidBuilder;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.loader.LoaderCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.cuboid.OptimisedBuildingsCuboidFinalDirectRejection;
import fr.ign.cogit.simplu3d.rjmcmc.generic.predicate.SamplePredicate;
import fr.ign.cogit.simplu3d.util.SDPCalc;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;
import fr.ign.parameters.Parameters;

public class TestCuboidePar {

	String zipCode;
	// String scenarName;
	File parcelFile;
	SimpleFeature feature;
	File buildFile;
	File roadFile;
	File paramFile = new File("donnee/couplage/pluZoning/codes/");
	File codeFile;
	File zoningFile;
	File simuFile;
	int compteurOutput = 0;

	IFeatureCollection<IFeature> iFeatGenC = new FT_FeatureCollection<>();

	File filePrescPonct;
	File filePrescLin;
	File filePrescSurf;
	File rootFile;

	Parameters p = null;

	public static void main(String[] args) throws Exception {
		TestCuboidePar yo = new TestCuboidePar();
	}

	public TestCuboidePar() throws Exception {

		rootFile = new File("/home/mcolomb/tmp/parallel/");
		simuFile = rootFile;
		parcelFile = new File(rootFile, "parcel.shp");
		paramFile = new File(rootFile, "param0.xml");
		buildFile = new File(rootFile, "batiment.shp");
		roadFile = new File(rootFile, "route2.shp");
		codeFile = new File(rootFile, "DOC_URBA.shp");
		filePrescPonct = new File(rootFile, "PRESCRIPTION_PONCT.shp");
		filePrescLin = new File(rootFile, "PRESCRIPTION_LIN.shp");
		filePrescSurf = new File(rootFile, "PRESCRIPTION_SURF.shp");
		zoningFile = new File("/home/mcolomb/donnee/couplage/pluZoning/ Zonage_CAGB_INSEE_25495.shp");
		Environnement env = LoaderSHP.load(simuFile, codeFile, zoningFile, parcelFile, roadFile, buildFile,
				filePrescPonct, filePrescLin, filePrescSurf, null);

		p = Parameters.unmarshall(paramFile);

		HashMap<String, SamplePredicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>>> catalog = new HashMap<String, SamplePredicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>>>();
		BasicPropertyUnit bPU = env.getBpU().get(0);

		// parralel stuff

		IFeatureCollection<Prescription> presc = env.getPrescriptions();

		List<IGeometry> lS = new ArrayList<>();

		for (Prescription p : presc) {

			List<IOrientableCurve> lsTemp = FromGeomToLineString.convert(p.getGeom());
			System.out.println(p.getGeom());
			if (lsTemp != null) {
				lS.addAll(lS);
			}

		}
		IGeometry[] geomTab = new IGeometry[lS.size()];
		geomTab = lS.toArray(geomTab);

		ParallelCuboidBuilder pcb = new ParallelCuboidBuilder(geomTab, 1);

		System.out.println(pcb);

		// Instantiation of the sampler
		OptimisedBuildingsCuboidFinalDirectRejection oCB = new OptimisedBuildingsCuboidFinalDirectRejection();

		String typez = new String();

		// Rules parameters

		Regulation regle = null;
		Map<Integer, List<Regulation>> regles = Regulation
				.loadRegulationSet("/home/mcolomb/donnee/couplage/pluZoning/codes/predicate.csv");
		for (UrbaZone zone : env.getUrbaZones()) {
			if (zone.getGeom().contains(bPU.getGeom())) {
				typez = zone.getLibelle();
				System.out.println(typez);
			}
		}

		for (int imu : regles.keySet()) {
			for (Regulation reg : regles.get(imu)) {
				if (reg.getLibelle_de_dul().equals(typez) && reg.getInsee() == 25495) {
					regle = reg;
					System.out.println("j'ai bien retrouvé la ligne. son type est " + typez);
				}
			}
		}

		if (regle == null) {
			System.out.println("iz null");
			regle = regles.get(999).get(0);
		}

		double distReculVoirie = regle.getArt_6();
		boolean align = false;
		if (distReculVoirie == 77) {
			distReculVoirie = 0;
			align = true;
		}
		double distReculFond = regle.getArt_73();
		// regle.getArt_74()) devrait prendre le minimum de la valeur fixe et du rapport
		// à la hauteur du batiment à coté ::à développer yo
		double distReculLat = regle.getArt_72();

		double distanceInterBati = regle.getArt_8();
		if (regle.getArt_8() == 99) {
			distanceInterBati = 0;
			presc = env.getPrescriptions();
		}

		double maximalCES = regle.getArt_9();
		if (regle.getArt_8() == 99) {
			maximalCES = 0;
		}

		// définition de la hauteur. Si elle est exprimé en nombre d'étage, on comptera
		// 3m pour le premier étage et 2.5m pour les étages supérieurs. Je ne sais pas
		// comment on
		// utilise ce paramètre car il n'est pas en argument dans le predicate.
		// TODO utiliser cette hauteur
		double maximalhauteur = regle.getArt_10_m();

			
		// Instantiation of the rule checker
		PredicatePLUCities<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new PredicatePLUCities<>(
				bPU,  align, distReculVoirie, distReculFond, distReculLat, distanceInterBati, maximalCES, maximalhauteur,
				 p.getInteger("nbCuboid"), true, presc);
		// PredicateDensification<Cuboid, GraphConfiguration<Cuboid>,
		// BirthDeathModification<Cuboid>> pred = new PredicateIAUIDF();
		Double areaParcels = 0.0;
		for (CadastralParcel yo : bPU.getCadastralParcels()) {
			areaParcels = areaParcels + yo.getArea();
		}

		// Run of the optimisation on a parcel with the predicate

		GraphConfiguration<Cuboid> cc = oCB.process(bPU, p, env, 1, pred);

		// Witting the output
		IFeatureCollection<IFeature> iFeatC = new FT_FeatureCollection<>();
		IFeatureCollection<IFeature> iFeatCtemp = new FT_FeatureCollection<>();

		// For all generated boxes
		for (GraphVertex<Cuboid> v : cc.getGraph().vertexSet()) {

			// Output feature with generated geometry
			IFeature feat = new DefaultFeature(v.getValue().generated3DGeom());

			// IFeature feat = new DefaultFeature(v.getValue().getFootprint());

			// We write some attributes

			AttributeManager.addAttribute(feat, "Longueur", Math.max(v.getValue().length, v.getValue().width),
					"Double");
			AttributeManager.addAttribute(feat, "Largeur", Math.min(v.getValue().length, v.getValue().width), "Double");
			AttributeManager.addAttribute(feat, "Hauteur", v.getValue().height, "Double");
			AttributeManager.addAttribute(feat, "Rotation", v.getValue().orientation, "Double");
			AttributeManager.addAttribute(feat, "SurfaceBox", v.getValue().getArea(), "Double");
			AttributeManager.addAttribute(feat, "areaParcel", areaParcels, "Double");
			iFeatCtemp.add(feat);
		}
		// TODO mettre la bonne aire des cuboides mergés
		List<Cuboid> cubes = LoaderCuboid.loadFromCollection(iFeatCtemp);
		SDPCalc surfGen = new SDPCalc();
		double formTot = surfGen.process(cubes);

		for (IFeature feat : iFeatCtemp) {
			AttributeManager.addAttribute(feat, "SurfaceTot", formTot, "Double");
			iFeatC.add(feat);
			iFeatGenC.add(feat);
		}
		// A shapefile is written as output
		// WARNING : 'out' parameter from configuration file have to be
		// change

		File output = new File(simuFile, "out-parcelle.shp");
		while (output.exists()) {
			output = new File(simuFile, "out-parcelle" + compteurOutput + ".shp");
			compteurOutput = compteurOutput + 1;
		}
		output.getParentFile().mkdirs();

		ShapefileWriter.write(iFeatC, output.toString());

	}

}
