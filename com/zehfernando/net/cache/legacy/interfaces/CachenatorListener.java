package com.zehfernando.net.cache.legacy.interfaces;

import com.zehfernando.net.cache.legacy.Cachenator;

public class CachenatorListener {

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public CachenatorListener() {
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	// File list events
	public void onFileListLoadSuccess(Cachenator __cachenator) {

	}

	public void onFileListLoadCancel(Cachenator __cachenator) {

	}

	public void onFileListLoadError(Cachenator __cachenator) {

	}

	// Files item update events
	public void onFilesUpdateItemSuccess(Cachenator __cachenator) {

	}

	public void onFilesUpdateItemError(Cachenator __cachenator) {

	}

	// Files update events
	public void onFilesUpdateSuccess(Cachenator __cachenator) {

	}

	public void onFilesUpdateSomeSuccess(Cachenator __cachenator) {

	}

	public void onFilesUpdateCancel(Cachenator __cachenator) {

	}

	public void onFilesUpdateError(Cachenator __cachenator) {

	}
}
