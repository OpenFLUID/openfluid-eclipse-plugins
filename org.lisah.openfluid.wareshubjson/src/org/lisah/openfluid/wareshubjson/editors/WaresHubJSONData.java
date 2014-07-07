package org.lisah.openfluid.wareshubjson.editors;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.ParseException;

public class WaresHubJSONData {

	public class IssueData {

		private String ID;

		private String title;

		private String creator;

		private String date;

		private String type;

		private boolean open;

		private String description;

		private String urgency;

		public IssueData() {

		}

		public String getID() {
			return ID;
		}

		public void setID(String iD) {
			ID = iD;
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

		public boolean isOpen() {
			return open;
		}

		public void setOpen(boolean open) {
			this.open = open;
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
		return null;
	}


	public void setIssue(IssueData data) {

	}


	public void addIssue(IssueData data) {

	}


	public void removeIssue(String ID) {

	}


	public boolean isIssueExists(String ID) {
		return false;
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
			writer.write("  \"issues\": {\n");
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
