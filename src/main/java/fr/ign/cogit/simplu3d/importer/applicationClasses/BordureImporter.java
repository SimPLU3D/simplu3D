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
import fr.ign.cogit.simplu3d.model.application.Bordure;
import fr.ign.cogit.simplu3d.model.application.Parcelle;

/**
 * Assigne les bordures aux parcelles
 * 
 * 3 types de bordure (voirie, fond ou latéral) en fonction du voisinage d'une
 * bo
 * 
 * @author MBrasebin
 * 
 */
public class BordureImporter {

  public static final int UNKNOWN = 99;
  public static final int LATERAL_TEMP = 98;
 // public static IFeatureCollection<IFeature> bordureCalculated = new FT_FeatureCollection<IFeature>();
  
  

  public static IFeatureCollection<Parcelle> assignBordureToParcelleWithOrientation(
      IFeatureCollection<IFeature> parcelCollection, double threshold) {

    System.out.println("NB Parcelles : " + parcelCollection.size());

    // On créer une carte topo avec les parcelles
    CarteTopo cT = newCarteTopo("Parcelles", parcelCollection, 0.1);

    // On parcourt les arcs (futures bordures)
    IPopulation<Arc> arcsParcelles = cT.getPopArcs();

    // Type voirie : elles n'ont pas de voisins
    for (Arc a : arcsParcelles) {
      
      
      
      if(a.longueur() == 0){
          System.out.println();
      }
      
      

      if (a.getFaceDroite() == null || a.getFaceGauche() == null) {
        a.setOrientation(Bordure.VOIE);
        a.setPoids(Bordure.VOIE);

      } else {
        a.setOrientation(UNKNOWN);
        a.setPoids(UNKNOWN);
      }

    }

    List<Arc> lArcLateral = new ArrayList<Arc>();

    // Type latéral, les noeuds débouchent sur une arrete sans parcelle
    for (Arc a : arcsParcelles) {
      


      if (a.getOrientation() == Bordure.VOIE) {

        List<Arc> lA = new ArrayList<Arc>();

        lA.addAll(a.getNoeudIni().getSortants());
        lA.addAll(a.getNoeudFin().getSortants());
        lA.addAll(a.getNoeudIni().getEntrants());
        lA.addAll(a.getNoeudFin().getEntrants());

        for (Arc aTemp : lA) {

          if (aTemp.getOrientation() == Bordure.VOIE) {
            continue;
          }

          aTemp.setOrientation(Bordure.LATERAL);
          aTemp.setPoids(Bordure.LATERAL);
          lArcLateral.add(aTemp);
        }

      }

    }
 
    // On affecte les types voiries et fond
    IPopulation<Face> facesParcelles = cT.getPopFaces();

    for (Face f : facesParcelles) {

      List<Arc> listArc = f.arcs();
      
      

      for (Arc a : listArc) {
        
        
        
        boolean  isFaceADroite = f.getArcsIndirects().contains(a);

        
        if (isFaceADroite   && a.getOrientation() != Bordure.LATERAL) {

          continue;
        }else if(! isFaceADroite && a.getPoids() != Bordure.LATERAL){
          continue;
          
        }

        IMultiCurve<IOrientableCurve> iMC = new GM_MultiCurve<IOrientableCurve>();
        iMC.add(a.getGeometrie());
        
        
        
  
        
        
   
        while (true) {
          
          
       



          // Nous avons un arc latéral, quel est son arc suivant ?
          List<Arc> arcsATraites = new ArrayList<Arc>();
          arcsATraites.addAll(a.getNoeudIni().arcs());
          arcsATraites.addAll(a.getNoeudFin().arcs());

          // On élimine ceux qui ne nous intéressent pas
          for (int i = 0; i < arcsATraites.size(); i++) {
            Arc aTemp = arcsATraites.get(i);
            
            
            isFaceADroite = f.getArcsIndirects().contains(aTemp);
            
            
            if (isFaceADroite   && aTemp.getOrientation() != UNKNOWN) {
              arcsATraites.remove(i);
              i--;
              continue;
            }

            if (  !listArc.contains(aTemp)) {
              arcsATraites.remove(i);
              i--;
              continue;
            }
            
            
            if (! isFaceADroite   && aTemp.getPoids() != UNKNOWN) {
              arcsATraites.remove(i);
              i--;
              continue;
            }


          }

          if (arcsATraites.isEmpty()) {
            break;
          }
                                                         // 1
          if(arcsATraites.size() > 1){

            System.out.println("NB arcs restants : " + arcsATraites.size()); // normalement
                    
          }
          
          
          // seul
                                                                           // ....
          Arc candidat = arcsATraites.get(0);

          isFaceADroite = f.getArcsIndirects().contains(candidat);
          
          iMC.add(candidat.getGeometrie());

          double largeur = 0;
          double area = iMC.convexHull().area();
          if (area > 0.001) {

            IPolygon poly = SmallestSurroundingRectangleComputation.getSSR(iMC);
            double l1 = poly.coord().get(0).distance2D(poly.coord().get(1));
            double l2 = poly.coord().get(1).distance2D(poly.coord().get(2));

            largeur = Math.min(l1, l2);

          }

          if (largeur < threshold) {
            
            
            
            if(isFaceADroite){
              candidat.setOrientation(LATERAL_TEMP);
            }else{
              candidat.setPoids(LATERAL_TEMP);
            }


        
            a = candidat;
            continue;

          }

          break;

        }

      }

    }

    IFeatureCollection<Parcelle> parcelles = new FT_FeatureCollection<Parcelle>();

    // Toutes les arretes sont supposées être affectées à un type
    for (Face f : facesParcelles) {

      IMultiSurface<IOrientableSurface> ms = FromGeomToSurface.convertMSGeom(f
          .getGeom());

      // On a la parcelle
      Parcelle p = new Parcelle(ms);
      parcelles.add(p);

      List<Arc> lArcs = new ArrayList<Arc>();
      lArcs.addAll(f.getArcsDirects());
      lArcs.addAll(f.getArcsIndirects());

      for (Arc a : lArcs) {

        Bordure b = new Bordure(a.getGeom());
        
        
        if(a.getOrientation() == LATERAL_TEMP){
          
          
          b.setTypeDroit(Bordure.LATERAL);
          
          
        }else if(a.getOrientation() == UNKNOWN){
          
          
          b.setTypeDroit(Bordure.FOND);
          
          
        }else{
          
          b.setTypeDroit(a.getOrientation());
          
        }
        
        
        
        
        
        if(a.getPoids() == LATERAL_TEMP){
          
          
          b.setTypeGauche(Bordure.LATERAL);
          
          
        }else if(a.getPoids() == UNKNOWN){
          
          
          b.setTypeGauche(Bordure.FOND);
          
          
        }else{
          
          b.setTypeGauche((int)a.getPoids());
          
        }



        p.getBordures().add(b);

      }

    }
    
    
    
    
    
    /*
    for(Arc a: cT.getPopArcs()){
      
   
      
      IGeometry geom = a.getGeom();
      
      Vecteur v = new Vecteur (geom.coord().get(0), geom.coord().get(geom.coord().size() -1 ));
      v.setZ(0);
      v.normalise();
      
      
      Vecteur vTrans = v.prodVectoriel(new Vecteur(0,0,1));
      double epsilon = 0.5;
      
      vTrans = vTrans.multConstante(epsilon);
System.out.println(vTrans.getX()+ "   " + vTrans.getY());
      IFeature feat1 = new DefaultFeature((IGeometry)geom.translate(vTrans.getX(), vTrans.getY(), vTrans.getZ()).clone());
      AddAttribute.addAttribute(feat1, "T", a.getOrientation(), "Double");
      
      
      IFeature feat2 = new DefaultFeature((IGeometry)geom.translate(-vTrans.getX(), -vTrans.getY(), -vTrans.getZ()).clone());
      AddAttribute.addAttribute(feat2, "T", a.getPoids(), "Double");
      
      bordureCalculated.add(feat1);
      bordureCalculated.add(feat2);
      
    }*/
    
    

    return parcelles;

  }

  public static IFeatureCollection<Parcelle> assignBordureToParcelle(
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
        a.setOrientation(Bordure.VOIE);

      } else {
        a.setOrientation(UNKNOWN);
      }

    }

    // Type latéral, les noeuds débouchent sur une arrete sans parcelle
    for (Arc a : arcsParcelles) {

      if (a.getOrientation() == Bordure.VOIE) {

        List<Arc> lA = new ArrayList<Arc>();

        lA.addAll(a.getNoeudIni().getSortants());
        lA.addAll(a.getNoeudFin().getSortants());
        lA.addAll(a.getNoeudIni().getEntrants());
        lA.addAll(a.getNoeudFin().getEntrants());

        for (Arc aTemp : lA) {

          if (aTemp.getOrientation() == Bordure.VOIE) {
            continue;
          }

          aTemp.setOrientation(Bordure.LATERAL);

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

        if (a.getOrientation() == Bordure.VOIE) {
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

          if (aTemp.getOrientation() == Bordure.VOIE) {
            continue bouclarc;
          }

        }

        a.setOrientation(Bordure.FOND);

      }

    }

    // IFeatureCollection<Bordure> bordures = new
    // FT_FeatureCollection<Bordure>();
    // List<Arc> arcsTreated = new ArrayList<Arc>();
    IFeatureCollection<Parcelle> parcelles = new FT_FeatureCollection<Parcelle>();

    // Toutes les arretes sont supposées être affectées à un type
    for (Face f : facesParcelles) {

      IMultiSurface<IOrientableSurface> ms = FromGeomToSurface.convertMSGeom(f
          .getGeom());

      // On a la parcelle
      Parcelle p = new Parcelle(ms);
      parcelles.add(p);

      List<Arc> lArcs = new ArrayList<Arc>();
      lArcs.addAll(f.getArcsDirects());
      lArcs.addAll(f.getArcsIndirects());

      for (Arc a : lArcs) {

        Bordure b = new Bordure(a.getGeom());
        b.setTypeDroit(a.getOrientation());

        p.getBordures().add(b);

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
          
          if(ls.length() == 0){
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
      
      
      
      if(!test(carteTopo)){
        System.out.println("Error 1");
      }
      
      

      carteTopo.creeNoeudsManquants(-1);
      
      
      if(!test(carteTopo)){
        System.out.println("Error 2");
      }
      
      

      // carteTopo.fusionNoeuds(threshold);

      // carteTopo.filtreArcsDoublons();

      // Création de la topologie Arcs Noeuds

      carteTopo.creeTopologieArcsNoeuds(threshold);
      // La carteTopo est rendue planaire
      
      
      if(!test(carteTopo)){
        System.out.println("Error 3"); 
      }
      

     carteTopo.rendPlanaire(threshold);
      
      
      if(!test(carteTopo)){
        System.out.println("Error 4");
      }
      

      carteTopo.filtreArcsDoublons();
      
      
      
      if(!test(carteTopo)){
        System.out.println("Error 5");
      }
      

      // DEBUG2.addAll(carteTopo.getListeArcs());

      carteTopo.creeTopologieArcsNoeuds(threshold);
      
      
      
      if(!test(carteTopo)){
        System.out.println("Error 6");
      }
      
      // carteTopo.creeTopologieFaces();

    //  carteTopo.filtreNoeudsSimples();

      // Création des faces de la carteTopo
      carteTopo.creeTopologieFaces();

      
      
      if(!test(carteTopo)){
        System.out.println("Error 7");
      }
      
      
      return carteTopo;

    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }
  
  
  
  public static boolean test(CarteTopo ct){
    
    for(Arc a:ct.getPopArcs()){
      if (a.getGeometrie().length() == 0){
        return false;
      }
    
    }
    return true;
  }

}
