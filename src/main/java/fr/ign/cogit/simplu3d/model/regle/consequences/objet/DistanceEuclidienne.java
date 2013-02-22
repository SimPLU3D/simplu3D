package fr.ign.cogit.simplu3d.model.regle.consequences.objet;

import fr.ign.cogit.simplu3d.model.regle.consequences.Consequence;

public class DistanceEuclidienne extends Consequence {

  double distanceMin, distanceMax;

  public DistanceEuclidienne(double distanceMin, double distanceMax) {
    super();
    this.distanceMin = distanceMin;
    this.distanceMax = distanceMax;
  }

  public static DistanceEuclidienne createLimitationdistanceMax(
      double distancemax) {

    return new DistanceEuclidienne(0, distancemax);

  }

  public static DistanceEuclidienne createLimitationdistanceMin(
      double distancemin) {

    return new DistanceEuclidienne(distancemin, Double.POSITIVE_INFINITY);

  }

  public double getdistanceMin() {
    return distanceMin;
  }

  public void setdistanceMin(double distanceMin) {
    this.distanceMin = distanceMin;
  }

  public double getdistanceMax() {
    return distanceMax;
  }

  public void setdistanceMax(double distanceMax) {
    this.distanceMax = distanceMax;
  }

  @Override
  public String toString() {

    StringBuffer sb = new StringBuffer("Le distance ");

    if (distanceMin != 0) {
      sb.append(" doit être supérieur à ");
      sb.append(this.getdistanceMin());

      if (!Double.isInfinite(distanceMax)) {

        sb.append(" et ");

      }

    }

    if (Double.isInfinite(distanceMax)) {

      sb.append(" et inférieur à " + distanceMax);
    }

    return sb.toString();
  }

}
