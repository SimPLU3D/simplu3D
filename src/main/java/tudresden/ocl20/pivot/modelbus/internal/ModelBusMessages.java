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
package tudresden.ocl20.pivot.modelbus.internal;

import org.eclipse.osgi.util.NLS;

/**
 * <p>
 * Constants to access localized messages for the Model Bus plug-in.
 * </p>
 * 
 * @author Matthias Braeuer
 */
public class ModelBusMessages extends NLS {

	public static String AbstractModelProvider_LoadingModel;

	public static String ModelRegistry_SettingActiveModel;

	public static String OclLibraryProvider_LoadOclLibrary;

	public static String IModelInstanceElement_CannotCast;

	public static String IModelInstanceElement_CannotConvertArray;

	public static String IModelInstanceElement_CannotAdaptToType;

	public static String IModelInstanceElement_CollectionHasUnknownContent;

	/** The location of the messages file. */
	private static final String MESSAGES_FILE_LOCATION =
			"tudresden.ocl20.pivot.modelbus.internal.messages"; //$NON-NLS-1$

	static {
		/* Initialize resource bundle. */
		NLS.initializeMessages(MESSAGES_FILE_LOCATION, ModelBusMessages.class);
	}

	/**
	 * <p>
	 * Initializes the {@link ModelBusMessages}.
	 * </p>
	 */
	private ModelBusMessages() {

		/* no implementaton necessary. */
	}
}