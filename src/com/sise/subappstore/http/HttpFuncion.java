package com.sise.subappstore.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.sise.subappstore.bean.UsuarioBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class HttpFuncion {
	/**
	 * @Description: converts byte stream to string response.
	 * @param is
	 *            (InputStream)
	 * @return string response
	 */
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			/**
			 * when reading from buffered reader using the readline function
			 * newline characters are omitted thats why we need to add \n after
			 * every line read.
			 */
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();

	}

	public static String returnServiceResponse(String Url, String jsonHeader) {
		
		
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(Url);
		httpost.setHeader("Content-type", "application/json");
		StringEntity entity = null;

		try {
			entity = new StringEntity(jsonHeader, "utf-8");
		}

		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		entity.setContentType("application/json;charset=UTF-8");

		httpost.setEntity(entity);
		HttpResponse response;
		String result = null;
		try {
			response = httpclient.execute(httpost);
			// Log.e("Response",response);
			HttpEntity httpentity = response.getEntity();
			if (httpentity != null) {
				/*** A Simple JSON Response Read ***/
				InputStream instream = httpentity.getContent();
				result = convertStreamToString(instream);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}
	

}
