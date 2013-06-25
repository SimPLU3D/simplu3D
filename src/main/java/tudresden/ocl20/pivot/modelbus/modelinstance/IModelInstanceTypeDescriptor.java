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
package tudresden.ocl20.pivot.modelbus.modelinstance;

import java.net.URL;

import tudresden.ocl20.pivot.modelbus.descriptor.IDescriptor;
import tudresden.ocl20.pivot.modelinstance.IModelInstanceType;

/**
 * <p>
 * This is an additional interface that is mixed in by the Eclipse-specific
 * implementation of the {@link IModelInstanceType} interface. It represents a
 * descriptor for the information contained in a single
 * {@link IModelInstanceType} tag in an extension that extends the
 * <code>modelinstancetypes</code> extension point.
 * </p>
 * 
 * @author Claas Wilke
 */
public interface IModelInstanceTypeDescriptor extends IDescriptor {

	/**
	 * <p>
	 * Returns a {@link URL} that can be used as an icon for the
	 * {@link IModelInstanceType} in the Eclipse user interface.
	 * </p>
	 * 
	 * @return A {@link URL}.
	 */
	URL getIconURL();
}