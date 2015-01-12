package uk.ac.cam.bch29.wordgame;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import uk.ac.cam.bch29.wordgame.grid.Grid;
import uk.ac.cam.bch29.wordgame.grid.GridGUI;
import uk.ac.cam.bch29.wordgame.grid.Tile;
import uk.ac.cam.bch29.wordgame.grid.TileCollection;
import uk.ac.cam.bch29.wordgame.logic.GameState;
import uk.ac.cam.bch29.wordgame.logic.Dictionary;
import uk.ac.cam.bch29.wordgame.logic.HighScore;

import java.awt.BorderLayout;
import java.io.InputStream;

/**
 * Game This is a basic class that can be modified to create a word game.
 * 
 * Hint: Can this class be converted into a singleton?
 * 
 * @author Bradley Hardy
 * @version 1.0 Released 11/10/2005
 */
public class Game extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final String WindowTitle = "Brad's Word Game";

	private static final String WordsFilePath = "main/resources/words.txt";

	private static final String UsedWordsText = "Used words:                    ";
	private static final String ScoreText = "Your score is:  ";
	private static final String HighScoreText = "High score:  ";
	private static final String ConfirmText = "Accept Word Selection";
	private static final String SurrenderText = "Surrender / (New Game)";

	private Dictionary dic = null;

	private GameState state = null;
	private HighScore highScore = null;

	private JFrame frame = null;
	private JPanel gameArea = null;
	private GridGUI gui = null;

	private JLabel score = null;
	private JLabel highScoreLabel = null;
	private JTextArea completedWords = null;
	private JLabel currentWord = null;

	/**
	 * Creates an instance of the Game.
	 */
	public Game() {
		// build the GUI as soon as the default constructor is called.
		buildGUI();
	}

	/**
	 * Updates the GUI to match the game state.
	 */
	private void updateGUI() {
		// all tiles are deselected and inactive by default
		gui.setTileForeground(Color.yellow);
		gui.setTileBackground(Color.blue);

		// gray out all active (confirmed) tiles
		for (Tile t : state.confirmedTiles()) {
			gui.setTileForeground(state.grid().positionOf(t), Color.gray);
			gui.setTileBackground(state.grid().positionOf(t), Color.lightGray);
		}

		// make selected tiles stand out
		for (Tile t : state.selectedTiles()) {
			gui.setTileForeground(state.grid().positionOf(t), Color.green);
			gui.setTileBackground(state.grid().positionOf(t), Color.red);
		}

		// update the high score
		highScore.newScore(state.score());
		
		// update GUI text
		score.setText(ScoreText + state.score());
		completedWords.setText(UsedWordsText + state.usedWordsText());
		String selectedString = state.selectedString();
		// to stop the call to frame.pack() from hiding the label
		currentWord.setText(selectedString.length() == 0 ? " " : selectedString);
		
		highScoreLabel.setText(HighScoreText + highScore.highScore());
	}

	/**
	 * Builds the score panel.
	 * 
	 * @return
	 */
	private JPanel scorePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(30, 0));
		score = new JLabel(" ");
		panel.add(score, BorderLayout.WEST);

		highScoreLabel = new JLabel(" ");
		panel.add(highScoreLabel, BorderLayout.CENTER);

		return panel;
	}

	/**
	 * Builds the completed words panel.
	 * 
	 * @return
	 */
	private JPanel completedWordsPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		completedWords = new JTextArea(UsedWordsText);
		completedWords.setEditable(false);
		completedWords.setBackground(getBackground());
		panel.add(completedWords, BorderLayout.CENTER);
		return panel;
	}

	/**
	 * Builds the button panel.
	 * 
	 * @return
	 */
	private JPanel buttonPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JButton confirmButton = new JButton(ConfirmText);
		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				state.confirmWord();
				updateGUI();
			}
		});
		panel.add(confirmButton, BorderLayout.WEST);

		JButton surrenderButton = new JButton(SurrenderText);
		surrenderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				resetGame();
			}
		});
		panel.add(surrenderButton, BorderLayout.EAST);
		return panel;
	}

	private GridGUI makeGridGUI() {
		GridGUI gui = new GridGUI(state.grid());
		gui.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				Tile source = (Tile) actionEvent.getSource();
				state.selectTile(source);
				updateGUI();
			}
		});

		return gui;
	}

	/**
	 * Builds the panel containing the grid and the current word.
	 * 
	 * @return
	 */
	private JPanel gameAreaPanel() {
		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout(0, 2));
		currentWord = new JLabel(" ");
		Font font = new Font("Arial", 0, 16);
		currentWord.setFont(font);
		panel.add(currentWord, BorderLayout.SOUTH);

		gui = makeGridGUI();
		panel.add(gui, BorderLayout.NORTH);

		return panel;
	}

	private Grid makeGrid() {
		TileCollection collection = new TileCollection();
		return new Grid(6, 6, collection);
	}

	/**
	 * Constructs each element of the game's GUI
	 */
	public void buildGUI() {
		// build the dictionary
		InputStream wordStream = getClass().getClassLoader()
				.getResourceAsStream(WordsFilePath);
		dic = new Dictionary(wordStream);

		// get the high score
		highScore = new HighScore("score.dat");

		frame = new JFrame(WindowTitle);

		state = new GameState(makeGrid(), dic);

		frame.setTitle(WindowTitle);

		frame.getContentPane().setLayout(new BorderLayout(2, 0));
		gameArea = gameAreaPanel();
		frame.getContentPane().add(gameArea, BorderLayout.WEST);
		frame.getContentPane().add(buttonPanel(), BorderLayout.SOUTH);
		frame.getContentPane().add(scorePanel(), BorderLayout.NORTH);
		frame.getContentPane().add(completedWordsPanel(), BorderLayout.EAST);
		frame.pack();
		frame.setResizable(false);
		frame.toFront();

		frame.setBackground(Color.lightGray);
		frame.setVisible(true);
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				highScore.writeScore();
			}
		});

		updateGUI();
	}

	public void resetGame() {
		highScore.writeScore();

		gameArea.remove(gui);
		state = new GameState(makeGrid(), dic);
		gui = makeGridGUI();
		gameArea.add(gui, BorderLayout.NORTH);
		frame.pack();

		updateGUI();
	}
}