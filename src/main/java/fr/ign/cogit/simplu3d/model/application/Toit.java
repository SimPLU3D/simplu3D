package fr.ign.cogit.simplu3d.model.application;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.sig3d.model.citygml.building.CG_RoofSurface;
import fr.ign.cogit.simplu3d.calculation.RoofAngle;

public class Toit extends CG_RoofSurface {

  private IMultiCurve<IOrientableCurve> gouttiere;
  private IMultiCurve<IOrientableCurve> pignons;
  private IMultiCurve<IOrientableCurve> faitage;
  private IMultiCurve<IOrientableCurve> interiorEdge;

  public IMultiCurve<IOrientableCurve> getInteriorEdge() {
    return interiorEdge;
  }

  public void setInteriorEdge(IMultiCurve<IOrientableCurve> interiorEdge) {
    this.interiorEdge = interiorEdge;
  }

  private Materiau mat;

  private int nbPans;

  public IMultiCurve<IOrientableCurve> getGouttiere() {
    return gouttiere;
  }

  public IMultiCurve<IOrientableCurve> getPignons() {
    return pignons;
  }

  public void setPignons(IMultiCurve<? extends IOrientableCurve> pignons) {
    this.pignons = new GM_MultiCurve<IOrientableCurve>();
    this.pignons.addAll(pignons);
  }

  public IMultiCurve<IOrientableCurve> getFaitage() {
    return faitage;
  }

  public void setFaitage(IMultiCurve<? extends IOrientableCurve> faitage) {
    this.faitage = new GM_MultiCurve<IOrientableCurve>();
    this.faitage.addAll(faitage);
  }

  public int getNbPans() {
    return nbPans;
  }

  public void setNbPans(int nbPans) {
    this.nbPans = nbPans;
  }

  public Materiau getMat() {
    return mat;
  }

  public void setMat(Materiau mat) {
    this.mat = mat;
  }

  public void setGouttiere(
      IMultiCurve<? extends IOrientableCurve> ligneGoutierre) {
    this.gouttiere = new GM_MultiCurve<IOrientableCurve>();
    this.gouttiere.addAll(ligneGoutierre);

  }

  public Object clone() {
    Toit tCopy = new Toit();

    tCopy.setGeom((IGeometry) this.getGeom().clone());
    tCopy.setLod2MultiSurface((IMultiSurface<IOrientableSurface>) this
        .getGeom().clone());
    tCopy.setGouttiere((IMultiCurve<IOrientableCurve>) this.getGouttiere()
        .clone());
    tCopy.setFaitage((IMultiCurve<IOrientableCurve>) this.getFaitage().clone());

    return tCopy;

  }


  private double angleMin = Double.NaN;
  private double angleMax = Double.NaN;
  
  
  
  public double getAngleMin() {
    
    
    if(Double.isNaN(angleMin)){
      angleMin = RoofAngle.angleMin(this);
    }
    
    return angleMin;
  }

  public void setAngleMin(double angleMin) {
    this.angleMin = angleMin;
  }

  public double getAngleMax() {
    
    if(Double.isNaN(angleMax)){
      angleMax = RoofAngle.angleMax(this);
    }
    
    return angleMax;
  }

  public void setAngleMax(double angleMax) {
    this.angleMax = angleMax;
  }
  

}
