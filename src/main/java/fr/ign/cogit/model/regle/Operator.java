package fr.ign.cogit.model.regle;

public class Operator implements ElementRegle {

  public enum Operateurs {
    NOT("!"), AND("AND"), OR("OR");

    private String nomOp;

    Operateurs(String nomCouche) {
      this.nomOp = nomCouche;
    }

    public String getNomCouche() {
      return nomOp;
    }

  }

  private Operateurs op;

  public Operator(Operateurs op) {
    this.op = op;
  }

  public Operateurs getOp() {
    return op;
  }

}
