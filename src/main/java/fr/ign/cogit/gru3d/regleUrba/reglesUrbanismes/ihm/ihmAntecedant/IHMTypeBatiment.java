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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.Regle;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.Texture;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.TypeBatiment;

public class IHMTypeBatiment extends JDialog implements WindowListener,
    ActionListener {

  private static final long serialVersionUID = 1L;

  // La liste des différentes checkboxes
  private ArrayList<JCheckBox> lCBTextures;
  private ArrayList<JCheckBox> lCBTypes;

  private TypeBatiment typeBatiment;

  JButton ok = new JButton();
  JButton annul = new JButton();

  private boolean modif = false;

  private Regle r;

  /**
   * Constructeur lors du cas de l'ajout de règle, l'antécédent sera alors
   * ajoutée à règle
   * 
   * @param reg
   */

  public IHMTypeBatiment(Regle reg) {
    super();

    this.r = reg;

    // Elle est rendue modale
    this.setFocusable(true);
    this.setModal(true);

    // Nous somes en mode ajout
    this.modif = false;

    // Titre
    this.setTitle(" Condition sur le type d'un batiment "); //$NON-NLS-1$
    this.setLayout(null);

    this.setBounds(0, 0, 470, 340);

    JLabel label = new JLabel(
        "Sur les batiments possédant quelles textures doivent s'appliquer les règles ?");
    label.setBounds(10, 5, 500, 30);
    this.add(label);

    // On récupére les textures codées en dur dans l'appli
    String[] lNomsTextures = Texture.getlNomsTextures();
    int nbElem = lNomsTextures.length;

    // On créer un panel que l'on place ensuite dans un Panel muni d'un
    // ascenseur
    JPanel panTemp = new JPanel();

    // Cela permet d'indique que l'on souhaite placer verticalement les
    // différents élements
    BoxLayout box = new BoxLayout(panTemp, BoxLayout.Y_AXIS);

    panTemp.setLayout(box);

    this.lCBTextures = new ArrayList<JCheckBox>(nbElem);

    // On place les différents boutons à cocher.
    for (int i = 0; i < nbElem; i++) {
      JCheckBox jChek = new JCheckBox(lNomsTextures[i]);
      jChek.setHorizontalAlignment(SwingConstants.LEFT);
      panTemp.add(jChek);
      this.lCBTextures.add(jChek);
    }

    // on ajoute dans le panneau dans celui muni d'ascenceurs
    JScrollPane ascenseurs = new JScrollPane(panTemp);

    ascenseurs.setBounds(25, 35, 400, 100);
    ascenseurs
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.add(ascenseurs);

    JLabel label2 = new JLabel(
        "Sur quels types de batîments doivent s'appliquer les règles ?");
    label2.setBounds(10, 140, 500, 30);
    this.add(label2);

    // On récupére les textures codées en dur dans l'appli
    String[] lNomsTypes = TypeBatiment.getLNomsTypes();
    nbElem = lNomsTextures.length;

    // On créer un panel que l'on place ensuite dans un Panel muni d'un
    // ascenseur
    JPanel panTemp2 = new JPanel();

    // Cela permet d'indique que l'on souhaite placer verticalement les
    // différents élements
    BoxLayout box2 = new BoxLayout(panTemp2, BoxLayout.Y_AXIS);

    panTemp2.setLayout(box2);

    this.lCBTypes = new ArrayList<JCheckBox>(nbElem);

    // On place les différents boutons à cocher.
    for (int i = 0; i < nbElem; i++) {
      JCheckBox jChek = new JCheckBox(lNomsTypes[i]);
      jChek.setHorizontalAlignment(SwingConstants.LEFT);
      panTemp2.add(jChek);
      this.lCBTypes.add(jChek);
    }

    // on ajoute dans le panneau dans celui muni d'ascenceurs
    JScrollPane ascenseurs2 = new JScrollPane(panTemp2);

    ascenseurs2.setBounds(25, 175, 400, 100);
    ascenseurs2
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.add(ascenseurs2);

    // Boutons de validations
    this.ok.setBounds(100, 280, 100, 20);
    this.ok.setText("Ok"); //$NON-NLS-1$
    this.ok.addActionListener(this);
    this.add(this.ok);

    this.annul.setBounds(200, 280, 100, 20);
    this.annul.setText("Annuler"); //$NON-NLS-1$
    this.annul.addActionListener(this);
    this.add(this.annul);

    this.setVisible(true);

  }

  public IHMTypeBatiment(TypeBatiment typeBati) {
    super();

    // Elle est rendue modale
    this.setFocusable(true);
    this.setModal(true);

    this.typeBatiment = typeBati;

    // Nous somes en mode modifcation
    this.modif = true;

    // Titre
    this.setTitle(" Condition sur le type d'un batîment "); //$NON-NLS-1$
    this.setLayout(null);

    this.setBounds(0, 0, 470, 340);

    JLabel label = new JLabel(
        "Sur les batiments possédant quelles textures doivent s'appliquer les règles ?");
    label.setBounds(10, 5, 500, 30);
    this.add(label);

    // On récupére les textures codées en dur dans l'appli
    String[] lNomsTextures = Texture.getlNomsTextures();
    int nbElem = lNomsTextures.length;

    // On créer un panel que l'on place ensuite dans un Panel muni d'un
    // ascenseur
    JPanel panTemp = new JPanel();

    // Cela permet d'indique que l'on souhaite placer verticalement les
    // différents élements
    BoxLayout box = new BoxLayout(panTemp, BoxLayout.Y_AXIS);

    panTemp.setLayout(box);

    this.lCBTextures = new ArrayList<JCheckBox>(nbElem);

    List<String> lTextureSelectionne = typeBati.getNomTextures();

    // On place les différents boutons à cocher.
    for (int i = 0; i < nbElem; i++) {

      String s = lNomsTextures[i];
      JCheckBox jChek = new JCheckBox(s);
      jChek.setHorizontalAlignment(SwingConstants.LEFT);

      if (IHMTypeBatiment.isDansList(s, lTextureSelectionne)) {

        jChek.setSelected(true);
      }

      panTemp.add(jChek);
      this.lCBTextures.add(jChek);
    }

    // on ajoute dans le panneau dans celui muni d'ascenceurs
    JScrollPane ascenseurs = new JScrollPane(panTemp);

    ascenseurs.setBounds(25, 35, 400, 100);
    ascenseurs
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.add(ascenseurs);

    JLabel label2 = new JLabel(
        "Sur quels types de batiments doivent s'appliquer les règles ?");
    label2.setBounds(10, 140, 500, 30);
    this.add(label2);

    // On récupére les textures codées en dur dans l'appli
    String[] lNomsTypes = TypeBatiment.getLNomsTypes();
    nbElem = lNomsTextures.length;

    // On créer un panel que l'on place ensuite dans un Panel muni d'un
    // ascenseur
    JPanel panTemp2 = new JPanel();

    // Cela permet d'indique que l'on souhaite placer verticalement les
    // différents élements
    BoxLayout box2 = new BoxLayout(panTemp2, BoxLayout.Y_AXIS);

    panTemp2.setLayout(box2);

    this.lCBTypes = new ArrayList<JCheckBox>(nbElem);

    List<String> lTypesSelectionne = typeBati.getNomTypes();

    // On place les différents boutons à cocher.
    for (int i = 0; i < nbElem; i++) {

      String s = lNomsTypes[i];
      JCheckBox jChek = new JCheckBox(s);
      jChek.setHorizontalAlignment(SwingConstants.LEFT);

      if (IHMTypeBatiment.isDansList(s, lTypesSelectionne)) {

        jChek.setSelected(true);

      }

      panTemp2.add(jChek);
      this.lCBTypes.add(jChek);
    }

    // on ajoute dans le panneau dans celui muni d'ascenceurs
    JScrollPane ascenseurs2 = new JScrollPane(panTemp2);

    ascenseurs2.setBounds(25, 175, 400, 100);
    ascenseurs2
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.add(ascenseurs2);

    // Boutons de validations
    this.ok.setBounds(100, 280, 100, 20);
    this.ok.setText("Ok"); //$NON-NLS-1$
    this.ok.addActionListener(this);
    this.add(this.ok);

    this.annul.setBounds(200, 280, 100, 20);
    this.annul.setText("Annuler"); //$NON-NLS-1$
    this.annul.addActionListener(this);
    this.add(this.annul);

    this.setVisible(true);

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
    Object source = e.getSource();

    if (source.equals(this.annul)) {
      this.dispose();
      return;

    }

    if (source.equals(this.ok)) {

      ArrayList<String> lTextures = new ArrayList<String>();

      int nbElem = this.lCBTextures.size();

      for (int i = 0; i < nbElem; i++) {

        JCheckBox jCB = this.lCBTextures.get(i);
        if (jCB.isSelected()) {
          lTextures.add(jCB.getText());

        }

      }

      ArrayList<String> lTypes = new ArrayList<String>();

      nbElem = this.lCBTypes.size();

      for (int i = 0; i < nbElem; i++) {

        JCheckBox jCB = this.lCBTypes.get(i);
        if (jCB.isSelected()) {
          lTypes.add(jCB.getText());

        }

      }

      if (this.modif) {
        this.typeBatiment.setNomTextures(lTextures);
        this.typeBatiment.setNomTypes(lTypes);

      } else {
        TypeBatiment tpB = new TypeBatiment(lTypes, lTextures);
        this.r.getAntecedent().add(tpB);
      }

      this.dispose();
      return;

    }
  }

  public static void main(String[] args) {

    List<String> lt = new ArrayList<String>();
    lt.add("Brique");
    lt.add("Pierre");
    lt.add("Crépi rose");
    lt.add("Tuile rouge");

    List<String> ltypes = new ArrayList<String>();
    ltypes.add("Construction à destination d'habitation");
    ltypes.add("Construction à destination d'enseignement");
    ltypes.add("Construction à destination de santé");
    ltypes.add("Construction à destination de loisirs");

    TypeBatiment t = new TypeBatiment(ltypes, lt);

    new IHMTypeBatiment(t);

  }

  private static boolean isDansList(String s, List<String> al) {
    boolean present = false;

    int nbElem = al.size();

    for (int i = 0; i < nbElem; i++) {

      if (al.get(i).equalsIgnoreCase(s)) {
        return true;
      }

    }

    return present;
  }
}
