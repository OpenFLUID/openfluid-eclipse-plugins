package org.lisah.openfluid.newfunc.wizards;

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


		
	}
	
	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		sourcesPage = new SourcesWizardPage(selection);
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

		
		funcProperties = new FunctionProperties();
		
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
		
		monitor.beginTask("Creating files", 2);
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
						
		IResource resource = root.findMember(new Path(funcProperties.container));
		
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException("Container \"" + funcProperties.container + "\" does not exist.");
		}
		
		IContainer container = (IContainer) resource;

		int dotPos = funcProperties.sourcesFilesRoot.lastIndexOf(".");
		funcProperties.sourcesFilesRoot = funcProperties.sourcesFilesRoot.substring(0, dotPos);
		
		
		
		IFile cmakelistsFile = null;
		IFile cmakeconfFile = null;
		IFile makeFile = null;
		IFile hFile = null;
		final IFile cppFile = container.getFile(new Path(funcProperties.sourcesFilesRoot+".cpp"));
		
		if (!funcProperties.singleSourceFile) {
			hFile = container.getFile(new Path(funcProperties.sourcesFilesRoot+".h"));
		}
		
		if (funcProperties.buildSystem.toUpperCase().contains("CMAKE")) {
			cmakelistsFile = container.getFile(new Path("CMakeLists.txt"));
			cmakeconfFile = container.getFile(new Path("CMake.in.config"));
			if (funcProperties.buildFolder != "") {
				IFolder buildFolder = container.getFolder(new Path(funcProperties.buildFolder));
				buildFolder.create(true, true, null);				
			}
		}
			
		if (funcProperties.buildSystem.toUpperCase().contains("MAKEFILE")) {
			makeFile = container.getFile(new Path("makefile"));
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
			
			
		}
			

		if (makeFile != null) {
			
			stream = openMakefileContentStream(funcProperties);
			
			if (makeFile.exists()) {
				makeFile.setContents(stream, true, true, monitor);
			} else {
				makeFile.create(stream, true, monitor);
			}
			
			stream.close();
			
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
		String funcDeclIData = "";
		String funcDeclVars = "";
		String funcDeclEvents = "";
		String funcDeclFiles = "";

		tplContent = tplContent.replaceAll("\\$\\$FUNCTIONID\\$\\$", funcProperties.functionID);
		tplContent = tplContent.replaceAll("\\$\\$FUNCTIONNAME\\$\\$", funcProperties.functionName);
		tplContent = tplContent.replaceAll("\\$\\$FUNCTIONDESC\\$\\$", funcProperties.functionDescription);
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
								 param.valueUnit + "\")\n";
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
								 				idata.valueUnit + "\")\n";
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
				
				funcDeclVars = funcDeclVars + var.name + "\",\"" +
											  var.unitClass + "\",\"" +
											  var.description + "\",\"" +
											  var.valueUnit + "\")\n";
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
	

	private InputStream openMakefileContentStream(FunctionProperties funcProperties)
	throws IOException {	


		
		String tplContent = readTemplateFile("/resources/makefile.tpl");
		
		
		
		tplContent = tplContent.replaceAll("\\$\\$ROOTFILENAME\\$\\$", funcProperties.sourcesFilesRoot);
		tplContent = tplContent.replaceAll("\\$\\$FUNCTIONID\\$\\$", funcProperties.functionID);
		String newStr = Matcher.quoteReplacement(funcProperties.installDir);
		tplContent = tplContent.replaceAll("\\$\\$INSTALLDIR\\$\\$", newStr);		

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