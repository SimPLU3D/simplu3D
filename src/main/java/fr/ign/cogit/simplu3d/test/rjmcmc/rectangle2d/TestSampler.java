package fr.ign.cogit.simplu3d.test.rjmcmc.rectangle2d;

import java.util.List;

import fr.ign.mpp.DirectSampler;
import fr.ign.rjmcmc.acceptance.Acceptance;
import fr.ign.rjmcmc.kernel.Kernel;
import fr.ign.rjmcmc.kernel.SimpleObject;
import fr.ign.rjmcmc.sampler.GreenSampler;
import fr.ign.simulatedannealing.temperature.Temperature;

public class TestSampler<O extends SimpleObject> extends GreenSampler<O> {

  public TestSampler(DirectSampler<O> d, Acceptance<? extends Temperature> a, List<Kernel<O>> k) {
    super(d, a, k);
  }

}
