package com.sise.subappstore.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

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


public class CommonFunctions 
{
	/**
	 * @Description: converts byte stream to string response.
	 * @param is(InputStream)
	 * @return string response
	 */
	public static String convertStreamToString(InputStream is) 
	{
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb      = new StringBuilder();
        String line           = null;
        try 
        {
        	/**
        	 * when reading from buffered reader using the readline function
        	 *  newline characters are omitted 
        	 *  thats why we need to add \n after every line read.
        	 */
            while ((line = reader.readLine()) != null) 
            {
                sb.append(line + "\n");
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        } 
        finally 
        {
            try 
            {
                is.close();
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
	/**
	 * @Description: Returns the Http response in string for the given URL when request type is Get
	 * @return string response
	 */
    public static String returnGetResponse(Context ctx,String Url)
    {
    	HttpClient httpclient = new DefaultHttpClient();
    	HttpGet url = new HttpGet(Url);
    	url.setHeader("Accept","application/json");
    	HttpResponse response = null;
    	String result = null;
    	try 
    	{
    		response          = httpclient.execute(url);
			HttpEntity entity = response.getEntity();
			if (entity != null) 
			{
				/***A Simple JSON Response Read***/
				InputStream instream = entity.getContent();
	            result = convertStreamToString(instream);
	            
			}
    	}
    	catch(Exception e)
    	{
    		Log.e("EXCEPTION---->",e.toString());
    	}
    	
    	return result;
    }
    
    
    /**
	 * @Description: Returns the Http response in string for the given URL when request type is POST
	 * @return string response
	 */
    public static String returnPostResponse(String Url,HashMap<String,String> additionaParameters)
    {
    	HttpClient httpclient = new DefaultHttpClient();
    	HttpPost url = new HttpPost(Url);
    	url.setHeader("Accept","application/json");
    	if(additionaParameters.size()>0)
    	{
    		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(additionaParameters.size());  
    		nameValuePairs.clear();
    		for(String key : additionaParameters.keySet())
    		{
    			nameValuePairs.add(new BasicNameValuePair(key,additionaParameters.get(key)));
    			
    		}
    		try 
    		{
				url.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			} 
    		catch (UnsupportedEncodingException e)
    		{
				e.printStackTrace();
			}
    	}
    	
    	HttpResponse response = null;
    	String result = null;
    	try 
    	{
    		response          = httpclient.execute(url);
			HttpEntity entity = response.getEntity();
			if (entity != null) 
			{
				/***A Simple JSON Response Read***/
				InputStream instream = entity.getContent();
	            result        = convertStreamToString(instream);
	            
			}
    	}
    	catch(Exception e)
    	{
    		Log.e("EXCEPTION---->",e.toString());
    	}
    	
    	return result;
    }
    
    public static String returnServiceResponse(String Url,String jsonHeader)
    {
    	URL u;
    	BufferedReader rd = null;
    	StringBuilder sb = new StringBuilder(); 
		try 
		{
			u = new URL(Url);
		
		    URLConnection uc = u.openConnection();
		    HttpURLConnection connection = (HttpURLConnection) uc;
		    connection.setDoOutput(true);
		    connection.setDoInput(true);
		    connection.setRequestProperty("Accept", "application/json");
		    connection.setRequestMethod("POST");
		    connection.setRequestProperty("Connection", "Keep-Alive");
		    connection.setRequestProperty("Content-type", "application/json");
		    
		    OutputStream out = connection.getOutputStream();
		    Writer wout = new OutputStreamWriter(out);
		    wout.write(jsonHeader);
		    wout.flush();
		    wout.close();
		    
		    rd = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
		    String line = null;
		    while ((line = rd.readLine()) != null) {
		        sb.append(line + "\n");
		    }
		    rd.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return sb.toString();
		
    }
	
 
    /**
     * This method is used to download the image from given url
     * 
     * @param imageUrl - URL of image being download
     * @return BitmapDrawable of image
     */
    public static BitmapDrawable downloadImage(String imageUrl)
    {
    	/*if(Constants.DEBUG)
    	{
    		Log.e("", "Inside downloadImage() method");
    	}*/
     
    	URL url     = null;
        HttpGet httpRequest  = null;
        BitmapDrawable TileMe  = null;
        BitmapDrawable bmd = null;
        try 
        {
        	url = new URL(imageUrl);
        	httpRequest = new HttpGet(url.toURI());
        	HttpClient httpclient = new DefaultHttpClient();
		    HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
		    HttpEntity entity = response.getEntity();
		    BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity); 
		    InputStream instream = bufHttpEntity.getContent();
		    Bitmap bm = BitmapFactory.decodeStream(instream);
		    TileMe = new BitmapDrawable(bm);
		    //Matrix matrix = new Matrix();
		    //matrix.postScale(25,15);
		    //Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, 100,80,true);
		    //bmd = new BitmapDrawable(resizedBitmap);
		    //System.out.println(bmd);
        } 
        catch (Exception e) 
        {
        	Log.e("CommonFunctions", "Error in downloadImage() method : " + e.toString());
        }
       /* if(Constants.DEBUG)
        {
        	Log.e("", "outside downloadImage() method");
        }*/
        return TileMe;
    }
    
    public static String returnUrlWithParams(String url,HashMap<String,String> parameters)
    {
        if(!url.endsWith("?"))
            url += "?";

        List<NameValuePair> params = new LinkedList<NameValuePair>();
        for(String key :parameters.keySet())
        {
        	params.add(new BasicNameValuePair(key,parameters.get(key)));
        }

        String paramString = URLEncodedUtils.format(params, "utf-8");

        url += paramString;
        //Log.e("",url);
        return url;
    }
    
    public static void DisplayToast(Context ctx,String msg)
    {
    	Toast.makeText(ctx, msg,Toast.LENGTH_SHORT).show();
    }
    
    public static String returnJson(HashMap<String,String> receivedMap)
    {
    	String str = null;
    	{
    		str = "{";
    		for(String key: receivedMap.keySet())
    		{
    			if(key.equals("fields"))
    			{
    				str = str + " \"" + key +"\": "+receivedMap.get(key) + ",";
    			}
    			else
    			{
    				str = str + " \"" + key +"\": \""+ receivedMap.get(key) + "\",";
    			}
    		}
    	}
    	str = str.substring(0,str.length()-1)+" }";
    	//Log.e("Str",str);
    	return str;
    }
    
    public static void turnGPSOn(Context context)
	 {
		//Log.e("","inside turnOn the GPS");
	     String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

	     if(!provider.contains("gps"))
	     {
	    	 Toast.makeText(context, "turning on the gps",Toast.LENGTH_SHORT).show();
	         final Intent poke = new Intent();
	         poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
	         poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	         poke.setData(Uri.parse("3")); 
	         context.sendBroadcast(poke);
	     }
	 }
    public static void displayAlert(Activity act,String msg)
    {
    	AlertDialog.Builder builder = new AlertDialog.Builder(act);
    	builder.setMessage(msg);
    	builder.setCancelable(false);
    	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() 
    	{
    		public void onClick(DialogInterface dialog, int id) 
    		{
    			dialog.dismiss();
    	    }
    	});
    	    
    	AlertDialog alert = builder.create();
    	alert.show();
    }
    
    //To check internet connection.
    public static boolean isOnline(Context ctx) 
	{
    	
    	//answers queries about the state of network connectivity.
		 ConnectivityManager cm = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		 NetworkInfo netInfo = cm.getActiveNetworkInfo();
		 if (netInfo != null && netInfo.isConnectedOrConnecting()) 
		 {
			return true;
		 }
		 return false;
	}
    
    
}
