package fr.ign.cogit.simplu3d.model.application;

import org.citygml4j.model.citygml.core.CityObject;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.sig3d.model.citygml.core.CG_CityObject;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.impl.Cuboid2;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.impl.CuboidSnap;

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

  public boolean prospect(AbstractBuilding b, double slope, double hIni) {

    double zMin = 0;
    IDirectPositionList dpl = null;

    double shift = 0;
    double h = -1;
    
    double distance = -1;
    
    if (b instanceof Cuboid) {

      dpl = b.getFootprint().coord();
      
      
      
     h =  ((Cuboid) b).height;
      
      
      
      
      distance = b.getFootprint().distance(this.getGeom());
      zMin = ((Cuboid) b).getZmin();

      shift = zMin;

    } else if (b instanceof Cuboid2) {

      dpl = b.getFootprint().coord();
      
      distance = b.getFootprint().distance(this.getGeom());
      
      h = ((Cuboid2) b).height;
      
      
      zMin = ((Cuboid2) b).getZmin();

      shift = zMin;

    }
    
    //cas des cuboid
    if(distance > 0 ){
      
      
      return distance * slope + hIni >  h;
      
    }
    
    Box3D box = new Box3D(b.getGeom());
    dpl = b.getToit().getGeom().coord();

    zMin = box.getLLDP().getZ();

    for (IDirectPosition dp : dpl) {

      if (this.geom.distance(new GM_Point(dp)) * slope + hIni < shift
          + dp.getZ() - zMin) {

        return false;
      }

    }

    return true;

  }
}
