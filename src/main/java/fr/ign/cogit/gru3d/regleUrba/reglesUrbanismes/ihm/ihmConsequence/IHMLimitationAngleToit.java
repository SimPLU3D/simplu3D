package fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.ihm.ihmConsequence;

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

import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.AngleToit;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.Regle;

public class IHMLimitationAngleToit extends JDialog implements WindowListener,
    ActionListener {

  // Il s'agit des éléments que l'on renvoie gréce à ce formulaire
  // Le CES Min
  // Le CES Max
  JTextField jTFAngleMin;
  JTextField jTFAngleMax;

  // Ce booléan indique si l'on est en mode édition ou création
  private boolean modif;

  // Bouton permettant de valider/annuler
  JButton ok = new JButton();
  JButton annul = new JButton();

  // La règle à laquelle sera ajoutée cette conséquence
  private Regle r;

  // En cas de mode modification, il s'agit de la conséquence que l'on modifie
  private AngleToit limitationAngle;

  /**
	 */
  private static final long serialVersionUID = 1L;

  /**
   * Méthode permettant de créer l'ajout d'une conséquence dans la règle règle
   */
  public IHMLimitationAngleToit(Regle regle) {

    super();
    this.modif = false;
    this.r = regle;

    // Elle est rendue modale
    this.setFocusable(true);
    this.setModal(true);

    // Titre
    this.setTitle(" Ajout d'une limitaiton dsur l'angle du toit "); //$NON-NLS-1$
    this.setLayout(null);

    JLabel labelAnglMin = new JLabel();
    labelAnglMin.setBounds(10, 10, 130, 20);
    labelAnglMin.setText("Valeur minimale en °"); //$NON-NLS-1$
    this.add(labelAnglMin);

    // Il s'agit du champs permettant de choisir la valeur minimale
    this.jTFAngleMin = new JTextField("10");
    this.jTFAngleMin.setBounds(160, 10, 40, 20);
    this.jTFAngleMin.setVisible(true);
    this.jTFAngleMin.addActionListener(this);
    this.add(this.jTFAngleMin);

    JLabel labelAnglMax = new JLabel();
    labelAnglMax.setBounds(10, 40, 130, 20);
    labelAnglMax.setText("Valeur maximale en °"); //$NON-NLS-1$
    this.add(labelAnglMax);

    // Il s'agit du champs permettant de choisir la valeur maximale
    this.jTFAngleMax = new JTextField("30");
    this.jTFAngleMax.setBounds(160, 40, 40, 20);
    this.jTFAngleMax.setVisible(true);
    this.jTFAngleMax.addActionListener(this);
    this.add(this.jTFAngleMax);

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
  public IHMLimitationAngleToit(AngleToit limitation) {

    super();

    this.limitationAngle = limitation;
    this.modif = true;

    // Elle est rendue modale
    this.setFocusable(true);
    this.setModal(true);

    this.setTitle(" Modification d'une limitaiton de CES "); //$NON-NLS-1$
    this.setLayout(null);

    JLabel labelCESMin = new JLabel();
    labelCESMin.setBounds(10, 10, 130, 20);
    labelCESMin.setText("Valeur minimale"); //$NON-NLS-1$
    this.add(labelCESMin);

    // Il s'agit du champs permettant de choisir la valeur min
    this.jTFAngleMin = new JTextField(limitation.getAngleMin() + "");
    this.jTFAngleMin.setBounds(160, 10, 40, 20);
    this.jTFAngleMin.setVisible(true);
    this.jTFAngleMin.addActionListener(this);
    this.add(this.jTFAngleMin);

    JLabel labelCESMax = new JLabel();
    labelCESMax.setBounds(10, 40, 130, 20);
    labelCESMax.setText("Valeur maximale"); //$NON-NLS-1$
    this.add(labelCESMax);

    // Il s'agit du champs permettant de choisir la valeur max
    this.jTFAngleMax = new JTextField(limitation.getAngleMax() + "");
    this.jTFAngleMax.setBounds(160, 40, 40, 20);
    this.jTFAngleMax.setVisible(true);
    this.jTFAngleMax.addActionListener(this);
    this.add(this.jTFAngleMax);

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
        valMin = Double.parseDouble(this.jTFAngleMin.getText());

      } catch (NumberFormatException nfe) {
        JOptionPane.showMessageDialog(this,
            "La valeur minimale n'est pas un nombre décimal", //$NON-NLS-1$
            "Erreur de validation", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
        return;

      }

      double valMax;
      try {
        valMax = Double.parseDouble(this.jTFAngleMax.getText());

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

      // En fonction du mode choisi, les conséquences ne sont pas les
      // mémes
      if (this.modif) {

        this.limitationAngle.setAngleMax(valMax);
        this.limitationAngle.setAngleMin(valMin);
      } else {
        AngleToit nouvelleLimit = new AngleToit(valMin, valMax);
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

}
