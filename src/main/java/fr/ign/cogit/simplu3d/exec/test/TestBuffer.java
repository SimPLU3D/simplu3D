package fr.ign.cogit.simplu3d.exec.test;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileWriter;
import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;
import fr.ign.parameters.Parameters;

public class TestBuffer {

  /**
   * @param args
   * @throws CloneNotSupportedException
   */
  public static void main(String[] args) throws CloneNotSupportedException {
    String folderName = "./src/main/resources/scenario/";
    String fileName = "building_parameters_project_expthese_3.xml";

    Parameters p = initialize_parameters(folderName + fileName);

    Environnement env = LoaderSHP.load(p.get("folder"));
    
    IFeatureCollection<IFeature> featC = new FT_FeatureCollection<>();

    List<IOrientableCurve> lCurveVoirie = new ArrayList<>();

    List<IOrientableCurve> lCurveLatBot = new ArrayList<>();

    for (BasicPropertyUnit bPU : env.getBpU()) {

      for (CadastralParcel cP : bPU.getCadastralParcel()) {

        for (SpecificCadastralBoundary sCB : cP.getBoundary()) {

          if (sCB.getType() == SpecificCadastralBoundary.ROAD) {

            IGeometry geom = sCB.getGeom();

            if (geom instanceof IOrientableCurve) {

              lCurveVoirie.add((IOrientableCurve) geom);

            } else {
              System.out
                  .println("Classe UB14PredicateFull : quelque chose n'est pas un ICurve");
            }

          } else if (sCB.getType() != SpecificCadastralBoundary.INTRA) {
            IGeometry geom = sCB.getGeom();

            if (geom instanceof IOrientableCurve) {

              lCurveLatBot.add((IOrientableCurve) geom);

            } else {
              System.out
                  .println("Classe UB14PredicateFull : quelque chose n'est pas un ICurve");
            }

          }

        }

      }
      
      
      IMultiCurve<IOrientableCurve> curveVoirie;
      IMultiCurve<IOrientableCurve> curveLatBot;



      IGeometry buffer13, buffer20, buffer20more;
      

      
      curveVoirie = new GM_MultiCurve<>(lCurveVoirie);

      curveLatBot = new GM_MultiCurve<>(lCurveLatBot);


      
      
      buffer13 = curveVoirie.buffer(13);
      buffer20 = curveVoirie.buffer(20).difference(buffer13);
      buffer20more = bPU.getGeom().difference(curveVoirie.buffer(20));
      
      
      
      

      
      
      
      
      IFeature feat = new DefaultFeature(buffer13);
      IFeature feat2 = new DefaultFeature(buffer20);
      IFeature feat3 = new DefaultFeature(      FromGeomToSurface.convertMSGeom(buffer20more));
      
      

      featC.add(feat);
      featC.add(feat2);
      featC.add(feat3);
      
      
     
      
      
    }
    
    
    ShapefileWriter.write(featC, "E:/temp/shp.shp");



  }

  private static Parameters initialize_parameters(String name) {
    return Parameters.unmarshall(name);
  }

}
