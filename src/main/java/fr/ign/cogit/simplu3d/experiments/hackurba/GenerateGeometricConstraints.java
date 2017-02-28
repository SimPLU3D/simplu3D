package fr.ign.cogit.simplu3d.experiments.hackurba;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion2DObject;
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
import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.model.ZoneRegulation;

public class GenerateGeometricConstraints {

	public static void main(String[] args) throws Exception {
		String path = "/home/mbrasebin/Bureau/Hackurba/Data/ZoneTest/";
		Environnement env = LoaderSHP.loadNoDTM(new File(path));

		IFeatureCollection<IFeature> featC = ShapefileReader.read(path + "parcelle.shp");
		IAUIDFRegulationReader reader = new IAUIDFRegulationReader();
		List<ZoneRegulation> lReg = reader.transformFeatureToRules(featC.get(0));

		IFeatureCollection<IFeature> featCollBat = ShapefileReader.read(path + "building_2.shp");
		double hauteur = Double.parseDouble(featCollBat.get(0).getAttribute("hauteur").toString());

		List<GeometricConstraints> lGC = new ArrayList<>();
		RuleContext context = new RuleContext();

		List<UnrespectedRule> unrespectedRulesTotal = new ArrayList<>();

		for (BasicPropertyUnit bPU : env.getBpU()) {

			// Génération des bandes de constructibilité

			IGeometry geom = Extrusion2DObject.convertFromGeometry(featCollBat.get(0).getGeom(), 0, hauteur);
			IMultiSurface<IOrientableSurface> buildingOut = FromGeomToSurface.convertMSGeom(geom);

			Building b = new Building(buildingOut);
			b.setNew(true);
			bPU.getBuildings().add(b);

			lGC = HackUrbaChecker.generateConstraints(bPU, lReg.get(0), lReg.get(1), context);
			unrespectedRulesTotal = HackUrbaChecker.check(bPU, lReg.get(0), lReg.get(1), context);

			if (true)
				break;

		}

		IFeatureCollection<IFeature> featCollOut = new FT_FeatureCollection<>();
		for (GeometricConstraints gc : lGC) {
			if (gc.getGeometry() != null && !gc.getGeometry().isEmpty()
					&& gc.getGeometry().coordinateDimension() == 3) {
				IFeature feat = new DefaultFeature(gc.getGeometry());
				AttributeManager.addAttribute(feat, "CODE", gc.getCode(), "String");
				AttributeManager.addAttribute(feat, "Message", gc.getMessage(), "String");
				featCollOut.add(feat);
			}

		}

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
