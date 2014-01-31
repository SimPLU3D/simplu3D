package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.Antecedent;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.Consequence;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.ConsequenceHauteur;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.ContrainteHauteur;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.DifferenceHauteur;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.DistanceEuclidienne;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.DistanceFHauteur;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.Recul;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.ReculAutreBatiment;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.ReculBordure;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.ReculRoute;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.Regle;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.RouteBordante;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.representation.Contrainte;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.representation.Incoherence;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.schemageo.Batiment;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.schemageo.EnveloppeConstructible;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.schemageo.Parcelle;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.schemageo.Route;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.util.ExtrusionTriangulation;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.util.Prospect;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.calculation.BooleanOperators;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSolid;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_OrientableSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Solid;

/**
 * Classe permettant à partir d'un environnement : - d'établir des contraintes
 * géométriques - de vérifier les contraintes
 * 
 * @author MBrasebin
 */
public class Moteur {

  private Environnement env;
  private List<Regle> lRegles;

  /**
   * On prépare les paramètres
   * 
   * @param env
   * @param lRegles
   */
  public Moteur(Environnement env, List<Regle> lRegles) {

    this.env = env;
    this.lRegles = lRegles;
  }

  public List<FT_FeatureCollection<Incoherence>> processIsParcelOkAll() {
    return this.processIsParcelOk(this.env.getlParcelles().getElements());
  }

  /**
   * Indique pour chaque parcelle si tout est ok
   */
  public List<FT_FeatureCollection<Incoherence>> processIsParcelOk(
      List<Parcelle> lParcelles) { // On récupère le nombre de règles et
    // de parcelles
    int nbParcelles = lParcelles.size();
    int nbRegles = this.lRegles.size();

    List<FT_FeatureCollection<Incoherence>> lIncoherencesTotales = new ArrayList<FT_FeatureCollection<Incoherence>>();

    // On vérifie pour chaque parcelle si les conditions sont ok
    for (int i = 0; i < nbParcelles; i++) {
      Parcelle p = lParcelles.get(i);

      FT_FeatureCollection<Incoherence> lIncoTemp = new FT_FeatureCollection<Incoherence>();

      if (Executor.VERBOSE) {
        System.out.println("**********************");
        System.out.println("Parcelle numero " + i);
      }

      // On teste chaque règle
      boucleregle: for (int j = 0; j < nbRegles; j++) {

        Regle r = this.lRegles.get(j);

        List<Route> lRoutes = new ArrayList<Route>();

        boolean isRouteBordante = false;
        // Si des antécédants existent on vérifie qu'ils sont ok
        List<Antecedent> lAntecedants = r.getAntecedent();
        int nbAntecedants = lAntecedants.size();
        for (int k = 0; k < nbAntecedants; k++) {
          Antecedent ant = lAntecedants.get(k);
          // L'antécédent est il vérifié
          if (!ant.isAntecedantChecked(p)) {
            // C'est non, la règle ne s'applique pas
            // On passe à la règle suivante
            continue boucleregle;

          }

          if (ant instanceof RouteBordante) {

            lRoutes.addAll(((RouteBordante) ant).getRoutesOk());
            isRouteBordante = true;
          }

          // Si oui,on vérifie les autres
        }

        // Nous avons vérifié les antécédents si ils existents
        // Vérifions maintenant les conséquences
        List<Consequence> lConsequences = r.getConsequence();

        int nbConsequences = lConsequences.size();
        for (int k = 0; k < nbConsequences; k++) {
          Consequence cons = lConsequences.get(k);

          // nombre d'incohérences actuelles
          int nbInco = lIncoTemp.size();

          if (cons instanceof ReculRoute) {

            if (lRoutes.size() == 0 && !isRouteBordante) {
              lRoutes = p.getlRouteBordante();
            }

            int nbRoutes = lRoutes.size();

            for (int l = 0; l < nbRoutes; l++) {

              lIncoTemp.addAll(((ReculRoute) cons).isConsequenceChecked(p,
                  lRoutes.get(l), true));
            }

          } else {

            lIncoTemp.addAll(cons.isConsequenceChecked(p, true));

            // Si oui,on vérifie les autres
          }

          // C'est non, la règle n'est pas respectée
          // On passe à la parcelle suivante

          if (lIncoTemp.size() == nbInco) {

            if (Executor.VERBOSE) {
              System.out.println("Règle respectée, parcelle " + i);
              System.out.println("Fin parcelle numero " + i);
              System.out.println("**********************");

            }
          } else {

            if (Executor.VERBOSE) {
              System.out.println("Règle non respectée, parcelle " + i);
              System.out.println("Fin parcelle numero " + i);
              System.out.println("**********************");

            }

          }

        }

      }

      lIncoherencesTotales.add(lIncoTemp);
    }
    return lIncoherencesTotales;
  }

  public boolean processIsParceFastlOk(Parcelle p) { // On récupère le nombre de
                                                     // règles et
    // de parcelles

    int nbRegles = this.lRegles.size();

    // On vérifie pour chaque parcelle si les conditions sont ok

    // On teste chaque règle
    boucleregle: for (int j = 0; j < nbRegles; j++) {

      Regle r = this.lRegles.get(j);

      List<Route> lRoutes = new ArrayList<Route>();

      boolean isRouteBordante = false;
      // Si des antécédants existent on vérifie qu'ils sont ok
      List<Antecedent> lAntecedants = r.getAntecedent();
      int nbAntecedants = lAntecedants.size();
      for (int k = 0; k < nbAntecedants; k++) {
        Antecedent ant = lAntecedants.get(k);
        // L'antécédent est il vérifiéEmpty
        if (!ant.isAntecedantChecked(p)) {
          // C'est non, la règle ne s'applique pas
          // On passe à la règle suivante
          continue boucleregle;

        }

        if (ant instanceof RouteBordante) {

          lRoutes.addAll(((RouteBordante) ant).getRoutesOk());
          isRouteBordante = true;
        }

        // Si oui,on vérifie les autres
      }

      // Nous avons vérifié les antécédents si ils existents
      // Vérifions maintenant les conséquences
      List<Consequence> lConsequences = r.getConsequence();

      int nbConsequences = lConsequences.size();
      for (int k = 0; k < nbConsequences; k++) {
        Consequence cons = lConsequences.get(k);

        if (cons instanceof ReculRoute) {

          if (lRoutes.size() == 0 && !isRouteBordante) {
            lRoutes = p.getlRouteBordante();
          }

          int nbRoutes = lRoutes.size();

          for (int l = 0; l < nbRoutes; l++) {
            if (!(((ReculRoute) cons).isConsequenceChecked(p, lRoutes.get(l),
                false).isEmpty())) {
              return false;
            }

          }

        } else {

          if (!cons.isConsequenceChecked(p, false).isEmpty()) {
            return false;
          }

          // Si oui,on vérifie les autres
        }

        // C'est non, la règle n'est pas respectée
        // On passe à la parcelle suivante

      }

    }

    return true;
  }

  /**
   * Construit pour une parcelle les enveloppes constructibles à partir des
   * règles de l'environnement du moteur et des informations incluses dans cette
   * environnement
   * 
   * @param p une parcelle de l'environnement
   * @return une liste de FT_Feature qui décrit l'enveloppe constructible
   */
  public IFeatureCollection<EnveloppeConstructible> computeBuildableEnvelopes(
      Parcelle p) {

    p.setlEnveloppeContenues(new ArrayList<EnveloppeConstructible>());
    List<FT_FeatureCollection<Contrainte>> lLContrainte = this
        .computeConstraints(p);

    IFeatureCollection<EnveloppeConstructible> ftColl = new FT_FeatureCollection<EnveloppeConstructible>();

    if (lLContrainte == null) {
      return ftColl;
    }

    int nbLContraintes = lLContrainte.size();

    System.out.println("NB Contraintes " + nbLContraintes);

    for (int i = 0; i < nbLContraintes; i++) {

      FT_FeatureCollection<Contrainte> collContraintes = lLContrainte.get(i);

      if (i == 0) {
        // collContraintes.remove(1);
        // collContraintes.remove(2);
      }

      System.out.println("Solide n°" + i);

      IFeatureCollection<EnveloppeConstructible> iFEC = this
          .computeGeomFromConstraints(collContraintes);
      ftColl.addAll(iFEC);
      p.getlEnveloppeContenues().addAll(iFEC);

    }

    return ftColl;
  }

  /**
   * A partir d'une liste de contraintes en calcule la géométrie finale sous la
   * forme d'un batîment et d'un toit
   * 
   * @param collContraintes
   * @return
   */
  public IFeatureCollection<EnveloppeConstructible> computeGeomFromConstraints(
      IFeatureCollection<Contrainte> collContraintes) {

    if (collContraintes == null) {
      return null;
    }
    int nbContraintesCalculees = collContraintes.size();

    FT_FeatureCollection<EnveloppeConstructible> enveloppCalculee = new FT_FeatureCollection<EnveloppeConstructible>();

    GM_Solid solidIni = (GM_Solid) collContraintes.get(0).getGeom();

    for (int j = 1; j < nbContraintesCalculees; j++) {

      IGeometry geom = collContraintes.get(j).getGeom();

      if (geom instanceof GM_Solid) {

        try {
          solidIni = BooleanOperators.compute(new DefaultFeature(geom),
              new DefaultFeature(solidIni), BooleanOperators.INTERSECTION);
        } catch (Exception ex) {
          ex.printStackTrace();
          System.out.println("Erreur calcul intersection");
          break;
        }
      }

    }

    if (solidIni != null) {

      // Petit camouflage pour l'aspect esthétique ...
      // Tetraedrisation tet = new Tetraedrisation(solidIni);
      // tet.tetraedriseWithNoConstraint(true);

      // On tient le bestio
      // On ajoute les contraintes non calculées

      // Batiment bat = new Batiment( new GM_Solid(
      // FusionTriangle.fusionnePolygonne(solidIni.getListeFacettes())));
      /*
       * if (Executor.VERBOSE) {
       * 
       * Batiment bat = new Batiment(new GM_Solid(
       * FusionTriangle.fusionnePolygonne(solidIni.getFacesList())));
       * 
       * enveloppCalculee.add(bat);
       * 
       * enveloppCalculee.add(bat.getToit());
       * 
       * } else {
       * 
       * /* Batiment bat = new Batiment(new GM_Solid(solidIni .getFacesList()));
       * 
       * enveloppCalculee.add(bat);
       * 
       * enveloppCalculee.add(bat.getToit());
       */

      enveloppCalculee.add(new EnveloppeConstructible(solidIni));

      // Tetraedrisation tet = new Tetraedrisation(solidIni);
      // tet.tetraedrise(false, true);
      // enveloppCalculee.add(new EnveloppeConstructible(new GM_Solid(tet
      // .getTriangles())));

    }

    return enveloppCalculee;
  }

  /**
   * Calcule les contraintes s'appliquant sur une parcelle
   */
  public List<FT_FeatureCollection<Contrainte>> computeConstraints(Parcelle p) {
    List<FT_FeatureCollection<Contrainte>> lLContraintes = new ArrayList<FT_FeatureCollection<Contrainte>>();

    // Liste des règles
    int nbRegles = this.lRegles.size();

    // List des conséquences que l'on applique à la parcelle
    List<Consequence> lConsTotales = new ArrayList<Consequence>();

    // Les routes paramètres bordantes
    List<Route> lRoutesParam = new ArrayList<Route>();

    boucleregle: for (int i = 0; i < nbRegles; i++) {

      List<Route> lRoutes = new ArrayList<Route>();
      Regle r = this.lRegles.get(i);
      boolean isRouteBordante = false;
      // Si des antécédants existent on vérifie qu'ils sont ok
      List<Antecedent> lAntecedants = r.getAntecedent();
      int nbAntecedants = lAntecedants.size();

      for (int k = 0; k < nbAntecedants; k++) {

        Antecedent ant = lAntecedants.get(k);

        if (!ant.isAntecedantChecked(p)) {
          // C'est non, la règle ne s'applique pas
          // On passe à la règle suivante
          continue boucleregle;

        }

        if (ant instanceof RouteBordante) {

          lRoutes.addAll(((RouteBordante) ant).getRoutesOk());
          isRouteBordante = true;
        }

        // Si oui,on vérifie les autres
      }

      List<Consequence> lCons = r.getConsequence();

      int nbConsequence = lCons.size();

      for (int j = 0; j < nbConsequence; j++) {

        Consequence cons = lCons.get(j);

        if (cons instanceof ReculRoute) {

          if (lRoutes.size() == 0 && !isRouteBordante) {
            lRoutes = p.getlRouteBordante();

          }

          int nbRoutes = lRoutes.size();

          for (int l = 0; l < nbRoutes; l++) {

            lRoutesParam.add(lRoutes.get(l));
            lConsTotales.add(cons);
          }

        } else {
          lRoutesParam.add(null);

          lConsTotales.add(cons);

          // Si oui,on vérifie les autres
        }

      }

    }

    // Nous avons extraits toutes les conséquences
    // Qui composerons les contraintes

    // On va effectuer un tri des conséquence afin de simplifier le calcul
    // final
    // Dans l'ordre :
    // 1) les conséquences ayant attrait au 2D (Recul avec distance
    // Euclidienne (ne premier les routes)))
    // 2) conséquences 3D simples (hauteur et différence de hauteur)
    // 3) conséquence plus complexes (recul et distance non euclidienne)
    // 4) les conséquence non-géométriques (COS,CES, Texture)
    double zmin = p.getGeom().coord().get(0).getZ();

    List<Consequence> lConsCat1 = new ArrayList<Consequence>();
    List<Consequence> lConsCat2 = new ArrayList<Consequence>();
    List<Consequence> lConsCat3 = new ArrayList<Consequence>();
    List<Consequence> lConsCat4 = new ArrayList<Consequence>();

    List<Route> lRoutesTriees = new ArrayList<Route>();

    int nbCons = lConsTotales.size();

    for (int i = 0; i < nbCons; i++) {
      Consequence cons = lConsTotales.get(i);

      if (cons instanceof Recul) {

        if (cons instanceof ReculRoute) {
          if (((ReculRoute) cons).getDistanceRecul() instanceof DistanceEuclidienne) {

            lConsCat1.add(0, cons);
            lRoutesTriees.add(0, lRoutesParam.get(i));
          } else {

            lConsCat3.add(0, cons);
            lRoutesTriees.add(lRoutesParam.get(i));

          }
          continue;
        }

        if (((Recul) cons).getDistanceRecul() instanceof DistanceEuclidienne) {

          lConsCat1.add(cons);

        } else {

          lConsCat3.add(cons);

        }

        continue;

      }

      if (cons instanceof ConsequenceHauteur) {
        lConsCat2.add(cons);
        continue;
      }

      lConsCat4.add(cons);

    }

    lConsTotales.clear();
    lRoutesParam.clear();

    // On traite les conséquences de type 1
    // on ne travaille ainsi que sur le polygone de la parcelle
    int nbConsCat1 = lConsCat1.size();
    int nbRoutes = 0;

    IGeometry poly = p.getGeom();

    for (int i = 0; i < nbConsCat1; i++) {

      Consequence cons = lConsCat1.get(i);
      if (cons instanceof ReculBordure) {

        IGeometry geom2 = ((ReculBordure) cons).genereContrainteEuclidienne(p);

        if (geom2 == null || geom2.isEmpty()) {
          poly = null;
          break;
        }

        poly = poly.intersection(geom2);
      } else if (cons instanceof ReculRoute) {

        IGeometry geom2 = ((ReculRoute) cons).genereContrainteEuclidienne(p,
            lRoutesTriees.get(nbRoutes));

        if (geom2 == null || geom2.isEmpty()) {
          poly = null;
          break;
        }

        poly = poly.intersection(geom2);
        nbRoutes++;
      } else if (cons instanceof ReculAutreBatiment) {

        IGeometry geom2 = ((ReculAutreBatiment) cons)
            .genereContrainteEuclidienne(p);
        if (geom2 == null || geom2.isEmpty()) {
          poly = null;
          break;
        }

        poly = poly.intersection(geom2);

      }

      if (poly == null || poly.isEmpty()) {
        break;
      }

    }

    if (poly == null || poly.isEmpty()) {
      return null;
    }

    if (poly.area() < 50) {
      System.out.println("Empreinte 2D trop petite");
      return null;
    }

    if (poly instanceof GM_MultiSurface<?>) {
      Prospect.clean((GM_MultiSurface<GM_OrientableSurface>) poly, 50);

    }

    double hauteurMax = Double.POSITIVE_INFINITY;

    int nbConsCat2 = lConsCat2.size();

    for (int i = 0; i < nbConsCat2; i++) {

      Consequence cons = lConsCat2.get(i);

      if (cons instanceof ContrainteHauteur) {
        hauteurMax = Math.min(((ContrainteHauteur) cons).getHauteurMax(),
            hauteurMax);

        // ***** Spécial PLU de Paris ****///
        // hauteurMax = Math.min(((ContrainteHauteur) cons).getHauteurMax()
        // + Double.parseDouble(p.getAttribute("Hauteur_Vo").toString()),
        // hauteurMax);
      } else if (cons instanceof DifferenceHauteur) {
        hauteurMax = Math.min(((DifferenceHauteur) cons).hauteurMax(p),
            hauteurMax);

      }

    }

    if (hauteurMax == Double.POSITIVE_INFINITY) {
      // On se limite à 30m dans ce cas
      hauteurMax = 30;
    }

    if (poly.isEmpty()) {
      return null;

    }

    // On créer l'enveloppe extrudée résultant de ces observations
    // IGeometry geomEtap1 = Extrusion2DObject.convertFromGeometry(poly, zmin,
    // zmin + hauteurMax);

    IGeometry geomEtap1 = ExtrusionTriangulation.process(poly, zmin, zmin
        + hauteurMax);

    List<GM_Solid> lSol = new ArrayList<GM_Solid>();

    if (geomEtap1 instanceof GM_MultiSolid<?>) {

      System.out.println(((GM_MultiSolid<GM_Solid>) geomEtap1).size()
          + " solutions");
      lSol.addAll(((GM_MultiSolid<GM_Solid>) geomEtap1));
    } else if (geomEtap1 instanceof GM_Solid) {
      lSol.add((GM_Solid) geomEtap1);
    } else {
      System.out.println("Other geom");
    }

    int nbSolid = lSol.size();

    for (int indSol = 0; indSol < nbSolid; indSol++) {

      FT_FeatureCollection<Contrainte> lContraintes = new FT_FeatureCollection<Contrainte>();

      nbRoutes = 0;

      GM_Solid solideEtap1 = lSol.get(indSol);
      lContraintes.add(new Contrainte(null, p, solideEtap1));

      // Maintenant il faut calculer l'intersection entre ce solide et les
      // dernières contributions

      int nbConsCat3 = lConsCat3.size();

      for (int i = 0; i < nbConsCat3; i++) {
        Consequence cons = lConsCat3.get(i);

        if (cons instanceof ReculRoute) {

          double pente = ((DistanceFHauteur) ((ReculRoute) cons)
              .getDistanceRecul()).getCoefficient();

          double hObj = ((DistanceFHauteur) ((ReculRoute) cons)
              .getDistanceRecul()).getHauteurOrigine();// Double.parseDouble(p.getAttribute("Hauteur_Vo").toString())Spécial
                                                       // PLU
                                                       // Paris

          GM_Solid solTemp = Prospect.calculeEmpriseSolid(p.getGeom(),
              lRoutesTriees.get(nbRoutes).getGeom(), pente, hObj, zmin);
          // poly ou p.getGeom()
          // ISolid solTemp = ExtrusionTriangulation.process(p.getGeom(),
          // lRoutesTriees
          // .get(nbRoutes).getGeom(), zmin,
          // ((DistanceFHauteur) ((ReculRoute) cons).getDistanceRecul()));
          nbRoutes++;

          lContraintes.add(new Contrainte(cons, p, solTemp));

        } else if (cons instanceof ReculAutreBatiment) {

          List<Batiment> lBati = p.getlBatimentsContenus();
          int nbBatiments = lBati.size();

          for (int j = 0; j < nbBatiments; j++) {
            IGeometry solTemp = Prospect.calculeEmpriseSolidBatiment(p.getGeom(),
                lBati.get(j).getToit().getGeom(),
                (DistanceFHauteur) ((ReculAutreBatiment) cons)
                    .getDistanceRecul(), zmin);
            if (solTemp != null) {
              lContraintes.add(new Contrainte(cons, p, solTemp));
            }
          }

        } else if (cons instanceof ReculBordure) {

          List<Parcelle> lParcelles = p.getlParcelleBordante();
          int nbBatiments = lParcelles.size();

          for (int j = 0; j < nbBatiments; j++) {

            // ISolid solTemp = ExtrusionTriangulation.process(p.getGeom(),
            // lParcelles.get(j).getGeom(), zmin,
            // ((DistanceFHauteur) ((ReculBordure) cons).getDistanceRecul()));

            GM_Solid solTemp = Prospect.calculeEmpriseSolid(p.getGeom(), lParcelles
                .get(j).getGeom(), (DistanceFHauteur) ((ReculBordure) cons)
                .getDistanceRecul(), zmin);
            lContraintes.add(new Contrainte(cons, p, solTemp));
          }
        }

      }
      lLContraintes.add(lContraintes);

    }

    int nbConsCat4 = lConsCat4.size();

    int nbLContraintes = lLContraintes.size();

    for (int i = 0; i < nbConsCat4; i++) {

      for (int j = 0; j < nbLContraintes; j++) {
        lLContraintes.get(j).add(
            new Contrainte(lConsCat4.get(i), p, new GM_Point(p.getGeom()
                .coord().get(0))));
      }

    }

    return lLContraintes;
  }

  public Environnement getEnv() {
    return this.env;
  }

  public List<Regle> getlRegles() {
    return this.lRegles;
  }

}
