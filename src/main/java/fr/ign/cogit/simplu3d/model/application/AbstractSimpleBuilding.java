package fr.ign.cogit.simplu3d.model.application;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.rjmcmc.kernel.SimpleObject;

public abstract class AbstractSimpleBuilding extends Building implements SimpleObject {
  public AbstractSimpleBuilding() {
    super();
  }
  public AbstractSimpleBuilding(IGeometry geom) {
    super(geom);
  }
}
