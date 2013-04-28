package com.wmkj.view;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import com.wmkj.activity.MainInterface;

public class TextView {

	public float width = 0, height = 0;
	public float x = 0, y = 0;
	public String classname;
	public String classplace;
	public String text;
	public float textSize;
	private static Paint paint;

	public TextView(float x, float y, float width, float height) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.classname = "";
		this.classplace = "";
		this.text = "";
		this.textSize = 0;
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
	}

	public void drawFormText(Canvas canvas) {
		paint.setTextAlign(Align.LEFT);
		canvas.drawText(classname, x, y+textSize, paint);
		
		String string[] = new String[2];
		if(classplace.length()>4){
			string[0] = classplace.substring(0, 4);
			string[1] = classplace.substring(4,classplace.length());
			canvas.drawText(string[0],  x,  y+height-textSize -2, paint);
			canvas.drawText(string[1],  x,  y+height -2, paint);
		}else {
			canvas.drawText(classplace, x, y+height-2, paint);
		}
	}

	public void drawHorizonText(Canvas canvas) {
		paint.setTextAlign(Align.CENTER);
		canvas.drawText(text, x + width / 2, y + textSize/2 + height / 2, paint);
	}

	public void drawVerticalText(Canvas canvas) {
		paint.setTextAlign(Align.CENTER);
		canvas.drawText(text, x + width / 2, y + height / 2+textSize/2, paint);
	}
	


	public void setTextSize(float size) {
		textSize = size * MainInterface.dm.density;
		paint.setTextSize(textSize);

	}

	public void setTextColor(int color) {
		paint.setColor(color);
	}

}
