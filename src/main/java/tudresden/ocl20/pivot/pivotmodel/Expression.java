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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Expression</b></em>'. <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc -->
 * <p>
 * An <code>Expression</code> contains a language- specific text string used to
 * describe a value or values, and an optional specification of the language.
 * One predefined language for specifying expressions is OCL. Natural language
 * or programming languages may also be used. Contrary to the UML specification,
 * the Pivot Model only permits one body string for each expression. This is
 * because the Pivot Model has been designed with an OCL integration in mind.
 * </p>
 * <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.Expression#getBody <em>Body</em>}
 * </li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.Expression#getLanguage <em>
 * Language</em>}</li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.Expression#getConstraint <em>
 * Constraint</em>}</li>
 * </ul>
 * </p>
 * 
 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getExpression()
 * @model
 * @generated
 */
public interface Expression extends EObject {

	/**
	 * Returns the value of the '<em><b>Body</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * This is the uninterpreted text of the expression.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Body</em>' attribute.
	 * @see #setBody(String)
	 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getExpression_Body()
	 * @model dataType="tudresden.ocl20.pivot.datatypes.String"
	 * @generated
	 */
	String getBody();

	/**
	 * Sets the value of the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Expression#getBody <em>Body</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *          the new value of the '<em>Body</em>' attribute.
	 * @see #getBody()
	 * @generated
	 */
	void setBody(String value);

	/**
	 * Returns the value of the '<em><b>Language</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * The language the expression is written in.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Language</em>' attribute.
	 * @see #setLanguage(String)
	 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getExpression_Language()
	 * @model dataType="tudresden.ocl20.pivot.datatypes.String"
	 * @generated
	 */
	String getLanguage();

	/**
	 * Sets the value of the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Expression#getLanguage
	 * <em>Language</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value
	 *          the new value of the '<em>Language</em>' attribute.
	 * @see #getLanguage()
	 * @generated
	 */
	void setLanguage(String value);

	/**
	 * Returns the value of the '<em><b>Constraint</b></em>' container reference.
	 * It is bidirectional and its opposite is '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Constraint#getSpecification
	 * <em>Specification</em>}'. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Constraint</em>' container reference isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Constraint</em>' container reference.
	 * @see #setConstraint(Constraint)
	 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getExpression_Constraint()
	 * @see tudresden.ocl20.pivot.pivotmodel.Constraint#getSpecification
	 * @model opposite="specification"
	 * @generated
	 */
	Constraint getConstraint();

	/**
	 * Sets the value of the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Expression#getConstraint
	 * <em>Constraint</em>}' container reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *          the new value of the '<em>Constraint</em>' container reference.
	 * @see #getConstraint()
	 * @generated
	 */
	void setConstraint(Constraint value);

} // Expression
