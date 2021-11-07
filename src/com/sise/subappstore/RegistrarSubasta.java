package com.sise.subappstore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.widget.AdapterView.OnItemSelectedListener;

import com.sise.subappstore.async.FunctionAsync;
import com.sise.subappstore.bean.BeanCategoria;
import com.sise.subappstore.bean.BeanMapper;
import com.sise.subappstore.bean.BeanToast;
import com.sise.subappstore.bean.ProductoBean;
import com.sise.subappstore.bean.UsuarioBean;
import com.sise.subappstore.http.Http_RProducto;
import com.sise.subappstore.modelo.CategoriaNetwork;
import com.sise.subappstore.modelo.CategoriaNetworkSpinnerAdapter;
import com.sise.subappstore.modelo.ItemLista;
import com.sise.subappstore.util.ImagePrepare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v7.internal.widget.AdapterViewCompat.OnItemClickListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrarSubasta extends Activity  implements OnClickListener {

	
	private EditText etTitulo , etDescripcion , etPrecio;
	private ImageView ivImagen;
	private ImageButton ibGaleria,ibCamara;
	private Button btnSubastar;
	
	// ####### Variables Imagen ######
	private List<Bitmap> listaimagenasubir;
	private Bitmap imagenasubir=null;
    private String rutafoto;
	private static final int METODO_CAMARA = 1000;
	private static final int METODO_GALERIA = 2000;
	private ImagePrepare imgp;
	private ProductoBean p;
	private Http_RProducto registro;
	private SharedPreferences pref;	
	private UsuarioBean bean;
	
	private Spinner spinner;
	private Integer pos;
	private int limitcat;
	
	BeanToast bt ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_registrar_subasta);
		
		bt= new BeanToast(RegistrarSubasta.this,"",
				getLayoutInflater(),findViewById(R.id.lyToast),R.id.tvMsgToast);
		
		//### OBTENIENDO DATOS GLOBALES DEL USUARIO EN SESION###
		pref=getSharedPreferences("prefsubappstore", Context.MODE_PRIVATE);  
		String usuario=pref.getString("usuario","Desconocido");
	    bean=(UsuarioBean) BeanMapper.fromJson(usuario,UsuarioBean.class);
	    
		listaimagenasubir = new ArrayList<Bitmap>();
		setTitle("Registro de nueva Subasta");
		imgp = new ImagePrepare();
		asignarcampos();
		
		
		FunctionAsync fa =  new FunctionAsync(RegistrarSubasta.this, new UsuarioBean() ,"Espere ...");
		fa.execute(this.getString(R.string.urllistarCategorias),"lstCategorias");
		

	    
	    
	}
	
	

	public void listaCategorias(JSONObject[] array)throws JSONException{
		
		/* START CODIGO SPINNER */
		int cantidad = array.length;
		limitcat= cantidad;
		List<CategoriaNetwork> items = new ArrayList<CategoriaNetwork>(cantidad);
		
		
		for (int i = 0; i <cantidad; i++) {

				items.add(new CategoriaNetwork(array[i].getInt("id"),array[i].getString("categoria").toString(), R.drawable.ic_launcher));
		}
		
		if(items!=null){
			spinner.setAdapter(new CategoriaNetworkSpinnerAdapter(this,items));
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() 
			{
				@Override
				public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
				{
					pos = position;
				}

				@Override
				public void onNothingSelected(AdapterView<?> adapterView)
				{
					pos=7;
					//nothing				
				}
			});
		}
	
		
		/* END CODIGO SPINNER*/
	}
	
	
	private void asignarcampos(){
		etTitulo = (EditText) findViewById(R.id.etTitulo);
		etDescripcion = (EditText) findViewById(R.id.etDescripcion);
		etPrecio = (EditText) findViewById(R.id.etPrecio);
		
		spinner = (Spinner) findViewById(R.id.spCategoriasRegisterSubasta);
		
		ivImagen = (ImageView) findViewById(R.id.ivImagenP);
		ibCamara = (ImageButton) findViewById(R.id.ibCamaraRegistrarSubasta);
		ibGaleria = (ImageButton) findViewById(R.id.ibGaleriaRegistrarSubasta);
		ibCamara.setOnClickListener(this);
		ibGaleria.setOnClickListener(this);
	
		btnSubastar = (Button) findViewById(R.id.btnRegistrarSubasta);
		btnSubastar.setOnClickListener(this);
		spinner = (Spinner) findViewById(R.id.spCategoriasRegisterSubasta);
		
	}
	
	public void clear(){
		etTitulo.setText("");
		etDescripcion.setText("");
		etPrecio.setText("");
		imagenasubir=null;
		listaimagenasubir = new ArrayList<Bitmap>();
		imgp = new ImagePrepare();
		ivImagen.setImageBitmap(null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registar_subasta, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		
		case R.id.ibGaleriaRegistrarSubasta :
			Intent elegirgaleria = new Intent(Intent.ACTION_PICK);
			elegirgaleria.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					MediaStore.Images.Media.CONTENT_TYPE);
			startActivityForResult(elegirgaleria, METODO_GALERIA);
			break;
		case R.id.ibCamaraRegistrarSubasta:
			 
			Intent intentcamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			
			File f = imgp.crearImagen();
			rutafoto = imgp.getRutafoto();
			intentcamara.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			startActivityForResult(intentcamara, METODO_CAMARA);
			
			break;
		case R.id.btnRegistrarSubasta:
			
			if(imagenasubir!=null || listaimagenasubir!=null){
				p = new ProductoBean();
				p.setTitulo(etTitulo.getText().toString());
				p.setPrecio(  Integer.parseInt(etPrecio.getText().toString()) );
				p.setUsuario(bean.getUsuario());
				p.setId(bean.getId());
				p.setDescripcion(etDescripcion.getText().toString());
				p.setCategoria(pos+1);
				p.setLimitcat(limitcat);
				
				if(p.validar()){
					registro = new Http_RProducto(this, "Espere un momento .Su producto se esta registrando ...", p);
					try {
						registro.execute(listaimagenasubir);
					} catch (Exception e) {
						switch (registro.getEstado()) {
						case 1:
							ShowLog("RegistarSubasta Http_RProducto.execute Estado","Error con la url de registro", 3);
						break;
						case 2:
							ShowLog("RegistarSubasta Http_RProducto.execute Estado", "Error al generar nombres de imágenes", 3);
						break;
						case 3:
							ShowMensaje("Parámetros no asignados correctamente ... ", true, 200, this, true);
							break;
						case 4:
							ShowMensaje("Probar conexión a INTERNET ... ", true, 400, this, true);
							break;
						case 5:
							ShowMensaje("Servicio de registro no indisponible por ahora ... ", false, 0, this, true);
							ShowLog("RegistarSubasta Http_RProducto.execute Estado", "Fallo al convertir Respuesta a clase", 3);
							break;

						default:
							ShowMensaje("El servicio de registros esta indisponible por ahora...",true, 300, this, false);
							break;
						}
					}
										
				}
			}
			break;
		}
	}
	
	
	
	/*INICIO onActivityResult*/
	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent data){
		if(resultCode == RESULT_OK){
					if(requestCode == METODO_CAMARA){
								if(imagenasubir==null){
									  Bitmap imagenCompleta = BitmapFactory.decodeFile(rutafoto);
										//Resize redimencionar la imagen
										Bitmap imagenResize = Bitmap.createScaledBitmap
												(imagenCompleta, 400, 400, false);
										imagenasubir = imagenResize;
										imagenCompleta.recycle();
										ivImagen.setImageBitmap(imagenasubir);
										imgp.setName("");
										listaimagenasubir.add(imagenasubir);
										ShowMensaje("Imagen añadida correctamente al carrito ...", false, 0, this, true);
								}else if(listaimagenasubir==null){
										Bitmap imagenCompleta = BitmapFactory.decodeFile(rutafoto);
											//Resize redimencionar la imagen
										Bitmap imagenResize = Bitmap.createScaledBitmap
													(imagenCompleta, 400, 400, false);
										imagenasubir = imagenResize;
										imagenCompleta.recycle();
										listaimagenasubir.add(imagenasubir);
										imgp.setName("");
										ShowMensaje("Imagen añadida correctamente al carrito ...", false, 0, this, true);
								}else{
									if(listaimagenasubir.size()<5){
										Bitmap imagenCompleta = BitmapFactory.decodeFile(rutafoto);
										//Resize redimencionar la imagen
										Bitmap imagenResize = Bitmap.createScaledBitmap
													(imagenCompleta, 400, 400, false);
										imagenasubir = imagenResize;
											
										imagenCompleta.recycle();
										listaimagenasubir.add(imagenasubir);
										imgp.setName("");
										ShowMensaje("Imagen añadida correctamente al carrito ...", false, 0, this, true);
									}else{
										ShowMensaje("Subappstore permite un máximo de 5 imágenes", true, 2000, this, true);
										
									}
								}
							}else if (requestCode == METODO_GALERIA) {
								if(imagenasubir==null){
									Uri imagenselecciona = data.getData();
									String ruta = imagenselecciona.getPath().toString();
									try {
										Bitmap imagenselec = MediaStore.Images.Media.getBitmap
												(getContentResolver(), imagenselecciona);
										Bitmap imagenResize = Bitmap.createScaledBitmap
												(imagenselec, 400, 400, false);
										imagenasubir = imagenResize;
										imagenselec.recycle();
										ivImagen.setImageBitmap(imagenasubir);
										
									} catch (FileNotFoundException e) {
										
										ShowMensaje("Hubo error: "+e.getMessage(), true, 2000, this, false);
									} catch (IOException e) {
										ShowMensaje("Hubo error: "+e.getMessage(), true, 2000, this, false);
										
									}
									listaimagenasubir.add(imagenasubir);
									ShowMensaje("Imagen añadida correctamente al carrito ...", false, 0, this, true);
								}else if(listaimagenasubir==null){
									//listaimagenasubir = new ArrayList<Bitmap>();
									listaimagenasubir.add(imagenasubir);
									
									Uri imagenselecciona = data.getData();
									String ruta = imagenselecciona.getPath().toString();
									try {
										Bitmap imagenselec = MediaStore.Images.Media.getBitmap
												(getContentResolver(), imagenselecciona);
										Bitmap imagenResize = Bitmap.createScaledBitmap
												(imagenselec, 400, 400, false);
										imagenasubir = imagenResize;
										imagenselec.recycle();
										listaimagenasubir.add(imagenasubir);
										ShowMensaje("Imagen añadida correctamente al carrito ...", false, 0, this, true);
									} catch (FileNotFoundException e) {
										
										ShowMensaje("Hubo error: "+e.getMessage(), true, 2000, this, true);
									} catch (IOException e) {
										
										ShowMensaje("Hubo error: "+e.getMessage(), true, 2000, this, true);
									}
								}else{
									if(listaimagenasubir.size()<5){
										Uri imagenselecciona = data.getData();
										String ruta = imagenselecciona.getPath().toString();
										try {
											Bitmap imagenselec = MediaStore.Images.Media.getBitmap
													(getContentResolver(), imagenselecciona);
											Bitmap imagenResize = Bitmap.createScaledBitmap
													(imagenselec, 400, 400, false);
											imagenasubir = imagenResize;
											imagenselec.recycle();
											listaimagenasubir.add(imagenasubir);
											ShowMensaje("Imagen añadida correctamente al carrito ...", false, 0, this, true);
										} catch (FileNotFoundException e) {
												
											ShowMensaje("Hubo error: "+e.getMessage(), true, 2000, this, false);
										} catch (IOException e) {
													
											ShowMensaje("Hubo error: "+e.getMessage(), true, 2000, this, false);
										}
									}else{
										
										ShowMensaje("Subappstore permite un máximo de 5 imágenes", true, 2000, this, true);
									}
								}
							}
				}
	}
	/*FIN onActivityResult*/
	
	
	@Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
    	if(keyCode==KeyEvent.KEYCODE_BACK){
    		new AlertDialog.Builder(this)
    		.setIcon(R.drawable.ic_launcher)
    		.setTitle("Registro de Subastas")
    		.setMessage("¿Desea continuar registrando nuevas subasta?")
    		.setPositiveButton("NO",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int whith) {
					Bundle bundle = new Bundle();
					Intent intent = new Intent(RegistrarSubasta.this,MenuActivity.class);
					bundle.putInt("posicion", 3);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			})
			.setNegativeButton("SI", null)
			.show();
    		return true;
			
    	}else{
    		return super.onKeyDown(keyCode, event);
    	}
    }
	
	
	public void ShowMensaje(String msg,boolean vibrate,Integer durate,Context cc,boolean longshort){
		
		if(vibrate){
	    	Vibrator vibrator =(Vibrator) cc.getSystemService(Context.VIBRATOR_SERVICE);
	    	vibrator.vibrate(durate);
		}

			bt.setMensaje(msg);
			bt.setDuracion(longshort);
			bt.show();
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
	
	

	
	
	
	
}
