
package hello;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.contrib.cartetopo.CarteTopo;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Face;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.simplu3d.importer.applicationClasses.CadastralParcelLoader;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;

public class Hello {
  public static void main(String[] args){
    List<String> ls =   prepareFiles("/media/mickael/Data/mbrasebin/donnees/IAUIDF/IMU_ee/");
      for(String s:ls){
          System.out.println(s);
      }
  }
  
  
  public static List<String> prepareFiles(String folder){
      List<String> lS = new ArrayList<>();
      File f = new File(folder);
      
      File[] fTab = f.listFiles();
      
      int nbF = fTab.length;
      
      for(int i=0;i<nbF;i++){
          File fTemp = fTab[i];
          if(fTemp.isDirectory()){
              lS.add(fTemp.getName());
          }
      }
      
      return lS;
      
  }
 

}

