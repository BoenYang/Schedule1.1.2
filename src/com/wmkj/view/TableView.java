package com.wmkj.view;

import com.wmkj.activity.MainInterface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class TableView extends View {

	protected static int row = 5, column = 7;
	protected static float tableWidth, tableHeight;
	protected float thWidth = 20, thHeight = 20;
	protected float tdWidth, tdHeight;
	private Paint linePaint;
	protected boolean isFirstStart = true;
	protected  Paint paint;

	public TableView(Context context) {
		super(context);
		linePaint = new Paint();
		linePaint.setAntiAlias(true);
		linePaint.setColor(Color.rgb(59, 59, 59));
		linePaint.setStrokeWidth(1 * MainInterface.dm.density);
		thHeight = thHeight * MainInterface.dm.density;
		thWidth = thWidth * MainInterface.dm.density;
		paint = new Paint();
		paint.setColor(Color.rgb(111, 37, 38));
		paint.setAlpha(90);
		paint.setAntiAlias(true);
	}

	public TableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		linePaint = new Paint();
		linePaint.setAntiAlias(true);
		linePaint.setColor(Color.rgb(59, 59, 59));
		linePaint.setStrokeWidth(1 * MainInterface.dm.density);
		thHeight = thHeight * MainInterface.dm.density;
		thWidth = thWidth * MainInterface.dm.density;
		paint = new Paint();
		paint.setColor(Color.rgb(111, 37, 38));
		paint.setAlpha(90);
		paint.setAntiAlias(true);
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (isFirstStart) {
			updateLayoutData();
			isFirstStart = false;
		}

		canvas.drawLine(0, 0, tableWidth, 0, linePaint);
		canvas.drawLine(0, 0 + thHeight, tableWidth, 0 + thHeight, linePaint);
		canvas.drawLine(0, 0, 0, tableHeight, linePaint);
		canvas.drawLine(0 + thWidth, 0, 0 + thWidth, tableHeight, linePaint);

		for (int i = 1; i <= row; i++) {
			canvas.drawLine(0, thHeight + i * tdHeight, tableWidth, thHeight
					+ i * tdHeight, linePaint);
		}

		for (int i = 1; i <= column; i++) {
			canvas.drawLine(thWidth + i * tdWidth, 0, thWidth + i * tdWidth,
					tableHeight, linePaint);
		}

		canvas.drawRect(0, 0, thWidth, thHeight, paint);
		for (int i = 0; i < row; i++) {
			canvas.drawRect(0, thHeight + tdHeight * i + 2, thWidth, thHeight
					+ tdHeight * i + tdHeight, paint);
		}
	}

	/**
	 * update view's width height
	 */
	public void updateLayoutData() {
		tableHeight = getHeight();
		tableWidth = getWidth();
		tdHeight = (tableHeight - thHeight) / row;
		tdWidth = (tableWidth - thWidth) / column;
	}

	/**
	 * set the number of row and column
	 * 
	 * @param r
	 *            row
	 * @param c
	 *            column
	 */
	public static void setRowAndColum(int r, int c) {
		row = r;
		column = c;
	}

	/**
	 * set the width and height of table head
	 * 
	 * @param thw
	 *            table head width
	 * @param thh
	 *            table head height
	 */
	public void setThwandThh(float thw, float thh) {
		thHeight = thh * MainInterface.dm.density;
		thWidth = thw * MainInterface.dm.density;
	}

	/**
	 * table form
	 * 
	 * @author yangbowen
	 * 
	 */
	final class Form {
		public int row, colum;
		public float x, y;
		public TextView tv;

		public Form(int r, int c) {
			this.row = r + 1;
			this.colum = c + 1;
			this.x = thWidth + c * tdWidth;
			this.y = thHeight + r * tdHeight;
		}

		@Override
		public String toString() {
			return "Form [row=" + row + ", colum=" + colum + "]";
		}
	}

}
