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

package tudresden.ocl20.pivot.modelbus;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import tudresden.ocl20.logging.LoggingPlugin;
import tudresden.ocl20.pivot.model.IModelRegistry;
import tudresden.ocl20.pivot.model.metamodel.IMetamodelRegistry;
import tudresden.ocl20.pivot.modelbus.metamodel.internal.MetamodelRegistry;
import tudresden.ocl20.pivot.modelbus.model.internal.ModelRegistry;
import tudresden.ocl20.pivot.modelbus.modelinstance.internal.ModelInstanceRegistry;
import tudresden.ocl20.pivot.modelbus.modelinstance.internal.ModelInstanceTypeRegistry;
import tudresden.ocl20.pivot.modelinstance.IModelInstanceRegistry;
import tudresden.ocl20.pivot.modelinstance.IModelInstanceTypeRegistry;

/**
 * <p>
 * The activator class controls the plug-in life cycle.
 * </p>
 * 
 * @author Matthias Braeuer
 */
public class ModelBusPlugin extends Plugin {

	/** The plug-in ID. */
	public static final String ID = "tudresden.ocl20.pivot.modelbus"; //$NON-NLS-1$

	/** The shared instance. */
	private static ModelBusPlugin plugin;

	/** The registry of registered meta-models. */
	private IMetamodelRegistry metamodelRegistry;

	/** The registry for models. */
	private IModelRegistry modelRegistry;

	/** The registry for model instances. */
	private IModelInstanceRegistry modelInstanceRegistry;

	/** The registry of registered model instance types. */
	private IModelInstanceTypeRegistry modelInstanceTypeRegistry;

	/**
	 * <p>
	 * Creates the {@link ModelBusPlugin}.
	 * </p>
	 */
	public ModelBusPlugin() {

		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext context) throws Exception {

		super.start(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void stop(BundleContext context) throws Exception {

		/* Dispose the meta-model registry. */
		if (this.metamodelRegistry != null) {
			this.metamodelRegistry.dispose();
			this.metamodelRegistry = null;
		}
		// no else.

		/* dispose the model registry. */
		if (this.modelRegistry != null) {
			this.modelRegistry.dispose();
			this.modelRegistry = null;
		}
		// no else.

		/* dispose the model instance type registry. */
		if (this.modelInstanceTypeRegistry != null) {
			this.modelInstanceTypeRegistry.dispose();
			this.modelInstanceTypeRegistry = null;
		}
		// no else.

		/* dispose the model instance registry. */
		if (this.modelInstanceRegistry != null) {
			this.modelInstanceRegistry.dispose();
			this.modelInstanceRegistry = null;
		}
		// no else.

		plugin = null;
		super.stop(context);
	}

	/**
	 * <p>
	 * Returns the shared instance.
	 * </p>
	 * 
	 * @return The shared instance.
	 */
	public static ModelBusPlugin getDefault() {

		return plugin;
	}

	/**
	 * <p>
	 * Returns the {@link IMetamodelRegistry} managed by the
	 * {@link ModelBusPlugin}.
	 * </p>
	 * <p>
	 * If no {@link IMetamodelRegistry} has been given to the
	 * {@link ModelBusPlugin}, the standard {@link MetamodelRegistry} will be
	 * used. This is important when using DresdenOCL stand-alone, as the
	 * {@link StandaloneMetamodelRegistry} should be set first, using
	 * {@link #setMetamodelRegistry(IMetamodelRegistry)}.
	 * </p>
	 * 
	 * @see #setMetamodelRegistry(IMetamodelRegistry)
	 * 
	 * @return An {@link IMetamodelRegistry} instance.
	 */
	public synchronized static IMetamodelRegistry getMetamodelRegistry() {

		/* Check that the plugin has been activated. */
		if (plugin == null) {
			// throw new IllegalStateException(
			//					"The Model plugin has not been activated."); //$NON-NLS-1$
			plugin = new ModelBusPlugin();
			System.out
					.println("The Model plugin has not been activated. Intialized it manually.");
		}

		/* Lazily create the registry. */
		if (plugin.metamodelRegistry == null) {
			plugin.metamodelRegistry = new MetamodelRegistry();
		}

		return plugin.metamodelRegistry;
	}

	/**
	 * Sets the {@link IMetamodelRegistry} of the {@link ModelBusPlugin}. This
	 * method has to be called when using DresdenOCL stand-alone. The standard
	 * argument should be {@link StandaloneMetamodelRegistry}.
	 * 
	 * @param metamodelRegistry
	 *            the {@link IMetamodelRegistry} to set
	 */
	public synchronized static void setMetamodelRegistry(
			IMetamodelRegistry metamodelRegistry) {

		/* Check that the plugin has been activated. */
		if (plugin == null) {
			throw new IllegalStateException(
					"The Model Bus plugin has not been activated."); //$NON-NLS-1$
		}

		plugin.metamodelRegistry = metamodelRegistry;
	}

	/**
	 * <p>
	 * Returns the {@link IModelRegistry} managed by the {@link ModelBusPlugin}.
	 * </p>
	 * 
	 * @return An {@link IModelRegistry} instance.
	 */
	public synchronized static IModelRegistry getModelRegistry() {

		/* Check that the plugin has been activated. */
		if (plugin == null) {
			throw new IllegalStateException(
					"The Model Bus plugin has not been activated."); //$NON-NLS-1$
		}
		// no else.

		/* Probably lazily create the registry. */
		if (plugin.modelRegistry == null) {
			plugin.modelRegistry = new ModelRegistry();
		}

		return plugin.modelRegistry;
	}

	/**
	 * <p>
	 * Returns the {@link IModelInstanceRegistry} managed by the
	 * {@link ModelBusPlugin}.
	 * </p>
	 * 
	 * @return An {@link IModelInstanceRegistry} instance.
	 */
	public synchronized static IModelInstanceRegistry getModelInstanceRegistry() {

		if (plugin == null) {
			throw new IllegalStateException(
					"The Model Bus plugin has not been activated."); //$NON-NLS-1$
		}
		// no else.

		/* Lazily create the registry. */
		if (plugin.modelInstanceRegistry == null) {
			plugin.modelInstanceRegistry = new ModelInstanceRegistry();
			ModelBusPlugin.getModelRegistry().addModelRegistryListener(
					plugin.modelInstanceRegistry);
		}
		// no else.

		return plugin.modelInstanceRegistry;
	}

	/**
	 * <p>
	 * Returns the {@link IModelInstanceTypeRegistry} managed by the
	 * {@link ModelBusPlugin}.
	 * </p>
	 * 
	 * @return An {@link IModelInstanceTypeRegistry} instance.
	 */
	public synchronized static IModelInstanceTypeRegistry getModelInstanceTypeRegistry() {

		/* Check that the plug-in has been activated. */
		if (plugin == null) {
			throw new IllegalStateException(
					"The Model Bus plug-in has not been activated.");
		}
		// no else.

		/* Else lazily create the registry. */
		else if (plugin.modelInstanceTypeRegistry == null) {
			plugin.modelInstanceTypeRegistry = new ModelInstanceTypeRegistry();
		}
		// no else.

		return plugin.modelInstanceTypeRegistry;
	}

	/**
	 * <p>
	 * Facade method for the classes in this plug-in that hides the dependency
	 * from the <code>tudresden.ocl20.logging</code> plug-in.
	 * </p>
	 * 
	 * @param clazz
	 *            the class to return the logger for
	 * 
	 * @return a log4j <code>Logger</code> instance
	 */
	public static Logger getLogger(Class<?> clazz) {

		return LoggingPlugin.getLogManager(plugin).getLogger(clazz);
	}
}