package com.zehfernando.net.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Debug;

import com.zehfernando.data.config.PersistentData;
import com.zehfernando.utils.F;
import com.zehfernando.utils.StringUtils;

public class FileCache {

	// Static properties
	private static ArrayList<FileCache> caches = new ArrayList<FileCache>();

	// Properties
	private final String id;
	private String uniqueId;				// Id used for subfolder name

	private long totalSize;					// Size of the cache, in bytes
	private int numFiles;					// Number of files in the cache
	private boolean fileListStatsDirty;		// If true, the number of files and file size in the cache is not known

	private File baseFolder;

	private PersistentData expirationDates;	// Date the file expires, if any, in ms

	// ================================================================================================================
	// STATIC INTERFACE -----------------------------------------------------------------------------------------------

	public static FileCache getFileCache(Context __context) {
		return getFileCache(__context, "");
	}

	public static FileCache getFileCache(Context __context, String __id) {
		int i;

		// Try to find the cache
		for (i = 0; i < caches.size(); i++) {
			if (caches.get(i).getId() == __id) return caches.get(i);
		}

		// Create in case it doesn't exist
		return new FileCache(__context, __id);
	}

	public static void addFileCache(FileCache __fileCache) {
		caches.add(__fileCache);
	}

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public FileCache(Context __context) {
		id = "";
		initialize(__context);
	}

	public FileCache(Context __context, String __id) {
		id = __id;
		initialize(__context);
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	private void initialize(Context __context) {
		//uniqueId = StringUtils.calculateMD5(id);
		//if (uniqueId == null) uniqueId = id.replace("/","_").replace(" ","_").replace("\\","_").replace(":","_");
		uniqueId = id == null ? "" : id.replace("/","_").replace(" ","_").replace("\\","_").replace(":","_");

		// Find the base folder

		// * Saved on internal memory, as part of the application data
		// * Cleared by the system if needed
		// * Goes to the "cache" area of the application data on Android's application data management screen
		// Equivalent to "/data/data/com.redken.stylestation/cache"
		// http://developer.android.com/reference/android/content/Context.html#getCacheDir()

		//if (id == "") return context.getCacheDir();

		// If a different id, use a subfolder
		//File baseFolder = new File(context.getCacheDir(), uniqueId);

		baseFolder = new File(__context.getCacheDir(), id.length() == 0 ? "defaultFileCache" : uniqueId);

		// * Saved on external memory
		// * Not cleared by the system
		// * Unavailable when SD card is mounted
		// * Requires WRITE_EXTERNAL_STORAGE permission
		// * Doesn't go to the "cache" area of the application data on Android's application data management screen
		// Equivalent to "/mnt/sdcard/Android/data/com.redken.stylestation/cache"
		// http://developer.android.com/reference/android/content/Context.html#getExternalCacheDir()
		//return context.getExternalCacheDir();

		totalSize = 0;
		numFiles = 0;
		fileListStatsDirty = true;

		FileCache.addFileCache(this);

		expirationDates = new PersistentData(__context, "filecache_expiration_" + uniqueId);
	}

	private String getFileName(String __id) {
		// Based on the file id, return the file location
//		String filename = __id;
//		filename = filename.replace("\\", "_");
//		filename = filename.replace("/", "_");
//		filename = filename.replace("?", "_");
//		filename = filename.replace("=", "_");
//		filename = filename.replace("&", "_");
//		filename = filename.replace(":", "_");
//		filename = filename.replace(" ", "_");
//		filename = filename.replace("+", "_");
		//Log.v("FileCache", "File [" + __id + "] using the filename [" + filename + "]");
//		return filename;

		String filename = StringUtils.calculateMD5(__id);
		if (filename == null) filename = __id.replace("\\", "_").replace("/", "_").replace("?", "_").replace("=", "_").replace("&", "_").replace(":", "_").replace(" ", "_").replace("+", "_");

		return filename;
	}

	private byte[] toByteArray(InputStream __inputStream) {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] fdata = new byte[16384];

		try {
			while ((nRead = __inputStream.read(fdata, 0, fdata.length)) != -1) {
				buffer.write(fdata, 0, nRead);
			}
			buffer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return buffer.toByteArray();
	}

	private void updateFileListStats(long __bytesToAdd, int __filesToAdd) {
		// Updates the file list as a file is added or removed
		// This is quicker than recalculating the whole list
		// Only updates if the current stats are up-to-date; otherwise just let the list remain dirty for the time being

		if (!fileListStatsDirty) {
			// The list is up-to-date, just add the new data
			totalSize += __bytesToAdd;
			numFiles += __filesToAdd;
		}

		// The list stats is dirty anyway, recalculate everything
		//recalculateFileListStats();
	}

	private void recalculateFileListStats() {
		// Recalculates list data: total file size and file number

		long ti = System.currentTimeMillis();

		totalSize = 0;
		numFiles = 0;
		File[] files = getCacheDir().listFiles();

		if (files == null) {
			F.warn("Cache dir " + getCacheDir() + " is not a directory!");
			fileListStatsDirty = false;
			return;
		}

		for (File f:files) {
			totalSize += f.length();
			numFiles++;
			//f.delete();
		}

		fileListStatsDirty = false;

		F.debug("Took " + (System.currentTimeMillis() - ti) + "ms to refresh the cache file size.");
	}

	private Long getFileExpirationTimeByFilename(String __fileName) {
		return expirationDates.getLong(__fileName, 0L);
	}

	private void setFileExpirationTimeByFilename(String __fileName, long __time) {
		expirationDates.putLong(__fileName, __time);
	}

	private void removeFileExpirationTimeByFilename(String __fileName) {
		expirationDates.remove(__fileName);
	}

	private boolean deleteFileIfExpired(String __id) {
		// Checks if a file is expired, deleting it if needed
		File file = getFileForId(__id, true);

		if (file.exists()) {

			long now = System.currentTimeMillis();
			long expDate = getFileExpirationTime(__id);

			if (expDate > 0 && expDate < now) {
				// File is old and must be deleted
				deleteFile(__id);
				return true;
			}
		}

		return false;
	}

	private File getFileForId(String __id) {
		return getFileForId(__id, false);
	}

	private File getFileForId(String __id, boolean __bypassExpirationCheck) {
		if (!__bypassExpirationCheck) deleteFileIfExpired(__id);
		return new File(getCacheDir(), getFileName(__id));
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public boolean getFileExists(String __id) {
		File file = getFileForId(__id);
		// If file size is 0, ignores it because it may be a faulty record
		return file.exists() && file.length() > 0;
	}

	public long getFileTime(String __id) {
		File file = getFileForId(__id);
		return file.lastModified();
	}

	public Long getFileExpirationTime(String __id) {
		return getFileExpirationTimeByFilename(getFileName(__id));
	}

	public void setFileExpirationTime(String __id, long __time) {
		setFileExpirationTimeByFilename(getFileName(__id), __time);
	}

	public void setFileExpirationTimeRelativeToNow(String __id, long __time) {
		// __time should be positive, and in ms
		setFileExpirationTimeByFilename(getFileName(__id), System.currentTimeMillis() + __time);
	}

	public void removeFileExpirationTime(String __id) {
		removeFileExpirationTimeByFilename(getFileName(__id));
	}

	public boolean touchFile(String __id) {
		return touchFile(__id, false);
	}

	public boolean touchFile(String __id, boolean __pushExpirationDate) {
		// "Touches" a file, changing its last modified date for today

		File file = getFileForId(__id);
		if (!file.exists()) return false;

		long newDate = System.currentTimeMillis();
		long lastDate = file.lastModified();

		boolean success = file.setLastModified(newDate);
		if (__pushExpirationDate) {
			// Pushes the file expiration dates forward by the same amount, if it has any
			long dateOffset = newDate - lastDate;
			long expDate = getFileExpirationTime(__id);
			if (expDate > 0) setFileExpirationTime(__id, expDate + dateOffset);
		}
		return success;
	}

	public String getFilePath(String __id) {
		// Return a direct file location
		File file = getFileForId(__id);
		return file.getPath();
	}

	public String getFileAsString(String __id) {
		FileInputStream input = getFile(__id);

		if (input != null) {
			StringBuffer fileContent = new StringBuffer("");

			byte[] buffer = new byte[1024];
			try {
				int length;
				while ((length = input.read(buffer)) != -1) {
					fileContent.append((new String(buffer)).substring(0, length));
				}
			} catch (IOException __e) {
				F.warn("Error reading file as string!");
				return null;
			}

			return fileContent.toString();
		}

		return null;
	}

	public FileInputStream getFile(String __id) {
		// Based on a file id, return the file input stream
		FileInputStream input = null;
		try {
			File file = getFileForId(__id);
			input = new FileInputStream(file);

			//F.info("File " + getFileName(__id) + " returned from " + getCacheDir().getAbsolutePath());
		} catch (Exception __e) {
			F.error("Error trying to open file! " + __e);
		}
		return input;
	}

//	public String getFileAsString(String __id) {
//		int ch;
//		StringBuffer buffer = new StringBuffer();
//		FileInputStream fis = getFile(__id);
//		InputStreamReader isr;
//
//		try {
//			isr = new InputStreamReader(fis, "UTF8");
//			Reader in = new BufferedReader(isr);
//			while ((ch = in.read()) > -1) {
//				buffer.append((char)ch);
//			}
//			in.close();
//		} catch (Exception __e) {
//			Log.e("FileCache", "Error trying to read file! " + __e);
//		}
//		return buffer.toString();
//	}

	public void putFile(String __id, String __content) {
		putFile(__id, new ByteArrayInputStream(__content.getBytes()));
	}

	public void putFile(String __id, InputStream __content) {
		putFile(__id, toByteArray(__content));
	}

	public void putFile(String __id, Bitmap __bitmap, CompressFormat __format, int __quality)  {
		if (getFileExists(__id)) deleteFile(__id);

		File file = getFileForId(__id);
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(file);
		} catch (FileNotFoundException __e) {
			F.error("Error trying to write file: File not found!");
			F.error(__e.toString());
		}

		if (output != null && __bitmap != null && __format != null) {
			__bitmap.compress(__format, __quality, output);
			updateFileListStats(file.length(), 1);

			F.debug("File " + __id + " saved as " + getFileName(__id) + " on " + getCacheDir().getAbsolutePath());
			F.debug("Total file cache size is " + getTotalSize() + " in " + getNumFiles() + " files");

			System.gc();
		}

		// TODO: delete files based on cache limit size?
	}

	public void putFile(String __id, byte[] __content) {
		// Record file
		if (getFileExists(__id)) deleteFile(__id);

		try {
			File file = getFileForId(__id);

			//F.log("===> trying to write " + __content.length + " bytes to " + file);
			//F.log("===> location is "  + getCacheDir() + " as exists = " + getCacheDir().exists());
			FileOutputStream output = new FileOutputStream(file);
			output.write(__content);
			output.close();

			//F.log("===> wrote file with size "  + file.length() + " bytes");
			updateFileListStats(__content.length, 1);
		} catch (Exception __e) {
			F.error("Error trying to write file!");
			F.error(__e.toString());
		}

		F.debug("File " + __id + " saved as " + getFileName(__id) + " on " + getCacheDir().getAbsolutePath());
		//F.debug("Total file cache size is " + getTotalSize() + " in " + getNumFiles() + " files");

		// TODO: delete files based on cache limit size?
	}

	public boolean deleteFile(String __id) {
		// Removes a file from the cache; returns true if deleted

		// Removes file
		File file = getFileForId(__id, true);
		long fileSize = file.length();
		boolean wasDeleted = file.delete();

		if (wasDeleted) updateFileListStats(-fileSize, -1);

		removeFileExpirationTime(__id);

		return wasDeleted;
	}

	public void deleteAllFiles() {
		File[] files = getCacheDir().listFiles();

		if (files == null) {
			F.warn("Cache dir " + getCacheDir() + " is not a directory!");
			return;
		}

		for (File f:files) f.delete();

		totalSize = 0;;
		numFiles = 0;;
		fileListStatsDirty = false;
	}

	public void deleteExpiredFiles() {
		// Removes files that have expired

		F.debug("Trimming expired files from cache [" + id + "]");

		long now = System.currentTimeMillis();
		long expDate;

		int bNumFiles = getNumFiles();
		long bTotalSize = getTotalSize();

		int filesDeleted = 0;
		long bytesDeleted = 0;
		boolean wasDeleted;
		long fileSize;

		File[] files = getCacheDir().listFiles();

		if (files == null) {
			F.warn("Cache dir " + getCacheDir() + " is not a directory!");
			return;
		}

		for (File f:files) {

			expDate = getFileExpirationTimeByFilename(f.getName());

			if (expDate > 0 && expDate < now) {
				// Delete the file
				if (Debug.isDebuggerConnected()) F.warn("      Deleting file ["+f.getName()+"] created in " + new Date(f.lastModified()).toString() + " with an exp date of" +new Date(expDate).toString());

				fileSize = Debug.isDebuggerConnected() ? 0 : f.length();
				wasDeleted = f.delete();

				if (wasDeleted) {
					bytesDeleted += fileSize;
					filesDeleted++;
					fileListStatsDirty = true;
				}
			}
		}

		F.debug("   " + filesDeleted + " files and " + bytesDeleted + " bytes deleted (of " + bNumFiles + " files and " + bTotalSize + " bytes)");

		F.debug("   Took " + (System.currentTimeMillis() - now) + "ms to trim file cache");
	}

	public void deleteFilesByAge(long __days) {
		// Delete files from the cache based on how old they are

		deleteExpiredFiles();

		F.debug("Erasing files from cache [" + id + "] that are " + __days + " or more days old");
		long now = System.currentTimeMillis();
		long maxAge = __days * 24 * 60 * 60 * 1000;

		//if (Debug.isDebuggerConnected()) maxAge /= 10;

		long cutTime = now - maxAge;

		F.debug("   Time now is " + new Date(now).toString() + "; erasing files created before " + new Date(cutTime).toString());

		int bNumFiles = getNumFiles();
		long bTotalSize = getTotalSize();

		int filesDeleted = 0;
		long bytesDeleted = 0;
		boolean wasDeleted;
		long fileSize;

		File[] files = getCacheDir().listFiles();

		if (files == null) {
			F.warn("Cache dir " + getCacheDir() + " is not a directory!");
			return;
		}

		for (File f:files) {

			if (f.lastModified() < cutTime) {
				// Delete the file
				if (Debug.isDebuggerConnected()) F.warn("      Deleting file ["+f.getName()+"] created in " + new Date(f.lastModified()).toString());

				fileSize = Debug.isDebuggerConnected() ? 0 : f.length();
				wasDeleted = f.delete();

				if (wasDeleted) {
					bytesDeleted += fileSize;
					filesDeleted++;
					fileListStatsDirty = true;
				}
			}
		}

		F.debug("   " + filesDeleted + " files and " + bytesDeleted + " bytes deleted (of " + bNumFiles + " files and " + bTotalSize + " bytes)");

		F.debug("   Took " + (System.currentTimeMillis() - now) + "ms to trim file cache");

	}


	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public String getId() {
		return id;
	}

	public File getCacheDir() {
		if (!baseFolder.exists()) {
			F.warn("Cache base folder [" + baseFolder + "] didn't exist! re-creating!!!");
			baseFolder.mkdir();
		}
		return baseFolder;
	}

	public long getTotalSize() {
		if (fileListStatsDirty) recalculateFileListStats();
		return totalSize;
	}

	public int getNumFiles() {
		if (fileListStatsDirty) recalculateFileListStats();
		return numFiles;
	}

}
