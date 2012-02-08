package com.zehfernando.net.cache.legacy.xml;

import java.util.ArrayList;


import android.text.format.Time;

public class FileList {

	// Properties
	private ArrayList<FileInfo> files;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public FileList() {
		files = new ArrayList<FileInfo>();
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public void add(String __id, String __time, String __url) {
		FileInfo file = new FileInfo();

		file.id = __id;
		file.url = __url;

		file.time = new Time();
		file.time.parse3339(__time);

		files.add(file);
	}

	public FileInfo get(int __index) {
		return files.get(__index);
	}

	public int size() {
		return files.size();
	}
}
