package org.lisah.openfluid.wareshubclient.importWizards;

import java.util.HashMap;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;

public class OpenFLUIDPreferences {

	
	private static final String WORKSPACE_DIR = "workspace";
	private static final String WARESDEV_DIR = "wares-dev";
	private static final String SIMULATORS_DIR = "simulators";
	private static final String OBSERVERS_DIR = "observers";
	private static final String BUILDEREXTS_DIR = "builderexts";

	
	private String userName;

	private String userPassword;

	private String workspacePath;

	private String requestedRemoteURL;

	
	private IEclipsePreferences prefs;
	
	
	private static final HashMap<String, String> WARES_DIRS =
			new HashMap<String, String>();
	static
	{
		WARES_DIRS.put("simulators", SIMULATORS_DIR);
		WARES_DIRS.put("observers", OBSERVERS_DIR);
		WARES_DIRS.put("builderexts", BUILDEREXTS_DIR);
	}

	
	// =====================================================================
	// =====================================================================

	
	public static String getDefaultWorkspacePath() {
		
		String Path = System.getProperty("user.home")+"/.openfluid/"+WORKSPACE_DIR;

		if (System.getProperty("os.name").startsWith("Windows")) {
			Path = System.getProperty("user.home")+"/openfluid/"+WORKSPACE_DIR;
		}
		
		return Path;
	}
	
	
	// =====================================================================
	// =====================================================================

	
	public OpenFLUIDPreferences() {
		workspacePath = getDefaultWorkspacePath();
		prefs = InstanceScope.INSTANCE.getNode("org.lisah.openfluid.wareshubclient");
		load();
	}
	
	
	// =====================================================================
	// =====================================================================	


	public void load() {
		userName = prefs.get("userName","");		
		workspacePath = prefs.get("workspacePath",getDefaultWorkspacePath());
		requestedRemoteURL = prefs.get("remoteURL","");		
	}

	
	// =====================================================================
	// =====================================================================	
	
	
	public void save() {
		prefs.put("userName",userName);
		prefs.put("workspacePath",workspacePath);
		prefs.put("remoteURL",requestedRemoteURL);
	}	

	
	// =====================================================================
	// =====================================================================	
	
	
	public String getUserPassword() {
		return userPassword;
	}

	
	// =====================================================================
	// =====================================================================	
	

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	
	// =====================================================================
	// =====================================================================	
	

	public String getUserName() {
		return userName;
	}


	// =====================================================================
	// =====================================================================	
	
	
	public void setUserName(String userName) {
		this.userName = userName;
	}


	// =====================================================================
	// =====================================================================	
	
	
	public String getRequestedRemoteURL() {
		return requestedRemoteURL;
	}


	// =====================================================================
	// =====================================================================	
	
	
	public void setRequestedRemoteURL(String requestedRemoteURL) {
		this.requestedRemoteURL = requestedRemoteURL;
	}


	// =====================================================================
	// =====================================================================	
	
	
	public String getWorkspacePath() {
		return workspacePath;
	}

	
	// =====================================================================
	// =====================================================================	
	
	
	public void setWorkspacePath(String path) {
		workspacePath = path;
	}
	

	// =====================================================================
	// =====================================================================	
	
	
	public String getWaresDevPath() {		
		return getWorkspacePath()+"/"+WARESDEV_DIR;
	}
	
	
	// =====================================================================
	// =====================================================================	
	
	
	public String getWaresTypeDevPath(String WType) {		
		return getWaresDevPath()+"/"+WARES_DIRS.get(WType);
	}
	

	// =====================================================================
	// =====================================================================	
	
	
	public String getSimsDevPath() {
		return getWaresDevPath()+"/"+SIMULATORS_DIR;
	}

	
	// =====================================================================
	// =====================================================================	
	
	
	public String getObssDevPath() {		
		return getWaresDevPath()+"/"+OBSERVERS_DIR;
	}
	
	
	// =====================================================================
	// =====================================================================	
	
	
	public String getBextsDevPath() {	
		return getWaresDevPath()+"/"+BUILDEREXTS_DIR;
		
	}
}
