package fr.ign.cogit.simplu3d.exec.compare;

import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.calculation.Util;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.sig3d.analysis.RoofDetection;
import fr.ign.cogit.simplu3d.calculation.SHONCalculation;
import fr.ign.cogit.simplu3d.model.application.Building;

public class CompareSHON {

  /**
   * @param args
   */
  public static void main(String[] args) {

    IFeature batBDTOPO = ShapefileReader
        .read(
            "E:/mbrasebin/Donnees/Strasbourg/ZoneTest/Experimentation/CompareSHON/donnees/batiBDTOPO.shp")
        .get(0);
    IFeature batBD3D = ShapefileReader
        .read(
            "E:/mbrasebin/Donnees/Strasbourg/ZoneTest/Experimentation/CompareSHON/donnees/batiBd3D.shp")
        .get(0);
  

    IMultiSurface<? extends IOrientableSurface> roofBDTopo = RoofDetection
        .detectRoof(batBDTOPO, 0.1, false);
    IMultiSurface<? extends IOrientableSurface> roofBat3D = RoofDetection
        .detectRoof(batBD3D, 0.1, false);

    System.out.println("Nombre de pans de toit BDTopo : " + roofBDTopo.size());
    System.out.println("Nombre de pans de toit : BD3D " + roofBat3D.size());

    double aireBDTopo = roofBDTopo.area();
    double aireBD3D = roofBat3D.area();

    System.out.println("Aire BDTopo : " + aireBDTopo);
    System.out.println("Aire BD3D : " + aireBD3D);

    Box3D bdTopo3DBox = new Box3D(batBDTOPO.getGeom());
    Box3D bdBoxBD3D = new Box3D(batBD3D.getGeom());

    Box3D bdRoofBD3D = new Box3D(roofBat3D.get(0));

    double hGouttiereBDTopo = bdTopo3DBox.getURDP().getZ()
        - bdTopo3DBox.getLLDP().getZ();
    double hGouttiereBD3D = bdRoofBD3D.getLLDP().getZ()
        - bdBoxBD3D.getLLDP().getZ();
    double hMaxBD3D = bdBoxBD3D.getURDP().getZ() - bdBoxBD3D.getLLDP().getZ();

    System.out.println("Hauteur gouttière BDTopo : " + hGouttiereBDTopo);
    System.out.println("Hauteur gouttière BD3D : " + hGouttiereBD3D);
    System.out.println("Hauteur faitage BD3D : " + hMaxBD3D);

    List<IOrientableSurface> iOS = FromGeomToSurface.convertGeom(batBDTOPO
        .getGeom());

    IMultiSurface<IOrientableSurface> lOS = Util.detectVertical(iOS, 0.2);
    lOS.addAll(roofBDTopo);

    double shon1 = SHONCalculation.assessCUTSHON(new Building(lOS));

    IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();
    featC.add(new DefaultFeature(new GM_MultiSurface<>(SHONCalculation.DEBUG)));

    ShapefileWriter
        .write(
            featC,
            "E:/mbrasebin/Donnees/Strasbourg/ZoneTest/Experimentation/CompareSHON/donnees/debugBDDTopo.shp");

    SHONCalculation.DEBUG.clear();

    

    
    double shon2 = SHONCalculation
        .assessCUTSHON(new Building(batBD3D.getGeom()));
    featC.clear(); 
    featC.add(new DefaultFeature(new GM_MultiSurface<>(SHONCalculation.DEBUG)));
    
    
        ShapefileWriter
        .write(
            featC,
            "E:/mbrasebin/Donnees/Strasbourg/ZoneTest/Experimentation/CompareSHON/donnees/debugBD3D.shp");

    System.out.println("Aire" + ":" + aireBDTopo + ":" + aireBD3D + ":"
        + (aireBD3D - aireBD3D));
    System.out.println("HauteurGouttière" + ":" + hGouttiereBDTopo + ":"
        + hGouttiereBD3D + ":" + (hGouttiereBD3D - hGouttiereBDTopo));
    System.out.println("Hauteur Faitage" + ":" + "----" + ":" + hMaxBD3D + ":"
        + "--");
    System.out.println("Surface plancher 2 étage" + ":" + 2 * aireBDTopo * 0.8
        + ":" + 2 * aireBD3D * 0.8 + ":" + (2 * 0.8 * (aireBD3D - aireBDTopo)));
    System.out.println("Surface plancher 3 étage" + ":" + 3 * aireBDTopo * 0.8
        + ":" + 3 * aireBD3D * 0.8 + ":" + (3 * 0.8 * (aireBD3D - aireBDTopo)));
    System.out.println("Surface plancher 4 étage" + ":" + 4 * aireBDTopo * 0.8
        + ":" + 4 * aireBD3D * 0.8 + ":" + (4 * 0.8 * (aireBD3D - aireBDTopo)));
    System.out.println("Surface plancher méthode proposée" + ":" + shon1 + ":"
        + shon2 + ":" + (shon2 - shon1));
    
    
    System.out.println();

    // System.out.println(shon1 + "   " + shon2);

  }

}
