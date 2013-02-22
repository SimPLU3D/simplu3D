package fr.ign.cogit.simplu3d.generation.toit;

import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.geoxygene.util.algo.geomstructure.Segment;
import fr.ign.cogit.sig3d.convert.geom.FromPolygonToLineString;
import fr.ign.cogit.simplu3d.generation.ToitProcedural;
import fr.ign.cogit.simplu3d.generation.TopologieBatiment;
import fr.ign.cogit.simplu3d.generation.TopologieBatiment.FormeToitEnum;
import fr.ign.cogit.simplu3d.model.application.EmpriseBatiment;
import fr.ign.cogit.simplu3d.model.application.Materiau;
import fr.ign.cogit.simplu3d.model.application.Toit;

public class GenerationToit {

  public static ToitProcedural generationToit(TopologieBatiment tB, double zGouttiere, double zMax,
      Materiau m, EmpriseBatiment emprise,double angleToit) {

    
    IPolygon poly =(IPolygon) emprise.getLod2MultiSurface().get(0);
    ToitProcedural t = new ToitProcedural();

    if (tB.getfT() == FormeToitEnum.PLAT) {

      generationToitPlat(tB, t, zGouttiere, zMax, poly);

    } else if (tB.getfT() == FormeToitEnum.EN_APPENTIS) {

      generationToitAppentis(tB, t, zGouttiere, zMax, poly);

    } else if (tB.getfT() == FormeToitEnum.SYMETRIQUE) {

      GenerationToitSymetrique.generate(tB, t, zGouttiere, zMax, poly,angleToit);

    }

    // On affecte le matériau
    t.setMat(m);

    return t;
  }

  public static void generationToitPlat(TopologieBatiment tB, Toit t,
      double zGouttiere, double zMax, IPolygon emprise) {

    // On affecte le z aux sommets
    for (IDirectPosition dp : emprise.coord()) {
      dp.setZ(zGouttiere);

    }

    // On récupère la bordue de l'emprise
    ILineString ls = new GM_LineString(emprise.coord());

    // C'est la gouttière
    IMultiCurve<IOrientableCurve> iMS = new GM_MultiCurve<IOrientableCurve>();
 
    
    
    
    iMS.addAll(Segment.getSegmentList(ls));

    // C'est la géométrie du toit
    IMultiSurface<IOrientableSurface> iS = new GM_MultiSurface<IOrientableSurface>();
    iS.add(emprise);

    // On affecte les géométries
    t.setGeom(emprise);
    t.setLod2MultiSurface(iS);

    // On affecte les autres informations géographiques
    t.setGouttiere(iMS);
    t.setFaitage(new GM_MultiCurve<IOrientableCurve>());
    t.setPignons(new GM_MultiCurve<IOrientableCurve>());

  }

  public static Toit generationToitAppentis(TopologieBatiment tB, Toit t,
      double zGouttiere, double zMax, IPolygon emprise) {

    // On affecte les z

    // La partie la plus haute du bâtiment
    IMultiCurve<ILineString> iMC = new GM_MultiCurve<ILineString>();

    List<Integer> lInd = CorrespondanceIDArcIDSom.getCorrespondanceAppentis(
        tB.getfE(), tB.getlIndArret().get(0));

    for (int i = 0; i < lInd.size(); i = i + 2) {
      IDirectPositionList dpl = new DirectPositionList();

      dpl.add(emprise.coord().get(lInd.get(i)));
      dpl.add(emprise.coord().get(lInd.get(i + 1)));

      iMC.add(new GM_LineString(dpl));

    }

    double dMax = Double.NEGATIVE_INFINITY;

    // On détermine le point le plus loin
    for (IDirectPosition dp : emprise.coord()) {

      double dist = iMC.distance(new GM_Point(dp));
      dMax = Math.max(dist, dMax);

    }

    // On affecte le z aux sommets
    for (IDirectPosition dp : emprise.coord()) {

      double dist = iMC.distance(new GM_Point(dp));

      double z = (1 - dist / dMax) * (zMax - zGouttiere) + zGouttiere;
      dp.setZ(z);

    }

    // On récupère la bordue de l'emprise
    ILineString ls = new GM_LineString(emprise.coord());

    // C'est la gouttière
    IMultiCurve<IOrientableCurve> iMS = new GM_MultiCurve<IOrientableCurve>();
    iMS.add(ls);

    // C'est la géométrie du toit
    IMultiSurface<IOrientableSurface> iS = new GM_MultiSurface<IOrientableSurface>();
    iS.add(emprise);

    // On affecte les géométries
    t.setGeom(emprise);
    t.setLod2MultiSurface(iS);

    // On affecte les autres informations géographiques
    t.setGouttiere(new GM_MultiCurve<ILineString>(FromPolygonToLineString
        .convertPolToLineStrings(emprise)));
    t.setFaitage(iMC);
    t.setPignons(new GM_MultiCurve<IOrientableCurve>());

    return t;

  }

}
