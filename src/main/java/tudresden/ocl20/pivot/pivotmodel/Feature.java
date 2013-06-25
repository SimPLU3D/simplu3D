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
 * <em><b>Feature</b></em>'. <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc -->
 * <p>
 * An additional metaclass in comparison with UML::Core::Basic, a
 * <code>Feature</code> introduces the notion of a class-scope (i.e. static)
 * feature in a {@link Type}.
 * </p>
 * <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.Feature#isStatic <em>Static</em>}
 * </li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.Feature#getSemantics <em>
 * Semantics</em>}</li>
 * </ul>
 * </p>
 * 
 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getFeature()
 * @model abstract="true"
 * @generated
 */
public interface Feature extends TypedElement {

	/**
	 * Returns the value of the '<em><b>Static</b></em>' attribute. The default
	 * value is <code>"false"</code>. <!-- begin-user-doc --> <!-- end-user-doc
	 * --> <!-- begin-model-doc --> <code>
	 * Specifies whether this feature characterizes individual 
	 * instances classified by a {@link Type} 
	 * (<code>false</code>) or the type itself (<code>true</code>). Default value
	 * is <code>false</code>. </code> <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Static</em>' attribute.
	 * @see #setStatic(boolean)
	 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getFeature_Static()
	 * @model default="false" dataType="tudresden.ocl20.pivot.datatypes.Boolean"
	 * @generated
	 */
	boolean isStatic();

	/**
	 * Sets the value of the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Feature#isStatic <em>Static</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *          the new value of the '<em>Static</em>' attribute.
	 * @see #isStatic()
	 * @generated
	 */
	void setStatic(boolean value);

	/**
	 * Returns the value of the '<em><b>Semantics</b></em>' reference. It is
	 * bidirectional and its opposite is '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Constraint#getDefinedFeature
	 * <em>Defined Feature</em>}'. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Semantics</em>' reference isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Semantics</em>' reference.
	 * @see #setSemantics(Constraint)
	 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getFeature_Semantics()
	 * @see tudresden.ocl20.pivot.pivotmodel.Constraint#getDefinedFeature
	 * @model opposite="definedFeature"
	 * @generated
	 */
	Constraint getSemantics();

	/**
	 * Sets the value of the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Feature#getSemantics
	 * <em>Semantics</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value
	 *          the new value of the '<em>Semantics</em>' reference.
	 * @see #getSemantics()
	 * @generated
	 */
	void setSemantics(Constraint value);

} // Feature
