package fr.ign.cogit.simplu3d.experiments.enau.transformation;

import fr.ign.rjmcmc.kernel.Transform;

public class ChangeHeight2Deformed implements Transform {

	  private double amplitude;

	  public ChangeHeight2Deformed(double amplitude) {
	    this.amplitude = amplitude;
	  }

	  @Override
	  public double apply(boolean direct, double[] val0, double[] val1) {

	    double dh = val0[9];

	    val1[0] = val0[0];
	    val1[1] = val0[1];
	    val1[2] = val0[2];
	    val1[3] = val0[3];
	    val1[4] = val0[4] ;
	    val1[5] = val0[5]+ (0.5 - dh) * amplitude;
	    val1[6] = val0[6];
	    val1[7] = val0[7];
	    val1[8] = val0[8];

	    val1[9] = 1 - dh;

	    return 1;
	  }

	  // @Override
	  public double getAbsJacobian(boolean direct) {
	    return 1;
	  }

	  @Override
	  public int dimension() {
	    return 10;
	  }

}
