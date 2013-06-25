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
 * <em><b>Type</b></em>'. <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc -->
 * <p>
 * A <code>Type</code> represents a set of values. A {@link TypedElement} that
 * has this type is constrained to represent values within this set. Contrary to
 * <code>Core::Basic::Type</code>, this is not an abstract metaclass. Instead,
 * the definition of a type in the pivot model is extended to what in many
 * metamodels is called <code>Classifier</code> or <code>Class</code>. In
 * particular, a <code>Type</code> can own {@link Property properties} and
 * {@link Operation operations}. It also may have super types.
 * </p>
 * <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.Type#getNamespace <em>Namespace
 * </em>}</li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.Type#getOwnedOperation <em>Owned
 * Operation</em>}</li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.Type#getOwnedProperty <em>Owned
 * Property</em>}</li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.Type#getSuperType <em>Super Type
 * </em>}</li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.Type#getGenericSuperType <em>
 * Generic Super Type</em>}</li>
 * </ul>
 * </p>
 * 
 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getType()
 * @model
 * @generated
 */
public interface Type extends NamedElement, ConstrainableElement,
		GenericElement {

	/**
	 * Returns the value of the '<em><b>Super Type</b></em>' reference list. The
	 * list contents are of type {@link tudresden.ocl20.pivot.pivotmodel.Type}.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * References the immediate supertypes of a <code>Type</code>, from which the
	 * type inherits.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Super Type</em>' reference list.
	 * @generated
	 */
	List<Type> getSuperType();

	/**
	 * Returns the value of the '<em><b>Generic Super Type</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link tudresden.ocl20.pivot.pivotmodel.GenericType}. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of the '<em>Generic Super Type</em>' containment reference
	 * list isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Generic Super Type</em>' containment
	 *         reference list.
	 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getType_GenericSuperType()
	 * @model containment="true"
	 * @generated
	 */
	List<GenericType> getGenericSuperType();

	/**
	 * Returns the value of the '<em><b>Owned Operation</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link tudresden.ocl20.pivot.pivotmodel.Operation}. It is bidirectional and
	 * its opposite is '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Operation#getOwningType
	 * <em>Owning Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 * <p>
	 * References the {@link Operation operations} owned by this <code>Type</code>
	 * . This is an ordered association.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Owned Operation</em>' containment reference
	 *         list.
	 * @see tudresden.ocl20.pivot.pivotmodel.Operation#getOwningType
	 * @generated
	 */
	List<Operation> getOwnedOperation();

	/**
	 * Returns the value of the '<em><b>Owned Property</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link tudresden.ocl20.pivot.pivotmodel.Property}. It is bidirectional and
	 * its opposite is '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Property#getOwningType
	 * <em>Owning Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 * <p>
	 * References the {@link Property properties} owned by this <code>Type</code>.
	 * This is an ordered association.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Owned Property</em>' containment reference
	 *         list.
	 * @see tudresden.ocl20.pivot.pivotmodel.Property#getOwningType
	 * @generated
	 */
	List<Property> getOwnedProperty();

	/**
	 * Returns the value of the '<em><b>Namespace</b></em>' container reference.
	 * It is bidirectional and its opposite is '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Namespace#getOwnedType
	 * <em>Owned Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 * <p>
	 * References the {@link Namespace} that contains this <code>Type</code>.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Namespace</em>' container reference.
	 * @see #setNamespace(Namespace)
	 * @see tudresden.ocl20.pivot.pivotmodel.Namespace#getOwnedType
	 * @generated
	 */
	Namespace getNamespace();

	/**
	 * Sets the value of the '
	 * {@link tudresden.ocl20.pivot.pivotmodel.Type#getNamespace
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
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * The query <code>conformsTo()</code> gives true for a type that conforms to
	 * another.
	 * 
	 * It is specified as follows:
	 * 
	 * <pre>
	 * context Type
	 * def: conformsTo(other: Type): Boolean =
	 *    (self=other) or (self.allParents()-&gt;includes(other))
	 * </pre>
	 * 
	 * where the query <code>allParents()</code> returns all of the direct and
	 * indirect ancestors of a type and is defined as:
	 * 
	 * <pre>
	 * context Type
	 * def: allParents(): Set(Type) =
	 *    self.superType-&gt;union(self.superType.allParents())
	 * </pre>
	 * 
	 * </p>
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @generated
	 */
	boolean conformsTo(Type other);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * The operation <code>commonSuperType</code> results in the most specific
	 * common supertype of two {@link Type types}.
	 * 
	 * It is specified as follows:
	 * 
	 * <pre>
	 * context Type
	 * def: commonSuperType (other : Type) : Type =
	 *    Type::allInstances()-&gt;select (cst |
	 *       other.conformsTo (cst) and
	 *       self.conformsTo (cst) and
	 *       not Type::allInstances()-&gt;exists (clst |
	 *          other.conformsTo (clst) and
	 *          self.conformsTo (clst) and
	 *          clst.conformsTo (cst) and
	 *          clst &lt;&gt; cst
	 *       )
	 *    )-&gt;any (true)
	 * </pre>
	 * 
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @generated
	 */
	Type commonSuperType(Type other);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Returns a {@link Property} of this <code>Type</code> with the given name.
	 * 
	 * It is specified as follows:
	 * 
	 * <pre>
	 * context Type
	 * def: lookupProperty(name : String) : Property =
	 *    self.allProperties()-&gt;any(p | p.name = name)
	 * </pre>
	 * 
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @generated
	 */
	Property lookupProperty(String name);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Returns an {@link Operation} of this <code>Type</code> with the given name
	 * and the given parameter types.
	 * 
	 * It is specified as follows:
	 * 
	 * <pre>
	 * context Type
	 * def: lookupOperation (name: String, paramTypes: Sequence(Type)): Operation =
	 *    self.allOperations()-&gt;any (op | op.name = name and
	 *       op.hasMatchingSignature(paramTypes))
	 * </pre>
	 * 
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @generated
	 */
	Operation lookupOperation(String name, List<Type> paramTypes);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Adds a {@link Property} to this <code>Type</code>. This is an additional
	 * operation in the Pivot Model to support properties defined via a
	 * {@link ConstraintKind#definition definition} constraint.The operation
	 * returns a reference to this <code>Type</code>.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @generated
	 */
	Type addProperty(Property property);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Adds an {@link Operation} to this <code>Type</code>. This is an additional
	 * operation in the Pivot Model to support operations defined via a
	 * {@link ConstraintKind#definition definition} constraint.The operation
	 * returns a reference to this <code>Type</code>.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @generated
	 */
	Type addOperation(Operation operation);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Adds a {@link Type} to the supertypes of this <code>Type</code>. This is an
	 * additional operation in the Pivot Model to support adding the OclAny type
	 * as a supertype. We cannot simply add it to the list returned by
	 * {@link #getSuperType()} because subclasses may override the behaviour of
	 * that list to adapt to custom repositories. The operation returns a
	 * reference to this <code>Type</code>.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @generated
	 */
	Type addSuperType(Type type);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Removes a {@link Property} from this <code>Type</code>. This is an
	 * additional operation in the Pivot Model to support removal of properties
	 * defined via a {@link ConstraintKind#definition definition} constraint. The
	 * operation returns a <code>boolean</code> indicating whether or not the
	 * {@link Property} has been removed successfully.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @model dataType="tudresden.ocl20.pivot.datatypes.Boolean" required="true"
	 * @generated
	 */
	boolean removeProperty(Property property);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Removes an {@link Operation} from this <code>Type</code>. This is an
	 * additional operation in the Pivot Model to support removal of operations
	 * defined via a {@link ConstraintKind#definition definition} constraint. The
	 * operation returns a <code>boolean</code> indicating whether or not the
	 * {@link Operation} has been removed successfully.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @model dataType="tudresden.ocl20.pivot.datatypes.Boolean" required="true"
	 * @generated
	 */
	boolean removeOperation(Operation operation);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Returns all properties which identify this <code>Type</code>. If no
	 * identifier property specify than an empty list will be return.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @model kind="operation"
	 * @generated
	 */
	List<Property> getIDProperties();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Returns all properties of this <code>Type</code> and its supertypes.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @generated
	 */
	List<Property> allProperties();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Returns all operations of this <code>Type</code> and its supertypes.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @generated
	 */
	List<Operation> allOperations();

	/**
	 * Overridden to specialize the co-variant return type to <code>Type</code>.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.GenericElement#bindTypeParameter(java.util.List,
	 *      java.util.List)
	 */
	Type bindTypeParameter(List<TypeParameter> parameters,
			List<? extends Type> types);

	/**
	 * Redefines {@link NamedElement#clone()} with a covariant return type.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.NamedElement#clone()
	 */
	Type clone();

} // Type
