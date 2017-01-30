package fr.ign.cogit.simplu3d.experiments.openmole.diversity;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;

public class MaxHeight {

	IFeatureCollection<IFeature> featColl;

	public MaxHeight(IFeatureCollection<IFeature> featColl) {
		this.featColl = featColl;

	}

	public double height() {

		double heightMax = 0;

		for (IFeature c : featColl) {
			
			Box3D b = new Box3D(c.getGeom());
			heightMax = Math.max(b.getHeight(), heightMax);

		}

		return heightMax;

	}

}
