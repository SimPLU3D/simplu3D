package fr.ign.cogit.simplu3d.rjmcmc.trapezoid.builder;

import fr.ign.cogit.simplu3d.rjmcmc.trapezoid.geometry.RightTrapezoid;
import fr.ign.mpp.kernel.ObjectBuilder;

public class SimpleRightTrapezoidBuilder implements ObjectBuilder<RightTrapezoid> {

	public SimpleRightTrapezoidBuilder() {
		super();
	}

	@Override
	public RightTrapezoid build(double[] val1) {
		// TODO Auto-generated method stub
		return new RightTrapezoid(val1[0], val1[1], val1[2], val1[3], val1[4], val1[5], val1[6], val1[7]);
	}

	@Override
	public void setCoordinates(RightTrapezoid t, double[] val1) {
		val1[0] = t.centerx;
		val1[1] = t.centery;
		val1[2] = t.length1;
		val1[3] = t.length2;
		val1[4] = t.length3;
		val1[5] = t.width;
		val1[6] = t.height;
		val1[7] = t.orientation;

	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 8;
	}

}
