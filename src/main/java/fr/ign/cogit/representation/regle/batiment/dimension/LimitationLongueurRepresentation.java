package fr.ign.cogit.representation.regle.batiment.dimension;

import fr.ign.cogit.model.application.Batiment;
import fr.ign.cogit.model.regle.consequences.batiment.dimension.LimitationLongueur;
import fr.ign.cogit.representation.regle.Incoherence;
import fr.ign.cogit.representation.regle.RepresentationFBG;
import fr.ign.cogit.representation.regle.util.GenerationPanneau;
import fr.ign.cogit.representation.regle.util.GenerationPanneau.Panneau;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;

public class LimitationLongueurRepresentation extends Incoherence {

  public LimitationLongueurRepresentation(LimitationLongueur lA, Batiment b) {
    super(lA);
    IDirectPosition dp = (new Box3D(b.getGeom())).getCenter();

    this.setGeom(new GM_Point(dp));
    this.setRepresentation(new RepresentationFBG(this, GenerationPanneau
        .generateFromPanneau(dp, Panneau.INCO_ANGLE_MAX)));

  }

}
