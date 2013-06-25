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
 * <em><b>Type Parameter</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.TypeParameter#getGenericElement
 * <em>Generic Element</em>}</li>
 * </ul>
 * </p>
 * 
 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getTypeParameter()
 * @model
 * @generated
 */
public interface TypeParameter extends NamedElement {

	/**
	 * Returns the value of the '<em><b>Generic Element</b></em>' container
	 * reference. It is bidirectional and its opposite is '
	 * {@link tudresden.ocl20.pivot.pivotmodel.GenericElement#getOwnedTypeParameter
	 * <em>Owned Type Parameter</em>}'. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Generic Element</em>' container reference isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Generic Element</em>' container reference.
	 * @see #setGenericElement(GenericElement)
	 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getTypeParameter_GenericElement()
	 * @see tudresden.ocl20.pivot.pivotmodel.GenericElement#getOwnedTypeParameter
	 * @model opposite="ownedTypeParameter" resolveProxies="false"
	 *        transient="false"
	 * @generated
	 */
	GenericElement getGenericElement();

	/**
	 * Sets the value of the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.TypeParameter#getGenericElement
	 * <em>Generic Element</em>}' container reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *          the new value of the '<em>Generic Element</em>' container
	 *          reference.
	 * @see #getGenericElement()
	 * @generated
	 */
	void setGenericElement(GenericElement value);

	/**
	 * Redefines {@link NamedElement#clone()} with a covariant return type.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.NamedElement#clone()
	 */
	TypeParameter clone();

} // TypeParameter
