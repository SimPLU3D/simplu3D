package fr.ign.cogit.model.application;

import org.citygml4j.model.citygml.transportation.Road;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.sig3d.model.citygml.transportation.CG_Road;

public class Voirie extends CG_Road {

  private IMultiCurve<ILineString> axe;
  private String nom;
  private double largeur;

  public Voirie() {
    super();
  }

  public Voirie(Road tO) {
    super(tO);

    // TODO Auto-generated constructor stub
  }

  public IMultiCurve<ILineString> getAxe() {
    return axe;
  }

  public void setAxe(IMultiCurve<ILineString> axe) {
    this.axe = axe;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public double getLargeur() {
    return largeur;
  }

  public void setLargeur(double largeur) {
    this.largeur = largeur;
  }

}
