package com.zehfernando.net.cache.legacy;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.util.Log;
import android.widget.Toast;

import com.zehfernando.net.cache.FileCache;
import com.zehfernando.net.cache.legacy.interfaces.CachenatorListener;
import com.zehfernando.net.cache.legacy.xml.FileInfo;
import com.zehfernando.net.cache.legacy.xml.FileList;
import com.zehfernando.net.loaders.XMLLoader;
import com.zehfernando.net.loaders.XMLLoader.OnXMLLoaderLoadingCompleteListener;
import com.zehfernando.net.loaders.XMLLoader.OnXMLLoaderLoadingErrorListener;

public class Cachenator {

	// Properties
	private boolean isSilent;
	private Context dialogContext;

	private boolean autoUpdateFiles;
	private boolean allowDialogCancel;

	private FileCache fileCache;
	private boolean ignoreCache;

	// File list loading properties
	private String fileListURL;
	private XMLLoader fileListLoader;
	private ProgressDialog fileListDialog;

	// Updated files loading properties
	private XMLLoader filesUpdateLoader;
	private ProgressDialog filesUpdateDialog;

	private FileList fileList;
	private ArrayList<FileInfo> filesToUpdate;
	private int fileBeingUpdated;

	private boolean someFileUpdatedSuccessfully;
	private boolean allFilesUpdatedSuccessfully;

	private ArrayList<CachenatorListener> eventListeners;

	// TODO: add last update check


	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public Cachenator(Context __dialogContext, FileCache __fileCache) {
		dialogContext = __dialogContext;
		fileCache = __fileCache;
		ignoreCache = false;

		eventListeners = new ArrayList<CachenatorListener>();
	}

	public Cachenator(Context __dialogContext, FileCache __fileCache, boolean __ignoreCache) {
		dialogContext = __dialogContext;
		fileCache = __fileCache;
		ignoreCache = __ignoreCache;

		eventListeners = new ArrayList<CachenatorListener>();
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	private void startFileListLoader() {
		stopFileListLoader();

		fileListLoader = new XMLLoader();

		// Set loading listeners
		fileListLoader.setOnXMLLoaderLoadingCompleteListener(new OnXMLLoaderLoadingCompleteListener() {
			@Override
			public void onLoadingComplete(XMLLoader __loader) {
				onFileListLoadSuccess();
			}
		});

		fileListLoader.setOnXMLLoaderLoadingErrorListener(new OnXMLLoaderLoadingErrorListener() {
			@Override
			public void onLoadingError(XMLLoader __loader) {
				onFileListLoadError();
			}
		});


		fileListLoader.load(fileListURL);
	}

	private void stopFileListLoader() {
		if (fileListLoader != null) {
			fileListLoader.cancel();
			fileListLoader = null;
		}
	}

	private void checkFileList() {

		// Check the file list and creates a list of files that will actually need to be updated
		// List of files that will actually need to be updated
		filesToUpdate = new ArrayList<FileInfo>();

		// Temp vars
		FileInfo fileInfo;

		for (int i = 0; i < fileList.size(); i++) {
			fileInfo = fileList.get(i);
			Log.d("Cachenator", i + " --> " + fileInfo.id + ", " + fileInfo.time + ", " + fileInfo.url + ", ");

			// Test if files exist in the cache
			if (fileCache.getFileExists(fileInfo.id)) {
				Log.v("Cachenator", "	File already exists on cache; timestamp is " + fileCache.getFileTime(fileInfo.id));

				if (ignoreCache || fileCache.getFileTime(fileInfo.id) < fileInfo.time.toMillis(true)) {
					Log.v("Cachenator", "		Cached file is older than online file, will update");
					filesToUpdate.add(fileInfo);
				} else {
					Log.v("Cachenator", "		Cached file is the same as online file, will not update");
				}
			} else {
				Log.v("Cachenator", "	File doesn't exist on cache yet, will update");
				filesToUpdate.add(fileInfo);
			}

		}
	}

	private void startFilesUpdater() {
		stopFilesUpdater();

		allFilesUpdatedSuccessfully = true;
		someFileUpdatedSuccessfully = false;

		fileBeingUpdated = 0;
		updateFile();
	}

	private void updateFile() {

		if (!isSilent) filesUpdateDialog.setProgress(fileBeingUpdated);

		if (fileBeingUpdated < getNumFilesToUpdate()) {
			// Still has a file left to update
			FileInfo fileInfo = filesToUpdate.get(fileBeingUpdated);

			Log.v("Cachenator", "Updating file " + fileInfo.url);

			// Load and update the next file
			filesUpdateLoader = new XMLLoader();

			filesUpdateLoader.setOnXMLLoaderLoadingCompleteListener(new OnXMLLoaderLoadingCompleteListener() {
				@Override
				public void onLoadingComplete(XMLLoader __loader) {
					onFilesUpdateItemSuccess();
				}
			});

			filesUpdateLoader.setOnXMLLoaderLoadingErrorListener(new OnXMLLoaderLoadingErrorListener() {
				@Override
				public void onLoadingError(XMLLoader __loader) {
					onFilesUpdateItemError();
				}
			});

			filesUpdateLoader.load(fileInfo.url);

		} else {
			// No more files!

			if (!isSilent) filesUpdateDialog.dismiss();

			if (allFilesUpdatedSuccessfully) {
				onFilesUpdateSuccess();
			} else if (someFileUpdatedSuccessfully) {
				onFilesUpdateSomeSuccess();
			} else {
				onFilesUpdateError();
			}
		}
	}

	private void stopFilesUpdater() {
		if (fileListLoader != null) {
			fileListLoader.cancel();
			fileListLoader = null;
		}
	}

	// ================================================================================================================
	// EVENT INTERFACE ------------------------------------------------------------------------------------------------

	private void onFileListLoadSuccess() {
		// Loaded file list XML, so parse it
		if (!isSilent) fileListDialog.dismiss();

		//fileList = ((FileListHandler)fileListLoader.getContentHandler()).getFileList();
		// ****************************************************************
		// TODO: fix this!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// Must get correct list from the XML!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// ****************************************************************

		checkFileList();

		stopFileListLoader();

		for (int i = 0; i < eventListeners.size(); i++) eventListeners.get(i).onFileListLoadSuccess(this);

		if (autoUpdateFiles) updateFiles(isSilent, allowDialogCancel);
	}

	private void onFileListLoadCancel() {
		// User canceled file loading
		stopFileListLoader();

		// TODO: dispatch event: file list load canceled
	}

	private void onFileListLoadError() {
		// Error loading the file list XML
		if (!isSilent) fileListDialog.dismiss();

		Toast.makeText(dialogContext, "Error loading file list", Toast.LENGTH_SHORT).show();

		stopFileListLoader();

		for (int i = 0; i < eventListeners.size(); i++) eventListeners.get(i).onFileListLoadError(this);
	}

	private void onFilesUpdateItemSuccess() {
		// Finished updating one file
		Log.v("Cachenator", "Updated one file");

		someFileUpdatedSuccessfully = true;

		// apparently this uses the system codepage instead of unicode
		//fileCache.putFile(filesToUpdate.get(fileBeingUpdated).id, filesUpdateLoader.getData());
		// ****************************************************************
		// TODO: fix this!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// Must allow saving with the correct date!!!!!!!!!!!!!!!!!!!!!!!!!
		// ****************************************************************

		fileBeingUpdated++;
		updateFile();

		for (int i = 0; i < eventListeners.size(); i++) eventListeners.get(i).onFilesUpdateItemSuccess(this);
	}

	private void onFilesUpdateItemError() {
		Log.v("Cachenator", "Error updating one file");

		allFilesUpdatedSuccessfully = false;

		fileBeingUpdated++;
		updateFile();

		for (int i = 0; i < eventListeners.size(); i++) eventListeners.get(i).onFilesUpdateItemError(this);
	}

	private void onFilesUpdateSuccess() {
		// Successfully updated all files
		Log.v("Cachenator", "Updated all files");

		for (int i = 0; i < eventListeners.size(); i++) eventListeners.get(i).onFilesUpdateSuccess(this);
	}

	private void onFilesUpdateSomeSuccess() {
		// Successfully updated SOME files
		Log.v("Cachenator", "Updated some files");

		for (int i = 0; i < eventListeners.size(); i++) eventListeners.get(i).onFilesUpdateSomeSuccess(this);
	}

	private void onFilesUpdateCancel() {
		// User canceled file loading
		stopFilesUpdater();

		for (int i = 0; i < eventListeners.size(); i++) eventListeners.get(i).onFilesUpdateCancel(this);
	}

	private void onFilesUpdateError() {
		// Error on updating all files
		Log.v("Cachenator", "Error updating files");

		for (int i = 0; i < eventListeners.size(); i++) eventListeners.get(i).onFilesUpdateError(this);
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	/**
	 * Loads the file list to check what needs to be updated
	 * @param __silently Whether it'll be done silently (no dialogs) or not (showing a dialog)
	 */
	public void loadFileList(String __fileListURL, boolean __silently, boolean __autoUpdateFiles, boolean __allowDialogCancel) {
		fileListURL = __fileListURL;
		isSilent = __silently;
		autoUpdateFiles = __autoUpdateFiles;
		allowDialogCancel = __allowDialogCancel;

		// TODO: somehow already have a cached file index

		// Shows dialog if needed
		if (!isSilent) {
			// Displays progress dialog first
			fileListDialog = new ProgressDialog(dialogContext);
			fileListDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			fileListDialog.setMessage("Loading file list. Please wait...");
			fileListDialog.setCancelable(allowDialogCancel);
			fileListDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					onFileListLoadCancel();
				}
			});
			fileListDialog.show();
		}

		// Create the file list loader
		startFileListLoader();

	}

	/**
	 * Load files that actually need to be updated
	 * @param __silently Whether it'll be done silently (no dialogs) or not (showing a dialog)
	 */
	public void updateFiles(boolean __silently, boolean __allowDialogCancel) {
		isSilent = __silently;
		allowDialogCancel = __allowDialogCancel;

		if (filesToUpdate.size() > 0) {
			// Will need to update files

			if (!isSilent) {
				filesUpdateDialog = new ProgressDialog(dialogContext);
				filesUpdateDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				filesUpdateDialog.setMessage("Updating files. Please wait...");
				filesUpdateDialog.setCancelable(allowDialogCancel);
				filesUpdateDialog.setMax(filesToUpdate.size());
				filesUpdateDialog.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						onFilesUpdateCancel();
					}
				});
				filesUpdateDialog.show();
			}

			// Starts updating
			startFilesUpdater();

		} else {
			// No files to update, just dispatch success
			onFilesUpdateSuccess();
		}
	}

	public void addEventListener(CachenatorListener __listener) {
		eventListeners.add(__listener);
	}

	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public int getNumFilesToUpdate() {
		return filesToUpdate.size();
	}
}

