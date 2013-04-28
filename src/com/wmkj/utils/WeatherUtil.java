package com.wmkj.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class WeatherUtil {

	private static final String locationapi = "http://api.map.baidu.com/geocoder?output=json&location=";
	private static final String key = "key=78E0B9D282C1B1EF29E1C4F785D92EFB36896269";
	private static final String weather_api = "http://sou.qq.com/online/get_weather.php?callback=Weather&city=";
	private URLConnection connection;

	public WeatherUtil() {
	}

	public String getWeatherString(double latitude, double longitude) {
		
		String city = getCity(latitude, longitude);
		String str = null;
		try {
			str = URLEncoder.encode(city, "UTF-8");
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		URL url = null;
		InputStream is = null;
		int _byte;
		ByteArrayBuffer data = new ByteArrayBuffer(1024);

		try {
			url = new URL(weather_api + str);
			connection = url.openConnection();
			is = connection.getInputStream();
			while ((_byte = is.read()) != -1) {
				data.append(_byte);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			try {
				if (is != null)
					is.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String datastr = EncodingUtils.getString(data.toByteArray(), "UTF-8");
		int datalength = datastr.length();
		datastr = datastr.substring(8, datalength - 2);
		JSONTokener jsonTokener = new JSONTokener(datastr);
		try {
			JSONObject jsonObject = new JSONObject(jsonTokener)
					.getJSONObject("future");
			if (city.equals(jsonObject.getString("name")))
				return "error";
			
			datastr = jsonObject.getString("name") + ":"
					+ "  今天  "+ jsonObject.getString("wea_0")
					+"   温度: "
					+ jsonObject.getString("tmin_0") + "~"
					+ jsonObject.getString("tmax_0") + "℃"
					+ "  明天  "+jsonObject.getString("wea_1")
					+"   温度: "
					+ jsonObject.getString("tmin_1") + "~"
					+ jsonObject.getString("tmax_1") + "℃";
					
		} catch (JSONException e) {
			e.printStackTrace();
		}
		data.clear();
		return datastr;
	}

	private String getCity(double latitude, double longitude) {
		URL url = null;
		InputStream is = null;
		int _byte;
		ByteArrayBuffer data = new ByteArrayBuffer(400);
		try {
			url = new URL(locationapi + latitude + "," + longitude + "&" + key);
			connection = url.openConnection();
			is = connection.getInputStream();
			while ((_byte = is.read()) != -1) {
				data.append(_byte);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			try {
				if (is != null)
					is.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String datastr = EncodingUtils.getString(data.toByteArray(), "UTF-8");
		JSONTokener jsonTokener = new JSONTokener(datastr);
		try {
			JSONObject jsonObject = new JSONObject(jsonTokener).getJSONObject(
					"result").getJSONObject("addressComponent");
			datastr = jsonObject.getString("city");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		data.clear();
		return datastr;
	}
}
