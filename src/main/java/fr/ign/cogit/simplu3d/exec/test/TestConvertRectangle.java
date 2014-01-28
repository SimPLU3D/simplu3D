package fr.ign.cogit.simplu3d.exec.test;

import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.conversion.JtsGeOxygene;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid2;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.loader.LoaderCuboid2;

public class TestConvertRectangle {

  /**
   * @param args
   * @throws Exception 
   */
  public static void main(String[] args) throws Exception {
    String strShpIn = "C:/Users/mbrasebin/Pictures/Experimentations/ExpCont/ExpRot2.shp";
    
    
    List<Cuboid2> lCuboid = LoaderCuboid2.loadFromShapeFile(strShpIn);
    
    IFeatureCollection<IFeature> featC1 = new FT_FeatureCollection<>();
    IFeatureCollection<IFeature> featC2 = new FT_FeatureCollection<>();
    
    
    for(Cuboid2 c: lCuboid){
      featC1.add(new DefaultFeature(c.getFootprint()));
      featC2.add(new DefaultFeature(   JtsGeOxygene.makeGeOxygeneGeom(c.getRectangle2D().toGeometry())));
      

    }
    
    
    ShapefileWriter.write(featC1, "E:/temp/shp1.shp");
    ShapefileWriter.write(featC2, "E:/temp/shp2.shp");
  }

}
