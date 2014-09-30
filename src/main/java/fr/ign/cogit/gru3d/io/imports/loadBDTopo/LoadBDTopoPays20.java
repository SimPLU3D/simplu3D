package fr.ign.cogit.gru3d.io.imports.loadBDTopo;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.feature.DataSet;
import fr.ign.cogit.geoxygene.schema.Produit;
import fr.ign.cogit.geoxygene.schema.schemaConceptuelISOJeu.SchemaConceptuelJeu;
import fr.ign.cogit.geoxygene.schema.schemaConceptuelISOProduit.SchemaConceptuelProduit;
import fr.ign.cogit.geoxygene.sig3d.semantic.DTM;
import fr.ign.cogit.geoxygene.sig3d.semantic.Layer;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses.LoadAireTriage;
import fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses.LoadBatiIndifferencie;
import fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses.LoadBatiIndustriel;
import fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses.LoadBatiRemarquable;
import fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses.LoadCanalisationEau;
import fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses.LoadCimetiere;
import fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses.LoadConduite;
import fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses.LoadConstructionLineaire;
import fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses.LoadConstructionPonctuelle;
import fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses.LoadCoursDEau;
import fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses.LoadGare;
import fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses.LoadLigneElectrique;
import fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses.LoadLigneOrographique;
import fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses.LoadPosteTransformation;
import fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses.LoadPylone;
import fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses.LoadReservoir;
import fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses.LoadReservoirDEau;
import fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses.LoadRoute;
import fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses.LoadSurfaceEau;
import fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses.LoadSurfaceRoute;
import fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses.LoadTerrainSport;
import fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses.LoadTronconVoieFerree;
import fr.ign.cogit.gru3d.io.imports.loadBDTopo.bdTopoClasses.LoadZoneVegetation;

public class LoadBDTopoPays20 {
  
  public static Logger LOGGER = Logger.getLogger(LoadBDTopoPays20.class); 

  private SchemaConceptuelJeu monschemabdt;
  private List<Layer> lLayers = new ArrayList<Layer>();
  private DataSet dataset;

  /**
   * Classe permettant de charger des fichiers de BD topo depuis un dossier et
   * de générer leur géométrie et représentation 3D
   * 
   * @param path chemin du dossier
   * @param mnt le MNTà utiliser
   */
  public LoadBDTopoPays20(String path, DTM mnt) {
    try {
      this.load(path, mnt);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void load(String path, DTM mnt) throws Exception {
    // Creation des schemas de donnees: schema de produit et schema de jeu
    SchemaConceptuelProduit schemaProduit = SchemaBDTopoPays20
        .creeSchemaBDTopoPays20();
    this.monschemabdt = new SchemaConceptuelJeu(schemaProduit);

    // Creation du produit correspondant au schéma de produit BDTOPO (facultatif
    // à partir de là…)
    Produit p = new Produit();
    p.setId(1);
    p.setNom("BD TOPO version 2.0");
    p.setProducteur("Institut Géographique National");
    p.setSchemaConceptuel(schemaProduit);
    p.setType(1);
    p.setEchelleMin(0.00004);
    p.setEchelleMax(0.0001);

    // Creation des dataset
    this.dataset = new DataSet();
    this.dataset.setAppartientA(null);
    this.dataset.setDate("Février 2008");
    this.dataset.setId(1);
    this.dataset.setNom("Jeu de données test BD TOPO");
    this.dataset.setPersistant(true);
    this.dataset.setProduit(p);
    this.dataset.setSchemaConceptuel(this.monschemabdt);
    this.dataset.setTypeBD("BD TOPO 2.0");
    this.dataset.setModele("Structuré");
    this.monschemabdt.setDataset(this.dataset);

    // On récupère les fichiers dans le répertoire
    File dir = new File(path);

    File[] fileL = dir.listFiles();

    int nbF = fileL.length;

    for (int i = 0; i < nbF; i++) {

      // Pour chaque fichier on récupère son nom
      File fTemp = fileL[i];
      String nomF = fTemp.getName();

      // Ce n'est pas un shape, on continue
      if (!nomF.contains(".shp")) {
        continue;
      }

      // On traite le fichier en fonction de son nom
      // On affecte à la collection
      IPopulation<IFeature> lFeat = null;

      Layer l = null;

      // ROUTE
      if (nomF.equals("route.shp") || nomF.contains("troncon_route") || nomF.contains("troncon_chemin")) {

        lFeat = ShapefileReader.read(fTemp.getAbsolutePath(), "ROUTE",
            this.dataset, false);
        l = LoadRoute.load(lFeat, mnt);

      }

      // SURFACE ROUTE
      if (nomF.equals("surface_route.shp")) {

        lFeat = ShapefileReader.read(fTemp.getAbsolutePath(), "SURFACE_ROUTE",
            this.dataset, false);
        l = LoadSurfaceRoute.load(lFeat, mnt);
      }

      // BATI_INDIFFERENCIE
      if (nomF.equals("bati_indifferencie.shp")) {
        lFeat = ShapefileReader.read(fTemp.getAbsolutePath(),
            "BATI_INDIFFERENCIE", this.dataset, false);
        l = LoadBatiIndifferencie.load(lFeat, mnt);
      }

      // BATI_REMARQUABLE
      if (nomF.equals("bati_remarquable.shp")) {
        lFeat = ShapefileReader.read(fTemp.getAbsolutePath(),
            "BATI_REMARQUABLE", this.dataset, false);
        l = LoadBatiRemarquable.load(lFeat, mnt);
      }

      // BATI_INDUSTRIEL
      if (nomF.equals("bati_industriel.shp")) {
        lFeat = ShapefileReader.read(fTemp.getAbsolutePath(),
            "BATI_INDUSTRIEL", this.dataset, false);
        l = LoadBatiIndustriel.load(lFeat, mnt);
      }

      // CIMETIERE
      if (nomF.equals("cimetiere.shp") || nomF.contains("cimetiere")) {
        lFeat = ShapefileReader.read(fTemp.getAbsolutePath(), "CIMETIERE",
            this.dataset, false);
        l = LoadCimetiere.load(lFeat, mnt);
      }

      // TERRAIN SPORT
      if (nomF.equals("terrain_sport.shp") || nomF.contains("terrain_sport")) {

        lFeat = ShapefileReader.read(fTemp.getAbsolutePath(), "TERRAIN_SPORT",
            this.dataset, false);
        l = LoadTerrainSport.load(lFeat, mnt);

      }

      // RESERVOIR
      if (nomF.equals("reservoir.shp")) {
        lFeat = ShapefileReader.read(fTemp.getAbsolutePath(), "RESERVOIR",
            this.dataset, false);
        l = LoadReservoir.load(lFeat, mnt);
      }

      // CONSTRUCTION_LINEAIRE
      if (nomF.equals("construction_lineaire.shp")) {
        lFeat = ShapefileReader.read(fTemp.getAbsolutePath(),
            "CONSTRUCTION_LINEAIRE", this.dataset, false);
        l = LoadConstructionLineaire.load(lFeat, mnt);

      }

      // CONSTRUCTION_PONCTUELLE
      if (nomF.equals("construction_ponctuelle.shp")) {
        lFeat = ShapefileReader.read(fTemp.getAbsolutePath(),
            "CONSTRUCTION_PONCTUELLE", this.dataset, false);
        l = LoadConstructionPonctuelle.load(lFeat, mnt);
      }

      // CONSTRUCTION_SURFACIQUE
      if (nomF.equals("construction_surfacique.shp")) {
        lFeat = ShapefileReader.read(fTemp.getAbsolutePath(),
            "CONSTRUCTION_SURFACIQUE", this.dataset, false);
        new VectorLayer(lFeat, "CONSTRUCTION_SURFACIQUE", Color.orange);

      }

      if (nomF.equals("aire_triage.shp")) {
        lFeat = ShapefileReader.read(fTemp.getAbsolutePath());
        l = LoadAireTriage.load(lFeat, mnt);

      }

      if (nomF.equals("gare.shp")) {
        lFeat = ShapefileReader.read(fTemp.getAbsolutePath());
        l = LoadGare.load(lFeat, mnt);

      }

      if (nomF.equals("troncon_voie_ferree.shp") || nomF.contains("troncon_voie_ferree")) {
        lFeat = ShapefileReader.read(fTemp.getAbsolutePath());
        l = LoadTronconVoieFerree.load(lFeat, mnt);

      }

      // Conduite
      if (nomF.equals("conduite.shp")) {
        lFeat = ShapefileReader.read(fTemp.getAbsolutePath());
        l = LoadConduite.load(lFeat, mnt);

      }

      if (nomF.equals("ligne_electrique.shp")) {
        lFeat = ShapefileReader.read(fTemp.getAbsolutePath());
        l = LoadLigneElectrique.load(lFeat, mnt);

      }

      if (nomF.equals("poste_transformation.shp")) {
        lFeat = ShapefileReader.read(fTemp.getAbsolutePath());
        l = LoadPosteTransformation.load(lFeat, mnt);

      }

      if (nomF.equals("pylone.shp")) {
        lFeat = ShapefileReader.read(fTemp.getAbsolutePath());
        l = LoadPylone.load(lFeat, mnt);

      }

      if (nomF.equals("reservoir_eau.shp")) {
        lFeat = ShapefileReader.read(fTemp.getAbsolutePath());
        l = LoadReservoirDEau.load(lFeat, mnt);
      }

      // surface_eau.shp
      if (nomF.equals("surface_eau.shp")) {
        lFeat = ShapefileReader.read(fTemp.getAbsolutePath());
        l = LoadSurfaceEau.load(lFeat, mnt);

      }

      if (nomF.equals("canalisation_eau.shp")) {
        lFeat = ShapefileReader.read(fTemp.getAbsolutePath());
        l = LoadCanalisationEau.load(lFeat, mnt);

      }

      // troncon_cours_eau.shp
      if (nomF.equals("troncon_cours_eau.shp") || nomF.contains("troncon_cours_eau")) {
        lFeat = ShapefileReader.read(fTemp.getAbsolutePath());
        l = LoadCoursDEau.load(lFeat, mnt);

      }

      if (nomF.equals("ligne_orographique.shp")) {
        lFeat = ShapefileReader.read(fTemp.getAbsolutePath());
        l = LoadLigneOrographique.load(lFeat, mnt);

      }

      if (nomF.equals("zone_vegetation.shp") || nomF.contains("vegetation")) {
        lFeat = ShapefileReader.read(fTemp.getAbsolutePath());
        l = LoadZoneVegetation.load(lFeat, mnt);

      }

      if (l != null) {

        this.dataset.addPopulation(lFeat);
        this.lLayers.add(l);

      } else {

        LOGGER.warn("Nom fichier BDTopo inconnue : " + nomF);

      }

    }

  }

  /**
   * @return the monschemabdt
   */
  public SchemaConceptuelJeu getMonschemabdt() {
    return this.monschemabdt;
  }

  /**
   * @return the lLayers
   */
  public List<Layer> getlLayers() {
    return this.lLayers;
  }

  /**
   * @return the dataset
   */
  public DataSet getDataset() {
    return this.dataset;
  }

}
