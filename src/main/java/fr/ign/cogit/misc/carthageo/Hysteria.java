package fr.ign.cogit.misc.carthageo;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.calculation.Util;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;

public class Hysteria {

  /**
   * @param args
   */
  public static void main(String[] args) {
    String fileIn = "E:/mbrasebin/Donnees/Strasbourg/GTRU/ProjectT3/BDTopoIni/bati.shp";
   IFeatureCollection<IFeature> featColl =  ShapefileReader.read(fileIn);
   
   
   String fileParc = "E:/mbrasebin/Donnees/Strasbourg/GTRU/ProjectT3/parcelle.shp";
  IFeatureCollection<IFeature> featCollParce =  ShapefileReader.read(fileParc);
  

  
  for(IFeature feat : featColl){
    
   IMultiSurface<IOrientableSurface> iMS = Util.detectNonVertical(FromGeomToSurface.convertGeom(feat.getGeom()),0.2);
    
    for(IFeature parc : featCollParce){
      
          if(parc.getGeom().intersects(iMS)){
            
            IGeometry geom1 =iMS.intersection( parc.getGeom());
            IGeometry geom2 =iMS.difference(parc.getGeom());
            
            iMS = new GM_MultiSurface<>();
            iMS.addAll(FromGeomToSurface.convertGeom(geom1));
            iMS.addAll(FromGeomToSurface.convertGeom(geom2));
            
          }
      
      
      
    }
    
    feat.setGeom(iMS);
    
  }
  
  String fileOut = "E:/mbrasebin/Donnees/Strasbourg/GTRU/ProjectT3/BDTopoIni/custombati.shp";
// IFeatureCollection<IFeature> featCollParce =  ShapefileReader.read(fileParc);
  ShapefileWriter.write(featColl, fileOut);

  }

}
