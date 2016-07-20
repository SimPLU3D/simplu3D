package fr.ign.cogit.simplu3d.experiments.enau.geometry;

import java.util.List;

import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.geometry.Primitive;

public class ParallelDeformedCuboid extends DeformedCuboid implements Primitive {


	public ParallelDeformedCuboid(double centerx, double centery,
			double length, double width, double height1, double height2,
			double height3, double height4, double orientation) {
		super(centerx, centery, length, width, height1, height2, height3,
				height4, orientation);

	}

	@Override
	public Object[] getArray() {
		return new Object[] { this.centerx, this.centery, this.length,
		 this.width, this.height1, this.height2, this.height3,
				this.height4,  this.orientation };
	}

	@Override
	public int size() {
		return 9;
	}

	@Override
	public int hashCode() {
		int hashCode = 1;
		double[] array = { this.centerx, this.centery, this.length, /*
																	 * this.width
																	 * ,
																	 */
		this.height1, this.height2, this.height3, this.height4 /*
																 * ,
																 * this.orientation
																 */};
		for (double e : array)
			hashCode = 31 * hashCode + hashCode(e);
		return hashCode;
	}

	public int hashCode(double value) {
		long bits = Double.doubleToLongBits(value);
		return (int) (bits ^ (bits >>> 32));
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Cuboid)) {
			return false;
		}
		ParallelDeformedCuboid r = (ParallelDeformedCuboid) o;
		return (this.centerx == r.centerx)
				&& (this.centery== r.centery)
				&& (this.width== r.width)
				&& (this.length== r.length)
				&& (this.height1== r.height1)
				&& (this.height2== r.height2)
				&& (this.height3== r.height3)
				&& (this.height4== r.height4);
	}

	public String toString() {
		return "Cuboid : " + " Centre " + this.centerx + "; " + this.centery
				+ " hauteur1 " + this.height1

				+ " hauteur2 " + this.height2 + " hauteur3 " + this.height3
				+ " hauteur4 " + this.height4

				+ " largeur " + this.width;

	}

	@Override
	public double[] toArray() {
		return new double[] { this.centerx, this.centery, this.length,
				this.height1, this.height2, this.height3, this.height4 };
	}

	@Override
	public void set(List<Double> list) {
		this.centerx = list.get(0);
		this.centery = list.get(1);
		this.length = list.get(2);
		this.height1 = list.get(3);
		this.height2 = list.get(4);
		this.height3 = list.get(5);
		this.height4 = list.get(6);
		this.setGenerated(true);
	}

}
