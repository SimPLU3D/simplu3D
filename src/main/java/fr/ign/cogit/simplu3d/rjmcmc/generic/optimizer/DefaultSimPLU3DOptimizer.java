package fr.ign.cogit.simplu3d.rjmcmc.generic.optimizer;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;
import fr.ign.cogit.simplu3d.rjmcmc.generic.visitor.CountVisitor;
import fr.ign.cogit.simplu3d.util.SimpluParameters;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.simulatedannealing.endtest.CompositeEndTest;
import fr.ign.simulatedannealing.endtest.EndTest;
import fr.ign.simulatedannealing.endtest.MaxIterationEndTest;
import fr.ign.simulatedannealing.endtest.StabilityEndTest;
import fr.ign.simulatedannealing.schedule.GeometricSchedule;
import fr.ign.simulatedannealing.schedule.Schedule;
import fr.ign.simulatedannealing.temperature.SimpleTemperature;

public abstract class DefaultSimPLU3DOptimizer<C extends ISimPLU3DPrimitive> implements ISimPLU3DOptimizer<ISimPLU3DPrimitive> {

  protected double coeffDec = Double.NaN;
  protected double deltaConf = Double.NaN;
  protected double minLengthBox = Double.NaN;
  protected double maxLengthBox = Double.NaN;
  protected double minWidthBox = Double.NaN;
  protected double maxWidthBox = Double.NaN;
  protected double energyCreation = Double.NaN;
  protected IGeometry samplingSurface = null;

  public void setSamplingSurface(IGeometry geom) {
    samplingSurface = geom;
  }

  public void setEnergyCreation(double energyCreation) {
    this.energyCreation = energyCreation;
  }

  public void setMinLengthBox(double minLengthBox) {
    this.minLengthBox = minLengthBox;
  }

  public void setMaxLengthBox(double maxLengthBox) {
    this.maxLengthBox = maxLengthBox;
  }

  public void setMinWidthBox(double minWidthBox) {
    this.minWidthBox = minWidthBox;
  }

  public void setMaxWidthBox(double maxWidthBox) {
    this.maxWidthBox = maxWidthBox;
  }

  public void setCoeffDec(double coeffDec) {
    this.coeffDec = coeffDec;
  }

  public void setDeltaConf(double deltaConf) {
    this.deltaConf = deltaConf;
  }

  public EndTest create_end_test(SimpluParameters p) {
  	double loc_deltaconf = Double.isNaN(this.deltaConf) ? p.getDouble("delta") : this.deltaConf;
  	String option =  p.getString("end_test_type").toLowerCase();
    switch (option) {
    case "absolute": return new MaxIterationEndTest(p.getInteger("absolute_nb_iter"));
    case "relative": 
      return new StabilityEndTest<Cuboid>(p.getInteger("relative_nb_iter"), loc_deltaconf);
    case "composite":
    	default:
    		EndTest abs = new MaxIterationEndTest(p.getInteger("absolute_nb_iter"));
    		EndTest rel = new StabilityEndTest<Cuboid>(p.getInteger("relative_nb_iter"), loc_deltaconf);
    		return new CompositeEndTest(abs,rel);
    }
  }

  CountVisitor<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> countV = null;

  public int getCount() {
    return countV.getCount();
  }

  public Schedule<SimpleTemperature> create_schedule(SimpluParameters p) {
    double coefDef = (Double.isNaN(this.coeffDec)) ? p.getDouble("deccoef") : this.coeffDec;
    return new GeometricSchedule<SimpleTemperature>(new SimpleTemperature(p.getDouble("temp")), coefDef);
  }
}
