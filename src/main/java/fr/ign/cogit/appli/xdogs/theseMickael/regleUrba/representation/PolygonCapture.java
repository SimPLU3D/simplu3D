package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.representation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.Appearance;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.LineStripArray;
import javax.media.j3d.Material;
import javax.media.j3d.PointArray;
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

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.sig3d.convert.java3d.ConversionJava3DGeOxygene;
import fr.ign.cogit.geoxygene.sig3d.representation.Default3DRep;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;

/**
 * 
 * Représentation utilisée lors de la saisie de polygone dans un univers 3D
 * 
 * Representation used during the capture of a polygon in 3D
 * 
 * @author MBrasebin
 * 
 */
public class PolygonCapture extends Default3DRep {

  /**
   * La représentation associée à une entité
   * @param feat l'entité que l'on représente
   */
  public PolygonCapture(IFeature feat) {
    super();
    this.feat = feat;
    IDirectPositionList dpl = feat.getGeom().coord();

    int nbPoints = dpl.size();

    // On gère les points
    ArrayList<TransformGroup> lTG = this.geometryWithColor(
        this.generateAppearance(Color.red, 0.75), dpl);
    // On ajoute les sphère à la Branch Group
    for (int i = 0; i < nbPoints; i++) {
      this.bGRep.addChild(lTG.get(i));
    }

    // On génère les lignes
    if (nbPoints > 1) {
      Shape3D shapepleine = new Shape3D(this.geometryWithColor(dpl, Color.red),
          this.generateAppearance(Color.red, 0.75));
      this.bGRep.addChild(shapepleine);
    }

    // On génère les surfaces
    if (nbPoints > 2) {
      List<IOrientableSurface> lOS = new ArrayList<IOrientableSurface>(1);
      lOS.add(new GM_Polygon(new GM_LineString(dpl)));
      Shape3D shapepleine = new Shape3D(ConversionJava3DGeOxygene
          .fromOrientableSToTriangleArray(lOS).getGeometryArray(),
          this.generateAppearance(Color.cyan, 1));
      this.bGRep.addChild(shapepleine);
    }

    this.bGRep.compile();
  }

  /**
   * Méthode permettant de créer l'apparence de la surface
   * 
   * @param isClrd
   * @param color
   * @param coefTransp
   * @param isSolid
   * @return
   */
  private Appearance generateAppearance(Color color, double coefTransp) {

    // Création de l'apparence
    Appearance apparenceFinale = new Appearance();

    // Autorisations pour l'apparence
    apparenceFinale.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_READ);
    apparenceFinale.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);

    // Autorisations pour le material
    apparenceFinale.setCapability(Appearance.ALLOW_MATERIAL_READ);
    apparenceFinale.setCapability(Appearance.ALLOW_MATERIAL_WRITE);

    // Création des attributs du polygone
    PolygonAttributes pa = new PolygonAttributes();

    pa.setCullFace(PolygonAttributes.CULL_NONE);
    pa.setCapability(PolygonAttributes.ALLOW_CULL_FACE_WRITE);

    pa.setBackFaceNormalFlip(false);

    LineAttributes lp = new LineAttributes();

    lp.setLineAntialiasingEnable(true);
    lp.setLineWidth(5);

    apparenceFinale.setLineAttributes(lp);

    // Association à l'apparence des attributs de géométrie et de material
    apparenceFinale.setPolygonAttributes(pa);

    Color3f couleur3F = new Color3f(color);
    // Création du material (gestion des couleurs et de l'affichage)
    Material material = new Material();

    material.setAmbientColor(couleur3F.x / 2, couleur3F.y / 2, couleur3F.z / 2);
    material.setDiffuseColor(couleur3F);
    material.setSpecularColor(new Color3f(1.0f, 1.0f, 1.0f));

    material.setShininess(128);
    apparenceFinale.setMaterial(material);

    if (coefTransp != 1) {

      TransparencyAttributes t_attr =

      new TransparencyAttributes(TransparencyAttributes.BLENDED,
          (float) coefTransp,

          TransparencyAttributes.BLEND_SRC_ALPHA,

          TransparencyAttributes.BLENDED);

      apparenceFinale.setTransparencyAttributes(t_attr);
    }

    return apparenceFinale;
  }

  /**
   * Permet de créer une géométrie Java3D à partir d'une apparence
   * 
   * @param ap
   * @return
   */
  private ArrayList<TransformGroup> geometryWithColor(Appearance ap,
      IDirectPositionList dpl) {
    // On récupère la géométrie sous forme de points

    int nbPoints = dpl.size();

    PointArray pA = new PointArray(nbPoints, GeometryArray.COORDINATES
        | GeometryArray.COLOR_3);

    ArrayList<TransformGroup> lTG = new ArrayList<TransformGroup>(nbPoints);

    pA.setCapability(GeometryArray.ALLOW_COLOR_READ);
    pA.setCapability(GeometryArray.ALLOW_COLOR_WRITE);

    for (int i = 0; i < nbPoints; i++) {

      // On crée la sphère et on lui applique l'apparence choisie
      Sphere s = new Sphere(1, ap);

      s.setCapability(Primitive.ENABLE_APPEARANCE_MODIFY);
      s.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
      s.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);

      s.getShape().setCapability(Shape3D.ALLOW_APPEARANCE_READ);
      s.getShape().setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);

      IDirectPosition pTemp = dpl.get(i);

      // On place le centre de la sphère aux bonnes coordonnées
      Transform3D translate = new Transform3D();
      translate.set(new Vector3f((float) pTemp.getX(), (float) pTemp.getY(),
          (float) pTemp.getZ()));

      TransformGroup TG1 = new TransformGroup(translate);
      TG1.addChild(s);

      lTG.add(TG1);

    }

    return lTG;

  }

  private LineStripArray geometryWithColor(IDirectPositionList dpl,
      Color couleur) {
    // On créer un tableau contenant les lignes à représenter
    Color3f couleur3F = new Color3f(couleur);

    // Effectue la conversion de la géométrie

    // on compte le nombre de points
    int nPoints = dpl.size();
    int nbLignes = 1;

    /*
     * Tableau permettant de définir le nombre de lignes représentées (cas des
     * multi-lignes Ici il y en a une contenant tous les points
     */
    int[] stripVertexCount = new int[nbLignes];
    stripVertexCount[0] = nPoints + 1;

    // On prépare la géométrie et ses autorisations
    LineStripArray geom = new LineStripArray(nPoints + 1,
        GeometryArray.COORDINATES | GeometryArray.COLOR_3, stripVertexCount);

    geom.setCapability(GeometryArray.ALLOW_COLOR_READ);
    geom.setCapability(GeometryArray.ALLOW_COLOR_WRITE);
    geom.setCapability(Geometry.ALLOW_INTERSECT);

    // compteurs pour remplir le tableau de points
    int elementajoute = 0;

    for (int j = 0; j < nPoints; j++) {
      IDirectPosition dp = dpl.get(j);
      Point3d point = new Point3d((float) dp.getX(), (float) dp.getY(),
          (float) dp.getZ());
      geom.setCoordinate(elementajoute, point);
      geom.setColor(elementajoute, couleur3F);

      elementajoute++;
    }
    IDirectPosition dp = dpl.get(0);
    Point3d point = new Point3d((float) dp.getX(), (float) dp.getY(),
        (float) dp.getZ());
    geom.setCoordinate(elementajoute, point);

    return geom;

  }

}
