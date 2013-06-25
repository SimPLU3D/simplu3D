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
package tudresden.ocl20.pivot.pivotmodel.impl;

import java.util.List;

import org.apache.commons.lang.NullArgumentException;

import tudresden.ocl20.pivot.pivotmodel.GenericElement;
import tudresden.ocl20.pivot.pivotmodel.Operation;
import tudresden.ocl20.pivot.pivotmodel.Parameter;
import tudresden.ocl20.pivot.pivotmodel.Type;
import tudresden.ocl20.pivot.pivotmodel.TypeParameter;
import tudresden.ocl20.pivot.pivotmodel.TypedElement;

/**
 * This class contains utility methods that are used by {@link GenericElement}s.
 * This is necessary because generic elements do not share a common superclass.
 * Due to overlapping inheritance hierachies, <code>GenericElement</code> is
 * only realized as a mixin interface.
 * 
 * @author Matthias Braeuer
 * @version 1.0 29.03.2007
 */
public class GenericElements {

	/**
	 * This class is not meant to be instantiated.
	 */
	private GenericElements() {

		// no implementation necessary
	}

	/**
	 * Checks the parameters for a binding and throws an exception if they are not
	 * valid.
	 * 
	 * @param parameters
	 *          the list of type parameters
	 * @param types
	 *          the list of types
	 */
	public static void checkBindingParameters(List<TypeParameter> parameters,
			List<? extends Type> types) {

		if (parameters == null || types == null) {
			throw new NullArgumentException("parameters or types"); //$NON-NLS-1$
		}

		if (parameters.size() != types.size()) {
			throw new IllegalArgumentException(
					"The list of type parameters must have the same size as the list of types."); //$NON-NLS-1$
		}

		if (parameters.contains(null) || types.contains(null)) {
			throw new IllegalArgumentException(
					"The lists with type parameters and types must not contain null values."); //$NON-NLS-1$
		}

	}

	/**
	 * Returns whether the given {@link TypedElement typed element} has a generic
	 * type amd no non-generic type. In other words, determines whether the typed
	 * element should be bound during a generic binding.
	 * 
	 * @param typedElement
	 *          the typed element
	 * 
	 * @return <code>true</code> if the typed element has a <code>null</code> type
	 *         and a non-<code>null</code> generic type
	 */
	public static boolean isGeneric(TypedElement typedElement) {

		return typedElement.getType() == null
				&& typedElement.getGenericType() != null;
	}

	/**
	 * Helper method that binds a {@link TypedElement}..
	 * 
	 * @param typedElement
	 *          the typed element
	 * @param parameters
	 *          the type parameters to bind
	 * @param types
	 *          the types to bind to the type parameters
	 */
	public static void bindTypedElement(TypedElement typedElement,
			List<TypeParameter> parameters, List<? extends Type> types) {

		if (isGeneric(typedElement)) {
			typedElement.getGenericType().bindGenericType(parameters, types,
					typedElement);
		}

	}

	/**
	 * Helper method to bind an operation.
	 * 
	 * @param operation
	 *          the operation to bind
	 * @param parameters
	 *          the type parameters to bind
	 * @param types
	 *          the types to bind to the type parameters
	 */
	public static void bindOperation(Operation operation,
			List<TypeParameter> parameters, List<? extends Type> types) {

		// bind the parameters of the operation
		for (Parameter parameter : operation.getOwnedParameter()) {
			GenericElements.bindTypedElement(parameter, parameters, types);
		}

		// bind the type of the operation
		GenericElements.bindTypedElement(operation, parameters, types);

	}
}
