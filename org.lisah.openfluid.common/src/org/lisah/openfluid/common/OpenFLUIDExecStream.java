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

package org.lisah.openfluid.common;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;


public class OpenFLUIDExecStream implements Runnable {

	private final InputStream inputStream;
	private final OutputStream outputStream;

	OpenFLUIDExecStream(InputStream inputStream,OutputStream outputStream) {
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}

	private BufferedReader getBufferedReader(InputStream is) {
		return new BufferedReader(new InputStreamReader(is));
	}

	
	@Override
	public void run() {
		
		BufferedReader br = getBufferedReader(inputStream);
		PrintStream ps = new PrintStream(outputStream);
		
		String line = "";
		try {
			while ((line = br.readLine()) != null) {
				ps.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
