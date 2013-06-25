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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import tudresden.ocl20.pivot.pivotmodel.ConstrainableElement;
import tudresden.ocl20.pivot.pivotmodel.GenericElement;
import tudresden.ocl20.pivot.pivotmodel.GenericType;
import tudresden.ocl20.pivot.pivotmodel.NamedElement;
import tudresden.ocl20.pivot.pivotmodel.Namespace;
import tudresden.ocl20.pivot.pivotmodel.Operation;
import tudresden.ocl20.pivot.pivotmodel.PivotModelFactory;
import tudresden.ocl20.pivot.pivotmodel.PivotModelPackage;
import tudresden.ocl20.pivot.pivotmodel.Property;
import tudresden.ocl20.pivot.pivotmodel.Type;
import tudresden.ocl20.pivot.pivotmodel.TypeParameter;
import tudresden.ocl20.pivot.pivotmodel.util.ListUtil;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link tudresden.ocl20.pivot.pivotmodel.impl.TypeImpl#getOwnedTypeParameter
 * <em>Owned Type Parameter</em>}</li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.impl.TypeImpl#getNamespace <em>
 * Namespace</em>}</li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.impl.TypeImpl#getOwnedOperation
 * <em>Owned Operation</em>}</li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.impl.TypeImpl#getOwnedProperty
 * <em>Owned Property</em>}</li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.impl.TypeImpl#getSuperType <em>
 * Super Type</em>}</li>
 * <li>
 * {@link tudresden.ocl20.pivot.pivotmodel.impl.TypeImpl#getGenericSuperType
 * <em>Generic Super Type</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class TypeImpl extends NamedElementImpl implements Type {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TypeImpl.class);

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
	 * The cached value of the '{@link #getOwnedOperation()
	 * <em>Owned Operation</em>}' containment reference list. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #getOwnedOperation()
	 * @generated
	 * @ordered
	 */
	protected EList<Operation> ownedOperation;

	/**
	 * The cached value of the '{@link #getOwnedProperty()
	 * <em>Owned Property</em>}' containment reference list. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #getOwnedProperty()
	 * @generated
	 * @ordered
	 */
	protected EList<Property> ownedProperty;

	/**
	 * The cached value of the '{@link #getSuperType() <em>Super Type</em>}'
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSuperType()
	 * @generated
	 * @ordered
	 */
	protected EList<Type> superType;

	/**
	 * The cached value of the '{@link #getGenericSuperType()
	 * <em>Generic Super Type</em>}' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getGenericSuperType()
	 * @generated
	 * @ordered
	 */
	protected EList<GenericType> genericSuperType;

	/**
	 * A map that contains instances of this Type with some or all of their type
	 * parameters bound.
	 */
	private static Map<Binding, Type> boundTypes;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected TypeImpl() {

		super();
	}

	/**
	 * Overridden to return the {@link #getNamespace() namespace} of this
	 * <code>Type</code>.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl#getOwner()
	 */
	@Override
	public NamedElement getOwner() {

		return getNamespace();
	}

	/**
	 * The implementation in this class simply redirects to {
	 * {@link #getSuperTypeGen()} which contains the code generated by EMF. Client
	 * may, however, override this method to provide specific behaviour, e.g.,
	 * adapt to other model repositories.
	 * 
	 * @generated NOT
	 */
	public List<Type> getSuperType() {

		return getSuperTypeGen();
	}

	/**
	 * <!-- begin-user-doc -->The code generated for {{@link #getSuperType()} is
	 * redirected to this method. <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected final List<Type> getSuperTypeGen() {

		if (superType == null) {
			superType =
					new EObjectEList<Type>(Type.class, this,
							PivotModelPackage.TYPE__SUPER_TYPE);
		}
		return superType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public List<GenericType> getGenericSuperType() {

		if (genericSuperType == null) {
			genericSuperType =
					new EObjectContainmentEList<GenericType>(GenericType.class, this,
							PivotModelPackage.TYPE__GENERIC_SUPER_TYPE);
		}
		return genericSuperType;
	}

	/**
	 * The implementation in this class simply redirects to {
	 * {@link #getOwnedOperationGen()} which contains the code generated by EMF.
	 * Clients may, however, override this method to provide specific behaviour,
	 * e.g., adapt to other model repositories.
	 * 
	 * @generated NOT
	 */
	public List<Operation> getOwnedOperation() {

		return getOwnedOperationGen();
	}

	/**
	 * The code generated for {{@link #getOwnedOperation()} is redirected to this
	 * method.
	 * 
	 * @generated
	 */
	protected final List<Operation> getOwnedOperationGen() {

		if (ownedOperation == null) {
			ownedOperation =
					new EObjectContainmentWithInverseEList<Operation>(Operation.class,
							this, PivotModelPackage.TYPE__OWNED_OPERATION,
							PivotModelPackage.OPERATION__OWNING_TYPE);
		}
		return ownedOperation;
	}

	/**
	 * The implementation in this class simply redirects to {
	 * {@link #getOwnedPropertyGen()} which contains the code generated by EMF.
	 * Clients may, however, override this method to provide specific behaviour,
	 * e.g., adapt to other model repositories.
	 * 
	 * @generated NOT
	 */
	public List<Property> getOwnedProperty() {

		return getOwnedPropertyGen();
	}

	/**
	 * The code generated for {{@link #getOwnedProperty()} is redirected to this
	 * method.
	 * 
	 * @generated
	 */
	protected final List<Property> getOwnedPropertyGen() {

		if (ownedProperty == null) {
			ownedProperty =
					new EObjectContainmentWithInverseEList<Property>(Property.class,
							this, PivotModelPackage.TYPE__OWNED_PROPERTY,
							PivotModelPackage.PROPERTY__OWNING_TYPE);
		}
		return ownedProperty;
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
	 * {@link #getOwnedTypeParameterGen()} is redirected to this method.<!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected final List<TypeParameter> getOwnedTypeParameterGen() {

		if (ownedTypeParameter == null) {
			ownedTypeParameter =
					new EObjectContainmentWithInverseEList<TypeParameter>(
							TypeParameter.class, this,
							PivotModelPackage.TYPE__OWNED_TYPE_PARAMETER,
							PivotModelPackage.TYPE_PARAMETER__GENERIC_ELEMENT);
		}
		return ownedTypeParameter;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Namespace getNamespace() {

		if (eContainerFeatureID() != PivotModelPackage.TYPE__NAMESPACE)
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
						PivotModelPackage.TYPE__NAMESPACE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setNamespace(Namespace newNamespace) {

		if (newNamespace != eInternalContainer()
				|| (eContainerFeatureID() != PivotModelPackage.TYPE__NAMESPACE && newNamespace != null)) {
			if (EcoreUtil.isAncestor(this, newNamespace))
				throw new IllegalArgumentException(
						"Recursive containment not allowed for " + toString()); //$NON-NLS-1$
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newNamespace != null)
				msgs =
						((InternalEObject) newNamespace).eInverseAdd(this,
								PivotModelPackage.NAMESPACE__OWNED_TYPE, Namespace.class, msgs);
			msgs = basicSetNamespace(newNamespace, msgs);
			if (msgs != null)
				msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					PivotModelPackage.TYPE__NAMESPACE, newNamespace, newNamespace));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public boolean conformsTo(Type other) {

		if (logger.isDebugEnabled()) {
			logger.debug("conformsTo(other=" + other + ") - enter"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		boolean conformant;

		// by default a type does not conform to another
		conformant = false;

		// a type conforms to itself
		if (this.equals(other)) {
			conformant = true;
		}

		// a type conforms to another type if one if its supertypes conforms to
		// the
		// other type
		else {
			for (Type superType : getSuperType()) {
				conformant = superType.conformsTo(other);
				if (conformant)
					break;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("conformsTo() - exit - return value=" + conformant); //$NON-NLS-1$
		}

		return conformant;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public Type commonSuperType(Type other) {

		if (logger.isDebugEnabled()) {
			logger.debug("commonSuperType(other=" + other + ") - enter"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		if (other == null) {
			throw new NullArgumentException("other"); //$NON-NLS-1$
		}

		Type commonSuperType;

		// by default there is no common supertype
		commonSuperType = null;

		// check direct conformance of the two types
		if (this.conformsTo(other)) {
			commonSuperType = other;
		}

		else if (other.conformsTo(this)) {
			commonSuperType = this;
		}

		// else check inheritance hierachies of this and the other type
		else {
			// the super types in one inheritance level for this and the other
			// type,
			// respectively, and the transitive closure of all types in both
			// hierachies
			Set<Type> thisSuperTypes, otherSuperTypes, allThisTypes, allOtherTypes;

			// initialize
			thisSuperTypes = new HashSet<Type>();
			otherSuperTypes = new HashSet<Type>();
			allThisTypes = new HashSet<Type>();
			allOtherTypes = new HashSet<Type>();

			// add the parents of both types to the corresponding sets
			thisSuperTypes.addAll(this.getSuperType());
			otherSuperTypes.addAll(other.getSuperType());

			// go up both inheritance hierarchies to the top
			while (!(thisSuperTypes.isEmpty() && otherSuperTypes.isEmpty())) {
				Set<Type> temp = new HashSet<Type>();

				// add the current super types to the transitive closure of each
				// hierarchy
				allThisTypes.addAll(thisSuperTypes);
				allOtherTypes.addAll(otherSuperTypes);

				// check if one of this type's current supertypes is contained
				// in the
				// other type's hierarchy
				for (Type type : thisSuperTypes) {

					if (allOtherTypes.contains(type)) {
						commonSuperType = type;
						break;
					}

					// remember the types of the next hierarchy level
					temp.addAll(type.getSuperType());
				}

				// quit if we have found a super type
				if (commonSuperType != null) {
					break;
				}

				// save the next hierarchy level types for the next iteration
				thisSuperTypes.addAll(temp);
				temp.clear();

				// check if one of the other type's current supertypes is
				// contained in
				// this type's hierarchy
				for (Type type : otherSuperTypes) {

					if (allThisTypes.contains(type)) {
						commonSuperType = type;
						break;
					}

					// remember the types of the next hierarchy level
					temp.addAll(type.getSuperType());
				}

				// quit if we have found a super type
				if (commonSuperType != null) {
					break;
				}

				// save the next hierarchy level types for the next iteration
				otherSuperTypes.addAll(temp);
				temp.clear();
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("commonSuperType() - exit - return value=" + //$NON-NLS-1$
					commonSuperType);
		}

		return commonSuperType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public List<Property> allProperties() {

		List<Property> allProperties = new ArrayList<Property>();

		Set<String> visibleProperties = new HashSet<String>();
		Set<Type> typesToVisit = new HashSet<Type>();
		Set<Type> visitedTypes = new HashSet<Type>();

		typesToVisit.add(this);

		while (!typesToVisit.isEmpty()) {

			Type currentType = typesToVisit.iterator().next();
			typesToVisit.remove(currentType);
			visitedTypes.add(currentType);

			/* Add the properties of this type */
			for (Property property : currentType.getOwnedProperty()) {
				/*
				 * Check if a property is shadowed by another property of a sub type.
				 */
				if (!visibleProperties.contains(property.getName())) {
					allProperties.add(property);
					visibleProperties.add(property.getName());
				}
				// no else.
			}
			// end for.

			for (Type type : currentType.getSuperType()) {
				if (!visitedTypes.contains(type))
					typesToVisit.add(type);
				// no else.
			}
			// end for.
		}
		// end while.

		return allProperties;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public List<Operation> allOperations() {

		List<Operation> allOperations = new ArrayList<Operation>();

		// add the properties of this type
		allOperations.addAll(getOwnedOperation());

		// add the properties of super types
		for (Type superType : getSuperType()) {
			allOperations.addAll(superType.allOperations());
		}

		return allOperations;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public Property lookupProperty(String name) {

		if (logger.isDebugEnabled()) {
			logger.debug("lookupProperty(name=" + name + ") - enter"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		Property property = null;

		// precondition check
		if (name == null) {
			logger.warn("Tried to lookup a property using a null name!"); //$NON-NLS-1$
			return null;
		}

		for (Property p : allProperties()) {
			if (name.equals(p.getName())) {
				property = p;
				break;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("lookupProperty() - exit - return value=" + property); //$NON-NLS-1$
		}

		return property;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public Operation lookupOperation(String name, List<Type> paramTypes) {

		if (logger.isDebugEnabled()) {
			logger.debug("lookupOperation(name=" + name + ", paramTypes=" //$NON-NLS-1$ //$NON-NLS-2$
					+ paramTypes + ") - enter"); //$NON-NLS-1$
		}

		Operation operation = null;

		// precondition check
		if (name == null || paramTypes == null) {
			throw new NullArgumentException("name or paramTypes"); //$NON-NLS-1$
		}

		// look for an operation with that name and matching signature
		for (Operation o : allOperations()) {
			if (name.equals(o.getName()) && o.hasMatchingSignature(paramTypes)) {
				operation = o;
				break;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("lookupOperation() - exit - return value=" + operation); //$NON-NLS-1$
		}

		return operation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public Type addProperty(Property property) {

		if (logger.isDebugEnabled()) {
			logger.debug("addProperty(p=" + property + ") - enter"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// use the generated method, not the client-specific list obtained
		// through
		// getOwnedProperty()
		getOwnedPropertyGen().add(property);

		if (logger.isDebugEnabled()) {
			logger.debug("addProperty(p=" + property + ") - exit"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		return this;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public Type addOperation(Operation operation) {

		if (logger.isDebugEnabled()) {
			logger.debug("addOperation(o=" + operation + ") - enter"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// use the generated method, not the client-specific list obtained
		// through
		// getOwnedOperation()
		getOwnedOperationGen().add(operation);

		if (logger.isDebugEnabled()) {
			logger.debug("addOperation() - exit"); //$NON-NLS-1$
		}

		return this;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public Type addSuperType(Type type) {

		if (logger.isDebugEnabled()) {
			logger.debug("addSuperType(t=" + type + ") - enter"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// use the generated method, not the client-specific list obtained
		// through
		// getSuperType()
		getSuperTypeGen().add(type);

		if (logger.isDebugEnabled()) {
			logger.debug("addSuperType(t=" + type + ") - exit"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		return this;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public boolean removeProperty(Property property) {

		if (logger.isDebugEnabled()) {
			logger.debug("removeProperty(p=" + property + ") - enter"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		boolean result;

		/*
		 * Use the generated method, not the client-specific list obtained through
		 * getOwnedProperty().
		 */
		result = this.getOwnedPropertyGen().remove(property);

		if (logger.isDebugEnabled()) {
			logger
					.debug("removeProperty(p=" + property + ") - exit - result = " + result); //$NON-NLS-1$ //$NON-NLS-2$
		}

		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public boolean removeOperation(Operation operation) {

		if (logger.isDebugEnabled()) {
			logger.debug("removeOperation(o=" + operation + ") - enter"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		boolean result;

		/*
		 * Use the generated method, not the client-specific list obtained through
		 * getOwnedOperation().
		 */
		result = this.getOwnedOperationGen().remove(operation);

		if (logger.isDebugEnabled()) {
			logger
					.debug("removeOperation(o=" + operation + ") - exit - result = " + result); //$NON-NLS-1$ //$NON-NLS-2$
		}

		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public List<Property> getIDProperties() {

		List<Property> props = new ArrayList<Property>();
		for (Property prop : allProperties()) {
			if (prop.isIdentifier())
				props.add(prop);
		}
		return props;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public Type addTypeParameter(TypeParameter typeParameter) {

		if (logger.isDebugEnabled()) {
			logger.debug("addTypeParameter(typeParameter=" + typeParameter //$NON-NLS-1$
					+ ") - enter"); //$NON-NLS-1$
		}

		// use the generated method, not the one that may be overridden by
		// clients
		getOwnedTypeParameterGen().add(typeParameter);

		if (logger.isDebugEnabled()) {
			logger.debug("addTypeParameter() - exit"); //$NON-NLS-1$
		}

		return this;
	}

	/**
	 * Performs a binding of type parameters as described for
	 * {@link GenericElement#bindTypeParameter()}. Note that the lists for
	 * <code>parameters</code> and <code>types</code> must support comparison of
	 * elements via equality, not identity. In particular, this means that Ecore
	 * {@link EObjectEList}s should be copied into a new list before calling this
	 * method.
	 * 
	 * @generated NOT
	 */
	public Type bindTypeParameter(List<TypeParameter> parameters,
			List<? extends Type> types) {

		if (logger.isDebugEnabled()) {
			logger.debug("bindTypeParameter(parameters=" + parameters + ", types=" //$NON-NLS-1$ //$NON-NLS-2$
					+ types + ") - enter"); //$NON-NLS-1$
		}

		// precondition check
		GenericElements.checkBindingParameters(parameters, types);

		Binding binding;
		Type boundType;

		// create a new binding
		binding = new Binding(this, parameters, types);

		// try to find a previously bound type and create a new one if necessary
		boundType = getBoundTypes().get(binding);

		if (boundType == null) {
			boundType = this.clone();

			// remove the type parameters that are going to be bound
			ListUtil.removeAll(boundType.getOwnedTypeParameter(), parameters);

			// cache early to prevent endless loop if type is required while
			// binding
			boundTypes.put(binding, boundType);

			// bind all properties
			for (Property property : boundType.getOwnedProperty()) {
				GenericElements.bindTypedElement(property, parameters, types);
			}

			// bind all operations
			for (Operation operation : boundType.getOwnedOperation()) {
				GenericElements.bindOperation(operation, parameters, types);
			}

			// bind all generic supertypes
			for (Iterator<GenericType> it =
					boundType.getGenericSuperType().iterator(); it.hasNext();) {
				GenericType genericSuperType = it.next();

				// if generic super type was successfully bound, remove it from
				// the type
				if (genericSuperType.bindGenericSuperType(parameters, types, boundType)) {
					it.remove();
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("bindTypeParameter() - exit - return value=" + boundType); //$NON-NLS-1$
		}

		return boundType;
	}

	/**
	 * Helper method that lazily creates the map with cached bound types
	 * 
	 * @return a {@code Map<String,TypeParameter>} instance
	 */
	protected static Map<Binding, Type> getBoundTypes() {

		if (boundTypes == null) {
			boundTypes = new HashMap<Binding, Type>();
		}

		return boundTypes;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Type clone() {

		return initialize(PivotModelFactory.eINSTANCE.createType());
	}

	/**
	 * Convenience method for subclasses that initializes a cloned
	 * <code>Type</code> with the properties of this <code>Type</code>.
	 */
	protected Type initialize(Type clone) {

		super.initialize(clone);

		// clone type parameters
		for (TypeParameter typeParameter : getOwnedTypeParameter()) {
			clone.addTypeParameter(typeParameter.clone());
		}

		// clone properties
		for (Property property : getOwnedProperty()) {
			clone.addProperty(property.clone());
		}

		// clone operations
		for (Operation operation : getOwnedOperation()) {
			clone.addOperation(operation.clone());
		}

		// copy supertype list; do not clone the super types because they are
		// not
		// contained by this type
		for (Type superType : getSuperType()) {
			clone.addSuperType(superType);
		}

		// clone the generic supertypes
		for (GenericType genericSuperType : getGenericSuperType()) {
			clone.getGenericSuperType().add(genericSuperType.clone());
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
		case PivotModelPackage.TYPE__OWNED_TYPE_PARAMETER:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getOwnedTypeParameter())
					.basicAdd(otherEnd, msgs);
		case PivotModelPackage.TYPE__NAMESPACE:
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			return basicSetNamespace((Namespace) otherEnd, msgs);
		case PivotModelPackage.TYPE__OWNED_OPERATION:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getOwnedOperation())
					.basicAdd(otherEnd, msgs);
		case PivotModelPackage.TYPE__OWNED_PROPERTY:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getOwnedProperty())
					.basicAdd(otherEnd, msgs);
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
		case PivotModelPackage.TYPE__OWNED_TYPE_PARAMETER:
			return ((InternalEList<?>) getOwnedTypeParameter()).basicRemove(otherEnd,
					msgs);
		case PivotModelPackage.TYPE__NAMESPACE:
			return basicSetNamespace(null, msgs);
		case PivotModelPackage.TYPE__OWNED_OPERATION:
			return ((InternalEList<?>) getOwnedOperation()).basicRemove(otherEnd,
					msgs);
		case PivotModelPackage.TYPE__OWNED_PROPERTY:
			return ((InternalEList<?>) getOwnedProperty())
					.basicRemove(otherEnd, msgs);
		case PivotModelPackage.TYPE__GENERIC_SUPER_TYPE:
			return ((InternalEList<?>) getGenericSuperType()).basicRemove(otherEnd,
					msgs);
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
		case PivotModelPackage.TYPE__NAMESPACE:
			return eInternalContainer().eInverseRemove(this,
					PivotModelPackage.NAMESPACE__OWNED_TYPE, Namespace.class, msgs);
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
		case PivotModelPackage.TYPE__OWNED_TYPE_PARAMETER:
			return getOwnedTypeParameter();
		case PivotModelPackage.TYPE__NAMESPACE:
			return getNamespace();
		case PivotModelPackage.TYPE__OWNED_OPERATION:
			return getOwnedOperation();
		case PivotModelPackage.TYPE__OWNED_PROPERTY:
			return getOwnedProperty();
		case PivotModelPackage.TYPE__SUPER_TYPE:
			return getSuperType();
		case PivotModelPackage.TYPE__GENERIC_SUPER_TYPE:
			return getGenericSuperType();
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
		case PivotModelPackage.TYPE__OWNED_TYPE_PARAMETER:
			getOwnedTypeParameter().clear();
			getOwnedTypeParameter().addAll(
					(Collection<? extends TypeParameter>) newValue);
			return;
		case PivotModelPackage.TYPE__NAMESPACE:
			setNamespace((Namespace) newValue);
			return;
		case PivotModelPackage.TYPE__OWNED_OPERATION:
			getOwnedOperation().clear();
			getOwnedOperation().addAll((Collection<? extends Operation>) newValue);
			return;
		case PivotModelPackage.TYPE__OWNED_PROPERTY:
			getOwnedProperty().clear();
			getOwnedProperty().addAll((Collection<? extends Property>) newValue);
			return;
		case PivotModelPackage.TYPE__SUPER_TYPE:
			getSuperType().clear();
			getSuperType().addAll((Collection<? extends Type>) newValue);
			return;
		case PivotModelPackage.TYPE__GENERIC_SUPER_TYPE:
			getGenericSuperType().clear();
			getGenericSuperType()
					.addAll((Collection<? extends GenericType>) newValue);
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
		case PivotModelPackage.TYPE__OWNED_TYPE_PARAMETER:
			getOwnedTypeParameter().clear();
			return;
		case PivotModelPackage.TYPE__NAMESPACE:
			setNamespace((Namespace) null);
			return;
		case PivotModelPackage.TYPE__OWNED_OPERATION:
			getOwnedOperation().clear();
			return;
		case PivotModelPackage.TYPE__OWNED_PROPERTY:
			getOwnedProperty().clear();
			return;
		case PivotModelPackage.TYPE__SUPER_TYPE:
			getSuperType().clear();
			return;
		case PivotModelPackage.TYPE__GENERIC_SUPER_TYPE:
			getGenericSuperType().clear();
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
		case PivotModelPackage.TYPE__OWNED_TYPE_PARAMETER:
			return ownedTypeParameter != null && !ownedTypeParameter.isEmpty();
		case PivotModelPackage.TYPE__NAMESPACE:
			return getNamespace() != null;
		case PivotModelPackage.TYPE__OWNED_OPERATION:
			return ownedOperation != null && !ownedOperation.isEmpty();
		case PivotModelPackage.TYPE__OWNED_PROPERTY:
			return ownedProperty != null && !ownedProperty.isEmpty();
		case PivotModelPackage.TYPE__SUPER_TYPE:
			return superType != null && !superType.isEmpty();
		case PivotModelPackage.TYPE__GENERIC_SUPER_TYPE:
			return genericSuperType != null && !genericSuperType.isEmpty();
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

		if (baseClass == ConstrainableElement.class) {
			switch (derivedFeatureID) {
			default:
				return -1;
			}
		}
		if (baseClass == GenericElement.class) {
			switch (derivedFeatureID) {
			case PivotModelPackage.TYPE__OWNED_TYPE_PARAMETER:
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

		if (baseClass == ConstrainableElement.class) {
			switch (baseFeatureID) {
			default:
				return -1;
			}
		}
		if (baseClass == GenericElement.class) {
			switch (baseFeatureID) {
			case PivotModelPackage.GENERIC_ELEMENT__OWNED_TYPE_PARAMETER:
				return PivotModelPackage.TYPE__OWNED_TYPE_PARAMETER;
			default:
				return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {

		return PivotModelPackage.Literals.TYPE;
	}

	/**
	 * Returns a string representing this <code>Type</code>. Includes its name and
	 * namespace.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("name", getName()).append("namespace", getNamespace()).toString(); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Overridden to return the same hash code for elements with the same
	 * {@link #getQualifiedName() qualified name}.
	 * 
	 * @return a hash code for this <code>Type</code>
	 */
	@Override
	public int hashCode() {

		String qualifiedName = getQualifiedName();
		final int prime = 31;
		int result = 1;
		result =
				prime * result
						+ ((qualifiedName == null) ? 0 : qualifiedName.hashCode());
		return result;
	}

	/**
	 * Overridden to treat types with the same {@link #getQualifiedName()
	 * qualified name} as equal. This is important to determine
	 * {@link #commonSuperType(Type) common super types} and
	 * {@link #conformsTo(Type) type conformance} correctly.
	 * 
	 * @param obj
	 *          the other object to test for equality
	 * 
	 * @return <code>true</code> if the other object is a <code>Type</code> with
	 *         the same qualified name, <code>false</code> otherwise
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;

		if (!(obj instanceof TypeImpl))
			return false;
		final TypeImpl other = (TypeImpl) obj;

		String qualifiedName = getQualifiedName();
		String otherQualifiedName = other.getQualifiedName();

		if (qualifiedName == null) {
			if (otherQualifiedName != null)
				return false;
		}

		else if (!qualifiedName.equals(otherQualifiedName))
			return false;

		return true;
	}

} // TypeImpl
