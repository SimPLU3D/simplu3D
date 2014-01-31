package fr.ign.cogit.appli.xdogs.theseMickael.indicators.oneBuildingPart.dim3;

import java.util.List;

import fr.ign.cogit.appli.xdogs.theseMickael.indicators.indicatorSchem.oneBuildingPart.SingleBIndicator;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion3DObject;
import fr.ign.cogit.geoxygene.sig3d.equation.PlanEquation;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.sig3d.geometry.topology.Triangle;
import fr.ign.cogit.sig3d.calculation.CutBuilding;
import fr.ign.cogit.sig3d.convert.decomposition.Building;

public class FloorArea extends SingleBIndicator {

  public double FLOORHEIGHT = 3;
  public double LIVEABLEFLOOR = 1.8;
  
  private double shon = 0;

  public FloorArea(Building p) {
    super(p);

    Box3D b = new Box3D(p.getGeom());

    double zMin = b.getLLDP().getZ();

    for (Triangle t : p.getlTriToit()) {

      Box3D bTemp = new Box3D(t);
      double zMinTemp = bTemp.getURDP().getZ();

      IGeometry geomTemp = Extrusion3DObject.convertitFromPolygon(t, zMin
          - zMinTemp);

      double shonTemp = calculSHONNotFlatRoof(geomTemp);

      if (shonTemp > 0) {
        shon = shon + shonTemp;
      }
    }

  }

  private double calculSHONNotFlatRoof(IGeometry geom) {
    Box3D b = new Box3D(geom);

    double zMin = b.getLLDP().getZ();
    double zMax = b.getURDP().getZ();

    int nbFloor = (int) ((zMax - zMin) / FLOORHEIGHT) + 1;

    double zActu = zMin;
    double areaSHON = 0;

    for (int i = 0; i < nbFloor; i++) {

      PlanEquation eq = new PlanEquation(0, 0, -1, zActu + LIVEABLEFLOOR);
      List<IOrientableSurface> lG = CutBuilding.cutWithPPPolygon(
          FromGeomToSurface.convertGeom(geom), eq);

      double areaTemp = calculAreaFromCut(lG);

      if (areaTemp == -1) {
        return -1;
      }

      zActu = zActu + FLOORHEIGHT;

      areaSHON = areaSHON + areaTemp;

    }

    if (areaSHON == 0 && nbFloor == 0 && (zMax - zMax) > LIVEABLEFLOOR) {
      PlanEquation eq = new PlanEquation(0, 0, -1, (zMax + zMax) / 2);

      List<IOrientableSurface> lG = CutBuilding.cutWithPPPolygon(
          FromGeomToSurface.convertGeom(geom), eq);

      double areaTemp = calculAreaFromCut(lG);

      areaSHON = areaTemp;

    }

    return areaSHON;
  }

  /**
   * 
   * @param lG
   * @return
   */
  private static double calculAreaFromCut(List<IOrientableSurface> lG) {
    double areaTemp = 0;

    if (lG == null) {
      return -1;
    }

    int nbGeom = lG.size();
    for (int j = 0; j < nbGeom; j++) {

      IGeometry gT = lG.get(j);

      if (gT instanceof IPolygon) {
        if (gT.isValid()) {
          areaTemp = areaTemp + gT.area();
        } else {
          return -1;

        }

      } else {
        areaTemp = -1;
        return areaTemp;
      }

    }

    return areaTemp;
  }

  
  
  public  String getType(){
    // TODO Auto-generated method stub
    return SingleBIndicator.NAME_NUMERIC;
  }


  public String getAttributeName() {
    // TODO Auto-generated method stub
    return "FloorArea";
  }
  



  @Override
  public String getAggregationType() {
    // TODO Auto-generated method stub
    return SingleBIndicator.AGG_SUM;
  }

  @Override
  public Object getValue() {
    // TODO Auto-generated method stub
    return shon;
  }

}
