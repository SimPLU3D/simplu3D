package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.Regle;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.ZonePLU;

public class OngletZone extends JPanel {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;
  private ZonePLU zonePlu;
  private JList jLRegles;

  protected static int compteurModif = 0;

  public OngletZone(ZonePLU zone) {
    super(false);

    this.zonePlu = zone;

    this.setBackground(Color.white);

    this.setLayout(new GridLayout(1, 1));

    this.jLRegles = new JList();
    this.jLRegles.setCellRenderer(new CustomListCellRenderer());
    this.jLRegles.setListData(this.zonePlu.getRegles().toArray());

    this.jLRegles.addMouseListener(new MouseAdapter() {

      @Override
      public void mouseClicked(MouseEvent evt) {
        JList list = (JList) evt.getSource();
        if (evt.getClickCount() == 2) { // Double-click
          int index = list.locationToIndex(evt.getPoint());

          if (index == -1) {
            return;
          }

          Regle rModifiee = OngletZone.this.zonePlu.getRegles().get(index);
          new FenetreEditionRegle(rModifiee, (OngletZone) list.getParent()
              .getParent().getParent());
        }
      }
    });

    // on ajoute dans le panneau dans celui muni d'ascenceurs
    JScrollPane ascenseurs2 = new JScrollPane(this.jLRegles);
    ascenseurs2.setBounds(10, 230, 400, 100);
    ascenseurs2
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.add(ascenseurs2);

  }

  public ZonePLU getZonePlu() {
    return this.zonePlu;
  }

  protected JComponent makeTextPanel(String text) {
    JPanel panel = new JPanel(false);

    return panel;
  }

  public String getNom() {
    return this.zonePlu.getNom();

  }

  public void setNom(String nom) {
    this.zonePlu.setNom(nom);

  }

  public void ajoutRegle() {
    this.zonePlu.getRegles().add(new Regle(OngletZone.compteurModif++));

    Object[] lObjets = this.zonePlu.getRegles().toArray();
    this.jLRegles.setListData(lObjets);
    this.setVisible(true);
  }

  public void ajoutRegle(Regle r) {

    OngletZone.compteurModif++;

    this.zonePlu.getRegles().add(r);
    this.jLRegles.setListData(this.zonePlu.getRegles().toArray());
    this.setVisible(true);
  }

  public void supprimeRegle() {
    int ind = this.jLRegles.getSelectedIndex();

    if (ind == -1) {
      return;
    }

    this.getZonePlu().getRegles().remove(ind);

    this.setVisible(true);

  }

  @Override
  public void setVisible(boolean bool) {
    this.jLRegles.setListData(this.getZonePlu().getRegles().toArray());
    super.setVisible(bool);
    this.jLRegles.setVisible(true);

  }

  private static class CustomListCellRenderer extends JPanel implements
      ListCellRenderer {

    /**
		 * 
		 */
    private static final long serialVersionUID = 1L;

    private static final Border NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);

    private final JTextArea contentArea;

    private CustomListCellRenderer() {

      this.contentArea = new JTextArea();
      this.contentArea.setLineWrap(true);
      this.contentArea.setWrapStyleWord(true);
      this.contentArea.setOpaque(true);
      this.contentArea.setBackground(Color.white);

      this.build();
    }

    private void build() {

      this.setLayout(new BorderLayout());
      this.add(this.contentArea, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
        int index, boolean isSelected, boolean cellHasFocus) {

      this.setBorder(cellHasFocus ? UIManager
          .getBorder("List.focusCellHighlightBorder")
          : CustomListCellRenderer.NO_FOCUS_BORDER);

      this.contentArea.setText(value.toString());
      return this;
    }

  }

}
