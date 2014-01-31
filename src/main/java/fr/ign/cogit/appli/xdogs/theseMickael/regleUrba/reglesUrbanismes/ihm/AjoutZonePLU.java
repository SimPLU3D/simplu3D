package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.ihm;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.ZonePLU;

public class AjoutZonePLU extends JDialog implements WindowListener,
    ActionListener {

  private MainWindow fen;

  // Zone d'inscription du nom de la zone
  private JTextField jTFNomZone;

  // Zone d'inscription de l'URL du document rattaché à la zone
  private JTextField jTFChemin;

  private JButton browse = new JButton();

  private JButton ok = new JButton();
  private JButton annul = new JButton();

  /**
		 */
  private static final long serialVersionUID = 1L;

  /**
   * Initialisation de la fenetre
   * @param f
   */
  public AjoutZonePLU(MainWindow fen) {
    super();

    this.fen = fen;
    // Elle est rendue modale
    this.setFocusable(true);
    this.setModal(true);

    // Titre
    this.setTitle(" Ajout d'une zone PLU "); //$NON-NLS-1$
    this.setLayout(null);

    // Formulaire du type
    JLabel labelNom = new JLabel();
    labelNom.setBounds(10, 10, 100, 20);
    labelNom.setText(" Nom de la Zone"); //$NON-NLS-1$
    this.add(labelNom);

    this.jTFNomZone = new JTextField();
    this.jTFNomZone.setBounds(160, 10, 200, 20);
    this.jTFNomZone.setVisible(true);
    this.jTFNomZone.addActionListener(this);
    this.add(this.jTFNomZone);

    this.jTFChemin = new JTextField();
    this.jTFChemin.setBounds(160, 50, 200, 20);
    this.jTFChemin.setVisible(true);
    this.jTFChemin.addActionListener(this);
    this.jTFChemin.setText(""); //$NON-NLS-1$
    this.add(this.jTFChemin);

    // Formulaire du chemin
    JLabel labelChemin = new JLabel();
    labelChemin.setBounds(10, 50, 100, 20);
    labelChemin.setText(" Fichier à joindre "); //$NON-NLS-1$
    this.add(labelChemin);

    this.browse.setBounds(360, 50, 20, 20);
    this.browse.setText("..."); //$NON-NLS-1$
    this.browse.setVisible(true);
    this.browse.addActionListener(this);
    this.add(this.browse);

    // Boutons de validations
    this.ok.setBounds(100, 90, 100, 20);
    this.ok.setText("Ok"); //$NON-NLS-1$
    this.ok.addActionListener(this);
    this.add(this.ok);

    this.annul.setBounds(200, 90, 100, 20);
    this.annul.setText("Non"); //$NON-NLS-1$
    this.annul.addActionListener(this);
    this.add(this.annul);

    this.setBackground(Color.white);
    this.setSize(400, 150);
    this.setVisible(true);
  }

  /**
   * Gestion des actions
   */
  @Override
  public void actionPerformed(ActionEvent actionevent) {
    Object source = actionevent.getSource();

    // Changement de valeur on efface le texte
    if (source.equals(this.jTFNomZone)) {

      this.jTFChemin.setText(""); //$NON-NLS-1$

    }

    // Navigateur pour sélectionné un fichier adapté au choix
    if (source.equals(this.browse)) {

      JFileChooser homeChooser = new JFileChooser(""); //$NON-NLS-1$

      javax.swing.filechooser.FileFilter filtre = null;

      homeChooser.setAcceptAllFileFilterUsed(false);

      // Un certain type de fichier est acceepté
      homeChooser.addChoosableFileFilter(filtre);

      homeChooser.showOpenDialog(null);

      File file = homeChooser.getSelectedFile();

      if (file == null) {
        return;
      }
      String nomfichier = file.getPath();

      this.jTFChemin.setText(nomfichier);

      return;

    }

    // bouton de validation
    if (source.equals(this.ok)) {

      // On calcule les paramêtres

      String nom = this.jTFNomZone.getText();
      String chemin = this.jTFChemin.getText();

      if (nom == null) {

        nom = "Sans Nom " + PanneauOnglets.compteurModif;
      }

      if (nom == "") {

        nom = "Sans Nom " + PanneauOnglets.compteurModif;
      }

      if (chemin == null) {
        chemin = "";

      }

      ZonePLU zone = new ZonePLU(nom, chemin);
      this.fen.getPanneauOnglets().ajoutOnglet(new OngletZone(zone));

      this.dispose();

      this.fen.setVisible(true);

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

  public static void main(String[] args) {

    new AjoutZonePLU(null);
  }

}
