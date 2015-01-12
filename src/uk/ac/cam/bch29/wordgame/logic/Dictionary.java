package uk.ac.cam.bch29.wordgame.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;

/**
 * A dictionary of words which allows querying whether a word exists, or if a string is
 * a prefix of an existing word. It uses dependency injection so that it can be tested.
 * 
 * @author Bradley Hardy
 *
 */
public class Dictionary {
	private HashSet<String> words = null;
	private HashSet<String> partialWords = null;
	
	/**
	 * Creates a dictionary from an InputStream.
	 * 
	 * @param from an InputStream containing newline-terminated words. It must terminate.
	 * @param charset the character set to use when reading.
	 */
	public Dictionary(InputStream from, Charset charset) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(from, charset));
			String line;
			
			words = new HashSet<String>();
			partialWords = new HashSet<String>();
			while ((line = br.readLine()) != null) {
				if (line.length() > 0) {
					line = line.toLowerCase();
					words.add(line);
					
					// save memory with the observation that all individual
					// letters in English are valid partial words
					for (int i = 2; i < line.length(); i++) {
						partialWords.add(line.substring(0, i));
					}
				}
			}
		}
		catch (IOException e) {
			throw new RuntimeException("Error reading input stream for Words constructor!", e);
		}
	}
	
	/**
	 * Creates a dictionary from an InputStream using the UTF-8 character set.
	 * 
	 * @param from an InputStream containing newline-terminated words. It must terminate.
	 */
	public Dictionary(InputStream from) {
		this(from, StandardCharsets.UTF_8);
	}
	
	/**
	 * Returns true if the given string is in the dictionary.
	 * 
	 * @param s the string to check for.
	 * @return is the string a word?
	 */
	public boolean isWord(String s) {
		return words.contains(s.toLowerCase());
	}
	
	/**
	 * Returns true if the given string is a prefix of a word in the dictionary (or a whole word).
	 * 
	 * @param s the string to check for.
	 * @return is the string a partial word?
	 */
	public boolean isPartialWord(String s) {
		return s.length() == 0 || s.length() == 1 || partialWords.contains(s.toLowerCase()) || isWord(s);
	}
}
