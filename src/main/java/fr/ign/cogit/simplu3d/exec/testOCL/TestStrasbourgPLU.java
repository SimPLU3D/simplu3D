package fr.ign.cogit.simplu3d.exec.testOCL;

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
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.solver.interpreter.OCLInterpreterSimplu3D;

public class TestStrasbourgPLU {

  public static void main(String[] args) {

    // Dossier contenant les fichiers définissant l'environnement géo
    String folderEnv = "E:/mbrasebin/Donnees/Strasbourg/GTRU/Project1/";

    // Fichier contenant les contraintes OCL à appliquer
    File oclConstraints = new File("src/main/resources/ocl/test_static.ocl");

    try {

      System.out.println("*******************************************");
      System.out.println("***********Import environnement************");
      System.out.println("*******************************************");
      Environnement env = LoaderSHP.load(folderEnv);

      System.out.println("*******************************************");
      System.out.println("************Import modèle******************");
      System.out.println("*******************************************");

      IModel model = ImportModelInstanceEnvironnement
          .getModel("target/classes/fr/ign/cogit/simplu3d/importer/model/ModelProviderClass.class");

      System.out.println("*******************************************");
      System.out.println("****Chargement des contraintes OCL*********");
      System.out.println("*******************************************");

      List<Constraint> constraintList = StandaloneFacade.INSTANCE
          .parseOclConstraints(model, oclConstraints);

      // ShapefileWriter.write(env.getSousParcelles(), "C:/temp/test.shp");

      System.out.println("*******************************************");
      System.out.println("****Peuplement du modèle en instances******");
      System.out.println("*******************************************");

      IModelInstance modelInstance = ImportModelInstanceEnvironnement
          .getModelInstance(model, env);

      System.out.println("*******************************************");
      System.out.println("**Interprétation des contraintes OCL*******");
      System.out.println("*******************************************");

      for (IInterpretationResult result : interpretEverything(modelInstance,
          constraintList)) {
        System.out.println("  " + result.getModelObject() + " ("
            + result.getConstraint().getKind() + ": "
            + result.getConstraint().getSpecification().getBody() + "): "
            + result.getResult());
      }

      /*
       * List<Theme> lTheme = new ArrayList<RepEnvironnement.Theme>();
       * lTheme.add(Theme.TOIT_BATIMENT); lTheme.add(Theme.FACADE_BATIMENT);
       * lTheme.add(Theme.FAITAGE); lTheme.add(Theme.PIGNON);
       * lTheme.add(Theme.GOUTTIERE); lTheme.add(Theme.VOIRIE); //
       * lTheme.add(Theme.PARCELLE); lTheme.add(Theme.SOUS_PARCELLE); //
       * lTheme.add(Theme.ZONE); lTheme.add(Theme.PAN);
       * 
       * Theme[] tab = lTheme.toArray(new Theme[0]);
       * 
       * List<VectorLayer> vl = RepEnvironnement.represent(env, tab);
       * 
       * MainWindow mW = new MainWindow();
       * 
       * for (VectorLayer l : vl) {
       * 
       * mW.getInterfaceMap3D().getCurrent3DMap().addLayer(l); }
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
