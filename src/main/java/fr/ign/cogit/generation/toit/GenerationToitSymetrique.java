package fr.ign.cogit.generation.toit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.ign.cogit.contrib.CampSkeleton;
import fr.ign.cogit.generation.TopologieBatiment;
import fr.ign.cogit.generation.TopologieBatiment.FormeEmpriseEnum;
import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IPoint;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IRing;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Arc;
import fr.ign.cogit.geoxygene.contrib.cartetopo.CarteTopo;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Face;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Noeud;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.model.application.Toit;

public class GenerationToitSymetrique {

  public static IMultiSurface<IPolygon> generate(TopologieBatiment tB, Toit t,
      double zGut, double zMax, IPolygon emprise,double angleToit) {

    IMultiCurve<ILineString> iMC = new GM_MultiCurve<ILineString>();

    List<Integer> lInd = CorrespondanceIDArcIDSom.getCorrespondanceSymetrique(
        tB.getfE(), tB.getlIndArret());
    int nbInt = lInd.size();

    for (int i = 0; i < nbInt; i = i + 2) {
      IDirectPositionList dpl = new DirectPositionList();

      dpl.add(emprise.coord().get(lInd.get(i)));
      dpl.add(emprise.coord().get(lInd.get(i + 1)));

      iMC.add(new GM_LineString(dpl));

    }

    // On affecte les pignons
    t.setPignons(iMC);

    // Les gouttières forment le countour de la forme - le toit pignon
    IMultiCurve<IOrientableCurve> gouttiere = new GM_MultiCurve<IOrientableCurve>();

    // On récupére les arrêtes qui sont sur le contour et n'intersecte pas le
    // toit pignon
    IDirectPositionList dplExt = emprise.getExterior().coord();
    int nbExt = dplExt.size();

    // Indices pour repérer les arrêtes du toit pignon
    List<Integer> lIndPignon = new ArrayList<Integer>();

    bouclext: for (int i = 0; i < nbExt - 1; i++) {
      // On récupère une arrête et on vérifie qu'elle intersecte le 2 sommets du
      // toit pignon

      IDirectPosition dp1 = dplExt.get(i);
      IDirectPosition dp2 = dplExt.get(i + 1);

      IPoint p1 = new GM_Point(dp1);
      IPoint p2 = new GM_Point(dp2);

      for (ILineString ls : iMC) {

        // C'est le cas, l'arrête est un pignon de toit
        if (p1.intersects(ls) && p2.intersects(ls)) {

          lIndPignon.add(i);
          continue bouclext;
        }

      }

      // Sinon, c'est une gouttière
      IDirectPositionList dplTemp = new DirectPositionList();

      dplTemp.add(dp1);
      dplTemp.add(dp2);

      gouttiere.add(new GM_LineString(dplTemp));
    }

    // On prépare les angles actuellement
    double[] angles = calculateAngle(emprise, lIndPignon,angleToit, CorrespondanceIDArcIDSom.getIDSpeed(tB.getfE()));

    if (emprise.getInterior() != null) {
      gouttiere.addAll(emprise.getInterior());
    }

    IMultiCurve<IOrientableCurve> goutOut = new GM_MultiCurve<IOrientableCurve>();

    IMultiSurface<IPolygon> generatedRoof = new GM_MultiSurface<IPolygon>();

    try {

      // On calcule le squelette et on récupère la carte topo
      CampSkeleton cS = new CampSkeleton(emprise, angles);

      IMultiCurve<IOrientableCurve> faitage = new GM_MultiCurve<IOrientableCurve>();

      if (tB.getfE() == FormeEmpriseEnum.CERCLE) {

        if (iMC.size() == 0) {
          // on crée un lineString de longueur0
          Noeud toKeep = null;
          int ind = 0;

          for (Noeud b : cS.getCarteTopo().getPopNoeuds()) {

            int indTemp = b.arcs().size();

            if (indTemp > ind) {
              ind = indTemp;
              toKeep = b;
            }

          }

          IDirectPositionList dpl = new DirectPositionList();
          dpl.add((IDirectPosition) toKeep.getGeometrie().coord().get(0)
              .clone());
          dpl.add((IDirectPosition) toKeep.getGeometrie().coord().get(0)
              .clone());
          iMC.add(new GM_LineString(dpl));
          faitage.addAll(iMC);
        } else {

          faitage.addAll(iMC);

          for (Arc a : cS.getIncludedArcs()) {

            if (a.getGeometrie().intersects(iMC)) {

              // on prend la partie qui va vers le centre

              List<Noeud> lNoeuds = new ArrayList<Noeud>();
              lNoeuds.add(a.getNoeudIni());
              lNoeuds.add(a.getNoeudFin());

              for (Noeud n : lNoeuds) {
                Set<Arc> arcs = new HashSet<Arc>();
                arcs.addAll(n.getEntrants());
                arcs.addAll(n.getSortants());

                for (Arc aToTest : arcs) {

                  if (aToTest.equals(a)) {
                    continue;
                  }

                  if (aToTest.getGeometrie().intersects(iMC)) {
                    if (aToTest.getFaceDroite() != null
                        && aToTest.getFaceGauche() != null) {
                      faitage.add((IOrientableCurve) aToTest.getGeometrie()
                          .clone());
                    }
                  }

                }

              }

            }

          }

        }
        t.setFaitage(faitage);
        // System.out.println("Faitage :  " + faitage.size());
      } else {

        for (Arc a : cS.getIncludedArcs()) {
          faitage.add((IOrientableCurve) a.getGeometrie().clone());
        }

        t.setFaitage(faitage);

      }

      CarteTopo ct = cS.getCarteTopo();

      // Les polygones des toits

      IPopulation<Face> popFace = ct.getPopFaces();
      Collection<Face> cF = new FT_FeatureCollection<Face>();
      for (Face f : popFace) {

        if (f.getGeometrie().area() < 0.01) {
          cF.add(f);
          continue;
        }

      }

      ct.enleveFaces(cF);

      // Pour chaque face, on assigne le z en fonction de la distance à
      // l'extérieur du bâtiment
      for (Face f : ct.getPopFaces()) {

        for (Arc a : f.arcs()) {

          if (a.getFaceDroite() == null && a.getFaceGauche() != null) {

            Face cf = a.getFaceGauche();

            if (!cf.getArcsIndirects().contains(a)) {

              IDirectPositionList dpl = new DirectPositionList();
              dpl.add(a.getNoeudFin().getCoord());
              dpl.add(a.getNoeudIni().getCoord());

              goutOut.add(new GM_LineString(dpl));
            } else {
              goutOut.add(a.getGeometrie());
            }

          }

          if (a.getFaceGauche() == null && a.getFaceDroite() != null) {

            Face cf = a.getFaceDroite();

            if (!cf.getArcsIndirects().contains(a)) {

              IDirectPositionList dpl = new DirectPositionList();
              dpl.add(a.getNoeudFin().getCoord());
              dpl.add(a.getNoeudIni().getCoord());

              goutOut.add(new GM_LineString(dpl));

            } else {
              goutOut.add(a.getGeometrie());
            }

          }

        }

        IPolygon poly = (IPolygon) f.getGeom();

        if (poly == null || poly.getExterior() == null) {
          continue;
        }

        IDirectPositionList dpl = poly.coord();

        for (IDirectPosition dp : dpl) {

          IPoint p = new GM_Point(dp);

          double dist = faitage.distance(p);

          if (dist > 0.50) {

            dp.setZ(zGut); // dp.setZ(zmax);

          } else {

            dp.setZ(zMax);
          }

        }
        generatedRoof.add(poly);

      }

      for (IDirectPosition dp : goutOut.coord()) {

        IPoint p = new GM_Point(dp);

        double dist = faitage.distance(p);

        if (dist > 0.50) {

          dp.setZ(zGut); // dp.setZ(zmax);

        } else {

          dp.setZ(zMax);
        }

      }

      for (IDirectPosition dp : faitage.coord()) {

        dp.setZ(zMax);

      }

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

    t.setGouttiere(goutOut);
    t.setGeom(generatedRoof);
    t.setLod2MultiSurface(generatedRoof);

    return generatedRoof;
  }

  private static double[] calculateAngle(IPolygon p, List<Integer> lInt, double angleToit, List<Integer> lIntSpeed) {

    List<IRing> interiorRing = p.getInterior();

    int nbTotalContrib = p.getExterior().coord().size() - 1;

    for (IRing r : interiorRing) {

      nbTotalContrib = nbTotalContrib + r.coord().size() - 1;

    }

    double[] angles = new double[nbTotalContrib];

    for (int i = 0; i < nbTotalContrib; i++) {

      if (lInt.contains(i)) {
        angles[i] = 0;
      } else {
        
        if(lIntSpeed.contains(i)){
          angles[i] = angleToit;
        }else{
          angles[i] = Math.PI / 4;
        }
        
     
      }

    }

    return angles;

  }

}
