package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid;

import java.util.List;

import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceObject;
import fr.ign.cogit.simplu3d.checker.VeryFastRuleChecker;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.cache.CacheModelInstance;
import fr.ign.mpp.DirectSampler;
import fr.ign.mpp.configuration.Configuration;
import fr.ign.mpp.configuration.Modification;
import fr.ign.mpp.kernel.ObjectSampler;
import fr.ign.rjmcmc.acceptance.Acceptance;
import fr.ign.rjmcmc.distribution.Distribution;
import fr.ign.rjmcmc.kernel.Kernel;
import fr.ign.rjmcmc.kernel.KernelFunctor;
import fr.ign.rjmcmc.kernel.SimpleObject;
import fr.ign.rjmcmc.sampler.GreenSampler;
import fr.ign.simulatedannealing.temperature.Temperature;

public class SimpleGreenSampler<O extends SimpleObject, C extends Configuration<O>, D extends Distribution, T extends Temperature, S extends ObjectSampler<O>>
    extends GreenSampler<O, C, D, T, S> {

  // FastRuleChecker fRC;

  VeryFastRuleChecker vFR;

  CacheModelInstance cMI;

  public SimpleGreenSampler(DirectSampler<O, C, D, S> d, Acceptance<T> a,
      List<Kernel<O, C, Modification<O, C>>> k, BasicPropertyUnit bPU) {
    super(d, a, k);

    vFR = new VeryFastRuleChecker(bPU);

    // fRC = new FastRuleChecker(bPU);

    // cMI = new CacheModelInstance(fRC.getBPu(), fRC.getMi());

    cMI = new CacheModelInstance(vFR.getbPU(), vFR.getlModeInstance().get(0));

  }

  protected boolean checkConfiguration(
      KernelFunctor<O, C, Modification<O, C>> kf) {
    /*
     * for (AbstractBuilding aB : (List<AbstractBuilding>) kf.getModif()
     * .getBirth()) {
     * 
     * IDirectPositionList dpl = aB.getFootprint().coord();
     * 
     * for (IDirectPosition dp : dpl) {
     * 
     * try { if (!vFR .getbPU() .getGeomJTS() .contains(
     * geometryFactory.createPoint(new Coordinate(dp.getX(), dp .getY())))) {
     * 
     * return false; } } catch (Exception e) { // TODO Auto-generated catch
     * block e.printStackTrace(); }
     * 
     * }
     * 
     * }
     */
    // return true;
    // On applique les modifications
    /*
     * cMI.update((List<AbstractBuilding>) kf.getModif().getBirth(),
     * (List<AbstractBuilding>) kf.getModif().getDeath());
     */

    List<IModelInstanceObject> lst = cMI.update((List<AbstractBuilding>) kf
        .getModif().getBirth(), (List<AbstractBuilding>) kf.getModif()
        .getDeath());
    /*
    for (IModelInstanceObject ins : lst) {

      Object o = ins.getObject();

      if (o instanceof Building) {
        System.out.println("BPU : " + ((Building) o).getbPU());

        if (((Building) o).getbPU() == null) {

          cMI.update((List<AbstractBuilding>) kf.getModif().getDeath(),
              (List<AbstractBuilding>) kf.getModif().getBirth());
          List<IModelInstanceObject> lst2 = cMI.update(
              (List<AbstractBuilding>) kf.getModif().getBirth(),
              (List<AbstractBuilding>) kf.getModif().getDeath());

          for (IModelInstanceObject ins2 : lst2) {

            Object o2 = ins2.getObject();

            if (o2 instanceof Building) {
              System.out.println("BPU : " + ((Building) o2).getbPU());

            }

          }
        }

      }

    }*/

    boolean isCheck = vFR.check(lst);

    if (!isCheck) {
      // Ce n'est pas vérifié, on fait marche arrière
      cMI.update((List<AbstractBuilding>) kf.getModif().getDeath(),
          (List<AbstractBuilding>) kf.getModif().getBirth());

    }

    return isCheck;
  }
}
