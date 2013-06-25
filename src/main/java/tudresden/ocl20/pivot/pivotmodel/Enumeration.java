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
 * <em><b>Enumeration</b></em>'. <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc -->
 * <p>
 * An <code>Enumeration</code> defines a set of {@link EnumerationLiteral
 * literals} that can be used as its values.
 * </p>
 * <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.Enumeration#getOwnedLiteral <em>
 * Owned Literal</em>}</li>
 * </ul>
 * </p>
 * 
 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getEnumeration()
 * @model
 * @generated
 */
public interface Enumeration extends Type {

	/**
	 * Returns the value of the '<em><b>Owned Literal</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link tudresden.ocl20.pivot.pivotmodel.EnumerationLiteral}. It is
	 * bidirectional and its opposite is '
	 * {@link tudresden.ocl20.pivot.pivotmodel.EnumerationLiteral#getEnumeration
	 * <em>Enumeration</em>}'. <!-- begin-user-doc --> <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 * <p>
	 * This is the ordered collection of literals for the enumeration.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Owned Literal</em>' containment reference
	 *         list.
	 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getEnumeration_OwnedLiteral()
	 * @see tudresden.ocl20.pivot.pivotmodel.EnumerationLiteral#getEnumeration
	 * @model opposite="enumeration" containment="true"
	 * @generated
	 */
	List<EnumerationLiteral> getOwnedLiteral();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Adds an {@link EnumerationLiteral} to this <code>Enumeration</code>. This
	 * operation is required to properly support cloning enumerations. The
	 * operation returns a reference to this <code>Enumeration</code>.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @model required="true" literalRequired="true"
	 * @generated
	 */
	Enumeration addLiteral(EnumerationLiteral literal);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Returns an {@link EnumerationLiteral} of this <code>Enumeration</code> with
	 * the given name.
	 * 
	 * It is specified as follows:
	 * 
	 * <pre>
	 * context Enumeration
	 * def: lookupLiteral(name : String) : EnumerationLiteral =
	 *    self.ownedLiteral->any(l | l.name = name)
	 * </pre>
	 * 
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @model nameDataType="tudresden.ocl20.pivot.datatypes.String"
	 * @generated
	 */
	EnumerationLiteral lookupLiteral(String name);

	/**
	 * Redefines {@link NamedElement#clone()} with a covariant return type.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.Type#clone()
	 */
	Enumeration clone();

} // Enumeration
