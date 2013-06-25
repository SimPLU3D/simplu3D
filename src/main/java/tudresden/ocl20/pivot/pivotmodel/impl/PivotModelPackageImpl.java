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
package tudresden.ocl20.pivot.pivotmodel.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import tudresden.ocl20.pivot.datatypes.DatatypesPackage;
import tudresden.ocl20.pivot.datatypes.impl.DatatypesPackageImpl;
import tudresden.ocl20.pivot.pivotmodel.AssociationProperty;
import tudresden.ocl20.pivot.pivotmodel.ComplexGenericType;
import tudresden.ocl20.pivot.pivotmodel.ConstrainableElement;
import tudresden.ocl20.pivot.pivotmodel.Constraint;
import tudresden.ocl20.pivot.pivotmodel.ConstraintKind;
import tudresden.ocl20.pivot.pivotmodel.Enumeration;
import tudresden.ocl20.pivot.pivotmodel.EnumerationLiteral;
import tudresden.ocl20.pivot.pivotmodel.Expression;
import tudresden.ocl20.pivot.pivotmodel.Feature;
import tudresden.ocl20.pivot.pivotmodel.GenericElement;
import tudresden.ocl20.pivot.pivotmodel.GenericType;
import tudresden.ocl20.pivot.pivotmodel.NamedElement;
import tudresden.ocl20.pivot.pivotmodel.Namespace;
import tudresden.ocl20.pivot.pivotmodel.Operation;
import tudresden.ocl20.pivot.pivotmodel.Parameter;
import tudresden.ocl20.pivot.pivotmodel.ParameterDirectionKind;
import tudresden.ocl20.pivot.pivotmodel.ParameterGenericType;
import tudresden.ocl20.pivot.pivotmodel.PivotModelFactory;
import tudresden.ocl20.pivot.pivotmodel.PivotModelPackage;
import tudresden.ocl20.pivot.pivotmodel.PrimitiveType;
import tudresden.ocl20.pivot.pivotmodel.PrimitiveTypeKind;
import tudresden.ocl20.pivot.pivotmodel.Property;
import tudresden.ocl20.pivot.pivotmodel.Type;
import tudresden.ocl20.pivot.pivotmodel.TypeArgument;
import tudresden.ocl20.pivot.pivotmodel.TypeParameter;
import tudresden.ocl20.pivot.pivotmodel.TypedElement;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains
 * accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * 
 * @generated
 */
public class PivotModelPackageImpl extends EPackageImpl implements
		PivotModelPackage {

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass enumerationEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass namedElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass operationEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass namespaceEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass typeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass parameterEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass enumerationLiteralEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass propertyEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass typedElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass primitiveTypeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass featureEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass constraintEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass expressionEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass associationPropertyEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass constrainableElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass genericElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass genericTypeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass typeParameterEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass typeArgumentEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass parameterGenericTypeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass complexGenericTypeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EEnum parameterDirectionKindEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EEnum constraintKindEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EEnum primitiveTypeKindEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EDataType cloneNotSupportedExceptionEDataType = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the
	 * package package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static factory
	 * method {@link #init init()}, which also performs initialization of the
	 * package, or returns the registered package, if one already exists. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private PivotModelPackageImpl() {

		super(eNS_URI, PivotModelFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and
	 * for any others upon which it depends.
	 * 
	 * <p>
	 * This method is used to initialize {@link PivotModelPackage#eINSTANCE} when
	 * that field is accessed. Clients should not invoke it directly. Instead,
	 * they should simply access that field to obtain the package. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static PivotModelPackage init() {

		if (isInited)
			return (PivotModelPackage) EPackage.Registry.INSTANCE
					.getEPackage(PivotModelPackage.eNS_URI);

		// Obtain or create and register package
		PivotModelPackageImpl thePivotModelPackage =
				(PivotModelPackageImpl) (EPackage.Registry.INSTANCE.get(eNS_URI) instanceof PivotModelPackageImpl ? EPackage.Registry.INSTANCE
						.get(eNS_URI) : new PivotModelPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		DatatypesPackageImpl theDatatypesPackage =
				(DatatypesPackageImpl) (EPackage.Registry.INSTANCE
						.getEPackage(DatatypesPackage.eNS_URI) instanceof DatatypesPackageImpl ? EPackage.Registry.INSTANCE
						.getEPackage(DatatypesPackage.eNS_URI) : DatatypesPackage.eINSTANCE);

		// Create package meta-data objects
		thePivotModelPackage.createPackageContents();
		theDatatypesPackage.createPackageContents();

		// Initialize created meta-data
		thePivotModelPackage.initializePackageContents();
		theDatatypesPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		thePivotModelPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(PivotModelPackage.eNS_URI,
				thePivotModelPackage);
		return thePivotModelPackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getEnumeration() {

		return enumerationEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getEnumeration_OwnedLiteral() {

		return (EReference) enumerationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getNamedElement() {

		return namedElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getNamedElement_Name() {

		return (EAttribute) namedElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getNamedElement_QualifiedName() {

		return (EAttribute) namedElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getNamedElement_Owner() {

		return (EReference) namedElementEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getOperation() {

		return operationEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getOperation_OwningType() {

		return (EReference) operationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getOperation_OwnedParameter() {

		return (EReference) operationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getOperation_InputParameter() {

		return (EReference) operationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getOperation_OutputParameter() {

		return (EReference) operationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getOperation_ReturnParameter() {

		return (EReference) operationEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getOperation_SignatureParameter() {

		return (EReference) operationEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getNamespace() {

		return namespaceEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getNamespace_OwnedType() {

		return (EReference) namespaceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getNamespace_OwnedRule() {

		return (EReference) namespaceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getNamespace_NestedNamespace() {

		return (EReference) namespaceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getNamespace_NestingNamespace() {

		return (EReference) namespaceEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getType() {

		return typeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getType_SuperType() {

		return (EReference) typeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getType_GenericSuperType() {

		return (EReference) typeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getType_OwnedOperation() {

		return (EReference) typeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getType_OwnedProperty() {

		return (EReference) typeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getType_Namespace() {

		return (EReference) typeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getParameter() {

		return parameterEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getParameter_Kind() {

		return (EAttribute) parameterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getParameter_Operation() {

		return (EReference) parameterEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getEnumerationLiteral() {

		return enumerationLiteralEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getEnumerationLiteral_Enumeration() {

		return (EReference) enumerationLiteralEClass.getEStructuralFeatures()
				.get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getProperty() {

		return propertyEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getProperty_OwningType() {

		return (EReference) propertyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getTypedElement() {

		return typedElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getTypedElement_Type() {

		return (EReference) typedElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getTypedElement_GenericType() {

		return (EReference) typedElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getPrimitiveType() {

		return primitiveTypeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getPrimitiveType_Kind() {

		return (EAttribute) primitiveTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getFeature() {

		return featureEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getFeature_Static() {

		return (EAttribute) featureEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getFeature_Semantics() {

		return (EReference) featureEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getConstraint() {

		return constraintEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getConstraint_Kind() {

		return (EAttribute) constraintEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getConstraint_Namespace() {

		return (EReference) constraintEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getConstraint_Specification() {

		return (EReference) constraintEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getConstraint_ConstrainedElement() {

		return (EReference) constraintEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getConstraint_DefinedFeature() {

		return (EReference) constraintEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getExpression() {

		return expressionEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getExpression_Body() {

		return (EAttribute) expressionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getExpression_Language() {

		return (EAttribute) expressionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getExpression_Constraint() {

		return (EReference) expressionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getAssociationProperty() {

		return associationPropertyEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAssociationProperty_InverseAssociationProperties() {

		return (EReference) associationPropertyEClass.getEStructuralFeatures().get(
				0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getConstrainableElement() {

		return constrainableElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getGenericElement() {

		return genericElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getGenericElement_OwnedTypeParameter() {

		return (EReference) genericElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getGenericType() {

		return genericTypeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getTypeParameter() {

		return typeParameterEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getTypeParameter_GenericElement() {

		return (EReference) typeParameterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getTypeArgument() {

		return typeArgumentEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getTypeArgument_OwningGenericType() {

		return (EReference) typeArgumentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getParameterGenericType() {

		return parameterGenericTypeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getParameterGenericType_TypeParameter() {

		return (EReference) parameterGenericTypeEClass.getEStructuralFeatures()
				.get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getComplexGenericType() {

		return complexGenericTypeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getComplexGenericType_UnboundType() {

		return (EReference) complexGenericTypeEClass.getEStructuralFeatures()
				.get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getComplexGenericType_TypeArgument() {

		return (EReference) complexGenericTypeEClass.getEStructuralFeatures()
				.get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EEnum getParameterDirectionKind() {

		return parameterDirectionKindEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EEnum getConstraintKind() {

		return constraintKindEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EEnum getPrimitiveTypeKind() {

		return primitiveTypeKindEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EDataType getCloneNotSupportedException() {

		return cloneNotSupportedExceptionEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public PivotModelFactory getPivotModelFactory() {

		return (PivotModelFactory) getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package. This method is guarded to
	 * have no affect on any invocation but its first. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void createPackageContents() {

		if (isCreated)
			return;
		isCreated = true;

		// Create classes and their features
		namedElementEClass = createEClass(NAMED_ELEMENT);
		createEAttribute(namedElementEClass, NAMED_ELEMENT__NAME);
		createEAttribute(namedElementEClass, NAMED_ELEMENT__QUALIFIED_NAME);
		createEReference(namedElementEClass, NAMED_ELEMENT__OWNER);

		typedElementEClass = createEClass(TYPED_ELEMENT);
		createEReference(typedElementEClass, TYPED_ELEMENT__TYPE);
		createEReference(typedElementEClass, TYPED_ELEMENT__GENERIC_TYPE);

		featureEClass = createEClass(FEATURE);
		createEAttribute(featureEClass, FEATURE__STATIC);
		createEReference(featureEClass, FEATURE__SEMANTICS);

		genericElementEClass = createEClass(GENERIC_ELEMENT);
		createEReference(genericElementEClass,
				GENERIC_ELEMENT__OWNED_TYPE_PARAMETER);

		constrainableElementEClass = createEClass(CONSTRAINABLE_ELEMENT);

		namespaceEClass = createEClass(NAMESPACE);
		createEReference(namespaceEClass, NAMESPACE__OWNED_TYPE);
		createEReference(namespaceEClass, NAMESPACE__OWNED_RULE);
		createEReference(namespaceEClass, NAMESPACE__NESTED_NAMESPACE);
		createEReference(namespaceEClass, NAMESPACE__NESTING_NAMESPACE);

		typeEClass = createEClass(TYPE);
		createEReference(typeEClass, TYPE__NAMESPACE);
		createEReference(typeEClass, TYPE__OWNED_OPERATION);
		createEReference(typeEClass, TYPE__OWNED_PROPERTY);
		createEReference(typeEClass, TYPE__SUPER_TYPE);
		createEReference(typeEClass, TYPE__GENERIC_SUPER_TYPE);

		primitiveTypeEClass = createEClass(PRIMITIVE_TYPE);
		createEAttribute(primitiveTypeEClass, PRIMITIVE_TYPE__KIND);

		enumerationEClass = createEClass(ENUMERATION);
		createEReference(enumerationEClass, ENUMERATION__OWNED_LITERAL);

		enumerationLiteralEClass = createEClass(ENUMERATION_LITERAL);
		createEReference(enumerationLiteralEClass, ENUMERATION_LITERAL__ENUMERATION);

		propertyEClass = createEClass(PROPERTY);
		createEReference(propertyEClass, PROPERTY__OWNING_TYPE);

		operationEClass = createEClass(OPERATION);
		createEReference(operationEClass, OPERATION__OWNING_TYPE);
		createEReference(operationEClass, OPERATION__OWNED_PARAMETER);
		createEReference(operationEClass, OPERATION__INPUT_PARAMETER);
		createEReference(operationEClass, OPERATION__OUTPUT_PARAMETER);
		createEReference(operationEClass, OPERATION__RETURN_PARAMETER);
		createEReference(operationEClass, OPERATION__SIGNATURE_PARAMETER);

		parameterEClass = createEClass(PARAMETER);
		createEAttribute(parameterEClass, PARAMETER__KIND);
		createEReference(parameterEClass, PARAMETER__OPERATION);

		genericTypeEClass = createEClass(GENERIC_TYPE);

		parameterGenericTypeEClass = createEClass(PARAMETER_GENERIC_TYPE);
		createEReference(parameterGenericTypeEClass,
				PARAMETER_GENERIC_TYPE__TYPE_PARAMETER);

		complexGenericTypeEClass = createEClass(COMPLEX_GENERIC_TYPE);
		createEReference(complexGenericTypeEClass,
				COMPLEX_GENERIC_TYPE__UNBOUND_TYPE);
		createEReference(complexGenericTypeEClass,
				COMPLEX_GENERIC_TYPE__TYPE_ARGUMENT);

		typeParameterEClass = createEClass(TYPE_PARAMETER);
		createEReference(typeParameterEClass, TYPE_PARAMETER__GENERIC_ELEMENT);

		typeArgumentEClass = createEClass(TYPE_ARGUMENT);
		createEReference(typeArgumentEClass, TYPE_ARGUMENT__OWNING_GENERIC_TYPE);

		constraintEClass = createEClass(CONSTRAINT);
		createEAttribute(constraintEClass, CONSTRAINT__KIND);
		createEReference(constraintEClass, CONSTRAINT__NAMESPACE);
		createEReference(constraintEClass, CONSTRAINT__SPECIFICATION);
		createEReference(constraintEClass, CONSTRAINT__CONSTRAINED_ELEMENT);
		createEReference(constraintEClass, CONSTRAINT__DEFINED_FEATURE);

		expressionEClass = createEClass(EXPRESSION);
		createEAttribute(expressionEClass, EXPRESSION__BODY);
		createEAttribute(expressionEClass, EXPRESSION__LANGUAGE);
		createEReference(expressionEClass, EXPRESSION__CONSTRAINT);

		associationPropertyEClass = createEClass(ASSOCIATION_PROPERTY);
		createEReference(associationPropertyEClass,
				ASSOCIATION_PROPERTY__INVERSE_ASSOCIATION_PROPERTIES);

		// Create enums
		primitiveTypeKindEEnum = createEEnum(PRIMITIVE_TYPE_KIND);
		parameterDirectionKindEEnum = createEEnum(PARAMETER_DIRECTION_KIND);
		constraintKindEEnum = createEEnum(CONSTRAINT_KIND);

		// Create data types
		cloneNotSupportedExceptionEDataType =
				createEDataType(CLONE_NOT_SUPPORTED_EXCEPTION);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model. This method
	 * is guarded to have no affect on any invocation but its first. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void initializePackageContents() {

		if (isInitialized)
			return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		DatatypesPackage theDatatypesPackage =
				(DatatypesPackage) EPackage.Registry.INSTANCE
						.getEPackage(DatatypesPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		typedElementEClass.getESuperTypes().add(this.getNamedElement());
		featureEClass.getESuperTypes().add(this.getTypedElement());
		genericElementEClass.getESuperTypes().add(this.getNamedElement());
		namespaceEClass.getESuperTypes().add(this.getNamedElement());
		namespaceEClass.getESuperTypes().add(this.getGenericElement());
		typeEClass.getESuperTypes().add(this.getNamedElement());
		typeEClass.getESuperTypes().add(this.getConstrainableElement());
		typeEClass.getESuperTypes().add(this.getGenericElement());
		primitiveTypeEClass.getESuperTypes().add(this.getType());
		enumerationEClass.getESuperTypes().add(this.getType());
		enumerationLiteralEClass.getESuperTypes().add(this.getNamedElement());
		propertyEClass.getESuperTypes().add(this.getFeature());
		propertyEClass.getESuperTypes().add(this.getTypedElement());
		propertyEClass.getESuperTypes().add(this.getNamedElement());
		propertyEClass.getESuperTypes().add(this.getConstrainableElement());
		operationEClass.getESuperTypes().add(this.getFeature());
		operationEClass.getESuperTypes().add(this.getTypedElement());
		operationEClass.getESuperTypes().add(this.getNamedElement());
		operationEClass.getESuperTypes().add(this.getConstrainableElement());
		operationEClass.getESuperTypes().add(this.getGenericElement());
		parameterEClass.getESuperTypes().add(this.getTypedElement());
		parameterEClass.getESuperTypes().add(this.getNamedElement());
		genericTypeEClass.getESuperTypes().add(this.getNamedElement());
		parameterGenericTypeEClass.getESuperTypes().add(this.getGenericType());
		complexGenericTypeEClass.getESuperTypes().add(this.getGenericType());
		typeParameterEClass.getESuperTypes().add(this.getNamedElement());
		typeArgumentEClass.getESuperTypes().add(this.getTypedElement());
		constraintEClass.getESuperTypes().add(this.getNamedElement());
		associationPropertyEClass.getESuperTypes().add(this.getProperty());

		// Initialize classes and features; add operations and parameters
		initEClass(namedElementEClass, NamedElement.class,
				"NamedElement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(
				getNamedElement_Name(),
				theDatatypesPackage.getString(),
				"name", "", 1, 1, NamedElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$
		initEAttribute(
				getNamedElement_QualifiedName(),
				theDatatypesPackage.getString(),
				"qualifiedName", null, 0, 1, NamedElement.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getNamedElement_Owner(),
				this.getNamedElement(),
				null,
				"owner", null, 0, 1, NamedElement.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		EOperation op =
				addEOperation(namedElementEClass, this.getNamedElement(),
						"clone", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEException(op, this.getCloneNotSupportedException());

		addEOperation(namedElementEClass, theDatatypesPackage.getString(),
				"getQualifiedNameList", 0, -1, !IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		initEClass(typedElementEClass, TypedElement.class,
				"TypedElement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getTypedElement_Type(),
				this.getType(),
				null,
				"type", null, 1, 1, TypedElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getTypedElement_GenericType(),
				this.getGenericType(),
				null,
				"genericType", null, 0, 1, TypedElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(featureEClass, Feature.class,
				"Feature", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(
				getFeature_Static(),
				theDatatypesPackage.getBoolean(),
				"static", "false", 0, 1, Feature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$
		initEReference(
				getFeature_Semantics(),
				this.getConstraint(),
				this.getConstraint_DefinedFeature(),
				"semantics", null, 0, 1, Feature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(
				genericElementEClass,
				GenericElement.class,
				"GenericElement", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getGenericElement_OwnedTypeParameter(),
				this.getTypeParameter(),
				this.getTypeParameter_GenericElement(),
				"ownedTypeParameter", null, 0, -1, GenericElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(genericElementEClass, this.getNamedElement(),
						"bindTypeParameter", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		EGenericType g1 = createEGenericType(theDatatypesPackage.getSequence());
		EGenericType g2 = createEGenericType(this.getTypeParameter());
		g1.getETypeArguments().add(g2);
		addEParameter(op, g1, "parameters", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		g1 = createEGenericType(theDatatypesPackage.getSequence());
		g2 = createEGenericType();
		g1.getETypeArguments().add(g2);
		EGenericType g3 = createEGenericType(this.getType());
		g2.setEUpperBound(g3);
		addEParameter(op, g1, "types", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(genericElementEClass, this.getGenericElement(),
						"addTypeParameter", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, this.getTypeParameter(),
				"typeParameter", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		initEClass(
				constrainableElementEClass,
				ConstrainableElement.class,
				"ConstrainableElement", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(namespaceEClass, Namespace.class,
				"Namespace", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getNamespace_OwnedType(),
				this.getType(),
				this.getType_Namespace(),
				"ownedType", null, 0, -1, Namespace.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getNamespace_OwnedRule(),
				this.getConstraint(),
				this.getConstraint_Namespace(),
				"ownedRule", null, 0, -1, Namespace.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getNamespace_NestedNamespace(),
				this.getNamespace(),
				this.getNamespace_NestingNamespace(),
				"nestedNamespace", null, 0, -1, Namespace.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getNamespace_NestingNamespace(),
				this.getNamespace(),
				this.getNamespace_NestedNamespace(),
				"nestingNamespace", null, 0, 1, Namespace.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(namespaceEClass, this.getNamespace(),
						"addType", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, this.getType(), "type", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(namespaceEClass, this.getNamespace(),
						"addRule", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, this.getConstraint(), "rule", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(namespaceEClass, this.getNamespace(),
						"addNestedNamespace", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, this.getNamespace(),
				"namespace", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		addEOperation(namespaceEClass, this.getConstraint(),
				"getOwnedAndNestedRules", 0, -1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(namespaceEClass, this.getType(),
						"lookupType", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, theDatatypesPackage.getString(),
				"name", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(namespaceEClass, this.getNamespace(),
						"lookupNamespace", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, theDatatypesPackage.getString(),
				"name", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		addEOperation(namespaceEClass, theDatatypesPackage.getBoolean(),
				"removeOwnedAndNestedRules", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(namespaceEClass, theDatatypesPackage.getBoolean(),
						"removeOwnedAndNestedRules", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, this.getConstraint(),
				"constraints", 0, -1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		initEClass(typeEClass, Type.class,
				"Type", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getType_Namespace(),
				this.getNamespace(),
				this.getNamespace_OwnedType(),
				"namespace", null, 0, 1, Type.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getType_OwnedOperation(),
				this.getOperation(),
				this.getOperation_OwningType(),
				"ownedOperation", null, 0, -1, Type.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getType_OwnedProperty(),
				this.getProperty(),
				this.getProperty_OwningType(),
				"ownedProperty", null, 0, -1, Type.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getType_SuperType(),
				this.getType(),
				null,
				"superType", null, 0, -1, Type.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getType_GenericSuperType(),
				this.getGenericType(),
				null,
				"genericSuperType", null, 0, -1, Type.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(typeEClass, theDatatypesPackage.getBoolean(),
						"conformsTo", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, this.getType(), "other", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(typeEClass, this.getType(),
						"commonSuperType", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, this.getType(), "other", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(typeEClass, null,
						"allProperties", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		g1 = createEGenericType(theDatatypesPackage.getSequence());
		g2 = createEGenericType(this.getProperty());
		g1.getETypeArguments().add(g2);
		initEOperation(op, g1);

		op =
				addEOperation(typeEClass, null,
						"allOperations", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		g1 = createEGenericType(theDatatypesPackage.getSequence());
		g2 = createEGenericType(this.getOperation());
		g1.getETypeArguments().add(g2);
		initEOperation(op, g1);

		op =
				addEOperation(typeEClass, this.getProperty(),
						"lookupProperty", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, theDatatypesPackage.getString(),
				"name", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(typeEClass, this.getOperation(),
						"lookupOperation", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, theDatatypesPackage.getString(),
				"name", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		g1 = createEGenericType(theDatatypesPackage.getSequence());
		g2 = createEGenericType(this.getType());
		g1.getETypeArguments().add(g2);
		addEParameter(op, g1, "paramTypes", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(typeEClass, this.getType(),
						"addProperty", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, this.getProperty(),
				"property", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(typeEClass, this.getType(),
						"addOperation", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, this.getOperation(),
				"operation", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(typeEClass, this.getType(),
						"addSuperType", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, this.getType(), "type", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(typeEClass, theDatatypesPackage.getBoolean(),
						"removeProperty", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, this.getProperty(),
				"property", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(typeEClass, theDatatypesPackage.getBoolean(),
						"removeOperation", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, this.getOperation(),
				"operation", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		addEOperation(typeEClass, this.getProperty(),
				"getIDProperties", 0, -1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		initEClass(
				primitiveTypeEClass,
				PrimitiveType.class,
				"PrimitiveType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(
				getPrimitiveType_Kind(),
				this.getPrimitiveTypeKind(),
				"kind", "Unknown", 1, 1, PrimitiveType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

		initEClass(enumerationEClass, Enumeration.class,
				"Enumeration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getEnumeration_OwnedLiteral(),
				this.getEnumerationLiteral(),
				this.getEnumerationLiteral_Enumeration(),
				"ownedLiteral", null, 0, -1, Enumeration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(enumerationEClass, this.getEnumeration(),
						"addLiteral", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, this.getEnumerationLiteral(),
				"literal", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(enumerationEClass, this.getEnumerationLiteral(),
						"lookupLiteral", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, theDatatypesPackage.getString(),
				"name", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		initEClass(
				enumerationLiteralEClass,
				EnumerationLiteral.class,
				"EnumerationLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getEnumerationLiteral_Enumeration(),
				this.getEnumeration(),
				this.getEnumeration_OwnedLiteral(),
				"enumeration", null, 0, 1, EnumerationLiteral.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(propertyEClass, Property.class,
				"Property", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getProperty_OwningType(),
				this.getType(),
				this.getType_OwnedProperty(),
				"owningType", null, 0, 1, Property.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(propertyEClass, theDatatypesPackage.getBoolean(),
						"cmpSlots", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, this.getProperty(), "p", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		addEOperation(propertyEClass, ecorePackage.getEBoolean(),
				"isIdentifier", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		initEClass(operationEClass, Operation.class,
				"Operation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getOperation_OwningType(),
				this.getType(),
				this.getType_OwnedOperation(),
				"owningType", null, 0, 1, Operation.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getOperation_OwnedParameter(),
				this.getParameter(),
				this.getParameter_Operation(),
				"ownedParameter", null, 0, -1, Operation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getOperation_InputParameter(),
				this.getParameter(),
				null,
				"inputParameter", null, 0, -1, Operation.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getOperation_OutputParameter(),
				this.getParameter(),
				null,
				"outputParameter", null, 0, -1, Operation.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getOperation_ReturnParameter(),
				this.getParameter(),
				null,
				"returnParameter", null, 0, 1, Operation.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getOperation_SignatureParameter(),
				this.getParameter(),
				null,
				"signatureParameter", null, 0, -1, Operation.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(operationEClass, theDatatypesPackage.getBoolean(),
						"hasMatchingSignature", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		g1 = createEGenericType(theDatatypesPackage.getSequence());
		g2 = createEGenericType(this.getType());
		g1.getETypeArguments().add(g2);
		addEParameter(op, g1, "paramTypes", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(operationEClass, this.getOperation(),
						"addParameter", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, this.getParameter(), "param", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		initEClass(parameterEClass, Parameter.class,
				"Parameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(
				getParameter_Kind(),
				this.getParameterDirectionKind(),
				"kind", "in", 0, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$
		initEReference(
				getParameter_Operation(),
				this.getOperation(),
				this.getOperation_OwnedParameter(),
				"operation", null, 0, 1, Parameter.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		addEOperation(parameterEClass, this.getProperty(),
				"asProperty", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		initEClass(genericTypeEClass, GenericType.class,
				"GenericType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		op =
				addEOperation(genericTypeEClass, theDatatypesPackage.getBoolean(),
						"bindGenericType", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		g1 = createEGenericType(theDatatypesPackage.getSequence());
		g2 = createEGenericType(this.getTypeParameter());
		g1.getETypeArguments().add(g2);
		addEParameter(op, g1, "parameters", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		g1 = createEGenericType(theDatatypesPackage.getSequence());
		g2 = createEGenericType();
		g1.getETypeArguments().add(g2);
		g3 = createEGenericType(this.getType());
		g2.setEUpperBound(g3);
		addEParameter(op, g1, "types", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, this.getTypedElement(),
				"typedElement", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(genericTypeEClass, theDatatypesPackage.getBoolean(),
						"bindGenericSuperType", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		g1 = createEGenericType(theDatatypesPackage.getSequence());
		g2 = createEGenericType(this.getTypeParameter());
		g1.getETypeArguments().add(g2);
		addEParameter(op, g1, "parameters", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		g1 = createEGenericType(theDatatypesPackage.getSequence());
		g2 = createEGenericType();
		g1.getETypeArguments().add(g2);
		g3 = createEGenericType(this.getType());
		g2.setEUpperBound(g3);
		addEParameter(op, g1, "types", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, this.getType(), "subType", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(genericTypeEClass, theDatatypesPackage.getBoolean(),
						"isConformant", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, this.getType(), "type", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		initEClass(
				parameterGenericTypeEClass,
				ParameterGenericType.class,
				"ParameterGenericType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getParameterGenericType_TypeParameter(),
				this.getTypeParameter(),
				null,
				"typeParameter", null, 0, 1, ParameterGenericType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(
				complexGenericTypeEClass,
				ComplexGenericType.class,
				"ComplexGenericType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getComplexGenericType_UnboundType(),
				this.getType(),
				null,
				"unboundType", null, 0, 1, ComplexGenericType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getComplexGenericType_TypeArgument(),
				this.getTypeArgument(),
				this.getTypeArgument_OwningGenericType(),
				"typeArgument", null, 0, -1, ComplexGenericType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(complexGenericTypeEClass, this.getComplexGenericType(),
						"addTypeArgument", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, this.getTypeArgument(),
				"typeArgument", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		initEClass(
				typeParameterEClass,
				TypeParameter.class,
				"TypeParameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getTypeParameter_GenericElement(),
				this.getGenericElement(),
				this.getGenericElement_OwnedTypeParameter(),
				"genericElement", null, 0, 1, TypeParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(
				typeArgumentEClass,
				TypeArgument.class,
				"TypeArgument", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getTypeArgument_OwningGenericType(),
				this.getComplexGenericType(),
				this.getComplexGenericType_TypeArgument(),
				"owningGenericType", null, 0, 1, TypeArgument.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(constraintEClass, Constraint.class,
				"Constraint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(
				getConstraint_Kind(),
				this.getConstraintKind(),
				"kind", null, 1, 1, Constraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getConstraint_Namespace(),
				this.getNamespace(),
				this.getNamespace_OwnedRule(),
				"namespace", null, 0, 1, Constraint.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getConstraint_Specification(),
				this.getExpression(),
				this.getExpression_Constraint(),
				"specification", null, 1, 1, Constraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getConstraint_ConstrainedElement(),
				this.getConstrainableElement(),
				null,
				"constrainedElement", null, 0, -1, Constraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getConstraint_DefinedFeature(),
				this.getFeature(),
				this.getFeature_Semantics(),
				"definedFeature", null, 0, 1, Constraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(constraintEClass, this.getConstraint(),
						"addConstrainedElement", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, this.getConstrainableElement(),
				"constrainedElement", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		initEClass(expressionEClass, Expression.class,
				"Expression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(
				getExpression_Body(),
				theDatatypesPackage.getString(),
				"body", null, 0, 1, Expression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(
				getExpression_Language(),
				theDatatypesPackage.getString(),
				"language", null, 0, 1, Expression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getExpression_Constraint(),
				this.getConstraint(),
				this.getConstraint_Specification(),
				"constraint", null, 0, 1, Expression.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(
				associationPropertyEClass,
				AssociationProperty.class,
				"AssociationProperty", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getAssociationProperty_InverseAssociationProperties(),
				this.getAssociationProperty(),
				null,
				"inverseAssociationProperties", null, 0, -1, AssociationProperty.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(associationPropertyEClass, null,
						"addAssociation", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, this.getAssociationProperty(),
				"bProperty", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(associationPropertyEClass, this.getAssociationProperty(),
						"getAssociation", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, theDatatypesPackage.getString(),
				"propName", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(associationPropertyEClass, null,
						"removeAssociation", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, this.getAssociationProperty(),
				"bProperty", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(associationPropertyEClass,
						theDatatypesPackage.getBoolean(),
						"isInverseAssociation", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, this.getAssociationProperty(),
				"bProperty", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		op =
				addEOperation(associationPropertyEClass, null,
						"addAssociations", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, this.getAssociationProperty(),
				"bProperty", 0, -1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		addEOperation(associationPropertyEClass, theDatatypesPackage.getBoolean(),
				"isNavigable", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		// Initialize enums and add enum literals
		initEEnum(primitiveTypeKindEEnum, PrimitiveTypeKind.class,
				"PrimitiveTypeKind"); //$NON-NLS-1$
		addEEnumLiteral(primitiveTypeKindEEnum, PrimitiveTypeKind.UNKNOWN);
		addEEnumLiteral(primitiveTypeKindEEnum, PrimitiveTypeKind.INTEGER);
		addEEnumLiteral(primitiveTypeKindEEnum, PrimitiveTypeKind.REAL);
		addEEnumLiteral(primitiveTypeKindEEnum, PrimitiveTypeKind.BOOLEAN);
		addEEnumLiteral(primitiveTypeKindEEnum, PrimitiveTypeKind.STRING);
		addEEnumLiteral(primitiveTypeKindEEnum, PrimitiveTypeKind.VOID);

		initEEnum(parameterDirectionKindEEnum, ParameterDirectionKind.class,
				"ParameterDirectionKind"); //$NON-NLS-1$
		addEEnumLiteral(parameterDirectionKindEEnum, ParameterDirectionKind.IN);
		addEEnumLiteral(parameterDirectionKindEEnum, ParameterDirectionKind.OUT);
		addEEnumLiteral(parameterDirectionKindEEnum, ParameterDirectionKind.INOUT);
		addEEnumLiteral(parameterDirectionKindEEnum, ParameterDirectionKind.RETURN);

		initEEnum(constraintKindEEnum, ConstraintKind.class, "ConstraintKind"); //$NON-NLS-1$
		addEEnumLiteral(constraintKindEEnum, ConstraintKind.INVARIANT);
		addEEnumLiteral(constraintKindEEnum, ConstraintKind.DEFINITION);
		addEEnumLiteral(constraintKindEEnum, ConstraintKind.PRECONDITION);
		addEEnumLiteral(constraintKindEEnum, ConstraintKind.POSTCONDITION);
		addEEnumLiteral(constraintKindEEnum, ConstraintKind.INITIAL);
		addEEnumLiteral(constraintKindEEnum, ConstraintKind.DERIVED);
		addEEnumLiteral(constraintKindEEnum, ConstraintKind.BODY);

		// Initialize data types
		initEDataType(
				cloneNotSupportedExceptionEDataType,
				CloneNotSupportedException.class,
				"CloneNotSupportedException", !IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		// Create resource
		createResource(eNS_URI);
	}

} // PivotModelPackageImpl
