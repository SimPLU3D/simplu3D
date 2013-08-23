package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.visitor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.gui.MainWindow;
import fr.ign.cogit.geoxygene.sig3d.representation.texture.TextureManager;
import fr.ign.cogit.geoxygene.sig3d.representation.texture.TexturedSurface;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.geoxygene.sig3d.util.ColorRandom;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.representation.RepEnvironnement;
import fr.ign.cogit.simplu3d.representation.RepEnvironnement.Theme;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.Cuboid;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.Cuboid2;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.CuboidSnap;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.GenerateSolidFromCuboid;
import fr.ign.mpp.configuration.Configuration;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.temperature.Temperature;
import fr.ign.simulatedannealing.visitor.Visitor;

public class ViewerVisitor<O, C extends Configuration<O>, T extends Temperature, S extends Sampler<O, C, T>>
    implements Visitor<O, C, T, S> {

  private MainWindow mW = null;
  private int save;
  private int iter;

  private GraphConfiguration<Cuboid> bestConfig = null;
  private double bestValue = Double.POSITIVE_INFINITY;

  public ViewerVisitor() {
    mW = new MainWindow();
    represent(Environnement.getInstance(), mW);
  }

  @Override
  public void init(int dump, int save) {
    this.iter = 0;
    this.save = save;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void visit(C config, S sampler, T t) {
    ++iter;

    if (config.getEnergy() < bestValue) {
      bestValue = config.getEnergy();
      bestConfig = (GraphConfiguration<Cuboid>)config;

    }

    if ((save > 0) && (iter % save == 0)) {
      this.addInformationToMainWindow((GraphConfiguration<Cuboid>) config);
    }
  }

  @Override
  public void begin(C config, S sampler, T t) {
  }

  @SuppressWarnings("unchecked")
  @Override
  public void end(C config, S sampler, T t) {

    this.addInformationToMainWindow(bestConfig);
  }

  private void addInformationToMainWindow(GraphConfiguration<Cuboid> config) {

    IFeatureCollection<IFeature> feat = new FT_FeatureCollection<>();

    for (GraphConfiguration<?>.GraphVertex v : config.getGraph().vertexSet()) {

      IGeometry geom = null;

      Object o = v.getValue();

      if (v.getValue() instanceof Cuboid) {
        geom = GenerateSolidFromCuboid.generate((Cuboid) o);

      } else if (v.getValue() instanceof Cuboid2) {
        geom = GenerateSolidFromCuboid.generate((Cuboid2) o);
      } else if (v.getValue() instanceof CuboidSnap) {
        geom = GenerateSolidFromCuboid.generate((CuboidSnap) o);
      }

      if (geom == null) {
        continue;
      }

      DefaultFeature df = new DefaultFeature(geom);
      AttributeManager.addAttribute(df, "Energy", v.getEnergy(), "Double");
      feat.add(df);

    }

    if (!feat.isEmpty()) {
      VectorLayer vl = new VectorLayer(feat, "Étape : " + iter,
          ColorRandom.getRandomColor());
      mW.getInterfaceMap3D().getCurrent3DMap().addLayer(vl);
    }

  }

  private static void represent(Environnement env, MainWindow mW) {
    List<Theme> lTheme = new ArrayList<RepEnvironnement.Theme>();
    lTheme.add(Theme.TOIT_BATIMENT);
    lTheme.add(Theme.FACADE_BATIMENT);
    // lTheme.add(Theme.FAITAGE);
    // lTheme.add(Theme.PIGNON);
    // lTheme.add(Theme.GOUTTIERE);
    // lTheme.add(Theme.VOIRIE);
    // lTheme.add(Theme.PARCELLE);
    // lTheme.add(Theme.BORDURE);
    // lTheme.add(Theme.ZONE);
    // lTheme.add(Theme.PAN);

    Theme[] tab = lTheme.toArray(new Theme[0]);

    List<VectorLayer> vl = RepEnvironnement.represent(env, tab);

    for (VectorLayer l : vl) {

      mW.getInterfaceMap3D().getCurrent3DMap().addLayer(l);
    }

    mW.getInterfaceMap3D().removeLight(0);
    mW.getInterfaceMap3D().addLight(new Color(147, 147, 147), 0, 0, 0);
    mW.getInterfaceMap3D().moveLight(180, -15, 120, 0);
    mW.getInterfaceMap3D().addLight(new Color(147, 147, 147), 0, 0, 0);
    mW.getInterfaceMap3D().moveLight(-140, 3, 120, 1);

    double z = 140;
    //
    // 1051042.8513268954120576,6840539.0837931865826249 :
    // 1051264.8064121364150196,6840679.2711814027279615
    // Projet 1
    /* IDirectPosition dpLL = new
     DirectPosition(1051042.8513268954120576,6840539.0837931865826249,z);
     IDirectPosition dpUR = new
     DirectPosition(1051264.8064121364150196,6840679.2711814027279615,z);*/

    // Projet 3
   IDirectPosition dpLL = new DirectPosition(1051157, 6840727, z);
  IDirectPosition dpUR = new DirectPosition(1051322, 6840858, z);

    IDirectPositionList dpl = new DirectPositionList();

    IDirectPosition dp2 = new DirectPosition(dpUR.getX(), dpLL.getY(), z);

    IDirectPosition dp4 = new DirectPosition(dpLL.getX(), dpUR.getY(), z);

    dpl.add(dpLL);
    dpl.add(dp2);
    dpl.add(dpUR);
    dpl.add(dp4);
    dpl.add(dpLL);

    IFeatureCollection<IFeature> fc = new FT_FeatureCollection<IFeature>();

    IFeature feat = new DefaultFeature(new GM_Polygon(new GM_LineString(dpl)));

    fc.add(feat);

    // feat.setRepresentation(new TexturedSurface(feat, TextureManager
    // .textureLoading(folder + "Env3D_86.png"), dpUR.getX()-dpLL.getX(),
    // dpUR.getY()-dpLL.getY()));

    feat.setRepresentation(new TexturedSurface(feat, TextureManager
        .textureLoading(env.folder + "env3DPrj3.png"), dpUR.getX()
        - dpLL.getX(), dpUR.getY() - dpLL.getY()));

    mW.getInterfaceMap3D().getCurrent3DMap()
        .addLayer(new VectorLayer(fc, "Fond"));

  }
}
