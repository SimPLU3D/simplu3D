package fr.ign.cogit.simplu3d.experiments.openmole.diversity;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;

public class BoxCounter {

	IFeatureCollection<IFeature> featColl;

	public BoxCounter(IFeatureCollection<IFeature> featColl) {
		this.featColl = featColl;

	}

	public int count() {

		return featColl.size();

	}
}
