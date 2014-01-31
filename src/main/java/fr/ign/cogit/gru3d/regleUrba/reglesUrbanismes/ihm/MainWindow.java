package fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.ihm;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class MainWindow extends JFrame implements WindowListener,
    WindowFocusListener {

  public BarreMenu getMenu() {
    return this.menu;
  }

  public PanneauOnglets getPanneauOnglets() {
    return this.panneauOnglets;
  }

  /**
	 * 
	 */
  private static final long serialVersionUID = -1542066413917807316L;

  private BarreMenu menu;
  private PanneauOnglets panneauOnglets;

  public MainWindow(final boolean isAlone) {

    super();

    // Titre
    this.setTitle(" Editeur de règles d'urbanisme "); //$NON-NLS-1$

    Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit()
        .getScreenSize();
    int hauteur = (int) tailleEcran.getHeight();

    this.setBounds(0, 0, 640, 480);

    GridBagLayout gbl = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();

    // Association du LayoutManager
    this.setLayout(gbl);

    // Contraintes sur les lignes et colonnes du GridBagLayout
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.weighty = 0;

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.ipady = 30; // pg.getHeight();

    this.menu = new BarreMenu(this);

    this.add(this.menu);

    gbl.setConstraints(this.menu, gbc);

    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.weighty = 1;

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.ipady = hauteur - 30; // pg.getHeight();

    this.panneauOnglets = new PanneauOnglets(this);

    this.add(this.panneauOnglets);

    gbl.setConstraints(this.panneauOnglets, gbc);

    this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

    // Ferme la jvm (pour éviter les "oublis")
    this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        MainWindow.this.dispose();
        
        if(isAlone){
          System.exit(NORMAL);
        }
        
 
      //  
        // Main.windowClosed();
      }
    });

  }

  public static void main(String[] args) {

    (new MainWindow(true)).setVisible(true);

  }

  @Override
  public void windowActivated(WindowEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void windowClosed(WindowEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void windowClosing(WindowEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void windowDeactivated(WindowEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void windowDeiconified(WindowEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void windowIconified(WindowEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void windowOpened(WindowEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void windowGainedFocus(WindowEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void windowLostFocus(WindowEvent e) {
    // TODO Auto-generated method stub

  }

  public void reset() {
    PanneauOnglets pan = this.getPanneauOnglets();

    int nbOnglets = pan.getlOnglets().size();

    for (int i = 0; i < nbOnglets; i++) {
      pan.retireOnglet(0);

    }
  }
}
