package fr.ign.cogit.gru3d.regleUrba.schemageo;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.feature.FT_Feature;

/**
 * Classe de l'environnement géographique Décrit une zone du PLU comme une
 * ensemble de parcelles
 * 
 * @author MBrasebin
 */
public class ZonePLUGeo extends FT_Feature {

  String nom = null;

  List<Parcelle> lParcelles = new ArrayList<Parcelle>();

  public String getNom() {
    return this.nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public List<Parcelle> getlParcelles() {
    return this.lParcelles;
  }

  public void setlParcelles(List<Parcelle> lParcelles) {
    this.lParcelles = lParcelles;
  }

}
