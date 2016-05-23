package fr.ign.cogit.simplu3d.rjmcmc.generic.optimizer;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;

public interface ISimPLU3DOptimizer<C extends ISimPLU3DPrimitive> {

	public void setSamplingSurface(IGeometry geom);

	public void setEnergyCreation(double energyCreation);

	public void setMinLengthBox(double minLengthBox);

	public void setMaxLengthBox(double maxLengthBox);

	public void setMinWidthBox(double minWidthBox);

	public void setMaxWidthBox(double maxWidthBox);

	public void setCoeffDec(double coeffDec);

	public void setDeltaConf(double deltaConf);

}