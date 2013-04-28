package com.wmkj.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 *
 * @Name: com.wmkj.activity.SplashActivity Help.java
 * @Author: nylqd
 * @Date: 2012-11-7 
 * @Description: 
 *
 */
public class Help extends Activity {
	Button backButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.help);
		
		
		// 更改背景图片
		LinearLayout llayout = (LinearLayout) findViewById(R.id.llayout);
		SetBackgroundImage.setBackGround(Help.this, llayout);
		
		backButton = (Button) findViewById(R.id.help_backBut);
		
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Help.this.finish();
			}
		});
	}
	

}
