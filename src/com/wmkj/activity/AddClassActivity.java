package com.wmkj.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wmkj.data.Course;
import com.wmkj.view.CornerListView;
import com.wmkj.view.LineEditText;

public class AddClassActivity extends Activity {

	private EditText class_name;
	private EditText teacher_name;
	private LineEditText class_place, start_week, end_week, every_week,
			week_date, class_num;
	
	private Button title_backBut, title_saveBut;
	private Button add_infoBut, dialog_button_ok, dialog_button_cancel;
	
	private List<Course> courses = new ArrayList<Course>();
	private List<Course> temp = new ArrayList<Course>();
	
	private Dialog add_infoDialog;
	private CornerListView listView;
	private List<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
	private Adapter simpleAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_class_activity);

		LinearLayout llayout = (LinearLayout) findViewById(R.id.addclass);
		SetBackgroundImage.setBackGround(AddClassActivity.this, llayout);

		add_infoDialog = new Dialog(this, R.style.dialog);
		add_infoDialog.getWindow().setContentView(R.layout.add_info);
		class_place = (LineEditText) add_infoDialog
				.findViewById(R.id.class_placeText);
		class_place.setLineColor(Color.BLACK);
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
		dialog_button_ok = (Button) add_infoDialog
				.findViewById(R.id.dialog_button_ok);
		dialog_button_cancel = (Button) add_infoDialog
				.findViewById(R.id.dialog_button_cancel);

		class_name = (EditText) findViewById(R.id.class_nameText);
		teacher_name = (EditText) findViewById(R.id.teacher_nameText);
		title_saveBut = (Button) findViewById(R.id.title_saveBut);
		title_backBut = (Button) findViewById(R.id.title_backBut);
		add_infoBut = (Button) findViewById(R.id.add_infoBUt);

		listView = (CornerListView) findViewById(R.id.classinfolist);
		simpleAdapter = new Adapter(this);
		listView.setAdapter(simpleAdapter);

		dialog_button_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (class_name.getText().toString() == "") {
					Toast.makeText(AddClassActivity.this, "请输入课程名称",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (teacher_name.getText().toString() == "") {
					Toast.makeText(AddClassActivity.this, "请输入教师",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (class_place.getText().length() == 0) {
					Toast.makeText(AddClassActivity.this, "请输入上课地点",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (week_date.getText().length() == 0) {
					Toast.makeText(AddClassActivity.this, "请输入上课时间",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (class_num.getText().length() == 0) {
					Toast.makeText(AddClassActivity.this, "请输入上课时间",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (end_week.getText().length() == 0) {
					Toast.makeText(AddClassActivity.this, "请输入上课时间",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (start_week.getText().length() == 0) {
					Toast.makeText(AddClassActivity.this, "请输入上课时间",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (start_week.getText().length() != 0
						&& Integer.parseInt(start_week.getText().toString()) <= 0) {
					Toast.makeText(AddClassActivity.this, "起始周次从1开始",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (end_week.getText().length() != 0
						&& (Integer.parseInt(end_week.getText().toString()) > MainInterface.weeknum || Integer
								.parseInt(start_week.getText().toString()) > MainInterface.weeknum)) {
					Toast.makeText(AddClassActivity.this, "周次大于总周数",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (Integer.parseInt(start_week.getText().toString()) >= Integer
						.parseInt(end_week.getText().toString())) {
					Toast.makeText(AddClassActivity.this, "起始周次应小于截至周次",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (class_num.getText().length() != 0
						&& Integer.parseInt(class_num.getText().toString()) > MainInterface.classnum) {
					Toast.makeText(AddClassActivity.this, "节次大于总节数",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (week_date.getText().length() != 0
						&& Integer.parseInt(week_date.getText().toString()) > 7) {
					Toast.makeText(AddClassActivity.this, "周次大于7",
							Toast.LENGTH_SHORT).show();
					return;
				}

				Course course = new Course(0);
				course.courseName = class_name.getText().toString();
				course.courseTeacher = teacher_name.getText().toString();
				course.coursePlace = class_place.getText().toString();
				course.courseTime = week_date.getText().toString() + "-"
						+ class_num.getText().toString();
				int endweek, startweek, internal;
				startweek = Integer.parseInt(start_week.getText().toString());
				endweek = Integer.parseInt(end_week.getText().toString());
				internal = Integer.parseInt(every_week.getText().toString());
				
				for (int i = 0; i < MainInterface.weeknum; i++) {
					course.coursePlan += "" + "0";
				}
				
				course.updateCoursePlan(startweek, endweek, internal);
				courses.add(course);
				if (isConflict(course)) {
					courses.remove(courses.size() - 1);
					Toast.makeText(AddClassActivity.this, "课程存在冲突",
							Toast.LENGTH_LONG).show();
					return;
				} else {
					addDisplayView(course, startweek, endweek, internal);
				}
				add_infoDialog.cancel();
			}
		});

		dialog_button_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				add_infoDialog.cancel();
			}
		});

		add_infoBut.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (class_name.getText().length() == 0) {
					Toast.makeText(AddClassActivity.this, "请输入课程名称",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (teacher_name.getText().length() == 0) {
					Toast.makeText(AddClassActivity.this, "请输入授课老师",
							Toast.LENGTH_SHORT).show();
					return;
				}
				addinfo();
			}
		});

		/************************* 标题栏按钮事件 *************************/
		title_backBut.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AddClassActivity.this.finish();
			}
		});

		title_saveBut.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (courses.size() == 0) {
					Toast.makeText(AddClassActivity.this, "您还没有输入课程计划",
							Toast.LENGTH_SHORT).show();
					return;
				}

				int startid = MainInterface.sqLiteManager.rawQueryAllCourses(
						class_name.getText().toString()).size();

				int size = courses.size();
				for (int i = 0; i < size; i++) {
					courses.get(i).id = startid + 1 + i;
					MainInterface.sqLiteManager.add(courses.get(i));
				}
				courses.clear();
				temp.clear();
				AddClassActivity.this.finish();
			}
		});

	}

	private void addinfo() {
		add_infoDialog.show();
	}

	private void addDisplayView(Course course, int start, int end, int internal) {
		HashMap<String, Object> item = new HashMap<String, Object>();
		item.put("classinfo", course.coursePlace + start + "~" + end + "周"
				+ " 每" + internal + "周" + "上" + " 周" + course.courseTime + "节");
		items.add(item);
		simpleAdapter.notifyDataSetChanged();

	}

	class Adapter extends BaseAdapter {

		private LayoutInflater inflater;
		@SuppressWarnings("unused")
		private Context context;
		private ViewHolder holder;

		private class ViewHolder {
			TextView textView;
			Button button;
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
				convertView = inflater.inflate(R.layout.show_classlistview,
						null);
				holder = new ViewHolder();
				holder.textView = (TextView) convertView
						.findViewById(R.id.classinfo);
				holder.button = (Button) convertView.findViewById(R.id.delet);
				convertView.setTag(holder);
			}
			HashMap<String, Object> appInfo = items.get(position);
			if (appInfo != null) {
				String text = (String) appInfo.get("classinfo");
				holder.textView.setText(text);
				holder.button.setOnClickListener(new BtnListener(position));
			}
			return convertView;
		}

		class BtnListener implements View.OnClickListener {
			private int position;

			public BtnListener(int pos) {
				position = pos;
			}

			@Override
			public void onClick(View v) {
				int vid = v.getId();
				if (vid == holder.button.getId()) {
					removeItem(position);
					courses.remove(position);
				}
			}
		}
	}

	private boolean isConflict(Course course) {
		for (int i = 0; i < temp.size(); i++) {
			if (course.courseTime == temp.get(i).courseTime) {
				return true;
			}
		}

		List<Course> courses = MainInterface.sqLiteManager.rawQueryAll();
		Course course1;
		for (int i = 0; i < courses.size(); i++) {
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
		courses = null;
		return false;
		
	}

}
