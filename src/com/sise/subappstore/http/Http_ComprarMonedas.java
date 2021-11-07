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

import com.sise.subappstore.ComprarMonedas;
import com.sise.subappstore.R;
import com.sise.subappstore.RegistrarSubasta;
import com.sise.subappstore.bean.BeanCompra;
import com.sise.subappstore.bean.BeanMapper;
import com.sise.subappstore.bean.ProductoBean;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

public class Http_ComprarMonedas extends AsyncTask<String, Void, String>{
		
	private BeanCompra p,presult;
	public Context ioContext;
	private String isMensaje;
	private ProgressDialog ioProgressDialog;
	private Integer estado=0;
	
	public Http_ComprarMonedas(Context psClase,String psMensaje, BeanCompra pBean)
	{
		ioContext=psClase;
		isMensaje=psMensaje;
		p=pBean;
	}
	
	
	@Override
	protected void onPostExecute(String result) {
		if (ioProgressDialog != null) {
			ioProgressDialog.dismiss();
			
			if(presult.getIdUsuario()==1){
				((ComprarMonedas)ioContext).clear();	
					
			}else if(presult.getIdUsuario()==2){
					Toast.makeText(ioContext, "No se logró a cabo la transacción ...",Toast.LENGTH_LONG).show();
			}else if(presult.getIdUsuario()==3){
				Toast.makeText(ioContext, "Saldo no suficiente ...",Toast.LENGTH_LONG).show();
			}else if(presult.getIdUsuario()==0){
				Toast.makeText(ioContext, "Tarjeta no encontrada ...",Toast.LENGTH_LONG).show();
			}
			
						
		}
	}

	
	
	@Override
	protected void onPreExecute() {
		ioProgressDialog = ProgressDialog.show(ioContext, "",isMensaje, true);
		ioProgressDialog.setMessage(Html.fromHtml("<font color='red'>" + isMensaje + "</font>"));
	}
	
	
	@Override
	protected String doInBackground(String... params) {
		return fnComprar(params[0]);
	}
	
	private String fnComprar(String url) {
			
			try {
				
				DefaultHttpClient httpclient = new DefaultHttpClient();
				
				Double total = calculartotal(p.getCantPlateadas(),p.getCantDoraradas());
				
				HttpPost httpost = new HttpPost(url);
	
				MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
				
				reqEntity.addPart("cantdoradas",
		                new StringBody(p.getCantDoraradas()+"", Charset.forName("UTF-8")));
				reqEntity.addPart("cantplateadas",
		                new StringBody(p.getCantPlateadas()+"", Charset.forName("UTF-8")));
				reqEntity.addPart("numtarjeta",
		                new StringBody(p.getNumtarjeta()+"", Charset.forName("UTF-8")));
				reqEntity.addPart("clavtarjeta",
		                new StringBody(p.getClavTarjeta()+"", Charset.forName("UTF-8")));
				reqEntity.addPart("idusuario",
		                new StringBody(p.getIdUsuario()+"", Charset.forName("UTF-8")));
				reqEntity.addPart("total",
		                new StringBody(total+"", Charset.forName("UTF-8")));
	
				httpost.setEntity(reqEntity);
				HttpResponse response = null;
				
				response = httpclient.execute(httpost);
				
				HttpEntity resEntity = response.getEntity();
			    String result = EntityUtils.toString(resEntity);
			    Log.v("ProductoBean result : ",result);
			    presult=(BeanCompra)BeanMapper.fromJson(result, BeanCompra.class);	
	
			} catch (Exception e) {
				Log.e("com.sise.subappstore.http.Http_ComprarMonedas fnComprar() : ",e.getMessage());
			}
			
			return null;
		}
	
	public Double calculartotal(Integer p,Integer d){
		Double subplateadas = p*0.25;
		Double subdoradas = d * 1.25;
		
		return subplateadas+subdoradas;
	}
}
