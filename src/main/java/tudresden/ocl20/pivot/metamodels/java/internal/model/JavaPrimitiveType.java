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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import tudresden.ocl20.pivot.metamodels.java.JavaMetaModelPlugin;
import tudresden.ocl20.pivot.model.ModelConstants;
import tudresden.ocl20.pivot.pivotmodel.Namespace;
import tudresden.ocl20.pivot.pivotmodel.PrimitiveType;
import tudresden.ocl20.pivot.pivotmodel.PrimitiveTypeKind;
import tudresden.ocl20.pivot.pivotmodel.base.AbstractPrimitiveType;

/**
 * <p>
 * An implementation of the Pivot Model {@link PrimitiveType} concept for Java.
 * </p>
 * 
 * @author Claas Wilke
 */
public class JavaPrimitiveType extends AbstractPrimitiveType implements
		PrimitiveType {

	/** The {@link Logger} for this class. */
	private static final Logger LOGGER = JavaMetaModelPlugin
			.getLogger(JavaPrimitiveType.class);

	/** The adapted {@link Class} of this {@link PrimitiveType}. */
	private Class<?> myClass;

	/** The {@link JavaAdapterFactory} the {@link JavaPrimitiveType} belongs to. */
	private JavaAdapterFactory myFactory;

	/**
	 * <p>
	 * Creates a new {@link JavaPrimitiveType} instance.
	 * </p>
	 * 
	 * @param dslPrimitiveType
	 *            The {@link Class} that is adopted by this class.
	 * @param aFactory
	 *            The {@link JavaAdapterFactory} the {@link JavaPrimitiveType}
	 *            belongs to.
	 */
	public JavaPrimitiveType(Class<?> dslPrimitiveType,
			JavaAdapterFactory aFactory) {

		/* Eventually log the entry of this method. */
		if (LOGGER.isDebugEnabled()) {
			String msg;

			msg = "JavaPrimitiveType(";
			msg += "dslPrimitiveType = " + dslPrimitiveType;
			msg += "aFactory = " + aFactory;
			msg += ") - enter";

			LOGGER.debug(msg); //$NON-NLS-1$
		}
		// no else.

		/* Initialize. */
		this.myClass = dslPrimitiveType;
		this.myFactory = aFactory;

		/* Eventually log the exit from this method. */
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("JavaPrimitiveType() - exit"); //$NON-NLS-1$
		}
		// no else.
	}

	/**
	 * <p>
	 * Returns the {@link PrimitiveTypeKind} of a given Java {@link Class}. If
	 * the {@link PrimitiveTypeKind#UNKNOWN} is returned, the given
	 * {@link Class} cannot be adapted as a {@link PrimitiveType}.
	 * </p>
	 * 
	 * @param aClass
	 *            The {@link Class} whose {@link PrimitiveTypeKind} shall be
	 *            returned.
	 * @return The {@link PrimitiveTypeKind} of the given {@link Class} or
	 *         {@link PrimitiveTypeKind#UNKNOWN}.
	 */
	public static PrimitiveTypeKind getPrimitiveTypeKind(Class<?> aClass) {

		PrimitiveTypeKind result;

		result = null;

		/* Check if the adapted class is a boolean. */
		for (Class<?> aBooleanClass : JavaMetaModelPlugin.BOOLEAN_CLASSES) {

			if (aBooleanClass.isAssignableFrom(aClass)) {
				result = PrimitiveTypeKind.BOOLEAN;
				break;
			}
			// no else.
		}

		/* Else check if the adapted class is an integer. */
		if (result == null) {

			for (Class<?> anIntegerClass : JavaMetaModelPlugin.INTEGER_CLASSES) {

				if (anIntegerClass.isAssignableFrom(aClass)) {
					result = PrimitiveTypeKind.INTEGER;
					break;
				}
				// no else.
			}
		}

		/* Else check if the adapted class is a real. */
		if (result == null) {

			for (Class<?> aRealClass : JavaMetaModelPlugin.REAL_CLASSES) {

				if (aRealClass.isAssignableFrom(aClass)) {
					result = PrimitiveTypeKind.REAL;
					break;
				}
				// no else.
			}
		}

		/* Else check if the adapted class is a string. */
		if (result == null) {

			for (Class<?> aStringClass : JavaMetaModelPlugin.STRING_CLASSES) {

				if (aStringClass.isAssignableFrom(aClass)) {
					result = PrimitiveTypeKind.STRING;
					break;
				}
				// no else.
			}
		}

		/* Else return 'UNKNOWN'. */
		if (result == null) {
			result = PrimitiveTypeKind.UNKNOWN;
		}
		// no else.

		return result;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tudresden.ocl20.pivot.pivotmodel.base.AbstractPrimitiveType#getKind()
	 */
	@Override
	public PrimitiveTypeKind getKind() {

		PrimitiveTypeKind result;

		result = getPrimitiveTypeKind(this.myClass);

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tudresden.ocl20.pivot.pivotmodel.base.AbstractPrimitiveType#getName()
	 */
	@Override
	public String getName() {

		return this.myClass.getSimpleName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tudresden.ocl20.pivot.pivotmodel.base.AbstractPrimitiveType#getNamespace
	 * ()
	 */
	@Override
	public Namespace getNamespace() {

		Namespace result;

		String[] namespacePath;
		List<String> namespaceList;

		namespaceList = new ArrayList<String>();
		namespaceList.add(ModelConstants.ROOT_PACKAGE_NAME);

		/* Add all packages of the canonical name to the path. */
		namespacePath = this.myClass.getCanonicalName().split("\\.");

		for (int index = 0; index < namespacePath.length - 1; index++) {
			namespaceList.add(namespacePath[index]);
		}

		/* Create the name space. */
		result = this.myFactory.createNamespace(namespaceList);

		return result;
	}
}