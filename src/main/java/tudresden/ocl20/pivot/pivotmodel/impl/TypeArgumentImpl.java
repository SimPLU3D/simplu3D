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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import tudresden.ocl20.pivot.pivotmodel.ComplexGenericType;
import tudresden.ocl20.pivot.pivotmodel.NamedElement;
import tudresden.ocl20.pivot.pivotmodel.PivotModelFactory;
import tudresden.ocl20.pivot.pivotmodel.PivotModelPackage;
import tudresden.ocl20.pivot.pivotmodel.TypeArgument;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Type Argument</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link tudresden.ocl20.pivot.pivotmodel.impl.TypeArgumentImpl#getOwningGenericType
 * <em>Owning Generic Type</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class TypeArgumentImpl extends TypedElementImpl implements TypeArgument {

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected TypeArgumentImpl() {

		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {

		return PivotModelPackage.Literals.TYPE_ARGUMENT;
	}

	/**
	 * Overridden to return the name of the type of this <code>TypeArgument</code>
	 * because commonly a type argument won't have a dedicated name. If neither a
	 * type nor a generic type is set, the empty string is returned.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl#getName()
	 */
	@Override
	public String getName() {

		return getType() != null ? getType().getName()
				: (getGenericType() != null ? getGenericType().getName() : ""); //$NON-NLS-1$
	}

	/**
	 * Overridden to prevent setting a name. The name of the
	 * <code>TypeArgument</code> is determined based on the referenced type. This
	 * method will throw an {@link UnsupportedOperationException}.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl#setName(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unused")
	public void setName(String newName) {

		throw new UnsupportedOperationException(
				"The name of a type argument cannot be changed."); //$NON-NLS-1$
	}

	/**
	 * Overridden to return the {@link #getOwningGenericType() generic type} that
	 * owns this <code>TypeArgument</code>
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl#getOwner()
	 */
	@Override
	public NamedElement getOwner() {

		return getOwningGenericType();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ComplexGenericType getOwningGenericType() {

		if (eContainerFeatureID() != PivotModelPackage.TYPE_ARGUMENT__OWNING_GENERIC_TYPE)
			return null;
		return (ComplexGenericType) eContainer();
	}

	/*
	 * (non-Javadoc)
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl#clone()
	 */
	@Override
	public TypeArgument clone() {

		return (TypeArgument) initialize(PivotModelFactory.eINSTANCE
				.createTypeArgument());
	}

	/**
	 * Overridden to indicate that the name is determined automatically. Assure
	 * that {@link #setName(String)} is not called which would throw an exception.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl#hasVolatileName()
	 */
	@Override
	protected boolean hasVolatileName() {

		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetOwningGenericType(
			ComplexGenericType newOwningGenericType, NotificationChain msgs) {

		msgs =
				eBasicSetContainer((InternalEObject) newOwningGenericType,
						PivotModelPackage.TYPE_ARGUMENT__OWNING_GENERIC_TYPE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setOwningGenericType(ComplexGenericType newOwningGenericType) {

		if (newOwningGenericType != eInternalContainer()
				|| (eContainerFeatureID() != PivotModelPackage.TYPE_ARGUMENT__OWNING_GENERIC_TYPE && newOwningGenericType != null)) {
			if (EcoreUtil.isAncestor(this, newOwningGenericType))
				throw new IllegalArgumentException(
						"Recursive containment not allowed for " + toString()); //$NON-NLS-1$
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newOwningGenericType != null)
				msgs =
						((InternalEObject) newOwningGenericType).eInverseAdd(this,
								PivotModelPackage.COMPLEX_GENERIC_TYPE__TYPE_ARGUMENT,
								ComplexGenericType.class, msgs);
			msgs = basicSetOwningGenericType(newOwningGenericType, msgs);
			if (msgs != null)
				msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					PivotModelPackage.TYPE_ARGUMENT__OWNING_GENERIC_TYPE,
					newOwningGenericType, newOwningGenericType));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID,
			NotificationChain msgs) {

		switch (featureID) {
		case PivotModelPackage.TYPE_ARGUMENT__OWNING_GENERIC_TYPE:
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			return basicSetOwningGenericType((ComplexGenericType) otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
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
		case PivotModelPackage.TYPE_ARGUMENT__OWNING_GENERIC_TYPE:
			return basicSetOwningGenericType(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(
			NotificationChain msgs) {

		switch (eContainerFeatureID()) {
		case PivotModelPackage.TYPE_ARGUMENT__OWNING_GENERIC_TYPE:
			return eInternalContainer().eInverseRemove(this,
					PivotModelPackage.COMPLEX_GENERIC_TYPE__TYPE_ARGUMENT,
					ComplexGenericType.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {

		switch (featureID) {
		case PivotModelPackage.TYPE_ARGUMENT__OWNING_GENERIC_TYPE:
			return getOwningGenericType();
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
		case PivotModelPackage.TYPE_ARGUMENT__OWNING_GENERIC_TYPE:
			setOwningGenericType((ComplexGenericType) newValue);
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
		case PivotModelPackage.TYPE_ARGUMENT__OWNING_GENERIC_TYPE:
			setOwningGenericType((ComplexGenericType) null);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * The EMF implementation is adapted to prevent that the name of the
	 * <code>TypeArgument</code> is serialized to XMI. This is necessary to
	 * prevent setting the name upon loading the document which would throw an
	 * exception.
	 * 
	 * @generated NOT
	 * 
	 * @see #setName(String)
	 */
	@Override
	public boolean eIsSet(int featureID) {

		switch (featureID) {
		case PivotModelPackageImpl.TYPE_ARGUMENT__OWNING_GENERIC_TYPE:
			return getOwningGenericType() != null;
		case PivotModelPackageImpl.TYPE_ARGUMENT__NAME:
			return false;
		}
		return super.eIsSet(featureID);
	}

} // TypeArgumentImpl
