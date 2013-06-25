/*
Copyright (C) 2009 by Claas Wilke (claaswilke@gmx.net)

This file is part of the Java Model Instance Plug-in of Dresden OCL2 for Eclipse.

Dresden OCL2 for Eclipse is free software: you can redistribute it and/or modify 
it under the terms of the GNU Lesser General Public License as published by the 
Free Software Foundation, either version 3 of the License, or (at your option)
any later version.

Dresden OCL2 for Eclipse is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License 
for more details.

You should have received a copy of the GNU Lesser General Public License along 
with Dresden OCL2 for Eclipse. If not, see <http://www.gnu.org/licenses/>.
 */
package tudresden.ocl20.pivot.modelinstancetype.java;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import tudresden.ocl20.logging.LoggingPlugin;
import tudresden.ocl20.pivot.pivotmodel.PrimitiveTypeKind;

/**
 * <p>
 * The activator class controls the plug-in life cycle.
 * </p>
 * 
 * @author Class Wilke
 */
public class JavaModelInstanceTypePlugin extends Plugin {

	/** The plug-in ID. */
	public static final String PLUGIN_ID =
			"tudresden.ocl20.pivot.modelinstancetype.java";

	/**
	 * An array containing all Java {@link Class}es that can be mapped to the
	 * {@link PrimitiveTypeKind#BOOLEAN}.
	 */
	public static final Class<?> BOOLEAN_CLASSES[] =
			new Class<?>[] { boolean.class, Boolean.class };

	/**
	 * An array containing all Java {@link Class}es that can be mapped to the
	 * {@link PrimitiveTypeKind#INTEGER}.
	 */
	public static final Class<?> INTEGER_CLASSES[] =
			new Class<?>[] { BigDecimal.class, BigInteger.class, byte.class,
					Byte.class, int.class, Integer.class, long.class, Long.class,
					short.class, Short.class };

	/**
	 * An array containing all Java {@link Class}es that can be mapped to the
	 * {@link PrimitiveTypeKind#REAL}.
	 */
	public static final Class<?> REAL_CLASSES[] =
			new Class<?>[] { double.class, Double.class, float.class, Float.class };

	/**
	 * An array containing all Java {@link Class}es that can be mapped to the
	 * {@link PrimitiveTypeKind#STRING}.
	 */
	public static final Class<?> STRING_CLASSES[] =
			new Class<?>[] { char.class, Character.class, String.class };

	/** The shared instance. */
	private static JavaModelInstanceTypePlugin plugin;

	/**
	 * @return The shared instance.
	 */
	public static JavaModelInstanceTypePlugin getDefault() {

		return plugin;
	}

	/**
	 * <p>
	 * Facade method for the classes in this plug-in that hides the dependency
	 * from the <code>tudresden.ocl20.logging</code> plug-in.
	 * </p>
	 * 
	 * @param clazz
	 *          The {@link Class} to return the {@link Logger} for.
	 * 
	 * @return A log4j {@link Logger}> instance.
	 * 
	 * @generated NOT
	 */
	public static Logger getLogger(Class<?> clazz) {

		return LoggingPlugin.getLogManager(plugin).getLogger(clazz);
	}

	/**
	 * <p>
	 * The constructor of the {@link JavaModelInstanceTypePlugin}.
	 * </p>
	 */
	public JavaModelInstanceTypePlugin() {

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {

		super.start(context);

		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {

		plugin = null;

		super.stop(context);
	}
}