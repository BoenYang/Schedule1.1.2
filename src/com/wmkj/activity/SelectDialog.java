package com.wmkj.activity;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wmkj.utils.FileUtil;

public class SelectDialog extends AlertDialog {

	private Button dialog_backupBut;
	private Button dialog_restoreBut;
	private Button autoprofilesBut;
	private Button helpBut;
	private Button aboutBut;
	public Notification Nf;
	public NotificationManager Nm;
	public PendingIntent Pi;
	private Button changbgBut;
	private Button time;

	private String folderName = "/ScheduleBackup/";
	private FileUtil fileUtil = new FileUtil();
	private Dialog aboutDialog;
	private Dialog remindDialog;
	private Dialog autoprofilesDialog;
	private Context context;
	private boolean checked = true;

	public SelectDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public SelectDialog(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_dialog);
		dialog_backupBut = (Button) findViewById(R.id.dialog_backupBut);
		dialog_restoreBut = (Button) findViewById(R.id.dialog_restoreBut);
		// autoprofilesBut = (Button) findViewById(R.id.autoprofilesBut);
		helpBut = (Button) findViewById(R.id.helpBut);
		aboutBut = (Button) findViewById(R.id.aboutBut);
		changbgBut = (Button) findViewById(R.id.changbgBut);
		time = (Button) findViewById(R.id.time);
		
		dialog_backupBut.setOnClickListener(new BtnListener());
		aboutBut.setOnClickListener(new BtnListener());
		changbgBut.setOnClickListener(new BtnListener());
		time.setOnClickListener(new BtnListener());
		dialog_restoreBut.setOnClickListener(new BtnListener());
		
		helpBut.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(context, Help.class);
				context.startActivity(intent);
				SelectDialog.this.cancel();
			}
		});

	}

	class BtnListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.dialog_backupBut:

				File file = new File(FileUtil.SDPATH
						+ "/ScheduleBackup/course.db");
				if (file.exists()) {
					creatDialog()
							.setTitle("提示")
							.setMessage("已存在备份，是否覆盖？")
							.setPositiveButton("是",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											if (fileUtil.FileBackup()) {
												Toast.makeText(getContext(),
														"备份成功",
														Toast.LENGTH_SHORT)
														.show();
											} else {
												Toast.makeText(getContext(),
														"备份失败",
														Toast.LENGTH_SHORT)
														.show();
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
										}

									}).show();
				} else {
					File folder = new File(FileUtil.SDPATH + "/ScheduleBackup/");
					if (folder.exists()) {
						if (fileUtil.FileBackup()) {
							Toast.makeText(getContext(), "备份成功",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getContext(), "备份失败",
									Toast.LENGTH_SHORT).show();
						}
					} else {
						creatDialog()
								.setTitle("提示")
								.setMessage("目标文件夹不存在是否创建？")
								.setPositiveButton("是",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {

												// 在sd卡上创建文件夹
												if (Environment.MEDIA_MOUNTED.equals(Environment
														.getExternalStorageState())) {
													File sdPath = Environment
															.getExternalStorageDirectory();
													String path = sdPath
															.getPath()
															+ folderName;
													File file = new File(path);
													if (!file.exists()) {
														file.mkdirs();
													}
												}

												if (fileUtil.FileBackup()) {
													Toast.makeText(
															getContext(),
															"您的数据成功的备份在ScheduleBackup文件夹中",
															Toast.LENGTH_SHORT)
															.show();
												} else {
													Toast.makeText(
															getContext(),
															"备份失败",
															Toast.LENGTH_SHORT)
															.show();
												}
											}
										})
								.setNegativeButton("否",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// stub
												dialog.cancel();
												Toast.makeText(getContext(),
														"备份失败",
														Toast.LENGTH_SHORT)
														.show();
											}
										}).show();
					}
				}
				break;
			case R.id.dialog_restoreBut:

				File file1 = new File(FileUtil.SDPATH
						+ "/ScheduleBackup/course.db");
				if (file1.exists()) {
					creatDialog()
							.setTitle("提示")
							.setMessage("确认恢复？")
							.setPositiveButton("是",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											if (fileUtil.FileRestore()) {
												Toast.makeText(getContext(),
														"恢复成功",
														Toast.LENGTH_SHORT)
														.show();
											} else {
												Toast.makeText(getContext(),
														"恢复失败",
														Toast.LENGTH_SHORT)
														.show();
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
										}
									}).show();
				} else {
					Toast.makeText(getContext(), "您要恢复的文件不存在",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.aboutBut:
				aboutDialog = new Dialog(getContext(), R.style.dialog);
				aboutDialog.getWindow().setContentView(R.layout.about_dialog);
				SelectDialog.this.cancel();
				aboutDialog.show();
				Button about_dialog_ok = (Button) aboutDialog
						.findViewById(R.id.about_dialog_ok);

				about_dialog_ok.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						aboutDialog.cancel();
					}
				});
				break;
			case R.id.changbgBut:
				intent.setClass(context, ChangeBgActivity.class);
				context.startActivity(intent);
				//((Activity)context).finish();
				SelectDialog.this.cancel();
				break;
			case R.id.time:
				intent.setClass(context, ClassTimeActivity.class);
				context.startActivity(intent);
				SelectDialog.this.cancel();
			}
		}
	}

	/************************* 创建Dialog *************************/
	private AlertDialog.Builder creatDialog() {
		return new AlertDialog.Builder(getContext());
	}

}