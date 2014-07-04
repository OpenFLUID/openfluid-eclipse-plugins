/*
  This program 
  Copyright (c) 2007-2010 INRA-Montpellier SupAgro

 == GNU General Public License Usage ==

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program  is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with This program.  If not, see <http://www.gnu.org/licenses/>.
  

 == Other Usage ==

  Other Usage means a use of This program that is inconsistent with
  the GPL license, and requires a written agreement between You and INRA.
  Licensees for Other Usage of This program may use this file in
  accordance with the terms contained in the written agreement between
  You and INRA.
*/


package org.lisah.openfluid.newsimulator.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.operation.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.regex.Matcher;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import java.io.*;

import org.eclipse.ui.*;
import org.eclipse.ui.ide.IDE;
import org.lisah.openfluid.common.*;
import org.lisah.openfluid.newsimulator.Activator;
import org.lisah.openfluid.newsimulator.wizards.SimulatorScheduling.ShedulingType;
import org.osgi.framework.Bundle;



/**
 * This is a sample new wizard. Its role is to create a new file 
 * resource in the provided container. If the container resource
 * (a folder or a project) is selected in the workspace 
 * when the wizard is opened, it will accept it as the target
 * container. The wizard creates one file with the extension
 * "mpe". If a sample multi-page editor (also available
 * as a template) is registered for the same extension, it will
 * be able to open it.
 */

public class NewSimulatorWizard extends Wizard implements INewWizard {
	private SourcesWizardPage sourcesPage;
	private InfosWizardPage infosPage;	
	private DataWizardPage dataPage;	
	private ISelection selection;
	
	private SimulatorProperties simProperties;

	/**
	 * Constructor for SampleNewWizard.
	 */
	public NewSimulatorWizard() {
		super();
		setNeedsProgressMonitor(true);

		simProperties = new SimulatorProperties();
		simProperties.cmakeCommandPath = OpenFLUIDCMakeTools.findCMakeCommand();
	}
	

	
	public void addPages() {
		sourcesPage = new SourcesWizardPage(selection,!simProperties.cmakeCommandPath.isEmpty());
		infosPage = new InfosWizardPage(selection);		
		dataPage = new DataWizardPage(selection);		
		addPage(sourcesPage);
		addPage(infosPage);
		addPage(dataPage);				
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		
		sourcesPage.fillSimulatorProperties(simProperties);
		infosPage.fillSimulatorProperties(simProperties);
		dataPage.fillSimulatorProperties(simProperties);
	
		
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(simProperties,monitor);
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
	

	private void doFinish(SimulatorProperties simProperties,IProgressMonitor monitor)
		throws CoreException, IOException {
		
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();

        IContainer location;
        IProject project = null;
        		
		
		if (simProperties.isNewProject) {

			monitor.beginTask("Creating project...", 2);

			project = OpenFLUIDProjectFactory.createProject(workspace,simProperties.project,null);
			
			location = (IContainer)project;
		}
		else
		{
			IResource resource = root.findMember(new Path(simProperties.container));
			
			if (!resource.exists() || !(resource instanceof IContainer)) {
				throwCoreException("Container \"" + simProperties.container + "\" does not exist.");
			}
			
			location = (IContainer)resource;
		}
		
		monitor.worked(1);
		monitor.setTaskName("Creating source(s) file(s)...");


		int dotPos = simProperties.sourcesFilesRoot.lastIndexOf(".");
		simProperties.sourcesFilesRoot = simProperties.sourcesFilesRoot.substring(0, dotPos);

		final IFile cppFile = location.getFile(new Path(simProperties.sourcesFilesRoot+".cpp"));

		
		InputStream stream = null;
		
		stream =  openSrcFileContentStream(simProperties);


		if (cppFile.exists()) {
			cppFile.setContents(stream, true, true, monitor);
		} else {
			cppFile.create(stream, true, monitor);
		}

		stream.close();
		
		
		monitor.worked(1);
		monitor.setTaskName("Creating build system...");

        if (simProperties.createBuildSystem)
        {
        	IFile cmakelistsFile = null;
        	IFile cmakeconfFile = null;

        	cmakelistsFile = location.getFile(new Path("CMakeLists.txt"));
        	cmakeconfFile = location.getFile(new Path("CMake.in.config"));

        	if (cmakelistsFile != null) {

        		stream = openCMakeListsContentStream(simProperties);

        		if (cmakelistsFile.exists()) {
        			cmakelistsFile.setContents(stream, true, true, monitor);
        		} else {
        			cmakelistsFile.create(stream, true, monitor);
        		}

        		stream.close();

        		stream = openCMakeConfigContentStream(simProperties);

        		if (cmakeconfFile.exists()) {
        			cmakeconfFile.setContents(stream, true, true, monitor);
        		} else {
        			cmakeconfFile.create(stream, true, monitor);
        		}

        		stream.close();

        		if (simProperties.buildSubdir != "") {
        			monitor.worked(1);
        			monitor.setTaskName("Configuring build system...");

        			OpenFLUIDProjectFactory.configureBuildSystem(simProperties,location,project);
        			
        		}
        	}
        }
        
		monitor.worked(1);
		monitor.setTaskName("Opening file for editing...");
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page =
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, cppFile, true);

				} catch (PartInitException e) {
				}
			}
		});
		
		location.refreshLocal(IResource.DEPTH_INFINITE, null);
		monitor.worked(1);
	}
	

	
	private String readTemplateFile(String entry) {

		String content =  "";
		
		Bundle bundle = Activator.getDefault().getBundle();
		InputStream in = null;
		try {
			in = bundle.getEntry(entry).openStream();
		} catch (IOException e) {
			e.printStackTrace();
		}						

		InputStreamReader streamReader = new InputStreamReader(in);
		BufferedReader buffer = new BufferedReader(streamReader);
		String line = "";
		
		try {
			while ( null != ( line = buffer.readLine())){
				content = content + line + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
		
	}
	
	
	private String cppSignatureReplacements(String Str) {
		
		String tplContent = Str;
		
		String simDeclParams = "";		
		String simDeclIData = "";
		String simDeclVars = "";
		String simDeclEvents = "";
		String simDeclFiles = "";
		String simDeclSched = "";
		String simDeclSDyn = "";

		// replace any linefeed in description by space character 
		// to avoid multi-line description
		String TmpDescription = simProperties.simulatorDescription;
		TmpDescription = TmpDescription.replaceAll("\n"," ");		
		
		tplContent = tplContent.replaceAll("\\$\\$SIMULATORID\\$\\$", simProperties.simulatorID);
		tplContent = tplContent.replaceAll("\\$\\$SIMULATORNAME\\$\\$", simProperties.simulatorName);
		tplContent = tplContent.replaceAll("\\$\\$SIMULATORVERSION\\$\\$", simProperties.simulatorVersion);		
		tplContent = tplContent.replaceAll("\\$\\$SIMULATORDESC\\$\\$", TmpDescription);
		tplContent = tplContent.replaceAll("\\$\\$SIMULATORAUTHOR\\$\\$", simProperties.simulatorAuthor);
		tplContent = tplContent.replaceAll("\\$\\$SIMULATORAUTHOREMAIL\\$\\$", simProperties.simulatorAuthorEmail);
		tplContent = tplContent.replaceAll("\\$\\$SIMULATORDOMAIN\\$\\$", simProperties.simulatorDomain);
		tplContent = tplContent.replaceAll("\\$\\$SIMULATORMETHOD\\$\\$", simProperties.simulatorMethod);
		tplContent = tplContent.replaceAll("\\$\\$SIMULATORPROCESS\\$\\$", simProperties.simulatorProcess);		
		
		
		Iterator<SimulatorParameter> itParams = null;
		
		if (simProperties.simulatorParameters.size() > 0) {
			itParams = simProperties.simulatorParameters.iterator();			
			simDeclParams = "\n// Parameters\n";			
			SimulatorParameter param = null;
			
			while (itParams.hasNext()) {
				
				param = itParams.next();				
				simDeclParams = simDeclParams + "  DECLARE_SIMULATOR_PARAM(\"" +
								 param.name + "\",\"" +
								 param.description + "\",\"" +
								 param.SIUnit + "\")\n";
			}
		}

		
		

		Iterator<SimulatorInputData> itIData = null;
		
		if (simProperties.simulatorInputData.size() > 0) {
			itIData = simProperties.simulatorInputData.iterator();			
			simDeclIData = "\n// Input data\n";			
			SimulatorInputData idata = null;
			
			while (itIData.hasNext()) {
				
				idata = itIData.next();
				
				if (idata.isRequired) {
					simDeclIData = simDeclIData + "  DECLARE_REQUIRED_INPUTDATA(\"";					
				} else {
					simDeclIData = simDeclIData + "  DECLARE_USED_INPUTDATA(\"";					
				}
				
				simDeclIData = simDeclIData + idata.name + "\",\"" +
												idata.unitClass + "\",\"" +
												idata.description + "\",\"" +
								 				idata.SIUnit + "\")\n";
			}
		}
		
		Iterator<SimulatorVariable> itVars = null;
		
		if (simProperties.simulatorVariables.size() > 0) {
			itVars = simProperties.simulatorVariables.iterator();			
			simDeclVars = "\n// Variables\n";			
			SimulatorVariable var = null;
			
			while (itVars.hasNext()) {
				
				var = itVars.next();
				
				if (var.isProduced) {
					simDeclVars = simDeclVars + "  DECLARE_PRODUCED_VAR(\"";					
				} else {
				
					if (var.isUpdated) {
						simDeclVars = simDeclVars + "  DECLARE_UPDATED_VAR(\"";					
					} else {	
						if (var.isRequired) {
							simDeclVars = simDeclVars + "  DECLARE_REQUIRED_VAR(\"";
						} else {
							simDeclVars = simDeclVars + "  DECLARE_USED_VAR(\"";
						}
					}
				}
				
				simDeclVars = simDeclVars + var.name + var.type + "\",\"" +
											  var.unitClass + "\",\"" +
											  var.description + "\",\"" +
											  var.SIUnit + "\")\n";
			}
		}

		
		Iterator<SimulatorEvent> itEvents = null;
		
		if (simProperties.simulatorEvents.size() > 0) {
			itEvents = simProperties.simulatorEvents.iterator();			
			simDeclEvents = "\n// Events\n";			
			SimulatorEvent event = null;
			
			while (itEvents.hasNext()) {
				event = itEvents.next();
				simDeclEvents = simDeclEvents + "  DECLARE_USED_EVENTS(\"";									
				simDeclEvents = simDeclEvents + event.unitClass + "\")\n";
			}
		}

		
		Iterator<SimulatorExtraFile> itFiles = null;
		
		if (simProperties.simulatorExtraFiles.size() > 0) {
			itFiles = simProperties.simulatorExtraFiles.iterator();			
			simDeclFiles = "\n// Extra Files\n";			
			SimulatorExtraFile extrafile = null;
			
			while (itFiles.hasNext()) {
				
				extrafile = itFiles.next();
				
				if (extrafile.isRequired) {
					simDeclFiles = simDeclFiles + "  DECLARE_REQUIRED_EXTRAFILE(\"";					
				} else {
					simDeclFiles = simDeclFiles + "  DECLARE_USED_EXTRAFILE(\"";					
				}
				
				simDeclFiles = simDeclFiles + extrafile.name + "\")\n";
			}
		}
		
		
		
		simDeclSched = "\n// Scheduling\n";
		
		if (simProperties.simulatorScheduling.type == ShedulingType.UNDEFINED)
			simDeclSched = simDeclSched + "  DECLARE_SCHEDULING_UNDEFINED;\n";		
	    else if (simProperties.simulatorScheduling.type == ShedulingType.DEFAULT)
	    	simDeclSched = simDeclSched + "  DECLARE_SCHEDULING_DEFAULT;\n";
	    else if (simProperties.simulatorScheduling.type == ShedulingType.FIXED)
	    	simDeclSched = simDeclSched + "  DECLARE_SCHEDULING_FIXED("+simProperties.simulatorScheduling.FixedValue+");\n";
	    else simDeclSched = simDeclSched + "  DECLARE_SCHEDULING_RANGE("+simProperties.simulatorScheduling.MinValue+","+simProperties.simulatorScheduling.MaxValue+");\n";
		
		
		
		Iterator<SimulatorSpatialDyn> itSDyn = null;
		
		if (simProperties.simulatorSpatialDyn.size() > 0) {
			itSDyn = simProperties.simulatorSpatialDyn.iterator();			
			simDeclSDyn = "\n// Spatial domain dynamic\n";
			simDeclSDyn = simDeclSDyn + "  DECLARE_UPDATED_UNITSGRAPH(\"\")\n";			
			SimulatorSpatialDyn sdyn = null;
			
			while (itSDyn.hasNext()) {
				
				sdyn = itSDyn.next();
				
				simDeclSDyn = simDeclSDyn + "  DECLARE_UPDATED_UNITSCLASS(\"";
				
				simDeclSDyn = simDeclSDyn + sdyn.unitClass + "\",\"" +
											  sdyn.description + "\")\n";
			}
		}
		
		
		tplContent = tplContent.replaceAll("\\$\\$SIMULATORDECLARATION_PARAMS\\$\\$", simDeclParams);
		tplContent = tplContent.replaceAll("\\$\\$SIMULATORDECLARATION_IDATA\\$\\$", simDeclIData);		
		tplContent = tplContent.replaceAll("\\$\\$SIMULATORDECLARATION_VARS\\$\\$", simDeclVars);
		tplContent = tplContent.replaceAll("\\$\\$SIMULATORDECLARATION_EVENTS\\$\\$", simDeclEvents);
		tplContent = tplContent.replaceAll("\\$\\$SIMULATORDECLARATION_FILES\\$\\$", simDeclFiles);
		tplContent = tplContent.replaceAll("\\$\\$SIMULATORDECLARATION_SCHED\\$\\$", simDeclSched);
		tplContent = tplContent.replaceAll("\\$\\$SIMULATORDECLARATION_SDYN\\$\\$", simDeclSDyn);
		
		return tplContent;
	}
	
	
	private InputStream openSrcFileContentStream(SimulatorProperties simProperties)
	throws IOException {	

		
		String tplContent = readTemplateFile("/resources/srcfile.cpp.tpl");
		
		tplContent = tplContent.replaceAll("\\$\\$CLASSNAME\\$\\$", simProperties.className);	
		tplContent = tplContent.replaceAll("\\$\\$ROOTFILENAME\\$\\$", simProperties.sourcesFilesRoot);
		
		tplContent = cppSignatureReplacements(tplContent);
		
		return new ByteArrayInputStream(tplContent.getBytes());
	}
		

	private InputStream openCMakeListsContentStream(SimulatorProperties simProperties)
	throws IOException {	



		String tplContent = readTemplateFile("/resources/CMakeLists.txt.tpl");

		
		String newStr = Matcher.quoteReplacement(simProperties.installDir);

		
		// adapt $(VARNAME) to CMake  : $ENV{VARNAME}
		newStr = newStr.replaceAll("\\$\\((\\w+)\\)","\\$ENV\\{$1\\}");
		
		tplContent = tplContent.replaceAll("\\$\\$INSTALLDIR\\$\\$", newStr);
		
		return new ByteArrayInputStream(tplContent.getBytes());
	}
	

	private InputStream openCMakeConfigContentStream(SimulatorProperties simProperties)
	throws IOException {	

        String sim2docStr = "1";
        if (simProperties.simulatorSim2Doc) sim2docStr = "0";
		
		String tplContent = readTemplateFile("/resources/CMake.in.config.tpl");
		
		tplContent = tplContent.replaceAll("\\$\\$ROOTFILENAME\\$\\$", simProperties.sourcesFilesRoot);
		tplContent = tplContent.replaceAll("\\$\\$SIMULATORID\\$\\$", simProperties.simulatorID);
		tplContent = tplContent.replaceAll("\\$\\$SIM2DOCENABLED\\$\\$", sim2docStr);

		return new ByteArrayInputStream(tplContent.getBytes());
	}


	
	private void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "org.lisah.openfluid.newsim", IStatus.OK, message, null);
		throw new CoreException(status);
	}

	/**
	 * We will accept the selection in the workbench to see if
	 * we can initialize from it.
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
				
		
	}
}