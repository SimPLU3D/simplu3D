package fr.ign.cogit.gru3d.regleUrba.representation;

import java.util.HashMap;
import java.util.Map;

import javax.media.j3d.BranchGroup;

import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.SchemaDefaultFeature;
import fr.ign.cogit.geoxygene.schema.schemaConceptuelISOJeu.AttributeType;
import fr.ign.cogit.geoxygene.schema.schemaConceptuelISOJeu.FeatureType;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.Consequence;
import fr.ign.cogit.gru3d.regleUrba.schemageo.Parcelle;

/**
 * Classe d'incohérence révélée lors de la vérifications des règles sur des
 * parcelles
 * 
 * @author MBrasebin
 */
public class Incoherence extends DefaultFeature {

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
  
  
  public Incoherence(Consequence cons){
    this.consequence = cons;
  }

  public Incoherence(Consequence cons, Parcelle p, BranchGroup bg) {

    if (Incoherence.fType == null) {
      Incoherence.initType();
    }

    this.consequence = cons;
    this.p = p;

    this.setSchema(Incoherence.schema);

    this.setFeatureType(Incoherence.fType);
    this.setAttributes(new String[] { this.consequence.getDescription() });

    this.setAttribute("Consequence", this.consequence.getDescription());

    this.setGeom(p.getGeom());
    if (bg != null) {
      this.setRepresentation(new RepresentationCoherence(this, bg));
    }
  }

  private static FeatureType fType = null;
  private static SchemaDefaultFeature schema = null;

  private static void initType() {

    Incoherence.fType = new fr.ign.cogit.geoxygene.schema.schemaConceptuelISOJeu.FeatureType();

    Map<Integer, String[]> attLookup = new HashMap<Integer, String[]>();

    AttributeType type = new AttributeType();
    String nomField = "Consequence";
    String memberName = "Consequence";
    String valueType = "String";
    type.setNomField(nomField);
    type.setMemberName(memberName);
    type.setValueType(valueType);
    Incoherence.fType.addFeatureAttribute(type);
    attLookup.put(new Integer(0), new String[] { nomField, memberName });

    Incoherence.schema = new SchemaDefaultFeature();
    Incoherence.schema.setFeatureType(Incoherence.fType);
    Incoherence.fType.setSchema(Incoherence.schema);
    Incoherence.schema.setAttLookup(attLookup);

  }
}
