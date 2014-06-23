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
import java.util.ArrayList;
import java.util.Iterator;


import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.lisah.openfluid.newsimulator.wizards.SimulatorScheduling.ShedulingType;




public class DataWizardPage extends WizardPage {
	

	private Table varsTable;
	private Table filesTable;	
	private Table paramsTable;
	private Table idataTable;
	private Table sdgraphTable;
	private Table eventsTable;
	
	private Button addVarButton;
	private Button addFileButton;
	private Button addParamButton;
	private Button addSDGraphButton;
	private Button addIDataButton;
	private Button addEventButton;
	private Button editVarButton;
	private Button editFileButton;
	private Button editParamButton;
	private Button editSDGraphButton;	
	private Button editIDataButton;
	private Button editEventButton;		
	private Button rmVarButton;
	private Button rmFileButton;
	private Button rmParamButton;
	private Button rmSDGraphButton;	
	private Button rmIDataButton;
	private Button rmEventButton;	

	private Button schedUndefinedButton;
	private Button schedDefaultButton;
	private Button schedFixedButton;
	private Button schedRangeButton;
	private Text schedFixedText;
	private Text schedRangeMinText;
	private Text schedRangeMaxText;
	
	private ArrayList<String> existingUnits;
	

	
	
	public DataWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("OpenFLUID simulator");
		setDescription("Data and dynamics");
		
	}


	
    
    
	private void addTabItem(TabFolder F, String title, Table T, ArrayList<String> colsTitles, 
			Button addB, Button editB, Button rmB) {
		TabItem item;
		GridData grid;
		TableColumn col;
		
		item = new TabItem (F, SWT.NONE);
		item.setText(title);		
		
		Composite C = new Composite(F,SWT.BORDER);
		GridLayout layout = new GridLayout();		
		C.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		layout.makeColumnsEqualWidth = false;
		layout.horizontalSpacing = 10;
		layout.verticalSpacing = 10;

		T.setParent(C);
		grid = new GridData(GridData.FILL_HORIZONTAL);
		grid = new GridData ();
		grid.horizontalAlignment = GridData.FILL;
		grid.verticalAlignment = GridData.FILL;
		grid.grabExcessHorizontalSpace = true;
		grid.grabExcessVerticalSpace = true;
		grid.horizontalSpan = 3;
		T.setLayoutData (grid);
		T.setHeaderVisible(true);			

		Iterator<String> it = colsTitles.iterator();

		while (it.hasNext()) {
			col = new TableColumn (T, SWT.NONE);
			col.setText ((String) it.next());				
		}
	
        for (int i=0; i<T.getColumnCount(); i++) {
  		  T.getColumn(i).pack();
  	    }			

		
		
		addB.setParent (C);
		addB.setText("Add...");
		grid = new GridData ();
		grid.horizontalAlignment = GridData.FILL;
		grid.grabExcessHorizontalSpace = true;
		addB.setLayoutData (grid);

		editB.setParent(C);
		editB.setText("Modify...");
		editB.setEnabled(false);
		grid = new GridData ();
		grid.horizontalAlignment = GridData.FILL;
		grid.grabExcessHorizontalSpace = true;
		editB.setLayoutData (grid);

		rmB.setParent(C);
		rmB.setText("Remove");
		rmB.setEnabled(false);
		grid = new GridData ();
		grid.horizontalAlignment = GridData.FILL;
		grid.grabExcessHorizontalSpace = true;
		rmB.setLayoutData (grid);
		
		
		item.setControl(C);
	}
	
	
	
	private void addSchedulingTab(TabFolder F, String title)
	{
		TabItem item;
		item = new TabItem (F, SWT.NONE);
		item.setText(title);
		
		Composite container = new Composite(F,SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 5;
		layout.verticalSpacing = 12;
		layout.makeColumnsEqualWidth = false;
		container.setLayout(layout);
		
		schedUndefinedButton = new Button(container, SWT.RADIO);		
		schedUndefinedButton.setText("Undefined scheduling ");
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 5;
		schedUndefinedButton.setLayoutData(data);
		schedUndefinedButton.setSelection(true);
		schedUndefinedButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				dialogChanged();
			}
		});

		
		schedDefaultButton = new Button(container, SWT.RADIO);		
		schedDefaultButton.setText("Scheduling uses the default DeltaT value");
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 5;
		schedDefaultButton.setLayoutData(data);
		schedDefaultButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				dialogChanged();
			}
		});

		
		schedFixedButton = new Button(container, SWT.RADIO);		
		schedFixedButton.setText("Scheduling uses a fixed DeltaT value: ");
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 1;
		schedFixedButton.setLayoutData(data);
		schedFixedButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				dialogChanged();
			}
		});


		schedFixedText = new Text(container,SWT.BORDER);
		data = new GridData();
		data.horizontalAlignment = GridData.BEGINNING;
		data.horizontalSpan = 4;
		schedFixedText.setLayoutData(data);
		schedFixedText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
				schedFixedText.setFocus();
			}
		});
		
		schedRangeButton = new Button(container, SWT.RADIO);		
		schedRangeButton.setText("Scheduling uses a DeltaT value varying from ");
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		schedRangeButton.setLayoutData(data);
		schedRangeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				dialogChanged();
			}
		});

		
		schedRangeMinText = new Text(container,SWT.BORDER);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		schedRangeMinText.setLayoutData(data);
		schedRangeMinText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
				schedRangeMinText.setFocus();
			}
		});
		
		Label toLabel = new Label(container,SWT.NONE);
		toLabel.setText("to");
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		toLabel.setLayoutData(data);
		
		schedRangeMaxText = new Text(container,SWT.BORDER);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		schedRangeMaxText.setLayoutData(data);
		schedRangeMaxText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
				schedRangeMaxText.setFocus();
			}
		});
			
		
		item.setControl(container);		
	}
	

	public void addToExistingUnits(String unit)
	{
		if (!existingUnits.contains(unit)) existingUnits.add(unit);
	}
	
	
	public void createControl(Composite parent) {
	
		GridData data;
		TabFolder dataTab;

		existingUnits = new ArrayList<String>();
		existingUnits.clear();
		
		ArrayList<String> colsTitles;
		
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();		
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		layout.makeColumnsEqualWidth = false;
		

		
		colsTitles = new ArrayList<String>();
		
		dataTab = new TabFolder (container, SWT.BORDER);

		
		
		//=============== Simulators parameters ===============  		
		
		colsTitles.clear();
		colsTitles.add("Name");
		colsTitles.add("SI unit"); 
		colsTitles.add("Description");
		
		
		paramsTable = new Table(parent, SWT.MULTI | SWT.BORDER );
		addParamButton = new Button(parent,SWT.PUSH);
		editParamButton= new Button(parent,SWT.PUSH);
		rmParamButton = new Button(parent,SWT.PUSH);		
		
		
		paramsTable.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {					
				if (paramsTable.getSelectionCount() == 1) editParamButton.setEnabled(true);
				else editParamButton.setEnabled(false);
				
				if (paramsTable.getSelectionCount() >= 1) rmParamButton.setEnabled(true);
				else rmParamButton.setEnabled(false);
					
			}
		});

		
		addTabItem(dataTab, "Parameters", paramsTable, colsTitles, addParamButton, editParamButton, rmParamButton);

		addParamButton.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
		    	SimulatorDataDialog dataDialog;
		    	dataDialog = new SimulatorDataDialog(getControl().getShell());
				if (dataDialog.open("Add parameter declaration",
						true, "", false, "", 
						false, "", 
						true, "", SimulatorDataDialog.dataConditionType.None, "", 
						true, "", existingUnits))
				{				
					TableItem item = new TableItem (paramsTable, SWT.NONE);
					item.setText (0, dataDialog.getName());
					item.setText (1, dataDialog.getSIUnit());
					item.setText (2, dataDialog.getDescription());
				}

					

			}
		});

		editParamButton.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
		    	SimulatorDataDialog dataDialog;
		    	dataDialog = new SimulatorDataDialog(getControl().getShell());
		    	
				TableItem item = paramsTable.getItem(paramsTable.getSelectionIndex()); 
					
				if (dataDialog.open("Edit parameter declaration",
						true, item.getText(0), false, "",
						false , "", 
						true, item.getText(1), SimulatorDataDialog.dataConditionType.None, "", 
						true, item.getText(2), existingUnits))
				{							
					item.setText (0, dataDialog.getName());
					item.setText (1, dataDialog.getSIUnit());
					item.setText (2, dataDialog.getDescription());
				}

					

			}
		});
		

		rmParamButton.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
				paramsTable.remove(paramsTable.getSelectionIndices());				
			}
		});

		
		
		
		
		
		//=============== Input Data ===============  		
		
		
		
		colsTitles.clear();
		colsTitles.add("Name");
		colsTitles.add("Unit class");
		colsTitles.add("SI unit");		
		colsTitles.add("Data condition"); 
		colsTitles.add("Description");
		
		idataTable = new Table(parent, SWT.MULTI | SWT.BORDER); 
		addIDataButton = new Button(parent,SWT.PUSH);
		editIDataButton= new Button(parent,SWT.PUSH);		
		rmIDataButton = new Button(parent,SWT.PUSH);		

		
		idataTable.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {					
				if (idataTable.getSelectionCount() == 1) editIDataButton.setEnabled(true);
				else editIDataButton.setEnabled(false);
				
				if (idataTable.getSelectionCount() >= 1) rmIDataButton.setEnabled(true);
				else rmIDataButton.setEnabled(false);
					
			}
		});

		
		
		addTabItem(dataTab, "Input data", idataTable, colsTitles, addIDataButton, editIDataButton, rmIDataButton);

		addIDataButton.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
		    	SimulatorDataDialog dataDialog;
		    	dataDialog = new SimulatorDataDialog(getControl().getShell());
		    	
				if (dataDialog.open("Add input data declaration",
						true, "",  false, "", 
						true, "", true, "", SimulatorDataDialog.dataConditionType.ReqUsed, "", true, "", existingUnits)) {

					TableItem item = new TableItem (idataTable, SWT.NONE);					
					item.setText (0, dataDialog.getName());
					item.setText (1, dataDialog.getUnitClass());
					item.setText (2, dataDialog.getSIUnit());
					item.setText (3, dataDialog.getDataCondition());
					item.setText (4, dataDialog.getDescription());
					addToExistingUnits(dataDialog.getUnitClass());
				}

			}
		});


		editIDataButton.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
		    	SimulatorDataDialog dataDialog;
		    	dataDialog = new SimulatorDataDialog(getControl().getShell());

				TableItem item = idataTable.getItem(idataTable.getSelectionIndex()); 		    	
		    	
				if (dataDialog.open("Edit input data declaration",
						true, item.getText(0), false, "", 
						true, item.getText(1), 
						true, item.getText(2), SimulatorDataDialog.dataConditionType.None, item.getText(3), 
						true, item.getText(4), existingUnits)) {
					
					item.setText (0, dataDialog.getName());
					item.setText (1, dataDialog.getUnitClass());
					item.setText (2, dataDialog.getSIUnit());
					item.setText (3, dataDialog.getDataCondition());
					item.setText (4, dataDialog.getDescription());
					addToExistingUnits(dataDialog.getUnitClass());
					
				}

			}
		});
		
		
		rmIDataButton.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
				idataTable.remove(idataTable.getSelectionIndices());				
			}
		});
		
		
		//=============== Variables ===============  		
		
		colsTitles.clear();
		colsTitles.add("Name");
		colsTitles.add("Type");		
		colsTitles.add("Unit class");
		colsTitles.add("SI unit");		
		colsTitles.add("Data condition");		 		
		colsTitles.add("Description");

		varsTable = new Table(parent, SWT.MULTI | SWT.BORDER);
		addVarButton = new Button(parent,SWT.PUSH);
		editVarButton = new Button(parent,SWT.PUSH);
		rmVarButton = new Button(parent,SWT.PUSH);

		
		varsTable.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {					
				if (varsTable.getSelectionCount() == 1) editVarButton.setEnabled(true);
				else editVarButton.setEnabled(false);
				
				if (varsTable.getSelectionCount() >= 1) rmVarButton.setEnabled(true);
				else rmVarButton.setEnabled(false);
					
			}
		});
		
		
		addTabItem(dataTab, "Variables", varsTable, colsTitles,addVarButton,editVarButton, rmVarButton);					

		addVarButton.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
		    	SimulatorDataDialog dataDialog;
		    	dataDialog = new SimulatorDataDialog(getControl().getShell());
				if (dataDialog.open("Add variable declaration",
						true, "", true, "", 
						true, "", 
						true, "", SimulatorDataDialog.dataConditionType.ProdUpReqUsed, "", 
						true, "", existingUnits)) {

					TableItem item = new TableItem (varsTable, SWT.NONE);					
					item.setText (0, dataDialog.getName());
					item.setText (1, dataDialog.getType());
					item.setText (2, dataDialog.getUnitClass());
					item.setText (3, dataDialog.getSIUnit());
					item.setText (4, dataDialog.getDataCondition());
					item.setText (5, dataDialog.getDescription());
					addToExistingUnits(dataDialog.getUnitClass());
				}				
				
			}
		});

		
		editVarButton.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
		    	SimulatorDataDialog dataDialog;
		    	dataDialog = new SimulatorDataDialog(getControl().getShell());

				TableItem item = varsTable.getItem(varsTable.getSelectionIndex());
				
				if (dataDialog.open("Edit variable declaration",
						true, item.getText(0), true, item.getText(1), 
						true, item.getText(2),
						true, item.getText(3), SimulatorDataDialog.dataConditionType.ProdUpReqUsed, item.getText(4), 
						true, item.getText(6), existingUnits)) {

					item.setText (0, dataDialog.getName());
					item.setText (1, dataDialog.getType());
					item.setText (2, dataDialog.getUnitClass());
					item.setText (3, dataDialog.getSIUnit());
					item.setText (4, dataDialog.getDataCondition());
					item.setText (5, dataDialog.getDescription());
					addToExistingUnits(dataDialog.getUnitClass());
				
				}
			}
		});

		
		rmVarButton.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
				varsTable.remove(varsTable.getSelectionIndices());
			}
		});
		

		//=============== Events ===============  		
		
		
		colsTitles.clear();
		colsTitles.add("Unit class");

		eventsTable = new Table(parent, SWT.MULTI | SWT.BORDER);
		addEventButton = new Button(parent,SWT.PUSH);
		editEventButton= new Button(parent,SWT.PUSH);
		rmEventButton = new Button(parent,SWT.PUSH);		

		eventsTable.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {					
				if (eventsTable.getSelectionCount() == 1) editEventButton.setEnabled(true);
				else editEventButton.setEnabled(false);
				
				if (eventsTable.getSelectionCount() >= 1) rmEventButton.setEnabled(true);
				else rmEventButton.setEnabled(false);
					
			}
		});
		
		
		addTabItem(dataTab, "Events", eventsTable, colsTitles, addEventButton, editEventButton, rmEventButton);

		addEventButton.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
		    	SimulatorDataDialog dataDialog;
		    	dataDialog = new SimulatorDataDialog(getControl().getShell());
		    	
		    	
				if (dataDialog.open("Add events class declaration",
						false, "", false, "", 
						true, "",
						false, "", SimulatorDataDialog.dataConditionType.None, "",
						false, "", existingUnits)) {

					TableItem item = new TableItem (eventsTable, SWT.NONE);					

					item.setText (0, dataDialog.getUnitClass());
					addToExistingUnits(dataDialog.getUnitClass());
					
				}

			}
		});

		editEventButton.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
		    	SimulatorDataDialog dataDialog;
		    	
		    	TableItem item 	= eventsTable.getItem(eventsTable.getSelectionIndex());	    	
		    	
		    	dataDialog = new SimulatorDataDialog(getControl().getShell());
				if (dataDialog.open("Edit events class declaration",
						false, "", false, "",
						true, item.getText(0), 
						false, "", SimulatorDataDialog.dataConditionType.None, "", 
						false, "", existingUnits)) {

					item.setText (0, dataDialog.getUnitClass());
					addToExistingUnits(dataDialog.getUnitClass());
					
				}

			}
		});

		
		rmEventButton.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
				eventsTable.remove(eventsTable.getSelectionIndices());				
			}
		});
		
		
		
		
		//=============== Files ===============  		
		
	
		
		colsTitles.clear();
		colsTitles.add("File name");
		colsTitles.add("Condition");
		
		filesTable = new Table(parent, SWT.MULTI | SWT.BORDER);
		addFileButton = new Button(parent,SWT.PUSH);
		editFileButton= new Button(parent,SWT.PUSH);
		rmFileButton = new Button(parent,SWT.PUSH);		
				
		filesTable.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {					
				if (filesTable.getSelectionCount() == 1) editFileButton.setEnabled(true);
				else editFileButton.setEnabled(false);
				
				if (filesTable.getSelectionCount() >= 1) rmFileButton.setEnabled(true);
				else rmFileButton.setEnabled(false);
					
			}
		});
		
		
		addTabItem(dataTab, "Extra files", filesTable, colsTitles, addFileButton, editFileButton, rmFileButton);

		addFileButton.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
		    	SimulatorDataDialog dataDialog;
		    	dataDialog = new SimulatorDataDialog(getControl().getShell());
		    	
				if (dataDialog.open("Add extra file declaration",
						true, "", false, "",
						false, "", 
						false, "", SimulatorDataDialog.dataConditionType.ReqUsed, "", 
						false, "", existingUnits)) {
					
					TableItem item = new TableItem (filesTable, SWT.NONE);					
					item.setText (0, dataDialog.getName());
					item.setText (1, dataDialog.getDataCondition());
					
				}

			}
		});
		
		editFileButton.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
		    	SimulatorDataDialog dataDialog;
		    	dataDialog = new SimulatorDataDialog(getControl().getShell());

		    	TableItem item 	= filesTable.getItem(filesTable.getSelectionIndex());	    			    	
		    	
				if (dataDialog.open("Edit extra file declaration",
						true, item.getText(0), false, "",
						false, "", 
						false, "", SimulatorDataDialog.dataConditionType.ReqUsed, item.getText(1),
						false, "", existingUnits)) {
					
					item.setText (0, dataDialog.getName());
					item.setText (1, dataDialog.getDataCondition());					
					
				}

			}
		});

		rmFileButton.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
				filesTable.remove(filesTable.getSelectionIndices());				
			}
		});

		
		//=============== Scheduling ===============
		
        addSchedulingTab(dataTab,"Scheduling");
		
		
		
		//=============== Spatial domain graph ===============  		
				
		
		colsTitles.clear();	
		colsTitles.add("Unit class");
		colsTitles.add("Description");
		
		sdgraphTable = new Table(parent, SWT.MULTI | SWT.BORDER); 
		addSDGraphButton = new Button(parent,SWT.PUSH);
		editSDGraphButton= new Button(parent,SWT.PUSH);		
		rmSDGraphButton = new Button(parent,SWT.PUSH);		

		
		sdgraphTable.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {					
				if (sdgraphTable.getSelectionCount() == 1) editSDGraphButton.setEnabled(true);
				else editSDGraphButton.setEnabled(false);
				
				if (sdgraphTable.getSelectionCount() >= 1) rmSDGraphButton.setEnabled(true);
				else rmSDGraphButton.setEnabled(false);
					
			}
		});

		
		
		addTabItem(dataTab, "Spatial dynamic", sdgraphTable, colsTitles, addSDGraphButton, editSDGraphButton, rmSDGraphButton);

		addSDGraphButton.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
		    	SimulatorDataDialog dataDialog;
		    	dataDialog = new SimulatorDataDialog(getControl().getShell());
		    	
				if (dataDialog.open("Add spatial dynamic declaration",
						false, "",  false, "", 
						true, "", false, "", SimulatorDataDialog.dataConditionType.None, "", true, "", existingUnits)) {

					TableItem item = new TableItem (sdgraphTable, SWT.NONE);					
					item.setText (0, dataDialog.getUnitClass());
					item.setText (1, dataDialog.getDescription());
					addToExistingUnits(dataDialog.getUnitClass());
				}

			}
		});


		editSDGraphButton.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
		    	SimulatorDataDialog dataDialog;
		    	dataDialog = new SimulatorDataDialog(getControl().getShell());

				TableItem item = sdgraphTable.getItem(sdgraphTable.getSelectionIndex()); 		    	
		    	
				if (dataDialog.open("Edit spatial dynamic declaration",
						false, "", false, "", 
						true, item.getText(0), 
						false, "", SimulatorDataDialog.dataConditionType.None, "", 
						true, item.getText(1), existingUnits)) {
					
					item.setText (0, dataDialog.getUnitClass());
					item.setText (1, dataDialog.getDescription());
					addToExistingUnits(dataDialog.getUnitClass());
					
				}

			}
		});
		
		
		rmSDGraphButton.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
				sdgraphTable.remove(sdgraphTable.getSelectionIndices());				
			}
		});

		
		
		
		dataTab.pack();
		
		data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;		
		data.horizontalSpan = 2;
		data.heightHint = 350;
		data.widthHint = 600;
		dataTab.setLayoutData(data);
						
		
		container.pack ();
		
		
		initialize();
		dialogChanged();
		setControl(container);
	}


	private void initialize() {
	}


	private void dialogChanged() {
		
		schedFixedText.setEnabled(schedFixedButton.getSelection());
		schedRangeMinText.setEnabled(schedRangeButton.getSelection());
		schedRangeMaxText.setEnabled(schedRangeButton.getSelection());
		
		
		if (schedFixedButton.getSelection()) {			
			int n = 0;
			
			try {  
			    n = Integer.parseInt(schedFixedText.getText());  
			}  
			catch(NumberFormatException nfe) {  
			    updateStatus("Fixed value for scheduling is not well formatted");
				return;  
			}
			
			if (n <= 0) {
				updateStatus("Fixed value for scheduling mustbe strictly greater than 0");
			    return;
			}
		}
		
		
		if (schedRangeButton.getSelection()) {
			int min = 0;
			int max = 0;
			
			try {  
			    min = Integer.parseInt(schedRangeMinText.getText());  
			    max = Integer.parseInt(schedRangeMaxText.getText());
			}  
			catch(NumberFormatException nfe) {  
			    updateStatus("Range values for scheduling are not well formatted");
				return;  
			}
			
			if (min <= 0 || max <= 0) {
				updateStatus("Range values for scheduling must be strictly greater than 0");
			    return;
			}
			
			if (min >= max ) {
				updateStatus("Range values for schedulling are not correctly set");
			    return;
			}
		}
		
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	
	public void fillSimulatorProperties(SimulatorProperties properties)
	{
		
		int i;
				
		for (i=0;i<paramsTable.getItemCount();i++)
		{
			SimulatorParameter param = new SimulatorParameter();
			param.name = paramsTable.getItem(i).getText(0);
			param.SIUnit = paramsTable.getItem(i).getText(1);
			param.description = paramsTable.getItem(i).getText(2);
			properties.simulatorParameters.add(param);
		}
		

		for (i=0;i<idataTable.getItemCount();i++)
		{
			SimulatorInputData idata = new SimulatorInputData();
			idata.name = idataTable.getItem(i).getText(0);
			idata.unitClass = idataTable.getItem(i).getText(1);
			idata.SIUnit = idataTable.getItem(i).getText(2);
			idata.isRequired = idataTable.getItem(i).getText(3).toUpperCase().contains("REQUIRED");
			idata.description = idataTable.getItem(i).getText(4);
			properties.simulatorInputData.add(idata);
		}
		
		
		for (i=0;i<varsTable.getItemCount();i++)
		{
			SimulatorVariable var = new SimulatorVariable();
			var.name = varsTable.getItem(i).getText(0);
			var.type = varsTable.getItem(i).getText(1);
			var.unitClass = varsTable.getItem(i).getText(2);
			var.SIUnit = varsTable.getItem(i).getText(3);
			var.isProduced = varsTable.getItem(i).getText(4).toUpperCase().contains("PRODUCED");
			var.isUpdated = varsTable.getItem(i).getText(4).toUpperCase().contains("UPDATED");			
			var.isRequired = varsTable.getItem(i).getText(4).toUpperCase().contains("REQUIRED");			
			var.description = varsTable.getItem(i).getText(6);
			properties.simulatorVariables.add(var);
		}

		
		for (i=0;i<eventsTable.getItemCount();i++)
		{
			SimulatorEvent event = new SimulatorEvent();
			event.unitClass = eventsTable.getItem(i).getText(0);
			properties.simulatorEvents.add(event);
		}
		
		for (i=0;i<filesTable.getItemCount();i++)
		{
			SimulatorExtraFile file = new SimulatorExtraFile();
			file.name = filesTable.getItem(i).getText(0);
			file.isRequired = filesTable.getItem(i).getText(1).toUpperCase().contains("REQUIRED");
			properties.simulatorExtraFiles.add(file);
		}

		
		for (i=0;i<sdgraphTable.getItemCount();i++)
		{
			SimulatorSpatialDyn sdyn = new SimulatorSpatialDyn();
            sdyn.description = sdgraphTable.getItem(i).getText(0);
			sdyn.unitClass = sdgraphTable.getItem(i).getText(1);
			properties.simulatorSpatialDyn.add(sdyn);
		}
		
		
		if (schedUndefinedButton.getSelection())
			properties.simulatorScheduling.type = ShedulingType.UNDEFINED;
		else if (schedDefaultButton.getSelection())
			properties.simulatorScheduling.type = ShedulingType.DEFAULT;
		else if (schedFixedButton.getSelection()) {			
			properties.simulatorScheduling.type = ShedulingType.FIXED;
			properties.simulatorScheduling.FixedValue = schedFixedText.getText();
		}
		else {
			properties.simulatorScheduling.type = ShedulingType.RANGE;
			properties.simulatorScheduling.MinValue = schedRangeMinText.getText();
			properties.simulatorScheduling.MaxValue = schedRangeMaxText.getText();
		}
	}	
}



