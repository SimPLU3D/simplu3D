package fr.ign.cogit.simplu3d.gui.button;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.conversion.Extrusion3DObject;
import fr.ign.cogit.geoxygene.sig3d.gui.MainWindow;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.sig3d.analysis.ProspectCalculation;
import fr.ign.cogit.sig3d.calculation.OrientedBoundingBox;
import fr.ign.cogit.simplu3d.exec.GTRU3D;
import fr.ign.cogit.simplu3d.gui.actionPanel.ButtonActionPanel;
import fr.ign.cogit.simplu3d.implantation.BasicIterator;
import fr.ign.cogit.simplu3d.implantation.method.impl.RandomWalk;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.scenario.AbstractDefaultScenario;
import fr.ign.cogit.simplu3d.scenario.implCube.COSBasicRectangleScenario;
import fr.ign.cogit.simplu3d.scenario.implCube.COSOrientedScenario;
import fr.ign.cogit.simplu3d.scenario.implCubeRoof.CESBasicRectangleRoofScenario;
import fr.ign.cogit.simplu3d.scenario.implCubeRoof.COSBasicRectangleRoofScenario;

public class GTRUToolBar extends JMenu implements ActionListener {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private MainWindow mW;

  private JMenuItem mITemActionBatiment, butProposeBuilding,
      butGenerateProspect, butGenerateHeight;

  private JMenu subMenuGenerateConstraint;

  private static int COUNT = 0;

  public GTRUToolBar(MainWindow mW) {

    super("GTRU3D");
    this.mW = mW;

    mITemActionBatiment = new JMenuItem("Action Bat");
    mITemActionBatiment.addActionListener(this);
    this.add(mITemActionBatiment);

    this.butProposeBuilding = new JMenuItem("Propose building");
    this.butProposeBuilding.addActionListener(this);
    this.add(this.butProposeBuilding);

    subMenuGenerateConstraint = new JMenu("Generate constraint");
    this.add(this.subMenuGenerateConstraint);

    this.butGenerateProspect = new JMenuItem("Generate prospect");
    subMenuGenerateConstraint.add(this.butGenerateProspect);
    this.butGenerateProspect.addActionListener(this);

    this.butGenerateHeight = new JMenuItem("Generate height");
    subMenuGenerateConstraint.add(this.butGenerateHeight);
    this.butGenerateHeight.addActionListener(this);

    this.mW.getMainMenuBar().add(this);

  }

  @Override
  public void actionPerformed(ActionEvent e) {

    Object source = e.getSource();

    if (source == mITemActionBatiment) {

      mW.getActionPanel().setActionComponent(
          new ButtonActionPanel(mW.getInterfaceMap3D()));

    }

    IFeatureCollection<IFeature> sel = this.mW.getInterfaceMap3D()
        .getSelection();

    if (sel.isEmpty()) {

      JOptionPane.showMessageDialog(this,
          "Module de règles d'urbanisme", "Aucune parcelle sélectionnée", //$NON-NLS-1$//$NON-NLS-2$ 
          JOptionPane.ERROR_MESSAGE);
      return;

    }

    IFeatureCollection<BasicPropertyUnit> bPUColl = new FT_FeatureCollection<BasicPropertyUnit>();

    for (IFeature feat : sel) {
      if (feat instanceof CadastralParcel) {

        bPUColl.addUnique(((CadastralParcel) feat).getbPU());

      }

    }

    if (source == butProposeBuilding) {

      System.out.println("Nombre de parcelles : " + bPUColl.size());

      IFeatureCollection<IFeature> new_Buildings = new FT_FeatureCollection<IFeature>();

      for (BasicPropertyUnit bpU : bPUColl) {

        OrientedBoundingBox oBB = new OrientedBoundingBox(bpU.generateGeom());

        System.out.println("Longueur : " + oBB.getLength());
        System.out.println("Largeur : " + oBB.getWidth());

        // On affiche les choix disponibles et on les récupère
        String[] options = { "Optimisation COS - Rectangle",
            "Optimisation COS - Rectangle orienté",
            "Optimisation COS - rectangle avec toit",
            "Optimisation CES - rectangle avec toit" };

        // Propose de choisir entre les différentes applications
        Object obj = JOptionPane.showInputDialog(null,
            "Quelle stratégie de peuplement ?", "Choix de la stratégie",
            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (obj == null) {

          return;
        }

        int nbOptions = options.length;
        int i;

        for (i = 0; i < nbOptions; i++) {
          if (options[i].equals(obj.toString())) {
            break;
          }

        }

        AbstractDefaultScenario bRS = null;

        switch (i) {
          case 0:
            bRS = new COSBasicRectangleScenario(bpU, 8, 30, 8, 30, 0, 15);
            break;
          case 1:
            bRS = new COSOrientedScenario(bpU, 8, 100, 8, 100, 0, 28);
            break;
          case 2:
            bRS = new COSBasicRectangleRoofScenario(bpU, 4, 13, 4, 13, 0, 12,
                0, 10);
            break;
          case 3:
            bRS = new CESBasicRectangleRoofScenario(bpU, 4, 13, 4, 13, 0, 12,
                0, 10);
            break;
        }


        
        if (bRS == null) {
          return;
        }

        // COSBasicRectangleScenario bRS = new COSBasicRectangleScenario(bpU, 8,
        // 30, 8, 30, 0, 15);

        // COSBasicRectangleRoofScenario bRS = new
        // COSBasicRectangleRoofScenario( bpU, 4, 15, 4, 15, 0, 12, 0, 3);

        // COSBasicLScenario bRS = new COSBasicLScenario(bpU, 8, 100, 8, 100, 0,
        // 15,
        // 2, 30, 2, 30);

        RandomWalk r = new RandomWalk(bRS);

        BasicIterator bI = new BasicIterator(r, GTRU3D.ITERATION);
        new_Buildings.add(bI.getFinalBuilding());
      }

      VectorLayer vL = new VectorLayer(new_Buildings, "Test"  + (++COUNT), Color.red);
      this.mW.getInterfaceMap3D().getCurrent3DMap().addLayer(vL);

      if (GTRU3D.DEBUG) {
        VectorLayer vL2 = new VectorLayer(GTRU3D.DEBUG_FEAT, "Debug"
            + (++COUNT), Color.green);
        this.mW.getInterfaceMap3D().getCurrent3DMap().addLayer(vL2);

        GTRU3D.DEBUG_FEAT.clear();
      }

    }

    if (source == butGenerateHeight) {

      String s = (String) JOptionPane.showInputDialog(this,
          "Quelle hauteur ? ", "Extrusion", JOptionPane.QUESTION_MESSAGE, null,
          null, // c'est ouvert !!!
          10); // valeur initiale

      double d = Double.parseDouble(s);

      if (!Double.isNaN(d)) {

        IFeatureCollection<IFeature> featColl = new FT_FeatureCollection<>();

        for (BasicPropertyUnit bPU : bPUColl) {

          IGeometry geom = Extrusion3DObject.conversionFromGeom(
              bPU.generateGeom(), d);
          featColl.add(new DefaultFeature(geom));

        }

        VectorLayer vL2 = new VectorLayer(featColl, "Result : " + (++COUNT),
            Color.green);
        this.mW.getInterfaceMap3D().getCurrent3DMap().addLayer(vL2);

      }

    }

    if (source == butGenerateProspect) {

      String s = (String) JOptionPane.showInputDialog(this,
          "Quelle hauteur initiale ? ", "Prospect",
          JOptionPane.QUESTION_MESSAGE, null, null, // c'est ouvert !!!
          10); // valeur initiale

      double hIni = Double.parseDouble(s);

      s = (String) JOptionPane.showInputDialog(this, "Quelle pente ? ",
          "Prospect", JOptionPane.QUESTION_MESSAGE, null, null, // c'est ouvert
                                                                // !!!
          10); // valeur initiale

      double slope = Double.parseDouble(s);

      if (!Double.isNaN(hIni) && !Double.isInfinite(slope)) {

        IFeatureCollection<IFeature> featColl = new FT_FeatureCollection<>();

        for (IFeature feat : sel) {

          if (feat instanceof SpecificCadastralBoundary) {

            for (CadastralParcel cP : Environnement.getInstance()
                .getParcelles()) {

              if (cP.getSpecificCadastralBoundary().contains(feat)) {
                IGeometry geom = ProspectCalculation.calculate(cP.getGeom(),
                    feat.getGeom(), slope, hIni);
                featColl.add(new DefaultFeature(geom));
              }

            }

          }

        }

        VectorLayer vL2 = new VectorLayer(featColl, "Result : " + (++COUNT),
            Color.green);
        this.mW.getInterfaceMap3D().getCurrent3DMap().addLayer(vL2);

      }

    }

  }
}
