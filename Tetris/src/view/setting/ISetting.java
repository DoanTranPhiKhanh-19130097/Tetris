package view.setting;

import java.util.Observable;

import translation.Language;

public interface ISetting {
	public void changeVolumeSoundTrack(int value);
	public void changeVolumeEffectMusic(int value);
	public void setStateSoundTrack();
	public void setStateEffectMusic();
	public void show();
}
