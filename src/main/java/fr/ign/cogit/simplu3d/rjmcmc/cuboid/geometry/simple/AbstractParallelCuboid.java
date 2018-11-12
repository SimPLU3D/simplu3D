package fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.simple;

import java.util.List;

import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;

public abstract class AbstractParallelCuboid extends Cuboid {

	  public AbstractParallelCuboid(double centerx, double centery, double length, double width, double height, double orientation) {
	    super(centerx, centery, length, width, height, orientation);
	  }
	  @Override
	  public Object[] getArray() {
	    return new Object[] { this.centerx, this.centery, this.length/*, this.width*/, this.height/*, this.orientation*/ };
	  }

	  @Override
	  public int size() {
	    return 4;
	  }

	  @Override
	  public int hashCode() {
	    int hashCode = 1;
	    double[] array = { this.centerx, this.centery, this.length/*, this.width*/, this.orientation/*, this.height*/ };
	    for (double e : array)
	      hashCode = 31 * hashCode + hashCode(e);
	    return hashCode;
	  }

	  @Override
	  public boolean equals(Object o) {
	    if (!(o instanceof ParallelCuboid)) {
	      return false;
	    }
	    ParallelCuboid r = (ParallelCuboid) o;
	    return (this.centerx== r.centerx) && (this.centery== r.centery)
	        && (this.width== r.width) && (this.length== r.length)
	        && (this.orientation== r.orientation) && (this.height== r.height);
	  }

	  public String toString() {
	    return "ParallelCuboid : " + " Centre " + this.centerx + "; " + this.centery + " hauteur " + this.height + " largeur "
	        + this.width + " longueur " + this.width + " orientation " + this.orientation;

	  }
	  @Override
	  public double[] toArray() {
	    return new double[] { this.centerx, this.centery, this.length/*, this.width*/, this.height/*, this.orientation*/ };
	  }

	  @Override
	  public void set(List<Double> list) {
	    this.centerx = list.get(0);
	    this.centery = list.get(1);
	    this.length = list.get(2);
//	    this.width = list.get(3);
	    this.height = list.get(3);
//	    this.orientation = list.get(5);
		this.setNew(true);
	  }
	  

	  
	  
	}
