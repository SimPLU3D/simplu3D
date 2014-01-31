package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.representation;

import java.awt.Color;

import javax.media.j3d.BranchGroup;

import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles.Consequence;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.schemageo.Parcelle;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.FT_Feature;
import fr.ign.cogit.geoxygene.sig3d.representation.sample.ObjectCartoon;

public class Contrainte extends FT_Feature {

  private Parcelle p = null;

  private Consequence consequence;

  public Parcelle getP() {
    return this.p;
  }

  public String getDescription() {
    if (this.consequence == null) {
      return null;
    }
    return this.consequence.getDescription();
  }

  /**
   * Construit une contrainte à partir d'une conséquence de règles d'une
   * parcelle et d'une représentation
   * 
   * @param cons
   * @param p
   * @param bg
   */
  public Contrainte(Consequence cons, Parcelle p, BranchGroup bg) {
    this.consequence = cons;
    this.p = p;
    this.setGeom(p.getGeom());
    if (bg != null) {
      this.setRepresentation(new RepresentationCoherence(this, bg));
    }
  }

  /**
   * Construit une contrainte à partir d'une conséquence de règles d'une
   * parcelle et d'une géométrie qui servira à la représentation( mode
   * bordEpais)
   * 
   * @param cons
   * @param p
   * @param geom
   */
  public Contrainte(Consequence cons, Parcelle p, IGeometry geom) {
    this.consequence = cons;
    this.p = p;
    this.setGeom(geom);
    int dimension = geom.dimension();
    if (dimension == 3) {

      ObjectCartoon objet = new ObjectCartoon(this, Color.red, Color.black, 3,
          1);
      this.setRepresentation(objet);
    } else if (dimension == 2) {

      ObjectCartoon objet = new ObjectCartoon(this, Color.red, Color.black, 3,
          1);

      this.setRepresentation(objet);
    }

  }

}
