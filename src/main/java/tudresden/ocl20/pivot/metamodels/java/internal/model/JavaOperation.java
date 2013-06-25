/*
 * Copyright (C) 2009 by Claas Wilke (claaswilke@gmx.net) This file is part of
 * the Java Meta Model of Dresden OCL2 for Eclipse. Dresden OCL2 for Eclipse is
 * free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. Dresden OCL2 for Eclipse is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details. You should have received a copy of
 * the GNU Lesser General Public License along with Dresden OCL2 for Eclipse. If
 * not, see <http://www.gnu.org/licenses/>.
 */
package tudresden.ocl20.pivot.metamodels.java.internal.model;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import org.apache.log4j.Logger;

import tudresden.ocl20.pivot.metamodels.java.JavaMetaModelPlugin;
import tudresden.ocl20.pivot.pivotmodel.Operation;
import tudresden.ocl20.pivot.pivotmodel.Parameter;
import tudresden.ocl20.pivot.pivotmodel.ParameterDirectionKind;
import tudresden.ocl20.pivot.pivotmodel.Type;
import tudresden.ocl20.pivot.pivotmodel.base.AbstractOperation;

/**
 * <p>
 * An implementation of the Pivot Model {@link Operation} concept for UML2.
 * </p>
 */
public class JavaOperation extends AbstractOperation implements Operation {

	/** The {@link Logger} for this class. */
	private static final Logger LOGGER = JavaMetaModelPlugin
			.getLogger(JavaOperation.class);

	/** The adapted {@link Method}. */
	private Method myMethod;

	/** The {@link JavaAdapterFactory} the {@link JavaOperation} belongs to. */
	private JavaAdapterFactory myFactory;

	/**
	 * <p>
	 * Creates a new {@link JavaOperation} instance.
	 * </p>
	 * 
	 * @param dslOperation
	 *            The {@link Method} that is adopted by this class.
	 * @param aFactory
	 *            The {@link JavaAdapterFactory}, the new created
	 *            {@link JavaOperation} shall belong to.
	 */
	public JavaOperation(Method dslOperation, JavaAdapterFactory aFactory) {

		/* Eventually log the entry into this method. */
		if (LOGGER.isDebugEnabled()) {
			String msg;

			msg = "JavaOperation(";
			msg += "dslOperation = " + dslOperation;
			msg += ", aFactory = " + aFactory;
			msg += ") - enter";

			LOGGER.debug(msg); //$NON-NLS-1$
		}
		// no else.

		/* Initialize. */
		this.myMethod = dslOperation;
		this.myFactory = aFactory;

		/* Eventually log the exit from this metod. */
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("JavaOperation() - exit"); //$NON-NLS-1$
		}
		// no else.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.base.AbstractOperation#getName()
	 */
	@Override
	public String getName() {

		return this.myMethod.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tudresden.ocl20.pivot.pivotmodel.base.AbstractOperation#getOwnedParameter
	 * ()
	 */
	@Override
	public List<Parameter> getOwnedParameter() {

		List<Parameter> result;

		result = this.myFactory.createInputParameters(this.myMethod);

		result.add(this.getReturnParameter());

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tudresden.ocl20.pivot.pivotmodel.base.AbstractOperation#getOwningType()
	 */
	@Override
	public Type getOwningType() {

		Type owner;

		owner = this.myFactory.createType(this.myMethod.getDeclaringClass());

		return owner;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.OperationImpl#getType()
	 */
	@Override
	public Type getType() {

		return this.myFactory.adaptJavaType(this.myMethod.getReturnType(),
				this.myMethod.getGenericReturnType());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tudresden.ocl20.pivot.pivotmodel.impl.OperationImpl#getReturnParameter()
	 */
	public Parameter getReturnParameter() {

		return this.myFactory.createParameter(this.myMethod.getReturnType(),
				this.myMethod.getGenericReturnType(),
				ParameterDirectionKind.RETURN, this.myMethod, 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.FeatureImpl#isStatic()
	 */
	public boolean isStatic() {

		return Modifier.isStatic(this.myMethod.getModifiers());
	}
}