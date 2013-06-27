package fr.ign.cogit.simplu3d.scenario.implCubeRoof;

import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.simplu3d.generation.ParametricBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.scenario.AbstractRectangleRoof;

public abstract class BasicRectangleRoofScenario extends AbstractRectangleRoof {

  public BasicRectangleRoofScenario(BasicPropertyUnit bPU, double largMin,
      double largMax, double longMin, double longMax, double hMin, double hMax,
      double hRoofMin, double hRoofMax) {
    super(bPU);
    this.largMin = largMin;
    this.largMax = largMax;
    this.longMin = longMin;
    this.longMax = longMax;
    this.hMin = hMin;
    this.hMax = hMax;
    this.hRoofMin = hRoofMin;
    this.hRoofMax = hRoofMax;
  }

  public double gethRoofMin() {
    return hRoofMin;
  }

  public double gethRoofMax() {
    return hRoofMax;
  }

  private double largMin;
  private double largMax;
  private double longMin;
  private double longMax;
  private double hMin;
  private double hMax;
  private double hRoofMin;
  private double hRoofMax;

  public double getRanLarg() {
    return Math.random() * (largMax - largMin) + largMin;
  }

  public double getRanLon() {
    return Math.random() * (longMax - longMin) + longMin;
  }

  public double getRanHei() {

    return Math.random() * (hMax - hMin) + hMin;

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

  
  public double getRanHRoof(){
    return Math.random() * (hRoofMax - hRoofMin) + hRoofMin;
  }

  
  
  
  @Override
  public ParametricBuilding randomMove() {

    int alea = (int) (6 * Math.random());

    switch (alea) {

      case 0:
        this.currentState.changeLargeur(this.getRanLarg());
        break;

      case 1:
        this.currentState.changeHauteur(this.getRanLon());
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
        this.currentState.moveZMax(this.getRanHRoof()+ this.currentState.getzGouttiere());
        break;
      default:
        this.currentState.moveGouttiere(this.getRanHei());
        break;

    }

    return this.currentState;

  }
  
  

}
