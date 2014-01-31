package fr.ign.cogit.gru3d.regleUrba.generation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.appli.geopensim.feature.micro.FormeBatiment;
import fr.ign.cogit.appli.geopensim.geom.ConstructionBatiment;
import fr.ign.cogit.appli.geopensim.geom.ShapeFactory;
import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.gui.MainWindow;
import fr.ign.cogit.geoxygene.sig3d.representation.sample.ObjectCartoon;
import fr.ign.cogit.geoxygene.sig3d.semantic.Map3D;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.gru3d.regleUrba.generation.toit.FlatRoof;
import fr.ign.cogit.gru3d.regleUrba.generation.toit.FourPlanesRoof;
import fr.ign.cogit.gru3d.regleUrba.generation.toit.OnePlaneRoof;
import fr.ign.cogit.gru3d.regleUrba.generation.toit.PignonRoof;
import fr.ign.cogit.sig3d.gui.toolbar.IOToolBar;

public class Construction3DBuilding {

  public static void main(String[] args) {

    List<IPolygon> lGeom = new ArrayList<IPolygon>();

    List<FormeBatiment> lForm = new ArrayList<FormeBatiment>();

    lGeom.add((IPolygon) ShapeFactory.createCarre(
        new DirectPosition(0, lGeom.size() * 100), 20));

    lForm.add(FormeBatiment.Carre);

    lGeom.add(construct(FormeBatiment.Rectangle,
        new DirectPosition(0, lGeom.size() * 100), 150, 0.6, 50, 500));

    lForm.add(FormeBatiment.Rectangle);

    lGeom.add((IPolygon) ShapeFactory.createBarre(
        new DirectPosition(0, lGeom.size() * 100), 30, 50, 10, 10));

    lForm.add(FormeBatiment.Barre);

    lGeom.add(construct(FormeBatiment.Cercle,
        new DirectPosition(0, lGeom.size() * 100), 150, 0.6, 50, 500));

    lForm.add(FormeBatiment.Cercle);

    lGeom.add((IPolygon) ShapeFactory.createEscalier2(new DirectPosition(0,
        lGeom.size() * 100), 30, 50, 10, 10, 10, 10));

    lForm.add(FormeBatiment.Escalier);

    lGeom.add((IPolygon) ShapeFactory.createU(
        new DirectPosition(0, lGeom.size() * 100), 30, 50, 10, 10));

    lForm.add(FormeBatiment.FormeU);

    lGeom.add((IPolygon) ShapeFactory.createT(
        new DirectPosition(0, lGeom.size() * 100), 20, 20, 20, 10));

    lForm.add(FormeBatiment.FormeT);

    lGeom.add((IPolygon) ShapeFactory.createL(
        new DirectPosition(0, lGeom.size() * 100), 20, 20, 4, 16));

    lForm.add(FormeBatiment.FormeL);

    IFeatureCollection<IFeature> featC = new FT_FeatureCollection<IFeature>();

    int nbPoly = lGeom.size();
    for (int i = 0; i < nbPoly; i++) {
      IPolygon geom = lGeom.get(i);
      FormeBatiment fB = lForm.get(i);

      System.out.println("NB : Coord " + geom.coord().size());

      // System.out.println(geom.getClass());

      FlatRoof fR = new FlatRoof(geom, 40);

      DefaultFeature df = new DefaultFeature(fR.generateBuilding(30));
      df.setRepresentation(new ObjectCartoon(df, Color.orange));

      featC.add(df);

      geom = (IPolygon) geom.translate(45, 0, 0);

      IDirectPositionList dpl = new DirectPositionList();
      dpl.add(geom.coord().get(0));
      dpl.add(geom.coord().get(1));

      ILineString lsTemp = new GM_LineString(dpl);

      OnePlaneRoof oPR = new OnePlaneRoof(geom, 40, 50, lsTemp);

      df = new DefaultFeature(oPR.generateBuilding(30));
      df.setRepresentation(new ObjectCartoon(df, Color.GRAY));

      featC.add(df);

      geom = (IPolygon) geom.translate(45, 0, 0);

      FourPlanesRoof Fpr = new FourPlanesRoof(geom, 40, 50);

      df = new DefaultFeature(Fpr.generateBuilding(30));
      df.setRepresentation(new ObjectCartoon(df, Color.BLUE));

      featC.add(df);

      geom = (IPolygon) geom.translate(45, 0, 0);

      List<ILineString> lPignons = new ArrayList<ILineString>();
      IDirectPosition dp1 = null, dp2 = null, dp3 = null, dp4 = null;

      if (fB.equals(FormeBatiment.Carre) || fB.equals(FormeBatiment.Rectangle)) {

        dp1 = geom.coord().get(0);
        dp2 = geom.coord().get(1);
        dp3 = geom.coord().get(2);
        dp4 = geom.coord().get(3);
      } else if (fB.equals(FormeBatiment.Barre)) {

        dp1 = geom.coord().get(0);
        dp2 = geom.coord().get(1);
        dp3 = geom.coord().get(4);
        dp4 = geom.coord().get(5);

      } else if (fB.equals(FormeBatiment.Cercle)) {

        dp1 = geom.coord().get(0);
        dp2 = geom.coord().get(1);
        dp3 = geom.coord().get(geom.coord().size() / 2);
        dp4 = geom.coord().get(geom.coord().size() / 2 + 1);

      } else if (fB.equals(FormeBatiment.Escalier)) {
        dp1 = geom.coord().get(3);
        dp2 = geom.coord().get(4);
        dp3 = geom.coord().get(7);
        dp4 = geom.coord().get(8);

      } else if (fB.equals(FormeBatiment.FormeU)) {

        dp1 = geom.coord().get(3);
        dp2 = geom.coord().get(4);
        dp3 = geom.coord().get(5);
        dp4 = geom.coord().get(6);

      } else if (fB.equals(FormeBatiment.FormeT)) {

        dp1 = geom.coord().get(0);
        dp2 = geom.coord().get(1);

        if (Math.random() > 0.5) {
          IDirectPositionList dplTemp = new DirectPositionList();
          dplTemp.add(geom.coord().get(3));
          dplTemp.add(geom.coord().get(4));

          lPignons.add(new GM_LineString(dplTemp));

        }

        dp3 = geom.coord().get(3);
        dp4 = geom.coord().get(4);

      } else if (fB.equals(FormeBatiment.FormeL)) {

        dp1 = geom.coord().get(2);
        dp2 = geom.coord().get(3);
        dp3 = geom.coord().get(4);
        dp4 = geom.coord().get(5);

      }

      if (dp1 == null) {
        System.out.println("BIG ERROR DATA");
      }

      IDirectPositionList dpl2 = new DirectPositionList();
      dpl2.add(dp1);
      dpl2.add(dp2);

      lPignons.add(new GM_LineString(dpl2));

      IDirectPositionList dpl3 = new DirectPositionList();
      dpl3.add(dp3);
      dpl3.add(dp4);

      lPignons.add(new GM_LineString(dpl3));

      PignonRoof pF = new PignonRoof(geom, 40, 50, lPignons);

      df = new DefaultFeature(pF.generateBuilding(30));
      df.setRepresentation(new ObjectCartoon(df, Color.red));

      featC.add(df);

    }

    MainWindow fen = new MainWindow();

    Map3D carte = fen.getInterfaceMap3D().getCurrent3DMap();

    carte.addLayer(new VectorLayer(featC, "Tt"));

    new IOToolBar(fen);

    // ShapefileWriter.write(featC,
    // "E:/mbrasebin/Donnees/Strasbourg/TestGeneration/test.shp");

  }

  public static IPolygon construct(FormeBatiment formeBatiment,
      IDirectPosition centre, double aire, double elongation, double epaisseur,
      double dimensionMax) {

    IPolygon poly = (IPolygon) ConstructionBatiment.construire(formeBatiment,
        centre, aire, elongation, epaisseur, dimensionMax);

    return poly;
  }

}
