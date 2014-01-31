package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.propositionBuilding.export;

import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.Executor;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.schemageo.Batiment;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.schemageo.Parcelle;
import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.sig3d.io.out.image.ExportObjectAsImage;
import fr.ign.cogit.sig3d.io.out.obj.OBJExport;

public class Export {
  
  
  private static String export = "E:/mbrasebin/Donnees/Strasbourg/TestRegles/Test2/Image/";
  public static enum AvailableExport  {IMAGE,SCENE,IMAGE_SCENE, NONE};
  public static AvailableExport doExport = AvailableExport.IMAGE_SCENE;
  public static boolean exportAll = false;
  
  
  private static void exportImg(Parcelle p, String val) {

    ExportObjectAsImage.export(p, export + val + ".png",
        Executor.fen.getInterfaceMap3D(), true);

  }

  private static void exportObj(Parcelle p, String val) {

    OBJExport.export(export + val + ".obj", Executor.fen.getInterfaceMap3D()
        .getCurrent3DMap());

  }

  public static void export(Batiment b, Parcelle p, String c) {
    
    if(doExport == AvailableExport.NONE){
      return;
    }

    IFeatureCollection<IFeature> featC = new FT_FeatureCollection<IFeature>();

    featC.add(b);
    featC.add(b.getToit());

    VectorLayer vl = new VectorLayer(featC, "Bati");
    Executor.fen.getInterfaceMap3D().getCurrent3DMap().addLayer(vl);

    try {
      Thread.sleep(1000);

      System.out.println("J'exporte");

    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    if (doExport == AvailableExport.IMAGE) {
      exportImg(p, c);
    } else if (doExport == AvailableExport.SCENE) {
      exportObj(p,c);
    }else if (doExport == AvailableExport.IMAGE_SCENE) {
      exportObj(p,c);
      exportImg(p, c);
    }



    Executor.fen.getInterfaceMap3D().getCurrent3DMap().removeLayer("Bati");

  }

}
