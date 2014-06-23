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
	private Combo typeCombo = null;
	private Combo unitClassCombo = null;
	private Text SIUnitText = null;
	private Text descText = null;
	private Combo dataCondCombo = null;
	private Combo timeCondCombo = null;
	
	private Boolean returnIsOK; 

	private String nameStr;
	private String typeStr;
	private String unitClassStr;
	private String SIUnitStr;
	private String descStr;
	private String dataCondStr;
	private String timeCondStr;
	
	public enum dataConditionType {None, ReqUsed, ProdUpReqUsed};
	
	 public final static ArrayList<String> valueTypes = new ArrayList<String>();
     static {
       valueTypes.add("");
       valueTypes.add("[boolean]");
       valueTypes.add("[double]");
       valueTypes.add("[integer]");
       valueTypes.add("[map]");
       valueTypes.add("[matrix]");
       valueTypes.add("[vector]");
	 }
	
	public FunctionDataDialog(Shell parent) {

		//super(parent);
		this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

	}


	public FunctionDataDialog(Shell parent, int style) {
		super(parent, style);
	}


	public Boolean open(String Title, 
			Boolean isDataName, String dataNameValue,
			Boolean isDataType, String dataTypeValue,
			Boolean isDataUnitClass, String dataUnitClassValue,
			Boolean isDataSIUnit, String dataSIUnitValue,
			dataConditionType isDataCondition, String dataConditionValue,
			Boolean isTimeCondition, String timeConditionValue,
			Boolean isDataDescription, String dataDescriptionValue,  ArrayList<String> existingUnits) {

		returnIsOK = false;
		nameStr = "";
		typeStr = "";
		unitClassStr = "";
		SIUnitStr = "";
		dataCondStr = "";
		timeCondStr = "";
		descStr = "";

		Shell shell = new Shell(getParent(), getStyle());
		shell.setText(Title);		    
		createContents(shell, 
				isDataName, dataNameValue,
				isDataType, dataTypeValue,				
				isDataUnitClass, dataUnitClassValue,
				isDataSIUnit, dataSIUnitValue,
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
			Boolean isDataType, String dataTypeValue,			
			Boolean isDataUnitClass, String dataUnitClassValue,
			Boolean isDataSIUnit, String dataSIUnitValue,
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

		if (isDataType) {
			label = new Label(shell, SWT.RIGHT);
			label.setText("Type:");
			data = new GridData ();
			data.horizontalAlignment = GridData.FILL;
			label.setLayoutData(data);

			typeCombo = new Combo (shell, SWT.NONE);			
			Iterator<String> i = valueTypes.iterator();
	        while (i.hasNext()) {
	            typeCombo.add(i.next());
	        }
	        typeCombo.setText(dataTypeValue);	        
			data = new GridData ();
			data.horizontalAlignment = GridData.FILL;
			typeCombo.setLayoutData(data);       
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

		if (isDataSIUnit) {
			label = new Label(shell, SWT.RIGHT);
			label.setText("SI unit:");
			data = new GridData ();
			data.horizontalAlignment = GridData.FILL;
			label.setLayoutData(data);

			SIUnitText = new Text (shell, SWT.BORDER);
			SIUnitText.setText(dataSIUnitValue);
			data = new GridData ();
			data.horizontalAlignment = GridData.FILL;
			SIUnitText.setLayoutData(data);
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
				if (typeCombo != null) typeStr = typeCombo.getText();				
				if (unitClassCombo != null) unitClassStr = unitClassCombo.getText();
				if (SIUnitText != null) SIUnitStr = SIUnitText.getText();
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

	public String getType() {
		return typeStr;		
	}
	
	
	public String getUnitClass() {
		return unitClassStr;		
	}

	public String getSIUnit() {
		return SIUnitStr;		
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

