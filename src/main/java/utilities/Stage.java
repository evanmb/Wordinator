package utilities;

import java.util.ArrayList;
import java.util.Random;

/**
 * 
 * @author Christian Klein
 *
 */
public class Stage {
	/**
	 * The word that the user has to guess
	 */
	private String word;
	
	/**
	 * The description for the provided word
	 */
	private String description;
	
	/**
	 * The difficulty of this stage
	 */
	private int difficulty;
	
	/**
	 * The letters to display to the user
	 * Contains all correct letters and 5 extra letters
	 */
	private ArrayList<Character> letters;
	
	/**
	 * Creates a state from a given Word
	 * 
	 * @param givenWord
	 * 		The word to create the stage from
	 */
	public Stage(Word givenWord) {
		this.word 			= givenWord.getWord();
		this.description 	= givenWord.getDescription();
		this.difficulty 	= givenWord.getRank();
		
		letters = new ArrayList<Character>();
		
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
}
