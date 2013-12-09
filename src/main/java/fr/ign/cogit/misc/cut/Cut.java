package fr.ign.cogit.misc.cut;

import fr.ign.cogit.contrib.util.DecoupageVecteur;

public class Cut {

  /**
   * @param args
   */
  public static void main(String[] args) {
    
    
    String fileIn = "E:/mbrasebin/Donnees/Strasbourg/ZoneTest/Experimentation/TestSensibilitePos/donnees/bati.shp";
    String fileOut = "E:/mbrasebin/Donnees/Strasbourg/ZoneTest/Experimentation/CompareSHON/Donnees/batiBDTOPO.shp";
    String fileDec = "E:/mbrasebin/Donnees/Strasbourg/ZoneTest/Experimentation/CompareSHON/Donnees/decoup.shp";
    
    DecoupageVecteur.decoupe5(fileIn, fileDec, fileOut);
    

  }

}
