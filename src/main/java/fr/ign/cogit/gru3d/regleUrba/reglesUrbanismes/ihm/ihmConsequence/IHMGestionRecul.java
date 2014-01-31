package fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.ihm.ihmConsequence;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.ihm.FenetreEditionRegle;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.Consequence;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.Distance;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.DistanceEuclidienne;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.DistanceFHauteur;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.Recul;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.ReculAutreBatiment;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.ReculBordure;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.ReculRoute;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.Regle;

public class IHMGestionRecul extends JDialog implements WindowListener,
    ActionListener {
  // Il s'agit des éléments que l'on renvoie gréce à ce formulaire
  // Les entités dont on se reculera
  // Le type de distance
  // La valeur de la distance
  JComboBox jCBEntite;
  JComboBox jCBBDistance;
  JTextField jTFDistMin;

  // Ce booléan indique si l'on est en mode édition ou création
  private boolean modif;

  // Bouton permettant de valider/annuler
  JButton ok = new JButton();
  JButton annul = new JButton();

  // La règle à laquelle sera ajoutée cette conséquence
  private Regle r;

  // En cas de mode modification, il s'agit de la conséquence que l'on modifie
  private Recul recul;

  // Il s'agit de la liste des entités sur lesquelles un recul est possible
  private static String[] entitesDisponibles = { "Route", "Parcelle",
      "Autre batiment" };

  // Il s'agit des différents types de distances
  private static String[] distancesDisponibles = { "Euclidienne",
      "Fraction de la hauteur" };

  // Il s'agit du texte correspondant à la distance min dont le sens varie en
  // fonction du type de distance choisie
  private JLabel labelDistMin;

  private JLabel jLHauteurSurRoute;
  private JTextField jTFHauteurSurRoute;

  /**
	 */
  private static final long serialVersionUID = 1L;

  /**
   * Méthode permettant de créer l'ajout d'une conséquence dans la règle règle
   */
  public IHMGestionRecul(Regle regle) {

    super();
    this.modif = false;
    this.r = regle;

    // Elle est rendue modale
    this.setFocusable(true);
    this.setModal(true);

    // Titre
    this.setTitle(" Ajout d'un recul "); //$NON-NLS-1$
    this.setLayout(null);

    // Formulaire du type
    JLabel labelNom = new JLabel();
    labelNom.setBounds(10, 10, 130, 20);
    labelNom.setText("Recul par rapport"); //$NON-NLS-1$
    this.add(labelNom);

    // Il s'agit de la liste déroulante permettant de choisir l'entitité
    // dont on se recul
    this.jCBEntite = new JComboBox(IHMGestionRecul.entitesDisponibles);
    this.jCBEntite.setBounds(160, 10, 200, 20);
    this.jCBEntite.setVisible(true);
    this.jCBEntite.addActionListener(this);
    this.add(this.jCBEntite);

    // Formulaire du chemin
    JLabel labelChemin = new JLabel();
    labelChemin.setBounds(10, 50, 130, 20);
    labelChemin.setText("Distance"); //$NON-NLS-1$
    this.add(labelChemin);

    // Il s'agit de la liste déroulante permettant de choisir le type de
    // distance
    this.jCBBDistance = new JComboBox(IHMGestionRecul.distancesDisponibles);
    this.jCBBDistance.setBounds(160, 50, 200, 20);
    this.jCBBDistance.setVisible(true);
    this.jCBBDistance.addActionListener(this);
    this.add(this.jCBBDistance);

    // Il s'agit du texte variant suivant le type de distance choisie
    this.labelDistMin = new JLabel();
    this.labelDistMin.setBounds(10, 90, 130, 20);
    this.labelDistMin.setText("Valeur minimale"); //$NON-NLS-1$
    this.add(this.labelDistMin);

    // Il s'agit du champs permettant de choisir la distance
    this.jTFDistMin = new JTextField("0");
    this.jTFDistMin.setBounds(160, 90, 200, 20);
    this.jTFDistMin.setVisible(true);
    this.jTFDistMin.addActionListener(this);
    this.add(this.jTFDistMin);

    this.jLHauteurSurRoute = new JLabel("Hauteur origine (m)");
    this.jLHauteurSurRoute.setBounds(10, 130, 200, 20);
    this.jLHauteurSurRoute.setVisible(false);
    this.add(this.jLHauteurSurRoute);

    this.jTFHauteurSurRoute = new JTextField("0");
    this.jTFHauteurSurRoute.setBounds(300, 130, 50, 20);
    this.jTFHauteurSurRoute.addActionListener(this);
    this.jTFHauteurSurRoute.setVisible(false);
    this.add(this.jTFHauteurSurRoute);

    // Boutons de validations
    this.ok.setBounds(100, 170, 100, 20);
    this.ok.setText("Ok"); //$NON-NLS-1$
    this.ok.addActionListener(this);
    this.add(this.ok);

    this.annul.setBounds(200, 170, 100, 20);
    this.annul.setText("Annuler"); //$NON-NLS-1$
    this.annul.addActionListener(this);
    this.add(this.annul);

    this.setBackground(Color.white);
    this.setSize(400, 230);
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
  public IHMGestionRecul(Recul recul) {

    super();

    this.recul = recul;
    this.modif = true;

    Distance d;

    // On récupére les informations contenues par la recul
    int index = 0;
    if (recul instanceof ReculRoute) {

      ReculRoute RR = (ReculRoute) recul;
      d = RR.getDistanceRecul();

    } else if (recul instanceof ReculBordure) {

      ReculBordure RB = (ReculBordure) recul;
      d = RB.getDistanceRecul();
      index = 1;
    } else {
      index = 2;
      ReculAutreBatiment RAB = (ReculAutreBatiment) recul;
      d = RAB.getDistanceRecul();
    }

    double coef = 0;

    double hOrigin = 0;

    int typeDist = 0;

    // On récupére les informations contenues dans la distance
    if (d instanceof DistanceEuclidienne) {
      DistanceEuclidienne de = (DistanceEuclidienne) d;
      coef = de.getDMin();

    } else if (d instanceof DistanceFHauteur) {
      DistanceFHauteur df = (DistanceFHauteur) d;
      coef = df.getCoefficient();
      typeDist = 1;
      hOrigin = df.getHauteurOrigine();
    }

    // Elle est rendue modale
    this.setFocusable(true);
    this.setModal(true);

    // Titre
    this.setTitle(" Edition d'un recul "); //$NON-NLS-1$
    this.setLayout(null);

    // Formulaire du type
    JLabel labelNom = new JLabel();
    labelNom.setBounds(10, 10, 130, 20);
    labelNom.setText("Recul par rapport"); //$NON-NLS-1$
    this.add(labelNom);

    // Il s'agit de la liste déroulante permettant de choisir l'entitité
    // dont on se recul
    this.jCBEntite = new JComboBox(IHMGestionRecul.entitesDisponibles);
    this.jCBEntite.setSelectedIndex(index);
    this.jCBEntite.setBounds(160, 10, 200, 20);
    this.jCBEntite.setVisible(true);
    this.jCBEntite.addActionListener(this);
    this.add(this.jCBEntite);

    // Formulaire du chemin
    JLabel labelChemin = new JLabel();
    labelChemin.setBounds(10, 50, 130, 20);
    labelChemin.setText("Distance"); //$NON-NLS-1$
    this.add(labelChemin);

    // Il s'agit de la liste déroulante permettant de choisir le type de
    // distance
    this.jCBBDistance = new JComboBox(IHMGestionRecul.distancesDisponibles);
    this.jCBBDistance.setSelectedIndex(typeDist);
    this.jCBBDistance.setBounds(160, 50, 200, 20);
    this.jCBBDistance.setVisible(true);
    this.jCBBDistance.addActionListener(this);
    this.add(this.jCBBDistance);

    // Il s'agit du texte variant suivant le type de distance choisie
    this.labelDistMin = new JLabel();
    this.labelDistMin.setBounds(10, 90, 130, 20);
    this.labelDistMin.setText("Valeur minimale"); //$NON-NLS-1$
    this.add(this.labelDistMin);

    // Il s'agit du champs permettant de choisir la distance
    this.jTFDistMin = new JTextField(coef + "");
    this.jTFDistMin.setBounds(160, 90, 200, 20);
    this.jTFDistMin.setVisible(true);
    this.jTFDistMin.addActionListener(this);
    this.add(this.jTFDistMin);

    this.jLHauteurSurRoute = new JLabel("Hauteur origine (m)");
    this.jLHauteurSurRoute.setBounds(10, 130, 200, 20);
    this.jLHauteurSurRoute.setVisible(typeDist == 1);
    this.add(this.jLHauteurSurRoute);

    this.jTFHauteurSurRoute = new JTextField(hOrigin + "");
    this.jTFHauteurSurRoute.setBounds(300, 130, 50, 20);
    this.jTFHauteurSurRoute.setVisible(typeDist == 1);
    this.jTFHauteurSurRoute.addActionListener(this);
    this.add(this.jTFHauteurSurRoute);

    // Boutons de validations
    this.ok.setBounds(100, 170, 100, 20);
    this.ok.setText("Ok"); //$NON-NLS-1$
    this.ok.addActionListener(this);
    this.add(this.ok);

    this.annul.setBounds(200, 170, 100, 20);
    this.annul.setText("Annuler"); //$NON-NLS-1$
    this.annul.addActionListener(this);
    this.add(this.annul);

    this.setBackground(Color.white);
    this.setSize(400, 230);
    this.setVisible(true);
  }

  /**
   * Gestion des actions
   */
  @Override
  public void actionPerformed(ActionEvent actionevent) {
    Object source = actionevent.getSource();

    // Changement du texte en fonction de la distance choisie
    if (source.equals(this.jCBBDistance)) {
      int val = this.jCBBDistance.getSelectedIndex();
      if (val == 0) {

        this.labelDistMin.setText("Valeur minimale");
        this.jTFHauteurSurRoute.setVisible(false);
        this.jLHauteurSurRoute.setVisible(false);
      } else {
        this.labelDistMin.setText("Coefficient hauteur");
        this.jTFHauteurSurRoute.setVisible(true);
        this.jLHauteurSurRoute.setVisible(true);
      }

    }

    // Navigateur pour sélectionné un fichier adapté au choix

    // bouton de validation
    if (source.equals(this.ok)) {
      // On récupére les différents objets et on génére la distance ...
      Distance d;

      // On récupére les différents objets et on génére la contrainte
      double valMin;

      // On gére les mauvaises saisies
      try {
        valMin = Double.parseDouble(this.jTFDistMin.getText());

      } catch (NumberFormatException nfe) {
        JOptionPane.showMessageDialog(this,
            "La valeur n'est pas un nombre décimal", //$NON-NLS-1$
            "Erreur de validation", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
        return;

      }

      if (valMin < 0) {

        JOptionPane.showMessageDialog(this, "Le recul ne peut être négatif", //$NON-NLS-1$
            "Erreur de validation", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
        return;
      }

      int val = this.jCBBDistance.getSelectedIndex();

      if (val == 0) {
        d = new DistanceEuclidienne(valMin);

      } else {

        double hauteurOrigine;

        try {
          hauteurOrigine = Double
              .parseDouble(this.jTFHauteurSurRoute.getText());

        } catch (NumberFormatException nfe) {
          JOptionPane.showMessageDialog(this,
              "La valeur n'est pas un nombre décimal", //$NON-NLS-1$
              "Erreur de validation", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
          return;

        }

        d = new DistanceFHauteur(valMin, hauteurOrigine);

      }

      Recul recul;

      // ... puis la contrainte de recul
      if (this.jCBEntite.getSelectedItem().toString().equalsIgnoreCase("Route")) {

        recul = new ReculRoute(d);
      } else if (this.jCBEntite.getSelectedItem().toString()
          .equalsIgnoreCase("Parcelle")) {

        recul = new ReculBordure(d);
      } else {
        recul = new ReculAutreBatiment(d);

      }

      // En fonction du mode choisi, les conséquences ne sont pas les
      // mémes
      if (this.modif) {

        List<Consequence> lCons = FenetreEditionRegle.getRegle()
            .getConsequence();
        int index = lCons.indexOf(this.recul);

        if (index != -1) {

          lCons.remove(index);
          lCons.add(index, recul);
        }

      } else {

        this.r.getConsequence().add(recul);
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
