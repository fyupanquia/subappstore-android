package com.sise.subappstore.http;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.sise.subappstore.LoginActivity;
import com.sise.subappstore.ModificarPerfil;
import com.sise.subappstore.R;
import com.sise.subappstore.bean.BeanMapper;
import com.sise.subappstore.bean.BeanToast;
import com.sise.subappstore.bean.UsuarioBean;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.util.Log;



public class Http_LoginActivity extends AsyncTask<Void, Void, String>{

	public Context context;
	private ProgressDialog dialog;
	private String mensaje; 
	public UsuarioBean usuario; 
	private   BeanToast bt ;
	
	private String regid ="";
	private GoogleCloudMessaging gcm;
	String SENDER_ID = "732774513635";
	static final String TAG = "GCMDemo";
	private String msg="",b="";
	
	
	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}

	public Http_LoginActivity(Context ctx, String pmensaje, UsuarioBean bean){
		context = ctx;
		mensaje = pmensaje;
		usuario = bean;
		bt= new BeanToast(ctx,"",((Activity)ctx).getLayoutInflater(),((Activity)ctx).findViewById(R.id.lyToast),R.id.tvMsgToast);
	}

	@Override
	protected void onPreExecute() {
		// ###### Iniciando diálogo de acceso
		dialog = ProgressDialog.show(context, "SubAppStore", mensaje,true);
	}
	@Override
	protected void onPostExecute(String result){
		if(dialog != null){
			dialog.dismiss();
			switch (usuario.getId()) {
			case 0:
				err_function("Usuario no registrado .");
				break;
			case -1:
				err_function("Error de conexión .");
				break;
			default:
				if(b=="")
					((LoginActivity) context).subResultado(); // ###### Ingreso al siguiente layout
				else
					((ModificarPerfil) context).subResultado2(); // ###### Ingreso al siguiente layout
				break;
			}

		}
	}
	
	@Override
	protected String doInBackground(Void... params) {

		String keypush = getkeypush();
		if(keypush!="err"){
			usuario.setCodigo(keypush);
		
		// TODO Auto-generated method stub
				try {
					String usuarioJson = BeanMapper.toJson(usuario, false);
					Log.v("usuario",usuarioJson);
					//Envio el Json con el usuario
					String result = HttpFuncion.returnServiceResponse(context.getString(R.string.urlGetUsuario), usuarioJson);
					Log.v("result", result);
					Log.v("result", result);
					Log.v("result", result);
					usuario = (UsuarioBean) BeanMapper.fromJson(result, UsuarioBean.class);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		}else{
			err_function(this.msg);
		}	
		return null;
	}
		
		
		protected String getkeypush() {

			String msgmetod = "";

			if (regid.equals("")) {
				try {
					if (gcm == null) {

						gcm = GoogleCloudMessaging.getInstance(context);
					}

					regid = gcm.register(SENDER_ID);

					msgmetod = regid;

				} catch (Exception ex) {
					Log.d(TAG, "Error registro en GCM:" + ex.getMessage());
					this.msg="El servicio de Google Play no esta disponible en estos instantes.Intente una ves más.";
					msgmetod="err";
				}
			}

			return msgmetod;
		}
	
			public void err_function(String _msg){
				
		    	Vibrator vibrator =(Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		    	vibrator.vibrate(200);
		    	
		    	bt.setMensaje(_msg);
			    bt.show();
		    	// Toast toast1 = Toast.makeText(cc,msg, Toast.LENGTH_SHORT);
		 	   // toast1.show();
		 	    
		}

}
