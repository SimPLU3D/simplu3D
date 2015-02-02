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
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion3DObject;
import fr.ign.cogit.geoxygene.sig3d.gui.MainWindow;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.sig3d.analysis.ProspectCalculation;
import fr.ign.cogit.simplu3d.gui.actionPanel.ButtonActionPanel;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;


/**
 * Barre d'outils permettant la génération de certains éléments réglementaires 
 * 
 * 
 * @author MBrasebin
 *
 */
public class GTRUToolBar extends JMenu implements ActionListener {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private MainWindow mW;

  private JMenuItem mITemActionBatiment, butGenerateProspect,
      butGenerateHeight, butAsBuilding;

  private JMenu subMenuGenerateConstraint;

  private static int COUNT = 0;

  public GTRUToolBar(MainWindow mW) {

    super("GTRU3D");
    this.mW = mW;

    mITemActionBatiment = new JMenuItem("Action Building");
    mITemActionBatiment.addActionListener(this);
    this.add(mITemActionBatiment);

    butAsBuilding = new JMenuItem("Convert as Building");
    butAsBuilding.addActionListener(this);
    this.add(butAsBuilding);

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

    if (source == butAsBuilding) {

      IFeatureCollection<Building> new_Buildings = new FT_FeatureCollection<Building>();

      for (IFeature feat : sel) {

        new_Buildings.add(new Building(feat.getGeom()));

      }

      VectorLayer vL2 = new VectorLayer(new_Buildings, "Result : " + (++COUNT),
          Color.green);

      this.mW.getInterfaceMap3D().getCurrent3DMap().addLayer(vL2);
    }

    // //////////Cette partie ne concerne que les unités foncières

    IFeatureCollection<BasicPropertyUnit> bPUColl = new FT_FeatureCollection<BasicPropertyUnit>();

    for (IFeature feat : sel) {
      if (feat instanceof CadastralParcel) {

        bPUColl.addUnique(((CadastralParcel) feat).getbPU());

      }

    }

    if (source == butGenerateHeight) {

      String s = (String) JOptionPane.showInputDialog(this,
          "Quelle hauteur ? ", "Extrusion", JOptionPane.QUESTION_MESSAGE, null,
          null, // c'est ouvert !!!
          10); // valeur initiale

      double d = Double.parseDouble(s);

      if (!Double.isNaN(d)) {

        IFeatureCollection<IFeature> featColl = new FT_FeatureCollection<IFeature>();

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

        IFeatureCollection<IFeature> featColl = new FT_FeatureCollection<IFeature>();

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
