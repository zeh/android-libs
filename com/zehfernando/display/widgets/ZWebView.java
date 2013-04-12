package com.zehfernando.display.widgets;

import android.content.Context;
import android.net.http.SslError;
import android.util.AttributeSet;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ZWebView extends WebView {

	/*
	 * A better WebView
	 * . Fixes SSL errors
	 */

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public ZWebView(Context __context, AttributeSet __attrs) {
		super(__context, __attrs);
		init();
	}

	public ZWebView(Context __context, AttributeSet __attrs, int __defStyle) {
		super(__context, __attrs, __defStyle);
		init();
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	private void init() {
		setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				// Ignore SSL certificate errors
				handler.proceed();
			}
		});
	}
}
