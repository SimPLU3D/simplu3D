package fr.ign.cogit.simplu3d.exec.test;

import java.awt.Color;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.gui.MainWindow;
import fr.ign.cogit.geoxygene.sig3d.representation.sample.ObjectCartoon;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.Cuboid2;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.GenerateSolidFromCuboid;

public class TestBidon {

  /**
   * @param args
   */
  public static void main(String[] args) {
    Cuboid2 c = new Cuboid2(50, 50, 10, 10, (Math.PI) / 4, 10);
    Cuboid2 c2 = new Cuboid2(50, 50, 10, 10, 0, 10);

    IFeature feat = new DefaultFeature(GenerateSolidFromCuboid.generate(c,0));
    IFeature feat2 = new DefaultFeature(GenerateSolidFromCuboid.generate(c2,0));
    
    feat.setRepresentation(new ObjectCartoon(feat, Color.red));
    feat2.setRepresentation(new ObjectCartoon(feat2, Color.red));

    IFeatureCollection<IFeature> iFeatC = new FT_FeatureCollection<>();
    iFeatC.add(feat);
    iFeatC.add(feat2);
    
    MainWindow mw = new MainWindow();
    mw.getInterfaceMap3D().getCurrent3DMap().addLayer(new VectorLayer(iFeatC,"tutu"));

    System.out.println(c.getFootprint().coord());

  }
}
