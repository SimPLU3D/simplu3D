package tudresden.ocl20.pivot.pivotmodel.base;

import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.ecore.util.BasicInternalEList;

import tudresden.ocl20.pivot.pivotmodel.Namespace;
import tudresden.ocl20.pivot.pivotmodel.Operation;
import tudresden.ocl20.pivot.pivotmodel.Property;
import tudresden.ocl20.pivot.pivotmodel.Type;
import tudresden.ocl20.pivot.pivotmodel.impl.TypeImpl;

/**
 * This class is meant as a base class for DSL- and/or repository-specific
 * implementations of the Pivot Model {@link Type} concept. It defines the
 * minimal set of operations that need to be supported by subclasses. This class
 * currently does not use any caching or other performance optimizations in
 * order to support repositories with alternating content. Clients may implement
 * their own caching behavior or override the implementation in this class
 * altogether. Of course, an entirely new implementation of the
 * <code>Type</code> interface is possible as well.
 * 
 * <p>
 * Special attention should be payed to methods returning collections. In the
 * superclass {@link TypeImpl}, the corresponding EMF-generated code is usually
 * forwarded to a method suffixed with <code>Gen</code>. These are used by
 * special <code>add...</code> methods which allow adding new elements to
 * multivalued properties. This design allows overriding the corresponding
 * getter method without losing the EMF implementation. Moreover, subclasses can
 * now combine the elements added via <code>add...</code> in the superclass and
 * those returned as adapters from the model, thereby creating a transient
 * "view" of the model.
 * </p>
 * 
 * <p>
 * In this class, the principle is exemplified in the
 * {@link #getOwnedProperty()}, {@link #getOwnedOperation()}, and
 * {@link #getSuperType()} methods. They combine the results of
 * {@link #getOwnedPropertyGen()} {@link #getOwnedOperationGen()}, and
 * {@link #getSuperTypeGen()} with {@link #getOwnedPropertyImpl()},
 * {@link #getOwnedOperationImpl()}, and {@link #getSuperTypeImpl()}
 * respectively. The latter have to be implemented in subclasses. This is useful
 * to add additional properties and operations to a <code>Type</code> when
 * parsing OCL definition expressions as well as to add In theory, this approach
 * could be repeated for other multivalued properties (e.g.,
 * {@link #getOwnedTypeParameter()}) as well, if there is a need for it in the
 * future.
 * </p>
 * 
 * @author Matthias Braeuer
 * @version 1.0 12.04.2007
 */
public abstract class AbstractType extends TypeImpl implements Type {

	/**
	 * Subclasses should return the name of the adapted {@link Type}.
	 */
	@Override
	public abstract String getName();

	/**
	 * Subclasses should return an adapter for the {@link Namespace} that owns
	 * this <code>Type</code>.
	 */
	@Override
	public abstract Namespace getNamespace();

	/**
	 * Combines the values of {@link #getOwnedPropertyGen()} and
	 * {@link #getOwnedPropertyImpl()} into a new list.
	 */
	@Override
	public final List<Property> getOwnedProperty() {

		List<Property> ownedProperty =
				new BasicInternalEList<Property>(Property.class);
		ownedProperty.addAll(getOwnedPropertyGen());
		ownedProperty.addAll(getOwnedPropertyImpl());
		return ownedProperty;
	}

	/**
	 * Subclasses should return a list of adapters for the properties owned by the
	 * adapted <code>Type</code>.
	 * 
	 * @return
	 */
	protected abstract List<Property> getOwnedPropertyImpl();

	/**
	 * Combines the values of {@link #getOwnedOperationGen()} and
	 * {@link #getOwnedOperationImpl()} into a new list.
	 */
	@Override
	public final List<Operation> getOwnedOperation() {

		List<Operation> ownedOperation = new BasicEList<Operation>();
		ownedOperation.addAll(getOwnedOperationGen());
		ownedOperation.addAll(getOwnedOperationImpl());
		return ownedOperation;
	}

	/**
	 * Subclasses should return a list of adapters for the operations owned by the
	 * adapted <code>Type</code>.
	 * 
	 * @return
	 */
	protected abstract List<Operation> getOwnedOperationImpl();

	/**
	 * Combines the values of {@link #getSupertypeGen()} and
	 * {@link #getSupertypeImpl()} into a new list.
	 */
	@Override
	public final List<Type> getSuperType() {

		// we use an Ecore List here so that the TypeItemProvider can properly
		// display super types
		// in the Properties View
		List<Type> superTypes = new BasicEList<Type>();

		superTypes.addAll(getSuperTypeGen());
		superTypes.addAll(getSuperTypeImpl());

		return superTypes;
	}

	/**
	 * Subclasses should return a list of adapters for the supertypes of the
	 * adapted <code>Type</code>.
	 */
	protected abstract List<Type> getSuperTypeImpl();
}
