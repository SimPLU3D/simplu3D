package fr.ign.cogit.gru3d.regleUrba.generation.toit;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IRing;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion3DObject;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;

public class FlatRoof extends AbstractRoof {

  private final static String FLATROOF = "Toit plat";

  IMultiSurface<IPolygon> generatedRoof = null;

  /**
   * La génération consiste à affecter z à chaque sommet de l'emprise
   * @param emprise
   * @param zMax
   */
  public FlatRoof(IPolygon emprise, double zMax) {

    IPolygon roof = (IPolygon) emprise.clone();

    IDirectPositionList dpl = roof.coord();

    for (IDirectPosition dp : dpl) {

      dp.setZ(zMax);

    }

    generatedRoof = new GM_MultiSurface<IPolygon>();
    generatedRoof.add(roof);

  }

  public String getType() {
    // TODO Auto-generated method stub
    return FLATROOF;
  }

  public IMultiSurface<IPolygon> getRoof() {
    // TODO Auto-generated method stub
    return generatedRoof;
  }

  public IMultiSurface<IPolygon> generateWall(double zMin) {

    IMultiSurface<IPolygon> mS = new GM_MultiSurface<IPolygon>();

    IGeometry geom = Extrusion3DObject.convertitFromLineUntilZMin(
        new GM_LineString(generatedRoof.get(0).getExterior().coord()), zMin);

    if (geom instanceof IMultiSurface<?>) {

      mS.addAll((IMultiSurface<IPolygon>) geom);

    } else {

      return null;
    }

    for (IRing r : generatedRoof.get(0).getInterior()) {

      ILineString lsTemp = new GM_LineString(r.coord());

      geom = Extrusion3DObject.convertitFromLineUntilZMin(lsTemp, zMin);
      if (geom instanceof IMultiSurface<?>) {

        mS.addAll((IMultiSurface<IPolygon>) geom);

      } else {

        return null;
      }

    }

    return mS;

  }

}
