package fr.ign.cogit.simplu3d.importer.model;

import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.BuildingPart;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.Prescription_SURF;
import fr.ign.cogit.simplu3d.model.application.PublicSpace;
import fr.ign.cogit.simplu3d.model.application.Road;
import fr.ign.cogit.simplu3d.model.application.RoofSurface;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.application.SpecificWallSurface;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.model.application.UrbaZone;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;

public class ModelProviderClass {
  protected UrbaZone zone;
  protected CadastralParcel parcelle;
  protected Building sousParcelle;
  protected SpecificWallSurface facade;
  protected SpecificCadastralBoundary bordure;
  protected RoofSurface toit;
  protected Integer inti;
  protected AbstractBuilding ab;
  protected SubParcel sP;
  protected IGeometry gm;
  protected BasicPropertyUnit bUP;
  protected IOrientableSurface oS;
  protected Road r;
  protected GM_Polygon poly;
  protected Prescription_SURF ps;
  protected PublicSpace pss;
  protected BuildingPart bp;
  protected Cuboid c2;

}
