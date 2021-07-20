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


	public static PlayerList loadPlayerListFromScoreFile(String scoreFileName, Observable observable) {
		return ReadResultPlayers.loadPlayerListFromScoreFile(scoreFileName, observable);
	}
	
	public static void addPlayerToPlayerList(File fileScore, Observable observable) throws IOException {
		ReadResultPlayers.addPlayerToPlayerList(fileScore, observable);
	}
	
	public static void saveAchievements(Player player, String scoreFileName) {
		SaveResultPlayers.saveAchievements(player, scoreFileName);
	}
	
	public static void writePlayerToScoreFile(File scoreFile, Player player) throws IOException {
		SaveResultPlayers.writePlayerToScoreFile(scoreFile, player);
	}
}
