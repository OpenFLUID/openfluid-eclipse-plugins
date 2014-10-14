package org.lisah.openfluid.common;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;


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


	private static MessageConsole findConsole(String name) {

		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		
		// search for existing console
		for (int i = 0; i < existing.length; i++) {
			if (name.equals(existing[i].getName())) {
				return (MessageConsole) existing[i];
			}
		}
		
		// no console found, so create a new one
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { myConsole });		
		return myConsole;
	}


	static public void runCMakeCommand(String cmakeCommandPath, 
			String projectPath, String projectBuildPath,
			boolean bringConsoleToFront) {
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

				// create or set active console
				MessageConsole cmakeConsole = findConsole("CMake on OpenFLUID ware");
				cmakeConsole.clearConsole();

				if (bringConsoleToFront) {
					// bring console to front
					IWorkbench wb = PlatformUI.getWorkbench();
					IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
					IWorkbenchPage page = win.getActivePage();		 
					String id = IConsoleConstants.ID_CONSOLE_VIEW;
					IConsoleView view = null;
					try {
						view = (IConsoleView) page.showView(id);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
					view.display(cmakeConsole);
				}

				MessageConsoleStream consoleOut = cmakeConsole.newMessageStream();

				
				// prepare CMake process to execute
				Process p = 
						Runtime.getRuntime().exec(commandStr,null,
								new File(projectBuildPath));

				OpenFLUIDExecStream outStream = new OpenFLUIDExecStream(p.getInputStream(),consoleOut);
				OpenFLUIDExecStream errorStream = new OpenFLUIDExecStream(p.getErrorStream(),consoleOut);

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
