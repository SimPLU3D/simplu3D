package fr.ign.cogit.simplu3d.importer.applicationClasses;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Arc;
import fr.ign.cogit.geoxygene.contrib.cartetopo.CarteTopo;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Face;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.util.algo.SmallestSurroundingRectangleComputation;
import fr.ign.cogit.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.sig3d.convert.geom.FromPolygonToLineString;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;

/**
 * Assigne les bordures aux parcelles
 * 
 * 3 types de bordure (voirie, fond ou latéral) en fonction du voisinage d'une
 * bo
 * 
 * @author MBrasebin
 * 
 */
public class CadastralParcelLoader {

  public static final int UNKNOWN = 99;
  public static final int LATERAL_TEMP = 98;

  public static IFeatureCollection<CadastralParcel> assignBordureToParcelleWithOrientation(
      IFeatureCollection<IFeature> parcelCollection, double threshold) {

    System.out.println("NB Parcelles : " + parcelCollection.size());

    // On créer une carte topo avec les parcelles
    CarteTopo cT = newCarteTopo("Parcelles", parcelCollection, 0.1);

    // On parcourt les arcs (futures limites de parcelles)
    IPopulation<Arc> arcsParcelles = cT.getPopArcs();

    // Type voirie : elles n'ont qu'un voisin
    for (Arc a : arcsParcelles) {

      if (a.longueur() == 0) {
        continue;
      }
      if (a.getFaceDroite() == null || a.getFaceGauche() == null) {
        a.setOrientation(SpecificCadastralBoundary.ROAD);

      } else {
        a.setOrientation(UNKNOWN);

      }

    }

    List<Arc> lArcLateral = new ArrayList<Arc>();

    // Type latéral, les noeuds débouchent sur une arete sans parcelle
    for (Arc a : arcsParcelles) {

      if (a.getOrientation() == SpecificCadastralBoundary.ROAD) {

        List<Arc> lA = new ArrayList<Arc>();

        lA.addAll(a.getNoeudIni().getSortants());
        lA.addAll(a.getNoeudFin().getSortants());
        lA.addAll(a.getNoeudIni().getEntrants());
        lA.addAll(a.getNoeudFin().getEntrants());

        for (Arc aTemp : lA) {

          if (aTemp.getOrientation() == SpecificCadastralBoundary.ROAD) {
            continue;
          }

          aTemp.setOrientation(SpecificCadastralBoundary.LAT);

          lArcLateral.add(aTemp);
        }

      }

    }

    IPopulation<Face> facesParcelles = cT.getPopFaces();

    IFeatureCollection<CadastralParcel> parcelles = new FT_FeatureCollection<CadastralParcel>();

    for (Face f : facesParcelles) {

      IMultiSurface<IOrientableSurface> ms = FromGeomToSurface.convertMSGeom(f
          .getGeom());

      // On a la parcelle
      CadastralParcel p = new CadastralParcel(ms);
      parcelles.add(p);

      List<Arc> listArc = new ArrayList<Arc>();
      listArc.addAll(f.arcs());

      List<Arc> listArcLat = new ArrayList<Arc>();

      // On classe les arcs
      for (Arc a : listArc) {

        System.out.println("Classement des arcs");

        // C'est une voie on le garde
        if (a.getOrientation() == SpecificCadastralBoundary.ROAD) {
          continue;
        }

        if (a.getOrientation() != SpecificCadastralBoundary.LAT) {
          continue;
        }

        // On a un arc latéral
        listArcLat.add(a);

        while (true) {
          List<Arc> arcsATraites = new ArrayList<Arc>();
          arcsATraites.addAll(a.getNoeudIni().arcs());
          arcsATraites.addAll(a.getNoeudFin().arcs());

          IMultiCurve<IOrientableCurve> iMC = new GM_MultiCurve<IOrientableCurve>();
          iMC.add(a.getGeometrie());

          // On élimine les arcs que l'on ne traitera pas
          for (int i = 0; i < arcsATraites.size(); i++) {

            Arc aTemp = arcsATraites.get(i);

            // déjà typé, on ne le traite pas
            if (aTemp.getOrientation() != UNKNOWN) {
              arcsATraites.remove(i);
              i--;
              continue;
            }

            // Pas un arc de la face en cours, on ne le traite pas
            boolean isArc = f.getArcsDirects().contains(aTemp)
                || f.getArcsIndirects().contains(aTemp);

            if (!isArc) {
              arcsATraites.remove(i);
              i--;
              continue;
            }

            // Dans une des listes, on ne le traite pas
            if (listArcLat.contains(aTemp)) {
              arcsATraites.remove(i);
              i--;
              continue;
            }

          }

          if (arcsATraites.isEmpty()) {
            break;
          }

          if (arcsATraites.size() > 1) {
            System.out.println("> 1, il doit y avoir un bug");
          }

          // Nous n'avons qu'un candidat ... normalement
          Arc aCandidat = arcsATraites.remove(0);
          iMC.add(aCandidat.getGeometrie());

          double largeur = 0;
          double area = iMC.convexHull().area();
          if (area > 0.001) {

            IPolygon poly = SmallestSurroundingRectangleComputation.getSSR(iMC);
            double l1 = poly.coord().get(0).distance2D(poly.coord().get(1));
            double l2 = poly.coord().get(1).distance2D(poly.coord().get(2));

            largeur = Math.min(l1, l2);

          }

          if (largeur < threshold) {

            a = aCandidat;

            // Le candidat est une limite latérale

            listArcLat.add(aCandidat);
          } else {

            // Le candidat est un fond de parcelle
            break;

          }

        }// Fin while

      }// Fin de boucle sur les arcs

      // Tous les arcs ont été mis dans une liste sauf les fonds de parcelle
      for (Arc a : listArc) {
        SpecificCadastralBoundary cB = new SpecificCadastralBoundary(
            a.getGeom());
        p.getSpecificCadastralBoundary().add(cB);

        if (a.getOrientation() == SpecificCadastralBoundary.ROAD) {
          cB.setType(SpecificCadastralBoundary.ROAD);
          continue;
        }

        if (listArcLat.contains(a)) {
          cB.setType(SpecificCadastralBoundary.LAT);

        } else {

          cB.setType(SpecificCadastralBoundary.BOT);

        }

      }

    }// Fin de boucle sur les faces

    // On affecte pour les bordures de type BOT et LAT les parcelles voisines
    // aux limites
    int nbElem = facesParcelles.size();

    for (int i = 0; i < nbElem; i++) {

      Face f = facesParcelles.get(i);
      CadastralParcel parc = parcelles.get(i);

      
      List<Arc> lArc = f.arcs();
      int nbArcs = lArc.size();
      
      for (int j = 0 ; j <nbArcs ; j++) {
        
        Arc a = lArc.get(j);
        SpecificCadastralBoundary sCB = parc.getSpecificCadastralBoundary().get(j);
        
        
       
        if(sCB.getType() == SpecificCadastralBoundary.ROAD){
          continue;
              
        }
        
        Face fCand = a.getFaceDroite();
      
        if(fCand == f){
          fCand = a.getFaceGauche();
        }
       
        int indexFCand = facesParcelles.getElements().indexOf(fCand);
        sCB.setFeatAdj(parcelles.get(indexFCand));

        
      }

    }

    return parcelles;

  }

  @Deprecated
  public static IFeatureCollection<CadastralParcel> assignBordureToParcelle(
      IFeatureCollection<IFeature> parcelCollection) {

    System.out.println("NB Parcelles : " + parcelCollection.size());

    // On créer une carte topo avec les parcelles
    CarteTopo cT = newCarteTopo("Parcelles", parcelCollection, 0.1);

    System.out.println("NB faces : " + cT.getPopFaces().size());

    // On parcourt les arcs (futures bordures)
    IPopulation<Arc> arcsParcelles = cT.getPopArcs();

    // Type voirie : elles n'ont pas de voisins
    for (Arc a : arcsParcelles) {

      if (a.getFaceDroite() == null || a.getFaceGauche() == null) {
        a.setOrientation(SpecificCadastralBoundary.ROAD);

      } else {
        a.setOrientation(UNKNOWN);
      }

    }

    // Type latéral, les noeuds débouchent sur une arrete sans parcelle
    for (Arc a : arcsParcelles) {

      if (a.getOrientation() == SpecificCadastralBoundary.ROAD) {

        List<Arc> lA = new ArrayList<Arc>();

        lA.addAll(a.getNoeudIni().getSortants());
        lA.addAll(a.getNoeudFin().getSortants());
        lA.addAll(a.getNoeudIni().getEntrants());
        lA.addAll(a.getNoeudFin().getEntrants());

        for (Arc aTemp : lA) {

          if (aTemp.getOrientation() == SpecificCadastralBoundary.ROAD) {
            continue;
          }

          aTemp.setOrientation(SpecificCadastralBoundary.LAT);

        }

      }

    }

    // On affecte les types voiries et fond
    IPopulation<Face> facesParcelles = cT.getPopFaces();

    boucleFace: for (Face f : facesParcelles) {
      List<Arc> lA = new ArrayList<Arc>();

      lA.addAll(f.getArcsDirects());
      lA.addAll(f.getArcsIndirects());

      for (Arc a : lA) {

        if (a.getOrientation() == SpecificCadastralBoundary.ROAD) {
          continue boucleFace;
        }

      }

      bouclarc: for (Arc a : lA) {

        List<Arc> lATemp = new ArrayList<Arc>();

        lATemp.addAll(a.getNoeudIni().getSortants());
        lATemp.addAll(a.getNoeudFin().getSortants());
        lATemp.addAll(a.getNoeudIni().getEntrants());
        lATemp.addAll(a.getNoeudFin().getEntrants());

        for (Arc aTemp : lATemp) {

          if (aTemp.getOrientation() == SpecificCadastralBoundary.ROAD) {
            continue bouclarc;
          }

        }

        a.setOrientation(SpecificCadastralBoundary.BOT);

      }

    }

    // IFeatureCollection<Bordure> bordures = new
    // FT_FeatureCollection<Bordure>();
    // List<Arc> arcsTreated = new ArrayList<Arc>();
    IFeatureCollection<CadastralParcel> parcelles = new FT_FeatureCollection<CadastralParcel>();

    // Toutes les arretes sont supposées être affectées à un type
    for (Face f : facesParcelles) {

      IMultiSurface<IOrientableSurface> ms = FromGeomToSurface.convertMSGeom(f
          .getGeom());

      // On a la parcelle
      CadastralParcel p = new CadastralParcel(ms);
      parcelles.add(p);

      List<Arc> lArcs = new ArrayList<Arc>();
      lArcs.addAll(f.getArcsDirects());
      lArcs.addAll(f.getArcsIndirects());

      for (Arc a : lArcs) {

        SpecificCadastralBoundary b = new SpecificCadastralBoundary(a.getGeom());
        b.setType(a.getOrientation());

        p.getSpecificCadastralBoundary().add(b);

      }

    }

    return parcelles;

  }

  public static CarteTopo newCarteTopo(String name,
      IFeatureCollection<? extends IFeature> collection, double threshold) {

    try {
      // Initialisation d'une nouvelle CarteTopo
      CarteTopo carteTopo = new CarteTopo(name);
      carteTopo.setBuildInfiniteFace(false);
      // Récupération des arcs de la carteTopo
      IPopulation<Arc> arcs = carteTopo.getPopArcs();
      // Import des arcs de la collection dans la carteTopo
      for (IFeature feature : collection) {

        List<ILineString> lLLS = FromPolygonToLineString
            .convertPolToLineStrings((IPolygon) FromGeomToSurface.convertGeom(
                feature.getGeom()).get(0));

        for (ILineString ls : lLLS) {

          if (ls.length() == 0) {
            System.out.println("PROOOOOOOOOOOO");
          }

          // création d'un nouvel élément
          Arc arc = arcs.nouvelElement();
          // affectation de la géométrie de l'objet issu de la collection
          // à l'arc de la carteTopo
          arc.setGeometrie(ls);
          // instanciation de la relation entre l'arc créé et l'objet
          // issu de la collection
          arc.addCorrespondant(feature);

        }

      }

      if (!test(carteTopo)) {
        System.out.println("Error 1");
      }

      carteTopo.creeNoeudsManquants(-1);

      if (!test(carteTopo)) {
        System.out.println("Error 2");
      }

      // carteTopo.fusionNoeuds(threshold);

      // carteTopo.filtreArcsDoublons();

      // Création de la topologie Arcs Noeuds

      carteTopo.creeTopologieArcsNoeuds(threshold);
      // La carteTopo est rendue planaire

      if (!test(carteTopo)) {
        System.out.println("Error 3");
      }

      carteTopo.rendPlanaire(threshold);

      if (!test(carteTopo)) {
        System.out.println("Error 4");
      }

      carteTopo.filtreArcsDoublons();

      if (!test(carteTopo)) {
        System.out.println("Error 5");
      }

      // DEBUG2.addAll(carteTopo.getListeArcs());

      carteTopo.creeTopologieArcsNoeuds(threshold);

      if (!test(carteTopo)) {
        System.out.println("Error 6");
      }

      // carteTopo.creeTopologieFaces();

      // carteTopo.filtreNoeudsSimples();

      // Création des faces de la carteTopo
      carteTopo.creeTopologieFaces();

      if (!test(carteTopo)) {
        System.out.println("Error 7");
      }

      return carteTopo;

    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public static boolean test(CarteTopo ct) {

    for (Arc a : ct.getPopArcs()) {
      if (a.getGeometrie().length() == 0) {
        return false;
      }

    }
    return true;
  }

}
