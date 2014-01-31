package fr.ign.cogit.appli.xdogs.theseMickael.indicators.oneBuildingPart.dim3;

import fr.ign.cogit.appli.xdogs.theseMickael.indicators.indicatorSchem.oneBuildingPart.SingleBIndicator;
import fr.ign.cogit.sig3d.convert.decomposition.Building;

public class GeometryComplexity  extends SingleBIndicator {

  private int value = 0 ;
  
  
  public GeometryComplexity(Building bp){
    super(bp);
    
    value = value + bp.getlTriToit().size();
    value = value + bp.getlTriWall().size();
    
    
  }
  
  
  @Override
  public Integer getValue() {
    // TODO Auto-generated method stub
    return value;
  }

  @Override
  public String getType() {
    // TODO Auto-generated method stub
    return SingleBIndicator.NAME_NUMERIC;
  }

  @Override
  public String getAttributeName() {
    // TODO Auto-generated method stub
    return "Complexity";
  }

  @Override
  public String getAggregationType() {
    // TODO Auto-generated method stub
    return SingleBIndicator.AGG_SUM;
  }

}
