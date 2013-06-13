package fr.ign.cogit.simplu3d.importer.model;

import fr.ign.cogit.appli.geopensim.feature.micro.Batiment;
import fr.ign.cogit.simplu3d.calculation.HauteurCalculation;
import fr.ign.cogit.simplu3d.calculation.HauteurCalculation.POINT_HAUT_TYPE;
import fr.ign.cogit.simplu3d.calculation.util.PointBasType;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.RoofSurface;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.application.UrbaZone;
import fr.ign.cogit.simplu3d.model.application.WallSurface;

public class ModelProviderClass {
  protected UrbaZone zone;
  protected CadastralParcel parcelle;
  protected Building sousParcelle;
  protected Batiment batiment;
  protected WallSurface facade;
  protected SpecificCadastralBoundary bordure;
  protected RoofSurface toit;
  protected HauteurCalculation vol;
  protected PointBasType pbt;
  protected POINT_HAUT_TYPE pbh;
  protected Integer inti;

}
