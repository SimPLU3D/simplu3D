package fr.ign.cogit.simplu3d.enau.energy;

import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.rjmcmc.energy.UnaryEnergy;

public class HauteurEnergy<T> implements UnaryEnergy<T> {

	private double hMax;

	public HauteurEnergy(double hMax) {
		this.hMax = hMax;
	}

	@Override
	public double getValue(T t) {

		Cuboid c = (Cuboid) t;

		double value = Math.min(c.height, hMax) / Math.max(c.height, hMax);
		
		
		return value;
	}

}
