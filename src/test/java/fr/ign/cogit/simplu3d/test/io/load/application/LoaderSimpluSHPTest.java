package fr.ign.cogit.simplu3d.test.io.load.application;

import junit.framework.Assert;

import org.junit.Test;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.PLU;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.application.UrbaZone;

/**
 * 
 * @author MBrasebin
 * 
 */
public class LoaderSimpluSHPTest {

  private static Environnement ENV_SINGLETON = null;

  public static Environnement getENVTest() {
    if (ENV_SINGLETON == null) {
      String folder = LoaderSimpluSHPTest.class.getClassLoader()
          .getResource("data3d/").getPath();

      try {
        ENV_SINGLETON = LoaderSHP.load(folder);
      } catch (CloneNotSupportedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    return ENV_SINGLETON;

  }

  @Test
  public void testImport() {
    
    if(ENV_SINGLETON != null){
 
      return;
    }

    getENVTest();

    PLU plu = ENV_SINGLETON.getPlu();

    Assert.assertNotNull(plu);
    Assert.assertEquals(1, plu.getlUrbaZone().size());

    // Test 2 : la zone UB16 a elles des règles
    for (UrbaZone z : plu.getlUrbaZone()) {

      if (z.getName().equalsIgnoreCase("UB16")) {

        Assert.assertNotNull(z.getRules());

        Assert.assertFalse("Les règles OCL ne sont pas chargées", z.getRules()
            .isEmpty());

      } else {
        Assert.fail("La zone UB16 n'existe pas");
      }

    }

    IFeatureCollection<SpecificCadastralBoundary> bordures = new FT_FeatureCollection<SpecificCadastralBoundary>();

    int count = 0;
    Assert.assertEquals("Toutes les parcelles sont chargées.", 19,
        ENV_SINGLETON.getParcelles().size());

    Assert.assertNotNull(ENV_SINGLETON.getBpU());

    Assert.assertEquals("Toutes les unités foncières sont chargées.", 19,
        ENV_SINGLETON.getBpU().size());

    for (BasicPropertyUnit bPU : ENV_SINGLETON.getBpU()) {

      Assert.assertNotNull(bPU.getCadastralParcel());
      Assert.assertFalse(bPU.getCadastralParcel().isEmpty());

      for (CadastralParcel sp : bPU.getCadastralParcel()) {

        count = count + sp.getBoundary().size();

        Assert.assertNotNull(sp.getBoundary());
        Assert.assertFalse(sp.getBoundary().isEmpty());

        for (SpecificCadastralBoundary b : sp.getBoundary()) {
          bordures.add(b);

        }

      }
    }

    Assert.assertEquals("Toutes les limites séparatives sont chargées.", 140,
        count);

    Assert.assertEquals("Toutes les sous parcelles sont chargées.", 19,
        ENV_SINGLETON.getSubParcels().size());

    IFeatureCollection<IFeature> featToits = new FT_FeatureCollection<IFeature>();

    Assert.assertEquals("Les emprises sont générées.", 40, ENV_SINGLETON
        .getBuildings().size());

    for (AbstractBuilding b : ENV_SINGLETON.getBuildings()) {
      featToits.add(new DefaultFeature(b.getFootprint()));
    }

    IFeatureCollection<IFeature> featFaitage = new FT_FeatureCollection<IFeature>();
    for (AbstractBuilding b : ENV_SINGLETON.getBuildings()) {
      featFaitage.add(new DefaultFeature(b.getToit().getRoofing()));
    }

    Assert.assertEquals("Les faîtages sont générés.", 40, featFaitage.size());

    Assert.assertNotNull(ENV_SINGLETON.getRoads());
    Assert.assertFalse(ENV_SINGLETON.getRoads().isEmpty());

    Assert.assertTrue("Le test est un succès", true);
  }

}
