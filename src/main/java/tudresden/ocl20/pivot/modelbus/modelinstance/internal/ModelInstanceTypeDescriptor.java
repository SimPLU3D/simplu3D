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

import java.net.URL;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;

import tudresden.ocl20.pivot.modelbus.IModelBusConstants;
import tudresden.ocl20.pivot.modelbus.ModelBusPlugin;
import tudresden.ocl20.pivot.modelbus.descriptor.AbstractDescriptor;
import tudresden.ocl20.pivot.modelbus.modelinstance.IModelInstanceTypeDescriptor;
import tudresden.ocl20.pivot.modelinstance.IModelInstanceProvider;
import tudresden.ocl20.pivot.modelinstance.IModelInstanceType;

/**
 * <p>
 * Implements the interfaces {@link IModelInstanceType} and
 * {@link IModelInstanceTypeDescriptor}.
 * </p>
 * 
 * @author Claas Wilke
 */
public class ModelInstanceTypeDescriptor extends AbstractDescriptor implements
		IModelInstanceType, IModelInstanceTypeDescriptor {

	/** A {@link Logger} for this class. */
	private static final Logger logger =
			ModelBusPlugin.getLogger(ModelInstanceTypeDescriptor.class);

	/** The name of the {@link IModelInstanceType}. */
	private String name;

	/**
	 * The {@link URL} of the icon used for the
	 * {@link ModelInstanceTypeDescriptor}.
	 */
	private URL iconURL;

	/** The cached instance of the {@link IModelInstanceProvider}. */
	private IModelInstanceProvider modelInstanceProvider;

	/**
	 * <p>
	 * Creates a new {@link ModelInstanceTypeDescriptor}.
	 * </p>
	 * 
	 * @param configElement
	 */
	public ModelInstanceTypeDescriptor(IConfigurationElement configElement) {

		super(configElement);

		/* Eventually log the entry of this method. */
		if (logger.isDebugEnabled()) {
			String msg;

			msg = "MetamodelDescriptor(configElement=" + configElement + ") - enter";

			logger.debug(msg);
		}
		// no else.

		/* Initialize the descriptor. */
		this.loadFromExtension();

		/* Eventually log the exit of this method. */
		if (logger.isDebugEnabled()) {
			logger.debug("MetamodelDescriptor() - exit");
		}
		// no else.
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * tudresden.ocl20.pivot.modelbus.IModelInstanceFileFormat#getModelProvider ()
	 */
	public IModelInstanceProvider getModelInstanceProvider() {

		/* Lazily create the model instance provider. */
		if (this.modelInstanceProvider == null) {
			this.modelInstanceProvider =
					this.createInstance(IModelBusConstants.ATT_MODELINSTANCEPROVIDER,
							IModelInstanceProvider.class);
		}
		// no else.

		return this.modelInstanceProvider;
	}

	/*
	 * (non-Javadoc)
	 * @see tudresden.ocl20.pivot.modelbus.IModelInstanceType#getName()
	 */
	public String getName() {

		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * @see tudresden.ocl20.pivot.modelbus.internal.IMetamodelDescriptor#getIcon()
	 */
	public URL getIconURL() {

		/* Lazily create the image descriptor. */
		if (this.iconURL == null) {
			this.iconURL = this.getIconURL(IModelBusConstants.ATT_ICON);
		}
		// no else.

		return this.iconURL;
	}

	/**
	 * <p>
	 * A helper method that loads the name and checks the 'itemProvider'
	 * attribute.
	 * </p>
	 */
	protected void loadFromExtension() {

		this.name = getAttribute(IModelBusConstants.ATT_NAME, false);

		if (this.name == null) {
			this.name = getId();
		}
		// no else.

		this.checkAttribute(IModelBusConstants.ATT_MODELINSTANCEPROVIDER);
	}
}