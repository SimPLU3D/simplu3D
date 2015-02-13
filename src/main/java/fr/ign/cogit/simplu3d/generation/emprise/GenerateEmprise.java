package fr.ign.cogit.simplu3d.generation.emprise;


import fr.ign.cogit.appli.geopensim.geom.ShapeFactory;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.simplu3d.generation.TopologieBatiment.FormeEmpriseEnum;
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
 * @version 1.0
 **/
public class GenerateEmprise {

  public static IPolygon generateEmprise(FormeEmpriseEnum fB, double l,
      double h, double l1, double h1) {

    if (fB.equals(FormeEmpriseEnum.RECTANGLE)) {

      return (IPolygon) ShapeFactory.createRectangle(new DirectPosition(0, 0),
          l, h);

    }

    if (fB.equals(FormeEmpriseEnum.CERCLE)) {

      return (IPolygon) ShapeFactory.createCercle(new DirectPosition(0, 0), l,
          20);

    }

    if (fB.equals(FormeEmpriseEnum.FORME_U)) {

      return (IPolygon) ShapeFactory.createU(new DirectPosition(0, 0), l, h,
          l1, h1);

    }

    if (fB.equals(FormeEmpriseEnum.FORME_T)) {

      return (IPolygon) ShapeFactory.createT(new DirectPosition(0, 0), l, h,
          l1, h1);

    }

    if (fB.equals(FormeEmpriseEnum.FORME_L)) {

      return (IPolygon) ShapeFactory.createL(new DirectPosition(0, 0), l, h,
          l1, h1);

    }



    return null;

  }

}
