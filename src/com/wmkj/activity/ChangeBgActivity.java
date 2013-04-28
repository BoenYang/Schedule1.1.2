package com.wmkj.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 
 * @Name: com.wmkj.activity.SplashActivity ChangBgActivity.java
 * @Author: nylqd
 * @Date: 2012-11-3
 * @Description:
 * 
 */
public class ChangeBgActivity extends Activity implements OnClickListener {

	Button bg01, bg02, bg03, bg_backBut;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.changebg);

		// 更改背景图片
		LinearLayout llayout = (LinearLayout) findViewById(R.id.llayout);
		SetBackgroundImage.setBackGround(ChangeBgActivity.this, llayout);

		bg01 = (Button) findViewById(R.id.bg01);
		bg02 = (Button) findViewById(R.id.bg02);
		bg03 = (Button) findViewById(R.id.bg03);
		bg_backBut = (Button) findViewById(R.id.bg_backBut);
		bg01.setOnClickListener(this);
		bg02.setOnClickListener(this);
		bg03.setOnClickListener(this);
		bg_backBut.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bg01:
			SetBackgroundImage.saveBackground(ChangeBgActivity.this, "bg1");
			ChangeBgActivity.this.finish();
			Toast.makeText(ChangeBgActivity.this, "更换背景成功", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.bg02:
			SetBackgroundImage.saveBackground(ChangeBgActivity.this, "bg2");
			ChangeBgActivity.this.finish();
			Toast.makeText(ChangeBgActivity.this, "更换背景成功", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.bg03:
			SetBackgroundImage.saveBackground(ChangeBgActivity.this, "bg3");
			ChangeBgActivity.this.finish();
			Toast.makeText(ChangeBgActivity.this, "更换背景成功", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.bg_backBut:
			ChangeBgActivity.this.finish();
			break;

		}

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_HOME:
			return true;
		case KeyEvent.KEYCODE_BACK:
			ChangeBgActivity.this.finish();
			return true;
		case KeyEvent.KEYCODE_CALL:
			return true;
		case KeyEvent.KEYCODE_SYM:
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			return true;
		case KeyEvent.KEYCODE_VOLUME_UP:
			return true;
		case KeyEvent.KEYCODE_STAR:
			return true;
		}
		return true;
	}

}
