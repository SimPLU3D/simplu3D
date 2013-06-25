/*
Copyright (C) 2009 by Claas Wilke (claaswilke@gmx.net)

This file is part of the Model Bus Plug-in of Dresden OCL2 for Eclipse.

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
package tudresden.ocl20.pivot.modelbus.modelinstance.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.IllegalClassException;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IRegistryEventListener;
import org.eclipse.core.runtime.Platform;

import tudresden.ocl20.pivot.modelbus.IModelBusConstants;
import tudresden.ocl20.pivot.modelbus.ModelBusPlugin;
import tudresden.ocl20.pivot.modelbus.descriptor.IDescriptor;
import tudresden.ocl20.pivot.modelbus.descriptor.InvalidDescriptorException;
import tudresden.ocl20.pivot.modelinstance.IModelInstanceType;
import tudresden.ocl20.pivot.modelinstance.IModelInstanceTypeRegistry;

/**
 * <p>
 * The standard implementation of the {@link IModelInstanceTypeRegistry}
 * interface that uses a simple list for storing references to
 * {@link IModelInstanceType}s.
 * 
 * @author Claas Wilke
 */
public class ModelInstanceTypeRegistry implements IModelInstanceTypeRegistry,
		IRegistryEventListener {

	/** The full identifier of the fileFormats extension point. */
	private static final String MODEL_INSTANCE_TYPE_EXTENSION_POINT_ID = ModelBusPlugin.ID
			+ '.' + IModelBusConstants.EXT_MODELINSTANCETYPES;

	/** The {@link Logger} for this class. */
	private static final Logger LOGGER = ModelBusPlugin
			.getLogger(ModelInstanceTypeRegistry.class);

	/** The registered {@link IModelInstanceType}s mapped by their id. */
	private Map<String, IModelInstanceType> modelInstanceTypes;

	/**
	 * A helper class to read the {@link ModelInstanceTypeRegistry}
	 * configuration from the Eclipse extension registry.
	 */
	private ModelInstanceTypeRegistryReader reader;

	/**
	 * <p>
	 * Creates a new {@link ModelInstanceTypeRegistry}.
	 * </p>
	 */
	public ModelInstanceTypeRegistry() {

		/* Eventually log the entry of this method. */
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ModelInstanceFileFormatRegistry() - enter");
		}
		// no else.

		/* Create a new reader and read in the configuration. */
		this.reader = new ModelInstanceTypeRegistryReader();
		IExtensionPoint extensionPoint = this.getExtensionPoint();
		if (extensionPoint != null)
			this.reader.read(this.getExtensionPoint(), this);
		// no else.

		/* Register the registry as a listener for plug-in events. */
		if (Platform.isRunning())
			Platform.getExtensionRegistry().addListener(this,
					MODEL_INSTANCE_TYPE_EXTENSION_POINT_ID);
		// no else.

		/* Eventually log the exit of this method. */
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ModelInstanceFileFormatRegistry() - exit");
		}
		// no else.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.IRegistryEventListener#added(org.eclipse.core.
	 * runtime.IExtension[])
	 */
	public void added(IExtension[] extensions) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("added(extensions=" + extensions + ") - enter"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		// no else.

		/* Use the registry reader to read in the new extension. */
		for (IExtension extension : extensions) {
			this.reader.read(extension, this);
		}
		// no else.

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("added(IExtension[]) - exit"); //$NON-NLS-1$
		}
		// no else.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.IRegistryEventListener#added(org.eclipse.core.
	 * runtime.IExtensionPoint[])
	 */
	public void added(IExtensionPoint[] extensionPoints) {

		/* Do nothing. Only listen for extensions. */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seetudresden.ocl20.pivot.modelbus.IModelInstanceTypeRegistry#
	 * addModelInstanceType(tudresden.ocl20.pivot.modelbus.IModelInstanceType)
	 */
	public void addModelInstanceType(IModelInstanceType modelInstanceType) {

		/* Probably log the entry of this method. */
		if (LOGGER.isDebugEnabled()) {
			String msg;

			msg = "addModelInstanceType(miType=" + modelInstanceType
					+ ") - enter";

			LOGGER.debug(msg);
		}
		// no else.

		/* Check if the miType is null. */
		if (modelInstanceType == null) {
			String msg;

			msg = "The parameter 'miType' must not be null.";

			throw new IllegalArgumentException(msg);
		}

		/* Else lazily create the list of Model Instance Types. */
		if (this.modelInstanceTypes == null) {
			this.modelInstanceTypes = new HashMap<String, IModelInstanceType>();
		}
		// no else.

		/*
		 * Else check if the file format is already contained in the registry.
		 * Than silently do nothing.
		 */
		else if (this.modelInstanceTypes.containsKey(modelInstanceType.getId())) {
			String msg;

			msg = "ModelInstanceType '" + modelInstanceType.getName()
					+ "' is already loaded.";

			throw new IllegalStateException(msg);
		}

		/* Add the file format. */
		this.modelInstanceTypes.put(modelInstanceType.getId(),
				modelInstanceType);

		/* Probably log the exit of this method. */
		if (LOGGER.isDebugEnabled()) {
			String msg;

			msg = "addModelInstanceType() - exit";

			LOGGER.debug(msg);
		}
		// no else.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tudresden.ocl20.pivot.modelbus.IModelInstanceTypeRegistry#dispose()
	 */
	public void dispose() {

		if (this.modelInstanceTypes != null) {
			this.modelInstanceTypes.clear();
			this.modelInstanceTypes = null;
		}
		// no else.

		Platform.getExtensionRegistry().removeListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seetudresden.ocl20.pivot.modelbus.IModelInstanceTypeRegistry#
	 * getModelInstanceType(java.lang.String)
	 */
	public IModelInstanceType getModelInstanceType(String id) {

		IModelInstanceType result;

		if (this.modelInstanceTypes == null)
			this.modelInstanceTypes = new HashMap<String, IModelInstanceType>();
		// no else.

		result = this.modelInstanceTypes.get(id);

		if (result == null) {
			String msg;

			msg = "The ModelInstanceType with the given id '" + id
					+ "' does not exist.";

			LOGGER.warn(msg);
		}
		// no else.

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seetudresden.ocl20.pivot.modelbus.IModelInstanceFileFormatRegistry#
	 * getFileFormats()
	 */
	public IModelInstanceType[] getModelInstanceTypes() {

		IModelInstanceType[] result;

		if (this.modelInstanceTypes == null) {
			result = new IModelInstanceType[0];
		}

		else {
			result = this.modelInstanceTypes.values().toArray(
					new IModelInstanceType[this.modelInstanceTypes.size()]);
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.IRegistryEventListener#removed(org.eclipse.core
	 * .runtime.IExtension[])
	 */
	public void removed(IExtension[] extensions) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("removed(extensions=" + extensions + ") - enter"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		// no else.

		/* Remove all registered objects from the cache. */
		if (this.modelInstanceTypes != null) {

			for (IExtension extension : extensions) {

				for (IConfigurationElement configurationElement : extension
						.getConfigurationElements()) {

					String modelInstanceTypeID;
					modelInstanceTypeID = this.getAttribute(IDescriptor.ATT_ID,
							configurationElement);

					if (modelInstanceTypeID != null) {
						this.modelInstanceTypes.remove(modelInstanceTypeID);
					}
					// no else.
				}
				// end for (configurationElements).
			}
			// end for (extensions).
		}
		// no else.

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("removed(IExtension[]) - exit"); //$NON-NLS-1$
		}
		// no else.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.IRegistryEventListener#removed(org.eclipse.core
	 * .runtime.IExtensionPoint[])
	 */
	public void removed(IExtensionPoint[] extensionPoints) {

		/* Do nothing. Only listen for extensions. */
	}

	/**
	 * <p>
	 * Helper method that returns the value of an attribute of the given
	 * {@link IConfigurationElement}. Throws an
	 * {@link InvalidDescriptorException} if the attribute is empty and
	 * required.
	 * </p>
	 * 
	 * @param attributeName
	 *            The name of the extension point attribute.
	 * @param configurationElement
	 *            The {@link IllegalClassException} whose attribute shall be
	 *            returned.
	 * 
	 * @throws InvalidDescriptorException
	 *             If the value of the attribute is invalid.
	 */
	private String getAttribute(String attributeName,
			IConfigurationElement configurationElement) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getAttribute(attributeName=" + attributeName + ", configurationElement=" + configurationElement //$NON-NLS-1$ //$NON-NLS-2$
					+ ") - enter"); //$NON-NLS-1$
		}
		// no else.

		String value = configurationElement.getAttribute(attributeName);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getAttribute() - exit - return value=" + value); //$NON-NLS-1$
		}
		// no else.

		return value;
	}

	/**
	 * <p>
	 * A helper method to get the model instance type extension point.
	 * </p>
	 */
	private IExtensionPoint getExtensionPoint() {

		IExtensionPoint result;

		/* Get the point from the registry. */
		if (Platform.isRunning()) {
			result = Platform.getExtensionRegistry().getExtensionPoint(
					MODEL_INSTANCE_TYPE_EXTENSION_POINT_ID);

			/* This should not happen unless the id changes. */
			if (result == null) {
				String msg;

				msg = "The extension point for new model instance types could not ";
				msg += "be found under the id ";
				msg += MODEL_INSTANCE_TYPE_EXTENSION_POINT_ID;

				throw new IllegalStateException(msg);
			}
			// no else.
		}

		else {
			System.out
					.println("Platform is not running. Please register ModelInstanceTypes manually.");
			result = null;
		}

		return result;
	}
}