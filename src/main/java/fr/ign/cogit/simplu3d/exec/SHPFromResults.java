package fr.ign.cogit.simplu3d.exec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader ;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;

/**
 * 
 * This software is released under the licence CeCILL
 * see LICENSE.TXT
 * see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * @copyright IGN
 * 
 * @author Paul Chapron (si si) 
 * 
 * @version 1.0
 * 
 *          Reconstruit un ShapeFile des bâtiments d'une zone urbaine (plusieurs parcelles) 
 *          à partir de ses caractéristiques géométriques  tirées des fichiers 
 *          de simulations ordonnés selon un plan d'exploration OpenMOLE
 * 
 */
public class SHPFromResults {

    /**
     * @param args
     */

    public static void main(String[] args) throws Exception {
     
      System.out.println("chargement fichiers resultats");
      // repertoire où openmole stocke les resultats 
     // File resultsFolder = new File("/home/pchapron/.openmole/HP1111W090-Ubuntu/webui/projects/results");
     
      File resultsFolder = new File("/home/pchapron/dev/result_Simplu/pointsInteret/");

      
      FilenameFilter csvfileFilter = new  FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
          return name.endsWith(".csv");
        }
      };
      
      FilenameFilter cuboidsFileFilter = new  FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
          return !(name.contains("energy"));
        }
      };
      
      
      
      FilenameFilter folderFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
          return new File(dir,name).isDirectory();
        }
      };
      
      
      
      File[] directories = resultsFolder.listFiles(folderFilter);
      
      for   (int idxdir = 0 ; idxdir < directories.length; idxdir++ ) {


        File dir = directories[idxdir];
        File[] listOfCuboidsdir = dir.listFiles(folderFilter);
        //System.out.println("Repertoire courrant:##"+dir.getName());

        //for ( File dd : listOfCuboidsdir){
         
          //premier et unique fichier du repertoire
          File f = dir.listFiles(csvfileFilter)[0];
          System.out.println("fichier  "+ (idxdir + 1)+ "/"+ directories.length);
          String finalFileName = dir.getName()+f.getName();
          finalFileName= finalFileName.replace(".csv", "");
          SHPWriterFromCSVfile(f, finalFileName);

        //}
        // File f = dir.listFiles(csvfileFilter)[idxdir];
        //File f = new File("/home/pchapron/dev/result_Simplu/results/shape_2025967707654652509_-19524.765210399386_0.09999942901127996_0/out.csv");
      }

      //SHPWriterFromCSVfile(f, FinalFileName);

      System.out.println("ta da ");
    }//main

    
    public static Cuboid CuboidFromLine(String l){

      if(l.isEmpty()){
        return null;
      }
      else {
        // splitte la ligne en colonnes 
        String[] columns = l.split(","); 
        ArrayList<Double> attr = new ArrayList<Double>() ;
        for (String col : columns){
          Double d = Double.parseDouble(col);
          attr.add(d);
        }
        // crée le cuboid 
        Cuboid c = new Cuboid(attr.get(1), attr.get(2),
            attr.get(3), attr.get(4),
            attr.get(5), attr.get(6));
        return c ;
      }
    }

    public static void SHPWriterFromCSVfile(File f, String finalFileName){

     
     
      // charge les lignes de f dans une liste  
      // et met les idParcell dans un hashset
     BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(f));
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
     List<String> lines = new ArrayList<>();
     ArrayList<Integer> idparcels = new ArrayList<Integer>();
     String line = null;
     try {
       reader.readLine(); // pour skip la première ligne
       
      while ((line = reader.readLine()) != null) {
           lines.add(line);
           idparcels.add(Integer.parseInt(line.split(",")[0])); 
       }
    } catch (NumberFormatException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
     try {
      reader.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
          
     // récupère les idParcel uniques par la création d'hashset
     HashSet<Integer> uniqueIdParcels = new HashSet<Integer>(idparcels);
    //System.out.println("id parcels distinctes "+ uniqueIdParcels.toString());

     ArrayList<Cuboid> cuboZone = new ArrayList<Cuboid>();
     // pour chaque parcelle , creation de cuboides  
     for (Integer idparc : uniqueIdParcels){
       ArrayList<Cuboid> cuboParc = new ArrayList<Cuboid>();
       //System.out.println("#Parcelle "+ idparc+"#");      
       for(String l : lines){
         int idCurrentParcel = Integer.parseInt(l.split(",")[0]);
         // pour les lignes de la même parcelle on crée des cuboid
         if (idCurrentParcel == idparc){
           Cuboid cu = CuboidFromLine(l);
           cuboParc.add(cu);
           //System.out.println("Cuboïde créé dans parcelle" + idparc +"\n");
           //System.out.println(cuboZone.size()+"cuboides au total");
         }
       }
       cuboZone.addAll(cuboParc);
     }
     System.out.println(cuboZone.size()+"cuboides dans la zone");
     
     
   //export shp 
     IFeatureCollection<IFeature> iFeatC = new FT_FeatureCollection<>();
        
     for (Cuboid c : cuboZone) {
       //Output feature with generated geometry
       IFeature feat = new DefaultFeature(c.generated3DGeom());
       
       AttributeManager.addAttribute(feat, "Longueur", Math.max(c.length, c.width),
           "Double");
   AttributeManager.addAttribute(feat, "Largeur", Math.min(c.length, c.width), "Double");
   AttributeManager.addAttribute(feat, "Hauteur", c.height, "Double");
   AttributeManager.addAttribute(feat, "Rotation", c.orientation, "Double");

       iFeatC.add(feat);
     }

     
     File folderSHP=new File("/home/pchapron/temp/");
      
     String pathShapeFile =folderSHP + File.separator + finalFileName+".shp";
     ShapefileWriter.write(iFeatC, pathShapeFile );

    }
}
