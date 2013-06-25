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
 * <em><b>Constraint</b></em>'. <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc -->
 * <p>
 * A <code>Constraint</code> is a condition or restriction expressed in natural
 * language text or in a machine readable language for the purpose of declaring
 * some of the semantics of an element. The semantics are specified via an
 * associated {@link Expression}.
 * </p>
 * <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.Constraint#getKind <em>Kind</em>}
 * </li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.Constraint#getNamespace <em>
 * Namespace</em>}</li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.Constraint#getSpecification <em>
 * Specification</em>}</li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.Constraint#getConstrainedElement
 * <em>Constrained Element</em>}</li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.Constraint#getDefinedFeature <em>
 * Defined Feature</em>}</li>
 * </ul>
 * </p>
 * 
 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getConstraint()
 * @model
 * @generated
 */
public interface Constraint extends NamedElement {

	/**
	 * Returns the value of the '<em><b>Kind</b></em>' attribute. The literals are
	 * from the enumeration
	 * {@link tudresden.ocl20.pivot.pivotmodel.ConstraintKind}. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * This attribute determines the semantics of a constraint in relation to its
	 * {@link #getConstrainedElement() constrained elements}.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Kind</em>' attribute.
	 * @see tudresden.ocl20.pivot.pivotmodel.ConstraintKind
	 * @see #setKind(ConstraintKind)
	 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getConstraint_Kind()
	 * @model required="true"
	 * @generated
	 */
	ConstraintKind getKind();

	/**
	 * Sets the value of the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Constraint#getKind <em>Kind</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *          the new value of the '<em>Kind</em>' attribute.
	 * @see tudresden.ocl20.pivot.pivotmodel.ConstraintKind
	 * @see #getKind()
	 * @generated
	 */
	void setKind(ConstraintKind value);

	/**
	 * Returns the value of the '<em><b>Namespace</b></em>' container reference.
	 * It is bidirectional and its opposite is '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Namespace#getOwnedRule
	 * <em>Owned Rule</em>}'. <!-- begin-user-doc --> <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 * <p>
	 * This is the {@link Namespace} this constraint is contained in. Essentially,
	 * this is just a way of placing the constraint somewhere in the model that it
	 * is part of. Contrary to the UML specification, the namespace of a
	 * constraint need not to be the context where the constraint is evaluated.
	 * This is mainly due to the different notion of a namespace in the Pivot
	 * Model, i.e. a {@link Type} is not a namespace.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Namespace</em>' container reference.
	 * @see #setNamespace(Namespace)
	 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getConstraint_Namespace()
	 * @see tudresden.ocl20.pivot.pivotmodel.Namespace#getOwnedRule
	 * @model opposite="ownedRule"
	 * @generated
	 */
	Namespace getNamespace();

	/**
	 * Sets the value of the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Constraint#getNamespace
	 * <em>Namespace</em>}' container reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *          the new value of the '<em>Namespace</em>' container reference.
	 * @see #getNamespace()
	 * @generated
	 */
	void setNamespace(Namespace value);

	/**
	 * Returns the value of the '<em><b>Specification</b></em>' containment
	 * reference. It is bidirectional and its opposite is '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Expression#getConstraint
	 * <em>Constraint</em>}'. <!-- begin-user-doc --> <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 * <p>
	 * This is the specification of the constraint as an {@link Expression} in a
	 * human- or machine-readable language.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Specification</em>' containment reference.
	 * @see #setSpecification(Expression)
	 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getConstraint_Specification()
	 * @see tudresden.ocl20.pivot.pivotmodel.Expression#getConstraint
	 * @model opposite="constraint" containment="true" required="true"
	 * @generated
	 */
	Expression getSpecification();

	/**
	 * Sets the value of the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Constraint#getSpecification
	 * <em>Specification</em>}' containment reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *          the new value of the '<em>Specification</em>' containment
	 *          reference.
	 * @see #getSpecification()
	 * @generated
	 */
	void setSpecification(Expression value);

	/**
	 * Returns the value of the '<em><b>Constrained Element</b></em>' reference
	 * list. The list contents are of type
	 * {@link tudresden.ocl20.pivot.pivotmodel.ConstrainableElement}. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * This is the ordered set of {@link ConstrainableElement}s referenced by this
	 * Constraint.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Constrained Element</em>' reference list.
	 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getConstraint_ConstrainedElement()
	 * @model resolveProxies="false"
	 * @generated
	 */
	List<ConstrainableElement> getConstrainedElement();

	/**
	 * Returns the value of the '<em><b>Defined Feature</b></em>' reference. It is
	 * bidirectional and its opposite is '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Feature#getSemantics
	 * <em>Semantics</em>}'. <!-- begin-user-doc --> <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 * <p>
	 * References the defined feature of a {@link ConstraintKind#DEFINITION}
	 * constraint.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Defined Feature</em>' reference.
	 * @see #setDefinedFeature(Feature)
	 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getConstraint_DefinedFeature()
	 * @see tudresden.ocl20.pivot.pivotmodel.Feature#getSemantics
	 * @model opposite="semantics" resolveProxies="false"
	 * @generated
	 */
	Feature getDefinedFeature();

	/**
	 * Sets the value of the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Constraint#getDefinedFeature
	 * <em>Defined Feature</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *          the new value of the '<em>Defined Feature</em>' reference.
	 * @see #getDefinedFeature()
	 * @generated
	 */
	void setDefinedFeature(Feature value);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Adds a {@link ConstrainableElement} to the constrained elements of this
	 * <code>Constraint</code>. This operation is required to properly support
	 * cloning constraints. The operation returns a reference to this
	 * <code>Constraint</code>.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @model required="true" constrainedElementRequired="true"
	 * @generated
	 */
	Constraint addConstrainedElement(ConstrainableElement constrainedElement);

	/**
	 * Redefines {@link NamedElement#clone()} with a covariant return type.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.NamedElement#clone()
	 */
	Constraint clone();

} // Constraint
