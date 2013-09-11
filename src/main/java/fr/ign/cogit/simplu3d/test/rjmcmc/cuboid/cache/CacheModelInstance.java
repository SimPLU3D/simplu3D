package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.cache;

import java.util.ArrayList;
import java.util.List;

import tudresden.ocl20.pivot.modelinstance.IModelInstance;
import tudresden.ocl20.pivot.modelinstancetype.exception.TypeNotFoundInModelException;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceElement;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceObject;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.SubParcel;

public class CacheModelInstance {

  private List<AbstractBuilding> lAB = new ArrayList<>();
  private List<IModelInstanceElement> mIEBat = new ArrayList<>();
  private List<IModelInstanceElement> mIEFootP = new ArrayList<>();

  private IModelInstance mI = null;
  private BasicPropertyUnit bPU;

  public CacheModelInstance(BasicPropertyUnit bPU, IModelInstance mI) {
    this.mI = mI;
    this.bPU = bPU;
  }

  public List<IModelInstanceObject> update(
      List<? extends AbstractBuilding> lBorn,
      List<? extends AbstractBuilding> lDeath) {
    /*
     * System.out.println("*******Born list*******"); for(AbstractBuilding ab:
     * lBorn){ System.out.println(ab); }
     * 
     * 
     * System.out.println("**********Kill list********"); for(AbstractBuilding
     * ab: lDeath){ System.out.println(ab); }
     */
    
    int nbElem = lDeath.size();
    for(int i=0;i<nbElem;i++){
      AbstractBuilding a = lDeath.get(i);
      
      boolean isRem = lBorn.remove(a);

      
      if(isRem){
        lDeath.remove(i);
        i--;
        nbElem--;
      }
      
    }

    
    

    List<IModelInstanceObject> lIME = addElements(lBorn);
    killElements(lDeath);


    
    /*
     * for (IModelInstanceObject o : mI.getAllModelInstanceObjects()) {
     * 
     * Object oTemp = o.getObject();
     * 
     * if (oTemp instanceof Building) { Building ab = (Building) oTemp;
     * 
     * if (ab.getbPU() == null) { System.out.println("STOOOOOP");
     * 
     * createLink(ab, bPU); } }
     * 
     * }
     */

    // System.out.println(this.toString());

    return lIME;

  }

  public String toString() {

    return "Nombre batiments : " + lAB.size() + "   nombre instance bâtiments "
        + mIEBat.size() + "   nombre empreinte bâtiment " + mIEFootP.size()
        + " nombre entité modèle " + mI.getAllModelInstanceObjects().size();

  }

  private List<IModelInstanceObject> addElements(
      List<? extends AbstractBuilding> lBorn) {

    List<IModelInstanceObject> lIME = new ArrayList<>();

    for (AbstractBuilding aB : lBorn) {

      if (lAB.contains(aB)) {
        continue;
      }

      lAB.add(aB);

      try {

        IModelInstanceObject iME = (IModelInstanceObject) mI
            .addModelInstanceElement(aB);

        AbstractBuilding abTemp = (AbstractBuilding) iME.getObject();

        createLink(abTemp, bPU);

        mIEBat.add(iME);
        mIEFootP.add(mI.addModelInstanceElement(((AbstractBuilding) iME
            .getObject()).getFootprint()));

        lIME.add(iME);

      } catch (TypeNotFoundInModelException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      /*
       * 
       * int indTemp = lAB.size() - 1; IModelInstanceObject iME =
       * (IModelInstanceObject) mIE.get(indTemp); if
       * (!lAB.get(indTemp).toString().equals(iME.getObject().toString())) {
       * System.out.println("Diffère");
       * 
       * try { aB.setbPU(bPU); IModelInstanceElement iMetemp =
       * mI.addModelInstanceElement(aB); System.out.println("Stem"); } catch
       * (TypeNotFoundInModelException e) { // TODO Auto-generated catch block
       * e.printStackTrace(); }
       * 
       * 
       * 
       * 
       * }
       */

    }

    return lIME;
  }

  private void createLink(AbstractBuilding aB, BasicPropertyUnit bPU) {

    if (aB instanceof Building) {
      bPU.getBuildings().add((Building) aB);
      aB.setbPU(bPU);
    }

    for (CadastralParcel cP : bPU.getCadastralParcel()) {
      for (SubParcel cB : cP.getSubParcel()) {

        cB.getBuildingsParts().add(aB);

      }
    }
  }

  private void removeLink(AbstractBuilding aB, BasicPropertyUnit bPU) {

    if (aB instanceof Building) {
      bPU.getBuildings().remove((Building) aB);
      aB.setbPU(null);
    }

    for (CadastralParcel cP : bPU.getCadastralParcel()) {
      for (SubParcel cB : cP.getSubParcel()) {

        cB.getBuildingsParts().remove(aB);

      }
    }

  }

  private void killElements(List<? extends AbstractBuilding> lDeath) {

    for (AbstractBuilding a : lDeath) {

      int ind = lAB.indexOf(a);

      if (ind == -1) {
        System.out.println(CacheModelInstance.class.getCanonicalName() + " : Objet à tuer absent du cache");
        continue;

      }

      // L'élément n'est pas dans le nouvelle liste
      lAB.remove(ind);

      IModelInstanceObject iIO = (IModelInstanceObject) mIEBat.remove(ind);
      mI.removeModelInstanceElement(iIO);

      IModelInstanceObject iFP = (IModelInstanceObject) mIEFootP.remove(ind);
      mI.removeModelInstanceElement(iFP);

      AbstractBuilding aBTemp = (AbstractBuilding) iIO.getObject();

      removeLink(aBTemp, bPU);

      aBTemp.setGeom(null);

    }

  }

  public List<AbstractBuilding> getlAB() {
    return lAB;
  }

  public List<IModelInstanceElement> getmIE() {
    return mIEBat;
  }

  public IModelInstance getmI() {
    return mI;
  }

}
