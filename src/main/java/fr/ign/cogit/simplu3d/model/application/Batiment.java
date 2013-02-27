package fr.ign.cogit.simplu3d.model.application;

import java.util.ArrayList;
import java.util.List;

import org.citygml4j.model.citygml.building.Building;

import fr.ign.cogit.sig3d.model.citygml.building.CG_Building;

public class Batiment extends CG_Building {

  private List<SousParcelle> sousParcelles = new ArrayList<SousParcelle>();
  private Toit toit;
  private List<Facade> facades;
  private EmpriseBatiment emprise;
  
  private String destination = "";

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public EmpriseBatiment getEmprise() {
    return emprise;
  }

  public void setEmprise(EmpriseBatiment emprise) {
    this.emprise = emprise;
  }

  public Batiment() {
    super();
  }

  public Batiment(Building build) {
    super(build);
  }

  public  List<SousParcelle>  getSousParcelles() {
    return sousParcelles;
  }

  public void setSousParcelle(List<SousParcelle>  sousParcelle) {
    this.sousParcelles = sousParcelle;
  }

  public Toit getToit() {
    return toit;
  }

  public void setToit(Toit toit) {
    this.toit = toit;
  }

  public List<Facade> getFacade() {
    return facades;
  }

  public void setFacade(List<? extends Facade> facades) {
    this.facades = new ArrayList<Facade>();
    this.facades.addAll(facades);
  }

}
