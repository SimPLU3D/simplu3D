package fr.ign.cogit.simplu3d.scenario.implLScenario;

import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.simplu3d.generation.ParametricBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.scenario.AbstractLScenario;

public abstract class BasicLScenario extends AbstractLScenario {

  public BasicLScenario(BasicPropertyUnit bPU, double largMin, double largMax,
      double longMin, double longMax, double hMin, double hMax,
   double largMin2, double largMax2,
      double hMin2, double hMax2) {
    super(bPU);
    this.largMin = largMin;
    this.largMax = largMax;
    this.longMin = longMin;
    this.longMax = longMax;
    this.hMin = hMin;
    this.hMax = hMax;
    this.hMin2 = hMin2;
    this.hMax2 = hMax2;
    this.largMin2 = largMin2;
    this.largMax2 = largMax2;
    this.hMin2 = hMin2;
    this.hMax2 = hMax2;
  }

  private double largMin;
  private double largMax;
  private double longMin;
  private double longMax;
  private double hMin;
  private double hMax;
  private double hRoofMin;
  private double hRoofMax;
  private double largMin2;
  private double largMax2;
  private double hMin2;
  private double hMax2;

  public double getRanLarg() {
    return Math.random() * (largMax - largMin) + largMin;
  }

  public double getRanLon() {
    return Math.random() * (longMax - longMin) + longMin;
  }

  public double getRanHei() {

    return Math.random() * (hMax - hMin) + hMin;

  }

  public double getLargMin2() {
    return largMin2;
  }

  public double getLargMax2() {
    return largMax2;
  }

  public double gethMin2() {
    return hMin2;
  }

  public double gethMax2() {
    return hMax2;
  }

  public double gethRoofMin() {
    return hRoofMin;
  }

  public double gethRoofMax() {
    return hRoofMax;
  }

  public double getRanOrientation() {
    return Math.random() * 2 * Math.PI;
  }

  public double getLargMin() {
    return largMin;
  }

  public void setLargMin(double largMin) {
    this.largMin = largMin;
  }

  public double getLargMax() {
    return largMax;
  }

  public void setLargMax(double largMax) {
    this.largMax = largMax;
  }

  public double getLongMin() {
    return longMin;
  }

  public void setLongMin(double longMin) {
    this.longMin = longMin;
  }

  public double getLongMax() {
    return longMax;
  }

  public void setLongMax(double longMax) {
    this.longMax = longMax;
  }

  public double gethMin() {
    return hMin;
  }

  public void sethMin(double hMin) {
    this.hMin = hMin;
  }

  public double gethMax() {
    return hMax;
  }

  public void sethMax(double hMax) {
    this.hMax = hMax;
  }

  public double getRanHRoof() {
    return Math.random() * (hRoofMax - hRoofMin) + hRoofMin;
  }

  @Override
  public ParametricBuilding randomMove() {

    int alea = (int) (7 * Math.random());

    switch (alea) {

      case 0:
        double newLargeur = this.getRanLarg();
        if (newLargeur < currentState.getLargeur2()) {
          currentState.setLargeur2(newLargeur);
        }
        this.currentState.changeLargeur(newLargeur);
        break;
      case 1:
        double newHauteur = this.getRanLon();
        if (newHauteur < currentState.getHauteur2()) {
          currentState.setHauteur2(newHauteur);
        }   
        this.currentState.changeHauteur(newHauteur);
        break;
      case 2:
        this.currentState.rotate(this.getRanOrientation());
        break;
      case 3:
        int nbP = grid.size();
        this.currentState.translate(new Vecteur(this.currentState.getCentre(),
            this.grid.get((int) (nbP * Math.random()))).getCoord());
        break;
      case 4:
        double newHauteur2 = getRanH2();
        if (newHauteur2 > currentState.getHauteur()) {
          currentState.setHauteur2(currentState.getHauteur());
        }   
        this.currentState.changeHauteur2(newHauteur2);
        break;
      case 5:
        double newLargeur2 = this.getRanLarg2();
        if (newLargeur2 > currentState.getLargeur()) {
          currentState.setLargeur2(currentState.getLargeur());
        }
        this.currentState.changeLargeur2(newLargeur2);
        break;
      default:
        this.currentState.moveGouttiere(this.getRanHei());
        break;

    }

    return this.currentState;

  }

  public double getRanLarg2() {
    return Math.random() * (largMax2 - largMin2) + largMin2;
  }

  public double getRanH2() {
    return Math.random() * (hMax2 - hMin2) + hMin2;
  }

}
