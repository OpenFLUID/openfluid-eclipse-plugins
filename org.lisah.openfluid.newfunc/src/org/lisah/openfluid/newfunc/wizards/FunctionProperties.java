package org.lisah.openfluid.newfunc.wizards;

import java.util.ArrayList;

public class FunctionProperties {


	public String container;
	public String sourcesFilesRoot;
	public Boolean singleSourceFile;
	public String className;	
	public String buildSystem;
	public String buildFolder;
	public String installDir;

	public String functionID;
	public String functionName;
	public String functionDescription;	
	public String functionDomain;
	public String functionProcess;
	public String functionMethod;
	public String functionAuthor;
	public String functionAuthorEmail;


	public ArrayList<FunctionParameter> functionParameters;
	public ArrayList<FunctionInputData> functionInputData;
	public ArrayList<FunctionVariable> functionVariables;
	public ArrayList<FunctionEvent> functionEvents;
	public ArrayList<FunctionExtraFile> functionExtraFiles;
	
	
	
	public FunctionProperties() {

		functionParameters = new ArrayList<FunctionParameter>();
		functionInputData = new ArrayList<FunctionInputData>();
		functionVariables = new ArrayList<FunctionVariable>();
		functionEvents = new ArrayList<FunctionEvent>();
		functionExtraFiles = new ArrayList<FunctionExtraFile>();
		
		
		this.buildSystem = "";
		this.buildFolder = "";
		this.className = "";
		this.container = "";
		this.functionAuthor = "";
		this.functionAuthorEmail = "";
		this.functionDescription = "";
		this.functionDomain = "";
		this.functionEvents.clear();
		this.functionExtraFiles.clear();
		this.functionID = "";
		this.functionInputData.clear();
		this.functionMethod = "";
		this.functionName = "";
		this.functionParameters.clear();
		this.functionProcess = "";
		this.functionVariables.clear();
		this.installDir = "";
		this.sourcesFilesRoot = "";
	}

	
	
	
}


