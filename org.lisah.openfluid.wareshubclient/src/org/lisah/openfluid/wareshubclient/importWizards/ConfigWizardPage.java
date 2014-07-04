
package org.lisah.openfluid.wareshubclient.importWizards;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


public class ConfigWizardPage extends WizardPage {
	
	
	private Group remoteGroup;	
	private Group localGroup;

	private Text URLText;
	private Text usernameText;
	private Text passwordText;

	private Text localPathText;
	private Label localSimsPathLabel;
	private Label localObssPathLabel;
	private Label localBextsPathLabel;
	
	private Button browseButton;

	private OpenFLUIDPreferences ofPrefs;
	
	
	// =====================================================================
	// =====================================================================

	
	public ConfigWizardPage(ISelection selection, 
			OpenFLUIDPreferences ofp) {
		super("");
		
		setTitle("");
		setDescription("Prepare to import OpenFLUID wares from a remote WaresHub");	
		
		ofPrefs = ofp;
	}


	// =====================================================================
	// =====================================================================

	
	public void createControl(Composite parent) {
    	
		GridData data;
		Label label;
		
		Composite mainComposite = new Composite(parent, SWT.NULL);
    	
		GridLayout layout = new GridLayout();		
		mainComposite.setLayout(layout);
		layout.verticalSpacing = 20;
		layout.makeColumnsEqualWidth = false;
		
    	remoteGroup = new Group(mainComposite,SWT.NULL);
		remoteGroup.setText(" Remote WaresHub ");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;	
		remoteGroup.setLayoutData(data);
		
		GridLayout remoteLayout = new GridLayout();		
		remoteGroup.setLayout(remoteLayout);
		remoteLayout.numColumns = 5;
		remoteLayout.verticalSpacing = 9;
		remoteLayout.makeColumnsEqualWidth = false;
    	
		label = new Label(remoteGroup, SWT.RIGHT);
		label.setText("WaresHub URL:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);				
		
		URLText = new Text(remoteGroup, SWT.BORDER | SWT.SINGLE);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 4;
		data.grabExcessHorizontalSpace = true;	
		URLText.setLayoutData(data);		
		URLText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				ofPrefs.setRequestedRemoteURL(URLText.getText());
				dialogChanged();
			}
		});
		
		
		label = new Label(remoteGroup, SWT.RIGHT);
		label.setText("Username:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);
		
		usernameText = new Text(remoteGroup, SWT.BORDER | SWT.SINGLE);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;	
		usernameText.setLayoutData(data);
		usernameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				ofPrefs.setUserName(usernameText.getText());				
				dialogChanged();
			}
		});
		
		
		label = new Label(remoteGroup, SWT.RIGHT);
		label.setText("Password:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);
		
		passwordText = new Text(remoteGroup, SWT.BORDER | SWT.PASSWORD);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;	
		passwordText.setLayoutData(data);
		passwordText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				ofPrefs.setUserPassword(passwordText.getText());
				dialogChanged();
			}
		});
		
		
    	localGroup = new Group(mainComposite,SWT.NULL);
		localGroup.setText(" Local path for cloned wares ");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;	
		localGroup.setLayoutData(data);
		
		GridLayout localLayout = new GridLayout();		
		localGroup.setLayout(localLayout);
		localLayout.numColumns = 3;
		localLayout.verticalSpacing = 9;
		localLayout.makeColumnsEqualWidth = false;
    	
		label = new Label(localGroup, SWT.RIGHT);
		label.setText("Workspace path:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);				
		
		localPathText = new Text(localGroup, SWT.BORDER | SWT.SINGLE);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;	
		localPathText.setLayoutData(data);
		localPathText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		browseButton = new Button(localGroup, SWT.PUSH);
		browseButton.setText("Browse...");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		browseButton.setLayoutData(data);

		browseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		}); 
		
		label = new Label(localGroup, SWT.RIGHT);
		label.setText("Simulators:");
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData(data);			
		
		localSimsPathLabel = new Label(localGroup, SWT.LEFT);
		localSimsPathLabel.setText("");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		localSimsPathLabel.setLayoutData(data);
		
		label = new Label(localGroup, SWT.RIGHT);
		label.setText("Observers:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData(data);			
		
		localObssPathLabel = new Label(localGroup, SWT.LEFT);
		localObssPathLabel.setText("");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		localObssPathLabel.setLayoutData(data);
		
		label = new Label(localGroup, SWT.RIGHT);
		label.setText("Builder-extenstions:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData(data);			
		
		localBextsPathLabel = new Label(localGroup, SWT.LEFT);
		localBextsPathLabel.setText("");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		localBextsPathLabel.setLayoutData(data);
		
		mainComposite.pack();
		initialize();
		dialogChanged();
		setControl(mainComposite);
	}
	

	// =====================================================================
	// =====================================================================

	
	private void handleBrowse() {
		
		DirectoryDialog dialog = new DirectoryDialog(getShell());
		
		
		String selectedPath = dialog.open();

		if (selectedPath != null) {
			ofPrefs.setWorkspacePath(selectedPath);
			localPathText.setText(ofPrefs.getWorkspacePath());
			localSimsPathLabel.setText(ofPrefs.getWaresTypeDevPath("simulators"));
			localObssPathLabel.setText(ofPrefs.getWaresTypeDevPath("observers"));
			localBextsPathLabel.setText(ofPrefs.getWaresTypeDevPath("builderexts"));
		}
	}
	
	
	// =====================================================================
	// =====================================================================
	
	
	private void initialize() {
		URLText.setText(ofPrefs.getRequestedRemoteURL());		
		usernameText.setText(ofPrefs.getUserName());
		
		localPathText.setText(ofPrefs.getWorkspacePath());
		localSimsPathLabel.setText(ofPrefs.getWaresTypeDevPath("simulators"));
		localObssPathLabel.setText(ofPrefs.getWaresTypeDevPath("observers"));
		localBextsPathLabel.setText(ofPrefs.getWaresTypeDevPath("builderexts"));
	}
	
	
	// =====================================================================
	// =====================================================================	
	
	
	private void dialogChanged() {
		if (URLText.getText().length() == 0) {
			updateStatus("WaresHub URL must be specified");
			return;
		}	
		
		if (usernameText.getText().length() == 0) {
			updateStatus("User name must be specified");
			return;
		}	
		
		if (passwordText.getText().length() == 0) {
			updateStatus("User password must be specified");
			return;
		}	
		
		if (localPathText.getText().length() == 0) {
			updateStatus("Local path for cloning wares must be specified");
			return;
		}	
		
		updateStatus(null);
	}
	
	
	// =====================================================================
	// =====================================================================

	
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	
}
