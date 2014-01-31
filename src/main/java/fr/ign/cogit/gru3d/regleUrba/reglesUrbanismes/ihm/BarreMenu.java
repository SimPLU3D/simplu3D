package fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.ihm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class BarreMenu extends JMenuBar implements ActionListener {

  public static void setNomDocument(String nomDocument) {
    BarreMenu.nomDocument = nomDocument;
  }

  public JButton getAjoutRegle() {
    return this.ajoutRegle;
  }

  /**
		 * 
		 */
  private static final long serialVersionUID = -4336699222086830401L;

  private JMenu file;

  private JMenuItem nouveau, ouvrir, sauvegarder, fermer;

  private JButton ajoutRegle, ajoutZone, suppZone, suppRegle;

  private MainWindow fen;

  private static String nomDocument = "";

  public static String getNomDocument() {
    return BarreMenu.nomDocument;
  }

  public BarreMenu(MainWindow fenPrin) {

    this.fen = fenPrin;

    this.file = new JMenu("Fichier");

    this.nouveau = new JMenuItem("Nouveau");
    this.ouvrir = new JMenuItem("Ouvrir");
    this.sauvegarder = new JMenuItem("Sauvegarder");
    this.fermer = new JMenuItem("Fermer");

    this.nouveau.addActionListener(this);
    this.file.add(this.nouveau);

    this.ouvrir.addActionListener(this);
    this.file.add(this.ouvrir);

    this.sauvegarder.addActionListener(this);
    this.file.add(this.sauvegarder);

    this.fermer.addActionListener(this);
    this.file.add(this.fermer);

    this.add(this.file);

    this.ajoutZone = new JButton("Ajout zone");
    this.ajoutZone.setEnabled(false);

    this.ajoutRegle = new JButton("Ajout règle");
    this.ajoutRegle.setEnabled(false);

    this.ajoutRegle.addActionListener(this);
    this.ajoutZone.addActionListener(this);

    this.add(this.ajoutZone);
    this.add(this.ajoutRegle);

    this.suppZone = new JButton("Supp. zone");
    this.suppRegle = new JButton("Supp. règle");

    this.suppZone.addActionListener(this);
    this.suppRegle.addActionListener(this);

    this.add(this.suppZone);
    this.add(this.suppRegle);

  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub

    Object source = e.getSource();

    if (source.equals(this.ajoutZone)) {

      new AjoutZonePLU(this.fen);
    }
    if (source.equals(this.sauvegarder)) {

      new IHMSauvegarde(this.fen);
    }

    if (source.equals(this.ouvrir)) {

      new IHMChargement(this.fen);

      this.ajoutZone.setEnabled(true);
    }

    if (source.equals(this.fermer)) {
      this.fen.dispose();
    }

    if (source.equals(this.nouveau)) {
      String inputValue = JOptionPane.showInputDialog("Nom du projet");

      if (inputValue == null) {

        return;
      }

      if (inputValue == "") {

        return;
      }

      BarreMenu.nomDocument = inputValue;

      this.fen.reset();

      this.ajoutZone.setEnabled(true);

      this.fen.setTitle(" Editeur de règles d'urbanisme - Projet : "
          + BarreMenu.nomDocument);
      new AjoutZonePLU(this.fen);

    }

    if (source.equals(this.ajoutRegle)) {
      OngletZone ongletVisible = this.fen.getPanneauOnglets()
          .getOngletVisible();
      if (ongletVisible != null) {
        ongletVisible.ajoutRegle();
      }
    }

    if (source.equals(this.suppRegle)) {

      OngletZone ongletZ = this.fen.getPanneauOnglets().getOngletVisible();

      if (ongletZ != null) {
        ongletZ.supprimeRegle();
      }

    }

    if (source.equals(this.suppZone)) {
      OngletZone ongletZ = this.fen.getPanneauOnglets().getOngletVisible();

      if (ongletZ != null) {
        this.fen.getPanneauOnglets().retireOnglet(ongletZ);
      }
    }

  }

}
