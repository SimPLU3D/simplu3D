package fr.ign.cogit.simplu3d.experiments.openmole;

public class TaskResult {
  public double energy;
  public double coverageRatio;
  public long signature;
  public TaskResult(double e, double c, long s) {
    this.energy = e;
    this.coverageRatio = c;
    this.signature = s;
  }
}
