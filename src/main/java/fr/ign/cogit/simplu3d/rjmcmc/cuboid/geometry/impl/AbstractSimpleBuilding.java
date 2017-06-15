package fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;
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
public abstract class AbstractSimpleBuilding extends Building implements SimpleObject, ISimPLU3DPrimitive {
	
	public double centerx;
	public double centery;
	public double length;
	public double width;
	public double orientation = 0;
	public double height;
	
	
  public AbstractSimpleBuilding() {
    super();
  }

  
  
  public abstract Polygon toGeometry();
  
  public abstract boolean prospectJTS(Geometry geom, double slope, double hIni);
  
  public abstract IGeometry generated3DGeom();
  
  
  public abstract void setCoordinates(double[] val1);

}
