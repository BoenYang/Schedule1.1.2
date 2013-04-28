package com.wmkj.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.wmkj.data.Course;
import com.wmkj.view.CornerListView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ClassManagerActivity extends Activity {

	private CornerListView listView;
	private Set<String> classnameset = new HashSet<String>();
	private List<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
	private Button backBtn, saveBtn;
	private List<String> removeList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.class_manager);

		// 更改背景图片
		LinearLayout llayout = (LinearLayout) findViewById(R.id.cm);
		SetBackgroundImage.setBackGround(ClassManagerActivity.this, llayout);

		backBtn = (Button) findViewById(R.id.class_manager_back);
		saveBtn = (Button) findViewById(R.id.cmsave);
		listView = (CornerListView) findViewById(R.id.classlist);
		List<Course> courses = MainInterface.sqLiteManager.rawQueryAll();
		int courseSize = courses.size();
		for (int i = 0; i < courseSize; i++) {
			classnameset.add(courses.get(i).courseName);
		}

		String str[] = new String[classnameset.size()];
		classnameset.toArray(str);
		for (int i = 0; i < str.length; i++) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("classname", str[i]);
			items.add(item);
		}

		Adapter simpleAdapter = new Adapter(this);
		listView.setAdapter(simpleAdapter);

		backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ClassManagerActivity.this.finish();
			}
		});

		saveBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (removeList.size() == 0) {
					Toast.makeText(ClassManagerActivity.this, "没有数据更新",
							Toast.LENGTH_SHORT).show();
				}
				for (int i = 0; i < removeList.size(); i++) {
					MainInterface.sqLiteManager.deleteCourseByName(removeList
							.get(i));
					Toast.makeText(ClassManagerActivity.this, "数据更新成功",
							Toast.LENGTH_SHORT).show();
				}
				ClassManagerActivity.this.finish();
			}
		});

	}

	class Adapter extends BaseAdapter {

		private LayoutInflater inflater;
		@SuppressWarnings("unused")
		private Context context;
		private ViewHolder holder;

		private class ViewHolder {
			TextView textView;
			Button edit;
			Button delete;
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
				convertView = inflater.inflate(R.layout.class_manager_list,
						null);
				holder = new ViewHolder();
				holder.textView = (TextView) convertView
						.findViewById(R.id.classname);
				holder.edit = (Button) convertView.findViewById(R.id.edit);
				holder.delete = (Button) convertView.findViewById(R.id.delete);
				convertView.setTag(holder);
			}
			HashMap<String, Object> appInfo = items.get(position);
			if (appInfo != null) {
				String text = (String) appInfo.get("classname");
				holder.textView.setText(text);
				holder.edit.setOnClickListener(new BtnListener(position));
				holder.delete.setOnClickListener(new BtnListener(position));
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
				if (vid == R.id.edit) {
					Intent intent = new Intent();
					intent.setClass(ClassManagerActivity.this,
							EditClassActivity.class);
					intent.putExtra("classname",
							items.get(position).get("classname").toString());
					ClassManagerActivity.this.startActivity(intent);
				}
				if (vid == R.id.delete) {
					removeList.add(items.get(position).get("classname")
							.toString());
					removeItem(position);
				}

			}
		}
	}

}
