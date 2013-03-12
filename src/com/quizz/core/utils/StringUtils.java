package com.quizz.core.utils;

public class StringUtils {

	/**
     * Mirror of the unicode table from 00c0 to 024f without diacritics.
     */
    //Latin 1 - Latin Extended-B
    private static final String tab00c0 = "aaaaaaaceeeeiiii" +
        "DNOOOOO\u00d7\u00d8UUUUYI\u00df" +
        "aaaaaaaceeeeiiii" +
        "\u00f0nooooo\u00f7\u00f8uuuuy\u00fey" +
        "aaaaaaccccccccdd" +
        "ddeeeeeeeeeegggg" +
        "gggghhhhiiiiiiii" +
        "iijjjjkkklllllll" +
        "lllnnnnnnnnnoooo" +
        "oooorrrrrrssssss" +
        "ssttttttuuuuuuuu" +
        "uuuuwwyyyzzzzzzf" +
        "bbbbbboccddddoee" +
        "effgyhltikklawnn" +
        "ooooopprsseltttt" +
        "uuuuyyzz3ee3255t" +
        "plll!dddjjjnnnaa" +
        "iioouuuuuuuuuuea" +
        "aaaaaggggkkoooo3" +
        "3jdddgghpnnaaaao" +
        "oaaaaeeeeiiiiooo" +
        "orrrruuuusstt33h" +
        "hnd88zzaaeeooooo" +
        "oooyybnbjbpacclt" +
        "sz??buaeejjqrrryy";

	/**
	 * Returns char without diacritics, converting to lowercase - 7 bit
	 * approximation.
	 * 
	 * @param source char to convert
	 * @return corresponding char without diacritics
	 */
    public static char removeDiacritic(char source) {
		if (source >= '\u00c0' && source <= '\u024f') {
			source = tab00c0.charAt((int) source - '\u00c0');
		}
		return source;
	}
	
	/**
	 * Returns string without diacritics - 7 bit approximation.
	 *
	 * @param source string to convert
	 * @return corresponding string without diacritics
	 */
	public static String removeDiacritic(String source) {
	    char[] vysl = new char[source.length()];
	    for (int i = 0; i < source.length(); i++) {
	        vysl[i] = removeDiacritic(source.charAt(i));
	    }
	    return new String(vysl);
	}
}
