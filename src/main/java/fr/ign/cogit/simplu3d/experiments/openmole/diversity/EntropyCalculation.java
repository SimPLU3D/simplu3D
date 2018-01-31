package fr.ign.cogit.simplu3d.experiments.openmole.diversity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.media.j3d.Sound;

import java.util.HashMap;

import java.lang.Math;
import java.lang.reflect.Array;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.io.vector.ShapeFileLoader;
import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.loader.LoaderCuboid;
import fr.ign.mpp.configuration.GraphConfiguration;
import javassist.bytecode.analysis.Subroutine;

public class EntropyCalculation {

  List<Cuboid> individualsCuboids;
  List<IFeature> individualsFeatures;

  public EntropyCalculation(List<Cuboid> lCuboids) {
    individualsCuboids = lCuboids;
  }

  public EntropyCalculation(GraphConfiguration<Cuboid> graphCuboid) {
    individualsCuboids = new ArrayList<>();
    for (Cuboid cc : graphCuboid) {
      individualsCuboids.add(cc);
    }
  }

  public EntropyCalculation(FT_FeatureCollection<IFeature> featColl) {
    individualsFeatures = new ArrayList<>();
    for (IFeature f : featColl) {
      individualsFeatures.add(f);
    }

  }

  public Double ShannonEntropyCuboidsHeight() {
    return ShannonEntropyCuboidsHeight(1);
  }

  public Double ShannonEntropyCuboidsHeight(Integer binwidth) {
    HashMap<Double, Integer> histoHeights = new HashMap<Double, Integer>();

    // populate hashmap according to rounded height
    for (Cuboid cucube : individualsCuboids) {
      Double height = cucube.getHeight();
      Double binarizedHeight = (double) Math.rint(height / binwidth) * binwidth;

      if (histoHeights.containsKey(binarizedHeight)) {
        histoHeights.put(binarizedHeight,
            histoHeights.get(binarizedHeight) + 1);
      } else {
        histoHeights.put(binarizedHeight, 1);
      }
    }

    // number of heights to consider = number of cuboids
    int nbHeights = individualsCuboids.size();
    // number of distinct heights = number of key in the hahmap
    int nbDistinctHeights = histoHeights.keySet().size();

    // shannon entropy is -sum (pi log2(pi))
    // probability of having an height h equal to H = number of H in hashmap /
    // number of heights
    Double shannonE = 0.0;
    for (Double k : histoHeights.keySet()) {
      Double probaHeight = (double) histoHeights.get(k) / (double) nbHeights;
      shannonE += (probaHeight * (Math.log(probaHeight) / Math.log(2)));
    }

    shannonE *= -1;

    if (individualsCuboids.isEmpty()) {
      System.out.println("Empty list of cuboids ! ");
      return -999.999;
    } else {
      return shannonE;
    }

  }

  public Double ShannonEntropyFeaturesHeight() {
    return ShannonEntropyFeaturesHeight(1);
  }

  public Double ShannonEntropyFeaturesHeight(Integer binwidth) {
    HashMap<Double, Integer> histoHeights = new HashMap<Double, Integer>();

    // populate hashmap according to rounded height
    for (IFeature batiment : individualsFeatures) {
      Double height = ((Long) batiment.getAttribute("hauteur")).doubleValue();

      Double binarizedHeight = (double) Math.rint(height / binwidth) * binwidth;
      if (histoHeights.containsKey(binarizedHeight)) {
        histoHeights.put(binarizedHeight,
            histoHeights.get(binarizedHeight) + 1);
      } else {
        histoHeights.put(binarizedHeight, 1);
      }
    }

    // number of heights to consider = number of cuboids
    int nbHeights = individualsFeatures.size();

    // shannon entropy is -sum (pi log2(pi))
    // probability of having an height h equal to H = number of H in hashmap /
    // number of heights
    Double shannonE = 0.0;
    for (Double k : histoHeights.keySet()) {
      Double probaHeight = (double) histoHeights.get(k) / (double) nbHeights;

      shannonE += probaHeight * (Math.log(probaHeight) / Math.log(2));
    }

    shannonE *= -1;
    //System.out.println(histoHeights.keySet().size() + "hauteurs distinctes");

    if (individualsFeatures.isEmpty()) {
      System.out.println("Empty list of cuboids ! ");
      return -999.999;
    } else {
      return shannonE;
    }

  }

  public static void main(String[] args) {
    System.out.println("load config");
    List<Cuboid> lC = LoaderCuboid.loadFromShapeFile(
        "/home/paulchapron/dev/simplu3D-openmole/visuPSE/PSEshp/run_-2085940094869048084out.shp");
    // List<Cuboid> lC =
    // LoaderCuboid.loadFromShapeFile("/home/paulchapron/dev/simplu3D-openmole/visuPSE/config.shp");
    System.out.println("load surrounding buildings");

    FT_FeatureCollection<IFeature> surroundingFabric = ShapeFileLoader
        .loadingShapeFile(
            "/home/paulchapron/dev/visuPSEPremium/scriptQGIS_genThree.js/Bati_zone/batiment.shp",
            "", "hauteur", true);

    EntropyCalculation eC = new EntropyCalculation(lC);
    double configEntropy = eC.ShannonEntropyCuboidsHeight();
    System.out.println("Entropie des hauteurs de la config: " + configEntropy);

    EntropyCalculation ecSurround = new EntropyCalculation(surroundingFabric);
    Double surroundEntropy = ecSurround.ShannonEntropyFeaturesHeight();

    System.out.println(surroundingFabric.size() + " batiments environnants");
    System.out.println(
        "Entropie des hauteurs de "+ surroundingFabric.size() + " batiments environnants : "+ surroundEntropy);

    
    
    
    System.exit(0);
    
  }

}
