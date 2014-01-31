package fr.ign.cogit.gru3d.regleUrba.generation.toit;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.sig3d.model.citygml.building.CG_RoofSurface;


/**
 * @TODO : vérifier la compatibilité avec CG_RoofSurface
 * 
 * @author MBrasebin
 *
 */
public abstract class AbstractRoof extends CG_RoofSurface implements IRoof {
  
  
  

  // Génération du bâtiment
  public IMultiSurface<IPolygon> generateBuilding(double zMin) {

    IMultiSurface<IPolygon> mSOut = new GM_MultiSurface<IPolygon>();

    IMultiSurface<IPolygon> mS1 = this.generateWall(zMin);

    if (mS1 == null) {
      return null;
    }

    IMultiSurface<IPolygon> mS2 = this.getRoof();
    
    if (mS2 == null) {
      return null;
    }

    mSOut.addAll(mS1);
    mSOut.addAll(mS2);

    return mSOut;

  }

}
