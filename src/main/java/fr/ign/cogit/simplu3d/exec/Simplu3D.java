package fr.ign.cogit.simplu3d.exec;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.gui.MainWindow;
import fr.ign.cogit.geoxygene.sig3d.representation.ConstantRepresentation;
import fr.ign.cogit.geoxygene.sig3d.representation.texture.TextureManager;
import fr.ign.cogit.geoxygene.sig3d.representation.texture.TexturedSurface;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.simplu3d.demo.DemoEnvironmentProvider;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.representation.RepEnvironnement;
import fr.ign.cogit.simplu3d.representation.RepEnvironnement.Theme;
import fr.ign.parameters.Parameters;

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
 * 
 *          Viewer avec quelques outils représentant les contraintes d'urbanisme
 * 
 * 
 * 
 */
public class Simplu3D {

  public static boolean DEBUG = false;

  public static final String folder = BasicSimulator.class.getClassLoader()
      .getResource("scenario/").getPath();

  public static IFeatureCollection<IFeature> DEBUG_FEAT = new FT_FeatureCollection<IFeature>();

  public static void main(String[] args) throws Exception {
    ConstantRepresentation.backGroundColor = new Color(156, 180, 193);

    String fileName = "building_parameters_project_expthese_3.xml";

    Parameters p = Parameters.unmarshall(new File(folder + fileName));

    Environnement env =  DemoEnvironmentProvider.getDefaultEnvironment();//; LoaderSHP.load(p.getString("folder"));

    MainWindow mW = new MainWindow();



    represent(folder, env, mW, p);

  }

  private static void represent(String folder, Environnement env,
      MainWindow mW, Parameters p) {
    List<Theme> lTheme = new ArrayList<RepEnvironnement.Theme>();
    lTheme.add(Theme.TOIT_BATIMENT);
    lTheme.add(Theme.FACADE_BATIMENT);
    // lTheme.add(Theme.FAITAGE);
    // lTheme.add(Theme.PIGNON);
    // lTheme.add(Theme.GOUTTIERE);
    // lTheme.add(Theme.VOIRIE);
    lTheme.add(Theme.PARCELLE);
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

    //
    // 1051042.8513268954120576,6840539.0837931865826249 :
    // 1051264.8064121364150196,6840679.2711814027279615
    // Projet 1
    // IDirectPosition dpLL = new
    // DirectPosition(1051042.8513268954120576,6840539.0837931865826249,z);
    // IDirectPosition dpUR = new
    // DirectPosition(1051264.8064121364150196,6840679.2711814027279615,z);

    // Projet 3

    double z = p.getDouble("z");

    double xmin = p.getDouble("xminbg");
    double xmax = p.getDouble("xmaxbg");
    double ymin = p.getDouble("yminbg");
    double ymax = p.getDouble("ymaxbg");

    //
    // 1051042.8513268954120576,6840539.0837931865826249 :
    // 1051264.8064121364150196,6840679.2711814027279615
    // Projet 1
    IDirectPosition dpLL = new DirectPosition(xmin, ymin, z);
    IDirectPosition dpUR = new DirectPosition(xmax, ymax, z);

    // Projet 3
    // IDirectPosition dpLL = new DirectPosition(1051157, 6840727, z);
    // IDirectPosition dpUR = new DirectPosition(1051322, 6840858, z);

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

    // feat.setRepresentation(new TexturedSurface(feat, TextureManager
    // .textureLoading(folder + "Env3D_86.png"), dpUR.getX()-dpLL.getX(),
    // dpUR.getY()-dpLL.getY()));

    String background = p.getString("background_img");

    String filePath = env.folder + background;

    feat.setRepresentation(new TexturedSurface(feat, TextureManager
        .textureLoading(filePath), dpUR.getX() - dpLL.getX(), dpUR.getY()
        - dpLL.getY()));

    fc.add(feat);

    // feat.setRepresentation(new TexturedSurface(feat, TextureManager
    // .textureLoading(folder + "Env3D_86.png"), dpUR.getX()-dpLL.getX(),
    // dpUR.getY()-dpLL.getY()));

    mW.getInterfaceMap3D().getCurrent3DMap()
        .addLayer(new VectorLayer(fc, "Fond"));

  }

}
