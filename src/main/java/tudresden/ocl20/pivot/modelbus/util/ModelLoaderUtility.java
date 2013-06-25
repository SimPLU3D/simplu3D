/**
 * 
 */
package tudresden.ocl20.pivot.modelbus.util;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import tudresden.ocl20.pivot.model.metamodel.IMetamodel;
import tudresden.ocl20.pivot.modelbus.ModelBusPlugin;
import tudresden.ocl20.pivot.modelinstance.IModelInstanceType;

/**
 * @author Lars Schuetze
 * 
 */
public final class ModelLoaderUtility {

	/**
	 * Prevent this class from being instantiated.
	 */
	private ModelLoaderUtility() {

		throw new AssertionError("This class must not be instantiated.");
	}

	private static final String m_src = File.separator + "src" + File.separator;
	private static final String m_bin = File.separator + "bin" + File.separator;

	/**
	 * <p>
	 * Helper method which substitutes the "/src/" with "/bin/" and set the file
	 * ending to ".class"
	 * </p>
	 * 
	 * @param A
	 *          {@link String} which represents the path to the .java file
	 * @return A {@link String} which represents the possible path to the
	 *         corresponding .class file of a .java file
	 */
	public static String getCorrespondingClassFileName(String pathToJavaFile) {

		if (pathToJavaFile == null)
			return null;

		String pathToClassFile = pathToJavaFile.replace(m_src, m_bin);

		pathToClassFile =
				pathToClassFile.substring(0, pathToClassFile.length() - 4) + "class";

		return pathToClassFile;
	}

	/**
	 * <p>
	 * Checks if the file extension is known so we can derive the metamodel to
	 * load.
	 * </p>
	 * 
	 * @param fileExtension
	 * @return the metamodel to load or null
	 */
	public static IMetamodel getMetamodelByExtension(String fileExtension) {

		IMetamodel[] metaModels;

		boolean isApplicable = false;
		metaModels = ModelBusPlugin.getMetamodelRegistry().getMetamodels();

		if (metaModels.length > 0) {

			IMetamodel mmType = null;

			if (fileExtension != null) {
				/* Check which meta model can be applied */
				if (fileExtension.equalsIgnoreCase("class")
						|| fileExtension.equalsIgnoreCase("javamodel")
						|| fileExtension.equalsIgnoreCase("java")) {
					mmType =
							ModelBusPlugin.getMetamodelRegistry().getMetamodel(
									"tudresden.ocl20.pivot.metamodels.java");

					isApplicable = true;
				}
				else if (fileExtension.equalsIgnoreCase("uml")) {
					mmType =
							ModelBusPlugin.getMetamodelRegistry().getMetamodel(
									"tudresden.ocl20.pivot.metamodels.uml2");

					isApplicable = true;
				}
				else if (fileExtension.equalsIgnoreCase("ecore")) {
					mmType =
							ModelBusPlugin.getMetamodelRegistry().getMetamodel(
									"tudresden.ocl20.pivot.metamodels.ecore");

					isApplicable = true;
				}
				else if (fileExtension.equalsIgnoreCase("xsd")) {
					mmType =
							ModelBusPlugin.getMetamodelRegistry().getMetamodel(
									"tudresden.ocl20.pivot.metamodels.xsd");

					isApplicable = true;
				}
			}

			/* Search for the meta model and select it */
			if (isApplicable && (mmType != null)) {
				List<IMetamodel> mmList = Arrays.asList(metaModels);
				return mmList.get(mmList.indexOf(mmType));
			}
			// no else.
		}
		// no else.
		return null;
	}

	public static IModelInstanceType getModelinstanceTypeByExtension(
			String fileExtension) {

		IModelInstanceType[] miTypes;
		boolean isApplicable = false;

		miTypes =
				ModelBusPlugin.getModelInstanceTypeRegistry().getModelInstanceTypes();

		if (miTypes.length > 0) {

			IModelInstanceType miType = null;

			if (fileExtension != null) {
				/* Check whether the model instance file is a .class file */
				if (fileExtension.equalsIgnoreCase("class")
						|| fileExtension.equalsIgnoreCase("java")) {
					miType =
							ModelBusPlugin.getModelInstanceTypeRegistry()
									.getModelInstanceType(
											"tudresden.ocl20.pivot.modelinstancetype.java");

					isApplicable = true;

				}
				else if (fileExtension.equalsIgnoreCase("pml")) {
					miType =
							ModelBusPlugin.getModelInstanceTypeRegistry()
									.getModelInstanceType(
											"tudresden.ocl20.pivot.modelinstancetype.ecore");

					isApplicable = true;

				}
				else if (fileExtension.equalsIgnoreCase("xml")) {
					miType =
							ModelBusPlugin.getModelInstanceTypeRegistry()
									.getModelInstanceType(
											"tudresden.ocl20.pivot.modelinstancetype.xml");

					isApplicable = true;
				}
			}
			// no else

			if (isApplicable && (miType != null)) {
				/* Search for the model instance type and select it */
				List<IModelInstanceType> miList = Arrays.asList(miTypes);
				return miList.get(miList.indexOf(miType));
			}
		}
		return null;
	}
}
