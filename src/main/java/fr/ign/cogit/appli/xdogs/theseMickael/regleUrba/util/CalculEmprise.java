package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.util;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Node;
import javax.swing.JButton;
import javax.vecmath.Point3d;

import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.Environnement;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.Moteur;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.PolygonSelection;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.Regle;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.representation.Contrainte;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.schemageo.Batiment;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.schemageo.Parcelle;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.schemageo.Toit;
import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.calculation.BooleanOperators;
import fr.ign.cogit.geoxygene.sig3d.calculation.Calculation3D;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion2DObject;
import fr.ign.cogit.geoxygene.sig3d.equation.ApproximatedPlanEquation;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.sig3d.gui.navigation3D.Picking;
import fr.ign.cogit.geoxygene.sig3d.representation.basic.Object3d;
import fr.ign.cogit.geoxygene.sig3d.representation.sample.ObjectCartoon;
import fr.ign.cogit.geoxygene.sig3d.semantic.Map3D;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.geoxygene.sig3d.simplification.aglokada.GroundCasting;
import fr.ign.cogit.geoxygene.sig3d.simplification.aglokada.SegmentSorting;
import fr.ign.cogit.geoxygene.sig3d.simplification.aglokada.ZCutDetection;
import fr.ign.cogit.geoxygene.sig3d.util.MathConstant;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_OrientableSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Solid;

/***
 * Classe exclue ancienne classe d'interface Contient l'algo de découpage de
 * batiments au cas ou ...
 * 
 * @author MBrasebin
 */
public class CalculEmprise implements ActionListener {
  public static int NB_Cont = 0;
  public static int HAUTEUR = 50;

  Map3D carte = null;
  List<Regle> lRegles = null;
  Environnement env = null;

  public CalculEmprise(Map3D carte, List<Regle> lRegles, Environnement env) {

    this.carte = carte;
    this.lRegles = lRegles;
    this.env = env;
  }

  private JButton jSaisie = null;
  private JButton jCOS = null;

  public CalculEmprise(Map3D carte2, JButton jSaisie) {
    // TODO Auto-generated constructor stub
    this.carte = carte2;
    this.jSaisie = jSaisie;

  }

  public CalculEmprise(Map3D carte2, JButton jCOS, boolean b) {
    // TODO Auto-generated constructor stub
    this.jCOS = jCOS;
    this.carte = carte2;

  }

  private static BranchGroup bg = null;

  @SuppressWarnings("unchecked")
  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub

    if (e.getSource().equals(this.jCOS)) {

      IFeature feat = this.carte.getIMap3D().getSelection().get(0);
      FT_FeatureCollection<IFeature> ftFeature = new FT_FeatureCollection<IFeature>();

      if (feat instanceof Batiment) {

        Toit t = ((Batiment) feat).getToit();

        GM_MultiSurface<GM_OrientableSurface> multiP = (GM_MultiSurface) feat
            .getGeom();
        GM_MultiSurface<GM_OrientableSurface> multiPT = (GM_MultiSurface) t
            .getGeom();

        GM_MultiSurface<IOrientableSurface> mTotal = new GM_MultiSurface<IOrientableSurface>();

        mTotal.addAll(multiP);
        mTotal.addAll(multiPT);

        for (int i = 0; i < mTotal.size(); i++) {
          ApproximatedPlanEquation eq = new ApproximatedPlanEquation(
              mTotal.get(i));

          if (Math.abs(eq.getNormale().prodVectoriel(MathConstant.vectZ)
              .norme()) < 0.1) {
            mTotal.remove(i);
            i--;

          }

        }

        double seuil = 2.7;

        ZCutDetection cz = new ZCutDetection();
        List<List<IOrientableSurface>> lsurf = cz.groupingFaces2(
            mTotal.getList(), 2.7);

        List<Double> lZtop = cz.getlZtop();

        // itération de l'algo pour chaque coupe en Z du bâtiment
        for (int n = 0; n < lsurf.size(); n++) {

          List<IOrientableSurface> listeFaces = new ArrayList<IOrientableSurface>();
          listeFaces = (lsurf.get(n));

          double Zmin = 30000;

          if (n == 0) {
            Zmin = Calculation3D.pointMin(listeFaces.get(0)).getZ();

          }
          double Zf = lZtop.get(n).doubleValue();
          Zmin = Zf - seuil;
          List<GM_LineString> lSegmentSol = new ArrayList<GM_LineString>();

          // projection des murs au sol afin d'obtenir la trace de la
          // coupe au
          // "sol" (segments au sol)
          for (int i = 0; i < listeFaces.size(); i++) {
            GroundCasting GroundCasting = new GroundCasting(listeFaces.get(i));
            IDirectPosition dp1 = GroundCasting.getGroundCasting().coord()
                .get(0);

            IDirectPosition dp2 = GroundCasting.getGroundCasting().coord()
                .get(1);

            if (!dp1.equals(dp2, 0.0001)) {
              lSegmentSol.add(GroundCasting.getGroundCasting());
              // mise à niveau de tous les segments au sol par
              // l'intermédiaire du Zmin
              /*
               * if (Zmin > GroundCasting.getZ()) { Zmin = GroundCasting.getZ();
               * }
               */
            }
          }

          // on réordonne les segments au sol par adjacence
          lSegmentSol = new SegmentSorting(lSegmentSol).getLSSSorted();

          List<IOrientableSurface> LFacesSol = new ArrayList<IOrientableSurface>();

          // contruction des murs à partir des segments au sol
          for (int i = 0; i < lSegmentSol.size(); i++) {
            DirectPositionList LPointFaceSol = new DirectPositionList();
            GM_LineString LS = lSegmentSol.get(i);
            DirectPosition dp1s = new DirectPosition(LS.coord().get(0).getX(),
                LS.coord().get(0).getY(), Zmin);
            DirectPosition dp2s = new DirectPosition(LS.coord().get(1).getX(),
                LS.coord().get(1).getY(), Zmin);
            DirectPosition dp3s = new DirectPosition(LS.coord().get(1).getX(),
                LS.coord().get(1).getY(), Zf);
            DirectPosition dp4s = new DirectPosition(LS.coord().get(0).getX(),
                LS.coord().get(0).getY(), Zf);

            LPointFaceSol.add(dp1s);
            LPointFaceSol.add(dp4s);
            LPointFaceSol.add(dp3s);
            LPointFaceSol.add(dp2s);
            LPointFaceSol.add(dp1s);

            GM_LineString ls = new GM_LineString(LPointFaceSol);
            GM_OrientableSurface face_sol = new GM_Polygon(ls);

            LFacesSol.add(face_sol);

          }

          IFeature featTemp = new DefaultFeature(new GM_Solid(LFacesSol));
          featTemp.setRepresentation(new Object3d(featTemp, new Color(
              (int) (Math.random() * 255), (int) (Math.random() * 255),
              (int) (Math.random() * 255))));

          ftFeature.add(featTemp);
        }

        this.carte.addLayer(new VectorLayer(ftFeature, "Découpe"));
      }

    } else if (e.getSource().equals(this.jSaisie)) {
      BranchGroup scene = this.carte.getIMap3D().getScene();
      int nbNoeuds = scene.numChildren();

      if (this.jSaisie.getBackground().equals(Color.red)) {

        for (int i = 0; i < nbNoeuds; i++) {

          Node n = scene.getChild(i);

          if (n instanceof Picking) {
            ((Picking) n).setEnable(false);
          }

        }
        if (CalculEmprise.bg == null) {
          CalculEmprise.bg = new BranchGroup();
          CalculEmprise.bg.setCapability(BranchGroup.ALLOW_DETACH);
          Behavior comportement = new PolygonSelection(this.carte.getIMap3D()
              .getCanvas3D().getOffscreenCanvas3D(), scene);
          BoundingSphere bounds = new BoundingSphere(new Point3d(),
              Double.POSITIVE_INFINITY);

          comportement.setSchedulingBounds(bounds);
          CalculEmprise.bg.addChild(comportement);
          scene.addChild(CalculEmprise.bg);
        } else if (!CalculEmprise.bg.isLive()) {
          scene.addChild(CalculEmprise.bg);

        }

        this.jSaisie.setBackground(Color.green);

      } else {

        for (int i = 0; i < nbNoeuds; i++) {

          Node n = scene.getChild(i);

          if (n instanceof Picking) {
            ((Picking) n).setEnable(true);
          }

        }
        if (CalculEmprise.bg != null && CalculEmprise.bg.isLive()) {
          CalculEmprise.bg.detach();
        }

        VectorLayer couche = (VectorLayer) this.carte
            .getLayer(PolygonSelection.NOM_COUCHE_POINTS);

        IFeature feat = couche.get(0);

        Box3D b = new Box3D(feat.getGeom());

        double zmin = b.getLLDP().getZ();

        IDirectPositionList dpl = feat.getGeom().coord();
        dpl.add(dpl.get(0));

        IGeometry geom = Extrusion2DObject.convertFromGeometry(new GM_Polygon(
            new GM_LineString(dpl)), zmin, zmin + CalculEmprise.HAUTEUR);

        couche.get(0).setGeom(geom);
        // couche.appliqueSemio(true, Color.cyan, 1, true);
        couche.get(0).setRepresentation(
            new ObjectCartoon(couche.get(0), Color.yellow, Color.black, 3, 1));
        couche.refresh();
        this.jSaisie.setBackground(Color.red);
        this.jSaisie = null;
      }

    } else {

      List<FT_FeatureCollection<Contrainte>> lContrainte = new ArrayList<FT_FeatureCollection<Contrainte>>();

      Moteur m = new Moteur(this.env, this.lRegles);

      IFeatureCollection<IFeature> featSelec = this.carte.getIMap3D()
          .getSelection();
      int nbSelec = featSelec.size();

      for (int i = 0; i < nbSelec; i++) {
        IFeature feat = featSelec.get(i);

        if (feat instanceof Parcelle) {
          if (feat.getGeom() instanceof GM_Polygon) {
            lContrainte.addAll(m.computeConstraints((Parcelle) feat));
          }
        }

      }

      int nbContraintesCalculees = lContrainte.size();

      for (int i = 0; i < nbContraintesCalculees; i++) {
        this.carte.addLayer(new VectorLayer(lContrainte.get(i), "RC ,°"
            + CalculEmprise.NB_Cont++));

      }

      FT_FeatureCollection<IFeature> enveloppCalculee = new FT_FeatureCollection<IFeature>();

      // Calcul du solide final
      boucleconstaintes: for (int i = 0; i < nbContraintesCalculees; i++) {

        FT_FeatureCollection<Contrainte> lContraintesActus = lContrainte.get(i);

        int nbContraintesActus = lContraintesActus.size();

        GM_Solid solidIni = (GM_Solid) lContraintesActus.get(0).getGeom();

        for (int j = 1; j < nbContraintesActus; j++) {

          IGeometry geom = lContraintesActus.get(j).getGeom();

          if (geom instanceof GM_Solid) {

            try {
              solidIni = BooleanOperators.compute(new DefaultFeature(geom),
                  new DefaultFeature(solidIni), BooleanOperators.INTERSECTION);
            } catch (Exception ex) {
              ex.printStackTrace();
              System.out.println("Erreur calcul intersection");
              continue boucleconstaintes;
            }
          }

        }

        if (solidIni != null) {
          // On tient le bestio
          // On ajoute les contraintes non calculées

          // Batiment bat = new Batiment( new GM_Solid(
          // FusionTriangle.fusionnePolygonne(solidIni.getListeFacettes())));
          Batiment bat = new Batiment(new GM_Solid(
              FusionTriangle.fusionnePolygonne(solidIni.getFacesList())));

          enveloppCalculee.add(bat);

          enveloppCalculee.add(bat.getToit());

        }
        System.out.println("number " + i + "/" + nbContraintesCalculees);
      }

      this.carte.addLayer(new VectorLayer(enveloppCalculee,
          "Emprises constructibles"));
    }
  }

  public Batiment build = null;
  public Toit t = null;

  public void genereBatimentToit(GM_Solid sol) {

    this.build = null;
    this.t = null;




    Batiment b = new Batiment(sol, new Color(127, 0, 0), new Color(127,80,0));

    b.setToit(t);
    t.setBatiment(b);
  }

}
