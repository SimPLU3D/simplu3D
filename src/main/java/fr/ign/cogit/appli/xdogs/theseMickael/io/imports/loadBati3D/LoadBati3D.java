package fr.ign.cogit.appli.xdogs.theseMickael.io.imports.loadBati3D;

import fr.ign.cogit.appli.xdogs.theseMickael.util.correction.CorrectionBati3D;
import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.semantic.DTM;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;

public class LoadBati3D {

  public static IFeatureCollection<IFeature> loadBati3D(String pathBati3D,
      DTM dtm) {

    IPopulation<IFeature> pDF = ShapefileReader.read(pathBati3D);

    int nbEl = pDF.size();
    IFeatureCollection<IFeature> featColl2 = new FT_FeatureCollection<IFeature>();
    for (int i = 0; i < nbEl; i++) {

      featColl2.add(pDF.get(i));
    }

    pDF.clear();
    pDF = null;

    CorrectionBati3D.correctionNormales(featColl2);
    CorrectionBati3D.close3DBatiCollWithDTM(featColl2, dtm);
    CorrectionBati3D.correctionNormales(featColl2);

    return featColl2;
  }

}
