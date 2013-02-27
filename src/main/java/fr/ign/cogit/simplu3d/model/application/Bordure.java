package fr.ign.cogit.simplu3d.model.application;

import java.util.List;

import org.citygml4j.model.citygml.core.CityObject;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.sig3d.model.citygml.core.CG_CityObject;

public class Bordure extends CG_CityObject{
  
  public final static int FOND = 0;
  public final static int LATERAL = 1;
  public final static int UNKNOWN = 2;
  public final static int FICTIVE = 3;
  public final static int VOIE = 4;
  
  
//Il s'agit de l'objet qui ne référence pas cette bordure et qui est adjacent à la bordure
 private List<? extends IFeature> featAdj = null;

  
  
  
  public List<? extends IFeature> getFeatAdj() {
    return featAdj;
  }



  public void setFeatAdj(List<? extends IFeature> featAdj) {
    this.featAdj = featAdj;
  }

  

  private int typeDroit = -1;
  private int typeGauche = -1;
  
  
  public Bordure(IGeometry geom){
    this.setGeom(geom);
  }
  
  

  @Override
  public CityObject export() {
    // TODO Auto-generated method stub
    return null;
  }

  public int getTypeDroit() {
    return typeDroit;
  }

  public void setTypeDroit(int type) {
    this.typeDroit = type;
  }
  
  
  
  
  public int getTypeGauche() {
    return typeGauche;
  }

  public void setTypeGauche(int type) {
    this.typeGauche = type;
  }
  
  
}
