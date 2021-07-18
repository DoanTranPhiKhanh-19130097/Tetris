package view.ingame;

public interface IInGame {
	public void pause();
	public void resume();
	public void startNewGame();
	public void requestFocus();
	public void left();
	public void right();
	public void down();
	public void backToHome();
	public void disposeIngame();
	public void setNormalSpeed();
	public void rotate();
	public void exit();
}
