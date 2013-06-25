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
package tudresden.ocl20.pivot.modelinstancetype.java.internal.provider;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.osgi.util.NLS;

import tudresden.ocl20.pivot.model.IModel;
import tudresden.ocl20.pivot.model.ModelAccessException;
import tudresden.ocl20.pivot.modelinstance.IModelInstance;
import tudresden.ocl20.pivot.modelinstance.ModelInstancePlugin;
import tudresden.ocl20.pivot.modelinstance.base.AbstractModelInstanceProvider;
import tudresden.ocl20.pivot.modelinstancetype.exception.TypeNotFoundInModelException;
import tudresden.ocl20.pivot.modelinstancetype.java.JavaModelInstanceTypePlugin;
import tudresden.ocl20.pivot.modelinstancetype.java.internal.modelinstance.JavaModelInstance;
import tudresden.ocl20.pivot.modelinstancetype.java.internal.msg.JavaModelInstanceTypeMessages;

/**
 * <p>
 * Provides methods to load or get {@link JavaModelInstance}s of a model.
 * 
 * @author Claas Wilke
 */
public class JavaModelInstanceProvider extends AbstractModelInstanceProvider {

	/** The {@link Logger} for this class. */
	private static final Logger LOGGER =
			JavaModelInstanceTypePlugin.getLogger(JavaModelInstanceProvider.class);

	/**
	 * <p>
	 * Creates a new {@link JavaModelInstanceProvider}.
	 * </p>
	 */
	public JavaModelInstanceProvider() {

		/* Remains empty. */
	}

	/*
	 * (non-Javadoc)
	 * @seetudresden.ocl20.pivot.modelbus.modelinstance.IModelInstanceProvider#
	 * createEmptyModelInstance(tudresden.ocl20.pivot.modelbus.IModel)
	 */
	public IModelInstance createEmptyModelInstance(IModel model) {

		if (model == null) {
			throw new IllegalArgumentException("Paramter 'model' must not be null.");
		}
		// no else.

		return new JavaModelInstance(model);
	}

	/**
	 * <p>
	 * Loads a model instance to a given URL.
	 * </p>
	 * 
	 * @param instanceURL
	 *          the {@link URL} of the model instance which shall be loaded.
	 * @param model
	 *          The UML2 {@link IModel} of the {@link JavaModelInstance} which
	 *          shall be loaded.
	 */
	public IModelInstance getModelInstance(URL instanceURL, IModel model)
			throws ModelAccessException {

		/* Probably debug the entry of this method. */
		if (LOGGER.isDebugEnabled()) {
			String msg;

			msg = "getModelInstance("; //$NON-NLS-1$
			msg += "instanceURL = " + instanceURL; //$NON-NLS-1$
			msg += ", model = " + model; //$NON-NLS-1$
			msg += ")"; //$NON-NLS-1$

			LOGGER.debug(msg);
		}
		// no else.

		/* Check if the given file is a class file. */
		if (!instanceURL.getFile().endsWith(".class")) {
			throw new ModelAccessException(
					JavaModelInstanceTypeMessages.JavaModelInstanceProvider_InvalidFileFormat);
		}
		// no else.

		File modelInstanceFile;
		String modelInstanceFilePath;

		modelInstanceFilePath = instanceURL.getFile();

		/* Replace eventually existing white spaces in the path. */
		modelInstanceFilePath = modelInstanceFilePath.replaceAll("%20", " ");

		modelInstanceFile = new File(modelInstanceFilePath);

		/* Check if the given file does exists. */
		if (!modelInstanceFile.exists()) {
			String msg;

			msg =
					NLS
							.bind(
									JavaModelInstanceTypeMessages.JavaModelInstanceProvider_FileDoesNotExist,
									modelInstanceFile);

			throw new ModelAccessException(msg);
		}
		// no else.

		IModelInstance result;

		URLClassLoader aClassLoader;
		Class<?> modelInstanceClass;

		URL[] folderURLs;
		List<String> possibleClassNames;

		int index;
		result = null;

		folderURLs = this.computeFolderURLs(modelInstanceFile);

		/* Compute all possible class names. */
		possibleClassNames = this.computePossibleClassNames(instanceURL);

		index = 0;

		/* Iterate through the names and try to load the class. */
		for (String aClassName : possibleClassNames) {

			if (index >= folderURLs.length) {
				break;
			}
			// no else.

			try {
				/*
				 * The parent class loader from the model bus plug-in is required to
				 * find types from EMF Ecore like EObject.
				 */
				aClassLoader =
						new URLClassLoader(new URL[] { folderURLs[index] },
								ModelInstancePlugin.class.getClassLoader());

				modelInstanceClass = Class.forName(aClassName, true, aClassLoader);

				/* If no exception is thrown, load the model instance. */
				result = new JavaModelInstance(modelInstanceClass, model);
				break;
			}

			catch (ClassNotFoundException e) {
				/* Do nothing, continue iteration. */
			}

			catch (NoClassDefFoundError e) {
				/* Do nothing, continue iteration. */
			}

			catch (SecurityException e) {
				/* Do nothing, continue iteration. */
				/* Could happen if the root package is called 'java' or 'javax'. */
			}

			catch (TypeNotFoundInModelException e) {
				throw new ModelAccessException(e.getMessage(), e);
			}

			index++;
		}
		// end for.

		/* Check if a Model has been loaded. */
		if (result == null) {
			String msg;

			msg =
					NLS
							.bind(
									JavaModelInstanceTypeMessages.JavaModelInstanceProvider_ClassNotFound,
									instanceURL);

			throw new ModelAccessException(msg);
		}
		// no else.

		/* Eventually debug the exit of this method. */
		if (LOGGER.isDebugEnabled()) {
			String msg;

			msg = "getModelInstance(URL, IModel) - exit"; //$NON-NLS-1$
			msg += " return value = " + result; //$NON-NLS-1$

			LOGGER.debug(msg);
		}
		// no else.

		return result;
	}

	/**
	 * <p>
	 * A helper method that computes the URLs of the folders which are parents of
	 * a given {@link File}.
	 * 
	 * @param modelFile
	 *          The File for which the folder URL shall be computed.
	 * @return The computed Folder URLs.
	 */
	private URL[] computeFolderURLs(File modelFile) {

		List<URL> resultList;
		File folderFile;

		resultList = new ArrayList<URL>();

		folderFile = modelFile.getParentFile();

		while (folderFile != null) {

			if (folderFile.exists()) {
				try {
					resultList.add(folderFile.toURI().toURL());
				}

				catch (MalformedURLException e) {
					/* Do nothing. */
				}

				folderFile = folderFile.getParentFile();
			}

			else {
				break;
			}
		}
		// end while.

		return resultList.toArray(new URL[0]);
	}

	/**
	 * <p>
	 * A helper method that computes all possible names of a {@link Class} which
	 * shall be loaded from a given {@link URL}.
	 * </p>
	 * 
	 * @param aURL
	 *          The {@link URL}
	 * @return The {@link List} of possible {@link Class} names.
	 */
	private List<String> computePossibleClassNames(URL aURL) {

		List<String> result;

		String urlString;
		String[] packages;
		String aClassName;

		result = new ArrayList<String>();

		urlString = aURL.toString();

		if (urlString.endsWith(".class")) {
			urlString = urlString.substring(0, urlString.length() - 6);
		}
		// no else.

		/* Split the urlString into possible packages. */
		packages = urlString.split("/");

		aClassName = null;

		/* Create all possible names (from the shortest to the longest). */
		for (int index = packages.length - 1; index >= 0; index--) {
			if (aClassName == null) {
				aClassName = packages[index];
			}

			else {
				aClassName = packages[index] + "." + aClassName;
			}

			result.add(aClassName);
		}

		return result;
	}
}