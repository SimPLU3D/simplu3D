package tudresden.ocl20.pivot;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import tudresden.ocl20.logging.LoggingPlugin;

/**
 * The only purpose of this plugin is to initialize the {@link LoggingPlugin
 * logging support}. This enables fine-tuning the log behaviour by providing a
 * custom <code>log4j.properties</code> file. If the Pivot Model implementation
 * is used outside of Eclipse, this plugin is not needed. The classes in this
 * plugin only refer to the log4j {@link Logger} class, never to the custom
 * Eclipse logging support provided by the <code>LoggingPlugin</code>. Thus,
 * logging behaviour can be retained even outside of Eclipse if the log4j
 * libraries are added to the classpath.
 * 
 * @author Matthias Braeuer
 * @version 1.0 20.03.2007
 */
public class PivotModelPlugin extends Plugin {

	/**
	 * The id of this plugin
	 */
	public static final String ID = "tudresden.ocl20.pivot.pivotmodel"; //$NON-NLS-1$

	// The shared instance
	private static PivotModelPlugin plugin;

	/**
	 * Creates a new <code>PivotModelPlugin</code>. This is done by the workbench.
	 */
	public PivotModelPlugin() {

		super();
		plugin = this;
	}

	/**
	 * Overridden to additionally configure the logging support for this plugin.
	 * 
	 * @see org.eclipse.core.runtime.Plugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {

		super.start(context);

		// configure custom logging properties
		LoggingPlugin.configureDefaultLogging(plugin);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {

		super.stop(context);
	}

}
