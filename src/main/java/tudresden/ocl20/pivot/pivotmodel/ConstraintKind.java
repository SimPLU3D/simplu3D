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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '
 * <em><b>Constraint Kind</b></em>', and utility methods for working with them.
 * <!-- end-user-doc --> <!-- begin-model-doc -->
 * <p>
 * A <code>ConstraintKind</code> determines the semantics of a
 * {@link Constraint}.
 * </p>
 * <!-- end-model-doc -->
 * 
 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getConstraintKind()
 * @model
 * @generated
 */
public enum ConstraintKind implements Enumerator {
	/**
	 * The '<em><b>Invariant</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #INVARIANT_VALUE
	 * @generated
	 * @ordered
	 */
	INVARIANT(0, "invariant", "invariant"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Definition</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #DEFINITION_VALUE
	 * @generated
	 * @ordered
	 */
	DEFINITION(1, "definition", "definition"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Precondition</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #PRECONDITION_VALUE
	 * @generated
	 * @ordered
	 */
	PRECONDITION(2, "precondition", "precondition"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Postcondition</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #POSTCONDITION_VALUE
	 * @generated
	 * @ordered
	 */
	POSTCONDITION(3, "postcondition", "postcondition"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Initial</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #INITIAL_VALUE
	 * @generated
	 * @ordered
	 */
	INITIAL(4, "initial", "initial"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Derived</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #DERIVED_VALUE
	 * @generated
	 * @ordered
	 */
	DERIVED(5, "derived", "derived"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Body</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #BODY_VALUE
	 * @generated
	 * @ordered
	 */
	BODY(6, "body", "body"); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Invariant</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * An invariant constraint is linked to a {@link Type}. It specifies
	 * invariants for the type, i.e. boolean conditions that must be true upon
	 * completion of every public operation, but not necessarily during the
	 * execution of operations.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @see #INVARIANT
	 * @model name="invariant"
	 * @generated
	 * @ordered
	 */
	public static final int INVARIANT_VALUE = 0;

	/**
	 * The '<em><b>Definition</b></em>' literal value. <!-- begin-user-doc -->
	 * <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * A definition constraint is linked to a {@link Type} and is used to define
	 * {@link Property properties} or {@link Operation operations} for that type.
	 * Defining a property or operation this way means that every instance of the
	 * type holds a property or operation that conforms to the given definition.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @see #DEFINITION
	 * @model name="definition"
	 * @generated
	 * @ordered
	 */
	public static final int DEFINITION_VALUE = 1;

	/**
	 * The '<em><b>Precondition</b></em>' literal value. <!-- begin-user-doc -->
	 * <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * A precondition is a boolean expression that must be true at the moment when
	 * an {@link Operation} starts its execution; otherwise, the operation will
	 * not be executed.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @see #PRECONDITION
	 * @model name="precondition"
	 * @generated
	 * @ordered
	 */
	public static final int PRECONDITION_VALUE = 2;

	/**
	 * The '<em><b>Postcondition</b></em>' literal value. <!-- begin-user-doc -->
	 * <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * A postcondition is a boolean expression that must be true at the moment
	 * when an {@link Operation} ends its execution; otherwise, the operation has
	 * not executed correctly.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @see #POSTCONDITION
	 * @model name="postcondition"
	 * @generated
	 * @ordered
	 */
	public static final int POSTCONDITION_VALUE = 3;

	/**
	 * The '<em><b>Initial</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * A constraint with the kind <em>initial</em> specifies the value of a
	 * {@link Property} at the moment an instance of a {@link Type} is created.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @see #INITIAL
	 * @model name="initial"
	 * @generated
	 * @ordered
	 */
	public static final int INITIAL_VALUE = 4;

	/**
	 * The '<em><b>Derived</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * A derivation rule specifies that the value of a {@link Property} should
	 * always be equal to the value given by the constraint expression.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @see #DERIVED
	 * @model name="derived"
	 * @generated
	 * @ordered
	 */
	public static final int DERIVED_VALUE = 5;

	/**
	 * The '<em><b>Body</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * An {@link Operation} can be fully defined by specifying the result of the
	 * operation in a single expression. Commonly, operations specified using a
	 * body constraint do not have side effects; i.e. they are pure query
	 * operations returning a value or a set of values.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @see #BODY
	 * @model name="body"
	 * @generated
	 * @ordered
	 */
	public static final int BODY_VALUE = 6;

	/**
	 * An array of all the '<em><b>Constraint Kind</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final ConstraintKind[] VALUES_ARRAY = new ConstraintKind[] {
			INVARIANT, DEFINITION, PRECONDITION, POSTCONDITION, INITIAL, DERIVED,
			BODY, };

	/**
	 * A public read-only list of all the '<em><b>Constraint Kind</b></em>'
	 * enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List<ConstraintKind> VALUES = Collections
			.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Constraint Kind</b></em>' literal with the specified
	 * literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static ConstraintKind get(String literal) {

		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ConstraintKind result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Constraint Kind</b></em>' literal with the specified
	 * name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static ConstraintKind getByName(String name) {

		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ConstraintKind result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Constraint Kind</b></em>' literal with the specified
	 * integer value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static ConstraintKind get(int value) {

		switch (value) {
		case INVARIANT_VALUE:
			return INVARIANT;
		case DEFINITION_VALUE:
			return DEFINITION;
		case PRECONDITION_VALUE:
			return PRECONDITION;
		case POSTCONDITION_VALUE:
			return POSTCONDITION;
		case INITIAL_VALUE:
			return INITIAL;
		case DERIVED_VALUE:
			return DERIVED;
		case BODY_VALUE:
			return BODY;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	private ConstraintKind(int value, String name, String literal) {

		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getValue() {

		return value;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getName() {

		return name;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getLiteral() {

		return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string
	 * representation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {

		return literal;
	}

} // ConstraintKind
