package com.sise.subappstore.http;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.sise.subappstore.bean.BeanMapper;
import com.sise.subappstore.bean.ProductoBean;
import com.sise.subappstore.MainActivity;
import com.sise.subappstore.RegistroActivity;
import com.sise.subappstore.bean.UsuarioBean;
import com.sise.subappstore.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

public class Http_RegistrarActivity extends AsyncTask<Bitmap, Void, String> {

	public Context ioContext;
	private ProgressDialog ioProgressDialog;
	private String isMensaje;
	private UsuarioBean ioBeanUsuario;
	public UsuarioBean loBeanUsuarioRes;
	private ProductoBean p;

	// ####### INICIO VARIABLES PUSH

	private String regid ="",msg="";
	private Context cc;
	private Activity a;
	private GoogleCloudMessaging gcm;
	private String usuario, url;
	
	public static final String EXTRA_MESSAGE = "message";
	private static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final String PROPERTY_EXPIRATION_TIME = "onServerExpirationTimeMs";
	private static final String PROPERTY_USER = "user";
	public static final long EXPIRATION_TIME_MS = 1000 * 3600 * 24 * 7;

	String SENDER_ID = "732774513635";
	static final String TAG = "GCMDemo";

	// ###### FIN VARIABLES PUSH

	public Http_RegistrarActivity(Context psClase, String psMensaje,
			UsuarioBean poBeanUsuario) {
		ioContext = psClase;
		isMensaje = psMensaje;
		ioBeanUsuario = poBeanUsuario;
	}

	@Override
	protected String doInBackground(Bitmap... bitmaps) {
		// Http_Suscripcion tarea = new Http_Suscripcion(ioContext);
		// tarea.execute();

		return fnRegistrar(bitmaps[0]);
	}

	protected String getkeypush(String usuario) {
		this.usuario = usuario;
		String msgmetod = "";

		//regid = getRegistrationId(cc);
		//Log.v("Hola", regid);
		if (regid.equals("")) {
			try {
				if (gcm == null) {
					//Log.v("Hola", "es null");
					gcm = GoogleCloudMessaging.getInstance(ioContext);
				}
				//Log.v("Hola", "paso");

				// Nos registramos en los servidores de GCM
				regid = gcm.register(SENDER_ID);

				//Log.d(TAG, "Registrado en GCM: registration_id=" + regid);
				msgmetod = regid;
				// Nos registramos en nuestro servidor

				/*
				 * boolean registrado = registroServidor(usuario, regid);
				 * 
				 * //Guardamos los datos del registro if(registrado) {
				 * setRegistrationId(cc, usuario, regid); //
				 * Toast.makeText(PushActivity
				 * .this,"Se subscribió correctamente.",
				 * Toast.LENGTH_SHORT).show(); }
				 */
			} catch (Exception ex) {
				Log.d(TAG, "Error registro en GCM:" + ex.getMessage());
				this.msg="El servicio de Google Play no esta disponible en estos instantes.Intente una ves más.";
				msgmetod="err";
			}
		}

		return msgmetod;
	}

	private String getRegistrationId(Context context) {
		SharedPreferences prefs = a.getSharedPreferences("mispreferencias",
				Context.MODE_PRIVATE);

		String registrationId = prefs.getString(PROPERTY_REG_ID, "");

		if (registrationId.length() == 0) {
			Log.d(TAG, "Registro GCM no encontrado.");
			return "";
		}

		String registeredUser = prefs.getString(PROPERTY_USER, "user");

		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);

		long expirationTime = prefs.getLong(PROPERTY_EXPIRATION_TIME, -1);

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm",
				Locale.getDefault());
		String expirationDate = sdf.format(new Date(expirationTime));

		Log.d(TAG, "Registro GCM encontrado (usuario=" + registeredUser
				+ ", version=" + registeredVersion + ", expira="
				+ expirationDate + ")");

		if (System.currentTimeMillis() > expirationTime) {
			Log.d(TAG, "Registro GCM expirado.");
			return "";
		} else if (!usuario.equals(registeredUser)) {
			Log.d(TAG, "Nuevo nombre de usuario.");
			return "";
		}

		return registrationId;
	}

	@Override
	protected void onPostExecute(String result) {
		if (ioProgressDialog != null) {
			ioProgressDialog.dismiss();

			switch (loBeanUsuarioRes.getId()) {
			case 1:
				((RegistroActivity) ioContext).subResultado();
				break;
			case 0:
				Toast.makeText(ioContext, "El servicio de registro no esta disponible en estos instante. Inténtelo luego",Toast.LENGTH_SHORT).show();
				Log.e("com.sise.subappstore.Http_RegistrarActivity.fnRegistrar() Registro de nuevo usuario ESTADO","Error en interacción con BD");
				break;
			case 2:
				Toast.makeText(ioContext, "Imagen dañada , seleccione otra imagen.",Toast.LENGTH_SHORT).show();
				Log.e("com.sise.subappstore.Http_RegistrarActivity.fnRegistrar() Registro de nuevo usuario ESTADO","No se logró mover la imagen");
				break;
			case 3:
				Toast.makeText(ioContext, "Error con creación de directorio para nuevo Usuario",Toast.LENGTH_SHORT).show();
				Log.e("com.sise.subappstore.Http_RegistrarActivity.fnRegistrar() Registro de nuevo usuario ESTADO","No se logró crear directorio en el servidor");
				break;
			case -1:
				Toast.makeText(ioContext, "Imagen dañada , por favor seleccione otra imagen",Toast.LENGTH_SHORT).show();
				Log.e("com.sise.subappstore.Http_RegistrarActivity.fnRegistrar() Registro de nuevo usuario ESTADO","No se ha recibido la imagen en el servidor");
				break;
			default:
				break;
			}

		}
	}

	@Override
	protected void onPreExecute() {
		ioProgressDialog = ProgressDialog.show(ioContext, "", isMensaje, true);
		ioProgressDialog.setMessage(Html.fromHtml("<font color='red'>"
				+ isMensaje + "</font>"));

	}

	private String fnRegistrar(Bitmap bitmap) {
		
		try {

			String keypush = getkeypush(ioBeanUsuario.getUsuario());
			if(keypush!="err"){
			ioBeanUsuario.setCodigo(keypush);
			
			String usuarioJson = BeanMapper.toJson(ioBeanUsuario, false);
			Log.v("usuarioJson", usuarioJson);

			// String nameimagen = System.currentTimeMillis()+ ".png";
			String nameimagen = ioBeanUsuario.getUsuario() + ".png";
			File f = new File(ioContext.getCacheDir(), nameimagen);
			f.createNewFile();

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); // convert
																		// Bitmap
																		// to
																		// ByteArrayOutputStream
			byte[] bitmapdata = stream.toByteArray();
			// write the bytes in file
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(bitmapdata);

			DefaultHttpClient httpclient = new DefaultHttpClient();
			String Url = ioContext.getString(R.string.urlPostRegistrarUsuario);
			HttpPost httpost = new HttpPost(Url);

			MultipartEntity reqEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			reqEntity.addPart("myFile", new FileBody(f, "image/png"));
			reqEntity.addPart("name",
					new StringBody(nameimagen, Charset.forName("UTF-8")));

			// ###### Asignando respectivo parámetros
			Log.v("usuario result : ", "Asignando respectivo parámetros");
			reqEntity.addPart(
					"nombre",
					new StringBody(ioBeanUsuario.getNombres(), Charset
							.forName("UTF-8")));
			reqEntity.addPart(
					"apellido",
					new StringBody(ioBeanUsuario.getApellidos(), Charset
							.forName("UTF-8")));
			reqEntity.addPart(
					"codigo",
					new StringBody(ioBeanUsuario.getCodigo(), Charset
							.forName("UTF-8")));
			reqEntity.addPart("email", new StringBody(ioBeanUsuario.getEmail(),
					Charset.forName("UTF-8")));
			reqEntity.addPart(
					"direccion",
					new StringBody(ioBeanUsuario.getDireccion(), Charset
							.forName("UTF-8")));
			reqEntity.addPart(
					"telefono",
					new StringBody(ioBeanUsuario.getTelefono(), Charset
							.forName("UTF-8")));

			reqEntity.addPart(
					"usuario",
					new StringBody(ioBeanUsuario.getUsuario(), Charset
							.forName("UTF-8")));

			reqEntity.addPart(
					"password",
					new StringBody(ioBeanUsuario.getPassword(), Charset
							.forName("UTF-8")));

			httpost.setEntity(reqEntity);
			
			HttpResponse response = null;
			response = httpclient.execute(httpost);
			Log.v("usuario result : ", "Parámetros enviados");
			HttpEntity resEntity = response.getEntity();
			String result = EntityUtils.toString(resEntity);
			Log.v("usuario result : ", result);
			loBeanUsuarioRes = (UsuarioBean) BeanMapper.fromJson(result,
					UsuarioBean.class);
			
			
			}

           //  Log.v("response registro",result);
		} catch (Exception e) {
			// TODO Auto-generated catch block

			Log.v("com.sise.subappstore.fnRegistrar() Registro Usuario ESTADO","Error a l preparar datos para registro de usuario , " + e.getMessage());
			((RegistroActivity) ioContext).subError();
			return null;
		}
		return null;
	}

}
