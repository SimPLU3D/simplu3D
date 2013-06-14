package fr.ign.cogit.simplu3d.model.application;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.sig3d.model.citygml.transportation.CG_Road;

public class Road extends CG_Road {

  private IMultiCurve<ILineString> axis;
  private String name;
  private double width;

  public Road() {
    super();
  }

  public Road(org.citygml4j.model.citygml.transportation.Road tO) {
    super(tO);

    // TODO Auto-generated constructor stub
  }

  public IMultiCurve<ILineString> getAxis() {
    return axis;
  }

  public void setAxe(IMultiCurve<ILineString> axis) {
    this.axis = axis;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getWidth() {
    return width;
  }

  public void setWidth(double width) {
    this.width = width;
  }

}
