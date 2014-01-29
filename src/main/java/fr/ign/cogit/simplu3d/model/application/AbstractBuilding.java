package fr.ign.cogit.simplu3d.model.application;

import java.util.ArrayList;
import java.util.List;

import org.citygml4j.model.citygml.core.CityObject;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.calculation.Util;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.sig3d.analysis.RoofDetection;
import fr.ign.cogit.sig3d.model.citygml.building.CG_AbstractBuilding;
import fr.ign.cogit.simplu3d.importer.applicationClasses.EmpriseGenerator;
import fr.ign.cogit.simplu3d.importer.applicationClasses.RoofImporter;
import fr.ign.cogit.simplu3d.indicator.HauteurCalculation;
import fr.ign.cogit.simplu3d.indicator.StoreyCalculation;

public abstract class AbstractBuilding extends CG_AbstractBuilding {

  public List<BuildingPart> buildingPart = new ArrayList<BuildingPart>();
  private RoofSurface roofSurface = null;
  private List<SpecificWallSurface> wallSurface;

  public boolean isNew = false;

  public void setNew(boolean isNew) {
    this.isNew = isNew;
  }

  private List<SubParcel> sousParcelles = new ArrayList<SubParcel>();
  private BasicPropertyUnit bPU;

  protected AbstractBuilding() {
    super();
  }

  public double height(String s, String s2) {
    return 0;
  }

  public AbstractBuilding(IGeometry geom) {
    this.setGeom(geom);
    this.setLod2MultiSurface(FromGeomToSurface.convertMSGeom(geom));

    // Etape 1 : détection du toit et des façades
    List<IOrientableSurface> lOS = FromGeomToSurface.convertGeom(geom);
    @SuppressWarnings("unchecked")
    IMultiSurface<IOrientableSurface> surfaceRoof = (IMultiSurface<IOrientableSurface>) RoofDetection
        .detectRoof(this, 0.2, true);

    // Util.detectRoof(lOS,
    // 0.2);
    IMultiSurface<IOrientableSurface> surfaceWall = Util.detectVertical(lOS,
        0.2);

    // Création facade
    SpecificWallSurface f = new SpecificWallSurface();
    f.setGeom(surfaceWall);
    f.setLod2MultiSurface(surfaceWall);

    List<SpecificWallSurface> lF = new ArrayList<SpecificWallSurface>();
    lF.add(f);
    this.setFacade(lF);

    // Etape 2 : on créé l'emprise du bâtiment
    footprint = EmpriseGenerator.convert(surfaceRoof);

    if (footprint == null) {
      System.out.println("Emprise nulle");
    }

    // Création toit
    RoofSurface t = RoofImporter.create(surfaceRoof,
        (IPolygon) footprint.clone());

    // Affectation
    this.setToit(t);
  }

  public List<SpecificWallSurface> getFacades() {
    return wallSurface;
  }

  public void setFacades(List<SpecificWallSurface> facades) {
    this.wallSurface = facades;
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
  public IOrientableSurface footprint;

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public void setFootprint(IOrientableSurface footprint) {
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

  public List<SpecificWallSurface> getFacade() {
    return wallSurface;
  }

  public void setFacade(List<? extends SpecificWallSurface> facades) {
    this.wallSurface = new ArrayList<SpecificWallSurface>();
    this.wallSurface.addAll(facades);

  }

  @Override
  public CityObject export() {
    // TODO Auto-generated method stub
    return null;
  }

  public double height(int pB, int pH) {
    double h = HauteurCalculation.calculate(this, pB, pH);
    System.out.println("Hauteur calculée : " + h);
    return h;
  }

  // @TODO compléter la méthode
  public abstract AbstractBuilding clone();

  public double distance(AbstractBuilding b2) {
    return this.footprint.distance(b2.getFootprint());
  }

  public RoofSurface getRoofSurface() {
    return roofSurface;
  }

  public List<SpecificWallSurface> getWallSurface() {
    return wallSurface;
  }

  public boolean isNew() {
    return isNew;
  }

  public IOrientableSurface getFootprint() {
    return footprint;
  }

  /**
   * @TODO : à implémenter
   * @return
   */
  public List<AbstractBuilding> getBuildingsParts() {
    return null;
  }

  /**
   * @TODO : à implémenter
   * @return
   */
  public List<AbstractBuilding> bandEpsilon(IGeometry geom, double d1, double d2) {
    return null;
  }

  public List<AbstractBuilding> bandEpsilon(IGeometry geom, double d1) {
    return bandEpsilon(geom, d1, Double.POSITIVE_INFINITY);
  }

  public List<AbstractBuilding> bandEpsilon(CadastralParcel cP, double distMin,
      double distMax) {
    return null;
  }

  public boolean prospect(IGeometry geom, double slope, double hIni) {
    double zMin = 0;
    IDirectPositionList dpl = null;
    double shift = 0;
    Box3D box = new Box3D(this.getGeom());
    dpl = this.getToit().getGeom().coord();
    zMin = box.getLLDP().getZ();
    for (IDirectPosition dp : dpl) {
      if (geom.distance(new GM_Point(dp)) * slope + hIni < shift
          + dp.getZ() - zMin) {
        return false;
      }
    }
    return true;
  }
}
