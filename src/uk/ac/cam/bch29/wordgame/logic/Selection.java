package uk.ac.cam.bch29.wordgame.logic;

import java.util.Collection;
import java.util.Stack;
import java.awt.Point;

import uk.ac.cam.bch29.wordgame.grid.*;

public class Selection {
	private Grid grid = null;
	private Dictionary words = null;

	private Stack<Tile> selected = null;

	/**
	 * Creates a new Selection with the given Grid and Dictionary
	 * @param grid
	 * @param words
	 */
	public Selection(Grid grid, Dictionary words) {
		this.grid = grid;
		this.words = words;
		selected = new Stack<Tile>();
	}

	/**
	 * Returns true if Tile a is adjacent or diagonal to Tile b
	 * @param a
	 * @param b
	 * @return
	 */
	private boolean canReach(Tile a, Tile b) {
		Point ap = grid.positionOf(a);
		Point bp = grid.positionOf(b);

		return Math.abs(ap.getX() - bp.getX()) <= 1
				&& Math.abs(ap.getY() - bp.getY()) <= 1;
	}

	/**
	 * Returns the selected tiles in a Collection.
	 * @return
	 */
	public Collection<Tile> tiles() {
		return selected;
	}

	/**
	 * Like Stack#contains, but for reference equality
	 * @param tile
	 * @return
	 */
	private boolean isTileSelected(Tile tile) {
		for (Tile t : selected) {
			if (t == tile) return true;
		}
		
		return false;
	}
	
	/**
	 * Adds the given tile to the selection, or clears it
	 * if it is already selected.
	 * @param tile
	 * @param onlyValidWords if true, only valid partial words may be selected.
	 */
	public void select(Tile tile, boolean onlyValidWords) {
		// can't select tiles that are already active
		if (tile.checkActive())
			return;
		
		if (isTileSelected(tile)) {
			// deselect tiles up to the clicked one
			while (selected.pop() != tile);
		} else if (selected.isEmpty() || canReach(tile, selected.peek())) {
			selected.push(tile);

			if (onlyValidWords && !words.isPartialWord(selectedString())) {
				selected.pop();
			}
		} else {
			selected = new Stack<Tile>();
			selected.push(tile);
		}
	}

	/**
	 * Returns the string that represents the selected tiles.
	 * @return
	 */
	public String selectedString() {
		StringBuilder sb = new StringBuilder();

		for (Tile t : selected) {
			sb.append(t.letter());
		}

		return sb.toString();
	}
	
	/**
	 * Returns the point value of the selection.
	 * @return
	 */
	public int value() {
		int val = 0;
		for (Tile t : selected) {
			val += t.value();
		}
		
		return val * selected.size();
	}
}
