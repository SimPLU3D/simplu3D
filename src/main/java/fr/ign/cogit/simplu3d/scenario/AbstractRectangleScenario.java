package fr.ign.cogit.simplu3d.scenario;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.simplu3d.generation.ParametricBuilding;
import fr.ign.cogit.simplu3d.generation.TopologieBatiment;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;

public abstract class AbstractRectangleScenario extends AbstractDefaultScenario {

  private ParametricBuilding currentState = null;

  public static int STOP_IT_INSIDE_PARCEL = 100;

  public AbstractRectangleScenario(BasicPropertyUnit bPU) {
    super(bPU);

  }

  public Building newConfiguration() {
    int nbP = grid.size();

    if (currentState == null) {
      this.initBPU();

      TopologieBatiment tB = new TopologieBatiment(
          TopologieBatiment.FormeEmpriseEnum.RECTANGLE,
          TopologieBatiment.FormeToitEnum.PLAT, null);

      currentState = new ParametricBuilding(tB, this.getRanLarg(),
          this.getRanLon(), 0, 0, 0, this.getRanHei(), null, null, null,
          grid.get((int) (Math.random() * nbP)), this.getRanOrientation(), 0);

    }

    currentState = randomMove();

    int it = 0;

    while (!(geom.contains(currentState.footprint))) {

      currentState = randomMove();

      it++;

      if (it > STOP_IT_INSIDE_PARCEL) {
        return null;
      }

    }

    return currentState;

  }

  public abstract ParametricBuilding randomMove();

  private IDirectPositionList grid = null;
  private IMultiSurface<IOrientableSurface> geom = null;

  /**
   * Initialise la grille et la géométrie de la sous-parcelle
   */
  private void initBPU() {
    // Création de la géométrie de l'unité foncière
    geom = new GM_MultiSurface<>();
    for (CadastralParcel cP : this.getbPU().getCadastralParcel()) {
      geom.addAll(FromGeomToSurface.convertGeom(cP.getGeom()));
    }

    // Création des centres possibles au sein de la parcelle cadastrale
    IEnvelope e = geom.envelope();

    double xmin = e.minX();
    double xmax = e.maxX();
    double ymin = e.minY();
    double ymax = e.maxY();

    grid = new DirectPositionList();

    for (double x = xmin; x <= xmax; x = x + 0.5) {
      for (double y = ymin; y <= ymax; y = y + 0.5) {

        IDirectPosition dp = new DirectPosition(x, y);

        if (geom.intersects(new GM_Point(dp))) {
          grid.add(dp);

        }

      }

    }

  }

  public abstract double getRanLarg();

  public abstract double getRanLon();

  public abstract double getRanHei();

  public abstract double getRanOrientation();

  public void end() {
    grid = null;
    geom = null;
    currentState = null;
  }

}
