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
package tudresden.ocl20.pivot.datatypes;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;

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
 * @see tudresden.ocl20.pivot.datatypes.DatatypesFactory
 * @model kind="package"
 * @generated
 */
public interface DatatypesPackage extends EPackage {

	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "datatypes"; //$NON-NLS-1$

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://www.tu-dresden.de/ocl20/pivot/2007/datatypes"; //$NON-NLS-1$

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "datatypes"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	DatatypesPackage eINSTANCE =
			tudresden.ocl20.pivot.datatypes.impl.DatatypesPackageImpl.init();

	/**
	 * The meta object id for the '<em>Boolean</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see tudresden.ocl20.pivot.datatypes.impl.DatatypesPackageImpl#getBoolean()
	 * @generated
	 */
	int BOOLEAN = 0;

	/**
	 * The meta object id for the '<em>Integer</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see tudresden.ocl20.pivot.datatypes.impl.DatatypesPackageImpl#getInteger()
	 * @generated
	 */
	int INTEGER = 1;

	/**
	 * The meta object id for the '<em>Real</em>' data type. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see tudresden.ocl20.pivot.datatypes.impl.DatatypesPackageImpl#getReal()
	 * @generated
	 */
	int REAL = 2;

	/**
	 * The meta object id for the '<em>String</em>' data type. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see java.lang.String
	 * @see tudresden.ocl20.pivot.datatypes.impl.DatatypesPackageImpl#getString()
	 * @generated
	 */
	int STRING = 3;

	/**
	 * The meta object id for the '<em>Unlimited Natural</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see tudresden.ocl20.pivot.datatypes.impl.DatatypesPackageImpl#getUnlimitedNatural()
	 * @generated
	 */
	int UNLIMITED_NATURAL = 4;

	/**
	 * The meta object id for the '<em>Collection</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see java.util.Collection
	 * @see tudresden.ocl20.pivot.datatypes.impl.DatatypesPackageImpl#getCollection()
	 * @generated
	 */
	int COLLECTION = 5;

	/**
	 * The meta object id for the '<em>Sequence</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see java.util.List
	 * @see tudresden.ocl20.pivot.datatypes.impl.DatatypesPackageImpl#getSequence()
	 * @generated
	 */
	int SEQUENCE = 6;

	/**
	 * The meta object id for the '<em>Bag</em>' data type. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see java.util.List
	 * @see tudresden.ocl20.pivot.datatypes.impl.DatatypesPackageImpl#getBag()
	 * @generated
	 */
	int BAG = 7;

	/**
	 * The meta object id for the '<em>Set</em>' data type. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see java.util.Set
	 * @see tudresden.ocl20.pivot.datatypes.impl.DatatypesPackageImpl#getSet()
	 * @generated
	 */
	int SET = 8;

	/**
	 * The meta object id for the '<em>Ordered Set</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see java.util.List
	 * @see tudresden.ocl20.pivot.datatypes.impl.DatatypesPackageImpl#getOrderedSet()
	 * @generated
	 */
	int ORDERED_SET = 9;

	/**
	 * Returns the meta object for data type '<em>Boolean</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Boolean</em>'.
	 * @model instanceClass="boolean"
	 * @generated
	 */
	EDataType getBoolean();

	/**
	 * Returns the meta object for data type '<em>Integer</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Integer</em>'.
	 * @model instanceClass="int"
	 * @generated
	 */
	EDataType getInteger();

	/**
	 * Returns the meta object for data type '<em>Real</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Real</em>'.
	 * @model instanceClass="float"
	 * @generated
	 */
	EDataType getReal();

	/**
	 * Returns the meta object for data type '{@link java.lang.String
	 * <em>String</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>String</em>'.
	 * @see java.lang.String
	 * @model instanceClass="java.lang.String"
	 * @generated
	 */
	EDataType getString();

	/**
	 * Returns the meta object for data type '<em>Unlimited Natural</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Unlimited Natural</em>'.
	 * @model instanceClass="long"
	 * @generated
	 */
	EDataType getUnlimitedNatural();

	/**
	 * Returns the meta object for data type '{@link java.util.Collection
	 * <em>Collection</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Collection</em>'.
	 * @see java.util.Collection
	 * @model instanceClass="java.util.Collection" serializeable="false"
	 *        typeParameters="T"
	 * @generated
	 */
	EDataType getCollection();

	/**
	 * Returns the meta object for data type '{@link java.util.List
	 * <em>Sequence</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Sequence</em>'.
	 * @see java.util.List
	 * @model instanceClass="java.util.List" serializeable="false"
	 *        typeParameters="T"
	 * @generated
	 */
	EDataType getSequence();

	/**
	 * Returns the meta object for data type '{@link java.util.List <em>Bag</em>}
	 * '. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Bag</em>'.
	 * @see java.util.List
	 * @model instanceClass="java.util.List" serializeable="false"
	 *        typeParameters="T"
	 * @generated
	 */
	EDataType getBag();

	/**
	 * Returns the meta object for data type '{@link java.util.Set <em>Set</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Set</em>'.
	 * @see java.util.Set
	 * @model instanceClass="java.util.Set" serializeable="false"
	 *        typeParameters="T"
	 * @generated
	 */
	EDataType getSet();

	/**
	 * Returns the meta object for data type '{@link java.util.List
	 * <em>Ordered Set</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Ordered Set</em>'.
	 * @see java.util.List
	 * @model instanceClass="java.util.List" serializeable="false"
	 *        typeParameters="T"
	 * @generated
	 */
	EDataType getOrderedSet();

	/**
	 * Returns the factory that creates the instances of the model. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DatatypesFactory getDatatypesFactory();

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
		 * The meta object literal for the '<em>Boolean</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see tudresden.ocl20.pivot.datatypes.impl.DatatypesPackageImpl#getBoolean()
		 * @generated
		 */
		EDataType BOOLEAN = eINSTANCE.getBoolean();

		/**
		 * The meta object literal for the '<em>Integer</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see tudresden.ocl20.pivot.datatypes.impl.DatatypesPackageImpl#getInteger()
		 * @generated
		 */
		EDataType INTEGER = eINSTANCE.getInteger();

		/**
		 * The meta object literal for the '<em>Real</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see tudresden.ocl20.pivot.datatypes.impl.DatatypesPackageImpl#getReal()
		 * @generated
		 */
		EDataType REAL = eINSTANCE.getReal();

		/**
		 * The meta object literal for the '<em>String</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see java.lang.String
		 * @see tudresden.ocl20.pivot.datatypes.impl.DatatypesPackageImpl#getString()
		 * @generated
		 */
		EDataType STRING = eINSTANCE.getString();

		/**
		 * The meta object literal for the '<em>Unlimited Natural</em>' data type.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see tudresden.ocl20.pivot.datatypes.impl.DatatypesPackageImpl#getUnlimitedNatural()
		 * @generated
		 */
		EDataType UNLIMITED_NATURAL = eINSTANCE.getUnlimitedNatural();

		/**
		 * The meta object literal for the '<em>Collection</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see java.util.Collection
		 * @see tudresden.ocl20.pivot.datatypes.impl.DatatypesPackageImpl#getCollection()
		 * @generated
		 */
		EDataType COLLECTION = eINSTANCE.getCollection();

		/**
		 * The meta object literal for the '<em>Sequence</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see java.util.List
		 * @see tudresden.ocl20.pivot.datatypes.impl.DatatypesPackageImpl#getSequence()
		 * @generated
		 */
		EDataType SEQUENCE = eINSTANCE.getSequence();

		/**
		 * The meta object literal for the '<em>Bag</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see java.util.List
		 * @see tudresden.ocl20.pivot.datatypes.impl.DatatypesPackageImpl#getBag()
		 * @generated
		 */
		EDataType BAG = eINSTANCE.getBag();

		/**
		 * The meta object literal for the '<em>Set</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see java.util.Set
		 * @see tudresden.ocl20.pivot.datatypes.impl.DatatypesPackageImpl#getSet()
		 * @generated
		 */
		EDataType SET = eINSTANCE.getSet();

		/**
		 * The meta object literal for the '<em>Ordered Set</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see java.util.List
		 * @see tudresden.ocl20.pivot.datatypes.impl.DatatypesPackageImpl#getOrderedSet()
		 * @generated
		 */
		EDataType ORDERED_SET = eINSTANCE.getOrderedSet();

	}

} // DatatypesPackage
