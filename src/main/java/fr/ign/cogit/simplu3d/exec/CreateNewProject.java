package fr.ign.cogit.simplu3d.exec;

import fr.ign.cogit.contrib.util.DecoupageVecteur;
import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;

public class CreateNewProject {
  
  
  
  
  
  

  /**
   * @param args
   */
  public static void main(String[] args) {

    //Emplacement du projet et de son empreinte
    String projectFolder = "E:/mbrasebin/Donnees/Strasbourg/GTRU/ProjectT1/";
    String decoupFileName = projectFolder + "decoup.shp";
    
    
    //Fichier à découper
    String parcelFile = "E:/mbrasebin/Donnees/Strasbourg/ZoneTest/Vecteur/Parcelles/parcellefin.shp";
    String roadFile = "E:/mbrasebin/Donnees/Strasbourg/ZoneTest/Vecteur/BDTopo/route_nommee.shp";
    String zonageFile = "E:/mbrasebin/Donnees/Strasbourg/ZoneTest/Raster/POS/zonage.shp";
    
    
    
    //Découpage des fichier
   DecoupageVecteur.decoupe2(parcelFile, decoupFileName, projectFolder + LoaderSHP.NOM_FICHIER_PARCELLE);
   DecoupageVecteur.decoupe5(roadFile, decoupFileName, projectFolder + LoaderSHP.NOM_FICHIER_VOIRIE);
   DecoupageVecteur.decoupe2(zonageFile, decoupFileName, projectFolder + LoaderSHP.NOM_FICHIER_ZONAGE);

  }

}
