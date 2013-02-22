package fr.ign.cogit.simplu3d.representation.theme;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.Appearance;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.LineStripArray;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TextureAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

import com.sun.j3d.utils.geometry.GeometryInfo;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.conversion.ConversionJava3DGeOxygene;
import fr.ign.cogit.geoxygene.sig3d.representation.ConstantRepresentation;
import fr.ign.cogit.geoxygene.sig3d.representation.Default3DRep;
import fr.ign.cogit.geoxygene.sig3d.representation.texture.TextureManager;
import fr.ign.cogit.geoxygene.sig3d.representation.texture.Util;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.spatial.geomcomp.GM_CompositeCurve;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_OrientableCurve;
import fr.ign.cogit.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.simplu3d.model.application.Batiment;
import fr.ign.cogit.simplu3d.model.application.Facade;
import fr.ign.cogit.simplu3d.model.application.Materiau;
import fr.ign.cogit.simplu3d.model.application.Toit;

public class RepresentationBatiment extends Default3DRep {

  boolean representFaitage;
  boolean representGouttiere;

  public RepresentationBatiment(Batiment b) {
    this(b, false, false);
  }

  public boolean isRepresentFaitage() {
    return representFaitage;
  }

  public boolean isRepresentGouttiere() {
    return representGouttiere;
  }

  public RepresentationBatiment(Batiment b, boolean representFaitage,
      boolean representGouttiere) {
    super();
    this.feat = b;
    this.representFaitage = representFaitage;
    this.representGouttiere = representGouttiere;
    Toit t = b.getToit();
    List<Facade> f = b.getFacade();

    // /1 on s'occupe du toit
    GeometryInfo geometryInfoToit = null;
    Appearance appToit = null;

    Materiau matToit = t.getMat();

    if (matToit != null) {
      geometryInfoToit = Util.geometryWithTexture(t.getGeom(),
          matToit.getTextL(), matToit.getTextH());
      appToit = generateAppearance(matToit.getTextRep());

    } else {
      geometryInfoToit = ConversionJava3DGeOxygene
          .fromOrientableSToTriangleArray(FromGeomToSurface.convertGeom(t
              .getGeom()));
      appToit = generateAppearanceNoTex(Color.red);

    }

    Shape3D sToit = new Shape3D(geometryInfoToit.getGeometryArray(), appToit);

    sToit.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
    sToit.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
    sToit.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
    sToit.setCapability(Node.ALLOW_LOCAL_TO_VWORLD_READ);
    sToit.setCapability(Node.ALLOW_LOCALE_READ);

    this.bGRep.addChild(sToit);

    // On s'occupe des façades

    for (Facade facade : f) {

      GeometryInfo geometryInfoF = null;
      Appearance appF = null;
      Materiau mat = facade.getMat();

      if (mat != null) {
        geometryInfoF = Util.geometryWithTexture(facade.getGeom(),
            mat.getTextL(), mat.getTextH());
        appF = generateAppearance(mat.getTextRep());

      } else {
        geometryInfoF = ConversionJava3DGeOxygene
            .fromOrientableSToTriangleArray(FromGeomToSurface
                .convertGeom(facade.getGeom()));
        appF = generateAppearanceNoTex(Color.LIGHT_GRAY);

      }

      Shape3D sFacade = new Shape3D(geometryInfoF.getGeometryArray(), appF);

      sFacade.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
      sFacade.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
      sFacade.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
      sFacade.setCapability(Node.ALLOW_LOCAL_TO_VWORLD_READ);
      sFacade.setCapability(Node.ALLOW_LOCALE_READ);

      this.bGRep.addChild(sFacade);

    }

    // On s'occupe de la gouttière
    if (representGouttiere) {
      Shape3D gut3D = new Shape3D(
          geometryWithColor(Color.blue, t.getGouttiere()), AppearanceLineApp());

      gut3D.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
      gut3D.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
      gut3D.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
      gut3D.setCapability(Node.ALLOW_LOCAL_TO_VWORLD_READ);
      gut3D.setCapability(Node.ALLOW_LOCALE_READ);

      this.bGRep.addChild(gut3D);

    }

    // On s'occupe du faitage
    if (representFaitage) {
      Shape3D gut3D = new Shape3D(
          geometryWithColor(Color.green, t.getFaitage()), AppearanceLineApp());

      gut3D.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
      gut3D.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
      gut3D.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
      gut3D.setCapability(Node.ALLOW_LOCAL_TO_VWORLD_READ);
      gut3D.setCapability(Node.ALLOW_LOCALE_READ);

      this.bGRep.addChild(gut3D);

    }

    // Optimisation
    this.bGRep.compile();
  }
  
  
  private Appearance AppearanceLineApp(){
    
    // Création de l'apparence
    Appearance apparenceFinale = new Appearance();

    // Autorisations pour l'apparence
    apparenceFinale.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_READ);
    apparenceFinale.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);

    // Autorisations pour le material
    apparenceFinale.setCapability(Appearance.ALLOW_MATERIAL_READ);
    apparenceFinale.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
    
    
    LineAttributes lp = new LineAttributes();

    lp.setLineAntialiasingEnable(true);
    lp.setLineWidth(5f);

      lp.setLinePattern(LineAttributes.PATTERN_SOLID);


  

    apparenceFinale.setLineAttributes(lp);
    
    return apparenceFinale;
  }

  /**
   * Génère l'apparence à appliquer à la géométrie
   * 
   * @param isClrd
   * @param color
   * @param coefTransp
   * @param isSolid
   * @return
   */
  private Appearance generateAppearanceNoTex(Color c) {

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

    // Indique que l'on est en mode surfacique
    pa.setPolygonMode(PolygonAttributes.POLYGON_FILL);

    // Indique que l'on n'affiche pas les faces cachées
    if (ConstantRepresentation.cullMode) {
      pa.setCullFace(PolygonAttributes.CULL_BACK);

    }

    pa.setBackFaceNormalFlip(false);

    // Association à l'apparence des attributs de géométrie et de material
    apparenceFinale.setPolygonAttributes(pa);

    Color3f couleur3F = new Color3f(c);
    // Création du material (gestion des couleurs et de l'affichage)
    Material material = new Material();

    material.setDiffuseColor(couleur3F);
    material.setSpecularColor(new Color3f(c.brighter()));
    material.setAmbientColor(new Color3f(c.darker()));
    material.setEmissiveColor(new Color3f(c.darker()));
    material.setShininess(128);

    apparenceFinale.setMaterial(material);

    return apparenceFinale;

  }

  /**
   * Génère l'apparence de l'objet pour la texture path
   * 
   * @return
   */
  private Appearance generateAppearance(String path) {

    // Création de l'apparence
    Appearance apparenceFinale = new Appearance();

    // Autorisations pour l'apparence
    apparenceFinale.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_READ);
    apparenceFinale.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);

    // Autorisations pour le material

    apparenceFinale.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_READ);
    apparenceFinale.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);

    apparenceFinale.setCapability(Appearance.ALLOW_TEXTURE_ATTRIBUTES_READ);
    apparenceFinale.setCapability(Appearance.ALLOW_TEXTURE_ATTRIBUTES_WRITE);

    apparenceFinale.setCapability(Appearance.ALLOW_TEXTURE_WRITE);

    apparenceFinale.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);

    apparenceFinale.setTexture(TextureManager.textureLoading(path));
    apparenceFinale.setTextureAttributes(new TextureAttributes());

    // Création des attributs du polygone
    PolygonAttributes pa = new PolygonAttributes();

    pa.setCullFace(PolygonAttributes.CULL_NONE);
    pa.setCapability(PolygonAttributes.ALLOW_CULL_FACE_WRITE);

    pa.setBackFaceNormalFlip(false);

    // Association à l'apparence des attributs de géométrie et de material
    apparenceFinale.setPolygonAttributes(pa);

    return apparenceFinale;

  }

  /**
   * Génère une géométrie Java3D à partir d'une couleur indiquée
   * 
   * @return
   */
  private LineStripArray geometryWithColor(Color color, IGeometry objgeom) {
    // On créer un tableau contenant les lignes à représenter
    Color3f couleur3F = new Color3f(color);

    ArrayList<IGeometry> lCurves = new ArrayList<IGeometry>();

    if (objgeom instanceof GM_OrientableCurve) {

      GM_OrientableCurve curve = (GM_OrientableCurve) objgeom;

      lCurves.add(curve);

    } else if (objgeom instanceof GM_MultiCurve<?>) {
      GM_MultiCurve<?> multiCurve = (GM_MultiCurve<?>) objgeom;
      lCurves.addAll(multiCurve.getList());

    } else if (objgeom instanceof GM_CompositeCurve) {
      GM_CompositeCurve multiCurve = (GM_CompositeCurve) objgeom;
      lCurves.addAll(multiCurve.getGenerator());

    } else {

      return null;
    }

    // Effectue la conversion de la géométrie

    // on compte le nombre de points
    int nPoints = 0;
    int nbLignes = lCurves.size();

    for (int i = 0; i < nbLignes; i++) {
      nPoints = nPoints + lCurves.get(i).coord().size();
    }

    // Problème de ligne vide
    if (nPoints < 2) {
      return null;
    }

    /*
     * Tableau permettant de définir le nombre de lignes représentées (cas des
     * multi-lignes Ici il y en a une contenant tous les points
     */
    int[] stripVertexCount = new int[nbLignes];

    // On indique de combien de points sera formé chaque fragment de lignes
    for (int i = 0; i < nbLignes; i++) {
      stripVertexCount[i] = lCurves.get(i).coord().size();

    }

    // On prépare la géométrie et ses autorisations
    LineStripArray geom = new LineStripArray(nPoints, GeometryArray.COORDINATES
        | GeometryArray.COLOR_3, stripVertexCount);

    geom.setCapability(GeometryArray.ALLOW_COLOR_READ);
    geom.setCapability(GeometryArray.ALLOW_COLOR_WRITE);
    geom.setCapability(Geometry.ALLOW_INTERSECT);

    // compteurs pour remplir le tableau de points
    int elementajoute = 0;

    for (int i = 0; i < nbLignes; i++) {
      // On récupère les points de chaque ligne
      IDirectPositionList lPoints = lCurves.get(i).coord();
      int nPointsTemp = lPoints.size();
      for (int j = 0; j < nPointsTemp; j++) {
        IDirectPosition dp = lPoints.get(j);
        Point3d point = new Point3d((float) dp.getX(), (float) dp.getY(),
            (float) dp.getZ());
        geom.setCoordinate(elementajoute, point);
        geom.setColor(elementajoute, couleur3F);

        elementajoute++;
      }
    }
    return geom;

  }
}
