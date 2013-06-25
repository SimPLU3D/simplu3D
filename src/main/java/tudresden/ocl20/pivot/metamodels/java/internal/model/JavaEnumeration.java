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
import org.eclipse.emf.common.util.BasicEList;

import tudresden.ocl20.pivot.metamodels.java.JavaMetaModelPlugin;
import tudresden.ocl20.pivot.model.ModelConstants;
import tudresden.ocl20.pivot.pivotmodel.Enumeration;
import tudresden.ocl20.pivot.pivotmodel.EnumerationLiteral;
import tudresden.ocl20.pivot.pivotmodel.Namespace;
import tudresden.ocl20.pivot.pivotmodel.base.AbstractEnumeration;

/**
 * <p>
 * An implementation of the Pivot Model {@link Enumeration} concept for Java.
 * </p>
 * 
 * @author Claas Wilke
 */
public class JavaEnumeration extends AbstractEnumeration implements Enumeration {

	/** The {@link Logger} for this {@link Class}. */
	private static final Logger LOGGER =
			JavaMetaModelPlugin.getLogger(JavaEnumeration.class);

	/** The adapted {@link Class} data type. */
	private Class<?> myJavaEnum;

	/** The {@link JavaAdapterFactory} the {@link JavaEnumeration} belongs to. */
	private JavaAdapterFactory myFactory;

	/**
	 * <p>
	 * Creates a new {@link JavaEnumeration} instance.
	 * </p>
	 * 
	 * @param dslEnumeration
	 *          The {@link Class} that is adopted by this {@link Class}.
	 * @param aFactory
	 *          The {@link JavaAdapterFactory}, the new created
	 *          {@link JavaEnumeration} shall belong to.
	 */
	public JavaEnumeration(Class<?> dslEnumeration, JavaAdapterFactory aFactory) {

		/* Eventually log the entry into this method. */
		if (LOGGER.isDebugEnabled()) {
			String msg;

			msg = "JavaEnumeration(";
			msg += "dslEnumeration = " + dslEnumeration;
			msg += ", aFactory = " + aFactory;
			msg += ") - enter";

			LOGGER.debug(msg); //$NON-NLS-1$
		}
		// no else.

		/* Initialize. */
		this.myJavaEnum = dslEnumeration;
		this.myFactory = aFactory;

		/* Eventually log the exit from this method. */
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("JavaEnumeration() - exit"); //$NON-NLS-1$
		}
		// no else.
	}

	/*
	 * (non-Javadoc)
	 * @see tudresden.ocl20.pivot.pivotmodel.base.AbstractEnumeration#getName()
	 */
	public String getName() {

		return this.myJavaEnum.getSimpleName();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * tudresden.ocl20.pivot.pivotmodel.base.AbstractEnumeration#getNamespace()
	 */
	@Override
	public Namespace getNamespace() {

		Namespace result;

		String[] namespacePath;
		List<String> namespaceList;

		namespaceList = new ArrayList<String>();
		namespaceList.add(ModelConstants.ROOT_PACKAGE_NAME);

		/* Add all packages of the canonical name to the path. */
		namespacePath = this.myJavaEnum.getCanonicalName().split("\\.");

		for (int index = 0; index < namespacePath.length - 1; index++) {
			namespaceList.add(namespacePath[index]);
		}

		/* Create the name space. */
		result = this.myFactory.createNamespace(namespaceList);

		return result;
	}

	/**
	 * @see tudresden.ocl20.pivot.pivotmodel.base.AbstractEnumeration#getOwnedLiteral()
	 * 
	 * @generated NOT
	 */
	@Override
	public List<EnumerationLiteral> getOwnedLiteral() {

		List<EnumerationLiteral> result;

		result = new BasicEList<EnumerationLiteral>();

		for (Enum<?> aLiteral : (Enum<?>[]) this.myJavaEnum.getEnumConstants()) {
			result.add(this.myFactory.createEnumerationLiteral(aLiteral));
		}

		return result;
	}
}