package wordinator;


import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

import org.parse4j.Parse;
import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
	private static Level currentLevel;
	
	/**
	 * GUI stuff
	 */
	private Scene startScene;

	private static Scene gameScene;

	private Scene endScene;
	private static BorderPane gamePane;
	private static VBox startBox, gameLayout;
	private static HBox scrambledBox, dBox, playerBox;
	private static Button startBtn;
	private static Text dTxt;
	private Stage stage;
	
	/**
	 * @param args the command line arguments
	 * @param args
	 */
    public static void main(String[] args) {
    	launch(args); //launches GUI
    	
		//DEBUGGING
		System.out.println(currentLevel.toString());
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
		
		currentDifficulty = 2;
		change = 0;
		
		easyWords 	= new LinkedList<Word>();
		mediumWords = new LinkedList<Word>();
		hardWords 	= new LinkedList<Word>();
		
		getAllWords();
		
		Level initialStage = new Level(mediumWords.poll());
		currentLevel = initialStage;
		
		//GUI
		//Game Scene
		scrambledBox = new HBox();
		scrambledBox.setPadding(new Insets(10));
    	dBox = new HBox(); 
    	dBox.setPadding(new Insets(10));
    	playerBox = new HBox();
    	playerBox.setPadding(new Insets(10));
    	gameLayout = new VBox();
    	gamePane = new BorderPane();
    	
    	Collections.shuffle(currentLevel.getLetters());
    	
    	//make each letter a button, and add to box
    	for(int i = 0; i < currentLevel.getLetters().size(); i++) {
    		String letter = currentLevel.getLetters().get(i).toString();
    		final Button b = new Button(letter);
    		b.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent e) {
					if(b.getParent().equals(scrambledBox)) {
						s2pBtnSwitch(b);
					}
					else if(b.getParent().equals(playerBox)) {
						p2sBtnSwitch(b);
					}
					else {
						System.out.println("Letter button error.");
					}
				}
    		});
    		scrambledBox.getChildren().add(b);
    	}
    	
    	dTxt = new Text(currentLevel.getDescription());
    	dBox.getChildren().add(dTxt);
    	gameLayout.getChildren().addAll(scrambledBox, dBox, playerBox);
    	gamePane.getChildren().add(gameLayout);
    	
    	gameScene = new Scene(gamePane, 450, 450);
    	//start scene
    	startBtn = new Button("Start Game");
    	startBox = new VBox();
    	startBox.getChildren().add(startBtn);
    	
	}
	
	//private static void setGameScene() {}
	
	/**
	 * Puts scrambledBox buttons down to the playerBox
	 * @param b
	 */
	private static void s2pBtnSwitch(Button b) {
		scrambledBox.getChildren().remove(b);
		playerBox.getChildren().add(b);
	}
	/**
	 * Puts playerBox buttons to the scrambledBox
	 * @param b
	 */
	private static void p2sBtnSwitch(Button b) {
		playerBox.getChildren().remove(b);
		scrambledBox.getChildren().add(b);
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
    
    private static Level generateNextStage() {
    	Level nextStage;
    	
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
    		nextStage = new Level(easyWords.poll());
    	}
    	else if (currentDifficulty == 2) {
    		nextStage = new Level(mediumWords.poll());
    	}
    	else {
    		nextStage = new Level(hardWords.poll());
    	}
    	
    	return nextStage;
    }
    
    
    
}
