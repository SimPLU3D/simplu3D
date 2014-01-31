package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.vecmath.Point3d;

import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;

import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.representation.PolygonCapture;
import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.gui.InterfaceMap3D;
import fr.ign.cogit.geoxygene.sig3d.representation.basic.Object0d;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiPoint;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;

/**
 * Outil permettant de sélectionner un polygone point calculé par picking
 * 
 * Tools to select a polygon by picking
 * 
 * @author MBrasebin
 */
public class PolygonSelection extends Behavior {

  /**
   * Nom de la couche dans laquelle on ajoute un polygone
   */
  public static final String NOM_COUCHE_POINTS = "Polygone";

  // Variables lié au picking (zone à "picker" et le résultat du "picking")
  private PickCanvas pickCanvas;

  // Canvas rafraîchit permettant le redimensionnement de l'objet
  private Canvas3D canvas3D;

  // variable pour récupèrer les coordonnées écran
  int x, y;

  // Boolean permettant d'afficher ou non les stats de l'objet
  public static boolean info = false;

  // Critère de réveil
  private WakeupOnAWTEvent wakeupCriterion;

  // Constructeur par défaut
  public PolygonSelection(Canvas3D canvas, BranchGroup scene) {

    this.canvas3D = canvas;

    this.pickCanvas = new PickCanvas(this.canvas3D, scene);
    // Tolérance à partir de laquelle les objets en fonction de la distance
    // avec la souris
    // sera sélectionnée
    this.pickCanvas.setTolerance(5f);
    // Pour avoir des infos à partir des frontières de l'objet intersecté
    this.pickCanvas.setMode(PickTool.GEOMETRY_INTERSECT_INFO);

  }

  @Override
  public void initialize() {
    // Réveil si un des boutton de la souris est pressé
    this.wakeupCriterion = new WakeupOnAWTEvent(MouseEvent.MOUSE_CLICKED);
    this.wakeupOn(this.wakeupCriterion);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void processStimulus(Enumeration criteria) {
    while (criteria.hasMoreElements()) {
      WakeupCriterion wakeup = (WakeupCriterion) criteria.nextElement();
      if (wakeup instanceof WakeupOnAWTEvent) {
        // Les évènements souris (ici que les clicks)
        AWTEvent[] events = ((WakeupOnAWTEvent) wakeup).getAWTEvent();

        // donc un seul élément
        for (AWTEvent e : events) {
          try {
            if (e instanceof MouseEvent) {
              MouseEvent evt = (MouseEvent) e;

              // Si cet évènement est bien un évènement souris
              if (evt.getID() == MouseEvent.MOUSE_CLICKED) {

                // On procède à la sélection
                this.pickCanvas.setShapeLocation(evt);

                // Objet récupèré
                PickResult pickResult = this.pickCanvas.pickClosest();

                InterfaceMap3D carte = (InterfaceMap3D) this.canvas3D
                    .getParent();

                // Cas du clic droit ajoute un objet ponctuel
                // dans la couche
                // NOM_COUCHE_POINTS
                if (evt.getButton() == MouseEvent.BUTTON1) {

                  if (pickResult == null) {

                    continue;
                  }

                  Point3d p = pickResult.getIntersection(0)
                      .getPointCoordinates();// pickResult.getIntersection(0).getClosestVertexCoordinates();

                  // On récupère le point
                  DirectPosition gmPoints = new DirectPosition(p.x, p.y, p.z);

                  VectorLayer c = (VectorLayer) carte.getCurrent3DMap()
                      .getLayer(PolygonSelection.NOM_COUCHE_POINTS);

                  if (c == null) {// Si la couche
                    // NOM_COUCHE_POINTS
                    // n'existe pas on l'ajoute

                    FT_FeatureCollection<IFeature> ftColl = new FT_FeatureCollection<IFeature>();

                    IFeature feat = new DefaultFeature(new GM_Point(gmPoints));
                    feat.setRepresentation(new Object0d(feat, true, Color.RED,
                        0.5, true));

                    ftColl.add(feat);

                    c = new VectorLayer(ftColl,
                        PolygonSelection.NOM_COUCHE_POINTS);

                    carte.getCurrent3DMap().addLayer(c);

                  } else {
                    // Sinon on la modifie
                    VectorLayer coucheVecteur = (VectorLayer) carte
                        .getCurrent3DMap().getLayer(
                            PolygonSelection.NOM_COUCHE_POINTS);
                    IFeature feat = coucheVecteur.get(0);

                    if (feat.getGeom() instanceof GM_MultiPoint) {

                      IDirectPositionList dpl = feat.getGeom().coord();
                      dpl.add(gmPoints);

                      GM_MultiPoint gms = new GM_MultiPoint(dpl);
                      feat.setGeom(gms);

                      feat.setRepresentation(new PolygonCapture(feat));
                      coucheVecteur.refresh();

                    } else {

                      DirectPositionList dpl = new DirectPositionList();
                      dpl.add(gmPoints);

                      GM_MultiPoint gms = new GM_MultiPoint(dpl);
                      feat.setGeom(gms);

                      feat.setRepresentation(new PolygonCapture(feat));
                      coucheVecteur.refresh();

                    }
                  }

                }
              }
            }
          } catch (Exception ex) {

            ex.printStackTrace();
          }
        }// Boucle i
      }
    }
    this.wakeupOn(this.wakeupCriterion);
  }

}
