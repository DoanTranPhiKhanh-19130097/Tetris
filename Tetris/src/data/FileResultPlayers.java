package data;

import java.io.File;
import java.io.IOException;
import java.util.Observable;

import obj.Player;

public abstract class FileResultPlayers {

	public <T> void excuteFile(String scoreFileName, Observable observable) {
		try {
			File scoreFile = new File(scoreFileName);
			testFile(scoreFile, observable);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeFile();
		}
	}
	
	public abstract <T> void testFile(File file, T player) throws IOException ;
	public abstract void closeFile();

}
