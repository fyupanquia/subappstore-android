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

public class Http_Subasta {
	
				public JSONObject[] array_json_data;
				private String urlConexion="";
				public String response = "";
				int indice=0;
				
				Httpposting post;
				UsuarioBean user;

				public Http_Subasta(String uConexion){
					post = new Httpposting();
					this.urlConexion=uConexion;	
				}
				
				public boolean estadoRetiroSubasta(String razon,String grupo,Integer id){
					
						boolean b= false;
		
						int response=0;
							Log.e("ESTADO estadoRetiroSubasta : ", "asignando parámetros post");
							ArrayList<NameValuePair> post3Parametrosenviar = new ArrayList<NameValuePair>();
							post3Parametrosenviar.add(new BasicNameValuePair("razon", razon));
							post3Parametrosenviar.add(new BasicNameValuePair("grupo", grupo));
							post3Parametrosenviar.add(new BasicNameValuePair("id", ""+id));
							Log.e("ESTADO estadoRetiroSubasta : ", "datos razon,"+razon+" grupo,"+grupo+" id,"+id);
							
							Log.e("ESTADO estadoRetiroSubasta : ", "entrando a zona try - catch");
							try {
								
								JSONArray jdata=post.getserverdata(post3Parametrosenviar, urlConexion);
								
								Log.e("ESTADO estadoRetiroSubasta : ", "dando tiempo al servidor de enviar una respuesta ... ");
								SystemClock.sleep(800);
								
								if (jdata!=null && jdata.length() > 0){
									Log.e("ESTADO estadoRetiroSubasta : ", "el servidor envió una respuesta no nula");	
								    		JSONObject json_data;
								    		
								    		try {
												
												json_data = jdata.getJSONObject(indice);
												response=json_data.getInt("response");

												Log.e("ESTADO estadoRetiroSubasta : ", "respuesta del servidor :"+response);
												 
												 if(response==1)
												 b=true;
												
											} catch (JSONException e) {
												//e.printStackTrace();
											}
						    		
						    		
								}
								
							} catch (Exception e) {
								//e.printStackTrace();
							}
							
									
						return b;
				}
			
				
				
				public boolean estadoIngresoSubasta(String razon,String grupo,Integer id){
					boolean b= false;
	
					int response=0;
					   Log.e("ESTADO estadoIngresoSubasta : ", "enviando parámetros ... ");
						ArrayList<NameValuePair> post3Parametrosenviar = new ArrayList<NameValuePair>();
						post3Parametrosenviar.add(new BasicNameValuePair("razon", razon));
						post3Parametrosenviar.add(new BasicNameValuePair("grupo", grupo));
						post3Parametrosenviar.add(new BasicNameValuePair("id", ""+id));
						
						Log.e("ESTADO estadoIngresoSubasta","datos :  razón,"+razon+" "+" grupo,"+grupo+" id,"+id);
						try {
							Log.e("ESTADO estadoIngresoSubasta : ", "enviando parámetros ... ");
							JSONArray jdata=post.getserverdata(post3Parametrosenviar, urlConexion);
							Log.e("ESTADO estadoIngresoSubasta : ", "esperando respuesta del servidor");
							SystemClock.sleep(800);
							if (jdata!=null && jdata.length() > 0){
								Log.e("estadoIngresoSubasta estado : ", "recibiendo respuesta no nula ");
							    		JSONObject json_data;
							    		
							    		try {
											
											json_data = jdata.getJSONObject(indice);
											response=json_data.getInt("response");

											 Log.e("ESTADO estadoIngresoSubasta : ","respuesta , "+ response+"");
											 
											 if(response==1)
											 b=true;
											 
											
										} catch (JSONException e) {
											e.printStackTrace();
										}
					    		
					    		
							}
							
						} catch (Exception e) {
							e.printStackTrace();
						}
						
								
					return b;
			}
				
				
				
				public boolean estadoNuevaOferta(String razon,String grupo,Integer idproducto,Integer idusuario,Integer nuevomonto){
					boolean b= false;
	
					int response=0;
					   Log.e("ESTADO estadoNuevaOferta : ", "asignando parámetros ... ");
						ArrayList<NameValuePair> post5Parametrosenviar = new ArrayList<NameValuePair>();
						post5Parametrosenviar.add(new BasicNameValuePair("razon", razon));
						post5Parametrosenviar.add(new BasicNameValuePair("grupo", grupo));
						post5Parametrosenviar.add(new BasicNameValuePair("idproducto", ""+idproducto));
						post5Parametrosenviar.add(new BasicNameValuePair("monto", ""+nuevomonto));
						post5Parametrosenviar.add(new BasicNameValuePair("idusuario", ""+idusuario));
						
						Log.e("estadoNuevaOferta estado","datos :  razón,"+razon+" "+" grupo,"+grupo+" idproducto,"+idproducto+" idusuario,"+idusuario+" nuevomonto,"+nuevomonto);
						try {
							Log.e("ESTADO estadoNuevaOferta : ", "enviando parámetros ... ");
							JSONArray jdata=post.getserverdata(post5Parametrosenviar, urlConexion);
							Log.e("ESTADO estadoNuevaOfertao : ", "esperando respuesta del servidor ... ");
							SystemClock.sleep(800);
							if (jdata!=null && jdata.length() > 0){
								Log.e("ESTADO estadoNuevaOferta : ", "recibiendo respuesta no nula... ");
							    		JSONObject json_data;
							    		
							    		try {
											
											json_data = jdata.getJSONObject(indice);
											response=json_data.getInt("response");

											 Log.e("ESTADO estadoNuevaOferta : ","respuesta , "+ response+"");
											 
											 if(response==1)
											 b=true;
											 
											
										} catch (JSONException e) {
											e.printStackTrace();
										}
					    		
					    		
							}
							
						} catch (Exception e) {
							e.printStackTrace();
						}
						
								
					return b;
			}
				
				
				public boolean estadoFinalizarSubasta(String razon,String grupo,Integer idproducto,Integer idusuario,Integer montofinal,String level){
					boolean b= false;
	
					int response=0;
					   Log.e("ESTADO estadoFinalizarSubasta : ", "asignando parámetros ... ");
						ArrayList<NameValuePair> post5Parametrosenviar = new ArrayList<NameValuePair>();
						post5Parametrosenviar.add(new BasicNameValuePair("razon", razon));
						post5Parametrosenviar.add(new BasicNameValuePair("grupo", grupo));
						post5Parametrosenviar.add(new BasicNameValuePair("idproducto", ""+idproducto));
						post5Parametrosenviar.add(new BasicNameValuePair("monto", ""+montofinal));
						post5Parametrosenviar.add(new BasicNameValuePair("idusuario", ""+idusuario));
						post5Parametrosenviar.add(new BasicNameValuePair("level", ""+level));
						 Log.e("ESTADO estadoFinalizarSubasta : ", "enviando parámetros ... ");
						Log.e("estadoNuevaOferta estado","datos :  razón,"+razon+" "+" grupo,"+grupo+" idproducto,"+idproducto+" idusuario,"+idusuario+" monto,"+montofinal);
						try {

							JSONArray jdata=post.getserverdata(post5Parametrosenviar, urlConexion);
							 Log.e("ESTADO estadoFinalizarSubasta : ", "esperando respuesta del servidor ... ");
							SystemClock.sleep(300);
							if (jdata!=null && jdata.length() > 0){
								Log.e("estadoNuevaOferta estado : ", "recibiendo respuesta no nula... ");
							    		JSONObject json_data;
							    		
							    		try {
											
											json_data = jdata.getJSONObject(indice);
											response=json_data.getInt("response");

											 Log.e("ESTADO estadoFinalizarSubasta : ", "respuesta,"+response);
											 
											 if(response==1)
											 b=true;
											 
											
										} catch (JSONException e) {
											e.printStackTrace();
										}
					    		
					    		
							}
							
						} catch (Exception e) {
							e.printStackTrace();
						}
						
								
					return b;
			}
				
				public boolean estadoEvaluarGanador(String razon,String grupo,Integer idproducto,Integer idusuario,Integer monto){
					boolean b= false;
	
					int response=0;
					   Log.e("ESTADO estadoNuevaOferta : ", "asignando parámetros ... ");
						ArrayList<NameValuePair> post5Parametrosenviar = new ArrayList<NameValuePair>();
						post5Parametrosenviar.add(new BasicNameValuePair("razon", razon));
						post5Parametrosenviar.add(new BasicNameValuePair("grupo", grupo));
						post5Parametrosenviar.add(new BasicNameValuePair("idproducto", ""+idproducto));
						post5Parametrosenviar.add(new BasicNameValuePair("idusuario", ""+idusuario));
						post5Parametrosenviar.add(new BasicNameValuePair("monto", ""+monto));
						
						Log.e("ESTADO estadoNuevaOferta : ","datos :  razón,"+razon+" "+" grupo,"+grupo+" idproducto,"+idproducto+" idusuario,"+idusuario);
						try {
							Log.e("ESTADO estadoNuevaOferta : ", "enviando parámetros ... ");
							JSONArray jdata=post.getserverdata(post5Parametrosenviar, urlConexion);
							SystemClock.sleep(300);
							if (jdata!=null && jdata.length() > 0){
								Log.e("ESTADO estadoNuevaOferta : ", "recibiendo respuesta no nula... ");
							    		JSONObject json_data;
							    		
							    		try {
											
											json_data = jdata.getJSONObject(indice);
											response=json_data.getInt("response");

											 Log.e("ESTADO estadoNuevaOferta : ","respuesta , "+ response+"");
											 
											 if(response==1)
												 b=true;
											 
											
										} catch (JSONException e) {
											e.printStackTrace();
										}
					    		
					    		
							}
							
						} catch (Exception e) {
							e.printStackTrace();
						}
						
								
					return b;
			}
				
				public boolean estadoSubasta(String razon,String grupo){
					
					int cantidad=0;
					
					boolean b=false;
					
					 Log.e("ESTADO estadoSubasta : ","asignando parámetros");
					ArrayList<NameValuePair> post2Parametrosenviar = new ArrayList<NameValuePair>();
					post2Parametrosenviar.add(new BasicNameValuePair("razon", razon));
					post2Parametrosenviar.add(new BasicNameValuePair("grupo", grupo));
					Log.e("ESTADO estadoSubasta : ","datos razon"+razon+" grupo,"+grupo);
					try {
						Log.e("ESTADO estadoSubasta : ","enviando parámetros");
						JSONArray jdata=post.getserverdata(post2Parametrosenviar, urlConexion);
						Log.e("ESTADO estadoSubasta : ","esperando respuesta del servidor");
						SystemClock.sleep(800);
						
						if (jdata!=null && jdata.length() > 0){
							Log.e("ESTADO estadoSubasta : ","recibiendo respuesta no nula");
				    		JSONObject json_data;

							try {
								
								json_data = jdata.getJSONObject(indice);
								cantidad=json_data.getInt("cantidadconectados");
								
								Log.e("ESTADO estadoSubasta : ","respuesta :"+cantidad);
								
								if(cantidad!=0){
								array_json_data = new JSONObject[cantidad];
								
								array_json_data[indice] = json_data;
								 indice+=1;
								 
								 Log.e("ESTADO estadoSubasta : ","creando array JSONObject");
									 while (cantidad>indice) {
										 json_data = jdata.getJSONObject(indice);
										 array_json_data[indice] = json_data;
										 indice+=1;
									 }
								Log.e("ESTADO estadoSubasta : ","finalizando creación array JSONObject"); 
									 b=true;
								Log.e("ESTADO estadoSubasta : ", " cantidad "+cantidad+"");
								}
								
								
							} catch (JSONException e) {
								e.printStackTrace();
							}		            
				             	
				    		 
					  }else{	
				    			 Log.e("JSON", "ERROR");
				    			 Log.e("JSON", "ERROR : com.sise.subappstore.estadoSubasta()");

					  }
						
					} catch (Exception e) {
						Log.e("CONEXIÓN","no se pudo establecer conexión con el servicio");
						e.printStackTrace();
					}
					
					
					 return b;
				}

}
