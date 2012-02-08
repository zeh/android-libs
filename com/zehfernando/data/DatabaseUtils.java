package com.zehfernando.data;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class DatabaseUtils {

	public static int deleteRecordAndFiles(Context __context, Uri __recordUri, String[] __columnsWithFilenames) {
		// Deletes a row from a database, and any related files

		// Find files to delete
		Cursor cursor = __context.getContentResolver().query(__recordUri, null, null, null, null);
		if (cursor.getCount() == 0) {
			Log.e("DatabaseUtils", "Error!!! Record " + __recordUri + " not found for deletion!");
			return 0;
		}
		cursor.moveToFirst();

		// Delete files
		File fileToDelete;
		for (int i = 0; i < __columnsWithFilenames.length; i++) {
			fileToDelete = new File(cursor.getString(cursor.getColumnIndex(__columnsWithFilenames[i])));
			Log.i("DatabaseUtils", "Deleted file: " + fileToDelete.toString());
			fileToDelete.delete();
		}

		cursor.close();
		cursor = null;

		// Delete record
		return __context.getContentResolver().delete(__recordUri, null, null);
	}

}
