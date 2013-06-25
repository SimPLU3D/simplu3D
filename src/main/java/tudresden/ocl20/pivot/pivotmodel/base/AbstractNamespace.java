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
package tudresden.ocl20.pivot.pivotmodel.base;

import java.util.List;

import org.eclipse.emf.common.util.BasicEList;

import tudresden.ocl20.pivot.pivotmodel.Constraint;
import tudresden.ocl20.pivot.pivotmodel.Namespace;
import tudresden.ocl20.pivot.pivotmodel.Type;
import tudresden.ocl20.pivot.pivotmodel.impl.NamespaceImpl;

/**
 * This class is meant as a base class for DSL- and/or repository-specific
 * implementations of the Pivot Model {@link Namespace} concept. It defines the
 * minimal set of operations that need to be supported by subclasses. This class
 * currently does not use any caching or other performance optimizations in
 * order to support repositories with alternating content. Clients may implement
 * their own caching behavior or override the implementation in this class
 * altogether. Of course, an entirely new implementation of the
 * <code>Namespace</code> interface is possible as well.
 * 
 * <p>
 * Special attention should be payed to methods returning collections. In the
 * superclass {@link NamespaceImpl}, the corresponding EMF-generated code is
 * usually forwarded to a method suffixed with <code>Gen</code>. These are used
 * by special <code>add...</code> methods which allow adding new elements to
 * multivalued properties. This design allows overriding the corresponding
 * getter method without losing the EMF implementation. Moreover, subclasses can
 * now combine the elements added via <code>add...</code> in the superclass and
 * those returned as adapters from the model, thereby creating a transient
 * "view" of the model.
 * </p>
 * 
 * <p>
 * In this class, the principle is exemplified in the
 * {@link #getNestedNamespace()} method. It combines the results of
 * {@link #getNestedNamespaceGen()} and {@link #getNestedNamespaceImpl()}. The
 * latter has to be implemented in subclasses. This is useful to add additional
 * namespaces for the {@link Constraint}s created when parsing OCL expressions.
 * In theory, this approach can be repeated for all other multivalued properties
 * ({@link #getOwnedRule()}, {@link #getOwnedType()}) as well.
 * </p>
 * 
 * @author Matthias Braeuer
 * @version 1.0 29.03.2007
 */
public abstract class AbstractNamespace extends NamespaceImpl implements
		Namespace {

	/**
	 * Subclasses should return the name of the adapted {@link Namespace} concept.
	 */
	@Override
	public abstract String getName();

	/**
	 * Subclasses should return an adapter for the {@link Namespace} that contains
	 * this <code>Namespace</code>.
	 */
	@Override
	public abstract Namespace getNestingNamespace();

	/**
	 * Combines the values of {@link #getNestedNamespaceGen()} and
	 * {@link #getNestedNamespaceImpl()} into a new list.
	 */
	@Override
	public final List<Namespace> getNestedNamespace() {

		List<Namespace> nestedNamespace = new BasicEList<Namespace>();
		nestedNamespace.addAll(getNestedNamespaceImpl());
		nestedNamespace.addAll(getNestedNamespaceGen());
		return nestedNamespace;
	}

	/**
	 * Subclasses should return a list of adapters for their nested
	 * {@link Namespace}s.
	 */
	protected abstract List<Namespace> getNestedNamespaceImpl();

	/**
	 * Subclasses should return a list for adapters for the {@link Type}s
	 * contained in this <code>Namespace</code>.
	 */
	@Override
	public abstract List<Type> getOwnedType();

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object anObject) {

		boolean result;

		if (anObject instanceof Namespace) {
			Namespace aNamespace;

			aNamespace = (Namespace) anObject;

			result = this.getQualifiedName().equals(aNamespace.getQualifiedName());
		}

		else {
			result = false;
		}

		return result;
	}
}