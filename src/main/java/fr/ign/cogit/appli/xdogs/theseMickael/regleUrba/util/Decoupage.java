package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;

public class Decoupage {

  /**
   * Extrait les entités du fichierADecouper à partir de l'entité dans le
   * fichier fichierDecoupeur. Sauvegarde un .shape en sortie
   * @param fichierADecouper
   * @param fichierDecoupeur
   * @param output
   */
  public static void decoupe(String fichierADecouper, String fichierDecoupeur,
      String output) {

    IFeatureCollection<IFeature> coll1 = ShapefileReader.read(fichierADecouper);

    IFeatureCollection<IFeature> collDecoupeur = ShapefileReader
        .read(fichierDecoupeur);

    IGeometry geomDecoupeuse = collDecoupeur.get(0).getGeom();

    int nbElem = coll1.size();
    FT_FeatureCollection<IFeature> featureCollection = new FT_FeatureCollection<IFeature>();
    featureCollection.setFeatureType(coll1.getFeatureType());

    for (int i = 0; i < nbElem; i++) {

      IFeature feat = coll1.get(i);

      Box3D b = new Box3D(feat.getGeom());

      // GM_Object geom = b.to_2D();

      if (geomDecoupeuse.contains(feat.getGeom())) {
        featureCollection.add(feat);
      }

    }

    ShapefileWriter.write(featureCollection, output);
  }

  public static void decoupe2(String fichierADecouper, String fichierDecoupeur,
      String output) {

    FT_FeatureCollection<IFeature> totalFeature = new FT_FeatureCollection<IFeature>();

    IFeatureCollection<IFeature> coll1 = ShapefileReader.read(fichierADecouper);

    IFeatureCollection<IFeature> collDecoupeur = ShapefileReader
        .read(fichierDecoupeur);

    int nbElem1 = coll1.size();
    int nbElem2 = collDecoupeur.size();

    for (int i = 0; i < nbElem1; i++) {
      IFeature feat1 = coll1.get(i);

      IGeometry geom = feat1.getGeom();

      List<GM_Polygon> lPoly = new ArrayList<GM_Polygon>();

      if (geom instanceof GM_Polygon) {
        lPoly.add((GM_Polygon) geom);
      } else if (geom instanceof GM_MultiSurface<?>) {

        lPoly.addAll(((GM_MultiSurface) geom).getList());
      }

      List<IFeature> featColl = new ArrayList<IFeature>();
      List<Integer> lIn = new ArrayList<Integer>();

      int nbGeom = lPoly.size();
      bouclek: for (int k = 0; k < nbGeom; k++) {
        GM_Polygon surfTemp = lPoly.get(k);
        GM_Point centre = new GM_Point((new Box3D(surfTemp).getCenter()));
        for (int j = 0; j < nbElem2; j++) {

          IGeometry geom2 = collDecoupeur.get(j).getGeom();

          if (!geom2.intersects(centre)) {

            continue;
          }

          int nbInt = lIn.size();

          for (int l = 0; l < nbInt; l++) {
            if (lIn.get(l) == j) {
              IFeature feat = featColl.get(l);

              GM_MultiSurface<GM_Polygon> multiS = (GM_MultiSurface<GM_Polygon>) feat
                  .getGeom();
              multiS.add(surfTemp);
              continue bouclek;
            }

          }
          lIn.add(j);

          GM_MultiSurface<GM_Polygon> multiS = new GM_MultiSurface<GM_Polygon>();
          multiS.add(surfTemp);
          IFeature feat = new DefaultFeature(multiS);

          featColl.add(feat);
          continue bouclek;

        }

      }

      totalFeature.addAll(featColl);

    }
    ShapefileWriter.write(totalFeature, output);
  }

  public static void main(String[] args) {
    String repository = "E:/mbrasebin/Donnees/Strasbourg/Vecteur/IRIS/CONTOURS-IRIS/1_DONNEES_LIVRAISON_2011-06-00249/IRIS_LAMB93_R42_2010/IRIS_LAMB93_D67_2010/";
    String repositoryOut = "E:/mbrasebin/Donnees/Strasbourg/ZoneTest/Vecteur/Iris/";

    List<File> lf = Decoupage.recupSHP(repository);

    int nblF = lf.size();

    String fichierDecoupeur = "E:/mbrasebin/Donnees/Strasbourg/ZoneTest/DecoupZone.shp";

    for (int i = 0; i < nblF; i++) {
      File f = lf.get(i);

      System.out.println(f.getName());

      String fichierADecouper = f.getAbsolutePath();
      String output = repositoryOut + f.getName();

      System.out.println(output);
      Decoupage.decoupe(fichierADecouper, fichierDecoupeur, output);

    }
    System.out.println("C'est fini");

  }

  /**
   * Petite fonction pour récupèrer les shapefils dans les dossiers d'un dossier
   * 
   * @param nomDossier le nom du dossier dans lequel on effectue la recherche
   * @return renvoie la liste des URL fichiers shapes sous forme de chaines de
   *         caractères
   */
  public static List<File> recupSHP(String nomDossier) {

    List<File> formatsDisponibles = new ArrayList<File>();

    File directoryToScan = new File(nomDossier);
    File[] lf = directoryToScan.listFiles();

    if (lf == null) {
      return null;
    }

    int nbFichiers = lf.length;

    for (int i = 0; i < nbFichiers; i++) {

      File dossier = lf[i];

      File[] files = dossier.listFiles();

      int nbFichiersDansDossier = files.length;

      for (int j = 0; j < nbFichiersDansDossier; j++) {

        File f = files[j];

        String nom = f.getName();

        int pos = nom.lastIndexOf('.');

        if (pos == -1) {

          continue;
        }

        String extension = nom.substring(pos);

        if (extension.equalsIgnoreCase(".SHP")) {
          formatsDisponibles.add(f);

        }
      }

    }

    return formatsDisponibles;
  }
}
