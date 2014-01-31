package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.ihm.ihmAntecedant.IHMRouteBordante;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.ihm.ihmAntecedant.IHMTypeBatiment;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.ihm.ihmConsequence.IHMBandeConstructibilite;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.ihm.ihmConsequence.IHMGestionRecul;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.ihm.ihmConsequence.IHMLimitationAngleToit;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.ihm.ihmConsequence.IHMLimitationCES;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.ihm.ihmConsequence.IHMLimitationCOS;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.ihm.ihmConsequence.IHMLimitationHauteur;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.ihm.ihmConsequence.IHMLimitationTexture;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.AngleToit;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.Antecedent;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.BandeConstructibilite;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.Consequence;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.ConsequenceTexture;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.ContrainteHauteur;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.Interdiction;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.LimitationCES;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.LimitationCOS;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.Recul;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.Regle;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.RouteBordante;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.TypeBatiment;

public class FenetreEditionRegle extends JDialog implements WindowListener,
    ActionListener, WindowFocusListener {

  // Il s'agit de la règle déclenchant l'ouverture de la fenêtre
  // c'est à dire la règle que l'on souhaite modifier/compléter
  private static Regle regle;

  private JButton ok;

  public static Regle getRegle() {
    return FenetreEditionRegle.regle;
  }

  private JButton annul;
  private JList jLRegle;

  private JButton jbRouteBordante, jbTypeBatiment;

  private JButton jbBandeConstructibilite, jbGestionRecul, jbAngleToit,
      jbLimitationCES, jbLimitationCOS, jbLimitationHauteur,
      jbLimitationTexture, jbInterdiction;

  private JButton jbSuppression;
  private OngletZone onglet;

  public FenetreEditionRegle(Regle regle, OngletZone onglet) {
    super();

    this.onglet = onglet;

    FenetreEditionRegle.regle = regle;

    this.addWindowFocusListener(this);

    // Elle est rendue modale
    this.setFocusable(true);
    this.setModal(true);

    this.setTitle(" Edition de la règle né : " + regle.getID()); //$NON-NLS-1$
    this.setLayout(null);

    this.setBounds(0, 0, 500, 600);

    JLabel label = new JLabel("Ajout d'éléments dans la règle");
    label.setBounds(10, 5, 500, 30);
    this.add(label);

    JLabel label2 = new JLabel(
        "<html>Conditions sur l'application<br>d'une contrainte pour une parcelle</html>");
    label2.setBounds(10, 25, 260, 60);
    this.add(label2);

    JLabel label3 = new JLabel(
        "<html>Contraintes à appliquer <br>sur une parcelle</html>");
    label3.setBounds(270, 25, 250, 60);
    this.add(label3);

    this.jbRouteBordante = new JButton("Routes bordantes");
    this.jbRouteBordante.setBounds(10, 90, 200, 20);
    this.jbRouteBordante.addActionListener(this);
    this.add(this.jbRouteBordante);

    this.jbTypeBatiment = new JButton("Type de batîment");
    this.jbTypeBatiment.setBounds(10, 110, 200, 20);
    this.jbTypeBatiment.addActionListener(this);
    this.add(this.jbTypeBatiment);

    this.jbBandeConstructibilite = new JButton("Bande de constructibilité");
    this.jbBandeConstructibilite.setBounds(270, 90, 200, 20);
    this.jbBandeConstructibilite.addActionListener(this);
    this.add(this.jbBandeConstructibilite);

    this.jbGestionRecul = new JButton("Recul");
    this.jbGestionRecul.setBounds(270, 110, 200, 20);
    this.jbGestionRecul.addActionListener(this);
    this.add(this.jbGestionRecul);

    this.jbAngleToit = new JButton("Angle de toit");
    this.jbAngleToit.setBounds(270, 130, 200, 20);
    this.jbAngleToit.addActionListener(this);
    this.add(this.jbAngleToit);

    this.jbLimitationCES = new JButton("Limitation du CES");
    this.jbLimitationCES.setBounds(270, 150, 200, 20);
    this.jbLimitationCES.addActionListener(this);
    this.add(this.jbLimitationCES);

    this.jbLimitationCOS = new JButton("Limitation du COS");
    this.jbLimitationCOS.setBounds(270, 170, 200, 20);
    this.jbLimitationCOS.addActionListener(this);
    this.add(this.jbLimitationCOS);

    this.jbLimitationHauteur = new JButton("Limitation de la hauteur");
    this.jbLimitationHauteur.setBounds(270, 190, 200, 20);
    this.jbLimitationHauteur.addActionListener(this);
    this.add(this.jbLimitationHauteur);

    this.jbLimitationTexture = new JButton("Limitation aspect");
    this.jbLimitationTexture.setBounds(270, 210, 200, 20);
    this.jbLimitationTexture.addActionListener(this);
    this.add(this.jbLimitationTexture);

    this.jbInterdiction = new JButton("Interdiction");
    this.jbInterdiction.setBounds(270, 230, 200, 20);
    this.jbInterdiction.addActionListener(this);
    this.add(this.jbInterdiction);

    JLabel label4 = new JLabel("Définition de la règle");
    label4.setBounds(10, 270, 150, 20);
    this.add(label4);

    this.jbSuppression = new JButton("Suppression");
    this.jbSuppression.setBounds(310, 270, 150, 20);
    this.jbSuppression.addActionListener(this);
    this.add(this.jbSuppression);

    this.jLRegle = new JList();
    this.jLRegle.setCellRenderer(new CustomListCellRenderer());

    this.jLRegle.setListData(regle.toArray());

    this.jLRegle.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent evt) {
        JList list = (JList) evt.getSource();
        if (evt.getClickCount() == 2) { // Double-click
          // Get item index

          int index = list.locationToIndex(evt.getPoint());

          if (index == -1) {
            return;
          }

          Object obj = list.getSelectedValue();

          if (obj instanceof BandeConstructibilite) {

            new IHMBandeConstructibilite((BandeConstructibilite) obj);
          }

          if (obj instanceof RouteBordante) {

            new IHMRouteBordante((RouteBordante) obj);

          }

          if (obj instanceof TypeBatiment) {

            new IHMTypeBatiment((TypeBatiment) obj);

          }

          if (obj instanceof Recul) {

            new IHMGestionRecul((Recul) obj);

          }

          if (obj instanceof AngleToit) {

            new IHMLimitationAngleToit((AngleToit) obj);

          }

          if (obj instanceof LimitationCES) {

            new IHMLimitationCES((LimitationCES) obj);

          }

          if (obj instanceof LimitationCOS) {

            new IHMLimitationCOS((LimitationCOS) obj);

          }

          if (obj instanceof ContrainteHauteur) {

            new IHMLimitationHauteur((ContrainteHauteur) obj);

          }

          if (obj instanceof ConsequenceTexture) {

            new IHMLimitationTexture((ConsequenceTexture) obj);

          }

        }
      }
    });

    // on ajoute dans le panneau dans celui muni d'ascenceurs
    JScrollPane ascenseurs2 = new JScrollPane(this.jLRegle);
    ascenseurs2.setBounds(10, 300, 470, 220);
    ascenseurs2
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.add(ascenseurs2);

    // Boutons de validations
    this.ok = new JButton("Ok");
    this.ok.setBounds(100, 525, 100, 20);
    this.ok.addActionListener(this);
    this.add(this.ok);

    this.annul = new JButton("Annuler");
    this.annul.setBounds(200, 525, 100, 20);
    this.annul.addActionListener(this);
    this.add(this.annul);

    this.setVisible(true);

  }

  /**
	 * 
	 */
  private static final long serialVersionUID = 3928921673929147459L;

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

    Object obj = e.getSource();

    if (obj.equals(this.jbBandeConstructibilite)) {

      new IHMBandeConstructibilite(FenetreEditionRegle.regle);
    }

    if (obj.equals(this.jbRouteBordante)) {

      new IHMRouteBordante(FenetreEditionRegle.regle);

    }

    if (obj.equals(this.jbTypeBatiment)) {

      new IHMTypeBatiment(FenetreEditionRegle.regle);

    }

    if (obj.equals(this.jbGestionRecul)) {

      new IHMGestionRecul(FenetreEditionRegle.regle);

    }

    if (obj.equals(this.jbAngleToit)) {

      new IHMLimitationAngleToit(FenetreEditionRegle.regle);

    }

    if (obj.equals(this.jbLimitationCES)) {

      new IHMLimitationCES(FenetreEditionRegle.regle);

    }

    if (obj.equals(this.jbLimitationCOS)) {

      new IHMLimitationCOS(FenetreEditionRegle.regle);

    }

    if (obj.equals(this.jbLimitationHauteur)) {

      new IHMLimitationHauteur(FenetreEditionRegle.regle);

    }

    if (obj.equals(this.jbLimitationTexture)) {

      new IHMLimitationTexture(FenetreEditionRegle.regle);

    }

    if (obj.equals(this.jbInterdiction)) {

      FenetreEditionRegle.regle.getConsequence().add(new Interdiction());
      this.setVisible(true);

    }

    if (obj.equals(this.ok)) {

      this.dispose();
      this.onglet.setVisible(true);
      return;
    }

    if (obj.equals(this.annul)) {

      this.dispose();
      this.onglet.setVisible(true);
      return;
    }

    if (obj.equals(this.jbSuppression)) {
      int ind = this.jLRegle.getSelectedIndex();

      if (ind == -1) {
        return;
      }

      int nbAnte = FenetreEditionRegle.getRegle().getAntecedent().size();

      // il s'agit d'un antécédent
      if (ind < nbAnte) {

        FenetreEditionRegle.getRegle().getAntecedent().remove(ind);

      } else {
        // C'est une conséquence

        FenetreEditionRegle.getRegle().getConsequence().remove(ind - nbAnte);
      }

      this.setVisible(true);
    }
  }

  public static void main(String[] args) {
    new FenetreEditionRegle(new Regle(), null);

  }

  private static class CustomListCellRenderer extends JPanel implements
      ListCellRenderer {

    /**
		 * 
		 */
    private static final long serialVersionUID = 1L;

    private static final Border NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);

    private final JTextArea contentArea;

    private CustomListCellRenderer() {

      this.contentArea = new JTextArea();
      this.contentArea.setLineWrap(true);
      this.contentArea.setWrapStyleWord(true);
      this.contentArea.setOpaque(true);
      this.contentArea.setBackground(Color.white);
      this.build();
    }

    private void build() {

      this.setLayout(new BorderLayout());
      this.add(this.contentArea, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
        int index, boolean isSelected, boolean cellHasFocus) {

      this.setBorder(cellHasFocus ? UIManager
          .getBorder("List.focusCellHighlightBorder")
          : CustomListCellRenderer.NO_FOCUS_BORDER);

      if (value instanceof Antecedent) {

        this.contentArea.setBackground(new Color(193, 255, 179));

      }

      if (value instanceof Consequence) {

        this.contentArea.setBackground(new Color(254, 210, 152));

      }

      this.contentArea.setText(value.toString());

      return this;
    }

  }

  @Override
  public void setVisible(boolean bool) {
    this.jLRegle.setListData(FenetreEditionRegle.regle.toArray());
    super.setVisible(bool);
    this.jLRegle.setVisible(true);

  }

  @Override
  public void windowGainedFocus(WindowEvent e) {
    // TODO Auto-generated method stub
    this.setVisible(true);

  }

  @Override
  public void windowLostFocus(WindowEvent e) {
    // TODO Auto-generated method stub

  }

}
