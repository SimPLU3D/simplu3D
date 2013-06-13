package fr.ign.cogit.simplu3d.model.application;

import org.citygml4j.model.citygml.landuse.LandUse;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.sig3d.model.citygml.landuse.CG_LandUse;

public class CadastralParcel extends CG_LandUse {


  public final String CLASSE = "Parcelle";
  
  private IFeatureCollection<SubParcel> sousParcelles = new FT_FeatureCollection<SubParcel>();
  private IFeatureCollection<SpecificCadastralBoundary> specificCB = new FT_FeatureCollection<SpecificCadastralBoundary>();
  
  
  public BasicPropertyUnit bPU;
  
  
  
  public BasicPropertyUnit getbPU() {
    return bPU;
  }


  public void setbPU(BasicPropertyUnit bPU) {
    this.bPU = bPU;
  }


  public double area;
  

  public CadastralParcel(){
    super();
  }
      
  
  public CadastralParcel(IMultiSurface<IOrientableSurface> iMS) {
    super();

    this.setLod2MultiSurface(iMS);
    this.setGeom(iMS);

    this.setClazz(CLASSE);

  }

  public IFeatureCollection<SpecificCadastralBoundary> getSpecificCadastralBoundary() {
    return specificCB;
  }


  public void setSpecificCadastralBoundary(IFeatureCollection<SpecificCadastralBoundary> bordures) {
    this.specificCB = bordures;
  }


  public CadastralParcel(LandUse landUse) {
    super(landUse);

    this.setClazz(CLASSE);

  }
  
  
  public IFeatureCollection<SubParcel> getSubParcel() {
    return sousParcelles;
  }

  public void setSubParcel(IFeatureCollection<SubParcel> sousParcelles) {
    this.sousParcelles = sousParcelles;
  }

  
  public double getArea() {
    return area;
  }


  public void setArea(double area) {
    this.area = area;
  }

  
}
