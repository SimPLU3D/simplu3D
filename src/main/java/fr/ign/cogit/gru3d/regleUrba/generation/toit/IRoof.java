package fr.ign.cogit.gru3d.regleUrba.generation.toit;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;

public interface IRoof {

  
  public String getType();
  public IMultiSurface<IPolygon> getRoof();
  public IMultiSurface<IPolygon> generateWall(double zMin);
  public IMultiSurface<IPolygon> generateBuilding(double zMin);
}
