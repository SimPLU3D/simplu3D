package fr.ign.cogit.misc.cut;

import fr.ign.cogit.contrib.util.DecoupageVecteur;

public class Cut {

  /**
   * @param args
   */
  public static void main(String[] args) {
    
    
    String fileIn = "E:/mbrasebin/Donnees/Strasbourg/GTRU/ProjectT3/BDTopoIni/bati_ini.shp";
    String fileOut = "E:/mbrasebin/Donnees/Strasbourg/GTRU/ProjectT3/BDTopoIni/bati.shp";
    String fileDec = "E:/mbrasebin/Donnees/Strasbourg/GTRU/ProjectT3/zonage.shp";
    
    DecoupageVecteur.decoupe5(fileIn, fileDec, fileOut);
    

  }

}
