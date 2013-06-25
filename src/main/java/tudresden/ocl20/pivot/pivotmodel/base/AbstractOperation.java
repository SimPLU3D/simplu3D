package tudresden.ocl20.pivot.pivotmodel.base;

import java.util.List;

import tudresden.ocl20.pivot.pivotmodel.Operation;
import tudresden.ocl20.pivot.pivotmodel.Parameter;
import tudresden.ocl20.pivot.pivotmodel.ParameterDirectionKind;
import tudresden.ocl20.pivot.pivotmodel.Type;
import tudresden.ocl20.pivot.pivotmodel.impl.OperationImpl;

/**
 * This class is meant as a base class for DSL- and/or repository-specific
 * implementations of the Pivot Model {@link Operation} concept. It defines the
 * minimal set of operations that are required for adapting operations in the
 * foreign DSL or repository. Implementors may choose to override additional
 * methods or write an entirely new implementation of the <code>Operation</code>
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
 * <p>
 * If the DSL does not support the notion of a {@link ParameterDirectionKind},
 * it will also have to overwrite the {@link #getType()} method. By default, the
 * implementation in the super class returns the type of the return parameter if
 * existing.
 * </p>
 * 
 * @author Matthias Braeuer
 * @version 1.0 12.04.2007
 */
public abstract class AbstractOperation extends OperationImpl implements
		Operation {

	/**
	 * Subclasses should return the name of the adapted <code>Operation</code>.
	 */
	@Override
	public abstract String getName();

	/**
	 * Subclasses should return adapters for the owned {@link Parameter}s of the
	 * adapted <code>Operation</code>.
	 */
	@Override
	public abstract List<Parameter> getOwnedParameter();

	/**
	 * Subclasses should return an adapter for the {@link Type} owning the adapted
	 * <code>Operation</code>.
	 */
	@Override
	public abstract Type getOwningType();

}
