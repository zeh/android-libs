package com.zehfernando.utils;

import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

public class TextViewUtils {

	// ================================================================================================================
	// STATIC INTERFACE -----------------------------------------------------------------------------------------------

	public static void setTextViewHTMLClickableWithCustomCommands(TextView __textView, String __textSource, InternalHardcodedURLSpan.OnClickLinkListener __onClickLinkListener) {
		// Shitty name!
		// Makes a TextView clickable, but ALSO makes it respond to internal links.
		Spannable sb = (Spannable) Html.fromHtml(__textSource);
		int start;
		int end;
		int i;
		// Finds all links and replaces it with events in case of internal links
		URLSpan[] links = sb.getSpans(0, sb.length(), URLSpan.class);
		//Log.v("TextLinearLayout", "--> text has: " + links.length + " links");
		for (i = 0; i < links.length; i++) {
			// This is a link, replace with new clickable span
			//Log.v("TextLinearLayout", "--> --> " + links[i].getURL());
			start = sb.getSpanStart(links[i]);
			end = sb.getSpanEnd(links[i]);
			sb.removeSpan(links[i]);
			sb.setSpan(new InternalHardcodedURLSpan(links[i].getURL(), __onClickLinkListener), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}

		__textView.setText(sb);
		__textView.setLinksClickable(true);
		__textView.setMovementMethod(LinkMovementMethod.getInstance());
	}


	// ================================================================================================================
	// AUXILIARY CLASES -----------------------------------------------------------------------------------------------

	public static class InternalHardcodedURLSpan extends URLSpan {

		// Properties
		private OnClickLinkListener onClickLinkListener;

		// Constructor

		public InternalHardcodedURLSpan(String __url, OnClickLinkListener __onClickLinkListener) {
			super(__url);

			onClickLinkListener = __onClickLinkListener;
		}

		@Override
		public void onClick(View __view) {
			if (getURL().indexOf("internal:") > -1) {
				// It's internal
				if (onClickLinkListener != null) onClickLinkListener.onClickInternalLink(getURL());
			} else {
				// It's a normal link
				if (onClickLinkListener != null) onClickLinkListener.onClickNormalLink(getURL());
			}
		}

		public interface OnClickLinkListener {
			public void onClickNormalLink(String __link);
			public void onClickInternalLink(String __link);
		}
	}
}
