package fr.ign.cogit.simplu3d.generation.facade;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ITriangle;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Triangle;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.simplu3d.generation.FacadeProcedural;
import fr.ign.cogit.simplu3d.model.application.Materiau;
import fr.ign.cogit.simplu3d.model.application.RoofSurface;

public class GenerationFacade {

  public static List<FacadeProcedural> generate(RoofSurface t,
      List<Materiau> lMat, double zMin, boolean[] facadesNonAveugles) {

    List<FacadeProcedural> lF = new ArrayList<FacadeProcedural>();

    IMultiCurve<IOrientableCurve> iMC = t.setGutter();

    int count = 0;

    for (IOrientableCurve iC : iMC) {

      IDirectPosition dp1 = iC.coord().get(0);
      IDirectPosition dp2 = iC.coord().get(1);
      IDirectPosition dp3 = (IDirectPosition) dp2.clone();
      dp3.setZ(zMin);
      IDirectPosition dp4 = (IDirectPosition) dp1.clone();
      dp4.setZ(zMin);

      IDirectPositionList dpl = new DirectPositionList();
      dpl.add(dp1);
      dpl.add(dp2);
      dpl.add(dp3);
      dpl.add(dp4);
      dpl.add(dp1);

      ITriangle t1 = new GM_Triangle(dp1, dp3, dp2);
      ITriangle t2 = new GM_Triangle(dp1, dp4, dp3);

      IMultiSurface<IOrientableSurface> mS = new GM_MultiSurface<IOrientableSurface>();
      mS.add(t1);
      mS.add(t2);

      FacadeProcedural f = new FacadeProcedural();
      f.setGeom(mS);
      f.setLod2MultiSurface(mS);

      if (lMat != null) {

        if (lMat.size() == 1) {
          f.setMat(lMat.get(0));
        }

        if (count < lMat.size()) {

          f.setMat(lMat.get(count));

        }

      }
      if (facadesNonAveugles != null) {

        if (count < facadesNonAveugles.length) {
          f.setAveugle(facadesNonAveugles[count]);
        } else {
          f.setAveugle(false);
        }
      }

      lF.add(f);
      count++;

    }

    return lF;

  }

}
