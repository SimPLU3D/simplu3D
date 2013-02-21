package fr.ign.cogit.model.regle.consequences.batiment.dimension;

import fr.ign.cogit.model.regle.consequences.Consequence;

public class LimitationLargeur extends Consequence{
  
  
  double largeurMin,largeurMax;

  public LimitationLargeur(double largeurMin, double largeurMax) {
    super();
    this.largeurMin = largeurMin;
    this.largeurMax = largeurMax;
  }

  public double getLargeurMin() {
    return largeurMin;
  }

  public void setLargeurMin(double largeurMin) {
    this.largeurMin = largeurMin;
  }

  public double getLargeurMax() {
    return largeurMax;
  }

  public void setLargeurMax(double largeurMax) {
    this.largeurMax = largeurMax;
  }

  @Override
  public String toString() {
    
    StringBuffer sb = new StringBuffer("La largeur ");
    
    if(getLargeurMin() != 0)
    {
      sb.append(" minimale du bâtiment doit être supérieur à ");
      sb.append(this.getLargeurMin());
      
      if(! Double.isInfinite(getLargeurMax())){
        
        sb.append(" et la largeur  ");
        
      }
      
    }
    
    if(Double.isInfinite(getLargeurMax())){
      
      sb.append(" maximale du bâtiment doit être inférieure à " + getLargeurMax()); 
    }
    
    
    return sb.toString();
  }
  

}
