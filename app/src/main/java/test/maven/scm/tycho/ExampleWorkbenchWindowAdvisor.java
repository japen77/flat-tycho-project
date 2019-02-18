package test.maven.scm.tycho;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * Configures the initial size and appearance of a workbench window.
 */
public class ExampleWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ExampleWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new SSPActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);
		configurer.setShowMenuBar(false);
		configurer.setShowFastViewBars(false);
		configurer.setShowPerspectiveBar(false);
		configurer.setShowProgressIndicator(false);
		//app in entire browser window
		configurer.setShellStyle(SWT.NO_TRIM);
		configurer.setTitle("SSP Gateway");
	}
	
	
	@Override
	public void postWindowCreate() {
		//This method is called on every perspective switch
		//app in entire browser window
		Shell shell = getWindowConfigurer().getWindow().getShell();
		shell.setMaximized(true);
	}
	
	
	//COOKBOOK: This is done so the FOLDER is not close when the last View open there is closed.
	@Override
	public boolean isDurableFolder(String perspectiveId, String folderId) {
		if( ConfigurationPerspective.ID.equals(perspectiveId) && ConfigurationPerspective.FOLDER_ID.equals(folderId) ) {
			return true; 
		}
		return super.isDurableFolder(perspectiveId, folderId);
	}
}
