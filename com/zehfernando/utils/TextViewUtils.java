package com.zehfernando.utils;

import java.util.ArrayList;

import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import com.zehfernando.display.CustomTypefaceSpan;

public class TextViewUtils {

	// ================================================================================================================
	// PRIVATE INTERFACE -----------------------------------------------------------------------------------------------

	private static void setTextViewSpanFromStyles(TextView __textView, String __text, String __tagOpen, String __tagClose, Object __span) {
		// Sets the text of a TextView with span replacing certain tags

		// Find the position of all tags
		int tagOpenPos = __text.indexOf(__tagOpen);
		int tagClosePos = __text.indexOf(__tagClose, tagOpenPos+1);

		ArrayList<Integer> positions = new ArrayList<Integer>();

		while (tagOpenPos > -1 && tagClosePos > -1) {

			__text = __text.substring(0, tagOpenPos) + __text.substring(tagOpenPos + __tagOpen.length(), tagClosePos) + __text.substring(tagClosePos + __tagClose.length(), __text.length());

			positions.add(tagOpenPos);
			positions.add(tagClosePos - __tagOpen.length());

			tagOpenPos = __text.indexOf(__tagOpen);
			tagClosePos = __text.indexOf(__tagClose, tagOpenPos+1);
		}

		// Create actual span
		SpannableString spanString = new SpannableString(__text);
		for (int i = 0; i < positions.size(); i += 2) {
			spanString.setSpan(__span, positions.get(i), positions.get(i+1), 0);
		}

		__textView.setText(spanString);
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

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

	public static void setTextViewBoldStyle(TextView __textView, Typeface __typeface, int __color) {
		// Replaces <b>...</b> inside a textview with proper bold styles
		CustomTypefaceSpan boldSpan = new CustomTypefaceSpan(__typeface, __color);
		setTextViewSpanFromStyles(__textView, __textView.getText().toString(), "<b>", "</b>", boldSpan);
	}


	// ================================================================================================================
	// AUXILIARY CLASES -----------------------------------------------------------------------------------------------

	public static class InternalHardcodedURLSpan extends URLSpan {

		// Properties
		private final OnClickLinkListener onClickLinkListener;

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
