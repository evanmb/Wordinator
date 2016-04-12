package utilities;

import org.parse4j.Parse;

public class Word {
	private String word;
	private String description;
	private int rank;
	
	public Word(String givenWord, String givenDescription, int givenRank) {
		this.word = givenWord;
		this.description = givenDescription;
		this.rank = givenRank;
	}
	
	public String getWord() {
		return this.word;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public int getRank() {
		return this.rank;
	}
}
