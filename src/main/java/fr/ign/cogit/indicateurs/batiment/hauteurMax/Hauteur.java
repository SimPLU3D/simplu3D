package fr.ign.cogit.indicateurs.batiment.hauteurMax;

import fr.ign.cogit.model.application.Batiment;
import fr.ign.cogit.model.application.Toit;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;

public class Hauteur {

  
  /**
   * Hauteur maximale d'un bâtiment (point le plus haut - point le plus bas)
   * @param b
   * @return
   */
  public static double hauteurMax(Batiment b) {

    if (b.isSetMeasuredHeight()) {

      return b.getMeasuredHeight();

    }
    Box3D b3D = new Box3D(b.getGeom());

    double hauteur = b3D.getURDP().getZ() - b3D.getLLDP().getZ();

    b.setMeasuredHeight(hauteur);

    return hauteur;

  }
  
  
  
  /**
   * Hauteur à la gouttière d'un bâtiment (point le plus haut de la gouttière point le plus bas du bâtiment)
   * @param b
   * @return
   */
  public static double hauteurGouttiere(Batiment b){
    
    
    Toit t = b.getToit();
    
    
   
    
    Box3D b3DT = new Box3D(t.getGouttiere());
    Box3D b3DB = new Box3D(b.getGeom());
    
    
    
    double hauteur =  b3DT.getURDP().getZ() - b3DB.getLLDP().getZ();

    
    return hauteur;
    
  }
  
  
  
  /**
   * Nombre d'étages d'un bâtiment par rapport à un hauteur de bâtiment floorHeight.
   * Il s'agit du nombre de fois que l'on peut mettre floorHeight dans hauteurGouttiere + 1 étage si le toit le permet
   * Il y a au minimum 1 étage dans un bâtiment
   * @param b
   * @param floorHeight
   * @return
   */
  public static int nombreEtages(Batiment b, double floorHeight){
    
    if(b.isSetStoreyHeightsAboveGround()){
      return b.getStoreysAboveGround();
    }
    
    
    double hauteurGouttiere = hauteurGouttiere(b);
    double hauteurMax = hauteurMax(b);

    int nbFloor = Math.max((int) ((hauteurGouttiere) / floorHeight), 1);
    
    if((hauteurMax - hauteurGouttiere) > floorHeight){
      nbFloor++;
      
    }
    
    b.setStoreyHeightsAboveGround(floorHeight);
    b.setStoreysAboveGround(nbFloor);
    
    
    return nbFloor;
    
    
  }

}
