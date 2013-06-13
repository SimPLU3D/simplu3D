package fr.ign.cogit.simplu3d.indicator;

import fr.ign.cogit.geoxygene.sig3d.calculation.Calculation3D;
import fr.ign.cogit.sig3d.convert.geom.FromPolygonToTriangle;
import fr.ign.cogit.simplu3d.model.application._AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.WallSurface;

public class FacadeArea {

  private double value = 0;

  public FacadeArea(_AbstractBuilding b) {

    for (WallSurface f : b.getFacade()) {

      FacadeArea fA = new FacadeArea(f);
      value = value + fA.getValue();

    }

  }

  public FacadeArea(WallSurface f) {

    value = Calculation3D.area(FromPolygonToTriangle.convertAndTriangle(f
        .getLod2MultiSurface().getList()));

  }

  public double getValue() {
    return value;
  }

}
