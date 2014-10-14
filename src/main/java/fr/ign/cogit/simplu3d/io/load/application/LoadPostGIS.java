package fr.ign.cogit.simplu3d.io.load.application;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.io.vector.PostgisManager;
import fr.ign.cogit.simplu3d.model.application.Environnement;

public class LoadPostGIS {

  /*
   * Nom des fichiers en entrée
   */
  public final static String NOM_TABLE_ZONAGE = "zonage";
  public final static String NOM_TABLE_PARCELLE = "parcelle";
  public final static String NOM_TABLE_VOIRIE = "route";
  public final static String NOM_TABLE_BATIMENTS = "bati";
  public final static String NOM_TABLE_PRESC_LINEAIRE = "prescription_lin";

  public final static String NOM_TABLE_TERRAIN = "mnt";

  String host = "";
  String port = "";
  String database = "";
  String user = "";
  String pw = "";

  public LoadPostGIS(String host, String port, String database, String user,
      String pw) {
    super();
    this.host = host;
    this.port = port;
    this.database = database;
    this.user = user;
    this.pw = pw;
  }

  public Environnement loadNoOCLRules() throws Exception {
    return load(null);
  }

  public Environnement load(String folder) throws Exception {
    Environnement env = Environnement.getInstance();
    env.folder = folder;

    IFeatureCollection<IFeature> zoneColl = PostgisManager.loadGeometricTable(
        host, port, database, user, pw, NOM_TABLE_ZONAGE);
    IFeatureCollection<IFeature> parcelleColl = PostgisManager
        .loadGeometricTable(host, port, database, user, pw, NOM_TABLE_PARCELLE);
    IFeatureCollection<IFeature> voirieColl = PostgisManager
        .loadGeometricTable(host, port, database, user, pw, NOM_TABLE_VOIRIE);
    IFeatureCollection<IFeature> batiColl = PostgisManager.loadGeometricTable(
        host, port, database, user, pw, NOM_TABLE_BATIMENTS);
    IFeatureCollection<IFeature> prescriptions = PostgisManager
        .loadGeometricTable(host, port, database, user, pw,
            NOM_TABLE_PRESC_LINEAIRE);

    DTMPostGISNoJava3D dtm = new DTMPostGISNoJava3D(host, port, database,
        NOM_TABLE_TERRAIN, user,pw);

    return LoadFromCollection.load(zoneColl, parcelleColl, voirieColl,
        batiColl, prescriptions, folder, dtm);

  }
}
