package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation.birth;

import org.apache.commons.math3.random.RandomGenerator;
import org.apache.log4j.Logger;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.mpp.kernel.ObjectBuilder;
import fr.ign.mpp.kernel.ObjectSampler;
import fr.ign.rjmcmc.kernel.SimpleObject;
import fr.ign.rjmcmc.kernel.Transform;
import fr.ign.rjmcmc.kernel.Variate;

public class UniformBirthInGeom<T extends SimpleObject> implements ObjectSampler<T> {
  /**
   * Logger.
   */
  static Logger LOGGER = Logger.getLogger(UniformBirthInGeom.class.getName());

  Transform transform;
  int dimension;
  ObjectBuilder<T> builder;
  Variate<T> variate;
  T object;

  public UniformBirthInGeom(T a, T b, ObjectBuilder<T> builder, IGeometry geom) {
    this.dimension = a.size();
    double[] d = new double[a.size()];
    for (int i = 0; i < a.size(); i++) {
      d[i] = ((Double) b.toArray()[i]).doubleValue() - ((Double) a.toArray()[i]).doubleValue();
    }
    double[] coordinates = new double[a.size()];
    builder.setCoordinates(a, coordinates);
    this.transform = new TransformToSurface(d, coordinates, geom);
    this.builder = builder;
    this.variate = new Variate<T>(this.builder.size());
  }

  @Override
  public double sample(RandomGenerator e) {
    double[] val0 = new double[this.dimension];
    double[] val1 = new double[this.dimension];
    double phi = variate.compute(e, val0);
    double jacob = this.transform.apply(val0, val1);
    this.object = this.builder.build(val1);
    return phi * jacob;
  }

  @Override
  public double pdf(T t) {
    double[] coordinates = new double[this.dimension];
    this.builder.setCoordinates(t, coordinates);
    double[] val = new double[this.dimension];
    double jacob = this.transform.inverse(coordinates, val);
    double pdf = this.variate.pdf(val);
    return pdf * jacob;
  }

  public Transform getTransform() {
    return this.transform;
  }

  public Variate<T> getVariate() {
    return this.variate;
  }

  @Override
  public T getObject() {
    return this.object;
  }
}
