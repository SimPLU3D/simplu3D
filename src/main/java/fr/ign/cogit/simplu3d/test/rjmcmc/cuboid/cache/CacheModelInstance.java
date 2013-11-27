package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.cache;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import tudresden.ocl20.pivot.modelinstance.IModelInstance;
import tudresden.ocl20.pivot.modelinstancetype.exception.TypeNotFoundInModelException;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceElement;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceObject;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.SubParcel;

public class CacheModelInstance<O extends AbstractBuilding> {
  /**
   * Logger.
   */
  static Logger LOGGER = Logger.getLogger(CacheModelInstance.class.getName());

  private List<O> lAB = new ArrayList<>();
  private List<IModelInstanceElement> mIEBat = new ArrayList<>();
  private List<IModelInstanceElement> mIEFootP = new ArrayList<>();

  private IModelInstance mI = null;
  private BasicPropertyUnit bPU;

  public CacheModelInstance(BasicPropertyUnit bPU, IModelInstance mI) {
    this.mI = mI;
    this.bPU = bPU;
  }

  public List<IModelInstanceObject> update(final List<O> lBorn, final List<O> lDeath) {
    LOGGER.debug("Cache content Before update");
    for (O o : this.lAB) {
      LOGGER.debug(o);
    }
    LOGGER.debug("*******Born list*******");
    for (AbstractBuilding ab : lBorn) {
      LOGGER.debug(ab);
    }
    LOGGER.debug("**********Kill list********");
    for (AbstractBuilding ab : lDeath) {
      LOGGER.debug(ab);
    }

    List<O> birthCopy = new ArrayList<O>(lBorn);
    List<O> deathCopy = new ArrayList<O>(lDeath);
    int nbElem = deathCopy.size();
    for (int i = 0; i < nbElem; i++) {
      O a = deathCopy.get(i);
      boolean isRem = birthCopy.remove(a);
      if (isRem) {
        deathCopy.remove(i);
        i--;
        nbElem--;
        LOGGER.error("Object both in birth and death");
      }
    }
    List<IModelInstanceObject> lIME = addElements(birthCopy);
    killElements(deathCopy);
    /*
     * for (IModelInstanceObject o : mI.getAllModelInstanceObjects()) {
     * Object oTemp = o.getObject();
     * if (oTemp instanceof Building) { Building ab = (Building) oTemp;
     * if (ab.getbPU() == null) { System.out.println("STOOOOOP");
     * createLink(ab, bPU); } }
     * }
     */
    LOGGER.debug(this.toString());
    return lIME;
  }

  public String toString() {
    return "Nombre batiments : " + lAB.size() + "   nombre instance bâtiments " + mIEBat.size()
        + "   nombre empreinte bâtiment " + mIEFootP.size() + " nombre entité modèle "
        + mI.getAllModelInstanceObjects().size();
  }

  private List<IModelInstanceObject> addElements(List<O> lBorn) {
    List<IModelInstanceObject> lIME = new ArrayList<>();
    for (O aB : lBorn) {
      if (lAB.contains(aB)) {
        LOGGER.error("Object already in cache " + aB);
        continue;
      }
      lAB.add(aB);
      try {
        IModelInstanceObject iME = (IModelInstanceObject) mI.addModelInstanceElement(aB);
        AbstractBuilding abTemp = (AbstractBuilding) iME.getObject();
        createLink(abTemp, bPU);
        mIEBat.add(iME);
        mIEFootP.add(mI.addModelInstanceElement(abTemp.getFootprint()));
        lIME.add(iME);
      } catch (TypeNotFoundInModelException e) {
        e.printStackTrace();
      }
      /*
       * int indTemp = lAB.size() - 1; IModelInstanceObject iME =
       * (IModelInstanceObject) mIE.get(indTemp); if
       * (!lAB.get(indTemp).toString().equals(iME.getObject().toString())) {
       * System.out.println("Diffère");
       * try { aB.setbPU(bPU); IModelInstanceElement iMetemp =
       * mI.addModelInstanceElement(aB); System.out.println("Stem"); } catch
       * (TypeNotFoundInModelException e) { // TODO Auto-generated catch block
       * e.printStackTrace(); }
       * }
       */
    }
    for (O aB : lBorn) {
      if (lAB.indexOf(aB) == -1) {
        LOGGER.error("Could not find : " + aB);
      }
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

  private void killElements(List<O> lDeath) {
    for (O a : lDeath) {
      int ind = lAB.indexOf(a);
      if (ind == -1) {
        LOGGER.error(CacheModelInstance.class.getCanonicalName()
            + " : Objet à tuer absent du cache");
        LOGGER.error("Object to remove " + a);
        LOGGER.error("Cache content = ");
        for (O o : this.lAB) {
          LOGGER.error(o);
        }
        System.exit(0);
        continue;
      }
      // L'élément n'est pas dans la nouvelle liste
      lAB.remove(ind);
      IModelInstanceObject iIO = (IModelInstanceObject) mIEBat.remove(ind);
      AbstractBuilding aBTemp = (AbstractBuilding) iIO.getObject();
      removeLink(aBTemp, bPU);
      mI.removeModelInstanceElement(iIO);
      IModelInstanceObject iFP = (IModelInstanceObject) mIEFootP.remove(ind);
      mI.removeModelInstanceElement(iFP);
      aBTemp.setGeom(null);
    }
  }

  public List<O> getlAB() {
    return lAB;
  }

  public List<IModelInstanceElement> getmIE() {
    return mIEBat;
  }

  public IModelInstance getmI() {
    return mI;
  }
}
