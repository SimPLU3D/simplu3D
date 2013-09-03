package fr.ign.cogit.simplu3d.importer.applicationClasses;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Arc;
import fr.ign.cogit.geoxygene.contrib.cartetopo.CarteTopo;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Groupe;
import fr.ign.cogit.geoxygene.contrib.geometrie.Operateurs;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromPolygonToLineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.RoofSurface;

public class EmpriseGenerator {

  public static IPolygon convert(Building b) {

    return convert(b.getToit());

  }

  public static IPolygon convert(RoofSurface t) {

    return convert(t.getGeom());

  }

  public static IPolygon convert(IGeometry geomIni) {

    List<IPolygon> lPol = new ArrayList<IPolygon>();

    // On créer la carte topo

    CarteTopo cT = newCarteTopo("Toto",
        FromPolygonToLineString.convertListPolToLineStrings(FromGeomToSurface
            .convertGeom(geomIni)), 0.1);

    Groupe gr = cT.getPopGroupes().nouvelElement();
    gr.setListeArcs(cT.getListeArcs());
    gr.setListeFaces(cT.getListeFaces());
    gr.setListeNoeuds(cT.getListeNoeuds());

    List<Groupe> lG = gr.decomposeConnexes();

    for (Groupe g : lG) {

      List<Arc> lArcs = g.getListeArcs();

      List<ILineString> bordBatiment = new ArrayList<ILineString>();

      for (Arc a : lArcs) {

        if (a.getFaceDroite() == null || a.getFaceGauche() == null) {
          bordBatiment.add(a.getGeometrie());

        }

      }

      ILineString lS = Operateurs.union(bordBatiment);

      if (lS == null) {
        System.out.println("L'union des lignes extérieures est nulle");
        return null;
      }
      IDirectPositionList dplTemp = lS.coord();
      if (dplTemp.size() <= 3) {
        System.out.println("L'emprise du bâtiment a moins de 3 sommets");
        return null;
      }

      if (!dplTemp.get(0).equals(dplTemp.get(dplTemp.size() - 1), 0.1)) {

        System.out.println("Problem");

        return null;

      }

      lPol.add(new GM_Polygon(lS));

    }

    if (lPol.size() == 1) {
      return lPol.get(0);
    }

    IGeometry geom = lPol.get(0).buffer(1);

    int nbPol = lPol.size();

    for (int i = 1; i < nbPol; i++) {

      geom = lPol.get(i).buffer(1).union(geom);

    }

    geom = geom.buffer(-1);

    return convert(geom);

  }

  public static CarteTopo newCarteTopo(String name, List<ILineString> lLLS,
      double threshold) {

    try {
      // Initialisation d'une nouvelle CarteTopo
      CarteTopo carteTopo = new CarteTopo(name);
      carteTopo.setBuildInfiniteFace(false);
      // Récupération des arcs de la carteTopo
      IPopulation<Arc> arcs = carteTopo.getPopArcs();
      // Import des arcs de la collection dans la carteTopo

      for (ILineString ls : lLLS) {
        // création d'un nouvel élément
        Arc arc = arcs.nouvelElement();
        // affectation de la géométrie de l'objet issu de la collection
        // à l'arc de la carteTopo
        arc.setGeometrie(ls);
        // instanciation de la relation entre l'arc créé et l'objet
        // issu de la collection
        // arc.addCorrespondant(feature);

      }

      carteTopo.creeNoeudsManquants(0.1);

      // carteTopo.fusionNoeuds(threshold);

      // carteTopo.filtreArcsDoublons();

      // Création de la topologie Arcs Noeuds

      carteTopo.creeTopologieArcsNoeuds(threshold);
      // La carteTopo est rendue planaire

      carteTopo.rendPlanaire(threshold);

      carteTopo.filtreArcsDoublons();

      // DEBUG2.addAll(carteTopo.getListeArcs());

      // carteTopo.creeTopologieFaces();
      carteTopo.creeTopologieArcsNoeuds(threshold);

      // Création des faces de la carteTopo
      carteTopo.creeTopologieFaces();

      return carteTopo;

    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

}
