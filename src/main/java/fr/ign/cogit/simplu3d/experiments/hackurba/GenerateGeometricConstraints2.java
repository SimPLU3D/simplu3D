package fr.ign.cogit.simplu3d.experiments.hackurba;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.checker.experiments.HackUrbaChecker;
import fr.ign.cogit.simplu3d.checker.model.GeometricConstraints;
import fr.ign.cogit.simplu3d.checker.model.RuleContext;
import fr.ign.cogit.simplu3d.checker.model.UnrespectedRule;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.io.regulation.IAUIDFRegulationReader;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.model.ZoneRegulation;

public class GenerateGeometricConstraints2 {

	public static void main(String[] args) throws Exception {
		String path = "/home/mbrasebin/workspace/simplu/simplu3d-api/src/test/resources/data/31251-saint-aubin-light/";
		Environnement env = LoaderSHP.loadNoDTM(new File(path));

		IFeatureCollection<IFeature> featC = ShapefileReader.read(path + "parcelle.shp");
		IAUIDFRegulationReader reader = new IAUIDFRegulationReader();
		List<ZoneRegulation> lReg = reader.transformFeatureToRules(featC.get(0));

		ZoneRegulation r1 = lReg.get(0);
		ZoneRegulation r2 = null;

		List<GeometricConstraints> lGC = new ArrayList<>();
		RuleContext context = new RuleContext();

		List<UnrespectedRule> unrespectedRulesTotal = new ArrayList<>();

		System.out.println("Number of BPU : " + env.getBpU().size());

		BasicPropertyUnit bPU = env.getBpU().get(5);

		lGC.addAll(HackUrbaChecker.generateConstraints(bPU, r1, r2, context));

		IFeatureCollection<IFeature> featCollOut = new FT_FeatureCollection<>();

		System.out.println("Number of GeometricConstraints : " + lGC.size());

		for (GeometricConstraints gc : lGC) {
			if (gc.getGeometry() != null && !gc.getGeometry().isEmpty()
					&& gc.getGeometry().coordinateDimension() == 3) {
				IFeature feat = new DefaultFeature(gc.getGeometry());
				AttributeManager.addAttribute(feat, "CODE", gc.getCode(), "String");
				AttributeManager.addAttribute(feat, "Message", gc.getMessage(), "String");
				featCollOut.add(feat);
			}

		}

		System.out.println(featCollOut.size());

		IFeatureCollection<IFeature> featCollOutUR = new FT_FeatureCollection<>();
		for (UnrespectedRule uR : unrespectedRulesTotal) {
			IFeature feat = new DefaultFeature(uR.getGeometry());
			AttributeManager.addAttribute(feat, "CODE", uR.getCode(), "String");
			AttributeManager.addAttribute(feat, "Message", uR.getMessage(), "String");
			featCollOutUR.add(feat);
		}

		System.out.println("Number of Unrespected rules : " + featCollOutUR.size());

		ShapefileWriter.write(featCollOut, path + "out.shp");
		ShapefileWriter.write(featCollOutUR, path + "outUR.shp");

	}

}
