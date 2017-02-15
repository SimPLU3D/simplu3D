package fr.ign.cogit.simplu3d.rjmcmc.paramshp.builder;

import fr.ign.cogit.simplu3d.rjmcmc.paramshp.geometry.impl.LBuildingWithRoof;
import fr.ign.mpp.kernel.ObjectBuilder;

public class LBuildingWithRoofBuilder implements ObjectBuilder<LBuildingWithRoof> {

	@Override
	public LBuildingWithRoof build(double[] val) {
		return new LBuildingWithRoof(val[0], val[1], val[2], val[3], val[4], val[5], val[6], val[7], val[8], val[9]);
	}

	@Override
	public void setCoordinates(LBuildingWithRoof t, double[] val) {
		t.setCoordinates(val);

	}

	@Override
	public int size() {
		return 10;
	}

}
