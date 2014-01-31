package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.generation.toit;

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
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.sig3d.model.citygml.building.CG_RoofSurface;

public class OnePlaneRoof  extends AbstractRoof {

  private final static String ONEPLANEROOF = "Toit 1 pan";

  IMultiSurface<IPolygon> generatedRoof = null;


  public OnePlaneRoof(IPolygon emprise, double zmin, double zmax,
      ILineString lineAtZMin) {

    double dMin = Double.POSITIVE_INFINITY;
    double dMax = Double.NEGATIVE_INFINITY;

    IPolygon roof = (IPolygon) emprise.clone();
    IDirectPositionList dpl = roof.coord();

    for (IDirectPosition dp : dpl) {

      double dist = lineAtZMin.distance(new GM_Point(dp));

      dMin = Math.min(dist, dMin);
      dMax = Math.max(dist, dMax);

    }

    for (IDirectPosition dp : dpl) {

      double dist = lineAtZMin.distance(new GM_Point(dp));

      double z = ((dist - dMin) / (dMax - dMin)) * (zmax - zmin) + zmin;

      dp.setZ(z);

    }

    generatedRoof = new GM_MultiSurface<IPolygon>();
    generatedRoof.add(roof);

  }

  @Override
  public String getType() {
    // TODO Auto-generated method stub
    return ONEPLANEROOF;
  }

  @Override
  public IMultiSurface<IPolygon> getRoof() {
    // TODO Auto-generated method stub
    return generatedRoof;
  }

  
  @Override
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
