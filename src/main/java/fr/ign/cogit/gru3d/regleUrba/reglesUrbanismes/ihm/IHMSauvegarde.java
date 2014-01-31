package fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.ihm;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.io.Sauvegarde;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.PLU;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.ZonePLU;

/**
 * Cette fenêtre permet l'affichage des différents types de données
 * sauvegardables
 * 
 * 
 * 
 * @author MBrasebin
 */
public class IHMSauvegarde extends JDialog implements WindowListener,
    ActionListener {

  JTextField jTFChemin;

  // Bouton activant le browser
  JButton browse = new JButton();

  JButton ok = new JButton();
  JButton annul = new JButton();

  private MainWindow fenetreP;

  /**
	 */
  private static final long serialVersionUID = 1L;

  /**
   * Il s'agit de la fenetre permettant de définir une contrainte concernant les
   * règles de constructibilité
   * 
   * @param regle
   */
  public IHMSauvegarde(MainWindow fenetre) {
    super();
    // Elle est rendue modale
    this.setFocusable(true);
    this.setModal(true);

    this.fenetreP = fenetre;

    // Titre
    this.setTitle(" Sauvegarde des règles "); //$NON-NLS-1$
    this.setLayout(null);

    // Formulaire du chemin
    JLabel labelChemin = new JLabel();
    labelChemin.setBounds(10, 10, 150, 20);
    labelChemin.setText("Choix d'un dossier"); //$NON-NLS-1$
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
      homeChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

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

      List<ZonePLU> listeZonePLU = new ArrayList<ZonePLU>();

      List<OngletZone> lONGZone = this.fenetreP.getPanneauOnglets()
          .getlOnglets();
      int nbZones = lONGZone.size();

      for (int i = 0; i < nbZones; i++) {

        listeZonePLU.add(lONGZone.get(i).getZonePlu());

      }

      PLU plu = new PLU(listeZonePLU);

      Sauvegarde.sauvegarde("", plu);

      this.fenetreP.getMenu();
      String s = this.jTFChemin.getText() + "\\" + BarreMenu.getNomDocument()
          + ".xml";

      Sauvegarde.sauvegarde(s, plu);

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
