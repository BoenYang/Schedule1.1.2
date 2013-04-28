package com.wmkj.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;

public class FileUtil {

	public static String SDPATH;
	public static String DATAPATH;

	public FileUtil() {
		SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
		DATAPATH = Environment.getDataDirectory().getAbsolutePath();
	}

	public boolean FileBackup() {

		File inputFile = new File(DATAPATH
				+ "/data/com.wmkj.activity/databases/course.db");
		File outputFile = new File(SDPATH + "/ScheduleBackup/course.db");

		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;

		byte[] bytes;
		bytes = new byte[(int) inputFile.length()];

		try {
			fileInputStream = new FileInputStream(inputFile);
			fileInputStream.read(bytes);
			fileOutputStream = new FileOutputStream(outputFile);
			fileOutputStream.write(bytes);
			return true;
		} catch (FileNotFoundException e) {
			try {
				fileInputStream.close();
				fileOutputStream.close();
				fileInputStream = null;
				fileOutputStream = null;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (fileInputStream != null)
					fileInputStream.close();
				if (fileOutputStream != null)
					fileOutputStream.close();
				fileInputStream = null;
				fileOutputStream = null;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public boolean FileRestore() {

		File inputFile = new File(SDPATH + "/ScheduleBackup/course.db");
		File outputFile = new File(DATAPATH
				+ "/data/com.wmkj.activity/databases/course.db");

		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		byte[] bytes;
		bytes = new byte[(int) inputFile.length()];

		try {
			fileInputStream = new FileInputStream(inputFile);
			fileInputStream.read(bytes);
			fileOutputStream = new FileOutputStream(outputFile);
			fileOutputStream.write(bytes);
			return true;
		} catch (FileNotFoundException e) {
			try {
				fileInputStream.close();
				fileOutputStream.close();
				fileInputStream = null;
				fileOutputStream = null;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (fileInputStream != null)
					fileInputStream.close();
				if (fileOutputStream != null)
					fileOutputStream.close();
				fileInputStream = null;
				fileOutputStream = null;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
