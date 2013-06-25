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

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;

import tudresden.ocl20.pivot.modelbus.IModelBusConstants;
import tudresden.ocl20.pivot.modelbus.ModelBusPlugin;
import tudresden.ocl20.pivot.modelinstance.IModelInstanceType;
import tudresden.ocl20.pivot.modelinstance.IModelInstanceTypeRegistry;

/**
 * <p>
 * A simple helper class that can fill an
 * {@link IModelInstanceTypeRegistry} with
 * {@link IModelInstanceType}s read from extensions of the 'modelinstancetype'
 * extension point.
 * </p>
 * 
 * @author Claas Wilke
 */
public class ModelInstanceTypeRegistryReader {

	/** A {@link Logger} for this class. */
	private static final Logger logger = ModelBusPlugin
			.getLogger(ModelInstanceTypeRegistryReader.class);

	/**
	 * @param extensionPoint
	 * @param registry
	 */
	public void read(IExtensionPoint extensionPoint,
			IModelInstanceTypeRegistry registry) {

		IExtension[] extensions;

		extensions = extensionPoint.getExtensions();

		for (int i = 0; i < extensions.length; i++) {
			read(extensions[i], registry);
		}
	}

	/**
	 * @param extension
	 * @param registry
	 */
	public void read(IExtension extension,
			IModelInstanceTypeRegistry registry) {

		IConfigurationElement[] elements;

		elements = extension.getConfigurationElements();

		for (int i = 0; i < elements.length; i++) {
			read(elements[i], registry);
		}
	}

	/**
	 * @param configElement
	 * @param registry
	 */
	public void read(IConfigurationElement configElement,
			IModelInstanceTypeRegistry registry) {

		if (configElement.getName().equals(IModelBusConstants.TAG_MODELINSTANCETYPE)) {

			try {
				registry.addModelInstanceType(new ModelInstanceTypeDescriptor(configElement));
			}

			catch (Exception e) {
				String msg;

				msg = "An error was encountered when reading config element "
						+ configElement;

				logger.warn(msg, e);
			}

		}

		else {
			String msg;

			msg = "Unable to read config element " + configElement;
			msg += " because its tag name is not recognized.";

			logger.warn(msg);
		}
	}
}