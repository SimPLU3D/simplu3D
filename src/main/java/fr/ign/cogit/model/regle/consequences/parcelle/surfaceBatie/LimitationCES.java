package fr.ign.cogit.model.regle.consequences.parcelle.surfaceBatie;

import fr.ign.cogit.model.regle.consequences.Consequence;

public class LimitationCES extends Consequence{
  
  
  double CESMin, CESMax;
  

  

  public LimitationCES(double cESMin, double cESMax) {
    super();
    CESMin = cESMin;
    CESMax = cESMax;
  }
  
  
  
  public static LimitationCES createLimitationCESMax(double cesmax){
    
    return new LimitationCES(0, cesmax);
    
  }
  
  
  public static LimitationCES createLimitationCESMin(double cesmin){

    return new LimitationCES(cesmin, Double.POSITIVE_INFINITY);
    
    
  }
  
  

  public double getCESMin() {
    return CESMin;
  }

  public void setCESMin(double cESMin) {
    CESMin = cESMin;
  }

  public double getCESMax() {
    return CESMax;
  }

  public void setCESMax(double cESMax) {
    CESMax = cESMax;
  }



  @Override
  public String toString() {
    
    StringBuffer sb = new StringBuffer("Le CES ");
    
    if(CESMin != 0)
    {
      sb.append(" doit être supérieur à ");
      sb.append(this.getCESMin());
      
      if(! Double.isInfinite(CESMax)){
        
        sb.append(" et ");
        
      }
      
    }
    
    if(Double.isInfinite(CESMax)){
      
      sb.append(" et inférieur à " + CESMax); 
    }
    
    
    return sb.toString();
  }
  
  
  
  
  
  
  
  
  
  
  
  
  

}
