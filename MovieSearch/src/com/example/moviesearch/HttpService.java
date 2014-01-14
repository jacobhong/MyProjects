package com.example.moviesearch;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

public class HttpService {
	private String api = "http://www.mymovieapi.com/?limit=10&q=";
	private String json;

	public String getJSON(String url) {
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			String encodedURL = api + URLEncoder.encode(url, "UTF-8");
			HttpGet getRequest = new HttpGet(encodedURL);
			HttpResponse getResponse = client.execute(getRequest);
			int statusCode = getResponse.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.e("statuscode", "http request failed");
				return null;
			}
			HttpEntity entity = getResponse.getEntity();
			json = EntityUtils.toString(entity);
			return json;
		} catch (ClientProtocolException e) {
			Log.e("clientprotocolexception", "error", e);

		} catch (IOException e) {
			Log.e("ioexception", "", e);

		}
		return null;
	}

	public InputStream getIs(String url) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(url);
		InputStream is;
		try {
			HttpResponse getResponse = client.execute(getRequest);
			int statusCode = getResponse.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.e("statuscode", "http request failed");
				return null;
			}
			is = getResponse.getEntity().getContent();
			return is;
		} catch (ClientProtocolException e) {
			Log.e("is", "", e);
		} catch (IOException e) {
			Log.e("is", "", e);
		}

		return null;

	}

	public Bitmap getBitmap(String url) {
		InputStream is;
		is = this.getIs(url);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		try {
			Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
			return bitmap;
		} catch (Exception e) {
			Log.e("bitmapNULL", "", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {

				}
			}
		}
		return null;
	}
}
