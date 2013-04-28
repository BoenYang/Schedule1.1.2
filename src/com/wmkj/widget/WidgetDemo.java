package com.wmkj.widget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.wmkj.activity.MainInterface;
import com.wmkj.activity.R;
import com.wmkj.data.Course;
import com.wmkj.utils.SQLiteManager;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;

public class WidgetDemo extends AppWidgetProvider {

	private Course coursetable[][];
	private SQLiteManager sqLiteManager;
	private int week;
	private int classnum;
	private List<String> list;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new MyTime(context, appWidgetManager), 1,
				60000);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	private class MyTime extends TimerTask {
		RemoteViews Class;
		RemoteViews Time;
		RemoteViews Place;
		AppWidgetManager appWidgetManager;
		ComponentName thisWidget;
		Context context;

		public MyTime(Context context, AppWidgetManager appWidgetManager) {

			this.appWidgetManager = appWidgetManager;
			Class = new RemoteViews(context.getPackageName(),
					R.layout.widgetmain);
			Time = new RemoteViews(context.getPackageName(),
					R.layout.widgetmain);
			Place = new RemoteViews(context.getPackageName(),
					R.layout.widgetmain);
			thisWidget = new ComponentName(context, WidgetDemo.class);
			this.context = context;
		}

		public void run() {

			System.out.println("widget fresh");

			sqLiteManager = new SQLiteManager(context);
			list = sqLiteManager.getTimeTable();
			if (list.size() == 0) {
				return;
			}

			// get course info in the week
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			String time = sqLiteManager.rawFirstday();
			week = MainInterface.week(time);

			List<Course> courses = sqLiteManager.rawQueryCourses(week);

			for (int i = 0; i < courses.size(); i++) {
				System.out.println(courses.get(i));
			}

			classnum = sqLiteManager.rawClassNum();

			int weeks = calendar.get(Calendar.DAY_OF_WEEK);
			if (weeks == 0) {
				weeks = 1;
			} else {
				weeks = weeks - 1;
			}

			if (courses.size() != 0) {
				int num = returnClass();
				setTimeTable(courses);
				System.out.println(num/2);
				int next = nextClass(num, weeks);
				System.out.println(next);
				if (next == 0) {
					Class.setTextViewText(R.id.Class, "今天没课了哦！！");
					appWidgetManager.updateAppWidget(thisWidget, Class);
					Time.setTextViewText(R.id.Time, "");
					appWidgetManager.updateAppWidget(thisWidget, Time);
					Place.setTextViewText(R.id.Place, "");
					appWidgetManager.updateAppWidget(thisWidget, Place);
					return;
				}

				Class.setTextViewText(R.id.Class,
						coursetable[next-1][weeks - 1].courseName);
				appWidgetManager.updateAppWidget(thisWidget, Class);
				Time.setTextViewText(R.id.Time, list.get(next - 1));
				appWidgetManager.updateAppWidget(thisWidget, Time);
				Place.setTextViewText(R.id.Place,
						coursetable[next-1][weeks - 1].coursePlace);
				appWidgetManager.updateAppWidget(thisWidget, Place);

			} else {
				Class.setTextViewText(R.id.Class, "没有今天的课程数据！");
				appWidgetManager.updateAppWidget(thisWidget, Class);
			}
		}
	}

	/**
	 * 需要优化
	 * @return
	 */
	public int returnClass() {
		
		Calendar start = Calendar.getInstance();
		Calendar _start = Calendar.getInstance();
		Calendar startnext = Calendar.getInstance();
		Calendar _startnext = Calendar.getInstance();
		Calendar _end = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		Date date1 = null, date2 = null, datenext = null;

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String str;
		for (int i = 0; i < list.size(); i++) {
			str = list.get(i);
			String strs[] = str.split("~");
			try {
				date1 = sdf.parse(strs[0]);
				date2 = sdf.parse(strs[1]);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			start.setTimeInMillis(System.currentTimeMillis());
			end.setTimeInMillis(System.currentTimeMillis());
			_start.setTime(date1);
			_end.setTime(date2);
			start.set(Calendar.HOUR_OF_DAY, _start.get(Calendar.HOUR_OF_DAY));
			start.set(Calendar.MINUTE, _start.get(Calendar.MINUTE));
			end.set(Calendar.HOUR_OF_DAY, _end.get(Calendar.HOUR_OF_DAY));
			end.set(Calendar.MINUTE, _end.get(Calendar.MINUTE));

			if (i != list.size() - 1) {
				str = list.get(i + 1);
				strs = str.split("~");
				try {
					datenext = sdf.parse(strs[0]);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				startnext.setTimeInMillis(System.currentTimeMillis());
				_startnext.setTime(datenext);
				startnext.set(Calendar.HOUR_OF_DAY,
						_startnext.get(Calendar.HOUR_OF_DAY));
				startnext.set(Calendar.MINUTE, _startnext.get(Calendar.MINUTE));
			}

			if (System.currentTimeMillis() >= start.getTimeInMillis()
					&& System.currentTimeMillis() < end.getTimeInMillis()) {
				return (i + 1) * 2;
			} else {
				if (i != list.size() - 1
						&& System.currentTimeMillis() >= end.getTimeInMillis()
						&& System.currentTimeMillis() < startnext
								.getTimeInMillis()) {
					return (i + 1) * 2 + 1;
				} else {
					if (i == list.size() - 1
							&& System.currentTimeMillis() >= end
									.getTimeInMillis()) {
						return (i + 1) * 2 + 1;
					} else {
						if (i == 0
								&& System.currentTimeMillis() < start
										.getTimeInMillis()) {
							return (i + 1);
						}
					}
				}
			}
		}
		return 0;
	}

	private void setTimeTable(List<Course> courses) {
		coursetable = new Course[classnum][7];
		Course course;
		for (int i = 0; i < courses.size(); i++) {
			course = courses.get(i);
			String str = course.courseTime;
			if (str == "") {
				return;
			}
			String strs[] = str.split("-");
			int r, c;
			c = Integer.parseInt(strs[0]) - 1;
			r = Integer.parseInt(strs[1]) - 1;
			coursetable[r][c] = course;
		}
	}

	/**
	 * 
	 * 
	 * @param now 当前的课程节次
	 * @param week 当前星期
	 * @return
	 */
	private int nextClass(int now, int week) {
		if (now % 2 != 0) {
			boolean mark = false;
			int c;
			if (now == 1) {
				c = 0;
			} else {
				c = (now + 1) / 2 + 1;
			}
			int i;
			for (i = c; i < classnum; i++) {
				if (coursetable[i-1][week - 1] != null) {
					mark = true;
					break;
				}
			}
			if (mark) {
				return i;
			} else {
				return 0;
			}
		} else {
			boolean mark = false;
			int c = now / 2 + 1;
			int i;
			for (i = c; i < classnum; i++) {
				if (coursetable[i-1][week - 1] != null) {
					mark = true;
					break;
				}
			}
			if (mark) {
				return i;
			} else {
				return 0;
			}
		}
	}
}
