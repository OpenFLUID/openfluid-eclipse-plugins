
package org.lisah.openfluid.wareshubclient.importWizards;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.SSLHandshakeException;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.lisah.openfluid.wareshubclient.importWizards.RemoteWaresHub.WareData;


public class SelectWizardPage extends WizardPage {
		
	private Label URLLabel;
	private Combo wareTypeCombo;
	private Table waresTable;
	private Button selectAllButton;
	private Button selectNoneButton;	
	
    private final OpenFLUIDPreferences ofPrefs;
	private final LocalWorkspaceInfos localWI;
	private RemoteWaresHub remoteWH;

	
	public SelectWizardPage(ISelection selection,
			final OpenFLUIDPreferences ofp,
			final LocalWorkspaceInfos lwi, RemoteWaresHub rwh) {
		super("");

		setTitle("");
		setDescription("Select OpenFLUID wares to import");
		
		ofPrefs = ofp;
		localWI = lwi;
		remoteWH = rwh;
	}


	public void createControl(Composite parent) {
    	Label label;
    	GridData data;
		
		Composite mainComposite = new Composite(parent, SWT.NULL);
    	
    	GridLayout layout = new GridLayout();		
		mainComposite.setLayout(layout);
		layout.verticalSpacing = 20;
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = false;
		
		label = new Label(mainComposite, SWT.RIGHT);
		label.setText("Connected to:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);				
    	
		URLLabel = new Label(mainComposite, SWT.LEFT);
		URLLabel.setText("");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;	
		URLLabel.setLayoutData (data);
		
		label = new Label(mainComposite, SWT.RIGHT);
		label.setText("Type:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);				
    
		wareTypeCombo = new Combo(mainComposite, SWT.READ_ONLY);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;	
		wareTypeCombo.setLayoutData(data);
		wareTypeCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				populateWaresTable(wareTypeCombo.getText());
			}
		});
		
		
		waresTable = new Table(mainComposite,
				SWT.CHECK | SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		data.horizontalSpan = 2;
		waresTable.setLayoutData(data);
		waresTable.setHeaderVisible (true);
		String[] titles = {" ","ID","Description"};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn(waresTable, SWT.NONE);
			column.setText(titles[i]);
		}
		waresTable.addListener(SWT.Selection, new Listener () {
			@Override
			public void handleEvent(Event event) {
				if (event.detail == SWT.CHECK) {
					dialogChanged();
				}
			}
		});
		
		Composite buttonsComposite = new Composite(mainComposite, SWT.NULL);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = false;
		data.grabExcessVerticalSpace = false;
		data.horizontalSpan = 2;
		buttonsComposite.setLayoutData(data);
		
		GridLayout buttonsLayout = new GridLayout();		
		buttonsComposite.setLayout(buttonsLayout);
		buttonsLayout.verticalSpacing = 10;
		buttonsLayout.numColumns = 2;
		//layout.makeColumnsEqualWidth = false;
		
		selectAllButton = new Button(buttonsComposite, SWT.PUSH);
		selectAllButton.setText("Select all");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = false;
		data.grabExcessVerticalSpace = false;
		selectAllButton.setLayoutData(data);

		selectAllButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				final TableItem [] items = waresTable.getItems();
				
				for (int i = 0; i < items.length; ++i) {
					items[i].setChecked(true);
				}
				
				dialogChanged();
			}
		}); 

		
		selectNoneButton = new Button(buttonsComposite, SWT.PUSH);
		selectNoneButton.setText("Select none");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = false;
		data.grabExcessVerticalSpace = false;
		selectNoneButton.setLayoutData(data);

		selectNoneButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				final TableItem [] items = waresTable.getItems();
				
				for (int i = 0; i < items.length; ++i) {
					items[i].setChecked(false);
				}
				
				dialogChanged();
				
			}
		}); 

		
    	dialogChanged();
    	
		setControl(mainComposite);		
	}
	
	
	private void dialogChanged() {
		
		if (!isAnyWareChecked()) {
    		updateStatus("No ware selected");
		}
		else {
			updateStatus(null);
		}
			
	}
	
	
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
	
	
	
	public void setVisible(boolean visible) {

		super.setVisible(visible);

		if (visible) {
			updateWaresHubInfos();
		}
	}

	
	private boolean isWareExists(String wareID) {
		IWorkspaceRoot wRoot = ResourcesPlugin.getWorkspace().getRoot();
		IPath ewsPath = wRoot.getFullPath();
		IPath epPath = ewsPath.append(wareID);

		String wareLocalPath = ofPrefs.getWaresTypeDevPath(getActiveWareType())+"/"+wareID;
        File fsPath = new File(wareLocalPath)	;	
		
		System.out.println(wareID+": "+ewsPath+ "  "+wRoot.exists(ewsPath));
		
		return wRoot.exists(epPath) || fsPath.exists();

	}
	
	
	private void populateWaresTable(String TypeStr) {
				
		waresTable.removeAll();
		
		ArrayList<RemoteWaresHub.WareData> Wares = remoteWH.getAvailableWares().get(TypeStr);
		
		for (RemoteWaresHub.WareData w: Wares) {
			TableItem item = new TableItem (waresTable, SWT.NONE);
			item.setText(1,w.ID);
			item.setText(2,w.Description);
			if (isWareExists(w.ID)) {
				// TODO do better!
				item.setGrayed(true);
			}
		}
		
		for (int i=0; i<waresTable.getColumnCount(); i++) {
			waresTable.getColumn(i).pack();
		}	
	}
	
	
	void updateWaresHubInfos() {
		
		URLLabel.setText("None");
	    wareTypeCombo.removeAll();
	    waresTable.removeAll();

		remoteWH.setURL(localWI.requestedRemoteURL);

		boolean connected = false;
		
		try {
			connected = remoteWH.connect(false);
		} catch (SSLHandshakeException e) {
			boolean OK =
					MessageDialog.openConfirm(getShell(), 
							"Untrusted certificate", 
							"The remote SSL certificate cannot be verified\n"
							+ "Are you sure you want to continue?");	
			
			if (OK) {
				try {
					connected = remoteWH.connect(true);
				} catch (SSLHandshakeException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		
		if (connected) {
			
				final HashMap<String, ArrayList<WareData>> wares = remoteWH.getAvailableWares();
				
				URLLabel.setText(localWI.requestedRemoteURL);
				
			    for (String t: wares.keySet()) {
				  wareTypeCombo.add(t);
			    }
						    
			    if (wareTypeCombo.getItemCount() > 0) {
			    	wareTypeCombo.select(0);
			    	populateWaresTable(wareTypeCombo.getItem(0));
			    }
			    	
			} else {
				MessageDialog.openError(getShell(), "Error", 
						remoteWH.getErrorMsg() + "\n" + localWI.requestedRemoteURL);
			}
		
	}
	
	
	public String getActiveWareType() {
		return wareTypeCombo.getText();
	}
	

	public ArrayList<String> getCheckedWaresForType() {
		ArrayList<String> checkedWares = new ArrayList<String>();
		
		final TableItem [] items = waresTable.getItems();
		
		for (int i = 0; i < items.length; ++i) {
			if (items[i].getChecked()) {
				checkedWares.add(items[i].getText(1));
			}
		}
		
		return checkedWares;
	}
	
	
	public boolean isAnyWareChecked() {
		return !getCheckedWaresForType().isEmpty();
	}
}
