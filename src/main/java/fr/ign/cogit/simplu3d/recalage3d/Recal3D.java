package fr.ign.cogit.simplu3d.recalage3d;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.vividsolutions.jts.geom.Geometry;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IPoint;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Arc;
import fr.ign.cogit.geoxygene.contrib.cartetopo.CarteTopo;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Face;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Groupe;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromPolygonToLineString;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion2DObject;
import fr.ign.cogit.geoxygene.sig3d.equation.ApproximatedPlanEquation;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.geoxygene.util.index.Tiling;
import fr.ign.cogit.simplu3d.checker.VeryFastRuleChecker;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.cache.CacheModelInstance;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.energy.cuboid2.DifferenceVolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.energy.cuboid2.IntersectionVolumeBinaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.energy.cuboid2.VolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.convert.GenerateSolidFromCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.loader.LoaderCuboid2;
import fr.ign.cogit.simplu3d.util.PointInPolygon;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.parameters.Parameters;
import fr.ign.rjmcmc.energy.BinaryEnergy;
import fr.ign.rjmcmc.energy.ConstantEnergy;
import fr.ign.rjmcmc.energy.MinusUnaryEnergy;
import fr.ign.rjmcmc.energy.MultipliesBinaryEnergy;
import fr.ign.rjmcmc.energy.MultipliesUnaryEnergy;
import fr.ign.rjmcmc.energy.PlusUnaryEnergy;
import fr.ign.rjmcmc.energy.UnaryEnergy;
/**
 * 
 *        This software is released under the licence CeCILL
 * 
 *        see LICENSE.TXT
 * 
 *        see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/ 
public class Recal3D {

  private static Logger logger = Logger.getLogger(Recal3D.class);

  /**
   * @param args
   * @throws CloneNotSupportedException
   */
  public static void main(String[] args) throws CloneNotSupportedException {

    String strShpOut = "E:/temp2/";
    String shpeIn = "E:/temp2/shp_0_ene-21877.23719914085.shp";

    List<Cuboid> lCuboid = LoaderCuboid2.loadFromShapeFile(shpeIn);

    IFeatureCollection<IFeature> featColl = new FT_FeatureCollection<>();

    List<AbstractBuilding> lAB = loadBuilding(lCuboid);
    featColl.addAll(fusionneGeom(lAB, 139.));

    // featColl.addAll(fusionneGeomTemp(lAB, 139.));
    ShapefileWriter.write(featColl, strShpOut + "test2.shp");

  }

  private static IFeatureCollection<? extends IFeature> fusionneGeomTemp(
      List<? extends AbstractBuilding> lAB, double zMini) {

    double threshold = 0.2;

    // Récupération des polygones de la solution
    IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();

    for (AbstractBuilding aB : lAB) {

      Face f = new Face();

      IPolygon poly = (IPolygon) aB.getFootprint().clone();

      if (!poly.isValid()) {
        System.out.println("Not valid");
      }

      if (poly.coord().size() != 5) {
        System.out.println("Coord " + poly.coord().size());
      }

      ApproximatedPlanEquation epq = new ApproximatedPlanEquation(poly);

      if (epq.getNormale().getZ() < 0) {
        System.out.println("Erroooooor");
      }

      f.setGeometrie(poly);

      featC.add(f);

    }

    // Création d'une carte topo
    CarteTopo carteTopo = newCarteTopo("-aex90", featC, threshold);

    IFeatureCollection<IFeature> featCollExport = new FT_FeatureCollection<>();

    for (Arc a : carteTopo.getPopArcs()) {

      IFeature feature = new DefaultFeature(a.getGeometrie());

      AttributeManager.addAttribute(feature, "FaceD",
          a.getFaceDroite(), "String");
      AttributeManager.addAttribute(feature, "FaceG",
          a.getFaceGauche(), "String");
      featCollExport.add(feature);
    }

    ShapefileWriter.write(carteTopo.getPopFaces(), "E:/temp2/outFace.shp");

    return featCollExport;

  }

  private static IFeatureCollection<IFeature> fusionneGeom(
      List<? extends AbstractBuilding> lAB, double zMini) {
    double threshold = 0.2;

    String attrzmax = "zMax";

    // Récupération des polygones de la solution
    IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();

    for (AbstractBuilding aB : lAB) {

      Face f = new Face();
      f.setGeometrie((IPolygon) aB.getFootprint().clone());

      featC.add(f);

    }

    // Création d'une carte topo
    CarteTopo carteTopo = newCarteTopo("-aex90", featC, threshold);

    IFeatureCollection<IFeature> featC2 = new FT_FeatureCollection<>();

    // Affectation d'un zMax aux faces de la carte topo
    for (AbstractBuilding aB : lAB) {

      IFeature feature = new DefaultFeature((IPolygon) aB.getFootprint());

      Box3D b = new Box3D(aB.getGeom());

      // System.out.println(b.getURDP().getZ());

      AttributeManager.addAttribute(feature, attrzmax, zMini
          + b.getURDP().getZ(), "Double");

      featC2.add(feature);
    }

    featC2.initSpatialIndex(Tiling.class, false);

    Groupe gr = carteTopo.getPopGroupes().nouvelElement();
    gr.setListeArcs(carteTopo.getListeArcs());
    gr.setListeFaces(carteTopo.getListeFaces());
    gr.setListeNoeuds(carteTopo.getListeNoeuds());

    List<Groupe> lG = gr.decomposeConnexes();

    IFeatureCollection<IFeature> featCollOut = new FT_FeatureCollection<>();

    logger.info("NB Groupes : " + lG.size());
    System.out.println("NB Groupes : " + lG.size());

    for (Groupe g : lG) {

      // On recrée les géométries
      List<IOrientableSurface> lOS = new ArrayList<>();

      List<Face> lF = new ArrayList<>();

      for (Arc a : g.getListeArcs()) {

        Face fg = a.getFaceDroite();
        Face fd = a.getFaceGauche();

        if (fg != null) {

          if (!lF.contains(fg)) {
            lF.add(fg);
          }

        }

        if (fd != null) {

          if (!lF.contains(fd)) {
            lF.add(fd);
          }

        }

      }

      for (Face f : lF) {

        if (f.isInfinite()) {
          continue;
        }

        IPoint p = new GM_Point(PointInPolygon.get(f.getGeometrie()));// f.getGeometrie().buffer(-0.05).coord().get(0));

        if (!f.getGeometrie().contains(p)) {
          logger.warn("Point not in polygon");
        }

        Collection<IFeature> featSelect = featC2.select(p);

        double zMax = Double.NEGATIVE_INFINITY;

        if (featSelect.isEmpty()) {

          zMax = zMini;

          logger.info("New empty face detected");
          // System.exit(666);
        }

        for (IFeature feat : featSelect) {
          zMax = Math.max(zMax,
              Double.parseDouble(feat.getAttribute(attrzmax).toString()));
        }

        IPolygon poly = (IPolygon) f.getGeometrie().clone();

        f.setArcsIgnores(zMax + "");

        // On affecte
        // AttributeManager.addAttribute(f, attrzmax, zMax, "Double");
        for (IDirectPosition dp : poly.coord()) {
          dp.setZ(zMax);
        }

        lOS.add(poly);

      }

      // Affectation d'un zMin et d'un zMax aux arêtes des faces

      for (Arc a : g.getListeArcs()) {

        Face fd = a.getFaceDroite();
        Face fg = a.getFaceGauche();

        double z1 = 0;
        double z2 = 0;

        if (fd == null || fd.isInfinite()) {

          z1 = Double.parseDouble(fg.getArcsIgnores());
          z2 = zMini;

        } else if (fg == null || fg.isInfinite()) {

          z1 = Double.parseDouble(fd.getArcsIgnores());
          z2 = zMini;

        } else {

          z1 = Double.parseDouble(fg.getArcsIgnores());
          z2 = Double.parseDouble(fd.getArcsIgnores());

        }

        double zMin = Math.min(z1, z2);
        double zMax = Math.max(z1, z2);

        if (zMax == zMini) {

          continue;
        }

        // if(Double.isNaN(zMin) || Double.isNaN(zMax)){

        // System.out.println("zMin : " + zMin + "  zMAx " + zMax);

        // }

        IGeometry geom = Extrusion2DObject.convertFromLine((ILineString) a
            .getGeometrie().clone(), zMin, zMax);

        lOS.addAll(FromGeomToSurface.convertGeom(geom));

      }

      featCollOut.add(new DefaultFeature(new GM_MultiSurface<>(lOS)));

    }

    return featCollOut;

  }



  public static CarteTopo newCarteTopo(String name,
      IFeatureCollection<? extends IFeature> collection, double threshold) {

    try {
      // Initialisation d'une nouvelle CarteTopo
      CarteTopo carteTopo = new CarteTopo(name);
      carteTopo.setBuildInfiniteFace(true);
      // Récupération des arcs de la carteTopo
      IPopulation<Arc> arcs = carteTopo.getPopArcs();
      // Import des arcs de la collection dans la carteTopo
      for (IFeature feature : collection) {

        List<ILineString> lLLS = FromPolygonToLineString
            .convertPolToLineStrings((IPolygon) FromGeomToSurface.convertGeom(
                feature.getGeom()).get(0));

        for (ILineString ls : lLLS) {

          // affectation de la géométrie de l'objet issu de la collection
          // à l'arc de la carteTopo
          for (int i = 0; i < ls.numPoints() - 1; i++) {
            // création d'un nouvel élément
            Arc arc = arcs.nouvelElement();
            arc.setGeometrie(new GM_LineString(ls.getControlPoint(i), ls
                .getControlPoint(i + 1)));
            // instanciation de la relation entre l'arc créé et l'objet
            // issu de la collection
            arc.addCorrespondant(feature);
          }

        }

      }
      if (!test(carteTopo)) {
        logger.error("");
        System.exit(0);
      }
      carteTopo.creeNoeudsManquants(0.01);
      
      
      
      if (!test(carteTopo)) {
        logger.error("");
        System.exit(0);
      }

      carteTopo.fusionNoeuds(threshold);
      
      
      if (!test(carteTopo)) {
        logger.error("");
        System.exit(0);
      }

      carteTopo.decoupeArcs(0.1);

      carteTopo.filtreArcsDoublons();
      if (!test(carteTopo)) {
        logger.error("");
        System.exit(0);
      }

      // Création de la topologie Arcs Noeuds

      carteTopo.creeTopologieArcsNoeuds(threshold);
      // La carteTopo est rendue planaire
      if (!test(carteTopo)) {
        logger.error("");
        System.exit(0);
      }

      carteTopo.rendPlanaire(threshold);
      if (!test(carteTopo)) {
        logger.error("");
        System.exit(0);
      }

      /*
       * if (!test(carteTopo)) { System.out.println("Error 4"); }
       * carteTopo.filtreArcsDoublons(); if (!test(carteTopo)) {
       * System.out.println("Error 5"); }
       */

      // DEBUG2.addAll(carteTopo.getListeArcs());

      carteTopo.creeTopologieArcsNoeuds(threshold);
      if (!test(carteTopo)) {
        logger.error("");
        System.exit(0);
      }

      /*
       * if (!test(carteTopo)) { System.out.println("Error 6"); }
       */

      // carteTopo.creeTopologieFaces();

      // carteTopo.filtreNoeudsSimples();
      // if (!test(carteTopo)) {
      // logger.error("");
      // System.exit(0);
      // }

      // Création des faces de la carteTopo
      carteTopo.creeTopologieFaces();
      if (!test(carteTopo)) {
        logger.error("");
        System.out.println("Error 3");
      }
      /*
       * if (!test(carteTopo)) { System.out.println("Error 7"); }
       */

      return carteTopo;

    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  private static boolean test(CarteTopo ct) {
    for (Arc a : ct.getPopArcs()) {

      if (a.getGeometrie().coord().size() < 2) {
        return false;
      }

    }

    return true;

  }

  public static List<AbstractBuilding> loadBuilding(List<Cuboid> lC) {

    List<AbstractBuilding> lAB = new ArrayList<>();

    for (Cuboid c : lC) {
      Building bP = new Building(new GM_MultiSurface<>(GenerateSolidFromCuboid
          .generate(c).getFacesList()));

      bP.isNew = true;

      lAB.add(bP);
    }

    return lAB;
  }

  @SuppressWarnings("unused")
  private static double getMoyGroup(List<AbstractBuilding> lAB) {

    double moy = 0;

    for (AbstractBuilding aB : lAB) {

      moy = moy + getZMax(aB);

    }

    return moy / lAB.size();

  }

  public static List<AbstractBuilding> changeHeight(List<AbstractBuilding> lAB,
      double diffHeight, VeryFastRuleChecker vFR,
      CacheModelInstance<AbstractBuilding> cMI) {

    List<List<AbstractBuilding>> lLLAB = new ArrayList<>();

    // On prépare les groupes
    boucleab: for (AbstractBuilding aB : lAB) {

      Box3D b1 = new Box3D(aB.getGeom());

      for (List<AbstractBuilding> lABTemp : lLLAB) {

        for (AbstractBuilding aBTemp : lABTemp) {
          if (!aB.getFootprint().intersects(aBTemp.getFootprint())) {
            continue;
          }

          Box3D b2 = new Box3D(aBTemp.getGeom());

          double hi = b1.getURDP().getZ() - b1.getLLDP().getZ();
          double hj = b2.getURDP().getZ() - b2.getLLDP().getZ();

          if (Math.abs(hi - hj) > diffHeight) {
            continue;
          }

          lABTemp.add(aB);
          continue boucleab;

        }

      }

      List<AbstractBuilding> bTT = new ArrayList<>();
      bTT.add(aB);
      lLLAB.add(bTT);
    }

    // On prépare les géométries des groupes
    List<IGeometry> lGeomGroup = new ArrayList<>();
    for (List<AbstractBuilding> lABTemp : lLLAB) {

      int nbElem = lABTemp.size();
      IGeometry geom = lABTemp.get(0).getFootprint();

      for (int i = 1; i < nbElem; i++) {
        geom = geom.union(lABTemp.get(i).getFootprint());

      }

      lGeomGroup.add(geom);

    }

    // On fusionne les groupes
    int nbGroup = lLLAB.size();

    bouclei: for (int i = 0; i < nbGroup; i++) {

      List<AbstractBuilding> lABTemp1 = lLLAB.get(i);

      double hMini = Double.POSITIVE_INFINITY;
      double hMaxi = Double.NEGATIVE_INFINITY;

      for (AbstractBuilding aB : lABTemp1) {
        hMini = Math.min(hMini, getH(aB));
        hMaxi = Math.max(hMaxi, getH(aB));
      }

      for (int j = i + 1; j < nbGroup; j++) {

        List<AbstractBuilding> lABTemp2 = lLLAB.get(j);

        IGeometry geomi = lGeomGroup.get(i);
        IGeometry geomj = lGeomGroup.get(j);

        if (!geomi.intersects(geomj)) {
          continue;
        }
        double hMinj = Double.POSITIVE_INFINITY;
        double hMaxj = Double.NEGATIVE_INFINITY;

        for (AbstractBuilding aB : lABTemp2) {
          hMinj = Math.min(hMinj, getH(aB));
          hMaxj = Math.max(hMaxj, getH(aB));
        }

        // Intervalle en commun ?
        if ((hMinj <= hMaxi && hMinj >= hMini)
            || ((hMaxj >= hMini) && (hMaxj <= hMaxi))) {

          // on fusionne
          lGeomGroup.set(j, geomi.union(geomj));
          lABTemp2.addAll(lABTemp1);

          lGeomGroup.remove(i);
          lLLAB.remove(i);
          nbGroup--;
          i = -1;
          continue bouclei;

        }

      }

    }

    // On bouge les groupes

    List<AbstractBuilding> lABOUT = new ArrayList<>();
    for (List<AbstractBuilding> lABTem : lLLAB) {

      double hMini = Double.POSITIVE_INFINITY;
      double hMaxi = Double.NEGATIVE_INFINITY;

      for (AbstractBuilding aB : lABTem) {
        hMini = Math.min(hMini, getZMax(aB));
        hMaxi = Math.max(hMaxi, getZMax(aB));
      }

      // on tue tout ce qui existe

      List<AbstractBuilding> lDeath = lABTem;
      // cMI.update(new ArrayList<AbstractBuilding>(), lABTem);

      for (double d = hMaxi; d > 0; d = d - diffHeight / 2) {

        List<AbstractBuilding> lBorn = new ArrayList<>();

        for (AbstractBuilding aBT : lDeath) {

          lBorn.add(changeGeomZMax(aBT, d));

        }

        boolean check = vFR.check(cMI.update(lBorn, lDeath));

        System.out.println(check + " d  " + d);

        if (check) {
          lABTem.clear();
          lABOUT.addAll(lBorn);

          break;
        }

        lDeath = lBorn;

      }

    }

    return lABOUT;
  }

  public static AbstractBuilding changeGeomZMax(AbstractBuilding aBIni,
      double zMaxNew) {

    AbstractBuilding aB = (AbstractBuilding) aBIni.clone();

    Box3D b = new Box3D(aBIni.getGeom());

    double zMax = b.getURDP().getZ();

    IDirectPositionList dpl = aB.getGeom().coord();

    for (IDirectPosition dp : dpl) {

      if (dp.getZ() == zMax) {

        dp.setZ(zMaxNew);

      }

    }

    return aB;

  }

  public static double getZMax(AbstractBuilding aB) {
    Box3D b1 = new Box3D(aB.getGeom());

    return b1.getURDP().getZ();

  }

  public static double getH(AbstractBuilding aB) {
    Box3D b1 = new Box3D(aB.getGeom());

    return b1.getURDP().getZ() - b1.getLLDP().getZ();

  }

  public static List<AbstractBuilding> changeHeight2(
      List<AbstractBuilding> lAB, double diffHeight, VeryFastRuleChecker vFR,
      CacheModelInstance<AbstractBuilding> cMI) {

    int nbB = lAB.size();

    List<AbstractBuilding> lABBirth = new ArrayList<AbstractBuilding>();
    List<AbstractBuilding> lABDeath = new ArrayList<AbstractBuilding>();

    bouclei: for (int i = 0; i < nbB; i++) {

      AbstractBuilding aBi = lAB.get(i);

      for (int j = i + 1; j < nbB; j++) {

        if (i == 1 && j == 4) {
          System.out.println("je rame");
        }

        AbstractBuilding aBj = lAB.get(j);

        if (!aBi.getFootprint().intersects(aBj.getFootprint())) {
          continue;
        }

        Box3D b1 = new Box3D(aBi.getGeom());

        Box3D b2 = new Box3D(aBj.getGeom());

        double hi = b1.getURDP().getZ() - b1.getLLDP().getZ();
        double hj = b1.getURDP().getZ() - b2.getLLDP().getZ();

        if (hi == hj) {
          continue;
        }

        if (Math.abs(hi - hj) > diffHeight) {
          continue;
        }

        double hMax = Math.max(hi, hj);

        // on augmente la taille

        AbstractBuilding aBinew = changeGeomHMax(aBi, b1, hi, hMax);

        AbstractBuilding aBjnew = changeGeomHMax(aBj, b2, hj, hMax);

        if (aBinew != null) {
          lABBirth.add(aBinew);
          lABDeath.add(aBi);
        }

        if (aBjnew != null) {
          lABBirth.add(aBjnew);
          lABDeath.add(aBj);
        }

        boolean isCheck = vFR.check(cMI.update(lABBirth, lABDeath));

        if (isCheck) {

          if (aBjnew != null) {
            lAB.remove(j);
            lAB.add(aBjnew);

          }

          if (aBinew != null) {
            lAB.remove(i);
            lAB.add(aBinew);

          }

          lABBirth.clear();
          lABDeath.clear();

          i = -1;
          continue bouclei;
        }

        // on annule
        cMI.update(lABDeath, lABBirth);
        lABBirth.clear();
        lABDeath.clear();

        hMax = Math.min(hi, hj);

        // on diminue la taille

        aBinew = changeGeomHMax(aBi, b1, hi, hMax);

        aBjnew = changeGeomHMax(aBj, b2, hj, hMax);

        if (aBinew != null) {
          lABBirth.add(aBinew);
          lABDeath.add(aBi);
        }

        if (aBjnew != null) {
          lABBirth.add(aBjnew);
          lABDeath.add(aBj);
        }

        isCheck = vFR.check(cMI.update(lABBirth, lABDeath));

        if (isCheck) {
          if (aBjnew != null) {
            lAB.remove(j);
            lAB.add(aBjnew);

          }

          if (aBinew != null) {
            lAB.remove(i);
            lAB.add(aBinew);

          }

          i = -1;
          continue bouclei;
        }

        // on annule
        cMI.update(lABDeath, lABBirth);
        lABBirth.clear();
        lABDeath.clear();

        // les modifs ne sont pas acceptée, on revient à l'origine

      }

    }

    return lAB;
  }

  public static AbstractBuilding changeGeomHMax(AbstractBuilding aBIni,
      Box3D b, double currentH, double newH) {

    AbstractBuilding aB = (AbstractBuilding) aBIni.clone();

    double zMin = b.getLLDP().getZ();
    double zMax = b.getURDP().getZ();

    IDirectPositionList dpl = aB.getGeom().coord();

    for (IDirectPosition dp : dpl) {

      if (dp.getZ() == zMax) {

        dp.setZ(zMin + newH);

      }

    }

    return aB;

  }

  /**
   * @param p paramètres importés depuis le fichier XML
   * @param bpu l'unité foncière considérée
   * @return la configuration chargée, c'est à dire la formulation énergétique
   *         prise en compte
   */
  public static GraphConfiguration<Cuboid> create_configuration(Parameters p,
      Geometry bpu) {

    // Énergie constante : à la création d'un nouvel objet
    ConstantEnergy<Cuboid, Cuboid> energyCreation = new ConstantEnergy<Cuboid, Cuboid>(
        p.getDouble("energy"));

    // Énergie constante : pondération de l'intersection
    ConstantEnergy<Cuboid, Cuboid> ponderationVolume = new ConstantEnergy<Cuboid, Cuboid>(
        p.getDouble("ponderation_volume"));

    // Énergie unaire : aire dans la parcelle
    UnaryEnergy<Cuboid> energyVolume = new VolumeUnaryEnergy<Cuboid>();
    // Multiplication de l'énergie d'intersection et de l'aire
    UnaryEnergy<Cuboid> energyVolumePondere = new MultipliesUnaryEnergy<Cuboid>(
        ponderationVolume, energyVolume);

    // On retire de l'énergie de création, l'énergie de l'aire
    UnaryEnergy<Cuboid> u3 = new MinusUnaryEnergy<Cuboid>(energyCreation,
        energyVolumePondere);

    // Énergie constante : pondération de la différence
    ConstantEnergy<Cuboid, Cuboid> ponderationDifference = new ConstantEnergy<Cuboid, Cuboid>(
        p.getDouble("ponderation_difference_ext"));
    // On ajoute l'énergie de différence : la zone en dehors de la parcelle
    UnaryEnergy<Cuboid> u4 = new DifferenceVolumeUnaryEnergy<Cuboid>(bpu);
    UnaryEnergy<Cuboid> u5 = new MultipliesUnaryEnergy<Cuboid>(
        ponderationDifference, u4);
    UnaryEnergy<Cuboid> unaryEnergy = new PlusUnaryEnergy<Cuboid>(u3, u5);

    // Énergie binaire : intersection entre deux rectangles
    ConstantEnergy<Cuboid, Cuboid> c3 = new ConstantEnergy<Cuboid, Cuboid>(
        p.getDouble("ponderation_volume_inter"));
    BinaryEnergy<Cuboid, Cuboid> b1 = new IntersectionVolumeBinaryEnergy<Cuboid>();
    BinaryEnergy<Cuboid, Cuboid> binaryEnergy = new MultipliesBinaryEnergy<Cuboid, Cuboid>(
        c3, b1);
    // empty initial configuration*/

    GraphConfiguration<Cuboid> conf = new GraphConfiguration<Cuboid>(unaryEnergy,
        binaryEnergy);

    return conf;
  }

}
