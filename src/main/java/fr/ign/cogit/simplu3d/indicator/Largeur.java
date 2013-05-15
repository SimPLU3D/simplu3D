package fr.ign.cogit.simplu3d.indicator;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.sig3d.calculation.OrientedBoundingBox;
import fr.ign.cogit.simplu3d.model.application.Batiment;

public class Largeur {

  private double value;

  public Largeur(Batiment bat) {
    this(bat.getGeom());

  }

  public Largeur(IGeometry geom) {
    OrientedBoundingBox Bb = new OrientedBoundingBox(geom);

    IPolygon pol = Bb.getPoly();

    IDirectPositionList dpl = pol.coord();

    double largeur = dpl.get(0).distance(dpl.get(1));

    value = Math.min(largeur, dpl.get(2).distance(dpl.get(1)));

  }

  public Double getValue() {
    // TODO Auto-generated method stub
    return value;
  }

}
