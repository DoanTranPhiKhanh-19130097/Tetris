package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.StringTokenizer;

import obj.Player;
import obj.PlayerList;

public class DataManager {
	//lay danh sach nguoi choi tu file
	public static PlayerList loadPlayerList(String fileName, Observable observable) {
		PlayerList playerList = new PlayerList();

		BufferedReader bufferedReader = null;
		FileReader fileReader = null;

		try {
			File file = new File(fileName);

			if (file.exists()) {

				fileReader = new FileReader(file);
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
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null)
					bufferedReader.close();
				if (fileReader != null)
					fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return playerList;
	}

	//luu thanh tich vao file
	public static void saveAchievements(Player player, String fileName) {

		BufferedWriter bufferedWriter = null;
		FileWriter fileWriter = null;

		try {

			File file = new File(fileName);

			if (!file.exists())
				file.createNewFile();

			fileWriter = new FileWriter(file.getAbsoluteFile(), true);
			bufferedWriter = new BufferedWriter(fileWriter);

			bufferedWriter.write(player.toString() + "\n");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
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
}
