package wordinator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.parse4j.Parse;
import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utilities.Level;
import utilities.Word;

/**
 * @author Christian Klein
 * @author Victoria Rasavanh
 *
 */
@SuppressWarnings("restriction")
public class Wordinator extends Application{
	/**
	 * The maximum difficulty in the database
	 */
	private static final int MAX_DIFFICULTY = 5;
	
	/**
	 * The width of the game's letter tiles
	 */
	private static final double TILE_WIDTH = 40;
	
	/**
	 * The current difficulty that the user is playing.
	 */
	private static int currentDifficulty;
	
	/**
	 * The variable for deciding when to increase or decrease difficulties.
	 * Starts at 0, Fails decrease and Perfects increase.
	 * -3 indicates to go down, while +3 indicates to go up
	 */
	private static int difficultyChecker;
	
	/**
	 * Master queue to hold all words
	 */
	private static ArrayList<Queue<Word>> allWords;
	
	/**
	 * The currently active level
	 */
	private static Level currentLevel;
	
	/**
	 * GUI stuff
	 */
	private static Stage 		stage;
	private static Scene 		startScene;
	private static Scene 		endScene;
	private static Scene 		gameScene;
	private static BorderPane 	gamePane;
	private static VBox 		startBox, gameLayout;
	private static FlowPane 	scrambledBox, dBox;
	private static HBox 		playerBox;
	private static Button 		startBtn;
	private static Text 		dTxt;

    public static void main(String[] args) {
    	launch(args); //launches GUI
    	
    	/*
		//DEBUGGING
		System.out.println(currentLevel.toString());
		System.out.println();
		
		for (Queue<Word> list : allWords) {
			for (Word w : list) {
				System.out.println(w.toString());
			}
			
			System.out.println();
		}
		*/
    }
    
    /**
     * The GUI
     */
    public void start(Stage primaryStage) {
    	setUp();
    	stage = primaryStage;
    	
    	//Button initialization
    	startBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e){
            	stage.setScene(gameScene);
            	stage.show();
            } 	
        });
    	
        startScene = new Scene(startBox, 400, 400);
        
        
        primaryStage.setTitle("Wordinator"); //sets the name of the Window
        primaryStage.setScene(startScene);
        primaryStage.show();
    }
    
    /**
     * Initializes the game
     */
	private static void setUp() {
		Parse.initialize(	"qUQ4H5VsD1t1fmQvJTZIvM76bPrEgNVR5sWAn9Vy",
							"juVtcRqhhYCjVqFDngDM0KoQYxj1EpEAIPmFuOvA");
		
		currentDifficulty = MAX_DIFFICULTY / 2;
		difficultyChecker = 0;
		
		allWords = new ArrayList<Queue<Word>>();
		
		getAllWords();
		
		currentLevel = new Level(allWords.get(currentDifficulty - 1).poll());
		
		//GUI
		//Game Scene
		scrambledBox = new FlowPane();
		scrambledBox.setPadding(new Insets(10));
		scrambledBox.setMinWidth(TILE_WIDTH * 10);
		
    	dBox = new FlowPane(); 
    	dBox.setPadding(new Insets(10));
    	
    	playerBox = new HBox();
    	playerBox.setPadding(new Insets(10));
    	
    	gameLayout = new VBox();
    	gamePane = new BorderPane();
    	
    	gameScene = new Scene(gamePane, 450, 450);
    	
    	displayLevel(currentLevel);
    	
    	gameLayout.getChildren().addAll(scrambledBox, dBox, playerBox);
    	gamePane.getChildren().add(gameLayout);
    	
    	//start scene
    	startBtn = new Button("Start Game");
    	startBox = new VBox();
    	startBox.getChildren().add(startBtn);
    	
	}

	/**
	 * Puts scrambledBox buttons down to the playerBox
	 * @param b
	 */
	private static void s2pBtnSwitch(Button b) {
		for (int i = 0; i < playerBox.getChildren().size(); i++) {
			if (((Button) playerBox.getChildren().get(i)).getText().length() < 1) {
				playerBox.getChildren().add(i, b);
				playerBox.getChildren().remove(i + 1);
				scrambledBox.getChildren().remove(b);
				
				if (i + 1 == currentLevel.getWord().length()) {
					checkAnswer();
				}
				
				break;
			}
		}
	}
	
	/**
	 * Puts playerBox buttons to the scrambledBox
	 * @param b
	 */
	private static void p2sBtnSwitch(Button b) {
		Button blank = new Button("");
		blank.setMinWidth(TILE_WIDTH);
		
		playerBox.getChildren().remove(b);
		playerBox.getChildren().add(blank);
		scrambledBox.getChildren().add(b);
	}

	/**
	 * Checks if the user has guessed the word
	 */
	private static void checkAnswer() {
		String attempt = "";
		
		for (Node n : playerBox.getChildren()) {
			attempt += ((Button) n).getText();
		}
		
		if (attempt.equals(currentLevel.getWord())) {
			//Color playerBox letters green
			for (Node n : playerBox.getChildren()) {
				n.setStyle("-fx-background-color: cyan;");
				((Button) n).setOnAction(new EventHandler<ActionEvent>(){
					public void handle(ActionEvent e) {
						
					}
	    		});
			}
			
			/*
			 * 
			 * MAKE "NEXT" BUTTON APPEAR HERE
			 * 
			 */
			
			//PLACE BELOW IN "NEXT" ONCLICK()
			difficultyChecker++;
			currentLevel = generateNextLevel();
			displayLevel(currentLevel);
			//PLACE ABOVE IN "NEXT" ONCLICK()
		}
		else {
			difficultyChecker--;
		}
	}
	
	/**
	 * Displays the given level on the game scene
	 */
	private static void displayLevel(Level levelToDisplay) {
		scrambledBox.getChildren().clear();
		playerBox.getChildren().clear();
		
		//make each letter a button, and add to box
    	for (Character c : currentLevel.getLetters()) {
    		String letter = c.toString();
    		
    		final Button b = new Button(letter);
    		b.setMinWidth(TILE_WIDTH);
    		
    		b.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent e) {
					if (b.getParent().equals(scrambledBox)) {
						s2pBtnSwitch(b);
					}
					else if (b.getParent().equals(playerBox)) {
						p2sBtnSwitch(b);
					}
					else {
						System.out.println("Letter button error.");
					}
				}
    		});
    		
    		scrambledBox.getChildren().add(b);
    	}
		
    	for (int i = 0; i < levelToDisplay.getWord().length(); i++) {
    		Button b = new Button("");
    		b.setMinWidth(TILE_WIDTH);
    		
    		playerBox.getChildren().add(b);
    	}
    	
		dTxt = new Text(currentLevel.getDescription());
		dTxt.setWrappingWidth(gameScene.getWidth() - 10);
		
    	dBox.getChildren().clear();
		dBox.getChildren().add(dTxt);
	}
	
	/**
	 * Populates all of the word queues
	 */
    private static void getAllWords() {
    	for (int i = 1; i <= MAX_DIFFICULTY; i++) {
    		allWords.add(getWordsOfRank(i));
    	}
    }
    
    /**
     * Populates a single word queue
     */
    private static Queue<Word> getWordsOfRank(int rank) {
    	Queue<Word> words = new LinkedList<Word>();
    	
    	ParseQuery<ParseObject> pq = new ParseQuery<ParseObject>("Word");
		
    	pq.whereEqualTo("Rank", rank);
		
		try {
			for (ParseObject w : pq.find()) {
				words.add(new Word(w));
			}
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		
		Collections.shuffle((List<?>) words);
		
		return words;
    }
    
    /**
     * Creates the next level
     * 
     * @return The next level
     */
    private static Level generateNextLevel() {
    	//Change difficulty based on performance
    	if (difficultyChecker <= -3) {
    		if (currentDifficulty > 1) {
    			currentDifficulty--;
    			difficultyChecker = 0;
    		}
    	}
    	else if (difficultyChecker >= 3) {
    		if (currentDifficulty < MAX_DIFFICULTY) {
    			currentDifficulty++;
    			difficultyChecker = 0;
    		}
    	}
    	
    	//Make sure the current difficulty list isn't empty
    	int initialDifficulty = currentDifficulty;
    	while (allWords.get(currentDifficulty - 1).size() < 1) {
    		currentDifficulty++;
    		if (currentDifficulty > MAX_DIFFICULTY) {
    			currentDifficulty = initialDifficulty - 1;
    			break;
    		}
    	}
    	while (allWords.get(currentDifficulty - 1).size() < 1) {
    		currentDifficulty--;
    		if (currentDifficulty <= 1) {
    			return new Level(new Word("Default", "NO WORDS FOUND", 0));
    		}
    	}
    	
    	return new Level(allWords.get(currentDifficulty - 1).poll());
    }
}
