package fr.ign.cogit.simplu3d.model.regle;

import java.util.List;

public class Regle {

  private List<ElementRegle> conditions;
  private List<ElementRegle> consequences;
  private int id;

  private static int ID_COUNT = 0;

  public Regle(List<ElementRegle> conditions, List<ElementRegle> consequences) {
    super();
    this.conditions = conditions;
    this.consequences = consequences;
    id = (++ID_COUNT);
  }

  public static int getID_COUNT() {
    return ID_COUNT;
  }



  public List<ElementRegle> getConditions() {
    return conditions;
  }

  public void setConditions(List<ElementRegle> conditions) {
    this.conditions = conditions;
  }

  public List<ElementRegle> getConsequences() {
    return consequences;
  }

  public void setConsequences(List<ElementRegle> consequences) {
    this.consequences = consequences;
  }

  public int getId() {
    return id;
  }

  @Override
  public String toString() {

    int nbAntecedents = this.getConditions().size();
    int nbConsequences = this.getConsequences().size();

    if (nbAntecedents + nbConsequences == 0) {

      return "La règle est vide";
    }

    StringBuffer sb = new StringBuffer();

    if (nbAntecedents == 0) {
      sb.append("La règle est valable pour toutes les parcelles\n");

    }

    sb.append("CONDITIONS :\n");
    for (int i = 0; i < nbAntecedents; i = i + 2) {

      if (i < nbAntecedents - 1) {
        sb.append(this.getConditions().get(i));
        sb.append(" ");
        sb.append(this.getConditions().get(i + 1));
        sb.append(" ");
      } else {
        sb.append(this.getConditions().get(i));
        sb.append(".");
      }

    }

    if (nbConsequences == 0) {
      sb.append(" Il n'y a pas de contraintes pour ces parcelles ");
      return sb.toString();

    }

    sb.append("CONSEQUENCES :\n");

    for (int i = 0; i < nbConsequences; i = i + 2) {
      if (i < nbConsequences - 1) {
        sb.append(this.getConsequences().get(i));
        sb.append(" ");
        sb.append(this.getConsequences().get(i + 1));
        sb.append(" ");
      } else {
        sb.append(this.getConsequences().get(i));
        sb.append(".");
      }

    }

    return sb.toString();

  }

}
