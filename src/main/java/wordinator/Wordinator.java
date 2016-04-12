package wordinator;

import java.util.LinkedList;
import java.util.Queue;

import org.parse4j.Parse;
import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;

import utilities.Stage;
import utilities.Word;

/**
 * @author Christian Klein
 * @author Victoria Rasavanh
 *
 */
public class Wordinator {
	/**
	 * The current difficulty that the user is playing.
	 */
	private static int currentDifficulty;
	
	/**
	 * The variable for deciding when to increase or decrease difficulties.
	 * Starts at 0, Fails decrease and Perfects increase.
	 * -3 indicates to go down, while +3 indicates to go up
	 */
	private static int change;
	
	/**
	 * Three different queues to hold the words in each difficulty
	 */
	private static Queue<Word> easyWords;
	private static Queue<Word> mediumWords;
	private static Queue<Word> hardWords;
	
	/**
	 * The currently active stage
	 */
	private static Stage currentStage;
	
    public static void main(String[] args) {
		init();
		
		//DEBUGGING
		System.out.println(currentStage.toString());
		System.out.println();
		
		System.out.println("EASY WORDS:");
		for (Word w : easyWords) {
			System.out.println(w);
		}
		System.out.println();
		
		System.out.println("MEDIUM WORDS:");
		for (Word w : mediumWords) {
			System.out.println(w);
		}
		System.out.println();
		
		System.out.println("HARD WORDS:");
		for (Word w : hardWords) {
			System.out.println(w);
		}
		System.out.println();
    }
    
    /**
     * Initializes the game
     */
	private static void init() {
		Parse.initialize(	"qUQ4H5VsD1t1fmQvJTZIvM76bPrEgNVR5sWAn9Vy",
							"juVtcRqhhYCjVqFDngDM0KoQYxj1EpEAIPmFuOvA");
		
		currentDifficulty = 2;
		change = 0;
		
		easyWords 	= new LinkedList<Word>();
		mediumWords = new LinkedList<Word>();
		hardWords 	= new LinkedList<Word>();
		
		getAllWords();
		
		Stage initialStage = new Stage(mediumWords.poll());
		currentStage = initialStage;
	}
	
	/**
	 * Populates all of the word queues
	 */
    private static void getAllWords() {
    	getSomeWords(easyWords, 1);
    	getSomeWords(mediumWords, 2);
    	getSomeWords(hardWords, 3);
    }

    /**
     * Populates a single word queue
     * 
     * @param list
     * 		The list to populate
     * @param rank
     * 		The rank to look for when populating
     */
    private static void getSomeWords(Queue<Word> list, int rank) {
    	ParseQuery<ParseObject> pq = new ParseQuery<ParseObject>("Word");
		
    	pq.whereEqualTo("Rank", rank);
		
		try {
			for (ParseObject w : pq.find()) {
				list.add(new Word(w));
			}
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
    }
    
    private static Stage generateNextStage() {
    	Stage nextStage;
    	
    	if (change <= -3) {
    		if (currentDifficulty > 1) {
    			currentDifficulty--;
    			change = 0;
    		}
    	}
    	else if (change >= 3) {
    		if (currentDifficulty < 3) {
    			currentDifficulty++;
    			change = 0;
    		}
    	}
    	
    	if (currentDifficulty <= 1) {
    		nextStage = new Stage(easyWords.poll());
    	}
    	else if (currentDifficulty == 2) {
    		nextStage = new Stage(mediumWords.poll());
    	}
    	else {
    		nextStage = new Stage(hardWords.poll());
    	}
    	
    	return nextStage;
    }
}
