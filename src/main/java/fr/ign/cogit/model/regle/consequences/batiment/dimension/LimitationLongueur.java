package fr.ign.cogit.model.regle.consequences.batiment.dimension;

import fr.ign.cogit.model.regle.consequences.Consequence;

public class LimitationLongueur extends Consequence {

  double longueurMin, distance;

  public LimitationLongueur(double longueurMin, double distance) {
    super();
    this.longueurMin = longueurMin;
    this.distance = distance;
  }

  public double getlongueurMin() {
    return longueurMin;
  }

  public void setlongueurMin(double longueurMin) {
    this.longueurMin = longueurMin;
  }

  public double getdistance() {
    return distance;
  }

  public void setdistance(double distance) {
    this.distance = distance;
  }

  @Override
  public String toString() {

    StringBuffer sb = new StringBuffer("La largeur ");

    if (getlongueurMin() != 0) {
      sb.append(" minimale du bâtiment doit être supérieur à ");
      sb.append(this.getlongueurMin());

      if (!Double.isInfinite(getdistance())) {

        sb.append(" et la largeur  ");

      }

    }

    if (Double.isInfinite(getdistance())) {

      sb.append(" maximale du bâtiment doit être inférieure à " + getdistance());
    }

    return sb.toString();
  }

}
