package com.wmkj.activity;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * 
 * @Name: cn.wongxming.ringer.RingToggle AutoProfiles.java
 * @Author: nylqd
 * @Date: 2012-11-6
 * @Description:
 * 
 */
public class AutoProfiles extends Activity {

	private ToggleButton swicther;
	private RadioGroup modeGroup;
	private int currentmode, modetobe;
	private Boolean flag = true;
	private AudioManager audio;


	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.auto);
		
		// 更改背景图片
		LinearLayout llayout = (LinearLayout) findViewById(R.id.auto_llayout);
		SetBackgroundImage.setBackGround(AutoProfiles.this, llayout);

		swicther = (ToggleButton) findViewById(R.id.swicther);
		modeGroup = (RadioGroup) findViewById(R.id.modeGroup);
		//settedmode = (TextView) findViewById(R.id.settedmode);

		audio = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

		swicther.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				swicther.setChecked(isChecked);
				//如果 打开
				if (isChecked == true) {

					// 选择震动
					if (modetobe == 1) {
						if (flag == true) {
							getInitring(audio);
							vibrate(audio);
						}
						if (flag == false) {
							turnToBefore();
						}

					}

					// 选择静音
					if (modetobe == 2) {
						if (flag == true) {
							getInitring(audio);
							noRingAndVibrate(audio);
						}
						if (flag == false) {
							turnToBefore();
						}
					}
				} else {    //如果关闭
					turnToBefore();
				}

			}
		});

		modeGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == R.id.vibrate) {
							modetobe = 1;
						} else if (checkedId == R.id.silent) {
							modetobe = 2;
						}
					}
				});

	}

	// 变回原来的情景模式
	void turnToBefore() {
		if (currentmode == AudioManager.RINGER_MODE_NORMAL) {
			ring(audio);
		} else if (currentmode == AudioManager.RINGER_MODE_NORMAL) {
			ringAndVibrate(audio);
		} else if (currentmode == AudioManager.RINGER_MODE_VIBRATE) {
			vibrate(audio);
		} else if (currentmode == AudioManager.RINGER_MODE_SILENT) {
			noRingAndVibrate(audio);
		}
	}

	// 获取当前情景模式
	void getInitring(AudioManager audio) {
		// 取得手机的初始音量，并初始化进度条
		// int volume=audio.getStreamVolume(AudioManager.STREAM_RING); //取得初始音量
		// 取得初始模式，并分别设置图标
		currentmode = audio.getRingerMode();
		// System.out.println(currentmode);// 取得初始模式
	}

	// 铃声模
	void ring(AudioManager audio) {
		audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
				AudioManager.VIBRATE_SETTING_OFF);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
				AudioManager.VIBRATE_SETTING_OFF);
		Toast.makeText(this, "设置成功！当前为铃声", Toast.LENGTH_LONG).show();
	}

	// 铃声和震动
	void ringAndVibrate(AudioManager audio) {
		audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
				AudioManager.VIBRATE_SETTING_ON);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
				AudioManager.VIBRATE_SETTING_ON);
		Toast.makeText(this, "设置成功！当前为铃声加振动", Toast.LENGTH_LONG).show();
	}

	// 震动
	void vibrate(AudioManager audio) {
		audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
				AudioManager.VIBRATE_SETTING_ON);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
				AudioManager.VIBRATE_SETTING_ON);
		Toast.makeText(this, "设置成功！当前为振动", Toast.LENGTH_LONG).show();
	}

	// 静音
	void noRingAndVibrate(AudioManager audio) {
		audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
				AudioManager.VIBRATE_SETTING_OFF);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
				AudioManager.VIBRATE_SETTING_OFF);
		Toast.makeText(this, "设置成功！当前为无声无振动", Toast.LENGTH_LONG).show();
	}

}
