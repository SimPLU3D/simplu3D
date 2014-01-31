package fr.ign.cogit.gru3d.regleUrba;

import java.awt.Color;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.gui.MainWindow;
import fr.ign.cogit.geoxygene.sig3d.representation.ConstantRepresentation;
import fr.ign.cogit.geoxygene.sig3d.representation.texture.TextureManager;
import fr.ign.cogit.geoxygene.sig3d.representation.texture.TexturedSurface;
import fr.ign.cogit.geoxygene.sig3d.semantic.Map3D;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.gru3d.regleUrba.propositionBuilding.export.Export;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.io.Chargement;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.PLU;
import fr.ign.cogit.sig3d.gui.toolbar.IOToolBar;

/**
 * Classe principale pour exécuter le module d'urbanisme A FAIRE : - Gestion des
 * textures à l'affichage - Intégrer les informations concernant le type du
 * batiment - Gérer le système de zone du PLU (1 zone étudiée pour l'instant
 * mais pas compliqué à rajouter ...)
 * 
 * @author MBrasebin
 */
public class Executor {

  // private static String REPERTOIRE =
  // "E://mbrasebin//Donnees//JeuDonneesSAGEO//EnvironnementUDMS//";

  private static String REPERTOIRE = "E:/mbrasebin/Donnees/Strasbourg/TestRegles/Test2/";

  public static DirectPosition dpTranslate = null;

  public static boolean VERBOSE = true;
  public static boolean TRANSLATE_TO_ZERO = false;

  public static MainWindow fen = null;

  /**
   * Lancement de l'application de gestions de règles d'urbanisme
   * 
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {

    ConstantRepresentation.backGroundColor = new Color(156, 180, 193);

    Export.doExport = Export.AvailableExport.NONE;

    try {

      // On propose une fichier initial pour le débug
      // Il sera chargeable dans l'interface par la suite
      String fichier_regle = "ProjRIG.xml";// "ProjTest2.xml"; //
                                           // "ProjTest2.xml"; //
                                           // //"ProspectRoute.xml";//
                                           // "Residentiel.xml";//"StrasbourgZI.xml";//
                                           // "Hauteur.xml"; //
      // On charge les données de règles

      // On charge les données nécessaires

      PLU plu = Chargement.chargementFichier(Executor.REPERTOIRE + "regles/"
          + fichier_regle);
      // On initialise l'environnement (Chargement d'objets et relations
      // entre eux)
      Environnement env = new Environnement(Executor.REPERTOIRE,
          Executor.VERBOSE);

      // On initialise l'interface
      fen = new MainWindow();

      Map3D carte = fen.getInterfaceMap3D().getCurrent3DMap();

      BarreReglesUrbanisme bR = new BarreReglesUrbanisme(
          fen.getInterfaceMap3D());
      fen.getMainMenuBar().add(bR);

      bR.setEnvironnement(env);
      bR.setRegles(plu.getZonePLU().get(0).getRegles());

      new IOToolBar(fen);

      carte.addLayer(new VectorLayer(env.getlToits(), "Toits"));
      carte.addLayer(new VectorLayer(env.getlBatiments(), "Batiments"));
      carte.addLayer(new VectorLayer(env.getlParcelles(), "Parcelles"));
      carte.addLayer(new VectorLayer(env.getlRoutes(), "Routes",
          Color.DARK_GRAY));

      if (Executor.VERBOSE) {
        carte.addLayer(new VectorLayer(env.getCollRouteParcelle(),
            "Rel_Route_Parcelle", Color.BLUE));
        carte.addLayer(new VectorLayer(env.getCollBatimentParcelle(),
            "Rel_Batiment_Parcelle", Color.red));
        carte.addLayer(new VectorLayer(env.getlBatimentsErreur(), "BatErreur",
            Color.CYAN));
        carte.addLayer(new VectorLayer(env.getCollParcelleParcelle(),
            "Rel_Parcelle_Parcelle", Color.green));
      }

      // On ajoute le bouton qui déclenche le calcul de l'enveloppe

      fen.setVisible(true);

      // fen.getInterfaceMap3D().getLights().get(0).setEnable(false);
      fen.getInterfaceMap3D().removeLight(0);
      fen.getInterfaceMap3D().addLight(new Color(147, 147, 147), 0, 0, 0);
      fen.getInterfaceMap3D().moveLight(180, -15, 120, 0);
      fen.getInterfaceMap3D().addLight(new Color(147, 147, 147), 0, 0, 0);
      fen.getInterfaceMap3D().moveLight(-140, 3, 120, 1);

      // 1051042.8513268954120576,6840539.0837931865826249 :
      // 1051264.8064121364150196,6840679.2711814027279615

      double xc = (1051042.8513268954120576 + 1051264.8064121364150196) / 2;
      double yc = (6840539.0837931865826249 + 6840679.2711814027279615) / 2;

      double z = 138;

      double longueur = 1051264.8064121364150196 - 1051042.8513268954120576;
      double largeur = 6840679.2711814027279615 - 6840539.0837931865826249;

      IDirectPositionList dpl = new DirectPositionList();

      IDirectPosition dp1 = new DirectPosition(xc - longueur / 2, yc - largeur
          / 2, z);
      IDirectPosition dp2 = new DirectPosition(xc + longueur / 2, yc - largeur
          / 2, z);
      IDirectPosition dp3 = new DirectPosition(xc + longueur / 2, yc + largeur
          / 2, z);
      IDirectPosition dp4 = new DirectPosition(xc - longueur / 2, yc + largeur
          / 2, z);

      dpl.add(dp1);
      dpl.add(dp2);
      dpl.add(dp3);
      dpl.add(dp4);
      dpl.add(dp1);

      IFeatureCollection<IFeature> fc = new FT_FeatureCollection<IFeature>();

      IFeature feat = new DefaultFeature(new GM_Polygon(new GM_LineString(dpl)));

      fc.add(feat);

      feat.setRepresentation(new TexturedSurface(feat, TextureManager
          .textureLoading("C:/Users/mbrasebin/Desktop/Env3D/TextParcelle.png"),
          longueur, largeur));

      carte.addLayer(new VectorLayer(fc, "Cool"));

      // //////////////////////
      // Post-treatments
      // //////////////////////
      /*
       * Moteur m = new Moteur(env, plu.getZonePLU().get(0).getRegles());
       * FT_FeatureCollection<Parcelle> lP = env.getlParcelles();
       * 
       * List<FT_FeatureCollection<Incoherence>> lFI = m.processIsParcelOkAll();
       * 
       * int nbErrP = lFI.size();
       * 
       * for (int i = 0; i < nbErrP; i++) { carte.addLayer(new
       * VectorLayer(lFI.get(i), "Error n°" + i)); }
       * 
       * /* int nbParcelles = lP.size(); FT_FeatureCollection<FT_Feature>
       * featColl = new FT_FeatureCollection<FT_Feature>(); bouclei: for (int i
       * = 118; i < 119; i++) {// 0 nbParcelles
       * 
       * Parcelle p = lP.get(i);
       * 
       * if (i == -1) {// || i == 76 75 118 System.out.println(i + "numéro" +
       * p.getAttribute("NUMERO")); continue bouclei; }
       * 
       * System.out.println(i + "numéro" + p.getAttribute("NUMERO"));
       * 
       * FT_FeatureCollection<FT_Feature> featCollTemp = m
       * .computeBuildableEnvelopes(p);
       * 
       * if (featCollTemp != null) { featColl.addAll(featCollTemp); }
       * 
       * p.calculateBuiltVolume(); p.assessFAR(Constant.COEFF_COS); } int
       * nbVolume = featColl.size();
       * 
       * for (int i = 0; i < nbVolume; i++) {
       * 
       * FT_Feature feat = featColl.get(i);
       * 
       * feat.setRepresentation(new ObjectCartoon(feat)); } carte.addLayer(new
       * VectorLayer(featColl, "Volume constructible"));
       */

      System.out.println("That's all folks!");
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
