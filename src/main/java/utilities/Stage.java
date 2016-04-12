package utilities;

import java.util.ArrayList;
import java.util.Random;

/**
 * 
 * @author Christian Klein
 *
 */
public class Stage {
	private String word;
	private String description;
	private int difficulty;
	private ArrayList<Character> letters;
	
	public Stage(Word givenWord) {
		this.word 			= givenWord.getWord();
		this.description 	= givenWord.getDescription();
		this.difficulty 	= givenWord.getRank();
		pickLetters();
	}
	
	private void pickLetters() {
		Random r = new Random();
		
		for (int i = 0; i < word.length(); i++) {
			letters.add(word.charAt(i));
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
