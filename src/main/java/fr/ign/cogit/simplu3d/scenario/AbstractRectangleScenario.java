package fr.ign.cogit.simplu3d.scenario;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IPoint;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.generation.ParametricBuilding;
import fr.ign.cogit.simplu3d.generation.TopologieBatiment;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.Environnement;

public abstract class AbstractRectangleScenario extends AbstractDefaultScenario {

  protected ParametricBuilding currentState = null;

  public static int STOP_IT_INSIDE_PARCEL = 0;

  public AbstractRectangleScenario(BasicPropertyUnit bPU) {
    super(bPU);

  }

  public Building newConfiguration() {

    if (currentState == null) {
      this.initBPU();

      int nbP = grid.size();

      TopologieBatiment tB = new TopologieBatiment(
          TopologieBatiment.FormeEmpriseEnum.RECTANGLE,
          TopologieBatiment.FormeToitEnum.PLAT, null);

      currentState = new ParametricBuilding(tB, this.getRanLarg(),
          this.getRanLon(), 0, 0, 0, this.getRanHei(), null, null, null,
          grid.get((int) (Math.random() * nbP)), this.getRanOrientation(),
          Environnement.getInstance().terrain, 0);
      currentState.generateAll();

    }

    currentState = randomMove();

    int it = 0;

    try {
      while (currentState == null
          || !(geom.contains(AdapterFactory.toGeometry(new GeometryFactory(),
              currentState.footprint)))) {

        /*
         * if(!(geom.contains(new
         * GM_MultiPoint(currentState.footprint.coord())))){ try {
         * GTRU3D.DEBUG_FEAT.add(currentState.cloneGeom()); } catch
         * (CloneNotSupportedException e) { // TODO Auto-generated catch block
         * e.printStackTrace(); } }
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

  public abstract ParametricBuilding randomMove();

  protected IDirectPositionList grid = null;
  private Geometry geom = null;

  /**
   * Initialise la grille et la géométrie de la sous-parcelle
   */
  private void initBPU() {
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
