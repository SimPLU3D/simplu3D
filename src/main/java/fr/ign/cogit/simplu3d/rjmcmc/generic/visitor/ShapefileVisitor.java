package fr.ign.cogit.simplu3d.rjmcmc.generic.visitor;

import fr.ign.cogit.simplu3d.io.feature.FeatureType;
import fr.ign.cogit.simplu3d.model.AbstractUrbanFeature;
import fr.ign.cogit.simplu3d.model.UrbanFeature;
import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;
import fr.ign.common.conversion.FromGeomToSurface;
import fr.ign.common.io.ShapefileWriter;
import fr.ign.mpp.configuration.AbstractBirthDeathModification;
import fr.ign.mpp.configuration.AbstractGraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.temperature.Temperature;
import fr.ign.simulatedannealing.visitor.Visitor;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * This software is released under the licence CeCILL
 * <p>
 * see LICENSE.TXT
 * <p>
 * see <a href="http://www.cecill.info/">...</a>
 * <p>
 * copyright IGN
 *
 * @author Brasebin Mickaël
 *
 * @version 1.0
 **/
public class ShapefileVisitor<O extends ISimPLU3DPrimitive, C extends AbstractGraphConfiguration<O, C, M>, M extends AbstractBirthDeathModification<O, C, M>>
		implements Visitor<C, M> {
	private int save;
	private int iter;
	private final String fileName;

	public ShapefileVisitor(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void init(int dump, int s) {
		this.iter = 0;
		this.save = s;
	}

	@Override
	public void begin(C config, Sampler<C, M> sampler, Temperature t) {
	}

	@Override
	public void end(C config, Sampler<C, M> sampler, Temperature t) {
		this.writeShapefile(fileName + "_" + (iter +1 )+ ".shp", config);
	}

	@Override
	public void visit(C config, Sampler<C, M> sampler, Temperature t) {
		++iter;
		if ((save > 0) && (iter % save == 0)) {
			this.writeShapefile(fileName + "_" + iter+ ".shp", config);
		}
	}

	private void writeShapefile(String aFileName, C config) {
		Collection<UrbanFeature> featureOut = new ArrayList<>();
		for (GraphVertex<O> v : config.getGraph().vertexSet()) {
            List<Polygon> iMS = new ArrayList<>(FromGeomToSurface.convertGeom(v.getValue().generated3DGeom()));
			UrbanFeature feat = new AbstractUrbanFeature();
			feat.setGeometry(new GeometryFactory().createMultiPolygon(iMS.toArray(new Polygon[0])));
			feat.setAttribute("Energy", v.getEnergy());
			feat.setAttribute("ToString", v.toString());
			featureOut.add(feat);
		}
		FeatureType featureType = new FeatureType("UrbanFeature");
		featureType.setGeometryType("MultiPolygon");
		featureType.addAttribute("Energy", "Double");
		featureType.addAttribute("ToString", "String");
		ShapefileWriter.write(featureOut, featureType, null, aFileName);//FIXME where should I get the crs?
	}
}
