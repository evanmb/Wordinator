package utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * 
 * @author Christian Klein
 *
 */
public class Level {
	/**
	 * The word given to create the level
	 */
	private Word wordData;
	
	/**
	 * The string word that the user has to guess
	 */
	private String word;
	
	/**
	 * The description for the provided word
	 */
	private String description;
	
	/**
	 * The difficulty of this level
	 */
	private int difficulty;
	
	/**
	 * The letters to display to the user
	 * Contains all correct letters and 5 extra letters
	 */
	private ArrayList<Character> letters;
	
	private int attempts;
	
	/**
	 * Creates a state from a given Word
	 * 
	 * @param givenWord
	 * 		The word to create the level from
	 */
	public Level(Word givenWord) {
		this.wordData = givenWord;
		
		this.word 			= givenWord.getWord();
		this.description 	= givenWord.getDescription();
		this.difficulty 	= givenWord.getRank();
		
		letters = new ArrayList<Character>();
		
		attempts = 0;
		
		pickLetters();
	}
	
	/**
	 * Picks the letters to display to the user
	 * These will be all of the correct letters plus 5 additional random letters
	 */
	private void pickLetters() {
		Random r = new Random();
		
		for (char c : word.toCharArray()) {
			letters.add(c);
		}

		for (int i = 0; i < 5; i++) {
			letters.add((char) (r.nextInt(26) + 'a'));
		}
		
		Collections.shuffle(letters);
	}
	
	public int getDifficulty() {
		return this.difficulty;
	}
	
	public String getWord() {
		return this.word;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public ArrayList<Character> getLetters() {
		return this.letters;
	}
	
	@Override
	public String toString() {
		return "CURRENT STAGE:\nAttempts - " + attempts + "\n" + wordData.toString();
	}
}