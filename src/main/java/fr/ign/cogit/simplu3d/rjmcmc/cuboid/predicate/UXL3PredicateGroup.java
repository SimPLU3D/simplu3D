package fr.ign.cogit.simplu3d.rjmcmc.cuboid.predicate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.mpp.configuration.AbstractBirthDeathModification;
import fr.ign.mpp.configuration.AbstractGraphConfiguration;
import fr.ign.rjmcmc.configuration.ConfigurationModificationPredicate;

/**
 * 
 *        This software is released under the licence CeCILL
 * 
 *        see LICENSE.TXT
 * 
 *        see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.7
 **/ 
public class UXL3PredicateGroup<O extends AbstractSimpleBuilding, C extends AbstractGraphConfiguration<O, C, M>, M extends AbstractBirthDeathModification<O, C, M>> implements
    ConfigurationModificationPredicate<C,M> {

  IMultiCurve<IOrientableCurve> curveS;
  int numberMaxOfBoxesInGroup;
  public UXL3PredicateGroup(BasicPropertyUnit bPU, int numberMaxOfBoxesInGroup) {
    
    
    this.numberMaxOfBoxesInGroup =  numberMaxOfBoxesInGroup;

    List<IOrientableCurve> lCurve = new ArrayList<>();

    for (CadastralParcel cP : bPU.getCadastralParcel()) {
      // for (SubParcel sB : cP.getSubParcel()) {
      for (SpecificCadastralBoundary sCB : cP.getSpecificCadastralBoundary()) {

        if (sCB.getType() != SpecificCadastralBoundary.INTRA) {
          IGeometry geom = sCB.getGeom();

          if (geom instanceof IOrientableCurve) {
            lCurve.add((IOrientableCurve) geom);

          } else {
            System.out
                .println("Classe UXL3 : quelque chose n'est pas un ICurve");
          }

          // }

        }

      }

    }

    curveS = new GM_MultiCurve<>(lCurve);

  }

  private List<List<O>> createGroupe(List<O> lBatIn) {

    List<List<O>> listGroup = new ArrayList<>();

    while (!lBatIn.isEmpty()) {

      O batIni = lBatIn.remove(0);

      List<O> currentGroup = new ArrayList<>();
      currentGroup.add(batIni);

      int nbElem = lBatIn.size();

      bouclei: for (int i = 0; i < nbElem; i++) {

        for (O batTemp : currentGroup) {

          if (lBatIn.get(i).getFootprint().distance(batTemp.getFootprint()) < 0.5) {

            currentGroup.add(lBatIn.get(i));
            lBatIn.remove(i);
            i = -1;
            nbElem--;
            continue bouclei;

          }
        }

      }

      listGroup.add(currentGroup);
    }

    return listGroup;
  }

  @Override
  public boolean check(C c, M m) {

    List<O> lO = m.getBirth();

    O batDeath = null;

    if (!m.getDeath().isEmpty()) {

      batDeath = m.getDeath().get(0);

    }

    for (O ab : lO) {
      // System.out.println("Oh une naissance");

      // Pas vérifié ?

      boolean checked = true;

      checked = ab.prospect(curveS, 0.5, 0);
      if (!checked) {
        return false;
      }

      checked = (ab.getFootprint().distance(curveS) > 5);
      if (!checked) {
        return false;
      }

    }

    List<O> lBatIni = new ArrayList<>();

    Iterator<O> iTBat = c.iterator();

    while (iTBat.hasNext()) {

      O batTemp = iTBat.next();

      if (batTemp == batDeath) {
        continue;
      }

      lBatIni.add(batTemp);

    }

    for (O ab : lO) {

      lBatIni.add(ab);

    }

    

    List<List<O>> groupes = createGroupe(lBatIni);

    int nbElem = groupes.size();
    
    for (int i = 0; i < nbElem; i++) {
      
      
      if(groupes.get(i).size() > numberMaxOfBoxesInGroup ){
        return false;
      }
      
      
      
      for (int j = i+1; j < nbElem; j++) {

        if (compareGroup(groupes.get(i), groupes.get(j)) < 5) {
          return false;
        }

      }
    }

    return true;

  }

  private double compareGroup(List<O> l1, List<O> l2) {

    double min = Double.POSITIVE_INFINITY;

    for (O o1 : l1) {
      for (O o2 : l2) {

        min = Math.min(o1.getFootprint().distance(o2.getFootprint()), min);

      }
    }
//    System.out.println(min);
    return min;

  }

}
