package fr.ign.cogit.simplu3d.experiments.openmole.diversity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.media.j3d.Sound;

import java.util.HashMap;


import java.lang.Math;
import java.lang.reflect.Array;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.loader.LoaderCuboid;
import fr.ign.mpp.configuration.GraphConfiguration;

public class EntropyCalculation {

  List<Cuboid> cuboids;

  public EntropyCalculation(List<Cuboid> lCuboids) {
    cuboids = lCuboids;
  }

  
  public EntropyCalculation(GraphConfiguration<Cuboid> graphCuboid) {
    cuboids = new ArrayList<>();
    for (Cuboid cc : graphCuboid) {
      cuboids.add(cc);
    }
  }


  public Double getHeightEntropy() {
    return getHeightEntropy(1);
  }
  
  
  public Double getHeightEntropy(Integer binwidth) {
    HashMap<Double, Integer> histoHeights = new HashMap<Double, Integer>();


    // populate hashmap according to rounded height 
    for(Cuboid cucube : cuboids) {
      Double height = cucube.getHeight();
      Double  binarizedHeight =  (double)  Math.rint(height / binwidth) * binwidth;
      
      if (histoHeights.containsKey(binarizedHeight)) {
        histoHeights.put(binarizedHeight, histoHeights.get(binarizedHeight) + 1);          
      }
      else {
        histoHeights.put(binarizedHeight, 1);
      }
    }


    // number of heights to consider = number of cuboids
    int nbHeights= cuboids.size();
    // number of distinct heights =  number of key in the hahmap
    int nbDistinctHeights = histoHeights.keySet().size();
    
   
    
    //shannon entropy is -sum (pi log2(pi))
    // probability of having an height h equal to H = number of H in hashmap / number of heights 
    Double shannonE = 0.0 ;
    for (Double k  : histoHeights.keySet()) {
      Double probaHeight = (double)histoHeights.get(k) / (double)nbHeights ;
      //  System.out.println("proba de la hauteur :"+k +" :" + probaHeight);
      shannonE += shannonE + (probaHeight * Math.log10(probaHeight)/Math.log10(2));
    }

    shannonE *= -1 ; 
    

    if (cuboids.isEmpty()) {
      System.out.println("Empty list of cuboids ! ");
      return -999.999 ;
    }
    else {
      return shannonE ;  
    }

  }

  
  public static void main(String[] args) {
System.out.println("load config");
    List<Cuboid> lC = LoaderCuboid.loadFromShapeFile("/home/paulchapron/dev/simplu3D-openmole/visuPSE/PSEshp/run_-2085940094869048084out.shp");
  //  List<Cuboid> lC = LoaderCuboid.loadFromShapeFile("/home/paulchapron/dev/simplu3D-openmole/visuPSE/config.shp");
    System.out.println("load surrounding buildings");
    List<Cuboid> surroundingFabric = LoaderCuboid.loadFromShapeFile("/home/paulchapron/dev/visuPSEPremium/scriptQGIS_genThree.js/Bati_zone/batiment.shp");
     
    
    
    
    System.out.println("calcul de l'entropie des hauteurs de la config");
    EntropyCalculation eC = new EntropyCalculation(lC);
    double configEntropy = eC.getHeightEntropy(2);

    EntropyCalculation ecSurround = new EntropyCalculation(surroundingFabric) ;
    Double surroundEntropy = ecSurround.getHeightEntropy();
    
    System.out.println("Entropie des hauteurs de la config: " + configEntropy);
    System.out.println("Entropie des hauteurs du tissu envirronant: " + surroundEntropy);
    
    
  }

}
