package fr.ign.cogit.simplu3d.rjmcmc.generic.optimizer;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;

public abstract class DefaultSimPLU3DOptimizer<C extends ISimPLU3DPrimitive> implements ISimPLU3DOptimizer<ISimPLU3DPrimitive> {

	protected double coeffDec = Double.NaN;
	protected double deltaConf = Double.NaN;

	protected double minLengthBox = Double.NaN;
	protected double maxLengthBox = Double.NaN;
	protected double minWidthBox = Double.NaN;
	protected double maxWidthBox = Double.NaN;

	protected double energyCreation = Double.NaN;
	protected IGeometry samplingSurface = null;

	public void setSamplingSurface(IGeometry geom) {
		samplingSurface = geom;
	}

	public void setEnergyCreation(double energyCreation) {
		this.energyCreation = energyCreation;
	}

	public void setMinLengthBox(double minLengthBox) {
		this.minLengthBox = minLengthBox;
	}

	public void setMaxLengthBox(double maxLengthBox) {
		this.maxLengthBox = maxLengthBox;
	}

	public void setMinWidthBox(double minWidthBox) {
		this.minWidthBox = minWidthBox;
	}

	public void setMaxWidthBox(double maxWidthBox) {
		this.maxWidthBox = maxWidthBox;
	}

	public void setCoeffDec(double coeffDec) {
		this.coeffDec = coeffDec;
	}

	public void setDeltaConf(double deltaConf) {
		this.deltaConf = deltaConf;
	}

}
