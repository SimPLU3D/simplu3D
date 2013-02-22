package fr.ign.cogit.simplu3d.importer;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.index.Tiling;
import fr.ign.cogit.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.sig3d.convert.geom.FromPolygonToLineString;
import fr.ign.cogit.simplu3d.model.application.Bordure;
import fr.ign.cogit.simplu3d.model.application.Parcelle;
import fr.ign.cogit.simplu3d.model.application.SousParcelle;
import fr.ign.cogit.simplu3d.model.application.Zone;

public class SousParcelleImporter {
  
  
  
  /**
   * Aire minimale pour une sous parcelle
   */
  public static double doubleMinArea = 3;

  /**
   * Crée les sousPArcelles ainsi que le lien entre sousParcelle et parcelle et
   * entre sousParcelle et zone
   * @param parcelles
   * @param zones
   * @return
   */
  public static IFeatureCollection<SousParcelle> create(
      IFeatureCollection<Parcelle> parcelles, IFeatureCollection<Zone> zones) {

    IFeatureCollection<SousParcelle> sousParcelles = new FT_FeatureCollection<SousParcelle>();

    if (!zones.hasSpatialIndex()) {
      zones.initSpatialIndex(Tiling.class, false);

    }

    for (Parcelle p : parcelles) {

      IGeometry geom = p.getGeom();

      Collection<Zone> zonesTemp = zones.select(geom);

      if (zonesTemp.size() < 2) {

        SousParcelle sp = new SousParcelle();
        sp.setGeom(geom);
        sp.setLod2MultiSurface(FromGeomToSurface.convertMSGeom(geom));
        
        
        //On crée le lien parcelle/sousParcelle
        sp.setParcelle(p);
        p.getSousParcelles().add(sp);

        if (zonesTemp.size() == 1) {
          //On crée le lien zone sousParcelle
            Zone z = zonesTemp.iterator().next();
            z.getSousParcelles().add(sp);
            
        }
        
        
        affecteBorduresToSousParcelles(sp,p);
        
        //Les mêmes bordures
        sp.setBordures(p.getBordures());
        
        sousParcelles.add(sp);
        
        continue;
      }
      //On a plus de 1 zone.
      sousParcelles.addAll(generateSousParcellesFromZone(p,zonesTemp));

    }

    return sousParcelles;

  }
  
  
  private static IFeatureCollection<SousParcelle> generateSousParcellesFromZone(Parcelle p, Collection<Zone> zones){
    
    
    
    IFeatureCollection<SousParcelle> sousParcelles = new FT_FeatureCollection<SousParcelle>();
    
    
    IGeometry geom = p.getGeom();
    
    Iterator<Zone> itZone = zones.iterator();
    
 
    while(itZone.hasNext()){
      
      Zone z = itZone.next();
      
      
      System.out.println(z.getGeom().getClass().toString());
      System.out.println(geom.getClass().toString());
      
      IGeometry geomInter = z.getGeom().intersection(geom);
      
      
      if(geomInter == null || geomInter.area() < doubleMinArea){
        continue;
      }
      SousParcelle sP = new SousParcelle();
      //Onaffecte les géométries
      sP.setGeom(geomInter);
      sP.setLod2MultiSurface(FromGeomToSurface.convertMSGeom(geomInter));
      
      
      //Lien sousParcelle <=> Parcelle
      p.getSousParcelles().add(sP);
      sP.setParcelle(p);
      
      
      
      //Lien zone => sousParcelle
      z.getSousParcelles().add(sP);
      
      affecteBorduresToSousParcelles(sP,p);
      
      
      sousParcelles.add(sP);
      
    }
  
    
    return sousParcelles;
  }
  
  
  
  /**
   * Renseigne les bordures à partir d'une parcelle donnée
   * - Si la bordure coincide avec une de la parcelle, le type est le même
   * - sinon le type est Bordure.FICTIVE
   * @param sp
   * @param p
   */
  public static void affecteBorduresToSousParcelles(SousParcelle sp, Parcelle p){
    
    
    IFeatureCollection<Bordure> lBordures = p.getBordures();
    

    
    List<IOrientableSurface> lOS = sp.getLod2MultiSurface().getList();
    
    
    for(IOrientableSurface os:lOS){
      
      IPolygon pol = (IPolygon) os;
      
      
      List<ILineString> lLS =   FromPolygonToLineString.convertPolToLineStrings(pol);
      
      
      for(ILineString ls:lLS){
        
        Bordure b = new Bordure(ls);
        sp.getBordures().add(b);
        
        boolean isFound = false;
        for(Bordure b2 : lBordures){

          
          if(b2.getGeom().buffer(0.5).contains(ls)){
            
            b.setTypeDroit(b2.getTypeDroit());
            
            isFound = true;
            break; 
          }
          
          
        }
        
        if(! isFound){
          
          b.setTypeDroit(Bordure.FICTIVE);
        }
        
      }  
      
      
      
    }
    
    
    
    
    
  }

}
