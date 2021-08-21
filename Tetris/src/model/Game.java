package model;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Observable;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import translation.TranslateChinese;
import translation.TranslateEnglish;
import translation.Language;
import translation.TranslateVietNamese;
import audio.Audio;
import data.ReadResultPlayers;
import data.SaveResultPlayers;
import factory.IShapeFactory;
import factory.ShapeRandomFactory;
import obj.AShape;
import obj.Player;
import obj.PlayerList;
import view.language.ILanguage;

public class Game extends Observable implements IGame {

	private boolean inGame;
	private boolean pause;
	private boolean gameOver;
	private long startTime;
	private int prepareTime;

	private int[][] board;
	private AShape currentShape;
	private AShape nextShape;
	private IShapeFactory shapeFactory;
	private ArrayList<AShape> storeShapes;

	private int score;
	private PlayerList playerList;
	private Player player;

	private Audio playSoundtrack;
	private Audio playEffectMusic;
	

	private ReadResultPlayers read;
	private SaveResultPlayers save;

	public Game() {
		playerList = new PlayerList();
		board = new int[HEIGHT][WIDTH];
		shapeFactory = new ShapeRandomFactory();
		pause = true;
		gameOver = false;

		startTime = System.nanoTime();
		storeShapes = new ArrayList<AShape>();
		
		read = new ReadResultPlayers();
		save = new SaveResultPlayers();
		playerList = read.loadPlayerListFromScoreFile("resource/highscore/high_score.txt", this);
		try {
			playSoundtrack = new Audio("resource/sound/bg_music.wav", false, 80);
			playEffectMusic = new Audio("resource/sound/effect_clear.wav", false, 80);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateInGame() {
		if (inGame && !pause && !gameOver) {
			if (prepareTime <= 0)
				currentShape.update();
			else {
				long time = (System.nanoTime() - startTime) / 1000000000;
				if (time >= 1) {
					prepareTime--;
					startTime = System.nanoTime();
				}
			}
		}
		
		playSoundtrack.playSound(pause, true);
		updateToObserver();

	}

	public void updateToObserver() {
		super.setChanged();
		super.notifyObservers();
	}

	// set up shape 3 hinh gan nhat khong trung nhau
	public void setNextShape() {
		nextShape = shapeFactory.creatShape(this);
		int i = storeShapes.size() - 1;
		int count = 0;
		while (i >= 0) {
			if (nextShape.getColorFlag() == storeShapes.get(i).getColorFlag())
				setNextShape();
			i--;
			count++;
			if (count >= 2) {
				break;
			}
		}
	}

	public void setCurrentShape() {
		currentShape = nextShape;
		storeShapes.add(nextShape);

		int[][] coords = currentShape.getCoords();
		for (int i = 0; i < coords.length; i++) {
			for (int j = 0; j < coords[i].length; j++) {
				if (coords[i][j] != 0 && board[currentShape.getY() + i + 1][currentShape.getX() + j] != 0) {
					player.setPlaying(false);
					playerList.add(player);
					save.saveAchievements(player, "resource/highscore/high_score.txt");
					gameOver = true;
					break;
					
				}
			}
		}
		setNextShape();
	}

	// kiem tra 1 cac hang da day chua
	public void checkLine() {
		int height = board.length - 1;
		for (int row = height; row > 0; row--) {
			int count = 0;
			for (int col = 0; col < board[0].length; col++) {
				if (board[row][col] != 0) {
					count++;
				}
				board[height][col] = board[row][col];
			}
			if (count < board[0].length)
				height--;
			else {
				score += count * 10;
				playEffectMusic.playSound(pause, false);
			}
		}
	}

	@Override
	public void pause() {
		pause = true;
	}

	@Override
	public void resume() {
		pause = false;
	}

	@Override
	public void left() {
		currentShape.left();
	}

	@Override
	public void right() {
		currentShape.right();
	}

	@Override
	public void setNormalSpeed() {
		currentShape.setNormalSpeed();
	}

	@Override
	public void rotate() {
		currentShape.rotate();
	}

	@Override
	public void start() {
		inGame = true;
		board = new int[HEIGHT][WIDTH];
		score = 0;
		pause = false;
		prepareTime = 3;
		gameOver = false;
		if (inGame) {
			setPlayer(player.getName());
		}
		setNextShape();
		setCurrentShape();
		super.notifyObservers();
		super.setChanged();
	}

	public void addScore() {
		score += currentShape.getScore();
	}

	@Override
	public void setPlayer(String name) {
		player = new Player(name, score, this);
		player.setPlaying(true);
	}

	@Override
	public void setInGame(boolean op) {
		inGame = op;
	}

	@Override
	public void down() {
		currentShape.down();
	}

	// getter/setter
	public int getScore() {
		return score;
	}

	public PlayerList getPlayerList() {
		return playerList;
	}

	@Override
	public void changeVolumeSoundtrack(int value) {
		playSoundtrack.setValueSound(value);
	}

	@Override
	public void changeVolumeEffect(int value) {
		playEffectMusic.setValueSound(value);
	}

	public void setStateSoundtrack() {
		playSoundtrack.setMute(!playSoundtrack.getMute());
	}

	public void setStateEffectMusic() {
		playEffectMusic.setMute(!playEffectMusic.getMute());
	}

	public int getValueEffect() {
		return playEffectMusic.getValueSound();
	}

	public int getValueSoundtrack() {
		return playSoundtrack.getValueSound();
	}

	public boolean isRunningSoundtrack() {
		return playSoundtrack.getMute();
	}

	public boolean isRunningEffect() {
		return playEffectMusic.getMute();
	}

	public AShape getNextShape() {
		return this.nextShape;
	}

	public AShape getCurrentShape() {
		return this.currentShape;
	}

	public Player getPlayer() {
		return player;
	}

	public int[][] getBoard() {
		return board;
	}

	public int getPrepareTime() {
		return prepareTime;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	@Override
	public void tranlateEnglish() {
		Language.getInstance().setLanguage(new TranslateEnglish());
	}

	@Override
	public void tranlateVietnamese() {
		Language.getInstance().setLanguage(new TranslateVietNamese());

	}

	@Override
	public void tranlateChinese() {
		Language.getInstance().setLanguage(new TranslateChinese());
	}

}
