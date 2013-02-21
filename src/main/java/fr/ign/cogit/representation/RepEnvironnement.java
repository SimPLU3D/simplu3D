package fr.ign.cogit.representation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.geometry.topology.Triangle;
import fr.ign.cogit.geoxygene.sig3d.representation.basic.Object1d;
import fr.ign.cogit.geoxygene.sig3d.representation.basic.Object2d;
import fr.ign.cogit.geoxygene.sig3d.representation.sample.ObjectCartoon;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.geoxygene.sig3d.util.ColorRandom;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.model.application.Batiment;
import fr.ign.cogit.model.application.Bordure;
import fr.ign.cogit.model.application.Environnement;
import fr.ign.cogit.model.application.Facade;
import fr.ign.cogit.model.application.Parcelle;
import fr.ign.cogit.model.application.SousParcelle;
import fr.ign.cogit.model.application.Toit;
import fr.ign.cogit.model.application.Voirie;
import fr.ign.cogit.model.application.Zone;
import fr.ign.cogit.sig3d.analysis.ClassifyRoof;
import fr.ign.cogit.sig3d.representation.color.ColorLocalRandom;
import fr.ign.cogit.sig3d.representation.rendering.CartooMod2;

public class RepEnvironnement {

  public enum Theme {
    BORDURE("Bordure"), EMPRISE_BATIMENT("Emprise"), TOIT_BATIMENT("Toit"), FACADE_BATIMENT(
        "Facade"), VOIRIE("Voirie"), ZONE("Zone"), PARCELLE("Parcelle"), SOUS_PARCELLE(
        "SousParcelle"), FAITAGE("FAITAGE"), PIGNON("Pignon"), GOUTTIERE(
        "GOUTIERRE"), PAN("PAN");

    private String nomCouche;

    Theme(String nomCouche) {
      this.nomCouche = nomCouche;
    }

    public String getNomCouche() {
      return nomCouche;
    }

  }

  public static List<VectorLayer> representAll(Environnement env) {

    return represent(env, Theme.values());

  }

  public static List<VectorLayer> represent(Environnement env, Theme[] lTheme) {

    List<VectorLayer> lLayers = new ArrayList<VectorLayer>();

    int nbT = lTheme.length;

    for (int i = 0; i < nbT; i++) {

      lLayers.add(associeLayerToTheme(env, lTheme[i]));
    }

    return lLayers;

  }

  public static VectorLayer associeLayerToTheme(Environnement env, Theme t) {

    IFeatureCollection<? extends IFeature> featC = null;

    switch (t) {
      case BORDURE:
        featC = generateRepresentationBordure(env);
        break;
      case TOIT_BATIMENT:
        featC = generateRepresentationToit(env);
        break;
      case FACADE_BATIMENT:
        featC = generateRepresentationFacade(env);
        break;
      case VOIRIE:
        featC = generateRepresentationVoirie(env);
        break;
      case ZONE:
        featC = generateRepresentationZone(env);
        break;
      case PARCELLE:
        featC = generateRepresentationParcelle(env);
        break;
      case SOUS_PARCELLE:
        featC = generateRepresentationSousParcelle(env);
        break;
      case FAITAGE:
        featC = generateRepresentationFaitage(env);
        break;
      case PIGNON:
        featC = generateRepresentationPignon(env);
        break;
      case GOUTTIERE:
        featC = generateRepresentationGouttiere(env);
        break;
      case PAN:
        featC = generateRepresentationPAN(env);
        break;
    }

    return new VectorLayer(featC, t.getNomCouche());

  }

  /*
   * -------------- STYLE BORDURE ---------------
   */

  private static final Color BORDURE_FICTIVE = new Color(204, 0, 204);
  private static final Color BORDURE_FOND = new Color(0, 0, 189);
  private static final Color BORDURE_LATERAL = new Color(189, 189, 189);
  private static final Color BORDURE_VOIE = new Color(51, 153, 153);

  private static IFeatureCollection<Bordure> generateRepresentationBordure(
      Environnement env) {

    IFeatureCollection<SousParcelle> sPF = env.getSousParcelles();
    IFeatureCollection<Bordure> featBordOut = new FT_FeatureCollection<Bordure>();

    for (SousParcelle sp : sPF) {

      IFeatureCollection<Bordure> featBord = sp.getBordures();

      for (Bordure b : featBord) {
        int type = b.getTypeDroit();

        Color c = null;

        switch (type) {
          case Bordure.FICTIVE:
            c = BORDURE_FICTIVE;
            break;
          case Bordure.FOND:
            c = BORDURE_FOND;
            break;

          case Bordure.LATERAL:
            c = BORDURE_LATERAL;
            break;

          case Bordure.VOIE:
            c = BORDURE_VOIE;
            break;

        }

        b.setRepresentation(new Object1d(b, c));

      }

      featBordOut.addAll(featBord);

    }

    return featBordOut;

  }

  /*
   * -------------- STYLE TOIT Batiment ---------------
   */

  private static final Color COLOR_TOIT = new Color(75, 75, 75);

  private static IFeatureCollection<? extends IFeature> generateRepresentationToit(
      Environnement env) {
    IFeatureCollection<Toit> toitOut = new FT_FeatureCollection<Toit>();

    for (Batiment b : env.getBatiments()) {

      Toit t = b.getToit();

      t.setRepresentation(new CartooMod2(t, COLOR_TOIT));
      toitOut.add(t);

    }

    return toitOut;
  }

  /*
   * ------------------- Style Facade
   */

  private static final Color COLOR_FACADE = new Color(255, 255, 255);

  private static IFeatureCollection<? extends IFeature> generateRepresentationFacade(
      Environnement env) {

    IFeatureCollection<Facade> facadesOut = new FT_FeatureCollection<Facade>();

    for (Batiment b : env.getBatiments()) {

      List<Facade> facades = b.getFacade();
      
      for(Facade f:facades){
        f.setRepresentation(new CartooMod2(f, COLOR_FACADE));
        facadesOut.add(f); 
      }



    }

    return facadesOut;
  }

  /*
   * ---------------------- Style StyleVOIRIE
   */

  private static final Color COLOR_VOIRIE = new Color(139,137,137);

  private static IFeatureCollection<? extends IFeature> generateRepresentationVoirie(
      Environnement env) {
    for (Voirie v : env.getVoiries()) {

      v.setRepresentation(new Object2d(v, COLOR_VOIRIE));

    }

     
    return env.getVoiries();
  }

  /*
   * ------------------------ Style Parcelle
   */
  private static final Color COLOR_PARCELLE = new Color(60, 60, 60);

  private static IFeatureCollection<? extends IFeature> generateRepresentationParcelle(
      Environnement env) {
    for (Parcelle p : env.getParcelles()) {

      p.setRepresentation(new ObjectCartoon(p, Color.white, ColorLocalRandom.getRandomColor(COLOR_PARCELLE, 10, 10, 10), 3,0.0));

    }
    return env.getParcelles();
  }

  /*
   * ------------------------ Style Sous - Parcelle
   */
  private static final Color COLOR_SOUS_PARCELLE = new Color(162,205,90);
  private static final int radius = 50;

  private static IFeatureCollection<? extends IFeature> generateRepresentationSousParcelle(
      Environnement env) {
    // TODO Auto-generated method stub

    for (SousParcelle sp : env.getSousParcelles()) {

      sp.setRepresentation(new Object2d(sp,  ColorLocalRandom.getRandomColor(COLOR_SOUS_PARCELLE, radius, radius, radius)));

    }

    return env.getSousParcelles();
    
    
  }

  /*
   * ------------------------ Style Sous - Faitage
   */

  private static final Color COULOR_FAITAGE = Color.red;

  private static IFeatureCollection<? extends IFeature> generateRepresentationFaitage(
      Environnement env) {

    IFeatureCollection<IFeature> featOut = new FT_FeatureCollection<IFeature>();

    for (Batiment b : env.getBatiments()) {

      IGeometry geom = b.getToit().getFaitage();

      if (geom == null || geom.isEmpty()) {
        continue;
      }

      IFeature feat = new DefaultFeature(geom);

      feat.setRepresentation(new Object1d(feat, COULOR_FAITAGE));

      featOut.add(feat);

    }

    return featOut;
  }

  /*
   * --------------------------- Style Pignon
   */
  private static final Color COULOR_PIGNON = Color.blue;

  private static IFeatureCollection<? extends IFeature> generateRepresentationPignon(
      Environnement env) {

    IFeatureCollection<IFeature> featOut = new FT_FeatureCollection<IFeature>();

    for (Batiment b : env.getBatiments()) {

      IMultiCurve<IOrientableCurve> geom = b.getToit().getPignons();

      if (geom == null || geom.isEmpty()) {
        continue;
      }

      for (IOrientableCurve oC : geom) {

        IFeature feat = new DefaultFeature(oC);

        feat.setRepresentation(new Object1d(feat, COULOR_PIGNON));

        featOut.add(feat);
      }

    }

    return featOut;
  }

  /*
   * --------------------------- Style Gouttierre
   */
  private static final Color COULOR_GOUTTIERE = Color.green;

  private static IFeatureCollection<? extends IFeature> generateRepresentationGouttiere(
      Environnement env) {

    IFeatureCollection<IFeature> featOut = new FT_FeatureCollection<IFeature>();

    for (Batiment b : env.getBatiments()) {

      IGeometry geom = b.getToit().getGouttiere();

      if (geom == null || geom.isEmpty()) {
        continue;
      }

      IFeature feat = new DefaultFeature(geom);

      feat.setRepresentation(new Object1d(feat, COULOR_GOUTTIERE));

      featOut.add(feat);

    }

    return featOut;
  }

  /*
   * Représentation zone
   */

  private static IFeatureCollection<? extends IFeature> generateRepresentationZone(
      Environnement env) {
    for (Zone z : env.getZones()) {

      z.setRepresentation(new Object2d(z, true, ColorRandom.getRandomColor(),
          1.0f, true));

    }

    return env.getZones();

  }

  /*
   * Représentation de pans
   */

  private static IFeatureCollection<? extends IFeature> generateRepresentationPAN(
      Environnement env) {

    IFeatureCollection<IFeature> pans = new FT_FeatureCollection<IFeature>();

    for (Batiment b : env.getBatiments()) {

      Toit t = b.getToit();

      ClassifyRoof cR = new ClassifyRoof(t, 0.2, 1);
      List<List<Triangle>> llTri = cR.getTriangleGroup();

     

      for (List<Triangle> lTri : llTri) {

        Color c = ColorRandom.getRandomColor();
        
        IFeature feat = new DefaultFeature(new GM_MultiSurface<Triangle>(lTri));
        feat.setRepresentation(new ObjectCartoon(feat, c));
        pans.add(feat);

      }

    }

    return pans;
  }

}
