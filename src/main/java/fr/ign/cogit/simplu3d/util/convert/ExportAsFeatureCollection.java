package fr.ign.cogit.simplu3d.util.convert;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;

public class ExportAsFeatureCollection {
  GraphConfiguration<? extends AbstractSimpleBuilding> config;
  int id = -1;
  long seed = -1;
  boolean exportId = false;
  boolean exportSeed = false;

  public ExportAsFeatureCollection(GraphConfiguration<? extends AbstractSimpleBuilding> cc) {
    this.config = cc;
  }

  public ExportAsFeatureCollection(GraphConfiguration<? extends AbstractSimpleBuilding> cc, int id) {
    this.config = cc;
    this.id = id;
    this.exportId = true;
  }

  public ExportAsFeatureCollection(GraphConfiguration<? extends AbstractSimpleBuilding> cc, int id, long seed) {
    this.config = cc;
    this.id = id;
    this.seed = seed;
    this.exportId = this.exportSeed = true;
  }

  public IFeatureCollection<IFeature> getFeatureCollection() {
    IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();
    for (GraphVertex<? extends AbstractSimpleBuilding> v : this.config.getGraph().vertexSet()) {

      IFeature feat = new DefaultFeature(v.getValue().generated3DGeom());
      if (this.exportId) {
        AttributeManager.addAttribute(feat, "idparc", this.id, "Integer");
      }
      //AttributeManager.addAttribute(feat, "largeur", largeur, "Double");
      //AttributeManager.addAttribute(feat, "longueur", longueur, "Double");
      //AttributeManager.addAttribute(feat, "hauteur", hauteur, "Double");
      //AttributeManager.addAttribute(feat, "orient", orientation, "Double");
      if (this.exportSeed) {
        AttributeManager.addAttribute(feat, "seed", this.seed, "Long");
      }
      featC.add(feat);
    }
    return featC;
  }
}
