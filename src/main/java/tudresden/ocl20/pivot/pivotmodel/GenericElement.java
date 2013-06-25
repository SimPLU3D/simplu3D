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
 * <em><b>Generic Element</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link tudresden.ocl20.pivot.pivotmodel.GenericElement#getOwnedTypeParameter
 * <em>Owned Type Parameter</em>}</li>
 * </ul>
 * </p>
 * 
 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getGenericElement()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface GenericElement extends NamedElement {

	/**
	 * Returns the value of the '<em><b>Owned Type Parameter</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link tudresden.ocl20.pivot.pivotmodel.TypeParameter}. It is bidirectional
	 * and its opposite is '
	 * {@link tudresden.ocl20.pivot.pivotmodel.TypeParameter#getGenericElement
	 * <em>Generic Element</em>}'. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Owned Type Parameter</em>' containment reference
	 * list isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Owned Type Parameter</em>' containment
	 *         reference list.
	 * @see tudresden.ocl20.pivot.pivotmodel.PivotModelPackage#getGenericElement_OwnedTypeParameter()
	 * @see tudresden.ocl20.pivot.pivotmodel.TypeParameter#getGenericElement
	 * @model opposite="genericElement" containment="true"
	 * @generated
	 */
	List<TypeParameter> getOwnedTypeParameter();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Adds a {@link TypeParameter} to the type parameters of this
	 * <code>GenericElement</code>. This operation is required to properly support
	 * cloning generic elements. The operation returns a reference to this
	 * <code>GenericElement</code>.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @model
	 * @generated
	 */
	GenericElement addTypeParameter(TypeParameter typeParameter);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * <p>
	 * Binds the given {@link TypeParameter type parameters} with the given
	 * {@link Type types}. This means that all references to the type parameters
	 * (e.g., in properties or operations) will be replaced with the corresponding
	 * types. The binding will result in a new <code>NamedElement</code> that has
	 * some or all of its type parameters bound. The original
	 * <code>GenericElement</code> will not be touched. If all type parameters are
	 * bound, this operation essentially represents an instantiation of a generic
	 * type with concrete values for its type parameters.
	 * </p>
	 * 
	 * <p>
	 * The two lists must have the same size and must not contain
	 * <code>null</code> elements. The lists may be empty, though. In this case,
	 * the binding will still work because the <code>GenericElement</code> may
	 * reference {@link ComplexGenericType}s that already have
	 * {@link TypeArgument}s attached to them. These bindings will be performed
	 * even if no further bindings have been defined via the arguments passed to
	 * this method.
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @model required="true" parametersDataType=
	 *        "tudresden.ocl20.pivot.datatypes.Sequence<tudresden.ocl20.pivot.pivotmodel.TypeParameter>"
	 *        parametersRequired="true" parametersMany="false" typesDataType=
	 *        "tudresden.ocl20.pivot.datatypes.Sequence<? extends tudresden.ocl20.pivot.pivotmodel.Type>"
	 *        typesRequired="true" typesMany="false"
	 * @generated
	 */
	NamedElement bindTypeParameter(List<TypeParameter> parameters,
			List<? extends Type> types);

} // GenericElement
