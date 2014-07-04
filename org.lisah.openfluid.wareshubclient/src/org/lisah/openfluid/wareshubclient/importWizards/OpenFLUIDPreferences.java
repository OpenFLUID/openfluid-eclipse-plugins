package org.lisah.openfluid.wareshubclient.importWizards;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OpenFLUIDPreferences {

	
	private static final String WORKSPACE_DIR = "workspace";
	private static final String WARESDEV_DIR = "wares-dev";
	private static final String SIMULATORS_DIR = "simulators";
	private static final String OBSERVERS_DIR = "observers";
	private static final String BUILDEREXTS_DIR = "builderexts";
	
	private static final HashMap<String, String> WARES_DIRS =
			new HashMap<String, String>();
	static
	{
		WARES_DIRS.put("simulators", SIMULATORS_DIR);
		WARES_DIRS.put("observers", OBSERVERS_DIR);
		WARES_DIRS.put("builderexts", BUILDEREXTS_DIR);
	}
	
	private String workspacePath;
	
	
	public static String getDefaultWorkspacePath() {
		
		String Path = System.getProperty("user.home")+"/.openfluid/"+WORKSPACE_DIR;

		if (System.getProperty("os.name").startsWith("Windows")) {
			Path = System.getProperty("user.home")+"/openfluid/"+WORKSPACE_DIR;
		}
		
		return Path;
	}
	
	
	public OpenFLUIDPreferences() {
		workspacePath = getDefaultWorkspacePath();
	}
	
	
	public String getWorkspacePath() {
		return workspacePath;
	}
	
	
	public void setWorkspacePath(String path) {
		workspacePath = path;
	}
	
	
	public String getWaresDevPath() {		
		return getWorkspacePath()+"/"+WARESDEV_DIR;
	}
	
	
	
	public String getWaresTypeDevPath(String WType) {		
		return getWaresDevPath()+"/"+WARES_DIRS.get(WType);
	}
	
	
	public String getSimsDevPath() {
		return getWaresDevPath()+"/"+SIMULATORS_DIR;
	}
	
	
	public String getObssDevPath() {		
		return getWaresDevPath()+"/"+OBSERVERS_DIR;
	}
	
	
	public String getBextsDevPath() {
		
		return getWaresDevPath()+"/"+BUILDEREXTS_DIR;
		
	}
}
