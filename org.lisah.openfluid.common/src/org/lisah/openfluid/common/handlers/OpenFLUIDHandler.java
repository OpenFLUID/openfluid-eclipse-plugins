package org.lisah.openfluid.common.handlers;

import org.eclipse.cdt.core.resources.IConsole;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.console.AbstractConsole;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.lisah.openfluid.common.OpenFLUIDCMakeTools;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class OpenFLUIDHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public OpenFLUIDHandler() {
	}

	
	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
				
		ISelectionService selectionService =     
				Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();    

		ISelection selection = selectionService.getSelection();    

		IProject project = null;    
		if(selection instanceof IStructuredSelection) {    
			Object element = ((IStructuredSelection)selection).getFirstElement();    

			if (element instanceof IResource) {    
				project= ((IResource)element).getProject();    
			}
		}
		
		if (project == null) {
			MessageDialog.openError(
					window.getShell(),
					"Run CMake on OpenFLUID ware project",
					"No project selected, cannot run CMake");
		} else {
			
			String projectPath = project.getLocation().toString();
			String projectBuildPath = projectPath+"/_build";
			String cmakeCommandPath = OpenFLUIDCMakeTools.findCMakeCommand();
						
			IFolder buildFolder = project.getFolder(new Path("_build"));
			if (!buildFolder.exists()) {
				try {
					buildFolder.create(true, true, null);
				} catch (CoreException e1) {
					System.out.println(e1);
				}						
			}
			
			if (buildFolder.exists()) {
				if (!cmakeCommandPath.isEmpty()) {
					OpenFLUIDCMakeTools.runCMakeCommand(cmakeCommandPath, projectPath, projectBuildPath);
				} else {
					MessageDialog.openError(
							window.getShell(),
							"Run CMake on OpenFLUID ware project",
							"CMake command not found!");

				}						
			}
			else {
				MessageDialog.openError(
						window.getShell(),
						"Run CMake on OpenFLUID ware project",
						"Cannot create build folder");
			}
		}
		return null;
	}
}
