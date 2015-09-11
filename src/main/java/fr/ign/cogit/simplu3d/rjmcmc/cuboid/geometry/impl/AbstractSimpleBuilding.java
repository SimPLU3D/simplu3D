package fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.geometry.Rectangle2D;
import fr.ign.rjmcmc.kernel.SimpleObject;

/**
 * 
 *        This software is released under the licence CeCILL
 * 
 *        see LICENSE.TXT
 * 
 *        see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public abstract class AbstractSimpleBuilding extends Building implements SimpleObject {
	
	public double centerx;
	public double centery;
	public double length;
	public double width;
	public double orientation = 0;
	public double height;
	
	
  public AbstractSimpleBuilding() {
    super();
  }
  public AbstractSimpleBuilding(IGeometry geom) {
    super(geom);
  }
  
  
  public abstract Polygon toGeometry();
  
  public abstract boolean prospectJTS(Geometry geom, double slope, double hIni);
  
  public abstract IGeometry generated3DGeom();
  
  public abstract 	Rectangle2D getRectangle2D();
  
}
