package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.representation;

import java.awt.Color;
import java.awt.Font;
import java.net.URL;

import javax.media.j3d.Appearance;
import javax.media.j3d.Billboard;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.LineStripArray;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
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

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.gui.MainWindow;
import fr.ign.cogit.geoxygene.sig3d.representation.Default3DRep;
import fr.ign.cogit.geoxygene.sig3d.representation.texture.TextureManager;
import fr.ign.cogit.geoxygene.sig3d.sample.Symbology;
import fr.ign.cogit.geoxygene.sig3d.semantic.Map3D;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;

public class ContrainteHauteurRepresentation extends Default3DRep {

  public static void main(String[] args) {

    MainWindow fenPrincipale = new MainWindow();
    Map3D carte = fenPrincipale.getInterfaceMap3D().getCurrent3DMap();

    // On créé la collection d'objets à afficher (100 éléments ici)
    FT_FeatureCollection<IFeature> featCollec = new FT_FeatureCollection<IFeature>();

    for (int i = 0; i < 5; i++) {

      // On prend un point au hasard dont les coordonnées varient
      // entre 0 et 100
      DirectPosition dp = new DirectPosition(Math.random() * Symbology.xmax,
          Math.random() * Symbology.ymax, Math.random() * Symbology.zmax);

      // ObjetGeographique est juste une implémentation de FT_Feautre
      // Elle n'a qu'un constructeur avec une géométrie.
      featCollec.add(new DefaultFeature(new GM_Point(dp)));
    }
    URL url = Symbology.class.getResource("/demo3D/reglesurba/hauteur.png");

    // On récupère le chemin du fichier
    // String path = url.getPath().toString();
    // On génère pour chaque élément la représentation que l'on souhaite

    for (int i = 0; i < 5; i++) {
      IFeature feat = featCollec.get(i);

      feat.setRepresentation(new ContrainteHauteurRepresentation(feat, 70, 50,
          Color.pink, 30, url.getPath(), 3.0, 10));
      /*
       * feat.setRepresentation(new RepresentationModel(feat, // L'entité qui //
       * aura une // nouvelle // représentation path, // L'objet Java3D qui le
       * représentera Math.PI * i / nbElement, // Rotation suivant X 0, //
       * Rotation suivant Y 0, // Rotation suivant Z i / 50// Taille de l'objet
       * ));
       */
    }

    // On crée la couche (les entités ayant une représentation on utilise ce
    // constructeur)
    VectorLayer couche = new VectorLayer(featCollec, "Liste points");
    // On ajoute la couche à la carte
    carte.addLayer(couche);
  }

  public ContrainteHauteurRepresentation(IFeature feat, double hauteur,
      double hMax, Color coul, double width, String imgPath, double lineWidth,
      double tailleText) {
    super();
    this.feat = feat;

    GM_Point p = (GM_Point) feat.getGeom();
    this.bGRep.addChild(ContrainteHauteurRepresentation.generateBG(p, hauteur,
        coul, width, "" + hMax, lineWidth, tailleText, imgPath));

  }

  public static BranchGroup generateBG(GM_Point p, double hauteur, Color coul,
      double width, String hMax, double lineWidth, double tailleText,
      String texPath) {

    Color3f col3f = new Color3f(coul);

    LineStripArray lsa = ContrainteHauteurRepresentation.geometryWithColor(p,
        hauteur, col3f);
    BranchGroup bh = ContrainteHauteurRepresentation.generateCube(
        p.getPosition(), width, col3f, hMax, hauteur, tailleText, texPath);

    BranchGroup bg = new BranchGroup();

    bg.addChild(new Shape3D(lsa, ContrainteHauteurRepresentation
        .lineAppearence(col3f, (float) lineWidth)));
    bg.addChild(bh);
    return bg;
  }

  private static LineStripArray geometryWithColor(GM_Point p, double hauteur,
      Color3f coul) {

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

    IDirectPosition dp = p.getPosition();

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

  /**
   * Génère un cube portant les spécifications indiquées dans le constructeurs
   * aux coordonnées dp
   */
  private static BranchGroup generateCube(IDirectPosition dp, double width,
      Color3f coul, String text, double hauteur, double tailleText,
      String imgPath) {

    double x = dp.getX();

    double y = dp.getY();

    double z = dp.getZ() + width / 2 + hauteur;

    Texture2D t = TextureManager.textureLoading(imgPath);

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

    if (t == null) {
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

      apparenceFinale.setTexture(t);
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

    Font3D f3d = new Font3D(new Font("ARIAL", Font.PLAIN, 1),
        new FontExtrusion());

    Text3D text3D = new Text3D(f3d, text, new Point3f(0, 0, 0),
        Text3D.ALIGN_CENTER, Text3D.PATH_RIGHT);

    text3D.setCapability(Text3D.ALLOW_FONT3D_WRITE);
    text3D.setCapability(Geometry.ALLOW_INTERSECT);

    Shape3D s3D1 = new Shape3D(text3D,
        ContrainteHauteurRepresentation.genereApparenceText(coul, 1.0));
    s3D1.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
    s3D1.setCapability(Shape3D.ALLOW_APPEARANCE_READ);

    TransformGroup objScale = new TransformGroup();
    Transform3D t3d = new Transform3D();
    t3d.setScale(tailleText);
    objScale.setTransform(t3d);
    objScale.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

    objScale.addChild(s3D1);

    Transform3D rotX2 = new Transform3D();
    rotX2.rotX(-Math.PI / 2.0);
    TransformGroup tgRotX2 = new TransformGroup(rotX2);
    tgRotX2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

    tgRotX2.addChild(objScale);

    Transform3D tg1 = new Transform3D();
    tg1.set(new Vector3f((float) 0.0, (float) 0.0, (float) tailleText / 3));

    TransformGroup tgTrans1 = new TransformGroup(tg1);
    tgTrans1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    tgTrans1.addChild(tgRotX2);

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
    tgRotX.addChild(tgTrans1);

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

  /**
   * Permet de créer l'apparence en fonction de paramètres Dans le cadre d'un
   * ponctuel, certains paramètres n'ont aucun sens
   * 
   * @param isColored
   * @param color
   * @param coefOpacity
   * @param isRepresentationSolid
   * @return l'apparence à appliquer
   */
  private static Appearance genereApparenceText(Color3f color,
      double coefOpacity) {

    // Création de l'apparence
    Appearance apparenceFinale = new Appearance();

    // Autorisations pour l'apparence
    apparenceFinale.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_READ);
    apparenceFinale.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);

    // Autorisations pour le material
    apparenceFinale.setCapability(Appearance.ALLOW_MATERIAL_READ);
    apparenceFinale.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
    apparenceFinale.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_READ);
    apparenceFinale.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);

    // Création du material (gestion des couleurs et de l'affichage)
    Material material = new Material();

    material.setAmbientColor(0.2f, 0.2f, 0.2f);
    material.setDiffuseColor(color);
    material.setSpecularColor(color);
    material.setShininess(128);

    // et de material
    apparenceFinale.setMaterial(material);

    if (coefOpacity != 1) {

      TransparencyAttributes t_attr =

      new TransparencyAttributes(TransparencyAttributes.BLENDED,
          (float) coefOpacity,

          TransparencyAttributes.BLEND_SRC_ALPHA,

          TransparencyAttributes.BLENDED);

      // et de transparence
      apparenceFinale.setTransparencyAttributes(t_attr);
    }

    return apparenceFinale;

  }

}
