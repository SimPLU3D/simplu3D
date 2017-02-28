package fr.ign.cogit.simplu3d.experiments.plu2plus.context;

import java.util.List;

import fr.ign.cogit.simplu3d.checker.model.RuleContext;
import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;

public class SimulationcheckerContext extends RuleContext {
	
	
	
	private Cuboid newCuboid;
	private List<AbstractBuilding> existingCuboid;

	public Cuboid getNewCuboid() {
		return newCuboid;
	}

	public void setNewCuboid(Cuboid newCuboid) {
		this.newCuboid = newCuboid;
	}

	public List<AbstractBuilding> getExistingCuboid() {
		return existingCuboid;
	}

	public void setExistingCuboid(List<AbstractBuilding> existingCuboid) {
		this.existingCuboid = existingCuboid;
	}	

}
