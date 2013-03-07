package fr.ign.cogit.simplu3d.importer.applicationClasses;

import java.util.Collection;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.util.index.Tiling;
import fr.ign.cogit.simplu3d.convert.ConvertToLineString;
import fr.ign.cogit.simplu3d.model.application.Alignement;
import fr.ign.cogit.simplu3d.model.application.Bordure;
import fr.ign.cogit.simplu3d.model.application.SousParcelle;
import fr.ign.cogit.util.AddAttribute;

public class AlignementImporter {

  public final static String ATT_TYPE = "TYPEPSC";
  public final static String ATT_Param = "Param";

  public static IFeatureCollection<Alignement> importRecul(
      IFeatureCollection<IFeature> prescriptions,
      IFeatureCollection<SousParcelle> spColl)
      throws CloneNotSupportedException {

    IFeatureCollection<Alignement> lAlignement = new FT_FeatureCollection<Alignement>();

    if (!prescriptions.hasSpatialIndex()) {

      prescriptions.initSpatialIndex(Tiling.class, false);

    }

    for (SousParcelle sP : spColl) {
      
      IFeatureCollection<Alignement> lAlignementTemp = new FT_FeatureCollection<Alignement>();
      

      Collection<IFeature> coll = prescriptions.select(sP.getGeom());

      if (coll.isEmpty()) {
        continue;
      }

      IFeatureCollection<IFeature> collToTreat = new FT_FeatureCollection<IFeature>();

      for (IFeature feat : coll) {

        // on ne garde que les types alignements et recul
        Double type = Double.parseDouble(feat.getAttribute(ATT_TYPE).toString());

        if (type != 11) {
          continue;
        }

        IFeature featTemp = feat.cloneGeom();

        IGeometry geom = feat.getGeom().intersection(sP.getGeom());

        if (geom.isEmpty()) {
          continue;
        }

        featTemp.setGeom(geom);

        collToTreat.add(featTemp);
      }

      // On a des entités avec les morceaux de prescription qui nous
      // intéressent, maintenant il faut éevntuellement décomposer en segment.
      int nbCollToTreat = collToTreat.size();

      for (int i = 0; i < nbCollToTreat; i++) {
        IFeature featTemp = collToTreat.get(i);
        List<IOrientableCurve> lIOC = ConvertToLineString.convert(featTemp
            .getGeom());

        if (lIOC.isEmpty()) {
         continue;
        }

        for (IOrientableCurve c : lIOC) {

          Alignement a = new Alignement();
          a.setGeom(c);

          Double type = Double.parseDouble(featTemp.getAttribute(ATT_TYPE)
              .toString());

          if (type == 11) {

            a.setType(Alignement.RECUL);

            Double param = Double.parseDouble(featTemp.getAttribute(ATT_Param)
                .toString());

            a.setLargeur(param);

          }

          lAlignementTemp.add(a);

        }

      }

      // On a 1 feature par segment d'alignement

      IFeatureCollection<Bordure> iFCVoie = sP.getBorduresVoies();
      
      
      
      

      for (Alignement a : lAlignementTemp) {
        Bordure b = determineBestBordure(iFCVoie,a);
        if(b != null) {
          b.setAlignement(a);
        }

      }
          lAlignement.addAll(lAlignementTemp);

    }

    return lAlignement;
  }

  private static Bordure determineBestBordure(
      IFeatureCollection<Bordure> bordures, Alignement a) {
    System.out.println("Alignement " + a.getId());
    
    for(Bordure b:bordures){
      
      if(b.getId() > 165  ){
        System.out.println("STOP");
      }
      
    }

    double scoreMax = -1;
    Bordure bCand = null;

    double rec = 0;

    if (a.getType() == Alignement.RECUL) {

      rec = a.getLargeur();
    }

    IOrientableCurve geomAlignement = ConvertToLineString.convert(a.getGeom())
        .get(0);

    Vecteur v = new Vecteur(geomAlignement.coord().get(0), geomAlignement
        .coord().get(1));
    v.normalise();

    for (Bordure b : bordures) {

      List<IOrientableCurve> lIOC = ConvertToLineString.convert(b.getGeom());

      if (lIOC.size() != 1) {
        System.out.println("Alignement : différent de 1 ???");
      }

      Vecteur v1 = new Vecteur(lIOC.get(0).coord().get(0), lIOC.get(0).coord()
          .get(1));
      v1.normalise();

      double scal = Math.abs(v.prodScalaire(v1));

      double distance = geomAlignement.distance(b.getGeom());

      double scoreDist = 0;

      if (distance == 0) {
        if (rec == 0) {
          scoreDist = 1;
        } else {
          scoreDist = 1 / Math.abs(rec);
        }

      } else {

        scoreDist = Math.abs(1 - Math.abs(distance - rec) / rec);
      }

      
      
      
      if(Math.abs(scal) > 0.8){
        
        if(scoreDist > 0.7){         
         System.out.println("ID " + b.getId());
        }
        
      
      }
      
      
      
      double scoreTemp =  scoreDist * scal;

      if (scoreTemp > scoreMax) {

        scoreMax = scoreTemp;
        bCand = b;

      }

    }

    System.out.println(scoreMax);
    AddAttribute.addAttribute(a, "ID_B", bCand.getId(), "Integer");
    AddAttribute.addAttribute(a, "score", scoreMax, "Double");
    return bCand;
  }

}
