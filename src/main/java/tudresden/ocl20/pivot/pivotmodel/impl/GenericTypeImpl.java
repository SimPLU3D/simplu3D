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
import org.eclipse.emf.ecore.EClass;

import tudresden.ocl20.pivot.pivotmodel.GenericType;
import tudresden.ocl20.pivot.pivotmodel.NamedElement;
import tudresden.ocl20.pivot.pivotmodel.PivotModelPackage;
import tudresden.ocl20.pivot.pivotmodel.Type;
import tudresden.ocl20.pivot.pivotmodel.TypeParameter;
import tudresden.ocl20.pivot.pivotmodel.TypedElement;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Generic Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * </p>
 * 
 * @generated
 */
public abstract class GenericTypeImpl extends NamedElementImpl implements
		GenericType {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(GenericTypeImpl.class);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected GenericTypeImpl() {

		super();
	}

	/**
	 * Overridden to return the EMF container reference. This will either be a
	 * {@link TypedElement} or a {@link Type}, depending on whether this
	 * <code>GenericType</code> is used as a generic type for a
	 * <code>TypedElement</code> or as a generic super type of a <code>Type</code>
	 * .
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl#getOwner()
	 */
	@Override
	public NamedElement getOwner() {

		return eContainer instanceof NamedElement ? (NamedElement) eContainer
				: null;
	}

	/**
	 * Overridden to prevent setting the name for the GenericType. This method
	 * will throw an {@link UnsupportedOperationException}.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl#setName(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unused")
	public void setName(String newName) {

		throw new UnsupportedOperationException(
				"The name of a generic type cannot be changed."); //$NON-NLS-1$
	}

	/**
	 * This method checks the input parameters and delegates to
	 * {@link #doBindGenericType(List, List)}.
	 * 
	 * @param parameters
	 *          the type parameters to be bound
	 * @param types
	 *          the types for binding
	 * @param typedElement
	 *          the <code>TypedElement</code> whose type should be bound
	 * 
	 * @return the given <code>TypedElement</code>, either with a bound type or
	 *         unchanged
	 * 
	 * @see GenericType#bindGenericType(List, List, TypedElement)
	 * 
	 * @generated NOT
	 */
	public final boolean bindGenericType(List<TypeParameter> parameters,
			List<? extends Type> types, TypedElement typedElement) {

		if (logger.isDebugEnabled()) {
			logger
					.debug("bindGenericType(parameters=" + parameters + ", types=" + types //$NON-NLS-1$ //$NON-NLS-2$
							+ ", typedElement=" + typedElement + ") - enter"); //$NON-NLS-1$//$NON-NLS-2$
		}

		// perform binding
		boolean success = doBindGenericType(parameters, types, typedElement);

		if (logger.isDebugEnabled()) {
			logger.debug("bindGenericType() - exit - return value=" + success); //$NON-NLS-1$
		}

		return success;
	}

/**
	 * This method checks the input parameters and delegates to
	 * {@link #doBindGenericSuperType(List, List, Type).
	 * 
	 * @param parameters the type parameters to be bound
	 * @param types the types for binding
	 * @param subType the type whose generic super type should be bound
	 * 
	 * @return a <code>boolean</code> indicating whether the binding was successful
	 * 
	 * @see GenericType#bindGenericType(List, List, TypedElement)
	 * 
	 * @generated NOT
	 */
	public boolean bindGenericSuperType(List<TypeParameter> parameters,
			List<? extends Type> types, Type subType) {

		if (logger.isDebugEnabled()) {
			logger
					.debug("bindGenericSuperType(parameters=" + parameters + ", types=" + types //$NON-NLS-1$ //$NON-NLS-2$
							+ ", subType=" + subType + ") - enter"); //$NON-NLS-1$//$NON-NLS-2$
		}

		// perform binding
		boolean success = doBindGenericSuperType(parameters, types, subType);

		if (logger.isDebugEnabled()) {
			logger.debug("bindGenericSuperType() - exit - return value=" + success); //$NON-NLS-1$
		}

		return success;
	}

	/**
	 * Subclasses need to implement the actual binding.
	 */
	protected abstract boolean doBindGenericType(List<TypeParameter> parameters,
			List<? extends Type> types, TypedElement typedElement);

	/**
	 * Subclasses need to implement the actual binding.
	 */
	protected abstract boolean doBindGenericSuperType(
			List<TypeParameter> parameters, List<? extends Type> types, Type subType);

	/**
	 * Made abstract in order to leave implementation to subclasses.
	 * 
	 * @generated NOT
	 */
	public abstract boolean isConformant(Type type);

	/**
	 * Overridden to set the covariant return type. The actual implementation is
	 * left to subclasses.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl#clone()
	 */
	@Override
	public abstract GenericType clone();

	/**
	 * The EMF implementation is adapted to prevent that the name of the generic
	 * type is serialized to XMI. This is necessary to prevent setting the name
	 * upon loading the document which would throw an exception.
	 * 
	 * @generated NOT
	 * 
	 * @see #setName(String)
	 */
	@Override
	public boolean eIsSet(int featureID) {

		switch (featureID) {
		case PivotModelPackageImpl.GENERIC_TYPE__NAME:
			return false;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {

		return PivotModelPackage.Literals.GENERIC_TYPE;
	}

} // GenericTypeImpl
