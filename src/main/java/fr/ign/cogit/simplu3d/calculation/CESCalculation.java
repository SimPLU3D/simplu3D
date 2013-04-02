package fr.ign.cogit.simplu3d.calculation;

import fr.ign.cogit.simplu3d.calculation.COSCalculation.METHOD;
import fr.ign.cogit.simplu3d.model.application.Batiment;
import fr.ign.cogit.simplu3d.model.application.SousParcelle;

public class CESCalculation {

  public static double assess(SousParcelle p, METHOD m) {

    double area = p.getLod2MultiSurface().area();
    double aireBatie = 0;
   
    
    for(Batiment b:p.getBatiments()){
      aireBatie = aireBatie + b.getEmprise().getLod2MultiSurface().area();
    }
    
    return aireBatie/area;
    
  }
}
