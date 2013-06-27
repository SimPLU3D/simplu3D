package fr.ign.cogit.simplu3d.scenario.implCube;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.util.algo.JtsUtil;
import fr.ign.cogit.geoxygene.util.algo.MesureOrientationFeuille;
import fr.ign.cogit.geoxygene.util.algo.MesureOrientationV2;
import fr.ign.cogit.geoxygene.util.conversion.JtsGeOxygene;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.Road;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;

public class COSOrientedScenario extends BasicRectangleScenario {

  public COSOrientedScenario(BasicPropertyUnit bPU, double largMin,
      double largMax, double longMin, double longMax, double hMin, double hMax) {
    super(bPU, largMin, largMax, longMin, longMax, hMin, hMax);
  }

  @Override
  public double satisfcation() {

    double volBuilt = 0;

    for (AbstractBuilding b : this.getbPU().getBuildings()) {
      volBuilt = volBuilt + b.getFootprint().area() * Math.sqrt(b.height(1, 2));
    }

    return volBuilt;
  }

  @Override
  public double getRanOrientation() {

    
    /*
    List<Road> lR = new ArrayList<Road>();

    for (CadastralParcel cP : this.getbPU().cadastralParcel) {

      for (SpecificCadastralBoundary sCB : cP.getSpecificCadastralBoundary()) {

        if (sCB.getType() == 4) {

          IFeature feat = sCB.getFeatAdj();

          if (feat instanceof Road) {

            lR.add((Road) feat);

          }

        }

      }

      if (lR.isEmpty()) {

        System.out.println("J'oriente au hasard");
        return Math.random() * 2 * Math.PI;
      }

    }

    Road r = null;

    for (Road rTemp : lR) {

      if (rTemp.getName() != null) {

        if (rTemp.getName()
            .equalsIgnoreCase("RTE DU RHIN")) {
          r = rTemp;

          break;
        }

      }

    }

    if (r == null) {
      int nbElem = lR.size();
      
      System.out.println("J'ai pas la route du Rhin");

      int index = (int) Math.random() * nbElem;
      r = lR.get(index);
    }

    // Orientation du bâtiment
    Geometry polygon;
    try {
      polygon = JtsGeOxygene.makeJtsGeom(this.currentState.getFootprint());

      double orientationBatiment = MesureOrientationV2
          .getOrientationGenerale(polygon);

      if (orientationBatiment == 999.9) {
        // dans ce cas on utilise les murs du batiment
        MesureOrientationFeuille mesureOrientation = new MesureOrientationFeuille(
            polygon, Math.PI * 0.5);
        double orientationCotes = mesureOrientation.getOrientationPrincipale();
        if (orientationCotes != -999.9) {
          orientationBatiment = orientationCotes;
        }
      }

      // Orientation du tronçon
      double orientationTroncon = JtsUtil.projectionPointOrientationTroncon(r
          .getGeom().coord().get(0), (ILineString) r.getAxis().get(0));


      double angle = orientationTroncon - orientationBatiment;

      return angle;
    } catch (Exception e) {
      e.printStackTrace();
    }*/

    return 0;

  }

}
