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
package tudresden.ocl20.pivot.metamodels.java.internal.provider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.osgi.util.NLS;

import tudresden.ocl20.pivot.metamodels.java.JavaMetaModelPlugin;
import tudresden.ocl20.pivot.metamodels.java.internal.model.JavaModel;
import tudresden.ocl20.pivot.metamodels.java.internal.msg.JavaMetaModelMessages;
import tudresden.ocl20.pivot.model.IModel;
import tudresden.ocl20.pivot.model.IModelProvider;
import tudresden.ocl20.pivot.model.ModelAccessException;
import tudresden.ocl20.pivot.model.ModelPlugin;
import tudresden.ocl20.pivot.model.base.AbstractModelProvider;
import tudresden.ocl20.pivot.modelbus.ModelBusPlugin;

/**
 * <p>
 * Implementation of the {@link IModelProvider} interface for Java {@link Class}
 * files. This implementation will create an {@link JavaModel} instance.
 * </p>
 * 
 * @author Claas Wilke
 */
public class JavaModelProvider extends AbstractModelProvider implements
		IModelProvider {

	/** The {@link Logger} for this class. */
	protected static final Logger LOGGER =
			JavaMetaModelPlugin.getLogger(JavaModelProvider.class);

	/*
	 * (non-Javadoc)
	 * @see tudresden.ocl20.pivot.modelbus.IModelProvider#getModel(java.net.URL)
	 */
	public IModel getModel(URL modelURL) throws ModelAccessException {

		/* Probably debug the entry of this method. */
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getModel(modelURL=" + modelURL + ") - enter");
		}
		// no else.

		File modelFile;
		String modelFilePath;

		modelFilePath = modelURL.getFile();

		/* Replace probably existing white spaces in the path. */
		modelFilePath = modelFilePath.replaceAll("%20", " ");

		modelFile = new File(modelFilePath);

		/* Check if the given file does exists. */
		if (!modelFile.exists()) {
			String msg;

			msg =
					NLS.bind(JavaMetaModelMessages.JavaMetaModel_FileDoesNotExist,
							modelURL);

			throw new ModelAccessException(msg);
		}
		// no else.

		IModel result;
		result = null;

		/* Probably handle the URL as a class file. */
		if (modelFile.toString().endsWith(".class")) {
			Class<?> modelClass;
			modelClass = loadClassFromUrl(modelURL, new ArrayList<URL>());
			result = this.getModel(modelClass);
		}

		/* Else handle the file as a configuration. */
		else if (modelFile.toString().endsWith(".javamodel")) {
			Class<?> modelClass;
			modelClass = loadClassWithJarsFromUrl(modelFile);
			result = this.getModel(modelClass);
		}

		else {
			throw new ModelAccessException(
					"Invalid kind of file. Java Meta-Model can only handle .class and .javamodel Files.");
		}

		/* Check if a Model has been loaded. */
		if (result == null) {
			String msg;

			msg =
					NLS.bind(JavaMetaModelMessages.JavaMetaModel_ClassNotFound, modelURL);

			throw new ModelAccessException(msg);
		}
		// no else.

		/* Probably debug the exit of this method. */
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getModel() - exit - return value=" + result); //$NON-NLS-1$
		}
		// no else.

		return result;
	}

	/**
	 * <p>
	 * Returns the {@link IModel} which is described by a given {@link Class}.
	 * </p>
	 * 
	 * @param modelClass
	 *          The {@link Class} which shall be loaded as {@link IModel}.
	 * @return The {@link IModel} created from the given {@link Class}.
	 * @throws ModelAccessException
	 *           Thrown, if an error during {@link IModel} loading occurs.
	 */
	public IModel getModel(Class<?> modelClass) throws ModelAccessException {

		/* Probably debug the entry of this method. */
		if (LOGGER.isDebugEnabled()) {
			String msg;

			msg = "getModel(";
			msg += "modelClass = " + modelClass;
			msg += ") - enter";

			LOGGER.debug(msg);
		}
		// no else.

		IModel result;

		result =
				new JavaModel(modelClass, ModelBusPlugin.getMetamodelRegistry()
						.getMetamodel(JavaMetaModelPlugin.ID));

		/* Probably debug the exit of this method. */
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getModel() - exit - return value=" + result); //$NON-NLS-1$
		}
		// no else.

		return result;
	}

	/**
	 * <p>
	 * A helper method that computes the URLs of the folders which are parents of
	 * a given {@link File}.
	 * 
	 * @param modelUrl
	 *          The {@link URL} for which the folder URL shall be computed.
	 * @return The computed Folder URLs.
	 */
	private URL[] computeFolderURLs(URL modelUrl) {

		List<URL> resultList;
		File folderFile;

		resultList = new ArrayList<URL>();

		String filePath;

		/* Replace probably existing white spaces in the path. */
		filePath = modelUrl.getFile();
		filePath = filePath.replaceAll("%20", " ");

		folderFile = new File(filePath).getParentFile();

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

	/**
	 * <p>
	 * A helper method that tries to find a {@link Class} that is described by the
	 * given URL.
	 * </p>
	 * 
	 * @param modelURL
	 *          The {@link URL} that shall be loaded as a {@link Class}.
	 * @param jarUrls
	 *          A {@link List} of {@link URL}s leading to additional JAR archives
	 *          the given {@link Class} (by its {@link URL}) references.
	 * @return The loaded {@link Class}.
	 * @throws ModelAccessException
	 *           Thrown, if the given {@link File} cannot be accessed as Java
	 *           {@link IModel}.
	 */
	private Class<?> loadClassFromUrl(URL modelURL, List<URL> jarUrls)
			throws ModelAccessException {

		String modelFilePath;
		modelFilePath = modelURL.getFile();

		/* Replace probably existing white spaces in the path. */
		modelFilePath = modelFilePath.replaceAll("%20", " ");

		if (!(new File(modelFilePath).exists())) {
			throw new ModelAccessException("The given java class '" + modelURL
					+ "' does not exists.");
		}
		// no else.

		Class<?> result;
		result = null;

		URLClassLoader aClassLoader;

		URL[] folderURLs;
		List<String> possibleClassNames;

		int index;

		folderURLs = this.computeFolderURLs(modelURL);

		/* Compute all possible class names. */
		possibleClassNames = this.computePossibleClassNames(modelURL);

		index = 0;

		/* Iterate through the names and try to load the class. */
		for (String aClassName : possibleClassNames) {

			if (index >= folderURLs.length) {
				break;
			}
			// no else.

			try {
				List<URL> urls;
				urls = new ArrayList<URL>();
				urls.add(folderURLs[index]);
				urls.addAll(jarUrls);

				/*
				 * The parent class loader from the model bus plug-in is required to
				 * find types from EMF Ecore like EObject.
				 */
				aClassLoader =
						new URLClassLoader(urls.toArray(new URL[0]), ModelPlugin.class
								.getClassLoader());

				result = Class.forName(aClassName, true, aClassLoader);

				/* If no exception is thrown, return the class. */
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

			index++;
		}
		// end for.

		return result;
	}

	/**
	 * <p>
	 * A helper method that reads a given {@link File} as a java {@link IModel}
	 * configuration.
	 * </p>
	 * <p>
	 * The configuration is a very simple text file. The first line contains the
	 * relative location of the java {@link IModel} main {@link Class}. The
	 * following lines can contain referenced JAR archives.
	 * </p>
	 * 
	 * @param modelFile
	 *          The {@link File} containing the {@link IModel} configuration.
	 * @return The loaded model main {@link Class}.
	 * @throws ModelAccessException
	 *           Thrown, if the given {@link File} cannot be accessed as Java
	 *           {@link IModel}.
	 */
	private Class<?> loadClassWithJarsFromUrl(File modelFile)
			throws ModelAccessException {

		Class<?> result;
		result = null;

		try {
			List<URL> urls;
			urls = new ArrayList<URL>();

			BufferedReader configFileReader;
			configFileReader = new BufferedReader(new FileReader(modelFile));

			/* Read the expected code from file. */
			while (configFileReader.ready()) {

				String relativeName;
				relativeName = configFileReader.readLine();

				URL aURL;
				aURL = new URL("file:" + modelFile.getParent() + "/" + relativeName);
				urls.add(aURL);
			}
			// end while.

			if (urls.size() == 0) {
				throw new ModelAccessException(
						"Invalid configuration: The Java Model Configuration File must contain at least one URL of a class file.");
			}
			// no else.

			result = this.loadClassFromUrl(urls.remove(0), urls);
		}
		// end try.

		catch (FileNotFoundException e) {
			throw new ModelAccessException("The given file '" + modelFile
					+ "' was not found.");
		}

		catch (IOException e) {
			throw new ModelAccessException("The given file '" + modelFile
					+ "' could not be opened.");
		}
		// end catch.

		return result;
	}
}