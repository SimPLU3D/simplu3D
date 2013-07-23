package fr.ign.cogit.simplu3d.importer.applicationClasses;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.calculation.Proximity;
import fr.ign.cogit.geoxygene.sig3d.equation.ApproximatedPlanEquation;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.simplu3d.model.application.Road;

public class RoadImporter {

  public final static String ATT_NOM_RUE = "nom_rue_g";
  public final static String ATT_LARGEUR = "largeur";
  public final static String ATT_TYPE = "nature";

  @SuppressWarnings("unchecked")
  public static IFeatureCollection<Road> importRoad(
      IFeatureCollection<IFeature> voirieColl) {

    IFeatureCollection<Road> voiries = new FT_FeatureCollection<Road>();

    for (IFeature feat : voirieColl) {

      double largeur = Double.parseDouble(feat.getAttribute(ATT_LARGEUR)
          .toString());
      String nom = feat.getAttribute(ATT_NOM_RUE).toString();
      String type = feat.getAttribute(ATT_TYPE).toString();

      Road v = new Road();
      v.setName(nom);
      v.setWidth(largeur);

      List<String> usages = new ArrayList<String>();
      usages.add(type);

      v.setUsage(usages);

      // on affecte l'axe de la rue

      IGeometry geom = feat.getGeom();

      IMultiCurve<ILineString> axe = null;

      if (geom instanceof ILineString) {

        ILineString c = (ILineString) geom;
        axe = new GM_MultiCurve<ILineString>();
        axe.add(c);

      } else if (geom instanceof IMultiCurve<?>) {

        axe = (IMultiCurve<ILineString>) geom;

      }

      if (axe == null) {
        System.out.println("Error in Voirie Importer axe is not a ILineString");
        continue;
      }

      v.setAxe(axe);

      // On créé la géométrie buffer

      if (largeur == 0) {
        continue;
      }

      IGeometry obj = geom.buffer(largeur);

      IDirectPositionList dpl = obj.coord();
      IDirectPositionList dplRoute = feat.getGeom().coord();

      int nbDPL = dpl.size();

      for (int i = 0; i < nbDPL; i++) {
        Proximity c = new Proximity();
        IDirectPosition dp = dpl.get(i);

        c.nearest(dp, dplRoute);
        dp.setZ(c.nearest.getZ());

      }

      ApproximatedPlanEquation eq = new ApproximatedPlanEquation(dpl);

      Vecteur normal = eq.getNormale();

      if (normal.getZ() < 0) {
        dpl.inverseOrdre();

      }

      IMultiSurface<IOrientableSurface> surfVoie = FromGeomToSurface
          .convertMSGeom(obj);

      v.setLod2MultiSurface(surfVoie);
      v.setGeom(surfVoie);

      voiries.add(v);
    }

    return voiries;
  }

}
