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

import tudresden.ocl20.pivot.pivotmodel.Constraint;
import tudresden.ocl20.pivot.pivotmodel.Feature;
import tudresden.ocl20.pivot.pivotmodel.PivotModelPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Feature</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.impl.FeatureImpl#isStatic <em>
 * Static</em>}</li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.impl.FeatureImpl#getSemantics
 * <em>Semantics</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public abstract class FeatureImpl extends TypedElementImpl implements Feature {

	/**
	 * The default value of the '{@link #isStatic() <em>Static</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isStatic()
	 * @generated
	 * @ordered
	 */
	protected static final boolean STATIC_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isStatic() <em>Static</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isStatic()
	 * @generated
	 * @ordered
	 */
	protected boolean static_ = STATIC_EDEFAULT;

	/**
	 * The cached value of the '{@link #getSemantics() <em>Semantics</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSemantics()
	 * @generated
	 * @ordered
	 */
	protected Constraint semantics;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected FeatureImpl() {

		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {

		return PivotModelPackage.Literals.FEATURE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isStatic() {

		return static_;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setStatic(boolean newStatic) {

		boolean oldStatic = static_;
		static_ = newStatic;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					PivotModelPackage.FEATURE__STATIC, oldStatic, static_));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Constraint getSemantics() {

		if (semantics != null && semantics.eIsProxy()) {
			InternalEObject oldSemantics = (InternalEObject) semantics;
			semantics = (Constraint) eResolveProxy(oldSemantics);
			if (semantics != oldSemantics) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							PivotModelPackage.FEATURE__SEMANTICS, oldSemantics, semantics));
			}
		}
		return semantics;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Constraint basicGetSemantics() {

		return semantics;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetSemantics(Constraint newSemantics,
			NotificationChain msgs) {

		Constraint oldSemantics = semantics;
		semantics = newSemantics;
		if (eNotificationRequired()) {
			ENotificationImpl notification =
					new ENotificationImpl(this, Notification.SET,
							PivotModelPackage.FEATURE__SEMANTICS, oldSemantics, newSemantics);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setSemantics(Constraint newSemantics) {

		if (newSemantics != semantics) {
			NotificationChain msgs = null;
			if (semantics != null)
				msgs =
						((InternalEObject) semantics).eInverseRemove(this,
								PivotModelPackage.CONSTRAINT__DEFINED_FEATURE,
								Constraint.class, msgs);
			if (newSemantics != null)
				msgs =
						((InternalEObject) newSemantics).eInverseAdd(this,
								PivotModelPackage.CONSTRAINT__DEFINED_FEATURE,
								Constraint.class, msgs);
			msgs = basicSetSemantics(newSemantics, msgs);
			if (msgs != null)
				msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					PivotModelPackage.FEATURE__SEMANTICS, newSemantics, newSemantics));
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
		case PivotModelPackage.FEATURE__SEMANTICS:
			if (semantics != null)
				msgs =
						((InternalEObject) semantics).eInverseRemove(this,
								PivotModelPackage.CONSTRAINT__DEFINED_FEATURE,
								Constraint.class, msgs);
			return basicSetSemantics((Constraint) otherEnd, msgs);
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
		case PivotModelPackage.FEATURE__SEMANTICS:
			return basicSetSemantics(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * Convenience method for subclasses that initializes the properties and
	 * references of a cloned <code>Feature</code>.
	 */
	protected Feature initialize(Feature clone) {

		super.initialize(clone);
		clone.setStatic(isStatic());
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
		case PivotModelPackage.FEATURE__STATIC:
			return isStatic();
		case PivotModelPackage.FEATURE__SEMANTICS:
			if (resolve)
				return getSemantics();
			return basicGetSemantics();
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
		case PivotModelPackage.FEATURE__STATIC:
			setStatic((Boolean) newValue);
			return;
		case PivotModelPackage.FEATURE__SEMANTICS:
			setSemantics((Constraint) newValue);
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
		case PivotModelPackage.FEATURE__STATIC:
			setStatic(STATIC_EDEFAULT);
			return;
		case PivotModelPackage.FEATURE__SEMANTICS:
			setSemantics((Constraint) null);
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
		case PivotModelPackage.FEATURE__STATIC:
			return static_ != STATIC_EDEFAULT;
		case PivotModelPackage.FEATURE__SEMANTICS:
			return semantics != null;
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
				.appendToString(super.toString())
				.append("static", isStatic()).toString(); //$NON-NLS-1$
	}

} // FeatureImpl
