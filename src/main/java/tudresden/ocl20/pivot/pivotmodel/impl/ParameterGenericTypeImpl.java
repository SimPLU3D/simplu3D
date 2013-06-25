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

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import tudresden.ocl20.pivot.pivotmodel.ParameterGenericType;
import tudresden.ocl20.pivot.pivotmodel.PivotModelFactory;
import tudresden.ocl20.pivot.pivotmodel.PivotModelPackage;
import tudresden.ocl20.pivot.pivotmodel.Type;
import tudresden.ocl20.pivot.pivotmodel.TypeParameter;
import tudresden.ocl20.pivot.pivotmodel.TypedElement;
import tudresden.ocl20.pivot.pivotmodel.util.ListUtil;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Parameter Generic Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link tudresden.ocl20.pivot.pivotmodel.impl.ParameterGenericTypeImpl#getTypeParameter
 * <em>Type Parameter</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ParameterGenericTypeImpl extends GenericTypeImpl implements
		ParameterGenericType {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(ParameterGenericTypeImpl.class);

	/**
	 * The cached value of the '{@link #getTypeParameter()
	 * <em>Type Parameter</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getTypeParameter()
	 * @generated
	 * @ordered
	 */
	protected TypeParameter typeParameter;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ParameterGenericTypeImpl() {

		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {

		return PivotModelPackage.Literals.PARAMETER_GENERIC_TYPE;
	}

	/**
	 * Overridden to return the name of the associated type parameter.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl#getName()
	 */
	@Override
	public String getName() {

		return getTypeParameter() != null ? getTypeParameter().getName() : ""; //$NON-NLS-1$
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public TypeParameter getTypeParameter() {

		return typeParameter;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setTypeParameter(TypeParameter newTypeParameter) {

		TypeParameter oldTypeParameter = typeParameter;
		typeParameter = newTypeParameter;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					PivotModelPackage.PARAMETER_GENERIC_TYPE__TYPE_PARAMETER,
					oldTypeParameter, typeParameter));
	}

	/**
	 * This method will bind the type of the <code>typedElement</code> if the
	 * {@link #getTypeParameter() type parameter} of this
	 * <code>ParameterGenericType</code> is in the given list of type parameters
	 * that shall be bound.
	 */
	@Override
	protected boolean doBindGenericType(List<TypeParameter> parameters,
			List<? extends Type> types, TypedElement typedElement) {

		int index;

		// search the type parameter of this generic type in the list of parameters
		index = ListUtil.indexOf(parameters, getTypeParameter());

		// bind the generic type if type parameter was found
		if (index != -1) {
			Type type = types.get(index);

			if (logger.isInfoEnabled()) {
				logger.info("Binding type of '" + typedElement.getQualifiedName() //$NON-NLS-1$
						+ "' with '" + type.getName() + "'."); //$NON-NLS-1$ //$NON-NLS-2$
			}

			// reset the generic type, then set the type (the order is important)
			typedElement.setGenericType(null);
			typedElement.setType(type);

			// break ans signal success
			return true;
		}

		return false;
	}

	/**
	 * This method currently does nothing since we do not support type parameters
	 * as generic super types. This can be implemented later if necessary.
	 * 
	 * @return always <code>false</code>
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.GenericTypeImpl#doBindGenericSuperType(java.util.List,
	 *      java.util.List, tudresden.ocl20.pivot.pivotmodel.Type)
	 */
	@Override
	@SuppressWarnings("unused")
	protected boolean doBindGenericSuperType(List<TypeParameter> parameters,
			List<? extends Type> types, Type subType) {

		return false;
	}

	/**
	 * This method returns <code>true</code> because any type is conformant to an
	 * unbound {@link TypeParameter}. The non-generic type of an element referring
	 * to a type parameter simply is the root of the object hierarchy (e.g.
	 * java.lang.Object or OclAny).
	 * 
	 * <p>
	 * To see why, consider the following example: a generic operation
	 * {@code <T> op(param:T) : T} can be called with any argument, no matter what
	 * type. The type parameter <code>T</code> will consecutively be bound to the
	 * type of the argument, determining the correct return type.
	 * </p>
	 * 
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.GenericTypeImpl#isConformantType(tudresden.ocl20.pivot.pivotmodel.Type)
	 */
	@Override
	@SuppressWarnings("unused")
	public boolean isConformant(Type type) {

		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.GenericTypeImpl#clone()
	 */
	@Override
	public ParameterGenericType clone() {

		return initialize(PivotModelFactory.eINSTANCE.createParameterGenericType());
	}

	/**
	 * Helper method that initializes a cloned <code>ParameterGenericType</code>.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl#initialize(tudresden.ocl20.pivot.pivotmodel.NamedElement)
	 */
	protected ParameterGenericType initialize(ParameterGenericType clone) {

		clone.setTypeParameter(getTypeParameter());
		return clone;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {

		switch (featureID) {
		case PivotModelPackage.PARAMETER_GENERIC_TYPE__TYPE_PARAMETER:
			return getTypeParameter();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {

		switch (featureID) {
		case PivotModelPackage.PARAMETER_GENERIC_TYPE__TYPE_PARAMETER:
			setTypeParameter((TypeParameter) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {

		switch (featureID) {
		case PivotModelPackage.PARAMETER_GENERIC_TYPE__TYPE_PARAMETER:
			setTypeParameter((TypeParameter) null);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {

		switch (featureID) {
		case PivotModelPackage.PARAMETER_GENERIC_TYPE__TYPE_PARAMETER:
			return typeParameter != null;
		}
		return super.eIsSet(featureID);
	}

} // ParameterGenericTypeImpl
