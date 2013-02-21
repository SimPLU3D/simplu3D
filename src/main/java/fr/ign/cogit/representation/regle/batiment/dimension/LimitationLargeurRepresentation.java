package fr.ign.cogit.representation.regle.batiment.dimension;

import fr.ign.cogit.model.application.Batiment;
import fr.ign.cogit.model.regle.consequences.batiment.dimension.LimitationLargeur;
import fr.ign.cogit.representation.regle.Incoherence;
import fr.ign.cogit.representation.regle.RepresentationFBG;
import fr.ign.cogit.representation.regle.util.GenerationPanneau;
import fr.ign.cogit.representation.regle.util.GenerationPanneau.Panneau;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;

public class LimitationLargeurRepresentation extends Incoherence {

  public LimitationLargeurRepresentation(LimitationLargeur lA, Batiment b) {
    super(lA);
    IDirectPosition dp = (new Box3D(b.getGeom())).getCenter();

    this.setGeom(new GM_Point(dp));
    this.setRepresentation(new RepresentationFBG(this, GenerationPanneau
        .generateFromPanneau(dp, Panneau.INCO_ANGLE_MAX)));

  }

}
