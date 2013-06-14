package fr.ign.cogit.simplu3d.representation;

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
import fr.ign.cogit.sig3d.analysis.ClassifyRoof;
import fr.ign.cogit.sig3d.representation.color.ColorLocalRandom;
import fr.ign.cogit.sig3d.representation.rendering.CartooMod2;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.Road;
import fr.ign.cogit.simplu3d.model.application.RoofSurface;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.application.SpecificWallSurface;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.model.application.UrbaZone;
import fr.ign.cogit.simplu3d.model.application._AbstractBuilding;

public class RepEnvironnement {

  public enum Theme {
    BORDURE("Bordure"),  TOIT_BATIMENT("Toit"), FACADE_BATIMENT(
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
        featC = generateCadastralBoundaryRepresentation(env);
        break;
      case TOIT_BATIMENT:
        featC = generateRoofRepresentation(env);
        break;
      case FACADE_BATIMENT:
        featC = generateWallRepresentation(env);
        break;
      case VOIRIE:
        featC = generateRoadRepresentation(env);
        break;
      case ZONE:
        featC = generateZoneRepresentation(env);
        break;
      case PARCELLE:
        featC = generateCadastraParcelRepresentation(env);
        break;
      case SOUS_PARCELLE:
        featC = generateSubParcelRepresentation(env);
        break;
      case FAITAGE:
        featC = generateRoofingRepresentation(env);
        break;
      case PIGNON:
        featC = generateGableRepresentation(env);
        break;
      case GOUTTIERE:
        featC = generateGutterRepresentation(env);
        break;
      case PAN:
        featC = generateRoofSlopesRepresentation(env);
        break;
      default:
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

  private static IFeatureCollection<SpecificCadastralBoundary> generateCadastralBoundaryRepresentation(
      Environnement env) {

    IFeatureCollection<CadastralParcel> sPF = env.getParcelles();
    IFeatureCollection<SpecificCadastralBoundary> featBordOut = new FT_FeatureCollection<SpecificCadastralBoundary>();

    for (CadastralParcel sp : sPF) {

      IFeatureCollection<SpecificCadastralBoundary> featBord = sp
          .getSpecificCadastralBoundary();

      for (SpecificCadastralBoundary b : featBord) {
        int type = b.getType();

        Color c = null;

        switch (type) {
          case SpecificCadastralBoundary.INTRA:
            c = BORDURE_FICTIVE;
            break;
          case SpecificCadastralBoundary.BOT:
            c = BORDURE_FOND;
            break;

          case SpecificCadastralBoundary.LAT:
            c = BORDURE_LATERAL;
            break;

          case SpecificCadastralBoundary.ROAD:
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

  private static IFeatureCollection<? extends IFeature> generateRoofRepresentation(
      Environnement env) {
    IFeatureCollection<RoofSurface> toitOut = new FT_FeatureCollection<RoofSurface>();

    for (_AbstractBuilding b : env.getBuildings()) {

      RoofSurface t = b.getToit();

      t.setRepresentation(new CartooMod2(t, COLOR_TOIT));
      toitOut.add(t);

    }

    return toitOut;
  }

  /*
   * ------------------- Style Facade
   */

  private static final Color COLOR_FACADE = new Color(255, 255, 255);

  private static IFeatureCollection<? extends IFeature> generateWallRepresentation(
      Environnement env) {

    IFeatureCollection<SpecificWallSurface> facadesOut = new FT_FeatureCollection<SpecificWallSurface>();

    for (_AbstractBuilding b : env.getBuildings()) {

      List<SpecificWallSurface> facades = b.getFacade();

      for (SpecificWallSurface f : facades) {
        f.setRepresentation(new CartooMod2(f, COLOR_FACADE));
        facadesOut.add(f);
      }

    }

    return facadesOut;
  }

  /*
   * ---------------------- Style StyleVOIRIE
   */

  private static final Color COLOR_VOIRIE = new Color(139, 137, 137);

  private static IFeatureCollection<? extends IFeature> generateRoadRepresentation(
      Environnement env) {
    for (Road v : env.getRoads()) {

      v.setRepresentation(new Object2d(v, COLOR_VOIRIE));

    }

    return env.getRoads();
  }

  /*
   * ------------------------ Style Parcelle
   */
  private static final Color COLOR_PARCELLE = new Color(60, 60, 60);

  private static IFeatureCollection<? extends IFeature> generateCadastraParcelRepresentation(
      Environnement env) {
    for (CadastralParcel p : env.getParcelles()) {

      p.setRepresentation(new ObjectCartoon(p, Color.white, ColorLocalRandom
          .getRandomColor(COLOR_PARCELLE, 10, 10, 10), 3, 0.0));

    }
    return env.getParcelles();
  }

  /*
   * ------------------------ Style Sous - Parcelle
   */
  private static final Color COLOR_SOUS_PARCELLE = new Color(162, 205, 90);
  private static final int radius = 50;

  private static IFeatureCollection<? extends IFeature> generateSubParcelRepresentation(
      Environnement env) {
    // TODO Auto-generated method stub

    for (SubParcel sp : env.getSubParcels()) {

      sp.setRepresentation(new Object2d(sp, ColorLocalRandom.getRandomColor(
          COLOR_SOUS_PARCELLE, radius, radius, radius)));

    }

    return env.getSubParcels();

  }

  /*
   * ------------------------ Style Sous - Faitage
   */

  private static final Color COULOR_FAITAGE = Color.red;

  private static IFeatureCollection<? extends IFeature> generateRoofingRepresentation(
      Environnement env) {

    IFeatureCollection<IFeature> featOut = new FT_FeatureCollection<IFeature>();

    for (_AbstractBuilding b : env.getBuildings()) {

      IGeometry geom = b.getToit().getRoofing();

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

  private static IFeatureCollection<? extends IFeature> generateGableRepresentation(
      Environnement env) {

    IFeatureCollection<IFeature> featOut = new FT_FeatureCollection<IFeature>();

    for (_AbstractBuilding b : env.getBuildings()) {

      IMultiCurve<IOrientableCurve> geom = b.getToit().setGable();

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

  private static IFeatureCollection<? extends IFeature> generateGutterRepresentation(
      Environnement env) {

    IFeatureCollection<IFeature> featOut = new FT_FeatureCollection<IFeature>();

    for (_AbstractBuilding b : env.getBuildings()) {

      IGeometry geom = b.getToit().setGutter();

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

  private static IFeatureCollection<? extends IFeature> generateZoneRepresentation(
      Environnement env) {
    for (UrbaZone z : env.getUrbaZones()) {

      z.setRepresentation(new Object2d(z, true, ColorRandom.getRandomColor(),
          1.0f, true));

    }

    return env.getUrbaZones();

  }

  /*
   * Représentation de pans
   */

  private static IFeatureCollection<? extends IFeature> generateRoofSlopesRepresentation(
      Environnement env) {

    IFeatureCollection<IFeature> pans = new FT_FeatureCollection<IFeature>();

    for (_AbstractBuilding b : env.getBuildings()) {

      RoofSurface t = b.getToit();

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
