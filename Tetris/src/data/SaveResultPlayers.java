package data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import obj.Player;

public class SaveResultPlayers {

	private BufferedWriter bufferedWriter = null;
	private FileWriter fileWriter = null;

	public void saveAchievements(Player player, String scoreFileName) {
		try {
			File scoreFile = new File(scoreFileName);
			if (!scoreFile.exists()) {
				scoreFile.createNewFile();
			}
			writePlayerToScoreFile(scoreFile, player);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeFileWriter();
		}
	}

<<<<<<< HEAD
	public void writePlayerToScoreFile(File scoreFile, Player player) throws IOException {
=======
	public static void writePlayerToScoreFile(File scoreFile, Player player) throws IOException {
>>>>>>> a1e6a4c013825e69e0d8583c2b2b319b80b970aa
		fileWriter = new FileWriter(scoreFile.getAbsoluteFile(), true);
		bufferedWriter = new BufferedWriter(fileWriter);
		bufferedWriter.write(player.toString() + "\n");
	}

	public void closeFileWriter() {
		try {
			if (bufferedWriter != null)
				bufferedWriter.close();
			if (fileWriter != null)
				fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
