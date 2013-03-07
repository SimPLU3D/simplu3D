package fr.ign.cogit.simplu3d.fuzzy;

import java.math.BigDecimal;

public class FuzzyDouble extends BigDecimal {
  
  
  
  private double value;
  private double eT;
  
  

  public FuzzyDouble(double value, double eT){
    super(value);
    this.value = value;
    this.eT = eT;
 }
  
  public double getValue() {
    return value;
  }


  public void setValue(double value) {
    this.value = value;
  }


  public double geteT() {
    return eT;
  }


  public void seteT(double eT) {
    this.eT = eT;
  }


  
  
  

}
