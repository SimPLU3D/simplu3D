package fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.ihm.ihmConsequence;

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

import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.BandeConstructibilite;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.Regle;

/**
 * Cette fenêtre permet l'affichage des différents types de données chargeables
 * 
 * Windows of this class manage the loadinf of different kinds of available data
 * 
 * 
 * @author MBrasebin TODO rajouter MNT une fois la solution trouvée
 */
public class IHMBandeConstructibilite extends JDialog implements
    WindowListener, ActionListener {

  // Chemin du fichier contenant les informations sur les bandes de
  // constructibilité
  // en espérant un .shp
  JTextField jTFChemin;

  // Bouton activant le browser
  JButton browse = new JButton();

  JButton ok = new JButton();
  JButton annul = new JButton();

  // La règle à laquelle sera ajoutée cette conséquence
  private Regle r;

  // En cas de mode modification, il s'agit de la conséquence que l'on modifie
  private BandeConstructibilite bandeCons;

  // Indique si l'on est en mode modification ou non
  private boolean modification = true;

  /**
	 */
  private static final long serialVersionUID = 1L;

  /**
   * Il s'agit de la fenetre permettant de définir une contrainte concernant les
   * règles de constructibilité
   * @param regle
   */
  public IHMBandeConstructibilite(Regle regle) {
    super();
    this.modification = false;
    // Elle est rendue modale
    this.setFocusable(true);
    this.setModal(true);

    this.r = regle;

    // Titre
    this.setTitle(" Contrainte de bande de constructibilité "); //$NON-NLS-1$
    this.setLayout(null);

    // Formulaire du chemin
    JLabel labelChemin = new JLabel();
    labelChemin.setBounds(10, 10, 100, 20);
    labelChemin.setText("Choix d'un fichier"); //$NON-NLS-1$
    this.add(labelChemin);

    // Le chemin du fichier s'affiche
    this.jTFChemin = new JTextField();
    this.jTFChemin.setBounds(160, 10, 200, 20);
    this.jTFChemin.setVisible(true);
    this.jTFChemin.addActionListener(this);
    this.jTFChemin.setEnabled(false);
    this.jTFChemin.setText(""); //$NON-NLS-1$
    this.add(this.jTFChemin);

    this.browse.setBounds(360, 10, 20, 20);
    this.browse.setText("..."); //$NON-NLS-1$
    this.browse.setVisible(true);
    this.browse.addActionListener(this);
    this.add(this.browse);

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
    this.setSize(400, 120);
    this.setVisible(true);
  }

  /**
   * Il s'agit de la fenetre permettant de définir une contrainte concernant les
   * règles de constructibilité
   * @param regle
   */
  public IHMBandeConstructibilite(BandeConstructibilite bandeCons) {
    super();
    this.modification = true;
    // Elle est rendue modale
    this.setFocusable(true);
    this.setModal(true);

    this.bandeCons = bandeCons;

    // Titre
    this.setTitle(" Contrainte de bande de constructibilité "); //$NON-NLS-1$
    this.setLayout(null);

    // Formulaire du chemin
    JLabel labelChemin = new JLabel();
    labelChemin.setBounds(10, 10, 100, 20);
    labelChemin.setText("Choix d'un fichier"); //$NON-NLS-1$
    this.add(labelChemin);

    // Le chemin du fichier s'affiche
    this.jTFChemin = new JTextField("" + bandeCons.getFichierBande());
    this.jTFChemin.setBounds(160, 10, 200, 20);
    this.jTFChemin.setVisible(true);
    this.jTFChemin.addActionListener(this);
    this.jTFChemin.setEnabled(false);
    this.jTFChemin.setText(""); //$NON-NLS-1$
    this.add(this.jTFChemin);

    this.browse.setBounds(360, 10, 20, 20);
    this.browse.setText("..."); //$NON-NLS-1$
    this.browse.setVisible(true);
    this.browse.addActionListener(this);
    this.add(this.browse);

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
    this.setSize(400, 120);
    this.setVisible(true);
  }

  /**
   * Gestion des actions
   */
  @Override
  public void actionPerformed(ActionEvent actionevent) {
    Object source = actionevent.getSource();

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

    }

    // bouton de validation
    if (source.equals(this.ok)) {

      // On calcule les paramêtres

      String chemin = "" + this.jTFChemin.getText();

      if (this.modification) {
        this.bandeCons.setFichierBande(chemin);
      } else {
        BandeConstructibilite cons = new BandeConstructibilite(chemin);
        this.r.getConsequence().add(cons);
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

    new IHMBandeConstructibilite(new Regle());
  }

}
