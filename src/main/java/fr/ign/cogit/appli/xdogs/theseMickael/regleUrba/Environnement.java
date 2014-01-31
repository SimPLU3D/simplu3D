package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.schemageo.Batiment;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.schemageo.Parcelle;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.schemageo.Route;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.schemageo.Toit;
import fr.ign.cogit.appli.xdogs.theseMickael.util.correction.CorrectionBati3D;
import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.calculation.Calculation3D;
import fr.ign.cogit.geoxygene.sig3d.calculation.Util;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion2DObject;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.geoxygene.spatial.geomroot.GM_Object;
import fr.ign.cogit.geoxygene.util.conversion.JtsGeOxygene;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;

/**
 * Classe permettant le chargement du modèle de résolution des règles urbas
 * 
 * @author MBrasebin
 */
public class Environnement {
  // Nom des différents fichiers
  public static String FICHIER_BATI = "Bati.shp";
  public static String FICHIER_PARCELLE = "Parcelle.shp";
  public static String FICHIER_ROUTE = "Route.shp";

  public static double DEFAULT_ZERO_Z = 139; // 41; //138

  // Listes des entités géographiques
  private FT_FeatureCollection<Toit> lToits = new FT_FeatureCollection<Toit>();
  private FT_FeatureCollection<Parcelle> lParcelles = new FT_FeatureCollection<Parcelle>();
  private FT_FeatureCollection<Route> lRoutes = new FT_FeatureCollection<Route>();
  private FT_FeatureCollection<Batiment> lBatiments = new FT_FeatureCollection<Batiment>();

  private FT_FeatureCollection<IFeature> collParcelleParcelle = new FT_FeatureCollection<IFeature>();
  private FT_FeatureCollection<IFeature> collRouteParcelle = new FT_FeatureCollection<IFeature>();
  private FT_FeatureCollection<IFeature> collBatimentParcelle = new FT_FeatureCollection<IFeature>();

  // Liste des bâtiments non associés à une parcelle
  private FT_FeatureCollection<Batiment> lBatimentsErreur = new FT_FeatureCollection<Batiment>();

  private List<Geometry> lGeometryParcelle = new ArrayList<Geometry>();
  
  
  
  public static Color COL_BAT_FAC = new Color(222, 222, 222) ;
  public static Color COL_BAT_ROOF =  new Color(153, 0, 13);

  /**
   * Création de l'environnement : 1) Charge les données du repertoire
   * repertoire portant les noms : - FICHIER_BATI - FICHIER_PARCELLE -
   * FICHIER_ROUTE 2) créer les relations Bati <=> Parcelle Parcelle <=>
   * Parcelle Bati <=> Toit Route <=> Parcelle 3) en mode affiche : affiche les
   * objets 4) en mode debug : affiche les relations
   * 
   * @param repertoire le répertoire dans lequel on fouille
   * @param debug debug mode
   * @param affiche affiche le résultat (notamment les liens)
   * @throws Exception
   */
  public Environnement(String repertoire, boolean debug) throws Exception {

    // On instancie les différents objest
    IFeatureCollection<IFeature> collBati = ShapefileReader.read(repertoire
        + Environnement.FICHIER_BATI);
    IFeatureCollection<IFeature> collParcelles = ShapefileReader
        .read(repertoire + Environnement.FICHIER_PARCELLE);
    IFeatureCollection<IFeature> collRoute = ShapefileReader.read(repertoire
        + Environnement.FICHIER_ROUTE);

    CorrectionBati3D.correctionNormalesNoFloor(collBati);

    // On les affecte à leures classes
    this.loadBuilding(collBati);
    this.loadParcel(collParcelles);
    this.loadRoute(collRoute);

    // On crée les assocaitions parcelles <-> bâtiment
    GM_Object obj = this.associeBatimentParcelle(debug);
    GM_Object obj2 = this.associeParcelleParcelle(debug, 3);
    GM_Object obj3 = this.associeRouteParcelle(debug, 8);

    if (debug) {
      IFeature featTest = new DefaultFeature(obj);
      this.collBatimentParcelle.add(featTest);

    }

    if (debug) {
      IFeature featTest = new DefaultFeature(obj2);
      this.collParcelleParcelle.add(featTest);

    }

    if (debug) {
      IFeature featTest = new DefaultFeature(obj3);
      this.collRouteParcelle.add(featTest);
    }

    this.lGeometryParcelle.clear();
  }

  public FT_FeatureCollection<IFeature> getCollParcelleParcelle() {
    return this.collParcelleParcelle;
  }

  public FT_FeatureCollection<IFeature> getCollRouteParcelle() {
    return this.collRouteParcelle;
  }

  public FT_FeatureCollection<IFeature> getCollBatimentParcelle() {
    return this.collBatimentParcelle;
  }

  /**
   * Permet de renseigner this.lToits et lBatiments à partir d'une liste de
   * batiments (de géométrie MultiSurface) Créer le lien entre un batiment et
   * son toit (le toit est une face ayant un z non horizontal)
   * 
   * @param collBati les batiments qui génèreront les informations
   */
  private void loadBuilding(IFeatureCollection<IFeature> collBati) {

    int nbElement = collBati.size();

    // On traite chaque batiment
    for (int i = 0; i < nbElement; i++) {

      IFeature feat = collBati.get(i);
      // Il a une géométrie multisurface
      IGeometry geom = feat.getGeom();

      if (Executor.TRANSLATE_TO_ZERO) {
        if (Executor.dpTranslate == null) {
          Executor.dpTranslate = new DirectPosition(
              -geom.coord().get(0).getX(), -geom.coord().get(0).getY(), 0.0);
        }
        Calculation3D.translate(geom, Executor.dpTranslate);
      }

      ArrayList<IOrientableSurface> lFacettes = new ArrayList<IOrientableSurface>();

      if (geom instanceof GM_MultiSurface<?>) {
        lFacettes.addAll(((GM_MultiSurface<?>) geom).getList());
      } else {
        System.out.println("Géométrie non reconnue");
        continue;
      }

      /*
       * int nbFacet = lFacettes.size(); // Une liste pour les facettes de Murs
       * et une autre pour les // facettes de // toits
       * ArrayList<IOrientableSurface> listeMurs = new
       * ArrayList<IOrientableSurface>(); ArrayList<IOrientableSurface>
       * listeToits = new ArrayList<IOrientableSurface>();
       * 
       * // On regarde pour chaque face son angle par rapport à l'horizontal, //
       * on trie en fonction for (int j = 0; j < nbFacet; j++) {
       * 
       * IOrientableSurface os = lFacettes.get(j);
       * 
       * ApproximatedPlanEquation eq = new ApproximatedPlanEquation(os); Vecteur
       * vect = eq.getNormale();
       * 
       * if (Math.abs(vect.prodScalaire(MathConstant.vectZ)) < 0.01) {
       * 
       * listeMurs.add(os);
       * 
       * } else {
       * 
       * listeToits.add(os);
       * 
       * 
       * }
       * 
       * }
       * 
       * if (listeToits.size() == 0 || listeMurs.size() == 0) { continue; }
       */

      // on a traité toutes les faces du batiments on instancie les objets
      // en fonction

      Batiment b = new Batiment(new GM_MultiSurface<IOrientableSurface>(
          lFacettes), COL_BAT_FAC, COL_BAT_ROOF);

      this.lBatiments.add(b);
      this.lToits.add(b.getToit());

    }

  }

  /**
   * Permet de charger les parcelles Renseigne lGeometryParcelle
   * 
   * @param collParcel
   * @throws Exception
   */
  private void loadParcel(IFeatureCollection<IFeature> collParcel)
      throws Exception {

    int nbElement = collParcel.size();

    for (int i = 0; i < nbElement; i++) {

      IFeature feat = collParcel.get(i);
      IGeometry geom = feat.getGeom();

      if (Executor.TRANSLATE_TO_ZERO) {
        if (Executor.dpTranslate == null) {
          Executor.dpTranslate = new DirectPosition(
              -geom.coord().get(0).getX(), -geom.coord().get(0).getY(), 0.0);
        }
        Calculation3D.translate(geom, Executor.dpTranslate);
      }

      feat.setGeom(Extrusion2DObject.convertFromGeometry(geom,
          Environnement.DEFAULT_ZERO_Z, Environnement.DEFAULT_ZERO_Z));

      this.lParcelles.add(new Parcelle((DefaultFeature) feat));

      Geometry jtsGeomPar = JtsGeOxygene.makeJtsGeom(geom);
      this.lGeometryParcelle.add(jtsGeomPar);
    }

  }

  /**
   * Permet de charger les routes. Affecte : - le type - la largeur - le nom de
   * la route (voire les variables static pour définir le nom des champs
   * utilisés)
   * 
   * @param collParcel
   */
  private void loadRoute(IFeatureCollection<IFeature> collRoute) {

    int nbElement = collRoute.size();

    for (int i = 0; i < nbElement; i++) {

      this.lRoutes.add(new Route(collRoute.get(i)));

    }

  }

  /**
   * Permet de faire le lien entre le Batiment et la Parcelle Si debug renvoie
   * une géométrie contenenant les liens et renseigne batierreur
   * 
   * @throws Exception
   */
  private GM_MultiCurve<GM_LineString> associeBatimentParcelle(boolean debug)
      throws Exception {

    // Renseigne les liens
    GM_MultiCurve<GM_LineString> gmCurve = new GM_MultiCurve<GM_LineString>();

    // Batiments et parcelles
    int nbBatiment = this.lBatiments.size();
    int nbParcell = this.lParcelles.size();

    // compteur
    int cal = 0;

    bouclebati: for (int i = 0; i < nbBatiment; i++) {
      if (debug) {
        System.out.println(i + "/" + nbBatiment);
      }
      // On prend un point central du batiement
      Batiment bat = this.lBatiments.get(i);

      Box3D b = new Box3D(bat.getGeom());

      GM_Point p = new GM_Point(Util.centerOf(bat.getToit().getGeom().coord()));
      // Géométrie JTS
      Geometry jtsGeomBat = JtsGeOxygene.makeJtsGeom(p);

      // On cherche l'intersection avec une parcelle (JTS est 2D)
      for (int j = 0; j < nbParcell; j++) {

        if (jtsGeomBat.intersects(this.lGeometryParcelle.get(j))
            || this.lGeometryParcelle.get(j).contains(jtsGeomBat)
            || this.lGeometryParcelle.get(j).touches(jtsGeomBat)) {
          // L'intersection existe

          Parcelle par = this.lParcelles.get(j);
          // On associe le batiment à la parcelle
          bat.setParcelle(par);
          // On associe la parcelle au batiment
          par.getlBatimentsContenus().add(bat);

          // par.setZ(b.getLLDP().getZ());

          par.setZ(Environnement.DEFAULT_ZERO_Z);

          if (debug) {
            // Mode débug, on crée le lien entre les 2 objets
            // associés
            Box3D b2 = new Box3D(par.getGeom());

            DirectPositionList dpl = new DirectPositionList();
            dpl.add(b.getCenter());
            dpl.add(b2.getCenter());

            gmCurve.add(new GM_LineString(dpl));
          }
          if (debug) {
            cal++;
          }
          continue bouclebati;
        }

      }
      if (debug) {
        // élément solitaire
        this.lBatimentsErreur.add(bat);
      }
    }
    if (debug) {
      // Résultat de l'association
      System.out.println(cal + "/" + nbBatiment);
    }
    return gmCurve;
  }

  /**
   * Permet d'indiquer les liens parcelles <=> parcelles Cela se fait en
   * regardant quelles sont les parcelles se trouvant à une certaine distance
   * d'une autre
   * 
   * @param debug si true, indique le nombre de liens et renvoie une géométrie
   *          pour afficher
   * @param distance la distance maximale pour que l'on considère 2 parcelles
   *          comme voisine
   * @return
   * @throws Exception
   */
  private GM_MultiCurve<GM_LineString> associeParcelleParcelle(boolean debug,
      double distance) throws Exception {

    int nbParcell = this.lParcelles.size();

    int compt = 0;

    // Renseigne les liens
    GM_MultiCurve<GM_LineString> gmCurve = new GM_MultiCurve<GM_LineString>();

    for (int i = 0; i < nbParcell; i++) {
      Parcelle par1 = this.lParcelles.get(i);

      for (int j = i + 1; j < nbParcell; j++) {
        Parcelle par2 = this.lParcelles.get(j);

        if (this.lGeometryParcelle.get(i).isWithinDistance(
            this.lGeometryParcelle.get(j), distance)) {

          par1.getlParcelleBordante().add(par2);
          par2.getlParcelleBordante().add(par1);

          Box3D b1 = new Box3D(par1.getGeom());
          Box3D b2 = new Box3D(par2.getGeom());

          IDirectPosition dp1 = b1.getCenter();
          dp1.setZ(dp1.getZ() + 1);
          IDirectPosition dp2 = b2.getCenter();
          dp2.setZ(dp2.getZ() + 1);

          DirectPositionList dpl = new DirectPositionList();
          dpl.add(dp1);
          dpl.add(dp2);

          gmCurve.add(new GM_LineString(dpl));
          if (debug) {
            compt++;
          }
          continue;
        }

      }

    }
    if (debug) {
      System.out.println("Relations parcelle/parcellle " + compt);
    }
    return gmCurve;

  }

  /**
   * Indique les liens entre Route et Parcelle (les routes bordant une parcelle
   * et vice versae
   * 
   * @param debug si debug renvoie une géométrie correspondant aux
   *          correspondances
   * @param distance distance pour laquelle on considère que le lien existe
   * @return une géométrie correspondant au liens si debug est vrai
   * @throws Exception
   */
  private GM_MultiCurve<GM_LineString> associeRouteParcelle(boolean debug,
      double distance) throws Exception {

    // Renseigne les liens
    GM_MultiCurve<GM_LineString> gmCurve = new GM_MultiCurve<GM_LineString>();

    // Routes et parcelles

    int nbParcell = this.lParcelles.size();
    int nbRoutes = this.lRoutes.size();

    // Optimisation on fait la traduction en géométrie JTS dès le début
    List<Geometry> lGeomRoute = new ArrayList<Geometry>();

    for (int i = 0; i < nbRoutes; i++) {
      Route route = this.lRoutes.get(i);
      Geometry jtsGeomRoute = JtsGeOxygene.makeJtsGeom(route.getGeom());
      lGeomRoute.add(jtsGeomRoute);
    }

    // compteur
    int cal = 0;

    for (int i = 0; i < nbParcell; i++) {
      if (debug) {
        System.out.println(i + "/" + nbParcell);
      }

      Geometry jtsGeomParcelle = this.lGeometryParcelle.get(i);

      // On cherche l'intersection avec une route (JTS est 2D)
      for (int j = 0; j < nbRoutes; j++) {

        if (jtsGeomParcelle.isWithinDistance(lGeomRoute.get(j), distance)) {
          // La proximité existe

          Route route = this.lRoutes.get(j);
          // On associe la parcelle à la route
          Parcelle parcelle = this.lParcelles.get(i);
          route.getParcellesBordantes().add(parcelle);

          // On associe la parcelle au batiment
          parcelle.getlRouteBordante().add(route);

          if (debug) {
            // Mode débug, on crée le lien entre les 2 objets
            // associés
            Box3D b1 = new Box3D(route.getGeom());
            Box3D b2 = new Box3D(parcelle.getGeom());

            DirectPositionList dpl = new DirectPositionList();
            dpl.add(b1.getCenter());
            dpl.add(b2.getCenter());

            gmCurve.add(new GM_LineString(dpl));
          }
          if (debug) {
            cal++;
          }
          continue;
        }

      }

    }
    if (debug) {
      // Résultat de l'association
      System.out.println(cal + "/relation parcelle => route");
    }
    return gmCurve;
  }

  public FT_FeatureCollection<Toit> getlToits() {
    return this.lToits;
  }

  public FT_FeatureCollection<Parcelle> getlParcelles() {
    return this.lParcelles;
  }

  public FT_FeatureCollection<Route> getlRoutes() {
    return this.lRoutes;
  }

  public FT_FeatureCollection<Batiment> getlBatiments() {
    return this.lBatiments;
  }

  public FT_FeatureCollection<Batiment> getlBatimentsErreur() {
    return this.lBatimentsErreur;
  }

  public List<Geometry> getlGeometryParcelle() {
    return this.lGeometryParcelle;
  }

}
