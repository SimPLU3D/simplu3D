package fr.ign.cogit.simplu3d.indicator;

import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ITriangle;
import fr.ign.cogit.geoxygene.sig3d.calculation.Calculation3D;
import fr.ign.cogit.geoxygene.sig3d.calculation.Util;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromPolygonToTriangle;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Solid;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;

public class Compacity {

  private double value = 0;

  public Compacity(AbstractBuilding bP) {

    this(FromPolygonToTriangle.convertAndTriangle(bP.getLod2MultiSurface()
        .getList()));

  }

  public Compacity(List<ITriangle> lTri) {
    double volConvex = Util.volumeTriangulatedSolid(Calculation3D
        .convexHull(new GM_Solid(lTri)));
    double volume = (new Volume(lTri)).getValue();

    value = volume / volConvex;

    if (value > 1) {
      System.out.println("Compacity : " + value);
    }

    value = Math.min(1.0, value);
  }

  public String getAttributeName() {
    // TODO Auto-generated method stub
    return "Compacité";
  }

}
