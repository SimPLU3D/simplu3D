package fr.ign.cogit.appli.xdogs.theseMickael.indicators.oneBuildingPart.dim3;

import java.util.List;

import fr.ign.cogit.appli.xdogs.theseMickael.indicators.indicatorSchem.oneBuildingPart.SingleBIndicator;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ITriangle;
import fr.ign.cogit.sig3d.convert.decomposition.Building;

public class WallArea extends SingleBIndicator {

  private double value = 0;

  public WallArea(Building bp) {
    super(bp);

    List<ITriangle> lTri = bp.getlTriWall();

    for (ITriangle tri : lTri) {
      value = value + tri.area();
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
    return "WallArea";
  }

  @Override
  public String getAggregationType() {
    // TODO Auto-generated method stub
    return SingleBIndicator.AGG_SUM;
  }

}
