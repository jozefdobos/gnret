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

/* <!-- in case someone opens this in a browser... --> <pre> */
import org.apache.commons.net.MalformedServerReplyException;
import org.apache.commons.net.ftp.*;

import eu.dobos.jozef.gnret.filehandling.FileReadingHandler;
import eu.dobos.jozef.gnret.gui.utils.Settings;

import java.util.Vector;
import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * This is a simple wrapper around the Jakarta Commons FTP library. I really
 * just added a few convenience methods to the class to suit my needs and make
 * my code easier to read.
 * <p>
 * If you want more information on the Jakarta Commons libraries (there is a LOT
 * more you can do than what you see here), go to:
 * http://jakarta.apache.org/commons
 * <p>
 * This Java class requires both the Jakarta Commons Net library and the Jakarta
 * ORO library (available at http://jakarta.apache.org/oro ). Make sure you have
 * both of the jar files in your path to compile. Both are free to use, and both
 * are covered under the Apache license that you can read on the apache.org
 * website. If you plan to use these libraries in your applications, please
 * refer to the Apache license first. While the libraries are free, you should
 * double-check to make sure you don't violate the license by using or
 * distributing it (especially if you use it in a commercial application).
 * <p>
 * Program version 1.0. Author Julian Robichaux, http://www.nsftools.com
 * 
 * @author Julian Robichaux ( http://www.nsftools.com )
 * @version 1.0
 */

public class JakartaFtpWrapper extends FTPClient {

	private String serverName;
	private String userName;
	private String password;
	private Integer port;
	public static final int MAX_FTP_CONNECTION_ATTEMPTS = 5;
	public static final int FTP_CONNECTION_TIMEOUT = 300000;  // 5 min
	public static final int FTP_DATA_TIMEOUT = 150000; // 2.5 min
	public static String ESTIMATED_SEPARATOR;
	private String rootDir = new String();
	

	public JakartaFtpWrapper(String serverName, Integer port, String userName,
			String password, boolean pasvOn) throws SocketException {
		this.serverName = serverName;
		this.userName = userName;
		this.password = password;
		this.port = port;
		
		setConnectTimeout(FTP_CONNECTION_TIMEOUT);		
		this.setPassiveMode(pasvOn);
//		setDataTimeout(FTP_DATA_TIMEOUT);
	}

	public String getServerName() {
		return serverName;
	}

	public static final void setEstimatedSeparator(String s) {
		ESTIMATED_SEPARATOR = s;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getUserName() {
		return userName;
	}

	public boolean connectAndLogin() throws UnknownHostException,
			FTPConnectionClosedException, IOException, SocketException {
		return connectAndLogin(serverName, port, userName, password);
	}

	/** A convenience method for connecting and logging in */
	public boolean connectAndLogin(String host, Integer port, String userName,
			String password) throws IOException, UnknownHostException,
			FTPConnectionClosedException, SocketException {
		boolean success = false;

		if (port == null)
			connect(host);
		else
			connect(host, port);

		int reply = getReplyCode();
		if (FTPReply.isPositiveCompletion(reply))
			success = login(userName, password);
		if (!success)
			disconnect();
		else {
			this.ascii();
			
			rootDir = printWorkingDirectory();
		}
		return success;
	}

	public boolean changeToRootDir() throws IOException, SocketException {
		// changeWorkingDirectory(rootDir);
		/*
		 * if (!rootDir.isEmpty()) { String workingDir =
		 * printWorkingDirectory(); if (!workingDir.isEmpty()) { while
		 * (rootDir.toString().length() < workingDir.length()) {
		 * changeToParentDirectory(); } } }
		 */
		// return !rootDir.isEmpty() && rootDir.equals(printWorkingDirectory());
		return changeWorkingDirectory(rootDir);
	}

	public boolean isInRootDir() throws IOException, SocketException {
		return rootDir.toString().equals(printWorkingDirectory());
	}

	public boolean isInDefaultDir() throws IOException, SocketException {
		if (!isConnected()) {
			disconnect();
			connectAndLogin();
		}
		String currentDir = printWorkingDirectory();
		return (!isInRootDir()
				&& currentDir != null
				&& ((rootDir.length() + 1 + Settings.DEFAULT_FOLDER.length()) == currentDir
						.length()) && currentDir
				.indexOf(Settings.DEFAULT_FOLDER) >= 0);
	}

	/**
	 * Turn passive transfer mode on or off. If Passive mode is active, a PASV
	 * command to be issued and interpreted before data transfers; otherwise, a
	 * PORT command will be used for data transfers. If you're unsure which one
	 * to use, you probably want Passive mode to be on.
	 */
	public void setPassiveMode(boolean setPassive) {
		if (setPassive)
			enterLocalPassiveMode();
		else
			enterLocalActiveMode();
	}

	/** Use ASCII mode for file transfers */
	public boolean ascii() throws IOException {
		return setFileType(FTP.ASCII_FILE_TYPE);
	}

	/** Use Binary mode for file transfers */
	public boolean binary() throws IOException {
		return setFileType(FTP.BINARY_FILE_TYPE);
	}

	/** Download a file from the server, and save it to the specified local file */
	public boolean downloadFile(String serverFile, String localFile)
			throws IOException, FTPConnectionClosedException, SocketException {
		FileOutputStream out = new FileOutputStream(localFile);
		boolean result = retrieveFile(serverFile, out);
		out.close();
		return result;
	}

	/***
	 * Returns empty string if file transfer not successful
	 * 
	 * @param serverFile
	 * @return
	 * @throws IOException
	 */
	public String getFileAsString(String serverFile) throws IOException {
		InputStream inputStream = retrieveFileStream(serverFile);
		String theString = FileReadingHandler
				.convertStreamToString(inputStream);
		if (!completePendingCommand()) // file transfer not successful
			theString = new String(); // empty string
		return theString;
	}

	/** Upload a file to the server */
	public boolean uploadFile(String localFile, String serverFile)
			throws IOException, FTPConnectionClosedException, SocketException {
		FileInputStream in = new FileInputStream(localFile);
		boolean result = storeFile(serverFile, in);
		in.close();
		return result;
	}

	/**
	 * Get the list of files in the current directory as a Vector of Strings
	 * (excludes subdirectories)
	 */
	public Vector<String> listFileNames() throws IOException,
			FTPConnectionClosedException, ConnectException, SocketException {
		FTPFile[] files = listFiles();
		Vector<String> v = new Vector<String>();
		for (int i = 0; i < files.length; i++) {
			if (!files[i].isDirectory())
				v.addElement(files[i].getName());
		}
		return v;
	}

	/**
	 * Get the list of files in the current directory as a single Strings,
	 * delimited by \n (char '10') (excludes subdirectories)
	 */
	public String listFileNamesString() throws IOException,
			FTPConnectionClosedException, ConnectException, SocketException {
		return vectorToString(listFileNames(), "\n");
	}

	/**
	 * Get the list of subdirectories in the current directory as a Vector of
	 * Strings (excludes files)
	 */
	public Vector<String> listSubdirNames() throws IOException,
			FTPConnectionClosedException, ConnectException,
			MalformedServerReplyException, SocketException {
		FTPFile[] files = listFiles();
		Vector<String> v = new Vector<String>();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory())
				v.addElement(files[i].getName());
		}
		return v;
	}

	/**
	 * Get the list of subdirectories in the current directory as a single
	 * Strings, delimited by \n (char '10') (excludes files)
	 */
	public String listSubdirNamesString() throws IOException,
			FTPConnectionClosedException, SocketException {
		return vectorToString(listSubdirNames(), "\n");
	}

	/** Convert a Vector to a delimited String */
	private static String vectorToString(Vector<String> v, String delim) {
		StringBuffer sb = new StringBuffer();
		String s = "";
		for (int i = 0; i < v.size(); i++) {
			sb.append(s).append(v.elementAt(i));
			s = delim;
		}
		return sb.toString();
	}

	public boolean tryToReconnectToFTP(boolean changeToDefaultServerDirectory)
			throws UnknownHostException, FTPConnectionClosedException,
			IOException, SocketException {
		boolean ret = false;
		if (ret = connectAndLogin()) {
			if (changeToDefaultServerDirectory)
				changeWorkingDirectory(Settings.DEFAULT_FOLDER);
		}
		return ret;
	}

	/*
	 * public boolean tryToReconnectToFTP(JFrame frame, boolean
	 * changeToDefaultServerDirectory, int attempts) {
	 * 
	 * boolean ret = false; if (attempts > 0) { try {
	 * ftpConnectionAttemptsCounter++; connectAndLogin();
	 * ftpConnectionAttemptsCounter = 0; // reset counter, connection //
	 * successful if (changeToDefaultServerDirectory)
	 * changeWorkingDirectory(Settings.DEFAULT_FOLDER); } catch (Exception e2) {
	 * String titleDescription = "Error: " + MAX_FTP_CONNECTION_ATTEMPTS +
	 * " recconection attempts to " + getServerName() + " failed"; if
	 * (ftpConnectionAttemptsCounter >= MAX_FTP_CONNECTION_ATTEMPTS && 0 ==
	 * Utils.showRetryCancelErrorDialog(frame, e2 .getMessage() + "\n" +
	 * titleDescription, titleDescription)) { ftpConnectionAttemptsCounter = 0;
	 * // reset counter tryToReconnectToFTP(frame,
	 * changeToDefaultServerDirectory, attempts); } else if
	 * (ftpConnectionAttemptsCounter < MAX_FTP_CONNECTION_ATTEMPTS)
	 * tryToReconnectToFTP(frame, changeToDefaultServerDirectory, attempts); } }
	 * return ret; }
	 */
	public static String getClientMacAddress() throws UnknownHostException,
			SocketException {
		InetAddress address = InetAddress.getLocalHost();
		NetworkInterface ni = NetworkInterface.getByInetAddress(address);
		byte[] mac = ni.getHardwareAddress();
		String ret = "";
		for (int i = 0; i < mac.length; i++) {
			ret += String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-"
					: "");
		}
		return ret;
	}

	public static String getClientIPAddress() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress();
	}

	public static String getClientHostName() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostName();
	}

	public static boolean canStart(JakartaFtpWrapper ftp)
			throws FTPConnectionClosedException, IOException, SocketException {
		if (!ftp.isInDefaultDir()) {
			ftp.changeToRootDir();
			ftp.changeWorkingDirectory(Settings.DEFAULT_FOLDER);
		}
		Vector<String> subdirs = ftp.listSubdirNames();
		return subdirs.contains(Settings.START_EXPERIMENT);
	}

	public static boolean canProceed(JakartaFtpWrapper ftp, int round)
			throws FTPConnectionClosedException, IOException, SocketException {
		if (!ftp.isConnected()) {
			ftp.tryToReconnectToFTP(false);
		}
		if (!ftp.isInDefaultDir()) {
			ftp.changeToRootDir();
			ftp.changeWorkingDirectory(Settings.DEFAULT_FOLDER);
		}
		Vector<String> subdirs = ftp.listSubdirNames();
		boolean ret;
		if (round == 0) {
			ret = subdirs.contains(Settings.PRACTICE_ROUND);
		} else {
			ret = subdirs.contains(Settings.PAYING_ROUND + " " + round);
		}
		return ret;
	}

	/***
	 * Checks connection (reconnects if necessary) and sets working default dir
	 * if not already in
	 * 
	 * @param ftp
	 * @throws IOException
	 */
	public static void setToDefaultDir(JakartaFtpWrapper ftp)
			throws IOException, SocketException {
		if (!ftp.isConnected()) {
			ftp.connectAndLogin();
		}
		if (!ftp.isInDefaultDir()) {
			ftp.changeToRootDir();
			ftp.changeWorkingDirectory(Settings.DEFAULT_FOLDER);
		}
	}
}
