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
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import tudresden.ocl20.pivot.pivotmodel.PivotModelFactory;
import tudresden.ocl20.pivot.pivotmodel.PivotModelPackage;
import tudresden.ocl20.pivot.pivotmodel.PrimitiveType;
import tudresden.ocl20.pivot.pivotmodel.PrimitiveTypeKind;
import tudresden.ocl20.pivot.pivotmodel.Type;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Primitive Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.impl.PrimitiveTypeImpl#getKind
 * <em>Kind</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class PrimitiveTypeImpl extends TypeImpl implements PrimitiveType {

	/**
	 * The default value of the '{@link #getKind() <em>Kind</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected static final PrimitiveTypeKind KIND_EDEFAULT =
			PrimitiveTypeKind.UNKNOWN;
	/**
	 * The cached value of the '{@link #getKind() <em>Kind</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected PrimitiveTypeKind kind = KIND_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected PrimitiveTypeImpl() {

		super();
	}

	/**
	 * Overridden to implement special type conformance rules. Since there is an
	 * endless number of primitive types in various metamodels (with differing
	 * names), we cannot solely check for conformance based on the qualified name
	 * of the primitive type. Instead, the {@link #getKind() kind} of the
	 * primitive type has to be considered as well.
	 * 
	 * <p>
	 * More precisely, two primitive types conform to each other if their
	 * <code>kind</code> is equal. Furthermore, we implement the special rule that
	 * a primitive type with kind {@link PrimitiveTypeKind#INTEGER INTEGER}
	 * conforms to one with kind {@link PrimitiveTypeKind#REAL REAL}.
	 * </p>
	 * 
	 * @param other
	 *          the other type
	 * 
	 * @return <code>true</code> if this type conforms to the other,
	 *         <code>false</code> otherwise
	 */
	@Override
	public boolean conformsTo(Type other) {

		boolean conformant = super.conformsTo(other);

		if (!conformant && other instanceof PrimitiveType) {
			PrimitiveTypeKind thisKind, otherKind;

			// get this and the other type's kind
			otherKind = ((PrimitiveType) other).getKind();
			thisKind = getKind();

			if (thisKind == PrimitiveTypeKind.INTEGER) {
				conformant =
						otherKind == PrimitiveTypeKind.INTEGER
								|| otherKind == PrimitiveTypeKind.REAL;
			}

			else {
				conformant = thisKind == otherKind;
			}
		}

		return conformant;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public PrimitiveTypeKind getKind() {

		return kind;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setKind(PrimitiveTypeKind newKind) {

		PrimitiveTypeKind oldKind = kind;
		kind = newKind == null ? KIND_EDEFAULT : newKind;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					PivotModelPackage.PRIMITIVE_TYPE__KIND, oldKind, kind));
	}

	/*
	 * (non-Javadoc)
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.TypeImpl#clone()
	 */
	@Override
	public PrimitiveType clone() {

		return initialize(PivotModelFactory.eINSTANCE.createPrimitiveType());
	}

	/**
	 * Helper method to initialize a cloned <code>PrimitiveType</code>.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.TypeImpl#initialize(tudresden.ocl20.pivot.pivotmodel.Type)
	 */
	protected PrimitiveType initialize(PrimitiveType clone) {

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
	public Object eGet(int featureID, boolean resolve, boolean coreType) {

		switch (featureID) {
		case PivotModelPackage.PRIMITIVE_TYPE__KIND:
			return getKind();
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
		case PivotModelPackage.PRIMITIVE_TYPE__KIND:
			setKind((PrimitiveTypeKind) newValue);
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
		case PivotModelPackage.PRIMITIVE_TYPE__KIND:
			setKind(KIND_EDEFAULT);
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
		case PivotModelPackage.PRIMITIVE_TYPE__KIND:
			return kind != KIND_EDEFAULT;
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

		return PivotModelPackage.Literals.PRIMITIVE_TYPE;
	}

	/**
	 * The EMF implementation is changed to use the Jakarta Commons Lang
	 * ToStringBuilder instead.
	 * 
	 * @generated NOT
	 */
	@Override
	public String toString() {

		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.appendToString(super.toString()).append("kind", getKind()).toString(); //$NON-NLS-1$
	}

	@Override
	public boolean equals(Object other) {

		if (other instanceof PrimitiveType) {
			PrimitiveType otherType = (PrimitiveType) other;
			return this.getKind().equals(otherType.getKind());
		}
		return false;
	}

} // PrimitiveTypeImpl
