package view.setting;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import translation. Language;
import controller.IController;
import model.Game;

public class Setting extends JDialog implements ISetting, Observer {

	private IController controller;
	private JLabel title, bgMsLb, effectLb, bgMsValueLb, effectValueLb;
	private JSlider bgMsSld, effectSld;
	private JPanel titlePn, bgMsPn, effectSldPn, btnPn;
	private JButton cancleBt;

	public Setting(IController controller, Observable observableModel, Observable observableLanguage) {
		this.controller = controller;
		observableModel.addObserver(this);
		observableLanguage.addObserver(this);

		this.getContentPane().setBackground(new Color(189, 215, 255));
		setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		add(title = new JLabel());
		title.setPreferredSize(new Dimension(200, 100));
		title.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
		title.setHorizontalAlignment(JLabel.CENTER);

		add(bgMsPn = new JPanel());
		bgMsPn.setBackground(new Color(189, 215, 255));
		bgMsPn.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 20));
		bgMsPn.add(bgMsLb = new JLabel());
		bgMsLb.setCursor(new Cursor(Cursor.HAND_CURSOR));
		bgMsLb.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				setStateBgMs();
			}
		});
		bgMsPn.add(bgMsSld = new JSlider(0, 100));
		bgMsSld.setBackground(Color.WHITE);
		bgMsSld.setCursor(new Cursor(Cursor.HAND_CURSOR));
		bgMsPn.add(bgMsValueLb = new JLabel(bgMsSld.getValue() + ""));
		bgMsSld.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				bgMsValueLb.setText(bgMsSld.getValue() + "");
				changeVolumeBgMs(bgMsSld.getValue());
			}
		});

		add(effectSldPn = new JPanel());
		effectSldPn.setBackground(new Color(189, 215, 255));
		effectSldPn.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 20));
		effectSldPn.add(effectLb = new JLabel());
		effectLb.setCursor(new Cursor(Cursor.HAND_CURSOR));
		effectLb.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				setStateEffect();
			}
		});
		effectSldPn.add(effectSld = new JSlider(0, 100));
		effectSld.setBackground(Color.WHITE);
		effectSld.setCursor(new Cursor(Cursor.HAND_CURSOR));
		effectSldPn.add(effectValueLb = new JLabel(effectSld.getValue() + ""));
		effectSld.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				effectValueLb.setText(effectSld.getValue() + "");
				changeVolumeEffect(effectSld.getValue());
			}
		});

		add(btnPn = new JPanel());
		btnPn.setBackground(new Color(189, 215, 255));
		btnPn.setPreferredSize(new Dimension(200, 50));
		btnPn.setLayout(new FlowLayout(FlowLayout.RIGHT));
		btnPn.add(cancleBt = new JButton());
		cancleBt.setPreferredSize(new Dimension(100, 30));
		cancleBt.setBackground(new Color(240, 205, 139));
		cancleBt.setCursor(new Cursor(Cursor.HAND_CURSOR));
		cancleBt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				dispose();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				super.mouseEntered(e);
				cancleBt.setBackground(new Color(240, 177, 60));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				super.mouseExited(e);
				cancleBt.setBackground(new Color(240, 205, 139));
			}
		});

		setSize(350, 350);
		setResizable(false);
		setIconImage(new ImageIcon("resource/img/104.png").getImage());
		setLocationRelativeTo(null);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Game) {
			Game game = (Game) o;
			if (game.isRunningBgMs())
				bgMsLb.setIcon(new ImageIcon("resource/img/102.png"));
			else
				bgMsLb.setIcon(new ImageIcon("resource/img/103.png"));

			if (game.isRunningEffect())
				effectLb.setIcon(new ImageIcon("resource/img/101.png"));
			else
				effectLb.setIcon(new ImageIcon("resource/img/100.png"));

			if (bgMsSld != null)
				bgMsSld.setValue(game.getValueMs());
			if (effectSld != null)
				effectSld.setValue(game.getValueEffect());
		}

		if (o instanceof Language) {
			 Language lan = ( Language) o;
			this.setTitle(lan.getSettingName());
			title.setText(lan.getSettingName());
			if (cancleBt != null)
				cancleBt.setText(lan.getCancleName());
		}
	}

	@Override
	public void changeVolumeBgMs(int value) {
		controller.changeVolumeBgMs(value);
	}

	@Override
	public void changeVolumeEffect(int value) {
		controller.changeVolumeEffect(value);
	}

	@Override
	public void setStateBgMs() {
		controller.setStateBgMs();
	}

	@Override
	public void setStateEffect() {
		controller.setStateEffect();
	}
}
