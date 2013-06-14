package fr.ign.cogit.simplu3d.indicator;

import fr.ign.cogit.geoxygene.sig3d.calculation.Calculation3D;
import fr.ign.cogit.sig3d.convert.geom.FromPolygonToTriangle;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.SpecificWallSurface;

public class FacadeArea {

  private double value = 0;

  public FacadeArea(AbstractBuilding b) {

    for (SpecificWallSurface f : b.getFacade()) {

      FacadeArea fA = new FacadeArea(f);
      value = value + fA.getValue();

    }

  }

  public FacadeArea(SpecificWallSurface f) {

    value = Calculation3D.area(FromPolygonToTriangle.convertAndTriangle(f
        .getLod2MultiSurface().getList()));

  }

  public double getValue() {
    return value;
  }

}
