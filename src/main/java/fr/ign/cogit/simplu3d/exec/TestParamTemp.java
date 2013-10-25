package fr.ign.cogit.simplu3d.exec;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.OCLBuildingsCuboid2;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.impl.Cuboid2;
import fr.ign.mpp.configuration.Configuration;
import fr.ign.parameters.Parameters;

public class TestParamTemp {

  /**
   * @param args
   */

  // [building_footprint_rectangle_cli_main
  public static void main(String[] args) throws Exception {

    String folderName = "./src/main/resources/scenario/";

    String fileName = "building_parameters_project_4.xml";

    String csvFile = "E:/Experimentation/export.csv";
    
    
    int nbInter = 20;
    
    double bMin =  0.999;
    double bMax = 0.9999999;
    
    List<Double> ld = new ArrayList<>();
    
    
    for(int i=0;i<nbInter){
      
      
      
      
    }
    
    
    
    
    
    
    

    double[] valCoeff = { 0.999, 0.9995, 0.9999 };

    int nbIt = 50;

    Path path = Paths.get(csvFile);

    BufferedWriter writer = Files.newBufferedWriter(path,
        StandardCharsets.UTF_8, StandardOpenOption.CREATE);
    
    
    
    String s = "Coeff,Iteration,Time,Energy";
        

    writer.append(s);
    writer.newLine();

    Parameters p = initialize_parameters(folderName + fileName);

    for (int i = 0; i < valCoeff.length; i++) {
      
//      writer.append(valCoeff[i] + ";");
      
      

      for (int j = 0; j < nbIt; j++) {
        Environnement env = LoaderSHP.load(p.get("folder"));
        
        
        
        
        

        OCLBuildingsCuboid2 ocb = new OCLBuildingsCuboid2();
        ocb.setCoeffDec(valCoeff[i]);

        double timeMs =  System.currentTimeMillis();
        
        Configuration<Cuboid2> cc = ocb.process(env.getBpU().get(1), p, env, 1);
        
        
        writer.append(valCoeff[i] + ","+ocb.getCount()+ ","+    ( System.currentTimeMillis() - timeMs) + "," +  cc.getEnergy());

        writer.newLine();
        writer.flush();
        
        
   
        
      }
      


    }
    writer.close();

  }

  private static Parameters initialize_parameters(String name) {
    return Parameters.unmarshall(name);
  }
}
