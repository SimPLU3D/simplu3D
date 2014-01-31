package fr.ign.cogit.appli.xdogs.theseMickael.indicators.oneBuildingPart.dim2;

import fr.ign.cogit.appli.xdogs.theseMickael.indicators.indicatorSchem.oneBuildingPart.SingleBIndicator;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.sig3d.geometry.topology.Triangle;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.sig3d.convert.decomposition.Building;

public class Area2D extends SingleBIndicator {

  private double value;

  public Area2D(Building bp) {
    super(bp);

    value = 0;

    for (Triangle t : bp.getlTriToit()) {

      IPolygon p = new GM_Polygon(t);

      value = p.area() + value;

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
    return "Area2D";
  }

  @Override
  public String getAggregationType() {
    // TODO Auto-generated method stub
    return SingleBIndicator.AGG_SUM;
  }

}
