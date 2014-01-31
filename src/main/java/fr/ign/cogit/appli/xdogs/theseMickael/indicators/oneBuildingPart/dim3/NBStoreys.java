package fr.ign.cogit.appli.xdogs.theseMickael.indicators.oneBuildingPart.dim3;

import fr.ign.cogit.appli.xdogs.theseMickael.indicators.indicatorSchem.oneBuildingPart.SingleBIndicator;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.sig3d.convert.decomposition.Building;

public class NBStoreys extends SingleBIndicator {
  
  public double FLOORHEIGHT = 3;
  
  private int nbFloor;
  
  public NBStoreys(Building bp){
    super(bp);
    Box3D b = new Box3D(bp.getGeom());
    
    
    double zMin = b.getLLDP().getZ();
    double zMax = b.getURDP().getZ();

     nbFloor = (int) ((zMax - zMin) / FLOORHEIGHT) + 1;
  }
  
  
  
  
  public  String getType(){
    // TODO Auto-generated method stub
    return SingleBIndicator.NAME_NUMERIC;
  }


  public String getAttributeName() {
    // TODO Auto-generated method stub
    return "NB_Storeys";
  }
  



  @Override
  public String getAggregationType() {
    // TODO Auto-generated method stub
    return SingleBIndicator.AGG_SUM;
  }

  @Override
  public Object getValue() {
    // TODO Auto-generated method stub
    return nbFloor;
  }
  

}
