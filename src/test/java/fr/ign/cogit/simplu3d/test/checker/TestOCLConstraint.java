package fr.ign.cogit.simplu3d.test.checker;

import java.util.ArrayList;

import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceObject;
import fr.ign.cogit.simplu3d.checker.ExhaustiveChecker;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.test.io.load.application.LoaderSimpluSHPTest;

public class TestOCLConstraint {

  /**
   * @param args
   */
  public static void main(String[] args) {
    Environnement env = LoaderSimpluSHPTest.getENVTest();

    for (BasicPropertyUnit bPU : env.getBpU()) {
      ExhaustiveChecker vFR = new ExhaustiveChecker(bPU);

      boolean ok = vFR.check(new ArrayList<IModelInstanceObject>());

      System.out.println(ok);

    }

  }

}
