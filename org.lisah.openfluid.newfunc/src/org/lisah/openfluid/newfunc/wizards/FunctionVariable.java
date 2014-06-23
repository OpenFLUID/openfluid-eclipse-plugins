package org.lisah.openfluid.newfunc.wizards;

public class FunctionVariable {

	public String name;
	public String unitClass;
	public String description;
	public String valueUnit;
	public Boolean isRequired;
	public Boolean isPrevious;
	public Boolean isProduced;
	public Boolean isUpdated;
	
	public FunctionVariable() {
		this.description = "";
		this.isPrevious = false;
		this.isRequired = false;
		this.isProduced = false;
		this.isUpdated = false;
		this.name = "";
		this.unitClass = "";
		this.valueUnit = "";
	}

}
