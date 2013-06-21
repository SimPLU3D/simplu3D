package fr.ign.cogit.simplu3d.implantation.scenario;

import fr.ign.cogit.simplu3d.generation.ParametricBuilding;
import fr.ign.cogit.simplu3d.implantation.AbstractRectangleScenario;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;

public class BasicRectangleScenario extends AbstractRectangleScenario{
  


  public BasicRectangleScenario(BasicPropertyUnit bPU, double largMin,
      double largMax, double longMin, double longMax, double hMin, double hMax,
      double largMin2, double largMax2, double longMin2, double longMax2,
      double hMin2, double hMax2) {
    super(bPU);
    largMin = largMin2;
    largMax = largMax2;
    longMin = longMin2;
    longMax = longMax2;
    hMin = hMin2;
    hMax = hMax2;
  }

  private double largMin;
  private double largMax;
  private double longMin;
  private double longMax;
  private double hMin;
  private double hMax;

  
  
  
  
  public double getRanLarg() {
    return Math.random() * (largMax - largMin) + largMin;
  }

  public double getRanLon() {
    return Math.random() * (longMax - longMin) + longMin;
  }

  public double getRanHei() {

    return Math.random() * (hMax - hMin) + hMax;

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

  
  
  @Override
  public double satisfcation() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public ParametricBuilding randomMove() {
    // TODO Auto-generated method stub
    return null;
  }

  

}
