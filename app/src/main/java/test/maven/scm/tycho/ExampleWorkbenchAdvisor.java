package test.maven.scm.tycho;

import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.JavaScriptExecutor;
import org.eclipse.rap.rwt.service.UISessionEvent;
import org.eclipse.rap.rwt.service.UISessionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;


/**
 * This workbench advisor creates the window advisor, and specifies the perspective id for the
 * initial window.
 */
public class ExampleWorkbenchAdvisor extends WorkbenchAdvisor {


	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new ExampleWorkbenchWindowAdvisor(configurer);
	}

	@Override
	public String getInitialWindowPerspectiveId() {
		return "Fail";
	}

	@SuppressWarnings("serial")
	@Override
	public void preStartup() {
		super.preStartup();
		final Display display = getWorkbenchConfigurer().getWorkbench().getDisplay();
		startPolling(display, 1000);

		//attach Listener to platform logging in order to print exceptions to log file for debugging purposes (exception during a job will be logged here)
		if (Platform.isRunning()) {
			Platform.addLogListener(new ILogListener() {
				@Override
				public void logging(IStatus status, String plugin) {
					if (status == null || status.getException() == null) {
						return;
					}
					
				}
			});
		}

		//It was used to test that the polling system was working
		//		RWT.getRequest().getSession().setMaxInactiveInterval(10);

		//this is run before the session is destroy
		final Runnable displayDisposeHook = new Runnable() {
			@Override
			public void run() {
				//				log.debug("Dispose display EXEC");
			}

		};
		display.disposeExec(displayDisposeHook);

		//COOKBOOK: If refresh is press in the browser, the user will be log out.
		// Whenever f5, or we leave the page or the browser is closed the whole UISession is disposed.
		// Trying to track refresh for keep the user log in, was possible but not very reliable, so that option was removed from the code.
		RWT.getUISession().addUISessionListener(new UISessionListener() {
			//This is called after the display is disposed but before the session is Destroy.
			@Override
			public void beforeDestroy(UISessionEvent event) {
				//Perform session cleanup
				
				stopPolling();
				//UserManager.logoutSession(RWT.getUISession().getHttpSession().getId());
				UserManager.logoutSession(RWT.getUISession().getId());
			}
		});

	}

	@Override
	public void postStartup() {
		//COOKBOOK: Sticky Views: Open LogView once. Log view is defined as sticky in the plugin.xml and therefore shown on all perspectives
		try {
			getWorkbenchConfigurer().getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(SSPLogView.ID);
		} catch (PartInitException e) {
			//log.error("Log view could not be opened.", e);
		}

	}

	@Override
	public void eventLoopException(Throwable exception) {
		super.eventLoopException(exception);
		if (exception == null) {
			return;
		}
		//log.error("Problem occurred: " + exception.getMessage(), exception);
	}

	//COOKBOOK: RAP Client polling for UI Updates.
	protected void startPolling(Display display, int mSeconds) {
		JavaScriptExecutor executor = RWT.getClient().getService(JavaScriptExecutor.class);
		if (executor != null) {
			//this will start polling from the client which will force UI Update.
			// when changing to RAP version 2.2+ then rwt.remote.Connection.getInstance().send() should be used instead
			// of rwt.remote.Server.getInstance().send().
			executor.execute("var aktiv = window.setInterval( function() {\n" + " rwt.remote.Connection.getInstance().send();\n" + "}, "
					+ mSeconds + " );");

			//log.debug("started polling script on client.");
		} else {
			//log.error("Could not run start polling script on client.");
		}
	}

	protected void stopPolling() {
		JavaScriptExecutor executor = RWT.getClient().getService(JavaScriptExecutor.class);
		if (executor != null) {
			executor.execute("window.clearInterval(aktiv);");
			//log.debug("stopped polling script on client.");
		} else {
			//log.error("Could not run stop polling script on client.");
		}
	}

}
