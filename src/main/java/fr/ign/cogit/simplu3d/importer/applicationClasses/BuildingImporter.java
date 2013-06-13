package fr.ign.cogit.simplu3d.importer.applicationClasses;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.calculation.Util;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.index.Tiling;
import fr.ign.cogit.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.RoofSurface;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.model.application.WallSurface;

public class BuildingImporter {

  /**
   * Aire minimale pour considérée un polygone comme attaché à une parcelle
   */
  public final static double RATIO_MIN = 0.1;

  public static IFeatureCollection<Building> importBuilding(
      IFeatureCollection<IFeature> featBati,
      IFeatureCollection<SubParcel> sousParcelles) {

    IFeatureCollection<Building> batiments = new FT_FeatureCollection<Building>();

    for (IFeature batiFeat : featBati) {

      // On crée le bâtiment
      Building b = new Building();
      batiments.add(b);
      b.setGeom(batiFeat.getGeom());
      b.setLod2MultiSurface((IMultiSurface<IOrientableSurface>) batiFeat.getGeom());

      // Etape 1 : détection du toit et des façades
      List<IOrientableSurface> lOS = FromGeomToSurface.convertGeom(batiFeat
          .getGeom());
      IMultiSurface<IOrientableSurface> surfaceRoof = Util.detectNonVertical(
          lOS, 0.2);
      IMultiSurface<IOrientableSurface> surfaceWall = Util.detectVertical(lOS,
          0.2);

      // Création facade
      WallSurface f = new WallSurface();
      f.setGeom(surfaceWall);
      f.setLod2MultiSurface(surfaceWall);
      
      List<WallSurface> lF = new ArrayList<WallSurface>();
      lF.add(f);
      b.setFacade(lF);
      
      
      // Etape 2 : on créé l'emprise du bâtiment
      IPolygon poly = EmpriseGenerator.convert(surfaceRoof);
      b.setFootprint(new GM_MultiSurface<IOrientableSurface>());
      
      b.getFootprint().add(poly);
      
      

      IMultiSurface<IOrientableSurface> iMS = new GM_MultiSurface<IOrientableSurface>();
      iMS.add(poly);

      
      
      // Création toit
      RoofSurface t = RoofImporter.create(surfaceRoof, (IPolygon) poly.clone());
     

      // Affectation
      b.setToit(t);

      

      // Etape3 : on associe le bâtiment à la sous parcelles
      if (!sousParcelles.hasSpatialIndex()) {

        sousParcelles.initSpatialIndex(Tiling.class, false);

      }

      Iterator<SubParcel> itSP = sousParcelles.select(poly).iterator();
      
      double aireEmprise = poly.area();

      while (itSP.hasNext()) {

        SubParcel sp = itSP.next();

        double area = (poly.intersection(sp.getGeom())).area();
        
        
        if(area/aireEmprise>RATIO_MIN){
          
          
          //On crée les association Batiments <=> Parcelle
          
          sp.getBuildingsParts().add(b);
          b.getSousParcelles().add(sp);
          
          
        }

      }
      


    }

    return batiments;

  }

}
