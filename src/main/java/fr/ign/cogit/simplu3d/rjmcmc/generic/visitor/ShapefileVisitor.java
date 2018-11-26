package fr.ign.cogit.simplu3d.rjmcmc.generic.visitor;



import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;
import fr.ign.mpp.configuration.AbstractBirthDeathModification;
import fr.ign.mpp.configuration.AbstractGraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.temperature.Temperature;
import fr.ign.simulatedannealing.visitor.Visitor;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see http://www.cecill.info/
 * 
 * 
 * 
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
	private String fileName;

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

	@SuppressWarnings({ "unchecked" })
	private void writeShapefile(String aFileName, C config) {
		
		IFeatureCollection<IFeature> featureOut = new FT_FeatureCollection<>();
		
	
	
			for (GraphVertex<O> v : config.getGraph().vertexSet()) {

			

				IMultiSurface<IOrientableSurface> iMS = new GM_MultiSurface<>();
				iMS.addAll(FromGeomToSurface.convertGeom(v.getValue().generated3DGeom()));
				
				IFeature feat = new DefaultFeature(iMS);
				AttributeManager.addAttribute(feat, "Energy", v.getEnergy(), "Double");
				AttributeManager.addAttribute(feat, "ToString", v.toString(), "String");
				
				featureOut.add(feat);
				
			}
			
			ShapefileWriter.write(featureOut, aFileName);
	
	}
}
