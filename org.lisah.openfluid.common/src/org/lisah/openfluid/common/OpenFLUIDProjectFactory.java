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

package org.lisah.openfluid.common;

import java.io.File;
import java.io.IOException;
import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.ICProjectDescriptionManager;
import org.eclipse.cdt.core.settings.model.WriteAccessException;
import org.eclipse.cdt.core.settings.model.extension.CConfigurationData;
import org.eclipse.cdt.managedbuilder.core.IBuilder;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.cdt.managedbuilder.internal.core.Configuration;
import org.eclipse.cdt.managedbuilder.internal.core.ManagedProject;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;

public class OpenFLUIDProjectFactory {

	static public IProject createProject(IWorkspace workspace, 
			String projectName,
			String projectPath) {

        IProject project = null;
        IWorkspaceRoot root = workspace.getRoot();
        
        IProject newProjectHandle = root.getProject(projectName);
		Assert.isNotNull(newProjectHandle);
		Assert.isTrue(!newProjectHandle.exists());

		IProjectDescription projectDesc = workspace.newProjectDescription(newProjectHandle.getName());
		
		if (projectPath != null) {
			File projectFilePath = new File(projectPath);
			projectDesc.setLocationURI(projectFilePath.toURI());
		}
		
		try {
			project = CCorePlugin.getDefault().createCDTProject(projectDesc, newProjectHandle,new NullProgressMonitor());
		} catch (OperationCanceledException e) {
			System.out.println(e);
		} catch (CoreException e) {
			System.out.println(e);
		}
		Assert.isTrue(newProjectHandle.isOpen());

		ICProjectDescriptionManager projectDescMan = CoreModel.getDefault().getProjectDescriptionManager();
		ICProjectDescription cprojectDesc = null;;
		try {
			cprojectDesc = projectDescMan.createProjectDescription(project,false);
		} catch (CoreException e) {
			System.out.println(e);
		}
		ManagedProject managedPrj = new ManagedProject(cprojectDesc);

		Configuration cfg = new Configuration(managedPrj, null,"buildinstall", "Build and Install");

		IBuilder builder = cfg.getEditableBuilder();
		Assert.isNotNull(builder);
		Assert.isTrue(!builder.isInternalBuilder());

		try {
			builder.setManagedBuildOn(false);
		} catch (CoreException e) {
			System.out.println(e);
		}

		CConfigurationData data = cfg.getConfigurationData();
		Assert.isNotNull(data);
		try {
			cprojectDesc.createConfiguration(ManagedBuildManager.CFG_DATA_PROVIDER_ID, data);
		} catch (WriteAccessException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			System.out.println(e);
		}

		// Persist the project description
		try {
			projectDescMan.setProjectDescription(project, cprojectDesc);
		} catch (CoreException e) {
			System.out.println(e);
		}
		
		return project;
	}
	
    static public void configureProject(IProject project) {
		
	}
	    
    
    
    static public void configureBuildSystem(OpenFLUIDPluginProperties properties, IContainer container, IProject project) {

		String projectPath = container.getLocation().toString();
		String projectBuildPath = container.getLocation().toString()+"/"+properties.buildSubdir;
		
		if (System.getProperty("os.name").startsWith("Windows")) {
			projectPath = "\""+container.getLocation().toString()+"\"";
		}
			

		IFolder buildFolder = container.getFolder(new Path(properties.buildSubdir));
		if (!buildFolder.exists()) {
			try {
				buildFolder.create(true, true, null);
			} catch (CoreException e1) {
				System.out.println(e1);
			}						
		}
		
		
		if (properties.isNewProject && project != null) {
			IConfiguration configuration = ManagedBuildManager.getBuildInfo(project).getDefaultConfiguration();
			//configuration.setBuildArguments("install");
			try {
				configuration.getBuilder().setCommand(properties.cmakeCommandPath);
                configuration.getBuilder().setArguments("--build \"${workspace_loc:/" +project.getDescription().getName() + "}/"  + properties.buildSubdir+"\" --target");
				configuration.getBuilder().setFullBuildTarget("install");
			} catch (CoreException e) {
				System.out.println(e);
			}
			String buildPath = configuration.getEditableBuilder().getBuildPath() + properties.buildSubdir;
			configuration.getEditableBuilder().setBuildPath(buildPath);
			ManagedBuildManager.saveBuildInfo(project, true);
		}
		
		
		if (properties.runCMake && properties.cmakeCommandPath != "") {
			OpenFLUIDCMakeTools.runCMakeCommand(properties.cmakeCommandPath, projectPath, projectBuildPath);
		}
	}
}
