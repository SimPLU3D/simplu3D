package fr.ign.cogit.appli.xdogs.theseMickael.indicators.oneBuildingPart.dim3;

import fr.ign.cogit.appli.xdogs.theseMickael.indicators.indicatorSchem.oneBuildingPart.SingleBIndicator;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.sig3d.convert.decomposition.Building;

public class HeightMax extends SingleBIndicator {

  private double value = 0;

  public HeightMax(Building bP) {

    super(bP);

    Box3D b3D = new Box3D(bP.getGeom());

    value = b3D.getURDP().getZ() - b3D.getLLDP().getZ();

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
    return "HMax";
  }

  @Override
  public String getAggregationType() {
    // TODO Auto-generated method stub
    return SingleBIndicator.AGG_MAX;
  }

}
