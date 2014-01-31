package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.ihm.ihmConsequence;

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

import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.ConsequenceTexture;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.Regle;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.Texture;

/**
 * 
 * @author MBrasebin
 * 
 */
public class IHMLimitationTexture extends JDialog implements WindowListener,
    ActionListener {

  private static final long serialVersionUID = -9154653195442442193L;

  JButton ok = new JButton();
  JButton annul = new JButton();

  private Regle reg;

  // La liste des différentes checkboxes
  private ArrayList<JCheckBox> lCBTextures;

  // La conséquence que l'on est susceptible de modifier
  private ConsequenceTexture consequenceTexture;

  // indique si l'on est en mode modification ou non
  private boolean modif;

  /**
   * Constructeur lors du cas de l'ajout de règle, la conséquence sera alors
   * ajoutée à règle
   * @param reg
   */
  public IHMLimitationTexture(Regle regle) {
    super();

    // Elle est rendue modale
    this.setFocusable(true);
    this.setModal(true);

    this.reg = regle;

    this.modif = false;

    // Titre
    this.setTitle(" Limitation des textures "); //$NON-NLS-1$
    this.setLayout(null);

    this.setBounds(0, 0, 450, 320);

    JLabel label = new JLabel("Quelles sont les textures à conserver ?");
    label.setBounds(10, 10, 500, 30);
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

    ascenseurs.setBounds(25, 40, 400, 200);
    ascenseurs
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.add(ascenseurs);

    // Boutons de validations
    this.ok.setBounds(100, 250, 100, 20);
    this.ok.setText("Ok"); //$NON-NLS-1$
    this.ok.addActionListener(this);
    this.add(this.ok);

    this.annul.setBounds(200, 250, 100, 20);
    this.annul.setText("Annuler"); //$NON-NLS-1$
    this.annul.addActionListener(this);
    this.add(this.annul);

    this.setVisible(true);

  }

  /**
   * Méme chose que précédemment mais dans le cadre de l'édition
   * 
   * Dans ce cas, la conséquence s'en retrouve modifiée
   * @param consquence
   */
  public IHMLimitationTexture(ConsequenceTexture consquence) {
    super();

    this.modif = true;
    this.consequenceTexture = consquence;
    // Elle est rendue modale
    this.setFocusable(true);
    this.setModal(true);

    // Titre
    this.setTitle(" Limitation des textures "); //$NON-NLS-1$
    this.setLayout(null);

    this.setBounds(0, 0, 450, 320);

    JLabel label = new JLabel("Quelles sont les textures à conserver ?");
    label.setBounds(10, 10, 500, 30);
    this.add(label);

    String[] lNomsTextures = Texture.getlNomsTextures();
    int nbElem = lNomsTextures.length;
    System.out.println(nbElem);

    JPanel panTemp = new JPanel();

    BoxLayout box = new BoxLayout(panTemp, BoxLayout.Y_AXIS);

    panTemp.setLayout(box);

    this.lCBTextures = new ArrayList<JCheckBox>(nbElem);

    List<String> lSelectionne = consquence.getNomTexture();

    for (int i = 0; i < nbElem; i++) {
      String s = lNomsTextures[i];
      JCheckBox jChek = new JCheckBox(s);
      jChek.setHorizontalAlignment(SwingConstants.LEFT);

      if (IHMLimitationTexture.isDansList(s, lSelectionne)) {

        jChek.setSelected(true);
      }

      panTemp.add(jChek);
      this.lCBTextures.add(jChek);
    }

    JScrollPane ascenseurs = new JScrollPane(panTemp);

    ascenseurs.setBounds(25, 40, 400, 200);
    ascenseurs
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.add(ascenseurs);

    // Boutons de validations
    this.ok.setBounds(100, 250, 100, 20);
    this.ok.setText("Ok"); //$NON-NLS-1$
    this.ok.addActionListener(this);
    this.add(this.ok);

    this.annul.setBounds(200, 250, 100, 20);
    this.annul.setText("Annuler"); //$NON-NLS-1$
    this.annul.addActionListener(this);
    this.add(this.annul);

    this.setVisible(true);

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

  /**
   * Gestion des actions
   */
  @Override
  public void actionPerformed(ActionEvent actionevent) {
    Object source = actionevent.getSource();

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

      if (this.modif) {
        this.consequenceTexture.setNomTexture(lTextures);
        System.out.println(this.consequenceTexture.toString());
      } else {
        this.reg.getConsequence().add(new ConsequenceTexture(lTextures));
      }

      this.dispose();
    }

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

  public static void main(String[] args) {

    List<String> lt = new ArrayList<String>();
    lt.add("Brique");
    lt.add("Pierre");
    lt.add("Crépi rose");
    lt.add("Tuile rouge");

    new IHMLimitationTexture(new ConsequenceTexture(lt));

  }

}
