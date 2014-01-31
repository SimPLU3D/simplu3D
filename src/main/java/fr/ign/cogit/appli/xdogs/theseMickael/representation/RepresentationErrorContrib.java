package fr.ign.cogit.appli.xdogs.theseMickael.representation;

import java.awt.Color;

import javax.media.j3d.BranchGroup;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion2DObject;
import fr.ign.cogit.geoxygene.sig3d.geometry.Cylinder;
import fr.ign.cogit.geoxygene.sig3d.representation.Default3DRep;
import fr.ign.cogit.geoxygene.sig3d.representation.basic.Object2d;
import fr.ign.cogit.geoxygene.sig3d.representation.basic.Object3d;
import fr.ign.cogit.geoxygene.sig3d.representation.sample.ObjectCartoon;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;

public class RepresentationErrorContrib extends Default3DRep {

  private String[] lAtt;
  private Color[] lColor;
  private String attAgg;
  private Color colorAgg;
  private double rayon;
  private double hauteur;

 /**
  * 
  * @param feat
  * @param lAtt
  * @param lColor
  * @param attAgg
  * @param colorAgg
  * @param rayon
  * @param hauteur
  * @param hauteurCylCentre
  */
  public RepresentationErrorContrib(IFeature feat, String[] lAtt,
      Color[] lColor, String attAgg, Color colorAgg, double rayon,
      double hauteur, double hauteurCylCentre) {
    super();
    this.feat = feat;
    this.lAtt = lAtt;
    this.lColor = lColor;
    this.attAgg = attAgg;
    this.colorAgg = colorAgg;
    this.rayon = rayon;
    this.hauteur = hauteur;

    int nbContrib = lAtt.length;

    for (int i = 0; i < nbContrib; i++) {

     this.getBGRep().addChild(processArcPart(i, nbContrib));

    }

    IDirectPosition centre = this.getFeature().getGeom().coord().get(0);
    double zAgg = hauteur * Double.parseDouble(feat.getAttribute(attAgg).toString());
    double zAggMin = Math.min(centre.getZ() + zAgg, centre.getZ());
    double zAggMAx = Math.max(centre.getZ() + zAgg, centre.getZ());

    IGeometry geom = Cylinder.generateCylinder(centre, zAggMin, zAggMAx, rayon);

    BranchGroup bg = null;

    if (geom.dimension() == 2) {
      
      bg = (new ObjectCartoon(new DefaultFeature(geom),Color.white,Color.black, 1, 0))
          .getBGRep();

    } else if (geom.dimension() == 3) {
      bg =(new ObjectCartoon(new DefaultFeature(geom),Color.white,Color.black, 1, 0))
      .getBGRep();

    }

    this.getBGRep().addChild(bg);

    IGeometry geom2 = Cylinder.generateCylinder2(centre, centre.getZ()
        - hauteurCylCentre, centre.getZ() + hauteurCylCentre, rayon + 2,100);

    if (geom2.dimension() == 2) {

   
      bg = (new Object2d(new DefaultFeature(geom2), true, Color.black, 1, true))
          .getBGRep();

    } else if (geom2.dimension() == 3) {
     
      bg = (new Object3d(new DefaultFeature(geom2), true, Color.black, 1, true))
          .getBGRep();

    }

 this.getBGRep().addChild(bg);

  }

  /**
   * 
   * @param i
   * @param nbContrib
   * @return
   */
  public BranchGroup processArcPart(int i, int nbContrib) {

    IDirectPosition centre = this.getFeature().getGeom().coord().get(0);

    double xcentre = centre.getX();
    double ycentre = centre.getY();
    
    
    IDirectPositionList dpl = new DirectPositionList();
    dpl.add(centre);

    
    
    for(int j=0;j<=100;j++){
      
      
      double xTemp = xcentre + this.getRayon()
      * Math.cos(2 * Math.PI * ( i + j / 100.0) / nbContrib);
      double yTemp = ycentre + this.getRayon()
      * Math.sin(2 * Math.PI * ( i + j / 100.0) / nbContrib);

      IDirectPosition dpTemp = new DirectPosition(xTemp, yTemp);
      
      dpl.add(dpTemp);
      
      
      
    }
    
    
    
    
    dpl.add(centre);
    
    IPolygon poly = new GM_Polygon(new GM_LineString(dpl));

    
    /*
   
    
    double xStart = xcentre + this.getRayon()
        * Math.cos(2 * Math.PI * i / nbContrib);
    double yStart = ycentre + this.getRayon()
        * Math.sin(2 * Math.PI * i / nbContrib);

    IDirectPosition startPoint = new DirectPosition(xStart, yStart);

    double xEnd = xcentre + this.getRayon()
        * Math.cos(2 * Math.PI * (i + 1) / nbContrib);
    double yEnd = ycentre + this.getRayon()
        * Math.sin(2 * Math.PI * (i + 1) / nbContrib);

    IDirectPosition endPoint = new DirectPosition(xEnd, yEnd);
    
    double xMid = xcentre + this.getRayon()
        * Math.cos(2 * Math.PI * (i + 0.5) / nbContrib);
    double yMid = ycentre + this.getRayon()
        * Math.sin(2 * Math.PI * (i + 0.5) / nbContrib);

    IDirectPosition midPoint = new DirectPosition(xMid, yMid);

    IArc arc = new GM_Arc(new GM_Position(startPoint),
        new GM_Position(midPoint), new GM_Position(endPoint));

    ILineString iLS = arc.asLineString(0, 0,0.1);

    iLS.coord().add(0, centre);
    iLS.coord().add(centre);

    IPolygon poly = new GM_Polygon(iLS);
    
    */

    double valShit = this.getHauteur()
        * Double.parseDouble(this.getFeature().getAttribute(this.getlAtt()[i])
            .toString());

    double zMin = Math.min(valShit + centre.getZ(), centre.getZ());

    double zMax = Math.max(valShit + centre.getZ(), centre.getZ());

    IGeometry geom = Extrusion2DObject.convertFromGeometry(poly, zMin, zMax);

    BranchGroup bg = null;

    if (geom.dimension() == 2) {

      bg = (new Object2d(new DefaultFeature(geom), this.getlColor()[i]))
          .getBGRep();

    } else if (geom.dimension() == 3) {
      bg = (new Object3d(new DefaultFeature(geom), this.getlColor()[i]))
          .getBGRep();

    }

    return bg;
  }

  public String[] getlAtt() {
    return lAtt;
  }

  public void setlAtt(String[] lAtt) {
    this.lAtt = lAtt;
  }

  public Color[] getlColor() {
    return lColor;
  }

  public void setlColor(Color[] lColor) {
    this.lColor = lColor;
  }

  public String getAttAgg() {
    return attAgg;
  }

  public void setAttAgg(String attAgg) {
    this.attAgg = attAgg;
  }

  public Color getColorAgg() {
    return colorAgg;
  }

  public void setColorAgg(Color colorAgg) {
    this.colorAgg = colorAgg;
  }

  public double getRayon() {
    return rayon;
  }

  public void setRayon(double rayon) {
    this.rayon = rayon;
  }

  public double getHauteur() {
    return hauteur;
  }

  public void setHauteur(double hauteur) {
    this.hauteur = hauteur;
  }

}
