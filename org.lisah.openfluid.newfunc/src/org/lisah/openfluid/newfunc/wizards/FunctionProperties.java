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

import org.lisah.openfluid.common.OpenFLUIDPluginProperties;

public class FunctionProperties extends OpenFLUIDPluginProperties {
	
	public Boolean singleSourceFile;

	public String functionID;
	public String functionName;
	public String functionVersion;
	public String functionDescription;	
	public String functionDomain;
	public String functionProcess;
	public String functionMethod;
	public String functionAuthor;
	public String functionAuthorEmail;


	public ArrayList<FunctionParameter> functionParameters;
	public ArrayList<FunctionInputData> functionInputData;
	public ArrayList<FunctionSpatialDyn> functionSpatialDyn;
	public ArrayList<FunctionVariable> functionVariables;
	public ArrayList<FunctionEvent> functionEvents;
	public ArrayList<FunctionExtraFile> functionExtraFiles;
	
	
	
	public FunctionProperties() {

		super();
		
		functionParameters = new ArrayList<FunctionParameter>();
		functionInputData = new ArrayList<FunctionInputData>();
		functionSpatialDyn = new ArrayList<FunctionSpatialDyn>();
		functionVariables = new ArrayList<FunctionVariable>();
		functionEvents = new ArrayList<FunctionEvent>();
		functionExtraFiles = new ArrayList<FunctionExtraFile>();
		
		this.singleSourceFile = true;
		
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


