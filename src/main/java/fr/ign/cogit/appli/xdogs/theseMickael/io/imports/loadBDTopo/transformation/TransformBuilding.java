package fr.ign.cogit.appli.xdogs.theseMickael.io.imports.loadBDTopo.transformation;

import java.util.List;


import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IRing;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Face;
import fr.ign.cogit.geoxygene.contrib.delaunay.TriangulationJTS;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.sig3d.calculation.Proximity;
import fr.ign.cogit.geoxygene.sig3d.equation.ApproximatedPlanEquation;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.sig3d.semantic.DTM;
import fr.ign.cogit.geoxygene.sig3d.util.MathConstant;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_OrientableSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.sig3d.topology.TriangulationLoader;

public class TransformBuilding {

  @SuppressWarnings("unchecked")
  public static IMultiSurface<IOrientableSurface> createBDTopoBuilding(
      IGeometry geom, DTM dtm) {
    
    if (geom instanceof IPolygon) {
      try {
        return TransformBuilding.createBDTopoBuildingFromPolygon(
            (IPolygon) geom, dtm);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    } else if (geom instanceof IMultiSurface<?>) {

      IMultiSurface<IOrientableSurface> gos = (IMultiSurface<IOrientableSurface>) geom;

      int nbPoly = gos.size();

      IMultiSurface<IOrientableSurface> finalSurface = new GM_MultiSurface<IOrientableSurface>();

      for (int i = 0; i < nbPoly; i++) {

        try {
          finalSurface.addAll(TransformBuilding
              .createBDTopoBuildingFromPolygon((IPolygon) gos.get(i), dtm));
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

      }

      return finalSurface;

    } else {

      System.out.println("error  : " + geom.getClass().getName());
    }
    return null;

  }

  private static IMultiSurface<IOrientableSurface> createBDTopoBuildingFromPolygon(
      IPolygon surf, DTM dtm) throws Exception {
    
    // Les surfaces qui constitueront le bâtiment final
    IMultiSurface<IOrientableSurface> finalSurface = new GM_MultiSurface<IOrientableSurface>();

    TriangulationJTS triJTS = TriangulationLoader.generate(surf);

    try {

      // /On tente la triangulation

      triJTS.triangule("");

    } catch (Exception e) {
      e.printStackTrace();
      // On arrive pas à trianguler alors on bidouille un truc
      int nbFinal = finalSurface.size();
      Box3D b = new Box3D(surf);
      double zMax = b.getURDP().getZ();

      IDirectPositionList dplSurf = surf.coord();

      nbFinal = dplSurf.size();

      for (int i = 0; i < nbFinal; i++) {
        dplSurf.get(i).setZ(zMax);
      }

      IGeometry obj = dtm.mapGeom(surf, 0, true, false);

      double zMin = (new Box3D(obj)).getLLDP().getZ();

      IPolygon surf2 = (IPolygon) surf.clone();
      for (int i = 0; i < nbFinal; i++) {
        surf2.coord().get(i).setZ(zMin);
      }

      surf2 = (IPolygon) surf2.reverse();

      finalSurface.add(surf);
      finalSurface.add(surf2);
      finalSurface.addAll(generateWall(surf, zMin, zMax));

      return finalSurface;

    }

    finalSurface.addAll(generateWall(surf, dtm));

    IPopulation<Face> popFaces = triJTS.getPopFaces();

    int nbFace = popFaces.size();

    // On traite chaque triangle
    for (int i = 0; i < nbFace; i++) {

      GM_OrientableSurface geom = (GM_OrientableSurface) popFaces.get(i)
          .getGeometrie();
      IDirectPositionList pointTri = geom.coord();

      double xC = (pointTri.get(0).getX() + pointTri.get(1).getX() + pointTri
          .get(2).getX()) / 3;
      double yC = (pointTri.get(0).getY() + pointTri.get(1).getY() + pointTri
          .get(2).getY()) / 3;
      double zC = (pointTri.get(0).getZ() + pointTri.get(1).getZ() + pointTri
          .get(2).getZ()) / 3;

      if (Double.isNaN(zC)) {
        zC = 141.5;
      }

      DirectPosition centre = new DirectPosition(xC, yC, zC);

      // Le centre du triangle est à l'intérieur du polygone initial ?
      // Si il est à l'extérieur il ne faut pas le conserver (triangle
      // dans un
      // trou ou dans la zone convexe)
      if (surf.intersects(new GM_Point(centre))) {

        IGeometry obj = dtm.mapGeom(geom, 0, true, false);

        if (obj instanceof IOrientableSurface) {

          // On le plaque sur le MNT pour former le sol

          ApproximatedPlanEquation eq = new ApproximatedPlanEquation(
              obj.coord());

          if (eq.getNormale().prodScalaire(MathConstant.vectZ) > 0) {

            IDirectPositionList dplExt = surf.getExterior().coord();
            dplExt.inverseOrdre();

            for (IRing r : surf.getInterior()) {

              r.coord().inverseOrdre();
            }

          }

          finalSurface.add((GM_OrientableSurface) obj);

        } else {
          System.out.println("Autre type : " + obj.getClass().getName());
        }

        // On l'ajuste aux Z en entrée pour former le toit.

        int nbP = pointTri.size();

        for (int k = 0; k < nbP; k++) {

          IDirectPosition pActu = pointTri.get(k);

          if (Double.isNaN(pActu.getZ())) {
            double z = TransformBuilding.calculZ(pActu, surf);

            pActu.setZ(z);
          }

        }

        finalSurface.add(geom);

      } else {



      }

    }

    return finalSurface;

  }

  private static IMultiSurface<IOrientableSurface> generateWall(IPolygon surf,
      double zmin, double zmax) {

    IMultiSurface<IOrientableSurface> finalSurface = new GM_MultiSurface<IOrientableSurface>();

    IDirectPositionList dpl = surf.getExterior().coord();

    int nbPoints = dpl.size();
    // On génère les murs
    for (int i = 0; i < nbPoints - 1; i++) {
      IDirectPosition dp1 = dpl.get(i);
      IDirectPosition dp2 = dpl.get(i + 1);

      dp1.setZ(zmax);
      dp2.setZ(zmax);

      DirectPosition dp3 = (DirectPosition) dp1.clone();
      DirectPosition dp4 = (DirectPosition) dp2.clone();

      dp3.setZ(zmin);
      dp4.setZ(zmin);

      IDirectPositionList dplTemp = new DirectPositionList();
      dplTemp.add(dp3);
      dplTemp.add(dp1);

      dplTemp.add(dp2);
      dplTemp.add(dp3);

      finalSurface.add(new GM_Polygon(new GM_LineString(dplTemp)));

      dplTemp = new DirectPositionList();

      dplTemp.add(dp3);

      dplTemp.add(dp2);
      dplTemp.add(dp4);
      dplTemp.add(dp3);

      finalSurface.add(new GM_Polygon(new GM_LineString(dplTemp)));
    }

    List<IRing> lInterior = surf.getInterior();
    int nbInterior = lInterior.size();

    // On génère les murs apparaissent dans les polygones troués extrudés
    for (int i = 0; i < nbInterior; i++) {
      IRing ringActu = lInterior.get(i);

      IDirectPositionList dplInterior = ringActu.coord();
      if (!dplInterior.get(0).equals(dplInterior.get(dplInterior.size() - 1))) {
        dplInterior.add(dpl.get(0));

      }

      int nbPInt = dplInterior.size();

      for (int j = 0; j < nbPInt - 1; j++) {

        IDirectPosition dp1 = dplInterior.get(j);
        IDirectPosition dp2 = dplInterior.get(j + 1);

        dp1.setZ(zmax);
        dp2.setZ(zmax);

        DirectPosition dp3 = (DirectPosition) dp1.clone();
        DirectPosition dp4 = (DirectPosition) dp2.clone();

        dp3.setZ(zmin);
        dp4.setZ(zmin);

        IDirectPositionList dplTemp = new DirectPositionList();
        dplTemp.add(dp3);
        dplTemp.add(dp1);

        dplTemp.add(dp2);
        dplTemp.add(dp3);

        finalSurface.add(new GM_Polygon(new GM_LineString(dplTemp)));

        dplTemp = new DirectPositionList();

        dplTemp.add(dp3);

        dplTemp.add(dp2);
        dplTemp.add(dp4);
        dplTemp.add(dp3);

        finalSurface.add(new GM_Polygon(new GM_LineString(dplTemp)));

      }

    }

    return finalSurface;

  }

  private static IMultiSurface<IOrientableSurface> generateWall(IPolygon surf,
      DTM dtm) {

    IMultiSurface<IOrientableSurface> finalSurface = new GM_MultiSurface<IOrientableSurface>();

    IDirectPositionList dpl = surf.getExterior().coord();

    int nbPoints = dpl.size();
    // On génère les murs
    for (int i = 0; i < nbPoints - 1; i++) {
      IDirectPosition dp1 = dpl.get(i);
      IDirectPosition dp2 = dpl.get(i + 1);

      DirectPosition dp3 = (DirectPosition) dp1.clone();
      DirectPosition dp4 = (DirectPosition) dp2.clone();

      dp3.setZ(dtm.cast(dp3).getZ());
      dp4.setZ(dtm.cast(dp4).getZ());

      IDirectPositionList dplTemp = new DirectPositionList();
      dplTemp.add(dp3);
      dplTemp.add(dp1);

      dplTemp.add(dp2);
      dplTemp.add(dp3);

      finalSurface.add(new GM_Polygon(new GM_LineString(dplTemp)));

      dplTemp = new DirectPositionList();

      dplTemp.add(dp3);

      dplTemp.add(dp2);
      dplTemp.add(dp4);
      dplTemp.add(dp3);

      finalSurface.add(new GM_Polygon(new GM_LineString(dplTemp)));
    }

    List<IRing> lInterior = surf.getInterior();
    int nbInterior = lInterior.size();

    // On génère les murs apparaissent dans les polygones troués extrudés
    for (int i = 0; i < nbInterior; i++) {
      IRing ringActu = lInterior.get(i);

      IDirectPositionList dplInterior = ringActu.coord();
      if (!dpl.get(0).equals(dpl.get(dpl.size() - 1))) {
        dpl.add(dpl.get(0));

      }

      int nbPInt = dplInterior.size();

      for (int j = 0; j < nbPInt - 1; j++) {

        IDirectPosition dp1 = dplInterior.get(j);
        IDirectPosition dp2 = dplInterior.get(j + 1);

        DirectPosition dp3 = (DirectPosition) dp1.clone();
        DirectPosition dp4 = (DirectPosition) dp2.clone();

        dp3.setZ(dtm.cast(dp3).getZ());
        dp4.setZ(dtm.cast(dp4).getZ());

        IDirectPositionList dplTemp = new DirectPositionList();
        dplTemp.add(dp3);
        dplTemp.add(dp1);

        dplTemp.add(dp2);
        dplTemp.add(dp3);

        finalSurface.add(new GM_Polygon(new GM_LineString(dplTemp)));

        dplTemp = new DirectPositionList();

        dplTemp.add(dp3);

        dplTemp.add(dp2);
        dplTemp.add(dp4);
        dplTemp.add(dp3);

        finalSurface.add(new GM_Polygon(new GM_LineString(dplTemp)));

      }

    }

    return finalSurface;

  }

  private static double calculZ(IDirectPosition dp, IPolygon poly) {

    IDirectPositionList dpl = poly.getExterior().coord();
    if (!dpl.get(0).equals(dpl.get(dpl.size() - 1))) {
      dpl.add(dpl.get(0));

    }

    int nbPointEx = dpl.size();

    for (int i = 0; i < nbPointEx - 1; i++) {
      IDirectPosition dp1 = dpl.get(i);
      IDirectPosition dp2 = dpl.get(i + 1);

      Vecteur v1 = new Vecteur(dp, dp1);
      Vecteur v2 = new Vecteur(dp, dp2);

      v1.normalise();
      v2.normalise();

      if (v1.prodScalaire(v2) < -0.9) {

        double dTemp = dp1.distance(dp);
        double dTot = dp1.distance(dp2);

        return dp1.getZ() + (dp2.getZ() - dp1.getZ()) * (dTemp / dTot);

      }

    }

    List<IRing> lInterior = poly.getInterior();
    int nbInterior = lInterior.size();

    // On génère les murs apparaissent dans les polygones troués extrudés
    for (int i = 0; i < nbInterior - 1; i++) {
      IRing ringActu = lInterior.get(i);

      IDirectPositionList dplInterior = ringActu.coord();
      if (!dplInterior.get(0).equals(dplInterior.get(dplInterior.size() - 1))) {
        dplInterior.add(dplInterior.get(0));

      }

      int nbPInt = dplInterior.size();

      for (int j = 0; j < nbPInt - 1; j++) {

        IDirectPosition dp1 = dplInterior.get(j);
        IDirectPosition dp2 = dplInterior.get(j + 1);

        Vecteur v1 = new Vecteur(dp, dp1);
        Vecteur v2 = new Vecteur(dp, dp2);

        v1.normalise();
        v2.normalise();

        if (v1.prodScalaire(v2) < -0.9) {

          double dTemp = dp1.distance(dp);
          double dTot = dp1.distance(dp2);

          return dp1.getZ() + (dp2.getZ() - dp1.getZ()) * (dTemp / dTot);

        }

      }

    }
    Proximity p = new Proximity();
    double nouvZ = p.nearest(dp, poly.coord()).getZ();

    return nouvZ;
  }

}
