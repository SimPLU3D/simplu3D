package fr.ign.cogit.simplu3d.recalage3d;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.ISolid;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion2DObject;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.checker.VeryFastRuleChecker;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.cache.CacheModelInstance;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.energy.cuboid2.DifferenceVolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.energy.cuboid2.IntersectionVolumeBinaryEnergy;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.energy.cuboid2.VolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.convert.GenerateSolidFromCuboid;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.impl.Cuboid2;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.loader.LoaderCuboid2;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.parameters.Parameters;
import fr.ign.rjmcmc.configuration.Configuration;
import fr.ign.rjmcmc.energy.BinaryEnergy;
import fr.ign.rjmcmc.energy.ConstantEnergy;
import fr.ign.rjmcmc.energy.MinusUnaryEnergy;
import fr.ign.rjmcmc.energy.MultipliesBinaryEnergy;
import fr.ign.rjmcmc.energy.MultipliesUnaryEnergy;
import fr.ign.rjmcmc.energy.PlusUnaryEnergy;
import fr.ign.rjmcmc.energy.UnaryEnergy;

public class Recal3D {

  /**
   * @param args
   * @throws CloneNotSupportedException
   */
  public static void main(String[] args) throws CloneNotSupportedException {

    String strShpOut = "E:/temp/shp3D/out/";
    String shpeIn = "E:/temp/shp3D/building13.shp";
    
    


    List<Cuboid2> lCuboid = LoaderCuboid2.loadFromShapeFile(shpeIn);
    
    
    IFeatureCollection<IFeature> featColl = new FT_FeatureCollection<>();
    
    List<AbstractBuilding> lAB = loadBuilding(lCuboid);
    featColl.addAll(fusionneGeom(lAB));

    ShapefileWriter.write(featColl, strShpOut + "test2.shp");
    
    /*
 
    
     
     Parameters p = initialize_parameters();
    Environnement env = LoaderSHP.load(p.get("folder"));
    BasicPropertyUnit bpu = env.getBpU().get(1);

    String configPath = p.get("config_shape_file").toString();

    VeryFastRuleChecker vFR = new VeryFastRuleChecker(bpu);

    CacheModelInstance<AbstractBuilding> cMI = new CacheModelInstance<AbstractBuilding>(bpu, vFR
        .getlModeInstance().get(0));

    // /La configuration est prête

    List<AbstractBuilding> lAB = loadBuilding(lCuboid);

    boolean check = vFR.check(cMI.update(lAB, new ArrayList<AbstractBuilding>()));

    if (check) {

      // Changement de la hauteur
      lAB = changeHeight(lAB, 1, vFR, cMI);

      IFeatureCollection<IFeature> featColl = new FT_FeatureCollection<>();

      featColl.addAll(lAB);

      ShapefileWriter.write(featColl, strShpOut + "test1.shp");
      featColl.clear();
      // Fusion des géométries
      featColl.addAll(fusionneGeom(lAB));

      ShapefileWriter.write(featColl, strShpOut + "test2.shp");

    } else {
      System.out.println("pas check ?");
    }*/

  }

  private static IFeatureCollection<IFeature> fusionneGeom(List<? extends AbstractBuilding> lAB) {

    int nbBat = lAB.size();

    // Conversion des géométries
    List<IOrientableSurface> lGeom = new ArrayList<>();

    List<Double> lMinZ = new ArrayList<>();
    List<Double> lMaxZ = new ArrayList<>();

    for (AbstractBuilding aB : lAB) {
      
      
      Box3D b = new Box3D(aB.getGeom());

      lGeom.add(aB.getFootprint());
      lMinZ.add(b.getLLDP().getZ());
      lMaxZ.add(b.getURDP().getZ());

    }

    for (int i = 0; i < nbBat; i++) {

      IOrientableSurface abi = lGeom.get(i);

      for (int j = i + 1; j < nbBat; j++) {

        IOrientableSurface abj = lGeom.get(j);

        if (!abi.intersects(abj)) {

          continue;

        }

        double zMaxi = lMaxZ.get(i);
        double zMini = lMinZ.get(i);

        double zMaxj = lMaxZ.get(j);
        double zMinj = lMinZ.get(j);

        int indexi, indexj;

        if (zMaxi > zMaxj) {

          indexi = j;
          indexj = i;

          zMaxi = zMaxj;
          zMini = zMinj;
        } else {
          indexi = i;
          indexj = j;
        }

        // indexi est forcément plus bas que indexj

        IOrientableSurface geomTemp = lGeom.get(indexi);

        IGeometry geom = geomTemp.difference(lGeom.get(indexj));

        if (geom.area() < 0.001) {
          continue;
        }

        // on a la géométrie qui va remplacer i

        nbBat--;
        lGeom.remove(indexi);
        lMinZ.remove(indexi);
        lMaxZ.remove(indexi);

        List<IOrientableSurface> lOS = FromGeomToSurface.convertGeom(geom);

        for (IOrientableSurface os : lOS) {

          for (IDirectPosition dp : os.coord()) {
            dp.setZ(0);
          }

          lGeom.add(os);
          lMinZ.add(zMini);
          lMaxZ.add(zMaxi);

          nbBat++;

        }

      }

    }

    IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();

    for (int i = 0; i < nbBat; i++) {

      ISolid sol = (ISolid) Extrusion2DObject.convertFromGeometry(lGeom.get(i), lMinZ.get(i),
          lMaxZ.get(i));
      featC.add(new DefaultFeature(new GM_MultiSurface<>(sol.getFacesList())));
    }

    System.out.println("NB geom : " + lGeom.size());

    System.out.println("zMin : " + lMinZ.size());

    System.out.println("zMax : " + lMaxZ.size());

    // TODO Auto-generated method stub
    return featC;
  }

  public static List<AbstractBuilding> loadBuilding(List<Cuboid2> lC) {

    List<AbstractBuilding> lAB = new ArrayList<>();

    for (Cuboid2 c : lC) {
      Building bP = new Building(new GM_MultiSurface<>(GenerateSolidFromCuboid.generate(c)
          .getFacesList()));

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

  public static List<AbstractBuilding> changeHeight(List<AbstractBuilding> lAB, double diffHeight,
      VeryFastRuleChecker vFR, CacheModelInstance<AbstractBuilding> cMI) {

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
        if ((hMinj <= hMaxi && hMinj >= hMini) || ((hMaxj >= hMini) && (hMaxj <= hMaxi))) {

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

  public static AbstractBuilding changeGeomZMax(AbstractBuilding aBIni, double zMaxNew) {

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

  public static List<AbstractBuilding> changeHeight2(List<AbstractBuilding> lAB, double diffHeight,
      VeryFastRuleChecker vFR, CacheModelInstance<AbstractBuilding> cMI) {

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

  public static AbstractBuilding changeGeomHMax(AbstractBuilding aBIni, Box3D b, double currentH,
      double newH) {

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
   * @param p
   *        paramètres importés depuis le fichier XML
   * @param bpu
   *        l'unité foncière considérée
   * @return la configuration chargée, c'est à dire la formulation énergétique
   *         prise en compte
   */
  public static Configuration<Cuboid2> create_configuration(Parameters p, Geometry bpu) {

    // Énergie constante : à la création d'un nouvel objet
    ConstantEnergy<Cuboid2, Cuboid2> energyCreation = new ConstantEnergy<Cuboid2, Cuboid2>(
        Double.parseDouble(p.get("energy")));

    // Énergie constante : pondération de l'intersection
    ConstantEnergy<Cuboid2, Cuboid2> ponderationVolume = new ConstantEnergy<Cuboid2, Cuboid2>(
        Double.parseDouble(p.get("ponderation_volume")));

    // Énergie unaire : aire dans la parcelle
    UnaryEnergy<Cuboid2> energyVolume = new VolumeUnaryEnergy<Cuboid2>();
    // Multiplication de l'énergie d'intersection et de l'aire
    UnaryEnergy<Cuboid2> energyVolumePondere = new MultipliesUnaryEnergy<Cuboid2>(
        ponderationVolume, energyVolume);

    // On retire de l'énergie de création, l'énergie de l'aire
    UnaryEnergy<Cuboid2> u3 = new MinusUnaryEnergy<Cuboid2>(energyCreation, energyVolumePondere);

    // Énergie constante : pondération de la différence
    ConstantEnergy<Cuboid2, Cuboid2> ponderationDifference = new ConstantEnergy<Cuboid2, Cuboid2>(
        Double.parseDouble(p.get("ponderation_difference_ext")));
    // On ajoute l'énergie de différence : la zone en dehors de la parcelle
    UnaryEnergy<Cuboid2> u4 = new DifferenceVolumeUnaryEnergy<Cuboid2>(bpu);
    UnaryEnergy<Cuboid2> u5 = new MultipliesUnaryEnergy<Cuboid2>(ponderationDifference, u4);
    UnaryEnergy<Cuboid2> unaryEnergy = new PlusUnaryEnergy<Cuboid2>(u3, u5);

    // Énergie binaire : intersection entre deux rectangles
    ConstantEnergy<Cuboid2, Cuboid2> c3 = new ConstantEnergy<Cuboid2, Cuboid2>(Double.parseDouble(p
        .get("ponderation_volume_inter")));
    BinaryEnergy<Cuboid2, Cuboid2> b1 = new IntersectionVolumeBinaryEnergy<Cuboid2>();
    BinaryEnergy<Cuboid2, Cuboid2> binaryEnergy = new MultipliesBinaryEnergy<Cuboid2, Cuboid2>(c3,
        b1);
    // empty initial configuration*/

    Configuration<Cuboid2> conf = new GraphConfiguration<Cuboid2>(unaryEnergy, binaryEnergy);

    return conf;
  }

  private static Parameters initialize_parameters() {
    return Parameters.unmarshall("./src/main/resources/scenario/building_parameters_project_expthese_1.xml");
  }

}
