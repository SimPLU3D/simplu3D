package fr.ign.cogit.gru3d.regleUrba.representation;

import javax.media.j3d.BranchGroup;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.sig3d.representation.Default3DRep;

/**
 * Représentation à partir d'un branch Group et d'un feature
 * 
 * @author MBrasebin
 * 
 */
public class RepresentationCoherence extends Default3DRep {
  /**
   * Génère une représentation à partir d'un branchGroup et d'un entité
   * @param c
   * @param bg
   */
  public RepresentationCoherence(IFeature c, BranchGroup bg) {
    super();
    this.bGRep.addChild(bg);
    this.feat = c;
  }
}
