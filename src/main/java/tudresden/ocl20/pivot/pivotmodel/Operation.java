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
 * <em><b>Operation</b></em>'. <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc -->
 * <p>
 * An operation is a behavioral feature of a {@link Type} that specifies the
 * name, type, parameters, and constraints for invoking an associated behavior.
 * </p>
 * <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.Operation#getOwningType <em>
 * Owning Type</em>}</li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.Operation#getOwnedParameter <em>
 * Owned Parameter</em>}</li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.Operation#getInputParameter <em>
 * Input Parameter</em>}</li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.Operation#getOutputParameter <em>
 * Output Parameter</em>}</li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.Operation#getReturnParameter <em>
 * Return Parameter</em>}</li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.Operation#getSignatureParameter
 * <em>Signature Parameter</em>}</li>
 * </ul>
 * </p>
 * 
 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getOperation()
 * @model
 * @generated
 */
public interface Operation extends Feature, TypedElement, NamedElement,
		ConstrainableElement, GenericElement {

	/**
	 * Returns the value of the '<em><b>Owning Type</b></em>' container reference.
	 * It is bidirectional and its opposite is '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Type#getOwnedOperation
	 * <em>Owned Operation</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * <p>
	 * Specifies the owner of this <code>Operation</code>.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Owning Type</em>' container reference.
	 * @see #setOwningType(Type)
	 * @see tudresden.ocl20.pivot.pivotmodel.Type#getOwnedOperation
	 * @generated
	 */
	Type getOwningType();

	/**
	 * Sets the value of the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Operation#getOwningType
	 * <em>Owning Type</em>}' container reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *          the new value of the '<em>Owning Type</em>' container reference.
	 * @see #getOwningType()
	 * @generated
	 */
	void setOwningType(Type value);

	/**
	 * Returns the value of the '<em><b>Owned Parameter</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link tudresden.ocl20.pivot.pivotmodel.Parameter}. It is bidirectional and
	 * its opposite is '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Parameter#getOperation
	 * <em>Operation</em>}'. <!-- begin-user-doc --> <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 * <p>
	 * Specifies the {@link Parameter parameters} owned by this
	 * <code>Operation</code>.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Owned Parameter</em>' containment reference
	 *         list.
	 * @see tudresden.ocl20.pivot.pivotmodel.Parameter#getOperation
	 * @generated
	 */
	List<Parameter> getOwnedParameter();

	/**
	 * Returns the value of the '<em><b>Input Parameter</b></em>' reference list.
	 * The list contents are of type
	 * {@link tudresden.ocl20.pivot.pivotmodel.Parameter}. <!-- begin-user-doc -->
	 * <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * This is the subset of all parameters with direction
	 * {@link ParameterDirectionKind#IN in} or
	 * {@link ParameterDirectionKind#INOUT inout}.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Input Parameter</em>' reference list.
	 * @generated
	 */
	List<Parameter> getInputParameter();

	/**
	 * Returns the value of the '<em><b>Output Parameter</b></em>' reference list.
	 * The list contents are of type
	 * {@link tudresden.ocl20.pivot.pivotmodel.Parameter}. <!-- begin-user-doc -->
	 * <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * This is the subset of all parameters with direction
	 * {@link ParameterDirectionKind#OUT out} or
	 * {@link ParameterDirectionKind#INOUT inout}.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Output Parameter</em>' reference list.
	 * @generated
	 */
	List<Parameter> getOutputParameter();

	/**
	 * Returns the value of the '<em><b>Signature Parameter</b></em>' reference
	 * list. The list contents are of type
	 * {@link tudresden.ocl20.pivot.pivotmodel.Parameter}. <!-- begin-user-doc -->
	 * <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * This is the subset of all parameters except the
	 * {@link ParameterDirectionKind#RETURN return} parameter.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Signature Parameter</em>' reference list.
	 * @generated
	 */
	List<Parameter> getSignatureParameter();

	/**
	 * Returns the value of the '<em><b>Return Parameter</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * References the return parameter of this <code>Operation</code>, if
	 * existent.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Return Parameter</em>' reference.
	 * @generated
	 */
	Parameter getReturnParameter();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * An additional operation defined in the OCL specification (Section 8.3.8)
	 * which checks whether the <code>Operation</code>'s signature matches with a
	 * sequence of {@link Type types}. Note that in making the match only
	 * parameters with {@link ParameterDirectionKind direction kind}
	 * <code>in</code> or <code>inout</code> are considered.
	 * 
	 * The operation is specified as follows:
	 * 
	 * <pre>
	 * context Operation
	 * def: hasMatchingSignature(paramTypes: Sequence(Type)) : Boolean =
	 *    let sigParamTypes: Sequence(Type) =
	 *       self.inParameter.union(self.inoutParameter).type in
	 *    (
	 *       ( sigParamTypes-&gt;size() = paramTypes-&gt;size() ) and
	 *       ( Set{1..paramTypes-&gt;size()}-&gt;forAll ( i |
	 *          paramTypes-&gt;at(i).conformsTo(sigParamTypes-&gt;at(i)) )
	 *       )
	 *    )
	 * </pre>
	 * 
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @generated
	 */
	boolean hasMatchingSignature(List<Type> paramTypes);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Adds a {@link Parameter} to this operation. This is required to define new
	 * operations via a {@link ConstraintKind#definition definition} constraint.
	 * The operation returns a reference to this <code>Operation</code>.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @generated
	 */
	Operation addParameter(Parameter param);

	/**
	 * Overridden to specialize the co-variant return type to
	 * <code>Operation</code>.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.GenericElement#bindTypeParameter(java.util.List,
	 *      java.util.List)
	 */
	Operation bindTypeParameter(List<TypeParameter> parameters,
			List<? extends Type> types);

	/**
	 * Redefines {@link NamedElement#clone()} with a covariant return type.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.NamedElement#clone()
	 */
	Operation clone();

} // Operation
