package fr.ign.cogit.simplu3d.model.application;

import java.util.ArrayList;
import java.util.List;

import org.citygml4j.model.citygml.landuse.LandUse;

import com.vividsolutions.jts.geom.Geometry;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.conversion.JtsGeOxygene;
import fr.ign.cogit.sig3d.model.citygml.landuse.CG_LandUse;

public class SubParcel extends CG_LandUse {

  public final String CLASSE = "SousParcelle";

  private CadastralParcel parcelle;

  private double avgSlope;
  private double area = -1;
  private List<UrbaZone> lUZ = new ArrayList<UrbaZone>();

  public List<UrbaZone> getUrbaZone() {
    return lUZ;
  }

  public void setlUZ(List<UrbaZone> lUZ) {
    this.lUZ = lUZ;
  }

  public double builtRatio() {
    return 0;
  }

  public double FARVolume() {
    return 0;
  }

  public double FARRatio() {
    return 0;
  }

  public SubParcel(IOrientableSurface iMS) {
    super();
    this.setGeom(iMS);
    this.setClazz(CLASSE);
  }

  public double getAvgSlope() {
    return avgSlope;
  }

  public double getArea() {

    if (area == -1) {
      area = this.getGeom().area();
    }
    return area;
  }

  public IFeatureCollection<AbstractBuilding> buildingsParts = new FT_FeatureCollection<AbstractBuilding>();
  // private IFeatureCollection<Voirie> voiries = new
  // FT_FeatureCollection<Voirie>();
  public IFeatureCollection<SpecificCadastralBoundary> sCBoundary = new FT_FeatureCollection<SpecificCadastralBoundary>();

  public List<SpecificCadastralBoundary> getBoundary() {
    return sCBoundary.getElements();
  }

  public SubParcel() {
    super();
    this.setClazz(CLASSE);

  }

  public SubParcel(LandUse landUse) {
    super(landUse);

    this.setClazz(CLASSE);

  }

  public IFeatureCollection<SpecificCadastralBoundary> getBorduresFond() {
    IFeatureCollection<SpecificCadastralBoundary> borduresFond = new FT_FeatureCollection<SpecificCadastralBoundary>();
    for (SpecificCadastralBoundary b : this.sCBoundary) {
      if (b.getType() == SpecificCadastralBoundary.BOT) {
        borduresFond.add(b);
      }

    }
    return borduresFond;
  }

  public IFeatureCollection<SpecificCadastralBoundary> getBorduresLat() {
    IFeatureCollection<SpecificCadastralBoundary> borduresLat = new FT_FeatureCollection<SpecificCadastralBoundary>();
    for (SpecificCadastralBoundary b : this.sCBoundary) {
      if (b.getType() == SpecificCadastralBoundary.LAT) {
        borduresLat.add(b);
      }

    }
    return borduresLat;
  }

  /*
   * public IFeatureCollection<Bordure> getBorduresVoies() {
   * IFeatureCollection<Bordure> bordureVoie = new
   * FT_FeatureCollection<Bordure>(); for (Bordure b : this.bordures) { if
   * (b.getTypeDroit() == Bordure.VOIE) { bordureVoie.add(b); } } return
   * bordureVoie; }
   */

  public void setParcelle(CadastralParcel cP) {
    this.parcelle = cP;
  }

  public CadastralParcel getParcel() {
    return parcelle;
  }

  public IFeatureCollection<AbstractBuilding> getBuildingsParts() {
    // System.out.println("NB BP : " + buildingsParts.size());
       return buildingsParts;
  }

  public void setBuildingsParts(
      IFeatureCollection<AbstractBuilding> buildingsParts) {
    this.buildingsParts = buildingsParts;
  }

  public double getces() {

    double area = this.getArea();

    int bP = this.getBuildingsParts().size();

    if (bP == 0) {
      return 0;
    }

    Geometry geom = null;
    try {
      geom = JtsGeOxygene.makeJtsGeom(this.getBuildingsParts().get(0)
          .getFootprint());

      for (int i = 0; i < bP; i++) {
        geom = geom.union(JtsGeOxygene.makeJtsGeom(this.getBuildingsParts()
            .get(i).getFootprint()));

      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    if (geom == null) {
      return 0;
    }

    // System.out.println(areaB / area);

    return geom.getArea() / area;

  }

  
  public IGeometry getConsLine(){
    SubParcel.getLogger().error("Empty method getConsline()");
    return null;
  }
  /*
   * public IFeatureCollection<Voirie> getVoiries() { return voiries; }
   * 
   * public void setVoiries(IFeatureCollection<Voirie> voiries) { this.voiries =
   * voiries; }
   */

  /*
   * public IFeatureCollection<Bordure> getBordures() { return bordures; }
   * 
   * 
   * public void setBordures(IFeatureCollection<Bordure> bordures) {
   * this.bordures = bordures; }
   * 
   * public void setParcelle(CadastralParcel parcelle) { this.parcelle =
   * parcelle; }
   */
}
