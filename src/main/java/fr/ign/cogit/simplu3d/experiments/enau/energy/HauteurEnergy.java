package fr.ign.cogit.simplu3d.experiments.enau.energy;

import fr.ign.cogit.simplu3d.experiments.enau.geometry.DeformedCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.rjmcmc.energy.UnaryEnergy;

public class HauteurEnergy<T> implements UnaryEnergy<T> {

	private double hMax;

	public HauteurEnergy(double hMax) {
		this.hMax = hMax;
	}

	@Override
	public double getValue(T t) {
		
		
		if(t instanceof Cuboid){

		Cuboid c = (Cuboid) t;

		double value = Math.min(c.height, hMax) / Math.max(c.height, hMax);
		
		
		return value;
		
		}
		
		
		if(t instanceof DeformedCuboid){
			DeformedCuboid c = (DeformedCuboid) t;
			
			double value1 = Math.min(c.height1, hMax) / Math.max(c.height1, hMax);
			double value2 = Math.min(c.height2, hMax) / Math.max(c.height2, hMax);
			double value3 = Math.min(c.height3, hMax) / Math.max(c.height3, hMax);
			double value4 = Math.min(c.height4, hMax) / Math.max(c.height4, hMax);
			
			
			return (value1 + value2 + value3 + value4)/4;
		}
		
		System.out.println("Classe non détectée");
		return 0;
	}

}
