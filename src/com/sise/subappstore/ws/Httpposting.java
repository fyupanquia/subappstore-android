package com.sise.subappstore.ws;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import android.util.Log;

public class Httpposting {
	InputStream is = null;
	String resultado = "";
	
public  JSONArray getserverdata(ArrayList<NameValuePair> p,String urlServer){
	httpPostConnect(p,urlServer);
	if(is!=null){
		getpostresponse();
		return getjsonarray();
	}else{
		return null;
	}
}

private void httpPostConnect(ArrayList<NameValuePair> p,String urlWS) {
	
	try {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(urlWS);
		httppost.setEntity(new UrlEncodedFormEntity(p));
		HttpResponse respose = httpclient.execute(httppost);
		HttpEntity entity = respose.getEntity();
		is = entity.getContent();
		
	} catch (Exception e) {
		Log.e("HttpException", "Error en conexion http "+e.toString()+" "+e.getMessage().toString());
	}
	
	
}


public void getpostresponse(){
	try {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line+"\n");
		}
		is.close();
		resultado = sb.toString();
		Log.e("getpostresponse", " result ="+sb.toString());		
		
	} catch (Exception e) {
		Log.e("getpostrespose()Exception", "Erro al convertir resultado "+e.toString());
	}
	
}


public JSONArray getjsonarray(){
	try {
		JSONArray jArray = new JSONArray(resultado);
		return jArray;
	} catch (Exception e) {
		Log.e("getjsonarray()Exception","Error al analizar datos "+e.toString());
		return null;
	}
}






}



