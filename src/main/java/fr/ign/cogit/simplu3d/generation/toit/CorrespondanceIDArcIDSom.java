package fr.ign.cogit.simplu3d.generation.toit;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.generation.TopologieBatiment.FormeEmpriseEnum;
/**
 * 
 *        This software is released under the licence CeCILL
 * 
 *        see LICENSE.TXT
 * 
 *        see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class CorrespondanceIDArcIDSom {

  public static List<Integer> getCorrespondanceSymetrique(FormeEmpriseEnum fB,
      Integer i) {

    List<Integer> lInt = new ArrayList<Integer>();
    if (fB.equals(FormeEmpriseEnum.RECTANGLE)) {
      if (i == 1) {
        lInt.add(2);
        lInt.add(3);
      } else if (i == 2) {

        lInt.add(3);
        lInt.add(4);

      } else if (i == 3) {
        lInt.add(0);
        lInt.add(1);

      } else if (i == 4) {
        lInt.add(1);
        lInt.add(2);

      }

    }

    if (fB.equals(FormeEmpriseEnum.FORME_U)) {
      if (i == 1) {

        lInt.add(1);
        lInt.add(2);

      } else if (i == 2) {
        lInt.add(5);
        lInt.add(6);

      } else if (i == 3) {
        lInt.add(4);
        lInt.add(5);
      } else if (i == 4) {
        lInt.add(2);
        lInt.add(3);

      } else {

        lInt.add(3);
        lInt.add(4);

      }

    }

    if (fB.equals(FormeEmpriseEnum.FORME_T)) {

      if (i == 1) {
        lInt.add(7);
        lInt.add(8);
      } else if (i == 2) {
        lInt.add(0);
        lInt.add(1);

      } else if (i == 3) {
        lInt.add(6);
        lInt.add(7);

      } else if (i == 4) {

        lInt.add(3);
        lInt.add(4);

      }

    }

    if (fB.equals(FormeEmpriseEnum.FORME_L)) {
      if (i == 1) {
        lInt.add(1);
        lInt.add(2);
      } else if (i == 2) {
        lInt.add(2);
        lInt.add(3);

      } else if (i == 3) {
        lInt.add(3);
        lInt.add(4);

      } else if (i == 4) {
        lInt.add(4);
        lInt.add(5);

      }

    }

    if (fB.equals(FormeEmpriseEnum.CERCLE)) {

      if (i == 0) {

        for (int j = 0; j < 10; j++) {
          lInt.add(j);
          lInt.add(j + 1);
        }
      } else if (i == 1) {

        for (int j = 20; j < 30; j++) {
          lInt.add(j);
          lInt.add(j + 1);
        }
      }

    }

    return lInt;
  }

  public static List<Integer> getCorrespondanceSymetrique(FormeEmpriseEnum fB,
      List<Integer> lSIN) {
    List<Integer> lInt = new ArrayList<Integer>();

    if (lSIN != null) {
      for (Integer i : lSIN) {
        lInt.addAll(getCorrespondanceSymetrique(fB, i));
      }
    }
    return lInt;

  }

  public static List<Integer> getCorrespondanceAppentis(FormeEmpriseEnum fB,
      Integer i) {
    List<Integer> lInt = new ArrayList<Integer>();
    if (fB.equals(FormeEmpriseEnum.RECTANGLE)) {
      if (i == 1) {
        lInt.add(2);
        lInt.add(3);
      } else {

        lInt.add(3);
        lInt.add(4);

      }

    }

    if (fB.equals(FormeEmpriseEnum.CERCLE)) {

      for (int j = 0; j < 10; j++) {
        lInt.add(j);
        lInt.add(j + 1);
      }

    }

    if (fB.equals(FormeEmpriseEnum.FORME_U)) {
      if (i == 1) {

        lInt.add(1);
        lInt.add(2);
        lInt.add(5);
        lInt.add(6);

      } else {

        lInt.add(4);
        lInt.add(5);

      }

    }

    if (fB.equals(FormeEmpriseEnum.FORME_T)) {

      if (i == 1) {
        lInt.add(7);
        lInt.add(8);
      } else if (i == 2) {
        lInt.add(0);
        lInt.add(1);

      } else if (i == 3) {
        lInt.add(6);
        lInt.add(7);

      } else if (i == 4) {

        lInt.add(3);
        lInt.add(4);

      }

    }

    if (fB.equals(FormeEmpriseEnum.FORME_L)) {
      if (i == 1) {
        lInt.add(1);
        lInt.add(2);
      } else if (i == 2) {
        lInt.add(2);
        lInt.add(3);

      } else if (i == 3) {
        lInt.add(3);
        lInt.add(4);

      } else if (i == 4) {
        lInt.add(4);
        lInt.add(5);

      }

    }

    return lInt;

  }

  public static List<Integer> getIDSpeed(FormeEmpriseEnum fB) {
    List<Integer> lInt = new ArrayList<Integer>();

    if (fB.equals(FormeEmpriseEnum.RECTANGLE)) {
      lInt.add(1);
      lInt.add(3);
    }

    if (fB.equals(FormeEmpriseEnum.CERCLE)) {

    }

    if (fB.equals(FormeEmpriseEnum.FORME_U)) {
      lInt.add(1);
      lInt.add(5);

    }

    if (fB.equals(FormeEmpriseEnum.FORME_T)) {

      lInt.add(0);
      lInt.add(3);
      lInt.add(6);
    }

    if (fB.equals(FormeEmpriseEnum.FORME_L)) {
      lInt.add(1);
      lInt.add(4);

    }

    return lInt;
  }

}
