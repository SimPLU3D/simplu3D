package tudresden.ocl20.pivot.pivotmodel.impl;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.util.EObjectEList;

import tudresden.ocl20.pivot.pivotmodel.GenericElement;
import tudresden.ocl20.pivot.pivotmodel.Type;
import tudresden.ocl20.pivot.pivotmodel.TypeParameter;

/**
 * A <code>Binding</code> contains all information about the binding of the
 * {@link TypeParameter type parameters} of a {@link GenericElement}.
 * 
 * @author Matthias Braeuer
 * @version 1.0 02.08.2007
 */
public class Binding {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Binding.class);

	/**
	 * The element that is going to be bound with the information provided to this
	 * binding.
	 */
	private GenericElement unboundElement;

	/**
	 * The type parameters that were bound during the binding.
	 */
	private List<TypeParameter> typeParameters;

	/**
	 * The types that were bound to the type parameters.
	 */
	private List<? extends Type> types;

	/**
	 * Creates a new <code>Binding</code> instance. None of the parameters must be
	 * <code>null</code>. Also, the given lists must support comparison of
	 * elements via {@link Object#equals(Object) equality}, not identity (in EMF,
	 * the {@link EObjectEList} does not use <code>equals()</code> by default).
	 * 
	 * @param unboundElement
	 *          the element before the binding
	 * @param typeParameters
	 *          the type parameters to be bound
	 * @param types
	 *          the types to bind to the type parameters
	 */
	public Binding(GenericElement unboundElement,
			List<TypeParameter> typeParameters, List<? extends Type> types) {

		if (logger.isDebugEnabled()) {
			logger.debug("Binding(unboundElement=" + unboundElement //$NON-NLS-1$
					+ ", typeParameters=" + typeParameters + ", types=" + types //$NON-NLS-1$ //$NON-NLS-2$
					+ ") - enter"); //$NON-NLS-1$
		}

		// initialize fields
		this.unboundElement = unboundElement;
		this.typeParameters = typeParameters;
		this.types = types;

		if (logger.isDebugEnabled()) {
			logger.debug("Binding() - exit"); //$NON-NLS-1$
		}
	}

	/**
	 * Returns the {@link Type} that has been bound to the given type parameter or
	 * <code>null</code> if the given type parameter was not part of the binding.
	 * 
	 * @param typeParameter
	 *          the type parameter bound during this binding
	 * 
	 * @return the <code>Type</code> bound to the type parameter
	 */
	public Type getType(TypeParameter typeParameter) {

		Type boundType = null;
		int index;

		// find the type parameter
		index = typeParameters.indexOf(typeParameter);

		if (index != -1) {
			boundType = types.get(index);
		}

		return boundType;
	}

	/**
	 * @return
	 */
	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result =
				prime * result
						+ ((typeParameters == null) ? 0 : typeParameters.hashCode());
		result = prime * result + ((types == null) ? 0 : types.hashCode());
		result =
				prime * result
						+ ((unboundElement == null) ? 0 : unboundElement.hashCode());
		return result;
	}

	/**
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Binding))
			return false;
		final Binding other = (Binding) obj;
		if (typeParameters == null) {
			if (other.typeParameters != null)
				return false;
		}
		else if (!typeParameters.equals(other.typeParameters))
			return false;
		if (types == null) {
			if (other.types != null)
				return false;
		}
		else if (!types.equals(other.types))
			return false;
		if (unboundElement == null) {
			if (other.unboundElement != null)
				return false;
		}
		else if (!unboundElement.equals(other.unboundElement))
			return false;
		return true;
	}

	/**
	 * Returns a string representation of this <code>Binding</code>. The string
	 * will contain the fully qualified name of the unbound element as well as the
	 * type parameters and corresponding types bound by this <code>Binding</code>.
	 * 
	 * <p>
	 * Example: Binding both type parameters of {@code Map<K,V>} with the type
	 * named <code>Boolean</code> will result in the key
	 * <code>Map&lt;K,V&gt;:K->Boolean,V->Boolean</code>.
	 * </p>
	 * 
	 * @return a <code>String</code> representing this binding
	 */
	@Override
	public String toString() {

		StringBuilder result;

		// initialize with name of unbound element
		result = new StringBuilder(unboundElement.getQualifiedName());

		// add existing type parameters
		if (!unboundElement.getOwnedTypeParameter().isEmpty()) {
			result.append('<');

			for (Iterator<TypeParameter> it =
					unboundElement.getOwnedTypeParameter().iterator(); it.hasNext();) {
				result.append(it.next().getName());

				if (it.hasNext()) {
					result.append(',');
				}
			}

			result.append('>');
		}

		// add the type parameters and types bound to them
		if (typeParameters.size() > 0) {
			result.append(':');

			for (int i = 0; i < typeParameters.size(); i++) {
				result.append(typeParameters.get(i).getName()).append("->").append( //$NON-NLS-1$
						types.get(i).getQualifiedName());

				if (i < typeParameters.size() - 1) {
					result.append(',');
				}
			}
		}

		return result.toString();
	}

}
