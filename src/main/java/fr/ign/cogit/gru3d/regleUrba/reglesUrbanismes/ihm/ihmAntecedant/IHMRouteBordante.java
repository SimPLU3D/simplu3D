package fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.ihm.ihmAntecedant;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.MinMax;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.Regle;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.RouteBordante;

public class IHMRouteBordante extends JDialog implements WindowListener,
    ActionListener {

  /**
	 * 
	 */
  private static final long serialVersionUID = 7042083567122975570L;

  private List<String> lNomsRoute = new ArrayList<String>();
  private List<MinMax> lMinMax = new ArrayList<MinMax>();
  private List<JCheckBox> lCBTypesRoutes;

  private RouteBordante routeBordante;

  private Regle regle;
  private boolean modif = false;

  // Saisie des routes
  private JTextField JTFSaisieRoute;
  private JButton valideSaisieRoute;
  private JButton supprimeSaisieRoute;
  private JList jLRoutesSaisies;

  private JTextField JTFSaisieValMin;
  private JTextField JTFSaisieValMax;
  private JButton valideSaisieVal;
  private JButton supprimeSaisieVal;
  private JList jLInterVal;

  private JButton ok;
  private JButton annul;

  /**
   * Constructeur lors du cas de l'ajout de règle, l'antécédent sera alors
   * ajoutée à une règle
   * 
   * @param reg
   */

  public IHMRouteBordante(Regle reg) {
    super();
    this.regle = reg;
    this.modif = false;

    // Elle est rendue modale
    this.setFocusable(true);
    this.setModal(true);

    // Nous somes en mode ajout
    this.modif = false;

    // Titre
    this.setTitle(" Conditions concernant les routes bordant une parcelle  "); //$NON-NLS-1$
    this.setLayout(null);

    this.setBounds(0, 0, 450, 600);

    JLabel label = new JLabel(
        "Noms des routes sur lesquelles on appliquera les règles");
    label.setBounds(10, 5, 500, 30);
    this.add(label);

    // Il s'agit du champs permettant de choisir la distance
    this.JTFSaisieRoute = new JTextField("");
    this.JTFSaisieRoute.setBounds(10, 40, 380, 20);
    this.JTFSaisieRoute.setVisible(true);
    this.JTFSaisieRoute.addActionListener(this);
    this.add(this.JTFSaisieRoute);

    this.valideSaisieRoute = new JButton("...");
    this.valideSaisieRoute.setBounds(390, 40, 20, 20);
    this.valideSaisieRoute.setVisible(true);
    this.valideSaisieRoute.addActionListener(this);
    this.add(this.valideSaisieRoute);

    this.jLRoutesSaisies = new JList();

    // on ajoute dans le panneau dans celui muni d'ascenceurs
    JScrollPane ascenseurs = new JScrollPane(this.jLRoutesSaisies);
    ascenseurs.setBounds(10, 65, 400, 100);
    ascenseurs
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.add(ascenseurs);

    this.supprimeSaisieRoute = new JButton("x");
    this.supprimeSaisieRoute.setBounds(410, 100, 20, 20);
    this.supprimeSaisieRoute.setVisible(true);
    this.supprimeSaisieRoute.addActionListener(this);
    this.add(this.supprimeSaisieRoute);

    JLabel label2 = new JLabel(
        "Largeurs des routes sur lesquelles on appliquera les règles");
    label2.setBounds(10, 170, 500, 30);
    this.add(label2);

    JLabel label3 = new JLabel("Valeur minimale (m)");
    label3.setBounds(10, 200, 130, 30);
    this.add(label3);

    this.JTFSaisieValMin = new JTextField("0");
    this.JTFSaisieValMin.setBounds(140, 205, 60, 20);
    this.JTFSaisieValMin.setVisible(true);
    this.JTFSaisieValMin.addActionListener(this);
    this.add(this.JTFSaisieValMin);

    JLabel label4 = new JLabel("Valeur maximale (m)");
    label4.setBounds(200, 200, 130, 30);
    this.add(label4);

    this.JTFSaisieValMax = new JTextField("0");
    this.JTFSaisieValMax.setBounds(330, 205, 60, 20);
    this.JTFSaisieValMax.setVisible(true);
    this.JTFSaisieValMax.addActionListener(this);
    this.add(this.JTFSaisieValMax);

    this.valideSaisieVal = new JButton("...");
    this.valideSaisieVal.setBounds(390, 205, 20, 20);
    this.valideSaisieVal.setVisible(true);
    this.valideSaisieVal.addActionListener(this);
    this.add(this.valideSaisieVal);

    this.jLInterVal = new JList();

    // on ajoute dans le panneau dans celui muni d'ascenceurs
    JScrollPane ascenseurs2 = new JScrollPane(this.jLInterVal);
    ascenseurs2.setBounds(10, 230, 400, 100);
    ascenseurs2
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.add(ascenseurs2);

    this.supprimeSaisieVal = new JButton("x");
    this.supprimeSaisieVal.setBounds(410, 270, 20, 20);
    this.supprimeSaisieVal.setVisible(true);
    this.supprimeSaisieVal.addActionListener(this);
    this.add(this.supprimeSaisieVal);

    JLabel label5 = new JLabel(
        "Types de routes auxquels on appliquera les règles");
    label5.setBounds(10, 345, 130, 30);
    this.add(label5);

    // On récupére les textures codées en dur dans l'appli
    String[] lNomsTypesRoutes = RouteBordante.getLNomsTypes();
    int nbElem = lNomsTypesRoutes.length;

    // On créer un panel que l'on place ensuite dans un Panel muni d'un
    // ascenseur
    JPanel panTemp = new JPanel();

    // Cela permet d'indique que l'on souhaite placer verticalement les
    // différents élements
    BoxLayout box = new BoxLayout(panTemp, BoxLayout.Y_AXIS);

    panTemp.setLayout(box);
    this.lCBTypesRoutes = new ArrayList<JCheckBox>(nbElem);

    // On place les différents boutons à cocher.
    for (int i = 0; i < nbElem; i++) {
      JCheckBox jChek = new JCheckBox(lNomsTypesRoutes[i]);
      jChek.setHorizontalAlignment(SwingConstants.LEFT);
      panTemp.add(jChek);
      this.lCBTypesRoutes.add(jChek);
    }

    // on ajoute dans le panneau dans celui muni d'ascenceurs
    JScrollPane ascenseurs3 = new JScrollPane(panTemp);

    ascenseurs3.setBounds(10, 390, 400, 100);
    ascenseurs3
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.add(ascenseurs3);

    // Boutons de validations
    this.ok = new JButton("Ok");
    this.ok.setBounds(100, 505, 100, 20);
    this.ok.addActionListener(this);
    this.add(this.ok);

    this.annul = new JButton("Annuler");
    this.annul.setBounds(200, 505, 100, 20);
    this.annul.addActionListener(this);
    this.add(this.annul);

    this.setVisible(true);

  }

  /**
   * Constructeur lors du cas de l'ajout de règle, l'antécédent sera alors
   * ajoutée à règle
   * 
   * @param reg
   */

  public IHMRouteBordante(RouteBordante rBordante) {
    super();

    this.routeBordante = rBordante;

    // Nous sommes en mode modification
    this.modif = true;

    // Elle est rendue modale
    this.setFocusable(true);
    this.setModal(true);

    // Titre
    this.setTitle(" Conditions concernant les routes bordant une parcelle  "); //$NON-NLS-1$

    this.setLayout(null);

    this.setBounds(0, 0, 450, 600);

    JLabel label = new JLabel(
        "Noms des routes sur lesquelles on appliquera les règles");
    label.setBounds(10, 5, 500, 30);
    this.add(label);

    // Il s'agit du champs permettant de choisir la distance
    this.JTFSaisieRoute = new JTextField("");
    this.JTFSaisieRoute.setBounds(10, 40, 380, 20);
    this.JTFSaisieRoute.setVisible(true);
    this.JTFSaisieRoute.addActionListener(this);
    this.add(this.JTFSaisieRoute);

    this.valideSaisieRoute = new JButton("...");
    this.valideSaisieRoute.setBounds(390, 40, 20, 20);
    this.valideSaisieRoute.setVisible(true);
    this.valideSaisieRoute.addActionListener(this);
    this.add(this.valideSaisieRoute);

    this.jLRoutesSaisies = new JList();

    this.lNomsRoute = this.routeBordante.getNoms();
    this.jLRoutesSaisies.setListData(this.lNomsRoute.toArray());

    // on ajoute dans le panneau dans celui muni d'ascenceurs
    JScrollPane ascenseurs = new JScrollPane(this.jLRoutesSaisies);
    ascenseurs.setBounds(10, 65, 400, 100);
    ascenseurs
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.add(ascenseurs);

    this.supprimeSaisieRoute = new JButton("x");
    this.supprimeSaisieRoute.setBounds(410, 100, 20, 20);
    this.supprimeSaisieRoute.setVisible(true);
    this.supprimeSaisieRoute.addActionListener(this);
    this.add(this.supprimeSaisieRoute);

    JLabel label2 = new JLabel(
        "Largeurs des routes sur lesquelles on appliquera les règles");
    label2.setBounds(10, 170, 500, 30);
    this.add(label2);

    JLabel label3 = new JLabel("Valeur minimale (m)");
    label3.setBounds(10, 200, 130, 30);
    this.add(label3);

    this.JTFSaisieValMin = new JTextField("0");
    this.JTFSaisieValMin.setBounds(140, 205, 60, 20);
    this.JTFSaisieValMin.setVisible(true);
    this.JTFSaisieValMin.addActionListener(this);
    this.add(this.JTFSaisieValMin);

    JLabel label4 = new JLabel("Valeur maximale (m)");
    label4.setBounds(200, 200, 130, 30);
    this.add(label4);

    this.JTFSaisieValMax = new JTextField("0");
    this.JTFSaisieValMax.setBounds(330, 205, 60, 20);
    this.JTFSaisieValMax.setVisible(true);
    this.JTFSaisieValMax.addActionListener(this);
    this.add(this.JTFSaisieValMax);

    this.valideSaisieVal = new JButton("...");
    this.valideSaisieVal.setBounds(390, 205, 20, 20);
    this.valideSaisieVal.setVisible(true);
    this.valideSaisieVal.addActionListener(this);
    this.add(this.valideSaisieVal);

    this.jLInterVal = new JList();

    this.lMinMax = this.routeBordante.getLLargeursRoutes();
    this.jLInterVal.setListData(this.lMinMax.toArray());

    // on ajoute dans le panneau dans celui muni d'ascenceurs
    JScrollPane ascenseurs2 = new JScrollPane(this.jLInterVal);
    ascenseurs2.setBounds(10, 230, 400, 100);
    ascenseurs2
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.add(ascenseurs2);

    this.supprimeSaisieVal = new JButton("x");
    this.supprimeSaisieVal.setBounds(410, 270, 20, 20);
    this.supprimeSaisieVal.setVisible(true);
    this.supprimeSaisieVal.addActionListener(this);
    this.add(this.supprimeSaisieVal);

    JLabel label5 = new JLabel(
        "Types de routes auxquels on appliquera les règles");
    label5.setBounds(10, 345, 500, 30);
    this.add(label5);

    // On récupére les textures codées en dur dans l'appli
    String[] lNomsTypesRoutes = RouteBordante.getLNomsTypes();
    int nbElem = lNomsTypesRoutes.length;

    // On créer un panel que l'on place ensuite dans un Panel muni d'un
    // ascenseur
    JPanel panTemp = new JPanel();

    // Cela permet d'indique que l'on souhaite placer verticalement les
    // différents élements
    BoxLayout box = new BoxLayout(panTemp, BoxLayout.Y_AXIS);

    panTemp.setLayout(box);
    this.lCBTypesRoutes = new ArrayList<JCheckBox>(nbElem);

    List<String> lTypesSelectionnes = rBordante.getTypes();

    // On place les différents boutons à cocher.
    for (int i = 0; i < nbElem; i++) {
      JCheckBox jChek = new JCheckBox(lNomsTypesRoutes[i]);

      if (IHMRouteBordante.isInList(lNomsTypesRoutes[i],
          lTypesSelectionnes.toArray())) {

        jChek.setSelected(true);
      }

      jChek.setHorizontalAlignment(SwingConstants.LEFT);
      panTemp.add(jChek);
      this.lCBTypesRoutes.add(jChek);
    }

    // on ajoute dans le panneau dans celui muni d'ascenceurs
    JScrollPane ascenseurs3 = new JScrollPane(panTemp);

    ascenseurs3.setBounds(10, 390, 400, 100);
    ascenseurs3
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.add(ascenseurs3);

    // Boutons de validations
    this.ok = new JButton("Ok");
    this.ok.setBounds(100, 505, 100, 20);
    this.ok.addActionListener(this);
    this.add(this.ok);

    this.annul = new JButton("Annuler");
    this.annul.setBounds(200, 505, 100, 20);
    this.annul.addActionListener(this);
    this.add(this.annul);

    this.setVisible(true);

  }

  public static void main(String[] args) {

    ArrayList<String> lNoms = new ArrayList<String>();
    lNoms.add("Avenue Charles de Gaulles");
    lNoms.add("Rue de la résistance");
    lNoms.add("Chemin vert");

    ArrayList<MinMax> lMinMax = new ArrayList<MinMax>();
    lMinMax.add(new MinMax(0, 1));
    lMinMax.add(new MinMax(3, 5));
    lMinMax.add(new MinMax(8, 10));

    ArrayList<String> lTypes = new ArrayList<String>();
    lTypes.add("Autoroute");
    lTypes.add("Route à 2 chaussées");
    lTypes.add("Escalier");

    new IHMRouteBordante(new RouteBordante(lTypes, lNoms, lMinMax));
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
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub
    Object source = e.getSource();

    if (source.equals(this.valideSaisieRoute)) {
      String text = this.JTFSaisieRoute.getText();

      if (text == null) {
        return;
      }

      if (text == "" || text == " " || text.length() == 0) {
        return;
      }

      // On évite les doublons
      if (IHMRouteBordante.isInList(text, this.lNomsRoute.toArray())) {

        JOptionPane.showMessageDialog(this,
            "Cette valeur est déjà définie dans le tableau", //$NON-NLS-1$
            "Erreur de validation", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
      } else {

        this.lNomsRoute.add(text);
        this.jLRoutesSaisies.setListData(this.lNomsRoute.toArray());
      }

      return;
    }

    if (source.equals(this.supprimeSaisieRoute)) {
      int[] selection = this.jLRoutesSaisies.getSelectedIndices();

      int nbElem = selection.length;

      for (int i = nbElem - 1; i > -1; i--) {

        this.lNomsRoute.remove(selection[i]);

      }

      this.jLRoutesSaisies.setListData(this.lNomsRoute.toArray());

      return;

    }

    if (source.equals(this.valideSaisieVal)) {

      // On récupére les différents objets et on génére la contrainte
      double valMin;

      // On gére les mauvaises saisies
      try {
        valMin = Double.parseDouble(this.JTFSaisieValMin.getText());

      } catch (NumberFormatException nfe) {
        JOptionPane.showMessageDialog(this,
            "La valeur n'est pas un nombre décimal", //$NON-NLS-1$
            "Erreur de validation", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
        return;

      }

      if (valMin < 0) {

        JOptionPane.showMessageDialog(this, "La largeur ne peut être négative", //$NON-NLS-1$
            "Erreur de validation", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
        return;
      }

      // On récupére les différents objets et on génére la contrainte
      double valMax;

      // On gére les mauvaises saisies
      try {
        valMax = Double.parseDouble(this.JTFSaisieValMax.getText());

      } catch (NumberFormatException nfe) {
        JOptionPane.showMessageDialog(this,
            "La valeur n'est pas un nombre décimal", //$NON-NLS-1$
            "Erreur de validation", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
        return;

      }

      if (valMax < valMin) {

        JOptionPane.showMessageDialog(this,
            "La valeur maximale ne peut être inférieure à la minimale", //$NON-NLS-1$
            "Erreur de validation", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
        return;
      }

      MinMax minMax = new MinMax(valMin, valMax);

      // On évite les doublons
      if (IHMRouteBordante.isInList(minMax.toString(), this.lMinMax.toArray())) {

        JOptionPane.showMessageDialog(this,
            "Cette valeur est déjà définie dans le tableau", //$NON-NLS-1$
            "Erreur de validation", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
      } else {

        this.lMinMax.add(minMax);
        this.jLInterVal.setListData(this.lMinMax.toArray());
      }

      return;
    }

    if (source.equals(this.supprimeSaisieVal)) {
      int[] selection = this.jLInterVal.getSelectedIndices();

      int nbElem = selection.length;

      for (int i = nbElem - 1; i > -1; i--) {

        this.lMinMax.remove(selection[i]);

      }

      this.jLInterVal.setListData(this.lMinMax.toArray());

      return;

    }

    if (source.equals(this.ok)) {

      ArrayList<String> lTypesRoutes = new ArrayList<String>();

      int nbElem = this.lCBTypesRoutes.size();

      for (int i = 0; i < nbElem; i++) {

        JCheckBox jCB = this.lCBTypesRoutes.get(i);
        if (jCB.isSelected()) {
          lTypesRoutes.add(jCB.getText());

        }

      }

      if (this.modif) {
        this.routeBordante.setlLargeursRoutes(this.lMinMax);
        this.routeBordante.setNoms(this.lNomsRoute);
        this.routeBordante.setTypes(lTypesRoutes);

        System.out.println(this.routeBordante.toString());
      } else {
        RouteBordante rb = new RouteBordante(lTypesRoutes, this.lNomsRoute,
            this.lMinMax);
        this.regle.getAntecedent().add(rb);
      }

      this.dispose();
      return;

    }

    if (source.equals(this.annul)) {

      this.dispose();
      return;

    }

  }

  private static boolean isInList(String s, Object[] al) {
    boolean present = false;

    int nbElem = al.length;

    for (int i = 0; i < nbElem; i++) {

      if (s.equalsIgnoreCase(al[i].toString())) {
        return true;
      }

    }

    return present;
  }
}
