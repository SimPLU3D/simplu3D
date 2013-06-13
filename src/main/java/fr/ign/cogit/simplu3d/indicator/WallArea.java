package fr.ign.cogit.simplu3d.indicator;

import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ITriangle;
import fr.ign.cogit.geoxygene.sig3d.calculation.Calculation3D;
import fr.ign.cogit.sig3d.convert.geom.FromPolygonToTriangle;
import fr.ign.cogit.simplu3d.model.application._AbstractBuilding;
public class WallArea {

  private double value = 0;

  public WallArea(_AbstractBuilding bp) {


    this(FromPolygonToTriangle.convertAndTriangle(bp.getLod2MultiSurface()
        .getList()));
  }
  
  
  
  public WallArea(List<ITriangle> lTri){
    
    value = Calculation3D.area(lTri);
  }

  public Double getValue() {
    // TODO Auto-generated method stub
    return value;
  }

}
