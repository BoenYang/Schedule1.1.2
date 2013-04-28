package com.wmkj.activity;

import java.io.File;

import com.wmkj.utils.FileUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class SplashActivity extends Activity {

	private FileUtil fileUtil;
	private String DATAfolder = "/databases/";
	SharedPreferences preferences;// 使用SharedPreferences来记录程序的使用次数
	public int screenHeight;
	public int screenWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置surfaceView是全屏显示的
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		setContentView(R.layout.splash_activity);
		
		fileUtil = new FileUtil();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenHeight = getWindowManager().getDefaultDisplay().getHeight();
		screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		// 读取SharedPreferences中需要的数据

		preferences = getSharedPreferences("count", MODE_WORLD_READABLE);

		int count = preferences.getInt("count", 0);

		// 判断程序与第几次运行，如果是第一次运行则跳转到引导页面
		/************************* 第一次运行 *************************/
		if (count == 0) {

			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					Intent intent = new Intent();
					intent.setClass(SplashActivity.this,
							GuideViewActivity.class);
					startActivity(intent);
					SplashActivity.this.finish();

				}
			}, 1000);

		} else {
			/************************* 非第一次运行 *************************/
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					File SDfile = new File(FileUtil.SDPATH
							+ "/ScheduleBackup/course.db");
					File DATAfile = new File(FileUtil.DATAPATH
							+ "/data/com.wmkj.activity/databases/course.db");

					if (DATAfile.exists()) {
						// data有数据库 正常跳转
						Intent intent = new Intent();
						intent.setClass(SplashActivity.this,
								MainInterface.class);
						startActivity(intent);
						SplashActivity.this.finish();
					} else if (!DATAfile.exists() && SDfile.exists()) {
						// data没有数据库，sd有数据库 提示恢复
						creatDialog()
								.setTitle("提示")
								.setMessage("SD卡存在备份，是否恢复？")
								.setPositiveButton("是",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {

												// 创建databases文件夹
												File file = new File(
														FileUtil.DATAPATH
																+ "/data/com.wmkj.activity/"
																+ DATAfolder);
												if (!file.exists()) {
													file.mkdirs();
												}

												if (fileUtil.FileRestore()) {
													Toast.makeText(
															SplashActivity.this,
															"恢复成功",
															Toast.LENGTH_SHORT)
															.show();
													// 恢复成功，正常跳转
													Intent intent = new Intent();
													intent.setClass(
															SplashActivity.this,
															MainInterface.class);
													startActivity(intent);
													SplashActivity.this
															.finish();

												} else {
													Toast.makeText(
															SplashActivity.this,
															"恢复失败",
															Toast.LENGTH_SHORT)
															.show();
													// 恢复失败，跳转到somequestions
													Intent intent = new Intent();
													intent.setClass(
															SplashActivity.this,
															SomeQuestions.class);
													startActivity(intent);
													SplashActivity.this
															.finish();
												}
											}

										})
								.setNegativeButton("否",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.cancel();
												Intent intent = new Intent();
												intent.setClass(
														SplashActivity.this,
														SomeQuestions.class);
												startActivity(intent);
												SplashActivity.this.finish();
											}

										}).show();
					} else if (!DATAfile.exists() && !SDfile.exists()) {
						// data没有数据库，sd没有数据库 跳转到somequestions
						Intent intent = new Intent();
						intent.setClass(SplashActivity.this,
								SomeQuestions.class);
						startActivity(intent);
						SplashActivity.this.finish();
					}
				}
			}, 1000);

		}
		Editor editor = preferences.edit();
		editor.putInt("count", ++count);// 存入数据
		editor.commit();// 提交修改

	}

	Handler handler = new Handler();

	/************************* 创建Dialog *************************/
	private AlertDialog.Builder creatDialog() {
		return new AlertDialog.Builder(this);
	}

}
