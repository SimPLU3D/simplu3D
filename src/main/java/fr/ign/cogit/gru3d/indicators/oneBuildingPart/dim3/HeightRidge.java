package fr.ign.cogit.gru3d.indicators.oneBuildingPart.dim3;

import fr.ign.cogit.appli.xdogs.theseMickael.indicators.indicatorSchem.oneBuildingPart.SingleBIndicator;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.sig3d.convert.decomposition.Building;

public class HeightRidge extends  SingleBIndicator {

  private double value = 0;

  public HeightRidge(Building bP) {

    super(bP);

    Box3D b3D = new Box3D(new GM_MultiSurface<IOrientableSurface>(bP.getlTriWall()));

    Box3D b3D2 = new Box3D(new GM_MultiSurface<IOrientableSurface>(bP.getlTriToit()));
    value = b3D2.getLLDP().getZ() - b3D.getLLDP().getZ();

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
    return "HRidge";
  }

  @Override
  public String getAggregationType() {
    // TODO Auto-generated method stub
    return SingleBIndicator.AGG_MAX;
  }


}
