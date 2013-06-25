package tudresden.ocl20.pivot.pivotmodel.base;

import java.util.List;

import tudresden.ocl20.pivot.pivotmodel.Enumeration;
import tudresden.ocl20.pivot.pivotmodel.EnumerationLiteral;
import tudresden.ocl20.pivot.pivotmodel.Namespace;
import tudresden.ocl20.pivot.pivotmodel.impl.EnumerationImpl;

/**
 * This class is meant as a base class for DSL- and/or repository-specific
 * implementations of the Pivot Model {@link Enumeration} concept. It defines
 * the minimal set of operations that are required for adapting enumeration
 * types in the foreign DSL or repository. Implementors may choose to override
 * additional methods or write an entirely new implementation of the
 * <code>Enumeration</code> interface.
 * 
 * @author Matthias Braeuer
 * @version 1.0 12.04.2007
 */
public abstract class AbstractEnumeration extends EnumerationImpl implements
		Enumeration {

	/**
	 * Subclasses should return the name of the adapted {@link Enumeration}.
	 */
	@Override
	public abstract String getName();

	/**
	 * Subclasses should return an adapter for the {@link Namespace} that owns
	 * this <code>Enumeration</code>.
	 */
	@Override
	public abstract Namespace getNamespace();

	/**
	 * Subclasses should return a list of adapters for the owned
	 * {@link EnumerationLiteral literals}.
	 */
	@Override
	public abstract List<EnumerationLiteral> getOwnedLiteral();

}
