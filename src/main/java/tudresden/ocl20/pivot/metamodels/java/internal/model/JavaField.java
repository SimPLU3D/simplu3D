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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.log4j.Logger;

import tudresden.ocl20.pivot.metamodels.java.JavaMetaModelPlugin;
import tudresden.ocl20.pivot.pivotmodel.Property;
import tudresden.ocl20.pivot.pivotmodel.Type;
import tudresden.ocl20.pivot.pivotmodel.base.AbstractProperty;

/**
 * <p>
 * An implementation of the Pivot Model {@link Property} concept for Java.
 * </p>
 * 
 * @author Claas Wilke
 */
public class JavaField extends AbstractProperty implements Property {

	/** The {@link Logger} for this class. */
	private static final Logger LOGGER =
			JavaMetaModelPlugin.getLogger(JavaField.class);

	/** The {@link Field} data type. */
	private Field myField;

	/** The {@link JavaAdapterFactory} the {@link JavaField} belongs to. */
	private JavaAdapterFactory myFactory;

	/**
	 * <p>
	 * Creates a new {@link JavaField} instance.
	 * </p>
	 * 
	 * @param dslProperty
	 *          the {@link Field} that is adopted by this class.
	 * @param aFactory
	 *          The {@link JavaAdapterFactory}, the new created {@link JavaField}
	 *          shall belong to.
	 */
	public JavaField(Field dslProperty, JavaAdapterFactory aFactory) {

		/* Eventually log the entry into this method. */
		if (LOGGER.isDebugEnabled()) {
			String msg;

			msg = "JavaField(";
			msg += "dslProperty = " + dslProperty;
			msg += ", aFactory = " + aFactory;
			msg += ") - enter";

			LOGGER.debug(msg); //$NON-NLS-1$
		}
		// no else.

		/* Initialize. */
		this.myField = dslProperty;
		this.myFactory = aFactory;

		/* Eventually log the exit from this method. */
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("JavaField() - exit"); //$NON-NLS-1$
		}
		// no else.
	}

	/*
	 * (non-Javadoc)
	 * @see tudresden.ocl20.pivot.pivotmodel.base.AbstractProperty#getName()
	 */
	@Override
	public String getName() {

		return this.myField.getName();
	}

	/*
	 * (non-Javadoc)
	 * @see tudresden.ocl20.pivot.pivotmodel.base.AbstractProperty#getType()
	 */
	@Override
	public Type getType() {

		return this.myFactory.adaptJavaType(this.myField.getType(), this.myField
				.getGenericType());
	}

	/*
	 * (non-Javadoc)
	 * @see tudresden.ocl20.pivot.pivotmodel.base.AbstractProperty#getOwningType()
	 */
	@Override
	public Type getOwningType() {

		Type owner;

		owner = this.myFactory.createType(this.myField.getDeclaringClass());

		return owner;
	}

	/*
	 * (non-Javadoc)
	 * @see tudresden.ocl20.pivot.pivotmodel.impl.FeatureImpl#isStatic()
	 */
	@Override
	public boolean isStatic() {

		return Modifier.isStatic(this.myField.getModifiers());
	}
}