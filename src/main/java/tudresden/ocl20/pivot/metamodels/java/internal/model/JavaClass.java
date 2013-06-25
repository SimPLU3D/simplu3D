/*
 * Copyright (C) 2009 by Claas Wilke (claaswilke@gmx.net)
 * 
 * This file is part of the Java Meta Model of Dresden OCL2 for Eclipse.
 * 
 * Dresden OCL2 for Eclipse is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * Dresden OCL2 for Eclipse is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Dresden OCL2 for Eclipse. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package tudresden.ocl20.pivot.metamodels.java.internal.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import tudresden.ocl20.pivot.metamodels.java.JavaMetaModelPlugin;
import tudresden.ocl20.pivot.model.ModelConstants;
import tudresden.ocl20.pivot.pivotmodel.Namespace;
import tudresden.ocl20.pivot.pivotmodel.Operation;
import tudresden.ocl20.pivot.pivotmodel.Property;
import tudresden.ocl20.pivot.pivotmodel.Type;
import tudresden.ocl20.pivot.pivotmodel.base.AbstractType;

/**
 * <p>
 * An implementation of the Pivot Model {@link Type} concept for Java.
 * </p>
 * 
 * @author Claas Wilke
 */
public class JavaClass extends AbstractType {

  /** The {@link Logger} for this class. */
  private static final Logger LOGGER = JavaMetaModelPlugin
      .getLogger(JavaClass.class);

  /** The adapted {@link Class}. */
  private Class<?> myClass;

  /** The {@link JavaAdapterFactory} the {@link JavaClass} belongs to. */
  private JavaAdapterFactory myFactory;

  /**
   * <p>
   * Creates a new {@link JavaClass} instance.
   * </p>
   * 
   * @param dslClass the {@link Class} that is adopted by this class.
   * @param aFactory The {@link JavaAdapterFactory}, the new created
   *          {@link JavaClass} shall belong to.
   */
  public JavaClass(Class<?> dslClass, JavaAdapterFactory aFactory) {

    /* Eventually log the entry into this method. */
    if (LOGGER.isDebugEnabled()) {
      String msg;

      msg = "JavaClass(";
      msg += "dslClass = " + dslClass;
      msg += ", aFactory = " + aFactory;
      msg += ") - enter";

      LOGGER.debug(msg); //$NON-NLS-1$
    }
    // no else.

    /* Initialize. */
    this.myClass = dslClass;
    this.myFactory = aFactory;

    /* Eventually log the exitr from this method. */
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("JavaClass() - exit"); //$NON-NLS-1$
    }
    // no else.
  }

  private String simpleName = null;

  /*
   * (non-Javadoc)
   * 
   * @see tudresden.ocl20.pivot.pivotmodel.base.AbstractType#getName()
   */
  @Override
  public String getName() {

    if (simpleName == null) {
      simpleName = this.myClass.getSimpleName();
    }

    return simpleName;
  }

  private Namespace nameSpace = null;

  /*
   * (non-Javadoc)
   * 
   * @see tudresden.ocl20.pivot.pivotmodel.base.AbstractType#getNamespace()
   */
  @Override
  public Namespace getNamespace() {

    if (nameSpace == null) {

      String[] namespacePath;
      List<String> namespaceList;

      namespaceList = new ArrayList<String>();
      namespaceList.add(ModelConstants.ROOT_PACKAGE_NAME);

      /* Add all packages of the canonical name to the path. */
      namespacePath = this.myClass.getCanonicalName().split("\\.");

      for (int index = 0; index < namespacePath.length - 1; index++) {
        namespaceList.add(namespacePath[index]);
      }

      /* Create the name space. */
      nameSpace = this.myFactory.createNamespace(namespaceList);
    }

    return nameSpace;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * tudresden.ocl20.pivot.pivotmodel.base.AbstractType#getOwnedPropertyImpl()
   */
  @Override
  protected List<Property> getOwnedPropertyImpl() {

    List<Property> result;

    result = new ArrayList<Property>();

    /* Add all fields to the result. */
    for (Field aField : this.myClass.getDeclaredFields()) {
      result.add(this.myFactory.createProperty(aField));
    }

    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * tudresden.ocl20.pivot.pivotmodel.base.AbstractType#getOwnedOperationImpl ()
   */
  @Override
  protected List<Operation> getOwnedOperationImpl() {

    List<Operation> result;

    result = new ArrayList<Operation>();

    /* Add all fields to the result. */
    for (Method aMethod : this.myClass.getDeclaredMethods()) {
      result.add(this.myFactory.createOperation(aMethod));
    }

    return result;
  }

  private List<Type> lTypes = null;

  /*
   * (non-Javadoc)
   * 
   * @see tudresden.ocl20.pivot.pivotmodel.base.AbstractType#getSuperTypeImpl()
   */
  @Override
  protected List<Type> getSuperTypeImpl() {

    if (lTypes == null) {
      lTypes = new ArrayList<Type>();

      /* Add the implemented interfaces to the result. */
      for (Class<?> aSuperClass : this.myClass.getInterfaces()) {
        lTypes.add(this.myFactory.createType(aSuperClass));
      }

      /* Eventually add the super class to the result. */
      if (this.myClass.getSuperclass() != null) {
        lTypes.add(this.myFactory.createType(this.myClass.getSuperclass()));
      }
      // no else.
    }

    return lTypes;
  }
}
