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


package org.lisah.openfluid.newfunc.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.ICProjectDescriptionManager;
import org.eclipse.cdt.core.settings.model.extension.CConfigurationData;
import org.eclipse.cdt.managedbuilder.core.IBuilder;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.cdt.managedbuilder.internal.core.Configuration;
import org.eclipse.cdt.managedbuilder.internal.core.ManagedProject;
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
import org.lisah.openfluid.common.OpenFLUIDCMakeTools;
import org.lisah.openfluid.common.OpenFLUIDProjectFactory;
import org.lisah.openfluid.newfunc.Activator;
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

public class NewFunctionWizard extends Wizard implements INewWizard {
	private SourcesWizardPage sourcesPage;
	private InfosWizardPage infosPage;	
	private DataWizardPage dataPage;	
	private ISelection selection;
	
	private FunctionProperties funcProperties;

	/**
	 * Constructor for SampleNewWizard.
	 */
	public NewFunctionWizard() {
		super();
		setNeedsProgressMonitor(true);

		funcProperties = new FunctionProperties();
		funcProperties.cmakeCommandPath = OpenFLUIDCMakeTools.findCMakeCommand();
	}
	

	
	public void addPages() {
		sourcesPage = new SourcesWizardPage(selection,!funcProperties.cmakeCommandPath.isEmpty());
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
		
		sourcesPage.fillFunctionProperties(funcProperties);
		infosPage.fillFunctionProperties(funcProperties);
		dataPage.fillFunctionProperties(funcProperties);
	
		
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(funcProperties,monitor);
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
	

	private void doFinish(FunctionProperties funcProperties,IProgressMonitor monitor)
		throws CoreException, IOException {
		
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();

        IContainer location;
        IProject project = null;
        		
		
		if (funcProperties.isNewProject) {

			monitor.beginTask("Creating project...", 2);

			
			project = OpenFLUIDProjectFactory.createProject(workspace,funcProperties.project);
			
			location = (IContainer)project;
		}
		else
		{
			IResource resource = root.findMember(new Path(funcProperties.container));
			
			if (!resource.exists() || !(resource instanceof IContainer)) {
				throwCoreException("Container \"" + funcProperties.container + "\" does not exist.");
			}
			
			location = (IContainer)resource;
		}
		
		monitor.worked(1);
		monitor.setTaskName("Creating source(s) file(s)...");


		int dotPos = funcProperties.sourcesFilesRoot.lastIndexOf(".");
		funcProperties.sourcesFilesRoot = funcProperties.sourcesFilesRoot.substring(0, dotPos);

		IFile hFile = null;
		final IFile cppFile = location.getFile(new Path(funcProperties.sourcesFilesRoot+".cpp"));

		if (!funcProperties.singleSourceFile) {
			hFile = location.getFile(new Path(funcProperties.sourcesFilesRoot+".h"));
		}

		

		InputStream stream = null;
		
		if (funcProperties.singleSourceFile) stream =  openSingleCPPContentStream(funcProperties);
		else stream = openCPPContentStream(funcProperties);


		if (cppFile.exists()) {
			cppFile.setContents(stream, true, true, monitor);
		} else {
			cppFile.create(stream, true, monitor);
		}

		stream.close();


		if (hFile != null) {
			
			stream = openHContentStream(funcProperties);
			
			if (hFile.exists()) {
				hFile.setContents(stream, true, true, monitor);
			} else {
				hFile.create(stream, true, monitor);
			}
			
			stream.close();
		}
		
		
		monitor.worked(1);
		monitor.setTaskName("Creating build system...");

        if (funcProperties.createBuildSystem)
        {
        	IFile cmakelistsFile = null;
        	IFile cmakeconfFile = null;

        	cmakelistsFile = location.getFile(new Path("CMakeLists.txt"));
        	cmakeconfFile = location.getFile(new Path("CMake.in.config"));

        	if (cmakelistsFile != null) {

        		stream = openCMakeListsContentStream(funcProperties);

        		if (cmakelistsFile.exists()) {
        			cmakelistsFile.setContents(stream, true, true, monitor);
        		} else {
        			cmakelistsFile.create(stream, true, monitor);
        		}

        		stream.close();

        		stream = openCMakeConfigContentStream(funcProperties);

        		if (cmakeconfFile.exists()) {
        			cmakeconfFile.setContents(stream, true, true, monitor);
        		} else {
        			cmakeconfFile.create(stream, true, monitor);
        		}

        		stream.close();

        		if (funcProperties.buildSubdir != "") {
        			monitor.worked(1);
        			monitor.setTaskName("Configuring build system...");

        			OpenFLUIDProjectFactory.configureBuildSystem(funcProperties,location,project);
        			
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
//					IDE.openEditor(page, hFile, true);

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
		
		String funcDeclParams = "";
		String funcDeclSDyn = "";
		String funcDeclIData = "";
		String funcDeclVars = "";
		String funcDeclEvents = "";
		String funcDeclFiles = "";

		// replace any linefeed in description by space character 
		// to avoid multi-line description
		String TmpDescription = funcProperties.functionDescription;
		TmpDescription = TmpDescription.replaceAll("\n"," ");		
		
		tplContent = tplContent.replaceAll("\\$\\$FUNCTIONID\\$\\$", funcProperties.functionID);
		tplContent = tplContent.replaceAll("\\$\\$FUNCTIONNAME\\$\\$", funcProperties.functionName);
		tplContent = tplContent.replaceAll("\\$\\$FUNCTIONVERSION\\$\\$", funcProperties.functionVersion);		
		tplContent = tplContent.replaceAll("\\$\\$FUNCTIONDESC\\$\\$", TmpDescription);
		tplContent = tplContent.replaceAll("\\$\\$FUNCTIONAUTHOR\\$\\$", funcProperties.functionAuthor);
		tplContent = tplContent.replaceAll("\\$\\$FUNCTIONAUTHOREMAIL\\$\\$", funcProperties.functionAuthorEmail);
		tplContent = tplContent.replaceAll("\\$\\$FUNCTIONDOMAIN\\$\\$", funcProperties.functionDomain);
		tplContent = tplContent.replaceAll("\\$\\$FUNCTIONMETHOD\\$\\$", funcProperties.functionMethod);
		tplContent = tplContent.replaceAll("\\$\\$FUNCTIONPROCESS\\$\\$", funcProperties.functionProcess);		
		
		
		Iterator<FunctionParameter> itParams = null;
		
		if (funcProperties.functionParameters.size() > 0) {
			itParams = funcProperties.functionParameters.iterator();			
			funcDeclParams = "\n// Parameters\n";			
			FunctionParameter param = null;
			
			while (itParams.hasNext()) {
				
				param = itParams.next();				
				funcDeclParams = funcDeclParams + "  DECLARE_FUNCTION_PARAM(\"" +
								 param.name + "\",\"" +
								 param.description + "\",\"" +
								 param.SIUnit + "\")\n";
			}
		}

		
		Iterator<FunctionSpatialDyn> itSDyn = null;
		
		if (funcProperties.functionSpatialDyn.size() > 0) {
			itSDyn = funcProperties.functionSpatialDyn.iterator();			
			funcDeclSDyn = "\n// Spatial domain dynamic\n";
			funcDeclSDyn = funcDeclSDyn + "  DECLARE_UPDATED_UNITSGRAPH(\"\")\n";			
			FunctionSpatialDyn sdyn = null;
			
			while (itSDyn.hasNext()) {
				
				sdyn = itSDyn.next();
				
				funcDeclSDyn = funcDeclSDyn + "  DECLARE_UPDATED_UNITSCLASS(\"";
				
				funcDeclSDyn = funcDeclSDyn + sdyn.unitClass + "\",\"" +
											  sdyn.description + "\")\n";
			}
		}
		
		

		Iterator<FunctionInputData> itIData = null;
		
		if (funcProperties.functionInputData.size() > 0) {
			itIData = funcProperties.functionInputData.iterator();			
			funcDeclIData = "\n// Input data\n";			
			FunctionInputData idata = null;
			
			while (itIData.hasNext()) {
				
				idata = itIData.next();
				
				if (idata.isRequired) {
					funcDeclIData = funcDeclIData + "  DECLARE_REQUIRED_INPUTDATA(\"";					
				} else {
					funcDeclIData = funcDeclIData + "  DECLARE_USED_INPUTDATA(\"";					
				}
				
				funcDeclIData = funcDeclIData + idata.name + "\",\"" +
												idata.unitClass + "\",\"" +
												idata.description + "\",\"" +
								 				idata.SIUnit + "\")\n";
			}
		}
		
		Iterator<FunctionVariable> itVars = null;
		
		if (funcProperties.functionVariables.size() > 0) {
			itVars = funcProperties.functionVariables.iterator();			
			funcDeclVars = "\n// Variables\n";			
			FunctionVariable var = null;
			
			while (itVars.hasNext()) {
				
				var = itVars.next();
				
				if (var.isProduced) {
					funcDeclVars = funcDeclVars + "  DECLARE_PRODUCED_VAR(\"";					
				} else {
				
					if (var.isUpdated) {
						funcDeclVars = funcDeclVars + "  DECLARE_UPDATED_VAR(\"";					
					} else {
									
						if (var.isRequired) {
							if (var.isPrevious) {
								funcDeclVars = funcDeclVars + "  DECLARE_REQUIRED_PREVVAR(\"";	
							} else {
								funcDeclVars = funcDeclVars + "  DECLARE_REQUIRED_VAR(\"";
							}
										
						} else {
							if (var.isPrevious) {
								funcDeclVars = funcDeclVars + "  DECLARE_USED_PREVVAR(\"";	
							} else {
								funcDeclVars = funcDeclVars + "  DECLARE_USED_VAR(\"";
							}
						}
					}
				}
				
				funcDeclVars = funcDeclVars + var.name + var.type + "\",\"" +
											  var.unitClass + "\",\"" +
											  var.description + "\",\"" +
											  var.SIUnit + "\")\n";
			}
		}

		
		Iterator<FunctionEvent> itEvents = null;
		
		if (funcProperties.functionEvents.size() > 0) {
			itEvents = funcProperties.functionEvents.iterator();			
			funcDeclEvents = "\n// Events\n";			
			FunctionEvent event = null;
			
			while (itEvents.hasNext()) {
				event = itEvents.next();
				funcDeclEvents = funcDeclEvents + "  DECLARE_USED_EVENTS(\"";									
				funcDeclEvents = funcDeclEvents + event.unitClass + "\")\n";
			}
		}

		
		Iterator<FunctionExtraFile> itFiles = null;
		
		if (funcProperties.functionExtraFiles.size() > 0) {
			itFiles = funcProperties.functionExtraFiles.iterator();			
			funcDeclFiles = "\n// Extra Files\n";			
			FunctionExtraFile extrafile = null;
			
			while (itFiles.hasNext()) {
				
				extrafile = itFiles.next();
				
				if (extrafile.isRequired) {
					funcDeclFiles = funcDeclFiles + "  DECLARE_REQUIRED_EXTRAFILE(\"";					
				} else {
					funcDeclFiles = funcDeclFiles + "  DECLARE_USED_EXTRAFILE(\"";					
				}
				
				funcDeclFiles = funcDeclFiles + extrafile.name + "\")\n";
			}
		}
		
		
		
		tplContent = tplContent.replaceAll("\\$\\$FUNCTIONDECLARATION_PARAMS\\$\\$", funcDeclParams);
		tplContent = tplContent.replaceAll("\\$\\$FUNCTIONDECLARATION_SDYN\\$\\$", funcDeclSDyn);
		tplContent = tplContent.replaceAll("\\$\\$FUNCTIONDECLARATION_IDATA\\$\\$", funcDeclIData);		
		tplContent = tplContent.replaceAll("\\$\\$FUNCTIONDECLARATION_VARS\\$\\$", funcDeclVars);
		tplContent = tplContent.replaceAll("\\$\\$FUNCTIONDECLARATION_EVENTS\\$\\$", funcDeclEvents);
		tplContent = tplContent.replaceAll("\\$\\$FUNCTIONDECLARATION_FILES\\$\\$", funcDeclFiles);
		
		return tplContent;
	}
	
	
	private InputStream openSingleCPPContentStream(FunctionProperties funcProperties)
	throws IOException {	

		
		String tplContent = readTemplateFile("/resources/singleCPP.cpp.tpl");
		
		tplContent = tplContent.replaceAll("\\$\\$CLASSNAME\\$\\$", funcProperties.className);	
		tplContent = tplContent.replaceAll("\\$\\$ROOTFILENAME\\$\\$", funcProperties.sourcesFilesRoot);
		
		tplContent = cppSignatureReplacements(tplContent);
		
		return new ByteArrayInputStream(tplContent.getBytes());
	}

	
	private InputStream openCPPContentStream(FunctionProperties funcProperties)
	throws IOException {	

		
		String tplContent = readTemplateFile("/resources/CPPwithH.cpp.tpl");
		
		tplContent = tplContent.replaceAll("\\$\\$CLASSNAME\\$\\$", funcProperties.className);	
		tplContent = tplContent.replaceAll("\\$\\$ROOTFILENAME\\$\\$", funcProperties.sourcesFilesRoot);
		
		tplContent = cppSignatureReplacements(tplContent);
		
		return new ByteArrayInputStream(tplContent.getBytes());
	}

	
	private InputStream openHContentStream(FunctionProperties funcProperties)
	throws IOException {	


		String headerGuard = "__"+funcProperties.sourcesFilesRoot.toUpperCase()+"_"+"H"+"__";
		
		String tplContent = readTemplateFile("/resources/CPPwithH.h.tpl");
		
		tplContent = tplContent.replaceAll("\\$\\$HEADERGUARD\\$\\$", headerGuard);
		tplContent = tplContent.replaceAll("\\$\\$CLASSNAME\\$\\$", funcProperties.className);	
		tplContent = tplContent.replaceAll("\\$\\$ROOTFILENAME\\$\\$", funcProperties.sourcesFilesRoot);
			

		return new ByteArrayInputStream(tplContent.getBytes());
	}
	

	private InputStream openCMakeListsContentStream(FunctionProperties funcProperties)
	throws IOException {	



		String tplContent = readTemplateFile("/resources/CMakeLists.txt.tpl");

		
		String newStr = Matcher.quoteReplacement(funcProperties.installDir);

		
		// adapt $(VARNAME) to CMake  : $ENV{VARNAME}
		newStr = newStr.replaceAll("\\$\\((\\w+)\\)","\\$ENV\\{$1\\}");
		
		tplContent = tplContent.replaceAll("\\$\\$INSTALLDIR\\$\\$", newStr);
		
		return new ByteArrayInputStream(tplContent.getBytes());
	}
	

	private InputStream openCMakeConfigContentStream(FunctionProperties funcProperties)
	throws IOException {	


		
		String tplContent = readTemplateFile("/resources/CMake.in.config.tpl");
		
		tplContent = tplContent.replaceAll("\\$\\$ROOTFILENAME\\$\\$", funcProperties.sourcesFilesRoot);
		tplContent = tplContent.replaceAll("\\$\\$FUNCTIONID\\$\\$", funcProperties.functionID);

		return new ByteArrayInputStream(tplContent.getBytes());
	}


	
	private void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "org.lisah.openfluid.newfunc", IStatus.OK, message, null);
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