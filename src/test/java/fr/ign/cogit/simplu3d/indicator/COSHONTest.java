package fr.ign.cogit.simplu3d.indicator;

import org.junit.Before;
import org.junit.Test;

import fr.ign.cogit.simplu3d.indicator.COSCalculation.METHOD;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.SubParcel;

public class COSHONTest {

  Environnement env;

  @Before
  public void init() throws CloneNotSupportedException {
    String folder = COSHONTest.class.getClassLoader().getClass().getResourceAsStream("./test/resources/data3D/").toString();

    System.out.println(folder);
    env = LoaderSHP.load(folder);

  }

  @Test
  public void testCOS() {

    int nbBat = 0;

    SubParcel sp = env.getSubParcels().get(0);

    double cos1 = COSCalculation.assess(sp, METHOD.SIMPLE);
    double cos2 = COSCalculation.assess(sp, METHOD.FLOOR_CUT);

    System.out.println(cos1);
    System.out.println(cos2);

    nbBat = nbBat + sp.getBuildingsParts().size();

  }

}
