package uk.ac.cam.bch29.wordgame.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import uk.ac.cam.bch29.wordgame.logic.Dictionary;

public class TestStreamDictionary {
	
	private void testWords(Dictionary words, String[] with) {
		for (int i = 0; i < with.length; i++) {
			String w = with[i];
			assertTrue(w + " is a word", words.isWord(w));
			String partial = w.substring(0, w.length()/2);
			assertTrue(partial + " partial word", words.isPartialWord(partial));
		}
	}
	
	@Test
	public void fromString() {
		String[] wordsArr = new String[] {
				"elephant",
				"giraffe",
				"a",
				"the",
				"poor",
				"hhhhhh",
				"jk",
				"ALLCAPS",
				"someCaps"
		};
		String wordsString = "";
		for (String word : wordsArr)
			wordsString += word + "\n";
		InputStream stream = new ByteArrayInputStream(wordsString.getBytes(StandardCharsets.UTF_8));
		
		Dictionary words = new Dictionary(stream);
		
		testWords(words, wordsArr);
	}
	
	@Test
	public void fromFile() throws FileNotFoundException {
		InputStream stream = getClass().getClassLoader().getResourceAsStream("main/resources/words.txt");
		Dictionary words = new Dictionary(stream);
		
		String[] testWith = new String[] {
				"elephant",
				"gorilla",
				"tea",
				"aardvark",
				"FerrY",
				"zoologist"
		};
		
		testWords(words, testWith);
	}

}
