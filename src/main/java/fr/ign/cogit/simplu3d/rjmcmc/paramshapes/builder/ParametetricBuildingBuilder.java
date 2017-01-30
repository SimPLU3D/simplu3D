package fr.ign.cogit.simplu3d.rjmcmc.paramshapes.builder;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.rjmcmc.paramshapes.factory.footprint.IWallFactory;
import fr.ign.cogit.simplu3d.rjmcmc.paramshapes.factory.roof.IRoofFactory;
import fr.ign.cogit.simplu3d.rjmcmc.paramshapes.impl.ParametricBuilding;
import fr.ign.mpp.kernel.ObjectBuilder;

public class ParametetricBuildingBuilder implements ObjectBuilder<ParametricBuilding> {

	private IRoofFactory roofFactory;
	private IWallFactory wallFactory;

	private int size;

	public ParametetricBuildingBuilder(IWallFactory wallFactory, IRoofFactory roofFactory) {
		super();
		this.roofFactory = roofFactory;
		this.wallFactory = wallFactory;
		size = roofFactory.getDimension() + wallFactory.getDimension();
	}

	public IRoofFactory getRoofFactory() {
		return roofFactory;
	}

	public IWallFactory getFootprintFactory() {
		return wallFactory;
	}

	@Override
	public ParametricBuilding build(double[] arg0) {
		return new ParametricBuilding(wallFactory, roofFactory,  arg0);
	}

	@Override
	public void setCoordinates(ParametricBuilding arg0, double[] arg1) {
		List<Double> d = new ArrayList<>();
		int nbdouble = arg1.length;

		for (int i = 0; i < nbdouble; i++) {
			d.add(arg1[i]);
		}
		arg0.set(d);

	}

	@Override
	public int size() {
		return size;
	}

}
