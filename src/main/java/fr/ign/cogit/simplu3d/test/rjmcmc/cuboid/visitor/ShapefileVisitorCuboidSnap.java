package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.visitor;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;


import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.impl.CuboidSnap;
import fr.ign.mpp.configuration.Configuration;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.temperature.Temperature;
import fr.ign.simulatedannealing.visitor.Visitor;

public class ShapefileVisitorCuboidSnap<O, C extends Configuration<O>, T extends Temperature, S extends Sampler<O, C, T>>
    implements Visitor<O, C, T, S> {
  private int save;
  private int iter;
  private String fileName;

  public ShapefileVisitorCuboidSnap(String fileName) {
    this.fileName = fileName;
  }

  @Override
  public void init(int dump, int s) {
    this.iter = 0;
    this.save = s;
  }

  @Override
  public void begin(C config, S sampler, T t) {
  }

  @SuppressWarnings( { "unchecked" })
  @Override
  public void end(C config, S sampler, T t) {
    this.writeShapefile(fileName + "_" + String.format(formatInt, iter + 1) + ".shp",
        (GraphConfiguration<CuboidSnap>) config);
  }

  String formatInt = "%1$-10d";

  @SuppressWarnings( { "unchecked" })
  @Override
  public void visit(C config, S sampler, T t) {
    ++iter;
    if ((save > 0) && (iter % save == 0)) {
      this.writeShapefile(fileName + "_" + String.format(formatInt, iter) + ".shp",
          (GraphConfiguration<CuboidSnap>) config);
    }
  }

  @SuppressWarnings( { "unchecked", "deprecation" })
  private void writeShapefile(String aFileName, GraphConfiguration<CuboidSnap> config) {
    try {
      ShapefileDataStore store = new ShapefileDataStore(new File(aFileName).toURI().toURL());
      String specs = "geom:Polygon,energy:double"; //$NON-NLS-1$
      String featureTypeName = "Building"; //$NON-NLS-1$
      SimpleFeatureType type = DataUtilities.createType(featureTypeName, specs);
      store.createSchema(type);
      FeatureStore<SimpleFeatureType, SimpleFeature> featureStore = (FeatureStore<SimpleFeatureType, SimpleFeature>) store
          .getFeatureSource(featureTypeName);
      Transaction transaction = new DefaultTransaction();
      FeatureCollection<SimpleFeatureType, SimpleFeature> collection = FeatureCollections
          .newCollection();
      int i = 1;
      GraphConfiguration<CuboidSnap> graph = (GraphConfiguration<CuboidSnap>) config;
      for (GraphConfiguration<CuboidSnap>.GraphVertex v : graph.getGraph().vertexSet()) {
        List<Object> liste = new ArrayList<Object>(0);
        liste.add(v.getValue().getRectangle2D().toGeometry());
        liste.add(v.getEnergy());
        SimpleFeature simpleFeature = SimpleFeatureBuilder.build(type, liste.toArray(), String
            .valueOf(i++));
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
    }
  }
}
