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
package tudresden.ocl20.pivot.datatypes.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import tudresden.ocl20.pivot.datatypes.*;
import tudresden.ocl20.pivot.datatypes.DatatypesFactory;
import tudresden.ocl20.pivot.datatypes.DatatypesPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class DatatypesFactoryImpl extends EFactoryImpl implements
		DatatypesFactory {

	/**
	 * Creates the default factory implementation. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public static DatatypesFactory init() {

		try {
			DatatypesFactory theDatatypesFactory =
					(DatatypesFactory) EPackage.Registry.INSTANCE
							.getEFactory("http://www.tu-dresden.de/ocl20/pivot/2007/datatypes"); //$NON-NLS-1$ 
			if (theDatatypesFactory != null) {
				return theDatatypesFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new DatatypesFactoryImpl();
	}

	/**
	 * Creates an instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public DatatypesFactoryImpl() {

		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {

		switch (eClass.getClassifierID()) {
		default:
			throw new IllegalArgumentException(
					"The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {

		switch (eDataType.getClassifierID()) {
		case DatatypesPackage.BOOLEAN:
			return createBooleanFromString(eDataType, initialValue);
		case DatatypesPackage.INTEGER:
			return createIntegerFromString(eDataType, initialValue);
		case DatatypesPackage.REAL:
			return createRealFromString(eDataType, initialValue);
		case DatatypesPackage.STRING:
			return createStringFromString(eDataType, initialValue);
		case DatatypesPackage.UNLIMITED_NATURAL:
			return createUnlimitedNaturalFromString(eDataType, initialValue);
		default:
			throw new IllegalArgumentException(
					"The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {

		switch (eDataType.getClassifierID()) {
		case DatatypesPackage.BOOLEAN:
			return convertBooleanToString(eDataType, instanceValue);
		case DatatypesPackage.INTEGER:
			return convertIntegerToString(eDataType, instanceValue);
		case DatatypesPackage.REAL:
			return convertRealToString(eDataType, instanceValue);
		case DatatypesPackage.STRING:
			return convertStringToString(eDataType, instanceValue);
		case DatatypesPackage.UNLIMITED_NATURAL:
			return convertUnlimitedNaturalToString(eDataType, instanceValue);
		default:
			throw new IllegalArgumentException(
					"The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@SuppressWarnings("unused")
	public String createUnlimitedNaturalFromString(EDataType eDataType,
			String initialValue) {

		return initialValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@SuppressWarnings("unused")
	public String convertUnlimitedNaturalToString(EDataType eDataType,
			Object instanceValue) {

		return (String) instanceValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DatatypesPackage getDatatypesPackage() {

		return (DatatypesPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@SuppressWarnings("unused")
	public Integer createIntegerFromString(EDataType eDataType,
			String initialValue) {

		return initialValue == null ? null : Integer.valueOf(initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@SuppressWarnings("unused")
	public String convertIntegerToString(EDataType eDataType, Object instanceValue) {

		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@SuppressWarnings("unused")
	public Boolean createBooleanFromString(EDataType eDataType,
			String initialValue) {

		return initialValue == null ? null : Boolean.valueOf(initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@SuppressWarnings("unused")
	public String convertBooleanToString(EDataType eDataType, Object instanceValue) {

		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@SuppressWarnings("unused")
	public String createStringFromString(EDataType eDataType, String initialValue) {

		return initialValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@SuppressWarnings("unused")
	public String convertStringToString(EDataType eDataType, Object instanceValue) {

		return (String) instanceValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@SuppressWarnings("unused")
	public Float createRealFromString(EDataType eDataType, String initialValue) {

		return initialValue == null ? null : Float.valueOf(initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@SuppressWarnings("unused")
	public String convertRealToString(EDataType eDataType, Object instanceValue) {

		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static DatatypesPackage getPackage() {

		return DatatypesPackage.eINSTANCE;
	}

} // DatatypesFactoryImpl
