/**
 * 
 */
package com.quizz.core.imageloader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.widget.ImageView;

/**
 * @author M-F.P
 * 
 */
public class ImageLoader {
	@SuppressWarnings("unused")
	private static final String TAG = ImageLoader.class.getName();

	MemoryCache mMemoryCache = new MemoryCache();

	private Map<ImageView, String> mImageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	ExecutorService mExecutorService;

	private int mDefaultDrawableId = 0;
	private ImageLoaderListener mDefaultImageLoaderListener;

	public static enum ImageType {
		SMALL, MEDIUM, NORMAL
	}

	public ImageLoader(Context context) {
		mExecutorService = Executors.newFixedThreadPool(5);
	}

	public void displayImage(String url, ImageView imageView,
			ImageType imageType) {
		this.displayImage(url, imageView, imageType, mDefaultDrawableId,
				mDefaultImageLoaderListener);
	}

	public void displayImage(String url, ImageView imageView,
			ImageType imageType, ImageLoaderListener listener) {
		this.displayImage(url, imageView, imageType, mDefaultDrawableId,
				listener);
	}

	public void displayImage(String url, ImageView imageView,
			ImageType imageType, int defaultDrawableResId,
			ImageLoaderListener listener) {
		mImageViews.put(imageView, url);
		Bitmap bitmap = mMemoryCache.get(url);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			if (listener != null) {
				listener.onImageLoaded(bitmap, url, imageView, imageType, true);
			}
		} else {
			queuePhoto(url, imageView, imageType, defaultDrawableResId,
					listener);
			if (defaultDrawableResId > 0) {
				imageView.setImageResource(defaultDrawableResId);
			} else {
				imageView.setImageDrawable(null);
			}
		}
	}
	
	public void setDefaultImageLoaderListener(ImageLoaderListener listener) {
		mDefaultImageLoaderListener = listener;
	}

	public Bitmap getFromCache(String url) {
		Bitmap bitmap;
		bitmap = mMemoryCache.get(url);
		if (bitmap != null)
			return bitmap;

		/*bitmap = decodeFile(mFileCache.getFile(url));
		if (bitmap != null)
			return bitmap;*/
		return null;
	}

	private void queuePhoto(String url, ImageView imageView,
			ImageType imageType, int defaultDrawableResId,
			ImageLoaderListener listener) {
		PhotoToLoad p = new PhotoToLoad(url, imageView, imageType,
				defaultDrawableResId, listener);
		mExecutorService.submit(new PhotosLoader(p));
	}

	private Bitmap getBitmap(PhotoToLoad photoToLoad) {
		AssetManager assetManager = photoToLoad.imageView.getContext().getAssets();
	    InputStream istr;
	    try {
	        istr = assetManager.open(photoToLoad.url);
        	Options opts = new BitmapFactory.Options();
        	if (photoToLoad.imageType == ImageType.SMALL) {
        		opts.inSampleSize = 4;
        	} else if (photoToLoad.imageType == ImageType.MEDIUM) {
        		opts.inSampleSize = 2;
        	} 
        	opts.inDither = true;
        	Bitmap bitmap = BitmapFactory.decodeStream(istr, new Rect(), opts);
	        return bitmap;
	        
	    } catch (IOException e) {
	    	e.printStackTrace();
	        return null;
	    }
	}
	/*
	private Bitmap decodeStream(InputStream istr, int wantedWidth) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(istr, null, o);

			// Find the correct scale value. It should be the power of 2.
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < wantedWidth)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			o2.inDither = true;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);

		} catch (FileNotFoundException e) {
		}

		return null;
	}*/

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;
		public ImageType imageType;
		public int width;
		public int height;
		public int defaultDrawableResId = 0;
		public ImageLoaderListener listener;

		public PhotoToLoad(String url, ImageView imageView,
				ImageType imageType, ImageLoaderListener listener) {
			this.url = url;
			this.imageView = imageView;
			this.imageType = imageType;
			this.listener = listener;
		}

		public PhotoToLoad(String url, ImageView imageView,
				ImageType imageType, int defaultDrawableResId,
				ImageLoaderListener listener) {
			this(url, imageView, imageType, listener);
			this.defaultDrawableResId = defaultDrawableResId;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;

			Bitmap bitmap = getBitmap(photoToLoad);
			mMemoryCache.put(photoToLoad.url, bitmap);
			if (imageViewReused(photoToLoad))
				return;

			BitmapDisplayer bd = new BitmapDisplayer(bitmap, photoToLoad);
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = mImageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null) {
				photoToLoad.imageView.setImageBitmap(bitmap);
			} else {
				photoToLoad.imageView.setImageResource(
						(photoToLoad.defaultDrawableResId > 0) ? photoToLoad.defaultDrawableResId
								: mDefaultDrawableId);
			}

			if (photoToLoad.listener != null) {
				photoToLoad.listener.onImageLoaded(bitmap, photoToLoad.url,
						photoToLoad.imageView, photoToLoad.imageType, false);
			}
		}
	}

	public void setDefaultDrawableId(int resId) {
		this.mDefaultDrawableId = resId;
	}

	public void clearCache() {
		mMemoryCache.clear();
	}

	/**
	 * @author M-F.P
	 * 
	 */
	public interface ImageLoaderListener {
		public void onImageLoaded(Bitmap bitmap, String url,
				ImageView imageView, ImageType imageType, boolean fromCache);
	}
}
