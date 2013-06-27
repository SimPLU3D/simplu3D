package fr.ign.cogit.simplu3d.scenario;

import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiPoint;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.exec.GTRU3D;
import fr.ign.cogit.simplu3d.generation.ParametricBuilding;
import fr.ign.cogit.simplu3d.generation.TopologieBatiment;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.Environnement;

public abstract class AbstractRectangleRoof extends AbstractScenario {

  public AbstractRectangleRoof(BasicPropertyUnit bPU) {
    super(bPU);
    // TODO Auto-generated constructor stub
  }

  public Building newConfiguration() {

    if (currentState == null) {
      this.initBPU();

      int nbP = grid.size();

      TopologieBatiment tB = new TopologieBatiment(
          TopologieBatiment.FormeEmpriseEnum.RECTANGLE,
          TopologieBatiment.FormeToitEnum.SYMETRIQUE, null);

      currentState = new ParametricBuilding(tB, this.getRanLarg(),
          this.getRanLon(), 0, 0, this.getRanHei(), this.getRanHRoof(), null,
          null, null, grid.get((int) (Math.random() * nbP)),
          this.getRanOrientation(), Environnement.getInstance().terrain,
          Math.PI / 4);
      currentState.generateAll();

    }

    currentState = randomMove();

    int it = 0;

    try {
      while (currentState == null
          || !(geom.contains(AdapterFactory.toGeometry(new GeometryFactory(),
              new GM_MultiPoint(currentState.footprint.coord()))))) {

        if (currentState != null) {
          if (GTRU3D.DEBUG) {

            GTRU3D.DEBUG_FEAT.add(currentState.clone());

          }
        }
        currentState = randomMove();

        it++;

        if (it > STOP_IT_INSIDE_PARCEL) {
          return null;
        }

      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return currentState;

  }

  public abstract double getRanHRoof();

}
