package com.wmkj.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.wmkj.data.Course;
import com.wmkj.view.CornerListView;
import com.wmkj.view.LineEditText;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EditClassActivity extends Activity {

	private List<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
	private CornerListView listView;
	private Dialog add_infoDialog;
	private Button dialog_button_ok, dialog_button_cancel, class_edit_back;
	private LineEditText class_place, start_week, end_week, every_week,
			week_date, class_num;
	private Course course;
	private int editposition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edit_service);

		// 更改背景图片
		LinearLayout llayout = (LinearLayout) findViewById(R.id.edit);
		SetBackgroundImage.setBackGround(EditClassActivity.this, llayout);

		add_infoDialog = new Dialog(this, R.style.dialog);
		add_infoDialog.getWindow().setContentView(R.layout.add_info);

		Intent intent = getIntent();
		String classname = intent.getStringExtra("classname");

		listView = (CornerListView) findViewById(R.id.servicelist);

		class_place = (LineEditText) add_infoDialog
				.findViewById(R.id.class_placeText);
		start_week = (LineEditText) add_infoDialog
				.findViewById(R.id.start_weekText);
		end_week = (LineEditText) add_infoDialog
				.findViewById(R.id.end_weekText);

		every_week = (LineEditText) add_infoDialog
				.findViewById(R.id.every_weekText);
		week_date = (LineEditText) add_infoDialog
				.findViewById(R.id.week_dateText);
		class_num = (LineEditText) add_infoDialog
				.findViewById(R.id.class_numText);

		List<Course> courses = MainInterface.sqLiteManager
				.rawQueryAllCourses(classname);
		

		int courseSzie = courses.size();
		for (int i = 0; i < courseSzie; i++) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("classinfo", courses.get(i).toString());
			item.put("courseobj", courses.get(i));
			items.add(item);
		}
		courses.clear();
		courses = null;

		final Adapter simpleAdapter = new Adapter(this);
		listView.setAdapter(simpleAdapter);
		listView.setOnItemLongClickListener(new ItemOnClickListener());

		dialog_button_ok = (Button) add_infoDialog
				.findViewById(R.id.dialog_button_ok);
		dialog_button_cancel = (Button) add_infoDialog
				.findViewById(R.id.dialog_button_cancel);
		class_edit_back = (Button) findViewById(R.id.class_edit_back);

		class_edit_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				EditClassActivity.this.finish();
			}
		});

		dialog_button_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (class_place.getText().length() == 0) {
					Toast.makeText(EditClassActivity.this, "请输入上课地点",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (week_date.getText().length() == 0) {
					Toast.makeText(EditClassActivity.this, "请输入上课时间",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (class_num.getText().length() == 0) {
					Toast.makeText(EditClassActivity.this, "请输入上课时间",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (end_week.getText().length() == 0) {
					Toast.makeText(EditClassActivity.this, "请输入上课时间",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (start_week.getText().length() == 0) {
					Toast.makeText(EditClassActivity.this, "请输入上课时间",
							Toast.LENGTH_SHORT).show();
					return;
				}

				course.coursePlace = class_place.getText().toString();
				course.courseTime = week_date.getText().toString() + "-"
						+ class_num.getText().toString();
				course.coursePlan = "";
				for (int i = 0; i < MainInterface.weeknum; i++) {
					course.coursePlan += "" + "0";
				}

				int endweek, startweek, internal;
				startweek = Integer.parseInt(start_week.getText().toString());
				endweek = Integer.parseInt(end_week.getText().toString());
				internal = Integer.parseInt(every_week.getText().toString());
				course.updateCoursePlan(startweek, endweek, internal);

				if (MainInterface.sqLiteManager.updateCourseInfo(course)) {
					add_infoDialog.cancel();
					HashMap<String, Object> updateItem = new HashMap<String, Object>();
					updateItem.put("classinfo", course.toString());
					updateItem.put("courseobj", course);
					items.set(editposition, updateItem);
					Toast.makeText(EditClassActivity.this, "更新成功",
							Toast.LENGTH_SHORT).show();
					simpleAdapter.notifyDataSetChanged();
				} else {
					add_infoDialog.cancel();
					Toast.makeText(EditClassActivity.this, "更新失败",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		dialog_button_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				add_infoDialog.cancel();
			}
		});
	}

	class ItemOnClickListener implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			add_infoDialog.show();
			HashMap<String, Object> item = items.get(position);
			course = (Course) item.get("courseobj");
			class_place.setText(course.coursePlace);
			editposition = position;
			return false;
		}
	}

	@SuppressWarnings("unused")
	private static boolean isConflict(Course course) {

		List<Course> courses = MainInterface.sqLiteManager.rawQueryAll();
		Course course1;
		int size = courses.size();
		for (int i = 0; i < size; i++) {
			course1 = courses.get(i);
			for (int j = 0; j < course.coursePlace.length(); j++) {
				if (course.coursePlan.charAt(j) == '1') {
					if (course.coursePlan.charAt(j) == course1.coursePlan
							.charAt(j)) {
						if (course.courseTime.equals(course1.courseTime)) {
							return true;
						}
					}
				}
			}
		}
		courses.clear();
		return false;
	}

	class Adapter extends BaseAdapter {

		private LayoutInflater inflater;
		@SuppressWarnings("unused")
		private Context context;
		private ViewHolder holder;

		private class ViewHolder {
			TextView textView;
		}

		public Adapter(Context context) {
			this.context = context;
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void removeItem(int position) {
			items.remove(position);
			this.notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView != null) {
				holder = (ViewHolder) convertView.getTag();
			} else {
				convertView = inflater.inflate(R.layout.classinfo, null);
				holder = new ViewHolder();
				holder.textView = (TextView) convertView
						.findViewById(R.id.classinfo1);
				convertView.setTag(holder);
			}
			HashMap<String, Object> appInfo = items.get(position);
			if (appInfo != null) {
				String text = (String) appInfo.get("classinfo");
				holder.textView.setText(text);
			}
			return convertView;
		}

		class TextViewListener implements View.OnClickListener {
			private int position;

			public TextViewListener(int pos) {
				position = pos;
			}

			@Override
			public void onClick(View v) {
				Toast.makeText(EditClassActivity.this, "" + position,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

}
