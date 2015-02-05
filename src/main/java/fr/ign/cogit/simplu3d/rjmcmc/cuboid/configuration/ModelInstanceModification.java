package fr.ign.cogit.simplu3d.rjmcmc.cuboid.configuration;

import java.util.ArrayList;

import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.mpp.configuration.AbstractBirthDeathModification;

public class ModelInstanceModification<T extends AbstractSimpleBuilding>
		extends
		AbstractBirthDeathModification<T, ModelInstanceGraphConfiguration<T>, ModelInstanceModification<T>> {
	/**
	 * Create a new empty configuration.
	 */
	public ModelInstanceModification() {
	}
}
