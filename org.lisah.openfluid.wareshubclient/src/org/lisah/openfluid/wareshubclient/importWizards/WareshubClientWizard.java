
package org.lisah.openfluid.wareshubclient.importWizards;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.lisah.openfluid.common.OpenFLUIDProjectFactory;


public class WareshubClientWizard extends Wizard implements IImportWizard {
	
	private IStructuredSelection selection;
	
	ConfigWizardPage configPage;
	SelectWizardPage selectPage;

	OpenFLUIDPreferences ofPrefs;
	RemoteWaresHub remoteWH;
	
	
	// =====================================================================
	// =====================================================================	

	
	public WareshubClientWizard() {
		super();
		setWindowTitle("Import OpenFLUID wares from WaresHub"); 
		setNeedsProgressMonitor(true);
	}


	// =====================================================================
	// =====================================================================	

	
    public void addPages() {
    	
    	ofPrefs = new OpenFLUIDPreferences();
    	remoteWH = new RemoteWaresHub();
    	
        configPage = new ConfigWizardPage(this.selection,ofPrefs);
        selectPage = new SelectWizardPage(this.selection,ofPrefs,remoteWH);

        addPage(configPage);
        addPage(selectPage);  
        
    }


	// =====================================================================
	// =====================================================================	
    
    
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		
	}


	// =====================================================================
	// =====================================================================	


	public boolean performFinish() {
	
		final String waresType = selectPage.getActiveWareType();
		final ArrayList<String> waresList = selectPage.getCheckedWaresForType();
				
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {				 
					monitor.beginTask("Importing "+waresType+"...",waresList.size());
					
					for (String w: waresList) {
						doFinish(waresType,w,monitor);
					}
					
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}

		return true;
	}
	

	// =====================================================================
	// =====================================================================	
	
	
	private void doFinish(String wareType,String wareID,IProgressMonitor monitor)
			throws CoreException, IOException {
			
			IWorkspace workspace = ResourcesPlugin.getWorkspace();

	        @SuppressWarnings("unused")
			IProject project = null;
	        
	        String wareLocalPath = ofPrefs.getWaresTypeDevPath(wareType)+"/"+wareID;
	        
		    String gitURL = remoteWH.getAvailableInfos().get("wares").asObject()
		    		.get(wareType).asObject().get(wareID).asObject().get("git-url").asString();
		
		    
			monitor.setTaskName("Cloning "+wareID);
  			  
  			CredentialsProvider cp = 
  					new UsernamePasswordCredentialsProvider(ofPrefs.getUserName(), ofPrefs.getUserPassword());  
  			  
  			try {
				Git.cloneRepository()
					.setCredentialsProvider(cp)
					.setURI(gitURL)
					.setDirectory(new File(wareLocalPath))
					.call();
				
				monitor.setTaskName("Creating project for "+wareID);
				
				project = OpenFLUIDProjectFactory.createProject(workspace,wareID,wareLocalPath);
				
			} catch (InvalidRemoteException e) {
				e.printStackTrace();
			} catch (TransportException e) {
				e.printStackTrace();
			} catch (GitAPIException e) {
				e.printStackTrace();
			}  			  
			
			monitor.worked(1);
	}

}
