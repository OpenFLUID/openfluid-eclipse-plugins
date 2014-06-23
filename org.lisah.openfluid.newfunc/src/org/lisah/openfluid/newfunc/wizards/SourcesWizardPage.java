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



import org.eclipse.core.resources.IContainer;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (mpe).
 */

public class SourcesWizardPage extends WizardPage {
	private Text containerText;
	private Text fileText;
	
	private Button buildCheck;
	private Group sourcesGroup;
	private Group buildGroup;
	private Combo filesCombo;
	private Combo buildSystemCombo;
	

	private Text funcClassText;
	

	private ISelection selection;
	
	private Label installLabel;
	private Label installTextLabel;	
	private Label buildSystemLabel;
	

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public SourcesWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("OpenFLUID simulation function");
		setDescription("Sources files and build system");
		this.selection = selection;
		
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
	
		
		
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();		
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 20;
		layout.makeColumnsEqualWidth = false;
		
				
		
		// Directory
		Label label = new Label(container, SWT.RIGHT);
		label.setText("&Container:");
		GridData data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);
		
		containerText = new Text(container, SWT.BORDER);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		containerText.setLayoutData (data);
		containerText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		Button browseButton = new Button(container, SWT.PUSH);
		browseButton.setText("Browse...");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		browseButton.setLayoutData (data);
		
		browseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});
		

		sourcesGroup = new Group(container,SWT.NULL);
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
		label.setText("&Sources output:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);				
		
		filesCombo = new Combo(sourcesGroup, SWT.READ_ONLY);
		filesCombo.add(".cpp and .h files");
		filesCombo.add("single .cpp file");
		filesCombo.select(1);
		data = new GridData ();
//		data.horizontalAlignment = GridData.FILL;
//		data.grabExcessHorizontalSpace = true;	
		filesCombo.setLayoutData(data);
		
		label = new Label(sourcesGroup, SWT.RIGHT);
		label.setText("&Class name:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);

		funcClassText = new Text(sourcesGroup, SWT.BORDER | SWT.SINGLE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		funcClassText.setLayoutData (data);

		
		// space
		label = new Label(container, SWT.RIGHT);
		label.setText("");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);
		
		
        // build system
		buildCheck = new Button (container, SWT.CHECK);
		buildCheck.setText ("Create build system file(s)");
		buildCheck.setSelection(true);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 3;
		data.grabExcessHorizontalSpace = true;		
		buildCheck.setLayoutData (data);
		buildCheck.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				dialogChanged();
			}
			public void widgetSelected(SelectionEvent e) {
				dialogChanged();
			}
			
		});	

		buildGroup = new Group(container,SWT.NULL);
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
		

		buildSystemLabel = new Label(buildGroup, SWT.RIGHT);
		buildSystemLabel.setText("&Build system:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		buildSystemLabel.setLayoutData (data);				
		
		buildSystemCombo = new Combo(buildGroup, SWT.READ_ONLY);
		buildSystemCombo.add("GNU Makefile");
		buildSystemCombo.add("CMake 2.6+");
		buildSystemCombo.select(1);
		data = new GridData ();
//		data.horizontalAlignment = GridData.FILL;
//		data.grabExcessHorizontalSpace = true;	
		buildSystemCombo.setLayoutData(data);
				
		
		installLabel = new Label(buildGroup, SWT.RIGHT);
		installLabel.setText("&Install directory:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		installLabel.setLayoutData (data);
		
		installTextLabel = new Label(buildGroup, SWT.LEFT);
		
		installTextLabel.setText("(not determined)");
		
		if (System.getProperty("os.name").equals("Linux")) {
	        installTextLabel.setText(System.getProperty("user.home") + System.getProperty("file.separator") +
		                        ".openfluid" + System.getProperty("file.separator") + 
		                        "functions");
			
		}

		if (System.getProperty("os.name").startsWith("Windows")) {
	        installTextLabel.setText(System.getenv("APPDATA") + System.getProperty("file.separator") +
	        		            "Application Data" + System.getProperty("file.separator") +
		                        "openfluid" + System.getProperty("file.separator") + 
		                        "functions");
			
		}

        
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
//		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		installTextLabel.setLayoutData (data);


		

		
		
		
		container.pack ();
		
		
		initialize();
		dialogChanged();
		setControl(container);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
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
				containerText.setText(container.getFullPath().toString());
			}
		}
		fileText.setText("NewFunc.cpp");
		funcClassText.setText("NewFunction");
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */

	private void handleBrowse() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
				"Select new file container");
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				containerText.setText(((Path) result[0]).toString());
			}
		}
	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {
		IResource container = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(new Path(getContainerName()));
		String fileName = getFileName();

		if (getContainerName().length() == 0) {
			updateStatus("File container must be specified");
			return;
		}
		if (container == null
				|| (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
			updateStatus("File container must exist");
			return;
		}
		if (!container.isAccessible()) {
			updateStatus("Project must be writable");
			return;
		}
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
		buildSystemLabel.setEnabled(buildCheck.getSelection());
		buildSystemCombo.setEnabled(buildCheck.getSelection());	
		installTextLabel.setEnabled(buildCheck.getSelection());
		installLabel.setEnabled(buildCheck.getSelection());
		
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getContainerName() {
		return containerText.getText();
	}

	public String getFileName() {
		return fileText.getText();
	}

	public String getFuncClass() {
		return funcClassText.getText();
	}
	
	
	public boolean getMakefileCheck() {
		return buildCheck.getSelection();
	}	

	public String getInstallDir() {
		return installTextLabel.getText();
	}
	
	public void fillFunctionProperties(FunctionProperties properties)
	{
		properties.container = containerText.getText();
		properties.sourcesFilesRoot = fileText.getText();
		properties.className = funcClassText.getText();
		properties.singleSourceFile = (filesCombo.getSelectionIndex() == 1);
		
		if (buildCheck.getSelection()) {
			properties.buildSystem = buildSystemCombo.getText();
			if (properties.buildSystem.toUpperCase().contains("CMAKE")) {
				properties.buildFolder = "_build";
			}
			properties.installDir = installTextLabel.getText();
		}
	}	
	
	
}