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
 * <em><b>Enumeration Literal</b></em>'. <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc -->
 * <p>
 * An <code>EnumerationLiteral</code> is a value of an {@link Enumeration}.
 * </p>
 * <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link tudresden.ocl20.pivot.pivotmodel.EnumerationLiteral#getEnumeration
 * <em>Enumeration</em>}</li>
 * </ul>
 * </p>
 * 
 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getEnumerationLiteral()
 * @model
 * @generated
 */
public interface EnumerationLiteral extends NamedElement {

	/**
	 * Returns the value of the '<em><b>Enumeration</b></em>' container reference.
	 * It is bidirectional and its opposite is '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Enumeration#getOwnedLiteral
	 * <em>Owned Literal</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * <p>
	 * The enumeration that this literal belongs to.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Enumeration</em>' container reference.
	 * @see #setEnumeration(Enumeration)
	 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getEnumerationLiteral_Enumeration()
	 * @see tudresden.ocl20.pivot.pivotmodel.Enumeration#getOwnedLiteral
	 * @model opposite="ownedLiteral"
	 * @generated
	 */
	Enumeration getEnumeration();

	/**
	 * Sets the value of the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.EnumerationLiteral#getEnumeration
	 * <em>Enumeration</em>}' container reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *          the new value of the '<em>Enumeration</em>' container reference.
	 * @see #getEnumeration()
	 * @generated
	 */
	void setEnumeration(Enumeration value);

	/**
	 * Redefines {@link NamedElement#clone()} with a covariant return type.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.NamedElement#clone()
	 */
	EnumerationLiteral clone();

} // EnumerationLiteral
