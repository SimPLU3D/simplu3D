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
 * <em><b>Parameter</b></em>'. <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc -->
 * <p>
 * A <code>Parameter</code> is a specification of an argument used to pass
 * information into or out of an invocation of an {@link Operation}.
 * </p>
 * <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.Parameter#getKind <em>Kind</em>}</li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.Parameter#getOperation <em>
 * Operation</em>}</li>
 * </ul>
 * </p>
 * 
 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getParameter()
 * @model
 * @generated
 */
public interface Parameter extends TypedElement, NamedElement {

	/**
	 * Returns the value of the '<em><b>Kind</b></em>' attribute. The default
	 * value is <code>"in"</code>. The literals are from the enumeration
	 * {@link tudresden.ocl20.pivot.pivotmodel.ParameterDirectionKind}. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Specifies the {@link ParameterDirectionKind direction kind} of this
	 * <code>Parameter</code>.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Kind</em>' attribute.
	 * @see tudresden.ocl20.pivot.pivotmodel.ParameterDirectionKind
	 * @see #setKind(ParameterDirectionKind)
	 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getParameter_Kind()
	 * @model default="in"
	 * @generated
	 */
	ParameterDirectionKind getKind();

	/**
	 * Sets the value of the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Parameter#getKind <em>Kind</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *          the new value of the '<em>Kind</em>' attribute.
	 * @see tudresden.ocl20.pivot.pivotmodel.ParameterDirectionKind
	 * @see #getKind()
	 * @generated
	 */
	void setKind(ParameterDirectionKind value);

	/**
	 * Returns the value of the '<em><b>Operation</b></em>' container reference.
	 * It is bidirectional and its opposite is '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Operation#getOwnedParameter
	 * <em>Owned Parameter</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * <p>
	 * References the {@link Operation} for which this is a formal parameter.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Operation</em>' container reference.
	 * @see #setOperation(Operation)
	 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getParameter_Operation()
	 * @see tudresden.ocl20.pivot.pivotmodel.Operation#getOwnedParameter
	 * @model opposite="ownedParameter"
	 * @generated
	 */
	Operation getOperation();

	/**
	 * Sets the value of the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Parameter#getOperation
	 * <em>Operation</em>}' container reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *          the new value of the '<em>Operation</em>' container reference.
	 * @see #getOperation()
	 * @generated
	 */
	void setOperation(Operation value);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * This is an additional operation defined in the OCL specification (Section
	 * 8.3.8). It results in a {@link Property} that has the same name, type, etc.
	 * as the parameter. This is necessary, for instance, to create tuple types
	 * from the out parameters of an {@link Operation}.
	 * 
	 * The operation is specified as:
	 * 
	 * <pre>
	 * context Parameter::asProperty(): Property
	 *    pre: -- none
	 *    post: result.name = self.name
	 *    post: result.type = self.type
	 *    post: result.isOrdered = self.isOrdered
	 *    post: result.isMultiple = self.isMultiple
	 *    post: result.isUnique = self.isUnique
	 *    post: result.isStatic = false
	 * </pre>
	 * 
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @model
	 * @generated
	 */
	Property asProperty();

	/**
	 * Redefines {@link NamedElement#clone()} with a covariant return type.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.NamedElement#clone()
	 */
	Parameter clone();

} // Parameter
