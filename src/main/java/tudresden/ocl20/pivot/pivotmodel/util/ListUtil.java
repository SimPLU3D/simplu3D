package tudresden.ocl20.pivot.pivotmodel.util;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.emf.ecore.util.EObjectEList;

/**
 * A helper class to properly deal with lists that use identity instead of
 * equality in their operations. This is particularly meant for those cases
 * where an {@link EObjectEList} is used.
 * 
 * @author Matthias Braeuer
 * @version 1.0 06.08.2007
 */
public final class ListUtil {

	/**
	 * This class is not meant to be instantiated.
	 */
	private ListUtil() {

		// no implementation by design
	}

	/**
	 * Removes the given <code>object</code> from the given <code>list</code>,
	 * comparing each element in the list with <code>equals</code>.
	 * 
	 * @param list
	 *          a list
	 * @param object
	 *          an object to be removed from the list
	 */
	public static <E> void remove(List<E> list, E object) {

		for (Iterator<E> it = list.iterator(); it.hasNext();) {
			if (it.next().equals(object)) {
				it.remove();
			}
		}
	}

	/**
	 * Removes all <code>objects</code> from <code>list</code>.
	 * 
	 * @param <E>
	 *          the element type of the list
	 * @param list
	 *          the list
	 * @param objects
	 *          a list of objects to be removed from list
	 */
	public static <E> void removeAll(List<E> list, List<? extends E> objects) {

		for (E object : objects) {
			remove(list, object);
		}

	}

	/**
	 * Returns the index of <code>object</code> in <code>list</code> or
	 * <code>-1</code> if the list does not contain the object.
	 * 
	 * @param <E>
	 *          the element type of the list
	 * @param list
	 *          the list
	 * @param object
	 *          the object to search for
	 * 
	 * @return the index of the object in the list or -1
	 */
	public static <E> int indexOf(List<E> list, E object) {

		for (ListIterator<E> it = list.listIterator(); it.hasNext();) {
			if (it.next().equals(object)) {
				return it.previousIndex();
			}
		}

		return -1;
	}
}
