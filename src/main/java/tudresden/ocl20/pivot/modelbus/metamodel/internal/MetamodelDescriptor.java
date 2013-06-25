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
package tudresden.ocl20.pivot.modelbus.metamodel.internal;

import java.net.URL;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;

import tudresden.ocl20.pivot.model.metamodel.IMetamodel;
import tudresden.ocl20.pivot.model.IModelProvider;
import tudresden.ocl20.pivot.modelbus.IModelBusConstants;
import tudresden.ocl20.pivot.modelbus.ModelBusPlugin;
import tudresden.ocl20.pivot.modelbus.descriptor.AbstractDescriptor;
import tudresden.ocl20.pivot.modelbus.metamodel.IMetamodelDescriptor;

/**
 * <p>
 * Implements the {@link IMetamodelDescriptor} interface.
 * </p>
 * 
 * @see IMetamodelDescriptor
 * 
 * @author Matthias Braeuer
 */
public class MetamodelDescriptor extends AbstractDescriptor implements
		IMetamodel, IMetamodelDescriptor {

	/** A logger for this class. */
	private static final Logger LOGGER =
			ModelBusPlugin.getLogger(MetamodelDescriptor.class);

	/** The cached instance of the {@link IModelProvider}. */
	private IModelProvider modelProvider;

	/** The {@link URL} of the icon of this {@link IMetamodel}. */
	private URL iconURL;

	/** The translatable name of the {@link IMetamodel}. */
	private String name;

	/**
	 * <p>
	 * Creates a new {@link MetamodelDescriptor} for a given
	 * {@link IConfigurationElement}.
	 * </p>
	 * 
	 * @param configElement
	 *          The {@link IConfigurationElement} describing the
	 *          {@link IExtension} of the {@link IMetamodel} belonging to this
	 *          {@link MetamodelDescriptor}.
	 */
	public MetamodelDescriptor(IConfigurationElement configElement) {

		super(configElement);

		if (LOGGER.isDebugEnabled()) {
			LOGGER
					.debug("MetamodelDescriptor(configElement=" + configElement + ") - enter"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		// no else.

		/* Initialize the descriptor. */
		this.loadFromExtension();

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("MetamodelDescriptor() - exit"); //$NON-NLS-1$
		}
		// no else.
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * tudresden.ocl20.pivot.modelbus.metamodel.IMetamodelDescriptor#getIconURL()
	 */
	public URL getIconURL() {

		/* Lazily create the image descriptor. */
		if (this.iconURL == null) {
			this.iconURL = this.getIconURL(IModelBusConstants.ATT_ICON);
		}
		// no else.

		return this.iconURL;
	}

	/*
	 * (non-Javadoc)
	 * @see tudresden.ocl20.pivot.modelbus.IMetamodel#getName()
	 */
	public String getName() {

		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * @see tudresden.ocl20.pivot.modelbus.IMetamodel#getModelProvider()
	 */
	public IModelProvider getModelProvider() {

		if (this.modelProvider == null) {
			this.modelProvider =
					this.createInstance(IModelBusConstants.ATT_MODELPROVIDER,
							IModelProvider.class);
		}
		// no else.

		return this.modelProvider;
	}

	/**
	 * <p>
	 * A helper method that loads the name and checks the 'itemProvider'
	 * attribute.
	 * </p>
	 */
	protected void loadFromExtension() {

		this.name = this.getAttribute(IModelBusConstants.ATT_NAME, false);

		if (this.name == null) {
			this.name = getId();
		}
		// no else.

		this.checkAttribute(IModelBusConstants.ATT_MODELPROVIDER);
	}
}