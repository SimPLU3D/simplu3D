package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.schemageo;

import java.awt.Color;

import fr.ign.cogit.geoxygene.feature.FT_Feature;
import fr.ign.cogit.geoxygene.sig3d.representation.basic.Object2d;
import fr.ign.cogit.geoxygene.spatial.geomroot.GM_Object;

/**
 * Classe de toits pour les règles d'urbanisme
 * 
 * @todo intégrer représentation en fonction de la texture
 * @author MBrasebin
 */
public class Toit extends FT_Feature {

  private Batiment batiment;

  private Texture texture;

  
  
  public Toit(GM_Object geom) {
    
    super(geom);
  }
  
  
  public Toit(GM_Object geom, Color couleur) {
    this(geom);
    /*
     * this.setRepresentation(new ObjectCartoon(this, couleur, Color.black, 3,
     * 1)); Texture2D tex = TextureManager.textureLoading(NPA.class
     * .getResource("/demo3D/path2816.png").getPath());
     * this.setRepresentation(new NPA(this, Color.white, tex, 10, 0.5));
     */

    // this.setRepresentation(new ObjectCartoon(this, couleur, Color.black, 3,
    // 1));

    this.setRepresentation(new Object2d(this, couleur));
  }

  public Texture getTexture() {
    return this.texture;
  }

  public void setTexture(Texture texture) {
    this.texture = texture;
  }

  public Batiment getBatiment() {
    return this.batiment;
  }

  public void setBatiment(Batiment batiment) {
    this.batiment = batiment;
  }

}
