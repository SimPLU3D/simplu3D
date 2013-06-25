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
import org.eclipse.emf.ecore.util.EcoreUtil;

import tudresden.ocl20.pivot.pivotmodel.GenericType;
import tudresden.ocl20.pivot.pivotmodel.NamedElement;
import tudresden.ocl20.pivot.pivotmodel.Operation;
import tudresden.ocl20.pivot.pivotmodel.Parameter;
import tudresden.ocl20.pivot.pivotmodel.ParameterDirectionKind;
import tudresden.ocl20.pivot.pivotmodel.PivotModelFactory;
import tudresden.ocl20.pivot.pivotmodel.PivotModelPackage;
import tudresden.ocl20.pivot.pivotmodel.Property;
import tudresden.ocl20.pivot.pivotmodel.Type;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Parameter</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.impl.ParameterImpl#getKind <em>
 * Kind</em>}</li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.impl.ParameterImpl#getOperation
 * <em>Operation</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ParameterImpl extends TypedElementImpl implements Parameter {

	/**
	 * The default value of the '{@link #getKind() <em>Kind</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected static final ParameterDirectionKind KIND_EDEFAULT =
			ParameterDirectionKind.IN;

	/**
	 * The cached value of the '{@link #getKind() <em>Kind</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected ParameterDirectionKind kind = KIND_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ParameterImpl() {

		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {

		return PivotModelPackage.Literals.PARAMETER;
	}

	/**
	 * Overridden to change the type of the owning {@link Operation} if the type
	 * of a return parameter is changed.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.TypedElementImpl#setType(tudresden.ocl20.pivot.pivotmodel.Type)
	 */
	@Override
	public void setType(Type newType) {

		super.setType(newType);

		// change the operation type if necessary
		if (kind == ParameterDirectionKind.RETURN) {
			Operation operation = getOperation();

			if (operation != null) {
				Type operationType = operation.getType();

				if ((operationType == null && newType != null)
						|| (operationType != null && !operationType.equals(newType))) {
					operation.setType(newType);
				}
			}
		}
	}

	/**
	 * Overridden to change the generic type of the owning {@link Operation} if
	 * the generic type of a return parameter is changed.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.TypedElementImpl#setGenericType(tudresden.ocl20.pivot.pivotmodel.GenericType)
	 */
	@Override
	public void setGenericType(GenericType newGenericType) {

		super.setGenericType(newGenericType);

		// change the operation generic type if necessary
		if (kind == ParameterDirectionKind.RETURN) {
			Operation operation = getOperation();

			if (operation != null) {
				GenericType operationGenericType = operation.getGenericType();

				if ((operationGenericType == null && newGenericType != null)
						|| (operationGenericType != null && !operationGenericType
								.equals(newGenericType))) {
					operation.setGenericType(operationGenericType);
				}
			}
		}
	}

	/**
	 * Overridden to return the {@link #getOperation() operation} that contains
	 * this <code>Parameter</code>.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl#getOwner()
	 */
	@Override
	public NamedElement getOwner() {

		return getOperation();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ParameterDirectionKind getKind() {

		return kind;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setKind(ParameterDirectionKind newKind) {

		ParameterDirectionKind oldKind = kind;
		kind = newKind == null ? KIND_EDEFAULT : newKind;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					PivotModelPackage.PARAMETER__KIND, oldKind, kind));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Operation getOperation() {

		if (eContainerFeatureID() != PivotModelPackage.PARAMETER__OPERATION)
			return null;
		return (Operation) eContainer();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetOperation(Operation newOperation,
			NotificationChain msgs) {

		msgs =
				eBasicSetContainer((InternalEObject) newOperation,
						PivotModelPackage.PARAMETER__OPERATION, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setOperation(Operation newOperation) {

		if (newOperation != eInternalContainer()
				|| (eContainerFeatureID() != PivotModelPackage.PARAMETER__OPERATION && newOperation != null)) {
			if (EcoreUtil.isAncestor(this, newOperation))
				throw new IllegalArgumentException(
						"Recursive containment not allowed for " + toString()); //$NON-NLS-1$
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newOperation != null)
				msgs =
						((InternalEObject) newOperation).eInverseAdd(this,
								PivotModelPackage.OPERATION__OWNED_PARAMETER, Operation.class,
								msgs);
			msgs = basicSetOperation(newOperation, msgs);
			if (msgs != null)
				msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					PivotModelPackage.PARAMETER__OPERATION, newOperation, newOperation));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public Property asProperty() {

		Property property;

		// create a new empty property
		property = PivotModelFactory.eINSTANCE.createProperty();

		// set properties
		property.setName(getName());
		property.setType(getType());

		return property;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public Parameter clone() {

		return initialize(PivotModelFactory.eINSTANCE.createParameter());
	}

	/**
	 * Helper method to initialize a cloned <code>Parameter</code>.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.TypedElementImpl#initialize(tudresden.ocl20.pivot.pivotmodel.TypedElement)
	 */
	protected Parameter initialize(Parameter clone) {

		super.initialize(clone);

		clone.setKind(getKind());

		return clone;
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
		case PivotModelPackage.PARAMETER__OPERATION:
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			return basicSetOperation((Operation) otherEnd, msgs);
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
		case PivotModelPackage.PARAMETER__OPERATION:
			return basicSetOperation(null, msgs);
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
		case PivotModelPackage.PARAMETER__OPERATION:
			return eInternalContainer().eInverseRemove(this,
					PivotModelPackage.OPERATION__OWNED_PARAMETER, Operation.class, msgs);
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
		case PivotModelPackage.PARAMETER__KIND:
			return getKind();
		case PivotModelPackage.PARAMETER__OPERATION:
			return getOperation();
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
		case PivotModelPackage.PARAMETER__KIND:
			setKind((ParameterDirectionKind) newValue);
			return;
		case PivotModelPackage.PARAMETER__OPERATION:
			setOperation((Operation) newValue);
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
		case PivotModelPackage.PARAMETER__KIND:
			setKind(KIND_EDEFAULT);
			return;
		case PivotModelPackage.PARAMETER__OPERATION:
			setOperation((Operation) null);
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
		case PivotModelPackage.PARAMETER__KIND:
			return kind != KIND_EDEFAULT;
		case PivotModelPackage.PARAMETER__OPERATION:
			return getOperation() != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * Changed EMF implementation in order to use a consistent style. In addition,
	 * the getter methods are used to get attribute values. This is important if
	 * repository-specific subclasses have alternative ways of obtaining their
	 * attribute values.
	 * 
	 * @generated NOT
	 */
	@Override
	public String toString() {

		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.appendToString(super.toString()).append("kind", kind).toString(); //$NON-NLS-1$
	}

} // ParameterImpl
