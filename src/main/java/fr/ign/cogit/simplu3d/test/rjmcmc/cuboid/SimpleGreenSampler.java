package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid;

import java.util.List;

import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceObject;
import fr.ign.cogit.simplu3d.checker.VeryFastRuleChecker;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
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

  public CacheModelInstance cMI;

  public SimpleGreenSampler(DirectSampler<O, C, D, S> d, Acceptance<T> a,
      List<Kernel<O, C, Modification<O, C>>> k, BasicPropertyUnit bPU) {
    super(d, a, k);

    vFR = new VeryFastRuleChecker(bPU);

    // fRC = new FastRuleChecker(bPU);

    // cMI = new CacheModelInstance(fRC.getBPu(), fRC.getMi());:!

    cMI = new CacheModelInstance(vFR.getbPU(), vFR.getlModeInstance().get(0));

  }

  public boolean checkNonUpdateConfiguration(
      KernelFunctor<O, C, Modification<O, C>> kf) {

    List<IModelInstanceObject> lst = cMI.update((List<AbstractBuilding>) kf
        .getModif().getBirth(), (List<AbstractBuilding>) kf.getModif()
        .getDeath());

    boolean isCheck = vFR.check(lst);

    // Ce n'est pas vérifié, on fait marche arrière
    cMI.update((List<AbstractBuilding>) kf.getModif().getDeath(),
        (List<AbstractBuilding>) kf.getModif().getBirth());

    return isCheck;
  }

  public boolean checkConfiguration(KernelFunctor<O, C, Modification<O, C>> kf) {

    List<IModelInstanceObject> lst = cMI.update((List<AbstractBuilding>) kf
        .getModif().getBirth(), (List<AbstractBuilding>) kf.getModif()
        .getDeath());

    boolean isCheck = vFR.check(lst);

    if (!isCheck) {
      // Ce n'est pas vérifié, on fait marche arrière
      cMI.update((List<AbstractBuilding>) kf.getModif().getDeath(),
          (List<AbstractBuilding>) kf.getModif().getBirth());

    }

    return isCheck;
  }

  public List<Kernel<O, C, Modification<O, C>>> getKernels() {
    return this.kernels;
  }

}
