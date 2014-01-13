package fr.ign.cogit.simplu3d.exec.calculation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ITriangle;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IRing;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.cartetopo.CarteTopo;
import fr.ign.cogit.geoxygene.contrib.cartetopo.CarteTopoFactory;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Face;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.filter.ShapeFilter;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromPolygonToTriangle;
import fr.ign.cogit.geoxygene.sig3d.equation.PlanEquation;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.sig3d.calculation.CutBuilding;

public class SHONFromShuangData {

  /**
   * @param args
   */
  public static void main(String[] args) {

    String folder = "E:/temp/shp3D/";
    String folderOut = "E:/temp/shp3D/out/";

    File f = new File(folder);

    File[] lfiles = f.listFiles(new ShapeFilter());

    int nbFile = lfiles.length;

    for (int j = 0; j < nbFile; j++) {

      File currentFile = lfiles[j];

      IPopulation<IFeature> featColl = ShapefileReader.read(currentFile
          .getAbsolutePath());

      double zMin = Double.POSITIVE_INFINITY;

      double zMax = Double.NEGATIVE_INFINITY;

      for (IFeature feat : featColl) {
        Box3D b = new Box3D(feat.getGeom());

        zMin = Math.min(b.getLLDP().getZ(), zMin);
        zMax = Math.max(b.getURDP().getZ(), zMax);

      }

      IFeatureCollection<IFeature> featCollOut2 = new FT_FeatureCollection<IFeature>();

      for (double i = zMin + 0.01; i < zMax; i = i + 3) {

        featCollOut2.addAll(cutAt(featColl, i));

      }

      double d = 0;
      for (IFeature feat : featCollOut2) {

        for (IOrientableSurface os : FromGeomToSurface.convertGeom(feat
            .getGeom())) {

          d = d + os.area();

        }

      }

      
      
      ShapefileWriter.write(featCollOut2, folderOut +currentFile.getName()+ "_SHON_" + d + ".shp");

    }

  }

  private static IFeatureCollection<IFeature> cutAt(
      IFeatureCollection<? extends IFeature> featColl, double z) {

    List<ITriangle> lT = new ArrayList<>();

    for (IFeature feat : featColl) {
      lT.addAll(FromPolygonToTriangle.convertAndTriangle(FromGeomToSurface
          .convertGeom(feat.getGeom())));
    }

    PlanEquation eq = new PlanEquation(0, 0, 1, -z);

    List<IGeometry> lG = CutBuilding.cut(lT, eq);

    IFeatureCollection<IFeature> featCollOut = new FT_FeatureCollection<>();
    for (IGeometry geom : lG) {

      if (geom.dimension() == 1) {
        featCollOut.add(new DefaultFeature(geom));
      }

    }

    CarteTopo map = CarteTopoFactory.newCarteTopo("", featCollOut, 0.5, true);

    map.creeTopologieArcsNoeuds(0.5);

    map.filtreDoublons(0);
    map.fusionNoeuds(0.5);
    map.filtreNoeudsIsoles();
    map.filtreArcsDoublons();

    map.creeTopologieArcsNoeuds(0.5);

    map.rendPlanaire(0.5);

    map.creeTopologieArcsNoeuds(0.5);

    map.creeTopologieFaces();

    IFeatureCollection<IFeature> featCollOut2 = new FT_FeatureCollection<>();

    for (Face f : map.getPopFaces()) {
      if (f.isInfinite()) {

        IPolygon pol = f.getGeometrie();

        for (IRing r : pol.getInterior()) {

          IDirectPositionList dpl = r.coord();
          dpl.inverseOrdre();

          for (IDirectPosition dp : dpl) {
            dp.setZ(z);
          }

          featCollOut2.add(new DefaultFeature(new GM_Polygon(new GM_LineString(
              dpl))));

        }

      }
    }

    return featCollOut2;

  }

}
