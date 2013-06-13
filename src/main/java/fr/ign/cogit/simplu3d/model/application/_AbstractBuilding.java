package fr.ign.cogit.simplu3d.model.application;

import java.util.ArrayList;
import java.util.List;

import org.citygml4j.model.citygml.core.CityObject;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.sig3d.model.citygml.building.CG_AbstractBuilding;
import fr.ign.cogit.simplu3d.calculation.StoreyCalculation;

public class _AbstractBuilding extends CG_AbstractBuilding{
  
  
  
  public List<BuildingPart> buildingPart = new ArrayList<BuildingPart>();
  private RoofSurface roofSurface;
  private List<WallSurface> wallSurface;
  private IOrientableSurface footPrint;
  
  
  private List<SubParcel> sousParcelles = new ArrayList<SubParcel>();
  private BasicPropertyUnit bPU;


  
  public List<WallSurface> getFacades() {
    return wallSurface;
  }
  public void setFacades(List<WallSurface> facades) {
    this.wallSurface = facades;
  }
  public IOrientableSurface getFootPrint() {
    return footPrint;
  }
  public void setFootPrint(IOrientableSurface footPrint) {
    this.footPrint = footPrint;
  }
  public List<SubParcel> getSousParcelles() {
    return sousParcelles;
  }
  public void setSousParcelles(List<SubParcel> sousParcelles) {
    this.sousParcelles = sousParcelles;
  }
  public BasicPropertyUnit getbPU() {
    return bPU;
  }
  public void setbPU(BasicPropertyUnit bPU) {
    this.bPU = bPU;
  }
  public List<BuildingPart> getBuildingPart() {
    return buildingPart;
  }

  public String destination;
  public IMultiSurface<IOrientableSurface> footprint;
  public String getDestination() {
    return destination;
  }
  public void setDestination(String destination) {
    this.destination = destination;
  }
  public IMultiSurface<IOrientableSurface> getFootprint() {
    return footprint;
  }
  public void setFootprint(IMultiSurface<IOrientableSurface> footprint) {
    this.footprint = footprint;
  }
  public List<BuildingPart> consistsOfBuildingPart() {
    return buildingPart;
  }
  public void setBuildingPart(List<BuildingPart> buildingPart) {
    this.buildingPart = buildingPart;
  }
  


  @Override
  public int getStoreysAboveGround() {
    if (this.storeysAboveGround == -1) {
      this.storeysAboveGround = StoreyCalculation.process(this);
    }

    return this.storeysAboveGround;

  }
  
  
  public RoofSurface getToit() {
    return roofSurface;
  }

  public void setToit(RoofSurface toit) {
    this.roofSurface = toit;
  }

  public List<WallSurface> getFacade() {
    return wallSurface;
  }

  public void setFacade(List<? extends WallSurface> facades) {
    this.wallSurface = new ArrayList<WallSurface>();
    this.wallSurface.addAll(facades);

  }
  @Override
  public CityObject export() {
    // TODO Auto-generated method stub
    return null;
  }

}
