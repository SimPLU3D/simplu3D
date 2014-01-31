package fr.ign.cogit.gru3d.regleUrba.schemageo;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.feature.FT_Feature;
import fr.ign.cogit.geoxygene.sig3d.calculation.Calculation3D;
import fr.ign.cogit.geoxygene.sig3d.calculation.Proximity;
import fr.ign.cogit.geoxygene.sig3d.equation.ApproximatedPlanEquation;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.gru3d.regleUrba.Environnement;
import fr.ign.cogit.gru3d.regleUrba.Executor;

/**
 * Route pour le projet de calcul des contraintes d'urbanisme
 * 
 * @author MBrasebin
 */
public class Route extends FT_Feature {

  private static String NOM_ATT = "nom_rue_g"; // "NOM_RUE_G";
  private static String TYPE = "nature"; // "NATURE";
  private static String LARGEUR = "largeur"; // "LARGEUR";

  private String nom = null;

  private String type = null;

  private double largeur = Double.NaN;

  private List<Parcelle> parcellesBordantes = new ArrayList<Parcelle>();

  public Route(IFeature feat) {

    Object o = feat.getAttribute(Route.NOM_ATT);

    if (o == null) {

      o = feat.getAttribute(Route.NOM_ATT.toUpperCase());

    }

    this.nom = o.toString();
    
      o = feat.getAttribute(Route.TYPE);
    
    if(o ==null){
      
      o = feat.getAttribute(Route.TYPE.toUpperCase());
      
    }
    
    
    this.type = o.toString();

    try {
      
      o = feat.getAttribute(Route.LARGEUR);
      
      if(o ==null){
        
        o = feat.getAttribute(Route.LARGEUR.toUpperCase());
        
      }
      
      
      this.largeur = Double.parseDouble(o.toString());
    } catch (Exception e) {
      this.largeur = Double.NaN;
      System.out.println("Largeur n'est pas un entier");
    }

    if (this.largeur > 0) {

      IGeometry obj = feat.getGeom().buffer(this.largeur);

      IDirectPositionList dpl = obj.coord();
      IDirectPositionList dplRoute = feat.getGeom().coord();

      int nbDPL = dpl.size();

      for (int i = 0; i < nbDPL; i++) {
        Proximity c = new Proximity();
        IDirectPosition dp = dpl.get(i);

        c.nearest(dp, dplRoute);
        dp.setZ(c.nearest.getZ());

        if (dp.getZ() == 0) {
          dp.setZ(Environnement.DEFAULT_ZERO_Z);
        }

      }

      ApproximatedPlanEquation eq = new ApproximatedPlanEquation(dpl);

      Vecteur normal = eq.getNormale();

      if (normal.getZ() < 0) {
        dpl.inverseOrdre();

      }

      this.setGeom(new GM_Polygon(new GM_LineString(dpl)));

    

      // this.setGeom(From2DGeomTo3DGeom.convertitFromGeom(obj, 40, 40));//
      // .buffer(largeur/2));
    } else {
      this.setGeom(feat.getGeom());
    }

    IGeometry geom = this.getGeom();

    if (Executor.TRANSLATE_TO_ZERO) {
      if (Executor.dpTranslate == null) {
        Executor.dpTranslate = new DirectPosition(-geom.coord().get(0).getX(),
            -geom.coord().get(0).getY(), 0.0);
      }
      Calculation3D.translate(geom, Executor.dpTranslate);
    }

    this.setGeom(geom);
  }

  public List<Parcelle> getParcellesBordantes() {
    return this.parcellesBordantes;
  }

  public void setParcellesBordantes(List<Parcelle> parcellesBordantes) {
    this.parcellesBordantes = parcellesBordantes;
  }

  public String getNom() {
    return this.nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public double getLargeur() {
    return this.largeur;
  }

  public void setLargeur(double largeur) {
    this.largeur = largeur;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

}
