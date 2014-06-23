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

import org.lisah.openfluid.common.OpenFLUIDPluginProperties;

public class SimulatorProperties extends OpenFLUIDPluginProperties {
	

	public String simulatorID;
	public String simulatorName;
	public String simulatorVersion;
	public String simulatorDescription;	
	public String simulatorDomain;
	public String simulatorProcess;
	public String simulatorMethod;
	public String simulatorAuthor;
	public String simulatorAuthorEmail;
	public boolean simulatorSim2Doc;


	public ArrayList<SimulatorParameter> simulatorParameters;
	public ArrayList<SimulatorInputData> simulatorInputData;
	public ArrayList<SimulatorVariable> simulatorVariables;
	public ArrayList<SimulatorEvent> simulatorEvents;
	public ArrayList<SimulatorExtraFile> simulatorExtraFiles;
	public SimulatorScheduling simulatorScheduling;
	public ArrayList<SimulatorSpatialDyn> simulatorSpatialDyn;
	
	
	public SimulatorProperties() {

		super();
		
		simulatorParameters = new ArrayList<SimulatorParameter>();
		simulatorInputData = new ArrayList<SimulatorInputData>();		
		simulatorVariables = new ArrayList<SimulatorVariable>();
		simulatorEvents = new ArrayList<SimulatorEvent>();
		simulatorExtraFiles = new ArrayList<SimulatorExtraFile>();
		simulatorSpatialDyn = new ArrayList<SimulatorSpatialDyn>();
		simulatorScheduling = new SimulatorScheduling();
		
		
		this.simulatorAuthor = "";
		this.simulatorAuthorEmail = "";
		this.simulatorDescription = "";
		this.simulatorDomain = "";
		this.simulatorEvents.clear();
		this.simulatorExtraFiles.clear();
		this.simulatorID = "";
		this.simulatorInputData.clear();
		this.simulatorMethod = "";
		this.simulatorName = "";
		this.simulatorParameters.clear();
		this.simulatorProcess = "";
		this.simulatorVariables.clear();
		this.sourcesFilesRoot = "";
		this.simulatorSim2Doc = false;
		
	}

	
	
	
}


