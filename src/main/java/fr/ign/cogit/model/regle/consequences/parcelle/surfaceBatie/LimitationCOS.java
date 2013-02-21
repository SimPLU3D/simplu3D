package fr.ign.cogit.model.regle.consequences.parcelle.surfaceBatie;


public class LimitationCOS {
  
  double COSMin, COSMax;
  

  

  public LimitationCOS(double cOSMin, double cOSMax) {
    super();
    COSMin = cOSMin;
    COSMax = cOSMax;
  }
  
  
  
  public static LimitationCOS createLimitationCOSMax(double COSmax){
    
    return new LimitationCOS(0, COSmax);
    
  }
  
  
  public static LimitationCOS createLimitationCOSMin(double COSmin){

    return new LimitationCOS(COSmin, Double.POSITIVE_INFINITY);
    
    
  }
  
  

  public double getCOSMin() {
    return COSMin;
  }

  public void setCOSMin(double COSMin) {
    this.COSMin = COSMin;
  }

  public double getCOSMax() {
    return COSMax;
  }

  public void setCOSMax(double COSMax) {
    this.COSMax = COSMax;
  }



  @Override
  public String toString() {
    
    StringBuffer sb = new StringBuffer("Le COS ");
    
    if(COSMin != 0)
    {
      sb.append(" doit être supérieur à ");
      sb.append(this.getCOSMin());
      
      if(! Double.isInfinite(COSMax)){
        
        sb.append(" et ");
        
      }
      
    }
    
    if(Double.isInfinite(COSMax)){
      
      sb.append(" et inférieur à " + COSMax); 
    }
    
    
    return sb.toString();
  }
  
  
}
