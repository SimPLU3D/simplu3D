package fr.ign.cogit.simplu3d.convert;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;

public class ConvertToLineString {

  public static List<IOrientableCurve> convert(IGeometry geom) {

    if (geom instanceof IMultiCurve<?>) {

      IMultiCurve<IOrientableCurve> iMC = (IMultiCurve<IOrientableCurve>) geom;

      return iMC.getList();

    } else if (geom instanceof IOrientableCurve) {
      List<IOrientableCurve> l = new ArrayList<IOrientableCurve>();
      l.add((IOrientableCurve) geom);

      return l;

    }

    System.out.println("ConvertToLineString : cas non traité "
        + geom.getClass());

    return new ArrayList<IOrientableCurve>();

  }
}
