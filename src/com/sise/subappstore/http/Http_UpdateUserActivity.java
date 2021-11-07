package com.sise.subappstore.http;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.sise.subappstore.ModificarPerfil;
import com.sise.subappstore.R;
import com.sise.subappstore.RegistroActivity;
import com.sise.subappstore.bean.BeanMapper;
import com.sise.subappstore.bean.BeanToast;
import com.sise.subappstore.bean.ProductoBean;
import com.sise.subappstore.bean.UsuarioBean;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

public class Http_UpdateUserActivity extends AsyncTask<String, Void, String> {
	
	public Context ioContext;
	private ProgressDialog ioProgressDialog;
	private String isMensaje;
	private UsuarioBean ioBeanUsuario;
	public UsuarioBean loBeanUsuarioRes;
	private BeanToast bt ;

	public Http_UpdateUserActivity(Context psClase, String psMensaje,UsuarioBean poBeanUsuario) {
		ioContext = psClase;
		isMensaje = psMensaje;
		ioBeanUsuario = poBeanUsuario;
		bt= new BeanToast(psClase,"",
				((Activity)psClase).getLayoutInflater(),((Activity)psClase).findViewById(R.id.lyToast),R.id.tvMsgToast);
	}
	
	@Override
	protected void onPreExecute() {
		ioProgressDialog = ProgressDialog.show(ioContext, "", isMensaje, true);
		ioProgressDialog.setMessage(Html.fromHtml("<font color='red'>"
				+ isMensaje + "</font>"));

	}
	
	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		return fnUpdate(arg0[0]);
	}
	
	private String fnUpdate(String b) {
		
		try {
			
			String usuarioJson = BeanMapper.toJson(ioBeanUsuario, false);
			Log.v("usuarioJson", usuarioJson);

			DefaultHttpClient httpclient = new DefaultHttpClient();
			String Url = ioContext.getString(R.string.urlPostUpdateUsuario);

				
			HttpPost httpost = new HttpPost(Url);

			MultipartEntity reqEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);

			// ###### Asignando respectivo parámetros
			Log.v("usuario result : ", "Asignando respectivo parámetros");
			reqEntity.addPart(
					"idusuario",
					new StringBody(ioBeanUsuario.getId()+"", Charset
							.forName("UTF-8")));
			
			reqEntity.addPart(
					"nombre",
					new StringBody(ioBeanUsuario.getNombres(), Charset
							.forName("UTF-8")));
			reqEntity.addPart(
					"apellido",
					new StringBody(ioBeanUsuario.getApellidos(), Charset
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
					"password",
					new StringBody(ioBeanUsuario.getPassword(), Charset
							.forName("UTF-8")));
			
			reqEntity.addPart(
					"bandera",
					new StringBody(b, Charset
							.forName("UTF-8")));

			
			httpost.setEntity(reqEntity);
			HttpResponse response = null;
			response = httpclient.execute(httpost);
			Log.v("usuario result : ", "Parámetros enviados");
			HttpEntity resEntity = response.getEntity();
			String result = EntityUtils.toString(resEntity);
			Log.v("usuario result : ", result);
			loBeanUsuarioRes = (UsuarioBean) BeanMapper.fromJson(result,UsuarioBean.class);
			
			
			} catch (Exception e) {
			// TODO Auto-generated catch block

			Log.v("com.sise.subappstore.Http_UpdateUserActivity.fnUpdate() Update Usuario ESTADO","Error al preparar datos para registro de usuario , " + e.getMessage());
			((ModificarPerfil) ioContext).subError();
			return null;
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		if (ioProgressDialog != null) {
			ioProgressDialog.dismiss();

			switch (loBeanUsuarioRes.getId()) {
			case 1:
				((ModificarPerfil) ioContext).subResultado();
				break;
			case 0:
				bt.setMensaje("El servicio de actualización de datos no esta disponible.");
				//Toast.makeText(ioContext, "El servicio de registro no esta disponible en estos instante. Inténtelo luego",Toast.LENGTH_SHORT).show();
				bt.show();
				Log.e("com.sise.subappstore.Http_RegistrarActivity.fnRegistrar() Registro de nuevo usuario ESTADO","Error en interacción con BD");
				break;
			default:
				break;
			}

		}
	}

}
