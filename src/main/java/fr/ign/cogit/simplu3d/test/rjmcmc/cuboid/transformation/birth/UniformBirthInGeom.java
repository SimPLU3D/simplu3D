package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.transformation.birth;

import org.apache.log4j.Logger;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.mpp.configuration.Configuration;
import fr.ign.mpp.configuration.Modification;
import fr.ign.mpp.kernel.ObjectBuilder;
import fr.ign.mpp.kernel.ObjectSampler;
import fr.ign.rjmcmc.kernel.SimpleObject;
import fr.ign.rjmcmc.kernel.Transform;
import fr.ign.rjmcmc.kernel.Variate;

public class UniformBirthInGeom<T extends SimpleObject, C extends Configuration<T>, M extends Modification<T, C>>
    implements ObjectSampler<T> {
  /**
   * Logger.
   */
  static Logger LOGGER = Logger.getLogger(UniformBirthInGeom.class.getName());

  Transform transform;
  int dimension;
  ObjectBuilder<T> builder;
  Variate<T, C, M> variate;

  public UniformBirthInGeom(T a, T b, ObjectBuilder<T> builder, IGeometry geom) {

    this.dimension = a.size();
    double[] d = new double[a.size()];

    for (int i = 0; i < a.size(); i++) {
      d[i] = ((Double) b.toArray()[i]).doubleValue()
          - ((Double) a.toArray()[i]).doubleValue();
    }

    double[] coordinates = new double[a.size()];

    builder.setCoordinates(a, coordinates);

    this.transform = new TransformToSurface(d, coordinates,   geom);

    this.builder = builder;

    this.variate = new Variate<T, C, M>(this.builder.size());
  }

  @Override
  public T sample() {
    double[] val0 = new double[this.dimension];
    double[] val1 = new double[this.dimension];
    /* double phi01 = */variate.compute(null, null, val0);
    this.transform.apply(val0, val1);
    return this.builder.build(val1);
    // FIXME return phi01*pdf(t)
  }

  @Override
  public double pdf(T t) {
    double[] coordinates = new double[this.dimension];
    this.builder.setCoordinates(t, coordinates);
    double[] val = new double[this.dimension];
    this.transform.inverse(coordinates, val);
    // String s = "";
    // for (int i = 0; i < coordinates.length; i++)
    // s += coordinates[i] + " ";
    // LOGGER.info("coordinates = " + s);
    // s = "";
    // for (int i = 0; i < val.length; i++)
    // s += val[i] + " ";
    // LOGGER.info("val = " + s);
    return this.variate.pdf(val) * this.transform.getAbsJacobian(val);
  }

  public Transform getTransform() {
    return this.transform;
  }

  public Variate<T, C, M> getVariate() {
    return this.variate;
  }
}
