package tudresden.ocl20.pivot.pivotmodel.base;

import tudresden.ocl20.pivot.pivotmodel.Property;
import tudresden.ocl20.pivot.pivotmodel.Type;
import tudresden.ocl20.pivot.pivotmodel.impl.PropertyImpl;

/**
 * This class is meant as a base class for DSL- and/or repository-specific
 * implementations of the Pivot Model {@link Property} concept. It defines the
 * minimal set of operations that are required for adapting properties in the
 * foreign DSL or repository. Implementors may choose to override additional
 * methods or write an entirely new implementation of the <code>Property</code>
 * interface.
 * 
 * <p>
 * In particular, the following methods will probably have to be additionally
 * overridden for most DSLs:
 * <ul>
 * <li>{@link #isMultiple()}
 * <li>{@link #isOrdered()}
 * <li>{@link #isUnique()}
 * <li>{@link #isStatic()}
 * </ul>
 * </p>
 * 
 * @author Matthias Braeuer
 * @version 1.0 12.04.2007
 */
public abstract class AbstractProperty extends PropertyImpl implements Property {

	/**
	 * Subclasses should return the name of the adapted <code>Property</code>.
	 */
	@Override
	public abstract String getName();

	/**
	 * Subclasses should return an adapter for the {@link Type} owning the adapted
	 * <code>Property</code>.
	 */
	@Override
	public abstract Type getOwningType();

	/**
	 * Subclasses should return an adapter for the {@link Type type} of the
	 * adapted <code>Property</code>.
	 */
	@Override
	public abstract Type getType();
}
