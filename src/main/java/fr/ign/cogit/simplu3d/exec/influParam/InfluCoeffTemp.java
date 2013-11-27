package fr.ign.cogit.simplu3d.exec.influParam;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.OCLBuildingsCuboidFinal;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.convert.GenerateSolidFromCuboid;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.impl.Cuboid2;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.parameters.Parameters;
import fr.ign.rjmcmc.configuration.Configuration;

public class InfluCoeffTemp {

  /**
   * @param args
   */

  // [building_footprint_rectangle_cli_main
  public static void main(String[] args) throws Exception {

    String folderName = "./src/main/resources/scenario/";

    String fileName = "building_parameters_project_expthese_1.xml";

    Parameters p = initialize_parameters(folderName + fileName);


    int nbInter = 5;

    double bMin = 0;
    double bMax = 0.99;
    
    int count = 0;

    List<Double> ld = new ArrayList<>();

    for (int i = 0; i < nbInter; i++) {

      ld.add(i * (bMax - bMin) / nbInter + bMin);

    }

 //   ld.add(bMax);

    Object[] valCoeff = ld.toArray();

    int nbIt = 10;


    for (int i = 0; i < valCoeff.length; i++) {

      // writer.append(valCoeff[i] + ";");

      for (int j = 0; j < nbIt; j++) {
        Environnement env = LoaderSHP.load(p.get("folder"));

        OCLBuildingsCuboidFinal ocb = new OCLBuildingsCuboidFinal();
        ocb.setCoeffDec((double) valCoeff[i]);

        double timeMs = System.currentTimeMillis();

        Configuration<Cuboid2> cc = ocb.process(env.getBpU().get(1), p, env, 1);

        
        
        IFeatureCollection<IFeature> iFeatC = new FT_FeatureCollection<>();
        
        for (GraphConfiguration<Cuboid2>.GraphVertex v : ((GraphConfiguration<Cuboid2>) cc)
                .getGraph().vertexSet()) {
            
            IMultiSurface<IOrientableSurface> iMS = new GM_MultiSurface<>();
            iMS.addAll(GenerateSolidFromCuboid.generate(v.getValue()).getFacesList());
            
            
            IFeature feat = new DefaultFeature(iMS);
            
            AttributeManager.addAttribute(feat, "Longueur", Math.max(v.getValue().length,v.getValue().width), "Double");
            AttributeManager.addAttribute(feat, "Largeur", Math.min(v.getValue().length,v.getValue().width), "Double");
            AttributeManager.addAttribute(feat, "Hauteur", v.getValue().height, "Double");
            AttributeManager.addAttribute(feat, "Rotation", v.getValue().orientation, "Double");
            
            iFeatC.add(feat);
            

        }
        
        ShapefileWriter.write(iFeatC, p.get("result").toString() +"shp_" +ld.get(i)+ "_ " +j+"_ene"+cc.getEnergy()+".shp");
        
        
        

        System.out.println(valCoeff[i] + "," + ocb.getCount() + ","
            + (System.currentTimeMillis() - timeMs) + "," + cc.getEnergy());
        
        count++;
        
        System.out.println("État itération : " + count + "  / " + (valCoeff.length *nbIt));



      }

    }

  }

  private static Parameters initialize_parameters(String name) {
    return Parameters.unmarshall(name);
  }
}
