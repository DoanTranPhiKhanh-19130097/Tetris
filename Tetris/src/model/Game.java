package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import translation.Chinese;
import translation.English;
import translation.Language;
import translation.VietNamese;
import audio.Audio;
import data.DataManager;
import factory.IShapeFactory;
import factory.ShapeRandomFactory;
import obj.AShape;
import obj.Player;
import obj.PlayerList;
import view.language.ILanguage;

public class Game extends Observable implements IGame {
	private int[][] board;
	private AShape currentShape;
	private AShape nextShape;
	private boolean inGame;
	private boolean pause;
	private boolean gameOver;
	private long startTime;
	private int prepareTime;
	private IShapeFactory shapeFactory;
	private int score;
	private PlayerList playerList;
	private Player player;
	private boolean stateBgMs;
	private boolean stateEffect;
	private int valueMs;
	private int valueEffect;
	private Audio collisionEffect, bgMs;
	private ArrayList<AShape> storeShapes;


	public Game() {
		playerList = new PlayerList();
		board = new int[HEIGHT][WIDTH];
		shapeFactory = new ShapeRandomFactory();
		pause = true;
		gameOver = false;
		stateBgMs = true;
		stateEffect = true;
		valueMs = 80;
		valueEffect = 80;
		startTime = System.nanoTime();
		storeShapes = new ArrayList<AShape>();
		playerList = DataManager.loadPlayerList("resource/highscore/high_score.txt", this);
		try {
			bgMs = new Audio("resource/sound/bg_music.wav");
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	// play audio
	private void playCollisionEffect() {
		try {
			collisionEffect = new Audio("resource/sound/effect_clear.wav");
			collisionEffect.changVolume(valueEffect);
			collisionEffect.start();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	// update game
	@Override
	public void update() {
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
		
		if (collisionEffect != null)
			collisionEffect.changVolume(valueEffect);
		if (bgMs != null)
			bgMs.changVolume(valueMs);
		if (inGame && stateBgMs && !pause) {
			bgMs.start();
			bgMs.loop();
		}
		if (!inGame || !stateBgMs || pause)
			bgMs.stop();

		// update cho observer
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
					DataManager.saveAchievements(player, "resource/highscore/high_score.txt");
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
				if (stateEffect)
					playCollisionEffect();
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
		if (inGame) 
			setPlayer(player.getName());
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
	public void changeVolumeBgMs(int value) {
		this.valueMs = value;
	}

	@Override
	public void changeVolumeEffect(int value) {
		this.valueEffect = value;
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

	public void setStateBgMs() {
		this.stateBgMs = !stateBgMs;
	}

	public void setStateEffect() {
		this.stateEffect = !stateEffect;
	}

	public int getValueEffect() {
		return valueEffect;
	}

	public int getValueMs() {
		return valueMs;
	}

	public boolean isRunningBgMs() {
		return stateBgMs;
	}

	public boolean isRunningEffect() {
		return stateEffect;
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
		Language.getInstance().setLanguage(new English());
	}

	@Override
	public void tranlateVietnamese() {
		Language.getInstance().setLanguage(new VietNamese());

	}

	@Override
	public void tranlateChinese() {
		Language.getInstance().setLanguage(new Chinese());
	}
	
	
}
