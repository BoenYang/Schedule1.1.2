package com.wmkj.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

/**
 * 
 * @Name: com.wmkj.activity.SplashActivity TimeDialog.java
 * @Author: nylqd
 * @Date: 2012-11-3
 * @Description:
 * 
 */
public class TimeDialog extends AlertDialog {

	private EditText classtimebegin, classtimeend;
	public String str1, str2;
	Context context;
	public Calendar calendar01 = Calendar.getInstance();
	public Calendar calendar02 = Calendar.getInstance();
	Date date01, date02;

	public TimeDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public TimeDialog(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timedialog);
		classtimebegin = (EditText) findViewById(R.id.classtimebegin);
		classtimeend = (EditText) findViewById(R.id.classtimeend);
		classtimebegin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker timerPicker,
							int hourOfDay, int minute) {

						String strm = "";
						String strh = "";
						if (minute < 10) {
							strm = "0" + minute;
						} else {
							strm = "" + minute;
						}
						if (hourOfDay < 10) {
							strh = "0" + hourOfDay;
						} else {
							strh = "" + hourOfDay;
						}
						classtimebegin.setText(strh + "时" + strm + "分");
						str1 = strh + ":" + strm;
						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
						try {
							date01 = sdf.parse(str1);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						calendar01.setTime(date01);
					}
				};

				Calendar calendar = Calendar.getInstance();
				TimePickerDialog timeDialog = new TimePickerDialog(
						getContext(), timeListener, calendar
								.get(Calendar.HOUR_OF_DAY), calendar
								.get(Calendar.MINUTE), true);// 是否为二十四制
				timeDialog.show();
			}
		});

		classtimeend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker timerPicker,
							int hourOfDay, int minute) {

						String strm = "";
						String strh = "";

						if (minute < 10) {
							strm = "0" + minute;
						} else {
							strm = "" + minute;
						}
						if (hourOfDay < 10) {
							strh = "0" + hourOfDay;
						} else {
							strh = "" + hourOfDay;
						}

						classtimeend.setText(strh + "时" + strm + "分");
						str2 = strh + ":" + strm;
						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
						try {
							date02 = sdf.parse(str2);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						calendar02.setTime(date02);
					}
				};

				Calendar calendar = Calendar.getInstance();
				TimePickerDialog timeDialog = new TimePickerDialog(
						getContext(), timeListener, calendar
								.get(Calendar.HOUR_OF_DAY), calendar
								.get(Calendar.MINUTE), true);// 是否为二十四制
				timeDialog.show();
			}
		});
	}

}
