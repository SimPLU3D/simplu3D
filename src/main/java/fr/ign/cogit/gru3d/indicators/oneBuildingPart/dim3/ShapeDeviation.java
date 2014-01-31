package fr.ign.cogit.gru3d.indicators.oneBuildingPart.dim3;

import fr.ign.cogit.appli.xdogs.theseMickael.indicators.indicatorSchem.oneBuildingPart.SingleBIndicator;
import fr.ign.cogit.sig3d.calculation.OrientedBoundingBox;
import fr.ign.cogit.sig3d.convert.decomposition.Building;

public class ShapeDeviation extends SingleBIndicator {

  private double value;

  
  /**
   * Rapport entre le volume d'un objet de le volume de sa boite orientée
   * @param bP
   */
  public ShapeDeviation(Building bP) {

    super(bP);

    value = 1;

    OrientedBoundingBox oBB = new OrientedBoundingBox(bP.getGeom());

    if (oBB.getPoly() != null) {

      double zMin = oBB.getzMin();
      double zMax = oBB.getzMax();


      double volArea = oBB.getPoly().area() * (zMax - zMin);

      if (volArea == 0) {
        return;
      }

      double vBati = (new Volume(bP)).getValue();

      value = vBati / volArea;

      if (value > 1) {

        System.out.println("Why ?" + value);
      }

    }

  }

  @Override
  public Double getValue() {
    // TODO Auto-generated method stub
    return value;
  }

  public String getType() {
    // TODO Auto-generated method stub
    return SingleBIndicator.NAME_NUMERIC;
  }

  public String getAttributeName() {
    // TODO Auto-generated method stub
    return "Deviation";
  }

  @Override
  public String getAggregationType() {
    // TODO Auto-generated method stub
    return SingleBIndicator.AGG_MAX;
  }
}
