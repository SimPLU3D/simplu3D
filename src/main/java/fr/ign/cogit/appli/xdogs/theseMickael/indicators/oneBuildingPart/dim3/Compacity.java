package fr.ign.cogit.appli.xdogs.theseMickael.indicators.oneBuildingPart.dim3;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.appli.xdogs.theseMickael.indicators.indicatorSchem.oneBuildingPart.SingleBIndicator;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ITriangle;
import fr.ign.cogit.geoxygene.sig3d.calculation.Calculation3D;
import fr.ign.cogit.geoxygene.sig3d.calculation.Util;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Solid;
import fr.ign.cogit.sig3d.convert.decomposition.Building;

public class Compacity extends SingleBIndicator {

  private double value = 0;

  public Compacity(Building bP) {
    super(bP);

    List<ITriangle> triL = new ArrayList<ITriangle>();
    triL.addAll(bP.getlTriToit());
    triL.addAll(bP.getlTriWall());

    double volConvex = Util.volumeTriangulatedSolid(Calculation3D
        .convexHull(new GM_Solid(triL)));
    double volume = (new Volume(bP)).getValue();

    value = volume / volConvex;

    if (value > 1) {
      System.out.println("Compacity : " + value);
    }
    
    value = Math.min(1.0, value);

  }

  @Override
  public Object getValue() {
    // TODO Auto-generated method stub
    return value;
  }

  public String getType() {
    // TODO Auto-generated method stub
    return SingleBIndicator.NAME_NUMERIC;
  }

  public String getAttributeName() {
    // TODO Auto-generated method stub
    return "Compacité";
  }

  @Override
  public String getAggregationType() {
    // TODO Auto-generated method stub
    return SingleBIndicator.AGG_MOY;
  }

}
