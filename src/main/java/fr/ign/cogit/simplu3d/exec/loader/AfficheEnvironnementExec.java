package fr.ign.cogit.simplu3d.exec.loader;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.sig3d.gui.MainWindow;
import fr.ign.cogit.geoxygene.sig3d.representation.ConstantRepresentation;
import fr.ign.cogit.geoxygene.sig3d.representation.basic.Object1d;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.simplu3d.importer.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.representation.RepEnvironnement;
import fr.ign.cogit.simplu3d.representation.RepEnvironnement.Theme;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class AfficheEnvironnementExec {

	public static void main(String[] args) throws Exception {

		Object1d.width = 4.0f;
		/*
		 * RoadImporter.ATT_NOM_RUE = "NOM_VOIE_G"; RoadImporter.ATT_LARGEUR =
		 * "LARGEUR"; RoadImporter.ATT_TYPE = "NATURE";
		 * 
		 * LoaderSHP.NOM_FICHIER_PARCELLE = "parcelle.shp";
		 * 
		 * 
		 * CadastralParcelLoader.ATT_HAS_TO_BE_SIMULATED = "simul";
		 * 
		 * PredicateIAUIDF.RIGHT_OF_LEFT_FOR_ART_71 =
		 * SpecificCadastralBoundary.LEFT_SIDE;
		 */

		CadastralParcelLoader.TYPE_ANNOTATION = 2;

		ConstantRepresentation.backGroundColor = new Color(156, 180, 193);

		String folderName =  "/home/mbrasebin/Documents/Donnees/Exp/Eugene_Million/";

		Environnement env = LoaderSHP.loadNoDTM(new File(folderName));

		System.out.println("Nombre de BPU " + env.getBpU().size());

		List<Theme> lTheme = new ArrayList<RepEnvironnement.Theme>();
		lTheme.add(Theme.TOIT_BATIMENT);
		lTheme.add(Theme.FACADE_BATIMENT);
		lTheme.add(Theme.FAITAGE);
		lTheme.add(Theme.PIGNON);
		lTheme.add(Theme.GOUTTIERE);
		lTheme.add(Theme.VOIRIE);
		lTheme.add(Theme.PARCELLE);
		lTheme.add(Theme.SOUS_PARCELLE);
		lTheme.add(Theme.ZONE);
		lTheme.add(Theme.PAN);
		lTheme.add(Theme.PAN_MUR);
		lTheme.add(Theme.BORDURE);

		Theme[] tab = lTheme.toArray(new Theme[0]);

		List<VectorLayer> vl = RepEnvironnement.represent(env, tab);
		MainWindow mW = new MainWindow();
		// COGITLauncher3D mW = new COGITLauncher3D();

		for (VectorLayer l : vl) {

			mW.getInterfaceMap3D().getCurrent3DMap().addLayer(l);
		}

	}

}
