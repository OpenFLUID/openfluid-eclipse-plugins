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


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class PrefDialog extends Dialog {


	private Text labelText = null;
	private Text paramText = null;
	private Text valueText = null;
	
	private String paramValue;
	private String valueValue;
	private String labelValue;
	
	private Boolean returnIsOK; 

	
	public PrefDialog(Shell parent) {

		//super(parent);
		this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

	}


	public PrefDialog(Shell parent, int style) {
		super(parent, style);
	}


	public Boolean open(String Title, 
			String labelStr, String paramStr, String valueStr) {

		returnIsOK = false;
		labelValue = "";
		valueValue = "";
		paramValue = "";
		
		Shell shell = new Shell(getParent(), getStyle());
		shell.setText(Title);		    
		createContents(shell, 
				labelStr,paramStr, valueStr);
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
			String labelStr, String paramStr, String valueStr) {


		GridData data;
		Label label;

		GridLayout layout = new GridLayout();		
		shell.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		layout.makeColumnsEqualWidth = false;

		label = new Label(shell, SWT.RIGHT);
		label.setText("Label:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData(data);

		labelText = new Text (shell, SWT.BORDER);
		labelText.setText(labelStr);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		labelText.setLayoutData(data);       
		
		
		label = new Label(shell, SWT.RIGHT);
		label.setText("Parameter:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData(data);

		paramText = new Text (shell, SWT.BORDER);
		paramText.setText(paramStr);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		paramText.setLayoutData(data);       
		
		label = new Label(shell, SWT.RIGHT);
		label.setText("Value:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData(data);

		valueText = new Text (shell, SWT.BORDER);
		valueText.setText(valueStr);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		valueText.setLayoutData(data);       

		
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
				labelValue = labelText.getText();
				valueValue = valueText.getText();
				paramValue = paramText.getText();				
				shell.close();
			}
		});


		paramText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				OKButton.setEnabled(paramText.getText().length() != 0 && valueText.getText().length() != 0 && labelText.getText().length() != 0);
			}
		});
		
		valueText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				OKButton.setEnabled(paramText.getText().length() != 0 && valueText.getText().length() != 0 && labelText.getText().length() != 0);
			}
		});
		
		OKButton.update();
		

	}


	public String getLabel() {
		return labelValue;		
	}
	
	
	public String getParam() {
		return paramValue;		
	}

	
	public String getValue() {
		return valueValue;		
	}
	
	
}

