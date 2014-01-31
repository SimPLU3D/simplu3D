package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.util;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.DistanceFHauteur;
import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IRing;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.ISolid;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Face;
import fr.ign.cogit.geoxygene.contrib.delaunay.TriangulationJTS;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Solid;
import fr.ign.cogit.sig3d.topology.TriangulationLoader;

public class ExtrusionTriangulation {

  /**
   * PRocède à une extrusion de geom triangulée. La hauteur de l'extrusion est
   * fonction de la distance à geom dist
   * @param geom
   * @param zmin
   * @param zmax
   * @param dfh
   * @return
   */
  public static ISolid process(IGeometry geom, IGeometry geomDist, double zmin,
      DistanceFHauteur dfh) {

    double coeff = dfh.getCoefficient();
    double h0 = dfh.getHauteurOrigine();

    List<IOrientableSurface> lOSOut = new ArrayList<IOrientableSurface>();

    IDirectPositionList dpl = geom.coord();

    for (IDirectPosition dp : dpl) {
      double dist = (new GM_Point(dp)).distance(geomDist);
      dp.setZ(dist * coeff + h0 + zmin);
    }

    List<IOrientableSurface> lOS = FromGeomToSurface.convertGeom(geom);

    for (IOrientableSurface os : lOS) {
      IPolygon poly = (IPolygon) os;

      lOSOut.addAll(generateWall(poly, zmin));

      TriangulationJTS triJTS = TriangulationLoader.generate(poly);

      try {

        // /On tente la triangulation

        triJTS.triangule("");

      } catch (Exception e) {

        e.printStackTrace();
      }

      IPopulation<Face> popFaces = triJTS.getPopFaces();

      int nbFace = popFaces.size();

      List<IOrientableSurface> lOSTemp = new ArrayList<IOrientableSurface>();

      // On traite chaque triangle
      for (int i = 0; i < nbFace; i++) {

        IPolygon polTri = (IPolygon) popFaces.get(i).getGeometrie();
        IDirectPositionList pointTri = polTri.coord();

        double xC = (pointTri.get(0).getX() + pointTri.get(1).getX() + pointTri
            .get(2).getX()) / 3;
        double yC = (pointTri.get(0).getY() + pointTri.get(1).getY() + pointTri
            .get(2).getY()) / 3;
        double zC = (pointTri.get(0).getZ() + pointTri.get(1).getZ() + pointTri
            .get(2).getZ()) / 3;

        DirectPosition centre = new DirectPosition(xC, yC, zC);

        if ((new GM_Point(centre)).intersects(poly)) {

          lOSTemp.add(polTri);

        }

      }

      IMultiSurface<IOrientableSurface> iMS1 = new GM_MultiSurface<IOrientableSurface>(
          lOSTemp);
      IMultiSurface<IOrientableSurface> iMS2 = (IMultiSurface<IOrientableSurface>) iMS1
          .clone();

      IDirectPositionList dpl1 = iMS1.coord();

      for (IDirectPosition dp1 : dpl1) {
        double dist = (new GM_Point(dp1)).distance(geomDist);
        dp1.setZ(dist * coeff + h0 + zmin);
      }

      lOSOut.addAll(iMS1.getList());

      IDirectPositionList dpl2 = iMS2.coord();

      for (IDirectPosition dp2 : dpl2) {
        dp2.setZ(zmin);
      }

      for (IOrientableSurface os2 : iMS2) {

        IPolygon pol2 = (IPolygon) os2;
        lOSOut.add(pol2.reverse());
      }

    }

    for (IOrientableSurface ios : lOSOut) {

      if (ios == null || ios.isEmpty() || ios.coord().size() == 0) {
        System.out.println("hguhuhu");
      }

    }

    return new GM_Solid(lOSOut);

  }

  /**
   * Procède à une extrusion de geom triangulée
   * @param geom
   * @param zmin
   * @param zmax
   * @return
   */
  public static ISolid process(IGeometry geom, double zmin, double zmax) {

    List<IOrientableSurface> lOSOut = new ArrayList<IOrientableSurface>();

    IDirectPositionList dpl = geom.coord();

    for (IDirectPosition dp : dpl) {
      dp.setZ(zmax);
    }

    List<IOrientableSurface> lOS = FromGeomToSurface.convertGeom(geom);

    for (IOrientableSurface os : lOS) {
      IPolygon poly = (IPolygon) os;

      lOSOut.addAll(generateWall(poly, zmin));

      TriangulationJTS triJTS = TriangulationLoader.generate(poly);

      try {

        // /On tente la triangulation

        triJTS.triangule("");

      } catch (Exception e) {

        e.printStackTrace();
      }

      IPopulation<Face> popFaces = triJTS.getPopFaces();

      int nbFace = popFaces.size();

      List<IOrientableSurface> lOSTemp = new ArrayList<IOrientableSurface>();

      // On traite chaque triangle
      for (int i = 0; i < nbFace; i++) {

        IPolygon polTri = (IPolygon) popFaces.get(i).getGeometrie();
        IDirectPositionList pointTri = polTri.coord();

        double xC = (pointTri.get(0).getX() + pointTri.get(1).getX() + pointTri
            .get(2).getX()) / 3;
        double yC = (pointTri.get(0).getY() + pointTri.get(1).getY() + pointTri
            .get(2).getY()) / 3;
        double zC = (pointTri.get(0).getZ() + pointTri.get(1).getZ() + pointTri
            .get(2).getZ()) / 3;

        DirectPosition centre = new DirectPosition(xC, yC, zC);

        if ((new GM_Point(centre)).intersects(poly)) {

          lOSTemp.add(polTri);

        }

      }

      IMultiSurface<IOrientableSurface> iMS1 = new GM_MultiSurface<IOrientableSurface>(
          lOSTemp);
      IMultiSurface<IOrientableSurface> iMS2 = (IMultiSurface<IOrientableSurface>) iMS1
          .clone();

      IDirectPositionList dpl1 = iMS1.coord();

      for (IDirectPosition dp1 : dpl1) {
        dp1.setZ(zmax);
      }

      lOSOut.addAll(iMS1.getList());

      IDirectPositionList dpl2 = iMS2.coord();

      for (IDirectPosition dp2 : dpl2) {
        dp2.setZ(zmin);
      }

      for (IOrientableSurface os2 : iMS2) {

        IPolygon pol2 = (IPolygon) os2;
        lOSOut.add(pol2.reverse());
      }

    }

    return new GM_Solid(lOSOut);
  }

  private static IMultiSurface<IOrientableSurface> generateWall(IPolygon surf,
      double zmin) {

    IMultiSurface<IOrientableSurface> finalSurface = new GM_MultiSurface<IOrientableSurface>();

    IDirectPositionList dpl = surf.getExterior().coord();

    int nbPoints = dpl.size();
    // On génère les murs
    for (int i = 0; i < nbPoints - 1; i++) {
      IDirectPosition dp1 = dpl.get(i);
      IDirectPosition dp2 = dpl.get(i + 1);

      DirectPosition dp3 = (DirectPosition) dp1.clone();
      DirectPosition dp4 = (DirectPosition) dp2.clone();

      dp3.setZ(zmin);
      dp4.setZ(zmin);

      IDirectPositionList dplTemp = new DirectPositionList();
      dplTemp.add(dp3);

      dplTemp.add(dp2);
      dplTemp.add(dp1);
      dplTemp.add(dp3);

      finalSurface.add(new GM_Polygon(new GM_LineString(dplTemp)));

      dplTemp = new DirectPositionList();

      dplTemp.add(dp3);
      dplTemp.add(dp4);
      dplTemp.add(dp2);

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

}
