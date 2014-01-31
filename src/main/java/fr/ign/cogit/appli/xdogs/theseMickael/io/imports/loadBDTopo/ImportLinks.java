package fr.ign.cogit.appli.xdogs.theseMickael.io.imports.loadBDTopo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.contrib.appariement.Lien;

public class ImportLinks {

  public static List<Lien> importLinks(String path,
      IFeatureCollection<IFeature> featCollRef,
      IFeatureCollection<IFeature> featCollcomp) throws FileNotFoundException {

    List<Lien> lLiens = new ArrayList<Lien>();

    BufferedReader in = new BufferedReader(new FileReader(path));

    try {
      String line = null;

      while ((line = in.readLine()) != null) {

        Lien l = new Lien();
        lLiens.add(l);

        String[] parts = line.split("/");

        String objRef = parts[0];

        String[] elementsRef = objRef.split(";");
        int lenRef = elementsRef.length;

        for (int i = 0; i < lenRef; i++) {

          int index = Integer.parseInt(elementsRef[i]);
          l.addObjetRef(featCollRef.get(index));

        }

        if (parts.length != 2) {
          continue;
        }

        String objComp = parts[1];

        String[] elementsComp = objComp.split(";");
        int lenComp = elementsComp.length;
        for (int i = 0; i < lenComp; i++) {
          int index = Integer.parseInt(elementsComp[i]);
          l.addObjetComp(featCollcomp.get(index));

        }

      }

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return lLiens;
  }

}
