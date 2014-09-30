package fr.ign.cogit.simplu3d.io.load.application;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.semantic.DTMArea;
import fr.ign.cogit.geoxygene.sig3d.util.ColorShade;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.simplu3d.model.application.Environnement;

public class LoaderSHP {

  /*
   * Nom des fichiers en entrée
   */
  public final static String NOM_FICHIER_ZONAGE = "zonage.shp";
  public final static String NOM_FICHIER_PARCELLE = "parcelle.shp";
  public final static String NOM_FICHIER_VOIRIE = "route.shp";
  public final static String NOM_FICHIER_BATIMENTS = "bati.shp";
  public final static String NOM_FICHIER_TERRAIN = "MNT_BD3D.asc";
  public final static String NOM_FICHIER_PRESC_LINEAIRE = "PRESCRIPTION_LIN.shp";

  
  /*
   * Attributs du fichier zone
   */

  public static Environnement load(String folder)
      throws CloneNotSupportedException {
    Environnement env = Environnement.getInstance();
    env.folder = folder;

    // Chargement des fichiers

    IFeatureCollection<IFeature> zoneColl = ShapefileReader.read(folder
        + NOM_FICHIER_ZONAGE);
    IFeatureCollection<IFeature> parcelleColl = ShapefileReader.read(folder
        + NOM_FICHIER_PARCELLE);
    IFeatureCollection<IFeature> voirieColl = ShapefileReader.read(folder
        + NOM_FICHIER_VOIRIE);
    IFeatureCollection<IFeature> batiColl = ShapefileReader.read(folder
        + NOM_FICHIER_BATIMENTS);
    IFeatureCollection<IFeature> prescriptions = ShapefileReader.read(folder
        + NOM_FICHIER_PRESC_LINEAIRE);


    // sous-parcelles route sans z, zonage, les bordures etc...
    DTMArea dtm = new DTMArea(folder + NOM_FICHIER_TERRAIN, "Terrain", true, 1,
        ColorShade.BLUE_CYAN_GREEN_YELLOW_WHITE);

    return LoadFromCollection.load(zoneColl, parcelleColl, voirieColl,
        batiColl, prescriptions, folder, dtm);
  }

}
