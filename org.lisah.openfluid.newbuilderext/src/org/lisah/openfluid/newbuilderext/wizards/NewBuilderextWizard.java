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


package org.lisah.openfluid.newbuilderext.wizards;

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
import org.lisah.openfluid.common.OpenFLUIDCMakeTools;
import org.lisah.openfluid.common.OpenFLUIDProjectFactory;
import org.lisah.openfluid.newbuilderext.Activator;
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

public class NewBuilderextWizard extends Wizard implements INewWizard {
	private SourcesWizardPage sourcesPage;
	private InfosWizardPage infosPage;	
	private PrefsWizardPage prefsPage;	
	private ISelection selection;
	
	private BuilderextProperties builderextProperties;

	/**
	 * Constructor for SampleNewWizard.
	 */
	public NewBuilderextWizard() {
		super();
		setNeedsProgressMonitor(true);

		builderextProperties = new BuilderextProperties();
		builderextProperties.cmakeCommandPath = OpenFLUIDCMakeTools.findCMakeCommand();
	}

	
	
	public void addPages() {
		sourcesPage = new SourcesWizardPage(selection,!builderextProperties.cmakeCommandPath.isEmpty());
		infosPage = new InfosWizardPage(selection);		
		prefsPage = new PrefsWizardPage(selection);		
		addPage(sourcesPage);
		addPage(infosPage);
		addPage(prefsPage);				
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		
		sourcesPage.fillBuilderextProperties(builderextProperties);
		infosPage.fillBuilderextProperties(builderextProperties);
		prefsPage.fillBuilderextProperties(builderextProperties);
	
		
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(monitor);
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
	

	private void doFinish(IProgressMonitor monitor)
		throws CoreException, IOException {
		
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();

        IContainer location;
        IProject project = null;
        		
		
		if (builderextProperties.isNewProject) {

			monitor.beginTask("Creating project...", 2);

			
			project = OpenFLUIDProjectFactory.createProject(workspace,builderextProperties.project);
			
			location = (IContainer)project;
		}
		else
		{
			IResource resource = root.findMember(new Path(builderextProperties.container));
			
			if (!resource.exists() || !(resource instanceof IContainer)) {
				throwCoreException("Container \"" + builderextProperties.container + "\" does not exist.");
			}
			
			location = (IContainer)resource;
		}
		
		monitor.worked(1);
		monitor.setTaskName("Creating source(s) file(s)...");


		int dotPos = builderextProperties.sourcesFilesRoot.lastIndexOf(".");
		builderextProperties.sourcesFilesRoot = builderextProperties.sourcesFilesRoot.substring(0, dotPos);

		final IFile cppFile = location.getFile(new Path(builderextProperties.sourcesFilesRoot+".cpp"));

		

		InputStream stream = openSingleCPPContentStream(builderextProperties);
		
		if (cppFile.exists()) {
			cppFile.setContents(stream, true, true, monitor);
		} else {
			cppFile.create(stream, true, monitor);
		}

		stream.close();


		
		monitor.worked(1);
		monitor.setTaskName("Creating build system...");

        if (builderextProperties.createBuildSystem)
        {
        	IFile cmakelistsFile = null;
        	IFile cmakeconfFile = null;

        	cmakelistsFile = location.getFile(new Path("CMakeLists.txt"));
        	cmakeconfFile = location.getFile(new Path("CMake.in.config"));

        	if (cmakelistsFile != null) {

        		stream = openCMakeListsContentStream(builderextProperties);

        		if (cmakelistsFile.exists()) {
        			cmakelistsFile.setContents(stream, true, true, monitor);
        		} else {
        			cmakelistsFile.create(stream, true, monitor);
        		}

        		stream.close();

        		stream = openCMakeConfigContentStream(builderextProperties);

        		if (cmakeconfFile.exists()) {
        			cmakeconfFile.setContents(stream, true, true, monitor);
        		} else {
        			cmakeconfFile.create(stream, true, monitor);
        		}

        		stream.close();

        		if (builderextProperties.buildSubdir != "") {
        			monitor.worked(1);
        			monitor.setTaskName("Configuring build system...");

        			OpenFLUIDProjectFactory.configureBuildSystem(builderextProperties,location,project);
        			
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
	
	
	private String generatePrefsEntriesDeclarations(BuilderextProperties extProperties)
	{
		String prefsDecl = "";
				
		Iterator<PrefsData> itData = extProperties.extDefaultPrefs.iterator();						
		while (itData.hasNext()) {
			PrefsData prefsData = itData.next();
			prefsDecl += "      m_Entries[\"" + prefsData.param + "\"] = NULL;  m_Labels[\"" + prefsData.param + "\"] = \"" + prefsData.label + "\";\n";
		}
		
		return prefsDecl;		
	}
	
	private InputStream openSingleCPPContentStream(BuilderextProperties extProperties)
	throws IOException {	
		
		
		String extensionHooks = "("+extProperties.className+")";
		String prefsPanelClassDef = "";
		String prefsEntriesDecl = "";
		String PrefsDefaults = "";
				
		if (extProperties.extIsPrefs) {
						
			// prefs panel
			prefsPanelClassDef = readTemplateFile("/resources/PrefsPanel.class.tpl");
						
			// prefs defaults values
			Iterator<PrefsData> itData = extProperties.extDefaultPrefs.iterator();						
			while (itData.hasNext()) {
				PrefsData prefsData = itData.next();
				PrefsDefaults += "(\"" + prefsData.param + "=" + prefsData.value + "\")";
			}
			
			// prefs entries
			prefsEntriesDecl = generatePrefsEntriesDeclarations(extProperties);
			
			// extension hooks
			extensionHooks = extensionHooks + "("+extProperties.className+"Prefs)";
		}
		
		
		
        String tplContent = readTemplateFile("/resources/"+ extProperties.extType +".cpp.tpl");
		
        tplContent = tplContent.replace("$$PREFSPANELCLASSDEF$$", prefsPanelClassDef);
        
		tplContent = tplContent.replace("$$CLASSNAME$$", extProperties.className);	
		tplContent = tplContent.replace("$$ROOTFILENAME$$", extProperties.sourcesFilesRoot);
		
		tplContent = tplContent.replace("$$EXTENSIONID$$", extProperties.extID);
		tplContent = tplContent.replace("$$EXTENSIONTYPE$$", extProperties.extType);
		tplContent = tplContent.replace("$$EXTENSIONSHORTNAME$$", extProperties.extShortName);
		tplContent = tplContent.replace("$$EXTENSIONNAME$$", extProperties.extName);
		tplContent = tplContent.replace("$$EXTENSIONDESC$$", extProperties.extDescription);
		tplContent = tplContent.replace("$$EXTENSIONAUTHORS$$", extProperties.extAuthor);
		tplContent = tplContent.replace("$$EXTENSIONEMAILS$$", extProperties.extAuthorEmail);
		
		tplContent = tplContent.replace("$$PREFSDEFAULTS$$", PrefsDefaults);
		tplContent = tplContent.replace("$$PREFSENTRIESDECL$$",prefsEntriesDecl);
		tplContent = tplContent.replace("$$EXTENSIONHOOKS$$", extensionHooks);
		
								
		return new ByteArrayInputStream(tplContent.getBytes());
	}

	
	private InputStream openCMakeListsContentStream(BuilderextProperties extProperties)
	throws IOException {	

		String tplContent = readTemplateFile("/resources/CMakeLists.txt.tpl");

		String newStr = Matcher.quoteReplacement(extProperties.installDir);

		// adapt $(VARNAME) to CMake  : $ENV{VARNAME}
		newStr = newStr.replaceAll("\\$\\((\\w+)\\)","\\$ENV\\{$1\\}");
		
		tplContent = tplContent.replaceAll("\\$\\$INSTALLDIR\\$\\$", newStr);
		
		return new ByteArrayInputStream(tplContent.getBytes());
	}
	

	private InputStream openCMakeConfigContentStream(BuilderextProperties extProperties)
	throws IOException {	
		
		String tplContent = readTemplateFile("/resources/CMake.in.config.tpl");
		
		tplContent = tplContent.replaceAll("\\$\\$ROOTFILENAME\\$\\$", extProperties.sourcesFilesRoot);
		tplContent = tplContent.replaceAll("\\$\\$BUILDEREXTID\\$\\$", extProperties.extID);

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