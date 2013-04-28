package com.wmkj.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.wmkj.utils.FileUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class GuideViewActivity extends Activity {
	private ViewPager viewPager;
	private List<View> mImageViews; // 滑动的图片集合
	private int currentItem = 0; // 当前图片的索引号
	private GestureDetector gestureDetector; // 用户滑动
	/** 记录当前分页ID */
	private int flaggingWidth;// 互动翻页所需滚动的长度是当前屏幕宽度的1/3
	FileUtil fileUtil = new FileUtil();
	private String DATAfolder = "/databases/";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window window = getWindow();
		// 设置surfaceView是全屏显示的

		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题

		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		setContentView(R.layout.guide_activity);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		gestureDetector = new GestureDetector(new GuideViewTouch());
		flaggingWidth = dm.widthPixels / 3;

		mImageViews = new ArrayList<View>();

		// 初始化图片资源
		LayoutInflater viewInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// 0
		View convertView0 = viewInflater.inflate(R.layout.guide_item, null);
		LinearLayout linearLayout0 = (LinearLayout) convertView0
				.findViewById(R.id.guide_item);
		linearLayout0.setBackgroundResource(R.drawable.guide01);
		mImageViews.add(linearLayout0);

		// 1
		View convertView1 = viewInflater.inflate(R.layout.guide_item, null);
		LinearLayout linearLayout1 = (LinearLayout) convertView1
				.findViewById(R.id.guide_item);
		linearLayout1.setBackgroundResource(R.drawable.guide02);
		mImageViews.add(linearLayout1);

		// 2
		View convertView2 = viewInflater.inflate(R.layout.guide_item, null);
		LinearLayout linearLayout2 = (LinearLayout) convertView2
				.findViewById(R.id.guide_item);
		linearLayout2.setBackgroundResource(R.drawable.guide03);

		Button btn = (Button) convertView2.findViewById(R.id.start);
		btn.setVisibility(View.VISIBLE);
		btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				GoToMainActivity();
			}
		});
		
		mImageViews.add(linearLayout2);

		viewPager = (ViewPager) findViewById(R.id.guide_view);
		viewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
		viewPager.setOnPageChangeListener(new MyPageChangeListener());// 设置一个监听器，当ViewPager中的页面改变时调用

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event)) {
			event.setAction(MotionEvent.ACTION_CANCEL);
		}
		return super.dispatchTouchEvent(event);
	}

	private class GuideViewTouch extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (currentItem == 2) {
				if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY()
						- e2.getY())
						&& (e1.getX() - e2.getX() <= (-flaggingWidth) || e1
								.getX() - e2.getX() >= flaggingWidth)) {
					if (e1.getX() - e2.getX() >= flaggingWidth) {
						Toast.makeText(GuideViewActivity.this, "点击按钮开始体验", Toast.LENGTH_SHORT).show();
						return true;
					}
				}
			}
			return false;
		}
	}

	/************************* 界面跳转 *************************/
	void GoToMainActivity() {

		File SDfile = new File(FileUtil.SDPATH + "/ScheduleBackup/course.db");
		File DATAfile = new File(FileUtil.DATAPATH
				+ "/data/com.wmkj.schedule/databases/course.db");

		if (DATAfile.exists()) {
			// data有数据库 正常跳转
			Intent intent = new Intent();
			intent.setClass(GuideViewActivity.this, MainInterface.class);
			startActivity(intent);
			GuideViewActivity.this.finish();
		} else if (!DATAfile.exists() && SDfile.exists()) {
			// data没有数据库，sd有数据库 提示恢复
			creatDialog()
					.setTitle("提示")
					.setMessage("SD卡存在备份，是否恢复？")
					.setPositiveButton("是",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									// 创建databases文件夹
									File file = new File(FileUtil.DATAPATH
											+ "/data/com.wmkj.activity/"
											+ DATAfolder);
									if (!file.exists()) {
										file.mkdirs();
									}

									if (fileUtil.FileRestore()) {
										Toast.makeText(GuideViewActivity.this,
												"恢复成功", Toast.LENGTH_SHORT)
												.show();
										// 恢复成功，正常跳转
										Intent intent = new Intent();
										intent.setClass(GuideViewActivity.this,
												MainInterface.class);
										startActivity(intent);
										GuideViewActivity.this.finish();

									} else {
										Toast.makeText(GuideViewActivity.this,
												"恢复失败", Toast.LENGTH_SHORT)
												.show();
										// 恢复失败，跳转到somequestions
										Intent intent = new Intent();
										intent.setClass(GuideViewActivity.this,
												SomeQuestions.class);
										startActivity(intent);
										GuideViewActivity.this.finish();
									}
								}

							})
					.setNegativeButton("否",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
									Intent intent = new Intent();
									intent.setClass(GuideViewActivity.this,
											SomeQuestions.class);
									startActivity(intent);
									GuideViewActivity.this.finish();
								}

							}).show();

		} else if (!DATAfile.exists() && !SDfile.exists()) {
			// data没有数据库，sd没有数据库 跳转到somequestions
			Intent intent = new Intent();
			intent.setClass(GuideViewActivity.this, SomeQuestions.class);
			startActivity(intent);
			GuideViewActivity.this.finish();
		}
	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyPageChangeListener implements OnPageChangeListener {

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			currentItem = position;
		}

		public void onPageScrollStateChanged(int arg0) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}

	/**
	 * 填充ViewPager页面的适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mImageViews.get(arg1));
			return mImageViews.get(arg1);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
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

	/************************* 创建Dialog *************************/
	private AlertDialog.Builder creatDialog() {
		return new AlertDialog.Builder(this);
	}

}
