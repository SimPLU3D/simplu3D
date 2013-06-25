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
package tudresden.ocl20.pivot.pivotmodel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

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
 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelFactory
 * @model kind="package"
 * @generated
 */
public interface PivotModelPackage extends EPackage {

	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "pivotmodel"; //$NON-NLS-1$

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://www.tu-dresden.de/ocl20/pivot/2007/pivotmodel"; //$NON-NLS-1$

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "pivot"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	PivotModelPackage eINSTANCE =
			tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl.init();

	/**
	 * The meta object id for the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl
	 * <em>Named Element</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getNamedElement()
	 * @generated
	 */
	int NAMED_ELEMENT = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT__NAME = 0;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT__QUALIFIED_NAME = 1;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT__OWNER = 2;

	/**
	 * The number of structural features of the '<em>Named Element</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.impl.TypedElementImpl
	 * <em>Typed Element</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.TypedElementImpl
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getTypedElement()
	 * @generated
	 */
	int TYPED_ELEMENT = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPED_ELEMENT__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPED_ELEMENT__QUALIFIED_NAME = NAMED_ELEMENT__QUALIFIED_NAME;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPED_ELEMENT__OWNER = NAMED_ELEMENT__OWNER;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPED_ELEMENT__TYPE = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Generic Type</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPED_ELEMENT__GENERIC_TYPE = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Typed Element</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPED_ELEMENT_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.impl.FeatureImpl <em>Feature</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.FeatureImpl
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getFeature()
	 * @generated
	 */
	int FEATURE = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE__NAME = TYPED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE__QUALIFIED_NAME = TYPED_ELEMENT__QUALIFIED_NAME;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE__OWNER = TYPED_ELEMENT__OWNER;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE__TYPE = TYPED_ELEMENT__TYPE;

	/**
	 * The feature id for the '<em><b>Generic Type</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE__GENERIC_TYPE = TYPED_ELEMENT__GENERIC_TYPE;

	/**
	 * The feature id for the '<em><b>Static</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE__STATIC = TYPED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Semantics</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE__SEMANTICS = TYPED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Feature</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE_FEATURE_COUNT = TYPED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.GenericElement
	 * <em>Generic Element</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.GenericElement
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getGenericElement()
	 * @generated
	 */
	int GENERIC_ELEMENT = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GENERIC_ELEMENT__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GENERIC_ELEMENT__QUALIFIED_NAME = NAMED_ELEMENT__QUALIFIED_NAME;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GENERIC_ELEMENT__OWNER = NAMED_ELEMENT__OWNER;

	/**
	 * The feature id for the '<em><b>Owned Type Parameter</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GENERIC_ELEMENT__OWNED_TYPE_PARAMETER = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Generic Element</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GENERIC_ELEMENT_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.ConstrainableElement
	 * <em>Constrainable Element</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.ConstrainableElement
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getConstrainableElement()
	 * @generated
	 */
	int CONSTRAINABLE_ELEMENT = 4;

	/**
	 * The number of structural features of the '<em>Constrainable Element</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONSTRAINABLE_ELEMENT_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.impl.NamespaceImpl
	 * <em>Namespace</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamespaceImpl
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getNamespace()
	 * @generated
	 */
	int NAMESPACE = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMESPACE__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMESPACE__QUALIFIED_NAME = NAMED_ELEMENT__QUALIFIED_NAME;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMESPACE__OWNER = NAMED_ELEMENT__OWNER;

	/**
	 * The feature id for the '<em><b>Owned Type Parameter</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMESPACE__OWNED_TYPE_PARAMETER = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Owned Type</b></em>' containment reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMESPACE__OWNED_TYPE = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Owned Rule</b></em>' containment reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMESPACE__OWNED_RULE = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Nested Namespace</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMESPACE__NESTED_NAMESPACE = NAMED_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Nesting Namespace</b></em>' container
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMESPACE__NESTING_NAMESPACE = NAMED_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Namespace</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMESPACE_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.impl.TypeImpl <em>Type</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.TypeImpl
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getType()
	 * @generated
	 */
	int TYPE = 6;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPE__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPE__QUALIFIED_NAME = NAMED_ELEMENT__QUALIFIED_NAME;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPE__OWNER = NAMED_ELEMENT__OWNER;

	/**
	 * The feature id for the '<em><b>Owned Type Parameter</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPE__OWNED_TYPE_PARAMETER = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Namespace</b></em>' container reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPE__NAMESPACE = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Owned Operation</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPE__OWNED_OPERATION = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Owned Property</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPE__OWNED_PROPERTY = NAMED_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Super Type</b></em>' reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPE__SUPER_TYPE = NAMED_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Generic Super Type</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPE__GENERIC_SUPER_TYPE = NAMED_ELEMENT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Type</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPE_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.impl.PrimitiveTypeImpl
	 * <em>Primitive Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PrimitiveTypeImpl
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getPrimitiveType()
	 * @generated
	 */
	int PRIMITIVE_TYPE = 7;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_TYPE__NAME = TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_TYPE__QUALIFIED_NAME = TYPE__QUALIFIED_NAME;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_TYPE__OWNER = TYPE__OWNER;

	/**
	 * The feature id for the '<em><b>Owned Type Parameter</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_TYPE__OWNED_TYPE_PARAMETER = TYPE__OWNED_TYPE_PARAMETER;

	/**
	 * The feature id for the '<em><b>Namespace</b></em>' container reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_TYPE__NAMESPACE = TYPE__NAMESPACE;

	/**
	 * The feature id for the '<em><b>Owned Operation</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_TYPE__OWNED_OPERATION = TYPE__OWNED_OPERATION;

	/**
	 * The feature id for the '<em><b>Owned Property</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_TYPE__OWNED_PROPERTY = TYPE__OWNED_PROPERTY;

	/**
	 * The feature id for the '<em><b>Super Type</b></em>' reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_TYPE__SUPER_TYPE = TYPE__SUPER_TYPE;

	/**
	 * The feature id for the '<em><b>Generic Super Type</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_TYPE__GENERIC_SUPER_TYPE = TYPE__GENERIC_SUPER_TYPE;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_TYPE__KIND = TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Primitive Type</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_TYPE_FEATURE_COUNT = TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.impl.EnumerationImpl
	 * <em>Enumeration</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.EnumerationImpl
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getEnumeration()
	 * @generated
	 */
	int ENUMERATION = 8;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION__NAME = TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION__QUALIFIED_NAME = TYPE__QUALIFIED_NAME;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION__OWNER = TYPE__OWNER;

	/**
	 * The feature id for the '<em><b>Owned Type Parameter</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION__OWNED_TYPE_PARAMETER = TYPE__OWNED_TYPE_PARAMETER;

	/**
	 * The feature id for the '<em><b>Namespace</b></em>' container reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION__NAMESPACE = TYPE__NAMESPACE;

	/**
	 * The feature id for the '<em><b>Owned Operation</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION__OWNED_OPERATION = TYPE__OWNED_OPERATION;

	/**
	 * The feature id for the '<em><b>Owned Property</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION__OWNED_PROPERTY = TYPE__OWNED_PROPERTY;

	/**
	 * The feature id for the '<em><b>Super Type</b></em>' reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION__SUPER_TYPE = TYPE__SUPER_TYPE;

	/**
	 * The feature id for the '<em><b>Generic Super Type</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION__GENERIC_SUPER_TYPE = TYPE__GENERIC_SUPER_TYPE;

	/**
	 * The feature id for the '<em><b>Owned Literal</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION__OWNED_LITERAL = TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Enumeration</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION_FEATURE_COUNT = TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.impl.EnumerationLiteralImpl
	 * <em>Enumeration Literal</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.EnumerationLiteralImpl
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getEnumerationLiteral()
	 * @generated
	 */
	int ENUMERATION_LITERAL = 9;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION_LITERAL__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION_LITERAL__QUALIFIED_NAME = NAMED_ELEMENT__QUALIFIED_NAME;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION_LITERAL__OWNER = NAMED_ELEMENT__OWNER;

	/**
	 * The feature id for the '<em><b>Enumeration</b></em>' container reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION_LITERAL__ENUMERATION = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Enumeration Literal</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION_LITERAL_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.impl.PropertyImpl
	 * <em>Property</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PropertyImpl
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getProperty()
	 * @generated
	 */
	int PROPERTY = 10;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PROPERTY__NAME = FEATURE__NAME;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PROPERTY__QUALIFIED_NAME = FEATURE__QUALIFIED_NAME;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PROPERTY__OWNER = FEATURE__OWNER;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PROPERTY__TYPE = FEATURE__TYPE;

	/**
	 * The feature id for the '<em><b>Generic Type</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PROPERTY__GENERIC_TYPE = FEATURE__GENERIC_TYPE;

	/**
	 * The feature id for the '<em><b>Static</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PROPERTY__STATIC = FEATURE__STATIC;

	/**
	 * The feature id for the '<em><b>Semantics</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PROPERTY__SEMANTICS = FEATURE__SEMANTICS;

	/**
	 * The feature id for the '<em><b>Owning Type</b></em>' container reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PROPERTY__OWNING_TYPE = FEATURE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Property</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PROPERTY_FEATURE_COUNT = FEATURE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.impl.OperationImpl
	 * <em>Operation</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.OperationImpl
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getOperation()
	 * @generated
	 */
	int OPERATION = 11;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int OPERATION__NAME = FEATURE__NAME;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int OPERATION__QUALIFIED_NAME = FEATURE__QUALIFIED_NAME;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int OPERATION__OWNER = FEATURE__OWNER;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int OPERATION__TYPE = FEATURE__TYPE;

	/**
	 * The feature id for the '<em><b>Generic Type</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int OPERATION__GENERIC_TYPE = FEATURE__GENERIC_TYPE;

	/**
	 * The feature id for the '<em><b>Static</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int OPERATION__STATIC = FEATURE__STATIC;

	/**
	 * The feature id for the '<em><b>Semantics</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int OPERATION__SEMANTICS = FEATURE__SEMANTICS;

	/**
	 * The feature id for the '<em><b>Owned Type Parameter</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int OPERATION__OWNED_TYPE_PARAMETER = FEATURE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Owning Type</b></em>' container reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int OPERATION__OWNING_TYPE = FEATURE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Owned Parameter</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int OPERATION__OWNED_PARAMETER = FEATURE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Input Parameter</b></em>' reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int OPERATION__INPUT_PARAMETER = FEATURE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Output Parameter</b></em>' reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int OPERATION__OUTPUT_PARAMETER = FEATURE_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Return Parameter</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int OPERATION__RETURN_PARAMETER = FEATURE_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Signature Parameter</b></em>' reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int OPERATION__SIGNATURE_PARAMETER = FEATURE_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Operation</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int OPERATION_FEATURE_COUNT = FEATURE_FEATURE_COUNT + 7;

	/**
	 * The meta object id for the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.impl.ParameterImpl
	 * <em>Parameter</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.ParameterImpl
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getParameter()
	 * @generated
	 */
	int PARAMETER = 12;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PARAMETER__NAME = TYPED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PARAMETER__QUALIFIED_NAME = TYPED_ELEMENT__QUALIFIED_NAME;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PARAMETER__OWNER = TYPED_ELEMENT__OWNER;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PARAMETER__TYPE = TYPED_ELEMENT__TYPE;

	/**
	 * The feature id for the '<em><b>Generic Type</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PARAMETER__GENERIC_TYPE = TYPED_ELEMENT__GENERIC_TYPE;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PARAMETER__KIND = TYPED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Operation</b></em>' container reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PARAMETER__OPERATION = TYPED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Parameter</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PARAMETER_FEATURE_COUNT = TYPED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.impl.GenericTypeImpl
	 * <em>Generic Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.GenericTypeImpl
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getGenericType()
	 * @generated
	 */
	int GENERIC_TYPE = 13;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GENERIC_TYPE__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GENERIC_TYPE__QUALIFIED_NAME = NAMED_ELEMENT__QUALIFIED_NAME;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GENERIC_TYPE__OWNER = NAMED_ELEMENT__OWNER;

	/**
	 * The number of structural features of the '<em>Generic Type</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GENERIC_TYPE_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.impl.ParameterGenericTypeImpl
	 * <em>Parameter Generic Type</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.ParameterGenericTypeImpl
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getParameterGenericType()
	 * @generated
	 */
	int PARAMETER_GENERIC_TYPE = 14;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PARAMETER_GENERIC_TYPE__NAME = GENERIC_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PARAMETER_GENERIC_TYPE__QUALIFIED_NAME = GENERIC_TYPE__QUALIFIED_NAME;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PARAMETER_GENERIC_TYPE__OWNER = GENERIC_TYPE__OWNER;

	/**
	 * The feature id for the '<em><b>Type Parameter</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PARAMETER_GENERIC_TYPE__TYPE_PARAMETER = GENERIC_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Parameter Generic Type</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PARAMETER_GENERIC_TYPE_FEATURE_COUNT = GENERIC_TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.impl.ComplexGenericTypeImpl
	 * <em>Complex Generic Type</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.ComplexGenericTypeImpl
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getComplexGenericType()
	 * @generated
	 */
	int COMPLEX_GENERIC_TYPE = 15;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPLEX_GENERIC_TYPE__NAME = GENERIC_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPLEX_GENERIC_TYPE__QUALIFIED_NAME = GENERIC_TYPE__QUALIFIED_NAME;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPLEX_GENERIC_TYPE__OWNER = GENERIC_TYPE__OWNER;

	/**
	 * The feature id for the '<em><b>Unbound Type</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPLEX_GENERIC_TYPE__UNBOUND_TYPE = GENERIC_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Type Argument</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPLEX_GENERIC_TYPE__TYPE_ARGUMENT = GENERIC_TYPE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Complex Generic Type</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPLEX_GENERIC_TYPE_FEATURE_COUNT = GENERIC_TYPE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.impl.TypeParameterImpl
	 * <em>Type Parameter</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.TypeParameterImpl
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getTypeParameter()
	 * @generated
	 */
	int TYPE_PARAMETER = 16;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPE_PARAMETER__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPE_PARAMETER__QUALIFIED_NAME = NAMED_ELEMENT__QUALIFIED_NAME;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPE_PARAMETER__OWNER = NAMED_ELEMENT__OWNER;

	/**
	 * The feature id for the '<em><b>Generic Element</b></em>' container
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPE_PARAMETER__GENERIC_ELEMENT = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Type Parameter</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPE_PARAMETER_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.impl.TypeArgumentImpl
	 * <em>Type Argument</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.TypeArgumentImpl
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getTypeArgument()
	 * @generated
	 */
	int TYPE_ARGUMENT = 17;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPE_ARGUMENT__NAME = TYPED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPE_ARGUMENT__QUALIFIED_NAME = TYPED_ELEMENT__QUALIFIED_NAME;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPE_ARGUMENT__OWNER = TYPED_ELEMENT__OWNER;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPE_ARGUMENT__TYPE = TYPED_ELEMENT__TYPE;

	/**
	 * The feature id for the '<em><b>Generic Type</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPE_ARGUMENT__GENERIC_TYPE = TYPED_ELEMENT__GENERIC_TYPE;

	/**
	 * The feature id for the '<em><b>Owning Generic Type</b></em>' container
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPE_ARGUMENT__OWNING_GENERIC_TYPE = TYPED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Type Argument</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPE_ARGUMENT_FEATURE_COUNT = TYPED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.impl.ConstraintImpl
	 * <em>Constraint</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.ConstraintImpl
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getConstraint()
	 * @generated
	 */
	int CONSTRAINT = 18;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__QUALIFIED_NAME = NAMED_ELEMENT__QUALIFIED_NAME;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__OWNER = NAMED_ELEMENT__OWNER;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__KIND = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Namespace</b></em>' container reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__NAMESPACE = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Specification</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__SPECIFICATION = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Constrained Element</b></em>' reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__CONSTRAINED_ELEMENT = NAMED_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Defined Feature</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__DEFINED_FEATURE = NAMED_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Constraint</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.impl.ExpressionImpl
	 * <em>Expression</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.ExpressionImpl
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getExpression()
	 * @generated
	 */
	int EXPRESSION = 19;

	/**
	 * The feature id for the '<em><b>Body</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EXPRESSION__BODY = 0;

	/**
	 * The feature id for the '<em><b>Language</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EXPRESSION__LANGUAGE = 1;

	/**
	 * The feature id for the '<em><b>Constraint</b></em>' container reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EXPRESSION__CONSTRAINT = 2;

	/**
	 * The number of structural features of the '<em>Expression</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EXPRESSION_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.impl.AssociationPropertyImpl
	 * <em>Association Property</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.AssociationPropertyImpl
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getAssociationProperty()
	 * @generated
	 */
	int ASSOCIATION_PROPERTY = 20;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_PROPERTY__NAME = PROPERTY__NAME;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_PROPERTY__QUALIFIED_NAME = PROPERTY__QUALIFIED_NAME;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_PROPERTY__OWNER = PROPERTY__OWNER;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_PROPERTY__TYPE = PROPERTY__TYPE;

	/**
	 * The feature id for the '<em><b>Generic Type</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_PROPERTY__GENERIC_TYPE = PROPERTY__GENERIC_TYPE;

	/**
	 * The feature id for the '<em><b>Static</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_PROPERTY__STATIC = PROPERTY__STATIC;

	/**
	 * The feature id for the '<em><b>Semantics</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_PROPERTY__SEMANTICS = PROPERTY__SEMANTICS;

	/**
	 * The feature id for the '<em><b>Owning Type</b></em>' container reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_PROPERTY__OWNING_TYPE = PROPERTY__OWNING_TYPE;

	/**
	 * The feature id for the '<em><b>Inverse Association Properties</b></em>'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_PROPERTY__INVERSE_ASSOCIATION_PROPERTIES =
			PROPERTY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Association Property</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_PROPERTY_FEATURE_COUNT = PROPERTY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.PrimitiveTypeKind
	 * <em>Primitive Type Kind</em>}' enum. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.PrimitiveTypeKind
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getPrimitiveTypeKind()
	 * @generated
	 */
	int PRIMITIVE_TYPE_KIND = 21;

	/**
	 * The meta object id for the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.ParameterDirectionKind
	 * <em>Parameter Direction Kind</em>}' enum. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.ParameterDirectionKind
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getParameterDirectionKind()
	 * @generated
	 */
	int PARAMETER_DIRECTION_KIND = 22;

	/**
	 * The meta object id for the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.ConstraintKind
	 * <em>Constraint Kind</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.ConstraintKind
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getConstraintKind()
	 * @generated
	 */
	int CONSTRAINT_KIND = 23;

	/**
	 * The meta object id for the '<em>Clone Not Supported Exception</em>' data
	 * type. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see java.lang.CloneNotSupportedException
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getCloneNotSupportedException()
	 * @generated
	 */
	int CLONE_NOT_SUPPORTED_EXCEPTION = 24;

	/**
	 * Returns the meta object for class '
	 * {@link tudresden.ocl20.pivot.pivotmodel.NamedElement
	 * <em>Named Element</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Named Element</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.NamedElement
	 * @generated
	 */
	EClass getNamedElement();

	/**
	 * Returns the meta object for the attribute '
	 * {@link tudresden.ocl20.pivot.pivotmodel.NamedElement#getName <em>Name</em>}
	 * '. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.NamedElement#getName()
	 * @see #getNamedElement()
	 * @generated
	 */
	EAttribute getNamedElement_Name();

	/**
	 * Returns the meta object for the attribute '
	 * {@link tudresden.ocl20.pivot.pivotmodel.NamedElement#getQualifiedName
	 * <em>Qualified Name</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Qualified Name</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.NamedElement#getQualifiedName()
	 * @see #getNamedElement()
	 * @generated
	 */
	EAttribute getNamedElement_QualifiedName();

	/**
	 * Returns the meta object for the reference '
	 * {@link tudresden.ocl20.pivot.pivotmodel.NamedElement#getOwner
	 * <em>Owner</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Owner</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.NamedElement#getOwner()
	 * @see #getNamedElement()
	 * @generated
	 */
	EReference getNamedElement_Owner();

	/**
	 * Returns the meta object for class '
	 * {@link tudresden.ocl20.pivot.pivotmodel.TypedElement
	 * <em>Typed Element</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Typed Element</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.TypedElement
	 * @generated
	 */
	EClass getTypedElement();

	/**
	 * Returns the meta object for the reference '
	 * {@link tudresden.ocl20.pivot.pivotmodel.TypedElement#getType <em>Type</em>}
	 * '. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Type</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.TypedElement#getType()
	 * @see #getTypedElement()
	 * @generated
	 */
	EReference getTypedElement_Type();

	/**
	 * Returns the meta object for the containment reference '
	 * {@link tudresden.ocl20.pivot.pivotmodel.TypedElement#getGenericType
	 * <em>Generic Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '
	 *         <em>Generic Type</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.TypedElement#getGenericType()
	 * @see #getTypedElement()
	 * @generated
	 */
	EReference getTypedElement_GenericType();

	/**
	 * Returns the meta object for class '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Feature <em>Feature</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Feature</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Feature
	 * @generated
	 */
	EClass getFeature();

	/**
	 * Returns the meta object for the attribute '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Feature#isStatic <em>Static</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Static</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Feature#isStatic()
	 * @see #getFeature()
	 * @generated
	 */
	EAttribute getFeature_Static();

	/**
	 * Returns the meta object for the reference '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Feature#getSemantics
	 * <em>Semantics</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Semantics</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Feature#getSemantics()
	 * @see #getFeature()
	 * @generated
	 */
	EReference getFeature_Semantics();

	/**
	 * Returns the meta object for class '
	 * {@link tudresden.ocl20.pivot.pivotmodel.GenericElement
	 * <em>Generic Element</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Generic Element</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.GenericElement
	 * @generated
	 */
	EClass getGenericElement();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link tudresden.ocl20.pivot.pivotmodel.GenericElement#getOwnedTypeParameter
	 * <em>Owned Type Parameter</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the containment reference list '
	 *         <em>Owned Type Parameter</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.GenericElement#getOwnedTypeParameter()
	 * @see #getGenericElement()
	 * @generated
	 */
	EReference getGenericElement_OwnedTypeParameter();

	/**
	 * Returns the meta object for class '
	 * {@link tudresden.ocl20.pivot.pivotmodel.ConstrainableElement
	 * <em>Constrainable Element</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for class '<em>Constrainable Element</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.ConstrainableElement
	 * @generated
	 */
	EClass getConstrainableElement();

	/**
	 * Returns the meta object for class '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Namespace <em>Namespace</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Namespace</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Namespace
	 * @generated
	 */
	EClass getNamespace();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Namespace#getOwnedType
	 * <em>Owned Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '
	 *         <em>Owned Type</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Namespace#getOwnedType()
	 * @see #getNamespace()
	 * @generated
	 */
	EReference getNamespace_OwnedType();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Namespace#getOwnedRule
	 * <em>Owned Rule</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '
	 *         <em>Owned Rule</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Namespace#getOwnedRule()
	 * @see #getNamespace()
	 * @generated
	 */
	EReference getNamespace_OwnedRule();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Namespace#getNestedNamespace
	 * <em>Nested Namespace</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '
	 *         <em>Nested Namespace</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Namespace#getNestedNamespace()
	 * @see #getNamespace()
	 * @generated
	 */
	EReference getNamespace_NestedNamespace();

	/**
	 * Returns the meta object for the container reference '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Namespace#getNestingNamespace
	 * <em>Nesting Namespace</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the container reference '
	 *         <em>Nesting Namespace</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Namespace#getNestingNamespace()
	 * @see #getNamespace()
	 * @generated
	 */
	EReference getNamespace_NestingNamespace();

	/**
	 * Returns the meta object for class '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Type <em>Type</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Type</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Type
	 * @generated
	 */
	EClass getType();

	/**
	 * Returns the meta object for the container reference '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Type#getNamespace
	 * <em>Namespace</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the container reference '<em>Namespace</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Type#getNamespace()
	 * @see #getType()
	 * @generated
	 */
	EReference getType_Namespace();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Type#getOwnedOperation
	 * <em>Owned Operation</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '
	 *         <em>Owned Operation</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Type#getOwnedOperation()
	 * @see #getType()
	 * @generated
	 */
	EReference getType_OwnedOperation();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Type#getOwnedProperty
	 * <em>Owned Property</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '
	 *         <em>Owned Property</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Type#getOwnedProperty()
	 * @see #getType()
	 * @generated
	 */
	EReference getType_OwnedProperty();

	/**
	 * Returns the meta object for the reference list '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Type#getSuperType
	 * <em>Super Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference list '<em>Super Type</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Type#getSuperType()
	 * @see #getType()
	 * @generated
	 */
	EReference getType_SuperType();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Type#getGenericSuperType
	 * <em>Generic Super Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the containment reference list '
	 *         <em>Generic Super Type</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Type#getGenericSuperType()
	 * @see #getType()
	 * @generated
	 */
	EReference getType_GenericSuperType();

	/**
	 * Returns the meta object for class '
	 * {@link tudresden.ocl20.pivot.pivotmodel.PrimitiveType
	 * <em>Primitive Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Primitive Type</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.PrimitiveType
	 * @generated
	 */
	EClass getPrimitiveType();

	/**
	 * Returns the meta object for the attribute '
	 * {@link tudresden.ocl20.pivot.pivotmodel.PrimitiveType#getKind
	 * <em>Kind</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Kind</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.PrimitiveType#getKind()
	 * @see #getPrimitiveType()
	 * @generated
	 */
	EAttribute getPrimitiveType_Kind();

	/**
	 * Returns the meta object for class '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Enumeration <em>Enumeration</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Enumeration</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Enumeration
	 * @generated
	 */
	EClass getEnumeration();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Enumeration#getOwnedLiteral
	 * <em>Owned Literal</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '
	 *         <em>Owned Literal</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Enumeration#getOwnedLiteral()
	 * @see #getEnumeration()
	 * @generated
	 */
	EReference getEnumeration_OwnedLiteral();

	/**
	 * Returns the meta object for class '
	 * {@link tudresden.ocl20.pivot.pivotmodel.EnumerationLiteral
	 * <em>Enumeration Literal</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for class '<em>Enumeration Literal</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.EnumerationLiteral
	 * @generated
	 */
	EClass getEnumerationLiteral();

	/**
	 * Returns the meta object for the container reference '
	 * {@link tudresden.ocl20.pivot.pivotmodel.EnumerationLiteral#getEnumeration
	 * <em>Enumeration</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the container reference '<em>Enumeration</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.EnumerationLiteral#getEnumeration()
	 * @see #getEnumerationLiteral()
	 * @generated
	 */
	EReference getEnumerationLiteral_Enumeration();

	/**
	 * Returns the meta object for class '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Property <em>Property</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Property</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Property
	 * @generated
	 */
	EClass getProperty();

	/**
	 * Returns the meta object for the container reference '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Property#getOwningType
	 * <em>Owning Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the container reference '<em>Owning Type</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Property#getOwningType()
	 * @see #getProperty()
	 * @generated
	 */
	EReference getProperty_OwningType();

	/**
	 * Returns the meta object for class '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Operation <em>Operation</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Operation</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Operation
	 * @generated
	 */
	EClass getOperation();

	/**
	 * Returns the meta object for the container reference '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Operation#getOwningType
	 * <em>Owning Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the container reference '<em>Owning Type</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Operation#getOwningType()
	 * @see #getOperation()
	 * @generated
	 */
	EReference getOperation_OwningType();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Operation#getOwnedParameter
	 * <em>Owned Parameter</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '
	 *         <em>Owned Parameter</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Operation#getOwnedParameter()
	 * @see #getOperation()
	 * @generated
	 */
	EReference getOperation_OwnedParameter();

	/**
	 * Returns the meta object for the reference list '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Operation#getInputParameter
	 * <em>Input Parameter</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference list '<em>Input Parameter</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Operation#getInputParameter()
	 * @see #getOperation()
	 * @generated
	 */
	EReference getOperation_InputParameter();

	/**
	 * Returns the meta object for the reference list '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Operation#getOutputParameter
	 * <em>Output Parameter</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference list '<em>Output Parameter</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Operation#getOutputParameter()
	 * @see #getOperation()
	 * @generated
	 */
	EReference getOperation_OutputParameter();

	/**
	 * Returns the meta object for the reference '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Operation#getReturnParameter
	 * <em>Return Parameter</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Return Parameter</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Operation#getReturnParameter()
	 * @see #getOperation()
	 * @generated
	 */
	EReference getOperation_ReturnParameter();

	/**
	 * Returns the meta object for the reference list '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Operation#getSignatureParameter
	 * <em>Signature Parameter</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the reference list '
	 *         <em>Signature Parameter</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Operation#getSignatureParameter()
	 * @see #getOperation()
	 * @generated
	 */
	EReference getOperation_SignatureParameter();

	/**
	 * Returns the meta object for class '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Parameter <em>Parameter</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Parameter</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Parameter
	 * @generated
	 */
	EClass getParameter();

	/**
	 * Returns the meta object for the attribute '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Parameter#getKind <em>Kind</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Kind</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Parameter#getKind()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_Kind();

	/**
	 * Returns the meta object for the container reference '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Parameter#getOperation
	 * <em>Operation</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the container reference '<em>Operation</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Parameter#getOperation()
	 * @see #getParameter()
	 * @generated
	 */
	EReference getParameter_Operation();

	/**
	 * Returns the meta object for class '
	 * {@link tudresden.ocl20.pivot.pivotmodel.GenericType <em>Generic Type</em>}
	 * '. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Generic Type</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.GenericType
	 * @generated
	 */
	EClass getGenericType();

	/**
	 * Returns the meta object for class '
	 * {@link tudresden.ocl20.pivot.pivotmodel.ParameterGenericType
	 * <em>Parameter Generic Type</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Parameter Generic Type</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.ParameterGenericType
	 * @generated
	 */
	EClass getParameterGenericType();

	/**
	 * Returns the meta object for the reference '
	 * {@link tudresden.ocl20.pivot.pivotmodel.ParameterGenericType#getTypeParameter
	 * <em>Type Parameter</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Type Parameter</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.ParameterGenericType#getTypeParameter()
	 * @see #getParameterGenericType()
	 * @generated
	 */
	EReference getParameterGenericType_TypeParameter();

	/**
	 * Returns the meta object for class '
	 * {@link tudresden.ocl20.pivot.pivotmodel.ComplexGenericType
	 * <em>Complex Generic Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for class '<em>Complex Generic Type</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.ComplexGenericType
	 * @generated
	 */
	EClass getComplexGenericType();

	/**
	 * Returns the meta object for the reference '
	 * {@link tudresden.ocl20.pivot.pivotmodel.ComplexGenericType#getUnboundType
	 * <em>Unbound Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Unbound Type</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.ComplexGenericType#getUnboundType()
	 * @see #getComplexGenericType()
	 * @generated
	 */
	EReference getComplexGenericType_UnboundType();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link tudresden.ocl20.pivot.pivotmodel.ComplexGenericType#getTypeArgument
	 * <em>Type Argument</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '
	 *         <em>Type Argument</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.ComplexGenericType#getTypeArgument()
	 * @see #getComplexGenericType()
	 * @generated
	 */
	EReference getComplexGenericType_TypeArgument();

	/**
	 * Returns the meta object for class '
	 * {@link tudresden.ocl20.pivot.pivotmodel.TypeParameter
	 * <em>Type Parameter</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Type Parameter</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.TypeParameter
	 * @generated
	 */
	EClass getTypeParameter();

	/**
	 * Returns the meta object for the container reference '
	 * {@link tudresden.ocl20.pivot.pivotmodel.TypeParameter#getGenericElement
	 * <em>Generic Element</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the container reference '
	 *         <em>Generic Element</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.TypeParameter#getGenericElement()
	 * @see #getTypeParameter()
	 * @generated
	 */
	EReference getTypeParameter_GenericElement();

	/**
	 * Returns the meta object for class '
	 * {@link tudresden.ocl20.pivot.pivotmodel.TypeArgument
	 * <em>Type Argument</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Type Argument</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.TypeArgument
	 * @generated
	 */
	EClass getTypeArgument();

	/**
	 * Returns the meta object for the container reference '
	 * {@link tudresden.ocl20.pivot.pivotmodel.TypeArgument#getOwningGenericType
	 * <em>Owning Generic Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the container reference '
	 *         <em>Owning Generic Type</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.TypeArgument#getOwningGenericType()
	 * @see #getTypeArgument()
	 * @generated
	 */
	EReference getTypeArgument_OwningGenericType();

	/**
	 * Returns the meta object for class '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Constraint <em>Constraint</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Constraint</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Constraint
	 * @generated
	 */
	EClass getConstraint();

	/**
	 * Returns the meta object for the attribute '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Constraint#getKind <em>Kind</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Kind</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Constraint#getKind()
	 * @see #getConstraint()
	 * @generated
	 */
	EAttribute getConstraint_Kind();

	/**
	 * Returns the meta object for the container reference '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Constraint#getNamespace
	 * <em>Namespace</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the container reference '<em>Namespace</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Constraint#getNamespace()
	 * @see #getConstraint()
	 * @generated
	 */
	EReference getConstraint_Namespace();

	/**
	 * Returns the meta object for the containment reference '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Constraint#getSpecification
	 * <em>Specification</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '
	 *         <em>Specification</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Constraint#getSpecification()
	 * @see #getConstraint()
	 * @generated
	 */
	EReference getConstraint_Specification();

	/**
	 * Returns the meta object for the reference list '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Constraint#getConstrainedElement
	 * <em>Constrained Element</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the reference list '
	 *         <em>Constrained Element</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Constraint#getConstrainedElement()
	 * @see #getConstraint()
	 * @generated
	 */
	EReference getConstraint_ConstrainedElement();

	/**
	 * Returns the meta object for the reference '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Constraint#getDefinedFeature
	 * <em>Defined Feature</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Defined Feature</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Constraint#getDefinedFeature()
	 * @see #getConstraint()
	 * @generated
	 */
	EReference getConstraint_DefinedFeature();

	/**
	 * Returns the meta object for class '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Expression <em>Expression</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Expression</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Expression
	 * @generated
	 */
	EClass getExpression();

	/**
	 * Returns the meta object for the attribute '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Expression#getBody <em>Body</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Body</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Expression#getBody()
	 * @see #getExpression()
	 * @generated
	 */
	EAttribute getExpression_Body();

	/**
	 * Returns the meta object for the attribute '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Expression#getLanguage
	 * <em>Language</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Language</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Expression#getLanguage()
	 * @see #getExpression()
	 * @generated
	 */
	EAttribute getExpression_Language();

	/**
	 * Returns the meta object for the container reference '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Expression#getConstraint
	 * <em>Constraint</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the container reference '<em>Constraint</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.Expression#getConstraint()
	 * @see #getExpression()
	 * @generated
	 */
	EReference getExpression_Constraint();

	/**
	 * Returns the meta object for class '
	 * {@link tudresden.ocl20.pivot.pivotmodel.AssociationProperty
	 * <em>Association Property</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for class '<em>Association Property</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.AssociationProperty
	 * @generated
	 */
	EClass getAssociationProperty();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link tudresden.ocl20.pivot.pivotmodel.AssociationProperty#getInverseAssociationProperties
	 * <em>Inverse Association Properties</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '
	 *         <em>Inverse Association Properties</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.AssociationProperty#getInverseAssociationProperties()
	 * @see #getAssociationProperty()
	 * @generated
	 */
	EReference getAssociationProperty_InverseAssociationProperties();

	/**
	 * Returns the meta object for enum '
	 * {@link tudresden.ocl20.pivot.pivotmodel.PrimitiveTypeKind
	 * <em>Primitive Type Kind</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for enum '<em>Primitive Type Kind</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.PrimitiveTypeKind
	 * @generated
	 */
	EEnum getPrimitiveTypeKind();

	/**
	 * Returns the meta object for enum '
	 * {@link tudresden.ocl20.pivot.pivotmodel.ParameterDirectionKind
	 * <em>Parameter Direction Kind</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Parameter Direction Kind</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.ParameterDirectionKind
	 * @generated
	 */
	EEnum getParameterDirectionKind();

	/**
	 * Returns the meta object for enum '
	 * {@link tudresden.ocl20.pivot.pivotmodel.ConstraintKind
	 * <em>Constraint Kind</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Constraint Kind</em>'.
	 * @see tudresden.ocl20.pivot.pivotmodel.ConstraintKind
	 * @generated
	 */
	EEnum getConstraintKind();

	/**
	 * Returns the meta object for data type '
	 * {@link java.lang.CloneNotSupportedException
	 * <em>Clone Not Supported Exception</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for data type '
	 *         <em>Clone Not Supported Exception</em>'.
	 * @see java.lang.CloneNotSupportedException
	 * @model instanceClass="java.lang.CloneNotSupportedException"
	 *        serializeable="false"
	 * @generated
	 */
	EDataType getCloneNotSupportedException();

	/**
	 * Returns the factory that creates the instances of the model. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	PivotModelFactory getPivotModelFactory();

	/**
	 * <!-- begin-user-doc --> Defines literals for the meta objects that
	 * represent
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
	interface Literals {

		/**
		 * The meta object literal for the '
		 * {@link tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl
		 * <em>Named Element</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getNamedElement()
		 * @generated
		 */
		EClass NAMED_ELEMENT = eINSTANCE.getNamedElement();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute NAMED_ELEMENT__NAME = eINSTANCE.getNamedElement_Name();

		/**
		 * The meta object literal for the '<em><b>Qualified Name</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute NAMED_ELEMENT__QUALIFIED_NAME = eINSTANCE
				.getNamedElement_QualifiedName();

		/**
		 * The meta object literal for the '<em><b>Owner</b></em>' reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference NAMED_ELEMENT__OWNER = eINSTANCE.getNamedElement_Owner();

		/**
		 * The meta object literal for the '
		 * {@link tudresden.ocl20.pivot.pivotmodel.impl.TypedElementImpl
		 * <em>Typed Element</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.TypedElementImpl
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getTypedElement()
		 * @generated
		 */
		EClass TYPED_ELEMENT = eINSTANCE.getTypedElement();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' reference feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference TYPED_ELEMENT__TYPE = eINSTANCE.getTypedElement_Type();

		/**
		 * The meta object literal for the '<em><b>Generic Type</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @generated
		 */
		EReference TYPED_ELEMENT__GENERIC_TYPE = eINSTANCE
				.getTypedElement_GenericType();

		/**
		 * The meta object literal for the '
		 * {@link tudresden.ocl20.pivot.pivotmodel.impl.FeatureImpl
		 * <em>Feature</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.FeatureImpl
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getFeature()
		 * @generated
		 */
		EClass FEATURE = eINSTANCE.getFeature();

		/**
		 * The meta object literal for the '<em><b>Static</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute FEATURE__STATIC = eINSTANCE.getFeature_Static();

		/**
		 * The meta object literal for the '<em><b>Semantics</b></em>' reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference FEATURE__SEMANTICS = eINSTANCE.getFeature_Semantics();

		/**
		 * The meta object literal for the '
		 * {@link tudresden.ocl20.pivot.pivotmodel.GenericElement
		 * <em>Generic Element</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see tudresden.ocl20.pivot.pivotmodel.GenericElement
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getGenericElement()
		 * @generated
		 */
		EClass GENERIC_ELEMENT = eINSTANCE.getGenericElement();

		/**
		 * The meta object literal for the '<em><b>Owned Type Parameter</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference GENERIC_ELEMENT__OWNED_TYPE_PARAMETER = eINSTANCE
				.getGenericElement_OwnedTypeParameter();

		/**
		 * The meta object literal for the '
		 * {@link tudresden.ocl20.pivot.pivotmodel.ConstrainableElement
		 * <em>Constrainable Element</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see tudresden.ocl20.pivot.pivotmodel.ConstrainableElement
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getConstrainableElement()
		 * @generated
		 */
		EClass CONSTRAINABLE_ELEMENT = eINSTANCE.getConstrainableElement();

		/**
		 * The meta object literal for the '
		 * {@link tudresden.ocl20.pivot.pivotmodel.impl.NamespaceImpl
		 * <em>Namespace</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamespaceImpl
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getNamespace()
		 * @generated
		 */
		EClass NAMESPACE = eINSTANCE.getNamespace();

		/**
		 * The meta object literal for the '<em><b>Owned Type</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference NAMESPACE__OWNED_TYPE = eINSTANCE.getNamespace_OwnedType();

		/**
		 * The meta object literal for the '<em><b>Owned Rule</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference NAMESPACE__OWNED_RULE = eINSTANCE.getNamespace_OwnedRule();

		/**
		 * The meta object literal for the '<em><b>Nested Namespace</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference NAMESPACE__NESTED_NAMESPACE = eINSTANCE
				.getNamespace_NestedNamespace();

		/**
		 * The meta object literal for the '<em><b>Nesting Namespace</b></em>'
		 * container reference feature. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @generated
		 */
		EReference NAMESPACE__NESTING_NAMESPACE = eINSTANCE
				.getNamespace_NestingNamespace();

		/**
		 * The meta object literal for the '
		 * {@link tudresden.ocl20.pivot.pivotmodel.impl.TypeImpl <em>Type</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.TypeImpl
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getType()
		 * @generated
		 */
		EClass TYPE = eINSTANCE.getType();

		/**
		 * The meta object literal for the '<em><b>Namespace</b></em>' container
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference TYPE__NAMESPACE = eINSTANCE.getType_Namespace();

		/**
		 * The meta object literal for the '<em><b>Owned Operation</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference TYPE__OWNED_OPERATION = eINSTANCE.getType_OwnedOperation();

		/**
		 * The meta object literal for the '<em><b>Owned Property</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference TYPE__OWNED_PROPERTY = eINSTANCE.getType_OwnedProperty();

		/**
		 * The meta object literal for the '<em><b>Super Type</b></em>' reference
		 * list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference TYPE__SUPER_TYPE = eINSTANCE.getType_SuperType();

		/**
		 * The meta object literal for the '<em><b>Generic Super Type</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference TYPE__GENERIC_SUPER_TYPE = eINSTANCE.getType_GenericSuperType();

		/**
		 * The meta object literal for the '
		 * {@link tudresden.ocl20.pivot.pivotmodel.impl.PrimitiveTypeImpl
		 * <em>Primitive Type</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PrimitiveTypeImpl
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getPrimitiveType()
		 * @generated
		 */
		EClass PRIMITIVE_TYPE = eINSTANCE.getPrimitiveType();

		/**
		 * The meta object literal for the '<em><b>Kind</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PRIMITIVE_TYPE__KIND = eINSTANCE.getPrimitiveType_Kind();

		/**
		 * The meta object literal for the '
		 * {@link tudresden.ocl20.pivot.pivotmodel.impl.EnumerationImpl
		 * <em>Enumeration</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.EnumerationImpl
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getEnumeration()
		 * @generated
		 */
		EClass ENUMERATION = eINSTANCE.getEnumeration();

		/**
		 * The meta object literal for the '<em><b>Owned Literal</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ENUMERATION__OWNED_LITERAL = eINSTANCE
				.getEnumeration_OwnedLiteral();

		/**
		 * The meta object literal for the '
		 * {@link tudresden.ocl20.pivot.pivotmodel.impl.EnumerationLiteralImpl
		 * <em>Enumeration Literal</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.EnumerationLiteralImpl
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getEnumerationLiteral()
		 * @generated
		 */
		EClass ENUMERATION_LITERAL = eINSTANCE.getEnumerationLiteral();

		/**
		 * The meta object literal for the '<em><b>Enumeration</b></em>' container
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ENUMERATION_LITERAL__ENUMERATION = eINSTANCE
				.getEnumerationLiteral_Enumeration();

		/**
		 * The meta object literal for the '
		 * {@link tudresden.ocl20.pivot.pivotmodel.impl.PropertyImpl
		 * <em>Property</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PropertyImpl
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getProperty()
		 * @generated
		 */
		EClass PROPERTY = eINSTANCE.getProperty();

		/**
		 * The meta object literal for the '<em><b>Owning Type</b></em>' container
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference PROPERTY__OWNING_TYPE = eINSTANCE.getProperty_OwningType();

		/**
		 * The meta object literal for the '
		 * {@link tudresden.ocl20.pivot.pivotmodel.impl.OperationImpl
		 * <em>Operation</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.OperationImpl
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getOperation()
		 * @generated
		 */
		EClass OPERATION = eINSTANCE.getOperation();

		/**
		 * The meta object literal for the '<em><b>Owning Type</b></em>' container
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference OPERATION__OWNING_TYPE = eINSTANCE.getOperation_OwningType();

		/**
		 * The meta object literal for the '<em><b>Owned Parameter</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference OPERATION__OWNED_PARAMETER = eINSTANCE
				.getOperation_OwnedParameter();

		/**
		 * The meta object literal for the '<em><b>Input Parameter</b></em>'
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference OPERATION__INPUT_PARAMETER = eINSTANCE
				.getOperation_InputParameter();

		/**
		 * The meta object literal for the '<em><b>Output Parameter</b></em>'
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference OPERATION__OUTPUT_PARAMETER = eINSTANCE
				.getOperation_OutputParameter();

		/**
		 * The meta object literal for the '<em><b>Return Parameter</b></em>'
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference OPERATION__RETURN_PARAMETER = eINSTANCE
				.getOperation_ReturnParameter();

		/**
		 * The meta object literal for the '<em><b>Signature Parameter</b></em>'
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference OPERATION__SIGNATURE_PARAMETER = eINSTANCE
				.getOperation_SignatureParameter();

		/**
		 * The meta object literal for the '
		 * {@link tudresden.ocl20.pivot.pivotmodel.impl.ParameterImpl
		 * <em>Parameter</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.ParameterImpl
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getParameter()
		 * @generated
		 */
		EClass PARAMETER = eINSTANCE.getParameter();

		/**
		 * The meta object literal for the '<em><b>Kind</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PARAMETER__KIND = eINSTANCE.getParameter_Kind();

		/**
		 * The meta object literal for the '<em><b>Operation</b></em>' container
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference PARAMETER__OPERATION = eINSTANCE.getParameter_Operation();

		/**
		 * The meta object literal for the '
		 * {@link tudresden.ocl20.pivot.pivotmodel.impl.GenericTypeImpl
		 * <em>Generic Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.GenericTypeImpl
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getGenericType()
		 * @generated
		 */
		EClass GENERIC_TYPE = eINSTANCE.getGenericType();

		/**
		 * The meta object literal for the '
		 * {@link tudresden.ocl20.pivot.pivotmodel.impl.ParameterGenericTypeImpl
		 * <em>Parameter Generic Type</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.ParameterGenericTypeImpl
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getParameterGenericType()
		 * @generated
		 */
		EClass PARAMETER_GENERIC_TYPE = eINSTANCE.getParameterGenericType();

		/**
		 * The meta object literal for the '<em><b>Type Parameter</b></em>'
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference PARAMETER_GENERIC_TYPE__TYPE_PARAMETER = eINSTANCE
				.getParameterGenericType_TypeParameter();

		/**
		 * The meta object literal for the '
		 * {@link tudresden.ocl20.pivot.pivotmodel.impl.ComplexGenericTypeImpl
		 * <em>Complex Generic Type</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.ComplexGenericTypeImpl
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getComplexGenericType()
		 * @generated
		 */
		EClass COMPLEX_GENERIC_TYPE = eINSTANCE.getComplexGenericType();

		/**
		 * The meta object literal for the '<em><b>Unbound Type</b></em>' reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference COMPLEX_GENERIC_TYPE__UNBOUND_TYPE = eINSTANCE
				.getComplexGenericType_UnboundType();

		/**
		 * The meta object literal for the '<em><b>Type Argument</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference COMPLEX_GENERIC_TYPE__TYPE_ARGUMENT = eINSTANCE
				.getComplexGenericType_TypeArgument();

		/**
		 * The meta object literal for the '
		 * {@link tudresden.ocl20.pivot.pivotmodel.impl.TypeParameterImpl
		 * <em>Type Parameter</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.TypeParameterImpl
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getTypeParameter()
		 * @generated
		 */
		EClass TYPE_PARAMETER = eINSTANCE.getTypeParameter();

		/**
		 * The meta object literal for the '<em><b>Generic Element</b></em>'
		 * container reference feature. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @generated
		 */
		EReference TYPE_PARAMETER__GENERIC_ELEMENT = eINSTANCE
				.getTypeParameter_GenericElement();

		/**
		 * The meta object literal for the '
		 * {@link tudresden.ocl20.pivot.pivotmodel.impl.TypeArgumentImpl
		 * <em>Type Argument</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.TypeArgumentImpl
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getTypeArgument()
		 * @generated
		 */
		EClass TYPE_ARGUMENT = eINSTANCE.getTypeArgument();

		/**
		 * The meta object literal for the '<em><b>Owning Generic Type</b></em>'
		 * container reference feature. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @generated
		 */
		EReference TYPE_ARGUMENT__OWNING_GENERIC_TYPE = eINSTANCE
				.getTypeArgument_OwningGenericType();

		/**
		 * The meta object literal for the '
		 * {@link tudresden.ocl20.pivot.pivotmodel.impl.ConstraintImpl
		 * <em>Constraint</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.ConstraintImpl
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getConstraint()
		 * @generated
		 */
		EClass CONSTRAINT = eINSTANCE.getConstraint();

		/**
		 * The meta object literal for the '<em><b>Kind</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute CONSTRAINT__KIND = eINSTANCE.getConstraint_Kind();

		/**
		 * The meta object literal for the '<em><b>Namespace</b></em>' container
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference CONSTRAINT__NAMESPACE = eINSTANCE.getConstraint_Namespace();

		/**
		 * The meta object literal for the '<em><b>Specification</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @generated
		 */
		EReference CONSTRAINT__SPECIFICATION = eINSTANCE
				.getConstraint_Specification();

		/**
		 * The meta object literal for the '<em><b>Constrained Element</b></em>'
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference CONSTRAINT__CONSTRAINED_ELEMENT = eINSTANCE
				.getConstraint_ConstrainedElement();

		/**
		 * The meta object literal for the '<em><b>Defined Feature</b></em>'
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference CONSTRAINT__DEFINED_FEATURE = eINSTANCE
				.getConstraint_DefinedFeature();

		/**
		 * The meta object literal for the '
		 * {@link tudresden.ocl20.pivot.pivotmodel.impl.ExpressionImpl
		 * <em>Expression</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.ExpressionImpl
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getExpression()
		 * @generated
		 */
		EClass EXPRESSION = eINSTANCE.getExpression();

		/**
		 * The meta object literal for the '<em><b>Body</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute EXPRESSION__BODY = eINSTANCE.getExpression_Body();

		/**
		 * The meta object literal for the '<em><b>Language</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute EXPRESSION__LANGUAGE = eINSTANCE.getExpression_Language();

		/**
		 * The meta object literal for the '<em><b>Constraint</b></em>' container
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference EXPRESSION__CONSTRAINT = eINSTANCE.getExpression_Constraint();

		/**
		 * The meta object literal for the '
		 * {@link tudresden.ocl20.pivot.pivotmodel.impl.AssociationPropertyImpl
		 * <em>Association Property</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.AssociationPropertyImpl
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getAssociationProperty()
		 * @generated
		 */
		EClass ASSOCIATION_PROPERTY = eINSTANCE.getAssociationProperty();

		/**
		 * The meta object literal for the '
		 * <em><b>Inverse Association Properties</b></em>' containment reference
		 * list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ASSOCIATION_PROPERTY__INVERSE_ASSOCIATION_PROPERTIES = eINSTANCE
				.getAssociationProperty_InverseAssociationProperties();

		/**
		 * The meta object literal for the '
		 * {@link tudresden.ocl20.pivot.pivotmodel.PrimitiveTypeKind
		 * <em>Primitive Type Kind</em>}' enum. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see tudresden.ocl20.pivot.pivotmodel.PrimitiveTypeKind
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getPrimitiveTypeKind()
		 * @generated
		 */
		EEnum PRIMITIVE_TYPE_KIND = eINSTANCE.getPrimitiveTypeKind();

		/**
		 * The meta object literal for the '
		 * {@link tudresden.ocl20.pivot.pivotmodel.ParameterDirectionKind
		 * <em>Parameter Direction Kind</em>}' enum. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see tudresden.ocl20.pivot.pivotmodel.ParameterDirectionKind
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getParameterDirectionKind()
		 * @generated
		 */
		EEnum PARAMETER_DIRECTION_KIND = eINSTANCE.getParameterDirectionKind();

		/**
		 * The meta object literal for the '
		 * {@link tudresden.ocl20.pivot.pivotmodel.ConstraintKind
		 * <em>Constraint Kind</em>}' enum. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see tudresden.ocl20.pivot.pivotmodel.ConstraintKind
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getConstraintKind()
		 * @generated
		 */
		EEnum CONSTRAINT_KIND = eINSTANCE.getConstraintKind();

		/**
		 * The meta object literal for the '<em>Clone Not Supported Exception</em>'
		 * data type. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see java.lang.CloneNotSupportedException
		 * @see tudresden.ocl20.pivot.pivotmodel.impl.PivotModelPackageImpl#getCloneNotSupportedException()
		 * @generated
		 */
		EDataType CLONE_NOT_SUPPORTED_EXCEPTION = eINSTANCE
				.getCloneNotSupportedException();

	}

} // PivotModelPackage
