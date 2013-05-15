package fr.ign.cogit.simplu3d.indicator;

import fr.ign.cogit.geoxygene.sig3d.calculation.Calculation3D;
import fr.ign.cogit.sig3d.convert.geom.FromPolygonToTriangle;
import fr.ign.cogit.simplu3d.model.application.Batiment;
import fr.ign.cogit.simplu3d.model.application.Facade;

public class FacadeArea {

  private double value = 0;

  public FacadeArea(Batiment b) {

    for (Facade f : b.getFacade()) {

      FacadeArea fA = new FacadeArea(f);
      value = value + fA.getValue();

    }

  }

  public FacadeArea(Facade f) {

    value = Calculation3D.area(FromPolygonToTriangle.convertAndTriangle(f
        .getLod2MultiSurface().getList()));

  }

  public double getValue() {
    return value;
  }

}
