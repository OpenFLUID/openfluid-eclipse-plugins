package org.lisah.openfluid.common;
import java.io.BufferedReader;
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
                        

            if (cmakeVersionStrTokens.length == 3 &&  cmakeVersionStrTokens[0].equals("cmake")) {
            	String[] cmakeVersionNbrTokens = cmakeVersionStrTokens[2].split("\\.");            	

            	try {
            		if (cmakeVersionNbrTokens.length == 3 && Integer.parseInt(cmakeVersionNbrTokens[0]) >= 2 && Integer.parseInt(cmakeVersionNbrTokens[1]) >= 8) {
            			foundCMakeCommand = cmakeProgram;		
            		}
            	}
            	catch (NumberFormatException nfe) {
            		System.out.println("cmake not found (wrong version format)");
            	}
            }	

		} catch (IOException e) {
			System.out.println("cmake not found");			
		}
		
		return foundCMakeCommand;
		
	}
	
}
