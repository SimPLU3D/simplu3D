package fr.ign.cogit.simplu3d.util;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;

public class Util {

  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub

  }

  
  public static double distance(IGeometry geom1, IGeometry geom2){
    
    System.out.println(geom1.distance(geom2));
    return geom1.distance(geom2);
  }
}
