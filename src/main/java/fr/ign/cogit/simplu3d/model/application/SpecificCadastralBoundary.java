package fr.ign.cogit.simplu3d.model.application;

import org.citygml4j.model.citygml.core.CityObject;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.sig3d.model.citygml.core.CG_CityObject;

public class SpecificCadastralBoundary extends CG_CityObject {

  public final static int BOT = 0;
  public final static int LAT = 1;
  public final static int UNKNOWN = 2;
  public final static int INTRA = 3;
  public final static int ROAD = 4;
  public final static int PUB = 5;

  public Alignement alignement = null;
  public Recoil recoil = null;
  public int type;

  // Il s'agit de l'objet qui ne référence pas cette bordure et qui est adjacent
  // à la bordure
  private IFeature featAdj = null;

  public IFeature getFeatAdj() {
    return featAdj;
  }

  public void setFeatAdj(IFeature featAdj) {
    this.featAdj = featAdj;
  }

  public SpecificCadastralBoundary(IGeometry geom) {
    this.setGeom(geom);
  }

  @Override
  public CityObject export() {
    // TODO Auto-generated method stub
    return null;
  }

  public Alignement getAlignement() {
    return alignement;
  }

  public void setAlignement(Alignement alignement) {
    this.alignement = alignement;
  }

  public Recoil getRecoil() {
    return recoil;
  }

  public void setRecoil(Recoil recoil) {
    this.recoil = recoil;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

}
