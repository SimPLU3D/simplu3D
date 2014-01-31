package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles;

public abstract class Texture {

  private static String[] lURLTextures = null;// A compléter
  private static String[] lNomsTextures = { "Brique", "Bois", "Pierre",
      "Crépi blanc", "Crépi rose", "Crépi vert", "Ardoise", "Tuile rouge",
      "Tuile brune"

  };

  public static String[] getlURLTextures() {
    return Texture.lURLTextures;
  }

  public static String[] getlNomsTextures() {
    return Texture.lNomsTextures;
  }

}
