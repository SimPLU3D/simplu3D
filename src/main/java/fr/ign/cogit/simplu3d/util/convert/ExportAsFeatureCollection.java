package fr.ign.cogit.simplu3d.util.convert;

import fr.ign.cogit.simplu3d.io.feature.FeatureType;
import fr.ign.cogit.simplu3d.model.AbstractUrbanFeature;
import fr.ign.cogit.simplu3d.model.UrbanFeature;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;

import java.util.ArrayList;
import java.util.Collection;

public class ExportAsFeatureCollection {
	GraphConfiguration<? extends AbstractSimpleBuilding> config;
	String id = "-1";
	long seed = -1;
	boolean exportId = false;
	boolean exportSeed = false;

	public ExportAsFeatureCollection(GraphConfiguration<? extends AbstractSimpleBuilding> cc) {
		this.config = cc;
	}

	public ExportAsFeatureCollection(GraphConfiguration<? extends AbstractSimpleBuilding> cc, String id) {
		this.config = cc;
		this.id = id;
		this.exportId = true;
	}

	public ExportAsFeatureCollection(GraphConfiguration<? extends AbstractSimpleBuilding> cc, String id, long seed) {
		this.config = cc;
		this.id = id;
		this.seed = seed;
		this.exportId = this.exportSeed = true;
	}

	public FeatureType getFeatureType() {
		FeatureType featureType = new FeatureType("SimPLU feature");
		featureType.setGeometryType("Polygon");
		if (this.exportId) {
			featureType.addAttribute("idparc", "Integer");
		}
		featureType.addAttribute("centerX", "Double");
		featureType.addAttribute("centerY", "Double");
		featureType.addAttribute("width", "Double");
		featureType.addAttribute("lenght", "Double");
		featureType.addAttribute("height", "Double");
		featureType.addAttribute("orient", "Double");
		if (this.exportSeed) {
			featureType.addAttribute("seed", "Long");
		}
		return featureType;
	}
	public Collection<UrbanFeature> getFeatureCollection() {
		Collection<UrbanFeature> featC = new ArrayList<>();
		for (GraphVertex<? extends AbstractSimpleBuilding> v : this.config.getGraph().vertexSet()) {
			AbstractSimpleBuilding build = v.getValue();
			UrbanFeature feat = new AbstractUrbanFeature();
			feat.setGeometry(build.generated3DGeom());
			if (this.exportId) {
				feat.setAttribute("idparc", this.id);
			}
			if (build instanceof Cuboid c) {
                feat.setAttribute("centerX", c.getCenterx());
				feat.setAttribute("centerY", c.getCentery());
				feat.setAttribute("width", c.getWidth());
				feat.setAttribute("lenght", c.getLength());
				feat.setAttribute("height", c.getHeight());
				feat.setAttribute("orient", c.getOrientation());
			}
			if (this.exportSeed) {
				feat.setAttribute("seed", this.seed);
			}
			featC.add(feat);
		}
		return featC;
	}
}
