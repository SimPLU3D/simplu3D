package fr.ign.cogit.model.regle.consequences.batiment.angleToit;


import fr.ign.cogit.model.regle.consequences.Consequence;


public class LimitationAngle extends Consequence {

  double angleMin, angleMax;
  

  

  public LimitationAngle(double angleMin, double angleMax) {
    super();
    this.angleMin = angleMin;
    this.angleMax = angleMax;
  }
  
  
  
  public static LimitationAngle createLimitationAngleMax(double angleMax){
    
    return new LimitationAngle(0, angleMax);
    
  }
  
  
  public static LimitationAngle createLimitationAngleMin(double angleMin){

    return new LimitationAngle(angleMin, Double.POSITIVE_INFINITY);
    
    
  }
  


  public double getAngleMin() {
    return angleMin;
  }



  public void setAngleMin(double angleMin) {
    this.angleMin = angleMin;
  }



  public double getAngleMax() {
    return angleMax;
  }



  public void setAngleMax(double angleMax) {
    this.angleMax = angleMax;
  }



  @Override
  public String toString() {
    
    StringBuffer sb = new StringBuffer("L'angle ");
    
    if(angleMin != 0)
    {
      sb.append(" minimal du toit doit être supérieur à ");
      sb.append(this.getAngleMin());
      
      if(! Double.isInfinite(angleMax)){
        
        sb.append(" et l'angle  ");
        
      }
      
    }
    
    if(Double.isInfinite(angleMax)){
      
      sb.append(" maximal du toit supérieur " + angleMax); 
    }
    
    
    return sb.toString();
  }
  
  
  
  
  
  
  
  

}
