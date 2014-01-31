package fr.ign.cogit.gru3d.regleUrba;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.Messages;
import fr.ign.cogit.geoxygene.sig3d.gui.InterfaceMap3D;
import fr.ign.cogit.geoxygene.sig3d.gui.filter.XMLFilter;
import fr.ign.cogit.geoxygene.sig3d.representation.sample.ObjectCartoon;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.geoxygene.sig3d.util.ColorRandom;
import fr.ign.cogit.gru3d.regleUrba.propositionBuilding.PropositionCOS;
import fr.ign.cogit.gru3d.regleUrba.propositionBuilding.PropositionResidential;
import fr.ign.cogit.gru3d.regleUrba.propositionScenario.ProposeCOS;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.ihm.MainWindow;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.io.Chargement;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.PLU;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.Regle;
import fr.ign.cogit.gru3d.regleUrba.representation.Contrainte;
import fr.ign.cogit.gru3d.regleUrba.representation.Incoherence;
import fr.ign.cogit.gru3d.regleUrba.schemageo.Batiment;
import fr.ign.cogit.gru3d.regleUrba.schemageo.EnveloppeConstructible;
import fr.ign.cogit.gru3d.regleUrba.schemageo.Parcelle;
import fr.ign.cogit.gru3d.regleUrba.schemageo.Toit;

/**
 * La barre à ajouter pour que l'on ait les règles d'urbanismes
 * @author MBrasebin
 * 
 */
public class BarreReglesUrbanisme extends JMenu implements ActionListener {

  /**
     * 
     */
  private static final long serialVersionUID = 4116419071955303775L;

  private Environnement env = null;
  private List<Regle> lRegles = null;

  private InterfaceMap3D interCarte3D;

  private static int comptCalc = 0;

  private JMenuItem butDefinirRegle, butChargerFichierRegle, butVerifieRegle,
      butCalculVolum, butTreatAll, butProposeBuilding, butProposeScenario;

  public BarreReglesUrbanisme(InterfaceMap3D c) {

    super("Outil regle");

    this.interCarte3D = c;

    this.butDefinirRegle = new JMenuItem("Définir une règle");
    this.butDefinirRegle.addActionListener(this);
    this.add(this.butDefinirRegle);

    this.butChargerFichierRegle = new JMenuItem("Charger un fichier  de règle");
    this.butChargerFichierRegle.addActionListener(this);
    this.add(this.butChargerFichierRegle);

    this.butVerifieRegle = new JMenuItem("Vérifier les règles");
    this.butVerifieRegle.addActionListener(this);
    this.add(this.butVerifieRegle);

    this.butCalculVolum = new JMenuItem("Calculer volume");
    this.butCalculVolum.addActionListener(this);
    this.add(this.butCalculVolum);

    /*
     * this.butTreatAll = new JButton("Calcul la totale");
     * this.butTreatAll.addActionListener(this); this.add(this.butTreatAll);
     */

    this.butProposeBuilding = new JMenuItem("Proposer bâtiment");
    this.butProposeBuilding.addActionListener(this);
    this.add(this.butProposeBuilding);
    
    this.butProposeScenario = new JMenuItem("Proposer scénario");
    this.butProposeScenario.addActionListener(this);
    this.add(this.butProposeScenario);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub

    Object o = e.getSource();

    if (o.equals(this.butDefinirRegle)) {
      // On affiche l'éditeur de règles
      (new MainWindow(false)).setVisible(true);
      return;
    }

    if (o.equals(this.butChargerFichierRegle)) {
      // On charge un fichier et on modifie les règles
      // chargées dans l'environnement
      JFileChooser homeChooser = new JFileChooser(
          Messages.getString("")); //$NON-NLS-1$

      FileFilter filtre = new XMLFilter();

      homeChooser.setAcceptAllFileFilterUsed(false);

      // Un certain type de fichier est acceepté
      homeChooser.addChoosableFileFilter(filtre);

      homeChooser.showOpenDialog(null);

      File file = homeChooser.getSelectedFile();

      if (file == null) {
        return;
      }
      String nomfichier = file.getPath();

      PLU plu = Chargement.chargementFichier(nomfichier);

      if (plu.getZonePLU().size() > 0) {

        this.lRegles = plu.getZonePLU().get(0).getRegles();

      }

      return;
    }

    if (this.env == null) {

      JOptionPane.showMessageDialog(this,
          "Module de règles d'urbanisme", "Environnement non défini", //$NON-NLS-1$//$NON-NLS-2$ 
          JOptionPane.ERROR_MESSAGE);
      return;
    }

    if (this.lRegles == null) {

      JOptionPane.showMessageDialog(this,
          "Module de règles d'urbanisme", "Aucune règle chargée", //$NON-NLS-1$//$NON-NLS-2$ 
          JOptionPane.ERROR_MESSAGE);
      return;
    }

    // On calcule les solides pour les parcelles sélectionnées

    if (o.equals(this.butTreatAll)) {
      FT_FeatureCollection<Parcelle> lParcelles = this.env.getlParcelles();
      int nbParcelle = lParcelles.size();

      Moteur m = new Moteur(this.env, this.lRegles);

      for (int i = 0; i < nbParcelle; i++) {

        Parcelle p = lParcelles.get(i);

        List<FT_FeatureCollection<Contrainte>> collContraintes = m
            .computeConstraints(p);

        /*
         * if (Executor.VERBOSE) {
         * 
         * VectorLayer cv = new VectorLayer(collContraintes,
         * "Contraintes parcelle : " + i);
         * this.interCarte3D.getCurrent3DMap().addLayer(cv);
         * 
         * }
         */

        int nbLContraintes = collContraintes.size();

        for (int j = 0; j < nbLContraintes; j++) {

          IFeatureCollection<EnveloppeConstructible> featColl = m
              .computeGeomFromConstraints(collContraintes.get(j));

          if (featColl.size() != 0) {

            VectorLayer cv = new VectorLayer(featColl, "Enveloppe parcelle : "
                + i);
            this.interCarte3D.getCurrent3DMap().addLayer(cv);

          }

        }

        System.out.println(i + "/" + nbParcelle);

      }

      return;
    }

    // Une sélection est obligatoire pour déclencher ces actions

    if (this.interCarte3D.getSelection().size() == 0) {

      JOptionPane.showMessageDialog(this,
          "Module de règles d'urbanisme", "Aucune parcelle sélectionnée", //$NON-NLS-1$//$NON-NLS-2$ 
          JOptionPane.ERROR_MESSAGE);
      return;

    }

    Moteur m = new Moteur(this.env, this.lRegles);

    IFeatureCollection<IFeature> objetSelc = this.interCarte3D.getSelection();
    List<Parcelle> lParcelles = new ArrayList<Parcelle>();
    int nbElem = objetSelc.size();

    for (int i = 0; i < nbElem; i++) {

      IFeature feat = objetSelc.get(i);

      if (feat instanceof Parcelle) {

        lParcelles.add((Parcelle) feat);
        
        
        
        
        
      }

    }
    
    this.interCarte3D.setSelection(new FT_FeatureCollection<IFeature>());

    // On vérifie les règles pour les parcelles sélectionnées

    if (o.equals(this.butVerifieRegle)) {

      List<FT_FeatureCollection<Incoherence>> lIncoherence = m
          .processIsParcelOk(lParcelles);

      int nbParcelleIncoherence = lIncoherence.size();

      int nbParcelleM = 0;
      
      
      IFeatureCollection<IFeature> lIncoherenceFeat = new FT_FeatureCollection<IFeature>();
      
      
      for (int i = 0; i < nbParcelleIncoherence; i++) {

        if (lIncoherence.get(i).size() != 0) {
          lIncoherenceFeat.addAll(lIncoherence.get(i));

          nbParcelleM++;
        }

      }
      
      VectorLayer cv = new VectorLayer(lIncoherenceFeat, "Incoherences détectées" + (comptCalc++) ) ;
      this.interCarte3D.getCurrent3DMap().addLayer(cv);

      JOptionPane
          .showMessageDialog(
              this,
              "Module de règles d'urbanisme", nbParcelleM + " ne respecte les règles sélectionnées", //$NON-NLS-1$//$NON-NLS-2$ 
              JOptionPane.ERROR_MESSAGE);

      return;
    }

    // On calcule les solides pour les parcelles sélectionnées
    // Ne prend en compte que la première liste de contraintes
    if (o.equals(this.butCalculVolum)) {

      int nbParcelle = lParcelles.size();

      for (int i = 0; i < nbParcelle; i++) {

        Parcelle p = lParcelles.get(i);

        List<FT_FeatureCollection<Contrainte>> lCollContraintes = m
            .computeConstraints(p);

        if (lCollContraintes == null) {
          continue;
        }

        FT_FeatureCollection<Contrainte> collContraintes = lCollContraintes
            .get(0);

        if (Executor.VERBOSE) {

          VectorLayer cv = new VectorLayer(collContraintes,
              "Contraintes parcelle : " + i);
          this.interCarte3D.getCurrent3DMap().addLayer(cv);

        }

        IFeatureCollection<EnveloppeConstructible> featColl = m
            .computeGeomFromConstraints(collContraintes);

        if (featColl != null && featColl.size() != 0) {

          int nbElemC = featColl.size();

          for (int j = 0; j < nbElemC; j++) {
            IFeature feat = featColl.get(j);

            feat.setRepresentation(new ObjectCartoon(feat, ColorRandom
                .getRandomColor()));

          }

          VectorLayer cv = new VectorLayer(featColl, "Enveloppe parcelle : "
              + (BarreReglesUrbanisme.comptCalc++));
          this.interCarte3D.getCurrent3DMap().addLayer(cv);

        }

        System.out.println(i + "/" + nbParcelle);
      }

      return;
    }

    if (o.equals(butProposeBuilding)) {

      IFeatureCollection<Batiment> featB = new FT_FeatureCollection<Batiment>();
      IFeatureCollection<Toit> featR = new FT_FeatureCollection<Toit>();
      
      
      // On affiche les choix disponibles et on les récupère
      String[] options = {  "Optimisation COS",  "Optimisation CES",};

      // Propose de choisir entre les différentes applications
      Object obj = JOptionPane.showInputDialog(null,
          "Quelle stratégie de peuplement ?", "Choix de la stratégie",
          JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
      if (obj == null) {

        return;
      }

      int nbOptions = options.length;
      int i;

      for (i = 0; i < nbOptions; i++) {
        if (options[i].equals(obj.toString())) {
          break;
        }

      }


      for (Parcelle p : lParcelles) {
        
        
        
        
        
        

        // Batiment b = RandomProposition.proposition(p,m);
        // Batiment b = ParisProposition.proposition(p, m);
        Batiment b = null;
        if(i ==0){
          
          System.out.println("Je passe là");
          b = PropositionCOS.proposition(p, m);

        }else{
          b =   PropositionResidential.proposition(p, m);
        }
        
      

        if (b != null) {
          featB.add(b);

          b.setRepresentation(new ObjectCartoon(b, Color.red));

          featR.add(b.getToit());

          b.getToit().setRepresentation(
              new ObjectCartoon(b.getToit(), Color.yellow));

        }

      }

      VectorLayer cv = new VectorLayer(featB, "Batiments "
          + (BarreReglesUrbanisme.comptCalc++));

      VectorLayer cv2 = new VectorLayer(featR, "Toits  "
          + (BarreReglesUrbanisme.comptCalc++));

      this.interCarte3D.getCurrent3DMap().addLayer(cv);
      this.interCarte3D.getCurrent3DMap().addLayer(cv2);
    }
    
    
    if(o.equals(butProposeScenario)){
      
      ProposeCOS.optimizeCOS(m, 0, 10, 5, 1, 3, 1, 5);    
      
    }

  }

  public void setEnvironnement(Environnement env) {

    this.env = env;
  }

  public void setRegles(List<Regle> lRegles) {
    this.lRegles = lRegles;
  }

}
