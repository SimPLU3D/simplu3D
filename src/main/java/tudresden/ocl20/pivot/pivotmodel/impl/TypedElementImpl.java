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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import tudresden.ocl20.pivot.pivotmodel.GenericElement;
import tudresden.ocl20.pivot.pivotmodel.GenericType;
import tudresden.ocl20.pivot.pivotmodel.PivotModelPackage;
import tudresden.ocl20.pivot.pivotmodel.Type;
import tudresden.ocl20.pivot.pivotmodel.TypeParameter;
import tudresden.ocl20.pivot.pivotmodel.TypedElement;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Typed Element</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.impl.TypedElementImpl#getType
 * <em>Type</em>}</li>
 * <li>
 * {@link tudresden.ocl20.pivot.pivotmodel.impl.TypedElementImpl#getGenericType
 * <em>Generic Type</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public abstract class TypedElementImpl extends NamedElementImpl implements
		TypedElement {

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected Type type;

	/**
	 * The cached value of the '{@link #getGenericType() <em>Generic Type</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getGenericType()
	 * @generated
	 * @ordered
	 */
	protected GenericType genericType;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected TypedElementImpl() {

		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Type getType() {

		return type;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setType(Type newType) {

		Type oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					PivotModelPackage.TYPED_ELEMENT__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public GenericType getGenericType() {

		return genericType;
	}

	/**
	 * Convenience method for subclasses that initializes a cloned
	 * <code>TypedElement</code> with the properties of this
	 * <code>TypedElement</code>.
	 */
	protected TypedElement initialize(TypedElement clone) {

		super.initialize(clone);

		// clone the generic type if existent
		if (getGenericType() != null) {
			clone.setGenericType(getGenericType().clone());
		}

		// it is important to do this AFTER the generic type has been set or it will
		// be reset
		clone.setType(getType());

		return clone;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetGenericType(GenericType newGenericType,
			NotificationChain msgs) {

		GenericType oldGenericType = genericType;
		genericType = newGenericType;
		if (eNotificationRequired()) {
			ENotificationImpl notification =
					new ENotificationImpl(this, Notification.SET,
							PivotModelPackage.TYPED_ELEMENT__GENERIC_TYPE, oldGenericType,
							newGenericType);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * Decorator method for {@link #setGenericTypeGen(GenericType)} that resets
	 * the {@link #getType() type} of this <code>TypedElement</code> if a
	 * <code>GenericType</code> is set. This ensures that a
	 * <code>TypedElement</code> with a generic type has to be
	 * {@link GenericType#bindTypedElement(java.util.List, java.util.List) bound}
	 * first before it has a proper non-generic type.
	 * 
	 * @see GenericElement#bind(TypeParameter, Type)
	 * 
	 * @generated NOT
	 */
	public void setGenericType(GenericType newGenericType) {

		// reset an existing type if the new generic type is different from the old
		// one
		if ((genericType == null && newGenericType != null)
				|| (genericType != null && !genericType.equals(newGenericType))) {
			setType(null);
		}

		setGenericTypeGen(newGenericType);
	}

	/**
	 * <!-- begin-user-doc --> The EMF implementation is forwarded to this method.
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setGenericTypeGen(GenericType newGenericType) {

		if (newGenericType != genericType) {
			NotificationChain msgs = null;
			if (genericType != null)
				msgs =
						((InternalEObject) genericType)
								.eInverseRemove(this, EOPPOSITE_FEATURE_BASE
										- PivotModelPackage.TYPED_ELEMENT__GENERIC_TYPE, null, msgs);
			if (newGenericType != null)
				msgs =
						((InternalEObject) newGenericType)
								.eInverseAdd(this, EOPPOSITE_FEATURE_BASE
										- PivotModelPackage.TYPED_ELEMENT__GENERIC_TYPE, null, msgs);
			msgs = basicSetGenericType(newGenericType, msgs);
			if (msgs != null)
				msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					PivotModelPackage.TYPED_ELEMENT__GENERIC_TYPE, newGenericType,
					newGenericType));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {

		return PivotModelPackage.Literals.TYPED_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd,
			int featureID, NotificationChain msgs) {

		switch (featureID) {
		case PivotModelPackage.TYPED_ELEMENT__GENERIC_TYPE:
			return basicSetGenericType(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {

		switch (featureID) {
		case PivotModelPackage.TYPED_ELEMENT__TYPE:
			return getType();
		case PivotModelPackage.TYPED_ELEMENT__GENERIC_TYPE:
			return getGenericType();
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
		case PivotModelPackage.TYPED_ELEMENT__TYPE:
			setType((Type) newValue);
			return;
		case PivotModelPackage.TYPED_ELEMENT__GENERIC_TYPE:
			setGenericType((GenericType) newValue);
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
		case PivotModelPackage.TYPED_ELEMENT__TYPE:
			setType((Type) null);
			return;
		case PivotModelPackage.TYPED_ELEMENT__GENERIC_TYPE:
			setGenericType((GenericType) null);
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
		case PivotModelPackage.TYPED_ELEMENT__TYPE:
			return type != null;
		case PivotModelPackage.TYPED_ELEMENT__GENERIC_TYPE:
			return genericType != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * Overridden to include the type and generic type in the stringification of
	 * subclasses.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl#toString()
	 */
	@Override
	public String toString() {

		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.appendToString(super.toString())
				.append("type", getType()).append("genericType", getGenericType()) //$NON-NLS-1$ //$NON-NLS-2$
				.toString();
	}

} // TypedElementImpl
