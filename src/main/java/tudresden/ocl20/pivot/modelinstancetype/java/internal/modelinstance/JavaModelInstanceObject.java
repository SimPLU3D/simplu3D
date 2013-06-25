/*
Copyright (C) 2009 by Claas Wilke (claaswilke@gmx.net)

This file is part of the Java Model Instance Plug-in of Dresden OCL2 for Eclipse.

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
package tudresden.ocl20.pivot.modelinstancetype.java.internal.modelinstance;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.osgi.util.NLS;

import tudresden.ocl20.pivot.modelinstance.base.AbstractModelInstance;
import tudresden.ocl20.pivot.modelinstancetype.exception.AsTypeCastException;
import tudresden.ocl20.pivot.modelinstancetype.exception.CopyForAtPreException;
import tudresden.ocl20.pivot.modelinstancetype.exception.OperationAccessException;
import tudresden.ocl20.pivot.modelinstancetype.exception.OperationNotFoundException;
import tudresden.ocl20.pivot.modelinstancetype.exception.PropertyAccessException;
import tudresden.ocl20.pivot.modelinstancetype.exception.PropertyNotFoundException;
import tudresden.ocl20.pivot.modelinstancetype.java.JavaModelInstanceTypePlugin;
import tudresden.ocl20.pivot.modelinstancetype.java.internal.msg.JavaModelInstanceTypeMessages;
import tudresden.ocl20.pivot.modelinstancetype.java.internal.util.JavaModelInstanceTypeUtility;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceElement;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceObject;
import tudresden.ocl20.pivot.modelinstancetype.types.base.AbstractModelInstanceObject;
import tudresden.ocl20.pivot.modelinstancetype.types.base.JavaModelInstanceCollection;
import tudresden.ocl20.pivot.pivotmodel.Operation;
import tudresden.ocl20.pivot.pivotmodel.Parameter;
import tudresden.ocl20.pivot.pivotmodel.Property;
import tudresden.ocl20.pivot.pivotmodel.Type;

/**
 * <p>
 * Represents model objects of a {@link JavaModelInstance}.
 * </p>
 * 
 * @author Claas Wilke
 */
public class JavaModelInstanceObject extends AbstractModelInstanceObject
		implements IModelInstanceObject {

	/** The {@link Logger} for this class. */
	private static final Logger LOGGER = JavaModelInstanceTypePlugin
			.getLogger(JavaModelInstanceObject.class);

	/** The Java {@link Object} adapted by this {@link JavaModelInstanceObject}. */
	private Object myAdaptedObject;

	/**
	 * The Java {@link Class} this {@link JavaModelInstanceObject} is casted to.
	 * This is required to access the right {@link Property}s and
	 * {@link Operation}s.
	 */
	private Class<?> myAdaptedType;

	/**
	 * The {@link JavaModelInstanceFactory} of this
	 * {@link JavaModelInstanceCollection}. Required to adapt the contained
	 * {@link Object}s to {@link IModelInstanceElement}s.
	 */
	private JavaModelInstanceFactory myFactory;

	/**
	 * <p>
	 * Creates a new {@link JavaModelInstanceObject}.
	 * </p>
	 * 
	 * @param object
	 *            The {@link Object} for which an
	 *            {@link JavaModelInstanceObject} shall be created.
	 * @param type
	 *            The {@link Type} this {@link IModelInstanceElement} belongs
	 *            to.
	 * @param originalType
	 *            The original {@link Type} this {@link IModelInstanceElement}
	 *            belongs to.
	 * @param factory
	 *            The {@link JavaModelInstanceFactory} of this
	 *            {@link JavaModelInstanceObject}. Required to adapt the
	 *            {@link Object}s of accesses {@link Property}s or results of
	 *            invoked {@link Operation}s.
	 */
	protected JavaModelInstanceObject(Object object, Type type,
			Type originalType, JavaModelInstanceFactory factory) {

		super(type, originalType);

		/* Probably debug the entry of this method. */
		if (LOGGER.isDebugEnabled()) {
			String msg;

			msg = "JavaModelInstanceObject("; //$NON-NLS-1$
			msg += "object = " + object; //$NON-NLS-1$
			msg += ", type = " + type; //$NON-NLS-1$
			msg += ", factory = " + factory; //$NON-NLS-1$
			msg += ")"; //$NON-NLS-1$

			LOGGER.debug(msg);
		}
		// no else.

		if (object != null) {
			this.initialize(object, object.getClass(), type, factory);
		}

		else {
			this.initialize(null, null, type, factory);
		}

		/* Probably debug the exit of this method. */
		if (LOGGER.isDebugEnabled()) {
			String msg;

			msg = "JavaModelInstanceObject(Object, "; //$NON-NLS-1$
			msg += "Type, IModelInstanceFactory) - exit"; //$NON-NLS-1$

			LOGGER.debug(msg);
		}
		// no else.
	}

	/**
	 * <p>
	 * Creates a new {@link JavaModelInstanceObject}.
	 * </p>
	 * 
	 * @param object
	 *            The {@link Object} for which an
	 *            {@link JavaModelInstanceObject} shall be created.
	 * @param castedToClass
	 *            The Java {@link Class} this {@link JavaModelInstanceObject} is
	 *            casted to. This is required to access the right
	 *            {@link Property}s and {@link Operation}s.
	 * @param type
	 *            The {@link Type} this {@link IModelInstanceElement} belongs
	 *            to.
	 * @param originalType
	 *            The original {@link Type} this {@link IModelInstanceElement}
	 *            belongs to.
	 * @param factory
	 *            The {@link JavaModelInstanceFactory} of this
	 *            {@link JavaModelInstanceObject}. Required to adapt the
	 *            {@link Object}s of accesses {@link Property}s or results of
	 *            invoked {@link Operation}s.
	 */
	protected JavaModelInstanceObject(Object object, Class<?> castedToClass,
			Type type, Type originalType, JavaModelInstanceFactory factory) {

		super(type, originalType);

		/* Probably debug the entry of this method. */
		if (LOGGER.isDebugEnabled()) {
			String msg;

			msg = "JavaModelInstanceObject("; //$NON-NLS-1$
			msg += "object = " + object; //$NON-NLS-1$
			msg += ", castedToClass = " + castedToClass; //$NON-NLS-1$
			msg += ", type = " + type; //$NON-NLS-1$
			msg += ", factory = " + factory; //$NON-NLS-1$
			msg += ")"; //$NON-NLS-1$

			LOGGER.debug(msg);
		}
		// no else.

		this.initialize(object, castedToClass, type, factory);

		/* Probably debug the exit of this method. */
		if (LOGGER.isDebugEnabled()) {
			String msg;

			msg = "JavaModelInstanceObject(Object, "; //$NON-NLS-1$
			msg += "Type, IModelInstanceFactory) - exit"; //$NON-NLS-1$

			LOGGER.debug(msg);
		}
		// no else.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tudresden.ocl20.pivot.modelbus.modelinstance.types.IModelInstanceElement
	 * #asType(tudresden.ocl20.pivot.pivotmodel.Type)
	 */
	public IModelInstanceElement asType(Type type) throws AsTypeCastException {

		if (type == null) {
			throw new IllegalArgumentException(
					"Parameter 'type' must not be null.");
		}
		// no else.

		IModelInstanceElement result;

		String typeClassName;
		Class<?> typeClass;

		Set<Type> types;

		types = new HashSet<Type>();
		types.add(type);

		/* For undefined elements, only model types can be checked. */
		if (this.myAdaptedObject == null) {

			result = null;

			/* If the type can be casted in the model, cast it. */
			if (type.conformsTo(this.myType)) {
				result = new JavaModelInstanceObject(null, type, this
						.getOriginalType(), this.myFactory);
			}
			// no else.

			/* If no cast has been done, throw an exception. */
			if (result == null) {
				String msg;

				msg = JavaModelInstanceTypeMessages.JavaModelInstance_CannotCast;
				msg = NLS.bind(msg, this.getName(), type);

				throw new AsTypeCastException(msg);
			}
			// no else.
		}

		/* Else handle the not undefined object. */
		else {
			/* Get a canonical name for the given type. */
			typeClassName = JavaModelInstanceTypeUtility.toCanonicalName(type
					.getQualifiedNameList());

			/* Try to find a class that is represented by the given type. */
			try {
				typeClass = this.myAdaptedObject.getClass().getClassLoader()
						.loadClass(typeClassName);
			}

			/* If no class has been found, throw an exception. */
			catch (ClassNotFoundException e) {
				String msg;

				msg = JavaModelInstanceTypeMessages.JavaModelInstance_CannotCastTypeClassNotFound;
				msg = NLS.bind(msg, this.getName(), type);

				throw new AsTypeCastException(msg, e);
			}

			/* Check if this object can be casted to the found class. */
			if (typeClass.isAssignableFrom(this.myAdaptedObject.getClass())) {

				/* Cast this object to the found type. */
				result = new JavaModelInstanceObject(this.myAdaptedObject,
						typeClass, type, this.getOriginalType(), this.myFactory);
			}

			/* Else throw an exception. */
			else {
				String msg;

				msg = JavaModelInstanceTypeMessages.JavaModelInstance_CannotCast;
				msg = NLS.bind(msg, this.getName(), type);

				throw new AsTypeCastException(msg);
			}
			// end else.
		}
		// end else.

		return result;
	}

	/**
	 * <p>
	 * Performs a copy of the adapted element of this
	 * {@link IModelInstanceElement} that can be used to store it as a @pre
	 * value of a postcondition's expression. The method should copy the adapted
	 * object and all its references that are expected to change during the
	 * methods execution the postcondition is defined on.
	 * </p>
	 * 
	 * <p>
	 * For {@link JavaModelInstanceObject}s this method tries to clone the
	 * adapted {@link Object} if the {@link Object} implements {@link Cloneable}
	 * . Else the {@link Object} will be copied using an probably existing empty
	 * {@link Constructor} and a flat copy will be created (means all attributes
	 * and associations will lead to the same values and identities. <strong>If
	 * neither the <code>clone()</code> method nor the emptry
	 * {@link Constructor} are provided, this operation will fail with an
	 * {@link CopyForAtPreException}.</strong>
	 * </p>
	 * 
	 * @return A copy of the adapted element.
	 * @throws CopyForAtPreException
	 *             Thrown, if an error during the copy process occurs.
	 */
	public IModelInstanceElement copyForAtPre() throws CopyForAtPreException {

		IModelInstanceElement result;
		result = null;

		/* Check if the adapted object is clone-able. */
		if (this.myAdaptedObject instanceof Cloneable) {

			try {
				result = copyForAtPreWithClone();
			}

			catch (CopyForAtPreException e) {
				/* Catch the exception and try to copy again with reflections. */
			}
		}
		// no else.

		/*
		 * If not object has been created yet. Try to copy the object with
		 * reflections.
		 */
		if (result == null) {

			result = this.copyForAtPreWithReflections();
		}
		// no else.

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seetudresden.ocl20.pivot.modelbus.modelinstance.IModelInstanceObject#
	 * getAdaptedObject()
	 */
	public Object getObject() {

		return this.myAdaptedObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tudresden.ocl20.pivot.modelbus.modelinstance.types.IModelInstanceObject
	 * #invokeOperation(tudresden.ocl20.pivot.pivotmodel.Operation,
	 * java.util.List)
	 */
	public IModelInstanceElement invokeOperation(Operation operation,
			List<IModelInstanceElement> args)
			throws OperationNotFoundException, OperationAccessException {

		if (operation == null) {
			throw new IllegalArgumentException(
					"Parameter 'operation' must not be null.");
		}
		// no else.

		else if (args == null) {
			throw new IllegalArgumentException(
					"Parameter 'args' must not be null.");
		}
		// no else.

		IModelInstanceElement result;

		/* Check if this object is undefined. */
		if (this.myAdaptedObject == null) {

			result = this.myFactory.createModelInstanceElement(null, operation
					.getType());
		}

		/* Else find and invoke the operation. */
		else {
			Method operationMethod;

			int argSize;
			Class<?>[] argumentTypes;
			Object[] argumentValues;

			/* Try to find the method to invoke. */
			operationMethod = this.findMethodOfAdaptedObject(operation);

			argumentTypes = operationMethod.getParameterTypes();
			argumentValues = new Object[args.size()];

			/* Avoid errors through to much arguments given by the invocation. */
			argSize = Math.min(args.size(), operation.getSignatureParameter()
					.size());

			/* Adapt the argument values. */
			for (int index = 0; index < argSize; index++) {

				Set<ClassLoader> classLoaders = new HashSet<ClassLoader>();
				classLoaders.add(this.myAdaptedType.getClassLoader());

				argumentValues[index] = JavaModelInstance.createAdaptedElement(
						args.get(index), argumentTypes[index], classLoaders);
			}

			/* Try to invoke the found method. */
			try {
				Object adapteeResult;
				operationMethod.setAccessible(true);

				adapteeResult = operationMethod.invoke(this.myAdaptedObject,
						argumentValues);

				/* Adapt the result to the expected result type. */
				result = AbstractModelInstance.adaptInvocationResult(
						adapteeResult, operation.getType(), this.myFactory);
			}

			catch (IllegalArgumentException e) {
				String msg;

				msg = JavaModelInstanceTypeMessages.JavaModelInstance_OperationAccessFailed;
				msg = NLS.bind(msg, operation, e.getMessage());

				throw new OperationAccessException(msg, e);
			}

			catch (IllegalAccessException e) {
				String msg;

				msg = JavaModelInstanceTypeMessages.JavaModelInstance_OperationAccessFailed;
				msg = NLS.bind(msg, operation, e.getMessage());

				throw new OperationAccessException(msg, e);
			}

			catch (InvocationTargetException e) {
				String msg;

				msg = JavaModelInstanceTypeMessages.JavaModelInstance_OperationAccessFailed;
				msg = NLS.bind(msg, operation, e.getMessage());

				throw new OperationAccessException(msg, e);
			}
		}
		// end else.

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tudresden.ocl20.pivot.modelbus.modelinstance.types.IModelInstanceObject
	 * #getProperty(tudresden.ocl20.pivot.pivotmodel.Property)
	 */
	public IModelInstanceElement getProperty(Property property)
			throws PropertyAccessException, PropertyNotFoundException {

		if (property == null) {
			throw new IllegalArgumentException(
					"Parameter 'property' must not be null.");
		}
		// no else.

		IModelInstanceElement result;

		Class<?> propertySourceClass;
		Field propertyField;

		/* Check if this object is undefined. */
		if (this.myAdaptedObject == null) {

			result = this.myFactory.createModelInstanceElement(null, property
					.getType());
		}

		/*
		 * Try to find a field with the property's name in the class the adapted
		 * object is currently casted to.
		 */
		else {
			propertyField = null;

			/*
			 * Search through all super classes as well. Until the field has
			 * been found, or no more super classes exist.
			 */
			propertySourceClass = this.myAdaptedType;

			while (propertySourceClass != null && propertyField == null) {

				try {
					propertyField = propertySourceClass
							.getDeclaredField(property.getName());
				}

				catch (NoSuchFieldException e) {
					/* Try to find the field again in super class. */
					propertySourceClass = propertySourceClass.getSuperclass();
				}
			}
			// end while.

			/* Check if the field has been found. */
			if (propertyField != null) {
				Object propertyValue;

				propertyValue = null;
				propertyField.setAccessible(true);

				/* Try to get the field's value. */
				try {
					propertyValue = propertyField.get(this.myAdaptedObject);

					result = JavaModelInstance.adaptInvocationResult(
							propertyValue, property.getType(), this.myFactory);
				}

				catch (IllegalArgumentException e) {
					String msg;

					msg = JavaModelInstanceTypeMessages.JavaModelInstance_PropertyAccessFailed;
					msg = NLS.bind(msg, property, e.getMessage());

					throw new PropertyAccessException(msg, e);
				}

				catch (IllegalAccessException e) {
					String msg;

					msg = JavaModelInstanceTypeMessages.JavaModelInstance_PropertyAccessFailed;
					msg = NLS.bind(msg, property, e.getMessage());

					throw new PropertyAccessException(msg, e);
				}
			}
			// no else.

			/* Else throw an exception. */
			else {
				String msg;

				msg = JavaModelInstanceTypeMessages.JavaModelInstance_PropertyNotFoundInModelInstanceElement;
				msg = NLS.bind(msg, property, this.myAdaptedObject.getClass());

				throw new PropertyNotFoundException(msg);
			}
			// end else.
		}
		// end else.

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {

		String result;

		result = this.getClass().getSimpleName();
		result += "[";
		result += this.myAdaptedObject.toString();
		result += "]";

		return result;
	}

	/**
	 * <p>
	 * A helper method that tries to copy the adapted object using the method
	 * {@link Object#clone()}.
	 * </p>
	 * 
	 * @return A copy of the adapted {@link Object} of this
	 *         {@link JavaModelInstanceObject}.
	 * @throws CopyForAtPreException
	 *             Thrown, if we adapted {@link Object} cannot be copied via
	 *             clone method.
	 */
	private IModelInstanceElement copyForAtPreWithClone()
			throws CopyForAtPreException {

		IModelInstanceElement result;
		Method cloneMethod;

		/* Try to find and invoke the clone method. */
		try {
			Object adaptedResult;

			cloneMethod = this.myAdaptedObject.getClass().getMethod("clone");
			cloneMethod.setAccessible(true);

			adaptedResult = cloneMethod.invoke(this.myAdaptedObject);
			result = new JavaModelInstanceObject(adaptedResult,
					this.myAdaptedType, this.myType, this.getOriginalType(),
					this.myFactory);
		}

		catch (SecurityException e) {
			String msg;

			msg = JavaModelInstanceTypeMessages.JavaModelInstance_CannotCopyForAtPre;
			msg = NLS.bind(msg, this.getName(), e.getMessage());

			throw new CopyForAtPreException(msg, e);
		}

		catch (NoSuchMethodException e) {
			String msg;

			msg = JavaModelInstanceTypeMessages.JavaModelInstance_CannotCopyForAtPre;
			msg = NLS.bind(msg, this.getName(), e.getMessage());

			throw new CopyForAtPreException(msg, e);
		}

		catch (IllegalArgumentException e) {
			String msg;

			msg = JavaModelInstanceTypeMessages.JavaModelInstance_CannotCopyForAtPre;
			msg = NLS.bind(msg, this.getName(), e.getMessage());

			throw new CopyForAtPreException(msg, e);
		}

		catch (IllegalAccessException e) {
			String msg;

			msg = JavaModelInstanceTypeMessages.JavaModelInstance_CannotCopyForAtPre;
			msg = NLS.bind(msg, this.getName(), e.getMessage());

			throw new CopyForAtPreException(msg, e);
		}

		catch (InvocationTargetException e) {
			String msg;

			msg = JavaModelInstanceTypeMessages.JavaModelInstance_CannotCopyForAtPre;
			msg = NLS.bind(msg, this.getName(), e.getMessage());

			throw new CopyForAtPreException(msg, e);
		}

		return result;
	}

	/**
	 * <p>
	 * A helper method that tries to copy the adapted {@link Object} of this
	 * {@link JavaModelInstanceObject} with an empty {@link Constructor} based
	 * on reflections. The copied {@link Object} will be a flat copy of this
	 * {@link Object}. Thus, the fields will all have the same value and id.
	 * </p>
	 * 
	 * @return A copy of the adapted {@link Object} of this
	 *         {@link JavaModelInstanceObject}.
	 * @throws CopyForAtPreException
	 *             Thrown, if the adapted {@link Object} cannot be copied using
	 *             an empty {@link Constructor}.
	 */
	private IModelInstanceObject copyForAtPreWithReflections()
			throws CopyForAtPreException {

		IModelInstanceObject result;
		Class<?> adapteeClass;

		adapteeClass = this.myAdaptedObject.getClass();

		try {
			Object copiedAdaptedObject;
			Constructor<?> emptyConstructor;

			/* Try to find an empty constructor. */
			emptyConstructor = adapteeClass.getConstructor(new Class[0]);

			/* Copy the adapted object. */
			copiedAdaptedObject = emptyConstructor.newInstance(new Object[0]);

			/* Iterate through the adapteeClass and all its super classes. */
			while (adapteeClass != null) {

				/* Initialize all fields with the same value. */
				for (Field field : adapteeClass.getDeclaredFields()) {

					field.setAccessible(true);

					/* Do not set static nor final fields. */
					if (!(Modifier.isFinal(field.getModifiers()) || Modifier
							.isStatic(field.getModifiers()))) {
						field.set(copiedAdaptedObject, field
								.get(this.myAdaptedObject));
					}
					// no else.
				}
				// end for.

				adapteeClass = adapteeClass.getSuperclass();
			}
			// end while.

			/* Create the adapter. */
			result = new JavaModelInstanceObject(copiedAdaptedObject,
					this.myAdaptedType, this.myType, this.getOriginalType(),
					this.myFactory);
		}

		catch (SecurityException e) {
			String msg;

			msg = JavaModelInstanceTypeMessages.JavaModelInstance_CannotCopyForAtPre;
			msg = NLS.bind(msg, this.getName(), e.getMessage());

			throw new CopyForAtPreException(msg, e);
		}

		catch (NoSuchMethodException e) {
			String msg;

			msg = JavaModelInstanceTypeMessages.JavaModelInstance_CannotCopyForAtPre;
			msg = NLS.bind(msg, this.getName(), e.getMessage());

			throw new CopyForAtPreException(msg, e);
		}

		catch (IllegalArgumentException e) {
			String msg;

			msg = JavaModelInstanceTypeMessages.JavaModelInstance_CannotCopyForAtPre;
			msg = NLS.bind(msg, this.getName(), e.getMessage());

			throw new CopyForAtPreException(msg, e);
		}

		catch (InstantiationException e) {
			String msg;

			msg = JavaModelInstanceTypeMessages.JavaModelInstance_CannotCopyForAtPre;
			msg = NLS.bind(msg, this.getName(), e.getMessage());

			throw new CopyForAtPreException(msg, e);
		}

		catch (IllegalAccessException e) {
			String msg;

			msg = JavaModelInstanceTypeMessages.JavaModelInstance_CannotCopyForAtPre;
			msg = NLS.bind(msg, this.getName(), e.getMessage());

			throw new CopyForAtPreException(msg, e);
		}

		catch (InvocationTargetException e) {
			String msg;

			msg = JavaModelInstanceTypeMessages.JavaModelInstance_CannotCopyForAtPre;
			msg = NLS.bind(msg, this.getName(), e.getMessage());

			throw new CopyForAtPreException(msg, e);
		}
		// end try.

		return result;
	}

	/**
	 * <p>
	 * A helper {@link Method} used to find a method of the adapted
	 * {@link Object} of this {@link JavaModelInstanceObject} that conforms to a
	 * given {@link Operation}.
	 * </p>
	 * 
	 * @param operation
	 *            The {@link Operation} for that a {@link Method} shall be
	 *            found.
	 * @return The found {@link Method}.
	 * @throws OperationNotFoundException
	 *             If no matching {@link Method} for the given {@link Operation}
	 *             can be found.
	 */
	private Method findMethodOfAdaptedObject(Operation operation)
			throws OperationNotFoundException {

		Method result;

		Class<?> methodSourceClass;

		result = null;
		methodSourceClass = this.myAdaptedType;

		/*
		 * Try to find an according method in the adapted objects class, or one
		 * of its super classes.
		 */
		while (methodSourceClass != null && result == null) {

			for (Method aMethod : methodSourceClass.getDeclaredMethods()) {

				/* Check if the name matches to the given operation's name. */
				if (aMethod.getName().equals(operation.getName())) {

					/*
					 * Check if the return type matches to the given operation's
					 * type.
					 */
					if (JavaModelInstanceTypeUtility.conformsTypeToType(aMethod
							.getGenericReturnType(), operation.getType())) {

						/*
						 * Check if the method has the same size of arguments as
						 * the given operation.
						 */
						if (aMethod.getParameterTypes().length == operation
								.getSignatureParameter().size()) {

							java.lang.reflect.Type[] javaTypes;
							List<Parameter> pivotModelParamters;

							boolean matches;

							javaTypes = aMethod.getGenericParameterTypes();
							pivotModelParamters = operation
									.getSignatureParameter();

							matches = true;

							/* Compare the types of all arguments. */
							for (int index = 0; index < operation
									.getSignatureParameter().size(); index++) {

								if (!JavaModelInstanceTypeUtility
										.conformsTypeToType(javaTypes[index],
												pivotModelParamters.get(index)
														.getType())) {
									matches = false;
									break;
								}
								// no else.
							}

							if (matches) {
								result = aMethod;
								break;
							}
							// no else.
						}
						// no else.
					}
					// no else (result type differs).
				}
				// no else (name does not match).
			}
			// end for.

			methodSourceClass = methodSourceClass.getSuperclass();
		}
		// end while.

		/* Probably throw an exception. */
		if (result == null) {
			String msg;

			msg = JavaModelInstanceTypeMessages.JavaModelInstance_OperationNotFoundInModelInstanceElement;
			msg = NLS.bind(msg, operation, this.myAdaptedObject.getClass());

			throw new OperationNotFoundException(msg);
		}
		// no else.

		return result;
	}

	/**
	 * <p>
	 * A helper method that initializes a {@link JavaModelInstanceObject}
	 * (Called from all constructors).
	 * </p>
	 * 
	 * @param object
	 *            The {@link Object} for which an
	 *            {@link JavaModelInstanceObject} shall be created.
	 * @param castedToClass
	 *            The Java {@link Class} this {@link JavaModelInstanceObject} is
	 *            casted to. This is required to access the right
	 *            {@link Property}s and {@link Operation}s.
	 * @param type
	 *            The {@link Type} this {@link IModelInstanceElement} belongs
	 *            to.
	 * @param factory
	 *            The {@link JavaModelInstanceFactory} of this
	 *            {@link JavaModelInstanceObject}. Required to adapt the
	 *            {@link Object}s of accesses {@link Property}s or results of
	 *            invoked {@link Operation}s.
	 */
	private void initialize(Object object, Class<?> castedToClass, Type type,
			JavaModelInstanceFactory factory) {

		this.myAdaptedObject = object;
		this.myAdaptedType = castedToClass;
		this.myType = type;
		this.myFactory = factory;
	}
}