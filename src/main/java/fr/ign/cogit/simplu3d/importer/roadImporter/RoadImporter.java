package fr.ign.cogit.simplu3d.importer.roadImporter;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.contrib.delaunay.TriangulationJTS;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.sig3d.topology.TriangulationLoader;

public class RoadImporter {

  /**
   * @param args
   * @throws Exception 
   */
  public static void main(String[] args) throws Exception {
    
    
    // TODO Auto-generated method stub
    String folder = "E:/mbrasebin/Donnees/Strasbourg/ZoneTest/Experimentation3/ImportRoute/";
    String fileParcelle = folder + "export3.shp";

    IFeatureCollection<IFeature> parcelles = ShapefileReader.read(fileParcelle);

    IMultiSurface<IOrientableSurface> iMS = new GM_MultiSurface<>();

    for (IFeature feat : parcelles) {

      iMS.addAll(FromGeomToSurface.convertMSGeom(feat.getGeom()).getList());

    }

    
    TriangulationJTS triJTS = TriangulationLoader.generate(iMS);
    
    triJTS.triangule();
    
    
    ShapefileWriter.write(triJTS.getPopFaces(), folder + "out.shp");
    
    
    
    
  }

}
