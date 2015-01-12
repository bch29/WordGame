package uk.ac.cam.bch29.wordgame.logic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class HighScore {
	private int highScore = 0;
	private boolean changed = false;
	private boolean error = false;
	private String filePath = null;

	public HighScore(String filePath) {
		this.filePath = filePath;
		try {
			FileInputStream file = new FileInputStream(filePath);
			highScore = file.read();
			file.close();
		} catch (FileNotFoundException e) {
			highScore = 0;
		} catch (IOException e) {
			error = true;
			highScore = -1;
		}
	}

	public int highScore() {
		return highScore;
	}

	public void newScore(int score) {
		if (score > highScore) {
			highScore = score;
			changed = true;
		}
	}

	public void writeScore() {
		if (!error && changed) {
			try {
				FileOutputStream file = new FileOutputStream(filePath);

				file.flush();
				file.write(highScore);

				file.close();
			} catch (Exception e) {
				// the file couldn't be written, oh well
			}
		}
	}
}
