package fr.ign.cogit.simplu3d.gui.button;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.gui.MainWindow;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.sig3d.calculation.OrientedBoundingBox;
import fr.ign.cogit.simplu3d.exec.GTRU3D;
import fr.ign.cogit.simplu3d.gui.actionPanel.ButtonActionPanel;
import fr.ign.cogit.simplu3d.implantation.BasicIterator;
import fr.ign.cogit.simplu3d.implantation.method.impl.RandomWalk;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.scenario.impl.CESBasicRectangleScenario;

public class GTRUToolBar extends JMenu implements ActionListener {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private MainWindow mW;

  private JMenuItem mITemActionBatiment, butProposeBuilding;

  public GTRUToolBar(MainWindow mW) {

    super("GTRU3D");
    this.mW = mW;

    mITemActionBatiment = new JMenuItem("Action Bat");
    mITemActionBatiment.addActionListener(this);
    this.add(mITemActionBatiment);

    this.butProposeBuilding = new JMenuItem("Proposer bâtiment");
    this.butProposeBuilding.addActionListener(this);
    this.add(this.butProposeBuilding);

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

        CESBasicRectangleScenario bRS = new CESBasicRectangleScenario(bpU, 0,
            oBB.getWidth(), 0, oBB.getLength(), 0, 20);

        RandomWalk r = new RandomWalk(bRS);

        BasicIterator bI = new BasicIterator(r, 5000);

        new_Buildings.add(bI.getFinalBuilding());

      }

      VectorLayer vL = new VectorLayer(new_Buildings, "Test", Color.red);
      this.mW.getInterfaceMap3D().getCurrent3DMap().addLayer(vL);
      
      
      if(GTRU3D.DEBUG){
        VectorLayer vL2 = new VectorLayer(GTRU3D.DEBUG_FEAT, "Debug", Color.green);
        this.mW.getInterfaceMap3D().getCurrent3DMap().addLayer(vL2);
      }

    }

  }
}
