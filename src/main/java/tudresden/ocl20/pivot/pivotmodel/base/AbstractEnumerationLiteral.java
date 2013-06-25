package tudresden.ocl20.pivot.pivotmodel.base;

import tudresden.ocl20.pivot.pivotmodel.Enumeration;
import tudresden.ocl20.pivot.pivotmodel.EnumerationLiteral;
import tudresden.ocl20.pivot.pivotmodel.impl.EnumerationLiteralImpl;

/**
 * This class is meant as a base class for DSL- and/or repository-specific
 * implementations of the Pivot Model {@link EnumerationLiteral} concept. It
 * defines the minimal set of operations that are required for adapting
 * enumeration literals in the foreign DSL or repository. Implementors may
 * choose to override additional methods or write an entirely new implementation
 * of the <code>EnumerationLiteral</code> interface.
 * 
 * @author Matthias Braeuer
 * @version 1.0 12.04.2007
 */
public abstract class AbstractEnumerationLiteral extends EnumerationLiteralImpl
		implements EnumerationLiteral {

	/**
	 * Subclasses should return the name of the adapted {@link EnumerationLiteral}
	 * .
	 */
	@Override
	public abstract String getName();

	/**
	 * Subclasses should return an adapter for the {@link Enumeration} that owns
	 * this <code>EnumerationLiteral</code>.
	 */
	@Override
	public abstract Enumeration getEnumeration();

}
