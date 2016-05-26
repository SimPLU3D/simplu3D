package fr.ign.cogit.simplu3d.experiments.enau.sampler;

import org.apache.commons.math3.random.RandomGenerator;

import fr.ign.cogit.simplu3d.experiments.enau.geometry.DeformedCuboid;
import fr.ign.mpp.kernel.ObjectBuilder;
import fr.ign.mpp.kernel.ObjectSampler;
import fr.ign.rjmcmc.kernel.Transform;
import fr.ign.rjmcmc.kernel.Variate;

public class ParalledDeformedSampler implements ObjectSampler<DeformedCuboid> {
	RandomGenerator engine;
	DeformedCuboid object;
	Variate variate;
	Transform transformBand1;
	ObjectBuilder<? extends DeformedCuboid> builder1;

	public ParalledDeformedSampler(RandomGenerator e, Transform transformBand1,
			ObjectBuilder<? extends DeformedCuboid> builder) {
		this.engine = e;

		this.transformBand1 = transformBand1;
		this.variate = new Variate(e);
		this.builder1 = builder;

	}

	@Override
	public double sample(RandomGenerator e) {
		double[] val0;
		double[] val1;

		val0 = new double[builder1.size()];
		val1 = new double[builder1.size()];
		double phi = this.variate.compute(val0, 0);
		double jacob = this.transformBand1.apply(true, val0, val1);
		this.object = builder1.build(val1);
		return phi / jacob;
	}

	@SuppressWarnings("unchecked")
	@Override
	public double pdf(DeformedCuboid t) {
		double[] val1 = new double[builder1.size()];
		((ObjectBuilder<DeformedCuboid>) builder1).setCoordinates(t, val1);
		double[] val0 = new double[builder1.size()];
		double J10 = this.transformBand1.apply(false, val1, val0);
		if (J10 == 0) {
			return 0;
		}
		double pdf = this.variate.pdf(val0, 0);
		return pdf * J10;
	}

	@Override
	public DeformedCuboid getObject() {
		return this.object;
	}
}