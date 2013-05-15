package fr.ign.cogit.simplu3d.indicator;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.sig3d.calculation.OrientedBoundingBox;
import fr.ign.cogit.simplu3d.model.application.Batiment;


public class ShapeFactor {

  private double value;

  /**
   *  Hauteur bâtiment / largeur + longueur
   * @param bP
   */
  public ShapeFactor(Batiment bP) {


    OrientedBoundingBox oBB = new OrientedBoundingBox(bP.getGeom());

    IPolygon poly = oBB.getPoly();

    value = 0;

    if (poly != null) {
      double h = oBB.getzMax() - oBB.getzMin();

      IDirectPositionList dpl = poly.coord();
      IDirectPosition dp1 = dpl.get(0);
      IDirectPosition dp2 = dpl.get(1);
      IDirectPosition dp3 = dpl.get(2);

      double d = dp1.distance2D(dp2);
      double w = dp2.distance(dp3);

      value = h * 2 / (w + d);

    }

  }

  public Double getValue() {
    // TODO Auto-generated method stub
    return value;
  }



}
