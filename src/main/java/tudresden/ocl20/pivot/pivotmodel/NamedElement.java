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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Named Element</b></em>'. <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc -->
 * <p>
 * A <code>NamedElement</code> represents elements that may have a name. The
 * name is used for identification of the named element within the elements
 * owned by its owner. A named element also has a qualified name that allows it
 * to be unambiguously identified within a hierarchy of nested named elements.
 * <code>NamedElement</code> is an abstract metaclass.
 * </p>
 * <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.NamedElement#getName <em>Name
 * </em>}</li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.NamedElement#getQualifiedName
 * <em>Qualified Name</em>}</li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.NamedElement#getOwner <em>Owner
 * </em>}</li>
 * </ul>
 * </p>
 * 
 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getNamedElement()
 * @model abstract="true"
 * @generated
 */
public interface NamedElement extends EObject {

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute. The default
	 * value is <code>""</code>. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * <p>
	 * Represents the name of the <code>NamedElement</code>.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getNamedElement_Name()
	 * @model default="" dataType="tudresden.ocl20.pivot.datatypes.String"
	 *        required="true" ordered="false"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.NamedElement#getName <em>Name</em>}
	 * ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *          the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Qualified Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * A qualified name allows the <code>NamedElement to be 
	 * identified within a hierarchy of nested elements. It is 
	 * constructed from the names of the owners starting at the 
	 * root of the hierarchy and ending with the name of the 
	 * <code>NamedElement</code> itself. This is a derived attribute.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Qualified Name</em>' attribute.
	 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getNamedElement_QualifiedName()
	 * @model dataType="tudresden.ocl20.pivot.datatypes.String" transient="true"
	 *        changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	String getQualifiedName();

	/**
	 * Returns the value of the '<em><b>Owner</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * A <code>NamedElement</code> may have an owner whose name is used for
	 * constructing the qualified name of the <code>NamedElement</code>. This is a
	 * derived attribute.
	 * 
	 * The concept of an owner was introduced in the Pivot Model to facilitate the
	 * computation of qualified names which are not available in UML::Core::Basic.
	 * However, the Pivot Model does not extend the concept of a {@link Namespace}
	 * to {@link Type types} and {@link Operation operations} as in the complete
	 * UML 2.0 specification. That's why arbitrary named elements are not
	 * necessarily located in a namespace.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Owner</em>' reference.
	 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getNamedElement_Owner()
	 * @model resolveProxies="false" transient="true" changeable="false"
	 *        volatile="true"
	 * @generated
	 */
	NamedElement getOwner();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Creates a copy of this <code>NamedElement</code>. In the Pivot Model, all
	 * clone operations are intended to perform deep cloning (as opposed to a
	 * shallow clone). That means, that all contained elements (i.e., all elements
	 * for which this <code>NamedElement</code> is the owner) have to be cloned as
	 * well. Cloning support is necessary for binding {@link GenericElement}s
	 * because such an element needs to be cloned first before its
	 * {@link TypeParameter}s can be bound.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @model required="true"
	 *        exceptions="tudresden.ocl20.pivot.pivotmodel.CloneNotSupportedException"
	 * @generated
	 */
	NamedElement clone() throws CloneNotSupportedException;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Returns the name of this {@link NamedElement} as a {@link java.util.List}
	 * of {@link java.lang.String}s containing the name of the name spaces and of
	 * this {@link NamedElement}.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @model kind="operation" dataType="tudresden.ocl20.pivot.datatypes.String"
	 *        unique="false"
	 * @generated
	 */
	List<String> getQualifiedNameList();

} // NamedElement
