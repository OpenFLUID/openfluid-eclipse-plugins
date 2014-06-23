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



import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;



public class SourcesWizardPage extends WizardPage {
	private Text projectText;
	private Text containerText;
	private Text fileText;
	
	private Button projectRadio;
	private Button containerRadio;
	private Button browseButton;
	
	private Button buildCheck;
	private Group sourcesGroup;
	private Group buildGroup;
	private Button runCMakeCheck;
	private Button enableSim2DocCheck;

	private Text simClassText;
	

	private ISelection selection;
	
	private Label installLabel;
	private Label installTextLabel;	
	private Label buildSubdirLabel;
	private Label buildSubdirTextLabel;
	
    private Boolean isCMakeFound;

	public SourcesWizardPage(ISelection selection,Boolean isCMakeFound) {
		super("wizardPage");
		setTitle("OpenFLUID simulator");
		setDescription("Sources files and build system");
		this.selection = selection;
		this.isCMakeFound = isCMakeFound;
	}

		
	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
	
		Label label;
		
		Composite mainComposite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();		
		mainComposite.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 20;
		layout.makeColumnsEqualWidth = false;
		
				
		
		// New project
		projectRadio = new Button(mainComposite, SWT.RADIO);
		projectRadio.setText("&Create new project:");
		GridData data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		projectRadio.setLayoutData (data);
		projectRadio.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				dialogChanged();
			}
		});
		
		projectText = new Text(mainComposite, SWT.BORDER);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.horizontalSpan = 2;
		projectText.setLayoutData(data);
		projectText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
				projectText.setFocus();	
			}
		});
		
		
		
		// Existing container
		containerRadio = new Button(mainComposite, SWT.RADIO);
		containerRadio.setText("&Use existing container:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		containerRadio.setLayoutData (data);
		containerRadio.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				dialogChanged();
				containerText.setFocus();
            }
		});

		containerText = new Text(mainComposite, SWT.BORDER);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		containerText.setLayoutData (data);
		containerText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		browseButton = new Button(mainComposite, SWT.PUSH);
		browseButton.setText("Browse...");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		browseButton.setLayoutData (data);

		browseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});

	    Label separator = new Label(mainComposite, SWT.HORIZONTAL | SWT.SEPARATOR);
	    data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.horizontalSpan = 3;
		separator.setLayoutData(data);
		
		sourcesGroup = new Group(mainComposite,SWT.NULL);
		sourcesGroup.setText(" Sources ");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 3;
		data.grabExcessHorizontalSpace = true;	
		sourcesGroup.setLayoutData(data);
		
		GridLayout sourcesLayout = new GridLayout();		
		sourcesGroup.setLayout(sourcesLayout);
		sourcesLayout.numColumns = 2;
		sourcesLayout.verticalSpacing = 9;
		sourcesLayout.makeColumnsEqualWidth = false;		
		
		
		label = new Label(sourcesGroup, SWT.RIGHT);
		label.setText("&Source file name (.cpp):");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);				
		
		fileText = new Text(sourcesGroup, SWT.BORDER | SWT.SINGLE);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;	
		fileText.setLayoutData(data);
		fileText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});			
		
		label = new Label(sourcesGroup, SWT.RIGHT);
		label.setText("&C++ class name:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);

		simClassText = new Text(sourcesGroup, SWT.BORDER | SWT.SINGLE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		simClassText.setLayoutData (data);

		
		// space
		label = new Label(mainComposite, SWT.RIGHT);
		label.setText("");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);
		
		
        // build system
		buildCheck = new Button (mainComposite, SWT.CHECK);
		buildCheck.setText("Create CMake build system files");
		buildCheck.setSelection(true);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 3;
		data.grabExcessHorizontalSpace = true;		
		buildCheck.setLayoutData(data);
		buildCheck.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				dialogChanged();
			}
			public void widgetSelected(SelectionEvent e) {
				dialogChanged();
			}
			
		});	

		buildGroup = new Group(mainComposite,SWT.NULL);
		buildGroup.setText(" Build system ");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 3;
		data.grabExcessHorizontalSpace = true;	
		buildGroup.setLayoutData(data);
		
		GridLayout buildLayout = new GridLayout();		
		buildGroup.setLayout(buildLayout);
		buildLayout.numColumns = 2;
		buildLayout.verticalSpacing = 9;
		buildLayout.makeColumnsEqualWidth = false;		
		

		buildSubdirLabel = new Label(buildGroup, SWT.RIGHT);
		buildSubdirLabel.setText("Build subdirectory:");
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		buildSubdirLabel.setLayoutData(data);				

		buildSubdirTextLabel = new Label(buildGroup, SWT.LEFT);
		buildSubdirTextLabel.setText("_build");
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
//		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		buildSubdirTextLabel.setLayoutData(data);
		
		
		installLabel = new Label(buildGroup, SWT.RIGHT);
		installLabel.setText("Install directory:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		installLabel.setLayoutData(data);
		
		installTextLabel = new Label(buildGroup, SWT.LEFT);
		
		installTextLabel.setText("(not determined)");
		
		if (System.getProperty("os.name").equals("Linux")) {
	        installTextLabel.setText(System.getProperty("user.home") + System.getProperty("file.separator") +
		                        ".openfluid" + System.getProperty("file.separator") + 
		                        "simulators");
			
		}

		if (System.getProperty("os.name").startsWith("Windows")) {
	        installTextLabel.setText(System.getenv("APPDATA") + System.getProperty("file.separator") +
	        		            "Application Data" + System.getProperty("file.separator") +
		                        "openfluid" + System.getProperty("file.separator") + 
		                        "simulators");
			
		}

        
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
//		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		installTextLabel.setLayoutData(data);


		enableSim2DocCheck = new Button (buildGroup, SWT.CHECK);
		enableSim2DocCheck.setText("Enable build of documentation using sim2doc");
		enableSim2DocCheck.setSelection(false);		
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;		
		enableSim2DocCheck.setLayoutData(data);

		
		runCMakeCheck = new Button (buildGroup, SWT.CHECK);
		runCMakeCheck.setText("Run CMake on project creation");
		runCMakeCheck.setSelection(isCMakeFound);
		runCMakeCheck.setEnabled(isCMakeFound);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;		
		runCMakeCheck.setLayoutData(data);

		
		
		
		mainComposite.pack ();
		
		
		initialize();
		dialogChanged();
		setControl(mainComposite);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		
		projectRadio.setSelection(true);
		containerRadio.setSelection(false);
		
		if (selection != null && selection.isEmpty() == false
				&& selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() > 1)
				return;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IResource) {
				IContainer container;
				if (obj instanceof IContainer)
					container = (IContainer) obj;
				else
					container = ((IResource) obj).getParent();
				projectRadio.setSelection(false);
				containerText.setText(container.getFullPath().toString());
				containerRadio.setSelection(true);
			}
		}
		fileText.setText("MySim.cpp");
		simClassText.setText("MySimulator");
	}


	private void dialogChanged() {
		
		projectText.setEnabled(projectRadio.getSelection());
		containerText.setEnabled(containerRadio.getSelection());
		browseButton.setEnabled(containerRadio.getSelection());
		
		
		if (containerRadio.getSelection()) {
						
			if (getContainerName().length() == 0) {
				updateStatus("File container must be specified");
				return;
			}
			
			IResource container = ResourcesPlugin.getWorkspace().getRoot()
					.findMember(new Path(getContainerName()));
			
			if (container == null
					|| (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
				updateStatus("Container must exist");
				return;
			}
			
			if (!container.isAccessible()) {
				updateStatus("Container must be writable");
				return;
			}
		}
		
		if (projectRadio.getSelection()) {

			if (getProjectName().length() == 0) {
				updateStatus("Project must be specified");
				return;
			}
			else {
				IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(getProjectName());
				if (project == null) {
					updateStatus("Invalid project name");
					return; 
				}
				if (project.exists()) {
					updateStatus("Project already exists");
					return;
				}
			}

		}
		
		String fileName = getFileName();
 
		
		if (fileName.length() == 0) {
			updateStatus("File name must be specified");
			return;
		}
		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus("File name must be valid");
			return;
		}
		int dotLoc = fileName.lastIndexOf('.');
		if (dotLoc != -1) {
			String ext = fileName.substring(dotLoc + 1);
			if (ext.equalsIgnoreCase("cpp") == false) {
				updateStatus("File extension must be \"cpp\"");
				return;
			}
		}

		buildGroup.setEnabled(buildCheck.getSelection());
		buildSubdirLabel.setEnabled(buildCheck.getSelection());
		installTextLabel.setEnabled(buildCheck.getSelection());
		installLabel.setEnabled(buildCheck.getSelection());
		runCMakeCheck.setEnabled(buildCheck.getSelection());
		
		updateStatus(null);
	}

	private void handleBrowse() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
				"Select container");
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				containerText.setText(((Path) result[0]).toString());
			}
		}
	}
	
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getProjectName() {
		return projectText.getText();
	}

	public String getContainerName() {
		return containerText.getText();
	}	
	
	public String getFileName() {
		return fileText.getText();
	}

	public String getSimClass() {
		return simClassText.getText();
	}
	
	
	public boolean getMakefileCheck() {
		return buildCheck.getSelection();
	}	

	public String getInstallDir() {
		return installTextLabel.getText();
	}
	
	public void fillSimulatorProperties(SimulatorProperties properties)
	{
		properties.isNewProject = projectRadio.getSelection();
		properties.project = projectText.getText();
		properties.container = containerText.getText();
		properties.sourcesFilesRoot = fileText.getText();
		properties.className = simClassText.getText();
		
		properties.buildSubdir = buildSubdirTextLabel.getText();
	    properties.installDir = installTextLabel.getText();
	    properties.simulatorSim2Doc = enableSim2DocCheck.getSelection();
	    properties.runCMake = runCMakeCheck.getSelection();
	    properties.createBuildSystem = buildCheck.getSelection();
	}	
	
	
}