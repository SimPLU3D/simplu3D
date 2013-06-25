package tudresden.ocl20.pivot.pivotmodel.base;

import tudresden.ocl20.pivot.pivotmodel.Operation;
import tudresden.ocl20.pivot.pivotmodel.Parameter;
import tudresden.ocl20.pivot.pivotmodel.ParameterDirectionKind;
import tudresden.ocl20.pivot.pivotmodel.Type;
import tudresden.ocl20.pivot.pivotmodel.impl.ParameterImpl;

/**
 * This class is meant as a base class for DSL- and/or repository-specific
 * implementations of the Pivot Model {@link Parameter} concept. It defines the
 * minimal set of operations that are required for adapting parameters in the
 * foreign DSL or repository. Implementors may choose to override additional
 * methods or write an entirely new implementation of the <code>Parameter</code>
 * interface.
 * 
 * <p>
 * In particular, the following methods will probably have to be additionally
 * overridden for most DSLs:
 * <ul>
 * <li>{@link #isMultiple()}
 * <li>{@link #isOrdered()}
 * <li>{@link #isUnique()}
 * </ul>
 * </p>
 * 
 * <p>
 * If the DSL supports the notion of a {@link ParameterDirectionKind}, the
 * {@link #getKind()} method should be overridden as well.
 * </p>
 * 
 * @author Matthias Braeuer
 * @version 1.0 12.04.2007
 */
public abstract class AbstractParameter extends ParameterImpl implements
		Parameter {

	/**
	 * Subclasses should return the name of the adapted <code>Parameter</code>.
	 */
	@Override
	public abstract String getName();

	/**
	 * Subclasses should return an adapter for the owning {@link Operation}.
	 */
	@Override
	public abstract Operation getOperation();

	/**
	 * Subclasses should return an adapter for the {@link Type} of the adapted
	 * <code>Parameter</code>.
	 */
	@Override
	public abstract Type getType();

}
