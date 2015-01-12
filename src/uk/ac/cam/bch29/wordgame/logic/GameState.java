package uk.ac.cam.bch29.wordgame.logic;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.cam.bch29.wordgame.grid.*;

public class GameState {
	private int score = 0;
	private ArrayList<Selection> usedWords = null;
	private Selection selection = null;
	private Grid grid = null;
	private Dictionary dic = null;
	
	/**
	 * Creates a GameState from a Grid and a Dictionary.
	 * @param grid
	 * @param dic
	 */
	public GameState(Grid grid, Dictionary dic) {
		this.grid = grid;
		this.dic = dic;
		
		score = 0;
		usedWords = new ArrayList<Selection>();
		
		selection = new Selection(grid, dic);
	}
	
	/**
	 * Returns the grid.
	 * @return
	 */
	public Grid grid() {
		return grid;
	}
	
	/**
	 * Returns a textual representation of the words used so far
	 * along with their point values.
	 * @return
	 */
	public String usedWordsText() {
		String result = "";
		
		for (Selection s : usedWords) {
			result += "\n" + s.selectedString() + " (" + s.value() + ")";
		}
		
		return result;
	}
	
	/**
	 * Returns the score so far.
	 * @return
	 */
	public int score() {
		return score;
	}
	
	/**
	 * Returns a string representation of the currently selected tiles.
	 * @return
	 */
	public String selectedString() {
		return selection.selectedString();
	}
	
	/**
	 * Selects the given tile.
	 * @param tile
	 */
	public void selectTile(Tile tile) {
		selection.select(tile, true);
	}
	
	/**
	 * Confirms the selected word if possible; otherwise does nothing.
	 */
	public void confirmWord() {
		if (dic.isWord(selectedString())) {
			for (Tile t : selection.tiles()) {
				t.active(true);
			}
			
			usedWords.add(selection);
			score += selection.value();
			selection = new Selection(grid, dic);
		}
	}
	
	/**
	 * Returns a collection of all confirmed tiles.
	 * @return
	 */
	public Collection<Tile> confirmedTiles() {
		ArrayList<Tile> result = new ArrayList<Tile>();
		for (Selection s : usedWords) {
			result.addAll(s.tiles());
		}
		return result;
	}
	
	/**
	 * Returns a collection of currently selected tiles.
	 * @return
	 */
	public Collection<Tile> selectedTiles() {
		return selection.tiles();
	}
}
