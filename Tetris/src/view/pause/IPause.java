package view.pause;
import view.high_score.IView;

public interface IPause extends IView {
	public void resume();
	public void show();
	public boolean isShow();
	public void dispose();
	public void restart();
	public void showHighScore();
	public void showTutorial();
	public void showSetting();
	public void backToHome();
	public void translate();
	public void exit();
}
