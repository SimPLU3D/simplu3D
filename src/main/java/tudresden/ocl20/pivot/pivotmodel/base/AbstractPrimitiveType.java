package tudresden.ocl20.pivot.pivotmodel.base;

import tudresden.ocl20.pivot.pivotmodel.Namespace;
import tudresden.ocl20.pivot.pivotmodel.PrimitiveType;
import tudresden.ocl20.pivot.pivotmodel.PrimitiveTypeKind;
import tudresden.ocl20.pivot.pivotmodel.impl.PrimitiveTypeImpl;

/**
 * This class is meant as a base class for DSL- and/or repository-specific
 * implementations of the Pivot Model {@link PrimitiveType} concept. It defines
 * the minimal set of operations that are required for adapting primitive types
 * in the foreign DSL or repository. For example, subclasses need to specify
 * what <code>kind</code> of primitive type they are adapting. This is important
 * for external clients such as an OCL engine to determine what capabilities the
 * primitive type has. Implementors may choose to override additional methods or
 * write an entirely new implementation of the <code>PrimitiveType</code>
 * interface.
 * 
 * @author Matthias Braeuer
 * @version 1.0 30.03.2007
 */
public abstract class AbstractPrimitiveType extends PrimitiveTypeImpl implements
		PrimitiveType {

	/**
	 * Subclasses should return the name of the adapted {@link PrimitiveType}.
	 */
	@Override
	public abstract String getName();

	/**
	 * Subclasses should return an adapter for the {@link Namespace} that owns
	 * this <code>PrimitiveType</code>.
	 */
	@Override
	public abstract Namespace getNamespace();

	/**
	 * Subclasses should specify which of the predefined Pivot Model types best
	 * fits the adapted type. Alternatively, {@link PrimitiveTypeKind#UNKNOWN} may
	 * be used.
	 */
	@Override
	public abstract PrimitiveTypeKind getKind();

}
