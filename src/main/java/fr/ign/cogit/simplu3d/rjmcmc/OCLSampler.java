package fr.ign.cogit.simplu3d.rjmcmc;

import java.util.List;

import fr.ign.mpp.DirectSampler;
import fr.ign.mpp.configuration.Configuration;
import fr.ign.mpp.configuration.Modification;
import fr.ign.mpp.kernel.ObjectSampler;
import fr.ign.rjmcmc.acceptance.Acceptance;
import fr.ign.rjmcmc.distribution.Distribution;
import fr.ign.rjmcmc.kernel.Kernel;
import fr.ign.rjmcmc.kernel.SimpleObject;
import fr.ign.rjmcmc.sampler.GreenSampler;
import fr.ign.simulatedannealing.temperature.Temperature;

public class OCLSampler<O extends SimpleObject, C extends Configuration<O>, D extends Distribution, T extends Temperature, S extends ObjectSampler<O>>
    extends GreenSampler<O, C, D, T, S> {

  public OCLSampler(DirectSampler<O, C, D, S> d, Acceptance<T> a,
      List<Kernel<O, C, Modification<O, C>>> k) {
    super(d, a, k);
  }

  @Override
  protected boolean checkConfiguration() {
    return true;
  }
}
