package fr.ign.cogit.gui.button;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import fr.ign.cogit.gui.actionPanel.ButtonActionPanel;
import fr.ign.cogit.geoxygene.sig3d.gui.MainWindow;

public class GTRUToolBar extends JMenu implements ActionListener {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private MainWindow mW;

  private JMenuItem mITemActionBatiment;

  public GTRUToolBar(MainWindow mW) {

    super("GRU3D");
    this.mW = mW;

    mITemActionBatiment = new JMenuItem("Action Bat");
    mITemActionBatiment.addActionListener(this);
    this.add(mITemActionBatiment);

    this.mW.getMainMenuBar().add(this);

  }

  @Override
  public void actionPerformed(ActionEvent e) {

    Object source = e.getSource();

    if (source == mITemActionBatiment) {

      mW.getActionPanel().setActionComponent(
          new ButtonActionPanel(mW.getInterfaceMap3D()));

    }

  }

}
