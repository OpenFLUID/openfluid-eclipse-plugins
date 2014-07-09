package org.lisah.openfluid.wareshubjson.editors;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class IssueDialog extends Dialog {
	
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
	

	public IssueDialog(Shell parent, int style) {
		super(parent, style);

	}

	
	public IssueDialog(Shell parent) {
		this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

	}
	
	
	
	public void open(final WaresHubJSONData.IssueData data, ArrayList<String> IDs) {

        this.existingIssueData = data;
        this.existingIDs = IDs;
		
		Shell shell = new Shell(getParent(), getStyle());
		shell.setText("Edit issue");		    
		createContents(shell);
		shell.pack();
		shell.open();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
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
		//issueData.setDate(String.valueOf(date.getYear())+"-"+String.valueOf(date.getMonth())+"-"+String.valueOf(date.getDay()));
		
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.DAY_OF_MONTH, date.getDay());
		instance.set(Calendar.MONTH, date.getMonth());
		instance.set(Calendar.YEAR, date.getYear());
		issueData.setDate(new SimpleDateFormat("yyyy-MM-dd").format(instance.getTime()));
		
		issueData.setType(typeCombo.getText());
		issueData.setState(stateCombo.getText());
		//issueData.setDescription(descText.getText().replace("\n","\\n").replace("\"","\\\""));
		issueData.setDescription(descText.getText());
		issueData.setUrgency(urgencyCombo.getText());
	}
	
	
	private void createContents(final Shell shell) {

		GridData data;
		Label label;

		GridLayout layout = new GridLayout();		
		shell.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		layout.makeColumnsEqualWidth = false;

		label = new Label(shell, SWT.RIGHT);
		label.setText("ID*:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData(data);
		
		IDText = new Text (shell, SWT.BORDER);
		data = new GridData ();
		data.minimumWidth = 100;
		IDText.setLayoutData(data);
		IDText.addModifyListener(new ModifyListener() {		
			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged();				
			}
		});
		
		
		label = new Label(shell, SWT.RIGHT);
		label.setText("Title*:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData(data);
		
	    titleText = new Text (shell, SWT.BORDER);
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
		
		
		label = new Label(shell, SWT.RIGHT);
		label.setText("Creator*:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData(data);
		
	    creatorText = new Text (shell, SWT.BORDER);
		data = new GridData ();
		data.widthHint = 100;
		creatorText.setLayoutData(data);
		creatorText.addModifyListener(new ModifyListener() {		
			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged();				
			}
		});
		
		label = new Label(shell, SWT.RIGHT);
		label.setText("Date*:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.BEGINNING;
		label.setLayoutData(data);
		
		date = new DateTime (shell, SWT.CALENDAR);
		data = new GridData ();
		date.setLayoutData(data);

		
		label = new Label(shell, SWT.RIGHT);
		label.setText("Type*:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData(data);
		
		typeCombo = new Combo(shell,SWT.READ_ONLY);
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
		
		
		label = new Label(shell, SWT.RIGHT);
		label.setText("State*:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData(data);
		
		stateCombo = new Combo(shell,SWT.READ_ONLY);
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
		
		
		label = new Label(shell, SWT.RIGHT);
		label.setText("Description:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.BEGINNING;
		label.setLayoutData(data);
		
	    descText = new Text (shell, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		data = new GridData ();
		data.widthHint = 300;
		data.heightHint = 100;
		descText.setLayoutData(data);
		
		
		label = new Label(shell, SWT.RIGHT);
		label.setText("Urgency:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData(data);
		
		urgencyCombo = new Combo(shell,SWT.READ_ONLY);
		String[] urgencies = {"low","normal","high"};
		urgencyCombo.setItems(urgencies);
		data = new GridData();
		urgencyCombo.setLayoutData(data);
		
		label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setText("");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		label.setLayoutData(data);		
		
		Composite buttonsContainer = new Composite(shell, SWT.NULL);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		data.verticalIndent = 15;		
		buttonsContainer.setLayoutData(data);

		GridLayout buttonsLayout = new GridLayout();		
		buttonsContainer.setLayout(buttonsLayout);
		buttonsLayout.numColumns = 2;
		buttonsLayout.verticalSpacing = 20;
		buttonsLayout.horizontalSpacing = 20;		
		buttonsLayout.makeColumnsEqualWidth = true;
		
		Button cancelButton = new Button(buttonsContainer, SWT.PUSH | SWT.CENTER);
		cancelButton.setText("Cancel");
		data = new GridData ();
		data.widthHint = 100;
		data.horizontalAlignment = GridData.END;
		data.grabExcessHorizontalSpace = true;		
		cancelButton.setLayoutData(data);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {				
				shell.close();
			}
		});


		OKButton = new Button(buttonsContainer, SWT.PUSH | SWT.CENTER);
		OKButton.setText("OK");
		data = new GridData ();
		data.widthHint = 100;
		OKButton.setLayoutData(data);
		OKButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {													
				updateDataFromDialog();
				shell.close();
			}
		});
		
		updateDialogFromData();
		
		dialogChanged();
		
		shell.pack();
	}
	
	
	private void dialogChanged() {
		
		String tmpStr = IDText.getText().trim();
		if (tmpStr.isEmpty()) {
			OKButton.setEnabled(false);
			return;
		}
		
		if (existingIssueData == null && existingIDs.contains(tmpStr)) {
			OKButton.setEnabled(false);
			return;
		}
		
		tmpStr = titleText.getText().trim();
		if (tmpStr.isEmpty()) {
			OKButton.setEnabled(false);
			return;
		}
		
		tmpStr = creatorText.getText().trim();
		if (tmpStr.isEmpty()) {
			OKButton.setEnabled(false);
			return;
		}
		
		if (typeCombo.getText().isEmpty()) {
			OKButton.setEnabled(false);
			return;
		}
		
		if (stateCombo.getText().isEmpty()) {
			OKButton.setEnabled(false);
			return;
		}
		
		OKButton.setEnabled(true);
	}
	
	public WaresHubJSONData.IssueData getIssueData() {
		return issueData;
	}

}
