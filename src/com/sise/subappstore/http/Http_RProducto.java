package com.sise.subappstore.http;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.sise.subappstore.MenuActivity;
import com.sise.subappstore.R;
import com.sise.subappstore.RegistroActivity;
import com.sise.subappstore.bean.BeanMapper;
import com.sise.subappstore.bean.ProductoBean;
import com.sise.subappstore.bean.UsuarioBean;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;


import com.sise.subappstore.RegistrarSubasta;


public class Http_RProducto extends AsyncTask<List<Bitmap>, Void, String>{

	private ProductoBean p,presult;
	public Context ioContext;
	private String isMensaje;
	private ProgressDialog ioProgressDialog;
	private Integer estado=0;
	
	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public Http_RProducto(Context psClase,String psMensaje, ProductoBean pBean)
	{
		ioContext=psClase;
		isMensaje=psMensaje;
		p=pBean;
	}
	
	@Override
	protected void onPostExecute(String result) {
		if (ioProgressDialog != null) {
			ioProgressDialog.dismiss();
			
			if(presult.getTitulo().equals("OK")){
					Toast.makeText(ioContext, "Su producto fue registrado correctamente ...",Toast.LENGTH_LONG).show();
					ShowMensaje("Producto registrado correctamente ... ", true, 200, ioContext, true);
					((RegistrarSubasta)ioContext).clear();				
			}else if(presult.getTitulo().equals("ERR")){

					ShowMensaje("Es servicio de registro esta indisponible actualmente ...", true, 200, ioContext, true);
					ShowLog("ESTADO REGISTRO PRODUCTO [Http_RProducto]", "No se pudo realizar inserción de datos", 3);
					
			}else if(presult.getTitulo().equals("ERR_EXISTS")){
				
				ShowMensaje("Es servicio de registro esta indisponible actualmente ...", true, 200, ioContext, true);
				ShowLog("ESTADO REGISTRO PRODUCTO [Http_RProducto]", "Ya existe un directorio u archivo con el mismo nombre", 3);
				
			}else if(presult.getTitulo().equals("ERR_MOVER")){
				
				ShowMensaje("Es servicio de registro esta indisponible actualmente ...", true, 200, ioContext, true);
				ShowLog("ESTADO REGISTRO PRODUCTO [Http_RProducto]", "Fallo al mover imágenes", 3);
				
			}else if(presult.getTitulo().equals("ERR_CREAR")){
				
				ShowMensaje("Es servicio de registro esta indisponible actualmente ...", true, 200, ioContext, true);
				ShowLog("ESTADO REGISTRO PRODUCTO [Http_RProducto]", "Fallo al crear directorios u archivos", 3);
				
			}else{
				ShowMensaje("Es servicio de registro esta indisponible actualmente ...", true, 200, ioContext, true);
				ShowLog("ESTADO REGISTRO PRODUCTO [Http_RProducto]", "Fallo no detectado", 3);
			}
			
			
						
		}
	}
	
	private void ShowMensaje(String msg,boolean vibrate,Integer durate,Context cc,boolean longshort){
		
		if(vibrate){
	    	Vibrator vibrator =(Vibrator) cc.getSystemService(Context.VIBRATOR_SERVICE);
	    	vibrator.vibrate(durate);
		}

		Toast toast1;
		if(longshort){
			toast1 = Toast.makeText(cc,msg, Toast.LENGTH_LONG);
		}else{
			toast1 = Toast.makeText(cc,msg, Toast.LENGTH_SHORT);
		}
	    
 	    toast1.show();
	}
	
	private void ShowLog(String motivo,String msg,Integer tipo){
		switch (tipo) {
		case 1:
			Log.v(motivo,msg);
			break;
		case 2:
			Log.i(motivo,msg);		
			break;
		case 3:
			Log.e(motivo,msg);	
			break;
		default:
			Log.w(motivo,msg);
			break;
		}
	}
	
	@Override
	protected void onPreExecute() {
		ioProgressDialog = ProgressDialog.show(ioContext, "",isMensaje, true);
		ioProgressDialog.setMessage(Html.fromHtml("<font color='red'>" + isMensaje + "</font>"));
	}
	
	
	@Override
	protected String doInBackground(List<Bitmap>... params) {
		return fnRegistrar(params[0]);
	}
	
	private String fnRegistrar(List<Bitmap> bitmap) {
		
		int cant;
		try {
			
			cant = bitmap.size();
			
			String productoJson  = BeanMapper.toJson(p,false);
			Log.v("com.sise.subappstore.http.fnRegistrar()  Estado: ","productoJson : "+productoJson);

			
			DefaultHttpClient httpclient = new DefaultHttpClient();
			
			this.estado +=1;
			String Url = ioContext.getString(R.string.urlPostRegistrarSubasta);
			
			HttpPost httpost = new HttpPost(Url);

			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

			String nameimagen="" ;
			
			this.estado +=1;
			for (int i = 0; i < cant ; i++) {
				
				if(i==0)
					nameimagen  = p.getId()+p.getTitulo().replace(" ", "")+ ".png";
				else
					nameimagen  = p.getId()+p.getTitulo().replace(" ", "")+i+ ".png";
				
				File f = new File(ioContext.getCacheDir(), nameimagen );
				f.createNewFile();
				
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.get(i).compress(Bitmap.CompressFormat.PNG, 100, stream); // convert Bitmap to ByteArrayOutputStream
				byte[] bitmapdata = stream.toByteArray();
				
				FileOutputStream fos = new FileOutputStream(f);
				fos.write(bitmapdata);
				
				reqEntity.addPart("myFile"+i,new FileBody(f, "image/png") ); 
				reqEntity.addPart("name"+i,new StringBody(nameimagen, Charset.forName("UTF-8")));	
				
			}
			
			this.estado +=1;
			reqEntity.addPart("titulo",
	                new StringBody(p.getTitulo(), Charset.forName("UTF-8")));
			reqEntity.addPart("descripcion",
	                new StringBody(p.getDescripcion(), Charset.forName("UTF-8")));
			reqEntity.addPart("precio",
	                new StringBody(p.getPrecio()+"", Charset.forName("UTF-8")));
			reqEntity.addPart("usuario",
	                new StringBody(p.getUsuario()+"", Charset.forName("UTF-8")));
			reqEntity.addPart("idusuario",
	                new StringBody(p.getId()+"", Charset.forName("UTF-8")));
			reqEntity.addPart("idcategoria",
	                new StringBody(p.getCategoria()+"", Charset.forName("UTF-8")));

			httpost.setEntity(reqEntity);
			HttpResponse response = null;
			
			this.estado +=1;
			response = httpclient.execute(httpost);
			
			HttpEntity resEntity = response.getEntity();
		    String result = EntityUtils.toString(resEntity);
		    Log.v("ProductoBean result : ",result);
		    this.estado +=1;
		    presult=(ProductoBean)BeanMapper.fromJson(result, ProductoBean.class);	

		} catch (Exception e) {
			Log.e("com.sise.subappstore.http.Http_RProducto fnRegistrar() : ",e.getMessage());
		}
		
		return null;
	}

}
