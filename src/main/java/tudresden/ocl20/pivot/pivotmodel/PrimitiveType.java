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

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Primitive Type</b></em>'. <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc -->
 * <p>
 * A <code>PrimitiveType</code> defines a predefined data type, without any
 * relevant substructure (i.e., it has no parts). A primitive datatype may have
 * an algebra and operations defined outside of the Pivot Model, for example,
 * mathematically.
 * </p>
 * <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.PrimitiveType#getKind <em>Kind
 * </em>}</li>
 * </ul>
 * </p>
 * 
 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getPrimitiveType()
 * @model
 * @generated
 */
public interface PrimitiveType extends Type {

	/**
	 * Returns the value of the '<em><b>Kind</b></em>' attribute. The default
	 * value is <code>"Unknown"</code>. The literals are from the enumeration
	 * {@link tudresden.ocl20.pivot.pivotmodel.PrimitiveTypeKind}. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Specifies the {@link PrimitiveTypeKind kind} of this
	 * <code>PrimitiveType</code>. Different DSLs may have different predefined
	 * primitive types, but it should be possible to classify them according to
	 * the literals defined in the Pivot Model.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Kind</em>' attribute.
	 * @see tudresden.ocl20.pivot.pivotmodel.PrimitiveTypeKind
	 * @see #setKind(PrimitiveTypeKind)
	 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getPrimitiveType_Kind()
	 * @model default="Unknown" required="true"
	 * @generated
	 */
	PrimitiveTypeKind getKind();

	/**
	 * Sets the value of the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.PrimitiveType#getKind
	 * <em>Kind</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *          the new value of the '<em>Kind</em>' attribute.
	 * @see tudresden.ocl20.pivot.pivotmodel.PrimitiveTypeKind
	 * @see #getKind()
	 * @generated
	 */
	void setKind(PrimitiveTypeKind value);

} // PrimitiveType
