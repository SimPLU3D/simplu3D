package fr.ign.cogit.simplu3d.experiments.openmole.diversity;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.loader.LoaderCuboid;
import fr.ign.mpp.configuration.GraphConfiguration;

public class ShonCalculation {

  public static void main(String[] args) {

    List<Cuboid> lC = LoaderCuboid.loadFromShapeFile(
        "/home/mickael/data/mbrasebin/these_Mickael/Exp3/result1/restrictedresult/shp_6.0_ 2.5_0_ene-56068.127520271904.shp");

    ShonCalculation sC = new ShonCalculation(lC);
    double shon = sC.getShon();
    System.out.println("Shon : " + shon);

  }

  List<Cuboid> cuboids;

  public ShonCalculation(List<Cuboid> lCuboids) {

    cuboids = lCuboids;

  }

  public ShonCalculation(GraphConfiguration<Cuboid> graphCuboid) {

    cuboids = new ArrayList<>();

    for (Cuboid cc : graphCuboid) {
      cuboids.add(cc);
    }

  }

  public double getShon() {

    double shon = 0;

    for (Cuboid c : cuboids) {

      List<Cuboid> intersectedCuboid = new ArrayList<Cuboid>();

      for (Cuboid cTemp : cuboids) {

        if (c.equals(cTemp)) {
          continue;
        }

        if (c.getRectangle2D().intersectionArea(cTemp.getRectangle2D()) < 0.1) {
          continue;
        }

        if (c.getHeight() > cTemp.getHeight()) {
          continue;
        }

        intersectedCuboid.add(cTemp);

      }

      IGeometry geom = c.getFootprint();

      for (Cuboid cTemp : intersectedCuboid) {

        if (geom == null || geom.isEmpty()) {
          break;

        }

        geom = geom.difference(cTemp.getFootprint());

      }

      if (!(geom == null) && !(geom.isEmpty())) {
        shon = shon + geom.area() * (c.height / 3);
      }

    }

    return shon;
  }

}
