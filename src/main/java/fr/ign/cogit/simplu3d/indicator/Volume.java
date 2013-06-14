package fr.ign.cogit.simplu3d.indicator;

import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ITriangle;
import fr.ign.cogit.geoxygene.sig3d.calculation.Util;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.sig3d.convert.geom.FromPolygonToTriangle;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;

public class Volume {

  private double value;

  public Volume(AbstractBuilding _AbstractBuilding) {

    this(FromPolygonToTriangle.convertAndTriangle(_AbstractBuilding.getToit()
        .getLod2MultiSurface().getList()));

  }

  public Volume(List<ITriangle> lTriToit) {

    Box3D b = new Box3D(new GM_MultiSurface<>(lTriToit));

    double zMin = b.getLLDP().getZ();

    value = 0;

    for (ITriangle t : lTriToit) {

      double volumeUnderT = Math.abs(Util.volumeUnderTriangle(t));

      if (volumeUnderT < 0) {
        System.out.println("Volume : Bad triangle orientation");
      }

      IPolygon p = new GM_Polygon(t.getExterior());
      double volumeUnderTBat = p.area() * (zMin);

      double contrib = volumeUnderT - volumeUnderTBat;

      if (contrib < -0.00001) {
        System.out.println("Volume : contrib <0 ????          " + contrib);
      }

      value = contrib + value;

    }

  }

  public Double getValue() {
    // TODO Auto-generated method stub
    return value;
  }

}
