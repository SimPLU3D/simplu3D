package fr.ign.cogit.representation.regle.util;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.Billboard;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.LineStripArray;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector3f;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.sig3d.representation.texture.TextureManager;
import fr.ign.cogit.geoxygene.sig3d.sample.Symbology;

public class GenerationPanneau {

  public enum Panneau {
    INCO_COS("/demo3D/reglesurba/cos.png"), INCO_DIST_BATI(
        "/demo3D/reglesurba/directionBati.png"), INCO_DIST_PARC(
        "/demo3D/reglesurba/directionParcelle.png"), INCO_DIST_VOIRIE(
        "/demo3D/reglesurba/directionRoute.png"), INCO_HT_MAX(
        "/demo3D/reglesurba/hauteur.png"), INCO_LARGEUR_MAX(
        "/demo3D/reglesurba/largeur.png"), INCO_INTERDICTION(
        "/demo3D/reglesurba/interdiction.png"), INCO_ADOSSEMENT(
        "/demo3D/reglesurba/interdiction.png"), INCO_ALIGNELENT(
        "/demo3D/reglesurba/alignement.png"), INCO_BANDE_CONSTRUCTIBLE(
        "/demo3D/reglesurba/bandeconstructible.png"), INCO_HAUTEUR_GOUTTIERE(
        "/demo3D/reglesurba/hauteurGouttiere.png"), INCO_HAUTEUR_ETAGE(
        "/demo3D/reglesurba/hauteurEtage.png"), 
        INCO_LARGEUR_BATIMENT("/demo3D/reglesurba/largeur.png"),
        INCO_EMPRISE_MIN("/demo3D/reglesurba/empriseMin.png"),
        INCO_EMPRISE_MAX("/demo3D/reglesurba/empriseMax.png"),
        INCO_ANGLE_MAX("/demo3D/reglesurba/angleMax.png"),
        INCO_ANGLE_MIN("/demo3D/reglesurba/angleMin.png"),
        INCO_SERVITUDE_ANGLE_VUE("/demo3D/reglesurba/ServitudeVueAngle.png"),
        INCO_SERVITUDE_DISTANCE_VUE("/demo3D/reglesurba/ServitudeVueDistance.png"),
        INCO_VISIBILTE("/demo3D/reglesurba/VueObjet.png"),
        INCO_CES("/demo3D/reglesurba/ces.png");

    private String url;

    Panneau(String url) {
      this.url = url;
    }

    public String getURL() {
      return url;
    }

  }

  public static BranchGroup generateFromPanneau(IDirectPosition dp, Panneau p) {

    return generateFromPanneau(dp, 20.0, Color.black, 10.0, p, 2.0);

  }

  public static BranchGroup generateFromPanneau(IDirectPosition dp,
      double hauteur, Color coul, double width, Panneau p, double lineWidth) {

    return generateBG(dp, hauteur, coul, width, lineWidth, Symbology.class
        .getResource(p.getURL()).getPath());
  }

  public static BranchGroup generate(IDirectPosition dp, double hauteur,
      Color coul, double width, String imgPath, double lineWidth) {

    return generateBG(dp, hauteur, coul, width, lineWidth, imgPath);

  }

  public static BranchGroup generateBG(IDirectPosition p, double hauteur,
      Color coul, double width, double lineWidth, String texPath) {

    Color3f col3f = new Color3f(coul);

    LineStripArray lsa = geometryWithColor(p, hauteur, col3f);
    BranchGroup bh = generatePanel(p, width, col3f, hauteur, texPath);

    BranchGroup bg = new BranchGroup();

    bg.addChild(new Shape3D(lsa, lineAppearence(col3f, (float) lineWidth)));
    bg.addChild(bh);

    return bg;
  }

  /**
   * Génère un cube portant les spécifications indiquées dans le constructeurs
   * aux coordonnées dp
   */
  private static BranchGroup generatePanel(IDirectPosition dp, double width,
      Color3f coul, double hauteur, String texture) {

    double x = dp.getX();

    double y = dp.getY();

    double z = dp.getZ() + hauteur + width / 2;

    Point3d p1 = new Point3d(-width / 2, 0, -width / 2);
    Point3d p2 = new Point3d(width / 2, 0, -width / 2);
    Point3d p3 = new Point3d(width / 2, 0, width / 2);
    Point3d p4 = new Point3d(-width / 2, 0, width / 2);

    // Construction de l'objet geometrique QuadArray constitue de 16
    // points
    QuadArray quadArray;

    // Création de l'apparence
    Appearance apparenceFinale = new Appearance();

    Point3d[] ptTab = new Point3d[] { p1, p2, p3, p4, p4, p3, p2, p1,

    };

    Texture2D text = TextureManager.textureLoading(texture);

    if (text == null) {
      // On applique une couleur unique au cube.
      quadArray = new QuadArray(8, GeometryArray.COORDINATES
          | GeometryArray.COLOR_3);

      Color3f color3f = new Color3f(coul);

      // Tableau des points constituant les faces
      quadArray.setCoordinates(0, ptTab);

      // Tableau des couleurs des 4 sommets de chaque face
      quadArray.setColors(0, new Color3f[] { color3f, color3f, color3f,
          color3f, color3f, color3f, color3f, color3f });

    } else {

      TexCoord2f t0 = new TexCoord2f(0f, 0f);
      TexCoord2f t1 = new TexCoord2f(1f, 0f);
      TexCoord2f t2 = new TexCoord2f(1f, 1f);
      TexCoord2f t3 = new TexCoord2f(0f, 1f);

      // On applique les textures
      TexCoord2f[] texCoord = new TexCoord2f[] { t0, t1, t2, t3, t0, t1, t2,
          t3, };

      // On applique une couleur unique au cube.
      quadArray = new QuadArray(8, GeometryArray.COORDINATES
          | GeometryArray.TEXTURE_COORDINATE_2);

      // Tableau des points constituant les faces
      quadArray.setCoordinates(0, ptTab);

      quadArray.setTextureCoordinates(0, 0, texCoord);

      TextureAttributes texAttr = new TextureAttributes();
      texAttr.setTextureMode(TextureAttributes.REPLACE);

      apparenceFinale.setTransparencyAttributes(new TransparencyAttributes(
          TransparencyAttributes.BLENDED, 1.0f));

      apparenceFinale.setTexture(text);
      apparenceFinale.setTextureAttributes(texAttr);
    }

    Shape3D s = new Shape3D();
    s.setGeometry(quadArray);
    s.setAppearance(apparenceFinale);

    // Autorisations sur la Shape3D
    s.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
    s.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
    s.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
    s.setCapability(Node.ALLOW_LOCAL_TO_VWORLD_READ);
    s.setCapability(Node.ALLOW_LOCALE_READ);

    // Group g1 = ContrainteCOSRepresentation.generateLocalText(cosMin, coul,
    // tailleText, -2 * tailleText / 3);
    // Group g2 = ContrainteCOSRepresentation.generateLocalText(cosActu, coul,
    // tailleText, tailleText / 3);
    // Group g3 = ContrainteCOSRepresentation.generateLocalText(cosMax, coul,
    // tailleText, 4 * tailleText / 3);

    // Create the transformgroup used for the billboard
    TransformGroup billBoardGroup = new TransformGroup();
    // Set the access rights to the group
    billBoardGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

    AxisAngle4d rotateAxisAngle = new AxisAngle4d(1f, 0f, 0f, Math.PI / 2.0);

    Transform3D rotX = new Transform3D();
    rotX.set(rotateAxisAngle);

    TransformGroup tgRotX = new TransformGroup(rotX);
    tgRotX.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

    tgRotX.addChild(s);
    // tgRotX.addChild(g1);
    // tgRotX.addChild(g2);
    // tgRotX.addChild(g3);

    // Add the cube to the group
    billBoardGroup.addChild(tgRotX);

    Billboard myBillboard = new Billboard(billBoardGroup,

    Billboard.ROTATE_ABOUT_POINT, new Point3f());

    myBillboard.setSchedulingBounds(new BoundingSphere(new Point3d(),
        Double.POSITIVE_INFINITY));

    // On place le centre aux bonnes coordonnées
    Transform3D translate = new Transform3D();
    translate.set(new Vector3f((float) x, (float) y, (float) z));
    TransformGroup transform = new TransformGroup(translate);
    transform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    transform.addChild(billBoardGroup);
    transform.addChild(myBillboard);

    BranchGroup bg = new BranchGroup();
    bg.addChild(transform);

    return bg;

  }

  private static LineStripArray geometryWithColor(IDirectPosition dp,
      double hauteur, Color3f coul) {

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

    geom.setCoordinate(0, new Point3f((float) dp.getX(), (float) dp.getY(),
        (float) dp.getZ()));
    geom.setColor(0, coul);

    geom.setCoordinate(1, new Point3f((float) dp.getX(), (float) dp.getY(),
        (float) (dp.getZ() + hauteur)));
    geom.setColor(1, coul);

    return geom;

  }

  private static Appearance lineAppearence(Color3f color, float lineWidth) {

    // Création de l'apparence
    Appearance apparenceFinale = new Appearance();

    // Autorisations pour l'apparence
    apparenceFinale.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_READ);
    apparenceFinale.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);

    // Autorisations pour le material
    apparenceFinale.setCapability(Appearance.ALLOW_MATERIAL_READ);
    apparenceFinale.setCapability(Appearance.ALLOW_MATERIAL_WRITE);

    apparenceFinale.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_READ);
    apparenceFinale.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);

    // Autorisations pour le material
    apparenceFinale.setCapability(Appearance.ALLOW_MATERIAL_READ);
    apparenceFinale.setCapability(Appearance.ALLOW_MATERIAL_WRITE);

    // Association à l'apparence des attributs de géométrie et de material

    // Création des attributs du polygone

    LineAttributes lp = new LineAttributes();

    lp.setLineAntialiasingEnable(true);
    lp.setLineWidth(lineWidth);

    lp.setLinePattern(LineAttributes.PATTERN_SOLID);

    apparenceFinale.setLineAttributes(lp);

    // Création du material (gestion des couleurs et de l'affichage)
    Material material = new Material();

    material.setAmbientColor(0.2f, 0.2f, 0.2f);
    material.setDiffuseColor(new Color3f(color));
    material.setSpecularColor(new Color3f(1.0f, 1.0f, 1.0f));
    material.setShininess(128);

    apparenceFinale.setMaterial(material);

    return apparenceFinale;
  }

}
