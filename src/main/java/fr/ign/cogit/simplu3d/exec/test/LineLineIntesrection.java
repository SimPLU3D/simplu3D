package fr.ign.cogit.simplu3d.exec.test;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.sig3d.equation.LineEquation;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;

public class LineLineIntesrection {

  /**
   * @param args
   */
  public static void main(String[] args) {
    
    
    IDirectPosition dp1 = new DirectPosition(0,0);
    IDirectPosition dp2 = new DirectPosition(1,2);
    IDirectPosition dp3 = new DirectPosition(0,1);
    IDirectPosition dp4 = new DirectPosition(1,0);
    
    LineEquation l1 = new LineEquation(dp1,dp2);
    LineEquation l2 = new LineEquation(dp3,dp4);
    
    
    
    IDirectPosition dpOut = l1.intersectionLineLine(l2);
    
    System.out.println(dpOut);
    
    

  }

}
