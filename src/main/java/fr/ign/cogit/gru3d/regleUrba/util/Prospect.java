package fr.ign.cogit.gru3d.regleUrba.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.LineStripArray;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion3DObject;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.sig3d.representation.ConstantRepresentation;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_OrientableSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Solid;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.DistanceFHauteur;
import fr.ign.cogit.sig3d.analysis.ProspectCalculation;

/**
 * Classe utile permettant de : - Générer des distance de prospect - Générer des
 * représentations Java3D basiques
 * 
 * @author MBrasebin
 */
public class Prospect {

  /**
   * Permet de calculer l'emprise d'une parcelle par d = alpha * h sur l'emprise
   * géomModif On calcule la distance a partir de la géométrie geom
   * 
   * @param geomModif emprise sur laquelle on calcule le solide
   * @param geom géométrie dont l'éloignement servira au calcul de la hauteur
   * @param d pente de la distance
   * @return
   */
  public static IGeometry calculeEmprise(IGeometry geomModif, IGeometry geom,
      DistanceFHauteur d) {

    return ProspectCalculation.calculate(geomModif, geom, d.getCoefficient(),
        d.getHauteurOrigine());

  }

  /**
   * Permet de calculer l'emprise d'une parcelle par d = alpha * h sur l'emprise
   * géomModif On calcule la distance a partir de la géométrie geom
   * 
   * @param geomModif géométrie sur laquelle on calcule la distance
   * @param geom la géométrie à partir de laquelle on calcule la distance
   * @param dist distance en fonction de la hauteur utilisée
   * @param zmin zmin du solide
   * @return
   */
  public static GM_Solid calculeEmpriseSolid(IGeometry geomModif,
      IGeometry geom, DistanceFHauteur dist, double zmin) {
    return Prospect.calculeEmpriseSolid(geomModif, geom, dist.getCoefficient(),
        dist.getHauteurOrigine(), zmin);

  }

  /**
   * Permet de calculer l'emprise d'une parcelle par d = alpha * h sur l'emprise
   * géomModif On calcule la distance a partir de la géométrie geom On calcule
   * un volume d'altitude zmin
   * 
   * @param geomModif emprise sur laquelle on calcule le solide
   * @param geom géométrie dont l'éloignement servira au calcul de la hauteur
   * @param pente pente de la distance
   * @param hObj la hauteur au niveau de l'objet
   * @param zmin zmin du solide
   * @return
   */
  public static GM_Solid calculeEmpriseSolid(IGeometry geomModif,
      IGeometry geom, double pente, double hObj, double zmin) {

    List<IOrientableSurface> lOSFinale = new ArrayList<IOrientableSurface>();

    IPolygon poly = (GM_Polygon) ProspectCalculation.calculate(geomModif, geom,
        pente, hObj);
    lOSFinale.add(poly);
    IDirectPositionList dpl = poly.coord();

    int nbPoints = dpl.size();

    DirectPositionList dplFaceInf = new DirectPositionList();

    for (int i = nbPoints - 1; i >= 0; i--) {
      DirectPosition dpTemp = new DirectPosition();
      IDirectPosition dpIni = dpl.get(i);
      dpTemp.setX(dpIni.getX());
      dpTemp.setY(dpIni.getY());
      dpTemp.setZ(zmin);
      dplFaceInf.add(dpTemp);

    }
    lOSFinale.add(new GM_Polygon(new GM_LineString(dplFaceInf)));

    for (int i = 0; i < nbPoints - 1; i++) {
      IDirectPosition dpPred = dpl.get(i);
      IDirectPosition dpSuiv = dpl.get(i + 1);

      DirectPosition dpPredBas = new DirectPosition();
      dpPredBas.setX(dpPred.getX());
      dpPredBas.setY(dpPred.getY());
      dpPredBas.setZ(zmin);

      DirectPosition dpSuivBas = new DirectPosition();
      dpSuivBas.setX(dpSuiv.getX());
      dpSuivBas.setY(dpSuiv.getY());
      dpSuivBas.setZ(zmin);

      DirectPositionList dplTemp = new DirectPositionList();
      dplTemp.add(dpPred);
      dplTemp.add(dpPredBas);
      dplTemp.add(dpSuivBas);
      dplTemp.add(dpSuiv);
      dplTemp.add(dpPred);

      lOSFinale.add(new GM_Polygon(new GM_LineString(dplTemp)));

    }

    return new GM_Solid(lOSFinale);
  }

  /**
   * Permet de calculer l'emprise d'une parcelle par d = alpha * h sur l'emprise
   * géomModif On calcule la distance a partir de la géométrie geom
   * 
   * @param geomModif emprise sur laquelle on calcule le solide
   * @param geom géométrie dont l'éloignement servira au calcul de la hauteur
   * @param d pente de la distance
   * @param zmin hauteur en d = 0
   * @return
   */
  @SuppressWarnings("unchecked")
  public static IGeometry calculeEmpriseSolidBatiment(IGeometry geomModif,
      IGeometry geom, DistanceFHauteur d, double zmin) {
    IGeometry geomDiff = null;
    if (geom instanceof GM_MultiSurface<?>) {
      GM_MultiSurface<GM_OrientableSurface> lSurf = (GM_MultiSurface<GM_OrientableSurface>) geom;
      int nbComp = lSurf.size();
      int ind = -1;
      double aireMax = Double.NEGATIVE_INFINITY;

      for (int i = 0; i < nbComp; i++) {
        double aireTemp = lSurf.get(i).area();
        if (aireTemp > aireMax) {
          aireMax = aireTemp;
          ind = i;
        }
      }
      geomDiff = geomModif.difference(lSurf.get(ind));
    } else {
      geomDiff = geomModif.difference(geom);

    }

    if (geomDiff == null) {
      return null;
    }

    for (IDirectPosition dp : geomDiff.coord()) {
      dp.setZ(zmin);
    }

    IGeometry poly = Prospect.calculeEmprise(geomDiff, geom, d);

    Box3D b = new Box3D(poly);
    double hauteur = b.getURDP().getZ() - zmin;

    return Extrusion3DObject.conversionFromGeom(poly, -hauteur);

  }

  /**
   * Permet de créer l'apparence en fonction de paramètres Dans le cadre d'un
   * ponctuel, certains paramètres n'ont aucun sens
   * 
   * @param isColored
   * @param color
   * @param coefficientTransparence
   * @param isRepresentationSolid
   * @return
   */
  public static Appearance genereApparence(boolean isClrd, Color color,
      double coefTransp, boolean isSolid) {

    // Création de l'apparence
    Appearance appFin = new Appearance();

    // Autorisations pour l'apparence
    appFin.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_READ);
    appFin.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);

    // Autorisations pour le material
    appFin.setCapability(Appearance.ALLOW_MATERIAL_READ);
    appFin.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
    // Création des attributs du polygone
    PolygonAttributes pa = new PolygonAttributes();

    pa.setCullFace(PolygonAttributes.CULL_NONE);
    pa.setCapability(PolygonAttributes.ALLOW_CULL_FACE_WRITE);
    if (isSolid) {
      // Indique que l'on est en mode surfacique
      pa.setPolygonMode(PolygonAttributes.POLYGON_FILL);

      // Indique que l'on n'affiche pas les faces cachées
      if (ConstantRepresentation.cullMode) {
        pa.setCullFace(PolygonAttributes.CULL_BACK);

      }

    } else {
      // Indique que l'on est en mode filaire
      pa.setPolygonMode(PolygonAttributes.POLYGON_LINE);

    }

    LineAttributes lp = new LineAttributes();

    lp.setLineAntialiasingEnable(true);
    lp.setLineWidth(4);
    appFin.setLineAttributes(lp);
    if (isClrd) {

      // Création du material (gestion des couleurs et de l'affichage)
      Material material = new Material();

      material.setAmbientColor(0.2f, 0.2f, 0.2f);
      material.setDiffuseColor(new Color3f(color));
      material.setSpecularColor(new Color3f(1.0f, 1.0f, 1.0f));
      material.setShininess(128);

      // et de material
      appFin.setMaterial(material);
    }

    if (coefTransp != 1) {

      TransparencyAttributes t_attr =

      new TransparencyAttributes(TransparencyAttributes.BLENDED,
          (float) coefTransp,

          TransparencyAttributes.BLEND_SRC_ALPHA,

          TransparencyAttributes.BLENDED);

      // et de transparence
      appFin.setTransparencyAttributes(t_attr);
    }

    appFin.setPolygonAttributes(pa);
    return appFin;

  }

  /**
   * Permet de créer une sphère a partir d'un jeu de coordonnées DP
   * 
   * @param dp
   * @param ap
   * @return
   */
  public static BranchGroup generePoint(IDirectPosition dp, Appearance ap) {
    // On crée la sphère et on lui applique l'apparence choisie
    Sphere s = new Sphere(1, ap);

    s.setCapability(Primitive.ENABLE_APPEARANCE_MODIFY);
    s.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
    s.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);

    s.getShape().setCapability(Shape3D.ALLOW_APPEARANCE_READ);
    s.getShape().setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);

    // On place le centre de la sphère aux bonnes coordonnées
    Transform3D translate = new Transform3D();
    translate.set(new Vector3f((float) dp.getX(), (float) dp.getY(), (float) dp
        .getZ()));

    TransformGroup TG1 = new TransformGroup(translate);
    TG1.addChild(s);

    BranchGroup bg = new BranchGroup();
    bg.addChild(TG1);
    return bg;
  }

  /**
   * Génère une géométrie Java3D à partir d'une couleur indiquée
   * 
   * @return
   */
  public static LineStripArray genereLigne(IDirectPosition dp,
      IDirectPosition dp2, Color couleur) {

    // on compte le nombre de points
    int nPoints = 2;
    int nbLignes = 1;

    /*
     * Tableau permettant de définir le nombre de lignes représentées (cas des
     * multi-lignes Ici il y en a une contenant tous les points
     */
    int[] stripVertexCount = new int[nbLignes];
    stripVertexCount[0] = 2;

    // On prépare la géométrie et ses autorisations
    LineStripArray geom = new LineStripArray(nPoints, GeometryArray.COORDINATES
        | GeometryArray.COLOR_3, stripVertexCount);

    geom.setCapability(GeometryArray.ALLOW_COLOR_READ);
    geom.setCapability(GeometryArray.ALLOW_COLOR_WRITE);
    geom.setCapability(Geometry.ALLOW_INTERSECT);

    Color3f coul = new Color3f(couleur);

    Point3d point = new Point3d((float) dp.getX(), (float) dp.getY(),
        (float) dp.getZ());

    geom.setCoordinate(0, point);
    geom.setColor(0, coul);

    Point3d point2 = new Point3d((float) dp2.getX(), (float) dp2.getY(),
        (float) dp2.getZ());

    geom.setCoordinate(1, point2);
    geom.setColor(1, coul);

    return geom;

  }

  /**
   * Nettoye une géométrie de type GM_MultiSurface en enlevant les polygones
   * d'une aire inférieure à area
   * 
   * @param geom
   */
  public static void clean(GM_MultiSurface<GM_OrientableSurface> gmSurf,
      double area) {

    int nbSurf = gmSurf.size();

    for (int i = 0; i < nbSurf; i++) {
      GM_Polygon poly = (GM_Polygon) gmSurf.get(i);

      if (!poly.isValid() || poly.area() < area) {

        gmSurf.remove(i);
        i--;
        nbSurf--;
      }

    }

  }
}
