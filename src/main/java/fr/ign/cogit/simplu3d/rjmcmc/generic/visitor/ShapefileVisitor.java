package fr.ign.cogit.simplu3d.rjmcmc.generic.visitor;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.conversion.JtsGeOxygene;
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
 * see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
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
		this.writeShapefile(fileName + "_" + String.format(formatInt, iter + 1) + ".shp", config);
	}

	String formatInt = "%1$-10d";

	@Override
	public void visit(C config, Sampler<C, M> sampler, Temperature t) {
		++iter;
		if ((save > 0) && (iter % save == 0)) {
			this.writeShapefile(fileName + "_" + String.format(formatInt, iter) + ".shp", config);
		}
	}

	@SuppressWarnings({ "unchecked" })
	private void writeShapefile(String aFileName, C config) {
		try {
			ShapefileDataStore store = new ShapefileDataStore(new File(aFileName).toURI().toURL());
			String specs = "geom:MultiPolygon:srid=2154,energy:double"; //$NON-NLS-1$
			String featureTypeName = "Building"; //$NON-NLS-1$
			SimpleFeatureType type = DataUtilities.createType(featureTypeName, specs);
			store.createSchema(type);
			FeatureStore<SimpleFeatureType, SimpleFeature> featureStore = (FeatureStore<SimpleFeatureType, SimpleFeature>) store
					.getFeatureSource(featureTypeName);
			Transaction transaction = new DefaultTransaction();
			// FeatureCollection<SimpleFeatureType, SimpleFeature> collection =
			// FeatureCollections
			// .newCollection();
			ListFeatureCollection collection = new ListFeatureCollection(type);
			int i = 1;
			for (GraphVertex<O> v : config.getGraph().vertexSet()) {

				List<Object> liste = new ArrayList<>();

				IMultiSurface<IOrientableSurface> iMS = new GM_MultiSurface<>();
				iMS.addAll(FromGeomToSurface.convertGeom(v.getValue().generated3DGeom()));

				liste.add(JtsGeOxygene.makeJtsGeom(iMS));
				liste.add(v.getEnergy());
				SimpleFeature simpleFeature = SimpleFeatureBuilder.build(type, liste.toArray(), String.valueOf(i++));
				collection.add(simpleFeature);
			}
			featureStore.addFeatures(collection);
			transaction.commit();
			transaction.close();
			store.dispose();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SchemaException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
