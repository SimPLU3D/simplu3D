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

import tudresden.ocl20.pivot.model.IModelProvider;
import tudresden.ocl20.pivot.modelinstance.IModelInstanceProvider;
import tudresden.ocl20.pivot.modelinstance.IModelInstanceType;

/**
 * <p>
 * A collection of constants used by the Model Bus plug-in.
 * </p>
 * 
 * @author Matthias Braeuer
 */
public interface IModelBusConstants {

	/**
	 * The name of the extension point used for defining metamodel extensions;
	 */
	String EXT_METAMODELS = "metamodels"; //$NON-NLS-1$

	/**
	 * The name of the extension point used for defining model instance file
	 * format extensions;
	 */
	String EXT_MODELINSTANCETYPES = "modelinstancetypes"; //$NON-NLS-1$

	/**
	 * The name of the tag used to define a metamodel.
	 */
	String TAG_METAMODEL = "metamodel"; //$NON-NLS-1$

	/**
	 * The name of the tag used to define an {@link IModelInstanceType}.
	 */
	String TAG_MODELINSTANCETYPE = "modelinstancetype"; //$NON-NLS-1$

	/**
	 * The name of the attribute that contains the class name of the
	 * {@link IModelProvider} for a metamodel.
	 */
	String ATT_MODELPROVIDER = "modelProvider"; //$NON-NLS-1$

	/**
	 * The name of the attribute that contains the class name of the
	 * {@link IModelInstanceProvider} for a {@link IModelInstanceType}.
	 */
	String ATT_MODELINSTANCEPROVIDER = "modelInstanceProvider"; //$NON-NLS-1$

	/**
	 * The name of the attribute that contains the path to an icon representing a
	 * metamodel.
	 */
	String ATT_ICON = "icon"; //$NON-NLS-1$

	/**
	 * The name of the attribute containing the name of the metamodel.
	 */
	String ATT_NAME = "name"; //$NON-NLS-1$

	/**
	 * The ID of the class tudresden.ocl20.pivot.modelbus.ui.views.ModelsView.
	 */
	public static final String MODELS_VIEW_ID =
			"tudresden.ocl20.pivot.modelbus.ui.views.models"; //$NON-NLS-1$

	/**
	 * The ID of the class
	 * tudresden.ocl20.pivot.modelbus.ui.views.ModelInstancesView.
	 */
	public static final String MODEL_INSTANCES_VIEW_ID =
			"tudresden.ocl20.pivot.modelbus.ui.views.modelinstances"; //$NON-NLS-1$
}