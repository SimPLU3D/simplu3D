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
package tudresden.ocl20.pivot.metamodels.java.internal.msg;

import org.eclipse.osgi.util.NLS;

/**
 * <p>
 * Provides localized String constants for the Java Meta-Model.
 * </p>
 * 
 * @author Claas Wilke
 */
public class JavaMetaModelMessages extends NLS {

	private static final String BUNDLE_NAME =
			"tudresden.ocl20.pivot.metamodels.java.internal.msg.messages"; //$NON-NLS-1$

	public static String JavaMetaModel_ClassNotFound;
	
	public static String JavaMetaModel_FileDoesNotExist;

	public static String JavaMetaModel_InvalidFileFormat;

	static {
		/* Initialize resource bundle. */
		NLS.initializeMessages(BUNDLE_NAME, JavaMetaModelMessages.class);
	}

	private JavaMetaModelMessages() {

		/* No implementation necessary. */
	}
}