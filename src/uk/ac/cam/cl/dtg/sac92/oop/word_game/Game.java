package uk.ac.cam.cl.dtg.sac92.oop.word_game;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.JFrame;

import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.Grid;
import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.GridGUI;
import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.Tile;
import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.TileCollection;

import java.awt.BorderLayout;

/**
 * Game This is a basic class that can be modified to create a word game.
 * 
 * Hint: Can this class be converted into a singleton?
 * 
 * @author Stephen Cummins
 * @version 1.0 Released 11/10/2005
 */
public class Game extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates an instance of the Game.
	 */
	public Game() {
		// build the GUI as soon as the default constructor is called.
		buildGUI();
	}

	/**
	 * This method will construct each element of the game's GUI
	 */
	public void buildGUI() {
		final JFrame frame = new JFrame("Java Word Game");

		TileCollection collection = new TileCollection();
		final Grid grid = new Grid(6, 6, collection);
		final GridGUI gui = new GridGUI(grid);
		gui.setTileForeground(Color.yellow);
		gui.setTileBackground(Color.blue);

		JPanel controls = new JPanel();
		JPanel wordEntry = new JPanel();
		JPanel buttons = new JPanel();

		buttons.setLayout(new BorderLayout());
		wordEntry.setLayout(new BorderLayout());
		controls.setLayout(new BorderLayout());

		gui.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				Tile source = (Tile) actionEvent.getSource();
				gui.setTileBackground(grid.positionOf(source), Color.red);
				gui.setTileForeground(grid.positionOf(source), Color.green);
				source.active(true); // changes flag on tile to show
										// used;
			}
		});

		frame.setTitle("Java Word Game");

		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(gui, BorderLayout.WEST);
		frame.pack();
		frame.setResizable(false);
		frame.toFront();

		frame.setBackground(Color.lightGray);
		frame.setVisible(true);
	}
}