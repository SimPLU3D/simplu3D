package fr.ign.cogit.simplu3d.test;

import java.io.File;
import java.util.List;

import tudresden.ocl20.pivot.interpreter.IInterpretationResult;
import tudresden.ocl20.pivot.model.IModel;
import tudresden.ocl20.pivot.modelinstance.IModelInstance;
import tudresden.ocl20.pivot.pivotmodel.Constraint;
import tudresden.ocl20.pivot.standalone.facade.StandaloneFacade;
import fr.ign.cogit.simplu3d.importer.model.ImportModelInstance;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.Environnement;

public class TestLoader {

  static File modelFile;

  static File oclConstraints;

  public static void main(String[] args) throws Exception {

    oclConstraints = new File(
        "src/main/resources/ocl/simple_allConstraints.ocl");
    String folderEnv = "E:/mbrasebin/Donnees/Strasbourg/GTRU/Project1/";

    try {

      System.out.println("*******************************************");
      System.out.println("***********Import environnement************");
      System.out.println("*******************************************");
      Environnement env = LoaderSHP.load(folderEnv);

      System.out.println("*******************************************");
      System.out.println("************Import modèle******************");
      System.out.println("*******************************************");

      IModel model = ImportModelInstance
          .getModel("target/classes/fr/ign/cogit/simplu3d/importer/model/ModelProviderClass.class");

      System.out.println("*******************************************");
      System.out.println("****Peuplement du modèle en instances******");
      System.out.println("*******************************************");

      IModelInstance modelInstance = ImportModelInstance.getModelInstance(
          model, env);

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
       * for(Constraint c : constraintList){
       * 
       * System.out.println(c.getKind() + "  "+c.getSpecification());
       * 
       * }
       */

      System.out.println("*******************************************");
      System.out.println("**Interprétation des contraintes OCL*******");
      System.out.println("*******************************************");

      for (IInterpretationResult result : StandaloneFacade.INSTANCE
          .interpretEverything(modelInstance, constraintList)) {
        System.out.println("  " + result.getModelObject() + " ("
            + result.getConstraint().getKind() + ": "
            + result.getConstraint().getSpecification().getBody() + "): "
            + result.getResult());
      }
      /*
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

}
