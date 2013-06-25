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
package tudresden.ocl20.pivot.pivotmodel;

import java.util.List;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Generic Type</b></em>'. <!-- end-user-doc -->
 * 
 * 
 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getGenericType()
 * @model abstract="true"
 * @generated
 */
public interface GenericType extends NamedElement {

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model dataType="tudresden.ocl20.pivot.datatypes.Boolean" required="true"
	 *        parametersDataType=
	 *        "tudresden.ocl20.pivot.datatypes.Sequence<tudresden.ocl20.pivot.pivotmodel.TypeParameter>"
	 *        parametersRequired="true" parametersMany="false" typesDataType=
	 *        "tudresden.ocl20.pivot.datatypes.Sequence<? extends tudresden.ocl20.pivot.pivotmodel.Type>"
	 *        typesRequired="true" typesMany="false"
	 * @generated
	 */
	boolean bindGenericType(List<TypeParameter> parameters,
			List<? extends Type> types, TypedElement typedElement);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model dataType="tudresden.ocl20.pivot.datatypes.Boolean"
	 *        parametersDataType=
	 *        "tudresden.ocl20.pivot.datatypes.Sequence<tudresden.ocl20.pivot.pivotmodel.TypeParameter>"
	 *        parametersRequired="true" parametersMany="false" typesDataType=
	 *        "tudresden.ocl20.pivot.datatypes.Sequence<? extends tudresden.ocl20.pivot.pivotmodel.Type>"
	 *        typesRequired="true" typesMany="false"
	 * @generated
	 */
	boolean bindGenericSuperType(List<TypeParameter> parameters,
			List<? extends Type> types, Type subType);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Returns <code>true</code> if the given type conforms to this
	 * <code>GenericType</code>, <code>false</code> otherwise.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @model dataType="tudresden.ocl20.pivot.datatypes.Boolean"
	 * @generated
	 */
	boolean isConformant(Type type);

	/**
	 * Redefines {@link NamedElement#clone()} with a covariant return type.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.NamedElement#clone()
	 */
	GenericType clone();

} // GenericType
