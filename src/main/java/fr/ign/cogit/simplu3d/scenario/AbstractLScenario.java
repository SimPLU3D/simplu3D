package fr.ign.cogit.simplu3d.scenario;

import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiPoint;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.generation.ParametricBuilding;
import fr.ign.cogit.simplu3d.generation.TopologieBatiment;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.Environnement;

public abstract class AbstractLScenario extends AbstractScenario {

  public AbstractLScenario(BasicPropertyUnit bPU) {
    super(bPU);
    // TODO Auto-generated constructor stub
  }

  public Building newConfiguration() {

    if (currentState == null) {
      this.initBPU();

      int nbP = grid.size();

      TopologieBatiment tB = new TopologieBatiment(
          TopologieBatiment.FormeEmpriseEnum.FORME_L,
          TopologieBatiment.FormeToitEnum.PLAT, null);

      currentState = new ParametricBuilding(tB, this.getRanLarg(),
          this.getRanLon(), this.getRanLarg2(), this.getRanH2(), 0,
          this.getRanHei(), null, null, null,
          grid.get((int) (Math.random() * nbP)), this.getRanOrientation(),
          Environnement.getInstance().terrain, 0);
      currentState.generateAll();

    }

    currentState = randomMove();

    int it = 0;

    try {
      while (currentState == null
          || !(geom.contains(AdapterFactory.toGeometry(new GeometryFactory(),
              new GM_MultiPoint(currentState.footprint.coord()))))) {
        /*
         * if (currentState != null) { try {
         * GTRU3D.DEBUG_FEAT.add(currentState.cloneGeom()); } catch
         * (CloneNotSupportedException e) { // TODO Auto-generated catch //
         * block e.printStackTrace(); } }
         */
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

  public abstract double getRanLarg2();

  public abstract double getRanH2();

}
