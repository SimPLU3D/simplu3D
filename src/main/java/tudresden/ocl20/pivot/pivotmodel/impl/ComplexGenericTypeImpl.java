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
import java.util.ListIterator;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

import tudresden.ocl20.pivot.pivotmodel.ComplexGenericType;
import tudresden.ocl20.pivot.pivotmodel.PivotModelFactory;
import tudresden.ocl20.pivot.pivotmodel.PivotModelPackage;
import tudresden.ocl20.pivot.pivotmodel.Type;
import tudresden.ocl20.pivot.pivotmodel.TypeArgument;
import tudresden.ocl20.pivot.pivotmodel.TypeParameter;
import tudresden.ocl20.pivot.pivotmodel.TypedElement;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Complex Generic Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link tudresden.ocl20.pivot.pivotmodel.impl.ComplexGenericTypeImpl#getUnboundType
 * <em>Unbound Type</em>}</li>
 * <li>
 * {@link tudresden.ocl20.pivot.pivotmodel.impl.ComplexGenericTypeImpl#getTypeArgument
 * <em>Type Argument</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ComplexGenericTypeImpl extends GenericTypeImpl implements
		ComplexGenericType {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(ComplexGenericTypeImpl.class);

	/**
	 * The cached value of the '{@link #getUnboundType() <em>Unbound Type</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getUnboundType()
	 * @generated
	 * @ordered
	 */
	protected Type unboundType;

	/**
	 * The cached value of the '{@link #getTypeArgument() <em>Type Argument</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTypeArgument()
	 * @generated
	 * @ordered
	 */
	protected EList<TypeArgument> typeArgument;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ComplexGenericTypeImpl() {

		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {

		return PivotModelPackage.Literals.COMPLEX_GENERIC_TYPE;
	}

	/**
	 * Overridden to return the name of the associated (non-)generic type wrapped
	 * by this <code>ComplexGenericType</code>.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl#getName()
	 */
	@Override
	public String getName() {

		return getUnboundType() != null ? getUnboundType().getName() : ""; //$NON-NLS-1$
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Type getUnboundType() {

		return unboundType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setUnboundType(Type newUnboundType) {

		Type oldUnboundType = unboundType;
		unboundType = newUnboundType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					PivotModelPackage.COMPLEX_GENERIC_TYPE__UNBOUND_TYPE, oldUnboundType,
					unboundType));
	}

	/**
	 * The implementation in this class simply redirects to {
	 * {@link #getTypeArgumentGen()} which contains the code generated by EMF.
	 * Client may, however, override this method to provide specific behaviour,
	 * e.g., adapt to other model repositories.
	 * 
	 * @generated NOT
	 */
	public List<TypeArgument> getTypeArgument() {

		return getTypeArgumentGen();
	}

	/**
	 * <!-- begin-user-doc -->The code generated for {{@link #getTypeArgument()}
	 * is redirected to this method. <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected final List<TypeArgument> getTypeArgumentGen() {

		if (typeArgument == null) {
			typeArgument =
					new EObjectContainmentWithInverseEList<TypeArgument>(
							TypeArgument.class, this,
							PivotModelPackage.COMPLEX_GENERIC_TYPE__TYPE_ARGUMENT,
							PivotModelPackage.TYPE_ARGUMENT__OWNING_GENERIC_TYPE);
		}
		return typeArgument;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public ComplexGenericType addTypeArgument(TypeArgument typeArgument) {

		if (logger.isDebugEnabled()) {
			logger.debug("addTypeArgument(typeArgument=" + typeArgument //$NON-NLS-1$
					+ ") - enter"); //$NON-NLS-1$
		}

		// use generated method as getTypeArgument() may be overridden by subclasses
		getTypeArgumentGen().add(typeArgument);

		if (logger.isDebugEnabled()) {
			logger.debug("addTypeArgument() - exit"); //$NON-NLS-1$
		}

		return this;
	}

	/**
	 * This method will bind the type of the <code>typedElement</code> if the
	 * {@link #getUnboundType() unbound type} of this
	 * <code>ComplexGenericType</code> can be fully bound with the bindings
	 * represented by the <code>parameters</code> and <code>types</code>
	 * arguments. Even if the given lists are empty, this method will attempt to
	 * bind the unbound type using the {@link TypeArgument}s set for this
	 * <code>ComplexGenericType</code>.
	 */
	@Override
	public boolean doBindGenericType(List<TypeParameter> parameters,
			List<? extends Type> types, TypedElement typedElement) {

		// bind the unbound type of this complex generic type
		Type boundType = bindUnboundType(parameters, types);

		// if all type parameters have been bound set the typed element's type
		if (boundType.getOwnedTypeParameter().isEmpty()) {
			if (logger.isInfoEnabled()) {
				logger.info("Binding type of '" + typedElement.getQualifiedName() //$NON-NLS-1$
						+ "' with '" + boundType.getName() + "'."); //$NON-NLS-1$ //$NON-NLS-2$
			}

			// reset the generic type first
			typedElement.setGenericType(null);

			// now set the type (order is important, see TypedElement.setGenericType)
			typedElement.setType(boundType);

			return true;
		}

		return false;
	}

	/**
	 * This method will add a bound super type to the given <code>Type</code> if
	 * the {@link #getUnboundType() unbound type} of this
	 * <code>ComplexGenericType</code> can be fully bound with the bindings
	 * represented by the <code>parameters</code> and <code>types</code>
	 * arguments. Even if the given lists are empty, this method will attempt to
	 * bind the unbound type using the {@link TypeArgument}s set for this
	 * <code>ComplexGenericType</code>.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.GenericTypeImpl#doBindGenericSuperType(java.util.List,
	 *      java.util.List, tudresden.ocl20.pivot.pivotmodel.Type)
	 */
	@Override
	protected boolean doBindGenericSuperType(List<TypeParameter> parameters,
			List<? extends Type> types, Type subType) {

		// bind the unbound type of this complex generic type
		Type boundType = bindUnboundType(parameters, types);

		// all type parameters bound, add bound type to the super types of subtype
		if (boundType.getOwnedTypeParameter().isEmpty()) {
			if (logger.isInfoEnabled()) {
				logger.info("Adding bound super type '" + boundType.getName() //$NON-NLS-1$
						+ "' to type '" + subType.getQualifiedName() + "'."); //$NON-NLS-1$ //$NON-NLS-2$
			}

			// add the bound super type
			subType.addSuperType(boundType);

			return true;
		}

		return false;
	}

	/**
	 * Helper method that binds the unbound type of this
	 * <code>ComplexGenericType</code> with the given bindings.
	 */
	protected Type bindUnboundType(List<TypeParameter> parameters,
			List<? extends Type> types) {

		Type unboundType = getUnboundType();

		// check that an unbound type has been set
		if (unboundType == null) {
			throw new IllegalStateException("No unbound type set for " + this); //$NON-NLS-1$
		}

		// number of type arguments must equal number of type parameters
		if (getTypeArgument().size() != unboundType.getOwnedTypeParameter().size()) {
			throw new IllegalStateException(
					"The number of type arguments for " + this //$NON-NLS-1$
							+ " does not match the number of type parameters of " //$NON-NLS-1$
							+ unboundType + "."); //$NON-NLS-1$
		}

		// create new lists for binding the generic types in the unbound type
		List<TypeParameter> unboundTypeParameters = new ArrayList<TypeParameter>();
		List<Type> typeArgumentTypes = new ArrayList<Type>();

		// collect all type arguments that have a normal type
		for (ListIterator<TypeArgument> it = getTypeArgument().listIterator(); it
				.hasNext();) {
			TypeArgument argument = it.next();

			// bind the type argument first if it has a generic type
			if (argument.getType() == null && argument.getGenericType() != null) {
				argument.getGenericType().bindGenericType(parameters, types, argument);
			}

			// if type argument has a non-generic type now, add it to the new bindings
			if (argument.getType() != null) {
				unboundTypeParameters.add(unboundType.getOwnedTypeParameter().get(
						it.previousIndex()));
				typeArgumentTypes.add(argument.getType());
			}
		}

		// now bind the type with the collected type parameters and types
		return unboundType.bindTypeParameter(unboundTypeParameters,
				typeArgumentTypes);
	}

	/**
	 * This method returns <code>true</code> if the given type conforms to the
	 * {@link #getUnboundType() unbound type} of this
	 * <code>ComplexGenericType</code>. * For example, if the unbound type is a
	 * generic type {@code List<T>}, then any <code>List</code> type will be
	 * conformant to this type.
	 * 
	 * <p>
	 * Note that this is not entirely correct, because nested generic types would
	 * actually need special treatment. Since this is a rather special case and
	 * not required for an OCL engine, we currently ignore it in this prototypical
	 * implementation.
	 * </p>
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.GenericTypeImpl#isConformantType(tudresden.ocl20.pivot.pivotmodel.Type)
	 */
	@Override
	public boolean isConformant(Type type) {

		return type != null && type.conformsTo(getUnboundType());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl#clone()
	 */
	@Override
	public ComplexGenericType clone() {

		return initialize(PivotModelFactory.eINSTANCE.createComplexGenericType());
	}

	/**
	 * Helper method that initializes a cloned <code>ComplexGenericType</code>.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl#initialize(tudresden.ocl20.pivot.pivotmodel.NamedElement)
	 */
	protected ComplexGenericType initialize(ComplexGenericType clone) {

		clone.setUnboundType(getUnboundType());

		// clone the contained type arguments
		for (TypeArgument typeArgument : getTypeArgument()) {
			clone.addTypeArgument(typeArgument.clone());
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
		case PivotModelPackage.COMPLEX_GENERIC_TYPE__TYPE_ARGUMENT:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getTypeArgument())
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
		case PivotModelPackage.COMPLEX_GENERIC_TYPE__TYPE_ARGUMENT:
			return ((InternalEList<?>) getTypeArgument()).basicRemove(otherEnd, msgs);
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
		case PivotModelPackage.COMPLEX_GENERIC_TYPE__UNBOUND_TYPE:
			return getUnboundType();
		case PivotModelPackage.COMPLEX_GENERIC_TYPE__TYPE_ARGUMENT:
			return getTypeArgument();
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
		case PivotModelPackage.COMPLEX_GENERIC_TYPE__UNBOUND_TYPE:
			setUnboundType((Type) newValue);
			return;
		case PivotModelPackage.COMPLEX_GENERIC_TYPE__TYPE_ARGUMENT:
			getTypeArgument().clear();
			getTypeArgument().addAll((Collection<? extends TypeArgument>) newValue);
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
		case PivotModelPackage.COMPLEX_GENERIC_TYPE__UNBOUND_TYPE:
			setUnboundType((Type) null);
			return;
		case PivotModelPackage.COMPLEX_GENERIC_TYPE__TYPE_ARGUMENT:
			getTypeArgument().clear();
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
		case PivotModelPackage.COMPLEX_GENERIC_TYPE__UNBOUND_TYPE:
			return unboundType != null;
		case PivotModelPackage.COMPLEX_GENERIC_TYPE__TYPE_ARGUMENT:
			return typeArgument != null && !typeArgument.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // ComplexGenericTypeImpl
