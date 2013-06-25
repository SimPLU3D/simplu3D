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

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.BasicEList;

import tudresden.ocl20.pivot.essentialocl.EssentialOclPlugin;
import tudresden.ocl20.pivot.essentialocl.types.CollectionType;
import tudresden.ocl20.pivot.metamodels.java.JavaMetaModelPlugin;
import tudresden.ocl20.pivot.pivotmodel.EnumerationLiteral;
import tudresden.ocl20.pivot.pivotmodel.Namespace;
import tudresden.ocl20.pivot.pivotmodel.Parameter;
import tudresden.ocl20.pivot.pivotmodel.ParameterDirectionKind;
import tudresden.ocl20.pivot.pivotmodel.PrimitiveTypeKind;
import tudresden.ocl20.pivot.pivotmodel.Type;

/**
 * <p>
 * A factory to create Pivot Model types for this meta model.
 * </p>
 * 
 * @author Claas Wilke
 */
public class JavaAdapterFactory {

  /** The {@link Logger} for this class. */
  private static final Logger LOGGER = JavaMetaModelPlugin
      .getLogger(JavaAdapterFactory.class);

  /**
   * A {@link Map} containing all {@link EnumerationLiteral}s already adapted.
   */
  private Map<Enum<?>, EnumerationLiteral> adaptedEnumLiterals;

  /** A {@link Map} containing all {@link Field}s already adapted. */
  private Map<Field, JavaField> adaptedFields;

  /** A {@link Map} containing all {@link Methods}s already adapted. */
  private Map<Method, JavaOperation> adaptedMethods;

  /** A {@link Map} containing all {@link Namespace}s already adapted. */
  private Map<String, Namespace> adaptedNamespaces;

  /** A {@link Map} containing all {@link Parameter}s already adapted. */
  private Map<String, Parameter> adaptedParameters;

  /**
   * A {@link Map} containing all {@link Class}es already adapted referenced by
   * their {@link Class} {@link Object}.
   */
  private Map<Class<?>, Type> adaptedTypes;

  /**
   * <p>
   * Creates a new {@link JavaAdapterFactory}.
   * </p>
   */
  public JavaAdapterFactory() {

    this.adaptedTypes = new WeakHashMap<Class<?>, Type>();
    this.adaptedFields = new WeakHashMap<Field, JavaField>();
    this.adaptedMethods = new WeakHashMap<Method, JavaOperation>();
    this.adaptedParameters = new HashMap<String, Parameter>();
    this.adaptedNamespaces = new HashMap<String, Namespace>();
    this.adaptedEnumLiterals = new WeakHashMap<Enum<?>, EnumerationLiteral>();
  }

  /**
   * <p>
   * A helper method which checks whether or not a given {@link Class}
   * represents a {@link Type} with multiple values (an array or a collection).
   * 
   * @param aType The {@link Class} which shall be checked.
   * @return True if the given {@link Class} has multiple values.
   */
  protected static boolean isMultiple(Class<?> aType) {

    boolean result;

    /* Check if the field's type is an array. */
    if (aType.isArray()) {
      result = true;
    }

    /* Else check if the fields type is a collection. */
    else if (Collection.class.isAssignableFrom(aType)) {
      result = true;
    }

    /* Else check if the fields type is a map. */
    else if (Map.class.isAssignableFrom(aType)) {
      result = true;
    }

    else {
      result = false;
    }

    return result;
  }

  /**
   * <p>
   * A helper method which checks whether or not a given {@link Class}
   * represents a {@link Type} with ordered values.
   * 
   * @param aType The {@link Class} which shall be checked.
   * @return True if the given {@link Class} contains ordered values.
   */
  protected static boolean isOrdered(Class<?> aType) {

    boolean result;

    /* Normally a Java field is ordered. */
    result = true;

    /* Sets are not ordered. */
    if (Set.class.isAssignableFrom(aType)) {
      result = false;
    }

    /* Maps are not ordered as well. */
    else if (Map.class.isAssignableFrom(aType)) {
      result = false;
    }
    // no else.

    return result;
  }

  /**
   * <p>
   * A helper method which checks whether or not a given {@link Class}
   * represents a {@link Type} with unique values.
   * 
   * @param aType The {@link Class} which shall be checked.
   * @return True if the given {@link Class} contains unique values.
   */
  protected static boolean isUnique(Class<?> aType) {

    boolean result;

    /* By default the result is true. */
    result = true;

    /* Arrays, Collections (except Sets), and Maps can have multiple values. */
    if (aType.isArray()) {
      result = false;
    }

    else if (Collection.class.isAssignableFrom(aType)) {

      if (Set.class.isAssignableFrom(aType)) {
        result = true;
      }

      else {
        result = false;
      }
    }

    else if (Map.class.isAssignableFrom(aType)) {
      result = false;
    }

    return result;
  }

  /**
   * <p>
   * Creates a new {@link JavaEnumerationLiteral} instance.
   * </p>
   * 
   * @param aLiteral The adapted {@link Object} of this
   *          {@link JavaEnumerationLiteral}.
   * @return A new {@link JavaEnumerationLiteral} instance.
   */
  public EnumerationLiteral createEnumerationLiteral(Enum<?> aLiteral) {

    /* Eventually log the entry of this method. */
    if (LOGGER.isDebugEnabled()) {
      String msg;

      msg = "createEnumerationLiteral(";
      msg += "aLiteral = " + aLiteral;
      msg += ") - enter";

      LOGGER.debug(msg); //$NON-NLS-1$
    }
    // no else.

    EnumerationLiteral result;

    /* Eventually use a cached result. */
    if (this.adaptedEnumLiterals.containsKey(aLiteral)) {
      result = this.adaptedEnumLiterals.get(aLiteral);
    }

    /* Else create the result. */
    else {
      result = new JavaEnumerationLiteral(aLiteral, this);

      /* Cache the result. */
      this.adaptedEnumLiterals.put(aLiteral, result);
    }

    /* Eventually log the exit of this method. */
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("createEnumerationLiteral() - exit"); //$NON-NLS-1$
    }
    // no else.

    return result;
  }

  /**
   * <p>
   * Creates a {@link List} of new input {@link JavaParameter} instances.
   * </p>
   * 
   * @param method The {@link Method} whose inpute parameters shall be created.
   * @return A new {@link List} of {@link JavaParameter} instances.
   */
  public List<Parameter> createInputParameters(Method method) {

    /* Eventually log the entry of this method. */
    if (LOGGER.isDebugEnabled()) {
      String msg;

      msg = "createInputParameters(";
      msg += "method = " + method;
      msg += ") - enter";

      LOGGER.debug(msg); //$NON-NLS-1$
    }
    // no else.

    List<Parameter> result;
    Class<?>[] classes;
    java.lang.reflect.Type[] genericTypes;

    classes = method.getParameterTypes();
    genericTypes = method.getGenericParameterTypes();

    result = new BasicEList<Parameter>();

    for (int index = 0; index < classes.length; index++) {
      result.add(this.createParameter(classes[index], genericTypes[index],
          ParameterDirectionKind.IN, method, index + 1));
    }

    /* Eventually log the exit of this method. */
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("createInputParameters() - exit"); //$NON-NLS-1$
    }
    // no else.

    return result;
  }

  /**
   * <p>
   * Creates a {@link Namespace} for its given qualified name as a {@link List}
   * of {@link Namespace} names.
   * </p>
   * 
   * @param qualifiedPath The qualified name of the {@link Namespace} which
   *          shall be created as a {@link List} of {@link Namespace} names.
   * @return The created {@link Namespace}.
   */
  public Namespace createNamespace(List<String> qualifiedPath) {

    Namespace result;
    String qualifiedName;

    qualifiedName = this.toQualifiedName(qualifiedPath);

    /* Eventually use a cached result. */
    if (this.adaptedNamespaces.containsKey(qualifiedName)) {
      result = this.adaptedNamespaces.get(qualifiedName);
    }

    /* Else create a new result. */
    else {
      result = new JavaPackage(qualifiedPath, this);
      this.adaptedNamespaces.put(qualifiedName, result);
    }

    return result;
  }

  /**
   * <p>
   * Creates a new {@link JavaClass} instance.
   * </p>
   * 
   * @param aClass The related {@link Class} of this {@link JavaClass}.
   * @return A new {@link JavaClass} instance.
   */
  public Type createType(Class<?> aClass) {

    /* Eventually log the entry of this method. */
    if (LOGGER.isDebugEnabled()) {
      String msg;

      msg = "createType(";
      msg += "aClass = " + aClass;
      msg += ") - enter";

      LOGGER.debug(msg); //$NON-NLS-1$
    }
    // no else.

    Type result;

    /* Check if the class has been adapted already. */
    if (this.adaptedTypes.containsKey(aClass)) {
      result = this.adaptedTypes.get(aClass);
    }

    /* Check if the given type is the void type. */
    else if (aClass.equals(void.class)) {
      result = EssentialOclPlugin.getOclLibraryProvider().getOclLibrary()
          .getOclVoid();
    }

    /* Else create the result. */
    else {

      /* Check if the class is a primitive type. */
      if (!JavaPrimitiveType.getPrimitiveTypeKind(aClass).equals(
          PrimitiveTypeKind.UNKNOWN)) {
        result = new JavaPrimitiveType(aClass, this);
      }

      /* Else check if the class is an Enumeration. */
      else if (aClass.isEnum()) {
        result = new JavaEnumeration(aClass, this);
      }

      /* Else create a regular type. */
      else {
        result = new JavaClass(aClass, this);
      }

      /*
       * Navigate to the root name space to register this packages transitively
       * with the root name space (eventually unknown sub name spaces are
       * adapted).
       */
      Namespace aNamespace;

      aNamespace = result.getNamespace();

      while (aNamespace != null) {
        aNamespace = aNamespace.getNestingNamespace();
      }

      /* Store the result. */
      adaptedTypes.put(aClass, result);
    }

    /* Eventually log the exit of this method. */
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("createType() - exit"); //$NON-NLS-1$
    }
    // no else.

    return result;
  }

  /**
   * <p>
   * Creates a new {@link JavaOperation} instance.
   * </p>
   * 
   * @param aField The related {@link Method} of this {@link JavaOperation}.
   * @return A new {@link JavaOperation} instance.
   */
  public JavaOperation createOperation(Method aMethod) {

    /* Eventually log the entry of this method. */
    if (LOGGER.isDebugEnabled()) {
      String msg;

      msg = "createMethod(";
      msg += "aMethod = " + aMethod;
      msg += ") - enter";

      LOGGER.debug(msg); //$NON-NLS-1$
    }
    // no else.

    JavaOperation result;

    /* Check if the class has been adapted already. */
    if (this.adaptedMethods.containsKey(aMethod)) {
      result = this.adaptedMethods.get(aMethod);
    }

    /* Else create the result. */
    else {
      result = new JavaOperation(aMethod, this);

      /* Cache the result. */
      this.adaptedMethods.put(aMethod, result);
    }

    /* Eventually log the exit of this method. */
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("createMethod() - exit"); //$NON-NLS-1$
    }
    // no else.

    return result;
  }

  /**
   * <p>
   * Creates a new {@link JavaParameter} instance.
   * </p>
   * 
   * @param aClass The {@link Class} that shall be adapted.
   * @param genericType The generic {@link java.lang.reflect.Type} of the
   *          {@link JavaParameter} or <code>null</code>.
   * @param parameterKind The {@link ParameterDirectionKind} of the
   *          {@link JavaParameter}.
   * @param method The {@link Method} of the {@link JavaParameter}.
   * @param parameterNumber A number used to create a unique parameter name.
   * @return A new {@link JavaParameter} instance.
   */
  public Parameter createParameter(Class<?> aClass,
      java.lang.reflect.Type genericType, ParameterDirectionKind parameterKind,
      Method method, int parameterNumber) {

    /* Eventually log the entry of this method. */
    if (LOGGER.isDebugEnabled()) {
      String msg;

      msg = "createParameter(";
      msg += "aClass = " + aClass;
      msg += ", genericType = " + genericType;
      msg += ", parameterKind = " + parameterKind;
      msg += ", method = " + method;
      msg += ", parameterNumber = " + parameterNumber;
      msg += ") - enter";

      LOGGER.debug(msg); //$NON-NLS-1$
    }
    // no else.

    Parameter result;
    String key;

    /* Generate a key to cache the parameter. */
    key = aClass.toString();
    key += genericType.toString();
    key += parameterKind.toString();
    key += method.toString();
    key += parameterNumber;

    /* Eventually use a cached parameter. */
    if (this.adaptedParameters.containsKey(key)) {
      result = this.adaptedParameters.get(key);
    }

    /* Else create the parameters. */
    else {
      result = new JavaParameter(aClass, genericType, parameterKind, method,
          parameterNumber, this);

      /* Cache the result. */
      this.adaptedParameters.put(key, result);
    }

    /* Eventually log the exit of this method. */
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("createParameter() - exit"); //$NON-NLS-1$
    }
    // no else.

    return result;
  }

  /**
   * <p>
   * Creates a new {@link JavaField} instance.
   * </p>
   * 
   * @param aField The related {@link Field} of this {@link JavaField}.
   * @return A new {@link JavaField} instance.
   */
  public JavaField createProperty(Field aField) {

    /* Eventually log the entry of this method. */
    if (LOGGER.isDebugEnabled()) {
      String msg;

      msg = "createProperty(";
      msg += "aField = " + aField;
      msg += ") - enter";

      LOGGER.debug(msg); //$NON-NLS-1$
    }
    // no else.

    JavaField result;

    /* Check if the class has been adapted already. */
    if (this.adaptedFields.containsKey(aField)) {
      result = this.adaptedFields.get(aField);
    }

    /* Else create the result. */
    else {
      result = new JavaField(aField, this);
      adaptedFields.put(aField, result);
    }

    /* Eventually log the exit of this method. */
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("createProperty() - exit"); //$NON-NLS-1$
    }
    // no else.

    return result;
  }

  /**
   * <p>
   * Returns all {@link Namespace} that are contained in a given
   * {@link Namespace}.
   * </p>
   * 
   * @param aNamespace The {@link Namespace} whose children shall be returned.
   * @return All found adapted {@link Namespace} located in this
   *         {@link Namespace}.
   */
  public List<Namespace> getNestedNamespaces(Namespace aNamespace) {

    List<Namespace> result;

    result = new ArrayList<Namespace>();

    /* Iterate through all adapted name spaces. */
    for (String aQualifiedName : this.adaptedNamespaces.keySet()) {

      Namespace anAdaptedNamespace;

      anAdaptedNamespace = this.adaptedNamespaces.get(aQualifiedName);

      if (anAdaptedNamespace.getNestingNamespace() != null
          && anAdaptedNamespace.getNestingNamespace().equals(aNamespace)) {
        result.add(anAdaptedNamespace);
      }
      // no else.
    }
    // end for.

    return result;
  }

  /**
   * <p>
   * Returns all {@link Type}s that are contained in a given {@link Namespace} .
   * </p>
   * 
   * @param aNamespace The {@link Namespace} whose children shall be returned.
   * @return All found adapted {@link Type}s located in this {@link Namespace} .
   */
  public List<Type> getOwnedTypes(Namespace aNamespace) {

    List<Type> result = new BasicEList<Type>();

    /* Iterate through all adapted name spaces. */
    for (Class<?> aClass : this.adaptedTypes.keySet()) {

      Type anAdaptedType;

      anAdaptedType = this.adaptedTypes.get(aClass);

      /* Special handling of the root name space. */
      if (aNamespace.getNestingNamespace() == null
          && anAdaptedType.getNamespace().getNestingNamespace() == null) {
        result.add(anAdaptedType);
      }

      /* Handling for other name spaces. */
      else if (anAdaptedType.getNamespace().equals(aNamespace)) {
        result.add(anAdaptedType);
      }
      // no else.
    }
    // end for.

    return result;
  }

  /**
   * <p>
   * A helper method which adapts a given Java {@link Class} as a pivot model
   * {@link Type}.
   * 
   * @param aClass The Java {@link Class} which shall be adapted.
   * @param aGenericType The generic {@link java.lang.reflect.Type} to the given
   *          {@link Class} or null, if the {@link Class} does not represent a
   *          generic type.
   * @return The adapted {@link Type}.
   */
  protected Type adaptJavaType(Class<?> aClass,
      java.lang.reflect.Type aGenericType) {

    Type result;

    /* If the type is an array convert the array to a collection. */
    if (aClass.isArray()) {

      if (aGenericType instanceof GenericArrayType) {

        GenericArrayType genericArrayType;
        genericArrayType = (GenericArrayType) aGenericType;

        java.lang.reflect.Type componentType = genericArrayType
            .getGenericComponentType();

        Type elementType;

        if (componentType instanceof ParameterizedType) {

          ParameterizedType paramterizedType;
          paramterizedType = (ParameterizedType) componentType;

          elementType = this.adaptParameterizedType(paramterizedType);
          elementType = this.adaptCollectionClass(
              (Class<?>) paramterizedType.getRawType(), elementType);
        }

        else if (componentType instanceof Class<?>) {
          Class<?> componentClass;
          componentClass = (Class<?>) componentType;

          if (componentClass.isArray()) {
            elementType = this.adaptArrayClass(componentClass);
          }

          else {
            elementType = this.createType(componentClass);
          }
        }

        else {
          elementType = this.createType(Object.class);
        }

        result = EssentialOclPlugin.getOclLibraryProvider().getOclLibrary()
            .getSequenceType(elementType);
      }

      else {
        result = this.adaptArrayClass(aClass);
      }
    }

    /* Else if the type is a collection get its generic type. */
    else if (Collection.class.isAssignableFrom(aClass)) {

      Type genericType;

      if (aGenericType instanceof ParameterizedType) {
        genericType = adaptParameterizedType((ParameterizedType) aGenericType);
      }

      else {
        genericType = this.createType(Object.class);
      }

      result = this.adaptCollectionClass(aClass, genericType);
    }

    else {
      result = this.createType(aClass);
    }

    return result;
  }

  /**
   * <p>
   * Adapts a given array {@link Class} to a {@link CollectionType}.
   * </p>
   * 
   * @param aClass The array {@link Class} that shall be converted into a
   *          {@link CollectionType}.
   * @return A {@link CollectionType} (probably nesting further
   *         {@link CollectionType}s).
   */
  private Type adaptArrayClass(Class<?> aClass) {

    Type result;
    Class<?> elementClass;

    /* Get the class of the elements. */
    elementClass = aClass.getComponentType();

    Type elementType;

    /* Recall method recursively, if element type is an array again. */
    if (elementClass.isArray()) {
      elementType = this.adaptArrayClass(elementClass);
    }

    else {
      elementType = this.createType(elementClass);
    }

    result = EssentialOclPlugin.getOclLibraryProvider().getOclLibrary()
        .getSequenceType(elementType);

    return result;
  }

  /**
   * <p>
   * Adapts a given {@link Class} to a {@link CollectionType} having the given
   * {@link Type} as its element {@link Type}.
   * </p>
   * 
   * @param aClass The {@link Class} that shall be converted into a
   *          {@link CollectionType}.
   * @param genericType The element {@link Type} of the collection.
   * @return A {@link CollectionType} (probably nesting further
   *         {@link CollectionType}s).
   */
  private Type adaptCollectionClass(Class<?> aClass, Type genericType) {

    Type result;

    if (List.class.isAssignableFrom(aClass)) {
      result = EssentialOclPlugin.getOclLibraryProvider().getOclLibrary()
          .getSequenceType(genericType);
    }

    else if (Set.class.isAssignableFrom(aClass)) {
      result = EssentialOclPlugin.getOclLibraryProvider().getOclLibrary()
          .getSetType(genericType);
    }

    else {
      result = EssentialOclPlugin.getOclLibraryProvider().getOclLibrary()
          .getCollectionType(genericType);
    }

    return result;
  }

  /**
   * <p>
   * Converts a given {@link ParameterizedType} to a {@link CollectionType}.
   * </p>
   * 
   * @param parameterizedType The {@link ParameterizedType} that shall be
   *          converted to a {@link CollectionType}.
   * @return The converted {@link CollectionType}.
   */
  private Type adaptParameterizedType(ParameterizedType parameterizedType) {

    Type result;

    java.lang.reflect.Type arguments[];
    java.lang.reflect.Type argument;

    arguments = parameterizedType.getActualTypeArguments();

    /* Use the first generic type of the collection as type. */
    argument = arguments[0];

    if (argument instanceof Class<?>) {

      Class<?> elementClass;
      elementClass = (Class<?>) argument;

      result = this.createType(elementClass);
    }

    else if (argument instanceof TypeVariable<?>) {
      result = this.createType((Class<?>) ((TypeVariable<?>) argument)
          .getBounds()[0]);
    }

    else if (argument instanceof ParameterizedType) {
      Type genericType;
      genericType = this.adaptParameterizedType((ParameterizedType) argument);

      result = this.adaptCollectionClass(
          (Class<?>) ((ParameterizedType) argument).getRawType(), genericType);
    }

    else if (argument instanceof GenericArrayType) {

      GenericArrayType genericArrayType;
      genericArrayType = (GenericArrayType) argument;

      java.lang.reflect.Type componentType = genericArrayType
          .getGenericComponentType();

      Type elementType;

      if (componentType instanceof ParameterizedType) {
        elementType = this
            .adaptParameterizedType((ParameterizedType) componentType);
      }

      else if (componentType instanceof Class<?>) {
        Class<?> componentClass;
        componentClass = (Class<?>) componentType;

        if (componentClass.isArray()) {
          elementType = this.adaptArrayClass(componentClass);
        }

        else {
          elementType = this.createType(componentClass);
        }
      }

      else {
        elementType = this.createType(Object.class);
      }

      result = EssentialOclPlugin.getOclLibraryProvider().getOclLibrary()
          .getSequenceType(elementType);
    }

    else {
      LOGGER.warn("Adapted Generic Type " + argument + " to java.lang.Object.");
      result = this.createType(Object.class);
    }

    return result;
  }

  /**
   * <p>
   * A helper method that converts a given qualified path (a {@link List} of
   * {@link String}s representing {@link Namespace}s) into a String containing
   * this names separated by <code>::</code>.
   * </p>
   * 
   * @param qualifiedPath The path which shall be converted.
   * @return The converted qualifiedName
   */
  private String toQualifiedName(List<String> qualifiedPath) {

    StringBuffer bF = new StringBuffer();

    int nbElem = qualifiedPath.size();
    bF.append(qualifiedPath.get(0));
    for (int i = 1; i < nbElem; i++) {

      bF.append("::");
      bF.append(qualifiedPath.get(i));
    }

    return bF.toString();

    /* Copy the list to avoid side effects. */
    // Code initiale
    /*
     * String result; // System.out.println("Et j'y passe");
     * 
     * 
     * 
     * 
     * 
     * qualifiedPath = new ArrayList<String>(qualifiedPath);
     * 
     * result = qualifiedPath.remove(0);
     * 
     * while (qualifiedPath.size() > 0) { result += "::" +
     * qualifiedPath.remove(0); } return result;
     */

  }
}
