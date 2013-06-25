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
package tudresden.ocl20.pivot.pivotmodel;

import java.util.List;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Complex Generic Type</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link tudresden.ocl20.pivot.pivotmodel.ComplexGenericType#getUnboundType
 * <em>Unbound Type</em>}</li>
 * <li>
 * {@link tudresden.ocl20.pivot.pivotmodel.ComplexGenericType#getTypeArgument
 * <em>Type Argument</em>}</li>
 * </ul>
 * </p>
 * 
 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getComplexGenericType()
 * @model
 * @generated
 */
public interface ComplexGenericType extends GenericType {

	/**
	 * Returns the value of the '<em><b>Unbound Type</b></em>' reference. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Unbound Type</em>' reference isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Unbound Type</em>' reference.
	 * @see #setUnboundType(Type)
	 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getComplexGenericType_UnboundType()
	 * @model resolveProxies="false"
	 * @generated
	 */
	Type getUnboundType();

	/**
	 * Sets the value of the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.ComplexGenericType#getUnboundType
	 * <em>Unbound Type</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *          the new value of the '<em>Unbound Type</em>' reference.
	 * @see #getUnboundType()
	 * @generated
	 */
	void setUnboundType(Type value);

	/**
	 * Returns the value of the '<em><b>Type Argument</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link tudresden.ocl20.pivot.pivotmodel.TypeArgument}. It is bidirectional
	 * and its opposite is '
	 * {@link tudresden.ocl20.pivot.pivotmodel.TypeArgument#getOwningGenericType
	 * <em>Owning Generic Type</em>}'. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type Argument</em>' containment reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Type Argument</em>' containment reference
	 *         list.
	 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getComplexGenericType_TypeArgument()
	 * @see tudresden.ocl20.pivot.pivotmodel.TypeArgument#getOwningGenericType
	 * @model opposite="owningGenericType" containment="true"
	 * @generated
	 */
	List<TypeArgument> getTypeArgument();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Adds a {@link TypeArgument} to this <code>ComplexGenericType</code>. This
	 * operation is required to properly support cloning complex generic types.
	 * The operation returns a reference to this <code>ComplexGenericType</code>.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @model typeArgumentRequired="true"
	 * @generated
	 */
	ComplexGenericType addTypeArgument(TypeArgument typeArgument);

	/**
	 * Redefines {@link NamedElement#clone()} with a covariant return type.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.NamedElement#clone()
	 */
	ComplexGenericType clone();

} // ComplexGenericType
