package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.propositionBuilding;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

import fr.ign.cogit.appli.geopensim.feature.micro.FormeBatiment;
import fr.ign.cogit.appli.geopensim.geom.ShapeFactory;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.Moteur;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.generation.toit.FlatRoof;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.generation.toit.FourPlanesRoof;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.generation.toit.IRoof;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.generation.toit.OnePlaneRoof;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.generation.toit.PignonRoof;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.representation.Incoherence;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.schemageo.Batiment;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.schemageo.EnveloppeConstructible;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.schemageo.Parcelle;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IRing;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Solid;
import fr.ign.cogit.geoxygene.util.algo.JtsUtil;
import fr.ign.cogit.geoxygene.util.algo.MesureOrientationFeuille;
import fr.ign.cogit.geoxygene.util.algo.MesureOrientationV2;
import fr.ign.cogit.geoxygene.util.conversion.JtsGeOxygene;

public class ZIProposition {

  private static int itMax = 5000;

  /**
   * 
   * @param p
   * @param m
   * @return
   */
  public static Batiment proposition(Parcelle p, Moteur m) {

    if (p.getlEnveloppeContenues().size() == 0) {

      m.computeBuildableEnvelopes(p);

      if (p.getlEnveloppeContenues().size() == 0) {

        return null;
      }

    }

    int iteration = 0;

    List<Parcelle> lP = new ArrayList<Parcelle>();
    lP.add(p);

    while (iteration++ < itMax) {

      System.out.println(iteration);

      Batiment b = generateRandomBuilding(p);

      p.getlBatimentsContenus().add(b);

      try {

        FT_FeatureCollection<Incoherence> lFI = m.processIsParcelOk(lP).get(0);

        if (lFI.size() == 0) {

          return b;

        }
      } catch (Exception e) {
        e.printStackTrace();
        p.getlBatimentsContenus().remove(b);
      }

      p.getlBatimentsContenus().remove(b);

    }

    return null;

  }

  /**
   * 
   * @param p
   * @return
   */
  private static Batiment generateRandomBuilding(Parcelle p) {

    int iteration = 0;
    while (iteration++ < 200) {
      List<EnveloppeConstructible> lE = p.getlEnveloppeContenues();
      EnveloppeConstructible eC = chooseE(lE);

      // List<IOrientableSurface> lOS =
      // ConvertGeomToIOS.convertGeom(eC.getGeom());

      // while (true) {

      // Quelle forme de bâtiment ?
      FormeBatiment fB = getType();

      System.out.println("Type : " + fB);

      // Quel centre ?
      IDirectPosition centre = propositionCentre(p.getGeom());

      if (centre == null) {
        return null;
      }

      // Quel polygone ?
      IPolygon poly = propositionPolygon(fB, centre);

      // Quelle rotation ?
      // poly = propositionRandomRotation(poly);
      poly = propositionOrientedRotation(poly, p);

      if (poly == null) {
        System.out.println("Rotation nulle");
        continue;
      }

      if (!p.getGeom().contains(poly)) {
        continue;
      }

      // Quel toit ?
      IMultiSurface<IPolygon> bGeom = generateRoof(poly, p.getGeom().coord()
          .get(0).getZ(), fB);

      Batiment bati = new Batiment(new GM_Solid(bGeom));

      return bati;
    }

    return null;

  }

  /**
   * 
   * @return
   */
  private static FormeBatiment getType() {

    int rand = (int) (5 * Math.random()) + 2;

    if (rand == 2) {
      return FormeBatiment.Rectangle;

    } else if (rand == 3) {
      return FormeBatiment.FormeL;

    } else if (rand == 4) {
      return FormeBatiment.FormeU;

    } else if (rand == 5) {
      return FormeBatiment.FormeT;

    } else if (rand == 6) {
      return FormeBatiment.Escalier;
    }

    return FormeBatiment.Rectangle;

  }

  /**
   * 
   * @param lE
   * @return
   */
  private static EnveloppeConstructible chooseE(List<EnveloppeConstructible> lE) {

    int nbElem = lE.size();

    return lE.get((int) Math.random() * nbElem);

  }

  /**
   * 
   * @param fB
   * @param iDirectPosition
   * @return
   */
  private static IPolygon propositionPolygon(FormeBatiment fB,
      IDirectPosition iDirectPosition) {
    double largeur = 5 + 10 * Math.random();
    double hauteur = 5 + 10 * Math.random();

    if (largeur > hauteur) {
      double temp = largeur;
      largeur = hauteur;
      hauteur = temp;

    }

    if (fB.equals(FormeBatiment.Rectangle)) {

      return (IPolygon) ShapeFactory.createRectangle(iDirectPosition, largeur,
          hauteur);
    } else if (fB.equals(FormeBatiment.Carre)) {
      return (IPolygon) ShapeFactory.createCarre(iDirectPosition, largeur);
    } else if (fB.equals(FormeBatiment.FormeL)) {

      return (IPolygon) ShapeFactory.createL(iDirectPosition, largeur, hauteur,
          largeur / ((int) (2 + Math.random() * 3)),
          hauteur / ((int) (2 + Math.random() * 3)));

    } else if (fB.equals(FormeBatiment.FormeU)) {
      return (IPolygon) ShapeFactory.createU(iDirectPosition, largeur, hauteur,
          largeur / ((int) (2 + Math.random() * 3)),
          hauteur / ((int) (2 + Math.random() * 3)));
    } else if (fB.equals(FormeBatiment.FormeT)) {

      return (IPolygon) ShapeFactory.createT(iDirectPosition, largeur, hauteur,
          largeur / ((int) (2 + Math.random() * 3)),
          hauteur / ((int) (2 + Math.random() * 3)));

    } else if (fB.equals(FormeBatiment.Escalier)) {

      double largeur1 = largeur / ((int) (2 + Math.random() * 3));
      double hauteur1 = hauteur / ((int) (2 + Math.random() * 3));

      return (IPolygon) ShapeFactory.createEscalier2(iDirectPosition, largeur,
          hauteur, largeur1, hauteur1, largeur1, hauteur1);

    }

    return (IPolygon) ShapeFactory.createRectangle(iDirectPosition, largeur,
        hauteur);

  }

  /**
   * 
   * @param p
   * @return
   */
  private static IDirectPosition propositionCentre(IGeometry p) {

    IDirectPositionList dpl = generateGrid(p);

    int nbP = dpl.size();

    if (nbP == 0) {
      return null;
    }

    return dpl.get((int) (nbP * Math.random()));

  }

  private static IDirectPositionList generateGrid(IGeometry pol) {
    IEnvelope e = pol.envelope();

    double xmin = e.minX();
    double xmax = e.maxX();
    double ymin = e.minY();
    double ymax = e.maxY();

    IDirectPositionList dpl = new DirectPositionList();

    for (double x = xmin; x <= xmax; x = x + 0.5) {
      for (double y = ymin; y <= ymax; y = y + 0.5) {

        IDirectPosition dp = new DirectPosition(x, y);

        if (pol.intersects(new GM_Point(dp))) {

          dpl.add(dp);
        }

      }

    }

    return dpl;

  }

  private static IPolygon propositionRandomRotation(IPolygon poly) {
    double angle = Math.random() * Math.PI * 2;
    try {
      poly = (IPolygon) JtsGeOxygene.makeGeOxygeneGeom(JtsUtil.rotation(
          (Polygon) JtsGeOxygene.makeJtsGeom(poly), angle));
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return poly;

  }

  private static IPolygon propositionOrientedRotation(IPolygon poly, Parcelle p) {

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

  private static IMultiSurface<IPolygon> generateRoof(IPolygon poly,
      double zMin, FormeBatiment fB) {

    int nbPans = (int) (4 * Math.random());

    double zGut = zMin + (int) (5 + 7.5 * Math.random());
    double zMax = zGut + (int) (4 * Math.random() + 1);

    IDirectPositionList dpl = new DirectPositionList();
    dpl.add(poly.coord().get(0));
    dpl.add(poly.coord().get(1));

    System.out.println("NBPans  : " + nbPans);

    IRoof roof;

    List<ILineString> lPignons;

    switch (nbPans) {
      case 0:

        roof = new FlatRoof(poly, zGut);

        break;
      case 1:
        lPignons = determinePignon(fB, 1, poly);
        roof = new OnePlaneRoof(poly, zGut, zMax, lPignons.get(0));
        break;
      case 2:

        lPignons = determinePignon(fB, 2, poly);
        roof = new PignonRoof(poly, zGut, zMax, lPignons);

        break;
      case 3:
        lPignons = determinePignon(fB, 1, poly);
        roof = new PignonRoof(poly, zGut, zMax, lPignons);
        break;

      default:
        roof = new FourPlanesRoof(poly, zGut, zMax);
        break;
    }

    return roof.generateBuilding(zMin);
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

      if (Math.random() > 0.5) {
        IDirectPositionList dplTemp = new DirectPositionList();
        dplTemp.add(geom.coord().get(3));
        dplTemp.add(geom.coord().get(4));

        lPignons.add(new GM_LineString(dplTemp));

      }

    } else if (fB.equals(FormeBatiment.FormeL)) {

      dp1 = geom.coord().get(1);
      dp2 = geom.coord().get(2);
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

}
