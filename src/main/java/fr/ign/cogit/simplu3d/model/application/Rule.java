package fr.ign.cogit.simplu3d.model.application;

import tudresden.ocl20.pivot.pivotmodel.Constraint;

public class Rule {
  
  
  public static int LAST_ID = 0;
  
  public int integerID = LAST_ID++;
  
  public Constraint constraint;
  
  public String text;

  public Rule(Constraint constraint, String text) {
    super();
    this.constraint = constraint;
    this.text = text;
  }
  
    

}
