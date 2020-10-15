package fr.ign.cogit.simplu3d.rjmcmc.generic.sampler;

import java.util.List;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.ign.mpp.DirectSampler;
import fr.ign.mpp.configuration.AbstractBirthDeathModification;
import fr.ign.mpp.configuration.AbstractGraphConfiguration;
import fr.ign.rjmcmc.acceptance.Acceptance;
import fr.ign.rjmcmc.kernel.Kernel;
import fr.ign.rjmcmc.kernel.KernelFunctor;
import fr.ign.rjmcmc.kernel.RandomApply;
import fr.ign.rjmcmc.kernel.RandomApplyResult;
import fr.ign.rjmcmc.kernel.SimpleObject;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.temperature.Temperature;

/**
 * 
 *        This software is released under the licence CeCILL
 * 
 *        see LICENSE.TXT
 * 
 *        see  http://www.cecill.info/
 * 
 * 
 * 
 * copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 *
 * 
 * Sampler bloquant la diminution de la température si une configuration ne
 * vérifie pas les résutlats
 * 
 *
 * @param <O> the class of object to sample
 */
public class GreenSamplerBlockTemperature<O extends SimpleObject, C extends AbstractGraphConfiguration<O, C, M>, M extends AbstractBirthDeathModification<O, C, M>>
		implements Sampler<C, M> {
	/**
	 * Logger.
	 */
	static Logger LOGGER = LogManager.getLogger(GreenSamplerBlockTemperature.class
			.getName());

	public static Logger getLOGGER() {
		return LOGGER;
	}

	DirectSampler<O, C, M> density;
	Acceptance<? extends Temperature> acceptance;
	Temperature temperature;
	protected List<Kernel<C, M>> kernels;
	AbstractRealDistribution die;
	double refPdfRatio;
	double kernelRatio;
	int kernelId;
	double greenRatio;
	double delta;
	boolean accepted;
	double acceptanceProbability;

	boolean blockedTemperature = false;

	public GreenSamplerBlockTemperature(RandomGenerator rng, DirectSampler<O, C, M> d,
			Acceptance<? extends Temperature> a, List<Kernel<C, M>> k) {
		this.density = d;
		this.acceptance = a;
		this.kernels = k;
		this.die = new UniformRealDistribution(rng, 0, 1);
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
	public void sample(RandomGenerator e, C config, Temperature t) {
		this.temperature = t;
		blockedTemperature = false;

		long start = System.currentTimeMillis();
		M modif = config.newModification();
		KernelFunctor<C, M> kf = new KernelFunctor<>(e, config, modif);
		RandomApplyResult randomApplyResult = RandomApply.randomApply(
				this.die.sample(), this.kernels, kf);
		long end = System.currentTimeMillis();
		this.timeRandomApply = (end - start);
		start = end;
		this.kernelRatio = randomApplyResult.kernelRatio;
		this.kernelId = randomApplyResult.kernelId;
		
	
		this.refPdfRatio = this.density.pdfRatio(config, modif);


		if (refPdfRatio == 0) {
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
		this.acceptanceProbability = this.acceptance.compute(this.delta,
				this.temperature, this.greenRatio);
		this.accepted = (this.die.sample() < this.acceptanceProbability);
		end = System.currentTimeMillis();
		this.timeAcceptance = (end - start);
		start = end;
		this.timeApply = 0;
		if (this.accepted) {
			// config.apply(modif);
			modif.apply(config);
			end = System.currentTimeMillis();
			this.timeApply = (end - start);
			start = end;
		}
	}

	public boolean blockTemperature() {
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

	public List<Kernel<C,M>> getKernels() {
		return this.kernels;
	}
}
