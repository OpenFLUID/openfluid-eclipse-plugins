package org.lisah.openfluid.wareshubjson.editors;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.*;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.EditorPart;

/**
 * An example showing how to create a multi-page editor.
 * This example has 3 pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
public class JSONEditor extends EditorPart implements IResourceChangeListener{


	private FormToolkit toolkit;
	private ScrolledForm form;

	Section generalSection;
	Section softwareSection;
	Section issuesSection;


	private Text tagsText;
	private Text contactsText;
	private Combo licenseCombo;
	private Combo statusCombo;
	private Text depsText;

	private Table issuesTable;
	private Button issuesAddButton;
	private Button issuesEditButton;
	private Button issuesRemoveButton;

	String inputFilePath = null;
	WaresHubJSONData WHJSONData;

	boolean dirty;

	boolean active;

	boolean isValid;

	/**
	 * Creates a multi-page editor example.
	 */
	public JSONEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);	

		WHJSONData = new WaresHubJSONData();
		active = false;
		dirty = false;
		isValid = false;
	}


	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		toolkit.dispose();
		super.dispose();
	}

	protected void setDirty(boolean value) {
		dirty = value;
		firePropertyChange(PROP_DIRTY);
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public void createPartControl(Composite parent) {

		TableWrapData td;
		GridData gd;
		Label label;

		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);

		File tmpF = new File(inputFilePath);
		tmpF.getParentFile().getName();
		form.setText(tmpF.getParentFile().getName() + " - wareshub.json");
		toolkit.decorateFormHeading(form.getForm());
		form.getToolBarManager().update(true);


		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = true;
		layout.horizontalSpacing = 30;
		layout.verticalSpacing = 30;		
		form.getBody().setLayout(layout);

		label = toolkit.createLabel(form.getBody(),"File path: "+inputFilePath,SWT.LEFT);
		td = new TableWrapData(TableWrapData.FILL);
		td.colspan = 2;
		td.grabHorizontal = true;
		label.setLayoutData(td);


		// - - - - - - - - - - - - - - - - - - - -


		generalSection = toolkit.createSection(form.getBody(),Section.TITLE_BAR);
		generalSection.setText("General informations");
		td = new TableWrapData(TableWrapData.FILL);
		td.grabHorizontal = true;
		generalSection.setLayoutData(td); 

		Composite generalComposite = toolkit.createComposite(generalSection);
		GridLayout generalLayout = new GridLayout();
		generalLayout.makeColumnsEqualWidth = false;
		generalLayout.numColumns = 2;
		generalComposite.setLayout(generalLayout);

		label = toolkit.createLabel(generalComposite, "Tags: ",SWT.RIGHT);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		label.setLayoutData(gd);				

		tagsText = toolkit.createText(generalComposite,"",SWT.LEFT | SWT.BORDER);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;        
		tagsText.setLayoutData(gd);
		tagsText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				WHJSONData.setTags(commaSepStringToStringArray(tagsText.getText()));
				dialogChanged();
			}
		});

		label = toolkit.createLabel(generalComposite,"Contacts: ",SWT.RIGHT);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		label.setLayoutData(gd);				

		contactsText = toolkit.createText(generalComposite,"",SWT.LEFT | SWT.BORDER);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		contactsText.setLayoutData(gd);
		contactsText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				WHJSONData.setContacts(commaSepStringToStringArray(contactsText.getText()));
				dialogChanged();
			}
		});


		generalComposite.pack();
		generalSection.setClient(generalComposite);
		generalSection.pack();


		// - - - - - - - - - - - - - - - - - - - - 

		softwareSection = toolkit.createSection(form.getBody(),Section.TITLE_BAR);
		softwareSection.setText("Development informations");
		td = new TableWrapData(TableWrapData.FILL);
		td.grabHorizontal = true;
		softwareSection.setLayoutData(td);
		softwareSection.descriptionVerticalSpacing = generalSection.getTextClientHeightDifference();

		Composite softwareComposite = toolkit.createComposite(softwareSection);
		GridLayout softwareLayout = new GridLayout();
		softwareLayout.makeColumnsEqualWidth = false;
		softwareLayout.numColumns = 2;
		softwareComposite.setLayout(softwareLayout);


		label = toolkit.createLabel(softwareComposite, "License: ",SWT.RIGHT);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		label.setLayoutData(gd);				

		licenseCombo = new Combo(softwareComposite,SWT.NONE);
		String[] licenses = 
			{"AFL-1.1","AFL-1.2","AFL-2.0","AFL-2.1","AFL-3.0","APL-1.0",
				"Aladdin","ANTLR-PD","Apache-1.0","Apache-1.1","Apache-2.0",
				"APSL-1.0","APSL-1.1","APSL-1.2","APSL-2.0","Artistic-1.0",
				"Artistic-1.0-cl8","Artistic-1.0-Perl","Artistic-2.0","AAL",
				"BitTorrent-1.0","BitTorrent-1.1","BSL-1.0","BSD-2-Clause",
				"BSD-2-Clause-FreeBSD","BSD-2-Clause-NetBSD","BSD-3-Clause",
				"BSD-3-Clause-Clear","BSD-4-Clause","BSD-4-Clause-UC","CECILL-1.0",
				"CECILL-1.1","CECILL-2.0","CECILL-B","CECILL-C","ClArtistic",
				"CNRI-Python","CNRI-Python-GPL-Compatible","CPOL-1.02","CDDL-1.0",
				"CDDL-1.1","CPAL-1.0","CPL-1.0","CATOSL-1.1","Condor-1.1","CC-BY-1.0",
				"CC-BY-2.0","CC-BY-2.5","CC-BY-3.0","CC-BY-ND-1.0","CC-BY-ND-2.0",
				"CC-BY-ND-2.5","CC-BY-ND-3.0","CC-BY-NC-1.0","CC-BY-NC-2.0",
				"CC-BY-NC-2.5","CC-BY-NC-3.0","CC-BY-NC-ND-1.0","CC-BY-NC-ND-2.0",
				"CC-BY-NC-ND-2.5","CC-BY-NC-ND-3.0","CC-BY-NC-SA-1.0",
				"CC-BY-NC-SA-2.0","CC-BY-NC-SA-2.5","CC-BY-NC-SA-3.0","CC-BY-SA-1.0",
				"CC-BY-SA-2.0","CC-BY-SA-2.5","CC-BY-SA-3.0","CC0-1.0","CUA-OPL-1.0",
				"D-FSL-1.0","WTFPL","EPL-1.0","eCos-2.0","ECL-1.0","ECL-2.0","EFL-1.0",
				"EFL-2.0","Entessa","ErlPL-1.1","EUDatagrid","EUPL-1.0","EUPL-1.1","Fair",
				"Frameworx-1.0","FTL","AGPL-1.0","AGPL-3.0","GFDL-1.1","GFDL-1.2",
				"GFDL-1.3","GPL-1.0","GPL-1.0+","GPL-2.0","GPL-2.0+","GPL-2.0-with-autoconf-exception",
				"GPL-2.0-with-bison-exception","GPL-2.0-with-classpath-exception",
				"GPL-2.0-with-font-exception","GPL-2.0-with-GCC-exception","GPL-3.0",
				"GPL-3.0+","GPL-3.0-with-autoconf-exception","GPL-3.0-with-GCC-exception",
				"LGPL-2.1","LGPL-2.1+","LGPL-3.0","LGPL-3.0+","LGPL-2.0","LGPL-2.0+",
				"gSOAP-1.3b","HPND","IBM-pibs","IPL-1.0","Imlib2","IJG","Intel","IPA",
				"ISC","JSON","LPPL-1.3a","LPPL-1.0","LPPL-1.1","LPPL-1.2","LPPL-1.3c",
				"Libpng","LPL-1.02","LPL-1.0","MS-PL","MS-RL","MirOS","MIT","Motosoto",
				"MPL-1.0","MPL-1.1","MPL-2.0","MPL-2.0-no-copyleft-exception","Multics",
				"NASA-1.3","Naumen","NBPL-1.0","NGPL","NOSL","NPL-1.0","NPL-1.1","Nokia",
				"NPOSL-3.0","NTP","OCLC-2.0","ODbL-1.0","PDDL-1.0","OGTSL","OLDAP-2.2.2",
				"OLDAP-1.1","OLDAP-1.2","OLDAP-1.3","OLDAP-1.4","OLDAP-2.0","OLDAP-2.0.1",
				"OLDAP-2.1","OLDAP-2.2","OLDAP-2.2.1","OLDAP-2.3","OLDAP-2.4","OLDAP-2.5",
				"OLDAP-2.6","OLDAP-2.7","OPL-1.0","OSL-1.0","OSL-2.0","OSL-2.1","OSL-3.0",
				"OLDAP-2.8","OpenSSL","PHP-3.0","PHP-3.01","PostgreSQL","Python-2.0",
				"QPL-1.0","RPSL-1.0","RPL-1.1","RPL-1.5","RHeCos-1.1","RSCPL","SAX-PD",
				"SGI-B-1.0","SGI-B-1.1","SGI-B-2.0","OFL-1.0","OFL-1.1","SimPL-2.0",
				"Sleepycat","SMLNJ","SugarCRM-1.1.3","SISSL","SISSL-1.2","SPL-1.0",
				"Watcom-1.0","NCSA","VSL-1.0","W3C","WXwindows","Xnet","X11","XFree86-1.1",
				"YPL-1.0","YPL-1.1","Zimbra-1.3","Zlib","ZPL-1.1","ZPL-2.0","ZPL-2.1","Unlicense"};
		licenseCombo.setItems(licenses);
		gd = new GridData();
		licenseCombo.setLayoutData(gd);
		licenseCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {				
				WHJSONData.setLicense(licenseCombo.getText());
				dialogChanged();
			}
		});


		label = toolkit.createLabel(softwareComposite, "Development status: ",SWT.RIGHT);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		label.setLayoutData(gd);


		statusCombo = new Combo(softwareComposite,SWT.READ_ONLY);
		String[] statuses = {"stable","experimental","beta"};
		statusCombo.setItems(statuses);
		gd = new GridData();
		statusCombo.setLayoutData(gd);
		statusCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				WHJSONData.setStatus(statusCombo.getText());
				dialogChanged();
			}
		});

		label = toolkit.createLabel(softwareComposite, "Dependencies: ",SWT.RIGHT);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		label.setLayoutData(gd);				

		depsText = toolkit.createText(softwareComposite,"",SWT.LEFT | SWT.BORDER);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		depsText.setLayoutData(gd);
		depsText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				WHJSONData.setDependencies(commaSepStringToStringArray(depsText.getText()));
				dialogChanged();
			}
		});


		softwareComposite.pack();
		softwareSection.setClient(softwareComposite);
		softwareSection.pack();


		// - - - - - - - - - - - - - - - - - - - -


		issuesSection = toolkit.createSection(form.getBody(),Section.TITLE_BAR);
		issuesSection.setText("Issues");
		td = new TableWrapData(TableWrapData.FILL);
		td.colspan = 2;
		td.grabHorizontal = true;
		issuesSection.setLayoutData(td);

		Composite issuesComposite = toolkit.createComposite(issuesSection);
		GridLayout issuesLayout = new GridLayout();
		issuesLayout.makeColumnsEqualWidth = false;
		issuesLayout.numColumns = 2;
		issuesComposite.setLayout(issuesLayout);

		label = toolkit.createLabel(issuesComposite, "Issues: ",SWT.LEFT);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.horizontalSpan = 2;
		label.setLayoutData(gd);

		issuesTable = toolkit.createTable(issuesComposite, SWT.BORDER);		
		issuesTable.setHeaderVisible (true);
		String[] titles = {"ID","Title","Type","State"};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn(issuesTable, SWT.NONE);
			column.setText(titles[i]);
		}
		for (int i=0; i<issuesTable.getColumnCount(); i++) {
			issuesTable.getColumn(i).pack();
		}
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalSpan = 3;
		gd.minimumHeight = 250;
		issuesTable.setLayoutData(gd);
		issuesTable.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				editSelectedIssue();
			}

			@Override
			public void mouseDown(MouseEvent arg0) {}

			@Override
			public void mouseUp(MouseEvent arg0) {}
		});

		issuesAddButton = toolkit.createButton(issuesComposite,"Add...",SWT.PUSH);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL|GridData.VERTICAL_ALIGN_BEGINNING);               
		issuesAddButton.setLayoutData(gd);
		issuesAddButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {				
				IssueDialog issueDlg = new IssueDialog(getSite().getShell());
				issueDlg.open(null,WHJSONData.getIssueIDs());

				WaresHubJSONData.IssueData issueData = issueDlg.getIssueData();
				if (issueData != null) {
					WHJSONData.addIssue(issueData);
					updateIssuesFromData();
					dialogChanged();
				}
			}
		});


		issuesEditButton = toolkit.createButton(issuesComposite,"Edit...",SWT.PUSH);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL|GridData.VERTICAL_ALIGN_BEGINNING);               
		issuesEditButton.setLayoutData(gd);
		issuesEditButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				editSelectedIssue();
			}
		});

		issuesRemoveButton = toolkit.createButton(issuesComposite,"Remove...",SWT.PUSH);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL|GridData.VERTICAL_ALIGN_BEGINNING);
		issuesRemoveButton.setLayoutData(gd);
		issuesRemoveButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				int[] indices = issuesTable.getSelectionIndices();

				if (indices.length == 1)
				{
					String ID = issuesTable.getItem(indices[0]).getText(0);

					boolean OK = MessageDialog.openConfirm(getSite().getShell(), 
							"Removing \""+ID+"\"", 
							"Are you sure to remove the \""+ID+"\" issue?");	

					if (OK) {
						WHJSONData.removeIssue(ID);
						updateIssuesFromData();
						dialogChanged();
					}
				}

			}
		});


		issuesComposite.pack();
		issuesSection.setClient(issuesComposite);
		issuesSection.pack();

		initialize();

	}

	private void editSelectedIssue() {
		int[] indices = issuesTable.getSelectionIndices();

		if (indices.length == 1)
		{
			String ID = issuesTable.getItem(indices[0]).getText(0);

			IssueDialog issueDlg = new IssueDialog(getSite().getShell());					
			issueDlg.open(WHJSONData.getIssue(ID),WHJSONData.getIssueIDs());

			WaresHubJSONData.IssueData issueData = issueDlg.getIssueData();
			if (issueData != null) {
				WHJSONData.setIssue(issueData);
				updateIssuesFromData();
				dialogChanged();
			}
		}
	}


	// =====================================================================
	// =====================================================================	


	private void initialize() {

		doLoad();

	}


	// =====================================================================
	// =====================================================================	


	private boolean checkCommaSepString(String str,String regex) {

		String[] Splitted = str.split(",");

		for (String s: Splitted) {
			s = s.trim();

			if (!s.isEmpty() && !s.matches(regex)) {
				return false;
			}
		}

		return true;
	}


	// =====================================================================
	// =====================================================================


	private ArrayList<String> commaSepStringToStringArray(String str) {

		ArrayList<String> array = new ArrayList<String>();

		String[] Splitted = str.split(",");

		for (String s: Splitted) {
			s = s.trim();

			if (s.length()>0) {
				array.add(s);
			}
		}

		return array;
	}


	// =====================================================================
	// =====================================================================


	private void dialogChanged() {
		if (active) {
			setDirty(true);
		}

		if (!checkCommaSepString(tagsText.getText(),"[a-zA-Z0-9-_]+")) {
			updateStatus("Wrong format for tags field");
		} else if (!checkCommaSepString(contactsText.getText(),"^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
			updateStatus("Wrong format for contacts field");
		} else if (!checkCommaSepString(depsText.getText(),"[a-zA-Z0-9-_]+")) {
			updateStatus("Wrong format for dependencies field");
		} else if (statusCombo.getText().isEmpty()) {
			updateStatus("Wrong value for status field");
		}


		else {
			updateStatus(null);
		}
	}


	// =====================================================================
	// =====================================================================	


	private void updateStatus(String message) {

		if (message == null) {
			form.setMessage(null, IMessageProvider.NONE);
			isValid = true;
		} else {
			form.setMessage(message, IMessageProvider.ERROR);
			isValid = false;
		}
	}


	// =====================================================================
	// =====================================================================	


	@Override
	public void setFocus() {
		form.setFocus();
	}


	// =====================================================================
	// =====================================================================	


	@Override
	public void resourceChanged(IResourceChangeEvent event) {


		boolean OK =
				MessageDialog.openConfirm(getSite().getShell(), 
						"File changed on disk", 
						"The wareshub.json file on disk is more recent\n"
								+ "Would you like to reload the file from disk?");	


		if (OK) doLoad();

	}


	// =====================================================================
	// =====================================================================	


	private void clearForm() {
		tagsText.setText("");
		contactsText.setText("");
		licenseCombo.setText("");
		statusCombo.setText("");
		depsText.setText("");

		issuesTable.removeAll();
	}


	// =====================================================================
	// =====================================================================	


	private static String stringArrayToCommaSeparatedString(ArrayList<String> stringList)
	{
		if (stringList == null) return "";

		String tmpStr = "";

		boolean isFirst = true;

		for (String s: stringList) {
			if (!isFirst) tmpStr += ",";
			tmpStr += s;
			isFirst = false;
		}

		return tmpStr;		
	}


	// =====================================================================
	// =====================================================================	


	private void updateIssuesFromData() {
		issuesTable.clearAll();
		issuesTable.removeAll();

		for (WaresHubJSONData.IssueData i: WHJSONData.getIssues()) {
			TableItem item = new TableItem (issuesTable, SWT.NONE);
			item.setText(0,i.getID());
			item.setText(1,i.getTitle());
			item.setText(2,i.getType());
			item.setText(3,i.getState());
		}

		for (int i=0; i<issuesTable.getColumnCount(); i++) {
			issuesTable.getColumn(i).pack();	
		}
	}


	// =====================================================================
	// =====================================================================	


	private void updateFormFromData() {

		tagsText.setText(stringArrayToCommaSeparatedString(WHJSONData.getTags()));
		contactsText.setText(stringArrayToCommaSeparatedString(WHJSONData.getContacts()));
		licenseCombo.setText(WHJSONData.getLicense());
		statusCombo.setText(WHJSONData.getStatus());
		depsText.setText(stringArrayToCommaSeparatedString(WHJSONData.getDependencies()));

		updateIssuesFromData();
	}


	// =====================================================================
	// =====================================================================	


	public void doLoad() {

		active = false;

		clearForm();

		if (WHJSONData.load(inputFilePath)) {

			generalSection.setVisible(true);
			softwareSection.setVisible(true);
			issuesSection.setVisible(true);

			updateFormFromData();

			dialogChanged();

		} else {
			generalSection.setVisible(false);
			softwareSection.setVisible(false);
			issuesSection.setVisible(false);
			form.setMessage("Error opening file. Fix or create a new file", IMessageProvider.ERROR);
		}

		active = true;
	}


	// =====================================================================
	// =====================================================================	


	@Override
	public void doSave(IProgressMonitor monitor) {

		if (isValid) {
			WHJSONData.save();
			setDirty(false);	
		} else {
			MessageDialog.openError(getSite().getShell(), 
					"File cannot be saved", 
					"The form data is not valid and cannot be saved on disk.");			
		}



	}


	// =====================================================================
	// =====================================================================	


	@Override
	public void doSaveAs() {

	}


	// =====================================================================
	// =====================================================================	


	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);

		IPath path = ((IPathEditorInput) input).getPath();
		inputFilePath = path.toString();   

	}


	// =====================================================================
	// =====================================================================	


	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
}