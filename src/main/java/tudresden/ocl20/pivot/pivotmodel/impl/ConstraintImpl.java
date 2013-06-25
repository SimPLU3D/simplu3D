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

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectEList;
import org.eclipse.emf.ecore.util.EcoreUtil;

import tudresden.ocl20.pivot.pivotmodel.ConstrainableElement;
import tudresden.ocl20.pivot.pivotmodel.Constraint;
import tudresden.ocl20.pivot.pivotmodel.ConstraintKind;
import tudresden.ocl20.pivot.pivotmodel.Expression;
import tudresden.ocl20.pivot.pivotmodel.Feature;
import tudresden.ocl20.pivot.pivotmodel.NamedElement;
import tudresden.ocl20.pivot.pivotmodel.Namespace;
import tudresden.ocl20.pivot.pivotmodel.PivotModelFactory;
import tudresden.ocl20.pivot.pivotmodel.PivotModelPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Constraint</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.impl.ConstraintImpl#getKind <em>
 * Kind</em>}</li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.impl.ConstraintImpl#getNamespace
 * <em>Namespace</em>}</li>
 * <li>
 * {@link tudresden.ocl20.pivot.pivotmodel.impl.ConstraintImpl#getSpecification
 * <em>Specification</em>}</li>
 * <li>
 * {@link tudresden.ocl20.pivot.pivotmodel.impl.ConstraintImpl#getConstrainedElement
 * <em>Constrained Element</em>}</li>
 * <li>
 * {@link tudresden.ocl20.pivot.pivotmodel.impl.ConstraintImpl#getDefinedFeature
 * <em>Defined Feature</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ConstraintImpl extends NamedElementImpl implements Constraint {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ConstraintImpl.class);

	/**
	 * The default value of the '{@link #getKind() <em>Kind</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected static final ConstraintKind KIND_EDEFAULT =
			ConstraintKind.INVARIANT;

	/**
	 * The cached value of the '{@link #getKind() <em>Kind</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected ConstraintKind kind = KIND_EDEFAULT;

	/**
	 * The cached value of the '{@link #getSpecification() <em>Specification</em>}
	 * ' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSpecification()
	 * @generated
	 * @ordered
	 */
	protected Expression specification;

	/**
	 * The cached value of the '{@link #getConstrainedElement()
	 * <em>Constrained Element</em>}' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getConstrainedElement()
	 * @generated
	 * @ordered
	 */
	protected EList<ConstrainableElement> constrainedElement;

	/**
	 * The cached value of the '{@link #getDefinedFeature()
	 * <em>Defined Feature</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getDefinedFeature()
	 * @generated
	 * @ordered
	 */
	protected Feature definedFeature;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ConstraintImpl() {

		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {

		return PivotModelPackage.Literals.CONSTRAINT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ConstraintKind getKind() {

		return kind;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setKind(ConstraintKind newKind) {

		ConstraintKind oldKind = kind;
		kind = newKind == null ? KIND_EDEFAULT : newKind;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					PivotModelPackage.CONSTRAINT__KIND, oldKind, kind));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Namespace getNamespace() {

		if (eContainerFeatureID() != PivotModelPackage.CONSTRAINT__NAMESPACE)
			return null;
		return (Namespace) eContainer();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetNamespace(Namespace newNamespace,
			NotificationChain msgs) {

		msgs =
				eBasicSetContainer((InternalEObject) newNamespace,
						PivotModelPackage.CONSTRAINT__NAMESPACE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setNamespace(Namespace newNamespace) {

		if (newNamespace != eInternalContainer()
				|| (eContainerFeatureID() != PivotModelPackage.CONSTRAINT__NAMESPACE && newNamespace != null)) {
			if (EcoreUtil.isAncestor(this, newNamespace))
				throw new IllegalArgumentException(
						"Recursive containment not allowed for " + toString()); //$NON-NLS-1$
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newNamespace != null)
				msgs =
						((InternalEObject) newNamespace).eInverseAdd(this,
								PivotModelPackage.NAMESPACE__OWNED_RULE, Namespace.class, msgs);
			msgs = basicSetNamespace(newNamespace, msgs);
			if (msgs != null)
				msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					PivotModelPackage.CONSTRAINT__NAMESPACE, newNamespace, newNamespace));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Expression getSpecification() {

		return specification;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetSpecification(Expression newSpecification,
			NotificationChain msgs) {

		Expression oldSpecification = specification;
		specification = newSpecification;
		if (eNotificationRequired()) {
			ENotificationImpl notification =
					new ENotificationImpl(this, Notification.SET,
							PivotModelPackage.CONSTRAINT__SPECIFICATION, oldSpecification,
							newSpecification);
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
	public void setSpecification(Expression newSpecification) {

		if (newSpecification != specification) {
			NotificationChain msgs = null;
			if (specification != null)
				msgs =
						((InternalEObject) specification).eInverseRemove(this,
								PivotModelPackage.EXPRESSION__CONSTRAINT, Expression.class,
								msgs);
			if (newSpecification != null)
				msgs =
						((InternalEObject) newSpecification).eInverseAdd(this,
								PivotModelPackage.EXPRESSION__CONSTRAINT, Expression.class,
								msgs);
			msgs = basicSetSpecification(newSpecification, msgs);
			if (msgs != null)
				msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					PivotModelPackage.CONSTRAINT__SPECIFICATION, newSpecification,
					newSpecification));
	}

	/**
	 * The implementation in this class simply redirects to {
	 * {@link #getConstrainedElementGen()} which contains the code generated by
	 * EMF. Client may, however, override this method to provide specific
	 * behaviour, e.g., adapt to other model repositories.
	 * 
	 * @generated NOT
	 */
	public List<ConstrainableElement> getConstrainedElement() {

		return getConstrainedElementGen();
	}

	/**
	 * <!-- begin-user-doc -->The code generated for {
	 * {@link #getConstrainedElement()} is redirected to this method.<!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected final List<ConstrainableElement> getConstrainedElementGen() {

		if (constrainedElement == null) {
			constrainedElement =
					new EObjectEList<ConstrainableElement>(ConstrainableElement.class,
							this, PivotModelPackage.CONSTRAINT__CONSTRAINED_ELEMENT);
		}
		return constrainedElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Feature getDefinedFeature() {

		return definedFeature;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetDefinedFeature(Feature newDefinedFeature,
			NotificationChain msgs) {

		Feature oldDefinedFeature = definedFeature;
		definedFeature = newDefinedFeature;
		if (eNotificationRequired()) {
			ENotificationImpl notification =
					new ENotificationImpl(this, Notification.SET,
							PivotModelPackage.CONSTRAINT__DEFINED_FEATURE, oldDefinedFeature,
							newDefinedFeature);
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
	public void setDefinedFeature(Feature newDefinedFeature) {

		if (newDefinedFeature != definedFeature) {
			NotificationChain msgs = null;
			if (definedFeature != null)
				msgs =
						((InternalEObject) definedFeature).eInverseRemove(this,
								PivotModelPackage.FEATURE__SEMANTICS, Feature.class, msgs);
			if (newDefinedFeature != null)
				msgs =
						((InternalEObject) newDefinedFeature).eInverseAdd(this,
								PivotModelPackage.FEATURE__SEMANTICS, Feature.class, msgs);
			msgs = basicSetDefinedFeature(newDefinedFeature, msgs);
			if (msgs != null)
				msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					PivotModelPackage.CONSTRAINT__DEFINED_FEATURE, newDefinedFeature,
					newDefinedFeature));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public Constraint addConstrainedElement(
			ConstrainableElement constrainedElement) {

		if (logger.isDebugEnabled()) {
			logger
					.debug("addConstrainedElement(constrainedElement=" + constrainedElement + ") - enter"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// use the generated method, not the one which may be overridden by clients
		getConstrainedElementGen().add(constrainedElement);

		if (logger.isDebugEnabled()) {
			logger.debug("addConstrainedElement() - exit"); //$NON-NLS-1$
		}

		return this;
	}

	/**
	 * Overridden to return the {@link #getNamespace() namespace} of this
	 * <code>Constraint</code>.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl#getOwner()
	 */
	@Override
	public NamedElement getOwner() {

		return getNamespace();
	}

	/*
	 * (non-Javadoc)
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl#clone()
	 */

	@Override
	public Constraint clone() {

		return initialize(PivotModelFactory.eINSTANCE.createConstraint());
	}

	/**
	 * Helper method that initializes a cloned <code>Constraint</code>.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl#initialize(tudresden.ocl20.pivot.pivotmodel.NamedElement)
	 */
	protected Constraint initialize(Constraint clone) {

		super.initialize(clone);

		clone.setKind(getKind());
		clone.setSpecification(getSpecification());
		clone.setDefinedFeature(getDefinedFeature());

		// copy the constrained elements
		for (ConstrainableElement constrainedElement : getConstrainedElement()) {
			// do not clone the elements as they are only referenced, not contained by
			// the constraint
			clone.addConstrainedElement(constrainedElement);
		}

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
		case PivotModelPackage.CONSTRAINT__NAMESPACE:
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			return basicSetNamespace((Namespace) otherEnd, msgs);
		case PivotModelPackage.CONSTRAINT__SPECIFICATION:
			if (specification != null)
				msgs =
						((InternalEObject) specification).eInverseRemove(this,
								EOPPOSITE_FEATURE_BASE
										- PivotModelPackage.CONSTRAINT__SPECIFICATION, null, msgs);
			return basicSetSpecification((Expression) otherEnd, msgs);
		case PivotModelPackage.CONSTRAINT__DEFINED_FEATURE:
			if (definedFeature != null)
				msgs =
						((InternalEObject) definedFeature).eInverseRemove(this,
								PivotModelPackage.FEATURE__SEMANTICS, Feature.class, msgs);
			return basicSetDefinedFeature((Feature) otherEnd, msgs);
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
		case PivotModelPackage.CONSTRAINT__NAMESPACE:
			return basicSetNamespace(null, msgs);
		case PivotModelPackage.CONSTRAINT__SPECIFICATION:
			return basicSetSpecification(null, msgs);
		case PivotModelPackage.CONSTRAINT__DEFINED_FEATURE:
			return basicSetDefinedFeature(null, msgs);
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
		case PivotModelPackage.CONSTRAINT__NAMESPACE:
			return eInternalContainer().eInverseRemove(this,
					PivotModelPackage.NAMESPACE__OWNED_RULE, Namespace.class, msgs);
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
		case PivotModelPackage.CONSTRAINT__KIND:
			return getKind();
		case PivotModelPackage.CONSTRAINT__NAMESPACE:
			return getNamespace();
		case PivotModelPackage.CONSTRAINT__SPECIFICATION:
			return getSpecification();
		case PivotModelPackage.CONSTRAINT__CONSTRAINED_ELEMENT:
			return getConstrainedElement();
		case PivotModelPackage.CONSTRAINT__DEFINED_FEATURE:
			return getDefinedFeature();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {

		switch (featureID) {
		case PivotModelPackage.CONSTRAINT__KIND:
			setKind((ConstraintKind) newValue);
			return;
		case PivotModelPackage.CONSTRAINT__NAMESPACE:
			setNamespace((Namespace) newValue);
			return;
		case PivotModelPackage.CONSTRAINT__SPECIFICATION:
			setSpecification((Expression) newValue);
			return;
		case PivotModelPackage.CONSTRAINT__CONSTRAINED_ELEMENT:
			getConstrainedElement().clear();
			getConstrainedElement().addAll(
					(Collection<? extends ConstrainableElement>) newValue);
			return;
		case PivotModelPackage.CONSTRAINT__DEFINED_FEATURE:
			setDefinedFeature((Feature) newValue);
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
		case PivotModelPackage.CONSTRAINT__KIND:
			setKind(KIND_EDEFAULT);
			return;
		case PivotModelPackage.CONSTRAINT__NAMESPACE:
			setNamespace((Namespace) null);
			return;
		case PivotModelPackage.CONSTRAINT__SPECIFICATION:
			setSpecification((Expression) null);
			return;
		case PivotModelPackage.CONSTRAINT__CONSTRAINED_ELEMENT:
			getConstrainedElement().clear();
			return;
		case PivotModelPackage.CONSTRAINT__DEFINED_FEATURE:
			setDefinedFeature((Feature) null);
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
		case PivotModelPackage.CONSTRAINT__KIND:
			return kind != KIND_EDEFAULT;
		case PivotModelPackage.CONSTRAINT__NAMESPACE:
			return getNamespace() != null;
		case PivotModelPackage.CONSTRAINT__SPECIFICATION:
			return specification != null;
		case PivotModelPackage.CONSTRAINT__CONSTRAINED_ELEMENT:
			return constrainedElement != null && !constrainedElement.isEmpty();
		case PivotModelPackage.CONSTRAINT__DEFINED_FEATURE:
			return definedFeature != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * Changed EMF implementation to use the Jakarta Commons Lang
	 * {@link ToStringBuilder}.
	 * 
	 * @generated NOT
	 */
	@Override
	public String toString() {

		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.appendToString(super.toString())
				.append("kind", getKind()).append("specification", getSpecification()) //$NON-NLS-1$ //$NON-NLS-2$
				.append("constrainedElement", getConstrainedElement()).append("definedFeature", //$NON-NLS-1$ //$NON-NLS-2$
						getDefinedFeature()).toString();
	}

} // ConstraintImpl
