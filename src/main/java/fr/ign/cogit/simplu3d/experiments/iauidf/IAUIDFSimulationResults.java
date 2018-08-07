package fr.ign.cogit.simplu3d.experiments.iauidf;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;

public class IAUIDFSimulationResults {
	
	IFeatureCollection<IFeature> featResults;
	int nbIteration;
	double energy;
	public IAUIDFSimulationResults(IFeatureCollection<IFeature> featResults, int nbIteration, double energy) {
		super();
		this.featResults = featResults;
		this.nbIteration = nbIteration;
		this.energy = energy;
	}
	public IFeatureCollection<IFeature> getFeatResults() {
		return featResults;
	}
	public int getNbIteration() {
		return nbIteration;
	}
	
	public double getEnergy() {
		return energy;
	}
	

}
