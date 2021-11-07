package com.sise.subappstore.http;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.SystemClock;
import android.util.Log;

import com.sise.subappstore.bean.UsuarioBean;
import com.sise.subappstore.ws.Httpposting;

public class Http_ProductosActivity {
	
	
	private String urlConexion="";
	Httpposting post;
	private Integer idUser=0;
	public JSONObject[] array_json_data;
	public String response = ""; 
	private String msg = "";
	
	public Http_ProductosActivity(String uConexion){
					post = new Httpposting();
					this.urlConexion=uConexion;	
	}
	
	
public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}


public Integer getIdUser() {
		return idUser;
	}


	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}


public boolean estadoPorSubastar(String filtro){
		
		int cantPorSubastarTotal=-1;
		
		boolean b=false;
		
		ArrayList<NameValuePair> post1Parametrosenviar = new ArrayList<NameValuePair>();
		post1Parametrosenviar.add(new BasicNameValuePair("filtro", filtro));
		post1Parametrosenviar.add(new BasicNameValuePair("vfiltro", idUser+""));
		
		JSONArray jdata=post.getserverdata(post1Parametrosenviar, urlConexion);
		SystemClock.sleep(550);
		
		if (jdata!=null && jdata.length() > 0){

    		JSONObject json_data;
    		
    		
    		int indice=0;
			try {
				
				
				//cantPorSubastarTotal=json_data.getInt("cantidadtotal");
				cantPorSubastarTotal=jdata.length();
				
				array_json_data = new JSONObject[cantPorSubastarTotal];
				
				 Log.e("valor de cantPorSubastarTotal : ", cantPorSubastarTotal+"");
				 
				 json_data = jdata.getJSONObject(indice);
				 array_json_data[indice] = json_data;
				 indice+=1;
				 
				 //if(cantPorSubastarTotal>indice){
					 
					 while (cantPorSubastarTotal>indice) {
						 json_data = jdata.getJSONObject(indice);
						 array_json_data[indice] = json_data;
						 indice+=1;
					}
					 
				 //}

				 b=true;
				
			} catch (JSONException e) {
				//e.printStackTrace();
			}		            
             	
    		 
	  }else{	
		  this.msg="vacio";
    			 Log.e("JSON  ", "ERROR : Http_ProductosActivity com.sise.subappstore.htttp.Http_ProductosActivity.estadoPorSubastar");
    			 Log.e("JSON  ", "ERROR : Http_ProductosActivity com.sise.subappstore.htttp.Http_ProductosActivity.estadoPorSubastar");
    			 Log.e("JSON  ", "ERROR : Http_ProductosActivity com.sise.subappstore.htttp.Http_ProductosActivity.estadoPorSubastar");
	  }
		 return b;
	}

	public boolean estadoCategorias(String filtro){
		
		int cantCategorias=-1;
		
		boolean b=false;
		
		ArrayList<NameValuePair> post1Parametrosenviar = new ArrayList<NameValuePair>();
		post1Parametrosenviar.add(new BasicNameValuePair("filtro", filtro));
		
	
		
		JSONArray jdata=post.getserverdata(post1Parametrosenviar, urlConexion);
		SystemClock.sleep(550);
		
		if (jdata!=null && jdata.length() > 0){
	
			JSONObject json_data;
			
			
			int indice=0;
			try {
				
				//json_data = jdata.getJSONObject(indice);
				//cantPorSubastarHoy=json_data.getInt("cantidadhoy");
				cantCategorias=jdata.length();
				
				array_json_data = new JSONObject[cantCategorias];
				
				 Log.e("valor de cantPorSubastarTotal : ", cantCategorias+"");
				 Log.e("valor de cantPorSubastarTotal : ", cantCategorias+"");
				 Log.e("valor de cantPorSubastarTotal : ", cantCategorias+"");
				 
				 array_json_data[indice] = jdata.getJSONObject(indice);
				 indice+=1;
				 
					 
					 while (cantCategorias>indice) {
						 json_data = jdata.getJSONObject(indice);
						 array_json_data[indice] = json_data;
						 indice+=1;
					}
					 
				 
	
				 b=true;
				
			} catch (JSONException e) {
				//e.printStackTrace();
			}		            
	         	
			 
	  }else{	
				 Log.e("JSON  ", "ERROR");
				 Log.e("JSON  ", "ERROR");
				 Log.e("JSON  ", "ERROR");
				 Log.e("JSON  ", "ERROR : Http_ProductosActivity com.sise.subappstore.htttp.Http_ProductosActivity.estadoCategorias");
				 Log.e("JSON  ", "ERROR : Http_ProductosActivity com.sise.subappstore.htttp.Http_ProductosActivity.estadoCategorias");
				 Log.e("JSON  ", "ERROR : Http_ProductosActivity com.sise.subappstore.htttp.Http_ProductosActivity.estadoCategorias");
	  }
		 return b;
	}
	
	
}
