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
import android.util.Log;

import com.redkenfb.stylestation.ApplicationConstants;
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
	private boolean fileListStatsDirty;			// If true, the number of files and file size in the cache is not known

	private File baseFolder;

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
		uniqueId = id.replace("/","_").replace(" ","_").replace("\\","_").replace(":","_");

		// Find the base folder

		// * Saved on internal memory, as part of the application data
		// * Cleared by the system if needed
		// * Goes to the "cache" area of the application data on Android's application data management screen
		// Equivalent to "/data/data/com.redken.stylestation/cache"
		// http://developer.android.com/reference/android/content/Context.html#getCacheDir()

		//if (id == "") return context.getCacheDir();

		// If a different id, use a subfolder
		//File baseFolder = new File(context.getCacheDir(), uniqueId);

		baseFolder = new File(__context.getCacheDir(), id.length() == 0 ? "none" : uniqueId);
		if (!baseFolder.exists()) baseFolder.mkdir();

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

		if (fileListStatsDirty) {
			// The list stats is dirty anyway, recalculate everything
			recalculateFileListStats();
		} else {
			// The list is up-to-date, just add the new data
			totalSize += __bytesToAdd;
			numFiles += __filesToAdd;
		}
	}

	private void recalculateFileListStats() {
		// Recalculates list data: total file size and file number

		long ti = System.currentTimeMillis();

		totalSize = 0;
		numFiles = 0;
		File[] files = getCacheDir().listFiles();

		for (File f:files) {
			totalSize += f.length();
			numFiles++;
			//f.delete();
		}

		fileListStatsDirty = false;

		Log.v("FileCache", "Took " + (System.currentTimeMillis() - ti) + "ms to refresh the cache file size.");
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public boolean getFileExists(String __id) {
		File file = new File(getCacheDir(), getFileName(__id));
		// If file size is 0, ignores it because it may be a faulty record
		return file.exists() && file.length() > 0;
	}

	public long getFileTime(String __id) {
		File file = new File(getCacheDir(), getFileName(__id));
		return file.lastModified();
	}

	public boolean touchFile(String __id) {
		// "Touches" a file, changing its date for today
		File file = new File(getCacheDir(), getFileName(__id));
		if (!file.exists()) return false;
		return file.setLastModified(System.currentTimeMillis());
	}

	public String getFilePath(String __id) {
		// Return a direct file location
		File file = new File(getCacheDir(), getFileName(__id));
		return file.getPath();
	}

	public FileInputStream getFile(String __id) {
		FileInputStream input = null;
		try {
			File file = new File(getCacheDir(), getFileName(__id));
			input = new FileInputStream(file);

			Log.i("FileCache", "getFile() :: File " + getFileName(__id) + " returned from " + getCacheDir().getAbsolutePath());
			//fis = context.openFileInput(getFileName(__id));
		} catch (Exception __e) {
			Log.e("FileCache", "Error trying to open file! " + __e);
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
		File file = new File(getCacheDir(), getFileName(__id));
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(file);
		} catch (FileNotFoundException __e) {
			Log.e("FileCache", "Error trying to write file: file not found! " + __e);
		}

		if (output != null && __bitmap != null && __format != null) {
			__bitmap.compress(__format, __quality, output);
			updateFileListStats(file.length(), 1);

			Log.i("FileCache", "putFile() :: File " + __id + " saved as " + getFileName(__id) + " on " + getCacheDir().getAbsolutePath());
			Log.i("FileCache", "putFile() :: Total file cache size is " + getTotalSize() + " in " + getNumFiles() + " files");

			System.gc();
		}

		// TODO: delete files based on cache limit size?
	}

	public void putFile(String __id, byte[] __content) {
		// Record file
		try {
			File file = new File(getCacheDir(), getFileName(__id));
			FileOutputStream output = new FileOutputStream(file);
			output.write(__content);
			output.close();

			updateFileListStats(__content.length, 1);
		} catch (Exception __e) {
			Log.e("FileCache", "Error trying to write file! " + __e);
		}

		Log.i("FileCache", "putFile() :: File " + __id + " saved as " + getFileName(__id) + " on " + getCacheDir().getAbsolutePath());
		Log.i("FileCache", "putFile() :: Total file cache size is " + getTotalSize() + " in " + getNumFiles() + " files");

		// TODO: delete files based on cache limit size?
	}

	public boolean deleteFile(String __id) {
		// Removes a file from the cache; returns true if deleted

		// Removes file
		File file = new File(getFileName(__id));
		long fileSize = file.length();
		boolean wasDeleted = file.delete();

		if (wasDeleted) updateFileListStats(-fileSize, -1);

		return wasDeleted;
	}

	public void trimFilesByAge(long __days) {
		// Delete files from the cache based on how old they are
		F.log("Erasing files from cache [" + id + "] that are " + __days + " or more days old");
		long now = System.currentTimeMillis();
		long maxAge = __days * 24 * 60 * 60 * 1000;

		if (ApplicationConstants.IS_DEBUGGING) maxAge /= 10;

		long cutTime = now - maxAge;

		F.log("   Time now is " + new Date(now).toString() + "; erasing files created before " + new Date(cutTime).toString());

		int bNumFiles = getNumFiles();
		long bTotalSize = getTotalSize();

		int filesDeleted = 0;
		long bytesDeleted = 0;
		boolean wasDeleted;
		long fileSize;

		File[] files = getCacheDir().listFiles();

		for (File f:files) {

			if (f.lastModified() < cutTime) {
				// Delete the file
				if (ApplicationConstants.IS_DEBUGGING) F.warn("      Deleting file ["+f.getName()+"] created in " + new Date(f.lastModified()).toString());

				fileSize = ApplicationConstants.IS_DEBUGGING ? 0 : f.length();
				wasDeleted = f.delete();

				if (wasDeleted) {
					bytesDeleted += fileSize;
					filesDeleted++;
					fileListStatsDirty = true;
				}
			}
		}

		F.log("   " + filesDeleted + " files and " + bytesDeleted + " bytes deleted (of " + bNumFiles + " files and " + bTotalSize + " bytes)");

		F.log("   Took " + (System.currentTimeMillis() - now) + "ms to trim file cache");

	}


	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public String getId() {
		return id;
	}

	public File getCacheDir() {
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
