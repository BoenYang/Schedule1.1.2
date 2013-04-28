package com.wmkj.activity;

import java.util.Calendar;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

public class SomeQuestions extends Activity {

	private Button some_questions_title_backBut, some_questions_saveBut;
	private EditText school_opendayText;
	private EditText how_many_weekText;
	private EditText how_many_classText;
	private String firstMonday;
	private int weeknum;
	private int how_many_class;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.some_questions);
		
		TableLayout llayout = (TableLayout) findViewById(R.id.question);
		SetBackgroundImage.setBackGround(SomeQuestions.this, llayout);

		some_questions_title_backBut = (Button) findViewById(R.id.some_questions_title_backBut);
		some_questions_saveBut = (Button) findViewById(R.id.some_questions_saveBut);
		how_many_weekText = (EditText) findViewById(R.id.how_many_weekText);
		how_many_classText = (EditText) findViewById(R.id.how_many_classText);
		school_opendayText = (EditText) findViewById(R.id.school_opendayText);

		/************************* 返回按钮事件 *************************/
		some_questions_title_backBut
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						SomeQuestions.this.finish();
					}
				});

		some_questions_saveBut.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (how_many_weekText.getText().toString().length() != 0) {
					weeknum = Integer.parseInt(how_many_weekText.getText()
							.toString());
				}
				if (how_many_classText.getText().toString().length() != 0) {
					how_many_class = Integer.parseInt(how_many_classText
							.getText().toString());
				}
				if (firstMonday == null) {
					Toast.makeText(SomeQuestions.this, "请输入第一周星期一日期",
							Toast.LENGTH_LONG).show();
				} else if (how_many_class == 0) {
					Intent intent = new Intent();
					intent.putExtra("firstday", firstMonday);
					intent.setClass(SomeQuestions.this, MainInterface.class);
					startActivity(intent);
					SomeQuestions.this.finish();
				} else {
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putInt("weeknum", weeknum);
					bundle.putInt("classnum", how_many_class);
					bundle.putString("firstday", firstMonday);
					intent.putExtras(bundle);
					intent.setClass(SomeQuestions.this, MainInterface.class);
					startActivity(intent);
					SomeQuestions.this.finish();
				}
			}
		});

		school_opendayText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker datePicker, int year,
							int month, int dayOfMonth) {
						// Calendar月份是从0开始,所以month要加1
						school_opendayText.setText((month + 1) + "月"
								+ dayOfMonth + "日");
						// 开学第一周的周一
						firstMonday = year + "-" + (month + 1) + "-"
								+ dayOfMonth;
					}
				};
				Calendar calendar = Calendar.getInstance();
				DatePickerDialog datePickerDialog = new DatePickerDialog(
						SomeQuestions.this, dateListener, calendar
								.get(Calendar.YEAR), calendar
								.get(Calendar.MONTH), calendar
								.get(Calendar.DAY_OF_MONTH));
				datePickerDialog.show();
			}
		});
	}
}
