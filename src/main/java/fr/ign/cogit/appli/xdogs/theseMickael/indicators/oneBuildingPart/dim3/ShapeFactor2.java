package fr.ign.cogit.appli.xdogs.theseMickael.indicators.oneBuildingPart.dim3;

import fr.ign.cogit.appli.xdogs.theseMickael.indicators.indicatorSchem.oneBuildingPart.SingleBIndicator;
import fr.ign.cogit.appli.xdogs.theseMickael.indicators.oneBuildingPart.dim2.Area2D;
import fr.ign.cogit.sig3d.convert.decomposition.Building;

public class ShapeFactor2 extends SingleBIndicator {

  private double value;

  public ShapeFactor2(Building bP) {
    super(bP);

    HeightMax hM = new HeightMax(bP);
    
    
    Area2D a = new Area2D(bP);
    
    
    
    
    value = Math.pow(hM.getValue(), 2) / a.getValue();

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
    return "IndForme2";
  }

  @Override
  public String getAggregationType() {
    // TODO Auto-generated method stub
    return SingleBIndicator.AGG_MAX;
  }


}
