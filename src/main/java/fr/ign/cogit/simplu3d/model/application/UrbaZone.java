package fr.ign.cogit.simplu3d.model.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.sig3d.model.citygml.landuse.CG_LandUse;

/**
 * 
 * @author MBrasebin
 * 
 */
public class UrbaZone extends CG_LandUse {

  public final String CLASSE = "Zone";

  private IFeatureCollection<SubParcel> subParcels = new FT_FeatureCollection<SubParcel>();
  private String name = "";
  private List<Rule> rules = new ArrayList<Rule>();
  private String text = "";
  private Date date = null;
  
  
  
 

  public UrbaZone(IOrientableSurface geom) {
    super();

  }
  
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public List<Rule> getRules() {
    return rules;
  }

  public void setRules(List<Rule> rules) {
    this.rules = rules;
  }

  public void setName(String name) {
    this.name = name;
  }
  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }


  public IFeatureCollection<SubParcel> getSubParcels() {
    return subParcels;
  }

  public void setSubParcels(IFeatureCollection<SubParcel> subParcels) {
    this.subParcels = subParcels;
  }

  public String getName() {
    return name;
  }



}
