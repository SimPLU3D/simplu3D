package fr.ign.cogit.simplu3d.model.application;

import org.citygml4j.model.citygml.landuse.LandUse;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.sig3d.model.citygml.landuse.CG_LandUse;

public class SousParcelle extends CG_LandUse {

  public final String CLASSE = "SousParcelle";

  private IFeatureCollection<Batiment> batiments = new FT_FeatureCollection<Batiment>();
  private IFeatureCollection<Voirie> voiries = new FT_FeatureCollection<Voirie>();
  private IFeatureCollection<Bordure> bordures = new FT_FeatureCollection<Bordure>();
  private Parcelle parcelle;

  public SousParcelle(IMultiSurface<IOrientableSurface> iMS) {
    super();

    this.setLod2MultiSurface(iMS);
    this.setGeom(iMS);

    this.setClazz(CLASSE);

  }

  public SousParcelle() {
    super();

    this.setClazz(CLASSE);

  }

  public SousParcelle(LandUse landUse) {
    super(landUse);

    this.setClazz(CLASSE);

  }

  public IFeatureCollection<Bordure> getBorduresFond() {
    IFeatureCollection<Bordure> borduresFond = new FT_FeatureCollection<Bordure>();
    for (Bordure b : this.bordures) {
      if (b.getTypeDroit() == Bordure.FOND) {
        borduresFond.add(b);
      }

    }
    return borduresFond;
  }

  public IFeatureCollection<Bordure> getBorduresLat() {
    IFeatureCollection<Bordure> borduresLat = new FT_FeatureCollection<Bordure>();
    for (Bordure b : this.bordures) {
      if (b.getTypeDroit() == Bordure.LATERAL) {
        borduresLat.add(b);
      }

    }
    return borduresLat;
  }

  public IFeatureCollection<Bordure> getBorduresVoies() {
    IFeatureCollection<Bordure> bordureVoie = new FT_FeatureCollection<Bordure>();
    for (Bordure b : this.bordures) {
      if (b.getTypeDroit() == Bordure.VOIE) {
        bordureVoie.add(b);
      }
    }
    return bordureVoie;
  }

  public Parcelle getParcelle() {
    return parcelle;
  }

  public IFeatureCollection<Batiment> getBatiments() {
    return batiments;
  }

  public void setBatiments(IFeatureCollection<Batiment> batiments) {
    this.batiments = batiments;
  }

  public IFeatureCollection<Voirie> getVoiries() {
    return voiries;
  }

  public void setVoiries(IFeatureCollection<Voirie> voiries) {
    this.voiries = voiries;
  }

  public IFeatureCollection<Bordure> getBordures() {
    return bordures;
  }

  public void setBordures(IFeatureCollection<Bordure> bordures) {
    this.bordures = bordures;
  }

  public void setParcelle(Parcelle parcelle) {
    this.parcelle = parcelle;
  }

}
