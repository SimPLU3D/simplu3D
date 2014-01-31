package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.ihm;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class PanneauOnglets extends JPanel {

  private JTabbedPane tPane;

  private static ArrayList<OngletZone> lOnglets;

  /**
	 * 
	 */
  private static final long serialVersionUID = 5348795190442549234L;

  private MainWindow fenetrePrincipale;

  protected static int compteurModif = 0;

  public PanneauOnglets(MainWindow fen) {
    super(new GridLayout(1, 1));

    this.tPane = new JTabbedPane();
    this.fenetrePrincipale = fen;
    PanneauOnglets.lOnglets = new ArrayList<OngletZone>();

    this.add(this.tPane);

    // The following line enables to use scrolling tabs.
    this.tPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

  }

  public void ajoutOnglet(OngletZone onglet) {

    if (PanneauOnglets.compteurModif == 0) {

      this.fenetrePrincipale.getMenu().getAjoutRegle().setEnabled(true);

    }
    PanneauOnglets.compteurModif++;
    PanneauOnglets.lOnglets.add(onglet);

    this.tPane.addTab(onglet.getNom(), onglet);

  }

  public void retireOnglet(int index) {

    PanneauOnglets.lOnglets.remove(index);
    this.tPane.remove(index);

  }

  public void retireOnglet(OngletZone onglet) {
    int index = PanneauOnglets.lOnglets.indexOf(onglet);
    if (index != -1) {
      this.retireOnglet(index);

    }
  }

  public ArrayList<OngletZone> getlOnglets() {
    if (PanneauOnglets.lOnglets == null) {
      PanneauOnglets.lOnglets = new ArrayList<OngletZone>();

    }
    return PanneauOnglets.lOnglets;
  }

  public OngletZone getOngletVisible() {

    int nbElem = PanneauOnglets.lOnglets.size();

    for (int i = 0; i < nbElem; i++) {
      OngletZone ong = PanneauOnglets.lOnglets.get(i);
      if (ong.isVisible()) {
        return ong;

      }

    }

    return null;
  }

}
