package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.generation.toit;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.contrib.CampSkeleton;
import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IPoint;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IRing;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.cartetopo.CarteTopo;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Face;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Noeud;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion2DObject;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;

public class PignonRoof extends AbstractRoof {

  private final static String PIGNONROOF = "Toit pignons";

  IMultiSurface<IPolygon> generatedRoof = null;

  private IPolygon emprise;
  private double zGutter;

  public PignonRoof(IPolygon emprise, double zmin, double zmax,
      List<ILineString> pignons) {

    this.emprise = emprise;
    this.zGutter = zmin;

    // Les gouttières forment le countour de la forme - le toit pignon
    IMultiCurve<IOrientableCurve> gouttiere = new GM_MultiCurve<IOrientableCurve>();

    // On récupére les arrêtes qui sont sur le contour et n'intersecte pas le
    // toit pignon
    IDirectPositionList dplExt = emprise.getExterior().coord();
    int nbExt = dplExt.size();

    // Indices pour repérer les arrêtes du toit pignon
    List<Integer> lIndPignon = new ArrayList<Integer>();

    bouclext: for (int i = 0; i < nbExt - 1; i++) {
      // On récupère une arrête et on vérifie qu'elle intersecte le 2 sommets du
      // toit pignon

      IDirectPosition dp1 = dplExt.get(i);
      IDirectPosition dp2 = dplExt.get(i + 1);

      IPoint p1 = new GM_Point(dp1);
      IPoint p2 = new GM_Point(dp2);

      for (ILineString ls : pignons) {

        // C'est le cas, l'arrête n'est plus une gouttière le pignon du toit
        if (p1.intersects(ls) && p2.intersects(ls)) {

          lIndPignon.add(i);
          continue bouclext;
        }

      }

      // Sinon, c'est une gouttière
      IDirectPositionList dplTemp = new DirectPositionList();

      dplTemp.add(dp1);
      dplTemp.add(dp2);

      gouttiere.add(new GM_LineString(dplTemp));
    }

    // On prépare les angles actuellement
    double[] angles = calculateAngle(emprise, lIndPignon);

    if (emprise.getInterior() != null) {
      gouttiere.addAll(emprise.getInterior());
    }

    try {

      // On calcule le squelette et on récupère la carte topo
      CampSkeleton cS = new CampSkeleton(emprise, angles);

      CarteTopo ct = cS.getCarteTopo();

      // Distance max des sommets aux gouttières
      double dMax = Double.NEGATIVE_INFINITY;

      IPopulation<Noeud> popN = ct.getPopNoeuds();

      for (Noeud n : popN) {

        double dist = n.getGeometrie().distance(gouttiere);

        dMax = Math.max(dMax, dist);

      }

      // Les polygones des toits
      generatedRoof = new GM_MultiSurface<IPolygon>();
      IPopulation<Face> popFace = ct.getPopFaces();

      // Pour chaque face, on assigne le z en fonction de la distance à
      // l'extérieur du bâtiment
      for (Face f : popFace) {

        IPolygon poly = (IPolygon) f.getGeom();

        if (poly == null || poly.getExterior() == null) {
          continue;
        }

        IDirectPositionList dpl = poly.coord();

        for (IDirectPosition dp : dpl) {

          IPoint p = new GM_Point(dp);

          double dist = gouttiere.distance(p);

          if (dist > 0.01) {

            dp.setZ((dist / dMax) * (zmax - zmin) + zmin); // dp.setZ(zmax);

          } else {

            if (dMax == 0) {

              dp.setZ(zmin);

            } else {

              dp.setZ((dist / dMax) * (zmax - zmin) + zmin);

            }
          }

        }
        generatedRoof.add(poly);

      }
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }

  }

  private static double[] calculateAngle(IPolygon p, List<Integer> lInt) {

    List<IRing> interiorRing = p.getInterior();

    int nbTotalContrib = p.getExterior().coord().size() - 1;

    for (IRing r : interiorRing) {

      nbTotalContrib = nbTotalContrib + r.coord().size() - 1;

    }

    double[] angles = new double[nbTotalContrib];

    for (int i = 0; i < nbTotalContrib; i++) {

      if (lInt.contains(i)) {
        angles[i] = 0;
      } else {
        angles[i] = Math.PI / 4;
      }

    }

    return angles;

  }

  @Override
  public String getType() {
    // TODO Auto-generated method stub
    return PIGNONROOF;
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
