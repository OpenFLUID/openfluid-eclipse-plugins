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
import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.ISelection;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;



public class InfosWizardPage extends WizardPage {
	
	private Text funcDescText;
	private Text funcIDText;
	private Text funcNameText;
	private Text funcVersionText;
	private Text funcDomainText;	
	private Text funcAuthorsText;
	private Text funcEmailsText;


	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public InfosWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("OpenFLUID simulation function");
		setDescription("Function ID and meta-information");
		
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
	
		GridData data;
		Label label;
		
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();		
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		layout.makeColumnsEqualWidth = false;
		
		
		// function ID		
		label = new Label(container, SWT.RIGHT);
		label.setText("Function ID:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);
		
		
		funcIDText = new Text(container, SWT.BORDER | SWT.SINGLE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		funcIDText.setLayoutData (data);
		funcIDText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		// function name		
		label = new Label(container, SWT.RIGHT);
		label.setText("Function Name:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);

		funcNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		//data = new GridData(GridData.FILL_HORIZONTAL);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;		
		data.horizontalSpan = 2;
		funcNameText.setLayoutData (data);
		
		// function version
		label = new Label(container, SWT.RIGHT);
		label.setText("Function version:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);

		// determine the version number to propose 
		java.util.Date currentDate = new java.util.Date();
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.setTime(currentDate);
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yy.MM");
		if ((calendar.get(java.util.Calendar.YEAR) % 100 < 10)) {
			dateFormatter = new SimpleDateFormat("y.MM");
		} 
		else {
			dateFormatter = new SimpleDateFormat("yy.MM");
		}			
			
		funcVersionText = new Text(container, SWT.BORDER | SWT.SINGLE);
		funcVersionText.setText(dateFormatter.format(currentDate.getTime()));
		//data = new GridData(GridData.FILL_HORIZONTAL);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;		
		data.horizontalSpan = 2;
		funcVersionText.setLayoutData (data);		

		// function domain		
		label = new Label(container, SWT.RIGHT);
		label.setText("Function Domain:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);

		funcDomainText = new Text(container, SWT.BORDER | SWT.SINGLE);
		//data = new GridData(GridData.FILL_HORIZONTAL);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;		
		data.horizontalSpan = 2;
		funcDomainText.setLayoutData (data);
		
		
		
		// function description		
		label = new Label(container, SWT.TOP | SWT.RIGHT);
		label.setText("Description:");
		data = new GridData ();
		data.verticalAlignment = GridData.BEGINNING;		
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);
		
		funcDescText = new Text(container, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL );
		//data = new GridData(GridData.FILL_HORIZONTAL);
		data = new GridData ();
		data.heightHint = 100;
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		funcDescText.setLayoutData (data);
		
		// function authors		
		label = new Label(container, SWT.RIGHT);
		label.setText("Author(s):");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);
		
		funcAuthorsText = new Text(container, SWT.BORDER | SWT.SINGLE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		funcAuthorsText.setLayoutData (data);

		// function authors 's email
		label = new Label(container, SWT.RIGHT);
		label.setText("Author(s)'s email(s):");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);
		
		funcEmailsText = new Text(container, SWT.BORDER | SWT.SINGLE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		funcEmailsText.setLayoutData (data);
		
		
		
		
		container.pack ();
		
		
		dialogChanged();
		setControl(container);

		
	}

	
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		
		// Set the initial focus
		if (visible) {
			getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					funcIDText.setFocus();
				}
			});

		}
	}

	

	private void dialogChanged() {
		if (getFuncID().length() == 0) {
			updateStatus("Function ID must be specified");
			return;
		}

		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	
	public String getFuncID() {
		return funcIDText.getText();
	}	
	
	public String getFuncName() {
		return funcNameText.getText();
	}	

	public String getFuncVersion() {
		return funcVersionText.getText();
	}		
	
	public String getFuncDomain() {
		return funcDomainText.getText();
	}	
	
	
	public String getFuncDesc() {
		return funcDescText.getText();
	}		
	
	public String getFuncAuthors() {
		return funcAuthorsText.getText();
	}	
	
	public String getFuncEmails() {
		return funcEmailsText.getText();
	}	
	
	public void fillFunctionProperties(FunctionProperties properties)
	{
		properties.functionID = funcIDText.getText();
		properties.functionName = funcNameText.getText();
		properties.functionVersion = funcVersionText.getText();
		properties.functionDomain = funcDomainText.getText();
		properties.functionDescription = funcDescText.getText();
		properties.functionAuthor = funcAuthorsText.getText();
		properties.functionAuthorEmail = funcEmailsText.getText();
	}	
	
	
}