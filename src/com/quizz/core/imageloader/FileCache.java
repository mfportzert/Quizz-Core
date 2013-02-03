/**
 * 
 */
package com.quizz.core.imageloader;

import java.io.File;

import android.content.Context;

/**
 * @author M-F.P
 * 
 */
public class FileCache {

	private File cacheDir;
	private static final String CACHE_DIR_NAME = "Android/data/org.aurora.android";
	
	public FileCache(Context context) {
		
		// Find the dir to save cached images
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
					CACHE_DIR_NAME);
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	public File getFile(String url) {
		
		// Images are identified by hashcode.
		String filename = String.valueOf(url.hashCode());
		// Another possible solution
		// String filename = URLEncoder.encode(url);
		File f = new File(cacheDir, filename);
		return f;

	}

	public void clear() {
		
		File[] files = cacheDir.listFiles();
		if (files == null)
			return;
		for (File f : files)
			f.delete();
	}
}
