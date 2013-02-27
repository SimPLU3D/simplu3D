package fr.ign.cogit.simplu3d.test;

import java.io.File;
import java.net.URL;
import java.util.List;

import tudresden.ocl20.pivot.interpreter.IInterpretationResult;
import tudresden.ocl20.pivot.model.IModel;
import tudresden.ocl20.pivot.modelinstance.IModelInstance;
import tudresden.ocl20.pivot.modelinstancetype.java.internal.modelinstance.JavaModelInstance;
import tudresden.ocl20.pivot.pivotmodel.Constraint;
import tudresden.ocl20.pivot.standalone.facade.StandaloneFacade;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ITriangle;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Triangle;
import fr.ign.cogit.simplu3d.model.application.SousParcelle;


public class TestLoader {
  
   static File modelFile;
  
 static File oclConstraints;




public static void main(String[] args) throws Exception {
  
  
  oclConstraints = new File("src/main/resources/ocl/simple_allConstraints.ocl");
  modelFile = new File(
      "target/classes/fr/ign/cogit/simplu3d/model/application/ModelProviderClass.class");

  StandaloneFacade.INSTANCE.initialize(new URL("file:"
          + new File("log4j.properties").getAbsolutePath()));

  
  /*
   * Simple
   */
  System.out.println();
  System.out.println("Simple Example");
  System.out.println("--------------");
  System.out.println();

  try {
      IModel model = StandaloneFacade.INSTANCE.loadJavaModel(modelFile);
      
      
      IModelInstance modelInstance = new JavaModelInstance(model);
      
      
      ITriangle tri = new GM_Triangle(new DirectPosition(0,0), new DirectPosition(1,1), new DirectPosition(0,1));
      ITriangle tri2 = new GM_Triangle(new DirectPosition(0,0), new DirectPosition(5,5), new DirectPosition(0,5));
      
      
      
      SousParcelle sousParcelle = new SousParcelle();
      sousParcelle.setId(4);
      sousParcelle.setGeom(tri);
      
      
  //    System.out.println(sousParcelle.getGeom().area());
      
      modelInstance.addModelInstanceElement(sousParcelle);
      modelInstance.addModelInstanceElement(tri);
      modelInstance.addModelInstanceElement(tri2);
      

      // create an empty model instance and put objects into it
     /* IModelInstance modelInstance = new JavaModelInstance(model);

      Person student = new Student();
      student.setName("Student-work-a-lot");
      student.setAge(23);

      Person prof = new Professor();
      prof.setName("Prof. Invalid");
      prof.setAge(-42);

      modelInstance.addModelInstanceElement(student);
      modelInstance.addModelInstanceElement(prof);*/

      List<Constraint> constraintList = StandaloneFacade.INSTANCE
              .parseOclConstraints(model, oclConstraints);

      /*
      for(Constraint c :  constraintList){
        
        System.out.println(c.getKind() + "  "+c.getSpecification());
        
      }*/
      
      
      
      for (IInterpretationResult result : StandaloneFacade.INSTANCE
              .interpretEverything(modelInstance, constraintList)) {
          System.out.println("  " + result.getModelObject() + " ("
                  + result.getConstraint().getKind() + ": "
                  + result.getConstraint().getSpecification().getBody()
                  + "): " + result.getResult());
      }
      /*
      IOcl2DeclSettings settings = Ocl2DeclCodeFactory.getInstance()
      .createOcl2DeclCodeSettings();
      settings.setSourceDirectory("src-gen/simple/");
      settings.setModus(Ocl2DeclSettings.MODUS_TYPED);
      settings.setSaveCode(true);
      settings.setTemplateGroup(TemplatePlugin.getTemplateGroupRegistry()
              .getTemplateGroup("Standard(SQL)"));
      StandaloneFacade.INSTANCE.generateSQLCode(constraintList, settings,
              model);

      System.out.println("Finished code generation.");

      settings.setSaveCode(false);*/

  } catch (Exception e) {
      e.printStackTrace();
  }
  
  
}

}
