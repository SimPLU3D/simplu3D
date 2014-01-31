package fr.ign.cogit.gru3d.indicators.oneBuildingPart.dim3;

import fr.ign.cogit.appli.xdogs.theseMickael.indicators.indicatorSchem.oneBuildingPart.SingleBIndicator;
import fr.ign.cogit.sig3d.convert.decomposition.Building;

public class RoofCoeff extends SingleBIndicator {

  private double value = 0;

  public RoofCoeff(Building bp) {
    super(bp);

    HeightMax hm = new HeightMax(bp);
    HeightRidge hr = new HeightRidge(bp);

    value = hm.getValue() / hr.getValue() - 1;

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
    return "RoofCoeff";
  }

  @Override
  public String getAggregationType() {
    // TODO Auto-generated method stub
    return SingleBIndicator.AGG_SUM;
  }
}
