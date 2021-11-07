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

public class Http_Autenticacion {
	
	
	private String urlConexion="";
	Httpposting post;
	UsuarioBean user;
	JSONObject[] array_json_data;
	public String response = ""; 
	private String msg="";


	public Http_Autenticacion(String uConexion){
		post = new Httpposting();
		this.urlConexion=uConexion;	
	}
	
	
	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}



	public boolean estadoLogin(String c,String u){
		
		int estadoLogin =-1;

		boolean b=false;
		
		// ###### Asignación de parámetros
		ArrayList<NameValuePair> post2Parametrosenviar = new ArrayList<NameValuePair>();
		post2Parametrosenviar.add(new BasicNameValuePair("password", c));
		post2Parametrosenviar.add(new BasicNameValuePair("usuario", u));
		
		// ###### Realización de petición POST
		JSONArray jdata=post.getserverdata(post2Parametrosenviar, urlConexion);
		// ###### Hacemos esperar a la aplicación lo suficiente como para que la petición nos devuelva una respuesta
		SystemClock.sleep(950);
		
		
		if (jdata!=null && jdata.length() > 0){

    		JSONObject json_data;
    		
			try {
				
				
				json_data = jdata.getJSONObject(0);
				estadoLogin=json_data.getInt("status");
				
				
				 Log.e("valor de status login : ", estadoLogin+"");
				
				
				  if(estadoLogin==1){
	    			 Log.v("loginstatus ", "válido");
	    			 b = true;
		    		 }else{	
		    			 		
		    			      this.msg = "Usuario y/o Clave son incorrectos , intételo una vez más .";
			    			 Log.e("loginstatus ", "no válido");
			    			 Log.e("loginstatus ", "= "+estadoLogin);
			    			 return b;
		    		 }
				
				
			} catch (JSONException e) {
				this.msg = "Servicio de Logueo no esta disponible en estos instantes , vuélvalo intentar mas tarde .";
				//e.printStackTrace();
			}		            
             	
    		 
	  }else{	
		  this.msg = "No se ha podido establecer la conexión .";
    	  Log.e("ERROR  ", "com.sise.subappstore.http.estadoLogin() respuesta nula");
	  }
		 return b;
	}
	
	
	public boolean estadoEncrypt(String c,String u,String em,String bandera){
		Integer estadoEncrypt;
		String passEncrypt;

		boolean b=false;
		
		// ###### Asignación de parámetros necesarios para petición POST
		ArrayList<NameValuePair> post2Parametrosenviar = new ArrayList<NameValuePair>();
		post2Parametrosenviar.add(new BasicNameValuePair("password", c));
		post2Parametrosenviar.add(new BasicNameValuePair("user", u));
		post2Parametrosenviar.add(new BasicNameValuePair("email", em));
		post2Parametrosenviar.add(new BasicNameValuePair("bandera", bandera));
		
		// ###### Ejecución de petición
		JSONArray jdata=post.getserverdata(post2Parametrosenviar, urlConexion);
		
		// ###### Espera de la aplicación a petición
		SystemClock.sleep(950);
		
		if (jdata!=null && jdata.length() > 0){

    		JSONObject json_data;
    		
			try {
				json_data = jdata.getJSONObject(0);
				estadoEncrypt=json_data.getInt("status");
				
				
				 Log.e("valor de status Encrypt : ", estadoEncrypt+"");
				
				if(estadoEncrypt==1){
					
					passEncrypt =json_data.getString("passEncrypt");
	    			 Log.v("estadoEncrypt ", "válido");
	    			 Log.v("estadoEncrypt ", estadoEncrypt+"");
	    			 response = passEncrypt;
	    			 b = true;
	    			 this.msg="ok";
				}else{
					 Log.v("estadoEncrypt ", "no válido , existe un usuario o email igual en la BD");
	    			 Log.v("estadoEncrypt ", estadoEncrypt+"");
					 b = false;
					 if(bandera=="1")
						 this.msg="Usuario u Email ya se encuentran registrados.";
					 else
						 this.msg="No se logró renovar su clave";
					 
					 Log.e("com.sise.subappstore.estadoEncrypt() Encriptación y Validación ESTADO: ", "Datos ya registrados");
				}
				 
				
			} catch (JSONException e) {
				//e.printStackTrace();
				this.msg="No se pudo procesar los datos , inténtelo mas tarde.";
				 Log.e("com.sise.subappstore.estadoEncrypt() Encriptación y Validación ESTADO: ", "Error al procesar la respuesta");
			}		            
             	
    		 
	  }else{	
		  		this.msg="El servicio de registro no esta disponible en estos instantes , inténtelo luego .";
    			 Log.e("com.sise.subappstore.estadoEncrypt() Encriptación y Validación ESTADO: ", "Respuesta NULA");
	  }
		 return b;
		
		
	}

}
