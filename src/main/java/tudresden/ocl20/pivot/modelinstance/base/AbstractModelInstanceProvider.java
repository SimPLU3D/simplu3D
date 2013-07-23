/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2007 Ronny Brandt (Ronny_Brandt@web.de).                    *
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
 */
package tudresden.ocl20.pivot.modelinstance.base;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import tudresden.ocl20.pivot.model.IModel;
import tudresden.ocl20.pivot.model.ModelAccessException;
import tudresden.ocl20.pivot.model.base.AbstractModelProvider;
import tudresden.ocl20.pivot.modelinstance.IModelInstance;
import tudresden.ocl20.pivot.modelinstance.IModelInstanceProvider;

/**
 * <p>
 * An abstract implementation of {@link IModelInstanceProvider}.
 * </p>
 * 
 * @author Ronny Brandt
 */
public abstract class AbstractModelInstanceProvider implements
		IModelInstanceProvider {

	/*
	 * (non-Javadoc)
	 * @see tudresden.ocl20.pivot.modelbus.IModelInstanceProvider#getModelInstance
	 * (java.io.File, tudresden.ocl20.pivot.modelbus.IModel)
	 */
	public IModelInstance getModelInstance(File modelInstanceFile, IModel model)
			throws ModelAccessException {

		IModelInstance modelInstance;
		URL modelInstanceFileUrl;

		if (modelInstanceFile == null) {
			throw new IllegalArgumentException(
					"The argument 'modelInstanceFile' was null."); //$NON-NLS-1$
		}
		// no else.

		try {
			/* File.toURL().toURL() handles white spaces differently. */
			// modelInstanceFileUrl = modelInstanceFile.toURL();
			modelInstanceFileUrl = modelInstanceFile.toURI().toURL();
		}
		// If a required system property value cannot be accessed
		catch (SecurityException e) {
			throw new ModelAccessException("Failed to create a URL for file " //$NON-NLS-1$
					+ modelInstanceFile.getAbsolutePath(), e);
		}
		// If this URL is not absolute
		catch (IllegalArgumentException e) {
			throw new ModelAccessException("Failed to create a URL for file " //$NON-NLS-1$
					+ modelInstanceFile.getAbsolutePath(), e);
		}
		// If a protocol handler for the URL could not be found,
		// or if some other error occurred while constructing the URL
		catch (MalformedURLException e) {
			throw new ModelAccessException("Failed to create a URL for file " //$NON-NLS-1$
					+ modelInstanceFile.getAbsolutePath(), e);
		}

		/* Delegate to user implementation. */
		modelInstance = this.getModelInstance(modelInstanceFileUrl, model);

		return modelInstance;
	}

	/*
	 * (non-Javadoc)
	 * @see tudresden.ocl20.pivot.modelbus.IModelInstanceProvider#getModelInstance
	 * (java.lang.String, tudresden.ocl20.pivot.modelbus.IModel)
	 */
	public IModelInstance getModelInstance(String modelInstanceName, IModel model)
			throws ModelAccessException {

		URL modelInstanceUrl;
		IModelInstance modelInstance;

		modelInstanceUrl =
				AbstractModelProvider.class.getResource(modelInstanceName);

		if (modelInstanceUrl == null) {
			throw new ModelAccessException(
					"Failed to create a URL for model " + modelInstanceName); //$NON-NLS-1$
		}
		// no else.

		/* Delegate to user implementation. */
		modelInstance = this.getModelInstance(modelInstanceUrl, model);

		return modelInstance;
	}
}