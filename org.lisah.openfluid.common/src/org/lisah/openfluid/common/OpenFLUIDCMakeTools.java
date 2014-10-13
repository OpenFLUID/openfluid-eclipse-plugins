package org.lisah.openfluid.common;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;


public class OpenFLUIDCMakeTools {

	
	public static String findCMakeCommand()
	{
		String cmakeProgram = "cmake";
		String foundCMakeCommand = "";
		
		if (System.getProperty("os.name").startsWith("Windows")) {
			cmakeProgram = "cmake.exe";
		}
				
		
		try {
		    Process cmakeProcess = Runtime.getRuntime().exec(new String[] { cmakeProgram, "--version"});
			
			BufferedReader cmakeInputStream = new BufferedReader(new InputStreamReader(cmakeProcess.getInputStream()));
            String cmakeVersionStr = cmakeInputStream.readLine();             
            
            String[] cmakeVersionStrTokens = cmakeVersionStr.split(" ");
                        

            if (cmakeVersionStrTokens.length >= 3 &&  cmakeVersionStrTokens[0].equals("cmake")) {
            	String[] cmakeVersionNbrTokens = cmakeVersionStrTokens[2].split("\\.");            	

            	try {
            		if (cmakeVersionNbrTokens.length >= 3 && 
            			Integer.parseInt(cmakeVersionNbrTokens[0]) >= 2 && 
            			Integer.parseInt(cmakeVersionNbrTokens[1]) >= 8 &&
            			Integer.parseInt(cmakeVersionNbrTokens[2]) >= 9) {
            			foundCMakeCommand = cmakeProgram;		
            		}
            	}
            	catch (NumberFormatException nfe) {
            		System.out.println("CMake not found (wrong version format)");
            	}
            }	

		} catch (IOException e) {
			System.out.println("CMake not found");			
		}
		
		return foundCMakeCommand;		
	}
	
	
    static public void runCMakeCommand(String cmakeCommandPath, String projectPath, String projectBuildPath) {
		String commands[] = {};
		
		if (System.getProperty("os.name").equals("Linux")) {
			commands = new String[] { cmakeCommandPath, projectPath};
		}
		else if (System.getProperty("os.name").startsWith("Windows")) {
			String OpenFLUIDInstallPrefix = System.getenv("OPENFLUID_INSTALL_PREFIX");								
			
			if (!OpenFLUIDInstallPrefix.isEmpty()) {

				commands = new String[] { 
						cmakeCommandPath, 
						projectPath,
						"-G \"MinGW Makefiles\"",
						"-DOpenFLUID_DIR=" + OpenFLUIDInstallPrefix + "/lib/cmake",
						"-DBOOST_ROOT=" + OpenFLUIDInstallPrefix};
			}
		}
		
		
		if (commands.length > 0)
		{
			try {
				// Aggregate commands as a single string
				String commandStr = "";

				for (String s : commands) {  
					commandStr += s + " ";
				}  

				System.out.println("CMD: "+commandStr);				
				System.out.println("CWD: "+projectBuildPath);
				
				Process p = 
						Runtime.getRuntime().exec(commandStr,null,
								new File(projectBuildPath));
				
				OpenFLUIDExecStream outStream = new OpenFLUIDExecStream(p.getInputStream());
				OpenFLUIDExecStream errorStream = new OpenFLUIDExecStream(p.getErrorStream());

				new Thread(outStream).start();
				new Thread(errorStream).start();

				p.waitFor();

			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    	
    }

	
}
