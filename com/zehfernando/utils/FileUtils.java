package com.zehfernando.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.redkenfb.stylestation.ApplicationConstants;

import android.util.Log;

public class FileUtils {

	public static void copyFile(String __from, String __to) {
		InputStream input = null;
		OutputStream output = null;

		try {
			input = new FileInputStream(__from);
			output = new FileOutputStream(__to);
			copyFile(input, output);
			input.close();
			input = null;
			output.flush();
			output.close();
			output = null;
		} catch (Exception __e) {
			Log.e("FileUtils", "Could not copy the files!" + __e.getMessage());
		}
	}

	public static void copyFile(InputStream __input, OutputStream __output) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = __input.read(buffer)) != -1) {
			__output.write(buffer, 0, read);
		}
	}

	public static void emptyFolder(File __file) {
		if (!__file.isDirectory()) {
			Log.e("FileUtils", "Tried to delete a folder that doesn't exist or is not a directory: " + __file);
		} else {
			long bytesDeleted = 0;
			long filesDeleted = 0;
			File[] files = __file.listFiles();

			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					if (ApplicationConstants.IS_DEBUGGING) bytesDeleted += files[i].length();
					filesDeleted++;
					files[i].delete();
				}

				__file.delete();

				Log.w("FileUtils", "Deleted " + (bytesDeleted/1024) + "kb in " + filesDeleted + " files from folder " + __file);

			} else {
				Log.e("FileUtils", "Tried to list contents of a folder and got null as a response!");
			}
		}
	}

	public static long getFolderSize(File __file) {
		long total = 0;
		File[] files = __file.listFiles();

		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				total += getFolderSize(files[i]);
			} else {
				total += files[i].length();
			}
		}
		return total;
	}
}
