package fr.ign.cogit.simplu3d.importer.model;

import java.io.File;
import java.net.URL;
import java.util.List;

import tudresden.ocl20.pivot.model.IModel;
import tudresden.ocl20.pivot.modelinstance.IModelInstance;
import tudresden.ocl20.pivot.modelinstancetype.exception.TypeNotFoundInModelException;
import tudresden.ocl20.pivot.modelinstancetype.java.internal.modelinstance.JavaModelInstance;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceObject;
import tudresden.ocl20.pivot.standalone.facade.StandaloneFacade;
import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.Road;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.application.SpecificWallSurface;
import fr.ign.cogit.simplu3d.model.application.SubParcel;

public class ImportModelInstanceBasicPropertyUnit {

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

  public static IModelInstance generateModelInstance(IModel model) {

    try {

      IModelInstance modelInstance = new JavaModelInstance(model);


      return modelInstance;

    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;

  }

  public static void importBuilding(IModelInstance modelInstance,
      AbstractBuilding b) throws TypeNotFoundInModelException {

    modelInstance.addModelInstanceElement(b);
    modelInstance.addModelInstanceElement(b.getFootprint());
    modelInstance.addModelInstanceElement(b.getGeom());

    for (SpecificWallSurface sW : b.getFacades()) {
      modelInstance.addModelInstanceElement(sW);
      modelInstance.addModelInstanceElement(sW.getGeom());

    }

    modelInstance.addModelInstanceElement(b.getToit());
    modelInstance.addModelInstanceElement(b.getToit().getGeom());

  }

  public static IModelInstanceObject importCadastralSubParcel(IModelInstance modelInstance,
      SubParcel sP) throws TypeNotFoundInModelException {
    IModelInstanceObject iMS = (IModelInstanceObject)modelInstance.addModelInstanceElement(sP);
    modelInstance.addModelInstanceElement(sP.getGeom());

    for (AbstractBuilding aB : sP.getBuildingsParts()) {
      importBuilding(modelInstance, aB);

    }

    return iMS;
  }

  public static void importCadastralParcel(IModelInstance modelInstance,
      CadastralParcel cP) throws TypeNotFoundInModelException {
    


    modelInstance.addModelInstanceElement(cP);
    modelInstance.addModelInstanceElement(cP.getGeom());

    for (SpecificCadastralBoundary sCB : cP.getSpecificCadastralBoundary()) {
      modelInstance.addModelInstanceElement(sCB);
      modelInstance.addModelInstanceElement(sCB.getGeom());

      IFeature feat = sCB.getFeatAdj();

      if (feat != null && feat instanceof Road) {
        modelInstance.addModelInstanceElement(feat);
      }

    }

  }

}
