package fr.ign.cogit.simplu3d.test.indicator;

import junit.framework.Assert;

import org.junit.Test;

import fr.ign.cogit.simplu3d.indicator.COSCalculation;
import fr.ign.cogit.simplu3d.indicator.COSCalculation.METHOD;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.test.io.load.application.LoaderSimpluSHPTest;

public class COSHONTest {
  
  
  public static void main(String[] args){
    
    (new COSHONTest()).testCOS();
  }

  @Test
  public void testCOS() {

    BasicPropertyUnit sp = LoaderSimpluSHPTest.getENVTest().getBpU().get(0);

    double cos1 = COSCalculation.assess(sp, METHOD.SIMPLE);
    double cos2 = COSCalculation.assess(sp, METHOD.FLOOR_CUT);
    System.out.println(cos1);
    System.out.println(cos2);
    
    double epsilon = 0.00001;

   Assert.assertTrue(Math.abs(1.599950942067229 -cos1) < epsilon);
    Assert.assertTrue(Math.abs(1.59979612630762089 - cos2) < epsilon);

  }

}
