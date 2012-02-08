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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

import com.zehfernando.utils.StringUtils;

public class FileCache {

	// Static properties
	private static Context context;

	private static ArrayList<FileCache> caches = new ArrayList<FileCache>();

	// Properties
	private String id;
	private String uniqueId;				// Id used for subfolder name

	private long totalSize;					// Size of the cache, in bytes
	private int numFiles;					// Number of files in the cache
	private boolean fileListStatsDirty;			// If true, the number of files and file size in the cache is not known

	// ================================================================================================================
	// STATIC INTERFACE -----------------------------------------------------------------------------------------------

	public static FileCache getFileCache() {
		return getFileCache("");
	}

	public static FileCache getFileCache(String __id) {
		int i;

		// Try to find the cache
		for (i = 0; i < caches.size(); i++) {
			if (caches.get(i).getId() == __id) return caches.get(i);
		}

		// Create in case it doesn't exist
		return new FileCache(__id);
	}

	public static void addFileCache(FileCache __fileCache) {
		caches.add(__fileCache);
	}

	public static void init(Context __context) {
		context = __context;
	}

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public FileCache() {
		id = "";

		initialize();
	}

	public FileCache(String __id) {
		id = __id;

		initialize();
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	private void initialize() {
		uniqueId = StringUtils.calculateMD5(id);
		if (uniqueId == null) id.replace("/","_").replace(" ","_").replace("\\","_").replace(":","_");

		FileCache.addFileCache(this);

		totalSize = 0;
		numFiles = 0;
		fileListStatsDirty = true;
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
		__bitmap.compress(__format, __quality, output);
		updateFileListStats(file.length(), 1);

		Log.i("FileCache", "putFile() :: File " + __id + " saved as " + getFileName(__id) + " on " + getCacheDir().getAbsolutePath());
		Log.i("FileCache", "putFile() :: Total file cache size is " + getTotalSize() + " in " + getNumFiles() + " files");

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

	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public String getId() {
		return id;
	}

	public File getCacheDir() {

		// * Saved on internal memory, as part of the application data
		// * Cleared by the system if needed
		// * Goes to the "cache" area of the application data on Android's application data management screen
		// Equivalent to "/data/data/com.redken.stylestation/cache"
		// http://developer.android.com/reference/android/content/Context.html#getCacheDir()

		//if (id == "") return context.getCacheDir();

		// If a different id, use a subfolder
		//File baseFolder = new File(context.getCacheDir(), uniqueId);
		File baseFolder = new File(context.getCacheDir(), id.length() == 0 ? "none" : id);
		if (!baseFolder.exists()) baseFolder.mkdir();

		return baseFolder;

		// * Saved on external memory
		// * Not cleared by the system
		// * Unavailable when SD card is mounted
		// * Requires WRITE_EXTERNAL_STORAGE permission
		// * Doesn't go to the "cache" area of the application data on Android's application data management screen
		// Equivalent to "/mnt/sdcard/Android/data/com.redken.stylestation/cache"
		// http://developer.android.com/reference/android/content/Context.html#getExternalCacheDir()
		//return context.getExternalCacheDir();

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
