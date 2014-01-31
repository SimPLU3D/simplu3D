package fr.ign.cogit.gru3d.regleUrba.generation.toit;

import fr.ign.cogit.contrib.CampSkeleton;
import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IRing;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.cartetopo.CarteTopo;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Face;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Noeud;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion2DObject;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.sig3d.model.citygml.building.CG_RoofSurface;

public class FourPlanesRoof  extends AbstractRoof {

  private final static String FOURPLANEROOF = "Toit 1 pan";

  IMultiSurface<IPolygon> generatedRoof = null;

  private IPolygon emprise;
  private double zGutter;

  public FourPlanesRoof(IPolygon emprise, double zmin, double zmax) {

    this.emprise = emprise;
    this.zGutter = zmin;

    IMultiCurve<IOrientableCurve> gouttiere = new GM_MultiCurve<IOrientableCurve>();
    gouttiere.add(emprise.getExterior());

    if (emprise.getInterior() != null) {
      gouttiere.addAll(emprise.getInterior());
    }

    try {
      CampSkeleton cS = new CampSkeleton(emprise);

      CarteTopo ct = cS.getCarteTopo();
      double dMax = Double.NEGATIVE_INFINITY;

      IPopulation<Noeud> popN = ct.getPopNoeuds();

      for (Noeud n : popN) {

        double dist = n.getGeometrie().distance(gouttiere);

        dMax = Math.max(dMax, dist);

      }

      generatedRoof = new GM_MultiSurface<IPolygon>();
      IPopulation<Face> popFace = ct.getPopFaces();

      for (Face f : popFace) {

        IPolygon poly = (IPolygon) f.getGeom();

        if (poly == null || poly.getExterior() == null) {
          continue;
        }

        IDirectPositionList dpl = poly.coord();

        for (IDirectPosition dp : dpl) {

          double dist = gouttiere.distance(new GM_Point(dp));

          dp.setZ((dist / dMax) * (zmax - zmin) + zmin);

        }
        generatedRoof.add(poly);

      }
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }

  }

  @Override
  public String getType() {
    // TODO Auto-generated method stub
    return FOURPLANEROOF;
  }

  @Override
  public IMultiSurface<IPolygon> getRoof() {
    // TODO Auto-generated method stub
    return generatedRoof;
  }

  @Override
  public IMultiSurface<IPolygon> generateWall(double zMin) {

    IMultiSurface<IPolygon> mS = new GM_MultiSurface<IPolygon>();

    IGeometry geom = Extrusion2DObject.convertFromLine(
        this.emprise.exteriorLineString(), zMin, this.zGutter);

    if (geom instanceof IMultiSurface<?>) {

      mS.addAll((IMultiSurface<IPolygon>) geom);

    } else {

      return null;
    }

    for (IRing r : this.emprise.getInterior()) {

      ILineString lsTemp = new GM_LineString(r.coord());

      geom = Extrusion2DObject.convertFromLine(lsTemp, zMin, this.zGutter);
      if (geom instanceof IMultiSurface<?>) {

        mS.addAll((IMultiSurface<IPolygon>) geom);

      } else {

        return null;
      }

    }

    return mS;

  }

}
