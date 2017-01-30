package fr.ign.cogit.simplu3d.experiments.openmole;

public class TaskExploPSE {
  
  
 public  double energyTot;
 public double coverageRatio;
 public double gini;
 public double moran;
 public double entropy;
 public double boxCount;
 public double maxHeight;
 public double densite;
 public double profileMoran ;


 public TaskExploPSE(double energyTot, double coverageRatio, double gini,
     double moran, double entropy, double boxCount, double maxHeight,
     double densite, double profileMoran) {
   super();
   this.energyTot = energyTot;
   this.coverageRatio = coverageRatio;
   this.gini = gini;
   this.moran = moran;
   this.entropy = entropy;
   this.boxCount = boxCount;
   this.maxHeight = maxHeight;
   this.densite = densite;
   this.profileMoran = profileMoran;
 }

  
  
  @Override
  public String toString() {
    return "TaskExploPSE [energyTot=" + energyTot + ", coverageRatio="
        + coverageRatio + ", gini=" + gini + ", moran=" + moran + ", entropy="
        + entropy + ", boxCount=" + boxCount + ", maxHeight=" + maxHeight
        + ", densite=" + densite + ", profileMoran=" + profileMoran + "]";
  }




  public double getEnergyTot() {
    return energyTot;
  }


  public double getCoverageRatio() {
    return coverageRatio;
  }


  public double getGini() {
    return gini;
  }


  public double getMoran() {
    return moran;
  }


  public double getEntropy() {
    return entropy;
  }


  public double getBoxCount() {
    return boxCount;
  }


  public double getMaxHeight() {
    return maxHeight;
  }


  public double getDensite() {
    return densite;
  }
  


  public double getProfileMoran() {
    return profileMoran;
  }







  
}
