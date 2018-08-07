package fr.ign.cogit.simplu3d.experiments.openmole.paris.visu;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion2DObject;
import fr.ign.cogit.geoxygene.sig3d.gui.MainWindow;
import fr.ign.cogit.geoxygene.sig3d.representation.ConstantRepresentation;
import fr.ign.cogit.geoxygene.sig3d.representation.basic.Object1d;
import fr.ign.cogit.geoxygene.sig3d.representation.basic.Object2d;
import fr.ign.cogit.geoxygene.sig3d.representation.sample.ObjectCartoon;
import fr.ign.cogit.geoxygene.sig3d.representation.symbol.Symbol3D;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.representation.RepEnvironnement;
import fr.ign.cogit.simplu3d.representation.RepEnvironnement.Theme;
import fr.ign.cogit.simplu3d.util.AssignZ;

public class DataViewer {

	public static void main(String[] args) throws Exception {

		// Données d'entrées avec toutes les données
		String folderName = "/home/mbrasebin/Documents/Donnees/Exp/Eugene_Million/";

		String simulationFile = folderName + "simulation.shp";
		String zoneSimFile = folderName + "zone_simulee.shp";
		String pointFile = folderName + "point.shp";

		///// Quelques paramètres par defaut
		// Point object size
		Object1d.width = 4.0f;
		// Default Z for Parcels
		AssignZ.DEFAULT_Z = 36;

		// Color of the skype
		ConstantRepresentation.backGroundColor = new Color(156, 180, 193);

		// Création de la fenêtre d'affichage
		MainWindow mW = new MainWindow();

		// Represent the input data of the simulation
		representEnvironnement(LoaderSHP.loadNoDTM(new File(folderName)), mW);

		// Add simulation results
		IFeatureCollection<IFeature> simulationResult = ShapefileReader.read(simulationFile);
		for (IFeature feat : simulationResult) {
			feat.setRepresentation(new ObjectCartoon(feat, new Color(115, 8, 0), new Color(255, 255, 255), 2));
		}
		mW.getInterfaceMap3D().getCurrent3DMap().addLayer(new VectorLayer(simulationResult, "Simulation"));

		// Add simulation focus
		IFeatureCollection<IFeature> simulationFocus = ShapefileReader.read(zoneSimFile);
		for (IFeature feat : simulationFocus) {
			feat.setGeom(Extrusion2DObject.convertFromFeature(feat, AssignZ.DEFAULT_Z, AssignZ.DEFAULT_Z).getGeom());
			feat.setRepresentation(new Object2d(feat, true, Color.red, 0.5, true));
		}
		mW.getInterfaceMap3D().getCurrent3DMap().addLayer(new VectorLayer(simulationFocus, "SimulationZone"));

		IFeatureCollection<IFeature> pointFileFeatures = ShapefileReader.read(pointFile);
		for (IFeature feat : pointFileFeatures) {
			feat.setRepresentation(new Symbol3D(feat, 2, folderName + "logo.jpg"));
		}
		mW.getInterfaceMap3D().getCurrent3DMap().addLayer(new VectorLayer(pointFileFeatures, "Points"));

	}

	private static void representEnvironnement(Environnement env, MainWindow mW) {

		System.out.println("Nombre de BPU " + env.getBpU().size());

		// Loaded them
		List<Theme> lTheme = new ArrayList<RepEnvironnement.Theme>();
		lTheme.add(Theme.TOIT_BATIMENT);
		lTheme.add(Theme.FACADE_BATIMENT);
		// lTheme.add(Theme.FAITAGE);
		// lTheme.add(Theme.PIGNON);
		// lTheme.add(Theme.GOUTTIERE);
		lTheme.add(Theme.VOIRIE);
		// lTheme.add(Theme.PARCELLE);
		lTheme.add(Theme.SOUS_PARCELLE);
		// lTheme.add(Theme.ZONE);
		// lTheme.add(Theme.PAN);
		// lTheme.add(Theme.PAN_MUR);
		lTheme.add(Theme.BORDURE);

		Theme[] tab = lTheme.toArray(new Theme[0]);

		List<VectorLayer> vl = RepEnvironnement.represent(env, tab);

		// COGITLauncher3D mW = new COGITLauncher3D();

		for (VectorLayer l : vl) {

			mW.getInterfaceMap3D().getCurrent3DMap().addLayer(l);
		}

	}

}
