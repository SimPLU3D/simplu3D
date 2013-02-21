package fr.ign.cogit.gui.actionPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.ign.cogit.generation.BatimentProcedural;
import fr.ign.cogit.generation.TopologieBatiment;
import fr.ign.cogit.generation.TopologieBatiment.FormeEmpriseEnum;
import fr.ign.cogit.generation.TopologieBatiment.FormeToitEnum;
import fr.ign.cogit.model.application.Materiau;
import fr.ign.cogit.representation.theme.RepresentationBatiment;
import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.Messages;
import fr.ign.cogit.geoxygene.sig3d.gui.InterfaceMap3D;

public class ButtonActionPanel extends JPanel implements ActionListener {

  /**
   * 
   */
  private static final long serialVersionUID = 8021783217076303874L;

  private InterfaceMap3D iMap3D;

  private JButton moveNorth, moveSouth, moveEast, moveWest;

  private JButton turnRight, turnLeft;

  private JButton changeFormeE, changeFormeT;

  private JButton zGoutUp, zGoutDown;
  private JButton zMaxUp, zMaxDown;

  private JButton hUp, hDown;
  private JButton h2Up, h2Down;

  private JButton lUp, lDown;
  private JButton l2Up, l2Down;

  private JButton textureMur, textureToit;

  private JButton changePignon;

  private JButton angleTUP, angleTDOWN;

  public ButtonActionPanel(InterfaceMap3D iMap3D) {
    super();

    this.iMap3D = iMap3D;

    this.setLayout(null);
    this.setBorder(new TitledBorder(new EtchedBorder(), Messages
        .getString("Action à porter sur le bâtiment")));

    moveNorth = new JButton("N");
    moveNorth.setBounds(100, 20, 45, 45);
    moveNorth.addActionListener(this);
    this.add(moveNorth);

    moveWest = new JButton("O");
    moveWest.setBounds(75, 70, 45, 45);
    moveWest.addActionListener(this);
    this.add(moveWest);

    moveEast = new JButton("E");
    moveEast.setBounds(125, 70, 45, 45);
    moveEast.addActionListener(this);
    this.add(moveEast);

    moveSouth = new JButton("S");
    moveSouth.setBounds(100, 120, 45, 45);
    moveSouth.addActionListener(this);
    this.add(moveSouth);

    turnLeft = new JButton("<-");
    turnLeft.setBounds(66, 200, 45, 45);
    turnLeft.addActionListener(this);
    this.add(turnLeft);

    turnRight = new JButton("->");
    turnRight.setBounds(137, 200, 45, 45);
    turnRight.addActionListener(this);
    this.add(turnRight);

    changeFormeE = new JButton("Change forme emprise");
    changeFormeE.setBounds(137, 300, 200, 30);
    changeFormeE.addActionListener(this);
    this.add(changeFormeE);

    changeFormeT = new JButton("Change forme toit");
    changeFormeT.setBounds(137, 350, 200, 30);
    changeFormeT.addActionListener(this);
    this.add(changeFormeT);

    zGoutUp = new JButton("Gout haut");
    zGoutUp.setBounds(56, 400, 100, 30);
    zGoutUp.addActionListener(this);
    this.add(zGoutUp);

    zGoutDown = new JButton("Gout bas");
    zGoutDown.setBounds(147, 400, 100, 30);
    zGoutDown.addActionListener(this);
    this.add(zGoutDown);

    zMaxUp = new JButton("ZMax haut");
    zMaxUp.setBounds(56, 450, 100, 30);
    zMaxUp.addActionListener(this);
    this.add(zMaxUp);

    zMaxDown = new JButton("ZMax bas");
    zMaxDown.setBounds(147, 450, 100, 30);
    zMaxDown.addActionListener(this);
    this.add(zMaxDown);

    hUp = new JButton("h haut");
    hUp.setBounds(0, 500, 100, 30);
    hUp.addActionListener(this);
    this.add(hUp);

    hDown = new JButton("h bas");
    hDown.setBounds(100, 500, 100, 30);
    hDown.addActionListener(this);
    this.add(hDown);

    h2Up = new JButton("h2 haut");
    h2Up.setBounds(200, 500, 100, 30);
    h2Up.addActionListener(this);
    this.add(h2Up);

    h2Down = new JButton("h2 bas");
    h2Down.setBounds(300, 500, 100, 30);
    h2Down.addActionListener(this);
    this.add(h2Down);

    lUp = new JButton("l haut");
    lUp.setBounds(0, 550, 100, 30);
    lUp.addActionListener(this);
    this.add(lUp);

    lDown = new JButton("l bas");
    lDown.setBounds(100, 550, 100, 30);
    lDown.addActionListener(this);
    this.add(lDown);

    l2Up = new JButton("l2 haut");
    l2Up.setBounds(200, 550, 100, 30);
    l2Up.addActionListener(this);
    this.add(l2Up);

    l2Down = new JButton("l2 bas");
    l2Down.setBounds(300, 550, 100, 30);
    l2Down.addActionListener(this);
    this.add(l2Down);

    textureMur = new JButton("Texture mur");
    textureMur.setBounds(50, 600, 100, 30);
    textureMur.addActionListener(this);
    this.add(textureMur);

    textureToit = new JButton("Texture toit");
    textureToit.setBounds(150, 600, 100, 30);
    textureToit.addActionListener(this);
    this.add(textureToit);

    changePignon = new JButton("Texture toit");
    changePignon.setBounds(150, 650, 100, 30);
    changePignon.addActionListener(this);
    this.add(changePignon);

    angleTUP = new JButton("AT plus");
    angleTUP.setBounds(56, 700, 100, 30);
    angleTUP.addActionListener(this);
    this.add(angleTUP);

    angleTDOWN = new JButton("AT moins");
    angleTDOWN.setBounds(147, 700, 100, 30);
    angleTDOWN.addActionListener(this);
    this.add(angleTDOWN);

    this.setSize(440, 750);

  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub

    Object source = e.getSource();

    IFeatureCollection<IFeature> featColl = iMap3D.getSelection();
    List<BatimentProcedural> bPList = new ArrayList<BatimentProcedural>();

    for (IFeature feat : featColl) {
      if (feat instanceof BatimentProcedural) {
        bPList.add((BatimentProcedural) feat);

      }

    }

    if (bPList.size() == 0) {
      System.out.println("No selection");
      return;
    }

    for (BatimentProcedural bP : bPList) {

      if (source == moveNorth) {

        bP.translate(0, 500);

      }

      if (source == moveEast) {

        bP.translate(500, 0);

      }

      if (source == moveWest) {
        bP.translate(-500, 0);
      }

      if (source == moveSouth) {
        bP.translate(0, -500);
      }

      if (source == turnLeft) {
        bP.rotate(10.0);
      }

      if (source == turnRight) {
        bP.rotate(-10.0);
      }

      if (source == changeFormeE) {
        FormeEmpriseEnum[] fEE = TopologieBatiment.FormeEmpriseEnum.values();
        int nbElem = fEE.length;
        int rand = (int) (Math.random() * nbElem);

        FormeEmpriseEnum fEENew = fEE[rand];

        if (fEENew != bP.gettB().getfE()) {
          bP.changeFormeEmprise(fEENew);
        }

      }

      if (source == changeFormeT) {
        FormeToitEnum[] fEE = TopologieBatiment.FormeToitEnum.values();
        int nbElem = fEE.length;
        int rand = (int) (Math.random() * nbElem);

        FormeToitEnum fEENew = fEE[rand];

        if (fEENew != bP.gettB().getfT()) {
          bP.changeFormeToit(fEENew);
        }

      }

      if (source == zGoutUp) {
        bP.moveGouttiere(bP.getzGouttiere() + 2);
      }

      if (source == zGoutDown) {
        bP.moveGouttiere(bP.getzGouttiere() - 2);
      }

      if (source == zMaxUp) {
        bP.moveZMax(bP.getzMax() + 2);
      }

      if (source == zMaxDown) {
        bP.moveZMax(bP.getzMax() - 2);
      }

      if (source == hUp) {
        bP.changeHauteur(bP.getHauteur() + 1);
      }

      if (source == hDown) {
        bP.changeHauteur(bP.getHauteur() - 1);
      }

      if (source == lUp) {
        bP.changeLargeur(bP.getLargeur() + 1);
      }

      if (source == lDown) {
        bP.changeLargeur(bP.getLargeur() - 1);
      }

      if (source == h2Up) {
        bP.changeHauteur2(bP.getHauteur2() + 1);
      }

      if (source == h2Down) {
        bP.changeHauteur2(bP.getHauteur2() - 1);
      }

      if (source == l2Up) {
        bP.changeLargeur2(bP.getLargeur2() + 1);
      }

      if (source == l2Down) {
        bP.changeLargeur2(bP.getLargeur2() - 1);
      }

      if (source == textureToit) {

        Materiau[] mat = Materiau.values();
        int ind = (int) (Math.random() * mat.length);

        bP.changeTextureToit(mat[ind]);

      }

      if (source == textureMur) {

        Materiau[] mat = Materiau.values();
        int ind = (int) (Math.random() * mat.length);

        int indFacade = (int) (Math.random() * bP.getFacade().size());

        bP.changeTextureFacade(mat[ind], indFacade);

      }

      if (source == changePignon) {

        int nbPoss = 0;

        if (bP.gettB().getfT().equals(FormeToitEnum.EN_APPENTIS)) {

          nbPoss = bP.gettB().getfE().getPossibleArcAppentis();

        } else if (bP.gettB().getfT().equals(FormeToitEnum.SYMETRIQUE)) {

          nbPoss = bP.gettB().getfE().getPossibleArcSymetrique();
        } else {
          return;
        }

        List<Integer> lIID = new ArrayList<Integer>();
        for (int i = 0; i < nbPoss; i++) {
          lIID.add(i);
        }

        int nbConserved = (int) (Math.random() * nbPoss);
        for (int i = 0; i < (nbPoss - nbConserved); i++) {

          lIID.remove((int) (Math.random() * lIID.size()));

        }

        bP.changePignon(lIID);

      }

      
      if(source == angleTUP){
        
        bP.changeAngleToit(bP.getAngleToit() + 0.1);
        
        
      }
      
      
      if(source == angleTDOWN){
        
        bP.changeAngleToit(bP.getAngleToit() - 0.1);
        
        
      }
      
      
      RepresentationBatiment pBIni = (RepresentationBatiment) bP
          .getRepresentation();

      bP.setRepresentation(new RepresentationBatiment(bP, pBIni
          .isRepresentFaitage(), pBIni.isRepresentGouttiere()));

    }

    this.iMap3D.getCurrent3DMap().getLayerList().get(0).refresh();

  }

}
