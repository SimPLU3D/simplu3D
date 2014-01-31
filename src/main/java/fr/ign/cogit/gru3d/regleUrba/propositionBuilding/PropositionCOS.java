package fr.ign.cogit.gru3d.regleUrba.propositionBuilding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

import fr.ign.cogit.appli.geopensim.feature.micro.FormeBatiment;
import fr.ign.cogit.appli.geopensim.geom.ShapeFactory;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IRing;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Solid;
import fr.ign.cogit.geoxygene.util.algo.JtsUtil;
import fr.ign.cogit.geoxygene.util.algo.MesureOrientationFeuille;
import fr.ign.cogit.geoxygene.util.algo.MesureOrientationV2;
import fr.ign.cogit.geoxygene.util.conversion.JtsGeOxygene;
import fr.ign.cogit.gru3d.regleUrba.Moteur;
import fr.ign.cogit.gru3d.regleUrba.generation.toit.FlatRoof;
import fr.ign.cogit.gru3d.regleUrba.generation.toit.IRoof;
import fr.ign.cogit.gru3d.regleUrba.propositionBuilding.export.Export;
import fr.ign.cogit.gru3d.regleUrba.schemageo.Batiment;
import fr.ign.cogit.gru3d.regleUrba.schemageo.Parcelle;
import fr.ign.cogit.gru3d.regleUrba.schemageo.Route;

public class PropositionCOS {

  private static int itMax = 100000;
  private final static Logger logger = Logger.getLogger(PropositionCOS.class
      .getName());

  private static IDirectPositionList grid = null;

  /**
   * 
   * @param p
   * @param m
   * @return
   */
  public static Batiment proposition(Parcelle p, Moteur m) {

    System.out.println("Je passe là les bibis");

    long t = System.currentTimeMillis();

    /*
     * if (p.getlEnveloppeContenues().size() == 0) {
     * 
     * m.computeBuildableEnvelopes(p);
     * 
     * if (p.getlEnveloppeContenues().size() == 0) {
     * 
     * return null; }
     * 
     * }
     */

    int iteration = 0;

    double satisfaction = Double.NEGATIVE_INFINITY;

    Batiment client = null;

    while (iteration++ < itMax) {

      // logger.warn("Nombre d'itération : " + iteration);

      Batiment b = generateRandomBuilding(p);

      if (b == null) {
        continue;
      }

      p.getlBatimentsContenus().add(b);
      p.setCos(-1);

      try {

        // long time = // System.currentTimeMillis();

        if (m.processIsParceFastlOk(p)) {

          // logger.warn("Vérification des contraintes : " +
          // (// System.currentTimeMillis // time = //
          // System.currentTimeMillis();

          double cosTemp = p.getCos();

          if (cosTemp > satisfaction) {

            satisfaction = cosTemp;

            // System.out.println("SCORE = " + satisfaction);

            client = b;

            if (Export.doExport != Export.AvailableExport.NONE
                && Export.exportAll) {
              Export.export(b, p, "COS=" + cosTemp);

            }

          }

        }
      } catch (Exception e) {
        e.printStackTrace();
        p.getlBatimentsContenus().remove(b);
      }

      p.getlBatimentsContenus().remove(b);

    }

    System.out.println("Exécution : " + (System.currentTimeMillis() - t));

    grid = null;
    if (client != null) {

      p.setCes(-1);
      p.setCos(-1);

      p.getlBatimentsContenus().add(client);

      if (Export.doExport != Export.AvailableExport.NONE && !Export.exportAll) {
        Export.export(client, p, "COS=" + p.getCes() + "CES=" + p.getCos());

      }

      System.out.println("Processus terminé, bâtiment trouvé");
      return client;
    }

    System.out.println("Processus terminé, pas de bâtiment trouvé");
    return null;

  }

  /**
   * 
   * @param p
   * @return
   */
  private static Batiment generateRandomBuilding(Parcelle p) {

    // List<EnveloppeConstructible> lE = p.getlEnveloppeContenues();
    // EnveloppeConstructible eC = chooseE(lE);

    // List<IOrientableSurface> lOS =
    // ConvertGeomToIOS.convertGeom(eC.getGeom());

    // while (true) {

    // Quelle forme de bâtiment ?

    Batiment b = null;

    // while(b == null){

    // if (p.getlBatimentsContenus().size() == 0) {
    b = generateBuildingType1(p);
    // } else {
    // b = generateBuildingType2(p);
    // }

    // }

    return b;

  }

  // //////////////////////
  // //////TYPE 1
  // //////////////////////

  private static Batiment generateBuildingType1(Parcelle p) {

    // long time = // System.currentTimeMillis();

    FormeBatiment fB = getType1();

    // logger.warn("Génération du type : " + (// System.currentTimeMillis() -
    // time));
    // time = System.currentTimeMillis();

    // Quel centre ?
    IDirectPosition centre = propositionCentre1(p);

    // logger.warn("Choix du centre : " + (// System.currentTimeMillis() -
    // time));
    // time = // System.currentTimeMillis();

    if (centre == null) {
      return null;
    }

    double largeur = 4 + Math.random() * 20;
    double longueur = 4 + Math.random() * 20;// largeur * (1 + Math.random() /
                                             // 2);

    if (longueur < largeur) {
      double temp = longueur;
      longueur = largeur;
      largeur = temp;
    }

    // Quel polygone ?
    IPolygon poly = propositionPolygon1(fB, centre, largeur, longueur);

    // logger.warn("Génération polygone : " + (// System.currentTimeMillis() -
    // time));
    // time = // System.currentTimeMillis();

    // Quelle rotation ?
    // poly = propositionRandomRotation(poly);
    poly = propositionOrientedRotation1(poly, p);

    // logger.warn("Orientation du polygone : " + (// System.currentTimeMillis()
    // -
    // time));
    // time = // System.currentTimeMillis();

    if (poly == null) {
      // System.out.println("Rotation nulle");
      return null;
    }

    if (!p.getGeom().contains(poly)) {
      return null;
    }

    // Quel toit ?
    IMultiSurface<IPolygon> bGeom = generateRoof(poly,
        p.getGeom().coord().get(0).getZ(), fB, largeur, longueur);

    // logger.warn("Génération du toit : " + (// System.currentTimeMillis() -
    // time));
    // time = // System.currentTimeMillis();

    Batiment bati = new Batiment(new GM_Solid(bGeom));

    return bati;
  }

  /**
   * 
   * @return
   */
  private static FormeBatiment getType1() {

    double rand = Math.random();

    if (rand < 0.5) {

      return FormeBatiment.FormeL;

    } else if (rand < 2) {
      return FormeBatiment.FormeT;
    }

    return FormeBatiment.FormeT;

  }

  /**
   * 
   * @param fB
   * @param iDirectPosition
   * @return
   */
  private static IPolygon propositionPolygon1(FormeBatiment fB,
      IDirectPosition iDirectPosition, double largeur, double longueur) {

    if (largeur > longueur) {
      double temp = largeur;
      largeur = longueur;
      longueur = temp;

    }

    if (fB.equals(FormeBatiment.Rectangle)) {

      return (IPolygon) ShapeFactory.createRectangle(iDirectPosition, largeur,
          longueur);

    } else if (fB.equals(FormeBatiment.FormeL)) {

      return (IPolygon) ShapeFactory.createL(iDirectPosition, largeur,
          longueur, largeur * -2, longueur - 2);

    }

    // FORM U

    return (IPolygon) ShapeFactory.createT(iDirectPosition, largeur, longueur,
        largeur - 2, longueur - 2);

  }

  /**
   * 
   * @param p
   * @return
   */
  private static IDirectPosition propositionCentre1(Parcelle p) {

    if (grid == null) {

      grid = generateGrid1(p);
    }

    int nbP = grid.size();

    if (nbP == 0) {
      return null;
    }

    return grid.get((int) (nbP * Math.random()));

  }

  private static IDirectPositionList generateGrid1(Parcelle p) {

    IEnvelope e = p.getGeom().envelope();

    double xmin = e.minX();
    double xmax = e.maxX();
    double ymin = e.minY();
    double ymax = e.maxY();

    IDirectPositionList dpl = new DirectPositionList();

    for (double x = xmin; x <= xmax; x = x + 0.5) {
      for (double y = ymin; y <= ymax; y = y + 0.5) {

        IDirectPosition dp = new DirectPosition(x, y);

        if (p.getGeom().intersects(new GM_Point(dp))) {/*
                                                        * 
                                                        * for (Route r : lR) {
                                                        * 
                                                        * double dist =
                                                        * r.getGeom
                                                        * ().distance(new
                                                        * GM_Point(dp));
                                                        * 
                                                        * if (dist > 8 && dist <
                                                        * 20) { dpl.add(dp); }
                                                        * 
                                                        * }
                                                        */

          dpl.add(dp);

        }

      }

    }

    return dpl;

  }

  private static IPolygon propositionOrientedRotation1(IPolygon poly, Parcelle p) {

    // Orientation du bâtiment
    Geometry polygon;
    try {
      polygon = JtsGeOxygene.makeJtsGeom(poly);

      double angle = Math.PI * 2 * Math.random();

      poly = (IPolygon) JtsGeOxygene.makeGeOxygeneGeom(JtsUtil.rotation(
          (Polygon) JtsGeOxygene.makeJtsGeom(poly), angle));

      return poly;

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return null;

  }

  private static IMultiSurface<IPolygon> generateRoof(IPolygon poly,
      double zMin, FormeBatiment fB, double largeur, double longueur) {

    /*
     * int nbPans = (int) (1 * Math.random());
     * 
     * Random r = new Random();
     * 
     * double spFacteur = 2 * Math.random();
     * 
     * double zMax = zMin + Math.sqrt(spFacteur * largeur * longueur);
     * 
     * double zGut = zMax - (zMax - zMin) / 3;
     * 
     * // // System.out.println("zGut     :  " + zGut + "    zMax   " + zMax );
     * 
     * IDirectPositionList dpl = new DirectPositionList();
     * dpl.add(poly.coord().get(0)); dpl.add(poly.coord().get(1));
     * 
     * // System.out.println("NBPans  : " + nbPans);
     * 
     * IRoof roof;
     * 
     * List<ILineString> lPignons;
     * 
     * switch (nbPans) {
     * 
     * case 0:
     * 
     * lPignons = determinePignon(fB, 2, poly); roof = new PignonRoof(poly,
     * zGut, zMax, lPignons);
     * 
     * break; case 1: lPignons = determinePignon(fB, 1, poly); roof = new
     * PignonRoof(poly, zGut, zMax, lPignons); break;
     * 
     * default: roof = new FourPlanesRoof(poly, zGut, zMax); break; }
     */

    double zMax = zMin + (int) (Math.random() * 20) + 1;

    IRoof roof = new FlatRoof(poly, zMax);
    IMultiSurface<IPolygon> iMOut = roof.generateBuilding(zMin);

    return iMOut;
  }

  public static List<ILineString> determinePignon(FormeBatiment fB, int nbPans,
      IPolygon geom) {
    List<ILineString> lPignons = new ArrayList<ILineString>();
    IDirectPosition dp1 = null, dp2 = null, dp3 = null, dp4 = null;

    if (fB.equals(FormeBatiment.Carre) || fB.equals(FormeBatiment.Rectangle)) {

      dp1 = geom.coord().get(0);
      dp2 = geom.coord().get(1);
      dp3 = geom.coord().get(2);
      dp4 = geom.coord().get(3);
    } else if (fB.equals(FormeBatiment.Barre)) {

      dp1 = geom.coord().get(0);
      dp2 = geom.coord().get(1);
      dp3 = geom.coord().get(4);
      dp4 = geom.coord().get(5);

    } else if (fB.equals(FormeBatiment.Cercle)) {

      dp1 = geom.coord().get(0);
      dp2 = geom.coord().get(1);
      dp3 = geom.coord().get(geom.coord().size() / 2);
      dp4 = geom.coord().get(geom.coord().size() / 2 + 1);

    } else if (fB.equals(FormeBatiment.Escalier)) {
      dp1 = geom.coord().get(3);
      dp2 = geom.coord().get(4);
      dp3 = geom.coord().get(7);
      dp4 = geom.coord().get(8);

    } else if (fB.equals(FormeBatiment.FormeU)) {

      dp1 = geom.coord().get(1);
      dp2 = geom.coord().get(2);
      dp3 = geom.coord().get(5);
      dp4 = geom.coord().get(6);

    } else if (fB.equals(FormeBatiment.FormeT)) {

      dp1 = geom.coord().get(0);
      dp2 = geom.coord().get(1);
      dp3 = geom.coord().get(6);
      dp4 = geom.coord().get(7);
      /*
       * if (Math.random() > 0.5) { IDirectPositionList dplTemp = new
       * DirectPositionList(); dplTemp.add(geom.coord().get(3));
       * dplTemp.add(geom.coord().get(4));
       * 
       * lPignons.add(new GM_LineString(dplTemp));
       * 
       * }
       */

    } else if (fB.equals(FormeBatiment.FormeL)) {

      dp1 = geom.coord().get(2);
      dp2 = geom.coord().get(3);
      dp3 = geom.coord().get(4);
      dp4 = geom.coord().get(5);

    }

    IDirectPositionList dpl2 = new DirectPositionList();
    dpl2.add(dp1);
    dpl2.add(dp2);

    lPignons.add(new GM_LineString(dpl2));

    if (nbPans == 2) {
      IDirectPositionList dpl3 = new DirectPositionList();
      dpl3.add(dp3);
      dpl3.add(dp4);

      lPignons.add(new GM_LineString(dpl3));
    }

    return lPignons;
  }

  // //////////////////////
  // //////TYPE 2
  // //////////////////////

  private static Batiment generateBuildingType2(Parcelle p) {

    FormeBatiment fB = FormeBatiment.Rectangle;

    // System.out.println("Type : " + fB);

    // Quel centre ?
    IDirectPosition centre = propositionCentre2(p);

    if (centre == null) {
      return null;
    }

    // Quel polygone ?
    IPolygon poly = propositionPolygon2(fB, centre);

    // Quelle rotation ?
    // poly = propositionRandomRotation(poly);
    poly = propositionOrientedRotation2(poly, p);

    if (poly == null) {
      // System.out.println("Rotation nulle");
      return null;
    }

    if (!p.getGeom().contains(poly)) {
      return null;
    }

    // Quel toit ?
    IMultiSurface<IPolygon> bGeom = generateRoof2(poly, p.getGeom().coord()
        .get(0).getZ(), fB);

    Batiment bati = new Batiment(new GM_Solid(bGeom));

    return bati;
  }

  /**
   * 
   * @param p
   * @return
   */
  private static IDirectPosition propositionCentre2(Parcelle p) {
    if (grid == null) {

      grid = generateGrid2(p);
    }

    int nbP = grid.size();

    if (nbP == 0) {
      return null;
    }

    return grid.get((int) (nbP * Math.random()));

  }

  private static IDirectPositionList generateGrid2(Parcelle p) {

    List<Parcelle> lP = p.getlParcelleBordante();

    List<Route> lRoute = p.getlRouteBordante();

    List<Parcelle> lPTemp = new ArrayList<Parcelle>();

    for (Parcelle parc : lP) {

      double dmin = Double.POSITIVE_INFINITY;

      for (Route r : lRoute) {

        dmin = Math.min(dmin, r.getGeom().distance(parc.getGeom()));

      }

      if (dmin > 5) {
        lPTemp.add(parc);
      }

    }

    IEnvelope e = p.getGeom().envelope();

    double xmin = e.minX();
    double xmax = e.maxX();
    double ymin = e.minY();
    double ymax = e.maxY();

    IDirectPositionList dpl = new DirectPositionList();

    for (double x = xmin; x <= xmax; x = x + 0.5) {
      for (double y = ymin; y <= ymax; y = y + 0.5) {

        IDirectPosition dp = new DirectPosition(x, y);

        if (p.getGeom().intersects(new GM_Point(dp))) {

          for (Parcelle r : lPTemp) {

            double dist = r.getGeom().distance(new GM_Point(dp));

            if (dist > 2 && dist < 4) {
              dpl.add(dp);
            }

          }

        }

      }

    }

    return dpl;

  }

  /**
   * 
   * @param fB
   * @param iDirectPosition
   * @return
   */
  private static IPolygon propositionPolygon2(FormeBatiment fB,
      IDirectPosition iDirectPosition) {

    Random r = new Random();

    double largeur = 4 + 1 * (r.nextGaussian() + 0.5);
    double hauteur = 6 + 1 * (r.nextGaussian() + 0.5);

    if (largeur > hauteur) {
      double temp = largeur;
      largeur = hauteur;
      hauteur = temp;

    }

    if (fB.equals(FormeBatiment.Rectangle)) {

      return (IPolygon) ShapeFactory.createRectangle(iDirectPosition, largeur,
          hauteur);

    } else if (fB.equals(FormeBatiment.FormeL)) {

      return (IPolygon) ShapeFactory.createL(iDirectPosition, largeur, hauteur,
          largeur - 2, hauteur - 2);

    }

    // FORM U

    return (IPolygon) ShapeFactory.createT(iDirectPosition, largeur, hauteur,
        largeur - 2, hauteur - 2);

  }

  private static IPolygon propositionOrientedRotation2(IPolygon poly, Parcelle p) {

    IRing ext = ((IPolygon) p.getGeom()).getExterior();

    double dmin = Double.POSITIVE_INFINITY;
    ILineString ls = null;

    for (int i = 0; i < ext.coord().size() - 1; i++) {

      IDirectPosition dp1 = ext.coord().get(i);
      IDirectPosition dp2 = ext.coord().get(i + 1);

      IDirectPositionList dpl = new DirectPositionList();
      dpl.add(dp1);
      dpl.add(dp2);

      ILineString lsTemp = new GM_LineString(dpl);
      double distT = lsTemp.distance(poly);

      if (distT < dmin) {
        ls = lsTemp;
        dmin = distT;
      }

    }

    // Orientation du bâtiment
    Geometry polygon;
    try {
      polygon = JtsGeOxygene.makeJtsGeom(poly);

      double orientationBatiment = MesureOrientationV2
          .getOrientationGenerale(polygon);

      if (orientationBatiment == 999.9) {
        // dans ce cas on utilise les murs du batiment
        MesureOrientationFeuille mesureOrientation = new MesureOrientationFeuille(
            polygon, Math.PI * 0.5);
        double orientationCotes = mesureOrientation.getOrientationPrincipale();
        if (orientationCotes != -999.9) {
          orientationBatiment = orientationCotes;
        }
      }

      // Orientation du tronçon
      double orientationTroncon = JtsUtil.projectionPointOrientationTroncon(ls
          .coord().get(0), ls);

      double angle = orientationTroncon - orientationBatiment;

      poly = (IPolygon) JtsGeOxygene.makeGeOxygeneGeom(JtsUtil.rotation(
          (Polygon) JtsGeOxygene.makeJtsGeom(poly), angle));

      return poly;

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return null;

  }

  private static IMultiSurface<IPolygon> generateRoof2(IPolygon poly,
      double zMin, FormeBatiment fB) {

    int nbPans = (int) (3 * Math.random());

    double zGut = zMin + (int) (4 + 4 * (0.5 - (Math.random())));

    IDirectPositionList dpl = new DirectPositionList();
    dpl.add(poly.coord().get(0));
    dpl.add(poly.coord().get(1));

    // System.out.println("NBPans  : " + nbPans);

    IRoof roof = new FlatRoof(poly, zGut);
    IMultiSurface<IPolygon> iMOut = roof.generateBuilding(zMin);

    return iMOut;
  }

}
