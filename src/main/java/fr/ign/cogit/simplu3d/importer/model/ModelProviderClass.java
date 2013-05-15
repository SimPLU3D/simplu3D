package fr.ign.cogit.simplu3d.importer.model;

import fr.ign.cogit.simplu3d.calculation.HauteurCalculation;
import fr.ign.cogit.simplu3d.calculation.HauteurCalculation.POINT_HAUT_TYPE;
import fr.ign.cogit.simplu3d.calculation.util.PointBasType;
import fr.ign.cogit.simplu3d.model.application.Batiment;
import fr.ign.cogit.simplu3d.model.application.Bordure;
import fr.ign.cogit.simplu3d.model.application.Facade;
import fr.ign.cogit.simplu3d.model.application.Parcelle;
import fr.ign.cogit.simplu3d.model.application.SousParcelle;
import fr.ign.cogit.simplu3d.model.application.Toit;
import fr.ign.cogit.simplu3d.model.application.Zone;

public class ModelProviderClass {
  protected Zone zone;
  protected Parcelle parcelle;
  protected SousParcelle sousParcelle;
  protected Batiment batiment;
  protected Facade facade;
  protected Bordure bordure;
  protected Toit toit;
  protected HauteurCalculation vol;
  protected PointBasType pbt;
  protected POINT_HAUT_TYPE pbh;
  protected Integer inti;

}
