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
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.Rule;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.solver.interpreter.OCLInterpreterSimplu3D;

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

          IInterpretationResult result = null;
          
          try {
             result = interpreter.interpretConstraint(
                r.constraint, imiObject);
          } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
          }
          
          
 
         
  

          // System.out.println("Contrainte : " + r.constraint.toString());

          if (result != null) {

            if (result.getResult() instanceof OclBoolean) {

              OclBoolean bool = (OclBoolean) result.getResult();

              if(bool.oclIsInvalid().isTrue()){
                System.out.println("  " + result.getModelObject() + " ("
                    + result.getConstraint().getKind() + ": "
                    + result.getConstraint().getSpecification().getBody() + "): "
                    + result.getResult());

              }
              
              
              if (!bool.isTrue()) {
            //    System.out.println("Règle non vérifiée");
                
   //             System.out.println(imiObject);
                
                
           /*   System.out.println("  " + result.getModelObject() + " ("
                    + result.getConstraint().getKind() + ": "
                    + result.getConstraint().getSpecification().getBody() + "): "
                    + result.getResult());*/

                
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
   // System.out.println("Toutes les règles sont  vérifiées");
    return true;

  }

  private void init() {

    for (CadastralParcel cP : bPU.getCadastralParcel()) {

      for (SubParcel sP : cP.getSubParcel()) {
        sPList.add(sP);

        IModelInstance iM = ImportModelInstanceBasicPropertyUnit
            .generateModelInstance(Environnement.model);

        try {
          
          ImportModelInstanceBasicPropertyUnit.importCadastralSubParcel(iM, sP);
          
          ImportModelInstanceBasicPropertyUnit.importCadastralParcel(iM, cP);
          iM.addModelInstanceElement(bPU);
        } catch (TypeNotFoundInModelException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

        mDList.add(iM);

      }

    }

  }

  public void addBuilding(AbstractBuilding b)
      throws TypeNotFoundInModelException {
    newBuildingInstance = true;
    ImportModelInstanceBasicPropertyUnit.importBuilding(mDList.get(0), b);
  }

  public boolean isNewBuildingInstance() {
    return newBuildingInstance;
  }

  public List<SubParcel> getsPList() {
    return sPList;
  }

  public List<IModelInstance> getmDList() {
    return mDList;
  }

  public BasicPropertyUnit getbPU() {
    return bPU;
  }

  
  
  
  
  
  
  
  
  
  
  
  
}
