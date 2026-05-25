package fr.ign.cogit.simplu3d.experiments.openmole.diversity;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

import java.util.HashMap;
import java.io.File;
import java.lang.Math;

import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.loader.LoaderCuboid;
import fr.ign.common.io.ShapefileReader;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.feature.DefaultFeatureCollection;

public class EntropyCalculation {


  public static Double ShannonEntropyCuboidsHeight(List<Cuboid> lC) {
    return ShannonEntropyCuboidsHeight(1, lC);
  }

  public static Double ShannonEntropyCuboidsHeight(Integer binwidth, List<Cuboid> lC) {
    List<Double> heights = new ArrayList<>();
    for (Cuboid cc : lC) {
      heights.add(cc.getHeight());
    }
    return shannonEntropy(binwidth, heights);
  }


  public static Double ShannonEntropyFeaturesHeight(DefaultFeatureCollection lF) {
    return ShannonEntropyFeaturesHeight(1, lF);
  }

  public static Double ShannonEntropyFeaturesHeight(Integer binwidth, DefaultFeatureCollection lF) {
    List<Double> heights = new ArrayList<>();
    for (SimpleFeature batiment : lF) {
      Double height = ((Long) batiment.getAttribute("hauteur")).doubleValue();
      heights.add(height);
    }
    return shannonEntropy(binwidth, heights);
  }

  // return shannon entropy of a list of dimension values e.g. heights
  public static Double shannonEntropy(Integer binwidth, List<Double> dims) {
    HashMap<Double, Integer> histoDims = new HashMap<Double, Integer>();
    for (Double dim : dims) {
      Double binarizedDim = Math.rint(dim / binwidth) * binwidth;
      if (histoDims.containsKey(binarizedDim)) {
        histoDims.put(binarizedDim, histoDims.get(binarizedDim) + 1);
      } else {
        histoDims.put(binarizedDim, 1);
      }
    }
    // shannon entropy is -sum (pi log2(pi))
    // probability of having an height h equal to H = number of H in hashmap /
    // number of heights
    Double shannonE = 0.0;
    for (Double k : histoDims.keySet()) {
      Double probaHeight = (double) histoDims.get(k) / (double) dims.size();
      shannonE += probaHeight * (Math.log(probaHeight) / Math.log(2));
    }
    return -shannonE;
  }


  public static Double binarize(Double d, Integer binwidth) {
    return Math.rint(d / binwidth) * binwidth;
  }

  //compute the joint entropy measure
  public static Double jointEntropyIndepVars(List<Double> X, List<Double> Y) {
    double jointE = 0.0;
    HashMap<Double, Integer> mapX = new HashMap<>();
    HashMap<Double, Integer> mapY = new HashMap<>();
    for (Double x : X) {
      if (mapX.containsKey(x)) {
        mapX.put(x, mapX.get(x) + 1);
      } else {
        mapX.put(x, 1);
      }
    }
    for (Double y : Y) {
      if (mapY.containsKey(y)) {
        mapY.put(y, mapY.get(y) + 1);
      } else {
        mapY.put(y, 1);
      }
    }
    //calcul de la proba jointe X,Y : px* py ???
    // entropyE = - sum x sum y p(x,y)log(p(x,y))
    for (Double x : mapX.keySet()) {
      double px = (double) mapX.get(x) / (double) X.size();
      for (Double y : mapY.keySet()) {
        Double py = (double) mapY.get(y) / (double) Y.size();
        jointE += -py * px * (Math.log(py * px) / Math.log(2));
      }
    }
    return jointE;
  }

  //
  public static Double KLdivXvsY(List<Double> X, List<Double> Y) {
    // Count every value occurences in the two lists
    HashMap<Double, Integer> mapX = new HashMap<>();
    HashMap<Double, Integer> mapY = new HashMap<>();
    for (Double x : X) {
      if (mapX.containsKey(x)) {
        mapX.put(x, mapX.get(x) + 1);
      } else {
        mapX.put(x, 1);
      }
    }
    for (Double y : Y) {
      if (mapY.containsKey(y)) {
        mapY.put(y, mapY.get(y) + 1);
      } else {
        mapY.put(y, 1);
      }
    }
    // proba distribution for x and y
    HashMap<Double, Double> pX = new HashMap<>();
    HashMap<Double, Double> pY = new HashMap<>();
    for (Double x : mapX.keySet()) {
      pX.put(x, mapX.get(x).doubleValue() / X.size());
    }
    for (Double y : mapY.keySet()) {
      pY.put(y, mapY.get(y).doubleValue() / Y.size());
    }
    // merge keys to constitute universe values of X and Y probabilities 
    HashSet<Double> universe = new HashSet<>();
    universe.addAll(mapX.keySet());
    universe.addAll(mapY.keySet());
//    System.out.println("clés X" + mapX.keySet());
//    System.out.println("clés Y" + mapY.keySet());
//    System.out.println("clés univers" + universe);
    double KLdivXY = 0.0;
    for (Double k : universe) {
      if (pY.containsKey(k) && pX.containsKey(k)) {
        KLdivXY += pX.get(k) * Math.log(pX.get(k) / pY.get(k)) / Math.log(2);
      }
    }
    return KLdivXY;
  }

  public static List<Double> dimsExtractor(DefaultFeatureCollection collFeat, String dimName) {
    List<Double> dims = new ArrayList<>();
    for (SimpleFeature feat : collFeat) {
      dims.add((double) feat.getAttribute(dimName));
    }
    return dims;
  }

  public static List<Double> dimsExtractor(List<Cuboid> cuboids, String dimName) {
    List<Double> dims = new ArrayList<>();
    for (Cuboid cc : cuboids) {
      switch (dimName) {
        case "hauteur":
          dims.add(cc.getHeight());
          break;

        default:
          break;
      }
    }
    return dims;
  }

  public static void main(String[] args) throws MalformedURLException {
    System.out.println("load config");
    List<Cuboid> config = LoaderCuboid.loadFromShapeFile(
            "/home/paulchapron/dev/simplu3D-openmole/visuPSE/PSEshp/run_-2085940094869048084out.shp");
    // List<Cuboid> lC =
    // LoaderCuboid.loadFromShapeFile("/home/paulchapron/dev/simplu3D-openmole/visuPSE/config.shp");
    System.out.println("load surrounding buildings");
//    FT_FeatureCollection<IFeature> surroundingFabric = ShapeFileLoader
//        .loadingShapeFile(
//            "/home/paulchapron/dev/visuPSEPremium/scriptQGIS_genThree.js/Bati_zone/batiment.shp",
//            "", "hauteur", true);
    DefaultFeatureCollection surroundingFabric = ShapefileReader.read(new File("/home/paulchapron/Bureau/titi.shp").toURI().toURL());
    double configEntropy = ShannonEntropyCuboidsHeight(1, config);
    System.out.println("Entropie des hauteurs de la config: " + configEntropy);
    Double surroundEntropy = ShannonEntropyFeaturesHeight(1, surroundingFabric);
    System.out.println(surroundingFabric.size() + " batiments environnants");
    System.out.println(
            "Entropie des hauteurs de " + surroundingFabric.size() + " batiments environnants : " + surroundEntropy);
    System.out.println("#####entropie jointe####");
    List<Double> surroundingHeights = new ArrayList<>();
    for (SimpleFeature batiment : surroundingFabric) {
      Double height = ((Long) batiment.getAttribute("hauteur")).doubleValue();
      surroundingHeights.add(binarize(height, 1));
    }
    System.out.println(surroundingHeights.size() + " hauteurs pour Y");
    List<Double> configHeights = new ArrayList<>();
    for (Cuboid cc : config) {
      configHeights.add(binarize(cc.getHeight(), 1));
    }
    System.out.println(configHeights.size() + " hauteurs pour X");
    Double kl = KLdivXvsY(configHeights, surroundingHeights);
    System.out.println("divergence de Kullbakc Liebler   " + kl);
    System.out.println("########################################################");
    File path = new File("/home/paulchapron/dev/simplu3D-openmole/visuPSE/PSEshp/");
    File klminSHPSoFar = new File("/home/paulchapron/dev/simplu3D-openmole/visuPSE/PSEshp/run_-2085940094869048084out.shp");
    Double kldminSoFar = Double.POSITIVE_INFINITY;
    File klmaxSHPSoFar = new File("/home/paulchapron/dev/simplu3D-openmole/visuPSE/PSEshp/run_-2085940094869048084out.shp");
    Double kldmaxSoFar = Double.NEGATIVE_INFINITY;
    File[] files = path.listFiles();
    for (int i = 0; i < files.length; i++) {
      if (files[i].isFile() && files[i].getName().endsWith(".shp")) {
        List<Cuboid> conf = LoaderCuboid.loadFromShapeFile(files[i].getPath());
        if (conf.size() == 0) {
          System.out.println("=#=#=#=#=#=#=#=#=#=#=#=#config vide  !!!");
        }
        List<Double> confHeights = new ArrayList<>();
        for (Cuboid cc : conf) {
          confHeights.add(binarize(cc.getHeight(), 1));
        }
        Double kld = KLdivXvsY(confHeights, surroundingHeights);
        if (kld < kldminSoFar) {
          System.out.println("divergence de Kullbakc Liebler minimum pour l'instant " + kld + "  pour " + files[i].getName() + " " + i + "/" + files.length);
          klminSHPSoFar = files[i];
          kldminSoFar = kld;
        }
        if (kld > kldmaxSoFar) {
          System.out.println("divergence de Kullbakc Liebler maximum pour l'instant " + kld + "  pour " + files[i].getName() + " " + i + "/" + files.length);
          klmaxSHPSoFar = files[i];
          kldmaxSoFar = kld;
        }
      }
    }
    System.out.println("ayé ! \n fichier de divergence mini par rapport à autour : " + klminSHPSoFar.getName());
    System.out.println("fichier de divergence maxi par rapport à autour : " + klmaxSHPSoFar.getName());
    System.exit(0);
  }
}
