package org.lisah.openfluid.newfunc.wizards;

public class FunctionInputData {

	public String name;
	public String unitClass;
	public String description;
	public String valueUnit;
	public Boolean isRequired;

	public FunctionInputData() {
		this.description = "";
		this.isRequired = true;
		this.name = "";
		this.unitClass = "";
		this.valueUnit = "";
	}

}
