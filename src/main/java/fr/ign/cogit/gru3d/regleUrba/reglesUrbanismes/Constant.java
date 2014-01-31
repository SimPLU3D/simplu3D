package fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes;

public abstract class Constant {
  /**
   * Coefficient permettant d'obtenir le COS en éléminant les parties communes,
   * locaux techniques, isolation ext.
   */
  public final static double COEFF_COS = 0.8;

  /**
   * Constante fixant la hauteur d'un étage
   */
  public static double HAUTEUR_ETAGE = 3;
}
