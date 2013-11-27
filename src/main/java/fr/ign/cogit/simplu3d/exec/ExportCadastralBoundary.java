package fr.ign.cogit.simplu3d.exec;

import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.Environnement;

public class ExportCadastralBoundary {

  /**
   * @param args
   */
  public static void main(String[] args) {

      String folder = "E:/mbrasebin/Donnees/Strasbourg/GTRU/Project4/";
//      String folderOut = "E:/mbrasebin/Donnees/Strasbourg/GTRU/Project4/out/";
      Environnement env = null;
      try {
         env = LoaderSHP.load(folder);
      } catch (CloneNotSupportedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      if(env == null){
        System.exit(0);
      }
      
      
      
      
      

  }

}
