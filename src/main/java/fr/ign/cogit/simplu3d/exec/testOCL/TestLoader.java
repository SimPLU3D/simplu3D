package fr.ign.cogit.simplu3d.exec.testOCL;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import tudresden.ocl20.pivot.interpreter.IInterpretationResult;
import tudresden.ocl20.pivot.interpreter.IOclInterpreter;
import tudresden.ocl20.pivot.interpreter.OclInterpreterPlugin;
import tudresden.ocl20.pivot.model.IModel;
import tudresden.ocl20.pivot.modelinstance.IModelInstance;
import tudresden.ocl20.pivot.modelinstancetype.java.internal.modelinstance.JavaModelInstance;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceObject;
import tudresden.ocl20.pivot.pivotmodel.Constraint;
import tudresden.ocl20.pivot.standalone.facade.StandaloneFacade;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ITriangle;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Triangle;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.simplu3d.importer.model.ImportModelInstanceEnvironnement;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.solver.interpreter.OCLInterpreterSimplu3D;

public class TestLoader {

  static File modelFile;

  static File oclConstraints;

  public static void main(String[] args) throws Exception {

    oclConstraints = new File(
        "src/main/resources/ocl/simple_allConstraintsThese.ocl");
//    String folderEnv = "E:/mbrasebin/Donnees/Strasbourg/GTRU/Project1/";

    try {

      System.out.println("*******************************************");
      System.out.println("***********Import environnement************");
      System.out.println("*******************************************");
      
      SubParcel p = new SubParcel();


      
      IDirectPosition dp1 = new DirectPosition(1,0,0);
      
      
      
      IDirectPosition dp2 = new DirectPosition(1,15,0);
      
      IDirectPosition dp3 = new DirectPosition(0,0,0);
      
      
      
      IMultiSurface<IOrientableSurface> ims = new GM_MultiSurface<IOrientableSurface>();
      
      
      ITriangle t = new GM_Triangle(dp1,dp2,dp3);
      ITriangle t2 = new GM_Triangle(dp1,dp2,dp1);
      
      ims.add(t);
      ims.add(t2);
      
      
      p.setGeom(ims);





      System.out.println("*******************************************");
      System.out.println("************Import modèle******************");
      System.out.println("*******************************************");

      IModel model = ImportModelInstanceEnvironnement
          .getModel("target/classes/fr/ign/cogit/simplu3d/importer/model/ModelProviderClass.class");

      System.out.println("*******************************************");
      System.out.println("****Peuplement du modèle en instances******");
      System.out.println("*******************************************");

      IModelInstance modelInstance = new JavaModelInstance(model);
      modelInstance.addModelInstanceElement(p);
      modelInstance.addModelInstanceElement(t);
      
      
      
      
      
      
      
      

      // create an empty model instance and put objects into it
      /*
       * IModelInstance modelInstance = new JavaModelInstance(model);
       * 
       * Person student = new Student(); student.setName("Student-work-a-lot");
       * student.setAge(23);
       * 
       * Person prof = new Professor(); prof.setName("Prof. Invalid");
       * prof.setAge(-42);
       * 
       * modelInstance.addModelInstanceElement(student);
       * modelInstance.addModelInstanceElement(prof);
       */

      System.out.println("*******************************************");
      System.out.println("****Chargement des contraintes OCL*********");
      System.out.println("*******************************************");

      List<Constraint> constraintList = StandaloneFacade.INSTANCE
          .parseOclConstraints(model, oclConstraints);

      /*
       * 
      System.out.println("*******************************************");
      System.out.println("**Interprétation des contraintes OCL*******");
      System.out.println("*******************************************");

      for (IInterpretationResult result : interpretEverything(modelInstance, constraintList)) {
        System.out.println("  " + result.getModelObject() + " ("
            + result.getConstraint().getKind() + ": "
            + result.getConstraint().getSpecification().getBody() + "): "
            + result.getResult());
      }

    
       * IOcl2DeclSettings settings = Ocl2DeclCodeFactory.getInstance()
       * .createOcl2DeclCodeSettings();
       * settings.setSourceDirectory("src-gen/simple/");
       * settings.setModus(Ocl2DeclSettings.MODUS_TYPED);
       * settings.setSaveCode(true);
       * settings.setTemplateGroup(TemplatePlugin.getTemplateGroupRegistry()
       * .getTemplateGroup("Standard(SQL)"));
       * StandaloneFacade.INSTANCE.generateSQLCode(constraintList, settings,
       * model);
       * 
       * System.out.println("Finished code generation.");
       * 
       * settings.setSaveCode(false);
       */

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
  
  
  
  /**
   * 
   * @param modelInstance
   * @param constraintList
   * @return
   */
  public static List<IInterpretationResult> interpretEverything(
      IModelInstance modelInstance, List<Constraint> constraintList) {

    new OclInterpreterPlugin();

  List<IInterpretationResult> resultList = new LinkedList<IInterpretationResult>();

  IOclInterpreter interpreter = new OCLInterpreterSimplu3D(modelInstance);

  for (IModelInstanceObject imiObject : modelInstance
          .getAllModelInstanceObjects()) {
    

    if(imiObject.getObject() instanceof SubParcel){
      System.out.println(imiObject.getName());
    }
    

    
      for (Constraint constraint : constraintList) {
          IInterpretationResult result = interpreter.interpretConstraint(
                  constraint, imiObject);
          if (result != null)
              resultList.add(result);
      }
  }

  return resultList;
}

}
