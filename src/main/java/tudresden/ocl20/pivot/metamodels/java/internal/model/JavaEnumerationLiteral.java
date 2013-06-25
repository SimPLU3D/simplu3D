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

import org.apache.log4j.Logger;

import tudresden.ocl20.pivot.metamodels.java.JavaMetaModelPlugin;
import tudresden.ocl20.pivot.pivotmodel.Enumeration;
import tudresden.ocl20.pivot.pivotmodel.EnumerationLiteral;
import tudresden.ocl20.pivot.pivotmodel.base.AbstractEnumerationLiteral;

/**
 * <p>
 * An implementation of the Pivot Model {@link EnumerationLiteral} concept for
 * Java.
 * </p>
 * 
 * @author Claas Wilkes
 */
public class JavaEnumerationLiteral extends AbstractEnumerationLiteral
		implements EnumerationLiteral {

	/** The {@link Logger} for this {@link Class}. */
	private static final Logger LOGGER =
			JavaMetaModelPlugin.getLogger(JavaEnumerationLiteral.class);

	/**
	 * <p>
	 * The adapted Java {@link Enum} which represents the
	 * {@link EnumerationLiteral}.
	 * </p>
	 * 
	 * @generated
	 */
	private Enum<?> myJavaEnumLiteral;

	/**
	 * The {@link JavaAdapterFactory} the {@link JavaEnumerationLiteral} belongs
	 * to.
	 */
	private JavaAdapterFactory myFactory;

	/**
	 * <p>
	 * Creates a new {@link JavaEnumerationLiteral} instance.
	 * </p>
	 * 
	 * @param adaptedEnum
	 *          The {@link Enum} that is adopted by this class
	 * 
	 * @param aFactory
	 *          The {@link JavaAdapterFactory}, the new created
	 *          {@link JavaEnumerationLiteral} shall belong to.
	 * @generated NOT
	 */
	public JavaEnumerationLiteral(Enum<?> adaptedEnum, JavaAdapterFactory aFactory) {

		/* Eventually log the entry into this method. */
		if (LOGGER.isDebugEnabled()) {
			String msg;

			msg = "JavaEnumerationLiteral(";
			msg += "dslEnumerationLiteral = " + adaptedEnum;
			msg += ", aFactory = " + aFactory;
			msg += ") - enter";

			LOGGER.debug(msg); //$NON-NLS-1$
		}
		// no else.

		/* Initialize. */
		this.myJavaEnumLiteral = adaptedEnum;
		this.myFactory = aFactory;

		/* Eventually log the exit from this method. */
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("JavaEnumerationLiteral() - exit"); //$NON-NLS-1$
		}
		// no else.
	}

	/*
	 * (non-Javadoc)
	 * @seetudresden.ocl20.pivot.pivotmodel.base.AbstractEnumerationLiteral#
	 * getEnumeration()
	 */
	public Enumeration getEnumeration() {

		return (Enumeration) this.myFactory.createType(this.myJavaEnumLiteral
				.getDeclaringClass());
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * tudresden.ocl20.pivot.pivotmodel.base.AbstractEnumerationLiteral#getName ()
	 */
	public String getName() {

		return this.myJavaEnumLiteral.name();
	}
}