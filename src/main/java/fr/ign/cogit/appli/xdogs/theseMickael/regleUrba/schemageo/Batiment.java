package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.schemageo;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.ISolid;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.sig3d.calculation.Util;
import fr.ign.cogit.geoxygene.sig3d.representation.basic.Object2d;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_OrientableSurface;

/**
 * Classe de corps de batiments pour les règles d'urbanisme Définir le type du
 * batiment ...
 * 
 * @author MBrasebin
 */
public class Batiment extends DefaultFeature {

  private Parcelle parcelle = null;

  private Toit toit = null;

  private Texture texture = null;

  private String type = null;
  
  
  public Batiment(ISolid sol,    Color couleur, Color couleurToit) {
    this(sol.getFacesList(), couleur, couleurToit);
    
  }
  
  
  public Batiment(IMultiSurface<? extends IOrientableSurface> iMs,
      Color couleur, Color couleurToit) {
    this(iMs.getList(), couleur, couleurToit);
}
  
  public Batiment(List<? extends IOrientableSurface> iMs,
      Color couleur, Color couleurToit) {
    this(iMs);
    this.setRepresentation(new Object2d(this, couleur));
    this.getToit().setRepresentation(new Object2d(  this.getToit(), couleurToit));
  }



  public Batiment(ISolid sol) {
    this(sol.getFacesList());

  }

  public Batiment(List<? extends IOrientableSurface> lFacettes) {

    // Une liste pour les facettes de Murs et une autre pour les
    // facettes de
    // toits
    List<IOrientableSurface> listeMurs = new ArrayList<IOrientableSurface>();
    List<IOrientableSurface> listeToits = new ArrayList<IOrientableSurface>();

    listeMurs = Util.detectVertical(lFacettes, 0.1).getList();
    listeToits = Util.detectNonVertical(lFacettes, 0.1).getList();

    // on a traité toutes les faces du batiments on instancie les objets
    // en fonction
    // Et on complète la liste
    Toit t = new Toit(new GM_MultiSurface<GM_OrientableSurface>(listeToits));
    this.setGeom(new GM_MultiSurface<GM_OrientableSurface>(listeMurs));

    this.setToit(t);
    t.setBatiment(this);

    // this.setRepresentation(new ObjectCartoon(this, new Color(127, 0, 0),
    // Color.black, 3, 1));
    // this.setRepresentation(new Object2d(this, new Color(127, 0, 0)));
    /*
     * Texture2D tex = TextureManager.textureLoading(NPA.class
     * .getResource("/demo3D/path2816.png").getPath());
     * this.setRepresentation(new NPA(this, Color.white, tex, 10, 0.5));
     */
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Parcelle getParcelle() {
    return this.parcelle;
  }

  public void setParcelle(Parcelle parcelle) {
    this.parcelle = parcelle;
  }

  public Toit getToit() {
    return this.toit;
  }

  public void setToit(Toit toit) {
    this.toit = toit;
  }

  public Texture getTexture() {
    return this.texture;
  }

  public void setTexture(Texture texture) {
    this.texture = texture;
  }

  public double area() {

    return Double.NaN;
  }

}
