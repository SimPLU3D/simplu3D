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

import tudresden.ocl20.pivot.pivotmodel.GenericElement;
import tudresden.ocl20.pivot.pivotmodel.NamedElement;
import tudresden.ocl20.pivot.pivotmodel.PivotModelFactory;
import tudresden.ocl20.pivot.pivotmodel.PivotModelPackage;
import tudresden.ocl20.pivot.pivotmodel.TypeParameter;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Type Parameter</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link tudresden.ocl20.pivot.pivotmodel.impl.TypeParameterImpl#getGenericElement
 * <em>Generic Element</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class TypeParameterImpl extends NamedElementImpl implements
		TypeParameter {

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected TypeParameterImpl() {

		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {

		return PivotModelPackage.Literals.TYPE_PARAMETER;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public GenericElement getGenericElement() {

		if (eContainerFeatureID() != PivotModelPackage.TYPE_PARAMETER__GENERIC_ELEMENT)
			return null;
		return (GenericElement) eContainer();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetGenericElement(
			GenericElement newGenericElement, NotificationChain msgs) {

		msgs =
				eBasicSetContainer((InternalEObject) newGenericElement,
						PivotModelPackage.TYPE_PARAMETER__GENERIC_ELEMENT, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setGenericElement(GenericElement newGenericElement) {

		if (newGenericElement != eInternalContainer()
				|| (eContainerFeatureID() != PivotModelPackage.TYPE_PARAMETER__GENERIC_ELEMENT && newGenericElement != null)) {
			if (EcoreUtil.isAncestor(this, newGenericElement))
				throw new IllegalArgumentException(
						"Recursive containment not allowed for " + toString()); //$NON-NLS-1$
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newGenericElement != null)
				msgs =
						((InternalEObject) newGenericElement).eInverseAdd(this,
								PivotModelPackage.GENERIC_ELEMENT__OWNED_TYPE_PARAMETER,
								GenericElement.class, msgs);
			msgs = basicSetGenericElement(newGenericElement, msgs);
			if (msgs != null)
				msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					PivotModelPackage.TYPE_PARAMETER__GENERIC_ELEMENT, newGenericElement,
					newGenericElement));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public TypeParameter clone() {

		return (TypeParameter) initialize(PivotModelFactory.eINSTANCE
				.createTypeParameter());
	}

	/**
	 * Returns the name of the {@link GenericElement} that owns this
	 * <code>TypeParameter</code>.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl#getOwner()
	 */
	@Override
	public NamedElement getOwner() {

		return getGenericElement();
	}

	/**
	 * Helper method that returns a name useful for identifying this
	 * <code>TypeParameter</code>. This method is used by {@link #equals(Object)}
	 * and {@link #hashCode()} when determining the equality with another
	 * <code>TypeParameter</code>. This is important in the context of
	 * {@link GenericElement#bindTypeParameter(java.util.List, java.util.List)
	 * binding} the type parameters of a {@link GenericElement generic element}.
	 * The name alone is not enough for this purpose, since two different type
	 * parameters may have the same name. However, the full
	 * {@link #getQualifiedName() qualified name} is not helpful either.
	 * 
	 * <p>
	 * To see why, consider that generic elements are cloned before they are
	 * bound. However, they are not added to the original element's namespace.
	 * Thus, their fully qualified name (and that of the owned
	 * <code>TypeParameter</code>s) will be different. During binding, this would
	 * prevent recognizing two <code>TypeParameter</code>s as equal and therefore
	 * hinder binding. Thus, changing the qualified name to only include the
	 * parent solves this problem.
	 * </p>
	 * 
	 * <p>
	 * Thus, this method returns a name that only includes the name of the owning
	 * {@link GenericElement} instead of the full namespace hierarchy. This should
	 * sufficiently identify any type parameter.
	 * </p>
	 */
	private String getIdentifyingName() {

		return getGenericElement() != null ? getGenericElement().getName() + "::" //$NON-NLS-1$
				+ getName() : getName();
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
		case PivotModelPackage.TYPE_PARAMETER__GENERIC_ELEMENT:
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			return basicSetGenericElement((GenericElement) otherEnd, msgs);
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
		case PivotModelPackage.TYPE_PARAMETER__GENERIC_ELEMENT:
			return basicSetGenericElement(null, msgs);
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
		case PivotModelPackage.TYPE_PARAMETER__GENERIC_ELEMENT:
			return eInternalContainer().eInverseRemove(this,
					PivotModelPackage.GENERIC_ELEMENT__OWNED_TYPE_PARAMETER,
					GenericElement.class, msgs);
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
		case PivotModelPackage.TYPE_PARAMETER__GENERIC_ELEMENT:
			return getGenericElement();
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
		case PivotModelPackage.TYPE_PARAMETER__GENERIC_ELEMENT:
			setGenericElement((GenericElement) newValue);
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
		case PivotModelPackage.TYPE_PARAMETER__GENERIC_ELEMENT:
			setGenericElement((GenericElement) null);
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
		case PivotModelPackage.TYPE_PARAMETER__GENERIC_ELEMENT:
			return getGenericElement() != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * Overridden to base the hash code on the name of this element and its owner
	 * only. See {@link #getIdentifyingName()} for more details.
	 * 
	 * @return a hash code for this type parameter
	 */
	@Override
	public int hashCode() {

		String identifyingName = getIdentifyingName();

		final int prime = 31;
		int result = 1;
		result =
				prime * result
						+ ((identifyingName == null) ? 0 : identifyingName.hashCode());
		return result;
	}

	/**
	 * Overridden so that type parameters are considered equal when their name and
	 * the name of their owning generic element are equal. See
	 * {@link #getIdentifyingName()} for more details.
	 * 
	 * @param obj
	 *          the other object
	 * 
	 * @return <code>true</code> if the other object is a
	 *         <code>TypeParameter</code> with the same name and equally-named
	 *         owner, <code>false</code> otherwise
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;

		if (!(obj instanceof TypeParameterImpl))
			return false;
		final TypeParameterImpl other = (TypeParameterImpl) obj;

		String identifyingName = getIdentifyingName();
		String otherIdentifyingName = other.getIdentifyingName();

		if (identifyingName == null) {
			if (otherIdentifyingName != null)
				return false;
		}

		else if (!identifyingName.equals(otherIdentifyingName))
			return false;

		return true;
	}

} // TypeParameterImpl
