package fr.ign.cogit.simplu3d.exec;

import java.awt.Color;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.gui.MainWindow;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.geoxygene.util.conversion.JtsGeOxygene;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.convert.GenerateSolidFromCuboid;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.impl.Cuboid2;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.loader.LoaderCuboid2;

public class CuboidRep {

  /**
   * @param args
   * @throws Exception 
   */
  public static void main(String[] args) throws Exception {
    // TODO Auto-generated method stub

    IFeatureCollection<IFeature> iFC = new FT_FeatureCollection<>();

    IFeatureCollection<IFeature> iFCWrite = new FT_FeatureCollection<>();

    for (int i = 0; i < 100; i++) {

      Cuboid2 c = new Cuboid2(50, 50 + i * 50, 10, 30, 5, i * Math.PI / 50);
      iFCWrite.add(new DefaultFeature(JtsGeOxygene.makeGeOxygeneGeom(c
          .getRectangle2D().toGeometry())));

      iFC.add(new DefaultFeature(GenerateSolidFromCuboid.generate(c, 0)));
    }

    
    List<Cuboid2> lCub =  LoaderCuboid2.loadFromShapeFile("C:/Users/mbrasebin/Pictures/Experimentations/ExpCont/Exp4Conf.shp"); //LoaderCuboid2.loadFromCollection(iFC);
    
    IFeatureCollection<IFeature> iFC2 = new FT_FeatureCollection<>();
    
    for(Cuboid2 c: lCub){
      iFC2.add(new DefaultFeature(GenerateSolidFromCuboid.generate(c, 0)));
    }
    
    
    
    
    
    
    
    
    MainWindow mW = new MainWindow();

    
    
    ShapefileWriter.write(iFCWrite, "C:/Temp/test.shp");
    
    mW.getInterfaceMap3D().getCurrent3DMap()
        .addLayer(new VectorLayer(iFC, "Toto", Color.black));
    
    
    mW.getInterfaceMap3D().getCurrent3DMap()
        .addLayer(new VectorLayer(iFC2, "Toto2", Color.red));

  }

}
