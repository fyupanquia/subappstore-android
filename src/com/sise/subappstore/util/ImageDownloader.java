package com.sise.subappstore.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
@SuppressWarnings("deprecation")
public class ImageDownloader {
	public enum Mode {
		NO_ASYNC_TASK, NO_DOWNLOADED_DRAWABLE, CORRECT
	}

	private Mode mode = Mode.CORRECT;
	private static final String SAVING_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/newImage.jpg";

	// private Resources res ;

	// public static LinkedHashMap<String,SoftReference<Bitmap>>
	// storeUserProfileImages = new
	// LinkedHashMap<String,SoftReference<Bitmap>>();
	public static HashMap<String, SoftReference<Bitmap>> storeBitmaps = new HashMap<String, SoftReference<Bitmap>>();

	
	public void download(String Url, ImageView view) {
		Bitmap bitmap = getBitmapFromCache(Url);
		if (bitmap == null) {
			forceDownload(Url, view);
		} else {
			cancelPotentialDownload(Url, view);

			view.setBackgroundDrawable(new BitmapDrawable(bitmap));
		}
	}

	
	private BitmapDrawable roundCornered(Bitmap bitmap) {

		Bitmap result = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(result);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		// prepare canvas for transfer
		paint.setAntiAlias(true);
		paint.setColor(0xFFFFFFFF);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawRoundRect(rectF,20,20, paint);

		// draw bitmap
		paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		BitmapDrawable finalresult = new BitmapDrawable(result);
		return finalresult;
	}

	public Bitmap getBitmapFromCache(String url) {
		if (storeBitmaps.containsKey(url)) {
			SoftReference<Bitmap> bitmapReference = storeBitmaps.get(url);
			if (bitmapReference != null) {
				final Bitmap bitmap = bitmapReference.get();
				if (bitmap != null) {
					// Bitmap found in soft cache
					return bitmap;
				} else {
					// Soft reference has been Garbage Collected
					storeBitmaps.remove(url);
				}
			}
		} else {
			return null;
		}

		return null;
	}

	
	private void forceDownload(String url, ImageView view) {
		// Log.e("Image Url",url+"**");
		// Log.e("View",view+"**");
		// State sanity: url is guaranteed to never be null in
		// DownloadedDrawable and cache keys.
		if (url == null) {
			view.setBackgroundDrawable(null);
			return;
		}

		if (cancelPotentialDownload(url, view)) {
			switch (mode) {
			case CORRECT:

				BitmapDownloaderTask task = new BitmapDownloaderTask(view, url);

				task = new BitmapDownloaderTask(view, url);

				DownloadedDrawable downloadedDrawable = new DownloadedDrawable(
						task);

				view.setBackgroundDrawable(downloadedDrawable);

				task.execute(url);
				break;
			}
		}
	}

	/**
	 * Returns true if the current download has been canceled or if there was no
	 * download in progress on this image view. Returns false if the download in
	 * progress deals with the same url. The download is not stopped in that
	 * case.
	 */

	private static boolean cancelPotentialDownload(String url,
			ImageView imageView) {
		BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

		if (bitmapDownloaderTask != null) {
			String bitmapUrl = bitmapDownloaderTask.url;

			if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
				bitmapDownloaderTask.cancel(true);
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * The actual AsyncTask that will asynchronously download the image.
	 */

	class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
		private String url;
		private final WeakReference<ImageView> imageViewReference;

		public BitmapDownloaderTask(ImageView imageView, String url) {
			imageViewReference = new WeakReference<ImageView>(imageView);
			this.url = url;
		}

		/**
		 * Actual download method.
		 */
		@Override
		protected Bitmap doInBackground(String... params) {
			url = params[0];
			return downloadBitmap(url);
		}

		/**
		 * Once the image is downloaded, associates it to the imageView
		 */
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}

			addBitmapToCache(url, bitmap);

			if (imageViewReference != null) {
				ImageView imageView = imageViewReference.get();
				BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
				// Change bitmap only if this process is still associated with
				// it
				// Or if we don't use any bitmap to task association
				// (NO_DOWNLOADED_DRAWABLE mode)
				if ((this == bitmapDownloaderTask) || (mode != Mode.CORRECT)) {
					imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));// .setImageBitmap(bitmap);
				}
			}
		}
	}

	/**
	 * Adds this bitmap to the cache.
	 * 
	 * @param bitmap
	 *            The newly downloaded bitmap.
	 */
	private void addBitmapToCache(String url, Bitmap bitmap) {
		if (bitmap != null) {
			storeBitmaps.put(url, new SoftReference<Bitmap>(bitmap));
		}
	}

	@SuppressWarnings("unused")
	@SuppressLint("NewApi")
	Bitmap downloadBitmap(String url) {

		URL Url = null;
		HttpURLConnection connection = null;
		InputStream inputStream = null;

		try {
			Url = new URL(url);
			connection = (HttpURLConnection) Url.openConnection();
			connection.setUseCaches(true);
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);
			connection.setInstanceFollowRedirects(true);
			inputStream = connection.getInputStream();

			FlushedInputStream fim = new FlushedInputStream(inputStream);
			Bitmap bmp = BitmapFactory.decodeStream(fim);
			if (bmp != null) {
				long lenght = (bmp.getRowBytes() * bmp.getHeight()) / 1000;
				System.out.println("lenght" + lenght);
				if (lenght > 70000) {
					System.out.println("entro1");
					connection = (HttpURLConnection) Url.openConnection();
					connection.setUseCaches(true);
					connection.setConnectTimeout(30000);
					connection.setReadTimeout(30000);
					connection.setInstanceFollowRedirects(true);
					inputStream = connection.getInputStream();
					BitmapFactory.Options o2 = new BitmapFactory.Options();
					o2.inSampleSize = 12;
					bmp = BitmapFactory.decodeStream(inputStream, null, o2);
				} else if (lenght > 30000) {
					System.out.println("entro2");
					connection = (HttpURLConnection) Url.openConnection();
					connection.setUseCaches(true);
					connection.setConnectTimeout(30000);
					connection.setReadTimeout(30000);
					connection.setInstanceFollowRedirects(true);
					inputStream = connection.getInputStream();
					BitmapFactory.Options o2 = new BitmapFactory.Options();
					o2.inSampleSize = 6;
					bmp = BitmapFactory.decodeStream(inputStream, null, o2);
				}

			}
			return bmp;

		} catch (Exception e) {
			Log.w("", "Error while retrieving bitmap from " + url, e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;

	}

	/*
	 * An InputStream that skips the exact number of bytes provided, unless it
	 * reaches EOF.
	 */
	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int b = read();
					if (b < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

	/**
	 * A fake Drawable that will be attached to the imageView while the download
	 * is in progress.
	 * 
	 * <p>
	 * Contains a reference to the actual download task, so that a download task
	 * can be stopped if a new binding is required, and makes sure that only the
	 * last started download process can bind its result, independently of the
	 * download finish order.
	 * </p>
	 */
	class DownloadedDrawable extends BitmapDrawable {
		private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

		public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
			super();
			bitmapDownloaderTaskReference = new WeakReference<BitmapDownloaderTask>(
					bitmapDownloaderTask);
		}

		public BitmapDownloaderTask getBitmapDownloaderTask() {
			return bitmapDownloaderTaskReference.get();
		}
	}

	/**
	 * @param imageView
	 *            Any imageView
	 * @return Retrieve the currently active download task (if any) associated
	 *         with this imageView. null if there is no such task.
	 */
	private static BitmapDownloaderTask getBitmapDownloaderTask(
			ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getBackground();
			if (drawable instanceof DownloadedDrawable) {
				DownloadedDrawable downloadedDrawable = (DownloadedDrawable) drawable;
				return downloadedDrawable.getBitmapDownloaderTask();
			}
		}
		return null;
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = 6;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}
}
