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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;





public class PrefsWizardPage extends WizardPage {
	
	private Button prefsCheck;
	
	private Table prefsTable;
	
	private Button addButton;
	private Button editButton;
	private Button rmButton;

	
	public PrefsWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("OpenFLUID builder extension");
		setDescription("Default preferences");
		
	}


	
    

	
	public void createControl(Composite parent) {
	
		Composite container = new Composite(parent, SWT.NULL);

		GridLayout layout = new GridLayout();		
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		layout.makeColumnsEqualWidth = false;
		layout.horizontalSpacing = 10;
		layout.verticalSpacing = 10;

		
		prefsCheck = new Button (container, SWT.CHECK);
		prefsCheck.setText("Extension is configurable");
		prefsCheck.setSelection(true);
		GridData grid = new GridData();
		grid.horizontalAlignment = GridData.FILL;
		grid.horizontalSpan = 3;
		grid.grabExcessHorizontalSpace = true;		
		prefsCheck.setLayoutData(grid);
		prefsCheck.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				dialogChanged();
			}
			public void widgetSelected(SelectionEvent e) {
				dialogChanged();
			}
		});
		prefsCheck.setSelection(false);

		
		prefsTable = new Table(container, SWT.MULTI | SWT.BORDER );
//		grid = new GridData(GridData.FILL_HORIZONTAL);
		grid = new GridData();
		grid.horizontalAlignment = GridData.FILL;
		grid.verticalAlignment = GridData.FILL;
		grid.grabExcessHorizontalSpace = true;
		grid.grabExcessVerticalSpace = true;
		grid.horizontalSpan = 3;
		prefsTable.setLayoutData(grid);
			
		prefsTable.setHeaderVisible(true);
		
		TableColumn col;
		col = new TableColumn (prefsTable, SWT.NONE);
		col.setText ("Label");
        col = new TableColumn (prefsTable, SWT.NONE);
		col.setText ("Parameter");				
        col = new TableColumn (prefsTable, SWT.NONE);
		col.setText ("Value");
		
		prefsTable.getColumn(0).pack();
		prefsTable.getColumn(1).pack();
		prefsTable.getColumn(2).pack();

		
		
		addButton = new Button(container,SWT.PUSH);
		addButton.setText("Add...");
		grid = new GridData ();
		grid.horizontalAlignment = GridData.FILL;
		grid.grabExcessHorizontalSpace = true;
		addButton.setLayoutData (grid);

		editButton = new Button(container,SWT.PUSH);
		editButton.setText("Modify...");
		editButton.setEnabled(false);
		grid = new GridData ();
		grid.horizontalAlignment = GridData.FILL;
		grid.grabExcessHorizontalSpace = true;
		editButton.setLayoutData (grid);

		rmButton = new Button(container,SWT.PUSH);
		rmButton.setText("Remove");
		rmButton.setEnabled(false);
		grid = new GridData ();
		grid.horizontalAlignment = GridData.FILL;
		grid.grabExcessHorizontalSpace = true;
		rmButton.setLayoutData (grid);

		
		
		addButton.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
		        PrefDialog dataDialog;
		    	dataDialog = new PrefDialog(getControl().getShell());
				if (dataDialog.open("Add parameter declaration","","",""))
				{				
					TableItem item = new TableItem (prefsTable, SWT.NONE);
					item.setText (0, dataDialog.getLabel());
					item.setText (1, dataDialog.getParam());
					item.setText (2, dataDialog.getValue());
				}				
			}
		});

		editButton.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
		    	PrefDialog dataDialog;
		    	dataDialog = new PrefDialog(getControl().getShell());
		    	
				TableItem item = prefsTable.getItem(prefsTable.getSelectionIndex()); 
					
				if (dataDialog.open("Edit parameter declaration",
						item.getText(0),item.getText(1),item.getText(2)))
				{							
					item.setText (0, dataDialog.getParam());
					item.setText (1, dataDialog.getParam());
					item.setText (2, dataDialog.getValue());
				}
			}
		});
		

		rmButton.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
				prefsTable.remove(prefsTable.getSelectionIndices());				
			}
		});
		
		
		container.pack ();
		
		
		initialize();
		dialogChanged();
		setControl(container);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
	}


	private void dialogChanged() {
		prefsTable.setEnabled(prefsCheck.getSelection());
		addButton.setEnabled(prefsCheck.getSelection());
		editButton.setEnabled(prefsCheck.getSelection());
		rmButton.setEnabled(prefsCheck.getSelection());		
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	
	public void fillBuilderextProperties(BuilderextProperties properties)
	{
		properties.extIsPrefs = prefsCheck.getSelection();
		properties.extDefaultPrefs.clear();
		
		for (int i=0;i<prefsTable.getItemCount();i++)
		{
			PrefsData prefsData = new PrefsData();
			prefsData.label = prefsTable.getItem(i).getText(0);
			prefsData.param = prefsTable.getItem(i).getText(1);
			prefsData.value = prefsTable.getItem(i).getText(2);			
			properties.extDefaultPrefs.add(prefsData); 
		}

	}	
	
}




