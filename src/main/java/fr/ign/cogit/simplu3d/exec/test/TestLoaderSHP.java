package fr.ign.cogit.simplu3d.exec.test;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.PLU;
import fr.ign.cogit.simplu3d.model.application.Road;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.model.application.UrbaZone;

/**
 * 
 * @author MBrasebin
 * 
 */
public class TestLoaderSHP {

  public static void main(String[] args) throws CloneNotSupportedException {
    String folder = "E:/mbrasebin/Donnees/Strasbourg/GTRU/Project4/";
    String folderOut = "E:/mbrasebin/Donnees/Strasbourg/GTRU/Project4/out/";

    Environnement env = LoaderSHP.load(folder);

    PLU plu = env.getPlu();

    System.out.println("Nombre de zones dans le PLU : "
        + plu.getlUrbaZone().size());

    // Test 2 : la zone UB16 a elles des règles
    for (UrbaZone z : plu.getlUrbaZone()) {
      if (z.getName().equalsIgnoreCase("UB16")) {
        System.out.println("Nombre de règles dans UB16 : "
            + z.getRules().size());
      }

    }

    IFeatureCollection<SpecificCadastralBoundary> bordures = new FT_FeatureCollection<SpecificCadastralBoundary>();

    for (CadastralParcel sp : env.getParcelles()) {

      AttributeManager.addAttribute(sp, "ID", sp.getId(), "Integer");

      for (SpecificCadastralBoundary b : sp.getSpecificCadastralBoundary()) {
        bordures.add(b);

        AttributeManager.addAttribute(b, "ID", b.getId(), "Integer");
        AttributeManager.addAttribute(b, "Type", b.getType(), "Integer");
        AttributeManager.addAttribute(b, "IDPar", sp.getId(), "Integer");

        if (b.getFeatAdj() != null) {
          
          if(b.getFeatAdj() instanceof CadastralParcel){
            
            AttributeManager.addAttribute(b, "Adj",
                ((CadastralParcel) b.getFeatAdj()).getId(), "Integer");
          }else if(b.getFeatAdj() instanceof Road){
            AttributeManager.addAttribute(b, "Adj",
                ((Road) b.getFeatAdj()).getId(), "Integer");
          }


        } else {
          AttributeManager.addAttribute(b, "Adj", 0, "Integer");

        }

      }

    }

    // Export des parcelles

   
    ShapefileWriter.write(env.getParcelles(), folderOut + "parcelles.shp");
    ShapefileWriter.write(bordures, folderOut + "bordures.shp");
    
    
    System.out.println("Nombre de bpu : " + env.getBpU().size());
    
   ShapefileWriter.write(env.getBpU(), folderOut + "bpu.shp");
    

    /*
     * IFeatureCollection<Alignement> featAL = new
     * FT_FeatureCollection<Alignement>();
     * 
     * 
     * 
     * for (Alignement a : env.getAlignements()) {
     * 
     * if (a != null) { featAL.add(a);
     * 
     * AttributeManager.addAttribute(a, "ID", a.getId(), "Integer"); }
     */

    System.out.println("Sous Parcelles  " + env.getSubParcels().size());

    for (SubParcel sp : env.getSubParcels()) {
      AttributeManager.addAttribute(sp, "Test", 0, "Integer");
    }

    ShapefileWriter.write(env.getSubParcels(), folderOut + "sousParcelles.shp");

    IFeatureCollection<IFeature> featToits = new FT_FeatureCollection<IFeature>();

    System.out.println("NB emprise " + env.getBuildings().size());
    for (AbstractBuilding b : env.getBuildings()) {
      featToits.add(new DefaultFeature(b.getFootprint()));
    }

    ShapefileWriter.write(featToits, folderOut + "emprise.shp");

    IFeatureCollection<IFeature> featFaitage = new FT_FeatureCollection<IFeature>();
    for (AbstractBuilding b : env.getBuildings()) {
      featFaitage.add(new DefaultFeature(b.getToit().getRoofing()));
    }

    System.out.println("Faitage : " + featFaitage.size());

    ShapefileWriter.write(featFaitage, folderOut + "pignon.shp");

    System.out.println("Chat qui râle");

  }

  /*
   * 
   * 
   * 
   * 
   * System.out.println("Nombre d'alignements concernés" + featAL.size());
   * ShapefileWriter.write(featAL, folderOut + "alignements.shp");
   */

}
