package fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.calculation.Proximity;
import fr.ign.cogit.geoxygene.sig3d.representation.basic.Object2d;
import fr.ign.cogit.geoxygene.sig3d.semantic.DTM;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_OrientableSurface;

public class LoadTronconVoieFerree {

  /**
   * Permet d'effectuer le chargement d'une classe de type route
   * @param featCol les entités que l'on souhaite charger
   * @param mnt le MNT (pour plaquer les routes)
   * @return
   */
  public static VectorLayer load(IPopulation<IFeature> featCol, DTM mnt) {
    int nbElem = featCol.size();

    for (int i = 0; i < nbElem; i++) {
      // On récupère les informations relatives à chaque éléments
      IFeature feat = featCol.get(i);
      IGeometry geom = feat.getGeom();

      // Etape 1 : génération de la géométrie

      // On génère les voies parallèles
      int nbVoie = 1;

      Object o = feat.getAttribute("nb_voies");

      if (o == null) {
        o = feat.getAttribute("NB_VOIES");
      }

      if (o != null) {
        nbVoie = Integer.parseInt(o.toString());
      }

      geom = LoadTronconVoieFerree.genereVoie(geom, nbVoie);

      // On applique 3 si la largeur n'est pas indiquée

      String largeurSTR = "";
      o = feat.getAttribute("largeur");

      if (o == null) {
        o = feat.getAttribute("LARGEUR");
      }

      if (o != null) {
        largeurSTR = o.toString();
      }

      // Largeur normale
      double largeur = 1.44 * nbVoie;

      if (largeurSTR.equalsIgnoreCase("large")) {

        largeur = 1.6 * nbVoie;

      } else if (largeurSTR.equalsIgnoreCase("etroite")) {
        largeur = 1.2 * nbVoie;
      }

      // En fonction du franchissement on plaquera ou non les données
      String franchissement = "";
      o = feat.getAttribute("franchisst");

      if (o != null) {
        franchissement = o.toString();
      } else {
        o = feat.getAttribute("FRANCHISST");

        if (o != null) {
          franchissement = o.toString();
        }
      }

      // Il s'agit d'un pont donc franchissement vers le haut (30m max)
      if (franchissement.equalsIgnoreCase("Pont")) {

        geom = LoadTronconVoieFerree.genereTunnel(geom, largeur, 30);

        // Il s'agit d'un tunnel donc franchissement vers le bas (-30m max)
      } else if (franchissement.equalsIgnoreCase("Tunnel")) {

        geom = LoadTronconVoieFerree.genereTunnel(geom, largeur, -30);

      } else {
        // Sinon, c'est une route classique, on plaque sur le terrain
        geom = LoadTronconVoieFerree.transformToSurface(geom, largeur);
        try {
          geom = mnt.mapGeom(geom, 0, true, true);
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

      }

      feat.setGeom(geom);

      // Etape 2 : génération de la représentation

      feat.setRepresentation(new Object2d(feat, Color.black));

    }

    return new VectorLayer(featCol, "Troncon voie ferrée");

  }

  /**
   * Permet de générer un tunnel
   * @param geomIni la géométrie initiale (ligne 3D)
   * @param largeur la largeur de la route
   * @param profondeur la profondeur (positive ou négative du tunnel, peut être
   *          zéro)
   * @return
   */
  @SuppressWarnings("unchecked")
  public static IGeometry genereTunnel(IGeometry geomIni, double largeur,
      double profondeur) {

    List<GM_LineString> gls = new ArrayList<GM_LineString>();

    List<IOrientableSurface> glos = new ArrayList<IOrientableSurface>();

    // On récupère les polylignes)
    if (geomIni instanceof GM_LineString) {

      gls.add((GM_LineString) geomIni);

    } else if (geomIni instanceof GM_MultiCurve<?>) {

      gls.addAll((GM_MultiCurve<GM_LineString>) geomIni);
    } else {

      //Autre classe de géométrie
      return geomIni;
    }

    int nbCurve = gls.size();
    // On traite chaque polyligne séparément
    for (int i = 0; i < nbCurve; i++) {
      GM_LineString ls = gls.get(i);

      // On affecte un nouveau Z à chaque sommet
      IDirectPositionList dpl = ls.coord();

      int nbP = dpl.size();

      double zIni = dpl.get(0).getZ();
      double zFin = dpl.get(nbP - 1).getZ();
      double distance = dpl.get(0).distance(dpl.get(nbP - 1));

      for (int j = 0; j < nbP; j++) {

        IDirectPosition dp = dpl.get(j);

        double distTemp = dp.distance(dpl.get(0));

        // Pour obtenir le z on a :
        // Une partie linéaire en fonction de la distance (raccord aux
        // extrémités)
        // Une partie carré pour la profondeur

        dp.setZ(zIni + (zFin - zIni) * (distTemp / distance)
            + (distTemp / distance) * (1 - distTemp / distance) * profondeur);

      }

      // On génère un buffer pour une route surfacique
      IOrientableSurface geom = (GM_OrientableSurface) ls.buffer(largeur);

      IDirectPositionList dplFin = geom.coord();

      int nbPFin = dplFin.size();

      // On affecte à chaque point du buffer le Z du point de la ligne initiale
      // la plus proche
      Proximity prox = new Proximity();

      for (int j = 0; j < nbPFin; j++) {
        IDirectPosition dpTemp = dplFin.get(j);

        IDirectPosition nearest = prox.nearest(dpTemp, dpl);

        dpTemp.setZ(nearest.getZ());

      }

      glos.add(geom);

    }

    // On renvoie les géométrie
    if (glos.size() == 1) {

      return glos.get(0);
    }

    return new GM_MultiSurface<IOrientableSurface>(glos);
  }

  /**
   * Permet de générer une route (pour l'instant juste un buffer appliqué à la
   * géométrie initiale
   * @param geomIni
   * @param largeur
   * @return
   */
  public static IGeometry transformToSurface(IGeometry geomIni, double largeur) {

    return geomIni.buffer(largeur);

  }

  /**
   * A compléter
   * 
   * @param geom
   * @param nbVoie
   * @return
   */
  public static IGeometry genereVoie(IGeometry geom, int nbVoie) {

    return geom;
  }

}
