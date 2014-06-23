package org.lisah.openfluid.newfunc.wizards;
import java.util.ArrayList;
import java.util.Iterator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;





public class DataWizardPage extends WizardPage {
	

	private Table varsTable;
	private Table filesTable;	
	private Table paramsTable;
	private Table idataTable;
	private Table eventsTable;
	
	private Button addVarButton;
	private Button addFileButton;
	private Button addParamButton;
	private Button addIDataButton;
	private Button addEventButton;
	private Button editVarButton;
	private Button editFileButton;
	private Button editParamButton;
	private Button editIDataButton;
	private Button editEventButton;		
	private Button rmVarButton;
	private Button rmFileButton;
	private Button rmParamButton;
	private Button rmIDataButton;
	private Button rmEventButton;	

	private ArrayList<String> existingUnits;
	

	
	
	public DataWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("OpenFLUID simulation function");
		setDescription("Handled data");
		
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
	
	

	public void addToExistingUnits(String unit)
	{
		if (!existingUnits.contains(unit)) existingUnits.add(unit);
	}
	
	
	public void createControl(Composite parent) {
	
		GridData data;
		
//		Label label;

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

		
		
		//=============== Function parameters ==============shell.setSize(width, height)=  		
		
		colsTitles.clear();
		colsTitles.add("Name");
		colsTitles.add("Value unit"); 
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
		    	FunctionDataDialog dataDialog;
		    	dataDialog = new FunctionDataDialog(getControl().getShell());
				if (dataDialog.open("Add parameter declaration",
						true, "", false, "", 
						true, "", FunctionDataDialog.dataConditionType.None, "", 
						false, "", true, "", existingUnits))
				{				
					TableItem item = new TableItem (paramsTable, SWT.NONE);
					item.setText (0, dataDialog.getName());
					item.setText (1, dataDialog.getValueUnit());
					item.setText (2, dataDialog.getDescription());
				}

					

			}
		});

		editParamButton.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
		    	FunctionDataDialog dataDialog;
		    	dataDialog = new FunctionDataDialog(getControl().getShell());
		    	
				TableItem item = paramsTable.getItem(paramsTable.getSelectionIndex()); 
					
				if (dataDialog.open("Edit parameter declaration",
						true, item.getText(0),false , "", 
						true, item.getText(1), FunctionDataDialog.dataConditionType.None, "", 
						false, "", true, item.getText(2), existingUnits))
				{							
					item.setText (0, dataDialog.getName());
					item.setText (1, dataDialog.getValueUnit());
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
		colsTitles.add("Value unit");		
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
		    	FunctionDataDialog dataDialog;
		    	dataDialog = new FunctionDataDialog(getControl().getShell());
		    	
				if (dataDialog.open("Add input data declaration",
						true, "", true, "", true, "", FunctionDataDialog.dataConditionType.ReqUsed, "", false, "", true, "", existingUnits)) {

					TableItem item = new TableItem (idataTable, SWT.NONE);					
					item.setText (0, dataDialog.getName());
					item.setText (1, dataDialog.getUnitClass());
					item.setText (2, dataDialog.getValueUnit());
					item.setText (3, dataDialog.getDataCondition());
					item.setText (4, dataDialog.getDescription());
					addToExistingUnits(dataDialog.getUnitClass());
				}

			}
		});


		editIDataButton.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
		    	FunctionDataDialog dataDialog;
		    	dataDialog = new FunctionDataDialog(getControl().getShell());

				TableItem item = idataTable.getItem(idataTable.getSelectionIndex()); 		    	
		    	
				if (dataDialog.open("Edit input data declaration",
						true, item.getText(0), true, item.getText(1), 
						true, item.getText(2), FunctionDataDialog.dataConditionType.None, item.getText(3), 
						false, "", true, item.getText(4), existingUnits)) {
					
					item.setText (0, dataDialog.getName());
					item.setText (1, dataDialog.getUnitClass());
					item.setText (2, dataDialog.getValueUnit());
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
		colsTitles.add("Unit class");
		colsTitles.add("Value unit");		
		colsTitles.add("Data condition");		 
		colsTitles.add("Time condition");		
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
		    	FunctionDataDialog dataDialog;
		    	dataDialog = new FunctionDataDialog(getControl().getShell());
				if (dataDialog.open("Add variable declaration",
						true, "", true, "", 
						true, "", FunctionDataDialog.dataConditionType.ProdUpReqUsed, "", 
						true, "", true, "", existingUnits)) {

					TableItem item = new TableItem (varsTable, SWT.NONE);					
					item.setText (0, dataDialog.getName());
					item.setText (1, dataDialog.getUnitClass());
					item.setText (2, dataDialog.getValueUnit());
					item.setText (3, dataDialog.getDataCondition());
					if (dataDialog.getDataCondition().toUpperCase().contains("REQUIRED") ||
							dataDialog.getDataCondition().toUpperCase().contains("USED")) {
								item.setText (4, dataDialog.getTimeCondition());
						}
					item.setText (5, dataDialog.getDescription());
					addToExistingUnits(dataDialog.getUnitClass());
				}				
				
			}
		});

		
		editVarButton.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
		    	FunctionDataDialog dataDialog;
		    	dataDialog = new FunctionDataDialog(getControl().getShell());

				TableItem item = varsTable.getItem(varsTable.getSelectionIndex());
				
				if (dataDialog.open("Edit variable declaration",
						true, item.getText(0), true, item.getText(1),
						true, item.getText(2), FunctionDataDialog.dataConditionType.ProdUpReqUsed, item.getText(3), 
						true, item.getText(4), true, item.getText(5), existingUnits)) {

					item.setText (0, dataDialog.getName());
					item.setText (1, dataDialog.getUnitClass());
					item.setText (2, dataDialog.getValueUnit());
					item.setText (3, dataDialog.getDataCondition());
					if (dataDialog.getDataCondition().toUpperCase().contains("REQUIRED") ||
						dataDialog.getDataCondition().toUpperCase().contains("USED")) {
							item.setText (4, dataDialog.getTimeCondition());
					}
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
		    	FunctionDataDialog dataDialog;
		    	dataDialog = new FunctionDataDialog(getControl().getShell());
		    	
		    	
				if (dataDialog.open("Add events class declaration",
						false, "", true, "",
						false, "", FunctionDataDialog.dataConditionType.None, "",
						false, "", false, "", existingUnits)) {

					TableItem item = new TableItem (eventsTable, SWT.NONE);					

					item.setText (0, dataDialog.getUnitClass());
					addToExistingUnits(dataDialog.getUnitClass());
					
				}

			}
		});

		editEventButton.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
		    	FunctionDataDialog dataDialog;
		    	
		    	TableItem item 	= eventsTable.getItem(eventsTable.getSelectionIndex());	    	
		    	
		    	dataDialog = new FunctionDataDialog(getControl().getShell());
				if (dataDialog.open("Edit events class declaration",
						false, "", true, item.getText(0), 
						false, "", FunctionDataDialog.dataConditionType.None, "", 
						false, "", false, "", existingUnits)) {

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
		    	FunctionDataDialog dataDialog;
		    	dataDialog = new FunctionDataDialog(getControl().getShell());
		    	
				if (dataDialog.open("Add extra file declaration",
						true, "", false, "", 
						false, "", FunctionDataDialog.dataConditionType.ReqUsed, "", 
						false, "", false, "", existingUnits)) {
					
					TableItem item = new TableItem (filesTable, SWT.NONE);					
					item.setText (0, dataDialog.getName());
					item.setText (1, dataDialog.getDataCondition());
					
				}

			}
		});
		
		editFileButton.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
		    	FunctionDataDialog dataDialog;
		    	dataDialog = new FunctionDataDialog(getControl().getShell());

		    	TableItem item 	= filesTable.getItem(filesTable.getSelectionIndex());	    			    	
		    	
				if (dataDialog.open("Edit extra file declaration",
						true, item.getText(0), false, "", 
						false, "", FunctionDataDialog.dataConditionType.ReqUsed, item.getText(1),
						false, "", false, "", existingUnits)) {
					
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

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
	}


	private void dialogChanged() {
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

/*	public String[][] getFuncVars() {
		
		String[][] vars = new String[varsTable.getItemCount()][5];
		
		for (int i=0;i<varsTable.getItemCount();i++) {
			vars[i][0] = varsTable.getItem(i).getText(0);
			vars[i][1] = varsTable.getItem(i).getText(1);
			vars[i][2] = varsTable.getItem(i).getText(2);
			vars[i][3] = varsTable.getItem(i).getText(3);
			vars[i][4] = varsTable.getItem(i).getText(4);			
		}
		
		return vars;
	}
	

	public int getFuncVarsCount() {		
		return varsTable.getItemCount();
	}

	
	public String[][] getFuncFiles() {
		
		String[][] vars = new String[filesTable.getItemCount()][5];
		
		for (int i=0;i<filesTable.getItemCount();i++) {
			vars[i][0] = filesTable.getItem(i).getText(0);
			vars[i][1] = filesTable.getItem(i).getText(1);
		}
		
		return vars;
	}
	

	public int getFuncFilesCount() {		
		return filesTable.getItemCount();
	}
	*/
	
	public void fillFunctionProperties(FunctionProperties properties)
	{
		
		int i;
				
		for (i=0;i<paramsTable.getItemCount();i++)
		{
			FunctionParameter param = new FunctionParameter();
			param.name = paramsTable.getItem(i).getText(0);
			param.valueUnit = paramsTable.getItem(i).getText(1);
			param.description = paramsTable.getItem(i).getText(2);
			properties.functionParameters.add(param);
		}
		

		for (i=0;i<idataTable.getItemCount();i++)
		{
			FunctionInputData idata = new FunctionInputData();
			idata.name = idataTable.getItem(i).getText(0);
			idata.unitClass = idataTable.getItem(i).getText(1);
			idata.valueUnit = idataTable.getItem(i).getText(2);
			idata.isRequired = idataTable.getItem(i).getText(3).toUpperCase().contains("REQUIRED");
			idata.description = idataTable.getItem(i).getText(4);
			properties.functionInputData.add(idata);
		}
		
		for (i=0;i<varsTable.getItemCount();i++)
		{
			FunctionVariable var = new FunctionVariable();
			var.name = varsTable.getItem(i).getText(0);
			var.unitClass = varsTable.getItem(i).getText(1);
			var.valueUnit = varsTable.getItem(i).getText(2);
			var.isProduced = varsTable.getItem(i).getText(3).toUpperCase().contains("PRODUCED");
			var.isUpdated = varsTable.getItem(i).getText(3).toUpperCase().contains("UPDATED");			
			var.isRequired = varsTable.getItem(i).getText(3).toUpperCase().contains("REQUIRED");
			var.isPrevious = varsTable.getItem(i).getText(4).toUpperCase().contains("PREVIOUS");			
			var.description = varsTable.getItem(i).getText(5);
			properties.functionVariables.add(var);
		}

		for (i=0;i<eventsTable.getItemCount();i++)
		{
			FunctionEvent event = new FunctionEvent();
			event.unitClass = eventsTable.getItem(i).getText(0);
			properties.functionEvents.add(event);
		}
		
		for (i=0;i<filesTable.getItemCount();i++)
		{
			FunctionExtraFile file = new FunctionExtraFile();
			file.name = filesTable.getItem(i).getText(0);
			file.isRequired = filesTable.getItem(i).getText(1).toUpperCase().contains("REQUIRED");
			properties.functionExtraFiles.add(file);
		}
		
		
	}
	
}



