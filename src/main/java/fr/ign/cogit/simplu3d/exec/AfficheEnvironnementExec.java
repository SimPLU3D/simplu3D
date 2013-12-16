package fr.ign.cogit.simplu3d.exec;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.representation.ConstantRepresentation;
import fr.ign.cogit.geoxygene.sig3d.representation.basic.Object1d;
import fr.ign.cogit.geoxygene.sig3d.representation.texture.TextureManager;
import fr.ign.cogit.geoxygene.sig3d.representation.texture.TexturedSurface;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.sig3d.COGITLauncher3D;
import fr.ign.cogit.simplu3d.gui.button.GTRUToolBar;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.representation.RepEnvironnement;
import fr.ign.cogit.simplu3d.representation.RepEnvironnement.Theme;
import fr.ign.parameters.Parameters;

public class AfficheEnvironnementExec {

  public static void main(String[] args) throws CloneNotSupportedException {

    Object1d.width = 4.0f;

    ConstantRepresentation.backGroundColor = new Color(156, 180, 193);

    String folderName = "./src/main/resources/scenario/";

    String fileName = "building_parameters_project_expthese_3.xml";

    Parameters p = initialize_parameters(folderName + fileName);

    String folder = p.get("folder");

    Environnement env = LoaderSHP.load(folder);

    List<Theme> lTheme = new ArrayList<RepEnvironnement.Theme>();
    // lTheme.add(Theme.TOIT_BATIMENT);
    // lTheme.add(Theme.FACADE_BATIMENT);
    // lTheme.add(Theme.FAITAGE);
    // lTheme.add(Theme.PIGNON);
    // lTheme.add(Theme.GOUTTIERE);
   lTheme.add(Theme.VOIRIE);
    // lTheme.add(Theme.PARCELLE);
    // lTheme.add(Theme.SOUS_PARCELLE);
    // lTheme.add(Theme.ZONE);
    // lTheme.add(Theme.PAN);
    // lTheme.add(Theme.PAN_MUR);
   // lTheme.add(Theme.BORDURE);

    Theme[] tab = lTheme.toArray(new Theme[0]);

    List<VectorLayer> vl = RepEnvironnement.represent(env, tab);

    COGITLauncher3D mW = new COGITLauncher3D();

    for (VectorLayer l : vl) {

      mW.getInterfaceMap3D().getCurrent3DMap().addLayer(l);
    }

    mW.getMainMenuBar().add(new GTRUToolBar(mW));

    if (!Boolean.parseBoolean(p.get("showbackground"))) {
      return;
    }

    double z = Double.parseDouble(p.get("z"));

    double xmin = Double.parseDouble(p.get("xminbg"));
    double xmax = Double.parseDouble(p.get("xmaxbg"));
    double ymin = Double.parseDouble(p.get("yminbg"));
    double ymax = Double.parseDouble(p.get("ymaxbg"));

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

    fc.add(feat);

    // feat.setRepresentation(new TexturedSurface(feat, TextureManager
    // .textureLoading(folder + "Env3D_86.png"), dpUR.getX()-dpLL.getX(),
    // dpUR.getY()-dpLL.getY()));

    feat.setRepresentation(new TexturedSurface(feat, TextureManager
        .textureLoading(env.folder + "background3D.png"), dpUR.getX()
        - dpLL.getX(), dpUR.getY() - dpLL.getY()));
    mW.getInterfaceMap3D().getCurrent3DMap()
        .addLayer(new VectorLayer(fc, "Fond"));
  }

  private static Parameters initialize_parameters(String name) {
    return Parameters.unmarshall(name);
  }

}
