package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.ihm.ihmConsequence;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.LimitationCOS;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.Regle;

public class IHMLimitationCOS extends JDialog implements WindowListener,
    ActionListener {

  // Il s'agit des éléments que l'on renvoie gréce à ce formulaire
  // Le COS Min
  // Le COS Max
  JTextField jTFCOSMin;
  JTextField jTFCOSMax;

  // Ce booléan indique si l'on est en mode édition ou création
  private boolean modif;

  // Bouton permettant de valider/annuler
  JButton ok = new JButton();
  JButton annul = new JButton();

  // La règle à laquelle sera ajoutée cette conséquence
  private Regle r;

  // En cas de mode modification, il s'agit de la conséquence que l'on modifie
  private LimitationCOS limitationCOS;

  /**
	 */
  private static final long serialVersionUID = 1L;

  /**
   * Méthode permettant de créer l'ajout d'une conséquence dans la règle règle
   */
  public IHMLimitationCOS(Regle regle) {

    super();
    this.modif = false;
    this.r = regle;

    // Elle est rendue modale
    this.setFocusable(true);
    this.setModal(true);

    // Titre
    this.setTitle(" Ajout d'une limitaiton de COS "); //$NON-NLS-1$
    this.setLayout(null);

    JLabel labelCOSMin = new JLabel();
    labelCOSMin.setBounds(10, 10, 130, 20);
    labelCOSMin.setText("Valeur minimale"); //$NON-NLS-1$
    this.add(labelCOSMin);

    // Il s'agit du champs permettant de choisir le cos min
    this.jTFCOSMin = new JTextField("0");
    this.jTFCOSMin.setBounds(160, 10, 40, 20);
    this.jTFCOSMin.setVisible(true);
    this.jTFCOSMin.addActionListener(this);
    this.add(this.jTFCOSMin);

    JLabel labelCOSMax = new JLabel();
    labelCOSMax.setBounds(10, 40, 130, 20);
    labelCOSMax.setText("Valeur maximale"); //$NON-NLS-1$
    this.add(labelCOSMax);

    // Il s'agit du champs permettant de choisir le COS max
    this.jTFCOSMax = new JTextField("0");
    this.jTFCOSMax.setBounds(160, 40, 40, 20);
    this.jTFCOSMax.setVisible(true);
    this.jTFCOSMax.addActionListener(this);
    this.add(this.jTFCOSMax);

    // Boutons de validations
    this.ok.setBounds(10, 70, 95, 20);
    this.ok.setText("Ok"); //$NON-NLS-1$
    this.ok.addActionListener(this);
    this.add(this.ok);

    this.annul.setBounds(105, 70, 95, 20);
    this.annul.setText("Annuler"); //$NON-NLS-1$
    this.annul.addActionListener(this);
    this.add(this.annul);

    this.setBackground(Color.white);
    this.setSize(250, 130);
    this.setVisible(true);
  }

  /**
   * Il s'agit du mode édition On charge les paramêtres déjé enregistrés dans le
   * recul choisit (ce recul est supposé faire partie des conséquences de la
   * règle reg
   * 
   * @param reg
   * @param recul
   */
  public IHMLimitationCOS(LimitationCOS limitation) {

    super();

    this.limitationCOS = limitation;

    this.modif = true;

    // Elle est rendue modale
    this.setFocusable(true);
    this.setModal(true);

    this.setTitle(" Ajout d'une limitaiton de COS "); //$NON-NLS-1$
    this.setLayout(null);

    JLabel labelCOSMin = new JLabel();
    labelCOSMin.setBounds(10, 10, 130, 20);
    labelCOSMin.setText("Valeur minimale"); //$NON-NLS-1$
    this.add(labelCOSMin);

    // Il s'agit du champs permettant de choisir le COS min
    this.jTFCOSMin = new JTextField(limitation.getCosMin() + "");
    this.jTFCOSMin.setBounds(160, 10, 40, 20);
    this.jTFCOSMin.setVisible(true);
    this.jTFCOSMin.addActionListener(this);
    this.add(this.jTFCOSMin);

    // Il s'agit du texte variant suivant le type de distance choisie
    JLabel labelCOSMax = new JLabel();
    labelCOSMax.setBounds(10, 40, 130, 20);
    labelCOSMax.setText("Valeur maximale"); //$NON-NLS-1$
    this.add(labelCOSMax);

    // Il s'agit du champs permettant de choisir le COS max
    this.jTFCOSMax = new JTextField(limitation.getCosMax() + "");
    this.jTFCOSMax.setBounds(160, 40, 40, 20);
    this.jTFCOSMax.setVisible(true);
    this.jTFCOSMax.addActionListener(this);
    this.add(this.jTFCOSMax);

    // Boutons de validations
    this.ok.setBounds(10, 70, 95, 20);
    this.ok.setText("Ok"); //$NON-NLS-1$
    this.ok.addActionListener(this);
    this.add(this.ok);

    this.annul.setBounds(105, 70, 95, 20);
    this.annul.setText("Annuler"); //$NON-NLS-1$
    this.annul.addActionListener(this);
    this.add(this.annul);

    this.setBackground(Color.white);
    this.setSize(250, 130);
    this.setVisible(true);
  }

  /**
   * Gestion des actions
   */
  @Override
  public void actionPerformed(ActionEvent actionevent) {
    Object source = actionevent.getSource();

    // bouton de validation
    if (source.equals(this.ok)) {

      // On récupére les différents objets et on génére la contrainte
      double valMin;

      // On gére les mauvaises saisies
      try {
        valMin = Double.parseDouble(this.jTFCOSMin.getText());

      } catch (NumberFormatException nfe) {
        JOptionPane.showMessageDialog(this,
            "La valeur minimale n'est pas un nombre décimal", //$NON-NLS-1$
            "Erreur de validation", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
        return;

      }

      double valMax;
      try {
        valMax = Double.parseDouble(this.jTFCOSMax.getText());

      } catch (NumberFormatException nfe) {
        JOptionPane.showMessageDialog(this,
            "La valeur maximale n'est pas un nombre décimal", //$NON-NLS-1$
            "Erreur de validation", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
        return;

      }

      if (valMin > valMax) {

        JOptionPane.showMessageDialog(this,
            "La valeur minimale est supérieure à la valeur maximale", //$NON-NLS-1$
            "Erreur de validation", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
        return;
      }

      if (valMin < 0 || valMax < 0) {

        JOptionPane.showMessageDialog(this, "Une des valeurs est négative", //$NON-NLS-1$
            "Erreur de validation", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
        return;
      }

      // En fonction du mode choisi, les conséquences ne sont pas les
      // mémes
      if (this.modif) {
        this.limitationCOS.setCosMax(valMax);
        this.limitationCOS.setCosMin(valMin);
      } else {
        LimitationCOS nouvelleLimit = new LimitationCOS(valMin, valMax);
        this.r.getConsequence().add(nouvelleLimit);
      }

      this.dispose();

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

  public static void main(String[] args) {
    LimitationCOS lim = new LimitationCOS(1, 2);
    new IHMLimitationCOS(lim);

  }

}
