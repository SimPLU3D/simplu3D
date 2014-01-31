package fr.ign.cogit.appli.xdogs.theseMickael.indicators.oneBuildingPart.dim3;

import fr.ign.cogit.appli.xdogs.theseMickael.indicators.indicatorSchem.oneBuildingPart.SingleBIndicator;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.sig3d.calculation.Util;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.sig3d.geometry.topology.Triangle;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.sig3d.convert.decomposition.Building;

public class Volume extends SingleBIndicator {

  private double value;

  public Volume(Building bp) {
    super(bp);

    Box3D b = new Box3D(bp.getGeom());

    double zMin = b.getLLDP().getZ();

    value = 0;

    for (Triangle t : bp.getlTriToit()) {

      double volumeUnderT = Math.abs(Util.volumeUnderTriangle(t));

      if (volumeUnderT < 0) {
        System.out.println("????");
      }

      IPolygon p = new GM_Polygon(t);
      double volumeUnderTBat = p.area() * (zMin);

      double contrib = volumeUnderT - volumeUnderTBat;

      if (contrib < -0.00001) {
        System.out.println("contrib <0 ????          " + contrib);
      }

      value = contrib + value;

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
    return "Volume";
  }

  @Override
  public String getAggregationType() {
    // TODO Auto-generated method stub
    return SingleBIndicator.AGG_SUM;
  }

}
