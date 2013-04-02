package fr.ign.cogit.simplu3d.exec.test;

import java.util.ArrayList;
import java.util.List;

import org.citygml4j.model.citygml.transportation.Road;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.gui.MainWindow;
import fr.ign.cogit.geoxygene.sig3d.semantic.DTM;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.geoxygene.sig3d.util.ColorShade;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.sig3d.gui.toolbar.IOToolBar;
import fr.ign.cogit.simplu3d.generation.BatimentProcedural;
import fr.ign.cogit.simplu3d.generation.TopologieBatiment;
import fr.ign.cogit.simplu3d.generation.TopologieBatiment.FormeEmpriseEnum;
import fr.ign.cogit.simplu3d.generation.TopologieBatiment.FormeToitEnum;
import fr.ign.cogit.simplu3d.gui.button.GTRUToolBar;
import fr.ign.cogit.simplu3d.model.application.Materiau;
import fr.ign.cogit.simplu3d.representation.theme.RepresentationBatiment;

public class TestGenerator {
  
  
  


  public static void main(String[] args) {

DTM dtm = new DTM("E:/mbrasebin/Donnees/test/ISERE_50_asc.asc", "Ours_vert_a_ailes", true , 1, ColorShade.BLUE_PURPLE_WHITE);

    
    IFeatureCollection<IFeature> featC = new FT_FeatureCollection<IFeature>();

    long l = System.currentTimeMillis();

    FormeEmpriseEnum[] val = FormeEmpriseEnum.values();

    for (int i = 0; i < val.length; i++) {
      TopologieBatiment tB = new TopologieBatiment(val[i],
          FormeToitEnum.SYMETRIQUE, new ArrayList<Integer>());

// tB.getlIndArret().add(1);

      List<Materiau> lMatF = new ArrayList<Materiau>();
      lMatF.add(Materiau.BRIQUE);

      BatimentProcedural bP = new BatimentProcedural(tB, 20, 30, 5, 5, 15, 24,
          null,new ArrayList<Materiau>(),  //  Materiau.TOLE, lMatF,
          new boolean[0], new DirectPosition(0, i * 100, 0), 0,dtm, Math.PI/4);

      bP.generationEmprise();
      bP.generationToit();
      bP.generationFacade();

      bP.setRepresentation(new RepresentationBatiment(bP, false, false));

      featC.add(bP);
    }

    System.out.println(System.currentTimeMillis() - l);

    MainWindow mW = new MainWindow();
    

    mW.getInterfaceMap3D().getCurrent3DMap()
        .addLayer(new VectorLayer(featC, "Bati"));

  //  mW.getInterfaceMap3D().getCurrent3DMap().addLayer(dtm);

    new GTRUToolBar(mW);
    new IOToolBar(mW);

    
  }

}
