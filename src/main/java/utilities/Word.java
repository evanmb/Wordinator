package utilities;

import org.parse4j.ParseObject;

/**
 * 
 * @author Christian Klein
 * 
 * This may be an unneeded class, but it does make the data management easier.
 *
 */
public class Word {
	private String word;
	private String description;
	private int rank;
	
	public Word(ParseObject po) {
		fillFromParseObject(po);
	}
	
	public Word(String givenWord, String givenDescription, int givenRank) {
		this.word 			= givenWord;
		this.description 	= givenDescription;
		this.rank 			= givenRank;
	}
	
	public void fillFromParseObject(ParseObject po) {
		this.word 			= po.getString("Word");
		this.description 	= po.getString("Description");
		this.rank 			= po.getInt("Rank");
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
