package fr.ign.cogit.simplu3d.importer.model;

import fr.ign.cogit.appli.geopensim.feature.micro.Batiment;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.simplu3d.calculation.HauteurCalculation;
import fr.ign.cogit.simplu3d.calculation.util.PointBasType;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.GeomUtil;
import fr.ign.cogit.simplu3d.model.application.Road;
import fr.ign.cogit.simplu3d.model.application.RoofSurface;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.application.SpecificWallSurface;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.model.application.UrbaZone;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.impl.CuboidSnap;

public class ModelProviderClass {
  protected UrbaZone zone;
  protected CadastralParcel parcelle;
  protected Building sousParcelle;
  protected Batiment batiment;
  protected SpecificWallSurface facade;
  protected SpecificCadastralBoundary bordure;
  protected RoofSurface toit;
  protected HauteurCalculation vol;
  protected PointBasType pbt;
  // protected POINT_HAUT_TYPE pbh;
  protected Integer inti;
  protected GeomUtil gg;
  protected AbstractBuilding ab;
  protected SubParcel sP;
  protected SpecificCadastralBoundary cB;
  protected IGeometry gm;
  protected BasicPropertyUnit bUP;
  protected IOrientableSurface oS;
  protected Road r;
  protected GM_Polygon poly;

  /*
   * protected Cuboid c; protected Cuboid2 c2;
   */

  protected CuboidSnap c;

}
