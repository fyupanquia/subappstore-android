package com.sise.subappstore.http;

import java.nio.charset.Charset;
import java.util.ArrayList;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;

import com.sise.subappstore.InfoActivity;
import com.sise.subappstore.R;
import com.sise.subappstore.RegistrarSubasta;
import com.sise.subappstore.bean.ProductoBean;
import com.sise.subappstore.ws.Httpposting;

public class Http_Acerca_de extends AsyncTask<Integer, Void, String>{
	
	private ProductoBean p,presult;
	public Context ioContext;
	private String isMensaje;
	private ProgressDialog ioProgressDialog;
	private String descripcion="";
	Httpposting post;
	
	public Http_Acerca_de(Context psClase)
	{
		ioContext=psClase;
	}
	
	@Override
	protected void onPostExecute(String result) {

			
			((InfoActivity)ioContext).describir(descripcion);

	}
	
	@Override
	protected void onPreExecute() {

	}

	@Override
	protected String doInBackground(Integer... params) {
		post = new Httpposting();

					
					Log.v("com.sise.subappstore.http.Http_Acerca_De.doInBackground() Estado ","Iniciando método doInBackground()");
		
					Integer pos = params[0];
					ArrayList<NameValuePair> post1Parametrosenviar = new ArrayList<NameValuePair>();
					post1Parametrosenviar.add(new BasicNameValuePair("posicion",pos +""));
					String ruta =  ioContext.getString(R.string.urlgetInfoAcercade);
					JSONArray jdata=post.getserverdata(post1Parametrosenviar,ruta);
					
					 Log.e("com.sise.subappstore.http.Http_Acerca_De.doInBackground() Estado : ", "esperando...");
					SystemClock.sleep(550);

					
					if (jdata!=null && jdata.length() > 0){

			    		JSONObject json_data;
			    		
						try {
							
							
							json_data = jdata.getJSONObject(0);
							this.descripcion=json_data.getString("descripcion");
							
							
							 Log.e("com.sise.subappstore.http.Http_Acerca_De.doInBackground() Estado : ","respuesta :"+ descripcion);

							
							
						} catch (JSONException e) {
							e.printStackTrace();
						}		            
			             	
			    		 
				  }else{	
			    			 Log.e(" com.sise.subappstore.http.Http_Acerca_De.doInBackground() Estado :  ", "com.sise.subappstore.http.Http_Acerca_De.doInBackground() respuesta nula");
				  }
				  

				
				return descripcion;
		
	}

	
	



	
	

}
