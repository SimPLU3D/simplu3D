package fr.ign.cogit.simplu3d.model.application;


/**
 * 
 * @author MBrasebin
 *
 */
  public enum Materiau{
    
    BRIQUE("Brique", Materiau.folderMat + "texture.jpg", 5, 5),
    PIERRE("Mur", Materiau.folderMat + "mur.jpg", 5, 5), MUR_BLANC("Mur", Materiau.folderMat + "murs.jpg", 5, 5), 
    VEGETAL("Vegetal", Materiau.folderMat + "murs.jpg", 5, 5),  TOLE("Toles", Materiau.folderMat + "toit.jpg", 5, 5)  ;
    
    
  private final static String folderMat = "E:/mbrasebin/Workspace/geoxygene/geoxygene-sig3d/src/main/resources/texture/";
    
    
  public String nomMateriau;
  public String textRep = null;
  public double textH = Double.NaN,textL = Double.NaN;
  
  
  
  
  
  
  
   Materiau(String nomMateriau, String textRep, double textH, double textL) {

    this.nomMateriau = nomMateriau;
    this.textRep = textRep;
    this.textH = textH;
    this.textL = textL;
  }
  public String getNomMateriau() {
    return nomMateriau;
  }
  public void setNomMateriau(String nomMateriau) {
    this.nomMateriau = nomMateriau;
  }
  public String getTextRep() {
    return textRep;
  }
  public void setTextRep(String textRep) {
    this.textRep = textRep;
  }
  public double getTextH() {
    return textH;
  }
  public void setTextH(double textH) {
    this.textH = textH;
  }
  public double getTextL() {
    return textL;
  }
  public void setTextL(double textL) {
    this.textL = textL;
  }

  
  }


