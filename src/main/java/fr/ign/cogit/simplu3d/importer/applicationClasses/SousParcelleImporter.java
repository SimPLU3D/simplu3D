package fr.ign.cogit.simplu3d.importer.applicationClasses;

import java.util.Collection;
import java.util.Iterator;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.index.Tiling;
import fr.ign.cogit.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.model.application.UrbaZone;

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
  public static IFeatureCollection<SubParcel> create(
      IFeatureCollection<CadastralParcel> parcelles,
      IFeatureCollection<UrbaZone> zones) {

    IFeatureCollection<SubParcel> sousParcelles = new FT_FeatureCollection<SubParcel>();

    if (!zones.hasSpatialIndex()) {
      zones.initSpatialIndex(Tiling.class, false);

    }

    for (CadastralParcel p : parcelles) {

      IGeometry geom = p.getGeom();

      Collection<UrbaZone> zonesTemp = zones.select(geom);

      if (zonesTemp.size() < 2) {

        SubParcel sp = new SubParcel();

        IMultiSurface<IOrientableSurface> ms = FromGeomToSurface
            .convertMSGeom(geom);

        if (ms.size() == 1) {
          sp.setGeom(ms.get(0));
        } else {
          sp.setGeom(ms);
        }

        sp.setLod2MultiSurface(FromGeomToSurface.convertMSGeom(geom));

        // On crée le lien parcelle/sousParcelle
        sp.setParcelle(p);
        p.getSubParcel().add(sp);

        if (zonesTemp.size() == 1) {
          // On crée le lien zone sousParcelle
          UrbaZone z = zonesTemp.iterator().next();
          z.getSubParcels().add(sp);

        }

        // affecteBorduresToSousParcelles(sp,p);

        // Les mêmes bordures
        // sp.setBordures(p.getBordures());

        sousParcelles.add(sp);

        continue;
      }
      // On a plus de 1 zone.
      sousParcelles.addAll(generateSousParcellesFromZone(p, zonesTemp));

    }

    return sousParcelles;

  }

  private static IFeatureCollection<SubParcel> generateSousParcellesFromZone(
      CadastralParcel p, Collection<UrbaZone> zones) {

    IFeatureCollection<SubParcel> sousParcelles = new FT_FeatureCollection<SubParcel>();

    IGeometry geom = p.getGeom();

    Iterator<UrbaZone> itZone = zones.iterator();

    while (itZone.hasNext()) {

      UrbaZone z = itZone.next();

      System.out.println(z.getGeom().getClass().toString());
      System.out.println(geom.getClass().toString());

      IGeometry geomInter = z.getGeom().intersection(geom);

      if (geomInter == null || geomInter.area() < doubleMinArea) {
        continue;
      }
      SubParcel sP = new SubParcel();
      // Onaffecte les géométries
      sP.setGeom(geomInter);
      sP.setLod2MultiSurface(FromGeomToSurface.convertMSGeom(geomInter));

      // Lien sousParcelle <=> Parcelle
      p.getSubParcel().add(sP);
      sP.setParcelle(p);

      // Lien zone => sousParcelle
      z.getSubParcels().add(sP);

      // affecteBorduresToSousParcelles(sP,p);

      sousParcelles.add(sP);

    }

    return sousParcelles;
  }

  /**
   * Renseigne les bordures à partir d'une parcelle donnée - Si la bordure
   * coincide avec une de la parcelle, le type est le même - sinon le type est
   * Bordure.FICTIVE
   * @param sp
   * @param p
   */

  /*
   * public static void affecteBorduresToSousParcelles(SubParcel sp,
   * CadastralParcel p){
   * 
   * 
   * IFeatureCollection<SpecificCadastralBoundary> lBordures =
   * p.getSpecificCadastralBoundary();
   * 
   * 
   * 
   * List<IOrientableSurface> lOS = sp.getLod2MultiSurface().getList();
   * 
   * 
   * for(IOrientableSurface os:lOS){
   * 
   * IPolygon pol = (IPolygon) os;
   * 
   * 
   * List<ILineString> lLS =
   * FromPolygonToLineString.convertPolToLineStrings(pol);
   * 
   * 
   * for(ILineString ls:lLS){
   * 
   * SpecificCadastralBoundary b = new SpecificCadastralBoundary(ls);
   * sp.getSpecificCadastralBoundary().add(b);
   * 
   * boolean isFound = false; for(SpecificCadastralBoundary b2 : lBordures){
   * 
   * 
   * if(b2.getGeom().buffer(0.5).contains(ls)){
   * 
   * b.setTypeDroit(b2.getTypeDroit());
   * 
   * isFound = true; break; }
   * 
   * 
   * }
   * 
   * if(! isFound){
   * 
   * b.setTypeDroit(SpecificCadastralBoundary.INTRA); }
   * 
   * }
   * 
   * 
   * 
   * }
   * 
   * 
   * 
   * 
   * 
   * }
   */

}
