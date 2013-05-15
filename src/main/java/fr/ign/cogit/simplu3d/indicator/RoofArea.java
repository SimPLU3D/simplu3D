package fr.ign.cogit.simplu3d.indicator;

import fr.ign.cogit.geoxygene.sig3d.calculation.Calculation3D;
import fr.ign.cogit.sig3d.convert.geom.FromPolygonToTriangle;
import fr.ign.cogit.simplu3d.model.application.Toit;

public class RoofArea {

  private double value = 0;

  public RoofArea(Toit t) {

    value = Calculation3D.area(FromPolygonToTriangle.convertAndTriangle(t
        .getLod2MultiSurface().getList()));

  }

  public Double getValue() {
    // TODO Auto-generated method stub
    return value;
  }

}
