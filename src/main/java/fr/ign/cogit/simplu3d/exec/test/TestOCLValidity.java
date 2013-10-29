package fr.ign.cogit.simplu3d.exec.test;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import tudresden.ocl20.pivot.interpreter.IInterpretationResult;
import tudresden.ocl20.pivot.interpreter.IOclInterpreter;
import tudresden.ocl20.pivot.interpreter.OclInterpreterPlugin;
import tudresden.ocl20.pivot.model.IModel;
import tudresden.ocl20.pivot.modelinstance.IModelInstance;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceObject;
import tudresden.ocl20.pivot.pivotmodel.Constraint;
import tudresden.ocl20.pivot.standalone.facade.StandaloneFacade;
import fr.ign.cogit.simplu3d.importer.model.ImportModelInstanceEnvironnement;
import fr.ign.cogit.simplu3d.solver.interpreter.OCLInterpreterSimplu3D;

/**
 * Paramètres d'exécition
 * 
 * -Djavax.xml.parsers.SAXParserFactory=com.sun.org.apache.xerces.internal.jaxp.
 * SAXParserFactoryImpl
 * -Djavax.xml.parsers.DocumentBuilderFactory=com.sun.org.apache
 * .xerces.internal.jaxp.DocumentBuilderFactoryImpl
 * -Djavax.xml.transform.TransformerFactory
 * =com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl -Xms756m
 * -Xmx756m -XX:PermSize=256m -XX:MaxPermSize=256m
 * 
 * @author MBrasebin
 * 
 */
public class TestOCLValidity {

  public static void main(String[] args) {

    // Fichier contenant les contraintes OCL à appliquer
    File oclConstraints = new File("E:/mbrasebin/Donnees/Strasbourg/GTRU/ProjectT1/UXL3.ocl");

    
    
    
    
    
    try {

      System.out.println("*******************************************");
      System.out.println("************Import modèle******************");
      System.out.println("*******************************************");

      IModel model = ImportModelInstanceEnvironnement
          .getModel("target/classes/fr/ign/cogit/simplu3d/importer/model/ModelProviderClass.class");

      System.out.println("*******************************************");
      System.out.println("****Chargement des contraintes OCL*********");
      System.out.println("*******************************************");

      StandaloneFacade.INSTANCE.parseOclConstraints(model, oclConstraints);

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
