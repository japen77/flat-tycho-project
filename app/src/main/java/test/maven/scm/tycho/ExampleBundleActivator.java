package test.maven.scm.tycho;

import java.util.concurrent.TimeUnit;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class ExampleBundleActivator extends AbstractUIPlugin {

	public static final String BUNDLE_ID = "com.lsespace.ssp";


	@Override
	public void start(BundleContext context) throws Exception {
		Thread shutdownHook = new Thread() {
			@Override
			public void run() {
				log.error("*** END ***");
			}
		};
		Runtime.getRuntime().addShutdownHook(shutdownHook);

	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

}
