package com.zehfernando.net.loaders;

import com.zehfernando.data.xml.XML;
import com.zehfernando.net.loaders.TextLoader.OnTextLoaderCompleteListener;
import com.zehfernando.net.loaders.TextLoader.OnTextLoaderErrorListener;
import com.zehfernando.net.loaders.TextLoader.OnTextLoaderProgressListener;
import com.zehfernando.net.loaders.TextLoader.OnTextLoaderStartListener;

public class XMLLoader {

	// Properties
	private TextLoader textLoader;
	private OnXMLLoaderStartListener onStartListener;
	private OnXMLLoaderErrorListener onErrorListener;
	private OnXMLLoaderProgressListener onProgressListener;
	private OnXMLLoaderCompleteListener onCompleteListener;
	private OnXMLLoaderCancelListener onCancelListener;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public XMLLoader() {
		textLoader = new TextLoader();
		textLoader.setOnLoadingStartListener(new OnTextLoaderStartListener() {
			@Override
			public void onStart(TextLoader __loader) {
				dispatchOnStart();
			}
		});
		textLoader.setOnLoadingErrorListener(new OnTextLoaderErrorListener() {
			@Override
			public void onError(TextLoader __loader) {
				dispatchOnError();
			}
		});
		textLoader.setOnLoadingProgressListener(new OnTextLoaderProgressListener() {
			@Override
			public void onProgress(TextLoader __loader, int __bytesLoaded, int __bytesTotal) {
				dispatchOnProgress();
			}
		});
		textLoader.setLoadingCompleteListener(new OnTextLoaderCompleteListener() {
			@Override
			public void onComplete(TextLoader __loader) {
				dispatchOnComplete();
			}
		});
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	protected void dispatchOnStart() {
		// Loading has started, header is known (total bytes, etc)
		if (onStartListener != null) onStartListener.onStart(this);
	}

	protected void dispatchOnError() {
		// Loading stopped
		if (onErrorListener != null) onErrorListener.onError(this);
	}

	protected void dispatchOnProgress() {
		// Loading progress, loadedBytes has been updated
		if (onProgressListener != null) onProgressListener.onProgress(this, getLoadedBytes(), getTotalBytes());
	}

	protected void dispatchOnComplete() {
		// Loading is complete
		if (onCompleteListener != null) onCompleteListener.onComplete(this);
	}

	protected void dispatchOnCancel() {
		// Loading has been canceled
		if (onCancelListener != null) onCancelListener.onCancel(this);
	}

	protected void clear() {
		if (textLoader != null) textLoader.clear();
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

	public void setOnStartListener(OnXMLLoaderStartListener __listener) {
		onStartListener = __listener;
	}

	public void setOnErrorListener(OnXMLLoaderErrorListener __listener) {
		onErrorListener = __listener;
	}

	public void setOnProgressListener(OnXMLLoaderProgressListener __listener) {
		onProgressListener = __listener;
	}

	public void setOnCompleteListener(OnXMLLoaderCompleteListener __listener) {
		onCompleteListener = __listener;
	}

	public void setOnCancelListener(OnXMLLoaderCancelListener __listener) {
		onCancelListener = __listener;
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

	public interface OnXMLLoaderStartListener {
		public void onStart(XMLLoader __loader);
	}

	public interface OnXMLLoaderErrorListener {
		public void onError(XMLLoader __loader);
	}

	public interface OnXMLLoaderProgressListener {
		public void onProgress(XMLLoader __loader, int __loadedBytes, int __totalBytes);
	}

	public interface OnXMLLoaderCompleteListener {
		public void onComplete(XMLLoader __loader);
	}

	public interface OnXMLLoaderCancelListener {
		public void onCancel(XMLLoader __loader);
	}
}
