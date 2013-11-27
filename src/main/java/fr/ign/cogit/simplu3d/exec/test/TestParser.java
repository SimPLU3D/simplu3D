package fr.ign.cogit.simplu3d.exec.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.util.AbstractTreeIterator;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;

import tudresden.ocl20.pivot.essentialocl.expressions.impl.ExpressionInOclImpl;
import tudresden.ocl20.pivot.essentialocl.expressions.impl.IntegerLiteralExpImpl;
import tudresden.ocl20.pivot.essentialocl.expressions.impl.IteratorExpImpl;
import tudresden.ocl20.pivot.essentialocl.expressions.impl.OperationCallExpImpl;
import tudresden.ocl20.pivot.essentialocl.expressions.impl.PropertyCallExpImpl;
import tudresden.ocl20.pivot.essentialocl.expressions.impl.TypeLiteralExpImpl;
import tudresden.ocl20.pivot.essentialocl.expressions.impl.VariableExpImpl;
import tudresden.ocl20.pivot.essentialocl.expressions.impl.VariableImpl;
import tudresden.ocl20.pivot.model.IModel;
import tudresden.ocl20.pivot.parser.ParseException;
import tudresden.ocl20.pivot.pivotmodel.Constraint;
import tudresden.ocl20.pivot.pivotmodel.impl.ConstraintImpl;
import tudresden.ocl20.pivot.standalone.facade.StandaloneFacade;
import fr.ign.cogit.simplu3d.importer.model.ImportModelInstanceEnvironnement;

public class TestParser {

  /**
   * @param args
   * @throws ParseException
   * @throws IOException
   */
  public static void main(String[] args) throws IOException, ParseException {

    // Fichier contenant les contraintes OCL à appliquer
    File oclConstraints = new File("src/main/resources/ocl/UB16.ocl");

    System.out.println("*******************************************");
    System.out.println("************Import modèle******************");
    System.out.println("*******************************************");

    IModel model = ImportModelInstanceEnvironnement
        .getModel("target/classes/fr/ign/cogit/simplu3d/importer/model/ModelProviderClass.class");

    System.out.println("*******************************************");
    System.out.println("****Chargement des contraintes OCL*********");
    System.out.println("*******************************************");

    List<Constraint> lC = StandaloneFacade.INSTANCE.parseOclConstraints(model, oclConstraints);
    Constraint c = lC.get(0);

    afficheConstraint(c);

    ConstraintImpl c1 = (ConstraintImpl) c.clone();

    AbstractTreeIterator<EObject> aTI = (AbstractTreeIterator<EObject>) c1.eAllContents();

    while (aTI.hasNext()) {

      /* EObject e = */aTI.next();

    }

    afficheConstraint((Constraint) c1);

  }

  public static void afficheConstraint(Constraint c) {

    TreeIterator<EObject> ti = c.eAllContents();

    AbstractTreeIterator<EObject> aTI = (AbstractTreeIterator<EObject>) ti;

    while (aTI.hasNext()) {

      int size = aTI.size();

      String s = "";

      for (int i = 0; i < size; i++) {
        s = s + "-";
      }

      System.out.println(s + toString(aTI.next()));

    }

  }

  public static String toString(EObject eo) {

    String notfound = "not found ";
    if (eo instanceof OperationCallExpImpl) {

      OperationCallExpImpl oCEI = (OperationCallExpImpl) eo;

      return oCEI.getReferredOperation().getName().toString();

    } else
      if (eo instanceof PropertyCallExpImpl) {

        PropertyCallExpImpl oCEI = (PropertyCallExpImpl) eo;

        return oCEI.getReferredProperty().getName().toString();

      } else
        if (eo instanceof VariableExpImpl) {

          VariableExpImpl oCEI = (VariableExpImpl) eo;

          return oCEI.getReferredVariable().getName().toString();

        } else
          if (eo instanceof TypeLiteralExpImpl) {

            TypeLiteralExpImpl oCEI = (TypeLiteralExpImpl) eo;

            return oCEI.getReferredType().getName().toString();

          } else
            if (eo instanceof IntegerLiteralExpImpl) {

              IntegerLiteralExpImpl oCEI = (IntegerLiteralExpImpl) eo;

              return oCEI.getIntegerSymbol() + "";

            } else
              if (eo instanceof VariableImpl) {

                VariableImpl oCEI = (VariableImpl) eo;

                return oCEI.getType().toString();

              } else
                if (eo instanceof IteratorExpImpl) {

                  IteratorExpImpl oCEI = (IteratorExpImpl) eo;

                  return oCEI.getName();

                } else
                  if (eo instanceof ExpressionInOclImpl) {

                    ExpressionInOclImpl oCEI = (ExpressionInOclImpl) eo;

                    return oCEI.getBodyExpression().toString();

                  }

    return notfound + "   " + eo.getClass();

  }

}
