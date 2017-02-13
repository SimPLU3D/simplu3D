package fr.ign.cogit.simplu3d.rjmcmc.trapezoid.transformation;

import fr.ign.rjmcmc.kernel.Transform;


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
public class MoveParallelRightTrapezoid implements Transform  {
	
	  private double amplitudeMove;

	  public MoveParallelRightTrapezoid(double amplitudeMove) {
	    this.amplitudeMove = amplitudeMove;
	  }

	  @Override
	  public double apply(boolean direct, double[] val0, double[] val1) {

	    double dx = val0[4];
	    double dy = val0[5];
	    val1[0] = val0[0] + (0.5 - dx) * amplitudeMove;
	    val1[1] = val0[1] + (0.5 - dy) * amplitudeMove;
	    val1[2] = val0[2];
	    val1[3] = val0[3];

	    val1[4] = 1 - dx;
	    val1[5] = 1 - dy;
	    return 1;
	  }

	//  @Override
	  public double getAbsJacobian(boolean direct) {
	    return 1;
	  }

	  @Override
	  public int dimension() {
	    return 6;
	  }
}
