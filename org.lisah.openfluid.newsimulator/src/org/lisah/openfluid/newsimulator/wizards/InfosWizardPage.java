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
	
	private Text simDescText;
	private Text simIDText;
	private Text simNameText;
	private Text simVersionText;
	private Text simDomainText;	
	private Text simAuthorText;
	private Text simEmailText;


	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public InfosWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("OpenFLUID simulator");
		setDescription("Simulator ID and meta-information");
		
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
		
		
		// simulator ID		
		label = new Label(container, SWT.RIGHT);
		label.setText("Simulator ID:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);
		
		
		simIDText = new Text(container, SWT.BORDER | SWT.SINGLE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		simIDText.setLayoutData (data);
		simIDText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		// simulator name		
		label = new Label(container, SWT.RIGHT);
		label.setText("Simulator name:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);

		simNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		//data = new GridData(GridData.FILL_HORIZONTAL);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;		
		data.horizontalSpan = 2;
		simNameText.setLayoutData (data);
		
		// simulator version
		label = new Label(container, SWT.RIGHT);
		label.setText("Simulator version:");
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
			
		simVersionText = new Text(container, SWT.BORDER | SWT.SINGLE);
		simVersionText.setText(dateFormatter.format(currentDate.getTime()));
		//data = new GridData(GridData.FILL_HORIZONTAL);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;		
		data.horizontalSpan = 2;
		simVersionText.setLayoutData (data);		

		// simulator domain		
		label = new Label(container, SWT.RIGHT);
		label.setText("Simulator Domain:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);

		simDomainText = new Text(container, SWT.BORDER | SWT.SINGLE);
		//data = new GridData(GridData.FILL_HORIZONTAL);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;		
		data.horizontalSpan = 2;
		simDomainText.setLayoutData (data);
		
		
		
		// simulator description		
		label = new Label(container, SWT.TOP | SWT.RIGHT);
		label.setText("Description:");
		data = new GridData ();
		data.verticalAlignment = GridData.BEGINNING;		
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);
		
		simDescText = new Text(container, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL );
		//data = new GridData(GridData.FILL_HORIZONTAL);
		data = new GridData ();
		data.heightHint = 100;
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		simDescText.setLayoutData (data);
		
		// simulator author's name		
		label = new Label(container, SWT.RIGHT);
		label.setText("Author's name:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);
		
		simAuthorText = new Text(container, SWT.BORDER | SWT.SINGLE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		simAuthorText.setLayoutData (data);

		// simulator author's email
		label = new Label(container, SWT.RIGHT);
		label.setText("Author's email(s):");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);
		
		simEmailText = new Text(container, SWT.BORDER | SWT.SINGLE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		simEmailText.setLayoutData (data);
		
		
		
		
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
					simIDText.setFocus();
				}
			});

		}
	}

	

	private void dialogChanged() {
		if (getSimID().length() == 0) {
			updateStatus("Simulator ID must be specified");
			return;
		}

		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	
	public String getSimID() {
		return simIDText.getText();
	}	
	
	public String getSimName() {
		return simNameText.getText();
	}	

	public String getSimVersion() {
		return simVersionText.getText();
	}		
	
	public String getSimDomain() {
		return simDomainText.getText();
	}	
	
	
	public String getSimDesc() {
		return simDescText.getText();
	}		
	
	public String getSimAuthor() {
		return simAuthorText.getText();
	}	
	
	public String getSimEmail() {
		return simEmailText.getText();
	}	
	
	public void fillSimulatorProperties(SimulatorProperties properties)
	{
		properties.simulatorID = simIDText.getText();
		properties.simulatorName = simNameText.getText();
		properties.simulatorVersion = simVersionText.getText();
		properties.simulatorDomain = simDomainText.getText();
		properties.simulatorDescription = simDescText.getText();
		properties.simulatorAuthor = simAuthorText.getText();
		properties.simulatorAuthorEmail = simEmailText.getText();
	}	
	
	
}