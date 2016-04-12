package wordinator;

import java.util.ArrayList;

import org.parse4j.Parse;
import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;

import utilities.Word;

/**
 * @author Christian Klein
 * @author Victoria Rasavanh
 *
 */
public class Wordinator {
	private static ArrayList<Word> easyWords;
	private static ArrayList<Word> mediumWords;
	private static ArrayList<Word> hardWords;
	
    public static void main( String[] args ) {
		init();
    }
    
	private static void init() {
		Parse.initialize("qUQ4H5VsD1t1fmQvJTZIvM76bPrEgNVR5sWAn9Vy",
				"juVtcRqhhYCjVqFDngDM0KoQYxj1EpEAIPmFuOvA");
		
		easyWords = new ArrayList<Word>();
		mediumWords = new ArrayList<Word>();
		hardWords = new ArrayList<Word>();
		
		getAllWords();
	}
	
    private static void getAllWords() {
    	getSomeWords(easyWords, 1);
    	getSomeWords(mediumWords, 2);
    	getSomeWords(hardWords, 3);
    }

    private static void getSomeWords(ArrayList<Word> list, int rank) {
    	ParseQuery<ParseObject> pq = new ParseQuery<ParseObject>("Word");
		
    	pq.whereEqualTo("Rank", rank);
		
		try {
			for (int i = 0; i < pq.getLimit(); i++) {
				ParseObject w = pq.find().get(i);
				
				list.add(new Word(w));
				
				System.out.println(w.getString("Word"));
			}
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
    }
}
