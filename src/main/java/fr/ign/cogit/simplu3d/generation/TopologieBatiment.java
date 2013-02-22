package fr.ign.cogit.simplu3d.generation;

import java.util.List;

public class TopologieBatiment {

  public enum FormeEmpriseEnum {
    FORME_T,  RECTANGLE, CERCLE, FORME_U, FORME_L  ;

    
    //Nombre de possibilité pour une arrête en appentis
    public int getPossibleArcAppentis() {

      if (this.equals(FORME_U) || this.equals(RECTANGLE)) {
        return 2;
      }

      if (this.equals(FORME_T)
          || this.equals(FORME_L)) {
        return 4;
      }

      if (this.equals(CERCLE)) {
        return 1;
      }

      return -1;

    }

    //Nombre de possibilité pour une arrête en toit symétrique
    public int getPossibleArcSymetrique() {
      if(this.equals(FORME_U)){
        return 5;
      }
      
      if (this.equals(RECTANGLE)) {
        return 4;
      }

      if ( this.equals(FORME_T)
          || this.equals(FORME_L)) {
        return 4;
      }

      if (this.equals(CERCLE)) {
        return 2;
      }

      return -1;

    }

  }

  public enum FormeToitEnum {
    PLAT, EN_APPENTIS, SYMETRIQUE;
  }
  
  private FormeEmpriseEnum fE;
  private FormeToitEnum fT;
  private List<Integer> lIndArret;
  
  
  
  public TopologieBatiment(FormeEmpriseEnum fE, FormeToitEnum fT,
      List<Integer> lIndArret) {
    super();
    this.fE = fE;
    this.fT = fT;
    this.lIndArret = lIndArret;
  }
  public FormeEmpriseEnum getfE() {
    return fE;
  }
  public FormeToitEnum getfT() {
    return fT;
  }
  public List<Integer> getlIndArret() {
    return lIndArret;
  }
  

  
  
  @Override
  public boolean equals(Object o){
    
   
    
    if(!( o instanceof TopologieBatiment)){
      return false;
    }
    
    TopologieBatiment b2 = (TopologieBatiment)o;
    
    if(! this.getfE().equals(b2.getfE())){
      return false;
    }
    
    
    if(! this.getfT().equals(b2.getfT())){
      return false;
    }
    
    
    
    for(Integer i : this.getlIndArret()){
      
      if(! b2.getlIndArret().contains(i)){
        return false;
      }
      
      
      
      
    }
    
    for(Integer i : b2.getlIndArret()){
      
      if(! this.getlIndArret().contains(i)){
        return false;
      }

      
      
    }

    return true;
    
  }
  public void setfE(FormeEmpriseEnum fE) {
    this.fE = fE;
  }
  public void setfT(FormeToitEnum fT) {
    this.fT = fT;
  }
  public void setlIndArret(List<Integer> lIndArret) {
    this.lIndArret = lIndArret;
  }
  
  
  

}
