package org.lisah.openfluid.wareshubjson.editors;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class IssueDialog extends TitleAreaDialog {


	private Text IDText;
	private Text titleText;
	private Text creatorText;
	private DateTime date;
	private Combo typeCombo;
	private Combo stateCombo;
	private Text descText;
	private Combo urgencyCombo;
	
	Button OKButton;
	
	private WaresHubJSONData.IssueData existingIssueData = null;
	private ArrayList<String> existingIDs = null;
	
	private WaresHubJSONData.IssueData issueData = null;
	
	
	public IssueDialog(Shell parentShell, final WaresHubJSONData.IssueData data, ArrayList<String> IDs) {
		super(parentShell);
        this.existingIssueData = data;
        this.existingIDs = IDs;
	}


	@Override
	public void create() {

		super.create();
	    
		if (this.existingIssueData == null) {
			setMessage("Adding new issue");
		} else {
			setMessage("Editing issue \""+this.existingIssueData.getID()+"\"");
		}
		
		
		updateDialogFromData();
		
		dialogChanged();
		
		getShell().pack();
		
	}


	@Override
	protected Control createDialogArea(Composite parent) {
						
		GridData data;
		Label label;

		GridLayout parentLayout = new GridLayout();		
		parentLayout.numColumns = 1;
		parentLayout.verticalSpacing = 0;
		parentLayout.horizontalSpacing = 0;
		parent.setLayout(parentLayout);
		
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData (SWT.FILL,SWT.FILL,true,true));
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		layout.horizontalSpacing = 5;
		layout.makeColumnsEqualWidth = false;
		composite.setLayout(layout);
		
		
		label = new Label(composite, SWT.RIGHT);
		label.setText("ID:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData(data);
		
		IDText = new Text (composite, SWT.BORDER);
		data = new GridData ();
		data.minimumWidth = 100;
		IDText.setLayoutData(data);
		IDText.addModifyListener(new ModifyListener() {		
			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged();				
			}
		});
		
		
		label = new Label(composite, SWT.RIGHT);
		label.setText("Title:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData(data);
		
	    titleText = new Text (composite, SWT.BORDER);
		data = new GridData ();
		data.widthHint = 250;
		data.horizontalAlignment = GridData.FILL;
		titleText.setLayoutData(data);
		titleText.addModifyListener(new ModifyListener() {		
			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged();				
			}
		});
		
		
		label = new Label(composite, SWT.RIGHT);
		label.setText("Creator:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData(data);
		
	    creatorText = new Text (composite, SWT.BORDER);
		data = new GridData ();
		data.widthHint = 100;
		creatorText.setLayoutData(data);
		creatorText.addModifyListener(new ModifyListener() {		
			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged();				
			}
		});
		
		label = new Label(composite, SWT.RIGHT);
		label.setText("Date:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.BEGINNING;
		label.setLayoutData(data);
		
		date = new DateTime (composite, SWT.CALENDAR);
		data = new GridData ();
		date.setLayoutData(data);

		
		label = new Label(composite, SWT.RIGHT);
		label.setText("Type:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData(data);
		
		typeCombo = new Combo(composite,SWT.READ_ONLY);
		String[] types = {"bug","feature","review"};
		typeCombo.setItems(types);
		data = new GridData();
		typeCombo.setLayoutData(data);
		typeCombo.addSelectionListener(new SelectionAdapter() {			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				dialogChanged(); 				
			}			
		});
		
		
		label = new Label(composite, SWT.RIGHT);
		label.setText("State:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData(data);
		
		stateCombo = new Combo(composite,SWT.READ_ONLY);
		String[] states = {"open","closed"};
		stateCombo.setItems(states);
		data = new GridData();
		stateCombo.setLayoutData(data);
		stateCombo.addSelectionListener(new SelectionAdapter() {			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				dialogChanged(); 				
			}			
		});
		
		
		label = new Label(composite, SWT.RIGHT);
		label.setText("Description:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.BEGINNING;
		label.setLayoutData(data);
		
	    descText = new Text (composite, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		data = new GridData ();
		data.widthHint = 400;
		data.heightHint = 150;
		descText.setLayoutData(data);
		
		
		label = new Label(composite, SWT.RIGHT);
		label.setText("Urgency:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData(data);
		
		urgencyCombo = new Combo(composite,SWT.READ_ONLY);
		String[] urgencies = {"low","normal","high"};
		urgencyCombo.setItems(urgencies);
		data = new GridData();
		urgencyCombo.setLayoutData(data);
		
		label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setText("");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		label.setLayoutData(data);		
		
		return parent;
	}


	private void dialogChanged() {
		
		String tmpStr = IDText.getText().trim();
		if (tmpStr.isEmpty()) {
			updateStatus("Issue ID cannot be empty");
			return;
		}
		
		if (existingIssueData == null && existingIDs.contains(tmpStr)) {
			updateStatus("Issue ID already exists");
			return;
		}
		
		tmpStr = titleText.getText().trim();
		if (tmpStr.isEmpty()) {
			updateStatus("Title cannot be empty");
			return;
		}
		
		tmpStr = creatorText.getText().trim();
		if (tmpStr.isEmpty()) {
			updateStatus("Creator cannot be empty");
			return;
		}
		
		if (typeCombo.getText().isEmpty()) {
			updateStatus("Issue type cannot be empty");
			return;
		}
		
		if (stateCombo.getText().isEmpty()) {
			updateStatus("Issue state cannot be empty");
			return;
		}
		
		updateStatus(null);
	}
	
	
	private void updateStatus(String message) {
		setErrorMessage(message);
		getButton(IDialogConstants.OK_ID).setEnabled(message == null);
	}

	
	
	private void updateDialogFromData() {
		
		if (existingIssueData != null) {
			IDText.setText(existingIssueData.getID());
			IDText.setEditable(false);			
			
			titleText.setText(existingIssueData.getTitle());
			
			creatorText.setText(existingIssueData.getCreator());
						
			SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");			
			try {
				Date tmpDate = sdf.parse(existingIssueData.getDate());
				Calendar instance = Calendar.getInstance();
				instance.setTime(tmpDate);				
				date.setDate(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH),instance.get(Calendar.DAY_OF_MONTH));
			} catch (ParseException e) { }
			
			typeCombo.setText(existingIssueData.getType());
			stateCombo.setText(existingIssueData.getState());
			descText.setText(existingIssueData.getDescription());
			urgencyCombo.setText(existingIssueData.getUrgency());
		}
		else {
			stateCombo.setText("open");
			urgencyCombo.setText("normal");
		}
	}
	

	private void updateDataFromDialog() {
		
		issueData = new WaresHubJSONData.IssueData();				
		issueData.setID(IDText.getText().trim());
		issueData.setTitle(titleText.getText().trim());
		issueData.setCreator(creatorText.getText().trim());
		
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.DAY_OF_MONTH, date.getDay());
		instance.set(Calendar.MONTH, date.getMonth());
		instance.set(Calendar.YEAR, date.getYear());
		issueData.setDate(new SimpleDateFormat("yyyy-MM-dd").format(instance.getTime()));
		
		issueData.setType(typeCombo.getText());
		issueData.setState(stateCombo.getText());
		issueData.setDescription(descText.getText());
		issueData.setUrgency(urgencyCombo.getText());
	}
	

	
	
	
	@Override
	protected void buttonPressed(int buttonId) {

		if (buttonId == IDialogConstants.OK_ID) {
			updateDataFromDialog();
		}
		
		super.buttonPressed(buttonId);
	}


	public WaresHubJSONData.IssueData getIssueData() {		
		
		return issueData;
	}	
}
