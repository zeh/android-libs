package com.zehfernando.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;

public class ImageUtils {

	public static void requestImageIntent(Activity __activity, String __chooserTitle, Uri __destinationFileUri, int __requestCode) {
		// Requests an image from the user, either from the camera or from the file system (user picks one)

		// Camera intents
		final List<Intent> cameraIntents = new ArrayList<Intent>();
		final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		final PackageManager packageManager = __activity.getPackageManager();
		final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
		for (ResolveInfo res:listCam) {
			final String packageName = res.activityInfo.packageName;
			final Intent intent = new Intent(captureIntent);
			intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
			intent.setPackage(packageName);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, __destinationFileUri);
			cameraIntents.add(intent);
		}

		// Gallery intents
		final Intent galleryIntent = new Intent();
		galleryIntent.setType("image/*");
		galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

		// Chooser of filesystem options
		final Intent chooserIntent = Intent.createChooser(galleryIntent, __chooserTitle);

		// Add the camera options
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));

		__activity.startActivityForResult(chooserIntent, __requestCode);

		// To find a good Uri:
		/*
		final String filename = StringUtils.calculateMD5(""+System.currentTimeMillis()) + ".jpg";
		final File sdImageMainDirectory = new File(ShareUtils.getTemporaryAttachmentLocation(this), filename);
		cameraFileURI = Uri.fromFile(sdImageMainDirectory);
		*/

		// From gallery: content://media/external/images/media/1
		// From camera: file:///mnt/sdcard/Android/data/com.shaklee.OneEighty/cache/share_attachments/7b5e0a34143523f3a33479f8bdf5c806.jpg

		// To receive:
		/*
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			if (resultCode == RESULT_OK) {
				if (requestCode == REQUEST_CODE_IMAGE) {
					final boolean isCamera;
					if (data == null) {
						isCamera = true;
					} else {
						final String action = data.getAction();
						if (action == null) {
							isCamera = false;
						} else {
							isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						}
					}

					selectedImageUri = null;

					if (isCamera) {
						selectedImageUri = getCameraFileURI(); // uri passed to requestImageIntent()
					} else {
						selectedImageUri = data == null ? null : data.getData();
					}

					if (selectedImageUri != null) {
						hasMotivationalImage = true;
						F.log("Camera file URI = " + selectedImageUri);

						motivationProvider.updateMotivationalPreview();
					}
				}
			}
		}
		*/

		// Complete usage when attaching:
		/*
		Bitmap resizedBitmap = ImageUtils.readAndResizeImage(selectedImageUri, 800, 800); // Max w/h of 800

		// Sets the attachment
		addImageRequest.setAttachment(AddHealthyCompetitionImage.ATTACHMENT_IMAGE, "image_" + System.currentTimeMillis()+ ".jpg", ImageUtils.bitmapToInputStream(resizedBitmap, OneEightyApplication.JPEG_PHOTO_QUALITY));

		resizedBitmap.recycle();
		System.gc();
		*/
	}

	public static Bitmap readAndResizeImage(Context __context, Uri __imageUri, int __maxWidth, int __maxHeight) {
		// Scales the image before loading, to avoid memory issues
		// Similar problem: http://stackoverflow.com/questions/3331527/android-resize-a-large-bitmap-file-to-scaled-output-file

		//File image = new File(__imageUri.getPath());

		// Find image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		try {
			// getContentResolver().openInputStream
			BitmapFactory.decodeStream(__context.getContentResolver().openInputStream(__imageUri), null, o);
			//BitmapFactory.decodeStream(new FileInputStream(image), null, o);
		} catch (FileNotFoundException e) {
			F.error("Could not read bitmap!");
			return null;
		}

		float desiredWidth;
		float desiredHeight;

		if (o.outHeight <= __maxHeight && o.outWidth <= __maxWidth) {
			// Image fits into the desired size
			desiredWidth = o.outWidth;
			desiredHeight = o.outHeight;
		} else {
			// Need to scale to fit
			if (o.outHeight > o.outWidth) {
				// Fit height
				desiredWidth = Math.round(((float)o.outWidth / (float)o.outHeight) * __maxHeight);
				desiredHeight = __maxHeight;
			} else {
				// Fit width
				desiredWidth = __maxWidth;
				desiredHeight = Math.round(((float)o.outHeight / (float)o.outWidth) * __maxWidth);
			}
		}

		// Decode and resample using the nearest possible sample size
		int scale = (int) Math.ceil(o.outWidth / desiredWidth);
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		Bitmap sampledBitmap = null;

		try {
			sampledBitmap = BitmapFactory.decodeStream(__context.getContentResolver().openInputStream(__imageUri), null, o2);
		} catch (FileNotFoundException e) {
			F.error("Could not read bitmap!");
			return null;
		}

		Bitmap resizedBitmap = null;

		// Scale down to 800 if needed - it's resizing twice because of memory constraints...
		if (o.outWidth > desiredWidth) {
			resizedBitmap = Bitmap.createScaledBitmap(sampledBitmap, (int)desiredWidth, (int)desiredHeight, true);
			if (resizedBitmap != sampledBitmap) sampledBitmap.recycle();
		} else {
			resizedBitmap = sampledBitmap;
		}
		sampledBitmap = null;

		System.gc();

		// Rotate if needed
		int rotationAngle = getImageRotation(__context, __imageUri);

		F.debug("Picture rotation read: " + rotationAngle);
		if (rotationAngle != 0) {
			Matrix matrix = new Matrix();
			matrix.setRotate(rotationAngle, (float) resizedBitmap.getWidth() / 2, (float) resizedBitmap.getHeight() / 2);
			Bitmap rotatedBitmap = Bitmap.createBitmap(resizedBitmap, 0, 0, (int)desiredWidth, (int)desiredHeight, matrix, true);

			if (rotatedBitmap != resizedBitmap) resizedBitmap.recycle();
			resizedBitmap = rotatedBitmap;
			rotatedBitmap = null;

			System.gc();
		}

		return resizedBitmap;
	}

	private static int getImageRotation(Context __context, Uri __imageUri) {

		ExifInterface exif = null;

		boolean isFromQuery = __imageUri.toString().indexOf("content://") == 0;

		try {
			exif = new ExifInterface(__imageUri.getPath());
		} catch (IOException __e) {
			F.error("Could not read EXIF of the file!");
		}

		int rotationAngle = 0;

		if (exif != null && !isFromQuery) {
			// Local file
			F.debug("Image is a local file (from camera?)");
			String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
			int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
			if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
			if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
			if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
		} else {
			// External media
			F.debug("Image is a query (from gallery?)");
			// http://stackoverflow.com/questions/3647993/android-bitmaps-loaded-from-gallery-are-rotated-in-imageview
			Cursor cursor = __context.getContentResolver().query(__imageUri, new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

			if (cursor.getCount() == 1) {
				cursor.moveToFirst();
				rotationAngle = cursor.getInt(0);
			}
		}

		return rotationAngle;
	}

	public static InputStream bitmapToInputStream(Bitmap __bitmap, int __quality) {
		// Converts a bitmap to an Input Stream
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		__bitmap.compress(CompressFormat.JPEG, __quality, bos);
		byte[] bitmapdata = bos.toByteArray();
		return new ByteArrayInputStream(bitmapdata);
	}

}
