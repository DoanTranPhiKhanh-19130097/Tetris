package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Observable;
import java.util.StringTokenizer;

import obj.Player;
import obj.PlayerList;

public class ReadResultPlayers {

	private static PlayerList playerList = new PlayerList();

	private static BufferedReader bufferedReader = null;
	private static FileReader fileReader = null;

	public static PlayerList loadPlayerListFromScoreFile(String scoreFileName, Observable observable) {
		try {
			File scoreFile = new File(scoreFileName);
			if (scoreFile.exists()) {
				addPlayerToPlayerList(scoreFile, observable);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeFileReader();
		}
		return playerList;
	}

	public static void addPlayerToPlayerList(File fileScore, Observable observable) throws IOException {
		fileReader = new FileReader(fileScore);
		bufferedReader = new BufferedReader(fileReader);
		String line = bufferedReader.readLine();
		while (line != null) {
			StringTokenizer tokenizer = new StringTokenizer(line, "\t");
			String name = tokenizer.nextToken();
			int score = Integer.parseInt(tokenizer.nextToken());
			playerList.add(new Player(name, score, observable));
			line = bufferedReader.readLine();
		}
	}

	public static void closeFileReader() {
		try {
			if (bufferedReader != null)
				bufferedReader.close();
			if (fileReader != null)
				fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
