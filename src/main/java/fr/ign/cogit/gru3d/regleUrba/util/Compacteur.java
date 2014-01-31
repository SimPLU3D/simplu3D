package fr.ign.cogit.gru3d.regleUrba.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;

public class Compacteur {

  public static void main(String[] args) {

    Compacteur
        .compact(
            "C:\\Documents and Settings\\mbrasebin\\Bureau\\DonneesUDMS\\Carte_Volume\\BatiV\\",

            "VolFin.shp");

  }

  /**
   * Permet de réunir les shapefiles d'un répertoire dans un seul shapefile
   * 
   * @param repositoryIni répertoire que l'on scan
   * @param outFileName nom du fichier en sortie
   */
  public static void compact(String repositoryIni, String outFileName) {

    File directoryToScan = new File(repositoryIni);
    File[] lf = directoryToScan.listFiles();

    List<File> fileToLoad = new ArrayList<File>();

    if (lf == null) {
      return;
    }

    int nbFiles = lf.length;

    for (int i = 0; i < nbFiles; i++) {

      File f = lf[i];

      String nom = f.getName();

      int pos = nom.lastIndexOf('.');

      if (pos == -1) {

        continue;
      }

      String extension = nom.substring(pos);

      if (extension.equalsIgnoreCase(".SHP")) {
        fileToLoad.add(f);

      }
    }

    int nbFileToLoad = fileToLoad.size();

    if (nbFileToLoad == 0) {
      return;
    }

    FT_FeatureCollection<IFeature> featColl = new FT_FeatureCollection<IFeature>();

    for (int i = 0; i < nbFileToLoad; i++) {
      featColl
          .addAll(ShapefileReader.read(fileToLoad.get(i).getAbsolutePath()));
    }

    ShapefileWriter.write(featColl, repositoryIni + outFileName);

  }

}
