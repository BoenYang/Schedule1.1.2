package com.wmkj.activity;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wmkj.utils.FixedSpeedScroller;
import com.wmkj.utils.SQLiteManager;
import com.wmkj.utils.WeatherUtil;
import com.wmkj.view.Table;

public class MainInterface extends Activity implements Runnable {

	public static SQLiteManager sqLiteManager;
	public static int weeknum;
	public static int classnum;
	public WeatherUtil weatherUtil;
	private ConnectivityManager connectivityManager;
	public double latitude;
	public double longitude;
	public Location location;
	private static final int UPDATETEXTVIEW = 1;
	private static final int BAD_REQUEST = -1;
	private static final int BAD_LOCATION = 2;
	public LocationManager locationManager;
	private String weatherStr, firstday;
	private Button more, classmanager, addclass, classreminder;
	private TextView displayWeek;
	private Button tocurrentweek;
	private TextView weatherText;
	private Button freshBtn;
	private ProgressBar pb;
	public ViewPager viewPager;
	private GestureDetector gestureDetector;
	public List<View> views = new ArrayList<View>();
	private int currentItem;
	private FixedSpeedScroller scroller;
	private Button time;
	private String updateTime;
	public static DisplayMetrics dm = new DisplayMetrics();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_main);
		
		LinearLayout Layout = (LinearLayout)findViewById(R.id.main_xml);
		SetBackgroundImage.setBackGround(MainInterface.this, Layout);
		
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		sqLiteManager = new SQLiteManager(this);
		if (savedInstanceState != null) {
			weatherStr = savedInstanceState.getString("weather");
		}

		connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		weatherUtil = new WeatherUtil();
		gestureDetector = new GestureDetector(new GuideViewTouch());

		tocurrentweek = (Button) findViewById(R.id.tocurrentweek);
		more = (Button) findViewById(R.id.more);
		classmanager = (Button) findViewById(R.id.class_adder);
		addclass = (Button) findViewById(R.id.class_manager);
		classreminder = (Button) findViewById(R.id.class_reminder);
		displayWeek = (TextView) findViewById(R.id.displayweek);
		weatherText = (TextView) findViewById(R.id.weather);
		freshBtn = (Button) findViewById(R.id.refresh);
		pb = (ProgressBar) findViewById(R.id.progress);
		time = (Button) findViewById(R.id.updatetime);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			firstday = bundle.getString("firstday");
			weeknum = bundle.getInt("weeknum");
			classnum = bundle.getInt("classnum");
			if (weeknum == 0) {
				weeknum = 20;
			}
			if (classnum == 0) {
				classnum = 4;
			}
			MainInterface.sqLiteManager
					.addtimedata(firstday, classnum, weeknum);
		} else {
			weeknum = MainInterface.sqLiteManager.rawWeekNum();
			classnum = MainInterface.sqLiteManager.rawClassNum();
			firstday = MainInterface.sqLiteManager.rawFirstday();
		}

		displayWeek.setText("第" + week(firstday) + "周");
		more.setOnClickListener(new BtnListener());
		classmanager.setOnClickListener(new BtnListener());
		addclass.setOnClickListener(new BtnListener());
		classreminder.setOnClickListener(new BtnListener());
		tocurrentweek.setOnClickListener(new BtnListener());
		freshBtn.setOnClickListener(new BtnListener());
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if(locationManager != null)
		{
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 1000, 10, listener);
			location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		SharedPreferences sharedPreferences = this.getSharedPreferences(
				"count", MODE_WORLD_READABLE);
		weatherStr = sharedPreferences.getString("weather", "null");
		updateTime = sharedPreferences.getString("time", "null");
		
		if (weatherStr.equals("null") || updateTime.equals("null")) {
			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();
			if (networkInfo == null || !networkInfo.isConnected()) {
			} else {
				Thread thread = new Thread(MainInterface.this);
				thread.start();
				freshBtn.setVisibility(View.GONE);
				pb.setVisibility(View.VISIBLE);
			}
		} else {
			weatherText.setText(weatherStr);
			time.setText(updateTime);
		}

		//add view 
		for (int i = 0; i < weeknum; i++) {
			LinearLayout linearLayout = new LinearLayout(this);
			linearLayout.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			linearLayout.setId(i);
			linearLayout.setTag("linear" + i);

			Table t = new Table(this);
			t.setTag("table" + i);
			Table.setRowAndColum(classnum, 7);
			t.addForm();
			t.setWeek(i + 1);
			LayoutParams layoutParams = new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			linearLayout.addView(t, layoutParams);
			views.add(linearLayout);
		}

		viewPager = (ViewPager) findViewById(R.id.tablepage);
		viewPager.setAdapter(new MyAdapter());
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
		viewPager.setCurrentItem(week(firstday) - 1);

		try {
			Field field = ViewPager.class.getDeclaredField("mScroller");
			field.setAccessible(true);
			scroller = new FixedSpeedScroller(viewPager.getContext(),
					new AccelerateInterpolator());
			scroller.setmDuration(200);
			field.set(viewPager, scroller);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}
	
	LocationListener listener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onLocationChanged(Location location) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		}
	};


	
	class BtnListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.more:
				SelectDialog selectDialog = new SelectDialog(
						MainInterface.this, R.style.dialog);
				Window window = selectDialog.getWindow();
				window.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
				android.view.WindowManager.LayoutParams layoutParams = window
						.getAttributes();

				layoutParams.x = (int) (3 * dm.density);
				layoutParams.y = (int) (65.0f * dm.density)
						- (int) (5 * dm.density);
				window.setAttributes(layoutParams);
				selectDialog.show();
				break;
			case R.id.class_adder:
				Intent intent = new Intent();
				intent.setClass(MainInterface.this, AddClassActivity.class);
				startActivity(intent);
				break;
			case R.id.class_manager:
				Intent intent01 = new Intent();
				intent01.setClass(MainInterface.this,
						ClassManagerActivity.class);
				startActivity(intent01);
				break;
			case R.id.tocurrentweek:
				int now_week = week(firstday);
				viewPager.setCurrentItem(now_week - 1);
				displayWeek.setText("第" + now_week + "周");
				break;
			case R.id.class_reminder:
				Intent intent00 = new Intent();
				intent00.setClass(MainInterface.this, ChangeBgActivity.class);
				startActivity(intent00);
				break;
			case R.id.refresh:
				NetworkInfo networkInfo = connectivityManager
						.getActiveNetworkInfo();
				if (networkInfo == null || !networkInfo.isConnected()) {
					Toast.makeText(MainInterface.this, "请打开网络连接",
							Toast.LENGTH_SHORT).show();
				} else {
					Thread thread = new Thread(MainInterface.this);
					thread.start();
					freshBtn.setVisibility(View.GONE);
					pb.setVisibility(View.VISIBLE);
				}
				break;
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click();// 调用双击退出函数
		}
		return false;
	}

	private static Boolean isExit = false;

	//double click exit
	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {

				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			finish();
		}
	}

	//thread use for update weather information
	@Override
	public void run() {

		Message msg = new Message();
		if(location == null)
		{
			msg.what = BAD_LOCATION;
		}else
		{
			weatherStr = weatherUtil.getWeatherString(location.getLatitude(),
					location.getLongitude());
		}
		

		if (weatherStr == "error" || weatherStr == null) {
			msg.what = BAD_REQUEST;
		} else {
			SharedPreferences sharedPreferences = this.getSharedPreferences(
					"count", Context.MODE_WORLD_READABLE);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			updateTime = (calendar.get(Calendar.MONTH) + 1) + "-"
					+ calendar.get(Calendar.DAY_OF_MONTH) + "  "
					+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
					+ calendar.get(Calendar.MINUTE);
			Editor editor = sharedPreferences.edit();
			editor.putString("weather", weatherStr);
			editor.putString("time", updateTime);
			editor.commit();
			msg.what = UPDATETEXTVIEW;
		}
		handler.sendMessage(msg);
	}


	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATETEXTVIEW:
				weatherText.setText(weatherStr);
				time.setText(updateTime);
				pb.setVisibility(View.GONE);
				freshBtn.setVisibility(View.VISIBLE);
				break;
			case BAD_REQUEST:
				Toast.makeText(MainInterface.this, "获取天气数据失败",
						Toast.LENGTH_SHORT).show();
				break;
			case BAD_LOCATION:
				Toast.makeText(MainInterface.this, "获取位置数据失败",
						Toast.LENGTH_SHORT).show();
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("weather", weatherStr);
		super.onSaveInstanceState(outState);
	}

	private class GuideViewTouch extends SimpleOnGestureListener {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (currentItem == 0) {
				Toast.makeText(MainInterface.this, "已经到第一周", Toast.LENGTH_SHORT)
						.show();
			}
			if (currentItem == (weeknum - 1)) {
				Toast.makeText(MainInterface.this, "已经到最后一周",
						Toast.LENGTH_SHORT).show();
			}
			return false;
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event)) {
			event.setAction(MotionEvent.ACTION_CANCEL);
		}
		return super.dispatchTouchEvent(event);
	}

	public static int week(String date) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int mouth = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		String now = year + "-" + mouth + "-" + day;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date firstDate = null;
		Date nowDate = null;
		try {
			firstDate = dateFormat.parse(date);
			nowDate = dateFormat.parse(now);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long days = (nowDate.getTime() - firstDate.getTime())
				/ (24 * 60 * 60 * 1000) + 1;

		if (days == 0) {
			return 1;
		}

		if (days % 7 == 0)
			return (int) days / 7;
		else
			return (int) (days / 7) + 1;
	}

	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(views.get(arg1));
			return views.get(arg1);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((LinearLayout) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public void finishUpdate(View arg0) {
		}
	}

	private class MyPageChangeListener implements OnPageChangeListener {

		public void onPageSelected(int position) {
			currentItem = position;
			displayWeek.setText("第" + (position + 1) + "周");
		}

		public void onPageScrollStateChanged(int arg0) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}

	@Override
	protected void onDestroy() {
		viewPager.removeAllViews();
		views.removeAll(views);
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		LinearLayout llayout = (LinearLayout) findViewById(R.id.main_xml);
		SetBackgroundImage.setBackGround(MainInterface.this, llayout);
		Table table = (Table) viewPager.findViewWithTag("table" + currentItem);
		table.invalidate();
		super.onRestart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

}
