/**
 * 
 */
package com.quizz.core.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

/**
 * @author M-F.P
 * 
 */
public class StreamUtils {

    public static void copyStream(InputStream is, OutputStream os) {
	final int buffer_size = 1024;
	
	try {
	    byte[] bytes = new byte[buffer_size];
	    for (;;) {

		int count = is.read(bytes, 0, buffer_size);
		if (count == -1)
		    break;
		os.write(bytes, 0, count);
	    }

	} catch (Exception ex) {
	}
    }

    public static String toString(InputStream is) {
	try {
	    return new Scanner(is).useDelimiter("\\A").next();
	} catch (java.util.NoSuchElementException e) {
	    return "";
	}
    }
}
