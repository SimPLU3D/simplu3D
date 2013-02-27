package fr.ign.cogit.simplu3d.solver.interpreter;

/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2007 Matthias Braeuer (braeuer.matthias@web.de).            *
 * All rights reserved.                                                      *
 *                                                                           *
 * This work was done as a project at the Chair for Software Technology,     *
 * Dresden University Of Technology, Germany (http://st.inf.tu-dresden.de).  *
 * It is understood that any modification not identified as such is not      *
 * covered by the preceding statement.                                       *
 *                                                                           *
 * This work is free software; you can redistribute it and/or modify it      *
 * under the terms of the GNU Library General Public License as published    *
 * by the Free Software Foundation; either version 2 of the License, or      *
 * (at your option) any later version.                                       *
 *                                                                           *
 * This work is distributed in the hope that it will be useful, but WITHOUT  *
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or     *
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Library General Public     *
 * License for more details.                                                 *
 *                                                                           *
 * You should have received a copy of the GNU Library General Public License *
 * along with this library; if not, you can view it online at                *
 * http://www.fsf.org/licensing/licenses/gpl.html.                           *
 *                                                                           *
 * To submit a bug report, send a comment, or get the latest news on this    *
 * project, please visit the website: http://dresden-ocl.sourceforge.net.    *
 * For more information on OCL and related projects visit the OCL Portal:    *
 * http://st.inf.tu-dresden.de/ocl                                           *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * $Id$
 */


import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import tudresden.ocl20.pivot.essentialocl.expressions.BooleanLiteralExp;
import tudresden.ocl20.pivot.essentialocl.expressions.CallExp;
import tudresden.ocl20.pivot.essentialocl.expressions.CollectionItem;
import tudresden.ocl20.pivot.essentialocl.expressions.CollectionLiteralExp;
import tudresden.ocl20.pivot.essentialocl.expressions.CollectionLiteralPart;
import tudresden.ocl20.pivot.essentialocl.expressions.CollectionRange;
import tudresden.ocl20.pivot.essentialocl.expressions.EnumLiteralExp;
import tudresden.ocl20.pivot.essentialocl.expressions.ExpressionInOcl;
import tudresden.ocl20.pivot.essentialocl.expressions.FeatureCallExp;
import tudresden.ocl20.pivot.essentialocl.expressions.IfExp;
import tudresden.ocl20.pivot.essentialocl.expressions.IntegerLiteralExp;
import tudresden.ocl20.pivot.essentialocl.expressions.InvalidLiteralExp;
import tudresden.ocl20.pivot.essentialocl.expressions.IterateExp;
import tudresden.ocl20.pivot.essentialocl.expressions.IteratorExp;
import tudresden.ocl20.pivot.essentialocl.expressions.LetExp;
import tudresden.ocl20.pivot.essentialocl.expressions.LiteralExp;
import tudresden.ocl20.pivot.essentialocl.expressions.LoopExp;
import tudresden.ocl20.pivot.essentialocl.expressions.NumericLiteralExp;
import tudresden.ocl20.pivot.essentialocl.expressions.OclExpression;
import tudresden.ocl20.pivot.essentialocl.expressions.OperationCallExp;
import tudresden.ocl20.pivot.essentialocl.expressions.PrimitiveLiteralExp;
import tudresden.ocl20.pivot.essentialocl.expressions.PropertyCallExp;
import tudresden.ocl20.pivot.essentialocl.expressions.RealLiteralExp;
import tudresden.ocl20.pivot.essentialocl.expressions.StringLiteralExp;
import tudresden.ocl20.pivot.essentialocl.expressions.TupleLiteralExp;
import tudresden.ocl20.pivot.essentialocl.expressions.TupleLiteralPart;
import tudresden.ocl20.pivot.essentialocl.expressions.TypeLiteralExp;
import tudresden.ocl20.pivot.essentialocl.expressions.UndefinedLiteralExp;
import tudresden.ocl20.pivot.essentialocl.expressions.UnlimitedNaturalExp;
import tudresden.ocl20.pivot.essentialocl.expressions.Variable;
import tudresden.ocl20.pivot.essentialocl.expressions.VariableExp;
import tudresden.ocl20.pivot.essentialocl.expressions.impl.ExpressionsPackageImpl;
import tudresden.ocl20.pivot.pivotmodel.Expression;
import tudresden.ocl20.pivot.pivotmodel.NamedElement;
import tudresden.ocl20.pivot.pivotmodel.TypedElement;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see tudresden.ocl20.pivot.essentialocl.expressions.impl.ExpressionsPackageImpl
 * @generated
 */
public class CustomSwitch<T> {

  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static ExpressionsPackageImpl modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CustomSwitch() {
    if (modelPackage == null) {
      modelPackage = ExpressionsPackageImpl.eINSTANCE;
    }
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  public T doSwitch(EObject theEObject) {
    return doSwitch(theEObject.eClass(),theEObject);
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  protected T doSwitch(EClass theEClass, EObject theEObject) {
    if (theEClass.eContainer() == modelPackage) {
      return doSwitch(theEClass.getClassifierID(),theEObject);
    }
    else {
      List<EClass> eSuperTypes = theEClass.getESuperTypes();
      return eSuperTypes.isEmpty() ? defaultCase(theEObject) : doSwitch(eSuperTypes.get(0),
          theEObject);
    }
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  protected T doSwitch(int classifierID, EObject theEObject) {
    switch (classifierID) {
      case ExpressionsPackageImpl.VARIABLE_EXP: {
        VariableExp variableExp = (VariableExp) theEObject;
        T result = caseVariableExp(variableExp);
        if (result == null) result = caseOclExpression(variableExp);
        if (result == null) result = caseTypedElement(variableExp);
        if (result == null) result = caseNamedElement(variableExp);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.VARIABLE: {
        Variable variable = (Variable) theEObject;
        T result = caseVariable(variable);
        if (result == null) result = caseTypedElement(variable);
        if (result == null) result = caseNamedElement(variable);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.UNLIMITED_NATURAL_EXP: {
        UnlimitedNaturalExp unlimitedNaturalExp = (UnlimitedNaturalExp) theEObject;
        T result = caseUnlimitedNaturalExp(unlimitedNaturalExp);
        if (result == null) result = caseNumericLiteralExp(unlimitedNaturalExp);
        if (result == null) result = casePrimitiveLiteralExp(unlimitedNaturalExp);
        if (result == null) result = caseLiteralExp(unlimitedNaturalExp);
        if (result == null) result = caseOclExpression(unlimitedNaturalExp);
        if (result == null) result = caseTypedElement(unlimitedNaturalExp);
        if (result == null) result = caseNamedElement(unlimitedNaturalExp);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.TYPE_LITERAL_EXP: {
        TypeLiteralExp typeLiteralExp = (TypeLiteralExp) theEObject;
        T result = caseTypeLiteralExp(typeLiteralExp);
        if (result == null) result = caseLiteralExp(typeLiteralExp);
        if (result == null) result = caseOclExpression(typeLiteralExp);
        if (result == null) result = caseTypedElement(typeLiteralExp);
        if (result == null) result = caseNamedElement(typeLiteralExp);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.TUPLE_LITERAL_PART: {
        TupleLiteralPart tupleLiteralPart = (TupleLiteralPart) theEObject;
        T result = caseTupleLiteralPart(tupleLiteralPart);
        if (result == null) result = caseTypedElement(tupleLiteralPart);
        if (result == null) result = caseNamedElement(tupleLiteralPart);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.TUPLE_LITERAL_EXP: {
        TupleLiteralExp tupleLiteralExp = (TupleLiteralExp) theEObject;
        T result = caseTupleLiteralExp(tupleLiteralExp);
        if (result == null) result = caseLiteralExp(tupleLiteralExp);
        if (result == null) result = caseOclExpression(tupleLiteralExp);
        if (result == null) result = caseTypedElement(tupleLiteralExp);
        if (result == null) result = caseNamedElement(tupleLiteralExp);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.STRING_LITERAL_EXP: {
        StringLiteralExp stringLiteralExp = (StringLiteralExp) theEObject;
        T result = caseStringLiteralExp(stringLiteralExp);
        if (result == null) result = casePrimitiveLiteralExp(stringLiteralExp);
        if (result == null) result = caseLiteralExp(stringLiteralExp);
        if (result == null) result = caseOclExpression(stringLiteralExp);
        if (result == null) result = caseTypedElement(stringLiteralExp);
        if (result == null) result = caseNamedElement(stringLiteralExp);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.REAL_LITERAL_EXP: {
        RealLiteralExp realLiteralExp = (RealLiteralExp) theEObject;
        T result = caseRealLiteralExp(realLiteralExp);
        if (result == null) result = caseNumericLiteralExp(realLiteralExp);
        if (result == null) result = casePrimitiveLiteralExp(realLiteralExp);
        if (result == null) result = caseLiteralExp(realLiteralExp);
        if (result == null) result = caseOclExpression(realLiteralExp);
        if (result == null) result = caseTypedElement(realLiteralExp);
        if (result == null) result = caseNamedElement(realLiteralExp);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.PROPERTY_CALL_EXP: {
        PropertyCallExp propertyCallExp = (PropertyCallExp) theEObject;
        T result = casePropertyCallExp(propertyCallExp);
        if (result == null) result = caseFeatureCallExp(propertyCallExp);
        if (result == null) result = caseCallExp(propertyCallExp);
        if (result == null) result = caseOclExpression(propertyCallExp);
        if (result == null) result = caseTypedElement(propertyCallExp);
        if (result == null) result = caseNamedElement(propertyCallExp);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.PRIMITIVE_LITERAL_EXP: {
        PrimitiveLiteralExp primitiveLiteralExp = (PrimitiveLiteralExp) theEObject;
        T result = casePrimitiveLiteralExp(primitiveLiteralExp);
        if (result == null) result = caseLiteralExp(primitiveLiteralExp);
        if (result == null) result = caseOclExpression(primitiveLiteralExp);
        if (result == null) result = caseTypedElement(primitiveLiteralExp);
        if (result == null) result = caseNamedElement(primitiveLiteralExp);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.OPERATION_CALL_EXP: {
        OperationCallExp operationCallExp = (OperationCallExp) theEObject;
        T result = caseOperationCallExp(operationCallExp);
        if (result == null) result = caseFeatureCallExp(operationCallExp);
        if (result == null) result = caseCallExp(operationCallExp);
        if (result == null) result = caseOclExpression(operationCallExp);
        if (result == null) result = caseTypedElement(operationCallExp);
        if (result == null) result = caseNamedElement(operationCallExp);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.OCL_EXPRESSION: {
        OclExpression oclExpression = (OclExpression) theEObject;
        T result = caseOclExpression(oclExpression);
        if (result == null) result = caseTypedElement(oclExpression);
        if (result == null) result = caseNamedElement(oclExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.NUMERIC_LITERAL_EXP: {
        NumericLiteralExp numericLiteralExp = (NumericLiteralExp) theEObject;
        T result = caseNumericLiteralExp(numericLiteralExp);
        if (result == null) result = casePrimitiveLiteralExp(numericLiteralExp);
        if (result == null) result = caseLiteralExp(numericLiteralExp);
        if (result == null) result = caseOclExpression(numericLiteralExp);
        if (result == null) result = caseTypedElement(numericLiteralExp);
        if (result == null) result = caseNamedElement(numericLiteralExp);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.UNDEFINED_LITERAL_EXP: {
        UndefinedLiteralExp undefinedLiteralExp = (UndefinedLiteralExp) theEObject;
        T result = caseUndefinedLiteralExp(undefinedLiteralExp);
        if (result == null) result = caseLiteralExp(undefinedLiteralExp);
        if (result == null) result = caseOclExpression(undefinedLiteralExp);
        if (result == null) result = caseTypedElement(undefinedLiteralExp);
        if (result == null) result = caseNamedElement(undefinedLiteralExp);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.LOOP_EXP: {
        LoopExp loopExp = (LoopExp) theEObject;
        T result = caseLoopExp(loopExp);
        if (result == null) result = caseCallExp(loopExp);
        if (result == null) result = caseOclExpression(loopExp);
        if (result == null) result = caseTypedElement(loopExp);
        if (result == null) result = caseNamedElement(loopExp);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.LITERAL_EXP: {
        LiteralExp literalExp = (LiteralExp) theEObject;
        T result = caseLiteralExp(literalExp);
        if (result == null) result = caseOclExpression(literalExp);
        if (result == null) result = caseTypedElement(literalExp);
        if (result == null) result = caseNamedElement(literalExp);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.LET_EXP: {
        LetExp letExp = (LetExp) theEObject;
        T result = caseLetExp(letExp);
        if (result == null) result = caseOclExpression(letExp);
        if (result == null) result = caseTypedElement(letExp);
        if (result == null) result = caseNamedElement(letExp);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.ITERATOR_EXP: {
        IteratorExp iteratorExp = (IteratorExp) theEObject;
        T result = caseIteratorExp(iteratorExp);
        if (result == null) result = caseLoopExp(iteratorExp);
        if (result == null) result = caseCallExp(iteratorExp);
        if (result == null) result = caseOclExpression(iteratorExp);
        if (result == null) result = caseTypedElement(iteratorExp);
        if (result == null) result = caseNamedElement(iteratorExp);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.ITERATE_EXP: {
        IterateExp iterateExp = (IterateExp) theEObject;
        T result = caseIterateExp(iterateExp);
        if (result == null) result = caseLoopExp(iterateExp);
        if (result == null) result = caseCallExp(iterateExp);
        if (result == null) result = caseOclExpression(iterateExp);
        if (result == null) result = caseTypedElement(iterateExp);
        if (result == null) result = caseNamedElement(iterateExp);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.INVALID_LITERAL_EXP: {
        InvalidLiteralExp invalidLiteralExp = (InvalidLiteralExp) theEObject;
        T result = caseInvalidLiteralExp(invalidLiteralExp);
        if (result == null) result = caseLiteralExp(invalidLiteralExp);
        if (result == null) result = caseOclExpression(invalidLiteralExp);
        if (result == null) result = caseTypedElement(invalidLiteralExp);
        if (result == null) result = caseNamedElement(invalidLiteralExp);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.INTEGER_LITERAL_EXP: {
        IntegerLiteralExp integerLiteralExp = (IntegerLiteralExp) theEObject;
        T result = caseIntegerLiteralExp(integerLiteralExp);
        if (result == null) result = caseNumericLiteralExp(integerLiteralExp);
        if (result == null) result = casePrimitiveLiteralExp(integerLiteralExp);
        if (result == null) result = caseLiteralExp(integerLiteralExp);
        if (result == null) result = caseOclExpression(integerLiteralExp);
        if (result == null) result = caseTypedElement(integerLiteralExp);
        if (result == null) result = caseNamedElement(integerLiteralExp);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.IF_EXP: {
        IfExp ifExp = (IfExp) theEObject;
        T result = caseIfExp(ifExp);
        if (result == null) result = caseOclExpression(ifExp);
        if (result == null) result = caseTypedElement(ifExp);
        if (result == null) result = caseNamedElement(ifExp);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.FEATURE_CALL_EXP: {
        FeatureCallExp featureCallExp = (FeatureCallExp) theEObject;
        T result = caseFeatureCallExp(featureCallExp);
        if (result == null) result = caseCallExp(featureCallExp);
        if (result == null) result = caseOclExpression(featureCallExp);
        if (result == null) result = caseTypedElement(featureCallExp);
        if (result == null) result = caseNamedElement(featureCallExp);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.BOOLEAN_LITERAL_EXP: {
        BooleanLiteralExp booleanLiteralExp = (BooleanLiteralExp) theEObject;
        T result = caseBooleanLiteralExp(booleanLiteralExp);
        if (result == null) result = casePrimitiveLiteralExp(booleanLiteralExp);
        if (result == null) result = caseLiteralExp(booleanLiteralExp);
        if (result == null) result = caseOclExpression(booleanLiteralExp);
        if (result == null) result = caseTypedElement(booleanLiteralExp);
        if (result == null) result = caseNamedElement(booleanLiteralExp);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.CALL_EXP: {
        CallExp callExp = (CallExp) theEObject;
        T result = caseCallExp(callExp);
        if (result == null) result = caseOclExpression(callExp);
        if (result == null) result = caseTypedElement(callExp);
        if (result == null) result = caseNamedElement(callExp);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.COLLECTION_ITEM: {
        CollectionItem collectionItem = (CollectionItem) theEObject;
        T result = caseCollectionItem(collectionItem);
        if (result == null) result = caseCollectionLiteralPart(collectionItem);
        if (result == null) result = caseTypedElement(collectionItem);
        if (result == null) result = caseNamedElement(collectionItem);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.COLLECTION_LITERAL_EXP: {
        CollectionLiteralExp collectionLiteralExp = (CollectionLiteralExp) theEObject;
        T result = caseCollectionLiteralExp(collectionLiteralExp);
        if (result == null) result = caseLiteralExp(collectionLiteralExp);
        if (result == null) result = caseOclExpression(collectionLiteralExp);
        if (result == null) result = caseTypedElement(collectionLiteralExp);
        if (result == null) result = caseNamedElement(collectionLiteralExp);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.COLLECTION_LITERAL_PART: {
        CollectionLiteralPart collectionLiteralPart = (CollectionLiteralPart) theEObject;
        T result = caseCollectionLiteralPart(collectionLiteralPart);
        if (result == null) result = caseTypedElement(collectionLiteralPart);
        if (result == null) result = caseNamedElement(collectionLiteralPart);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.COLLECTION_RANGE: {
        CollectionRange collectionRange = (CollectionRange) theEObject;
        T result = caseCollectionRange(collectionRange);
        if (result == null) result = caseCollectionLiteralPart(collectionRange);
        if (result == null) result = caseTypedElement(collectionRange);
        if (result == null) result = caseNamedElement(collectionRange);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.ENUM_LITERAL_EXP: {
        EnumLiteralExp enumLiteralExp = (EnumLiteralExp) theEObject;
        T result = caseEnumLiteralExp(enumLiteralExp);
        if (result == null) result = caseLiteralExp(enumLiteralExp);
        if (result == null) result = caseOclExpression(enumLiteralExp);
        if (result == null) result = caseTypedElement(enumLiteralExp);
        if (result == null) result = caseNamedElement(enumLiteralExp);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ExpressionsPackageImpl.EXPRESSION_IN_OCL: {
        ExpressionInOcl expressionInOcl = (ExpressionInOcl) theEObject;
        T result = caseExpressionInOcl(expressionInOcl);
        if (result == null) result = caseExpression(expressionInOcl);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      default:
        return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Variable Exp</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Variable Exp</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseVariableExp(VariableExp object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Variable</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Variable</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseVariable(Variable object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Unlimited Natural Exp</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Unlimited Natural Exp</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseUnlimitedNaturalExp(UnlimitedNaturalExp object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Type Literal Exp</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Type Literal Exp</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTypeLiteralExp(TypeLiteralExp object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Tuple Literal Part</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Tuple Literal Part</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTupleLiteralPart(TupleLiteralPart object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Tuple Literal Exp</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Tuple Literal Exp</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTupleLiteralExp(TupleLiteralExp object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>String Literal Exp</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>String Literal Exp</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseStringLiteralExp(StringLiteralExp object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Real Literal Exp</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Real Literal Exp</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseRealLiteralExp(RealLiteralExp object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Property Call Exp</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Property Call Exp</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casePropertyCallExp(PropertyCallExp object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Primitive Literal Exp</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Primitive Literal Exp</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casePrimitiveLiteralExp(PrimitiveLiteralExp object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Operation Call Exp</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Operation Call Exp</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOperationCallExp(OperationCallExp object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Ocl Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Ocl Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOclExpression(OclExpression object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Numeric Literal Exp</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Numeric Literal Exp</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseNumericLiteralExp(NumericLiteralExp object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Undefined Literal Exp</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Undefined Literal Exp</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseUndefinedLiteralExp(UndefinedLiteralExp object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Loop Exp</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Loop Exp</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseLoopExp(LoopExp object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Literal Exp</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Literal Exp</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseLiteralExp(LiteralExp object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Let Exp</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Let Exp</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseLetExp(LetExp object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Iterator Exp</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Iterator Exp</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIteratorExp(IteratorExp object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Iterate Exp</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Iterate Exp</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIterateExp(IterateExp object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Invalid Literal Exp</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Invalid Literal Exp</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseInvalidLiteralExp(InvalidLiteralExp object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Integer Literal Exp</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Integer Literal Exp</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIntegerLiteralExp(IntegerLiteralExp object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>If Exp</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>If Exp</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIfExp(IfExp object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Feature Call Exp</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Feature Call Exp</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseFeatureCallExp(FeatureCallExp object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Boolean Literal Exp</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Boolean Literal Exp</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseBooleanLiteralExp(BooleanLiteralExp object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Call Exp</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Call Exp</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCallExp(CallExp object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Collection Item</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Collection Item</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCollectionItem(CollectionItem object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Collection Literal Exp</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Collection Literal Exp</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCollectionLiteralExp(CollectionLiteralExp object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Collection Literal Part</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Collection Literal Part</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCollectionLiteralPart(CollectionLiteralPart object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Collection Range</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Collection Range</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCollectionRange(CollectionRange object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Enum Literal Exp</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Enum Literal Exp</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseEnumLiteralExp(EnumLiteralExp object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Expression In Ocl</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Expression In Ocl</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseExpressionInOcl(ExpressionInOcl object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Typed Element</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Typed Element</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTypedElement(TypedElement object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Named Element</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Named Element</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseNamedElement(NamedElement object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseExpression(Expression object) {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
  public T defaultCase(EObject object) {
    return null;
  }

} //ExpressionsSwitch
