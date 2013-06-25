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
package tudresden.ocl20.pivot.metamodels.java.internal.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import tudresden.ocl20.pivot.metamodels.java.JavaMetaModelPlugin;
import tudresden.ocl20.pivot.pivotmodel.Namespace;
import tudresden.ocl20.pivot.pivotmodel.Type;
import tudresden.ocl20.pivot.pivotmodel.base.AbstractNamespace;
import tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl;

/**
 * <p>
 * An implementation of the Pivot Model {@link Namespace} concept for Java.
 * </p>
 */
public class JavaPackage extends AbstractNamespace implements Namespace {

  /** The {@link Logger} for this class. */
  private static final Logger LOGGER = JavaMetaModelPlugin
      .getLogger(JavaPackage.class);

  /** The qualified name of this {@link Namespace}. */
  private List<String> myQualifiedName;

  /** The {@link JavaAdapterFactory} the {@link JavaPackage} belongs to. */
  private JavaAdapterFactory myFactory;

  /**
   * <p>
   * Creates a new {@link JavaPackage} instance.
   * </p>
   * 
   * @param qualifiedName The qualified name of this {@link JavaPackage}.
   * @param aFactory The {@link JavaAdapterFactory}, the new created
   *          {@link JavaPackage} shall belong to.
   */
  public JavaPackage(List<String> qualifiedName, JavaAdapterFactory aFactory) {

    /* Eventually log the entry of this method. */
    if (LOGGER.isDebugEnabled()) {
      String msg;

      msg = "JavaPackage(";
      msg += "qualifiedName = " + qualifiedName;
      msg += "aFactory = " + aFactory;
      msg += ") - enter";

      LOGGER.debug(msg); //$NON-NLS-1$
    }
    // no else.

    /* Initialize this Package. */
    this.myQualifiedName = qualifiedName;
    this.myFactory = aFactory;

    /* Eventually log the exit of this method. */
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("JavaPackage() - exit"); //$NON-NLS-1$
    }
    // no else.
  }

  private String name = null;

  /*
   * (non-Javadoc)
   * 
   * @see tudresden.ocl20.pivot.pivotmodel.base.AbstractNamespace#getName()
   */
  @Override
  public String getName() {

    if (name == null) {
      name = this.myQualifiedName.get(this.myQualifiedName.size() - 1);
    }

    return name;
  }

  private Namespace namespace = null;

  /*
   * (non-Javadoc)
   * 
   * @see
   * tudresden.ocl20.pivot.pivotmodel.base.AbstractNamespace#getNestingNamespace
   * ()
   */
  @Override
  public Namespace getNestingNamespace() {

    
    if (namespace == null) {
      /* Check if this is the root name space. */
      if (this.myQualifiedName.size() <= 1) {
        namespace = null;
      }

      /* Else create the nesting name space. */
      else {
        List<String> qualifiedPath;

        /* Copy the qualified path to avoid side effects. */
        qualifiedPath = new ArrayList<String>(this.myQualifiedName);

        /* Remove this package from the path. */
        qualifiedPath.remove(this.myQualifiedName.size() - 1);

        namespace = this.myFactory.createNamespace(qualifiedPath);
      }
    }

    return namespace;
  }

  /*
   * (non-Javadoc)
   * 
   * @see tudresden.ocl20.pivot.pivotmodel.base.AbstractNamespace#getOwnedType()
   */
  @Override
  public List<Type> getOwnedType() {

    List<Type> result;

    result = this.myFactory.getOwnedTypes(this);

    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @seetudresden.ocl20.pivot.pivotmodel.base.AbstractNamespace#
   * getNestedNamespaceImpl()
   */
  @Override
  protected List<Namespace> getNestedNamespaceImpl() {

    List<Namespace> result;

    result = this.myFactory.getNestedNamespaces(this);

    return result;
  }
}
