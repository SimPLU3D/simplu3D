package fr.ign.cogit.simplu3d.importer.model;

import java.io.File;
import java.net.URL;

import tudresden.ocl20.pivot.model.IModel;
import tudresden.ocl20.pivot.modelinstance.IModelInstance;
import tudresden.ocl20.pivot.modelinstancetype.exception.TypeNotFoundInModelException;
import tudresden.ocl20.pivot.modelinstancetype.java.internal.modelinstance.JavaModelInstance;
import tudresden.ocl20.pivot.standalone.facade.StandaloneFacade;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.model.application._AbstractBuilding;

public class ImportModelInstance {

  public static IModel getModel(String modelPorviderURL) {

    try {
      StandaloneFacade.INSTANCE.initialize(new URL("file:"
          + new File("log4j.properties").getAbsolutePath()));

      IModel model = StandaloneFacade.INSTANCE.loadJavaModel(new File(
          modelPorviderURL));

      return model;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;

  }

  public static IModelInstance getModelInstance(IModel model, Environnement env) {

    try {

      IModelInstance modelInstance = new JavaModelInstance(model);

      completeInstances(modelInstance, env);

      return modelInstance;

    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;

  }

  private static void completeInstances(IModelInstance modelInstance,
      Environnement env) throws TypeNotFoundInModelException {

    /*
     * 
     * 
     * Gestion des sous parcelles
     */

    for (SubParcel sp : env.getSubParcels()) {
      modelInstance.addModelInstanceElement(sp);
      if (sp.getGeom() == null) {
        System.out.println("Patata");
      }
      modelInstance.addModelInstanceElement(sp.getLod2MultiSurface());

      // Gestion des bordures
      /*
       * for(SpecificCadastralBoundary b:sp.getSpecificCadastralBoundary()){
       * modelInstance.addModelInstanceElement(b);
       * modelInstance.addModelInstanceElement(b.getGeom()); }
       */

    }

    // Gestion des bâtiments

    for (_AbstractBuilding b : env.getBuildings()) {
      modelInstance.addModelInstanceElement(b);
    }

  }

}
