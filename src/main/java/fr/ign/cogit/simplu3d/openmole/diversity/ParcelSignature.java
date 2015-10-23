package fr.ign.cogit.simplu3d.openmole.diversity;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Envelope;
import fr.ign.cogit.simplu3d.io.ExportAsFeatureCollection;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.mpp.configuration.GraphConfiguration;

public class ParcelSignature {
  GraphConfiguration<Cuboid> conf;
  BasicPropertyUnit propertyUnit;

  public ParcelSignature(GraphConfiguration<Cuboid> c, BasicPropertyUnit bPU) {
    this.conf = c;
    this.propertyUnit = bPU;
  }

  public long getSignature(double tileSize) {
    String result = "";
    ExportAsFeatureCollection exporter = new ExportAsFeatureCollection(this.conf);
    IFeatureCollection<IFeature> collection = exporter.getFeatureCollection();
    IEnvelope envelope = this.propertyUnit.getGeom().getEnvelope();
    double w = envelope.width();
    double l = envelope.length();
    int numberOfTilesX = Math.max(1, (int) (w / tileSize));
    int numberOfTilesY = Math.max(1, (int) (l / tileSize));
    double tileSizeX = w / numberOfTilesX;
    double tileSizeY = l / numberOfTilesY;
    double currentX = envelope.minX();
//    List<IPolygon> list = new ArrayList<>();
    for (int i = 0; i < numberOfTilesX; i++, currentX += tileSizeX) {
      double currentY = envelope.minY();
      for (int j = 0; j < numberOfTilesY; j++, currentY += tileSizeY) {
        IEnvelope env = new GM_Envelope(currentX, currentX + tileSizeX, currentY, currentY + tileSizeY);
        boolean empty = collection.select(env).isEmpty();
        result += empty ? "0" : "1";
//        if (!empty) {
//          list.add(env.getGeom());
//        }
      }
    }
//    System.out.println(new GM_MultiSurface<IPolygon>(list));
//    System.out.println(result);
    return Long.parseLong(result, 2);
  }
}
