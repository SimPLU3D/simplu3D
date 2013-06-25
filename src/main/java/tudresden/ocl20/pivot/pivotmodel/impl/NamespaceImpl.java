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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import tudresden.ocl20.pivot.pivotmodel.Constraint;
import tudresden.ocl20.pivot.pivotmodel.ConstraintKind;
import tudresden.ocl20.pivot.pivotmodel.Feature;
import tudresden.ocl20.pivot.pivotmodel.GenericElement;
import tudresden.ocl20.pivot.pivotmodel.NamedElement;
import tudresden.ocl20.pivot.pivotmodel.Namespace;
import tudresden.ocl20.pivot.pivotmodel.Operation;
import tudresden.ocl20.pivot.pivotmodel.PivotModelFactory;
import tudresden.ocl20.pivot.pivotmodel.PivotModelPackage;
import tudresden.ocl20.pivot.pivotmodel.Property;
import tudresden.ocl20.pivot.pivotmodel.Type;
import tudresden.ocl20.pivot.pivotmodel.TypeParameter;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Namespace</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link tudresden.ocl20.pivot.pivotmodel.impl.NamespaceImpl#getOwnedTypeParameter
 * <em>Owned Type Parameter</em>}</li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.impl.NamespaceImpl#getOwnedType
 * <em>Owned Type</em>}</li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.impl.NamespaceImpl#getOwnedRule
 * <em>Owned Rule</em>}</li>
 * <li>
 * {@link tudresden.ocl20.pivot.pivotmodel.impl.NamespaceImpl#getNestedNamespace
 * <em>Nested Namespace</em>}</li>
 * <li>
 * {@link tudresden.ocl20.pivot.pivotmodel.impl.NamespaceImpl#getNestingNamespace
 * <em>Nesting Namespace</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class NamespaceImpl extends NamedElementImpl implements Namespace {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(NamespaceImpl.class);

	/**
	 * The cached value of the '{@link #getOwnedTypeParameter()
	 * <em>Owned Type Parameter</em>}' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOwnedTypeParameter()
	 * @generated
	 * @ordered
	 */
	protected EList<TypeParameter> ownedTypeParameter;

	/**
	 * The cached value of the '{@link #getOwnedType() <em>Owned Type</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOwnedType()
	 * @generated
	 * @ordered
	 */
	protected EList<Type> ownedType;

	/**
	 * The cached value of the '{@link #getOwnedRule() <em>Owned Rule</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOwnedRule()
	 * @generated
	 * @ordered
	 */
	protected EList<Constraint> ownedRule;

	/**
	 * The cached value of the '{@link #getNestedNamespace()
	 * <em>Nested Namespace</em>}' containment reference list. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #getNestedNamespace()
	 * @generated
	 * @ordered
	 */
	protected EList<Namespace> nestedNamespace;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected NamespaceImpl() {

		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {

		return PivotModelPackage.Literals.NAMESPACE;
	}

	/**
	 * Overridden to return the {@link #getNestingNamespace() nesting namespace}
	 * of this <code>Namespace</code>.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl#getOwner()
	 */
	@Override
	public NamedElement getOwner() {

		return getNestingNamespace();
	}

	/**
	 * The implementation in this class simply redirects to {
	 * {@link #getOwnedTypeParameterGen()} which contains the code generated by
	 * EMF. Clients may, however, override this method to provide specific
	 * behaviour, e.g., adapt to other model repositories.
	 * 
	 * @generated NOT
	 */
	public List<TypeParameter> getOwnedTypeParameter() {

		return getOwnedTypeParameterGen();
	}

	/**
	 * <!-- begin-user-doc -->The code generated for {
	 * {@link #getOwnedTypeParameter} is redirected to this method.<!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected final List<TypeParameter> getOwnedTypeParameterGen() {

		if (ownedTypeParameter == null) {
			ownedTypeParameter =
					new EObjectContainmentWithInverseEList<TypeParameter>(
							TypeParameter.class, this,
							PivotModelPackage.NAMESPACE__OWNED_TYPE_PARAMETER,
							PivotModelPackage.TYPE_PARAMETER__GENERIC_ELEMENT);
		}
		return ownedTypeParameter;
	}

	/**
	 * The implementation in this class simply redirects to {
	 * {@link #getOwnedRuleGen()} which contains the code generated by EMF.
	 * Clients may, however, override this method to provide specific behaviour,
	 * e.g., adapt to other model repositories.
	 * 
	 * @generated NOT
	 */
	public List<Constraint> getOwnedRule() {

		return getOwnedRuleGen();
	}

	/**
	 * <!-- begin-user-doc -->The code generated for {{@link #getOwnedRule()} is
	 * redirected to this method.<!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected final List<Constraint> getOwnedRuleGen() {

		if (ownedRule == null) {
			ownedRule =
					new EObjectContainmentWithInverseEList<Constraint>(Constraint.class,
							this, PivotModelPackage.NAMESPACE__OWNED_RULE,
							PivotModelPackage.CONSTRAINT__NAMESPACE);
		}
		return ownedRule;
	}

	/**
	 * The implementation in this class simply redirects to {
	 * {@link #getOwnedTypeGen()} which contains the code generated by EMF.
	 * Clients may, however, override this method to provide specific behaviour,
	 * e.g., adapt to other model repositories.
	 * 
	 * @generated NOT
	 */
	public List<Type> getOwnedType() {

		return getOwnedTypeGen();
	}

	/**
	 * <!-- begin-user-doc -->The code generated for {{@link #getOwnedType()} is
	 * redirected to this method.<!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected final List<Type> getOwnedTypeGen() {

		if (ownedType == null) {
			ownedType =
					new EObjectContainmentWithInverseEList<Type>(Type.class, this,
							PivotModelPackage.NAMESPACE__OWNED_TYPE,
							PivotModelPackage.TYPE__NAMESPACE);
		}
		return ownedType;
	}

	/**
	 * The implementation in this class simply redirects to {
	 * {@link #getNestedNamespaceGen()} which contains the code generated by EMF.
	 * Clients may, however, override this method to provide specific behaviour,
	 * e.g., adapt to other model repositories.
	 * 
	 * @generated NOT
	 */
	public List<Namespace> getNestedNamespace() {

		return getNestedNamespaceGen();
	}

	/**
	 * <!-- begin-user-doc -->The code generated for {
	 * {@link #getNestedNamespace()} is redirected to this method.<!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected final List<Namespace> getNestedNamespaceGen() {

		if (nestedNamespace == null) {
			nestedNamespace =
					new EObjectContainmentWithInverseEList<Namespace>(Namespace.class,
							this, PivotModelPackage.NAMESPACE__NESTED_NAMESPACE,
							PivotModelPackage.NAMESPACE__NESTING_NAMESPACE);
		}
		return nestedNamespace;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Namespace getNestingNamespace() {

		if (eContainerFeatureID() != PivotModelPackage.NAMESPACE__NESTING_NAMESPACE)
			return null;
		return (Namespace) eContainer();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetNestingNamespace(
			Namespace newNestingNamespace, NotificationChain msgs) {

		msgs =
				eBasicSetContainer((InternalEObject) newNestingNamespace,
						PivotModelPackage.NAMESPACE__NESTING_NAMESPACE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setNestingNamespace(Namespace newNestingNamespace) {

		if (newNestingNamespace != eInternalContainer()
				|| (eContainerFeatureID() != PivotModelPackage.NAMESPACE__NESTING_NAMESPACE && newNestingNamespace != null)) {
			if (EcoreUtil.isAncestor(this, newNestingNamespace))
				throw new IllegalArgumentException(
						"Recursive containment not allowed for " + toString()); //$NON-NLS-1$
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newNestingNamespace != null)
				msgs =
						((InternalEObject) newNestingNamespace).eInverseAdd(this,
								PivotModelPackage.NAMESPACE__NESTED_NAMESPACE, Namespace.class,
								msgs);
			msgs = basicSetNestingNamespace(newNestingNamespace, msgs);
			if (msgs != null)
				msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					PivotModelPackage.NAMESPACE__NESTING_NAMESPACE, newNestingNamespace,
					newNestingNamespace));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public Namespace addType(Type type) {

		if (logger.isDebugEnabled()) {
			logger.debug("addType(type=" + type + ") - enter"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// use the generated method instead of the client-specific one
		getOwnedTypeGen().add(type);

		if (logger.isDebugEnabled()) {
			logger.debug("addType() - exit"); //$NON-NLS-1$
		}

		return this;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public Namespace addRule(Constraint rule) {

		if (logger.isDebugEnabled()) {
			logger.debug("addRule(rule=" + rule + ") - enter"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// use the generated method instead of the client-specific one
		getOwnedRuleGen().add(rule);

		if (logger.isDebugEnabled()) {
			logger.debug("addRule() - exit"); //$NON-NLS-1$
		}

		return this;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public Namespace addNestedNamespace(Namespace namespace) {

		if (logger.isDebugEnabled()) {
			logger.debug("addNestedNamespace(namespace=" + namespace + ") - enter"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// use the generated method instead of the client-specific one
		getNestedNamespaceGen().add(namespace);

		if (logger.isDebugEnabled()) {
			logger.debug("addNestedNamespace() - exit"); //$NON-NLS-1$
		}

		return this;
	}

	/**
	 * (non-JavaDoc)
	 * 
	 * @see Namespace#getOwnedAndNestedRules()
	 * 
	 * @generated NOT
	 */
	public List<Constraint> getOwnedAndNestedRules() {

		List<Constraint> result;

		result = new ArrayList<Constraint>();

		/* Collect the constraints of all nested name spaces. */
		for (Namespace aNestedNamespace : this.getNestedNamespace()) {
			result.addAll(aNestedNamespace.getOwnedAndNestedRules());
		}

		result.addAll(this.getOwnedRule());

		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public Type lookupType(String name) {

		if (logger.isDebugEnabled()) {
			logger.debug("lookupType(name=" + name + ") - enter"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		Type type = null;

		for (Type ownedType : getOwnedType()) {
			if (ownedType.getName().equals(name)) {
				type = ownedType;
				break;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("lookupType() - exit - return value=" + type); //$NON-NLS-1$
		}

		return type;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public Namespace lookupNamespace(String name) {

		if (logger.isDebugEnabled()) {
			logger.debug("lookupNamespace(name=" + name + ") - enter"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		Namespace namespace = null;

		for (Namespace nestedNamespace : getNestedNamespace()) {
			if (nestedNamespace.getName().equals(name)) {
				namespace = nestedNamespace;
				break;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("lookupNamespace() - exit - return value=" + namespace); //$NON-NLS-1$
		}

		return namespace;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public boolean removeOwnedAndNestedRules() {

		boolean result;
		result = true;

		/* Collect the constraints of all nested name spaces. */
		for (Namespace nestedNamespace : this.getNestedNamespace()) {
			result &= nestedNamespace.removeOwnedAndNestedRules();
		}
		// end for.

		/* Probably remove defined features as well. */
		for (Constraint constraint : this.ownedRule) {

			if (constraint.getKind() == ConstraintKind.DEFINITION) {

				Feature feature;
				feature = constraint.getDefinedFeature();

				if (feature instanceof Operation) {
					Operation operation;
					operation = (Operation) feature;

					((Type) operation.getOwner()).removeOperation(operation);
				}

				else if (feature instanceof Property) {
					Property property;
					property = (Property) feature;

					((Type) property.getOwner()).removeProperty(property);
				}
				// no else.
			}
			// no else (remove only defined features).
		}
		// no else.

		this.ownedRule.clear();
		result &= this.ownedRule.isEmpty();

		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public boolean removeOwnedAndNestedRules(List<Constraint> constraints) {

		boolean result;
		result = false;

		/* Collect the constraints of all nested name spaces. */
		for (Namespace nestedNamespace : this.getNestedNamespace()) {
			result |= nestedNamespace.removeOwnedAndNestedRules(constraints);
		}
		// end for.

		if (this.ownedRule != null) {
			/* Probably remove defined features. */
			for (Constraint constraint : constraints) {

				if (this.ownedRule.contains(constraint)
						&& constraint.getKind() == ConstraintKind.DEFINITION) {

					Feature feature;
					feature = constraint.getDefinedFeature();

					if (feature instanceof Operation) {
						Operation operation;
						operation = (Operation) feature;

						((Type) operation.getOwner()).removeOperation(operation);
					}

					else if (feature instanceof Property) {
						Property property;
						property = (Property) feature;

						((Type) property.getOwner()).removeProperty(property);
					}
					// no else.
				}
				// no else (remove only defined features).

			}
			// no else.

			/*
			 * Results in true if at least one constraint has been removed. Same
			 * semantic as Collection.removeAll()
			 */
			result |= this.ownedRule.removeAll(constraints);
		}
		// no else.

		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public Namespace addTypeParameter(TypeParameter typeParameter) {

		if (logger.isDebugEnabled()) {
			logger
					.debug("addTypeParameter(typeParameter=" + typeParameter + ") - enter"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// use the generated method instead of the client-specific one
		getOwnedTypeParameterGen().add(typeParameter);

		if (logger.isDebugEnabled()) {
			logger.debug("addTypeParameter() - exit"); //$NON-NLS-1$
		}

		return this;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NamedElement bindTypeParameter(List<TypeParameter> parameters,
			List<? extends Type> types) {

		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl#clone()
	 */
	@Override
	public Namespace clone() {

		return initialize(PivotModelFactory.eINSTANCE.createNamespace());
	}

	/**
	 * Helper method that initializes this <code>Namespace</code>.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl#initialize(tudresden.ocl20.pivot.pivotmodel.NamedElement)
	 */
	protected Namespace initialize(Namespace clone) {

		super.initialize(clone);

		// clone all owned elements
		for (TypeParameter typeParameter : getOwnedTypeParameter()) {
			clone.addTypeParameter(typeParameter.clone());
		}

		for (Constraint constraint : getOwnedRule()) {
			clone.addRule(constraint.clone());
		}

		for (Type type : getOwnedType()) {
			clone.addType(type.clone());
		}

		for (Namespace namespace : getNestedNamespace()) {
			clone.addNestedNamespace(namespace.clone());
		}

		return clone;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID,
			NotificationChain msgs) {

		switch (featureID) {
		case PivotModelPackage.NAMESPACE__OWNED_TYPE_PARAMETER:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getOwnedTypeParameter())
					.basicAdd(otherEnd, msgs);
		case PivotModelPackage.NAMESPACE__OWNED_TYPE:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getOwnedType())
					.basicAdd(otherEnd, msgs);
		case PivotModelPackage.NAMESPACE__OWNED_RULE:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getOwnedRule())
					.basicAdd(otherEnd, msgs);
		case PivotModelPackage.NAMESPACE__NESTED_NAMESPACE:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getNestedNamespace())
					.basicAdd(otherEnd, msgs);
		case PivotModelPackage.NAMESPACE__NESTING_NAMESPACE:
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			return basicSetNestingNamespace((Namespace) otherEnd, msgs);
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
		case PivotModelPackage.NAMESPACE__OWNED_TYPE_PARAMETER:
			return ((InternalEList<?>) getOwnedTypeParameter()).basicRemove(otherEnd,
					msgs);
		case PivotModelPackage.NAMESPACE__OWNED_TYPE:
			return ((InternalEList<?>) getOwnedType()).basicRemove(otherEnd, msgs);
		case PivotModelPackage.NAMESPACE__OWNED_RULE:
			return ((InternalEList<?>) getOwnedRule()).basicRemove(otherEnd, msgs);
		case PivotModelPackage.NAMESPACE__NESTED_NAMESPACE:
			return ((InternalEList<?>) getNestedNamespace()).basicRemove(otherEnd,
					msgs);
		case PivotModelPackage.NAMESPACE__NESTING_NAMESPACE:
			return basicSetNestingNamespace(null, msgs);
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
		case PivotModelPackage.NAMESPACE__NESTING_NAMESPACE:
			return eInternalContainer().eInverseRemove(this,
					PivotModelPackage.NAMESPACE__NESTED_NAMESPACE, Namespace.class, msgs);
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
		case PivotModelPackage.NAMESPACE__OWNED_TYPE_PARAMETER:
			return getOwnedTypeParameter();
		case PivotModelPackage.NAMESPACE__OWNED_TYPE:
			return getOwnedType();
		case PivotModelPackage.NAMESPACE__OWNED_RULE:
			return getOwnedRule();
		case PivotModelPackage.NAMESPACE__NESTED_NAMESPACE:
			return getNestedNamespace();
		case PivotModelPackage.NAMESPACE__NESTING_NAMESPACE:
			return getNestingNamespace();
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
		case PivotModelPackage.NAMESPACE__OWNED_TYPE_PARAMETER:
			getOwnedTypeParameter().clear();
			getOwnedTypeParameter().addAll(
					(Collection<? extends TypeParameter>) newValue);
			return;
		case PivotModelPackage.NAMESPACE__OWNED_TYPE:
			getOwnedType().clear();
			getOwnedType().addAll((Collection<? extends Type>) newValue);
			return;
		case PivotModelPackage.NAMESPACE__OWNED_RULE:
			getOwnedRule().clear();
			getOwnedRule().addAll((Collection<? extends Constraint>) newValue);
			return;
		case PivotModelPackage.NAMESPACE__NESTED_NAMESPACE:
			getNestedNamespace().clear();
			getNestedNamespace().addAll((Collection<? extends Namespace>) newValue);
			return;
		case PivotModelPackage.NAMESPACE__NESTING_NAMESPACE:
			setNestingNamespace((Namespace) newValue);
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
		case PivotModelPackage.NAMESPACE__OWNED_TYPE_PARAMETER:
			getOwnedTypeParameter().clear();
			return;
		case PivotModelPackage.NAMESPACE__OWNED_TYPE:
			getOwnedType().clear();
			return;
		case PivotModelPackage.NAMESPACE__OWNED_RULE:
			getOwnedRule().clear();
			return;
		case PivotModelPackage.NAMESPACE__NESTED_NAMESPACE:
			getNestedNamespace().clear();
			return;
		case PivotModelPackage.NAMESPACE__NESTING_NAMESPACE:
			setNestingNamespace((Namespace) null);
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
		case PivotModelPackage.NAMESPACE__OWNED_TYPE_PARAMETER:
			return ownedTypeParameter != null && !ownedTypeParameter.isEmpty();
		case PivotModelPackage.NAMESPACE__OWNED_TYPE:
			return ownedType != null && !ownedType.isEmpty();
		case PivotModelPackage.NAMESPACE__OWNED_RULE:
			return ownedRule != null && !ownedRule.isEmpty();
		case PivotModelPackage.NAMESPACE__NESTED_NAMESPACE:
			return nestedNamespace != null && !nestedNamespace.isEmpty();
		case PivotModelPackage.NAMESPACE__NESTING_NAMESPACE:
			return getNestingNamespace() != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {

		if (baseClass == GenericElement.class) {
			switch (derivedFeatureID) {
			case PivotModelPackage.NAMESPACE__OWNED_TYPE_PARAMETER:
				return PivotModelPackage.GENERIC_ELEMENT__OWNED_TYPE_PARAMETER;
			default:
				return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {

		if (baseClass == GenericElement.class) {
			switch (baseFeatureID) {
			case PivotModelPackage.GENERIC_ELEMENT__OWNED_TYPE_PARAMETER:
				return PivotModelPackage.NAMESPACE__OWNED_TYPE_PARAMETER;
			default:
				return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

} // NamespaceImpl
