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
 * <em><b>Parameter Direction Kind</b></em>', and utility methods for working
 * with them. <!-- end-user-doc --> <!-- begin-model-doc -->
 * <p>
 * An enumeration type that defines literals used to specify direction of
 * {@link Parameter parameters}.
 * </p>
 * <!-- end-model-doc -->
 * 
 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getParameterDirectionKind()
 * @model
 * @generated
 */
public enum ParameterDirectionKind implements Enumerator {
	/**
	 * The '<em><b>In</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #IN_VALUE
	 * @generated
	 * @ordered
	 */
	IN(0, "in", "in"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Out</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #OUT_VALUE
	 * @generated
	 * @ordered
	 */
	OUT(1, "out", "out"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Inout</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #INOUT_VALUE
	 * @generated
	 * @ordered
	 */
	INOUT(2, "inout", "inout"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Return</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #RETURN_VALUE
	 * @generated
	 * @ordered
	 */
	RETURN(3, "return", "return"); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>In</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Indicates that parameter values are passed into the {@link Operation} by
	 * the caller.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @see #IN
	 * @model name="in"
	 * @generated
	 * @ordered
	 */
	public static final int IN_VALUE = 0;

	/**
	 * The '<em><b>Out</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Indicates that parameter values are passed from an {@link Operation} out to
	 * the caller.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @see #OUT
	 * @model name="out"
	 * @generated
	 * @ordered
	 */
	public static final int OUT_VALUE = 1;

	/**
	 * The '<em><b>Inout</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Indicates that parameter values are passed into an {@link Operation} by the
	 * caller and then back out to the caller from the {@link Operation}.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @see #INOUT
	 * @model name="inout"
	 * @generated
	 * @ordered
	 */
	public static final int INOUT_VALUE = 2;

	/**
	 * The '<em><b>Return</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Indicates that parameter values are passed as return values from an
	 * {@link Operation} back to the caller.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @see #RETURN
	 * @model name="return"
	 * @generated
	 * @ordered
	 */
	public static final int RETURN_VALUE = 3;

	/**
	 * An array of all the '<em><b>Parameter Direction Kind</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final ParameterDirectionKind[] VALUES_ARRAY =
			new ParameterDirectionKind[] { IN, OUT, INOUT, RETURN, };

	/**
	 * A public read-only list of all the '
	 * <em><b>Parameter Direction Kind</b></em>' enumerators. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List<ParameterDirectionKind> VALUES = Collections
			.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Parameter Direction Kind</b></em>' literal with the
	 * specified literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static ParameterDirectionKind get(String literal) {

		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ParameterDirectionKind result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Parameter Direction Kind</b></em>' literal with the
	 * specified name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static ParameterDirectionKind getByName(String name) {

		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ParameterDirectionKind result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Parameter Direction Kind</b></em>' literal with the
	 * specified integer value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static ParameterDirectionKind get(int value) {

		switch (value) {
		case IN_VALUE:
			return IN;
		case OUT_VALUE:
			return OUT;
		case INOUT_VALUE:
			return INOUT;
		case RETURN_VALUE:
			return RETURN;
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
	private ParameterDirectionKind(int value, String name, String literal) {

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

} // ParameterDirectionKind
