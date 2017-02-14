package fr.ign.cogit.simplu3d.rjmcmc.cuboid.sampler;

import org.apache.commons.math3.random.RandomGenerator;

import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.mpp.kernel.ObjectBuilder;
import fr.ign.mpp.kernel.ObjectSampler;
import fr.ign.rjmcmc.kernel.Transform;
import fr.ign.rjmcmc.kernel.Variate;

public class MixAbstractBuildingSampler implements ObjectSampler<AbstractSimpleBuilding> {

	RandomGenerator engine;
	double p_simple;
	AbstractSimpleBuilding object;
	Variate variate;
	Transform transformBand2;
	Transform transformBand1;
	ObjectBuilder<AbstractSimpleBuilding> builder1;
	ObjectBuilder<AbstractSimpleBuilding> builder2;
	Class<?> c1;
	Class<?> c2;

	public MixAbstractBuildingSampler(RandomGenerator e, double p_simple, Transform transformBand1, Transform transformBand2,
			ObjectBuilder<AbstractSimpleBuilding> builder1, ObjectBuilder<AbstractSimpleBuilding> builder2,
			Class<? extends AbstractSimpleBuilding> c1, Class<? extends AbstractSimpleBuilding> c2) {
		this.engine = e;
		this.p_simple = p_simple;
		this.transformBand2 = transformBand2;
		this.transformBand1 = transformBand1;
		this.variate = new Variate(e);
		this.builder1 = builder1;
		this.builder2 = builder2;
		this.c1 = c1;
		this.c2 = c2;
	}

	@Override
	public double sample(RandomGenerator e) {
		double[] val0;
		double[] val1;
		if (engine.nextDouble() < p_simple) {
			val0 = new double[builder2.size()];
			val1 = new double[builder2.size()];
			double phi = this.variate.compute(val0, 0);
			double jacob = this.transformBand2.apply(true, val0, val1);
			this.object = builder2.build(val1);
			return phi / jacob;
		}
		val0 = new double[builder1.size()];
		val1 = new double[builder1.size()];
		double phi = this.variate.compute(val0, 0);
		double jacob = this.transformBand1.apply(true, val0, val1);
		this.object = builder1.build(val1);
		return phi / jacob;
	}

	@Override
	public double pdf(AbstractSimpleBuilding t) {
		if (c2 != null && c2.isInstance(t)) {
			double[] val1 = new double[builder2.size()];
			builder2.setCoordinates(t, val1);
			double[] val0 = new double[builder2.size()];
			double J10 = this.transformBand2.apply(false, val1, val0);
			double pdf = this.variate.pdf(val0, 0);
			return pdf * J10;
		}

		double[] val1 = new double[builder1.size()];
		builder1.setCoordinates(t, val1);
		double[] val0 = new double[builder1.size()];
		double J10 = this.transformBand1.apply(false, val1, val0);
		if (J10 == 0) {
			return 0;
		}
		double pdf = this.variate.pdf(val0, 0);
		return pdf * J10;
	}

	@Override
	public AbstractSimpleBuilding getObject() {
		return this.object;
	}

}
