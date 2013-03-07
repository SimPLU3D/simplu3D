package fr.ign.cogit.simplu3d.importer.model;

import fr.ign.cogit.simplu3d.calculation.fuzzy.Calculation2D;
import fr.ign.cogit.simplu3d.fuzzy.FuzzyDouble;
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
  protected Calculation2D c;
  protected FuzzyDouble fd;
}
