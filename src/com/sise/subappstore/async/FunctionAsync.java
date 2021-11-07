package com.sise.subappstore.async;

	import org.json.JSONException;
import org.json.JSONObject;
	
	import com.sise.subappstore.Fragment_Home;
import com.sise.subappstore.LoginActivity;
import com.sise.subappstore.MenuActivity;
import com.sise.subappstore.ModificarPerfil;
import com.sise.subappstore.R;
import com.sise.subappstore.RegistrarSubasta;
import com.sise.subappstore.RegistroActivity;
import com.sise.subappstore.Subappstore;
import com.sise.subappstore.bean.BeanToast;
import com.sise.subappstore.bean.SesionBean;
import com.sise.subappstore.bean.UsuarioBean;
import com.sise.subappstore.http.Http_Autenticacion;
import com.sise.subappstore.http.Http_ProductosActivity;
import com.sise.subappstore.http.Http_Subasta;
import com.sise.subappstore.ws.Httpposting;
	
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;
	
	import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.gcm.GoogleCloudMessaging;
	

	public class FunctionAsync extends AsyncTask< String, String, String > {
		
		private String url,msg="",caso,respuesta="";
		private ProgressDialog pDialog;
		private Context cc;
		private Httpposting post;
		private UsuarioBean u;
		public JSONObject[] response_array_json_data;
		
		private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
		private Activity a;
		static final String TAG = "GCMDemo";
		private GoogleCloudMessaging gcm;
		private SharedPreferences.Editor edit;
		private   BeanToast bt ;
		
		
		public SharedPreferences.Editor getEdit() {
			return edit;
		}

		public void setEdit(SharedPreferences.Editor edit) {
			this.edit = edit;
		}

		public FunctionAsync(Context c,UsuarioBean user,String msg){
			post = new Httpposting();
			this.cc = c;  	// ###### Contexto del layout de la solicitud
			this.u= user; 	// ###### Datos del objeto
			this.msg = msg; // ###### Mensaje de proceso
			
			bt= new BeanToast(c,"",((Activity)c).getLayoutInflater(),((Activity)c).findViewById(R.id.lyToast),R.id.tvMsgToast);
		}
		
		public FunctionAsync(Context c,SharedPreferences.Editor edit,String msg){
			post = new Httpposting();
			this.cc = c;  	// ###### Contexto del layout de la solicitud
			this.edit= edit; 	// ###### Datos del objeto
			this.msg = msg; // ###### Mensaje de proceso
			bt= new BeanToast(c,"",((Activity)c).getLayoutInflater(),((Activity)c).findViewById(R.id.lyToast),R.id.tvMsgToast);
		}
		
		public FunctionAsync(Context c,UsuarioBean user,String msg,Activity a){
			post = new Httpposting();
			this.cc = c;  	// ###### Contexto del layout de la solicitud
			this.u= user; 	// ###### Datos del objeto
			this.msg = msg; // ###### Mensaje de proceso
			this.a = a;
			bt= new BeanToast(c,"",((Activity)c).getLayoutInflater(),((Activity)c).findViewById(R.id.lyToast),R.id.tvMsgToast);
		}
		
		
		protected void onPreExecute() {
			
			if(msg!=""){
				// ###### Carga del diálogo de proceso
		        pDialog = new ProgressDialog(cc);
		        pDialog.setTitle("SubAppStore");
		        pDialog.setMessage(msg);
		        pDialog.setIndeterminate(false);
		        pDialog.setCancelable(false);
		        pDialog.show();
			}

	    }
		
		@Override
		protected String doInBackground(String... params) {
			this.url=params[0];     // ###### URL para procesar datos
			this.caso = params[1];  // ###### Proceso del caso
			
			String response="err";
			
			if(caso=="login"){
				
				Http_Autenticacion bUsuario = new Http_Autenticacion(url) ;
				if (bUsuario.estadoLogin(u.getPassword(),u.getUsuario())==true){ 
					response= "ok";
					this.msg = bUsuario.getMsg();
				}else{
					this.msg = bUsuario.getMsg();
				}
			}else if(caso=="encrypt"){
				
				Http_Autenticacion bEncrypt = new Http_Autenticacion(url) ;
				if (bEncrypt.estadoEncrypt(u.getPassword(),u.getUsuario(),u.getEmail(),params[2])==true){ 
					response= "ok-"+params[2];
					respuesta = bEncrypt.response;
				}else{
					this.msg = bEncrypt.getMsg();
				}
			}else if(caso=="xSubastar"){
				
				Http_ProductosActivity bProductos = new Http_ProductosActivity(url) ;
				if (bProductos.estadoPorSubastar(params[2])==true){ 
					response= "ok-"+params[2];
					response_array_json_data = bProductos.array_json_data ;
				}
			}else if(caso=="xSubastarMios"){
				
				Http_ProductosActivity bProductos = new Http_ProductosActivity(url) ;
				bProductos.setIdUser(u.getId());
				if (bProductos.estadoPorSubastar(params[2])==true){ 
					response= "ok-"+params[2];
					response_array_json_data = bProductos.array_json_data ;
				}else{
					if(bProductos.getMsg()=="vacio"){
						response= "okvacio-"+params[2];
					}
				}
			}else if(caso=="xSubastarGanar"){
				
				Http_ProductosActivity bProductos = new Http_ProductosActivity(url) ;
				bProductos.setIdUser(u.getId());
				if (bProductos.estadoPorSubastar(params[2])==true){ 
					response= "ok-"+params[2];
					response_array_json_data = bProductos.array_json_data ;
				}else{
					if(bProductos.getMsg()=="vacio"){
						response= "okvacio-"+params[2];
					}
				}
			}else if(caso=="lstCategorias"){
				
				Http_ProductosActivity bProductos = new Http_ProductosActivity(url) ;
				if (bProductos.estadoCategorias("all")==true){ 
					response= "ok";
					response_array_json_data = bProductos.array_json_data ;
				}
			}else if(caso=="Subastando"){
				Http_Subasta https = new Http_Subasta(url);
				if (https.estadoSubasta("conectados",params[2])==true){ 
					response= "ok";
					response_array_json_data = https.array_json_data ;
				}
			}else if(caso=="RetirarSubasta"){
				respuesta="0";
				Http_Subasta https = new Http_Subasta(url);
				if (https.estadoRetiroSubasta("retirarsubasta",params[2],u.getId())==true){ 
					response= "ok";
					respuesta=params[3];
				}
			}else if(caso=="ConectarSubasta"){
				Http_Subasta https = new Http_Subasta(url);
				if (https.estadoIngresoSubasta("conectarsubasta",params[2],u.getId())==true){ 
					response= "ok";
				}
			}else if(caso=="OfrecerMonto"){
				Http_Subasta https = new Http_Subasta(url);
				if (https.estadoNuevaOferta("nuevaoferta",params[2],Integer.parseInt(params[3]),u.getId(),Integer.parseInt(params[4]))==true){ 
					response= "ok";
				}
			}else if(caso=="EvaluarGanador"){
				Http_Subasta https = new Http_Subasta(url);
				if (https.estadoFinalizarSubasta("setGanador",params[2],Integer.parseInt(params[3]),u.getId(),Integer.parseInt(params[4]),params[5])==true){ 
					response= "ok";
				}
			}else if(caso=="logout"){
				SesionBean sb = new SesionBean(edit);
				if(sb.logout()){
					response= "ok";
				}
			}
						
			return response;
	        
		}
		
		protected void onPostExecute(String result) {
			
			if(pDialog!=null){
				pDialog.dismiss();
			}
			
			Log.e("onPostExecute -"+caso+" :",""+result);
			
			if(caso=="login"){
				
			        if (result.equals("ok")){
			        	Log.e("onPostExecute=","login ok");
			        	((LoginActivity)cc).getUser();
						
			         }else{
			        	 err_function(this.msg);
			         }
				
			}else if(caso=="encrypt"){
				
			        if (result.equals("ok-1")){
			        	Log.e("onPostExecute=","encrypt ok");
			        	((RegistroActivity)cc).registroUser(respuesta);
						
			         }else if (result.equals("ok-0")){
				        	Log.e("onPostExecute=","encrypt ok");
				        	((ModificarPerfil)cc).updateUser(respuesta);
							
				     }else{
			         	err_function(this.msg);
			         }
			        
			}else if(caso=="xSubastar"){
				String[] response = result.split("-");
			        if (response[0].equals("ok")){
			        	Log.e("xSubastar onPostExecute=","xsubastar ok");
			        	((MenuActivity)cc).instanciar(response_array_json_data,response[1]);
						
			         }else{
			         	err_function("Error al listar productos por subastar");
			         }
		        
			}else if(caso=="xSubastarMios"){
				String[] response = result.split("-");
		        if (response[0].equals("ok")){
		        	Log.e("xSubastarMios onPostExecute=","xsubastarMios ok");
		        	((MenuActivity)cc).instanciar(response_array_json_data,response[1]);
					
		         }else if (response[0].equals("okvacio")){
			        	Log.e("xSubastarMios onPostExecute=","xsubastarMios ok");
			        	((MenuActivity)cc).instanciar(null,response[1]);
						
			     }else{
		         	err_function("Error al listar productos por subastar");
		         }
	        
			}else if(caso=="xSubastarGanar"){
				String[] response = result.split("-");
		        if (response[0].equals("ok")){
		        	Log.e("xSubastarGanar onPostExecute=","xsubastarMios ok");
		        	((MenuActivity)cc).instanciar(response_array_json_data,response[1]);
					
		         }else if (response[0].equals("okvacio")){
			        	Log.e("xSubastarGanar onPostExecute=","xsubastarMios ok");
			        	((MenuActivity)cc).instanciar(null,response[1]);
						
			     }else{
		         	err_function("Error al listar productos por subastar");
		         }
	        
			}else if(caso=="lstCategorias"){
				
		        if (result.equals("ok")){
		        	Log.e("onPostExecute lista categorias=","lstCategorias ok");
		        	try {
						((RegistrarSubasta)cc).listaCategorias(response_array_json_data);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
		         }else{
		         	err_function("Error al listar categorias [FUNCTION ASYNC][lstCategorias]");
		         }
	        
			}else if(caso=="Subastando"){
				
			        if (result.equals("ok")){
			        	Log.e("onPostExecute=","Subastando ok");
			        	try {
							((Subappstore)cc).listar(response_array_json_data);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
			         }else{
			         	err_function("No se encontraron usuarios conectados ...");
			         }
		        
			}else if(caso=="RetirarSubasta"){
				
			        if (result.equals("ok")){
			        	Log.e("onPostExecute=","RetirarSubasta ok");
			        	((Subappstore)cc).salir(Integer.parseInt( respuesta ) );
						
			         }else{
			         	err_function("Error de retiro de usuario");
			         }
			        
			}else if(caso=="ConectarSubasta"){
					if (result.equals("ok")){
						Log.e("onPostExecute=","ConectarSubasta ok");
							        	((Subappstore)cc).cargarSubasta();
										
			         }else{
			         	err_function("Error al registrar usuario en subasta");
			         }
			}else if(caso=="OfrecerMonto"){
					if (result.equals("ok")){
						Log.e("onPostExecute=","OfrecerMonto ok");
			        	((Subappstore)cc).continuarSubasta();
						//err_function("Su nueva oferta fue asignada correctamente ..."); 
			         }else{
			         	err_function("Ya hay una mejor oferta que la de usted ... ");
			         	((Subappstore)cc).continuarSubasta();
			         }
			}else if(caso=="EvaluarGanador"){
					if (result.equals("ok")){
						Log.e("onPostExecute=","EvaluarGanador ok");
			        		((Subappstore)cc).finSubasta(true);
			         }else{
			        	   ((Subappstore)cc).finSubasta(false);
			         }
			}else if(caso=="logout"){
				if (result.equals("ok")){
					Log.e("onPostExecute=","Sesion off ok");
		        		((MenuActivity)cc).close();
		         }else{
		        	   Toast.makeText(cc, "Error al cerrar sesión ...",Toast.LENGTH_SHORT).show();
		         }
		}
			
			
			
	        
	         
	    }
		
		public void err_function(String _msg){
			
		    	Vibrator vibrator =(Vibrator) cc.getSystemService(Context.VIBRATOR_SERVICE);
		    	vibrator.vibrate(200);
		    	
		    	bt.setMensaje(_msg);
			    bt.show();
		    	// Toast toast1 = Toast.makeText(cc,msg, Toast.LENGTH_SHORT);
		 	   // toast1.show();
		 	    
		}
		
		
		
		/*
		private boolean checkPlayServices() {
			
		    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(cc);
		    
		    if (resultCode != ConnectionResult.SUCCESS)
		    {
		        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
		        {
		            GooglePlayServicesUtil.getErrorDialog(resultCode, a,
		                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
		        }
		        else
		        {
		            Log.i(TAG, "Dispositivo no soportado.");
		            a.finish();
		        }
		        return false;
		    }
		    return true;
		}
		 */
	}

