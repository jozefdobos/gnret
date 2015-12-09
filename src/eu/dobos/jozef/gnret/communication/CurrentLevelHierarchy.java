/**
*  Copyright (C) 2011 Jozef Dobos
*
*  This program is free software: you can redistribute it and/or modify
*  it under the terms of the GNU Affero General Public License as
*  published by the Free Software Foundation, either version 3 of the
*  License, or (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU Affero General Public License for more details.
*
*  You should have received a copy of the GNU Affero General Public License
*  along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package eu.dobos.jozef.gnret.communication;

import java.io.IOException;
import java.util.Vector;

import org.apache.commons.net.ftp.FTPConnectionClosedException;

public class CurrentLevelHierarchy {

	private String workingDirectory = new String();
	private Vector<String> fileNames = new Vector<String>();
	private Vector<String> subdirNames = new Vector<String>();
	
	public void populate(JakartaFtpWrapper ftp) throws FTPConnectionClosedException, IOException {
		this.workingDirectory = ftp.printWorkingDirectory();
		this.fileNames = ftp.listFileNames();
		this.subdirNames = ftp.listSubdirNames();
	}

	public String getWorkingDirectory() {
		return workingDirectory;
	}

	public void setWorkingDirectory(String workingDirectory) {
		this.workingDirectory = workingDirectory;
	}

	public Vector<String> getFileNames() {
		return fileNames;
	}

	public void setFileNames(Vector<String> fileNames) {
		this.fileNames = fileNames;
	}

	public Vector<String> getSubdirNames() {
		return subdirNames;
	}

	public void setSubdirNames(Vector<String> subdirNames) {
		this.subdirNames = subdirNames;
	}
}
