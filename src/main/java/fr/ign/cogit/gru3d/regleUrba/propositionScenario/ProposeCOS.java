package fr.ign.cogit.gru3d.regleUrba.propositionScenario;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.gru3d.regleUrba.Moteur;
import fr.ign.cogit.gru3d.regleUrba.propositionBuilding.PropositionCOS;
import fr.ign.cogit.gru3d.regleUrba.propositionBuilding.export.Export;
import fr.ign.cogit.gru3d.regleUrba.propositionBuilding.export.Export.AvailableExport;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.Consequence;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.ContrainteHauteur;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.DistanceEuclidienne;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.ReculBordure;
import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.Regle;
import fr.ign.cogit.gru3d.regleUrba.schemageo.Batiment;
import fr.ign.cogit.gru3d.regleUrba.schemageo.Parcelle;

public class ProposeCOS {

  public static List<String> optimizeCOS(Moteur m, double hMin, double hMax,
      double hPas, double recMin, double recMax, double recPas, int nbIt) {

    List<String> lS = new ArrayList<String>();

    Export.doExport = AvailableExport.NONE;
    List<Regle> lRegle = m.getlRegles();
    FT_FeatureCollection<Parcelle> sP = m.getEnv().getlParcelles();

    double totalArea = 0;

    for (Parcelle p : sP) {

      totalArea = totalArea + p.getGeom().area();

    }

    for (double h = hMin; h < hMax; h = h + hPas) {

      for (double rec = recMin; rec < recMax; rec = rec + recPas) {

        List<Double> lVals = new ArrayList<Double>();
        System.out.println("Valeur hauteur  : " + h + "   valeur rec : " + rec);
        for (int i = 0; i < nbIt; i++) {

          // On initialise les valeurs des règles
          initialiseRule(lRegle, rec, h);

          List<Batiment> lBatimentsPropose = new ArrayList<Batiment>();

          // On initialise le COS des parcelles
          for (Parcelle p : sP) {
            p.setCos(-1);
            // Pour chaque parcelle, on génère une configuration
            Batiment b = PropositionCOS.proposition(p, m);
            lBatimentsPropose.add(b);

          }

          double valTemp = 0;

          // On remet la parcelle en état
          int count = 0;
          for (Parcelle p : sP) {

            valTemp = valTemp + p.getCos() * p.getGeom().area();

            p.setCos(-1);
            Batiment b = lBatimentsPropose.get(count);

            if (b != null) {

              p.getlBatimentsContenus().remove(b);
            }

            count++;

          }

          lVals.add(valTemp / totalArea);

        }

        lS.add(processStats(rec, h, lVals));

      }

    }

    for (String s : lS) {
      System.out.println(s);
    }

    return lS;
  }

  private static String processStats(double rec, double h, List<Double> lVals) {

    StringBuffer st = new StringBuffer();
    st.append(rec + ";");
    st.append(h + ";");

    double min = Double.POSITIVE_INFINITY;
    double max = Double.NEGATIVE_INFINITY;
    double moy = 0;
    double et = 0;

    for (Double d : lVals) {
      min = Math.min(d, min);
      max = Math.max(max, d);
      moy = moy + d;

    }

    moy = moy / lVals.size();

    for (Double d : lVals) {

      et = et + (moy - d) * (moy - d);

    }

    et = Math.sqrt((et / lVals.size()));

    st.append(min + ":");
    st.append(max + ";");
    st.append(moy + ";");
    st.append(et + ";");

    // TODO Auto-generated method stub
    return st.toString();
  }

  private static void initialiseRule(List<Regle> lRegle, double rec, double h) {

    for (Regle r : lRegle) {

      List<Consequence> lConsequence = r.getConsequence();

      for (Consequence c : lConsequence) {

        if (c instanceof ReculBordure) {

          ((ReculBordure) c).setDistanceRecul(new DistanceEuclidienne(rec));

        } else if (c instanceof ContrainteHauteur) {

          ((ContrainteHauteur) c).setHauteurMax(h);

        }

      }

    }

  }

}
