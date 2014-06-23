package org.lisah.openfluid.newfunc.wizards;


import java.util.ArrayList;
import java.util.Iterator;


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
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class FunctionDataDialog extends Dialog {


	private Text nameText = null;
	private Combo unitClassCombo = null;
	private Text valueUnitText = null;
	private Text descText = null;
	private Combo dataCondCombo = null;
	private Combo timeCondCombo = null;
	
	private Boolean returnIsOK; 

	private String nameStr;
	private String unitClassStr;
	private String valueUnitStr;
	private String descStr;
	private String dataCondStr;
	private String timeCondStr;
	
	public enum dataConditionType {None, ReqUsed, ProdUpReqUsed};
	
	public FunctionDataDialog(Shell parent) {

		//super(parent);
		this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

	}


	public FunctionDataDialog(Shell parent, int style) {
		super(parent, style);
	}


	public Boolean open(String Title, 
			Boolean isDataName, String dataNameValue,
			Boolean isDataUnitClass, String dataUnitClassValue,
			Boolean isDataValueUnit, String dataValueUnitValue,
			dataConditionType isDataCondition, String dataConditionValue,
			Boolean isTimeCondition, String timeConditionValue,
			Boolean isDataDescription, String dataDescriptionValue,  ArrayList<String> existingUnits) {

		returnIsOK = false;
		nameStr = "";
		unitClassStr = "";
		valueUnitStr = "";
		dataCondStr = "";
		timeCondStr = "";
		descStr = "";

		Shell shell = new Shell(getParent(), getStyle());
		shell.setText(Title);		    
		createContents(shell, 
				isDataName, dataNameValue,
				isDataUnitClass, dataUnitClassValue,
				isDataValueUnit, dataValueUnitValue,
				isDataCondition, dataConditionValue,
				isTimeCondition, timeConditionValue,
				isDataDescription, dataDescriptionValue, existingUnits);
		shell.pack();
		shell.open();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		return returnIsOK;
	}


	private void createContents(final Shell shell, 
			Boolean isDataName, String dataNameValue,
			Boolean isDataUnitClass, String dataUnitClassValue,
			Boolean isDataValueUnit, String dataValueUnitValue,
			dataConditionType isDataCondition, String dataConditionValue,
			final Boolean isTimeCondition, String timeConditionValue,
			Boolean isDataDescription, String dataDescriptionValue, ArrayList<String> existingUnits) {


		GridData data;
		Label label;

		GridLayout layout = new GridLayout();		
		shell.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		layout.makeColumnsEqualWidth = false;

		if (isDataName) {
			label = new Label(shell, SWT.RIGHT);
			label.setText("Name:");
			data = new GridData ();
			data.horizontalAlignment = GridData.FILL;
			label.setLayoutData(data);

			nameText = new Text (shell, SWT.BORDER);
			nameText.setText(dataNameValue);
			data = new GridData ();
			data.horizontalAlignment = GridData.FILL;
			nameText.setLayoutData(data);       


		}

		if (isDataUnitClass) {
			label = new Label(shell, SWT.RIGHT);
			label.setText("Unit class:");
			data = new GridData ();
			data.horizontalAlignment = GridData.FILL;
			label.setLayoutData(data);

			unitClassCombo = new Combo (shell, SWT.NONE);
			unitClassCombo.setText(dataUnitClassValue);
			Iterator<String> i = existingUnits.iterator();
	        while (i.hasNext()) {
	            unitClassCombo.add(i.next());
	        }
			data = new GridData ();
			data.horizontalAlignment = GridData.FILL;
			unitClassCombo.setLayoutData(data);        	
		}

		if (isDataValueUnit) {
			label = new Label(shell, SWT.RIGHT);
			label.setText("Data unit:");
			data = new GridData ();
			data.horizontalAlignment = GridData.FILL;
			label.setLayoutData(data);

			valueUnitText = new Text (shell, SWT.BORDER);
			valueUnitText.setText(dataValueUnitValue);
			data = new GridData ();
			data.horizontalAlignment = GridData.FILL;
			valueUnitText.setLayoutData(data);
		}

		if (isDataCondition != dataConditionType.None) {
			label = new Label(shell, SWT.RIGHT);
			label.setText("Data condition:");
			data = new GridData ();
			data.horizontalAlignment = GridData.FILL;
			label.setLayoutData(data);        	

			dataCondCombo = new Combo (shell, SWT.READ_ONLY);
			if (isDataCondition == dataConditionType.ReqUsed ) {
				dataCondCombo.setItems (new String [] {"Required", "Used if present"});
			} else {
				dataCondCombo.setItems (new String [] {"Produced","Updated","Required", "Used if present"});
			}				
			dataCondCombo.select(0);
			if (dataConditionValue != "") dataCondCombo.select(dataCondCombo.indexOf(dataConditionValue));
			data = new GridData ();
			data.horizontalAlignment = GridData.FILL;    		
			dataCondCombo.setLayoutData (data);
		}

		if (isTimeCondition) {

			label = new Label(shell, SWT.RIGHT);
			label.setText("Time condition:");
			data = new GridData ();
			data.horizontalAlignment = GridData.FILL;
			label.setLayoutData(data);        	

			timeCondCombo = new Combo (shell, SWT.READ_ONLY);
			timeCondCombo.setItems (new String [] {"On same step", "On a previous step"});
			timeCondCombo.select(0);
			if (timeConditionValue != "") timeCondCombo.select(timeCondCombo.indexOf(timeConditionValue));
			data = new GridData ();
			data.horizontalAlignment = GridData.FILL;
			timeCondCombo.setLayoutData (data);
		}

		if (isDataDescription) {
			label = new Label(shell, SWT.RIGHT);
			label.setText("Description:");
			data = new GridData ();
			data.horizontalAlignment = GridData.FILL;
			label.setLayoutData(data);

			descText = new Text (shell, SWT.BORDER);
			descText.setText(dataDescriptionValue);
			data = new GridData ();
			data.horizontalAlignment = GridData.FILL;
			descText.setLayoutData(data);        	
		}


		if (isDataCondition != dataConditionType.None) {
			dataCondCombo.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {				
					if (isTimeCondition) {
						if (dataCondCombo.getText().toUpperCase().contains("PRODUCED") ||
								dataCondCombo.getText().toUpperCase().contains("UPDATED")) {
							timeCondCombo.setEnabled(false);
						} else {
							timeCondCombo.setEnabled(true);
						}
					}

				}
			});

			if (isTimeCondition) {
				if (dataCondCombo.getText().toUpperCase().contains("PRODUCED") ||
						dataCondCombo.getText().toUpperCase().contains("UPDATED")) {
					timeCondCombo.setEnabled(false);
				}
			}

		}		
		
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
				returnIsOK = false;
				shell.close();
			}
		});


		final Button OKButton = new Button(buttonsContainer, SWT.PUSH | SWT.CENTER);
		OKButton.setText("OK");
		data = new GridData ();
		data.widthHint = 100;
		OKButton.setLayoutData(data);
		OKButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {										
				returnIsOK = true;
				if (nameText != null) nameStr = nameText.getText();
				if (unitClassCombo != null) unitClassStr = unitClassCombo.getText();
				if (valueUnitText != null) valueUnitStr = valueUnitText.getText();
				if (dataCondCombo != null) dataCondStr = dataCondCombo.getText();
				if (timeCondCombo != null) timeCondStr = timeCondCombo.getText();
				if (descText != null) descStr = descText.getText();
				shell.close();
			}
		});


		if (isDataName && !isDataUnitClass ) {
			nameText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					OKButton.setEnabled(nameText.getText().length() != 0);
				}
			});
			
			OKButton.setEnabled(nameText.getText().length() != 0);
		}

		if (!isDataName && isDataUnitClass ) {
			unitClassCombo.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					OKButton.setEnabled(unitClassCombo.getText().length() != 0);
				}
			});
			
			OKButton.setEnabled(unitClassCombo.getText().length() != 0);
		}
			

		if (isDataName && isDataUnitClass ) {
			nameText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					OKButton.setEnabled(nameText.getText().length() != 0 && unitClassCombo.getText().length() != 0);
				}
			});
			
			unitClassCombo.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					OKButton.setEnabled(nameText.getText().length() != 0 && unitClassCombo.getText().length() != 0);
				}
			});

			OKButton.setEnabled(nameText.getText().length() != 0 && unitClassCombo.getText().length() != 0);			
			
		}

		OKButton.update();

	}

	
	public String getName() {
		return nameStr;		
	}

	public String getUnitClass() {
		return unitClassStr;		
	}

	public String getValueUnit() {
		return valueUnitStr;		
	}

	public String getDescription() {
		return descStr;		
	}

	public String getDataCondition() {
		return dataCondStr;		
	}

	public String getTimeCondition() {
		return timeCondStr;	
	}
	
}

