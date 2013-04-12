package com.zehfernando.utils;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;

public class ShareUtils {

//	public static void shareContent(Context __context) {
//		final Intent sendIntent = new Intent(Intent.ACTION_SEND);
//		sendIntent.setType("image/jpeg");
//		__context.startActivity(Intent.createChooser(sendIntent , "Send .."));
//	}

	public static void sendEmail(Context __context, String __dialogTitle, String __subject, String __body) {
		sendEmail(__context, __dialogTitle, __subject, __body, null, null);
	}

	public static void sendEmail(Context __context, String __dialogTitle, String __subject, String __body, Uri __attachmentUri, String __attachmentName) {
		Intent i = new Intent(Intent.ACTION_SEND);

		i.setType("text/message"); // This works for email with HTML
		//i.setType("message/rfc822"); // This is for emails only, but plain text
		//i.setType("text/html"); // This is for HTML-formatted email, but also shows Bluetoogh
		//i.setType("text/plain"); // This is for standard text: use for twitter/facebook/text message/email/etc

		//i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
		//i.putExtra(Intent.EXTRA_CC, new String[] { "carbon@somewhere.com" });

		i.putExtra(Intent.EXTRA_SUBJECT, __subject);
		i.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(__body));

		String tempFile = null;

		if (__attachmentUri != null) {
			// If a file should be attached, it needs to be copied to an external location first
			tempFile = getTemporaryAttachmentLocation(__context) + __attachmentName;

			FileUtils.copyFile(__attachmentUri.getPath(), tempFile);

			//File file = new File(__attachmentUri.getPath());
			File file = new File(tempFile);

			file.deleteOnExit();

			Log.v("ShareUtils", "File size is " + file.length());
			Log.v("ShareUtils", "File readable is " + file.canRead());
			Log.v("ShareUtils", "New file placement is " + tempFile);
			i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		}

		try {
			__context.startActivity(Intent.createChooser(i, __dialogTitle));
			Log.v("ShareUtils", "Copy DONE");

		} catch (android.content.ActivityNotFoundException __e) {
			Log.v("ShareUtils", "Can't send email - no clients installed?!");
		}
	}

	public static String getTemporaryAttachmentLocation(Context __context) {
		String attachmentDir =  __context.getExternalCacheDir() + "/share_attachments/";
		(new File(attachmentDir)).mkdirs();
		//boolean created = (new File(attachmentDir)).mkdirs();
		boolean exists = (new File(attachmentDir)).exists();
		if (!exists) {
			Log.e("ShareUtils", "Error! Could not create temporary external attachment location! Using local!");
			attachmentDir = __context.getCacheDir() + "/share_attachments/";
			(new File(attachmentDir)).mkdirs();
		}
		return attachmentDir;
		//return __context.getCacheDir() + "/attachment";
	}

	public static void shareURL(Context __context, String __dialogTitle, String __subject, String __url) {
		shareText(__context, __dialogTitle, __subject, __url);
	}

	public static void shareText(Context __context, String __dialogTitle, String __subject, String __body) {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_SUBJECT, __subject);
		i.putExtra(Intent.EXTRA_TEXT, __body);
		__context.startActivity(Intent.createChooser(i, __dialogTitle));
	}
}