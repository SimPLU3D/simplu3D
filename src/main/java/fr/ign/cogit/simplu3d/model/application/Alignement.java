package fr.ign.cogit.simplu3d.model.application;

import org.citygml4j.model.citygml.core.CityObject;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.sig3d.model.citygml.core.CG_CityObject;

public class Alignement extends CG_CityObject {

  private int type;
  private IMultiCurve<IOrientableCurve> iMC;
  private double largeur = Double.NaN;

  public final static int ALIGNEMENT = 0;
  public final static int RECUL = 1;

  public Alignement() {
    super();
  }

  public Alignement(int type, IMultiCurve<IOrientableCurve> iMC, double largeur) {
    super();
    this.type = type;
    this.iMC = iMC;
    this.largeur = largeur;
  }

  @Override
  public CityObject export() {
    // TODO Auto-generated method stub
    return null;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public IMultiCurve<IOrientableCurve> getiMC() {
    return iMC;
  }

  public void setiMC(IMultiCurve<IOrientableCurve> iMC) {
    this.iMC = iMC;
  }

  public double getLargeur() {
    return largeur;
  }

  public void setLargeur(double largeur) {
    this.largeur = largeur;
  }

}
