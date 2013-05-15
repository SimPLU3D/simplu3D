package fr.ign.cogit.simplu3d.indicator;

import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ITriangle;
import fr.ign.cogit.sig3d.convert.geom.FromPolygonToTriangle;
import fr.ign.cogit.simplu3d.model.application.Batiment;

public class GeometryComplexity {

  private int value = 0;

  public GeometryComplexity(Batiment bati){

    this(FromPolygonToTriangle.convertAndTriangle(bati.getLod2MultiSurface()
        .getList()));
    
    
    
  }

  public GeometryComplexity(List<ITriangle> lTri) {
    value = lTri.size();
  }


  public Integer getValue() {
    // TODO Auto-generated method stub
    return value;
  }


}
