package fr.ign.cogit.model.regle.consequences.batiment.emprise;

import fr.ign.cogit.model.regle.consequences.Consequence;

public class LimitationEmprise extends Consequence{
  
  private double aireMin, aireMax;
  
  public LimitationEmprise(double aireMin, double aireMax) {
    super();
    this.aireMin = aireMin;
    this.aireMax = aireMax;
  }
  
  public LimitationEmprise createLimitationEmpriseMin(double aireMin){
    return new LimitationEmprise(aireMin, Double.POSITIVE_INFINITY);   
  }
  
  
  public LimitationEmprise createLimitationEmpriseMax(double aireMax){
    return new LimitationEmprise(Double.POSITIVE_INFINITY, aireMax);   
  }
  

  public double getAireMin() {
    return aireMin;
  }

  public void setAireMin(double aireMin) {
    this.aireMin = aireMin;
  }

  public double getAireMax() {
    return aireMax;
  }

  public void setAireMax(double aireMax) {
    this.aireMax = aireMax;
  }

  @Override
  public String toString() {
    
    StringBuffer sb = new StringBuffer("L'aire du bâtiment ");
    
    if(aireMin != 0)
    {
      sb.append(" doit être supérieure à ");
      sb.append(this.getAireMin());
      
      if(! Double.isInfinite(this.getAireMax())){
        
        sb.append(" et   ");
        
      }
      
    }
    
    if(Double.isInfinite(aireMax)){
      
      sb.append(" doit être inférieure à " + this.getAireMax()); 
    }
    
    return sb.toString();
    
    
  }
    
  
  
}
