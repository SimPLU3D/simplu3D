package fr.ign.cogit.simplu3d.exec.test;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.Alignement;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.model.application._AbstractBuilding;

/**
 * 
 * @author MBrasebin
 *
 */
public class TestLoaderSHP {

  public static void main(String[] args) throws CloneNotSupportedException {
    String folder = "E:/mbrasebin/Donnees/Strasbourg/GTRU/Project1/";
    String folderOut = "E:/mbrasebin/Donnees/Strasbourg/GTRU/Project1/out/";

    Environnement env = LoaderSHP.load(folder);

    IFeatureCollection<SpecificCadastralBoundary> bordures = new FT_FeatureCollection<SpecificCadastralBoundary>();

    for (CadastralParcel sp : env.getParcelles()) {

      AttributeManager.addAttribute(sp, "ID", sp.getId(), "Integer");

      bordures.addAll(sp.getSpecificCadastralBoundary());

    }

    IFeatureCollection<Alignement> featAL = new FT_FeatureCollection<Alignement>();

    for (SpecificCadastralBoundary b : bordures) {
      AttributeManager.addAttribute(b, "ID", b.getId(), "Integer");
      AttributeManager.addAttribute(b, "Type", b.getType(), "Integer");

    }

    for (Alignement a : env.getAlignements()) {

      if (a != null) {
        featAL.add(a);

        AttributeManager.addAttribute(a, "ID", a.getId(), "Integer");
      }
    }

    ShapefileWriter.write(env.getParcelles(), folderOut + "parcelles.shp");
    ShapefileWriter.write(bordures, folderOut + "bordures.shp");

    System.out.println("Nombre d'alignements concernés" + featAL.size());
    ShapefileWriter.write(featAL, folderOut + "alignements.shp");

    System.out.println("Sous Parcelles  " + env.getSubParcels().size());

    for (SubParcel sp : env.getSubParcels()) {
      AttributeManager.addAttribute(sp, "Test", 0, "Integer");
    }

    ShapefileWriter.write(env.getSubParcels(), folderOut + "sousParcelles.shp");

    IFeatureCollection<IFeature> featToits = new FT_FeatureCollection<IFeature>();

    System.out.println("NB emprise " + env.getBuildings().size());
    for (_AbstractBuilding b : env.getBuildings()) {
      featToits.add(new DefaultFeature(b.getFootprint()));
    }

    ShapefileWriter.write(featToits, folderOut + "emprise.shp");

    IFeatureCollection<IFeature> featFaitage = new FT_FeatureCollection<IFeature>();
    for (_AbstractBuilding b : env.getBuildings()) {
      featFaitage.add(new DefaultFeature(b.getToit().getRoofing()));
    }

    System.out.println("Chat qui râle");

    System.out.println("Faitage : " + featFaitage.size());

    ShapefileWriter.write(featFaitage, folderOut + "pignon.shp");

  }

}
