package test.maven.scm.tycho;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.ServiceHandler;
import org.eclipse.rap.rwt.service.ServiceManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;


/**
 * This class controls all aspects of the application's execution and is contributed through the
 * plugin.xml.
 */
public class ExampleApp implements IApplication {


	protected static final AtomicBoolean exportXmlHandlerRegistered = new AtomicBoolean(false);

	@Override
	public Object start(IApplicationContext context) throws Exception {
		Display display = null;
		try {
			WorkbenchAdvisor advisor = new ExampleWorkbenchAdvisor();

			return PlatformUI.createAndRunWorkbench(display, advisor);
		} catch (Exception e) {
			
			return org.eclipse.ui.PlatformUI.RETURN_EMERGENCY_CLOSE;
		} finally {
			if (display != null) {
				display.dispose();
			}
		}
	}

	@Override
	public void stop() {
		// Do nothing
	}

}
