package fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.simplu3d.model.application.Building;
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
  public AbstractSimpleBuilding() {
    super();
  }
  public AbstractSimpleBuilding(IGeometry geom) {
    super(geom);
  }
}
