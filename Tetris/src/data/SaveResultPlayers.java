package data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import obj.Player;

public class SaveResultPlayers {

	private static BufferedWriter bufferedWriter = null;
	private static FileWriter fileWriter = null;

	public static void saveAchievements(Player player, String scoreFileName) {
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

	public static void writePlayerToScoreFile(File scoreFile, Player player) throws IOException {
		fileWriter = new FileWriter(scoreFile.getAbsoluteFile(), true);
		bufferedWriter = new BufferedWriter(fileWriter);
		bufferedWriter.write(player.toString() + "\n");
	}

	public static void closeFileWriter() {
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
