package fr.ign.cogit.simplu3d.scenario.implCubeRoof;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IPoint;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;

public class COSBasicRectangleRoofScenario extends BasicRectangleRoofScenario {

  public COSBasicRectangleRoofScenario(BasicPropertyUnit bPU, double largMin,
      double largMax, double longMin, double longMax, double hMin, double hMax,
      double hZMin, double hZMax) {
    super(bPU, largMin, largMax, longMin, longMax, hMin, hMax, hZMin, hZMax);
  }

  @Override
  public double satisfcation() {

    double volBuilt = 0;

    for (AbstractBuilding b : this.getbPU().getBuildings()) {
      volBuilt = volBuilt + b.getFootprint().area() * b.height(1, 2);
    }

    return volBuilt;
  }


  protected void initBPU() {
    // Création de la géométrie de l'unité foncière

    IGeometry geomTemp = this.getbPU().generateGeom();

    // Création des centres possibles au sein de la parcelle cadastrale
    IEnvelope e = geomTemp.envelope();

    try {
      geom = AdapterFactory.toGeometry(new GeometryFactory(), geomTemp);
    } catch (Exception e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    List<SpecificCadastralBoundary> sCB = new ArrayList<SpecificCadastralBoundary>();

    for (CadastralParcel cP : this.getbPU().getCadastralParcel()) {

      for (SpecificCadastralBoundary s : cP.getBoundary()) {
        if (s.getType() == 4) {
          sCB.add(s);
        }
      }

    }

    double xmin = e.minX();
    double xmax = e.maxX();
    double ymin = e.minY();
    double ymax = e.maxY();

    grid = new DirectPositionList();

    for (double x = xmin; x <= xmax; x = x + 2) {
      boucley: for (double y = ymin; y <= ymax; y = y + 2) {

        IDirectPosition dp = new DirectPosition(x, y, 0);

        IPoint pTest = new GM_Point(dp);

        if (geomTemp.intersects(pTest)) {

          for (SpecificCadastralBoundary s : sCB) {

            if (s.getGeom().distance(pTest) > 25) {
              continue boucley;
            }

          }

          for (AbstractBuilding b : this.getbPU().buildings) {

            if (b.footprint.intersects(pTest)) {
              continue boucley;
            }

          }

          grid.add(dp);

        }

      }

    }

  }

}
