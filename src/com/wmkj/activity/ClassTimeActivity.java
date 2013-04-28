package com.wmkj.activity;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @Name: com.wmkj.activity.SplashActivity ClassTimeActivity.java
 * @Author: nylqd
 * @Date: 2012-11-3
 * @Description:
 * 
 */
public class ClassTimeActivity extends Activity {

	private List<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
	private ListView listView;
	private Adapter adapter;
	private List<String> startT = new ArrayList<String>();
	private List<String> endT = new ArrayList<String>();
	private List<String> times;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.inputtime);

		// 更改背景图片
		LinearLayout llayout = (LinearLayout) findViewById(R.id.llayout);
		SetBackgroundImage.setBackGround(ClassTimeActivity.this, llayout);

		times = MainInterface.sqLiteManager.getTimeTable();
		Button title_saveBut = (Button) findViewById(R.id.title_saveBut);
		Button title_backBut = (Button) findViewById(R.id.title_backBut);

		title_saveBut.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// 存入数据库
				// 后续更新
				String strs[];
				if (times.size() == MainInterface.classnum) {

					boolean mark = true;
					int timeSize = times.size();
					for (int i = 0; i < timeSize; i++) {
						strs = times.get(i).split("~");
						if (!MainInterface.sqLiteManager.updateTimeTable(
								strs[0], strs[1], i + 1)) {
							mark = false;
						}
					}
					if (mark) {
						Toast.makeText(ClassTimeActivity.this, "时间保存成功",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(ClassTimeActivity.this, "时间保存失败",
								Toast.LENGTH_SHORT).show();
					}

				}

				// 第一次输入
				if (times.size() == 0) {
					// 判断是否输入完整
					if (startT.size() != MainInterface.classnum) {
						Toast.makeText(ClassTimeActivity.this, "请把时间输入完整",
								Toast.LENGTH_SHORT).show();
						return;
					}
					boolean mark = true;
					int startTSize = startT.size();
					for (int i = 0; i < startTSize; i++) {
						if (!MainInterface.sqLiteManager.addTimeTable(
								startT.get(i), endT.get(i), (i + 1))) {
							mark = false;
						}
					}
					if (mark) {
						Toast.makeText(ClassTimeActivity.this, "时间保存成功",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(ClassTimeActivity.this, "时间保存失败",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

		title_backBut.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ClassTimeActivity.this.finish();
			}
		});

		listView = (ListView) findViewById(R.id.timelist);

		if (!items.isEmpty()) {
			items.removeAll(items);
		}

		if (times.size() != 0) {
			for (int i = 0; i < MainInterface.classnum; i++) {
				String strs[] = new String[2];
				strs = times.get(i).split("~");
				HashMap<String, Object> item = new HashMap<String, Object>();
				item.put("id", (i + 1));
				item.put("time", times.get(i));
				startT.add(strs[0]);
				endT.add(strs[1]);
				items.add(item);
			}
		} else {
			for (int i = 0; i < MainInterface.classnum; i++) {
				HashMap<String, Object> item = new HashMap<String, Object>();
				item.put("id", (i + 1));
				item.put("time", "");
				items.add(item);
			}
		}
		adapter = new Adapter(this);
		listView.setAdapter(adapter);
	}

	class Adapter extends BaseAdapter {

		private LayoutInflater inflater;
		private Context context;
		private ViewHolder holder;

		private class ViewHolder {
			TextView textView;
			TextView button;
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
				convertView = inflater.inflate(R.layout.time_info, null);
				holder = new ViewHolder();
				holder.textView = (TextView) convertView
						.findViewById(R.id.timeno);
				holder.button = (TextView) convertView
						.findViewById(R.id.timetext);
				convertView.setTag(holder);
			}
			HashMap<String, Object> appInfo = items.get(position);
			if (appInfo != null) {
				String text = "第" + appInfo.get("id") + "节课:";
				holder.textView.setText(text);
				String text1 = (String) appInfo.get("time");
				holder.button.setText(text1);
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

				// 第一次输入时间时需要判断
				if (times.size() == 0) {
					// 按顺序输入
					if (position != startT.size()) {
						Toast.makeText(context, "请按顺序输入", Toast.LENGTH_SHORT)
								.show();
						return;
					}
				}
				final TimeDialog timeDialog = new TimeDialog(
						ClassTimeActivity.this);
				timeDialog.show();

				Button time_dialog_ok = (Button) timeDialog
						.findViewById(R.id.time_dialog_ok);
				Button time_dialog_cancel = (Button) timeDialog
						.findViewById(R.id.time_dialog_cancel);

				time_dialog_cancel
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								timeDialog.cancel();
							}
						});

				time_dialog_ok.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
				
						if (times.size() == MainInterface.classnum) {
							if (timeDialog.str1 == null) {
								Toast.makeText(context, "请输入上课时间",
										Toast.LENGTH_SHORT).show();
								return;
							} else if (timeDialog.str2 == null) {
								Toast.makeText(context, "请输入下课时间",
										Toast.LENGTH_SHORT).show();
								return;
							}
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
									"hh:mm");
							try {
								Date end = simpleDateFormat
										.parse(timeDialog.str2);
								Date start = simpleDateFormat
										.parse(timeDialog.str1);
								Date preEnd = null;
								Date nextStart = null;
								if (end.after(start)) {
									if (position == 0) {
										nextStart = simpleDateFormat
												.parse(startT.get(position + 1));
										if (end.after(nextStart)) {
											Toast.makeText(context, "输入时间有误！",
													Toast.LENGTH_SHORT).show();
											return;
										}
									} else if (position == MainInterface.classnum - 1) {
										preEnd = simpleDateFormat.parse(endT
												.get(position - 1));
										if (preEnd.after(start)) {

											Toast.makeText(context, "输入时间有误！",
													Toast.LENGTH_SHORT).show();
											return;
										}
									}else
									{
										preEnd = simpleDateFormat.parse(endT
												.get(position - 1));
										nextStart = simpleDateFormat.parse(startT
												.get(position + 1));
										if (nextStart.before(end)
												|| preEnd.after(start)) {
											Toast.makeText(context, "输入时间有误！",
													Toast.LENGTH_SHORT).show();
											return;
										}
									}
									startT.set(position,
											timeDialog.str1);
									endT.set(position, timeDialog.str2);
									times.set(position, timeDialog.str1+ "~" + timeDialog.str2);
									HashMap<String, Object> item  = new HashMap<String, Object>();
									item.put("id", (position + 1));
									item.put("time", times.get(position));
									items.set(position,item);
									
								}else
								{
									Toast.makeText(context, "输入时间有误！",
											Toast.LENGTH_SHORT).show();
									return;
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}
						} else {
							if(times.size() != position+1)
							{
								Toast.makeText(context, "请按顺序输入时间！",
										Toast.LENGTH_SHORT).show();
								return ;
							}else
							{
								times.add(timeDialog.str1 + "~" + timeDialog.str2);
								startT.add(timeDialog.str1);
								endT.add(timeDialog.str2);
								HashMap<String, Object> item  = new HashMap<String, Object>();
								item.put("id", (position + 1));
								item.put("time", times.get(position));
								items.add(item);
							}
						}
						
						timeDialog.cancel();
						adapter.notifyDataSetChanged();
					}
				});
			}
		}
	}
}
