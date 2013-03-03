package com.neustar.dece;

import java.io.BufferedReader;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.HashMap;

/**
 * This class provides various File related utilities
 * 
 * @author Narayan Partangel 02/28/2011
 * @version 1.0
 */

public class FileUtils {

	/**
	 * Retrieve the contents of a file in a bte array.
	 * 
	 * @param file
	 *            a <code>File</code> value
	 * @return a <code>byte[]</code> value
	 */
	private static final Logger logger = Logger.getLogger(FileUtils.class
			.getName());

	public static String getFileContents(String location) throws IOException {
		return getFileContents(new File(location));
	}

	public static String getFileContents(File file) throws IOException {
		String line;
		StringBuffer outputBuf = new StringBuffer();
		InputStream is = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		while ((line = br.readLine()) != null) {
			outputBuf.append(line);
			outputBuf.append('\n');
		}
		is.close();
		return outputBuf.toString();
	}

	public static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();

		if (length > Integer.MAX_VALUE) {
			return null;
		}
		byte[] bytes = new byte[(int) length];

		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			throw new IOException("Cannot read file " + file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}

	/**
	 * Retrieve the contents of a file in a String.
	 * 
	 * @param file
	 *            a <code>File</code> value
	 * @return a <code>String</code> value
	 */

	public static String getStringFromFile(File file) throws IOException {
		byte[] bytes = getBytesFromFile(file);
		if (bytes == null)
			return new String("");
		return bytes.toString();
	}

	/**
	 * Determine if a File is older than n days.
	 * 
	 * @param file
	 *            a <code>File</code> value
	 * @param days
	 *            a <code>int</code> value
	 * @return a <code>boolean</code> value
	 */
	public static boolean isOlder(File file, int days) {
		return isOlder(file.lastModified(), days);
	}

	public static boolean isOlder(long lastModified, int days) {
		long systemTime = new Date().getTime();
		logger.fine("System Time=" + systemTime);
		logger.fine("lastModified Time=" + lastModified);
		logger.fine("Days=" + days);

		logger.fine("Difference=" + (systemTime - lastModified));
		logger.fine("Days Difference=" + (systemTime - lastModified)
				/ (1000 * 60 * 60 * 24));
		if (((systemTime - lastModified) / (1000 * 60 * 60 * 24)) > days)
			return true;
		return false;
	}

/*
	public static void writeFile(String outDirectory, String fileContents,
			String inFileName) throws Exception {
		System.out.println(outDirectory + "/" + inFileName);
		if (inFileName.endsWith(".csv")) {
			inFileName = StringUtils.replaceAll(inFileName, ".csv", ".sql");
		}
		FileWriter fw = new FileWriter(outDirectory + "/" + inFileName);

		fw.write(fileContents);
		fw.flush();
		fw.close();
	}
*/	
	public static void closeFileHandlers (HashMap fileHash) throws IOException{
		
        //close out all the files
		Iterator i = fileHash.keySet().iterator();
		Object keyObj;
		FileWriter obj;
		while (i.hasNext()) {
			keyObj = i.next();
			if (keyObj != null) {
				obj = (FileWriter)fileHash.get(keyObj);
				obj.flush();
				obj.close();    				    				
			}
		}
	}
}
