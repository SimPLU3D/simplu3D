package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.sampler;



import java.util.List;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.log4j.Logger;

import fr.ign.mpp.DirectSampler;
import fr.ign.rjmcmc.acceptance.Acceptance;
import fr.ign.rjmcmc.configuration.Configuration;
import fr.ign.rjmcmc.configuration.Modification;
import fr.ign.rjmcmc.kernel.Kernel;
import fr.ign.rjmcmc.kernel.KernelFunctor;
import fr.ign.rjmcmc.kernel.RandomApply;
import fr.ign.rjmcmc.kernel.RandomApplyResult;
import fr.ign.rjmcmc.kernel.SimpleObject;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.temperature.Temperature;

public class GreenSamplerBlockTemperature<O extends SimpleObject> implements Sampler<O> {
  /**
   * Logger.
   */
  static Logger LOGGER = Logger.getLogger(GreenSamplerBlockTemperature.class.getName());

  public static Logger getLOGGER() {
    return LOGGER;
  }

  DirectSampler<O> density;
  Acceptance<? extends Temperature> acceptance;
  Temperature temperature;
  protected List<Kernel<O>> kernels;
  AbstractRealDistribution die;
  double refPdfRatio;
  double kernelRatio;
  int kernelId;
  double greenRatio;
  double delta;
  boolean accepted;
  double acceptanceProbability;
  
  boolean blockedTemperature = false;

  public GreenSamplerBlockTemperature(DirectSampler<O> d, Acceptance<? extends Temperature> a, List<Kernel<O>> k) {
    this.density = d;
    this.acceptance = a;
    this.kernels = k;
    this.die = new UniformRealDistribution(0, 1);
    this.die.reseedRandomGenerator(System.currentTimeMillis());
  }

  long timeRandomApply;
  long timeGreenRatio;
  long timeDelta;
  long timeAcceptance;
  long timeApply;

  @Override
  public long getTimeRandomApply() {
    return timeRandomApply;
  }

  @Override
  public long getTimeGreenRatio() {
    return timeGreenRatio;
  }

  @Override
  public long getTimeDelta() {
    return timeDelta;
  }

  @Override
  public long getTimeAcceptance() {
    return timeAcceptance;
  }

  @Override
  public long getTimeApply() {
    return timeApply;
  }

  @Override
  public void sample(RandomGenerator e, Configuration<O> config, Temperature t) {
    this.temperature = t;
    blockedTemperature= false;
    
    
    
    long start = System.currentTimeMillis();
    Modification<O, Configuration<O>> modif = new Modification<O, Configuration<O>>();
    KernelFunctor<O> kf = new KernelFunctor<O>(e, config, modif);
    RandomApplyResult randomApplyResult = RandomApply.randomApply(this.die.sample(), this.kernels,
        kf);
    long end = System.currentTimeMillis();
    this.timeRandomApply = (end - start);
    start = end;
    this.kernelRatio = randomApplyResult.kernelRatio;
    this.kernelId = randomApplyResult.kernelId;
    this.refPdfRatio = this.density.pdfRatio(config, modif);
    
    if(refPdfRatio == 0){
      blockedTemperature = true;
      this.delta = 0;
      this.accepted = false;
      this.timeDelta = 0;
      this.timeAcceptance = 0;
      this.timeApply = 0;
      return;
    }
    
    
    this.greenRatio = this.kernelRatio * this.refPdfRatio;
    end = System.currentTimeMillis();
    this.timeGreenRatio = (end - start);
    start = end;
    if (this.greenRatio <= 0) {
      this.delta = 0;
      this.accepted = false;
      this.timeDelta = 0;
      this.timeAcceptance = 0;
      this.timeApply = 0;
      return;
    }
    this.delta = config.deltaEnergy(modif);
    end = System.currentTimeMillis();
    this.timeDelta = (end - start);
    start = end;
    this.acceptanceProbability = this.acceptance.compute(this.delta, this.temperature,
        this.greenRatio);
    this.accepted = (this.die.sample() < this.acceptanceProbability);
    end = System.currentTimeMillis();
    this.timeAcceptance = (end - start);
    start = end;
    this.timeApply = 0;
    if (this.accepted) {
      config.apply(modif);
      end = System.currentTimeMillis();
      this.timeApply = (end - start);
      start = end;
    }
  }
  
  
  public boolean blockTemperature(){
    return blockedTemperature;
  }
  

  @Override
  public double acceptanceProbability() {
    return this.acceptanceProbability;
  }

  @Override
  public boolean accepted() {
    return this.accepted;
  }

  @Override
  public double delta() {
    return this.delta;
  }

  @Override
  public double greenRatio() {
    return this.greenRatio;
  }

  @Override
  public int kernelId() {
    return this.kernelId;
  }

  @Override
  public String kernelName(int i) {
    return this.kernels.get(i).getName();
  }

  @Override
  public double kernelRatio() {
    return this.kernelRatio;
  }

  @Override
  public int kernelSize() {
    return this.kernels.size();
  }

  @Override
  public double refPdfRatio() {
    return this.refPdfRatio;
  }

  @Override
  public Temperature temperature() {
    return this.temperature;
  }

  public List<Kernel<O>> getKernels() {
    return this.kernels;
  }
}
