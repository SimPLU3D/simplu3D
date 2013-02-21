package fr.ign.cogit.importer;

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
import fr.ign.cogit.model.application.Batiment;
import fr.ign.cogit.model.application.EmpriseBatiment;
import fr.ign.cogit.model.application.Facade;
import fr.ign.cogit.model.application.SousParcelle;
import fr.ign.cogit.model.application.Toit;
import fr.ign.cogit.sig3d.convert.geom.FromGeomToSurface;

public class BuildingImporter {

  /**
   * Aire minimale pour considérée un polygone comme attaché à une parcelle
   */
  public final static double AREA_MIN = 3;

  public static IFeatureCollection<Batiment> importBuilding(
      IFeatureCollection<IFeature> featBati,
      IFeatureCollection<SousParcelle> sousParcelles) {

    IFeatureCollection<Batiment> batiments = new FT_FeatureCollection<Batiment>();

    for (IFeature batiFeat : featBati) {

      // On crée le bâtiment
      Batiment b = new Batiment();
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
      Facade f = new Facade();
      f.setGeom(surfaceWall);
      f.setLod2MultiSurface(surfaceWall);
      
      List<Facade> lF = new ArrayList<Facade>();
      lF.add(f);
      b.setFacade(lF);
      
      
      // Etape 2 : on créé l'emprise du bâtiment
      IPolygon poly = EmpriseGenerator.convert(surfaceRoof);
      
      
      
      EmpriseBatiment empB = new EmpriseBatiment();
      empB.setGeom(poly);

      IMultiSurface<IOrientableSurface> iMS = new GM_MultiSurface<IOrientableSurface>();
      iMS.add(poly);

      empB.setLod2MultiSurface(iMS);
      b.setEmprise(empB);
      
      
      // Création toit
      Toit t = RoofImporter.create(surfaceRoof, (IPolygon) poly.clone());
     

      // Affectation
      b.setToit(t);

      

      // Etape3 : on associe le bâtiment à la sous parcelles
      if (!sousParcelles.hasSpatialIndex()) {

        sousParcelles.initSpatialIndex(Tiling.class, false);

      }

      Iterator<SousParcelle> itSP = sousParcelles.select(poly).iterator();

      while (itSP.hasNext()) {

        SousParcelle sp = itSP.next();

        double area = (poly.intersection(sp.getGeom())).area();
        
        
        if(area>AREA_MIN){
          
          
          //On crée les association Batiments <=> SousParcelle
          
          sp.getBatiments().add(b);
          b.getSousParcelles().add(sp);
          
          
        }

      }

    }

    return batiments;

  }

}
