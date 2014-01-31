package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.schemageo;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.media.j3d.TriangleArray;

import com.sun.j3d.utils.geometry.GeometryInfo;

import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.Constant;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.SchemaDefaultFeature;
import fr.ign.cogit.geoxygene.schema.schemaConceptuelISOJeu.AttributeType;
import fr.ign.cogit.geoxygene.schema.schemaConceptuelISOJeu.FeatureType;
import fr.ign.cogit.geoxygene.sig3d.convert.java3d.ConversionJava3DGeOxygene;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.sig3d.representation.basic.Object2d;
import fr.ign.cogit.geoxygene.sig3d.representation.sample.ObjectCartoon;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;

/**
 * Parcelle pour le projet de contraintes de règles d'urbanisme
 * 
 * @author MBrasebin
 */
public class Parcelle extends DefaultFeature {

  private List<Route> lRouteBordante = new ArrayList<Route>();

  private List<Parcelle> lParcelleBordante = new ArrayList<Parcelle>();

  private List<Batiment> lBatimentsContenus = new ArrayList<Batiment>();
  
  private List<EnveloppeConstructible> lEnvelopeConstructibles = new ArrayList<EnveloppeConstructible>();


  private ZonePLUGeo zonePLU = null;

  public Parcelle(DefaultFeature feat) {
    super(feat.getGeom());

    this.setFeatureType(feat.getFeatureType());
    this.setSchema((feat).getSchema());
    this.setAttributes((feat).getAttributes());
    
    
    this.setRepresentation(new ObjectCartoon(this, Color.white, Color.black));

  //  this.setRepresentation(new Object2d(this, true, new Color((int) (Math
  //      .random() * 255), (int) (Math.random() * 255),
  //      (int) (Math.random() * 255)), 1, true));
  }

  public ZonePLUGeo getZonePLU() {
    return this.zonePLU;
  }

  public void setZonePLU(ZonePLUGeo zonePLU) {
    this.zonePLU = zonePLU;
  }

  public List<Batiment> getlBatimentsContenus() {
    return this.lBatimentsContenus;
  }

  public void setlBatimentsContenus(List<Batiment> lBatimentsContenus) {
    this.lBatimentsContenus = lBatimentsContenus;
  }

  private double cos = -1;

  private double ces = -1;

  public void setZ(double z) {
    IDirectPositionList dpl = this.geom.coord();
    int nbPoints = dpl.size();

    for (int i = 0; i < nbPoints; i++) {

      dpl.get(i).setZ(z);
    }

    this.setRepresentation(new ObjectCartoon(this, Color.white, Color.black));
  }

  public List<Route> getlRouteBordante() {
    return this.lRouteBordante;
  }

  public void setlRouteBordante(List<Route> lRouteBordante) {
    this.lRouteBordante = lRouteBordante;
  }

  public List<Parcelle> getlParcelleBordante() {
    return this.lParcelleBordante;
  }

  public void setlParcelleBordante(List<Parcelle> lParcelleBordante) {
    this.lParcelleBordante = lParcelleBordante;
  }

  public double getCos() {
    if (this.cos != -1) {

      return this.cos;
    }
    return this.assessFAR(Constant.COEFF_COS);
  }

  public void setCos(double cos) {
    this.cos = cos;
  }

  public double getCes() {
    
    if(ces == -1){
      ces = assesCES();
    }
    return this.ces;
  }

  public void setCes(double ces) {
    this.ces = ces;
  }
  
  
  private  double assesCES(){
    
    double ces = 0;
    
    double area = this.getGeom().area();
    
    double areaB = 0;
    
    for(Batiment b: this.getlBatimentsContenus()){
      
      
      
      areaB = areaB + b.getToit().getGeom().area();
      
      
      
    }
    
    
    ces = areaB/area;
    
    
    
    
    return ces;
  }
  
  

  private final static String NOM_ATT_BUILT_VOLUME = "VConstruit";
  private final static String NOM_ATT_COS = "COS";

  /**
   * Calcule le volume bati d'une parcelle Ajoute l'information dans ses
   * attributs
   * 
   * @return le volume bati d'une parcelle
   */
  public double calculateBuiltVolume() {
    // On initialize la valeur total des volumes batis
    double volume = 0.0;

    List<Batiment> lBatiments = this.getlBatimentsContenus();

    int nbBatiment = lBatiments.size();

    // On parcourt chaque batiment
    for (int j = 0; j < nbBatiment; j++) {

      Batiment b = lBatiments.get(j);
      // On récupère la hauteur minimale du batiment
      double zMinBati = (new Box3D(b.getGeom())).getLLDP().getZ();

      // On récupère le toit
      Toit t = b.getToit();

      // Pas de toit ??? Problème de données
      if (t == null || t.getGeom().isEmpty()) {

        System.out.println("Batiment sans toit - Erreur");
        continue;
      }

      // On récupère la géométrie du toit
      IGeometry geom = t.getGeom();

      List<IOrientableSurface> lSurf = new ArrayList<IOrientableSurface>();

      if (geom instanceof GM_Polygon) {
        // Si c'est un polygone on calcule le volume se trouvant sous le
        // toit
        GM_Polygon poly = (GM_Polygon) geom;
        lSurf.add(poly);

      } else if (geom instanceof GM_MultiSurface<?>) {

        // Multi polygone on prend en compte toutes les surfaces
        GM_MultiSurface<?> multi_s = (GM_MultiSurface<?>) geom;
        lSurf.addAll(multi_s.getList());

      } else {
        // Un toit peut il être un autre type de géométrie
        System.out.println("Géométrie inconnue");
        continue;
      }

      // On triangule la géométrie grâce à J3D
      GeometryInfo g = ConversionJava3DGeOxygene
          .fromOrientableSToTriangleArray(lSurf);

      // On récupère le tableau de triangles
      TriangleArray ta = (TriangleArray) g.getGeometryArray();

      int nbVertex = ta.getVertexCount();
      // On traite chaque triangle
      for (int k = 0; k < nbVertex; k = k + 3) {
        // On récupère les coordonnées des 3 sommets
        double[] cd1 = new double[3];
        double[] cd2 = new double[3];
        double[] cd3 = new double[3];

        ta.getCoordinate(k, cd1);
        ta.getCoordinate(k + 1, cd2);
        ta.getCoordinate(k + 2, cd3);
        // On calcule le prisme se trouvant sous le triangle
        double vContrib = Math.abs(fr.ign.cogit.geoxygene.sig3d.calculation.Util
            .volumeUnderTriangle(cd1[0], cd1[1], cd1[2] - zMinBati,

            cd2[0], cd2[1], cd2[2] - zMinBati, cd3[0], cd3[1], cd3[2]
                - zMinBati));
        // On met à jour la contribution pour le volume
        volume = volume + vContrib;
      }

    }

    // On crée si nécessaire le FT_Type
    FeatureType ftType =(FeatureType) this.getFeatureType();

    if (ftType == null) {

      ftType = new FeatureType();
      ftType.setSchema(new SchemaDefaultFeature());
      this.setFeatureType(ftType);
      this.setSchema((SchemaDefaultFeature) ftType.getSchema());

      this.setAttributes(new Object[1]);

    }
    // On le complète éventuellement
    if (ftType.getFeatureAttributeByName(Parcelle.NOM_ATT_BUILT_VOLUME) == null) {

      AttributeType aT = new AttributeType();

      aT.setMemberName(Parcelle.NOM_ATT_BUILT_VOLUME);
      aT.setNomField(Parcelle.NOM_ATT_BUILT_VOLUME);
      aT.setValueType("Double");

      ftType.addFeatureAttribute(aT);

      SchemaDefaultFeature sft = (SchemaDefaultFeature) ftType.getSchema();

      if (!sft.getColonnes().contains(Parcelle.NOM_ATT_BUILT_VOLUME)) {
        System.out.println("sdfsd");
        sft.getColonnes().add(Parcelle.NOM_ATT_BUILT_VOLUME);
        sft.getAttLookup().put(ftType.getFeatureAttributes().size() - 1,
            new String[] { aT.getNomField(), aT.getMemberName() });

        sft.getColonnes().add(Parcelle.NOM_ATT_BUILT_VOLUME);

      }

      /*
       * List<String> lCol = new ArrayList<String>(); lCol.add("Distance");
       * SchemaDefaultFeature sft = new SchemaDefaultFeature();
       * sft.addFeatureType(featType); sft.setColonnes(lCol);
       * sft.setAttLookup(attLookup); feat.setSchema(sft);
       * feat.setAttributes(new Object[1]); Map<Integer, String[]> attLookup =
       * new HashMap<Integer, String[]>(); attLookup.put(0, new String[] {
       * aT.getNomField(), aT.getMemberName() }); List<String> lCol = new
       * ArrayList<String>(); lCol.add(NOM_ATT_BUILT_VOLUME);
       */

    }

    Object[] att = this.getAttributes();
    att = Arrays.copyOf(att, att.length + 1);
    this.setAttributes(att);

    this.setAttribute(Parcelle.NOM_ATT_BUILT_VOLUME, (volume));

    // On renvoie le resultat
    return volume;

  }

  /**
   * Calcule le COS d'un batiment
   * 
   * @param coefficient à appliquer pour obtenir le SHON pour chaque étage
   * @return le COS du batiment
   */
  public double assessFAR(double coefficient) {

    double aireParcelle = this.getGeom().area();
    double aireBatie = 0;
    // On parcourt la liste des batiments
    List<Batiment> lBatiments = this.getlBatimentsContenus();

    int nbBatiments = lBatiments.size();

    for (int i = 0; i < nbBatiments; i++) {
      Batiment bati = lBatiments.get(i);

      Box3D b = new Box3D(bati.getGeom());
      
      Toit t = bati.getToit();

      if (t == null) {

        System.out.println("ERROR COS : rajouter cas ou pas de toit");

        continue;
      }
      
      Box3D b2 = new Box3D(t.getGeom());
     

      double hauteur = b2.getLLDP().getZ() - b.getLLDP().getZ();

      int nbEtage =1 + (int) (hauteur / Constant.HAUTEUR_ETAGE);

      if (nbEtage == 0) {
        nbEtage++;
      }



      aireBatie = aireBatie + t.getGeom().area() * nbEtage;

    }

    double COSactuel = (Constant.COEFF_COS * aireBatie) / aireParcelle;
    this.setCos(COSactuel);

    // On crée si nécessaire le FT_Type
    FeatureType ftType = (FeatureType) this.getFeatureType();

    if (ftType == null) {

      ftType = new FeatureType();
      ftType.setSchema(new SchemaDefaultFeature());
      this.setFeatureType(ftType);
      this.setSchema((SchemaDefaultFeature) ftType.getSchema());

      this.setAttributes(new Object[1]);

    }
    // On le complète éventuellement
    if (ftType.getFeatureAttributeByName(Parcelle.NOM_ATT_COS) == null) {

      AttributeType aT = new AttributeType();

      aT.setMemberName(Parcelle.NOM_ATT_COS);
      aT.setNomField(Parcelle.NOM_ATT_COS);
      aT.setValueType("Double");

      ftType.addFeatureAttribute(aT);

      SchemaDefaultFeature sft = (SchemaDefaultFeature) ftType.getSchema();

      if (!sft.getColonnes().contains(Parcelle.NOM_ATT_COS)) {
        sft.getColonnes().add(Parcelle.NOM_ATT_COS);
        sft.getAttLookup().put(ftType.getFeatureAttributes().size() - 1,
            new String[] { aT.getNomField(), aT.getMemberName() });
        sft.getColonnes().add(Parcelle.NOM_ATT_COS);

      }

    }

    Object[] att = this.getAttributes();
    att = Arrays.copyOf(att, att.length + 1);
    this.setAttributes(att);

    this.setAttribute(Parcelle.NOM_ATT_COS, (COSactuel));
    return COSactuel;
  }
  
  
  
  
  

  public List<EnveloppeConstructible> getlEnveloppeContenues() {
    return lEnvelopeConstructibles;
  }

  public void setlEnveloppeContenues(
      List<EnveloppeConstructible> lEnveloppeContenues) {
    this.lEnvelopeConstructibles = lEnveloppeContenues;
  }

}
