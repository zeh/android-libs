package com.zehfernando.net.loaders;

import com.zehfernando.data.xml.XML;
import com.zehfernando.net.loaders.TextLoader.OnTextLoaderLoadingCompleteListener;
import com.zehfernando.net.loaders.TextLoader.OnTextLoaderLoadingErrorListener;
import com.zehfernando.net.loaders.TextLoader.OnTextLoaderLoadingProgressListener;
import com.zehfernando.net.loaders.TextLoader.OnTextLoaderLoadingStartListener;

public class XMLLoader {

	// Properties
	private TextLoader textLoader;
	private OnXMLLoaderLoadingStartListener onLoadingStartListener;
	private OnXMLLoaderLoadingErrorListener onLoadingErrorListener;
	private OnXMLLoaderLoadingProgressListener onLoadingProgressListener;
	private OnXMLLoaderLoadingCompleteListener onLoadingCompleteListener;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public XMLLoader() {
		textLoader = new TextLoader();
		textLoader.setOnTextLoaderLoadingStartListener(new OnTextLoaderLoadingStartListener() {
			@Override
			public void onLoadingStart(TextLoader __loader) {
				dispatchOnLoadingStart();
			}
		});
		textLoader.setOnTextLoaderLoadingErrorListener(new OnTextLoaderLoadingErrorListener() {
			@Override
			public void onLoadingError(TextLoader __loader) {
				dispatchOnLoadingError();
			}
		});
		textLoader.setOnTextLoaderLoadingProgressListener(new OnTextLoaderLoadingProgressListener() {
			@Override
			public void onLoadingProgress(TextLoader __loader, int __bytesLoaded, int __bytesTotal) {
				dispatchOnLoadingProgress();
			}
		});
		textLoader.setOnTextLoaderLoadingCompleteListener(new OnTextLoaderLoadingCompleteListener() {
			@Override
			public void onLoadingComplete(TextLoader __loader) {
				dispatchOnLoadingComplete();
			}
		});
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	protected void dispatchOnLoadingStart() {
		// Loading has started, header is known (total bytes, etc)
		if (onLoadingStartListener != null) onLoadingStartListener.onLoadingStart(this);
	}

	protected void dispatchOnLoadingError() {
		// Loading stopped
		if (onLoadingErrorListener != null) onLoadingErrorListener.onLoadingError(this);
	}

	protected void dispatchOnLoadingProgress() {
		// Loading progress, loadedBytes has been updated
		if (onLoadingProgressListener != null) onLoadingProgressListener.onLoadingProgress(this, getLoadedBytes(), getTotalBytes());
	}

	protected void dispatchOnLoadingComplete() {
		// Loading is complete
		if (onLoadingCompleteListener != null) onLoadingCompleteListener.onLoadingComplete(this);
	}

	protected void clear() {
		textLoader = null;
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public void load(String __url) {
		textLoader.load(__url);
	}

	public void cancel() {
		if (textLoader != null) textLoader.cancel();

		clear();
	}

	public void setOnXMLLoaderLoadingStartListener(OnXMLLoaderLoadingStartListener __listener) {
		onLoadingStartListener = __listener;
	}

	public void setOnXMLLoaderLoadingErrorListener(OnXMLLoaderLoadingErrorListener __listener) {
		onLoadingErrorListener = __listener;
	}

	public void setOnXMLLoaderLoadingProgressListener(OnXMLLoaderLoadingProgressListener __listener) {
		onLoadingProgressListener = __listener;
	}

	public void setOnXMLLoaderLoadingCompleteListener(OnXMLLoaderLoadingCompleteListener __listener) {
		onLoadingCompleteListener = __listener;
	}

	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public XML getData() {
		return new XML(textLoader.getData());
	}

	public int getTotalBytes() {
		return textLoader.getTotalBytes();
	}

	public int getLoadedBytes() {
		return textLoader.getLoadedBytes();
	}

	public boolean getIsLoading() {
		return textLoader.getIsLoading();
	}

	public boolean getIsLoaded() {
		return textLoader.getIsLoaded();
	}

	public long getLastModified() {
		return textLoader.getLastModified();
	}

	// ================================================================================================================
	// INTERFACE CLASSES ----------------------------------------------------------------------------------------------

	public interface OnXMLLoaderLoadingStartListener {
		public void onLoadingStart(XMLLoader __loader);
	}

	public interface OnXMLLoaderLoadingErrorListener {
		public void onLoadingError(XMLLoader __loader);
	}

	public interface OnXMLLoaderLoadingProgressListener {
		public void onLoadingProgress(XMLLoader __loader, int __loadedBytes, int __totalBytes);
	}

	public interface OnXMLLoaderLoadingCompleteListener {
		public void onLoadingComplete(XMLLoader __loader);
	}
}
