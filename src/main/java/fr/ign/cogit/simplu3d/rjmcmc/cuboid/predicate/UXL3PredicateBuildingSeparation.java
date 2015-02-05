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

public class UXL3PredicateBuildingSeparation<O extends AbstractSimpleBuilding, C extends AbstractGraphConfiguration<O, C, M>, M extends AbstractBirthDeathModification<O, C, M>>
    implements ConfigurationModificationPredicate<C, M> {

  IMultiCurve<IOrientableCurve> curveS;

  public UXL3PredicateBuildingSeparation(BasicPropertyUnit bPU) {

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

  @Override
  public boolean check(C c, M m) {

    List<O> lO = m.getBirth();

    O batDeath = null;

    if (!m.getDeath().isEmpty()) {

      batDeath = m.getDeath().get(0);

    }

    for (O ab : lO) {
      // System.out.println("Oh une naissance");

      Iterator<O> iTBat = c.iterator();

      while (iTBat.hasNext()) {

        O batTemp = iTBat.next();

        if (batTemp == batDeath) {
          continue;
        }

        if (batTemp.getFootprint().distance(ab.getFootprint()) < 5) {
          return false;
        }

      }

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

    return true;

  }
}
