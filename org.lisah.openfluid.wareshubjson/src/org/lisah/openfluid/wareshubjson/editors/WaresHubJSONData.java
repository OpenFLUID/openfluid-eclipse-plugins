package org.lisah.openfluid.wareshubjson.editors;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.ParseException;

public class WaresHubJSONData {

	public static class IssueData {

		private String ID;

		private String title;

		private String creator;

		private String date;

		private String type;

		private String state;

		private String description;

		private String urgency;

		public IssueData() {
			ID = "";
			title = "";
			creator = "";
			date = "";
			type = "";
			state = "";
			description = "";
			urgency = "";
		}

		public String getID() {
			return ID;
		}

		public void setID(String ID) {
			this.ID = ID;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getCreator() {
			return creator;
		}

		public void setCreator(String creator) {
			this.creator = creator;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getUrgency() {
			return urgency;
		}

		public void setUrgency(String urgency) {
			this.urgency = urgency;
		}
	}

	
	// =====================================================================
	// =====================================================================	
	
	
	private ArrayList<String> tags;

	private ArrayList<String> contacts;

	private String license;

	private String status;

	private ArrayList<String> dependencies;

	private ArrayList<IssueData> issues;

	private String filePath;


	// =====================================================================
	// =====================================================================	
	
	
	public WaresHubJSONData() {

	}


	// =====================================================================
	// =====================================================================	

	
	public ArrayList<String> getTags() {
		return tags;
	}


	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}



	public ArrayList<String> getContacts() {
		return contacts;
	}



	public void setContacts(ArrayList<String> contacts) {
		this.contacts = contacts;
	}



	public String getLicense() {
		return license;
	}



	public void setLicense(String license) {
		this.license = license;
	}



	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}



	public ArrayList<String> getDependencies() {
		return dependencies;
	}



	public void setDependencies(ArrayList<String> dependencies) {
		this.dependencies = dependencies;
	}

	
	// =====================================================================
	// =====================================================================	
	

	public IssueData getIssue(String ID) {
		for (int i=0;i<issues.size();i++) {
			if (issues.get(i).getID().equals(ID) ) {				
				return issues.get(i);
			}
		}				
		return null;
	}


	public void setIssue(IssueData data) {

		for (int i=0;i<issues.size();i++) {
			if (issues.get(i).getID().equals(data.getID())) {
				issues.set(i,data);
				return;
			}
		}		
		addIssue(data);
	}


	public void addIssue(IssueData data) {
		issues.add(data);
	}


	public void removeIssue(String ID) {

		for (int i=0;i<issues.size();i++) {
			if (issues.get(i).getID().equals(ID)) {
				issues.remove(i);
				return;
			}
		}		
	}


	public ArrayList<String> getIssueIDs() {
		
		ArrayList<String> IDs = new ArrayList<String>();
		
		for (int i=0;i<issues.size();i++) {
		  IDs.add(issues.get(i).getID());
		}
		
		return IDs;
	}


	public ArrayList<IssueData> getIssues() {
		return issues;
	}


	// =====================================================================
	// =====================================================================	
	
	
	private static ArrayList<String> jsonArrayToStringArray(JsonArray array) {

		ArrayList<String> tags = new ArrayList<String>();

		for (JsonValue V: array.values()) {
			tags.add(V.asString());
		}

		return tags;		
	}


	// =====================================================================
	// =====================================================================	
	
	
	private static String stringArrayToJSONString(ArrayList<String> array) {

		if (array.isEmpty()) {
			return "[]";
		}
		
		String tmpStr = "[";

		boolean isFirst = true;

		for (String s: array) {
			if (!isFirst) tmpStr += ",";
			tmpStr += JsonObject.valueOf(s).toString();
			isFirst = false;
		}
		tmpStr += "]"; 

		return tmpStr;		
	}

	
	// =====================================================================
	// =====================================================================	
	

	private void resetData() {
		
		tags = new ArrayList<String>();
		contacts = new ArrayList<String>();
		
		license = "";
		status = "";
		dependencies = new ArrayList<String>();
		
		issues = new ArrayList<WaresHubJSONData.IssueData>();
	}

	
	// =====================================================================
	// =====================================================================	
	
	
	public boolean load(String filePath) {

		resetData();
		
		try {
			FileInputStream fis = new FileInputStream(filePath);

			Reader reader = new InputStreamReader(fis);
			try {
				JsonObject infos = JsonObject.readFrom(reader);

				if (infos.get("tags") != null) {
					setTags(jsonArrayToStringArray(infos.get("tags").asArray()));
				}

				if (infos.get("contacts") != null) {
					setContacts(jsonArrayToStringArray(infos.get("contacts").asArray()));
				}

				if (infos.get("status") != null) {
					setStatus(infos.get("status").asString());
				}					

				if (infos.get("license") != null) {
					setLicense(infos.get("license").asString());
				}

				if (infos.get("external-deps") != null) {
					setDependencies(jsonArrayToStringArray(infos.get("external-deps").asArray()));
				}

				
				JsonValue issuesValue = infos.get("issues");
				
				if (issuesValue != null) {
					JsonObject issuesObject = issuesValue.asObject();
					
					for(String ID : issuesObject.names()) {
						
						JsonObject currentObject = issuesObject.get(ID).asObject();
						
						if (currentObject != null) {
						
							IssueData issueData = new IssueData();						
							
							issueData.setID(ID);

							if (currentObject.get("title") != null) {
								issueData.setTitle(currentObject.get("title").asString());
							}
							  
							if (currentObject.get("creator") != null) {
								issueData.setCreator(currentObject.get("creator").asString());
							}
							
							if (currentObject.get("date") != null) {
								issueData.setDate(currentObject.get("date").asString());
							}

							if (currentObject.get("type") != null) {
								issueData.setType(currentObject.get("type").asString());
							}
							
							if (currentObject.get("state") != null) {
								issueData.setState(currentObject.get("state").asString());
							}
							
							if (currentObject.get("description") != null) {
								issueData.setDescription(currentObject.get("description").asString());
							}
							
							if (currentObject.get("urgency") != null) {
								issueData.setUrgency(currentObject.get("urgency").asString());
							}





							
							addIssue(issueData);
						}
						
						
						
					}
				}
					
				this.filePath = filePath;

			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} catch (ParseException e) {
				e.printStackTrace();
				return false;
			}


		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} 

		return true;
	}


	// =====================================================================
	// =====================================================================	

	
	public void save() {


		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(filePath));
			writer.write("{\n");

			writer.write("  \"tags\": "+stringArrayToJSONString(getTags())+",\n");
			writer.write("  \"contacts\": "+stringArrayToJSONString(getContacts())+",\n");
			writer.write("  \"status\": "+JsonObject.valueOf(getStatus()).toString()+",\n");
			writer.write("  \"license\": "+JsonObject.valueOf(getLicense()).toString()+",\n");
			writer.write("  \"external-deps\": "+stringArrayToJSONString(getDependencies())+",\n");
			writer.write("  \"issues\": {");
			
			String issueSep = "";
			
			for(IssueData i: issues) {
				writer.write(issueSep+"\n");
				writer.write("    \""+i.getID()+"\": {\n");
				writer.write("      \"title\": "+JsonObject.valueOf(i.getTitle()).toString()+",\n");
				writer.write("      \"creator\": "+JsonObject.valueOf(i.getCreator()).toString()+",\n");
				writer.write("      \"date\": \""+i.getDate()+"\",\n");
				writer.write("      \"type\": \""+i.getType()+"\",\n");
				writer.write("      \"state\": \""+i.getState()+"\",\n");
				writer.write("      \"description\": "+JsonObject.valueOf(i.getDescription()).toString()+",\n");
				writer.write("      \"urgency\": \""+i.getUrgency()+"\"\n");
				writer.write("    }");
				
				issueSep = ",";
			}
			writer.write("\n");
			writer.write("  }\n");
			writer.write("}\n");

			writer.close();
		} catch (IOException e) {

			e.printStackTrace();
		} finally {

			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
	// =====================================================================
	// =====================================================================	
	

	public boolean check(String ErrorMsg) {
		return false;

	}


}
