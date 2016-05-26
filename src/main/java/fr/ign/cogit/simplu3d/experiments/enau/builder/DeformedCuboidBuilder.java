package fr.ign.cogit.simplu3d.experiments.enau.builder;

import fr.ign.cogit.simplu3d.experiments.enau.geometry.DeformedCuboid;
import fr.ign.mpp.kernel.ObjectBuilder;

public class DeformedCuboidBuilder implements ObjectBuilder<DeformedCuboid> {

	@Override
	public int size() {
		return 9;
	}

	@Override
	public DeformedCuboid build(double[] val1) {
		return new DeformedCuboid(val1[0], val1[1], val1[2], val1[3], val1[4], val1[5], val1[6], val1[7], val1[8]);
	}

	@Override
	public void setCoordinates(DeformedCuboid t, double[] val1) {
		val1[0] = t.centerx;
		val1[1] = t.centery;
		val1[2] = t.length;
		val1[3] = t.width;
		val1[4] = t.height1;
		val1[5] = t.height2;
		val1[6] = t.height3;
		val1[7] = t.height4;
		val1[8] = t.orientation;
	}

}
