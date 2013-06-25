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

/**
 * <p>
 * An unchecked exception that is thrown when unexpected errors or
 * misconfigurations occur in the model bus plug-in.
 * </p>
 * 
 * @author Matthias Braeuer
 */
public class ModelBusException extends RuntimeException {

	/**
	 * A generated serial version id.
	 */
	private static final long serialVersionUID = -5558531392214126891L;

	/**
	 * <p>
	 * Creates a new {@link ModelBusException}.
	 * </p>
	 */
	public ModelBusException() {

		/* No implementation necessary. */
	}

	/**
	 * <p>
	 * Creates a new {@link ModelBusException} with an error message.
	 * </p>
	 * 
	 * @param message
	 *          An error message.
	 */
	public ModelBusException(String message) {

		super(message);
	}

	/**
	 * <p>
	 * Creates a new {@link ModelBusException} with a root cause.
	 * </p>
	 * 
	 * @param cause
	 *          A causing {@link Throwable}.
	 */
	public ModelBusException(Throwable cause) {

		super(cause);
	}

	/**
	 * <p>
	 * Creates a new {@link ModelBusException} with an error message and a root
	 * cause.
	 * </p>
	 * 
	 * @param message
	 *          An error message.
	 * @param cause
	 *          A causing {@link Throwable}.
	 */
	public ModelBusException(String message, Throwable cause) {

		super(message, cause);
	}
}