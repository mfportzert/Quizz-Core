/**
 * 
 */
package com.quizz.core.imageloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.quizz.core.utils.ImageUtils;
import com.quizz.core.utils.StreamUtils;

/**
 * @author M-F.P
 * 
 */
public class ImageLoader {
    @SuppressWarnings("unused")
    private static final String TAG = ImageLoader.class.getName();

    MemoryCache mMemoryCache = new MemoryCache();
    FileCache mFileCache;

    private Map<ImageView, String> mImageViews = Collections
	    .synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService mExecutorService;

    private int mDefaultDrawableId = 0;
    private boolean mHQLocalBitmap = true;
    private ImageLoaderListener mDefaultImageLoaderListener;

    public static enum ImageType {
	LOCAL, REMOTE
    }

    public ImageLoader(Context context) {
	mFileCache = new FileCache(context);
	mExecutorService = Executors.newFixedThreadPool(5);
    }

    public void displayImage(String url, ImageView imageView, ImageType imageType) {
	this.displayImage(url, imageView, imageType, mDefaultDrawableId, mDefaultImageLoaderListener);
    }

    public void displayImage(String url, ImageView imageView, ImageType imageType, ImageLoaderListener listener) {
	this.displayImage(url, imageView, imageType, mDefaultDrawableId, listener);
    }
    
    public void displayImage(String url, ImageView imageView, ImageType imageType,
	    int defaultDrawableResId, ImageLoaderListener listener) {
	mImageViews.put(imageView, url);
	Bitmap bitmap = mMemoryCache.get(url);
	if (bitmap != null) {
	    imageView.setImageBitmap(bitmap);
	    if (listener != null) {
		listener.onImageLoaded(bitmap, url, imageView, imageType, true);
	    }
	} else {
	    queuePhoto(url, imageView, imageType, defaultDrawableResId, listener);
	    if (defaultDrawableResId > 0) {
		imageView.setImageResource(defaultDrawableResId);
	    }
	}
    }

    /**
     * Experimental
     */
    public void setHQLocalBitmapEnabled(boolean hqEnabled) {
	mHQLocalBitmap = hqEnabled;
    }

    public void setDefaultImageLoaderListener(ImageLoaderListener listener) {
	mDefaultImageLoaderListener = listener;
    }

    public Bitmap getFromCache(String url) {
	Bitmap bitmap;
	bitmap = mMemoryCache.get(url);
	if (bitmap != null)
	    return bitmap;

	bitmap = decodeFile(mFileCache.getFile(url));
	if (bitmap != null)
	    return bitmap;
	return null;
    }

    private void queuePhoto(String url, ImageView imageView, ImageType imageType,
	    int defaultDrawableResId, ImageLoaderListener listener) {
	PhotoToLoad p = new PhotoToLoad(url, imageView, imageType, defaultDrawableResId, listener);
	mExecutorService.submit(new PhotosLoader(p));
    }

    private Bitmap getBitmap(PhotoToLoad photoToLoad) {
	File f = mFileCache.getFile(photoToLoad.url);

	// from SD cache
	Bitmap b = decodeFile(f);
	if (b != null)
	    return b;

	switch (photoToLoad.imageType) {

	case REMOTE:
	    try {
		// from web
		URL imageUrl = new URL(photoToLoad.url);

		HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
		conn.setConnectTimeout(30000);
		conn.setReadTimeout(30000);
		conn.setInstanceFollowRedirects(true);

		InputStream is = conn.getInputStream();
		OutputStream os = new FileOutputStream(f);
		StreamUtils.copyStream(is, os);
		os.close();

		Bitmap bitmap = decodeFile(f);
		return bitmap;

	    } catch (Exception ex) {
		// Log.e(TAG, ex.getMessage(), ex);
		return null;
	    }

	    // LOCAL
	default:
	    Drawable drawable = ImageUtils.createFromAsset(photoToLoad.imageView.getContext(),
		    photoToLoad.url);

	    if (mHQLocalBitmap) {
		drawable = new BitmapDrawable(photoToLoad.imageView.getContext().getResources(),
			Bitmap.createScaledBitmap(((BitmapDrawable) drawable).getBitmap(),
				drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), true));

		drawable.setFilterBitmap(true);
		((BitmapDrawable) drawable).setAntiAlias(true);
	    }
	    return ((BitmapDrawable) drawable).getBitmap();

	}
    }

    // decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f) {
	try {
	    // decode image size
	    BitmapFactory.Options o = new BitmapFactory.Options();
	    o.inJustDecodeBounds = true;
	    BitmapFactory.decodeStream(new FileInputStream(f), null, o);

	    // Find the correct scale value. It should be the power of 2.
	    final int REQUIRED_SIZE = 70;
	    int width_tmp = o.outWidth, height_tmp = o.outHeight;
	    int scale = 1;
	    while (true) {
		if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
		    break;
		width_tmp /= 2;
		height_tmp /= 2;
		scale *= 2;
	    }

	    // decode with inSampleSize
	    BitmapFactory.Options o2 = new BitmapFactory.Options();
	    o2.inSampleSize = scale;
	    return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);

	} catch (FileNotFoundException e) {
	}

	return null;
    }

    // Task for the queue
    private class PhotoToLoad {
	public String url;
	public ImageView imageView;
	public ImageType imageType;
	public int defaultDrawableResId = 0;
	public ImageLoaderListener listener;

	public PhotoToLoad(String url, ImageView imageView, ImageType imageType, ImageLoaderListener listener) {
	    this.url = url;
	    this.imageView = imageView;
	    this.imageType = imageType;
	    this.listener = listener;
	}

	public PhotoToLoad(String url, ImageView imageView, ImageType imageType,
		int defaultDrawableResId, ImageLoaderListener listener) {
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

	    Bitmap bmp = getBitmap(photoToLoad);
	    mMemoryCache.put(photoToLoad.url, bmp);
	    if (imageViewReused(photoToLoad))
		return;

	    BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
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
	    if (bitmap != null)
		photoToLoad.imageView.setImageBitmap(bitmap);
	    else {
		photoToLoad.imageView
			.setImageResource((photoToLoad.defaultDrawableResId > 0) ? photoToLoad.defaultDrawableResId
				: mDefaultDrawableId);
	    }

	    if (photoToLoad.listener != null) {
		photoToLoad.listener.onImageLoaded(bitmap, photoToLoad.url, photoToLoad.imageView,
			photoToLoad.imageType, false);
	    }
	}
    }

    public void setDefaultDrawableId(int resId) {
	this.mDefaultDrawableId = resId;
    }

    public void clearCache() {
	mMemoryCache.clear();
	mFileCache.clear();
    }

    /**
     * @author M-F.P
     * 
     */
    public interface ImageLoaderListener {
	public void onStartImageLoading(Bitmap bitmap, String url, ImageView imageView,
		ImageType imageType);
	public void onImageLoaded(Bitmap bitmap, String url, ImageView imageView,
		ImageType imageType, boolean fromCache);
    }
}
