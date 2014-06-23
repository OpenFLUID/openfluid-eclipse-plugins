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
import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.ISelection;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;



public class InfosWizardPage extends WizardPage {
	
	private Text extDescText;
	private Text extIDText;
	private Text extShortNameText;
	private Text extNameText;
	private Text extVersionText;
	private Combo extTypeCombo;	
	private Text extAuthorsText;
	private Text extEmailsText;


	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public InfosWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("OpenFLUID builder extension");
		setDescription("Extension ID and meta-information");
		
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
		
		
		// extension ID		
		label = new Label(container, SWT.RIGHT);
		label.setText("Extension ID:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);
				
		extIDText = new Text(container, SWT.BORDER | SWT.SINGLE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		extIDText.setLayoutData (data);
		extIDText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		
		label = new Label(container, SWT.RIGHT);
		label.setText("&Extension type:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;		
		label.setLayoutData (data);				
		
		extTypeCombo = new Combo(container, SWT.READ_ONLY);
//		extTypeCombo.add("HomeLauncher");
		extTypeCombo.add("EventsImporter");
		extTypeCombo.add("ExtraImporter");
		extTypeCombo.add("InputdataImporter");
		extTypeCombo.add("SpatialgraphImporter");
		extTypeCombo.add("MixedImporter");
		extTypeCombo.add("WorkspaceTab");
		extTypeCombo.add("SimulationListener");		
		extTypeCombo.add("ModalWindow");
		extTypeCombo.add("ModelessWindow");		

		
		extTypeCombo.select(0);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		extTypeCombo.setLayoutData(data);

		
		// extension shortname		
		label = new Label(container, SWT.RIGHT);
		label.setText("Extension short name:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);
		
		extShortNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		extShortNameText.setLayoutData (data);
		extShortNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		// extension name		
		label = new Label(container, SWT.RIGHT);
		label.setText("Extension name:");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);

		extNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		//data = new GridData(GridData.FILL_HORIZONTAL);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;		
		data.horizontalSpan = 2;
		extNameText.setLayoutData (data);
		
		// extension version
		label = new Label(container, SWT.RIGHT);
		label.setText("Extension version:");
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
			
		extVersionText = new Text(container, SWT.BORDER | SWT.SINGLE);
		extVersionText.setText(dateFormatter.format(currentDate.getTime()));
		//data = new GridData(GridData.FILL_HORIZONTAL);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;		
		data.horizontalSpan = 2;
		extVersionText.setLayoutData (data);		

		
		
		
		// extension description		
		label = new Label(container, SWT.TOP | SWT.RIGHT);
		label.setText("Description:");
		data = new GridData ();
		data.verticalAlignment = GridData.BEGINNING;		
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);
		
		extDescText = new Text(container, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL );
		//data = new GridData(GridData.FILL_HORIZONTAL);
		data = new GridData ();
		data.heightHint = 100;
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		extDescText.setLayoutData (data);
		
		// extension authors		
		label = new Label(container, SWT.RIGHT);
		label.setText("Author(s):");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);
		
		extAuthorsText = new Text(container, SWT.BORDER | SWT.SINGLE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		extAuthorsText.setLayoutData (data);

		// extension authors 's email
		label = new Label(container, SWT.RIGHT);
		label.setText("Author(s)'s email(s):");
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData (data);
		
		extEmailsText = new Text(container, SWT.BORDER | SWT.SINGLE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		extEmailsText.setLayoutData (data);
		
		
		
		
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
					extIDText.setFocus();
				}
			});

		}
	}

	

	private void dialogChanged() {
		if (extIDText.getText().length() == 0) {
			updateStatus("Extension ID must be specified");
			return;
		}
		
		if (extShortNameText.getText().length() == 0) {
			updateStatus("Extension short name must be specified");
			return;
		}

		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}


	public void fillBuilderextProperties(BuilderextProperties properties)
	{
        properties.extID = extIDText.getText();
        properties.extType = extTypeCombo.getText();
        properties.extName = extNameText.getText();
        properties.extShortName = extShortNameText.getText();
        properties.extDescription = extDescText.getText();
        properties.extAuthor = extAuthorsText.getText();
        properties.extAuthorEmail = extEmailsText.getText();
	}	
	
	
}