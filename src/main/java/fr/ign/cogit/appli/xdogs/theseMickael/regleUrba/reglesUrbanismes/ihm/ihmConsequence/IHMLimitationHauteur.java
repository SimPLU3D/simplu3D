package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.ihm.ihmConsequence;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.ihm.FenetreEditionRegle;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.Consequence;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.ConsequenceHauteur;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.ContrainteHauteur;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.DifferenceHauteur;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.Regle;

/**
 * 
 * Fenetre permettant l'ajout de shape, en fonction de paramêtres de passage é
 * la 3D choisis
 * 
 * The windows of this class enable shapefile loading according to 3D-parameters
 * 
 * @author MBrasebin TODO rajouter MNT une fois la solution trouvée
 */
public class IHMLimitationHauteur extends JDialog implements WindowListener,
    ActionListener {

  // Il s'agit des radios boutons permettant de gérer le type de hauteur
  // souhaitée
  JRadioButton jRadioHMax;
  JRadioButton jRadioDiffHauteur;

  // Il s'agit d'un Text Field permettant d'entrer la valeur correspondant é
  // la hauteur ou différence de hauteur
  JTextField jTFHauteur;

  JButton ok = new JButton();
  JButton annul = new JButton();

  // Il s'agit de la règle sur laquelle on applique la modification
  Regle regle;

  private boolean modif = false;

  private static final long serialVersionUID = 1L;

  // Il s'agit de la conséuqence de hauteur que l'on modifie
  private ConsequenceHauteur consequenceHauteur;

  /**
   * Initialisation de la fenetre
   * 
   * @param f
   * @param file
   */
  public IHMLimitationHauteur(Regle r) {
    super();
    // Elle est rendue modale
    this.setFocusable(true);
    this.setModal(true);

    this.regle = r;

    // Titre
    this.setTitle(" Limitation sur les hauteurs "); //$NON-NLS-1$
    this.setLayout(null);

    this.modif = false;

    // Formulaire du type
    JLabel labelNom = new JLabel();
    labelNom.setBounds(10, 20, 100, 20);
    labelNom.setText("Type de limitation"); //$NON-NLS-1$
    this.add(labelNom);

    this.jRadioHMax = new JRadioButton();
    this.jRadioHMax.setBounds(150, 10, 150, 20);
    this.jRadioHMax.setText("Hauteur maximale"); //$NON-NLS-1$
    this.jRadioHMax.setSelected(true);
    this.jRadioHMax.addActionListener(this);
    this.add(this.jRadioHMax);

    this.jRadioDiffHauteur = new JRadioButton();
    this.jRadioDiffHauteur.setBounds(150, 30, 150, 20);
    this.jRadioDiffHauteur.setText("Différence hauteur"); //$NON-NLS-1$
    this.jRadioDiffHauteur.setSelected(false);
    this.jRadioDiffHauteur.addActionListener(this);
    this.add(this.jRadioDiffHauteur);

    ButtonGroup group = new ButtonGroup();
    group.add(this.jRadioHMax);
    group.add(this.jRadioDiffHauteur);

    this.jTFHauteur = new JTextField("0");
    this.jTFHauteur.setBounds(300, 20, 50, 20);
    this.jTFHauteur.addActionListener(this);

    this.add(this.jTFHauteur);

    JLabel textM = new JLabel(" m");
    textM.setBounds(350, 15, 50, 30);
    this.add(textM);

    // Boutons de validations
    this.ok.setBounds(100, 50, 100, 20);
    this.ok.setText("Ok"); //$NON-NLS-1$
    this.ok.addActionListener(this);
    this.add(this.ok);

    this.annul.setBounds(200, 50, 100, 20);
    this.annul.setText("Annuler"); //$NON-NLS-1$
    this.annul.addActionListener(this);
    this.add(this.annul);

    this.setBackground(Color.white);
    this.setSize(440, 110);

    this.setVisible(true);
  }

  /**
   * 
   * @param consHauteur
   */
  public IHMLimitationHauteur(ConsequenceHauteur consHauteur) {
    super();
    // Elle est rendue modale
    this.setFocusable(true);
    this.setModal(true);

    this.consequenceHauteur = consHauteur;

    int index = 0;
    double val = 0;

    if (consHauteur instanceof ContrainteHauteur) {

      ContrainteHauteur c = (ContrainteHauteur) consHauteur;
      val = c.getHauteurMax();
      index = 0;
    } else {
      // on doit être de l'autre instanciation possible à savoir
      // DifferenceHauteur
      DifferenceHauteur c = (DifferenceHauteur) consHauteur;
      val = c.getDifferenceHauteur();
      index = 1;
    }

    // Titre
    this.setTitle(" Limitation sur les hauteurs "); //$NON-NLS-1$
    this.setLayout(null);

    this.modif = true;

    // Formulaire du type
    JLabel labelNom = new JLabel();
    labelNom.setBounds(10, 20, 100, 20);
    labelNom.setText("Type de limitation"); //$NON-NLS-1$
    this.add(labelNom);

    this.jRadioHMax = new JRadioButton();
    this.jRadioHMax.setBounds(150, 10, 150, 20);
    this.jRadioHMax.setText("Hauteur maximale"); //$NON-NLS-1$
    this.jRadioHMax.setSelected(index == 0);// On adapte la sélection en
    // fonction de l'instance
    this.jRadioHMax.addActionListener(this);
    this.add(this.jRadioHMax);

    this.jRadioDiffHauteur = new JRadioButton();
    this.jRadioDiffHauteur.setBounds(150, 30, 150, 20);
    this.jRadioDiffHauteur.setText("Différence hauteur"); //$NON-NLS-1$
    this.jRadioDiffHauteur.setSelected(index == 1);// On adapte la sélection
    // en fonction de
    // l'instance
    this.jRadioDiffHauteur.addActionListener(this);
    this.add(this.jRadioDiffHauteur);

    ButtonGroup group = new ButtonGroup();
    group.add(this.jRadioHMax);
    group.add(this.jRadioDiffHauteur);
    this.jTFHauteur = new JTextField(val + "");
    this.jTFHauteur.setBounds(300, 20, 50, 20);
    this.jTFHauteur.addActionListener(this);

    this.add(this.jTFHauteur);

    JLabel textM = new JLabel(" m");
    textM.setBounds(350, 15, 50, 30);
    this.add(textM);

    // Boutons de validations
    this.ok.setBounds(100, 50, 100, 20);
    this.ok.setText("Ok"); //$NON-NLS-1$
    this.ok.addActionListener(this);
    this.add(this.ok);

    this.annul.setBounds(200, 50, 100, 20);
    this.annul.setText("Annuler"); //$NON-NLS-1$
    this.annul.addActionListener(this);
    this.add(this.annul);

    this.setBackground(Color.white);
    this.setSize(440, 110);

    this.setVisible(true);
  }

  /**
   * Gestion des actions
   */
  @Override
  public void actionPerformed(ActionEvent actionevent) {
    Object source = actionevent.getSource();

    if (source.equals(this.ok)) {

      // On récupére les différents objets et on génére la contrainte
      double valMin;

      // On gére les mauvaises saisies
      try {
        valMin = Double.parseDouble(this.jTFHauteur.getText());

      } catch (NumberFormatException nfe) {
        JOptionPane.showMessageDialog(this,
            "La valeur minimale n'est pas un nombre décimal", //$NON-NLS-1$
            "Erreur de validation", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
        return;

      }

      ConsequenceHauteur consHauteur;

      // On crée une instance en fonction de la ligne sélecionnée
      if (this.jRadioHMax.isSelected()) {

        consHauteur = new ContrainteHauteur(valMin);
      } else {

        consHauteur = new DifferenceHauteur(valMin);
      }

      // On gére différent les cas de modification ou d'ajout
      if (this.modif) {

        List<Consequence> lCons = FenetreEditionRegle.getRegle()
            .getConsequence();
        int index = lCons.indexOf(this.consequenceHauteur);

        if (index != -1) {

          lCons.remove(index);
          lCons.add(index, consHauteur);
        }
      } else {

        this.regle.getConsequence().add(consHauteur);

      }

      this.dispose();
      return;

    }

    // bouton d'annulation
    if (source.equals(this.annul)) {
      this.dispose();
    }

  }

  @Override
  public void windowActivated(WindowEvent arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void windowClosed(WindowEvent arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void windowClosing(WindowEvent arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void windowDeactivated(WindowEvent arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void windowDeiconified(WindowEvent arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void windowIconified(WindowEvent arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void windowOpened(WindowEvent arg0) {
    // TODO Auto-generated method stub

  }

}
