package fr.ign.cogit.simplu3d.checker;

import java.util.ArrayList;
import java.util.List;

import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclBoolean;
import tudresden.ocl20.pivot.interpreter.IInterpretationResult;
import tudresden.ocl20.pivot.interpreter.IOclInterpreter;
import tudresden.ocl20.pivot.interpreter.OclInterpreterPlugin;
import tudresden.ocl20.pivot.modelinstance.IModelInstance;
import tudresden.ocl20.pivot.modelinstancetype.exception.TypeNotFoundInModelException;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceObject;
import fr.ign.cogit.simplu3d.importer.model.ImportModelInstanceBasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.Rule;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.test.solver.interpreter.OCLInterpreterSimplu3D;

public class FastRuleChecker {

  private boolean newBuildingInstance = false;

  public boolean hasNewBuildingInstance() {
    return newBuildingInstance;
  }

  private List<SubParcel> sPList = new ArrayList<SubParcel>();
  private List<IModelInstance> mDList = new ArrayList<IModelInstance>();

  private BasicPropertyUnit bPU;

  public FastRuleChecker(BasicPropertyUnit bPU) {
    this.bPU = bPU;
    init();

  }

  public boolean check() {

    new OclInterpreterPlugin();

    int nbInt = sPList.size();

    for (int i = 0; i < nbInt; i++) {
      IOclInterpreter interpreter = new OCLInterpreterSimplu3D(mDList.get(i));
      SubParcel sP = sPList.get(i);

      for (Rule r : sP.getUrbaZone().get(0).getRules()) {

        for (IModelInstanceObject imiObject : mDList.get(i)
            .getAllModelInstanceObjects()) {

          IInterpretationResult result = interpreter.interpretConstraint(
              r.constraint, imiObject);

          // System.out.println("Contrainte : " + r.constraint.toString());

          if (result != null) {

            if (result.getResult() instanceof OclBoolean) {

              OclBoolean bool = (OclBoolean) result.getResult();

              if (!bool.isTrue()) {
                System.out.println("Règle non vérifiée");
                return false;
              }

            } else {
              System.out.println("  " + result.getModelObject() + " ("
                  + result.getConstraint().getKind() + ": "
                  + result.getConstraint().getSpecification().getBody() + "): "
                  + result.getResult());

            }
          }

        }

      }
    }
    System.out.println("Toutes les règles sont  vérifiées");
    return true;

  }

  private void init() {

    for (CadastralParcel cP : bPU.getCadastralParcel()) {

      for (SubParcel sP : cP.getSubParcel()) {
        sPList.add(sP);

        IModelInstance iM = ImportModelInstanceBasicPropertyUnit
            .getModelInstance(Environnement.model, sP);
        try {
          ImportModelInstanceBasicPropertyUnit.importCadastralParcel(iM, cP);
        } catch (TypeNotFoundInModelException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

        mDList.add(iM);

      }

    }

  }

  public void addBuilding(Building b) throws TypeNotFoundInModelException {
    newBuildingInstance = true;
    ImportModelInstanceBasicPropertyUnit.importBuilding(mDList.get(0), b);
  }

}
