package fr.ign.cogit.simplu3d.experiments.openmole.diversity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;

public class EntropyIndicator {

  double giniFinal = -2.0;
  double moranFinal = -2.0;
  double entropyRelFinal = -2.0;

  public static void main(String[] args) {

    Environnement env = Environnement.createEnvironnement();

    List<Double> energy_parcels = new ArrayList<>();

    IFeatureCollection<IFeature> featCOut = new FT_FeatureCollection<>();
    double energyTot = 0;
    double areaTot = 0;
    Random randomnum =new Random();
    
    for (int i = 0; i < 20; i++) {

      for (int j = 0; j < 20; j++) {

        // A chaque case

        double height;
        if (randomnum.nextInt(5)>3  ) { //damier (i+j) % 2 ==0 // random  : randomnum.nextint(2)>0 
          height = 20;
        } else {
          height = 0;
        }

        Cuboid c = new Cuboid(i * 20, j * 20, 20, 20, height, 0);
        c.setGeom(c.getFootprint());
        
        AttributeManager.addAttribute(c, "Hauteur", height, "Double");
        featCOut.add(c);
        
        
        

        BasicPropertyUnit bPU = new BasicPropertyUnit();
        bPU.setGeom(c.getFootprint());
        bPU.setPol2D((IPolygon) FromGeomToSurface.convertGeom(c.getFootprint()).get(0));

        env.getBpU().add(bPU);
        energy_parcels.add(- c.getHeight() * c.length * c.width);

        areaTot = areaTot + c.getFootprint().area();
        energyTot = energyTot - c.getHeight() * c.length * c.width;
        
      }

    }

    EntropyIndicator eI = new EntropyIndicator();
    eI.calculate(env, energy_parcels, areaTot, energyTot);
    
    System.out.println(eI.toString());
    
    ShapefileWriter.write(featCOut, "/home/pchapron/temp/outdamier.shp");
    
    
  }

  public EntropyIndicator() {

  }
  
  
  

  @Override
  public String toString() {
    return "EntropyIndicator [giniFinal=" + giniFinal + ", moranFinal="
        + moranFinal + ", entropyRelFinal=" + entropyRelFinal + "]";
  }

  public void calculate(Environnement env, List<Double> energy_parcels,
      double areaTot, double energyTot) {
    // calculs des indicateurs Gini, Moran et entropie relative
    int nbparcelles = env.getBpU().size();

    double gini = 0;
    double[] density_vals = new double[nbparcelles];
    double density_tot = 0;
    int nb_non_empty_parcels = 0;

    // TODO verifier que le remplissage des deux collections (parcelles et
    // energies) se fait dans le même ordre
    // et que energy_parcel[i] est bien l'energie de la parcelle
    // env.getBpu.get(i)
    for (int i = 0; i < nbparcelles; i++) {

      BasicPropertyUnit parcelle = env.getBpU().get(i);
      double energy_parcel = energy_parcels.get(i);
      double surface_parcel = parcelle.getArea();

      //System.out.println("parcelle i " + i + " energie" + energy_parcel);

      // one way of computing gini indice is taking sum for every parcels
      // of abs of surface proportion - minus variable (here energy)
      gini += Math.abs(surface_parcel / areaTot - energy_parcel / energyTot);

      // density values for entropy calculation (non empty parcels)
      if (energy_parcel != 0) {
        //System.out.println("parcelle non vide ! ");
        nb_non_empty_parcels += 1;
        density_vals[i] = -energy_parcel / surface_parcel;
        //System.out.println("parcelle non vide ! ");

        density_tot += density_vals[i];
      }
    }
    // Gini
    gini *= 0.5;

    this.giniFinal = gini;

    //System.out.println("gini de la zone " + gini);

    // distance matrix between parcels using centroids of parcels
    double[][] dist_mat = new double[nbparcelles][nbparcelles];
    for (int i = 0; i < nbparcelles; i++) {
      for (int j = 0; j < nbparcelles; j++) {
        if (i == j) {
          dist_mat[i][j] = 0;
        } else {
          IDirectPosition centro_i = env.getBpU().get(i).getGeom().centroid();
          IDirectPosition centro_j = env.getBpU().get(j).getGeom().centroid();
          dist_mat[i][j] = centro_i.distance2D(centro_j);
        }
      }
    }

    // sum of inverse distances (weights of moran)
    double sum_weights = 0;
    for (int i = 0; i < nbparcelles; i++) {
      for (int j = 0; j < nbparcelles; j++) {
        if (i != j) {
          sum_weights += 1.0 / dist_mat[i][j];
        }
      }
    }

    // Moran
    double mean_energy = energyTot / nbparcelles;
    double numer = 0;
    double denom = 0;
    double moran = 0 ;
    for (int i = 0; i < nbparcelles; i++) {
      for (int j = 0; j < nbparcelles; j++) {
        if (i != j) {
          numer += (energy_parcels.get(i) - mean_energy) * (energy_parcels.get(j) - mean_energy) / dist_mat[i][j];
         // denom += ((energy_parcels.get(i) - mean_energy) * (energy_parcels.get(i) - mean_energy));
          
        }
      }
     denom += ((energy_parcels.get(i) - mean_energy) * (energy_parcels.get(i) - mean_energy));
    }
    moran = numer /denom ;
    
    moran *= (nbparcelles / sum_weights);

    
    
    this.moranFinal = moran;

    // moran should be between -1 and 1
    if (moran > 1 || moran < -1) {
      System.out.println("###### erreur dans le calcul de moran :" + moran);
    }
    //System.out.println("moran de la zone " + moran);

    // ne fonctionne pas pour le moment 
    // relative entropy ONLY for non empty parcels otherwise NaN
//
//    double[] proba_density = new double[nbparcelles];
//    double relative_entropy = 0;
//
//    for (int i = 0; i < nbparcelles; i++) {
//      //if (density_vals[i] > 0) {
//        
//      if (energy_parcels.get(i)<0){
//        density_vals[i] = - energy_parcels.get(i)/  env.getBpU().get(i).getArea();
//         proba_density[i] = density_vals[i] / density_tot;
//         System.out.println("prob densité pour i="+i+" : "+ proba_density[i]);
//         
//         relative_entropy += proba_density[i] * Math.log(1 / proba_density[i])/Math.log(nb_non_empty_parcels);;
//      }
//
//    }
//    System.out.println("densité totale" + density_tot);
//    
//    this.entropyRelFinal = relative_entropy;
 this.entropyRelFinal = Double.NaN;
    
  }

  public double getGiniFinal() {
    return giniFinal;
  }

  public double getMoranFinal() {
    return moranFinal;
  }

  public double getEntropyRelFinal() {
    return entropyRelFinal;
  }

}
